package co.uk.sentinelweb.views.draw.util;
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

import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;
import co.uk.sentinelweb.views.draw.controller.TransformController;
import co.uk.sentinelweb.views.draw.model.DrawingElement;
import co.uk.sentinelweb.views.draw.model.PointVec;
import co.uk.sentinelweb.views.draw.model.Stroke;
import co.uk.sentinelweb.views.draw.model.Stroke.Type;
import co.uk.sentinelweb.views.draw.model.TransformOperatorInOut;
import co.uk.sentinelweb.views.draw.model.path.Arc;
import co.uk.sentinelweb.views.draw.model.path.Bezier;
import co.uk.sentinelweb.views.draw.model.path.PathData;
import co.uk.sentinelweb.views.draw.model.path.Quartic;
import co.uk.sentinelweb.views.draw.render.VecRenderer;

public class StrokeUtil {
	private static int resolution = 200;
	/** This assumes the stroke is bounded [-1,-1,1,1] (centerd on origin)
	 * @param s Stroke
	 * @param r Scaling rect
	 */
	public static void fitStroke(ArrayList<PathData> curVec,RectF r) {
		
		//PointVec curVec = s.points.get(0);
		
		float left = Math.min(r.right,r.left);
		float right = Math.max(r.right,r.left);
		float top = Math.min(r.bottom,r.top);
		float bottom = Math.max(r.bottom,r.top);
		float midx = left+(right-left)/2;
		float midy = top+(bottom-top)/2;

		PointF circleCentre = new PointF(midx,midy);
		
		double matrix[][] = new double[2][2];
		matrix[0][0] = (midx-left);
		matrix[0][1] = 0;
		matrix[1][0] = 0;
		matrix[1][1] = (midy-top);
		
		for (int i=0;i<curVec.size();i++) {
			PointF pin = curVec.get(i);
			PointUtil.mulMatrix(pin, pin, matrix);// scale 
			PointUtil.addVector(pin, pin, circleCentre);// translate
		}
	}
	
	public static void centreOnOrigin(Stroke s) {
		s.updateBoundsAndCOG();
		PointF trans = new PointF(s.calculatedCentre.x/2,s.calculatedCentre.y/2);
		for (PointVec curVec:s.points) {
			for (int i=0;i<curVec.size();i++) {
				PointUtil.addVector(curVec.get(i), curVec.get(i), trans);// translate
			}
		}
		s.updateBoundsAndCOG();
	}
	
	public static void translate(DrawingElement de, PointF p,VecRenderer agr) {
		TransformOperatorInOut t = TransformOperatorInOut.makeTranslate(p);
		TransformController.transform(de, de, t, agr);
	}
	
	public static void scale(DrawingElement de, float sc,VecRenderer agr) {
		TransformOperatorInOut t = TransformOperatorInOut.makeScale(sc,de.calculatedBounds);
		TransformController.transform(de, de, t, agr);
	}
	public static void scale(DrawingElement de, float sc,RectF bounds, VecRenderer agr) {
		TransformOperatorInOut t = TransformOperatorInOut.makeScale(sc,bounds);
		TransformController.transform(de, de, t, agr);
	}
	public static void scale(DrawingElement de, float scx,float scy,RectF bounds, VecRenderer agr) {
		TransformOperatorInOut t = TransformOperatorInOut.makeScale(new PointF(scx,scy),bounds);
		TransformController.transform(de, de, t, agr);
	}
	public static void scaleAndTrans(DrawingElement de, float sc,PointF p,VecRenderer agr) {
		TransformOperatorInOut t = TransformOperatorInOut.makeScaleAndTranslate(p, sc,de.calculatedBounds);
		TransformController.transform(de, de, t, agr);
	}
	/** Scale a stroke
	 * @param s Stroke
	 * @param scale Scaling factor
	 */
	public static void scaleStroke(Stroke s,float scale) {
		double matrix[][] = new double[2][2];
		matrix[0][0] = scale;
		matrix[0][1] = 0;
		matrix[1][0] = 0;
		matrix[1][1] = scale;
		for (PointVec pv:s.points) {
			for (int i=0;i<pv.size();i++) {
				PointF pin = pv.get(i);
				PointUtil.mulMatrix(pin, pin, matrix);// scale 
			}
		}
	}
	
	public void genRect(PointVec curVec,RectF r) {
		curVec.clear();
		curVec.closed=true;
		curVec.add(new PathData(r.left,r.top));
		curVec.add(new PathData(r.right,r.top));
		curVec.add(new PathData(r.right,r.bottom));
		curVec.add(new PathData(r.left,r.bottom));
	}
	public static void genCircle(PointVec curVec) {
		for (float i=0;i<2*Math.PI;i+=Math.PI/resolution) {
			curVec.add(new PathData((float)Math.cos(i),(float)Math.sin(i)));
		}
	}
	
	public static void generatePoly(PointVec pv , int sides){
		pv.clear();
		pv.closed=true;
		double rot = Math.PI*2/sides;
		for (float i=0;i<2*Math.PI;i+=rot) {
			pv.add(new PathData((float)Math.cos(i-rot/4),(float)Math.sin(i-rot/4)));
		}
	}
	
