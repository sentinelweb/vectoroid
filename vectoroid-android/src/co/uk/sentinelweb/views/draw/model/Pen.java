package co.uk.sentinelweb.views.draw.model;
/*
Vectoroid API for Android
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
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Cap;
import android.graphics.Paint.Join;
import android.graphics.PointF;
import android.util.Log;
import co.uk.sentinelweb.views.draw.VecGlobals;
import co.uk.sentinelweb.views.draw.model.StrokeDecoration.Tip;

public class Pen {
	//Paint paintFg = new Paint();
	//Paint paintBg = new Paint();
	public enum PenField {
		STROKE_WIDTH,STROKE_COLOUR,
		GLOW_WIDTH,GLOW_COLOUR,GLOW_OFFSET,
		ROUNDING,
		EMBOSS,EMBOSS_AMBIENT,EMBOSS_SPECULAR,EMBOSS_RADIUS,
		CAP,JOIN,
		STARTTIP,ENDTIP,
		BREAKTYPE,
		SCALE_PEN
	};//ALPHA,
	public float rounding = 0;
	public boolean embEnable = false;
	public float embAmbient = 0;
	public float embSpecular = 0;
	public float embRadius = 0;
	
	//public int alpha = 255;
	public int strokeColour = Color.BLACK;
	public float strokeWidth = 0;
	
	public int glowColour = Color.RED;
	public float glowWidth = 0;
	public PointF glowOffset = new PointF(0,0);
	
	public boolean scalePen = true;
	
	public Paint.Cap cap = Paint.Cap.ROUND;
	public Paint.Join join = Paint.Join.ROUND;
	
	public StrokeDecoration.Tip startTip = null;
	public StrokeDecoration.Tip endTip =  null;
	

	public StrokeDecoration.BreakType breakType =  StrokeDecoration.BreakType.SOLID;
	
	public void setField(PenField field,Object o) {
		switch (field) {
			case STROKE_WIDTH:strokeWidth =  (Float)o;
				break;
			case STROKE_COLOUR:strokeColour = (Integer)o;
				break;
			case GLOW_WIDTH:glowWidth =  (Float)o;
				break;
			case GLOW_COLOUR:glowColour = (Integer)o;
				break;
			//case ALPHA:alpha = Math.round((Float)o);
			//	break;
			case ROUNDING: rounding= Math.round((Float)o);
				break;
			case EMBOSS:embEnable = (Boolean)o;
				break;
			case EMBOSS_AMBIENT:embAmbient = (Float)o;
				break;
			case SCALE_PEN:scalePen = (Boolean)o;
				break;
			case EMBOSS_SPECULAR:embSpecular =  (Float)o;
				break;
			case EMBOSS_RADIUS:embRadius =  (Float)o;
				break;
			case CAP:cap =  (Cap)o;
				break;
			case JOIN:join =  (Join) o;
				break;
			case STARTTIP:startTip =  (Tip) o;
				break;
			case ENDTIP:endTip =  (Tip) o;
				break;
			case BREAKTYPE:breakType =  (StrokeDecoration.BreakType) o;
				break;
		}
	}
	
	public Pen duplicate() {
		Pen newPen = new Pen();
		newPen.copyFrom(this);
		return newPen;
	}
	
	public void copyFrom(Pen newPen) {
		rounding = newPen.rounding;
		embEnable = newPen.embEnable;
		embAmbient = newPen.embAmbient;
		embSpecular = newPen.embSpecular;
		embRadius =newPen.embRadius;
		//alpha = alpha;
		strokeWidth = newPen.strokeWidth;
		glowWidth = newPen.glowWidth;
		strokeColour = newPen.strokeColour;
		glowColour = newPen.glowColour;
		glowOffset.set(newPen.glowOffset);
		scalePen = newPen.scalePen;
		cap=newPen.cap;
		join=newPen.join;
		startTip=newPen.startTip!=null?newPen.startTip.duplicate():null;
		endTip=newPen.endTip!=null?newPen.endTip.duplicate():null;
		breakType=newPen.breakType;
	}
	
	public void log() {
		Log.d(VecGlobals.LOG_TAG,"pen:sw:"+strokeWidth+" sc:"+strokeColour+" gw:"+glowWidth+" gc:"+glowColour);

	}
}
