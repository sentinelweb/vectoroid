package co.uk.sentinelweb.views.draw.view.drawable;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import org.xml.sax.InputSource;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.widget.ImageView.ScaleType;
import co.uk.sentinelweb.views.draw.VecGlobals;
import co.uk.sentinelweb.views.draw.file.svg.importer.SVGParser;
import co.uk.sentinelweb.views.draw.model.Drawing;
import co.uk.sentinelweb.views.draw.model.DrawingElement;
import co.uk.sentinelweb.views.draw.model.Stroke;
import co.uk.sentinelweb.views.draw.model.UpdateFlags;
import co.uk.sentinelweb.views.draw.model.ViewPortData;
import co.uk.sentinelweb.views.draw.model.Fill.Type;
import co.uk.sentinelweb.views.draw.render.ag.AndGraphicsRenderer;
import co.uk.sentinelweb.views.draw.util.OnAsyncListener;
import co.uk.sentinelweb.views.draw.util.PointUtil;
import co.uk.sentinelweb.views.draw.util.StrokeUtil;

/**
 * Static renderObject Caching:
 * one render is used statically in this class - which is actually a good thing since all the drawing objects in MyPOS are the same and stored in their own cache. 
 * So really one drawing per sizing then hopefully it doesn't resize the SVG everytime it draws. i think not though it seems to work  ...
 * 
 * For Memory Management: The static AndGraphicsRenderer renderer's cache still needs to be dropped on app destroy though. along with the relevant drawing Cache (SVGDrawingCache in mYPODs case).
 * 
 * TODO actually there is a big bug!! the static AGR is created each time the object is ? hmm maybe not acutually. check.
 * @author robert
 */
public class SVGDrawable extends Drawable {
	static AndGraphicsRenderer _agr = new AndGraphicsRenderer();;
	ScaleType _scaleType = ScaleType.CENTER_INSIDE;
	int opacity = 255;
	SVGDrawable _parent = null;
	PointF _tl;
	Drawing _drawing = null;
	Bitmap cache = null;
	Rect cachePadding = new Rect();
	Paint _cachePaint= null;
	
	Paint _testPaint= null;
	public boolean _debug = false;
	Rect _clipping = null;
	PointF _offset = new PointF();
	Modifier _modifier = null;
	Rect _intrinsicBounds = null;
	//Rect clipping  = null;
	RectF _useRectF = new RectF();
	Rect _useRect = new Rect();
	PointF _usePointF = new PointF();
	
	public static class Modifier {
		public UpdateFlags modify(Drawing d){return null;}
	}
	
	public SVGDrawable(InputStream is,Modifier modifier) {
		super();
		//this._agr = new AndGraphicsRenderer();//TODO move out
		this._modifier=modifier;
		loadDrawing(is);
		init(  );
	}
	
	public SVGDrawable(Drawing d,Modifier modifier) {
		super();
		//this._agr = new AndGraphicsRenderer();//TODO move out
		this._modifier=modifier;
		this._drawing=d;
		d.update(true, _agr, UpdateFlags.ALL);
		init(  );
	}
	public SVGDrawable(SVGDrawable root,Modifier modifier) {
		super();
		//this._agr = root._agr;
		this._parent=root;
		this._modifier=modifier;
		this._clipping=root._clipping;
		this._scaleType=root._scaleType;
		init();
	}
	
	private void init() {
		_tl=new PointF();
		this._clipping=new Rect(10,10,10,10);
		_testPaint = new Paint();
		_testPaint.setColor(Color.RED);
		_testPaint.setStyle(Style.STROKE);
		if (this._parent!=null) {
			_drawing = (Drawing)this._parent._drawing;//.duplicate();
			if (this._parent._intrinsicBounds!=null) {
				_intrinsicBounds=new Rect(this._parent._intrinsicBounds);
			}
			_offset=this._parent._offset;
		}
		if (_drawing!=null) {
			
		}
	}
	private DrawingElement getElement() {
		if (_drawing!=null && _drawing.elements.size()>0) {
			return _drawing.elements.get(0);
		}
		return null;
	}
	
	
	public Rect getIntrinsicBounds() {
		return this._intrinsicBounds;
	}

	public void setIntrinsicBounds(Rect defaultBounds) {
		if (this._intrinsicBounds==null) {this._intrinsicBounds=new Rect();}
		this._intrinsicBounds.set(defaultBounds);
		setBounds(this._intrinsicBounds);
	}
	public void setIntrinsicSquare(float defaultBounds) {
		if (this._intrinsicBounds==null) {this._intrinsicBounds=new Rect();}
		this._intrinsicBounds.set(0,0,(int)defaultBounds,(int)defaultBounds);
		setBounds(this._intrinsicBounds);
	}
	
	public PointF getOffset() {
		return this._offset;
	}

	public void setOffset(PointF offset) {
		this._offset.set(offset);
	}
	public void setOffset(float x,float y) {
		this._offset.set(x, y);
	}
	
	
	public ScaleType getScaleType() {
		return _scaleType;
	}

	public void setScaleType(ScaleType scaleType) {
		_scaleType = scaleType;
	}

	public Rect getClipping() {
		return this._clipping;
	}

	public void setClipping(Rect clipping) {
		if (this._clipping==null) {this._clipping=new Rect();}
		this._clipping = clipping;
	}

