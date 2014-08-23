package android.graphics;

public class PointF {
	public float x,y;
	public PointF() {}
	public PointF(float x, float y) {
		super();
		this.x = x;
		this.y = y;
	}
	public PointF(PointF p) {
		this.x = p.x;
		this.y = p.y;
	}
	public void set(float x, float y) {
		this.x = x;
		this.y = y;
	}
	public void set(PointF p) {
		this.x = p.x;
		this.y = p.y;
	}
}
