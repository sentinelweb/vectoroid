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

import android.graphics.Paint.Cap;
import android.graphics.Paint.Join;
import android.util.Log;
import co.uk.sentinelweb.views.draw.DVGlobals;
import co.uk.sentinelweb.views.draw.file.SaveFile;
import co.uk.sentinelweb.views.draw.file.export.json.JSONConst;
import co.uk.sentinelweb.views.draw.file.export.json.JSONParent;
import co.uk.sentinelweb.views.draw.model.Pen;
import co.uk.sentinelweb.views.draw.model.StrokeDecoration.BreakType;
import co.uk.sentinelweb.views.draw.model.StrokeDecoration.Tip.Type;

public class JSONPen extends JSONParent {
	private JSONTip je;
	public JSONPen(SaveFile _saveFile) {
		super(_saveFile);
		je=new JSONTip(_saveFile);
	}

	public JSONObject toJSON(Pen pen) {
		JSONObject o = getJSONObject();
		try {
			if (pen.strokeWidth>0) {o.put(JSONConst.JSON_STR_WID, pen.strokeWidth);}
			if (pen.glowWidth>0) {o.put(JSONConst.JSON_GLO_WID, pen.glowWidth);}
			if (pen.strokeColour!=0) {o.put(JSONConst.JSON_STR_COL, pen.strokeColour);}
			if (pen.glowColour!=0) {o.put(JSONConst.JSON_GLO_COL, pen.glowColour);}
			if (pen.scalePen) {o.put(JSONConst.JSON_SCALE_PEN, pen.scalePen);}
			//o.put(JSON_ALPHA, pen.alpha);
			if (pen.rounding>0) {o.put(JSONConst.JSON_ROUNDING, pen.rounding);}
			if (pen.cap!=Cap.ROUND) {o.put(JSONConst.JSON_CAP, pen.cap);}
			if (pen.join!=Join.ROUND) {o.put(JSONConst.JSON_JOIN, pen.join);}
			if (pen.embEnable) {o.put(JSONConst.JSON_EMB_ENABLE, pen.embEnable);}
			if (pen.embAmbient>0) {o.put(JSONConst.JSON_EMB_AMBIENT, pen.embAmbient);}
			if (pen.embRadius>0) {o.put(JSONConst.JSON_EMB_RADIUS, pen.embRadius);}
			if (pen.embSpecular>0) {o.put(JSONConst.JSON_EMB_SPECULAR, pen.embSpecular);}
			if (pen.startTip!=null && pen.startTip.type!=Type.NONE) {o.put(JSONConst.JSON_START_TIP, je.toJSON(pen.startTip));}
			if (pen.endTip!=null && pen.endTip.type!=Type.NONE) {o.put(JSONConst.JSON_END_TIP, je.toJSON(pen.endTip));}
			if (pen.breakType!=null) {o.put(JSONConst.JSON_BREAK_TYPE, pen.breakType.toString());}
			
		} catch(JSONException e) {
			Log.d(DVGlobals.LOG_TAG, "toJSON pen", e);
		}
		return o;
	}

	public Pen fromJSON(JSONObject object) {
		Pen pen = new Pen();
		try {
			if (object.has(JSONConst.JSON_STR_WID)) {pen.strokeWidth=(float)object.getDouble(JSONConst.JSON_STR_WID);} else {pen.strokeWidth=0;}
			if (object.has(JSONConst.JSON_STR_COL)) {pen.strokeColour=object.getInt(JSONConst.JSON_STR_COL);} else {pen.strokeColour=0;}
			if (object.has(JSONConst.JSON_GLO_WID)) {pen.glowWidth=(float)object.getDouble(JSONConst.JSON_GLO_WID);} else {pen.glowWidth=0;}
			if (object.has(JSONConst.JSON_GLO_COL)) {pen.glowColour=object.getInt(JSONConst.JSON_GLO_COL);} else {pen.glowColour=0;}
			//pen.alpha=object.getInt(JSON_ALPHA);
			if (object.has(JSONConst.JSON_ROUNDING)) {pen.rounding=(float)object.getDouble(JSONConst.JSON_ROUNDING);}else {pen.rounding=0;}
			if (object.has(JSONConst.JSON_SCALE_PEN)) {pen.scalePen=object.getBoolean(JSONConst.JSON_SCALE_PEN);} else {pen.scalePen=false;}
			if (object.has(JSONConst.JSON_EMB_ENABLE)) {pen.embEnable=object.getBoolean(JSONConst.JSON_EMB_ENABLE);}else {pen.embEnable=false;}
			if (object.has(JSONConst.JSON_EMB_AMBIENT)) {pen.embAmbient=(float)object.getDouble(JSONConst.JSON_EMB_AMBIENT);}else {pen.embAmbient=0;}
			if (object.has(JSONConst.JSON_EMB_RADIUS)) {pen.embRadius=(float)object.getDouble(JSONConst.JSON_EMB_RADIUS);}else {pen.embRadius=0;}
			if (object.has(JSONConst.JSON_EMB_SPECULAR)) {pen.embSpecular=(float)object.getDouble(JSONConst.JSON_EMB_SPECULAR);} else {pen.embSpecular=0;}
			if (object.has(JSONConst.JSON_CAP)) {try {pen.cap=Cap.valueOf(object.getString(JSONConst.JSON_CAP));}catch(Exception e){pen.cap=Cap.ROUND;}}else {pen.cap=Cap.ROUND;}
			if (object.has(JSONConst.JSON_JOIN)) {try {pen.join=Join.valueOf(object.getString(JSONConst.JSON_JOIN));}catch(Exception e){}}else {pen.join=Join.ROUND;}
			if (object.has(JSONConst.JSON_START_TIP)) {pen.startTip=je.fromJSON(object.getJSONObject(JSONConst.JSON_START_TIP));}
			if (object.has(JSONConst.JSON_END_TIP)) {pen.endTip=je.fromJSON(object.getJSONObject(JSONConst.JSON_END_TIP));}
			if (object.has(JSONConst.JSON_BREAK_TYPE)) {try {pen.breakType=BreakType.valueOf(object.getString(JSONConst.JSON_BREAK_TYPE));}catch(Exception e){}}
		} catch (JSONException e) {
			Log.d(DVGlobals.LOG_TAG, "fromJSON pen", e);
		}
		return pen;
	}

}
