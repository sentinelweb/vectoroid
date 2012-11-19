package co.uk.sentinelweb.views.draw.model.path;

import java.util.ArrayList;

import co.uk.sentinelweb.views.draw.util.PointUtil;
import android.graphics.PointF;
import android.graphics.RectF;

public class PathData extends PointF {
	// why do this? hopefully its more efficient in inner loops -- but its just a guess really.
	// but at least i can drop through a switch statement
	public enum Type {POINT,BEZIER,ARC,QUAD}
	public Type type = Type.POINT;
	public Float pressure;
	public Integer timeDelta;// offset from the start time
	public ArrayList<PathData> boundsCheck = null;
	
	public PathData() {
		super();
	}
	
	public PathData(PathData p) {
		super(p.x,p.y);
		this.pressure=p.pressure;
		this.timeDelta=p.timeDelta;
	}
	
	public PathData(PointF p) {
		super(p.x,p.y);
	}
	
	public PathData(float x, float y) {
		super(x,y);
	}

	public PathData duplicate() {return new PathData(this);}
	
	public void computeBounds(PointF calculatedCOG, RectF calculatedBounds,RectF internalBounds, PathData last){
		incrementBounds(calculatedCOG, calculatedBounds,this);
	}
	
	protected static void incrementBounds(PointF calculatedCOG, RectF calculatedBounds, PathData p){
		if (calculatedCOG!=null) {
			calculatedCOG.x += p.x;
			calculatedCOG.y += p.y;
		}
		calculatedBounds.top=Math.min(calculatedBounds.top, p.y);
		calculatedBounds.left=Math.min(calculatedBounds.left, p.x);
		calculatedBounds.bottom=Math.max(calculatedBounds.bottom, p.y);
		calculatedBounds.right=Math.max(calculatedBounds.right, p.x);
	}
	
	protected static void incrementBounds(PointF calculatedCOG, RectF calculatedBounds,RectF internalBounds) {
		if (calculatedCOG!=null) {
			PointF mid = PointUtil.midpoint(internalBounds);
			calculatedCOG.x += mid.x;
			calculatedCOG.y += mid.y;
		}
		calculatedBounds.top=Math.min(calculatedBounds.top, internalBounds.top);
		calculatedBounds.left=Math.min(calculatedBounds.left,  internalBounds.left);
		calculatedBounds.bottom=Math.max(calculatedBounds.bottom,internalBounds.bottom);
		calculatedBounds.right=Math.max(calculatedBounds.right, internalBounds.right);
	}
}
	