package co.uk.sentinelweb.views.draw.model.path;

import android.graphics.PointF;

public class Quadratic extends PathData {
	//public Type type = Type.QUAD;
	public PointF control1 = new PointF();
	public Quadratic(Quadratic p) {
		super(p);
		type = Type.QUAD;
		this.control1.set(p.control1);
	}
	public Quadratic(PointF p, PointF c1) {
		super(p);
		type = Type.QUAD;
		this.control1.set(c1);
	}
	public Quadratic duplicate() {return new Quadratic(this);}
}