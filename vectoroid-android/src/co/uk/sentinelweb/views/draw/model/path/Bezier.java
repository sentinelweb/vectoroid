package co.uk.sentinelweb.views.draw.model.path;

import android.graphics.PointF;

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
}
	