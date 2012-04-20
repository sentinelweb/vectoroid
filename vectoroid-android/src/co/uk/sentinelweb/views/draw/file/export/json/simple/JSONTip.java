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
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;
import co.uk.sentinelweb.views.draw.VecGlobals;
import co.uk.sentinelweb.views.draw.file.SaveFile;
import co.uk.sentinelweb.views.draw.file.export.json.JSONConst;
import co.uk.sentinelweb.views.draw.file.export.json.JSONParent;
import co.uk.sentinelweb.views.draw.model.StrokeDecoration.Tip;

public class JSONTip extends JSONParent {
	//private static final String JSON_SIZE = "size";
	
	public JSONTip(SaveFile _saveFile) {
		super(_saveFile);
	}
	public JSONObject toJSON(Tip e) {
		JSONObject o = new JSONObject();
		try {
			if (e.type!=Tip.Type.NONE) {o.put(JSONConst.JSON_TYPE, e.type.toString());}
			if (e.closed) {o.put(JSONConst.JSON_CLOSED, e.closed);}
			if (e.filled) {o.put(JSONConst.JSON_FILLED, e.filled);}
			if (e.inside) {o.put(JSONConst.JSON_INSIDE, e.inside);}
			if (e.size>0) {o.put(JSONConst.JSON_SIZE, e.size);}
		} catch(JSONException ex) {
			Log.d(VecGlobals.LOG_TAG, "toJSON endtype", ex);
		}
		return o;
	}
	
	public Tip fromJSON(JSONObject object) {
		Tip e = new Tip();
		try {
			if (object.has(JSONConst.JSON_TYPE)) {e.type=Tip.Type.valueOf(object.getString(JSONConst.JSON_TYPE));} else {e.type=Tip.Type.NONE;}
			if (object.has(JSONConst.JSON_CLOSED)) {e.closed=object.getBoolean(JSONConst.JSON_CLOSED);} else {e.closed=false;}
			if (object.has(JSONConst.JSON_FILLED)) {e.filled=object.getBoolean(JSONConst.JSON_FILLED);} else {e.filled=false;}
			if (object.has(JSONConst.JSON_INSIDE)) {e.inside=object.getBoolean(JSONConst.JSON_INSIDE);} else {e.inside=false;}
			if (object.has(JSONConst.JSON_SIZE)) {e.size=(float)object.getDouble(JSONConst.JSON_SIZE);} else {e.size=0;}
		} catch (JSONException ex) {
			Log.d(VecGlobals.LOG_TAG, "fromJSON EndType", ex);
		}
		return e;
	}
}
