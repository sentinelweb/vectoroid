package co.uk.sentinelweb.views.draw.render.ag;
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
import android.graphics.Canvas;
import android.graphics.PointF;
import android.graphics.RectF;
import android.util.Log;

import co.uk.sentinelweb.views.draw.VecGlobals;
import co.uk.sentinelweb.views.draw.model.Drawing;
import co.uk.sentinelweb.views.draw.model.DrawingElement;
import co.uk.sentinelweb.views.draw.model.Group;
import co.uk.sentinelweb.views.draw.model.Layer;
import co.uk.sentinelweb.views.draw.model.Stroke;
import co.uk.sentinelweb.views.draw.model.UpdateFlags;
import co.uk.sentinelweb.views.draw.model.ViewPortData;
import co.uk.sentinelweb.views.draw.render.VecRenderObject;
import co.uk.sentinelweb.views.draw.render.VecRenderer;

public class AndGraphicsRenderer extends VecRenderer {
	public enum DrawingMode {DISPLAY,WRITE}
	//public float density=1;
	public boolean _debug= VecGlobals._isDebug && false;
	private ViewPortData _vpd ;
	public StrokesRenderer sr ;
	public boolean _feedBackLevelSnappingToModel=false;
	Canvas canvas;
	protected DrawingMode mode = DrawingMode.DISPLAY;
	
	public AndGraphicsRenderer() {//Context c
		super();//c
		sr = new StrokesRenderer(this);//c,
		//density=DispUtil.getDensity(c);
	}
//	public AndGraphicsRenderer(float density) {//
//		super();//c
//		sr = new StrokesRenderer(this);//c,
//		//this.density=density;
//	}
	@Override
	public void update(DrawingElement de,UpdateFlags flags) {
		VecRenderObject ro = getObject(de);
		ro.update(de, flags);
	}

	@Override
	public void render(DrawingElement de) {
		if (de instanceof Drawing) {
			Drawing drawing = (Drawing)de;
			//float z = _vpd.zoom;
			//_vpd.zoom=1;// wtf is this???
			FillRenderer.drawBackground(this, drawing);
			sr.drawStrokes(canvas,drawing.elements);
			for (Layer l : drawing.layers) {
				if (!l.visible) {continue;}
				sr.drawStrokes(canvas,l.elements);
			}
			//_vpd.zoom=z;// wtf is this???
		} else if (de instanceof Group) {// includes Layer
			Group g = (Group)de;
			sr.drawStrokes(canvas,g.elements);
		} else if (de instanceof Stroke) {
			Stroke s = (Stroke)de;
			sr.drawStroke(canvas,s);
		}
	}
	
	
	@Override
	public void setup() {
		

	}

	/**
	 * @return the c
	 */
	public Canvas getCanvas() {
		return canvas;
	}

	/**
	 * @param c the c to set
	 */
	public void setCanvas(Canvas c) {
		this.canvas = c;
	}

	/**
	 * @return the _vpd
	 */
	public ViewPortData getVpd() {
		return _vpd;
	}

	/**
	 * @param _vpd the _vpd to set
	 */
	public void setVpd(ViewPortData _vpd) {
		this._vpd = _vpd;
	}
	@Override
	public VecRenderObject getObject(DrawingElement de) {
		VecRenderObject ro = renderObjects.get(de);
		if (ro==null) {
			//DebugUtil.logCall("getObject:",new Exception());
			if (de instanceof Stroke) {
				StrokeRenderObject sro = new StrokeRenderObject(this);
				//sro.init((Stroke)de);
				ro=sro;
			} else if (de instanceof Group) {
				ro = new GroupRenderObject(this);
			} else if (de instanceof Layer) {
				ro = new LayerRenderObject(this);
			} else if (de instanceof Drawing){
				ro = new DrawingRenderObject(this);
			}
			renderObjects.put(de, ro);
		}
		return ro;
	}

	@Override
	public void setupViewPort() {
		//canvas.translate(-_vpd.topLeft.x*_vpd.zoom, -_vpd.topLeft.y*_vpd.zoom);// conver to vector op
		if (_debug)Log.d(VecGlobals.LOG_TAG, "renderer zoom:"+_vpd.zoom);
		canvas.scale(_vpd.zoom, _vpd.zoom);
		canvas.translate(-_vpd.topLeft.x,-_vpd.topLeft.y);
	}

	@Override
	public void revertViewPort() {
		//canvas.translate(_vpd.topLeft.x*_vpd.zoom, _vpd.topLeft.y*_vpd.zoom);
		canvas.translate(_vpd.topLeft.x,_vpd.topLeft.y);
		canvas.scale(1/_vpd.zoom, 1/_vpd.zoom);
		
	}

	public void convert(RectF r) {
		r.left = (r.left-_vpd.topLeft.x)*_vpd.zoom;
		r.top = (r.top-_vpd.topLeft.y)*_vpd.zoom;
		r.right = (r.right-_vpd.topLeft.x)*_vpd.zoom;
		r.bottom = (r.bottom-_vpd.topLeft.y)*_vpd.zoom;
	}

	public void convert(PointF p) {
		p.x = (p.x-_vpd.topLeft.x)*_vpd.zoom;
		p.y = (p.y-_vpd.topLeft.y)*_vpd.zoom;
	}
}
