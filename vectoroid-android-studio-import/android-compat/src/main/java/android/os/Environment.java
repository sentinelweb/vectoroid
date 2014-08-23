package android.os;

import java.io.File;

public class Environment {
	public static final String MEDIA_MOUNTED ="mounted";
	public static File getExternalStorageDirectory(){
		return new File("/home/robert");
	}
	public static String getExternalStorageState() {
		return MEDIA_MOUNTED;
	}
}
