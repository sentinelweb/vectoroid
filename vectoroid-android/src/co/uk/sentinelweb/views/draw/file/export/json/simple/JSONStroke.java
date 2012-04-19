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
import co.uk.sentinelweb.views.draw.model.PointVec;
import co.uk.sentinelweb.views.draw.model.Stroke;

public class JSONStroke extends JSONParent{
	JSONFill jsf;
	JSONPen jsp ;
	JSONPointVec jspv ;
	public JSONStroke(SaveFile _saveFile) {
		super(_saveFile);
		 jsf = new JSONFill(_saveFile);
		 jsp = new JSONPen(_saveFile);
		 jspv = new JSONPointVec(_saveFile);
	}

	//private static final String JSON_TYPE = "type";
	//public static final String JSON_ID = "id";
	
	
	
	public JSONObject toJSON(Stroke de) {
		JSONObject o = getJSONObject();
		try {
			o.put(JSONConst.JSON_EL_TYPE,JSONConst.JSON_EL_TYPE_STROKE);
			o.put(JSONConst.JSON_ID, de.getId());
			o.put(JSONConst.JSON_TYPE, de.type);
			if (de.locked) {o.put(JSONConst.JSON_LOCKED,de.locked);}
			if (de.visible) {o.put(JSONConst.JSON_VISIBLE,de.visible);}
			if (de.textXScale!=1) {o.put(JSONConst.JSON_TEXTXSCALE, de.textXScale);}
			if (de.text!=null && !"".equals(de.text)) {
				o.put(JSONConst.JSON_TEXT, de.text);
				if (de.fontName!=null && !"".equals(de.fontName)) {
					o.put(JSONConst.JSON_FONT_NAME, de.fontName);
				}
			}
			if (de.pen!=null) {o.put(JSONConst.JSON_PEN, jsp.toJSON(de.pen));}
			if (de.fill!=null) {o.put(JSONConst.JSON_FILL, jsf.toJSON(de.fill));}
			JSONArray jsPts = new JSONArray();
			for (PointVec pv : de.points) {
				jsPts.put(jspv.toJSON(pv));
			}
			o.put(JSONConst.JSON_POINTS, jsPts);
		} catch(JSONException e) {
			Log.d(DVGlobals.LOG_TAG, "JSON to stroke", e);
		}
		return o;
	}

	public DrawingElement fromJSON(JSONObject object) {
		Stroke s = new Stroke();
		try {
			if (object.has(JSONConst.JSON_TYPE)) {
				s.type=Stroke.Type.valueOf(object.getString(JSONConst.JSON_TYPE));
				if (object.has(JSONConst.JSON_ID)) {s.setId(object.getString(JSONConst.JSON_ID));}
				if (object.has(JSONConst.JSON_LOCKED)) {s.locked=object.getBoolean(JSONConst.JSON_LOCKED);} else {s.locked=false;}
				if (object.has(JSONConst.JSON_VISIBLE)) {s.visible=object.getBoolean(JSONConst.JSON_VISIBLE);} else {s.visible=true;}
				if (object.has(JSONConst.JSON_TEXT)) {s.text=object.getString(JSONConst.JSON_TEXT);}
				if (object.has(JSONConst.JSON_TEXTXSCALE)) {s.textXScale=(float)object.getDouble(JSONConst.JSON_TEXTXSCALE);} else {s.textXScale=1;}
				if (object.has(JSONConst.JSON_FONT_NAME)) {s.fontName=object.getString(JSONConst.JSON_FONT_NAME);}
				//s.fontTTF=FileRepository.getFileRepository()._fontController.getTTFont(s.fontName);
				JSONArray jsPts = object.getJSONArray(JSONConst.JSON_POINTS);
				for (int i=0;i<jsPts.length();i++) {
					s.points.add(jspv.fromJSON(jsPts.getJSONObject(i)));
				}
				if (object.has(JSONConst.JSON_PEN)) {s.pen = jsp.fromJSON(object.getJSONObject(JSONConst.JSON_PEN));}
				if (object.has(JSONConst.JSON_FILL)) {s.fill = jsf.fromJSON(object.getJSONObject(JSONConst.JSON_FILL));}
			}
		} catch (JSONException e) {
			Log.d(DVGlobals.LOG_TAG, "JSON fromstroke", e);
		}
		return s;
	}
	
	public void setReUse(boolean reuse) {
		reUseJSONObjects=reuse;
		jsf.setReUse( reuse );
		jsp.reUseJSONObjects=reuse;
		jspv.reUseJSONObjects=reuse;
	}
}
