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
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RadialGradient;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.SweepGradient;
import android.util.Log;
import co.uk.sentinelweb.views.draw.DVGlobals;
import co.uk.sentinelweb.views.draw.model.Drawing;
import co.uk.sentinelweb.views.draw.model.DrawingElement;
import co.uk.sentinelweb.views.draw.model.Fill;
import co.uk.sentinelweb.views.draw.model.Stroke;
import co.uk.sentinelweb.views.draw.model.Stroke.Type;
import co.uk.sentinelweb.views.draw.render.ag.StrokeRenderer.TextLineHandler;
import co.uk.sentinelweb.views.draw.util.DebugUtil;
import co.uk.sentinelweb.views.draw.util.PointUtil;

public class FillRenderer {
	static PointF _usePoint1 = new PointF();
	static PointF _usePoint2 = new PointF();
	static Rect _useRect = new Rect();
	static RectF _useRectF = new RectF();
	public static Paint _maskPaint ;
	
	static {
		_maskPaint=new Paint();
		_maskPaint.setStyle(Style.FILL_AND_STROKE);
		_maskPaint.setAntiAlias(true);
		_maskPaint.setAlpha(255);
		_maskPaint.setColor(Color.WHITE);
		_maskPaint.setFilterBitmap(true);
		_maskPaint.setStrokeWidth(0);
		
	}
	
