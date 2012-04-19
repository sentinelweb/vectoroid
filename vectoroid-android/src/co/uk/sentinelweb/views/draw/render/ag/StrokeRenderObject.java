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
import android.graphics.Bitmap;
import android.graphics.BlurMaskFilter;
import android.graphics.ComposePathEffect;
import android.graphics.CornerPathEffect;
import android.graphics.DashPathEffect;
import android.graphics.EmbossMaskFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.Path.FillType;
import android.graphics.PathEffect;
import android.graphics.PointF;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.util.Log;
import co.uk.sentinelweb.views.draw.DVGlobals;
import co.uk.sentinelweb.views.draw.file.FileRepository;
import co.uk.sentinelweb.views.draw.model.DrawingElement;
import co.uk.sentinelweb.views.draw.model.Fill;
import co.uk.sentinelweb.views.draw.model.Fill.Type;
import co.uk.sentinelweb.views.draw.model.PointVec;
import co.uk.sentinelweb.views.draw.model.Stroke;
import co.uk.sentinelweb.views.draw.model.StrokeDecoration;
import co.uk.sentinelweb.views.draw.model.StrokeDecoration.BreakType;
import co.uk.sentinelweb.views.draw.model.StrokeDecoration.Tip;
import co.uk.sentinelweb.views.draw.model.UpdateFlags;
import co.uk.sentinelweb.views.draw.model.UpdateFlags.UpdateType;
import co.uk.sentinelweb.views.draw.render.RenderObject;
import co.uk.sentinelweb.views.draw.util.PointUtil;

public class StrokeRenderObject extends RenderObject{
	

	PointF _usePoint = new PointF();
	PointF _usePoint2 = new PointF();
	RectF _useRect = new RectF();
	
	public Path path;
	public Path testpath;
	public Paint fgInner;
	public Paint fgOuter;
	public Paint fgFill;
	public Typeface fontTTF; 
	public float textCalcwidth;
	public float textAngle=0;
	public float textHeight=0;
	public float textPathScaling=1;
	//public RectF renderCalcBounds=new RectF();
	Paint testPaint = new Paint();
	public Bitmap bitmapMask = null;
	public boolean dontDisplay = false;
	public String[] splitStr=null;
	public boolean fgOnly=false;
	
	public StrokeRenderObject(AndGraphicsRenderer r) {
		super(r);
		path=new Path();
		testpath=new Path();
		fgInner = new Paint();
		fgInner.setAntiAlias(true);
		fgOuter = new Paint();
		fgOuter.setAntiAlias(true);
		fgFill = new Paint();
		fgFill.setAntiAlias(true);
	}

	@Override
	public void update(DrawingElement de,UpdateFlags flags) {
		Stroke s = (Stroke)de;
		//float z = r.getVpd().zoom;
		 //r.getVpd().zoom=1;
		 dontDisplay = false;
		//Log.d(Globals.LOG_TAG, "update:"+flags.updateTypes.toString());
		if (flags.updateTypes.contains(UpdateType.BOUNDS) ) {
			setBounds(s);
		}
		if (flags.updateTypes.contains(UpdateType.PAINT) ) {
			setPainters(s);
		}
		if (flags.updateTypes.contains(UpdateType.PATH)) {
			makePath(s);
		}
		if (flags.updateTypes.contains(UpdateType.FILL) && flags.fillTypes.contains(s.fill.type)) {
			//Log.d(Globals.LOG_TAG, "updateFill:"+s.fill.type+":"+flags.fillTypes.size());
			//DebugUtil.logCall("fill call:", new Exception());
			updateFill(s);
		}
		//r.getVpd().zoom=z;
	}
	
