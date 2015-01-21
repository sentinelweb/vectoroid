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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.graphics.PointF;
import android.graphics.RectF;
import android.util.Log;
import co.uk.sentinelweb.views.draw.VecGlobals;
import co.uk.sentinelweb.views.draw.model.TransformOperatorInOut.Axis;
import co.uk.sentinelweb.views.draw.model.TransformOperatorInOut.Trans;
import co.uk.sentinelweb.views.draw.model.UpdateFlags.UpdateType;
import co.uk.sentinelweb.views.draw.model.path.Arc;
import co.uk.sentinelweb.views.draw.model.path.PathData;
import co.uk.sentinelweb.views.draw.render.VecRenderObject;
import co.uk.sentinelweb.views.draw.render.VecRenderer;
import co.uk.sentinelweb.views.draw.util.PointUtil;

public class Stroke extends DrawingElement {
	public static final int DEFAULT_FONT_HEIGHT=20;
	//float density=1;
	public enum Type  {FREE, LINE, TEXT_TTF, TEXT_VECTOR};
	static List<Type> typeLookup = Arrays.asList(Type.values());
	public Type type = Type.FREE;
	public Fill fill;
	public Pen pen;
	
	public ArrayList<PointVec> points = new ArrayList<PointVec>();
	public PointVec currentVec;
	
	public String text = "";
	public float textXScale = 1;
	public String fontName = "";
	public boolean holesEven = false;
	
	RectF _useRect = new RectF();
	RectF _useRect2 = new RectF();
	public Stroke () {
		//density = DispUtil.getDensity(context);
	}
	public Stroke (boolean init) {
		//density = DrawView._density;
		if ( init ){
			currentVec = new PointVec();
			points.add(currentVec);
		}
	}
	public Stroke (Pen pen, Fill fill ) {
		//density = DrawView._density;
		this.pen=pen.duplicate();
		this.fill=fill.duplicate();
		currentVec = new PointVec();
		points.add(currentVec);
		//init();
	}
	
	
	
	public DrawingElement duplicate(boolean shallow) {// shallow invalid here
		Stroke s = new Stroke();
		s.pen=pen.duplicate();
		s.fill=fill.duplicate();
		s.id=id;
		s.type=type;
		s.text=text;
		s.fontName=fontName;
		s.textXScale=textXScale;
		s.holesEven=holesEven;
		for ( int i=points.size()-1; i>=0; i-- ) {
			s.points.add( 0, points.get(i).duplicate() );
		}
		if (currentVec!=null) {
			s.currentVec = s.points.get( points.indexOf( currentVec ) );
		}
		s.locked=locked;
		s.visible=visible;
		return s;
	}
	public void clearPoints() {
		points.clear();
		currentVec=null;
	}
	public void newPoints() {
		PointVec pv = new PointVec();
		currentVec=pv;
		points.add(pv);
	}
	public void addPoints(PointVec pv) {
		currentVec=pv;
		points.add(pv);
	}

	@Override
	public void update(boolean deep, VecRenderer r, UpdateFlags flags) {
		VecRenderObject sro = r.getObject(this);
		if (flags.updateTypes.contains(UpdateType.BOUNDS)) {
			updateBoundsAndCOG(deep);
			if (sro!=null) {
				sro.update(this, UpdateFlags.BOUNDSONLY);
			}
		}
		
		if (flags.updateTypes.contains(UpdateType.PAINT)) {
			if (sro!=null) {
				sro.update(this, UpdateFlags.PAINTONLY);
			}
		}
		
		if (flags.updateTypes.contains(UpdateType.PATH)) {
			if (sro!=null) {
				sro.update(this, UpdateFlags.PATHONLY);
			}
		}

		if (flags.updateTypes.contains(UpdateType.FILL) ) {
			UpdateFlags copy = UpdateFlags.FILLONLY.copy();
			copy.fillTypes.retainAll(flags.fillTypes);
			if (sro!=null) {
				sro.update(this, copy);
			}
		}
		
		if (flags.runListeners && updateListener!=null) {
			updateListener.onAsync(this);
		}
	}

	
	public void updateBoundsAndCOG() {//TODO getting called too many times
		 updateBoundsAndCOG( false ); 
	}
	

