package co.uk.sentinelweb.views.draw.file.export.json.v3.gson.serialisers;
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
import java.util.Map.Entry;
import java.util.Set;

import android.graphics.Color;
import co.uk.sentinelweb.views.draw.file.SaveFile;
import co.uk.sentinelweb.views.draw.file.export.json.v3.JSONConst;
import co.uk.sentinelweb.views.draw.file.export.json.v3.gson.GSONStatic;
import co.uk.sentinelweb.views.draw.model.Fill;
import co.uk.sentinelweb.views.draw.model.Gradient;
import co.uk.sentinelweb.views.draw.model.GradientData;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

public class FillDeserializer implements JsonDeserializer<Fill> {
	SaveFile _saveFile = null;
	
	public FillDeserializer(SaveFile _saveFile) {
		super();
		this._saveFile = _saveFile;
	}

	@Override
	public Fill deserialize(JsonElement el, Type type, JsonDeserializationContext cxt) throws JsonParseException {
		//return BreakType.valueOf(el.getAsString());
		JsonObject fillJS = el.getAsJsonObject();
		Fill f = new Fill();
		Set<Entry<String, JsonElement>> fillEntrySet = fillJS.entrySet();
		JsonElement typeElement = GSONStatic.getElement(fillEntrySet,JSONConst.JSON_TYPE);
		if (typeElement!=null) {
			f.type = Fill.Type.valueOf(typeElement.getAsString());
			if (f.type==Fill.Type.COLOUR ) {
				typeElement = GSONStatic.getElement(fillEntrySet, JSONConst.JSON_COLOUR);
				if (typeElement!=null) {
					f._color=typeElement.getAsInt();
				} else {
					typeElement = GSONStatic.getElement(fillEntrySet, JSONConst.JSON_WEBCOLOUR);
					if (typeElement!=null) {
						Color.parseColor(typeElement.getAsString());
					}
				}
			} else if (f.type==Fill.Type.GRADIENT) {
				Gradient g = new Gradient();
				typeElement = GSONStatic.getElement(fillEntrySet, JSONConst.JSON_GRADIENT);
				Set<Entry<String, JsonElement>> gradEntrySet = typeElement.getAsJsonObject().entrySet();
				for (Entry<String, JsonElement> e:gradEntrySet) {
					if (JSONConst.JSON_TYPE.equals(e.getKey())) {
						g.type = Gradient.Type.valueOf(e.getValue().getAsString());
					} else if (JSONConst.JSON_COLOURS.equals(e.getKey())) {
						JsonArray jscols = e.getValue().getAsJsonArray();
						g.colors=new int[jscols.size()];
						for (int i=0;i<jscols.size();i++) {
							g.colors[i]=jscols.get(i).getAsInt();
						}
						
					} else if (JSONConst.JSON_POSITIONS.equals(e.getKey())) {
						JsonArray jspos = e.getValue().getAsJsonArray();
						g.positions=new float[jspos.size()];
						for (int i=0;i<jspos.size();i++) {
							g.positions[i]=(float)jspos.get(i).getAsFloat();
						}
					} else if (JSONConst.JSON_GD.equals(e.getKey())) {
						GradientData gd = cxt.deserialize(e.getValue(), GradientData.class);
						g.data=gd;
					}
				}
				f.setGradient(g);
			} else if (f.type==Fill.Type.BITMAP) {
				JsonElement assetElement = GSONStatic.getElement(fillEntrySet, JSONConst.JSON_ASSET);
				JsonElement assetName = GSONStatic.getElement(assetElement.getAsJsonObject().entrySet(),JSONConst.JSON_NAME);
				if (assetName!=null && _saveFile!=null) {
					f._bitmapFill=_saveFile.getAssetManager().loadAsset(assetName.getAsString());
					JsonElement alphaElement = GSONStatic.getElement(fillEntrySet, JSONConst.JSON_BMP_ALPHA);
					if (alphaElement!=null) {
						f._bitmapAlpha=alphaElement.getAsInt();
					}
				}
			}
		}
		
		
		return f;
	}

}
