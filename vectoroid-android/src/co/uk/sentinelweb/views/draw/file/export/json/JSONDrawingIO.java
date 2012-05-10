package co.uk.sentinelweb.views.draw.file.export.json;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PushbackInputStream;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.util.Log;
import co.uk.sentinelweb.views.draw.VecGlobals;
import co.uk.sentinelweb.views.draw.file.SaveFile;
import co.uk.sentinelweb.views.draw.file.SaveFile.Option;
import co.uk.sentinelweb.views.draw.file.export.json.v3.JSONConst;
import co.uk.sentinelweb.views.draw.file.export.json.v3.gson.GSONDrawing;
import co.uk.sentinelweb.views.draw.file.export.json.v2.simple.JSONDrawing;
import co.uk.sentinelweb.views.draw.file.export.json.v3.string.SJSONDrawing;
import co.uk.sentinelweb.views.draw.model.Drawing;

public class JSONDrawingIO {
	/* ********************************** drawing save/load ******************************************/
	private SaveFile _saveFile;
	
	public JSONDrawingIO(SaveFile s) {
		super();
		this._saveFile = s;
	}

	//private boolean useGSON = true;
	public boolean saveJSON(Drawing d,ArrayList<Option> options) {
		return saveJSON( d, options, null);
	}
	
	public boolean saveJSON(Drawing d,ArrayList<Option> options, File file) {
		try {
			long st = System.currentTimeMillis();
			if (_saveFile!=null) {
				_saveFile._options=options;
				_saveFile.makeAssetList(d);
			}
			if (file==null) {
				file = _saveFile.getDrawingFile(d.getId());
			}
			Writer out = new BufferedWriter(new FileWriter(file));
			//Writer out = new FileWriter(file);
//			if (!useGSON) {
//				JSONObject o = toJSON(d);
//				out.write(o.toString(2));//2
//				out.flush();out.close();
//			} else {
				try {
					//GSONDrawing gd = new GSONDrawing(this);
					//gd.toJSON(d, out);
					co.uk.sentinelweb.views.draw.file.export.json.v3.string.SJSONDrawing sjdv3 = 
						new co.uk.sentinelweb.views.draw.file.export.json.v3.string.SJSONDrawing(_saveFile);
			//		SJSONDrawing sjd = new SJSONDrawing(_saveFile);
					sjdv3.toJSON(d, out);
					out.close();
				} catch (Exception e) {
					Log.d(VecGlobals.LOG_TAG, "gson save ex:"+file.getAbsolutePath(),e);
//					out.close();
//					file.delete();
//					out = new FileWriter(file);
//					JSONObject o = toJSON(d);
//					out.write(o.toString(2));//2
//					out.flush();
//					out.close();
				}
			//}
			Log.d(VecGlobals.LOG_TAG, "saveJSON: time :"+(System.currentTimeMillis()-st));
			return true;
		} catch (FileNotFoundException e) {
			Log.d(VecGlobals.LOG_TAG, "saveJSON:FileNotFoundException:",e);
		} catch (IOException e) {
			Log.d(VecGlobals.LOG_TAG, "saveJSON:IOException:",e);
		} 
//		catch (JSONException e) {
//			Log.d(VecGlobals.LOG_TAG, "saveJSON:JSONException:",e);
//		}
		return false;
	}
	
	public Drawing loadJSON(String drawingId) {
		return loadJSON(drawingId,null);
	}
	
	public Drawing loadJSON(String drawingId, File file) {
		long st = System.currentTimeMillis();
		if (file==null) {
			file = new File(_saveFile.getDataDir(), drawingId + SaveFile.JSON_FILE_EXT);
		}
		Drawing d = null;
		try {
			BufferedInputStream is = new BufferedInputStream(new FileInputStream(file));
			d = loadJSON( is);
			
			is.close();
		} catch (Exception e) {
			Log.d(VecGlobals.LOG_TAG, "loadGSON: ex :",e);
			//d = parseJSONV1Simple(file);
		}
		Log.d(VecGlobals.LOG_TAG, "loadJSON: time :"+(System.currentTimeMillis()-st));
		if (d!=null) {d.setId(drawingId);}
		return d;
	}

