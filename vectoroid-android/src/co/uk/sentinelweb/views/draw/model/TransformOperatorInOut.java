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
import java.util.HashSet;

import android.graphics.PointF;
import android.graphics.RectF;
import co.uk.sentinelweb.views.draw.util.PointUtil;

public class TransformOperatorInOut {
	public enum Axis {NONE,BOTH,X,Y,PRESERVE_ASPECT}

	public enum Trans {NONE,ROTATE,MOVE,SCALE,SHEAR,PROJ}
	
	public HashSet<Trans> ops= new HashSet<Trans>();
	public Axis axis= Axis.PRESERVE_ASPECT;
	public double scaleValue = 1;
	public double scaleXValue = 1;
	public double scaleYValue = 1;
	public double rotateValue = 0;// radians
	public double skewXValue = 0;
	public double skewYValue = 0;
	
	public double[][] matrix3;
	private double[][] matrix2;
	public PointF anchor = new PointF();
	
	public PointF trans = new PointF();
	
	PointUtil _pointUtil;
	
	public TransformOperatorInOut() {
		super();
		_pointUtil = new PointUtil();
	}
	
	public void generate() {
		double[][] matrixRot = new double[2][2];
		matrixRot[0][0] = Math.cos(rotateValue);
		matrixRot[0][1] = Math.sin(rotateValue);
		matrixRot[1][0] = -Math.sin(rotateValue);
		matrixRot[1][1] = Math.cos(rotateValue);
		
		double[][] matrixShear = new double[2][2];
		matrixShear[0][0] = 1;
		matrixShear[0][1] = skewXValue;
		matrixShear[1][0] = skewYValue;
		matrixShear[1][1] = 1;
		
		double[][] matrixScale = new double[2][2];
		matrixScale[0][0] = scaleXValue;
		matrixScale[0][1] = 0;
		matrixScale[1][0] = 0;
		matrixScale[1][1] = scaleYValue;
		
		double[][] useMatrix=PointUtil.mulMatrix(matrixRot, matrixShear, null);
		matrix2=PointUtil.mulMatrix(useMatrix, matrixScale, null);
		
		PointF anchorCorrect = getTranslationForCOGAndAnchor(matrix2,anchor);
		
		matrix3=new double[3][3];
		matrix3[2][2]=1;
		matrix3[0][0]=matrix2[0][0];
		matrix3[0][1]=matrix2[0][1];
		matrix3[1][0]=matrix2[1][0];
		matrix3[1][1]=matrix2[1][1];
		matrix3[0][2]=anchorCorrect.x+trans.x;
		matrix3[1][2]=anchorCorrect.y+trans.y;
	}
	
	public final void operate(PointF pin,PointF pout){
		_pointUtil.mul3(pin,pout,matrix3);
	}
	
	/**
	 * Calculates a difference vector between the transformed origin and the original origin (anchor)
	 * @param matrix 2x2 
	 * @param anchor
	 * @return
	 */
	public PointF getTranslationForCOGAndAnchor(double[][] matrix,PointF anchor) {//PointF refCOG,
		PointF trans=new PointF(anchor.x,anchor.y);
		PointF tmp=new PointF();
		PointUtil.mulMatrix(trans,tmp, matrix);
		PointUtil.subVector(tmp,trans, anchor);// diff vector
		PointUtil.mulVector(trans,trans, -1);// invert
		return trans;
	}
	public void correctTranslation(PointF p) {
		matrix3[0][2]=p.x+trans.x;
		matrix3[1][2]=p.y+trans.y;
	}
	public double[][] get2x2() {
		return matrix2;
	}
	public TransformOperatorInOut invert() {
		TransformOperatorInOut t = new TransformOperatorInOut();
		t.ops.addAll(ops);
		t.axis=axis;
		t.scaleValue=1/scaleValue;
		t.scaleXValue=1/scaleXValue;
		t.scaleYValue=1/scaleYValue;
		t.rotateValue=-rotateValue;
		t.skewXValue=-skewXValue;
		t.skewYValue=-skewYValue;
		PointUtil.mulVector(trans, t.trans, -1);//-1
		//PointUtil.mulVector(anchor, t.anchor, -1);//-1
		t.anchor.set(anchor);
		t.generate();
		//PointUtil.mulVector(transOut, t.transOut, -1);
		return t;
	}
	
	public static TransformOperatorInOut makeTranslate(PointF trans) {
		TransformOperatorInOut o = new TransformOperatorInOut();
		o.axis=Axis.PRESERVE_ASPECT;
		o.ops.add(Trans.MOVE);
		o.trans=trans;
		o.generate();
		return o;
	}
	
	public static TransformOperatorInOut makeScale(float scale,RectF bounds) {
		//final usePoint=new PointF();
		TransformOperatorInOut o = new TransformOperatorInOut() ;
		//o.matrix=new double[][]{{scale,0},{0,scale}};
		o.scaleValue=scale;
		o.scaleXValue=scale;
		o.scaleYValue=scale;
		o.axis=Axis.PRESERVE_ASPECT;
		o.ops.add(Trans.SCALE);
		o.anchor.set(new PointF((bounds.left+bounds.right)/2,(bounds.top+bounds.bottom)/2));
		o.generate();
		return o;
	}
	public static TransformOperatorInOut makeScaleAndTranslate(PointF trans, float scale, RectF bounds) {
		TransformOperatorInOut o = new TransformOperatorInOut();
		//o.matrix=new double[][]{{scale,0},{0,scale}};
		o.scaleValue=scale;
		o.scaleXValue=scale;
		o.scaleYValue=scale;
		o.axis=Axis.PRESERVE_ASPECT;
		o.ops.add(Trans.SCALE);
		o.ops.add(Trans.MOVE);
		
		o.anchor.set(new PointF((bounds.left+bounds.right)/2,(bounds.top+bounds.bottom)/2));//
		o.trans=trans;
		o.generate();
		return o;
	}
	
}

