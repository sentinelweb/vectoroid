package co.uk.sentinelweb.views.draw.file.export.json.simple;
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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;
import co.uk.sentinelweb.views.draw.VecGlobals;
import co.uk.sentinelweb.views.draw.file.SaveFile;
import co.uk.sentinelweb.views.draw.file.export.json.JSONConst;
import co.uk.sentinelweb.views.draw.file.export.json.JSONParent;
import co.uk.sentinelweb.views.draw.model.Drawing;
import co.uk.sentinelweb.views.draw.model.DrawingElement;
import co.uk.sentinelweb.views.draw.model.Group;
import co.uk.sentinelweb.views.draw.model.Layer;
import co.uk.sentinelweb.views.draw.model.Stroke;

public class JSONDrawing extends JSONParent{
	public static float version = 1;
	
	JSONLayer jsl ;
	JSONGroup jsg ;
	JSONStroke jss ;
	JSONFill jsf;
	
	public JSONDrawing(SaveFile _saveFile) {
		super(_saveFile);
		 jsl = new JSONLayer(_saveFile);
		 jsg = new JSONGroup(_saveFile);
		 jss = new JSONStroke(_saveFile);
		 jsf = new JSONFill(_saveFile);
	}
	
	public JSONObject toJSON(Drawing d) {
		try {
			JSONObject drawingJSON = new JSONObject();
			drawingJSON.put(JSONConst.JSON_ID, d.getId());
			drawingJSON.put(JSONConst.JSON_VERSION, version);
			drawingJSON.put(JSONConst.JSON_SIZE, JSONStatic.toJSON(d.size));
			drawingJSON.put(JSONConst.JSON_BG, jsf.toJSON(d.background));
			JSONArray jsElements = new JSONArray();
			for (DrawingElement de : d.elements) {
				if (de instanceof Group) {
					jsElements.put(jsg.toJSON((Group)de));
				} else if (de instanceof Stroke) {
					jsElements.put(jss.toJSON((Stroke)de));
				}
			}
			drawingJSON.put(JSONConst.JSON_ELEMENTS, jsElements);
			JSONArray jsLayers = new JSONArray();
			for (Layer de : d.layers) {
				jsLayers.put(jsl.toJSON(de));
			}
			drawingJSON.put(JSONConst.JSON_LAYERS, jsLayers);
			return drawingJSON;
		} catch (JSONException e) {
			Log.d(VecGlobals.LOG_TAG, "JSON Drawing to drawing", e);
		}
		return null;
	}
	
	public Drawing fromJSON(JSONObject o) {
		Drawing d = new Drawing();
		try {
			if (o.has(JSONConst.JSON_ID)) {d.setId(o.getString(JSONConst.JSON_ID));}
			if (o.has(JSONConst.JSON_VERSION)) {float ver = (float) o.getDouble(JSONConst.JSON_VERSION);}
			d.background=jsf.fromJSON(o.getJSONObject(JSONConst.JSON_BG));
			d.size=JSONStatic.fromJSON(o.getJSONObject(JSONConst.JSON_SIZE));
			JSONArray jsElementsArr = o.getJSONArray(JSONConst.JSON_ELEMENTS);
			for (int i=0;i<jsElementsArr.length();i++) {
				JSONObject object = (JSONObject)jsElementsArr.get(i);
				String type = (String)object.getString(JSONConst.JSON_EL_TYPE);
				if (JSONConst.JSON_EL_TYPE_GROUP.equals(type)){
					d.elements.add(jsg.fromJSON(object));
				} else if (JSONConst.JSON_EL_TYPE_STROKE.equals(type)){
					d.elements.add(jss.fromJSON(object));
				}
			}
			JSONArray jsLayersArr = o.getJSONArray(JSONConst.JSON_LAYERS);
			for (int i=0;i<jsLayersArr.length();i++) {
				JSONObject object = (JSONObject)jsLayersArr.get(i);
				String type = (String)object.getString(JSONConst.JSON_EL_TYPE);
				if (JSONConst.JSON_EL_TYPE_LAYER.equals(type)){
					d.layers.add((Layer)jsl.fromJSON(object));
				}
			}
		} catch (JSONException e) {
			Log.d(VecGlobals.LOG_TAG, "JSON Drawing from drawing", e);
		}
		return d;
	}
}
