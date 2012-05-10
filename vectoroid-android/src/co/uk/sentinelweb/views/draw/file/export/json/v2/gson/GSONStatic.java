package co.uk.sentinelweb.views.draw.file.export.json.v2.gson;
/*
Vectoroid API for Andorid
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
import java.io.IOException;
import java.util.Map.Entry;
import java.util.Set;

import android.graphics.PointF;
import co.uk.sentinelweb.views.draw.file.export.json.v2.JSONConst;
import co.uk.sentinelweb.views.draw.model.DrawingElement;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

public class GSONStatic {
	public static PointF fromJSON(Gson gson, JsonReader reader,float dimensionRatio) {
		try {
			//return new PointF((float)pointJSON.getDouble(JSONConst.JSON_X),(float)pointJSON.getDouble(JSONConst.JSON_Y));
			//reader.beginObject();
			
			PointF p = gson.fromJson(reader, PointF.class);
			//Log.d(DVGlobals.LOG_TAG, "gson pt:"+PointUtil.tostr(p));
			return p;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static PointF fromJSON(Gson gson,JsonReader reader) {
		return fromJSON(gson, reader,1f);
	}

	public static JsonElement getElement(Set<Entry<String, JsonElement>> entrySet, String jsonType) {
		for (Entry<String, JsonElement> e:entrySet) {
			if (jsonType.equals(e.getKey())) {
				return e.getValue();
			}
		}
		return null;
	}
	
	public static void writeDrawingElement(DrawingElement de, String type, JsonWriter writer) throws IOException{
		writer.name(JSONConst.JSON_EL_TYPE).value(type); 
		writer.name(JSONConst.JSON_ID).value(de.getId()); 
		writer.name(JSONConst.JSON_LOCKED).value(de.locked); 
		writer.name(JSONConst.JSON_VISIBLE).value(de.visible); 
	}
}
