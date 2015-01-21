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
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.Typeface;

import co.uk.sentinelweb.views.draw.model.StrokeDecoration.Tip;
import co.uk.sentinelweb.views.draw.model.StrokeDecoration.Tip.Type;
import co.uk.sentinelweb.views.draw.model.Fill;
import co.uk.sentinelweb.views.draw.model.PointVec;
import co.uk.sentinelweb.views.draw.model.Stroke;

public class StrokeRenderer {
	public static final float MIN_ROTATION = 5f*(float)Math.PI/180f;
	Rect _useRect = new Rect();
	
	public Paint noImgPaint = new Paint();
	Paint dbgpaint = new Paint();
	Paint _textPaint = new Paint();
	Path _textPath = new Path();
	PointF _textPoint = new PointF();
	Matrix _textMatrix = new Matrix(); 
	AndGraphicsRenderer renderer;
	//Context c;
	
	public StrokeRenderer(AndGraphicsRenderer renderer) {//Context context,
		//BitmapDrawable tspdrawable = (BitmapDrawable)context.getResources().getDrawable(R.drawable.ge_tsp_bg); 
		//noImgPaint.setShader(new BitmapShader(tspdrawable.getBitmap(), TileMode.REPEAT, TileMode.REPEAT));
		noImgPaint.setColor(Color.RED);
		noImgPaint.setStyle(Style.FILL_AND_STROKE);
		this.renderer=renderer;
		dbgpaint.setColor(Color.RED);
		dbgpaint.setStrokeWidth(1);
		dbgpaint.setStyle(Style.STROKE);
	}
	
	public void render(Canvas canvas , Stroke stroke) {
		StrokeRenderObject sro = (StrokeRenderObject)renderer.getObject(stroke);
		if (sro==null||sro.dontDisplay) {return;}
		//TODO check this works looks like bounds test is failing at high zoom levels >400
		if (stroke.calculatedBounds.width()*renderer.getVpd().zoom<0.5 && stroke.calculatedBounds.height()*renderer.getVpd().zoom<0.5) {
			// Log.d(DVGlobals.LOG_TAG, "Skipping ..."+PointUtil.tostr(stroke.calculatedBounds)+" x "+renderer.getVpd().zoom +": w:"+(stroke.calculatedBounds.width()*renderer.getVpd().zoom)+" h:"+(stroke.calculatedBounds.height()*renderer.getVpd().zoom));
			return;
		}
		if (stroke.type!= Stroke.Type.TEXT_TTF) {
			if (!sro.fgOnly) {
				if (sro.fgOuter.getStrokeWidth()>0) {
					canvas.drawPath(sro.path,sro.fgOuter);
					renderTip(canvas, stroke, stroke.pen.startTip,true, false,sro);
					renderTip(canvas, stroke, stroke.pen.endTip,false, false,sro);
				}
				if (stroke.fill.type!= Fill.Type.NONE && stroke.fill.type!= Fill.Type.COLOUR_STROKE && stroke.fill.type!= Fill.Type.BITMAP) {
					canvas.drawPath(sro.path, sro.fgFill);
				} else if (stroke.fill.type== Fill.Type.BITMAP) {
					if (!renderBitmap(canvas, stroke,sro)) {
						canvas.drawPath(sro.path, noImgPaint);
					}
				}
			}
			if (sro.fgInner.getStrokeWidth()*renderer.getVpd().zoom>0.05 ) {
				canvas.drawPath(sro.path, sro.fgInner);
				renderTip(canvas, stroke, stroke.pen.startTip, true, true,sro);
				renderTip(canvas, stroke, stroke.pen.endTip, false, true,sro);
			}
		} else if ( stroke.text!=null && !"".equals(stroke.text)) {
			if (stroke.calculatedBounds!=null) {
				if (stroke.fill.type== Fill.Type.BITMAP) {
					if (!sro.fgOnly) {
						processText(stroke,sro.path, strokeBgRenderHandler);
						if (stroke.fill.type!= Fill.Type.NONE && stroke.fill.type!= Fill.Type.COLOUR_STROKE && stroke.fill.type!= Fill.Type.BITMAP) {
							processText(stroke,sro.path, strokeFillRenderHandler);
						} else if (stroke.fill.type== Fill.Type.BITMAP) {
							if (!renderBitmap(renderer.getCanvas(), stroke,sro)) {
								noImgPaint.setTypeface(sro.fgFill.getTypeface());
								noImgPaint.setTextSize(sro.fgFill.getTextSize());
								noImgPaint.setTextScaleX(sro.fgFill.getTextScaleX());
								noImgPaint.setStrokeWidth(sro.fgFill.getStrokeWidth());
								noImgPaint.setStyle(sro.fgFill.getStyle());
								processText(stroke,sro.path, strokeNoImgRenderHandler);
							}
						}
					}
					processText(stroke,sro.path, strokeFgRenderHandler);
				} else {
					processText(stroke,sro.path, strokeRenderHandler);
				}
			}
		}
	}
	/*
	private String fillStr(Paint fgFill) {
		return "text render:"+fgFill.getStyle()+" stroke:"+fgFill.getStrokeWidth()+": scale"+fgFill.getTextScaleX()+" size:"+fgFill.getTextSize();
	}
	*/
	private void renderTip(Canvas canvas, Stroke s,Tip et,boolean start,boolean inner,StrokeRenderObject renderObject) {
		if (et!=null && et.type!=Type.NONE) {
			for (PointVec pv : s.points) {
				Path p =start?renderObject.startTips.get(pv):renderObject.endTips.get(pv);
				if (p!=null) {
					canvas.drawPath( p,inner?et.inner: et.outer ); 
				}
			}
		}
	}