	public static void populateFillPaint(Fill fill,Paint fgFill, RectF calculatedBounds,DrawingElement de,final AndGraphicsRenderer agr) {
		//
		fgFill.setShader(null);
		fgFill.setStyle(Style.FILL_AND_STROKE);
		if (de instanceof Stroke) {
			StrokeRenderObject sro = (StrokeRenderObject)agr.renderObjects.get(de);
			if (sro!=null && sro.fgInner!=null) {fgFill.setStrokeWidth(sro.fgInner.getStrokeWidth());}
		}
		switch (fill.type) {
			case NONE:break;
			case COLOUR:
				if (calculatedBounds!=null) {
					//fgInner.setShader(new LinearGradient(calculatedBounds.left, 0, calculatedBounds.right, 0, fill.color,  fill.color, TileMode.CLAMP));
					//Log.d(Globals.LOG_TAG, "fill colour set:"+ColorUtil.toColorString(fill._color));
					fgFill.setColor(fill._color);
					//fgFill.setStyle(Style.FILL_AND_STROKE);
				}
				break;
			case GRADIENT:
				//float zoomLevel = agr.getVpd().zoom;
				if (fill._gradient!=null && calculatedBounds!=null) {
					if (fill._gradient.data!=null && fill._gradient.data.p1!=null) {
						_usePoint1.set(fill._gradient.data.p1);
					} else {_usePoint1.set(calculatedBounds.left, calculatedBounds.top);}
					if (fill._gradient.data!=null && fill._gradient.data.p2!=null) {
						_usePoint2.set(fill._gradient.data.p2);
					} else {_usePoint2.set(calculatedBounds.right, calculatedBounds.top);}
					switch (fill._gradient.type) {
						case LINEAR:
							fgFill.setShader(
									new LinearGradient(
											_usePoint1.x/* *zoomLevel */, 
											_usePoint1.y/* *zoomLevel */, 
											_usePoint2.x/* *zoomLevel */, 
											_usePoint2.y/* *zoomLevel */, 
											fill._gradient.colors,  
											fill._gradient.positions, 
											fill._gradient.tile
									)
								);
							break;
						case RADIAL:{
							float radius = PointUtil.dist(_usePoint1, _usePoint2);
							radius = radius>0?radius:0.01f;
							fgFill.setShader(
									new RadialGradient(
											_usePoint1.x/* *zoomLevel */, 
											_usePoint1.y/* *zoomLevel */, 
											radius/* *zoomLevel */,
											fill._gradient.colors,  
											fill._gradient.positions, 
											fill._gradient.tile
									)
								);
						}
						break;
						case SWEEP:
							fgFill.setShader(
									new SweepGradient(
											_usePoint1.x/* *zoomLevel */, 
											_usePoint1.y/* *zoomLevel */, 
											fill._gradient.colors,  
											fill._gradient.positions
									)
								);
							break;
					}
					//fgFill.setStyle(Style.FILL);
				} else {
					
				}
				break;
			case BITMAP:
				if ( fill._bitmapFill!=null ) {//fill._bitmapFill!=null&& !fill._bitmapFill.getName().equals(fill.bitmapMaskAssetName)
					Bitmap bitmap = fill._bitmapFill.getBitmap();
					//Log.d( DVGlobals.LOG_TAG, "fillrender: bitmap:"+bitmap );
					if (de instanceof Drawing) {
						DrawingRenderObject dro = (DrawingRenderObject)agr.renderObjects.get(de);
						//Log.d(DVGlobals.LOG_TAG, "drawing set bitmap :"+bitmap);
						dro.bitmapMask = bitmap;// may need to memscale
					} else if (de instanceof Stroke) {
						final StrokeRenderObject sro = (StrokeRenderObject)agr.renderObjects.get(de);
						Stroke s = (Stroke)de;
						if (sro.bitmapMask != null) {
							sro.bitmapMask.recycle();
							System.gc();
						}
						sro.bitmapMask = null;
						if ( bitmap!=null ) {
							try {
								//Log.d( Globals.LOG_TAG, "fillrender: dims"+bitmap.getWidth()+"x"+bitmap.getHeight()+" : "+calculatedBounds.width()+"x"+calculatedBounds.height() );
								//DebugUtil.logMemory("fillrender");
								
								float width =  calculatedBounds.width();
								float height =  calculatedBounds.height();
								//nessecary memory check
								int maxMem =1*1024*1024;//200*1024;//
								float memScale = 1;
								if (width*height>maxMem) {
									memScale = maxMem/(width*height); 
									width*=memScale;
									height*=memScale;
								}
								Log.d(DVGlobals.LOG_TAG, "mask bmp:"+s.hashCode()+" memScale:"+memScale);
								if (width<=0 || height<=0) {return;}
								final Bitmap maskBitmap = Bitmap.createBitmap( (int) width, (int) height, Bitmap.Config.ARGB_8888 );// set lowmem argb 4444
								final Canvas maskCanvas = new Canvas();
								_maskPaint.setXfermode(null);
								maskCanvas.setBitmap( maskBitmap );
								if ( sro.path!=null ) {
									maskCanvas.drawColor(0, PorterDuff.Mode.CLEAR );
									final Matrix m = new Matrix();
									final float scale = memScale; /* agr.getVpd().zoom* */// scale path back to normal size.
									//m.postScale( scale, scale );///s.renderObject.getZoomLevel()
									//m.postTranslate( -calculatedBounds.left*memScale, -calculatedBounds.top*memScale );
									 
									if (s.type!=Type.TEXT_TTF) {
										Path maskPath = new Path();
										m.postTranslate( -calculatedBounds.left, -calculatedBounds.top );
										m.postScale( memScale, memScale );  
										sro.path.transform( m, maskPath );
								    	maskCanvas.drawPath( maskPath, _maskPaint );
								    } else {
								    	_maskPaint.setTypeface(sro.fgInner.getTypeface());
								    	_maskPaint.setTextSize(sro.fgInner.getTextSize()*scale);  
								    	_maskPaint.setTextScaleX(sro.fgInner.getTextScaleX());
								    	_maskPaint.setStyle(Style.FILL_AND_STROKE);
								    	final Path maskPath = new Path(sro.path);
								    	//sro.path.transform( new Matrix(), maskPath );
								    	DebugUtil.logCall("textXscale:paint:"+sro.fgInner.getTextScaleX()+": val:"+s.textXScale, new Exception());
								    	final StrokeRenderer sr = agr.sr.getStrokeRenderer();
										sr.processText(s, maskPath, new TextLineHandler() {   
											@Override
											public void onLoop(Stroke stroke, String s) {
												//maskCanvas.translate(-stroke.calculatedBounds.left, -stroke.calculatedBounds.top);
												//maskCanvas.scale( scale, scale );
												if (stroke.points.size()==1 && (sro.textAngle<StrokeRenderer.MIN_ROTATION|| sro.textAngle>2*Math.PI-StrokeRenderer.MIN_ROTATION)) {
													//maskCanvas.drawText(s, sr._textPoint.x, sr._textPoint.y*scale, _maskPaint);
													maskCanvas.drawText(s, 
															(sr._textPoint.x-stroke.calculatedBounds.left)*scale, 
															(sr._textPoint.y-stroke.calculatedBounds.top)*scale,
															_maskPaint);
													/*
													Log.d(DVGlobals.LOG_TAG, "textMask.onLoop:no rotation:"+PointUtil.tostr(sr._textPoint)+": converted"+
															(sr._textPoint.x-stroke.calculatedBounds.left)*scale+" : "+
															(sr._textPoint.y-stroke.calculatedBounds.top)*scale
													);
													Log.d(DVGlobals.LOG_TAG, "textMask.onLoop:dims:maskBitmap:"+maskBitmap.getWidth()+"x"+maskBitmap.getHeight()+
															" sro.textCalcwidth="+sro.textCalcwidth+" measured width: "+_maskPaint.measureText(s)
															);
															*/
												} else {
													// still needs mem scaling
													maskCanvas.translate(-stroke.calculatedBounds.left, -stroke.calculatedBounds.top);
													//maskCanvas.scale( scale, scale );
													sr._textPath.computeBounds(_useRectF, true);
													maskCanvas.drawTextOnPath(s, sr._textPath, 0,0, _maskPaint);
													Log.d(DVGlobals.LOG_TAG, "textMask.onLoop:rotation: path bounds"+PointUtil.tostr(_useRectF));
													//maskCanvas.scale( 1/scale, 1/scale );
													maskCanvas.translate(stroke.calculatedBounds.left, stroke.calculatedBounds.top);
												}
												//maskCanvas.scale( 1/scale, 1/scale );
												//maskCanvas.translate(stroke.calculatedBounds.left, stroke.calculatedBounds.top);
											}
										});
								    }
									_maskPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.MULTIPLY));
								}
								
								Matrix scmat = new Matrix();
								scmat.postScale(width/bitmap.getWidth(),height/bitmap.getHeight());
								maskCanvas.drawBitmap( bitmap, scmat, _maskPaint );
								sro.bitmapMask =  maskBitmap ;
								fill.bitmapMaskAssetName=fill._bitmapFill.getName();
								//Log.d( Globals.LOG_TAG, "fillrender: rendered:"+sro.bitmapMask.getWidth() );
							} catch (OutOfMemoryError e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					}
				}
				fgFill.setAlpha( fill._bitmapAlpha );
				fgFill.setStyle(Style.FILL);
				break;
		}
	}
	
