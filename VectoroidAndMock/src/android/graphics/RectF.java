package android.graphics;

public class RectF {
	public float top,bottom,left,right=0;

	public RectF( float left, float top, float right,float bottom) {
		super();
		this.top = top;
		this.bottom = bottom;
		this.left = left;
		this.right = right;
	}
	
	public RectF( ) {
		super();
	}
	
	public float width() {
		return Math.abs(left-right);
	}
	public float height() {
		return Math.abs(top-bottom);
	}
	public void set( float left, float top, float right,float bottom) {
		this.top = top;
		this.bottom = bottom;
		this.left = left;
		this.right = right;
	}
	public void set( RectF r) {
		this.top = r.top;
		this.bottom = r.bottom;
		this.left = r.left;
		this.right = r.right;
	}
	public void set( Rect r) {
		this.top = r.top;
		this.bottom = r.bottom;
		this.left = r.left;
		this.right = r.right;
	}

	public boolean intersect(RectF tst) {
		return true;
	}

	public boolean intersect(float f, float g, float h, float i) {
		return false;
	}

	public boolean contains(float x, float y) {
		return top<y&&bottom>y&&left<x&&right>x;
	}
}
