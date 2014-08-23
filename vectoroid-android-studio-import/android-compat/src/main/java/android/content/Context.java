package android.content;

import java.io.File;

import android.content.res.AssetManager;
import android.content.res.Resources;
import co.uk.sentinelweb.vectoroid.andmock.GlobalsJava;

public class Context {

	public static final int BIND_NOT_FOREGROUND = 4;
	public static Resources _resources = new Resources();
	public Object getString(int font_sample) {
		return null;
	}

	public Resources getResources() {
		return _resources;
	}
	
	public AssetManager getAssets() {
		return _resources.getAssets();
	}
	
	public File getExternalFilesDir(Object dontUse) {
		File file = new File(GlobalsJava.APP_DIR,"external");
		if (!file.exists()) {
			file.mkdirs();
		}
		return file;
		
	}

	public File getExternalCacheDir() {
		File file = new File(GlobalsJava.APP_DIR,"cache");
		if (!file.exists()) {
			file.mkdirs();
		}
		return file;
	}
	public File getFilesDir() {
		File file = new File(GlobalsJava.APP_DIR,"files");
		if (!file.exists()) {
			file.mkdirs();
		}
		return file;
	}
}
