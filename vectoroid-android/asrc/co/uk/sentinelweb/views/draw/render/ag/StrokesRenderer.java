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
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.RectF;
import android.util.Log;
import co.uk.sentinelweb.views.draw.VecGlobals;
import co.uk.sentinelweb.views.draw.model.DrawingElement;
import co.uk.sentinelweb.views.draw.model.Group;
import co.uk.sentinelweb.views.draw.model.Stroke;
import co.uk.sentinelweb.views.draw.render.VecRenderer.Operator;
import co.uk.sentinelweb.views.draw.util.BoundsUtil;
import co.uk.sentinelweb.views.draw.util.PointUtil;

public class StrokesRenderer {
	StrokeRenderer strokeRenderer;
	RectF useRect = new RectF();
	AndGraphicsRenderer r;
	//private ViewPort viewPort;
	public boolean _debug=VecGlobals._isDebug || false;
	
	PointF usePoint = new PointF();
	PointF usePoint2 = new PointF();
	
	public StrokesRenderer( AndGraphicsRenderer r) {//Context c,
		super();
		this.r = r;
		strokeRenderer = new StrokeRenderer(r);//c,
		
	}
	ArrayList<Matrix> matrixStack = new ArrayList<Matrix> ();
	// draw a vector of strokes
	public void drawStrokes(Canvas canvas,ArrayList<DrawingElement> strokes) {//, boolean drawselected, boolean noTest
		for (int i=0;i<strokes.size();i++) {
			DrawingElement de = strokes.get(i);
			if (!de.visible) {continue;}
			
			Operator trans = r.animations.get(de);
			//usePoint.set(de.calculatedCentre.x*r.getVpd().zoom,de.calculatedCentre.y/r.getVpd().zoom);
			
			if (trans!=null) {
				/*
				if (trans.rotation!=null) {
					if (trans.translate!=null) {
						PointUtil.addVector(usePoint, usePoint2, trans.translate);
						canvas.rotate(trans.rotation,usePoint2.x,usePoint2.y);
					} else {
						canvas.rotate(trans.rotation,usePoint.x,usePoint.y);
					}
				}
				*/
				if (trans.translate!=null) {
					if (trans.scale!=null) {
						canvas.translate(
								(trans.translate.x/1),
								(trans.translate.y/1)
							);
					} else {
						canvas.translate(
								(trans.translate.x),
								(trans.translate.y)
							);
					}
				}
				if (trans.scale!=null) {//
					trans.scale.set(Math.abs(trans.scale.x),Math.abs(trans.scale.y));
					getScaleOffset(trans);
					canvas.scale(trans.scale.x,trans.scale.y,usePoint.x,usePoint.y);//,usePoint.x+trans.translate.x,usePoint.y+trans.translate.y
				}
				if (trans.m!=null) {
					//canvas.setMatrix(trans.m);
					Matrix m = canvas.getMatrix();
					matrixStack.add(new Matrix(m));
					m.postConcat(trans.m);
					canvas.setMatrix(m);
				}
			}
			
			if (de instanceof Stroke) {
				drawStroke(canvas, (Stroke) de );
			} else if (de instanceof Group) {
				Group g = (Group)de;
				//for (Stroke s : g.getAllStrokes() ) {
				//	drawStroke(canvas, s );
				//}
				drawStrokes( canvas,g.elements);
			} 
			if (trans!=null) {
				if (trans.m!=null) {
					//if (trans.im==null) {
					//	trans.im=new Matrix();
					//	trans.m.invert(trans.im);
					//}
					Matrix m = matrixStack.remove(matrixStack.size()-1);
					canvas.setMatrix(m);
					
				}
				if (trans.scale!=null) {
					getScaleOffset(trans);
					canvas.scale(1/trans.scale.x,1/trans.scale.y,-usePoint.x,-usePoint.y);//,-usePoint.x-trans.translate.x,-usePoint.y-trans.translate.y
				}
				if (trans.translate!=null) {
					if (trans.scale!=null) {
						canvas.translate(
							-(trans.translate.x)*1,
							-(trans.translate.y)*1
						);
					}else {
						canvas.translate(
							-(trans.translate.x),
							-(trans.translate.y)
						);
					}
					//canvas.translate(-trans.translate.x*1/trans.scale.x,-trans.translate.y*1/trans.scale.y);
				}
				/*
				if (trans.rotation!=null) {
					if (trans.translate!=null) {
						PointUtil.addVector(usePoint, usePoint2, trans.translate);
						canvas.rotate(-trans.rotation,usePoint2.x,usePoint2.y);
					} else {
						canvas.rotate(-trans.rotation,usePoint.x,usePoint.y);
					}
				}
				*/
			}
		}
	}

	public void getScaleOffset(Operator trans) {
		// needs the right calculation for 
		usePoint.set(0,0);// 1024/2-1024/2*trans.scale.x,600/2-600/2*trans.scale.y
		// usePoint.set(0,0);
	}
	// draw a single stroke
	public void drawStroke(Canvas canvas, Stroke stroke) {//, boolean drawselected, boolean noTest
		if (stroke.points.size()>0) {
			useRect.set(r.getVpd().zoomCullingRectF);
			if (_debug) {Log.d(VecGlobals.LOG_TAG, "viewPort:"+PointUtil.tostr(useRect));}
			//TODO check this works, looks like bounds test is failing at high zoom levels >400
			//TODO we need to expand the canvas clipBounds here (somehow?) for object with TopLefts in the -ve region
			boolean checkBoundsIntersect = BoundsUtil.checkBoundsIntersect(useRect, stroke.calculatedBounds, Math.max(stroke.pen.strokeWidth, stroke.pen.glowWidth) );
			if (_debug) {Log.d(VecGlobals.LOG_TAG, "checkBoundsIntersect:"+checkBoundsIntersect+":"+PointUtil.tostr(useRect)+" :: "+":"+PointUtil.tostr(stroke.calculatedBounds));}
			if (checkBoundsIntersect) {
				strokeRenderer.render(canvas, stroke);
				if (_debug) {Log.d(VecGlobals.LOG_TAG, "inbounds:");}
			} else {
				if (_debug) {Log.d(VecGlobals.LOG_TAG, "outbounds:");}
			}
		}
	}

	/**
	 * @return the strokeRenderer
	 */
	public StrokeRenderer getStrokeRenderer() {
		return strokeRenderer;
	}
	
	
}
