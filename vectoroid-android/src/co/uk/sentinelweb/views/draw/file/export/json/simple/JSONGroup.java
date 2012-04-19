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
import co.uk.sentinelweb.views.draw.DVGlobals;
import co.uk.sentinelweb.views.draw.file.SaveFile;
import co.uk.sentinelweb.views.draw.file.export.json.JSONConst;
import co.uk.sentinelweb.views.draw.file.export.json.JSONParent;
import co.uk.sentinelweb.views.draw.model.DrawingElement;
import co.uk.sentinelweb.views.draw.model.Group;
import co.uk.sentinelweb.views.draw.model.Stroke;

public class JSONGroup extends JSONParent{
	public JSONGroup(SaveFile _saveFile) {
		super(_saveFile);
	}
	//public static final String JSON_ELEMENTS = "elements";
	//public static final String JSON_LOCKED = "locked";
	//public static final String JSON_VISIBLE = "visible";
	//public static final String JSON_ID = "id";
	
	JSONStroke jss = new JSONStroke(_saveFile);
	public JSONObject toJSON(Group g) {
		JSONObject o = new JSONObject();
		try {
			o.put(JSONConst.JSON_EL_TYPE,JSONConst.JSON_EL_TYPE_GROUP);
			o.put(JSONConst.JSON_ID, g.getId());
			o.put(JSONConst.JSON_LOCKED,g.locked);
			o.put(JSONConst.JSON_VISIBLE,g.visible);
			JSONArray jsElements = new JSONArray();
			for (DrawingElement de : g.elements) {
				if (de instanceof Group) {
					jsElements.put(toJSON((Group)de));
				} else if (de instanceof Stroke) {
					jsElements.put(jss.toJSON((Stroke)de));
				}
			}
			o.put(JSONConst.JSON_ELEMENTS, jsElements);
		} catch (JSONException e) {
			Log.d(DVGlobals.LOG_TAG, "JSON to Group", e);
		}
		return o;
	}
	
	public DrawingElement fromJSON(JSONObject object) {
		Group g = new Group();
		try {
			if (object.has(JSONConst.JSON_LOCKED)) {g.locked=object.getBoolean(JSONConst.JSON_LOCKED);}
			if (object.has(JSONConst.JSON_VISIBLE)) {g.visible=object.getBoolean(JSONConst.JSON_VISIBLE);}
			
			if (object.has(JSONConst.JSON_ID)) {g.setId(object.getString(JSONConst.JSON_ID));}
			JSONArray jsels = object.getJSONArray(JSONConst.JSON_ELEMENTS);
			for (int i =0;i<jsels.length();i++) {
				JSONObject el = jsels.getJSONObject(i);
				String type = (String)el.getString(JSONConst.JSON_EL_TYPE);
				if (JSONConst.JSON_EL_TYPE_GROUP.equals(type)){
					g.elements.add(fromJSON(el));
				} else if (JSONConst.JSON_EL_TYPE_STROKE.equals(type)){
					g.elements.add(jss.fromJSON(el));
				}
			}
		} catch (JSONException e) {
			Log.d(DVGlobals.LOG_TAG, "JSON from Group", e);
		}
		return g;
	}

}
