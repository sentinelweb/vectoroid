package co.uk.sentinelweb.views.draw.file.export.json.v3.simple;
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

import android.graphics.PointF;
import co.uk.sentinelweb.views.draw.file.export.json.v3.JSONConst;
import co.uk.sentinelweb.views.draw.file.export.json.v3.JSONUtil;
import co.uk.sentinelweb.views.draw.model.path.PathData;

public class JSONStatic {
	public static JSONObject toJSON(PathData pointF) {
		if (pointF==null) {return null;}
		try {
			JSONObject pointJSON = new JSONObject();
			pointJSON.put(JSONConst.JSON_X, JSONUtil.dp2f(pointF.x));
			pointJSON.put(JSONConst.JSON_Y, JSONUtil.dp2f(pointF.y));
			// TODO save time/pressure?
			return pointJSON;
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}
	public static JSONObject toJSON(PointF pointF) {
		if (pointF==null) {return null;}
		try {
			JSONObject pointJSON = new JSONObject();
			pointJSON.put(JSONConst.JSON_X, JSONUtil.dp2f(pointF.x));
			pointJSON.put(JSONConst.JSON_Y, JSONUtil.dp2f(pointF.y));
			return pointJSON;
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}
	public static PathData fromJSON(JSONObject pointJSON,float dimensionRatio) {
		try {
			return new PathData((float)pointJSON.getDouble(JSONConst.JSON_X),(float)pointJSON.getDouble(JSONConst.JSON_Y));
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static PathData fromJSON(JSONObject pointJSON) {
		return fromJSON( pointJSON,1f);
	}
}