	@Override
	public void draw(Canvas canvas) {
		DrawingElement de = getElement();
		if (de!=null) {
			UpdateFlags flags = UpdateFlags.ALL;
			if (_modifier!=null ) {
				flags =_modifier.modify(_drawing);
			}
			_drawing.update(true, _agr, flags);
			Rect bounds = getBoundsRect();
			int dWidth = bounds.width();//-padding.left-padding.right // - getPaddingLeft() - getPaddingRight();
			int dHeight = bounds.height();//-padding.top-padding.bottom //getMeasuredHeight() - getPaddingTop() - getPaddingBottom();
			RectF calculatedBounds = de.calculatedBounds;
			float xscaling = 1;
			float yscaling = 1;
			//float scaling = 1;
			if (_scaleType==ScaleType.CENTER_INSIDE) {
				xscaling = (float)dWidth / calculatedBounds.width();
				yscaling = (float)dHeight / calculatedBounds.height();
				//scaling = Math.min( xscaling,  yscaling );
				xscaling= Math.min( xscaling,  yscaling );
				yscaling=xscaling;
			} else if (_scaleType==ScaleType.FIT_XY) {
				xscaling = (float)dWidth / calculatedBounds.width();
				yscaling = (float)dHeight / calculatedBounds.height();
				//scaling = Math.min( xscaling,  yscaling );
			}
			if (yscaling<0.99f || yscaling>1.01f || xscaling<0.99f || xscaling>1.01f) {
				if (_debug) 	Log.d(VecGlobals.LOG_TAG, "scaling vector:"+xscaling+","+yscaling);
				StrokeUtil.scale(_drawing, xscaling,yscaling, new RectF(), _agr);
				calculatedBounds = de.calculatedBounds;
				
			} else {
				if (_debug) 	Log.d(VecGlobals.LOG_TAG, "NOT scaling vector:"+xscaling+","+yscaling);
			}
			yscaling=xscaling=1;
			//scaling=1;
			//scaling = scaling*0.95f;
			ViewPortData vpd = ViewPortData.getFragmentViewPort(de);//ViewPortData.getFullDrawing(d);
			_tl.set(0, 0);
			_tl.y=(dHeight/yscaling - calculatedBounds.height())/-2+calculatedBounds.top-2;
			_tl.x=(dWidth/xscaling - calculatedBounds.width())/-2+calculatedBounds.left-2;
			if (bounds == _intrinsicBounds) {
				_useRectF.set(_intrinsicBounds);
				_usePointF.set(_intrinsicBounds.left,_intrinsicBounds.top);
				PointUtil.subVector(_tl, _tl, PointUtil.midpoint(_useRectF));//
				//_tl.x-=_offset.x;
				//_tl.y-=_offset.y;
			}
			_tl.x-=_offset.x;
			_tl.y-=_offset.y;
			// topleft is inverted, hence -=
			// mod for padding
			//_tl.y-=padding.top/scaling;
			//_tl.x-=padding.left/scaling;
			//if (_debug) {
			getPadding(_useRect);Rect padding =_useRect;
			String file = (String)_drawing.getNameSpaced("co.uk.sentinel", "file");
			if (_debug) Log.d(VecGlobals.LOG_TAG, "SVGDrawable.draw:src:"+file+" dim:"+dWidth +"x"+dHeight +" : "+ PointUtil.tostr(calculatedBounds)+" : scaling:"+xscaling+","+xscaling+":"+_drawing.elements.size()+":"+PointUtil.tostr(_tl));
			if (_debug) Log.d(VecGlobals.LOG_TAG, "SVGDrawable.draw:"+PointUtil.tostr(getBounds())+" tl:"+PointUtil.tostr(_tl)+":"+PointUtil.tostr(padding));
			//}
			vpd.topLeft.set(_tl);
			vpd.zoom=1;
			Rect clipbounds = null;
			if (_clipping!=null) {
				clipbounds = canvas.getClipBounds();
				canvas.clipRect(clipbounds.left-_clipping.left, clipbounds.top-_clipping.top, clipbounds.right+_clipping.right, clipbounds.bottom+_clipping.bottom);
			}
			_agr.setVpd(vpd);
			_agr.setCanvas(canvas);
			_agr.setupViewPort();
			_agr.render(de);
			_agr.revertViewPort();
			if (_clipping!=null) {
				canvas.clipRect(clipbounds.left, clipbounds.top, clipbounds.right, clipbounds.bottom);
			}
		}
	}
	private Rect getBoundsRect() {
		Rect bounds = getBounds();//new Rect(0,0,canvas.getWidth(),canvas.getHeight());
		if (bounds==null || bounds.width()<=0 ) {
			if (_intrinsicBounds!=null) {
				bounds = _intrinsicBounds; 
			} else {
				bounds = new Rect();
			}
		} //else {
			//_offset.set(0,0);
		//}
		return bounds;
	}
	
	@Override
	public int getOpacity() {
		return opacity;
	}

	@Override
	public void setAlpha(int alpha) {
		opacity=alpha;
	}

	@Override
	public void setColorFilter(ColorFilter cf) {
		// TODO Auto-generated method stub
		
	}
	
	private void loadDrawing(InputStream is) {
		try {
			InputSource isc = new InputSource(is);
			SVGParser svgp = new SVGParser();
			_drawing = svgp.parseSAX(isc);
			is.close();
			_drawing.update(true, _agr, UpdateFlags.ALL);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public Modifier getModifier() {
		return _modifier;
	}

	public void setModifier(Modifier modifier) {
		this._modifier = modifier;
	}
	
	@Override
	public int getIntrinsicWidth() {
		if (_intrinsicBounds!=null) {
			return _intrinsicBounds.width();
		}
	    return super.getIntrinsicWidth();
	}
	
	@Override
	public int getIntrinsicHeight() {
		if (_intrinsicBounds!=null) {
			return _intrinsicBounds.height();
		}
	    return super.getIntrinsicHeight();
	}
	
}
