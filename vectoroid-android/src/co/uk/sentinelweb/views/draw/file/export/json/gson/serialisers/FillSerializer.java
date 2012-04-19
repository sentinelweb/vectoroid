package co.uk.sentinelweb.views.draw.file.export.json.gson.serialisers;
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
import java.lang.reflect.Type;

import android.graphics.PointF;
import co.uk.sentinelweb.views.draw.file.SaveFile;
import co.uk.sentinelweb.views.draw.file.export.json.JSONConst;
import co.uk.sentinelweb.views.draw.model.Fill;
import co.uk.sentinelweb.views.draw.model.GradientData;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class FillSerializer implements JsonSerializer<Fill> {
	SaveFile _saveFile = null;
	Gson gson;
	public FillSerializer(SaveFile _saveFile) {
		super();
		this._saveFile = _saveFile;
	}
	
	@Override
	public JsonElement serialize(Fill f, Type arg1, JsonSerializationContext cxt) {
		JsonObject e = new JsonObject();
		e.addProperty(JSONConst.JSON_TYPE,  f.type.toString());
		switch ( f.type ) {
			case COLOUR: 
				e.addProperty(JSONConst.JSON_COLOUR,  f._color);
				break;
			case GRADIENT: 
				//o.put(JSONConst.JSON_GRADIENT, jsg.toJSON(f._gradient));
				if (f._gradient!=null) {
					JsonObject gradientJS = new JsonObject();
					gradientJS.addProperty(JSONConst.JSON_TYPE, f._gradient.type.toString());
					JsonArray jscols = new JsonArray();
					for (int col : f._gradient.colors) {
						jscols.add(new JsonPrimitive(col));// TODO weird
					}
					gradientJS.add(JSONConst.JSON_COLOURS, jscols);
					
					JsonArray jspos = new JsonArray();
					for (float pos : f._gradient.positions) {
						jspos.add(new JsonPrimitive(pos));
					}
					gradientJS.add(JSONConst.JSON_POSITIONS, jspos);
					if (f._gradient.data!=null && f._gradient.data.p1!=null && f._gradient.data.p2!=null ) {
						gradientJS.add(JSONConst.JSON_GD, toJSON(f._gradient.data));
						
					}
					e.add(JSONConst.JSON_GRADIENT,  gradientJS);
				}
				break;
			case BITMAP: 
				if (f._bitmapFill!=null) {
					JsonObject assetJS = new JsonObject();
					assetJS.addProperty(JSONConst.JSON_NAME, f._bitmapFill.getName());
					e.add(JSONConst.JSON_ASSET, assetJS);
				}
				e.addProperty(JSONConst.JSON_BMP_ALPHA, f._bitmapAlpha);
				break;
		}
		return e;
	}
	public JsonObject toJSON(GradientData gd) {
		JsonObject o = new JsonObject();
			o.add(JSONConst.JSON_GD_P1, gson.toJsonTree(gd.p1, PointF.class));
			o.add(JSONConst.JSON_GD_P2, gson.toJsonTree(gd.p2, PointF.class));
		return o;
	}

	/**
	 * @return the gson
	 */
	public Gson getGson() {
		return gson;
	}

	/**
	 * @param gson the gson to set
	 */
	public void setGson(Gson gson) {
		this.gson = gson;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
