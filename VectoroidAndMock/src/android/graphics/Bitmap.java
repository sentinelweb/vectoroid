package android.graphics;

import java.io.FileOutputStream;

import android.graphics.Bitmap.Config;

public class Bitmap {
	public static class Config {
		public static final Config ARGB_8888 = null;
		public static Config RGB_565 = new Config();
	}
	public static class CompressFormat{
		public static final CompressFormat PNG = null;
		
	}
	
	public static Bitmap createBitmap(float wid,float hgt, Config c) {
		return new Bitmap();
	}

	public void compress(CompressFormat png, int i, FileOutputStream out) {
		
		
	}

	public void recycle() {
		
	}
}
