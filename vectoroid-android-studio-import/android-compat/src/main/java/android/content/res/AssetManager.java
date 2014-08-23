package android.content.res;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.PrintWriter;

import co.uk.sentinelweb.vectoroid.andmock.GlobalsJava;


public class AssetManager {
	public static File getAssetsDir() {
		File file = new File(GlobalsJava.APP_DIR,"assets");
		if (!file.exists()) {
			file.mkdirs();
		}
		return file;
		
	}
	public InputStream open(String fileName) {
		File f = new File(getAssetsDir(),fileName);// hopefully i don't have to split here
		if (f.exists()) {
			try {
				return new FileInputStream(f);
			} catch (FileNotFoundException e) {
				System.out.println();
				e.printStackTrace(new PrintWriter(System.err ));
			}
		}
		return null;
	}
	
}
