package co.uk.sentinelweb.views.draw.model.path;

import java.util.ArrayList;

import co.uk.sentinelweb.views.draw.util.StrokeUtil;

import android.graphics.PointF;
import android.graphics.RectF;

public class Quartic extends PathData {
	//public Type type = Type.QUAD;
	public PointF control1 = new PointF();
	public Quartic(Quartic p) {
		super(p);
		type = Type.QUAD;
		this.control1.set(p.control1);
	}
	public Quartic(PointF p, PointF c1) {
		super(p);
		type = Type.QUAD;
		this.control1.set(c1);
	}
	public Quartic duplicate() {return new Quartic(this);}
	
	public final boolean computQuarticFrom( PathData lx) {
		if (lx.x-x!=0 || lx.y-y!=0) {
			if (boundsCheck==null) {
	        	boundsCheck=new ArrayList<PathData>();
	        	for (int i=0;i<5;i++) {boundsCheck.add(new PathData());}
	        }
	        StrokeUtil.genQuadric(this,lx);
	        return true;
		}
		return false;  
	}
	
	@Override
	public void computeBounds(PointF calculatedCOG, RectF calculatedBounds,RectF accum,PathData lastPathData) {
		if (lastPathData!=null && computQuarticFrom( lastPathData )) {
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