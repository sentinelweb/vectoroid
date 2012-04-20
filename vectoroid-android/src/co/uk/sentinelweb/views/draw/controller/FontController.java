package co.uk.sentinelweb.views.draw.controller;
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
      derived from this software	 without specific prior written permission.

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
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Typeface;
import android.util.Log;
import co.uk.sentinelweb.vectoroid.R;
import co.uk.sentinelweb.views.draw.VecGlobals;
import co.uk.sentinelweb.views.draw.file.FileRepository;
import co.uk.sentinelweb.views.draw.file.FileRepository.Directory;
import co.uk.sentinelweb.views.draw.file.SaveFile;
import co.uk.sentinelweb.views.draw.util.DispUtil;

public class FontController {
	private static final int PRV_MARGIN = 5;
	private static final int NM_FONT_SZ = 15;
	private static final int PRV_HGT = 50;
	private static final int PRV_WID = 300;
	private static final int PRV_FONT_SZ = 35;
	private static final String DEFAULT_FONT = "Default";
	public static final String FONT_PREVIEW_DIR = ".preview";
	public static HashMap<String, SoftReference<Typeface>> _ttfonts = new HashMap<String, SoftReference<Typeface>>();
	public HashMap<String, Font> _ttfontFiles = new HashMap<String, Font>();
	public ArrayList<String> _ttFontsOrder=new ArrayList<String>();
	FileRepository _fr;
	
	public FontController(FileRepository fr) {
		super();
		this._fr=fr;
		scanFonts(null);
	}
	
	public Typeface getTTFont(String name) {
		try {
			if (DEFAULT_FONT.equals(name)) {
				return Typeface.DEFAULT;
			} else if ((!_ttfonts.containsKey(name) || _ttfonts.get(name).get()==null) && _fr!=null && name!=null && !name.equals("")) {
				File file = _ttfontFiles.get(name)._fontFile;//new File(fr.getDirectory(Directory.TTF),ttfontFiles.get(name));
				Typeface t = Typeface.createFromFile(file);
				SoftReference<Typeface> typeRef = new SoftReference<Typeface>(t);
				_ttfonts.put(name, typeRef);
				return t;
			} else if (_ttfonts.containsKey(name) && _ttfonts.get(name).get()!=null && !name.equals("")) {
				return _ttfonts.get(name).get();
			} else { return Typeface.DEFAULT;}
			
		} catch (Exception e) {
			Log.d(VecGlobals.LOG_TAG, "Exception loading TTF Font : "+ name,e);
			return Typeface.DEFAULT;
		}
	}
	
	public void unloadFont(String name) {
		_ttfonts.remove(name);
	}
	
	public void scanFonts(Context c) {// NOTE context is optional 
		_ttfontFiles.clear();
		_ttFontsOrder.clear();
		Font font = new Font(null);
		_ttFontsOrder.add(font._fontName);
		_ttfontFiles.put(font._fontName, font);
		// make default preview if nessecary
		File previewFile = getFontPreviewFile(font._fontName);
		if (c!=null && !previewFile.exists()) {
			makePreview(c, font);
		}
		File ttfdir = _fr.getDirectory(Directory.TTF);
		File[] ttfFile = ttfdir.listFiles();
		if (ttfFile!=null) {
			for (File f: ttfFile) {
				if (!f.isDirectory() && f.getName().toLowerCase().endsWith(".ttf")) {
					scanFontFile(c,f);
				}
			}
		}
	}

	public void scanFontFile(Context c,File f) {
		if (f.getName().indexOf(".")>0) {
			Font font = new Font(f);
			if (_ttfontFiles.containsKey(font._fontName)) {
				_ttFontsOrder.add(font._fontName);
			} else {
				_ttFontsOrder.add(font._fontName);
				_ttfontFiles.put(font._fontName, font);
			}
			File previewFile = getFontPreviewFile(font._fontName);
			if (c!=null && !previewFile.exists()) {
				makePreview(c, font);
				unloadFont(font._fontName);
			}
		}
		sortFonts();
	}
	
	public void sortFonts() {
		Collections.sort(_ttFontsOrder);
	}
	
	public class Font{
		public String _fontName;
		public File _fontFile;
		public Font(File f) {
			super();
			if (f==null) {
				_fontName=DEFAULT_FONT;
				return;
			}
			this._fontName = f.getName().substring(0,f.getName().lastIndexOf("."));;
			this._fontFile = f;
		}
	}
	
	public void makePreview(Context c, Font fontFile) {
		float density = DispUtil.getDensity(c);
		Bitmap b = Bitmap.createBitmap((int)(PRV_WID*density),(int)(PRV_HGT*density),Config.RGB_565);
		Paint fontPaint = new Paint();
		fontPaint.setColor(Color.BLACK);
		fontPaint.setAlpha(255);
		fontPaint.setAntiAlias(true);
		fontPaint.setStrokeWidth(0);
		fontPaint.setStyle(Style.FILL);
		//String name = fontFile.getName();
		//name = name.substring(0,name.lastIndexOf("."));
		String name = fontFile._fontName;
		fontPaint.setTypeface(getTTFont(name));
		
		fontPaint.setTextSize(PRV_FONT_SZ*density);
		Paint namePaint = new Paint(fontPaint);
		namePaint.setTypeface(Typeface.DEFAULT);
		namePaint.setTextSize(NM_FONT_SZ*density);
		Canvas cvs = new Canvas(b);
		cvs.drawColor(Color.WHITE);
		cvs.drawText(c.getString(R.string.font_sample), PRV_MARGIN*density, (PRV_HGT-PRV_MARGIN)*density, fontPaint);
		float nameWidth = namePaint.measureText(name);
		cvs.drawText(name, PRV_WID*density-nameWidth-PRV_MARGIN*density, NM_FONT_SZ*density, namePaint);
		File outFile = getFontPreviewFile(name);
		Log.d(VecGlobals.LOG_TAG, "makeFontPreview ..."+outFile.getAbsolutePath());
		try {
			FileOutputStream out = new FileOutputStream(outFile);
			b.compress(Bitmap.CompressFormat.PNG, 90,out);
			out.close();
			Log.d(VecGlobals.LOG_TAG, "makeFontPreview ... preview generated.");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public File getFontPreviewFile(String name) {
		return new File(getFontPreviewDir(), name+SaveFile.PREVIEW_FILE_EXT);
	}

	public File getFontPreviewDir() {
		File file = new File(_fr.getDirectory(Directory.TTF),FONT_PREVIEW_DIR);
		if (!file.exists()) {file.mkdir();}
		return file;
	}
}
