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
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import org.json.JSONException;

import co.uk.sentinelweb.views.draw.file.export.json.JSONDrawingIO;
import co.uk.sentinelweb.views.draw.model.Drawing;

public class DrawingFileUtil {
	public static boolean saveJSON(Drawing d,File file,SaveFile sv) {
		//try {
			//File file = getClipJson(ci);
			//FileWriter out = new FileWriter(file);
			//JSONDrawing jsd = new JSONDrawing(sv);
			//JSONObject o = jsd.toJSON(d);
			//out.write(o.toString());
			//out.flush();
			//out.close();
			JSONDrawingIO jdio = new JSONDrawingIO(sv);
			return jdio.saveJSON(d, null,file);
//		} 
//		catch (FileNotFoundException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		} 
//		return false;
	}

	public static Drawing loadJSON(File file,SaveFile sv) {
		try {
			//File file = getClipJson(ci);
			
//			StringWriter sw = new StringWriter();
//			BufferedReader reader = new BufferedReader(	new FileReader(file));
//			String readline = "";
//			while ((readline = reader.readLine()) != null) { 
//				sw.append(readline);
//			}
//			String string = sw.toString();
//			JSONObject o = new JSONObject(new JSONTokener(string));
//			JSONDrawing jsd = new JSONDrawing(sv);
			JSONDrawingIO jdio = new JSONDrawingIO(sv);
			BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
			Drawing dd=jdio.loadJSON(bis);
			return dd;
			//Drawing d =jsd.fromJSON(o);
			//return d;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}
	/*
	public static Drawing loadJSON(InputStream is) {
		try {
			InputStream fis = new BufferedInputStream(is);
			GSONDrawing gd = new GSONDrawing(null);
			Drawing dd=gd.fromJSON( fis);
			return dd;
		} catch (Exception e) {
			Log.d(VecGlobals.LOG_TAG, "loadGSON: ex :",e);
			//d = parseJSONSimple(file);
		}
		return null;
	}
	*/
	public static Drawing loadJSON(InputStream is) {
		try {
			//File file = getClipJson(ci);
			//InputStream fis = new BufferedInputStream(is);
			//GSONDrawing gd = new GSONDrawing(null);
			//Drawing dd=gd.fromJSON( fis);
			JSONDrawingIO jdio = new JSONDrawingIO(null);
			BufferedInputStream bis = new BufferedInputStream(is);
			Drawing dd=jdio.loadJSON(bis);
			return dd;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (Exception e) {
			//Log.d(VecGlobals.LOG_TAG, "loadGSON: ex :",e);
			//d = parseJSONSimple(file);
		}
		return null;
	}
}
