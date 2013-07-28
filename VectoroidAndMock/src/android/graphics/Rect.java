package android.graphics;

public class Rect {
	public int top,bottom,left,right=0;

	public Rect( int left, int top, int right,int bottom) {
		super();
		this.top = top;
		this.bottom = bottom;
		this.left = left;
		this.right = right;
	}
	
	public Rect( ) {
		super();
	}
	
	public int width() {
		return Math.abs(left-right);
	}
	public int height() {
		return Math.abs(top-bottom);
	}
	public void set( int left, int top, int right,int bottom) {
		this.top = top;
		this.bottom = bottom;
		this.left = left;
		this.right = right;
	}
	public void set( Rect r) {
		this.top = r.top;
		this.bottom = r.bottom;
		this.left = r.left;
		this.right = r.right;
	}
}