	private boolean renderBitmap(Canvas canvas, Stroke stroke,StrokeRenderObject renderObject) {
		if (renderObject.bitmapMask!=null ) {
			_useRect.set(0,0 ,  renderObject.bitmapMask.getWidth(),renderObject.bitmapMask.getHeight());
			canvas.drawBitmap( renderObject.bitmapMask, _useRect, stroke.calculatedBounds, renderObject.fgFill);
			//Log.d(VecGlobals.LOG_TAG, "stroke bmp: id="+stroke.hashCode()+" bnds:"+PointUtil.tostr(stroke.calculatedBounds)+" mask:"+PointUtil.tostr(_useRect));
			return true;
		}
		return false;
	}
	
	public interface TextLineHandler {
		public void onLoop(Stroke stroke, String s);
	}
	
	public void processText(Stroke stroke, Path thePath,TextLineHandler loopMethod) {
		StrokeRenderObject sro = (StrokeRenderObject)renderer.getObject(stroke);
		if (sro.splitStr!=null) {
			for (int i = 0 ; i< sro.splitStr.length;i++) {
				float heightOffset = -sro.fgInner.getTextSize()*(sro.splitStr.length-1-i);
				float xstart = 0;
				float ystart = heightOffset;
				if (stroke.points.size()>=2 || (sro.textAngle>=MIN_ROTATION && sro.textAngle<=2*Math.PI-MIN_ROTATION)) {//
					xstart = -(float)Math.sin(sro.textAngle)*heightOffset;
					ystart = (float)Math.cos(sro.textAngle)*heightOffset;
					_textMatrix.reset();
					_textMatrix.postTranslate(xstart,ystart);
					_textPath.reset();
					thePath.transform(_textMatrix, _textPath);
					//Log.d(VecGlobals.LOG_TAG, "angle:"+ sro.textAngle);
				} else {
					_textPoint.set(stroke.calculatedBounds.left+xstart,stroke.calculatedBounds.bottom+ystart);
					//Log.d(VecGlobals.LOG_TAG, "level"+ sro.textAngle);
				}
				loopMethod.onLoop(stroke,  sro.splitStr[i]);
			}
		}
	}
	
	
	private TextLineHandler strokeBgRenderHandler = new TextLineHandler() {
		@Override
		public void onLoop(Stroke stroke,String s) {
			StrokeRenderObject sro = (StrokeRenderObject)renderer.getObject(stroke);
			if (sro==null||sro.dontDisplay) {return;}
			if (stroke.pen.glowWidth>0) {
				if (stroke.points.size()==1 && (sro.textAngle<MIN_ROTATION|| sro.textAngle>2*Math.PI-MIN_ROTATION)) {
					renderer.getCanvas().drawText(s,_textPoint.x,_textPoint.y ,sro.fgOuter);
				} else {renderer.getCanvas().drawTextOnPath(s, _textPath, 0,0, sro.fgOuter);}
			}
		}
	};
	
    private TextLineHandler strokeFillRenderHandler = new TextLineHandler() {
		@Override
		public void onLoop(Stroke stroke,String s) {
			StrokeRenderObject sro = (StrokeRenderObject)renderer.getObject(stroke);
			if (sro==null||sro.dontDisplay) {return;}
			if (stroke.points.size()==1 && (sro.textAngle<MIN_ROTATION|| sro.textAngle>2*Math.PI-MIN_ROTATION)) {
				renderer.getCanvas().drawText(s,_textPoint.x,_textPoint.y ,sro.fgFill);
			} else {renderer.getCanvas().drawTextOnPath(s, _textPath, 0,0, sro.fgFill);}
		}
	};
	private TextLineHandler strokeNoImgRenderHandler = new TextLineHandler() {
		@Override
		public void onLoop(Stroke stroke,String s) {
			StrokeRenderObject sro = (StrokeRenderObject)renderer.getObject(stroke);
			if (sro==null||sro.dontDisplay) {return;}
			if (stroke.points.size()==1 && (sro.textAngle<MIN_ROTATION|| sro.textAngle>2*Math.PI-MIN_ROTATION)) {
				renderer.getCanvas().drawText(s,_textPoint.x,_textPoint.y ,noImgPaint);
			} else {renderer.getCanvas().drawTextOnPath(s, _textPath, 0,0, noImgPaint);}
		}
	};

