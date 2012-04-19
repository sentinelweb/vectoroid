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
import java.util.Arrays;
import java.util.List;

import android.util.Log;
import co.uk.sentinelweb.views.draw.DVGlobals;
import co.uk.sentinelweb.views.draw.util.ColorUtil;

public class Fill {
	public enum Type  { NONE, COLOUR_STROKE, COLOUR, BITMAP, GRADIENT };
	public static List<Type> typeLookup = Arrays.asList(Type.values());
	public Type type = Type.NONE;
	//public SoftReference<Bitmap> bitmap = new SoftReference<Bitmap>(null);
	//public SoftReference<Bitmap> bitmapMask = null;//new SoftReference<Bitmap>(null);
	public String bitmapMaskAssetName = null;
	public Asset _bitmapFill;
	public int _bitmapAlpha = 255;
	public Gradient _gradient;
	public int _color;
	//public Shader shader;
	
	public void setColor(int color) {
		this.type = Type.COLOUR;
		this._color = color;
	}
	
	public void setFill(int color) {
		this.type = Type.COLOUR;
		this._color = color;
	}
	
	public void clear() {
		this.type = Type.NONE;
	}
	
	public void setGradient(Gradient g) {
		this.type = Type.GRADIENT;
		this._gradient = g;
	}
	
	public void setAsset(Asset a) {
		this.type = Type.BITMAP;
		_bitmapFill = a;
		//this.bitmap = new SoftReference<Bitmap>(b);
		//Log.d(Globals.LOG_TAG,"setAsset:"+b.getWidth()+"x"+b.getHeight());
	}
	
	public void reset() {
		type = Type.NONE;
		_bitmapFill = null;
		_gradient = null;
		_color=0;
	}
	
	public void copyFill (Fill f, boolean keepApply) {
		Object applyData = null;
		if (this.type==Type.GRADIENT && this._gradient!=null && f.type!=null) {
			applyData=this._gradient.data;
		}
		reset();
		this.type=f.type;
		switch (f.type) {
			case COLOUR:this._color=f._color;break;
			case GRADIENT:
				if (f._gradient!=null) {
					this._gradient=f._gradient.duplicate();
				} else {this._gradient=new Gradient();}
				if (applyData!=null) {
					this._gradient.data=(GradientData)applyData;
				}
				break;
			case BITMAP:
				this._bitmapFill=f._bitmapFill;
				this._bitmapAlpha=f._bitmapAlpha;
		}
	}
	
	public Fill duplicate() {
		Fill newFill= new Fill();
		newFill.type=type;
		if (_gradient!=null) {
			newFill._gradient=_gradient.duplicate();
		}
		newFill._color=_color;
		newFill._bitmapFill=_bitmapFill;
		newFill._bitmapAlpha=_bitmapAlpha;
		return newFill;
	}
	@Override
	public String toString() {
		StringBuffer s = new StringBuffer();
		s.append(type);
		s.append(" : ");
		switch (type){
			case NONE:break;
			case COLOUR_STROKE:break;
			case COLOUR:
				s.append(ColorUtil.toColorString(_color));
				break;
			case BITMAP:
				s.append(_bitmapFill._name);
				break;
			case GRADIENT:
				s.append(_gradient.toString());
				break;
		}
		return s.toString();
	}
	public void log() {
		Log.d(DVGlobals.LOG_TAG,"fill:"+toString());
	}
	
}
