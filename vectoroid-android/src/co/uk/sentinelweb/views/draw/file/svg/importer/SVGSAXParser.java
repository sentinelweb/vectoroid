package co.uk.sentinelweb.views.draw.file.svg.importer;

import java.util.HashMap;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class SVGSAXParser extends DefaultHandler {
	private SVGParser sp;
	HashMap<String,String> _prefixes = new HashMap<String,String>();
	public SVGSAXParser(SVGParser sp) {
		super();
		this.sp = sp;
	}

	/* (non-Javadoc)
	 * @see org.xml.sax.helpers.DefaultHandler#endDocument()
	 */
	@Override
	public void endDocument() throws SAXException {
		// TODO Auto-generated method stub
		//super.endDocument();
	}

	/* (non-Javadoc)
	 * @see org.xml.sax.helpers.DefaultHandler#endElement(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public void endElement(String uri, String localName, String qName)	throws SAXException {
		SVGTagObject lastTag = sp._parseContext.scope.remove(sp._parseContext.scope.size()-1);
		if (SVGParser.TAG_STYLE.equals(lastTag.tagName)) {
			sp.parseStyleTag(lastTag);
		}
	}

	/* (non-Javadoc)
	 * @see org.xml.sax.helpers.DefaultHandler#startDocument()
	 */
	@Override
	public void startDocument() throws SAXException {
		sp._parseContext=new SVGParser.ParseContext();
	}
	
	/* (non-Javadoc)
	 * @see org.xml.sax.helpers.DefaultHandler#startPrefixMapping(java.lang.String, java.lang.String)
	 */
	@Override
	public void startPrefixMapping(String prefix, String uri)
			throws SAXException {
		// TODO Auto-generated method stub
		//Log.d(Globals.LOG_TAG, "startPrefixMapping: "+prefix+":"+uri);
		_prefixes.put(uri, prefix);
	}

	/* (non-Javadoc)
	 * @see org.xml.sax.helpers.DefaultHandler#startElement(java.lang.String, java.lang.String, java.lang.String, org.xml.sax.Attributes)
	 */
	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		SVGTagObject sto;
		if (SVGParser.TAG_SVG.equals(localName)) {
			sto = new SVGTagObject();
			sto.tagName = localName;
			sto.atts = getAttributes(attributes);
			sto.style=new HashMap<String, String>();
			//scope.clear();
			sp._parseContext.root=sto;
			//sto.parent=root;
		} else  {
			sto = new SVGTagObject();
			if (qName!=null && !"".equals(qName)) {
				sto.tagName=qName;
			} else {
				if (uri!=null && !uri.endsWith("svg") && _prefixes.get(uri)!=null) {
					sto.tagName=_prefixes.get(uri)+":"+localName;
				} else {
					sto.tagName=localName;
				}
			}
			sto.atts = getAttributes(attributes);
			sto.id = sto.atts.get("id");
			sto.style=new HashMap<String, String>();
			for (SVGTagObject ancestor : sp._parseContext.scope) {
				sto.style.putAll(ancestor.style);
			}
			sp.addStyles(sto.style, sto.atts.get(SVGParser.ATT_STYLE));
			SVGTagObject parent = sp._parseContext.scope.get(sp._parseContext.scope.size()-1);
			parent.children.add(sto);
			sto.parent=parent;
			//Log.d(DVGlobals.LOG_TAG, "add tag: "+sto.tagName);
		}
		sp._parseContext.scope.add(sto);
		if (sto.atts!=null) {
			String id = sto.atts.get(SVGParser.ATT_ID);
			if (id!=null && !"".equals(id)) {
				sp._parseContext.idMap.put(id, sto);
			}
		}
	}
	
	protected HashMap<String,String> getAttributes(Attributes attributes)  
    {
    	HashMap<String,String> result = new HashMap<String,String>();
    	
    	for (int i=0;i<attributes.getLength();i++) {
    		String qName = attributes.getQName(i);
    		if (qName!=null && !"".equals(qName)) {
    			result.put(qName, attributes.getValue(i));
			} else {
				String uri = attributes.getURI(i);
				if (uri!=null && !uri.endsWith("svg") && _prefixes.get(uri)!=null) {
					result.put(_prefixes.get(uri)+":"+attributes.getLocalName(i), attributes.getValue(i));
				} else {
					result.put(attributes.getLocalName(i), attributes.getValue(i));
				}
			}
    		
    	}
    	
    	return result;
    }

	/* (non-Javadoc)
	 * @see org.xml.sax.helpers.DefaultHandler#characters(char[], int, int)
	 */
	@Override
	public void characters(char[] ch, int start, int length) throws SAXException {
		SVGTagObject parent = sp._parseContext.scope.get(sp._parseContext.scope.size()-1);
		if (parent.text==null) {parent.text="";}
		parent.text += new String( ch, start, length );
		//Log.d(DVGlobals.LOG_TAG, "SVGPARSE:text:"+parent.text);
	}
	
	
}