	public void updateBoundsAndCOG(boolean deep) {// deep isnt used here
		this.calculatedCOG = new PointF();
		this.calculatedBounds = new RectF(1e8f,1e8f,-1e8f,-1e8f);
		float ctr=0;
		for (int i=points.size()-1;i>=0;i--) {
			ArrayList<PathData> usePoints = points.get(i);
			for (int j=usePoints.size()-1;j>=0;j--) {
				PathData p = usePoints.get(j);
				//TODO need to check arc point .. and bezier/quad ? calculate? or control points ... hmmm.
				PathData lastPathData = j>0?usePoints.get(j-1):null;
				p.computeBounds( calculatedCOG,  calculatedBounds, _useRect, lastPathData);
				/*
				switch (p.type) {
				case POINT:
				case BEZIER:// still ned a calc fn for bezier area
				case QUAD:// still ned a calc fn for quad area
					this.calculatedCOG.x += p.x;
					this.calculatedCOG.y += p.y;
					this.calculatedBounds.top=Math.min(this.calculatedBounds.top, p.y);
					this.calculatedBounds.left=Math.min(this.calculatedBounds.left, p.x);
					this.calculatedBounds.bottom=Math.max(this.calculatedBounds.bottom, p.y);
					this.calculatedBounds.right=Math.max(this.calculatedBounds.right, p.x);
					break;
				case ARC:
					Arc a = (Arc)p;
					if (lastPathData!=null) {// you won't have arc at the first position.
						if (a.computArcFrom( lastPathData)) {//.x, lastPathData.y
							_useRect.set(1e8f,1e8f,-1e8f,-1e8f);//bounds accumulator
							//_useRect2.set( lastPathData.x, lastPathData.y,a.x,a.y);
							for (PathData boundsPoint : a.boundsCheck) {
								PointF testPoint = boundsPoint;
//								if (_useRect2.contains(boundsPoint.x, boundsPoint.y)) {
//									
//								} else {
//									testPoint=a;
//								}
								_useRect.top=Math.min(_useRect.top, testPoint.y);
								_useRect.left=Math.min(_useRect.left, testPoint.x);
								_useRect.bottom=Math.max(_useRect.bottom, testPoint.y);
								_useRect.right=Math.max(_useRect.right, testPoint.x);
//								if (testPoint==a) {break;}
								//Log.d(VecGlobals.LOG_TAG, "updateBoundsAndCOG:arc:bounds:"+PointUtil.tostr(boundsPoint)+": rect:"+PointUtil.tostr(_useRect));
							}
							//Log.d(VecGlobals.LOG_TAG, "updateBoundsAndCOG:arc:bounds:"+PointUtil.tostr(_useRect));
							PointF mid = PointUtil.midpoint(_useRect);
							this.calculatedCOG.x += mid.x;
							this.calculatedCOG.y += mid.y;
							this.calculatedBounds.top=Math.min(this.calculatedBounds.top, _useRect.top);
							this.calculatedBounds.left=Math.min(this.calculatedBounds.left,  _useRect.left);
							this.calculatedBounds.bottom=Math.max(this.calculatedBounds.bottom,_useRect.bottom);
							this.calculatedBounds.right=Math.max(this.calculatedBounds.right, _useRect.right);
						} else {
							// copy of point logic above
							this.calculatedCOG.x += p.x;
							this.calculatedCOG.y += p.y;
							this.calculatedBounds.top=Math.min(this.calculatedBounds.top, p.y);
							this.calculatedBounds.left=Math.min(this.calculatedBounds.left, p.x);
							this.calculatedBounds.bottom=Math.max(this.calculatedBounds.bottom, p.y);
							this.calculatedBounds.right=Math.max(this.calculatedBounds.right, p.x);
						}
					}
					break;
				}
				*/
				ctr++;
			}
		}
		this.calculatedCOG.x/=ctr;
		this.calculatedCOG.y/=ctr;
		
		this.calculatedDim = new PointF(this.calculatedBounds.width(),this.calculatedBounds.height());
		this.calculatedCentre = PointUtil.midpoint(this.calculatedBounds);
	}
	
	public void applyTransform(TransformOperatorInOut t , DrawingElement tgtde) {
		Stroke tgt = (Stroke) tgtde;
		if (fill.type==Fill.Type.GRADIENT && fill._gradient!=null && fill._gradient.data!=null) {
			t.operate(fill._gradient.data.p1,tgt.fill._gradient.data.p1);
			t.operate(fill._gradient.data.p2,tgt.fill._gradient.data.p2);
		}
		if (t.ops.contains(Trans.SCALE) &&  pen.rounding>0) {
			tgt.pen.rounding=pen.rounding*(float)Math.abs(t.scaleValue);
		}
		if (t.ops.contains(Trans.SCALE) &&  pen.scalePen) {
			tgt.pen.strokeWidth=pen.strokeWidth*(float)Math.abs(t.scaleValue);
			tgt.pen.glowWidth=pen.glowWidth*(float)Math.abs(t.scaleValue);
			tgt.pen.glowOffset.x=pen.glowOffset.x*(float)t.scaleValue;
			tgt.pen.glowOffset.y=pen.glowOffset.y*(float)t.scaleValue;
			if (tgt.pen.embEnable) {
				tgt.pen.embRadius=pen.embRadius*(float)t.scaleValue;
			}
		}
		if (t.ops.contains(Trans.SCALE) && t.axis==Axis.X) {
			//if (type==Type.TEXT_TTF && textXScale>0) {
					//if (tgt.textXScale<1) {tgt.textXScale=1;}
			//		tgt.textXScale=(float)t.scaleXValue*(textXScale);
			//}
			if (type==Type.TEXT_TTF ) {
				tgt.textXScale=textXScale*Math.abs((float)t.scaleXValue);
			}
		}
	}
	/**
	 * @return the id
	 */
	public String getId() {
		if (id==null) {id="Stroke_"+hashCode();}
		return id;
	}
	@Override
	public ArrayList<Stroke> getAllStrokes() {
		ArrayList<Stroke> s = new ArrayList<Stroke>();
		s.add(this);
		return s;
	}
	
}
