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
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import co.uk.sentinelweb.views.draw.util.PointUtil;

public class ViewPortData {
	public PointF topLeft=new PointF(-1,-1);// the drawing viewport topleft in drawing pixels, TOPLEFT IS NEGATED!!!
	public float zoom=-1;// zoom: drawingpixels*zoom=screenPixels
	public float minZoom=1; // the minimum zoom for the drawing viewport, claculated in calclayout
	public float referenceZoom=1;// reference zoo use when zooming in / out in multitouch
	
	public PointF minTopLeft=new PointF(0,0);
	public PointF maxBottomRight=new PointF(0,0);
	public RectF drawArea=new RectF();// the area onscreen of the drawing page.
	public PointF drawAreaTopLeft=new PointF(0,0);
	
	public Rect zoomSrcRect=new Rect(0,0,0,0);
	public RectF zoomSrcRectF=new RectF(0f,0f,0f,0f);
	public Rect zoomTgtRect=new Rect(0,0,0,0);
	public RectF zoomCullingRectF=new RectF(0f,0f,0f,0f);
	
	public PointF topLeftReference=new PointF(0,0);// the reference drawing viewport topleft in drawing pixels
	public PointF widthHeightReference=new PointF(0,0);// the screen width x height in drawing pixels
	public PointF screenWidthHeightReference=new PointF(0,0);// the real screen dimensions in pixels
	
	public ViewPortData(){
		
	}
	
	public ViewPortData(PointF topLeft, float zoom, float minZoom,
			float referenceZoom, PointF minTopLeft, PointF maxBottomRight,
			RectF drawArea, Rect zoomSrcRect, RectF zoomSrcRectF,
			Rect zoomTgtRect, Rect magSrcRect, RectF magTgtRect,
			PointF topLeftReference, PointF widthHeightReference) {
		this.topLeft = topLeft;
		this.zoom = zoom;
		this.minZoom = minZoom;
		this.referenceZoom = referenceZoom;
		this.minTopLeft = minTopLeft;
		this.maxBottomRight = maxBottomRight;
		this.drawArea = drawArea;
		this.zoomSrcRect = zoomSrcRect;
		this.zoomSrcRectF = zoomSrcRectF;
		this.zoomTgtRect = zoomTgtRect;
		this.topLeftReference = topLeftReference;
		this.widthHeightReference = widthHeightReference;
	}
	
	public static ViewPortData getFullDrawing(Drawing drawing){
		ViewPortData vpd = new ViewPortData();
		vpd.zoom=1;
		vpd.zoomSrcRect.set(0,0,(int)drawing.size.x,(int)drawing.size.y);
		vpd.zoomSrcRectF.set(vpd.zoomSrcRect);
		vpd.zoomCullingRectF.set(vpd.zoomSrcRectF);// maybe shuld just be huge?
		return vpd;
	}
	
	public static ViewPortData getFragmentViewPort(DrawingElement de){// should be getElementViewPort
		ViewPortData vpd = new ViewPortData();
		vpd.zoom=1;
		vpd.zoomSrcRect.set((int)de.calculatedBounds.left,(int)de.calculatedBounds.top,(int)de.calculatedBounds.right,(int)de.calculatedBounds.bottom);//0,0,(int)de.calculatedBounds.width(),(int)de.calculatedBounds.height()
		vpd.zoomSrcRectF.set(vpd.zoomSrcRect);
		vpd.topLeft.set(de.calculatedBounds.left,de.calculatedBounds.top);
		vpd.zoomCullingRectF.set(vpd.zoomSrcRectF);// maybe shuld just be huge?
		return vpd;
	}
	
	public static ViewPortData getFromBounds(RectF r){
		ViewPortData vpd = new ViewPortData();
		vpd.zoom=1;
		vpd.zoomSrcRect.set((int)r.left, (int)r.top, (int)r.right, (int)r.bottom);
		vpd.zoomSrcRectF.set(vpd.zoomSrcRect);
		vpd.topLeft.set(r.left,r.top);
		vpd.zoomCullingRectF.set(vpd.zoomSrcRectF);// maybe shuld just be huge?
		return vpd;
	}
	
	public void zoomOutVpd( float mult,int width, int height) {// this only works for centered vp
		float origZoom=zoom;
		zoom=zoom*mult;
		//width/=mult;
		//height/=mult;
		widthHeightReference.set(width,height);
		PointF _usePoint = new PointF();
		_usePoint.set(width, height);
		PointUtil.mulVector(_usePoint, _usePoint, 1/zoom);
		PointUtil.subVector(widthHeightReference, _usePoint, _usePoint);// new win size
		PointUtil.mulVector(_usePoint, _usePoint, 0.5f);// centering offset
		PointUtil.addVector(topLeft, topLeft, _usePoint);
		_usePoint.set(width*mult, height*mult);
		PointUtil.addVector(topLeft, topLeft, _usePoint);
		
		//topLeft.set(width*(1+1/zoom)/2f,height*(1+1/zoom)/2f);
		zoomSrcRectF.set(
				topLeft.x,
				topLeft.y,
				(topLeft.x+width/zoom),
				(topLeft.y+height/zoom)
			);
		zoomCullingRectF.set(zoomSrcRectF);
		zoomSrcRect.set((int)zoomSrcRectF.left,(int)zoomSrcRectF.top,(int)zoomSrcRectF.right,(int)zoomSrcRectF.bottom);
	}
}