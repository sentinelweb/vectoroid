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
import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.PointF;
import android.graphics.PorterDuff;
import android.graphics.RectF;
import android.util.Log;
import co.uk.sentinelweb.views.draw.DVGlobals;
import co.uk.sentinelweb.views.draw.model.DrawingElement;
import co.uk.sentinelweb.views.draw.model.Group;
import co.uk.sentinelweb.views.draw.model.Stroke;
import co.uk.sentinelweb.views.draw.model.UpdateFlags;
import co.uk.sentinelweb.views.draw.model.ViewPortData;
import co.uk.sentinelweb.views.draw.util.DispUtil;

public class StrokeIconRenderer {
	private float density = 1f;
	Paint paint = new Paint();
	Paint fill = new Paint();
	AndGraphicsRenderer _renderer;
	//Canvas _rendererCanvas;
	PointF _tl = new PointF();

	public StrokeIconRenderer(Context c) {
		super();
		this.density = DispUtil.getDensity(c);
		paint.setStyle(Style.STROKE);
		fill.setStyle(Style.FILL);
		_renderer=new AndGraphicsRenderer(c);
		//_rendererCanvas=new Canvas();
		//_renderer.setCanvas(_rendererCanvas);
	}
	/*
	public  Bitmap makeIcon(DrawingElement de,int size,Bitmap b) {
		if (de instanceof Group) {
			return makeIcon((Group)de, size,b);
		} else {
			return makeIcon((Stroke)de, size,b);
		}
	}
	*/
	public  Bitmap makeIcon(DrawingElement de,int size,Bitmap b) {
		
		PointF sizeIcon = new PointF();
		PointF sizeStroke = new PointF();
		try {
			//int bgColor = Color.TRANSPARENT;
			//boolean isgroup = false;
			//if (de instanceof Stroke) {
			//	int c = ((Stroke)de).pen.strokeColour;
			//	if (Color.red(c)<32 && Color.green(c)<32 && Color.blue(c)<32) { bgColor = Color.WHITE;}
			//} else { isgroup = true;}
			ViewPortData vpd = ViewPortData.getFragmentViewPort(de);
			getSizes(size, de.calculatedBounds, sizeIcon, sizeStroke);
			
			float scaling = Math.max(sizeIcon.x/sizeStroke.x, sizeIcon.y/sizeStroke.y);
			float aspect = sizeStroke.x/sizeStroke.y;
			_tl.set(0, 0);
			float offset = (size/scaling - Math.min(sizeStroke.x, sizeStroke.y))/2;
			if (aspect>1) {
				_tl.y=offset;
			} else {
				_tl.x=offset;
			}
			vpd.zoom=scaling;//1
			//DebugUtil.logCall("---------------------makeIcon: cb:"+PointUtil.tostr(de.calculatedBounds)+": sizeIcon:"+
			//		PointUtil.tostr(sizeIcon)+": sizeStroke:"+PointUtil.tostr(sizeStroke)+":"+scaling+" making: vpd tl:"+PointUtil.tostr(vpd.topLeft)+"\n",
			//		new Exception(),10);
			if (b==null ) {
				b = Bitmap.createBitmap(size, size, Bitmap.Config.RGB_565);
			}
			//_rendererCanvas.setBitmap(b);
			Canvas c = new Canvas();
			c.setBitmap(b);
			_renderer.setCanvas(c);
			_renderer.setVpd(vpd);
			// creates/sets the render objects

			de.update(true, _renderer, UpdateFlags.ALL_NOFILLANDLISTENERS);// modify to not do fill
			
			ArrayList<Stroke> strokes = new ArrayList<Stroke>();
			if (de instanceof Stroke) {
				strokes.add((Stroke)de);
			} else if (de instanceof Group) {
				strokes.addAll(((Group)de).getStrokes()); 
			}
			
			for (Stroke s : strokes) {
				StrokeRenderObject sro =(StrokeRenderObject) _renderer.getObject(s);
				sro.fgInner.setStrokeWidth(1*density/scaling);
				sro.fgInner.setAlpha(255);
				//if (isgroup) {
					sro.fgInner.setColor(Color.WHITE);
					sro.fgInner.setStyle(Style.STROKE);
				//}
				sro.fgOnly=true;
			}
			//_rendererCanvas.drawColor(bgColor);
			//c.drawColor(bgColor);
			c.drawColor(0, PorterDuff.Mode.CLEAR );
			_renderer.setupViewPort();
			//_rendererCanvas.translate(_tl.x, _tl.y);
			c.translate(_tl.x, _tl.y);
			_renderer.render(de);
			//_rendererCanvas.translate(-_tl.x, -_tl.y);
			c.translate(-_tl.x, -_tl.y);
			_renderer.revertViewPort();
			//Log.d(DVGlobals.LOG_TAG,"makeIcon:"+sizeIcon.x+"x"+sizeIcon.x+": made");//,new Exception("trace")
		} catch (OutOfMemoryError e) {
			Log.d(DVGlobals.LOG_TAG,"makeIcon: OME ");//,new Exception("trace")
			return null;
		}
		
		 
        return b;
	}


	private void getSizes(int size, RectF calculatedBounds, PointF sizeIcon, PointF sizeStroke) {
		sizeStroke.x = calculatedBounds.width();
		sizeStroke.y = calculatedBounds.height();
        if (Float.isNaN(sizeStroke.x)) {sizeStroke.x=1;}
        if (Float.isNaN(sizeStroke.y)) {sizeStroke.y=1;}
        float aspect = sizeStroke.x/sizeStroke.y;
        float sizeh=size;float sizev=size;
        if (aspect<1) {
        	sizeh*=aspect;
        } else {
        	sizev/=aspect;
        }
        if (sizev<=1 || Float.isNaN(sizev)) {sizev=3;}
        if (sizeh<=1 || Float.isNaN(sizeh)) {sizeh=3;}
        //Log.d(DVGlobals.LOG_TAG,"makeIcon:"+sizeh+"x"+sizev+":"+aspect);//,new Exception("trace")
        sizeh*=density;
        sizev*=density;
        sizeIcon.set(sizeh, sizev);
	}


	/**
	 * @return the _renderer
	 */
	public AndGraphicsRenderer getRenderer() {
		return _renderer;
	}

	/**
	 * @param _renderer the _renderer to set - only do this if you know what your doing.
	 */
	public void setRenderer(AndGraphicsRenderer renderer) {
		this._renderer = renderer;
	}
	
	
}