	public Drawing loadJSON(BufferedInputStream is) throws IOException, FileNotFoundException, JSONException {
		Drawing d = null;
		//if (is.markSupported()) {is.mark(0);}// likely unnessecary
		int len = 50;
		PushbackInputStream pbis = new PushbackInputStream(is,len);
		byte[] buf = new byte[len];
		Float ver = null;
		int read = -1;
		int pos = 0;
		while (pos<len) {
			read = pbis.read(buf,pos,buf.length-pos);
			pos+=read;
		}
		String s = new String(buf,"utf-8");
		int versionIdx = s.indexOf(JSONConst.JSON_VERSION);
		if (versionIdx>-1) {
			int colonIdx = s.indexOf(":",versionIdx);
			String verStr = s.substring(colonIdx+1,s.indexOf(",",colonIdx));
			try {
				ver=Float.parseFloat(verStr);
			} catch (Exception e) {
				Log.d(VecGlobals.LOG_TAG, "loadJSON: ex : colldnt parse version:"+verStr);
			}
		}
		pbis.unread(buf);// push back the read buffer
		Log.d(VecGlobals.LOG_TAG, "loadJSON: ver : "+ver);
		try {
			Log.d(VecGlobals.LOG_TAG, "loadJSON: 1 : "+ver);
			if (ver!=null && ver==JSONConst.version) {
				Log.d(VecGlobals.LOG_TAG, "loadJSON: 2 : "+ver);
				GSONDrawing gdv3 = new GSONDrawing(_saveFile);
				d=gdv3.fromJSON( pbis);
			} else if (ver!=null && ver==co.uk.sentinelweb.views.draw.file.export.json.v2.gson.GSONDrawing.version) {
				Log.d(VecGlobals.LOG_TAG, "loadJSON:3 : "+ver);
				co.uk.sentinelweb.views.draw.file.export.json.v2.gson.GSONDrawing gdv2 = 
					new co.uk.sentinelweb.views.draw.file.export.json.v2.gson.GSONDrawing(_saveFile);
				d=gdv2.fromJSON( pbis);
			} else {
				d = parseJSONV1Simple(pbis);
			}
			Log.d(VecGlobals.LOG_TAG, "loadJSON: 4 : "+ver);
		} catch (Exception e) {
			Log.d(VecGlobals.LOG_TAG, "loadGSON: ex :",e);
		}
		return d;
	}

	private Drawing parseJSONV1Simple(File file) throws FileNotFoundException, IOException, JSONException {
		Drawing d;
		StringWriter sw = new StringWriter();
		BufferedReader reader = new BufferedReader(	new FileReader(file));
		String readline = "";
		while ((readline = reader.readLine()) != null) { 
			sw.append(readline);
		}
		String string = sw.toString();
		return parseJSONV1Simple(string);
	}
	
	private Drawing parseJSONV1Simple(InputStream is) throws FileNotFoundException, IOException, JSONException {
		Drawing d;
		StringWriter sw = new StringWriter();
		BufferedInputStream reader = new BufferedInputStream(	is);
		String readline = "";
		byte[] buffer = new byte[1000];
		int offset=0;
		int block=-1;
		while ((block = reader.read(buffer, 0, buffer.length))>-1) { 
			sw.append(new String(buffer,0,block));
			offset+=block;
		}
		String string = sw.toString();
		return parseJSONV1Simple(string);
	}
	
	public Drawing parseJSONV1Simple(String string) throws JSONException {
		Drawing d;
		JSONObject o = new JSONObject(new JSONTokener(string));
		d = fromJSONV1(o);
		return d;
	}
	public  String toJSONString(Drawing d) {
		try {
			StringWriter out = new StringWriter();
			co.uk.sentinelweb.views.draw.file.export.json.v3.string.SJSONDrawing sjdv3 = 
				new co.uk.sentinelweb.views.draw.file.export.json.v3.string.SJSONDrawing(_saveFile);
			sjdv3.toJSON(d, out);
			out.close();
			return out.toString();
		} catch (Exception e) {
			Log.d(VecGlobals.LOG_TAG, "sjson tojson ex:"+d.getId(),e);
		}
		return null;
	}
	public  JSONObject toJSONV1(Drawing d) {
		JSONDrawing jsd = new JSONDrawing(_saveFile);
		return jsd.toJSON(d);
	}
	
	public  Drawing fromJSONV1(JSONObject o) {
			JSONDrawing jsd = new JSONDrawing(_saveFile);
			return jsd.fromJSON(o);
	}

}
