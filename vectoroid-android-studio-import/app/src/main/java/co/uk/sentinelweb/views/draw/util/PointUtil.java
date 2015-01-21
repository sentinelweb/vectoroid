package co.uk.sentinelweb.views.draw.util;
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
import java.util.Arrays;
import java.util.Vector;

import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;

public class PointUtil {
	private static PointF usePoint = new PointF();
	private static PointF usePoint2 = new PointF();
	public static float PI = (float)Math.PI;
	
	public static float getMedian(float[] arr,int ctr){
		Arrays.sort(arr, 0, ctr);
		float val = 0;
		if (ctr%2==0) {// even interlpoate
			val =  (arr[ctr/2]+arr[ctr/2+1])/2f;
		} else {
			val =  arr[ctr/2];
		}
		//Log.d(Globals.TAG,"median:"+val+":"+ ctr+ ":"+s.toString() );
		return val;
	}
	
	public static float getMean(float[] arr,int ctr){
		//StringBuffer s = new StringBuffer();
		float accum = 0;
		for (int i=0;i<ctr;i++) {
			accum+=arr[i];
		//	s.append(" arr:"+i+":v:"+arr[i]+":accum:"+accum);
		}
		//Log.d(Globals.TAG,"mean:"+accum+":"+ ctr+ ":"+s.toString() );
		return accum/ctr;
	}
	
	public static float getMidpoint(float[] arr,float factor){
		float midpoint = arr[0]+(arr[2]-arr[0])/2f;
		float diff=(arr[1]-midpoint); 
		return arr[1]-diff*factor;
	}
	
	public static double[][]  mulMatrix(double[][] matrixA,double[][] matrixB,double[][] matrixOut) {
		if (matrixOut==null) {matrixOut=new double[2][2];}
		matrixOut[0][0]=matrixA[0][0]*matrixB[0][0]+matrixA[1][0]*matrixB[0][1];
		matrixOut[1][0]=matrixA[0][0]*matrixB[1][0]+matrixA[1][0]*matrixB[1][1];
		matrixOut[0][1]=matrixA[0][1]*matrixB[0][0]+matrixA[1][1]*matrixB[0][1];
		matrixOut[1][1]=matrixA[0][1]*matrixB[1][0]+matrixA[1][1]*matrixB[1][1];
		return matrixOut;
	}
	
	//mul points out = in+point(2x2) 
	public static  void mulMatrix(PointF pin,PointF pout, double[][] matrix) {//matrix is 2x2
		pout.x = (float)(pin.x*matrix[0][0]+pin.y*matrix[0][1]);
		pout.y = (float)(pin.x*matrix[1][0]+pin.y*matrix[1][1]);
	}
	
	//add points out = in+point(2x1) 
	public static  void addVector(PointF pin,PointF pout, PointF vec) {//matrix is 2x1
		pout.x = pin.x+vec.x;
		pout.y = pin.y+vec.y;
	}
	
	public static  void subVector(PointF pin,PointF pout, PointF vec) {//matrix is 2x1
		pout.x = pin.x-vec.x;
		pout.y = pin.y-vec.y;
	}
	
	public static  void mulVector(PointF pin,PointF pout, float scalar) {//matrix is 2x1
		pout.x = pin.x*scalar;
		pout.y = pin.y*scalar;
	}
	public static  void mulVector(PointF pin,PointF pout, PointF vec) {//matrix is 2x1
		pout.x = pin.x*vec.x;
		pout.y = pin.y*vec.y;
	}
	public static void addMatrix(Vector<PointF> pin, PointF matrix) {//matrix is 2x1
		for (int i=0;i<pin.size();i++) {
			PointF p =pin.get(i);
			//if (p.x==StrokeAPI.POINT_BREAK) {continue;}
			addVector( p,p, matrix);
		}
	}
	
	public static  void applyRotation(PointF input,PointF output, double[][] matrix,PointF trans) {
		mulMatrix(input,output, matrix);// rotates around origin
		addVector(output,output, trans);// move back
	}
	private double[] vin=new double[3];
	//vin[2]=1;
	private double[] vout=new double[3];
	public void mul3(PointF pin,PointF pout,double m[][]) {// matrix is 3x3
		vin[0]=pin.x;vin[1]=pin.y;vin[2]=1;
		vout[0]=m[0][0]*vin[0]+m[0][1]*vin[1]+m[0][2]*vin[2];
		vout[1]=m[1][0]*vin[0]+m[1][1]*vin[1]+m[1][2]*vin[2];
		vout[2]=m[2][0]*vin[0]+m[2][1]*vin[1]+m[2][2]*vin[2];
		//Log.d(Globals.LOG_TAG, "vout[2]="+vout[2] +"; m[2]=["+m[2][0]+","+m[2][1]+","+m[2][2]+"]");
		pout.set((float)vout[0],(float)vout[1]);
	}
	