	public static void generateStar(PointVec pv , int sides,float mod){
		pv.clear();
		pv.closed=true;
		double rot = Math.PI*2/sides;
		for (float i=0;i<2*Math.PI;i+=rot) {
			pv.add(new PathData((float)Math.cos(i-rot/2),(float)Math.sin(i-rot/2)));
			pv.add(new PathData(mod*(float)Math.cos(i),mod*(float)Math.sin(i)));
		}
	}
	
	public static void generateRose(PointVec pv , int sides,float mod){
		pv.clear();
		pv.closed=true;
		int res = resolution;
		if (res<sides*20) {res=sides*20;}
		PolarCoords p = new PolarCoords(0, 0);
		double rot = Math.PI*2/sides;
		for (float i=0;i<2*Math.PI;i+=2*Math.PI/res) {
			float r = (float)(Math.sin(sides*i-rot/4)+mod);
			p.set(r,i);
			pv.add(new PathData(p.x(),p.y()));
		}
	}
	
	public static void generateHypocycloid(PointVec pv , int sides,float mod){
		pv.clear();
		pv.closed=true;
		//PolarCoords p = new PolarCoords(0, 0);
		int res = resolution;
		if (res<sides*20) {res=sides*20;}
		for (float angle=0;angle<2*Math.PI;angle+=2*Math.PI/res) {
			PathData pt = new PathData();
			pt.x = (sides-1) *(float)Math.cos(angle)+mod*(float)Math.cos((sides-1)*angle);
			pt.y = (sides-1) *(float)Math.sin(angle)-mod*(float)Math.sin((sides-1)*angle);
			pv.add(pt);
		}
	}
	
	public static void generateEpicycloid(PointVec pv , int sides,float mod){
		pv.clear();
		pv.closed=true;
		PolarCoords p = new PolarCoords(0, 0);
		int res = resolution;
		if (res<sides*20) {res=sides*20;}
		for (float angle=0;angle<2*Math.PI;angle+=2*Math.PI/res) {
			PathData pt = new PathData();
			pt.x = (sides+1) *(float)Math.cos(angle)-mod*(float)Math.cos((sides+1)*angle);
			pt.y = (sides+1) *(float)Math.sin(angle)-mod*(float)Math.sin((sides+1)*angle);
			pv.add(pt);
		}
	}
	
	public static void generateLissajous(PointVec pv , float a, float b,float delta){
		pv.clear();
		pv.closed=true;
		PolarCoords p = new PolarCoords(0, 0);
		int res = resolution;
		if (res<a*20) {res=(int)a*20;}
		float A = 1;
		float B = 1;
		for (float angle=0;angle<2*Math.PI;angle+=2*Math.PI/res) {
			PathData pt = new PathData();
			pt.x=A * (float)Math.sin(a*angle+delta);
			pt.y=B * (float)Math.sin(b*angle);
			pv.add(pt);
		}
	}
	
	public static void makeRect(Stroke s,RectF r) {
		s.type=Stroke.Type.LINE;
		PointVec curVec = s.currentVec;
		curVec.clear();
		curVec.closed=true;
		curVec.add(new PathData(r.left,r.top));
		curVec.add(new PathData(r.right,r.top));
		curVec.add(new PathData(r.right,r.bottom));
		curVec.add(new PathData(r.left,r.bottom));
	}
	
	//TODO need a method that takes a font
	public static Stroke makeTextRec(String text, RectF r) {//,String fontName
		Stroke s = new Stroke(true);
		s.type=Stroke.Type.TEXT_TTF;
		Paint pt = new Paint();
		pt.setTextSize(r.height());
		s.textXScale = pt.measureText(text)/r.width();
		s.text=text;
		PointVec curVec = s.currentVec;
		curVec.clear();
		curVec.closed=true;
		float left = Math.min(r.left, r.right);
		float right = Math.max(r.left, r.right);
		float top = Math.min(r.top, r.bottom);
		float bottom = Math.max(r.top, r.bottom);
		curVec.add(new PathData(left,bottom));
		curVec.add(new PathData(right,bottom));
		curVec.add(new PathData(right,top));
		curVec.add(new PathData(left,top));
		return s;
	}

    /**
     *   Make a new text stroke
     *   measures the text
     *   @param text the text string
     *   @param p the bottom left for the text
     *   @param size text size(px)
     */
	public static Stroke makeText( String text,PointF p,float size) {
		Stroke s = new Stroke(true);
		s.type=Stroke.Type.TEXT_TTF;
		s.text=text;
		PointVec curVec = s.points.get(0);
		Paint pt  =new Paint();
		pt.setTextSize(size);
		float wid = pt.measureText(text);
		curVec.clear();
		curVec.closed=true;
		float left = p.x;
		float bottom = p.y;
		float right = left+wid;
		float top = bottom-size;
		curVec.add(new PathData(left,bottom));
		curVec.add(new PathData(right,bottom));
		curVec.add(new PathData(right,top));
		curVec.add(new PathData(left,top));
		return s;
	}