	private TextLineHandler strokeFgRenderHandler = new TextLineHandler() {
		@Override
		public void onLoop(Stroke stroke,String s) {
			StrokeRenderObject sro = (StrokeRenderObject)renderer.getObject(stroke);
			if (sro==null||sro.dontDisplay) {return;}
			if (sro.fgInner.getStrokeWidth()>0) {
				if (stroke.points.size()==1 && (sro.textAngle<MIN_ROTATION|| sro.textAngle>2*Math.PI-MIN_ROTATION)) {
					renderer.getCanvas().drawText(s,_textPoint.x,_textPoint.y ,sro.fgInner);
				} else {
					renderer.getCanvas().drawTextOnPath(s, _textPath, 0,0, sro.fgInner);
				}
			}
		}
	};
	
	private TextLineHandler strokeRenderHandler = new TextLineHandler() {
		@Override
		public void onLoop(Stroke stroke,String s) {
			StrokeRenderObject sro = (StrokeRenderObject)renderer.getObject(stroke);
			if (sro==null||sro.dontDisplay) {return;}
			if (!sro.fgOnly) {
				if (stroke.pen.glowWidth>0) {
					if (sro.textAngle<MIN_ROTATION|| sro.textAngle>2*Math.PI-MIN_ROTATION) {
						renderer.getCanvas().drawText(s,_textPoint.x,_textPoint.y ,sro.fgOuter);
					} else {
						renderer.getCanvas().drawTextOnPath(s, _textPath, 0,0, sro.fgOuter);
					}
				}
				if (stroke.fill.type!= Fill.Type.NONE && stroke.fill.type!= Fill.Type.COLOUR_STROKE) {
					if (sro.textAngle<MIN_ROTATION|| sro.textAngle>2*Math.PI-MIN_ROTATION) {
						renderer.getCanvas().drawText(s,_textPoint.x,_textPoint.y ,sro.fgFill);
					} else {
						renderer.getCanvas().drawTextOnPath(s, _textPath, 0,0, sro.fgFill);
					}
				}
			}
			if (sro.fgInner.getStrokeWidth()>0) {
				if (stroke.points.size()==1 && (sro.textAngle<MIN_ROTATION|| sro.textAngle>2*Math.PI-MIN_ROTATION)) {
					renderer.getCanvas().drawText(s,_textPoint.x,_textPoint.y ,sro.fgInner);
				} else {
					renderer.getCanvas().drawTextOnPath(s, _textPath, 0,0, sro.fgInner);
				}
				// baseline debugging
//				PathData st = stroke.points.get(0).get(0);
//				PathData ed = stroke.points.get(0).get(1);
//				PathData ed1 = stroke.points.get(0).get(2);
//				PathData ed2 = stroke.points.get(0).get(3);
//				renderer.getCanvas().drawLine(st.x, st.y,ed.x, ed.y, dbgpaint);
//				renderer.getCanvas().drawLine(ed.x, ed.y,ed1.x, ed1.y, dbgpaint);
//				renderer.getCanvas().drawLine(ed1.x, ed1.y,ed2.x, ed2.y, dbgpaint);
			}
			
		}
	};
	
    private TextLineHandler strokeSelRenderHandler = new TextLineHandler() {
		
		@Override
		public void onLoop(Stroke stroke,String s) {
			StrokeRenderObject sro = (StrokeRenderObject)renderer.getObject(stroke);
			if (sro==null||sro.dontDisplay) {return;}
			if (stroke.points.size()==1 && (sro.textAngle<MIN_ROTATION|| sro.textAngle>2*Math.PI-MIN_ROTATION)) {
				renderer.getCanvas().drawText(s,_textPoint.x,_textPoint.y ,_textPaint);
			} else {
				renderer.getCanvas().drawTextOnPath(s, _textPath, 0,0, _textPaint);
			}
		}
	};
	
	public void renderSeleceted(Canvas canvas , Stroke stroke,final Paint p) {
		StrokeRenderObject sro = (StrokeRenderObject) renderer.getObject(stroke);
		if (stroke.type!= Stroke.Type.TEXT_TTF) {
			canvas.drawPath(sro.path, p);
		} else {
			if (stroke.calculatedBounds!=null) {
				if (sro.fgInner.getTypeface()!=null) {
					p.setTypeface( sro.fgInner.getTypeface());
				} else {
					p.setTypeface( Typeface.DEFAULT);
				}
				p.setTextSize( sro.fgInner.getTextSize());
				p.setTextScaleX(sro.fgInner.getTextScaleX());
				_textPaint.set(p);
				processText(stroke,sro.path, strokeSelRenderHandler);
			}
		}
	}
}
