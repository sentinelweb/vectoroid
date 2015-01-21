package co.uk.sentinelweb.views.draw.model;

import java.util.ArrayList;

import android.graphics.PointF;

public class ComposeTransformOperatorInOut extends TransformOperatorInOut {
	private ArrayList<TransformOperatorInOut> stack;

	public ComposeTransformOperatorInOut(ArrayList<TransformOperatorInOut> stack) {
		super();
		this.stack = stack;
	}
	
	@Override
	public void operate(PointF pin, PointF pout) {
		for (int i=stack.size()-1;i>-1;i--) {
		//for (int i=0;i<stack.size();i++) {
			stack.get(i).operate(pin, pout);
		}
	}

	public ArrayList<TransformOperatorInOut> getStack() {
		return stack;
	}

//	public void setStack(ArrayList<TransformOperatorInOut> stack) {
//		this.stack = stack;
//	}
//	
}
