package android.util;

import java.io.IOException;

public class Log {
	public static int d(String tag,String msg){
		System.out.println(tag+" :: "+msg);
		return 0;
	}
	public static  int d(String tag,String msg, Throwable e){
		System.out.println(tag+" :: "+msg+" : "+e.getClass().getSimpleName()+" - "+e.getMessage());
		e.printStackTrace();
		return 0;
	}
	public static int d(String logTag, String msg, Object ome) {
		System.out.println(logTag+" :: "+msg+" : "+ome.getClass().getSimpleName());
		if (ome instanceof Throwable) {
			((Throwable)ome).printStackTrace();
		}
		return 0;
	}
	public static int e(String tag, String msg, Throwable error) {
		System.err.println(tag+" :: "+msg+" : "+error.getClass().getSimpleName());
		error.printStackTrace();
		
		return 0;
	}
	public static int e(String tag, String msg) {
		System.err.println(tag+" :: "+msg);
		return 0;
	}
	public static int w(String tag, String msg, Throwable error) {
		System.out.println(tag+" :: "+msg+" : "+error.getClass().getSimpleName());
		error.printStackTrace();
		
		return 0;
	}
	public static int w(String tag, String msg) {
		System.out.println(tag+" :: "+msg);
		return 0;
	}
}
