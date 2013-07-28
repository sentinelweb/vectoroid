package android.widget;

import android.content.Context;

public class Toast {
	public static class Text {
		public void show() {}
	}
	public static Text makeText(Context act, String string, int i) {
		return new Text();
	}

}
