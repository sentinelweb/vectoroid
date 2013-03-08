package co.uk.sentinelweb.views.draw.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.Log;
import co.uk.sentinelweb.views.draw.VecGlobals;
import co.uk.sentinelweb.views.draw.model.Drawing;
import co.uk.sentinelweb.views.draw.model.DrawingElement;
import co.uk.sentinelweb.views.draw.model.UpdateFlags;
import co.uk.sentinelweb.views.draw.model.ViewPortData;
import co.uk.sentinelweb.views.draw.render.ag.AndGraphicsRenderer;

public class BitmapUtil {
	private static final int MAX_OP_PXL = 10*1024*1024;//10MB
	
	static RectF _useRectF = new RectF();
	static Rect _useRect = new Rect();
	static PointF _usePointF = new PointF();
	
	public static Bitmap renderFull(DrawingElement drawing, AndGraphicsRenderer agr)	 {
		return renderFull( drawing,  agr,  null,0);
	}
	public static Bitmap renderFull(DrawingElement drawing, AndGraphicsRenderer agr,int wid,int hgt)	 {
		return renderFull( drawing,  agr,  new RectF(0,0,wid,hgt),0);
	}
	public static Bitmap renderFull(DrawingElement drawing, AndGraphicsRenderer agr,int wid,int hgt,int bg)	 {
		return renderFull( drawing,  agr,  new RectF(0,0,wid,hgt),bg);
	}
	public static Bitmap renderFull(DrawingElement drawing, AndGraphicsRenderer agr,RectF window)	 {
		return renderFull( drawing,  agr,  window,0);
	}
	public static Bitmap renderFull(DrawingElement de, AndGraphicsRenderer agr, RectF window,int bg)	 {
		if (agr==null) {
			agr = new AndGraphicsRenderer();
		}
		if (de.calculatedBounds==null) {
			de.update(true, agr, UpdateFlags.ALL_NOLISTENERS);
		}
		RectF srcRect = new RectF( 0, 0, de.calculatedBounds.width(), de.calculatedBounds.height() );
		RectF bounds = window!=null?window:srcRect;
		ViewPortData vpd = ViewPortData.getFragmentViewPort(de);
		float dWidth = bounds.width();
		float dHeight = bounds.height();
		RectF calculatedBounds = de.calculatedBounds;
		float xscaling = (float)dWidth / calculatedBounds.width();
		float yscaling = (float)dHeight / calculatedBounds.height();
		float scaling = Math.min( xscaling,  yscaling );
		PointF _tl = new PointF();
		_tl.set(0, 0);
		_tl.y=(dHeight/scaling - calculatedBounds.height())/-2+calculatedBounds.top;
		_tl.x=(dWidth/scaling - calculatedBounds.width())/-2+calculatedBounds.left;
		vpd.topLeft.set(_tl);
		vpd.zoom=scaling;
		agr.setVpd(vpd);
		//long st = System.currentTimeMillis();
		try {
			//if (!previewOnly) {
				Bitmap b = Bitmap.createBitmap((int)dWidth, (int)dHeight, Bitmap.Config.ARGB_8888);
				Canvas drawCanvas = new Canvas();
				if (bg!=0) {
					drawCanvas.drawColor(bg);
				}
				drawCanvas.setBitmap(b);
				agr.setCanvas(drawCanvas);
				de.update(true, agr , UpdateFlags.ALL_NOLISTENERS);// shouldnt have to do this
				
				agr.setupViewPort();
				agr.render(de);
				agr.revertViewPort();
				//setPublished(drawing, makeVisible);
			//}
				//Log.d(VecGlobals.LOG_TAG, "BitmapUtil.renderFull: large :"+(System.currentTimeMillis()-st));
				return b;
		} catch (OutOfMemoryError ome ) {
			Log.d(VecGlobals.LOG_TAG, "saveBitmaps: full : OME", ome);
		}
		return null;
	}
}
