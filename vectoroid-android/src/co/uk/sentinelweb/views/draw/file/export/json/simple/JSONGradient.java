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
import co.uk.sentinelweb.views.draw.model.Gradient;
import co.uk.sentinelweb.views.draw.model.GradientData;

public class JSONGradient extends JSONParent{
	public JSONGradient(SaveFile _saveFile) {
		super(_saveFile);
	}

	public Object toJSON(Gradient g) {
		JSONObject o = getJSONObject();
		try {
			o.put(JSONConst.JSON_TYPE, g.type);
			JSONArray jscols = new JSONArray();
			for (int col : g.colors) {
				jscols.put(col);
			}
			o.put(JSONConst.JSON_COLOURS, jscols);
			
			JSONArray jspos = new JSONArray();
			for (float pos : g.positions) {
				jspos.put(pos);
			}
			o.put(JSONConst.JSON_POSITIONS, jspos);
			
			if (g.data!=null) {
				o.put(JSONConst.JSON_GD, toJSON(g.data));
			}
		} catch(JSONException e) {
			Log.d(DVGlobals.LOG_TAG, "JSON to gradient", e);
		}
		return o;
	}
	
	//JSONObject jsonGradientData;
	public JSONObject toJSON(GradientData g) {
		JSONObject jsonGradientData = new JSONObject();//getJSONObject(jsonGradientData);
		try {
			jsonGradientData.put(JSONConst.JSON_GD_P1, JSONStatic.toJSON(g.p1));
			jsonGradientData.put(JSONConst.JSON_GD_P2, JSONStatic.toJSON(g.p2));
		} catch(JSONException e) {
			Log.d(DVGlobals.LOG_TAG, "JSON to gradient data", e);
		}
		return jsonGradientData;
	}

	public Gradient fromJSON(JSONObject o) {
		Gradient g = new Gradient();
		try {
			g.type = Gradient.Type.valueOf(o.getString(JSONConst.JSON_TYPE));
			JSONArray jscols = o.getJSONArray(JSONConst.JSON_COLOURS);
			g.colors=new int[jscols.length()];
			for (int i=0;i<jscols.length();i++) {
				g.colors[i]=jscols.getInt(i);
			}
			JSONArray jspos = o.getJSONArray(JSONConst.JSON_POSITIONS);
			g.positions=new float[jspos.length()];
			for (int i=0;i<jspos.length();i++) {
				g.positions[i]=(float)jspos.getDouble(i);
			}
			g.data=fromJSONGD(o.getJSONObject(JSONConst.JSON_GD));
		} catch (JSONException e) {
			Log.d(DVGlobals.LOG_TAG, "JSON from gradient", e);
		}
		return g;
	}
	
	public GradientData fromJSONGD(JSONObject o) {
		GradientData gd = new GradientData();
		try {
			gd.p1=JSONStatic.fromJSON(o.getJSONObject(JSONConst.JSON_GD_P1));
			gd.p2=JSONStatic.fromJSON(o.getJSONObject(JSONConst.JSON_GD_P2));
		} catch (JSONException e) {
			Log.d(DVGlobals.LOG_TAG, "JSON from gradient data", e);
		}
		return gd;
	}
}