	public void setBounds (Stroke s) {
		
		if (s.type==Stroke.Type.TEXT_TTF && s.text!=null) { 
			splitStr = s.text.split("\n");
			fontTTF = FileRepository.getFileRepository(this.r.c).getFontController().getTTFont(s.fontName);
			
			//if (s.calculatedBounds.width()==0) {
			//DebugUtil.logCall("Stroke setBounds: "+s.points.get(0).size()+" : ", new Exception());
			/*
			if (s.points.get(0).size()>0) {
				Log.d(Globals.LOG_TAG, "setBounds txt pt1 : "+PointUtil.tostr(s.points.get(0).get(0)));
			}
			if (s.points.get(0).size()>1) {
				_usePoint.set(s.points.get(0).get(1).x,s.points.get(0).get(1).y);
				_usePoint2.set(s.points.get(0).get(1).x+30,s.points.get(0).get(1).y);
				float angle = PointUtil.calcAngle360(_usePoint2, _usePoint, s.points.get(0).get(0));
				Log.d(Globals.LOG_TAG, "setBounds txt pt2 : "+ PointUtil.tostr(s.points.get(0).get(1))+" : "+(angle*180/PointUtil.PI)+" deg");
			}
			*/
			float width=s.calculatedBounds.width();
			float lineHeight=s.calculatedBounds.height()/splitStr.length;
			if (s.points.get(0).size()==4) { 
				width = PointUtil.dist(s.points.get(0).get(0), s.points.get(0).get(1));
				lineHeight = PointUtil.dist(s.points.get(0).get(0), s.points.get(0).get(3))/splitStr.length;
				//Log.d(Globals.LOG_TAG, "text calc bounds:'"+width+" x "+lineHeight);
				//_usePoint.set(_useRect.left,_useRect.bottom);
				//_usePoint2.set(_useRect.left+30,_useRect.bottom);
				//float angle = PointUtil.calcAngle360(_usePoint2, _usePoint, s.points.get(0).get(0));
				//Log.d(Globals.LOG_TAG, "text calc bounds:'"+width+" x "+height+" ; angle:"+(angle*180/PointUtil.PI));
			} else if (s.points.get(0).size()==1) {
				width=300;//not used
				lineHeight=(getDefaultTextHeight());///r.getVpd().zoom
			}
			testPaint.setTypeface(fontTTF);
			//testPaint.setTextSize(s.calculatedBounds.height(););
			//if (lineHeight<0.5) {dontDisplay=true;}
			lineHeight = Math.max(1,lineHeight);
			textHeight = lineHeight*splitStr.length;
			testPaint.setTextScaleX(1);
			float length = width;
			textPathScaling=1;
			if (s.points.size()>1) {
				PointVec pv = s.points.get(1);
				if (pv.size()>1) {
					length = 0;
					for (int i=1;i<pv.size();i++) {
						length+=PointUtil.dist(pv.get(i-1), pv.get(i));
					}
					textPathScaling=width/length*0.98f;
				}
			}
			testPaint.setTextSize(lineHeight);
			textCalcwidth = 0;
			for (String lineStr:splitStr) {
				textCalcwidth =Math.max(textCalcwidth, testPaint.measureText(lineStr));
			}
			//Log.d(DVGlobals.LOG_TAG, "text calc bounds out:'"+lineHeight+" w "+textCalcwidth+" : xsc: "+s.textXScale);
			if (s.points.get(0).size()>1) {
				_usePoint.set(s.points.get(0).get(0).x+30,s.points.get(0).get(0).y);
				this.textAngle = PointUtil.calcAngle2PI(_usePoint, s.points.get(0).get(0),s.points.get(0).get(1) );
			} else {
				this.textAngle = 0;
			}
			//TODO RM 131011: can take out the s.points.get(0).size()==1 cases - they shouldnt be hit..
			if (s.textXScale>0 ){
				if (s.points.get(0).size()==4) {
					// need to add tangential width & height
					_usePoint.set(s.points.get(0).get(0).x+30,s.points.get(0).get(0).y);
					//float angle = PointUtil.calcAngle360(_usePoint, s.points.get(0).get(0),s.points.get(0).get(1) );
					_usePoint.set(textCalcwidth*s.textXScale*(float)Math.cos(textAngle),textCalcwidth*s.textXScale*(float)Math.sin(textAngle));
					PointUtil.addVector(s.points.get(0).get(0), s.points.get(0).get(1), _usePoint);
					PointUtil.addVector(s.points.get(0).get(3), s.points.get(0).get(2), _usePoint);
					
				} /*else if (s.points.get(0).size()==1){
					_usePoint2.set(textCalcwidth*s.textXScale,0);
					s.points.get(0).add(new PointF());
					PointUtil.addVector(s.points.get(0).get(0), s.points.get(0).get(1), _usePoint2);
					_usePoint2.set(textCalcwidth*s.textXScale,-lineHeight*splitStr.length);
					s.points.get(0).add(new PointF());
					PointUtil.addVector(s.points.get(0).get(0), s.points.get(0).get(2), _usePoint2);
					_usePoint2.set(0,-lineHeight*splitStr.length);
					s.points.get(0).add(new PointF());
					PointUtil.addVector(s.points.get(0).get(0), s.points.get(0).get(3), _usePoint2);
				}*/
			} else {
				if (s.points.get(0).size()==4) {
					if (this.textAngle != 0) {
						// need to add tangential width & height
						_usePoint.set(lineHeight*(float)Math.sin(textAngle)*splitStr.length,-lineHeight*(float)Math.cos(textAngle)*splitStr.length);
						PointUtil.addVector(s.points.get(0).get(0), s.points.get(0).get(3), _usePoint);
						PointUtil.addVector(s.points.get(0).get(1), s.points.get(0).get(2), _usePoint);
					} else {
						// not needed ?
						
					}
				} /*else if (s.points.get(0).size()==1){
				
					_usePoint2.set(textCalcwidth,0);
					s.points.get(0).add(new PointF());
					PointUtil.addVector(s.points.get(0).get(0), s.points.get(0).get(1), _usePoint2);
					_usePoint2.set(textCalcwidth,-lineHeight*splitStr.length);
					s.points.get(0).add(new PointF());
					PointUtil.addVector(s.points.get(0).get(0), s.points.get(0).get(2), _usePoint2);
					_usePoint2.set(0,-lineHeight*splitStr.length);
					s.points.get(0).add(new PointF());
					PointUtil.addVector(s.points.get(0).get(0), s.points.get(0).get(3), _usePoint2);
				}*/
				//s.updateBoundsAndCOG();
				//s.calculatedBounds.height()
			}
			// modify text bounds here for below the line bitmaps - doesnt work :(
			//_usePoint.set(0,3*textHeight/2);
			//PointUtil.addVector(s.points.get(0).get(3), s.points.get(0).get(0), _usePoint);
			//PointUtil.addVector(s.points.get(0).get(2), s.points.get(0).get(1), _usePoint);
			s.updateBoundsAndCOG();
			//Log.d(DVGlobals.LOG_TAG, "Calculated bounds x:"+PointUtil.tostr(s.calculatedBounds)+" : "+(3*lineHeight/2)+" : "+lineHeight);
			/*
			if (s.points.get(0).size()==0) {
				Log.d(Globals.LOG_TAG, "setbounds:add 0");
				s.points.get(0).add(new PointF(s.calculatedBounds.left,s.calculatedBounds.top));
			}
			if (s.points.get(0).size()==1) {
				Log.d(Globals.LOG_TAG, "setbounds:add 1");
				s.points.get(0).add(new PointF(s.calculatedBounds.right,s.calculatedBounds.bottom));
			}
			*/
				//}
			//} else {
				
			//}
		}
		/*
		renderCalcBounds.set(
				s.calculatedBounds.left,//*zoomLevel
				s.calculatedBounds.top,//*zoomLevel
				s.calculatedBounds.right,//*zoomLevel
				s.calculatedBounds.bottom//*zoomLevel
		);
		*/
	}
	private float getDefaultTextHeight() {
		return 30*r.density;
	}
	
