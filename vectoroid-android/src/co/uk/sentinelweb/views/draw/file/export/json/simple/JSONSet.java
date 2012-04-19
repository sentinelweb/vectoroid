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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;
import co.uk.sentinelweb.views.draw.DVGlobals;
import co.uk.sentinelweb.views.draw.file.SaveFile;
import co.uk.sentinelweb.views.draw.file.export.json.JSONParent;
import co.uk.sentinelweb.views.draw.model.DrawingSet;

public class JSONSet extends JSONParent {
	SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyy hh:mm:ss");
	public static float version = 1;
	private static final String JSON_DRAWIDS = "itemids";
	private static final String JSON_ID = "id";
	private static final String JSON_NAME = "name";
	private static final String JSON_TEMPLATEID = "templateID";
	private static final String JSON_NOTES = "notes";
	private static final String JSON_DATECREATED = "dateCreated";
	private static final String JSON_DATEMODIFIED = "dateModified";
	private static final String JSON_DEFAULT_TMPL = "defaultTemplate";
	private static final String JSON_VERSION = "version";
	
	JSONTemplate jst = null;
	
	public JSONSet(SaveFile _saveFile) {
		super(_saveFile);
		jst = new JSONTemplate(_saveFile);
	}
	
	public JSONObject toJSON(DrawingSet ds) {
		try {
			JSONObject setJSON = new JSONObject();
			setJSON.put(JSON_ID, ds.getId());
			setJSON.put(JSON_VERSION, version);
			setJSON.put(JSON_NAME, ds.getName());
			setJSON.put(JSON_TEMPLATEID, ds.getTemplateID());
			setJSON.put(JSON_NOTES, ds.getNotes());
			setJSON.put(JSON_DATECREATED, sdf.format(new Date(ds.getDateCreated())));
			setJSON.put(JSON_DATEMODIFIED, sdf.format(new Date(ds.getDateModified())));
			setJSON.put(JSON_DEFAULT_TMPL, jst.toJSONTmpl(ds.getDefaultTemplate()));
			JSONArray jsids = new JSONArray();
			for (String id : ds.getDrawingIDs()) {
				jsids.put(id);
			}
			setJSON.put(JSON_DRAWIDS, jsids);
			return setJSON;
		} catch (JSONException e) {
			Log.d(DVGlobals.LOG_TAG, "JSON DrawingSet to ", e);
		}
		return null;
	}
	
	public DrawingSet fromJSON(JSONObject o) {
		DrawingSet ds = new DrawingSet();
		try {
			float thisVersion = version;
			if (o.has(JSON_VERSION)) {thisVersion=(float)o.getDouble(JSON_VERSION);}
			if (o.has(JSON_ID)) {ds.setId(o.getString(JSON_ID));}
			if (o.has(JSON_NAME)) {ds.setName(o.getString(JSON_NAME));}
			if (o.has(JSON_TEMPLATEID)) {ds.setTemplateID(o.getString(JSON_TEMPLATEID));}
			if (o.has(JSON_NOTES)) {ds.setNotes(o.getString(JSON_NOTES));}
			if (o.has(JSON_DATECREATED)) {
				try {
					ds.setDateCreated(sdf.parse(o.getString(JSON_DATECREATED)).getTime());
				} catch (Exception e) {
					ds.setDateCreated(System.currentTimeMillis());
				}
			}
			if (o.has(JSON_DATEMODIFIED)) {
				try {
					ds.setDateModified(sdf.parse(o.getString(JSON_DATEMODIFIED)).getTime());
				} catch (Exception e) {
					ds.setDateModified(System.currentTimeMillis());
				}
			}
			if (o.has(JSON_DEFAULT_TMPL)) {ds.setDefaultTemplate(jst.fromJSONTmpl(o.getJSONObject(JSON_DEFAULT_TMPL)));}
			if (o.has(JSON_DRAWIDS)) {
				ArrayList<String> ids = new ArrayList<String>();
				JSONArray jsids = o.getJSONArray(JSON_DRAWIDS);
				for (int i=0;i<jsids.length();i++) {
					ids.add(jsids.getString(i));
				}
				ds.setDrawingIDs(ids);
			}
		} catch (JSONException e) {
			Log.d(DVGlobals.LOG_TAG, "JSON DrawingSet from ", e);
		}
		return ds;
	}
}