	public static void drawBackground(AndGraphicsRenderer agr, Drawing d) {
		DrawingRenderObject dro = (DrawingRenderObject)agr.renderObjects.get(d);
		//Log.d(DVGlobals.LOG_TAG, "drawingBg: dro"+dro+" type:"+d.background.type+" bounds:"+PointUtil.tostr(d.calculatedBounds));
		if (dro!=null && dro.bgFill!=null) {
			_useRectF.set(agr.getVpd().zoomSrcRect);
			_useRectF.intersect(0,0,d.size.x/* *agr.getVpd().zoom*/,d.size.y/* *agr.getVpd().zoom*/);
			Fill fill = d.background;
			//Log.d(DVGlobals.LOG_TAG, "drawingBg1: drofill"+dro.bgFill+"");
			if (fill.type==Fill.Type.BITMAP) {
				//Log.d(DVGlobals.LOG_TAG, "drawingBg1: drofill"+dro.bitmapMask+"");
				if (dro.bitmapMask!=null ) {//&& stroke.fill.bitmap.get()!=null
					//Bitmap bitmap = fill.bitmapMask;
					//_useRect.set(0, 0, bitmap.getWidth(), bitmap.getHeight());
					agr.getCanvas().drawBitmap(dro.bitmapMask,0,0, dro.bgFill);
				} else {
					//agr.getCanvas().drawColor(Color.argb(255, 0, 0, 0));
					//StrokeRenderer sg = agr.sr.
					agr.getCanvas().drawRect(0,0,d.size.x/* *agr.getVpd().zoom*/,d.size.y/* *agr.getVpd().zoom*/, agr.sr.getStrokeRenderer().noImgPaint);
				}
			} else if (fill.type!=Fill.Type.NONE) {
				agr.getCanvas().drawRect(0,0,d.size.x/* *agr.getVpd().zoom*/,d.size.y/* *agr.getVpd().zoom*/, dro.bgFill);
			}
		} else {
			//agr.getCanvas().drawColor(Color.argb(255, 128, 128, 128));
			agr.getCanvas().drawRect(0,0,d.size.x/* *agr.getVpd().zoom*/,d.size.y/* *agr.getVpd().zoom*/, agr.sr.getStrokeRenderer().noImgPaint);
		}
	}
}
