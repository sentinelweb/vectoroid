package android.graphics;

import java.io.File;

import android.content.res.AssetManager;

public class Typeface {
	public static Typeface DEFAULT =  new Typeface();
	
	public static Typeface createFromFile(File file) {
		return new Typeface();
	}

	public static Typeface createFromAsset(AssetManager assets, String name) {
		return new Typeface();
	}
}
