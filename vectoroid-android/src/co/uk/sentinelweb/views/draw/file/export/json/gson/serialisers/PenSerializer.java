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

import android.graphics.Paint.Cap;
import android.graphics.Paint.Join;
import android.graphics.PointF;
import android.util.Log;
import co.uk.sentinelweb.views.draw.VecGlobals;
import co.uk.sentinelweb.views.draw.file.SaveFile;
import co.uk.sentinelweb.views.draw.file.export.json.JSONConst;
import co.uk.sentinelweb.views.draw.model.Pen;
import co.uk.sentinelweb.views.draw.model.StrokeDecoration;
import co.uk.sentinelweb.views.draw.model.StrokeDecoration.Tip;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class PenSerializer implements JsonSerializer<Pen> {
	SaveFile _saveFile = null;
	Gson gson;
	public PenSerializer(SaveFile _saveFile) {
		super();
		this._saveFile = _saveFile;
	}
	
	@Override
	public JsonElement serialize(Pen pen, Type arg1, JsonSerializationContext cxt) {
		JsonObject o = new JsonObject();
		o.addProperty(JSONConst.JSON_STR_WID, pen.strokeWidth);
		o.addProperty(JSONConst.JSON_GLO_WID, pen.glowWidth);
		o.addProperty(JSONConst.JSON_STR_COL, pen.strokeColour);
		o.addProperty(JSONConst.JSON_GLO_COL, pen.glowColour);
		o.addProperty(JSONConst.JSON_SCALE_PEN, pen.scalePen);
		if (pen.rounding>0) {o.addProperty(JSONConst.JSON_ROUNDING, pen.rounding);}
		if (pen.cap!=Cap.ROUND) {o.addProperty(JSONConst.JSON_CAP, pen.cap.toString());}
		if (pen.join!=Join.ROUND) {o.addProperty(JSONConst.JSON_JOIN, pen.join.toString());}
		if (pen.embEnable) {o.addProperty(JSONConst.JSON_EMB_ENABLE, pen.embEnable);}
		if (pen.embAmbient>0) {o.addProperty(JSONConst.JSON_EMB_AMBIENT, pen.embAmbient);}
		if (pen.embRadius>0) {o.addProperty(JSONConst.JSON_EMB_RADIUS, pen.embRadius);}
		if (pen.embSpecular>0) {o.addProperty(JSONConst.JSON_EMB_SPECULAR, pen.embSpecular);}
		if (pen.startTip!=null && pen.startTip.type!=StrokeDecoration.Tip.Type.NONE) {o.add(JSONConst.JSON_START_TIP, tip(pen.startTip));}
		if (pen.endTip!=null && pen.endTip.type!=StrokeDecoration.Tip.Type.NONE) {o.add(JSONConst.JSON_END_TIP, tip(pen.endTip));}
		if (pen.breakType!=null) {o.addProperty(JSONConst.JSON_BREAK_TYPE, pen.breakType.toString());}
		if (pen.glowOffset!=null && (pen.glowOffset.x!=0 || pen.glowOffset.y!=0)) {
			o.add(JSONConst.JSON_GLO_OFFSET, gson.toJsonTree(pen.glowOffset, PointF.class));
		}
		return o;
	}
	public JsonObject tip(Tip e) {
		JsonObject o = new JsonObject();
		try {
			if (e.type!=Tip.Type.NONE) {o.addProperty(JSONConst.JSON_TYPE, e.type.toString());}
			if (e.closed) {o.addProperty(JSONConst.JSON_CLOSED, e.closed);}
			if (e.filled) {o.addProperty(JSONConst.JSON_FILLED, e.filled);}
			if (e.inside) {o.addProperty(JSONConst.JSON_INSIDE, e.inside);}
			if (e.size>0) {o.addProperty(JSONConst.JSON_SIZE, e.size);}
		} catch(Exception ex) {
			Log.d(VecGlobals.LOG_TAG, "toJSON endtype", ex);
		}
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
