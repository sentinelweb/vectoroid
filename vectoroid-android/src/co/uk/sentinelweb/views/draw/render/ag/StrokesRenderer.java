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
import android.graphics.RectF;
import android.util.Log;
import co.uk.sentinelweb.views.draw.DVGlobals;
import co.uk.sentinelweb.views.draw.model.DrawingElement;
import co.uk.sentinelweb.views.draw.model.Group;
import co.uk.sentinelweb.views.draw.model.Stroke;
import co.uk.sentinelweb.views.draw.util.BoundsUtil;
import co.uk.sentinelweb.views.draw.util.PointUtil;

public class StrokesRenderer {
	StrokeRenderer strokeRenderer;
	RectF useRect = new RectF();
	AndGraphicsRenderer r;
	//private ViewPort viewPort;
	public boolean _debug=DVGlobals._isDebug && false;
	public StrokesRenderer(Context c, AndGraphicsRenderer r) {
		super();
		this.r = r;
		strokeRenderer = new StrokeRenderer(c,r);
		
	}

	// draw a vector of strokes
	public void drawStrokes(Canvas canvas,ArrayList<DrawingElement> strokes) {//, boolean drawselected, boolean noTest
		for (int i=0;i<strokes.size();i++) {
			DrawingElement de = strokes.get(i);
			if (!de.visible) {continue;}
			if (de instanceof Stroke) {
				drawStroke(canvas, (Stroke) de );
			} else if (de instanceof Group) {
				Group g = (Group)de;
				for (Stroke s : g.getStrokes() ) {
					drawStroke(canvas, s );
				}
			} 
		}
	}
	// draw a single stroke
	public void drawStroke(Canvas canvas, Stroke stroke) {//, boolean drawselected, boolean noTest
		if (stroke.points.size()>0) {
			useRect.set(r.getVpd().zoomCullingRectF);
			if (_debug) {Log.d(DVGlobals.LOG_TAG, "viewPort:"+PointUtil.tostr(useRect));}
			//TODO check this works, looks like bounds test is failing at high zoom levels >400
			boolean checkBoundsIntersect = BoundsUtil.checkBoundsIntersect(useRect, stroke.calculatedBounds, Math.max(stroke.pen.strokeWidth, stroke.pen.glowWidth) );
			if (_debug) {Log.d(DVGlobals.LOG_TAG, "checkBoundsIntersect:"+checkBoundsIntersect+":"+PointUtil.tostr(useRect)+" :: "+":"+PointUtil.tostr(stroke.calculatedBounds));}
			if (checkBoundsIntersect) {
				strokeRenderer.render(canvas, stroke);
				if (_debug) {Log.d(DVGlobals.LOG_TAG, "inbounds:");}
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
