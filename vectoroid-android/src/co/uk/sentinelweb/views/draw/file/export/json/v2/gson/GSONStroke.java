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

import co.uk.sentinelweb.views.draw.file.SaveFile;
import co.uk.sentinelweb.views.draw.file.export.json.v2.JSONConst;
import co.uk.sentinelweb.views.draw.file.export.json.v2.JSONParent;
import co.uk.sentinelweb.views.draw.model.Fill;
import co.uk.sentinelweb.views.draw.model.Pen;
import co.uk.sentinelweb.views.draw.model.PointVec;
import co.uk.sentinelweb.views.draw.model.Stroke;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

public class GSONStroke extends JSONParent {
	
	GSONPointVec gspv = null ;
	
	public GSONStroke(SaveFile _saveFile) {
		super(_saveFile);
		gspv = new GSONPointVec();
	}
	
	public void toJSON(Stroke s, Gson gson, JsonWriter writer) throws IOException {
		writer.beginObject();
		GSONStatic.writeDrawingElement(s, JSONConst.JSON_EL_TYPE_STROKE, writer);
		writer.name(JSONConst.JSON_TYPE).value(s.type.toString());
		if (s.fontName!=null && !"".equals(s.fontName)) {writer.name(JSONConst.JSON_FONT_NAME).value(s.fontName); }
		if (s.text!=null && !"".equals(s.text)) {writer.name(JSONConst.JSON_TEXT).value(s.text); }
		if (s.textXScale!=1){writer.name(JSONConst.JSON_TEXTXSCALE).value(s.textXScale); }
		if (s.pen !=null) { writer.name(JSONConst.JSON_PEN);gson.toJson(s.pen, Pen.class,writer);}
		if (s.fill !=null) { writer.name(JSONConst.JSON_FILL);gson.toJson(s.fill, Fill.class,writer);}
		if (s.points!=null) {
			writer.name(JSONConst.JSON_POINTS);
			writer.beginArray();
			for (PointVec pv : s.points) {
				gspv.toJSON(pv,gson, writer);
			}
			writer.endArray();
		}
		writer.endObject();
	}
	
	public Stroke fromJSON(Gson gson,JsonReader reader) {
		Stroke s = new Stroke();
		//Log.d(DVGlobals.LOG_TAG, "process Stroke");
		try {
			reader.peek();
			while (reader.hasNext()) {
				String name = reader.nextName();
				//Log.d(DVGlobals.LOG_TAG, "stroke nextname:"+name);
				if (JSONConst.JSON_EL_TYPE.equals(name)) {
					reader.skipValue();// should have been read a higher level
				} else if (JSONConst.JSON_ID.equals(name)) {
					s.setId(reader.nextString());
				} else if (JSONConst.JSON_VISIBLE.equals(name) || "visble".equals(name)) {
					s.visible = reader.nextBoolean();
				} else if (JSONConst.JSON_LOCKED.equals(name)) {
					s.locked = reader.nextBoolean();
				} else if (JSONConst.JSON_FONT_NAME.equals(name)) {
					s.fontName = reader.nextString();
				} else if (JSONConst.JSON_TEXT.equals(name)) {
					s.text = reader.nextString();
				} else if (JSONConst.JSON_TEXTXSCALE.equals(name)) {
					s.textXScale = (float) reader.nextDouble();
				} else if (JSONConst.JSON_PEN.equals(name)) {
					s.pen = gson.fromJson(reader, Pen.class);
					//s.pen.log();
				} else if (JSONConst.JSON_FILL.equals(name)) {
					s.fill = gson.fromJson(reader, Fill.class);
					//s.fill.log();
				} else if (JSONConst.JSON_TYPE.equals(name)) {
					s.type=Stroke.Type.valueOf(reader.nextString());
				} else if (JSONConst.JSON_POINTS.equals(name)) {
					reader.beginArray();
					while (reader.hasNext()) {
						s.points.add(gspv.fromJSON(gson, reader));
					}
					reader.endArray();
				} else {
					reader.skipValue();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return s;
	}
}
