package co.uk.sentinelweb.views.draw.file.svg.importer;

import java.io.IOException;
import java.util.HashMap;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.util.Log;
import co.uk.sentinelweb.views.draw.VecGlobals;

public class SVGPullParser {
	private SVGParser sp;
	
	public SVGPullParser(SVGParser sp) {
		super();
		this.sp = sp;
	}

	public void parse(XmlPullParser xpp) {
		sp._parseContext=new SVGParser.ParseContext();
		try {
			
			//xpp.setInput( new InputStreamReader(is.getByteStream()) );
			int eventType = xpp.getEventType();
			while (eventType != XmlPullParser.END_DOCUMENT)
			{
				if (eventType == XmlPullParser.START_TAG)
				{
					SVGTagObject sto;
					if (SVGParser.TAG_SVG.equals(xpp.getName())) {
						sto = new SVGTagObject();
						sto.tagName = xpp.getName();
						sto.atts = getAttributes(xpp);
						sto.style=new HashMap<String, String>();
						sp._parseContext.root=sto;
					} else  {
						sto = new SVGTagObject();
						if (xpp.getPrefix()!=null) {
							sto.tagName=xpp.getPrefix()+":"+xpp.getName();
						} else {
							sto.tagName = xpp.getName();
						}
						sto.atts = getAttributes(xpp);
						sto.style=new HashMap<String, String>();
						for (SVGTagObject ancestor : sp._parseContext.scope) {
							sto.style.putAll(ancestor.style);
						}
						sp.addStyles(sto.style, sto.atts.get(SVGParser.ATT_STYLE));
						SVGTagObject parent = sp._parseContext.scope.get(sp._parseContext.scope.size()-1);
						parent.children.add(sto);
						//Log.d(DVGlobals.LOG_TAG, "add tag: "+sto.tagName);
					}
					sp._parseContext.scope.add(sto);
					if (sto.atts!=null) {
						String id = sto.atts.get(SVGParser.ATT_ID);
						if (id!=null && !"".equals(id)) {
							sp._parseContext.idMap.put(id, sto);
						}
					}
				} else if (eventType == XmlPullParser.END_TAG) {
					SVGTagObject lastTag = sp._parseContext.scope.remove(sp._parseContext.scope.size()-1);
					if (SVGParser.TAG_STYLE.equals(lastTag.tagName)) {
						sp.parseStyleTag(lastTag);
					}
				} else if (eventType == XmlPullParser.TEXT) {
					SVGTagObject parent = sp._parseContext.scope.get(sp._parseContext.scope.size()-1);
					if (parent.text==null) {parent.text="";}
					parent.text += xpp.getText();
				}
				eventType = xpp.next();
			}
			xpp=null;
			System.gc();
			Log.d(VecGlobals.LOG_TAG, "xml parse finished");
			//return makeDrawing(sp._parseContext.root);
		}  catch (XmlPullParserException e) {
			Log.d(VecGlobals.LOG_TAG, "xml parse",e);
		} catch (IOException e) {
			Log.d(VecGlobals.LOG_TAG, "xml parse",e);
		} 
	}
	
	protected HashMap<String,String> getAttributes(XmlPullParser xpp)  
    {
    	HashMap<String,String> result = new HashMap<String,String>();
    	for (int i=0;i<xpp.getAttributeCount();i++) {
    		if (xpp.getAttributePrefix(i)==null) {
    			result.put(xpp.getAttributeName(i), xpp.getAttributeValue(i));
    		} else {
    			result.put(xpp.getAttributePrefix(i)+":"+xpp.getAttributeName(i), xpp.getAttributeValue(i));
    		}
    	}
    	return result;
    }
}
