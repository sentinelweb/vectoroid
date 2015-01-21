package co.uk.sentinelweb.views.draw.model.path;

import java.util.ArrayList;

import android.graphics.PointF;
import android.graphics.RectF;
import co.uk.sentinelweb.views.draw.util.StrokeUtil;

public class Bezier extends PathData {
	//public Type 
	public PointF control1 = new PointF();
	public PointF control2 = new PointF();
	public Bezier(PathData p) {
		super(p);
		type = Type.BEZIER;
	}
	public Bezier(PointF p,PointF c1,PointF c2) {
		super(new PathData(p));
		type = Type.BEZIER;
		this.control1.set(c1);
		this.control2.set(c2);
	}
	public Bezier(Bezier p) {
		super(p);
		type = Type.BEZIER;
		this.control1.set(p.control1);
		this.control2.set(p.control2);
	}
	public Bezier duplicate() {return new Bezier(this);}
	public final boolean computBezierFrom( PointF lx) {
		if (lx.x-x!=0 || lx.y-y!=0) {
			if (boundsCheck==null) { 
	        	boundsCheck=new ArrayList<PathData>();
	        	for (int i=0;i<5;i++) {boundsCheck.add(new PathData());}
	        }
	        StrokeUtil.genBezier(this,lx);
	        return true;
		}
		return false;
	}
	@Override
	public void computeBounds(PointF calculatedCOG, RectF calculatedBounds,RectF accum,PathData lastPathData) {
		if (lastPathData!=null && computBezierFrom( lastPathData )) {
			accum.set(1e8f,1e8f,-1e8f,-1e8f);//bounds accumulator
			for (PathData boundsPoint : boundsCheck) {
				super.incrementBounds((PointF)null, accum, boundsPoint);
			}
			super.incrementBounds(calculatedCOG, calculatedBounds, accum);
		} else {
			super.incrementBounds(calculatedCOG, calculatedBounds,this);
		}
	}
}
	