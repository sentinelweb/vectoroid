package co.uk.sentinelweb.views.draw.file.svg.importer;

import java.util.ArrayList;
import java.util.HashMap;

import android.util.Log;

public class SVGTagObject {
	String id;
	String tagName;
	HashMap<String,String> atts=null;
	ArrayList<SVGTagObject> children = new ArrayList<SVGTagObject>();
	HashMap<String,String> style=null;
	String text = null;
	SVGTagObject parent;
	
	public SVGTagObject duplicate() {
		SVGTagObject out = new SVGTagObject();
		out.id=id;
		out.tagName=tagName;
		out.text=text;
		out.atts = new HashMap<String,String>();
		out.atts.putAll(atts);
		if (style!=null) {
			out.style = new HashMap<String,String>();
			out.style.putAll(style); 
		}
		out.children = new ArrayList<SVGTagObject>();
		out.children.addAll(children);
		parent=null;
		return out;
	}
	
	public SVGTagObject overwrite(SVGTagObject out,boolean useTag) {
		id=out.id;
		tagName=out.tagName;
		//text=out.text;
		if (useTag) {
			for (String attKey:out.atts.keySet()) {
				if (SVGParser.ATT_X.equals(attKey) || SVGParser.ATT_Y.equals(attKey)) {
					float outx = SVGParser.parseFloat(out.atts.get(attKey), 0);
					float thisx = SVGParser.parseFloat(atts.get(attKey), 0);
					atts.put(attKey, Float.toString(outx+thisx));
				} else {
					atts.put(attKey,out.atts.get(attKey));
				}
			}
		} else {
			atts.putAll(out.atts);
		}
		if (out.style!=null) {
			if (style==null) {style = new HashMap<String,String>();}
			
				style.putAll(out.style);

		}
		//children.addAll(out.children);
		for (SVGTagObject child:out.children) {
			if (!children.contains(child)) {
				children.add(child);
			}
		}
		return out;
	}
	
	public ArrayList<SVGTagObject> getChildrenByTagName(String tagName) {
		ArrayList<SVGTagObject> tags = new ArrayList<SVGTagObject>();
		for (SVGTagObject c : children) {
			if (tagName.equals(c.tagName)) {
				tags.add(c);
			}
		}
		return tags;
	}
	
	public void logTag() {
		StringBuilder sb = new StringBuilder();
		sb.append( "<");
		sb.append( tagName);
		if (atts!=null) {
			for (String att: atts.keySet()) {
				sb.append( " ");
				sb.append( att);
				sb.append( "=");
				sb.append( atts.get(att));
			}
		}
		if (style!=null) {
			sb.append( " style=");
			for (String st: style.keySet()) {
				sb.append( st);
				sb.append( ":");
				sb.append( style.get(st));
				sb.append( "; ");
				
			}
		}
		sb.append(">");
		sb.append(children.size());
		sb.append(" - ");
		sb.append(text);
		Log.d("SVGParser",sb.toString());
	}
}