	public void makePath(Stroke s) {
		makePath(s, path );
	}
	
	public void makePath(Stroke s, Path path) {//TODO getting called too many times
		path.reset();
		testpath.reset();
		//float zoom = r.getVpd().zoom;
		path.setFillType(s.holesEven?FillType.EVEN_ODD:FillType.WINDING);
		if (s.type!=Stroke.Type.TEXT_TTF) {
			for (PointVec pv:s.points) {
				path.incReserve(pv.size());
				for (int i=0;i<pv.size();i++) {
					if (i==0) {
						path.moveTo(pv.get(i).x/* *zoom */, pv.get(i).y/* *zoom */);
					} else {
						//Log.d(Globals.LOG_TAG,""+pv.beizer1);
						if (pv.beizer1==null || i>=pv.beizer1.size() || pv.beizer1.get(i)==null) {
							path.lineTo(pv.get(i).x/* *zoom */, pv.get(i).y/* *zoom */);
						} else {
							PointF pt = pv.get(i);
							PointF b1 = pv.beizer1.get(i);
							PointF b2 = pv.beizer2.get(i);
							if (pt!=null && b1!=null && b2!=null) {
								path.cubicTo(b1.x/* *zoom */,b1.y/* *zoom */,b2.x/* *zoom */,b2.y/* *zoom */,pt.x/* *zoom */,pt.y/* *zoom */);
							} else {
								path.lineTo(pt.x/* *zoom */, pt.y/* *zoom */);
							}
						}
					} 
				}
				if (pv.size()>0 && pv.closed) {
					path.lineTo(pv.get(0).x/* *zoom */, pv.get(0).y/* *zoom */);
				}
			}
			if (s.points.size()==1 && s.points.get(0).closed ) {
				path.close();
			}
		} else if ( s.text!=null && !"".equals(s.text)) {
			if (s.points.get(0)!=null && s.points.get(0).size()>0){
				/*
				if(s.points.get(0).size()==2) {
					Log.d(Globals.LOG_TAG, "mkopath txt 2 : "+PointUtil.tostr(s.points.get(0).get(0))+" - "+PointUtil.tostr( s.points.get(0).get(1)));
					path.moveTo(s.points.get(0).get(0).x*zoom, s.points.get(0).get(1).y*zoom);
					path.lineTo(s.points.get(0).get(1).x*zoom, s.points.get(0).get(1).y*zoom);
				} else if(s.points.get(0).size()==1) {
					Log.d(Globals.LOG_TAG, "mkopath txt 1 : "+s.points.get(0).get(0).x+","+  s.points.get(0).get(0).y+s.calculatedBounds.height()+" - "+ 
							s.points.get(0).get(0).x+s.calculatedBounds.width()+","+ s.points.get(0).get(0).y+s.calculatedBounds.height());
					path.moveTo((s.points.get(0).get(0).x*zoom), (s.points.get(0).get(0).y+s.calculatedBounds.height())*zoom);
					path.lineTo((s.points.get(0).get(0).x+s.calculatedBounds.width())*zoom, (s.points.get(0).get(0).y+s.calculatedBounds.height())*zoom);
				}
				*/
				PointF point0 = s.points.get(0).get(0);
				if (s.points.size()==2) {
					PointVec pv=s.points.get(1);
					path.incReserve(pv.size());
					for (int i=0;i<pv.size();i++) {
						PointF p = pv.get(i);
						if (textPathScaling!=1) {
							//_usePoint.set(point0);
							//PointUtil.mulVector(_usePoint, _usePoint, -1);
							PointUtil.subVector(p, _usePoint2, point0);
							PointUtil.mulVector(_usePoint2, _usePoint2, textPathScaling);
							PointUtil.addVector(_usePoint2, _usePoint2, point0);
						}
						
						if (i==0) {
							path.moveTo(_usePoint2.x, _usePoint2.y);
						} else {
							path.lineTo(_usePoint2.x, _usePoint2.y);
						} 
					}
				} else 	if(s.points.get(0).size()==4) {
					path.moveTo(point0.x/* *zoom */, point0.y/* *zoom */);
					path.lineTo(s.points.get(0).get(1).x, s.points.get(0).get(1).y);
					
				} else if (s.points.get(0).size()==1) {
					Log.d(DVGlobals.LOG_TAG, "mkopath txt 2 :shouldnt be here... ");
					path.moveTo((point0.x), point0.y);
					path.lineTo((point0.x+s.calculatedBounds.width()), point0.y);
					path.lineTo((point0.x+s.calculatedBounds.width()), (point0.y-getDefaultTextHeight()));
					path.lineTo((point0.x), (point0.y-getDefaultTextHeight()));
				} else {
					dontDisplay=true;
				}
				//path.close();
			}
		}
	}
	int ctr=0;
	public void setPainters(Stroke s) {
		//float zoom = r.getVpd().zoom;
		fgInner.setStrokeWidth(s.pen.strokeWidth);//*density
		fgInner.setColor(s.pen.strokeColour);
		//fgInner.setAlpha(Color.alpha(s.pen.strokeColour));
		fgInner.setStyle(s.fill.type==Type.COLOUR_STROKE?Style.FILL_AND_STROKE:Style.STROKE);
		if (s.type==Stroke.Type.TEXT_TTF  && s.text!=null) {
			float width = s.calculatedBounds.width();
			float height  = s.calculatedBounds.height();
			float xscale =  s.textXScale>0?s.textXScale:1;
			if (s.points.get(0).size() == 4 ){
				height = PointUtil.dist(s.points.get(0).get(0), s.points.get(0).get(3))/splitStr.length;
				if(s.textXScale<=0) {
					width = PointUtil.dist(s.points.get(0).get(0), s.points.get(0).get(1));
					
					float widthRatio = width/textCalcwidth;
					//Log.d(Globals.LOG_TAG, "text calc Painters:'"+width+" x "+height+"  f:"+widthRatio+" textCalcwidth:"+textCalcwidth);
					
					if ( Math.abs(widthRatio)>0.05 && Math.abs(widthRatio)<100 ) {
						xscale=widthRatio;
					} else {
						xscale=1;
					}
				} 
			} else {
				
			}
			float heightOnScreen = height;
			heightOnScreen=Math.max(heightOnScreen, 1);
			//if (heightOnScreen<1) dontDisplay=true;
			fgInner.setTextSize(heightOnScreen);
			fgOuter.setTextSize(heightOnScreen);
			fgFill.setTextSize(heightOnScreen);
			
			//xscale=xscale*0.98f;
			fgInner.setTextScaleX(xscale);
			fgOuter.setTextScaleX(xscale);
			fgFill.setTextScaleX(xscale);
			
		}
		//fgInner.setAlpha(pen.alpha);
		if (s.pen.embEnable) {
			EmbossMaskFilter mEmboss = new EmbossMaskFilter(new float[]{10,10,10},s.pen.embAmbient,s.pen.embSpecular,s.pen.embRadius/* *zoom */);
			fgInner.setMaskFilter(mEmboss);
		} else {
			fgInner.setMaskFilter(null);
		}
		fgInner.setTypeface(fontTTF);
		fgInner.setStrokeCap(s.pen.cap);
		fgInner.setStrokeJoin(s.pen.join);
		fgFill.setStyle(Style.FILL_AND_STROKE);
		fgFill.setTypeface(fontTTF);
		fgFill.setFilterBitmap(true);
		fgFill.setStrokeWidth(fgInner.getStrokeWidth());
		fgFill.setStrokeCap(s.pen.cap);
		fgFill.setStrokeJoin(s.pen.join);
		fgOuter.setColor(s.pen.glowColour);
		//fgOuter.setAlpha(pen.alpha);
		fgOuter.setStrokeWidth(s.pen.glowWidth/* *zoom */);//*density
		if (s.pen.glowWidth>0) { 
			float blurRadius = s.pen.glowWidth/* *r.density  */;//*zoom
			//ctr++;
			//BlurMaskFilter.Blur blurTyp=BlurMaskFilter.Blur.values()[ctr%4];
			//Log.d(Globals.LOG_TAG, "blurRadius:"+blurRadius+" type:"+blurTyp);
			BlurMaskFilter.Blur blurTyp=BlurMaskFilter.Blur.NORMAL;
			BlurMaskFilter mBlur = new BlurMaskFilter(blurRadius, blurTyp);
			fgOuter.setMaskFilter(mBlur);
		} else {
			fgOuter.setMaskFilter(null);
		}
		fgOuter.setStyle(Style.STROKE);
		fgOuter.setTypeface(fontTTF);
		fgOuter.setStrokeCap(s.pen.cap);
		fgOuter.setStrokeJoin(s.pen.join);
		//fgOuter.setStrokeWidth(fgInner.getStrokeWidth());
		DashPathEffect dashPathEffect = null;
		if (s.pen.breakType!=BreakType.SOLID) {
			float[] arr=StrokeDecoration.getBreakTypeArray(s.pen.breakType);
			float[] dashArr = new float[arr.length];
			for (int i=0;i<arr.length;i++) {dashArr[i]=arr[i]*s.pen.strokeWidth/* *zoom */;}
			dashPathEffect = new DashPathEffect(dashArr, 0);
		}
		//fgOuter.setPathEffect(dashPathEffect);
		if (s.pen.rounding>0) {
			PathEffect pe = null;
			CornerPathEffect cpe = new CornerPathEffect(s.pen.rounding/* *zoom */);
			if (dashPathEffect!=null) {
				pe=new ComposePathEffect(cpe, dashPathEffect);
			} else {
				pe=cpe;
			}
			fgOuter.setPathEffect(pe);
			fgInner.setPathEffect(pe);
			fgFill.setPathEffect(pe);
		} else {
			fgOuter.setPathEffect(dashPathEffect);
			fgInner.setPathEffect(dashPathEffect);
			fgFill.setPathEffect(dashPathEffect);
		}
		setupTipType(s,s.pen.startTip,true);
		setupTipType(s,s.pen.endTip,false);
	}

