package co.uk.sentinelweb.views.draw.util;

import android.graphics.Color;

public class ColorUtil {
	public static String hex2(int val) {
		String a = Integer.toHexString(val);
		if (a.length()<2) {a="0"+a;}
		return a;
	}
	
	public static String toColorString(int bgColor) {
		return "#"+hex2(Color.alpha(bgColor))
				+hex2(Color.red(bgColor))
				+hex2(Color.green(bgColor))
				+hex2(Color.blue(bgColor));
	}
	
	public static String toColorStringNoAlpha(int bgColor) {
		return "#"+hex2(Color.red(bgColor))
				+hex2(Color.green(bgColor))
				+hex2(Color.blue(bgColor));
	}
	public static String toAlphaString(int bgColor) {
		return ""+(Color.alpha(bgColor)/255f);
	}
	
	
}
