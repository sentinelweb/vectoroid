package co.uk.sentinelweb.views.draw.model.path;

import android.graphics.PointF;

public class PathData extends PointF {
	// why do this? hopefully its more efficient in inner loops -- but its just a guess really.
	// but at least i can drop through a switch statement
	public enum Type {POINT,BEZIER,ARC,QUAD}
	public Type type = Type.POINT;
	public Float pressure;
	public Integer timeDelta;// offset from the start time
	
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
}
	