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
import co.uk.sentinelweb.views.draw.model.DrawingElement;
import co.uk.sentinelweb.views.draw.model.Group;
import co.uk.sentinelweb.views.draw.model.Stroke;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

public class GSONGroup extends JSONParent {

	public GSONGroup(SaveFile _saveFile) {
		super(_saveFile);
	}

	public void toJSON(Group g,Gson gson, JsonWriter writer, GSONStroke gss) throws IOException {
		writer.beginObject();
		GSONStatic.writeDrawingElement(g, JSONConst.JSON_EL_TYPE_GROUP, writer);
		writer.name(JSONConst.JSON_ELEMENTS); 
		writer.beginArray();
		for (DrawingElement de : g.elements) {
			if (de instanceof Group) {
				toJSON((Group)de,gson,writer,gss);
			} else if (de instanceof Stroke) {
				gss.toJSON((Stroke)de,gson,writer);
			}
		}
		writer.endArray();
		writer.endObject();
	}
	
	public Group fromJSON(Gson gson, JsonReader reader, GSONStroke gss) throws Exception {
		Group g = new Group();
		try {
			//reader.beginObject();
			reader.peek();
			while (reader.hasNext()) {
				String name = reader.nextName();
				//Log.d(DVGlobals.LOG_TAG, "group nextname:"+name);
				if (JSONConst.JSON_ID.equals(name)) {
					g.setId(reader.nextString());
				} else if (JSONConst.JSON_VISIBLE.equals(name)) {
					g.visible = reader.nextBoolean();
				} else if (JSONConst.JSON_LOCKED.equals(name)) {
					g.locked = reader.nextBoolean();
				} else if (JSONConst.JSON_ELEMENTS.equals(name)) {
					reader.beginArray();
					while (reader.hasNext()) {
						//reader.beginObject();
						//l.elements.add(gss.fromJSON(gson, reader));
						//reader.endObject();
						reader.beginObject();
						String elname = reader.nextName();
						if (JSONConst.JSON_EL_TYPE.equals(elname)) {
							String typeName = reader.nextString();
							if (JSONConst.JSON_EL_TYPE_GROUP.equals(typeName)) {
								g.elements.add(fromJSON(gson,reader,gss));
							} else if (JSONConst.JSON_EL_TYPE_STROKE.equals(typeName)) {
								g.elements.add(gss.fromJSON(gson,reader));
							}
						} else {
							throw new Exception("no element type");
						}
						reader.endObject();
					}
					reader.endArray();
				} else if (JSONConst.JSON_EL_TYPE.equals(name)) {
					reader.skipValue();
				} else {
					reader.skipValue();
				}
			}
			//reader.endObject();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return g;
	}

}