    /**
     *   Sets the text position
     *   measures the text
     *   @param s the stroke
     *   @param text new text string (only set if not null)
     *   @param p the bottom left for the text
     *   @param size text size(px)
     */
    public static Stroke setTextPosition( Stroke s,String text, PointF p, float size) {
        s.type=Stroke.Type.TEXT_TTF;
        if (text!=null) {
            s.text=text;
        }
        PointVec curVec = s.points.get(0);
        Paint pt  =new Paint();
        pt.setTextSize(size);
        float wid = pt.measureText(s.text);
        curVec.clear();
        curVec.closed=true;
        float left = p.x;
        float bottom = p.y;
        float right = left+wid;
        float top = bottom-size;
        curVec.add(new PathData(left,bottom));
        curVec.add(new PathData(right,bottom));
        curVec.add(new PathData(right,top));
        curVec.add(new PathData(left,top));
        return s;
    }

    /**
     * Sets the text height.
     * @param s
     * @param size
     */
	public static void setTextHeight(Stroke s,float size) {
		if (s.type==Type.TEXT_TTF) {
			// TODO will have to make work with rotated text. use trig
			PointVec pointVec = s.points.get(0);
			if (pointVec.size()==4 && Math.abs(pointVec.get(0).y-pointVec.get(1).y)<0.00001) {//check horizontal else need trig
				float height = pointVec.get(0).y-pointVec.get(3).y;
				pointVec.get(3).y=pointVec.get(0).y-size;
				pointVec.get(2).y=pointVec.get(1).y-size;
			}
		}
	}

    /**
     * Generate an arc from the Arc parameters
     * @param arc Arc data
     */
	public static void genArc(Arc arc) {
		float interval = (arc.startAndSweepAngle.y)/(arc.boundsCheck.size()-1);

        float start = (arc.startAndSweepAngle.x*(float)Math.PI/180);
		float sweep = (arc.startAndSweepAngle.y*(float)Math.PI/180);
		
		interval =  sweep/(arc.boundsCheck.size()-1);
		int ctr=0;
		float i=0;
		for (i=start;
				ctr<arc.boundsCheck.size();
				i+=interval) {
			arc.boundsCheck.get(ctr).set((float)Math.cos(i),(float)Math.sin(i));
			ctr++;
		}
		//Log.d(VecGlobals.LOG_TAG, "genArc:end i:"+i);
		//TODO merge fit&rotate ops together to optimise
		fitStroke(arc.boundsCheck, arc.oval);
		if (arc.xrot>0) {
			TransformOperatorInOut t = new TransformOperatorInOut();
			t.rotateValue = arc.xrot;
			t.anchor.set(PointUtil.midpoint(arc.oval));
			t.generate();
			TransformController.transform(arc.boundsCheck, arc.boundsCheck, t);
		}
	}
	private static float cubic(float t,float p1,float p2, float p3, float p4) {
		//q(t) = p1(1-t)3 + 3p2t(1-t)2 + 3p3t2 + p4t3
		float tm1=1-t;
		return p1*tm1*tm1*tm1+3*p2*tm1*tm1*t+3*p3*tm1*t*t+p4*t*t*t;
	}
	private static float quadric(float t,float p1,float p2, float p3) {
		//(1-t)2•P1 + 2•(1-t) •t•p2 + t2•P3
		float tm1=1-t;
		return p1*tm1*tm1+2*p2*tm1*t+p3*t*t;
	}
	public static void genBezier(Bezier b,PointF last){
		//PointF interval = new PointF(1f/(b.boundsCheck.size()-1),1f/(b.boundsCheck.size()-1));//new PointF( (b.x-last.x)/(b.boundsCheck.size()-1),(b.y-last.y)/(b.boundsCheck.size()-1));
		float interval = 1f/(b.boundsCheck.size()-1);
		int ctr=0;
		float t =0;
		for (t=0; ctr<b.boundsCheck.size(); t+=interval ) {
			b.boundsCheck.get(ctr).set(
					cubic(t,last.x,b.control1.x,b.control2.x,b.x),
					cubic(t,last.y,b.control1.y,b.control2.y,b.y)
					);
			ctr++;
		}
		//Log.d(VecGlobals.LOG_TAG, "genBezier:"+ctr+":");
	}
	public static void genQuadric(Quartic b,PointF last){
		float interval = 1f/(b.boundsCheck.size()-1);//new PointF(1f/(b.boundsCheck.size()-1),1f/(b.boundsCheck.size()-1));//new PointF( (b.x-last.x)/(b.boundsCheck.size()-1),(b.y-last.y)/(b.boundsCheck.size()-1));
		int ctr=0;
		float t =0;
		for (t=0; ctr<b.boundsCheck.size(); t+=interval ) {
			b.boundsCheck.get(ctr).set(
					quadric(t,last.x,b.control1.x,b.x),
					quadric(t,last.y,b.control1.y,b.y)
					);
			ctr++;
		}
		//Log.d(VecGlobals.LOG_TAG, "genQuadric:"+ctr+":");
	}
}
