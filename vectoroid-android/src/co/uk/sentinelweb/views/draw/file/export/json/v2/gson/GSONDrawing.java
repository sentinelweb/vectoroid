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
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Writer;

import android.graphics.PointF;
import android.util.Log;
import co.uk.sentinelweb.views.draw.VecGlobals;
import co.uk.sentinelweb.views.draw.file.SaveFile;
import co.uk.sentinelweb.views.draw.file.export.json.v2.JSONConst;
import co.uk.sentinelweb.views.draw.file.export.json.v2.JSONParent;
import co.uk.sentinelweb.views.draw.file.export.json.v2.gson.serialisers.FillDeserializer;
import co.uk.sentinelweb.views.draw.file.export.json.v2.gson.serialisers.FillSerializer;
import co.uk.sentinelweb.views.draw.file.export.json.v2.gson.serialisers.PenSerializer;
import co.uk.sentinelweb.views.draw.model.Drawing;
import co.uk.sentinelweb.views.draw.model.DrawingElement;
import co.uk.sentinelweb.views.draw.model.Fill;
import co.uk.sentinelweb.views.draw.model.Group;
import co.uk.sentinelweb.views.draw.model.Layer;
import co.uk.sentinelweb.views.draw.model.Pen;
import co.uk.sentinelweb.views.draw.model.Stroke;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

public class GSONDrawing extends JSONParent{
	public static float version = 2;
	Gson gson;
	GSONStroke gss;
	GSONLayer gsl;
	GSONGroup gsg;
	public GSONDrawing(SaveFile _saveFile) {
		super(_saveFile);
		GsonBuilder gsonb = new GsonBuilder();
		gsonb.registerTypeAdapter(Fill.class, new FillDeserializer(_saveFile));
		FillSerializer fillSerializer = new FillSerializer(_saveFile);
		gsonb.registerTypeAdapter(Fill.class, fillSerializer);
		PenSerializer penSerializer = new PenSerializer(_saveFile);
		gsonb.registerTypeAdapter(Pen.class, penSerializer);
		gson = gsonb.create();
		penSerializer.setGson(gson);
		fillSerializer.setGson(gson);
		
		gss = new GSONStroke(_saveFile);
		gsg = new GSONGroup(_saveFile);
		gsl = new GSONLayer(_saveFile);
		
	}
	
	
	public void toJSON(Drawing d,Writer out) throws Exception{
		try {
			JsonWriter jwriter = new JsonWriter(out);
			//writer.setIndent("  ");// testing
			jwriter.beginObject();
			jwriter.name(JSONConst.JSON_ID).value(d.getId()); 
			jwriter.name(JSONConst.JSON_VERSION).value(version); 
			jwriter.name(JSONConst.JSON_SIZE); 
			gson.toJson(d.size, PointF.class, jwriter);
			jwriter.name(JSONConst.JSON_BG); 
			gson.toJson(d.background, Fill.class, jwriter);
			
			jwriter.name(JSONConst.JSON_ELEMENTS); 
			jwriter.beginArray();
			for (DrawingElement de : d.elements) {
				if (de instanceof Group) {
					//jsElements.put(jsg.toJSON((Group)de));
					gsg.toJSON((Group)de,gson,jwriter,gss);
				} else if (de instanceof Stroke) {
					//jsElements.put(jss.toJSON((Stroke)de));
					gss.toJSON((Stroke)de,gson,jwriter);
				}
			}
			jwriter.endArray();
			
			jwriter.name(JSONConst.JSON_LAYERS); 
			if (d.layers!=null) {
				jwriter.beginArray();
				for (Layer l : d.layers) {
					gsl.toJSON(l,gson,jwriter,gsg,gss);
				}
				jwriter.endArray();
			}
			jwriter.endObject(); // }
			jwriter.close();
		}catch (Exception e) {
			Log.d(VecGlobals.LOG_TAG, "GSON Drawing to drawing", e);
		}
	}
	
	public Drawing fromJSON(InputStream in) throws Exception {
		Drawing d = new Drawing();
		try {
			JsonReader reader = new JsonReader(new InputStreamReader(in, "UTF-8"));
			reader.beginObject();
			reader.peek();
			while (reader.hasNext()) {
				String name = reader.nextName();
				//Log.d(DVGlobals.LOG_TAG, "nextname:"+name);
				if (JSONConst.JSON_ID.equals(name)) {
					d.setId(reader.nextString());
				} else if (JSONConst.JSON_VERSION.equals(name)) {
					double ver = reader.nextDouble();
					if (ver!=version) {
						//throw new Exception("incorrect version");
					}
				} else if (JSONConst.JSON_BG.equals(name)) {
					//reader.skipValue();
					d.background = gson.fromJson(reader, Fill.class);
					Log.d(VecGlobals.LOG_TAG, "GSON v2 Drawing bg: "+d.background);
				} else if (JSONConst.JSON_SIZE.equals(name)) {
					d.size = GSONStatic.fromJSON(gson, reader);
				} else if (JSONConst.JSON_ELEMENTS.equals(name)) {
					reader.beginArray();
					while (reader.hasNext()) {
						reader.beginObject();
						String elname = reader.nextName();
						if (JSONConst.JSON_EL_TYPE.equals(elname)) {
							String typeName = reader.nextString();
							if (JSONConst.JSON_EL_TYPE_GROUP.equals(typeName)) {
								d.elements.add(gsg.fromJSON(gson,reader,gss));
							} else if (JSONConst.JSON_EL_TYPE_STROKE.equals(typeName)) {
								d.elements.add(gss.fromJSON(gson,reader));
							}
						} else {
							throw new Exception("no element type");
						}
						reader.endObject();
					}
					reader.endArray();
					
				} else if (JSONConst.JSON_LAYERS.equals(name)) {
					reader.beginArray();
					//reader.skipValue();
					while (reader.hasNext()) {
						d.layers.add(gsl.fromJSON(gson, reader,gsg,gss));
					}
					reader.endArray();
				}
			}
			reader.endObject();
			reader.close();
		} catch (IOException e) {
			Log.d(VecGlobals.LOG_TAG, "JSON Drawing from drawing", e);
		}
		return d;
	}
}