	private void setupTipType(Stroke s,Tip et,boolean start) {//,float zoom
		if (et!=null && et.type!=Tip.Type.NONE) {
			et.setTmplPath();
			et.inner = new Paint(fgInner);
			et.outer = new Paint(fgOuter);
			if (et.filled) {
				et.inner.setStyle(et.filled?Style.FILL_AND_STROKE:Style.FILL);
				et.outer.setStyle(et.filled?Style.FILL_AND_STROKE:Style.FILL);
			}
			et.inner.setPathEffect(null);
			et.outer.setPathEffect(null);
			et.inner.setTypeface(null);
			et.outer.setTypeface(null);
			et.m = new Matrix();
			//float size = pen.strokeWidth;
			float size = 15;
			
			et.m.postScale(size*et.size/* *zoom */,size*et.size/* *zoom */);
			for (PointVec pv : s.points) {
				if (start) pv.startTip=makeTipPath(pv,s.pen.startTip,true,pv.startTip);
				else  pv.endTip=makeTipPath(pv,s.pen.endTip,false,pv.endTip);
				//pv.endTip=makeTipPath(pv,pen.endType,false,pv.endTip);
			}
		}
	}
	
	private Path makeTipPath( PointVec pv,Tip et,boolean start,Path p) {
		if (pv.size()>1 && !pv.closed) {
			float angle = 0;
			if (start) {
				_usePoint.set( pv.get(0).x,pv.get(0).y-5 );
				angle = (et.inside?PointUtil.PI:0)+-1*PointUtil.calcAngle2PI( pv.get(1), pv.get(0), _usePoint );
				et.m.postRotate( angle*180/PointUtil.PI );
				et.m.postTranslate( pv.get(0).x, pv.get(0).y );
				
			} else {
				_usePoint.set( pv.get(pv.size()-1).x,pv.get(pv.size()-1).y-5 );
				angle = (et.inside?PointUtil.PI:0)+-1*PointUtil.calcAngle2PI( pv.get(pv.size()-2), pv.get(pv.size()-1), _usePoint );
				et.m.postRotate( angle*180/PointUtil.PI );
				et.m.postTranslate( pv.get(pv.size()-1).x, pv.get(pv.size()-1).y );
				
			}
			//et.m.postScale(zoomLevel,zoomLevel);
			if (p==null) {p=new Path();}
			et.tmplPath.transform( et.m, p );
			if (et.closed) {p.close();}
			return p;
		}
		return null;
	}
	
	public void updateFill(Stroke s) {
		if (s.fill!=null) {
			if (s.fill.type==Fill.Type.NONE) {
				fgFill.setShader(null);
				fgInner.setStyle(Style.STROKE);
				fgOuter.setStyle(Style.STROKE);
			} else if (s.fill.type==Fill.Type.COLOUR_STROKE) {
				fgFill.setShader(null);
				fgInner.setStyle(Style.FILL_AND_STROKE);
				fgOuter.setStyle(Style.STROKE);//FILL_AND_
			} else {
				FillRenderer.populateFillPaint(s.fill,fgFill,s.calculatedBounds,s,r);
				
			}
		}
	}
}