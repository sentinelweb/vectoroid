package android.net;

public class Uri {
	private String url = null;
	
	public static Uri parse(String url) {
		Uri uri = new Uri();
		uri.url=url;
		return uri;
	}

	@Override
	public String toString() {
		return url;
	}

}
