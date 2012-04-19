package co.uk.sentinelweb.views.draw.file;
/*
Vectoroid API for Android
Copyright (C) 2010-12 Sentinel Web Technologies Ltd
All rights reserved.
 
This software is made available under a Dual Licence:
 
Use is permitted under LGPL terms for Non-commercial projects
  
see: LICENCE.txt for more information

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met:
    * Redistributions of source code must retain the above copyright
      notice, this list of conditions and the following disclaimer.
    * Redistributions in binary form must reproduce the above copyright
      notice, this list of conditions and the following disclaimer in the
      documentation and/or other materials provided with the distribution.
    * Neither the name of the Sentinel Web Technologies Ltd. nor the
      names of its contributors may be used to endorse or promote products
      derived from this software without specific prior written permission.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
DISCLAIMED. IN NO EVENT SHALL SENTINEL WEB TECHNOLOGIES LTD BE LIABLE FOR ANY
DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
(INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
(INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.Log;
import co.uk.sentinelweb.views.draw.DVGlobals;
import co.uk.sentinelweb.views.draw.controller.FontController.Font;
import co.uk.sentinelweb.views.draw.file.FileRepository.Directory;
import co.uk.sentinelweb.views.draw.file.export.json.gson.GSONDrawing;
import co.uk.sentinelweb.views.draw.file.export.json.simple.JSONDrawing;
import co.uk.sentinelweb.views.draw.file.export.json.simple.JSONSet;
import co.uk.sentinelweb.views.draw.file.export.json.string.SJSONDrawing;
import co.uk.sentinelweb.views.draw.model.Asset;
import co.uk.sentinelweb.views.draw.model.Drawing;
import co.uk.sentinelweb.views.draw.model.DrawingElement;
import co.uk.sentinelweb.views.draw.model.DrawingSet;
import co.uk.sentinelweb.views.draw.model.Fill.Type;
import co.uk.sentinelweb.views.draw.model.Group;
import co.uk.sentinelweb.views.draw.model.Stroke;
import co.uk.sentinelweb.views.draw.model.UpdateFlags;
import co.uk.sentinelweb.views.draw.model.ViewPortData;
import co.uk.sentinelweb.views.draw.render.ag.AndGraphicsRenderer;
import co.uk.sentinelweb.views.draw.util.FileUtil;

public class SaveFile {
	private static final String SET_FILENAME = "set";
	private static final int MAX_OP_PXL = 2*1024*1024;
	public static final String PREVIEW_FILE_EXT = ".preview.png";
	public static final String PROPS_FILE_EXT = ".props";
	public static final String JSON_FILE_EXT = ".json";
	public static final String SVG_FILE_EXT = ".svg";
	public static final String ZIP_FILE_EXT = ".zip";
	public static final String BMP_FILE_EXT = ".png";
	public static final String TMP_FILE_EXT = ".tmp";
	public static final String AUTOSAVE_FILE_EXT = ".auto";
	
	public enum Option {EMBED_ASSET}
	public ArrayList<Option> _options = new ArrayList<SaveFile.Option>();
	public ArrayList<Object> _assetList;
	//String _setName;
	DrawingSet _set;
	File _dataDir;
	FileRepository _fr;
	
	AssetManager _assetManager;
	private float _previewThumbSize = 200;
	private Context c;
	
	public SaveFile(String _name, FileRepository _fr,Context c) throws Exception {
		super();
		this._fr = _fr;
		
		this.c=c;
		setDataDir( _name );
		_set = loadSet();
		if (_set==null) {
			_set=new DrawingSet();
			_set.setId(_name);
			_set.setName(_name);
			_set.setDateCreated(System.currentTimeMillis());
			_set.setDateModified(System.currentTimeMillis());
		}
		verfiyDrawings();
	}
	
	/**
	 * @return the _set
	 */
	public DrawingSet getSet() {
		return _set;
	}

	private void verfiyDrawings() {// TODO this need a re-write - consider changing drawing file ext to be unique
		File[] list = _dataDir.listFiles();
		ArrayList<File> filesToAdd = new ArrayList<File>();
		ArrayList<String> ids = new ArrayList<String>();
		ids.addAll(_set.getDrawingIDs());
		boolean changed = false;
		for (File f : list) {
			String name = f.getName().substring(0, f.getName().length()-JSON_FILE_EXT.length());
			if (ids.contains(name)) {
				while (ids.contains(name)) {// prevent duplicate ids
					ids.remove(name);
				}
			} else {
				if (f.getName().endsWith(JSON_FILE_EXT) && !f.getName().startsWith(SET_FILENAME)) {
					filesToAdd.add(f);
				}
			}
		}
		for (File f : filesToAdd) {
			String name = f.getName().substring(0, f.getName().length()-JSON_FILE_EXT.length());
			if (!_set.getDrawingIDs().contains(name)) {// TODO revisit this when there is more time to stop id duplicates getting asdded somewhere
				_set.getDrawingIDs().add(name);
				Log.d(DVGlobals.LOG_TAG, "added to set:"+name+" -> "+f.getName());
				changed=true;
			}
		}
		// delete non-existing ids
		for (String id : ids) {
			FileUtil.deleteIfExists(getPreviewFile(id));
			FileUtil.deleteIfExists(getBitmapFile(id));
			FileUtil.deleteIfExists(getBitmapOutputFile(id));
			FileUtil.deleteIfExists(getSVGFile(id));
			//FileUtil.deleteIfExists(getDrawingFile(id));
			
		}
		if (changed) {
			saveSet();
		}
		// maybe remove unused ids here?
	}
	
	/**
	 * @return the _assetManager
	 */
	public AssetManager getAssetManager() {
		return _assetManager;
	}
	
	/**
	 * @return the _dataDir
	 */
	public File getDataDir() {
		return _dataDir;
	}

	/**
	 * @return the _name
	 */
	public String getName() {
		if (_set==null) {return null;}
		return _set.getId();
	}

	/**
	 * @param _dataDir the _dataDir to set
	 */
	public void setDataDir(String id) throws Exception{
		if (_set!=null || id.equals(getName())) {return;}
		if (id==null || "".equals(id)) {throw new NullPointerException("name cannot be null or blank");}
		File newdir = new File(_fr.getDirectory(Directory.SAVES), id);
		if (newdir==null || (newdir.exists() && newdir.isFile())) {throw new FileNotFoundException("A file exists in place: "+id);}
		
		this._dataDir = newdir;
		if (_set!=null) {_set.setId(id);}
		if (!this._dataDir.exists()) {
			this._dataDir.mkdirs();
		}
		// copy assets from old manager
		AssetManager oldAssetManager = _assetManager;
		_assetManager = new AssetManager(this);
		if (oldAssetManager!=null) {
			_assetManager.copyFrom(oldAssetManager); 
		}
	}
	/*
	public boolean saveSVG(Drawing d,ArrayList<Option> options) {
		try {
			this._options=options;
			makeAssetList(d);
			File file = getSVGFile(d.getId());
			FileWriter out = new FileWriter(file);
			BufferedWriter outs = new BufferedWriter(out,4096);
			SVGDrawing svgd = new SVGDrawing(this);
			//StringBuffer outs = new StringBuffer();
			svgd.toSVG(d, outs);
			//out.write(outs.toString());
			outs.flush();
			out.flush();// shouldnt need this
			out.close();
			return true;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}
	*/
	
	
	private void makeAssetList(Drawing d) {
		if (_assetList==null) {
			_assetList=new ArrayList<Object>();
		} else {_assetList.clear();}
		if (d.background!=null && d.background.type==Type.BITMAP) {
			_assetList.add(d.background._bitmapFill);
		}
		for (DrawingElement de : d.elements) {
			makeAssetList(de);
		}
	}
	
	private void makeAssetList(DrawingElement de) {// recurses down
		if (de instanceof Group) {
			for (DrawingElement de1:((Group)de).elements) {
				makeAssetList(de1);
			}
		} else if (de instanceof Stroke) {
			Stroke s = (Stroke)de;
			if (s.type==Stroke.Type.TEXT_TTF) {
				Font f = _fr.getFontController()._ttfontFiles.get(s.fontName);
				if (!_assetList.contains(f)) {
					_assetList.add(f);
				}
			} else if (s.fill.type==Type.BITMAP) {
				if (!checkAssetInList(s.fill._bitmapFill)) {
					_assetList.add(s.fill._bitmapFill);
				}
			}
		}
	}
	
	private boolean checkAssetInList(Asset a) {
		for (Object o : _assetList) {
			if (o instanceof Asset) {
				if (((Asset)a).getName().equals(a.getName())) {return true;}
			}
		}
		return false;
	}
	
	public  JSONObject toJSON(Drawing d) {
			JSONDrawing jsd = new JSONDrawing(this);
			return jsd.toJSON(d);
	}
	
	public  Drawing fromJSON(JSONObject o) {
			JSONDrawing jsd = new JSONDrawing(this);
			return jsd.fromJSON(o);
	}
	/* ********************************** set save / load ******************************************/
	public boolean saveSet() {
		try {
			File file = getSetFile();
			FileWriter out = new FileWriter(file);
			JSONSet jss = new JSONSet(this);
			//JSONObject o = toJSON(d);
			out.write(jss.toJSON(_set).toString());
			out.flush();
			out.close();
			Log.d(DVGlobals.LOG_TAG, "saved set:"+file.getAbsolutePath());
			
			
			return true;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}/* catch (JSONException e) {
			e.printStackTrace();
		}*/
		return false;
	}
	
	
	public DrawingSet loadSet() {
		File file = getSetFile();
		try {
			StringWriter sw = new StringWriter();
			BufferedReader reader = new BufferedReader(	new FileReader(file));
			String readline = "";
			while ((readline = reader.readLine()) != null) { 
				sw.append(readline);
			}
			String string = sw.toString();
			JSONObject o = new JSONObject(new JSONTokener(string));
			JSONSet jss = new JSONSet(this);
			return jss.fromJSON(o);
		} catch (FileNotFoundException e) {
			//e.printStackTrace();
			Log.d(DVGlobals.LOG_TAG, "no file found:"+file.getAbsolutePath());
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}
	/* ********************************** drawing save/load ******************************************/
	private boolean useGSON = true;
	public boolean saveJSON(Drawing d,ArrayList<Option> options) {
		return saveJSON( d, options, null);
	}
	
	public boolean saveJSON(Drawing d,ArrayList<Option> options, File file) {
		try {
			long st = System.currentTimeMillis();
			this._options=options;
			makeAssetList(d);
			if (file==null) {
				file = getDrawingFile(d.getId());
			}
			Writer out = new BufferedWriter(new FileWriter(file));
			//Writer out = new FileWriter(file);
			if (!useGSON) {
				JSONObject o = toJSON(d);
				out.write(o.toString(2));//2
				out.flush();out.close();
			} else {
				try {
					//GSONDrawing gd = new GSONDrawing(this);
					//gd.toJSON(d, out);
					SJSONDrawing sjd = new SJSONDrawing(this);
					sjd.toJSON(d, out);
					out.close();
				} catch (Exception e) {
					Log.d(DVGlobals.LOG_TAG, "gson load ex:"+file.getAbsolutePath(),e);
					out.close();
					file.delete();
					out = new FileWriter(file);
					JSONObject o = toJSON(d);
					out.write(o.toString(2));//2
					out.flush();
					out.close();
				}
			}
			Log.d(DVGlobals.LOG_TAG, "saveJSON: time :"+(System.currentTimeMillis()-st));
			return true;
		} catch (FileNotFoundException e) {
			Log.d(DVGlobals.LOG_TAG, "saveJSON:FileNotFoundException:",e);
		} catch (IOException e) {
			Log.d(DVGlobals.LOG_TAG, "saveJSON:IOException:",e);
		} catch (JSONException e) {
			Log.d(DVGlobals.LOG_TAG, "saveJSON:JSONException:",e);
		}
		return false;
	}
	
	public Drawing loadJSON(String drawingId) {
		return loadJSON(drawingId,null);
	}
	
	public Drawing loadJSON(String drawingId, File file) {
		try {
			long st = System.currentTimeMillis();
			if (file==null) {
				file = new File(_dataDir, drawingId + JSON_FILE_EXT);
			}
			Drawing d = null;
			boolean noGSON = true;//!useGSON;
			if (noGSON) {
				d = parseJSONSimple(file);
			} else {
				try {
					InputStream is = new BufferedInputStream(new FileInputStream(file));
					GSONDrawing gd = new GSONDrawing(this);
					d=gd.fromJSON( is);
				} catch (Exception e) {
					Log.d(DVGlobals.LOG_TAG, "loadGSON: ex :",e);
					d = parseJSONSimple(file);
				}
			}
			Log.d(DVGlobals.LOG_TAG, "loadJSON: time :"+(System.currentTimeMillis()-st));
			d.setId(drawingId);
			return d;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}

	private Drawing parseJSONSimple(File file) throws FileNotFoundException, IOException, JSONException {
		Drawing d;
		StringWriter sw = new StringWriter();
		BufferedReader reader = new BufferedReader(	new FileReader(file));
		String readline = "";
		while ((readline = reader.readLine()) != null) { 
			sw.append(readline);
		}
		String string = sw.toString();
		JSONObject o = new JSONObject(new JSONTokener(string));
		d = fromJSON(o);
		return d;
	}
	
	
	
	public void saveBitmaps (Drawing drawing) {
		saveBitmaps(drawing,true,false);
	}
	
	public void saveBitmaps (Drawing drawing, boolean makeVisible, boolean previewOnly) {
		// make renderer here so render objects are reused...
		AndGraphicsRenderer agr = new AndGraphicsRenderer(c);
		if (!previewOnly) {
			boolean success = renderFull(drawing, agr, getBitmapFile(drawing.getId()));
			setPublished( drawing,  makeVisible && success);
		}
		renderPreview(drawing, agr,getPreviewFile(drawing.getId()));
	}

	public void renderPreview(Drawing drawing, AndGraphicsRenderer agr,File f)  {
		try {
			long st = System.currentTimeMillis();
			try {
				float scale = _previewThumbSize/Math.max(drawing.size.x, drawing.size.y);
				ViewPortData vpd = ViewPortData.getFullDrawing(drawing);
				vpd.zoom=scale;
				if (agr==null) {
					agr = new AndGraphicsRenderer(c);
				}
				agr.setVpd(vpd);
				Bitmap thumbBitmap= Bitmap.createBitmap((int)(drawing.size.x*scale), (int)(drawing.size.y*scale), Bitmap.Config.ARGB_8888);
				Canvas thumbCanvas = new Canvas();
				thumbCanvas.setBitmap(thumbBitmap);
				agr.setCanvas(thumbCanvas);
				drawing.update(true, agr , UpdateFlags.ALL_NOLISTENERS);// shouldnt have to do this
				agr.setupViewPort();
				agr.render(drawing);
				agr.revertViewPort();
				FileOutputStream out = new FileOutputStream(f);
				thumbBitmap.compress(Bitmap.CompressFormat.PNG, 90,out);
				out.close();
			} catch (OutOfMemoryError ome ) {
				Log.d(DVGlobals.LOG_TAG, "saveBitmaps: preview : OME",ome);
			}
			Log.d(DVGlobals.LOG_TAG, "saveBitmaps: preview :"+(System.currentTimeMillis()-st));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public boolean renderFull(Drawing drawing, AndGraphicsRenderer agr,File f)	 {
		
		try {
			ViewPortData vpd = ViewPortData.getFullDrawing(drawing);
		// scale down dimensions to something maneagable.
			float size = drawing.size.x*drawing.size.y;
			float dwidth = drawing.size.x;
			float dheight = drawing.size.y;
			if (size>MAX_OP_PXL) {
				float scale = MAX_OP_PXL/size;
				vpd.zoom=scale;
				dwidth*=scale;
				dheight*=scale;
			}
			agr.setVpd(vpd);
			
			long st = System.currentTimeMillis();
			try {
				//if (!previewOnly) {
					Bitmap b = Bitmap.createBitmap((int)dwidth, (int)dheight, Bitmap.Config.ARGB_8888);
					Canvas drawCanvas = new Canvas();
					drawCanvas.setBitmap(b);
					agr.setCanvas(drawCanvas);
					drawing.update(true, agr , UpdateFlags.ALL_NOLISTENERS);// shouldnt have to do this
					
					agr.setupViewPort();
					agr.render(drawing);
					agr.revertViewPort();
				
					FileOutputStream out = new FileOutputStream(f);
					b.compress(Bitmap.CompressFormat.PNG, 90,out);
					out.flush();
					out.close();
					//setPublished(drawing, makeVisible);
				//}
					Log.d(DVGlobals.LOG_TAG, "saveBitmaps: large :"+(System.currentTimeMillis()-st));
					return true;
			} catch (OutOfMemoryError ome ) {
				Log.d(DVGlobals.LOG_TAG, "saveBitmaps: full : OME", ome);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}

	public void setPublished(Drawing drawing, boolean makeVisible) {
		if (makeVisible) {
			getBitmapFile( drawing.getId() ).renameTo(getBitmapOutputFile( drawing.getId() ));
		} else {
			if (getBitmapOutputFile(drawing.getId()).exists()) {
				getBitmapOutputFile(drawing.getId()).delete();
			}
		}
	}
	
	public File getPreviewFile() {
		ArrayList<String> drawingIDs = _set.getDrawingIDs();
		if (drawingIDs==null ||drawingIDs.size()==0){return null;}
		return  getPreviewFile(drawingIDs.get(0));
	}
	public File getPreviewFile(String id) {
		 File f = new File( _dataDir,"."+id + PREVIEW_FILE_EXT);
		 Log.d(DVGlobals.LOG_TAG, "preview file:"+f.getAbsolutePath());
		 return f;
		 
	} 
	
	public Bitmap getPreviewBitmap() {
		return BitmapFactory.decodeFile(getPreviewFile().getAbsolutePath());
	}
	public Bitmap getPreviewBitmap(String id) {
		return BitmapFactory.decodeFile(getPreviewFile(id).getAbsolutePath());
	}
	
	public File getBitmapFile(String id) {
		File f = new File(_dataDir, id + BMP_FILE_EXT);
		return f;
	}
	public Bitmap getBitmapBitmap(String id) {
		return BitmapFactory.decodeFile(getBitmapFile(id).getAbsolutePath());
	}
	
	public File getBitmapOutputFile(String id) {
		File f = new File(_fr.getDirectory(Directory.OUTPUT), _set.getId() +"_"+ id + BMP_FILE_EXT);
		return f;
	}
	public File getSVGFile(String id) {
		return new File(_dataDir, id + SVG_FILE_EXT);
	}
	public File getSetFile() {
		return new File(_dataDir, SET_FILENAME + JSON_FILE_EXT);
	}
	public File getDrawingFile(String id) {
		return new File(_dataDir, id + JSON_FILE_EXT);
	}
	public File getDrawingAutoSaveFile(String id) {
		return new File(_dataDir, id + AUTOSAVE_FILE_EXT);
	}
	public String ensureUnique(String newID) {
		String testID = newID;
		int ctr=1;
		while (_set.getDrawingIDs().indexOf(testID)!=-1) {
			testID=newID+ctr++;
		}
		return testID;
	}

	public void addDrawingToSet(Drawing d) {
		if (d.getId()==null) {d.setId("New");}
		d.setId(ensureUnique(d.getId()));
		getSet().getDrawingIDs().add(d.getId());
		saveSet();
	}
	public void addDrawingToSetIfNessecary(Drawing d) {
		if (d.getId()==null) {
			//d.setId("New");
			//d.setId(ensureUnique(d.getId()));
			return;
		}
		if (!getSet().getDrawingIDs().contains(d.getId())) {
			getSet().getDrawingIDs().add(d.getId());
		}
		saveSet();
	}
	public void delete() {
		FileUtil.deleteWithChildren(_dataDir);
	}
}
