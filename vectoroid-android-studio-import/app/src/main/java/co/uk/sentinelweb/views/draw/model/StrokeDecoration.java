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
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;

public class StrokeDecoration {
	public static class Tip {
		public static enum Type {NONE,ARROW,DBL_ARROW,SQUARE,CIRCLE,DIAMOND,CUSTOM}
		public static final float SIZE_SMALL=1f;
		public static final float SIZE_MED=2f;
		public static final float SIZE_LARGE=3f;
		
		public Type type = Type.NONE;
		public boolean closed;
		public boolean filled;
		public boolean inside;
		public float size = SIZE_SMALL;
		
		//public Path usePath=null;
		public Path tmplPath;
		public Paint inner=null;
		public Paint outer=null;
		public Matrix m = null;
		public Tip duplicate() {
			Tip t = new Tip();
			t.type=type;
			t.closed=closed;
			t.filled=filled;
			t.inside=inside;
			t.size=size;
			setTmplPath( );
			return t;
		}
		public void setTmplPath( ) {
			switch (type) {
				case NONE:tmplPath=null;break;
				case ARROW:tmplPath=arrow;break;
				case DBL_ARROW:tmplPath=dblarrow;break;
				case SQUARE:tmplPath=square;break;
				case CIRCLE:tmplPath=circle;break;
				case DIAMOND:tmplPath=diamond;break;
			}
		}
		
		public DecorationPathData getTmplFloatArray( ) {
			switch (type) {
				case NONE:return null;
				case ARROW:return arrowPathData;
				case DBL_ARROW:return dblarrowPathData;
				case SQUARE:return squarePathData;
				case CIRCLE:return circlePathData;
				case DIAMOND:return diamondPathData;
				default: return null;
			}
		}
	};
	public static class DecorationPathData {
		public float[] pts;
		public char[] cmd;
		public boolean closed;
		public DecorationPathData(float[] pts, char[] cmd, boolean closed) {
			super();
			this.pts = pts;
			this.cmd = cmd;
			this.closed = closed;
		}
	}
	public static final DecorationPathData arrowPathData = new DecorationPathData(
			new float[]{1,-1,0,1,-1,-1},
			new char[] {'M','L','-'},
			false
		);
	public static Path arrow = new Path();
	static {
		arrow.moveTo(1,-1);
		arrow.lineTo(0,1);
		arrow.lineTo(-1,-1);
		//arrow.close();
	}
	public static final DecorationPathData dblarrowPathData = new DecorationPathData(
			new float[]{1,-2,0,0,-1,-2,1,0,0,2,-1,0},
			new char[] {'M','L','-','M','L','-'},
			false
		);
	//public static float[] dblarrowFloat = new float[]{1,-2,0,0,-1,-2,1,0,0,2,-1,0};
	public static Path dblarrow = new Path();
	static {
		dblarrow.moveTo(1,-2);
		dblarrow.lineTo(0,0);
		dblarrow.lineTo(-1,-2);
		dblarrow.moveTo(1,0);
		dblarrow.lineTo(0,2);
		dblarrow.lineTo(-1,0);
	}
	public static final DecorationPathData diamondPathData = new DecorationPathData(
			new float[]{0,2,1f,0,0,-2,-1f,0},
			new char[] {'M','L','-','-'},
			true
		);
	//public static float[] diamondFloat = new float[]{0,2,1f,0,0,-2,-1f,0};
	public static Path diamond = new Path();
	static {
		diamond.moveTo(0,2);
		diamond.lineTo(1f,0);
		diamond.lineTo(0,-2);
		diamond.lineTo(-1f,0);
		diamond.close();
	}
	public static final DecorationPathData squarePathData = new DecorationPathData(
			new float[]{1,1,1,-1,-1,-1,-1,1},
			new char[] {'M','L','-','-'},
			true
		);
	//public static float[] squareFloat = new float[]{1,1,1,-1,-1,-1,-1,1};
	public static Path square = new Path();
	static {
		square.moveTo(1,1);
		square.lineTo(1,-1);
		square.lineTo(-1,-1);
		square.lineTo(-1,1);
		square.close();
	}
	//public static  float[] circleFloat;
	static int res = 20;
	public static final DecorationPathData circlePathData = new DecorationPathData(
			new float[res*4],
			new char[res*2] ,
			true
		);
	public static Path circle = new Path();
	static {
		int ctr =0;
		for (float i=0;i<2*Math.PI;i+=Math.PI/res) {
			circlePathData.pts[ctr*2]=(float)Math.cos(i);
			circlePathData.pts[ctr*2+1]=(float)Math.sin(i);
			if (i==0) {circlePathData.cmd[ctr]='M';circle.moveTo(circlePathData.pts[ctr*2],circlePathData.pts[ctr*2+1]);}
			else  {circlePathData.cmd[ctr]='L';circle.lineTo(circlePathData.pts[ctr*2],circlePathData.pts[ctr*2+1]);}
			ctr++;
		}
	}

	public enum BreakType {SOLID,DOT,DASH,DOT_DASH};
	public static float[] DOT = {1f,2f};
	public static float[] DASH = {3f,3f};
	public static float[] DOT_DASH = {1f,2f,6f,2f};
	public static float[] getBreakTypeArray(BreakType b) {
		switch(b){
			case DOT:return DOT;
			case DASH:return DASH;
			case DOT_DASH:return DOT_DASH;
			default:return null;
		}
	}
	
}