	public static boolean checkBounds(RectF r, PointF p ) {
		return r.top<p.y && r.bottom>p.y && r.left<p.x && r.right>p.x;
	}
	
	public static boolean checkBounds(RectF r, RectF tst ) {// tests that tst in within the bounds of r
		return r.top<tst.top && r.bottom>tst.bottom && r.left<tst.left && r.right>tst.right;
	}
	public static boolean checkBounds2(RectF r, PointF p ) {
		return Math.min(r.top, r.bottom)<p.y && Math.max(r.top, r.bottom)>p.y && Math.min(r.left, r.right)<p.x && Math.max(r.left, r.right)>p.x;
	}
	public static float norm(PointF p) {
		return (float)Math.sqrt(p.x*p.x+p.y*p.y);
	}
	
	public static float calcAngle(PointF p1, PointF p2, PointF p3) {//p2 is the middle point(making the angle)
		//TODO test this works
		usePoint.set(p2.x-p1.x,p2.y-p1.y);
		usePoint2.set(p3.x-p2.x,p3.y-p2.y);
		float dotprod = (float)((usePoint.x*usePoint2.x+usePoint.y*usePoint2.y)/(Math.sqrt(usePoint.x*usePoint.x+usePoint.y*usePoint.y)*Math.sqrt(usePoint2.x*usePoint2.x+usePoint2.y*usePoint2.y)));
		float angle = (float)Math.acos(dotprod);
		
		//Log.d(Globals.TAG, "calcAngle:p1:("+p1.x+","+p1.y+"):p2:("+p2.x+","+p2.y+"):p3("+p3.x+","+p3.y+"): d1:("+d1.x+","+d1.y+"): d2:("+d2.x+","+d2.y+"): dp"+dp+": angle:"+angle+": "+Math.abs(angle-Math.PI));
		return angle;
	}
	public static float calcAngle2PI(PointF p1, PointF p2, PointF p3) {//p2 is the middle point(making the angle)
		usePoint.set(p1.x-p2.x,p1.y-p2.y);
		usePoint2.set(p3.x-p2.x,p3.y-p2.y);
		float angle = (float)(Math.atan2(usePoint2.y , usePoint2.x )-Math.atan2( usePoint.y,  usePoint.x));
		if (angle <0) angle += 2*PI;
		return angle;
	}
	public static float dist(PointF chk ,PointF tgt ) {
		usePoint.x = Math.abs(chk.x-tgt.x);
		usePoint.y = Math.abs(chk.y-tgt.y);
		return (float)Math.sqrt(usePoint.x*usePoint.x+usePoint.y*usePoint.y);
	}
	
	public static void scale(RectF r,float factor){
		r.left=r.left*factor;
		r.right=r.right*factor;
		r.top=r.top*factor;
		r.bottom=r.bottom*factor;
	}
	public static void translate(RectF r,PointF trans,float transFactor){
		r.left=r.left+trans.x*transFactor;
		r.right=r.right+trans.x*transFactor;
		r.top=r.top+trans.y*transFactor;
		r.bottom=r.bottom+trans.y*transFactor;
	}
	
	public static void setRectFromRectF(Rect dst,RectF src){
		dst.set((int)src.left,(int)src.top,(int)src.bottom,(int)src.right);
	}
	public static void setRectFromRect(RectF dst,Rect src){
		dst.set(src.left,src.top,src.bottom,src.right);
	}
	public static PointF midpoint(RectF r){
		return new PointF((Math.max(r.left,r.right)-Math.min(r.left,r.right))/2f+Math.min(r.left,r.right),(Math.max(r.top,r.bottom)-Math.min(r.top,r.bottom))/2f+Math.min(r.top,r.bottom));
	}
	public static PointF midpoint(PointF p1,PointF p2){
		return new PointF((Math.max(p1.x,p2.x)-Math.min(p1.x,p2.x))/2f+Math.min(p1.x,p2.x),(Math.max(p1.y,p2.y)-Math.min(p1.y,p2.y))/2f+Math.min(p1.y,p2.y));
	}
	
	public static String tostr(RectF r) {
		return "rect["+r.left+","+r.top+" -> "+r.right+","+r.bottom+" dim:"+r.width()+","+r.height()+":"+r.width()/r.height()+"]";
	}
	public static String tostr(Rect r) {
		return "rect["+r.left+","+r.top+" -> "+r.right+","+r.bottom+" dim:"+r.width()+","+r.height()+":"+r.width()/(float)r.height()+"]";
	}
	
	public static String tostr(PointF pt) {
		if (pt!=null) {
			return "pt["+pt.x+","+pt.y+"]";
		} else {
			return "pt[null]";
		}
	}
	public static String tostr(double[][] mat) {
		return "mat[ ["+mat[0][0]+","+mat[0][1]+"] , ["+mat[1][0]+","+mat[1][1]+"] ]";
	}
}
