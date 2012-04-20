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

import android.graphics.Color;
import android.util.Log;
import co.uk.sentinelweb.views.draw.VecGlobals;
import co.uk.sentinelweb.views.draw.file.SaveFile;
import co.uk.sentinelweb.views.draw.file.export.json.JSONConst;
import co.uk.sentinelweb.views.draw.file.export.json.JSONParent;
import co.uk.sentinelweb.views.draw.model.Fill;

public class JSONFill extends JSONParent{
	
	public JSONGradient jsg ;
	
	public JSONFill(SaveFile _saveFile) {
		super(_saveFile);
		jsg = new JSONGradient(_saveFile);
	}
	
	public JSONObject toJSON(Fill f) {
		JSONObject o = getJSONObject();
		try {
			o.put( JSONConst.JSON_TYPE, f.type );
			switch ( f.type ) { 
				case COLOUR: o.put(JSONConst.JSON_COLOUR, f._color);break;
				case GRADIENT: o.put(JSONConst.JSON_GRADIENT, jsg.toJSON(f._gradient));break;
				case BITMAP: 
					if (f._bitmapFill!=null) {o.put(JSONConst.JSON_ASSET, _saveFile.getAssetManager().toJSON(f._bitmapFill));}
					o.put(JSONConst.JSON_BMP_ALPHA, f._bitmapAlpha);
					break;
			}
		} catch(JSONException e) {
			Log.d(VecGlobals.LOG_TAG, "JSON to fill", e);
		}
		return o;
	}

	public Fill fromJSON(JSONObject o) {
		Fill f = new Fill();
		try {
			f.type=Fill.Type.valueOf(o.getString(JSONConst.JSON_TYPE));
			switch ( f.type ) { 
				case COLOUR: 
					if (o.has(JSONConst.JSON_COLOUR)) {
						f._color=o.getInt(JSONConst.JSON_COLOUR);
					} else if (o.has(JSONConst.JSON_WEBCOLOUR)) {
						f._color=Color.parseColor(o.getString(JSONConst.JSON_WEBCOLOUR));
					}
					break;
				case GRADIENT: f._gradient=jsg.fromJSON(o.getJSONObject(JSONConst.JSON_GRADIENT));break;
				case BITMAP: 
					if (o.has(JSONConst.JSON_ASSET)) {
						JSONObject jsonObject = o.getJSONObject(JSONConst.JSON_ASSET);
						f._bitmapFill=_saveFile.getAssetManager().fromJSON(jsonObject);
						
					}
					if (o.has(JSONConst.JSON_BMP_ALPHA)) {f._bitmapAlpha=o.getInt(JSONConst.JSON_BMP_ALPHA);}
				break;
			}
		} catch (JSONException e) {
			Log.d(VecGlobals.LOG_TAG, "JSON from fill", e);
		}
		return f;
	}
	
	public void setReUse(boolean reuse) {
		reUseJSONObjects = reuse;
		jsg.reUseJSONObjects = reuse;
	}
}
