package co.uk.sentinelweb.views.draw.file.svg.importer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;
import android.util.Log;
import co.uk.sentinelweb.views.draw.VecGlobals;
import co.uk.sentinelweb.views.draw.controller.TransformController;
import co.uk.sentinelweb.views.draw.file.export.json.v3.SVGStatic;
import co.uk.sentinelweb.views.draw.model.ComposeTransformOperatorInOut;
import co.uk.sentinelweb.views.draw.model.Drawing;
import co.uk.sentinelweb.views.draw.model.DrawingElement;
import co.uk.sentinelweb.views.draw.model.Fill;
import co.uk.sentinelweb.views.draw.model.Gradient;
import co.uk.sentinelweb.views.draw.model.GradientData;
import co.uk.sentinelweb.views.draw.model.Group;
import co.uk.sentinelweb.views.draw.model.Pen;
import co.uk.sentinelweb.views.draw.model.PointVec;
import co.uk.sentinelweb.views.draw.model.Stroke;
import co.uk.sentinelweb.views.draw.model.Stroke.Type;
import co.uk.sentinelweb.views.draw.model.TransformOperatorInOut;
import co.uk.sentinelweb.views.draw.model.TransformOperatorInOut.Trans;
import co.uk.sentinelweb.views.draw.model.path.PathData;
import co.uk.sentinelweb.views.draw.util.DebugUtil;
import co.uk.sentinelweb.views.draw.util.PointUtil;
import co.uk.sentinelweb.views.draw.util.StrokeUtil;

public class SVGParser {
	//XmlPullParser parser = 
	public static final String TAG_SVG = "svg";
	public static final String TAG_G = "g";
	public static final String TAG_LINEARGRADIENT = "linearGradient";
	public static final String TAG_RADIALGRADIENT = "radialGradient";
	public static final String TAG_CLIPPATH = "clipPath";
	public static final String TAG_STOP = "stop";
	public static final String TAG_DEFS = "defs";
	public static final String TAG_PATH = "path";
	public static final String TAG_RECT = "rect";
	public static final String TAG_CIRCLE = "circle";
	public static final String TAG_ELLIPSE = "ellipse";
	public static final String TAG_LINE = "line";
	public static final String TAG_POLYLINE = "polyline";
	public static final String TAG_POLYGON = "polygon";
	public static final String TAG_TEXT = "text";
	public static final String TAG_TSPAN = "tspan";
	public static final String TAG_USE = "use";
	public static final String TAG_FLOWROOT = "flowRoot";
	public static final String TAG_FLOWPARA = "flowPara";
	public static final String TAG_FLOWREIGON = "flowRegion";
	public static final String TAG_STYLE = "style";
	
	public static final String ATT_ID = "id";
	public static final String ATT_STYLE = "style";
	public static final String ATT_D = "d";//path
	public static final String ATT_OFFSET = "offset";//stop
	public static final String ATT_CLIPPATHUNITS = "clipPathUnits";//clipPath
	public static final String ATT_FX = "fx";//radialGradient
	public static final String ATT_FY = "fy";//radialGradient
	public static final String ATT_CX = "cx";//radialGradient,circle,ellipse
	public static final String ATT_CY = "cy";//radialGradient,circle,ellipse
	public static final String ATT_R = "r";//radialGradient,circle
	public static final String ATT_GRADUNIT = "gradientUnits";//radialGradient
	public static final String ATT_GRADTRANS = "gradientTransform";//radialGradient
	public static final String ATT_SPREADMETHOD = "spreadMethod";//radialGradient
	public static final String ATT_X1 = "x1";//linearGradient,line
	public static final String ATT_X2 = "x2";//linearGradient,line
	public static final String ATT_Y1 = "y1";//linearGradient,line
	public static final String ATT_Y2 = "y2";//linearGradient,line
	public static final String ATT_WID = "width";//svg, rect
	public static final String ATT_HGT = "height";//svg, rect
	public static final String ATT_TRANSFORM = "transform";//svg
	public static final String ATT_XLINK_HREF = "xlink:href";
	
	public static final String ATT_X = "x";//rect
	public static final String ATT_Y = "y";//rect
	public static final String ATT_RX = "rx";//rect,ellipse
	public static final String ATT_RY = "ry";//rect,ellipse
	
	public static final String ATT_POINTS = "points";//polyline
	public static final String ATT_CLASS = "class";//class
	
	public static final String STYLE_STROKE = "stroke";//
	public static final String STYLE_STROKE_OPACITY = "stroke-opacity";//
	public static final String STYLE_STROKE_WIDTH = "stroke-width";//
	public static final String STYLE_STROKE_LINECAP = "stroke-linecap";//
	public static final String STYLE_STROKE_LINEJOIN = "stroke-linejoin";//
	public static final String STYLE_STROKE_MITRELIMIT = "stroke-miterlimit";//
	public static final String STYLE_STROKE_DASHARRAY = "stroke-dasharray";//
	
	public static final String STYLE_OPACITY = "opacity";//
	
	public static final String STYLE_STOP_OPACITY= "stop-opacity";//
	public static final String STYLE_STOP_COLOR = "stop-color";//
	
	public static final String STYLE_FILL = "fill";//
	public static final String STYLE_FILL_OPACITY = "fill-opacity";//
	public static final String STYLE_FILL_RULE = "fill-rule";//
	public static final String STYLE_FONT_SIZE = "font-size";
	public static final String STYLE_FONT_FAMILY = "font-family";
	
	public static final String TRANSFORM_TRANS = "translate";//
	public static final String TRANSFORM_SCALE = "scale";//
	public static final String TRANSFORM_MATRIX = "matrix";//
	public static final String TRANSFORM_SKEW = "skew";//
	public static final String TRANSFORM_ROTATE = "rotate";//
	
	private List<String> defcolors = Arrays.asList(new String[]{"transparent","red","white","blue","black","yellow","green","grey","magenta"});
	private List<Integer> mapcolors = Arrays.asList(new Integer[]{Color.TRANSPARENT,Color.RED,Color.WHITE,Color.BLUE,Color.BLACK,Color.YELLOW,Color.GREEN,Color.GRAY,Color.MAGENTA});
	
	PointF _usePointF = new PointF();
	RectF _useRectF = new RectF();
	PointUtil _pointUtil = new PointUtil();
    static class ParseContext {
		boolean isDefs = false;
		ArrayList<SVGTagObject> scope=new ArrayList<SVGTagObject>();
		HashMap<String,SVGTagObject> idMap=new HashMap<String,SVGTagObject>();
		SVGTagObject root;
		HashMap<String,HashMap<String,String>> classMap=new HashMap<String,HashMap<String,String>>();
	}
    String[] testPaths = new String[] {
    		"134.106,42.256 130.356,43.633 129.364,47.502 126.896,44.361 122.908,44.613 125.134,41.294   123.662,37.581 127.505,38.672 130.583,36.124 130.732,40.116",
    		"M515.038,467.945c0-20.25-16.415-36.664-36.663-36.664h-41.902\nc-20.249,0-36.665,16.414-36.665,36.664l1.165,31.425c0,20.252,16.415,36.664,36.663,36.664h40.739 c20.248,0,36.663-16.412,36.663-36.664V467.945z",
    		"M334.351,128.419c-4.815-2.35-10.809-3.314-13.668,1.862c-5.273-2.676-9.69,1.49-12.353,6.139l-3.845,4.412 c0,0,4.796,2.104,10.042,0.278c1.664-0.579,3.035-1.376,4.146-2.193l-2.691-4.583l4.613,2.923c0.058-0.059,0.122-0.122,0.181-0.179 c0.961-1.01,1.426-1.771,1.426-1.771s0.87,0.384,2.31,0.678l2.582-6.107l0.482,6.431c1.29,0.017,2.741-0.115,4.284-0.529c5.364-1.438,8.853-5.56,8.853-5.56L334.351,128.419z",
    		"M499,1v498H1V1H499 M500,0H0v500h500V0L500,0z",
    		"m 365.511,209.258 c -0.077,-0.459 0.397,-0.916 1.045,-1.097 l 0,0 c 0.651,-0.163 1.295,0.048 1.441,0.482 l 0,0 c 0.153,0.457 -0.306,0.988 -1.029,1.17 l 0,0 c -0.158,0.043 -0.313,0.063 -0.46,0.063 l 0,0 c -0.522,0 -0.937,-0.254 -0.997,-0.618"
    };
	ParseContext _parseContext = new ParseContext();
	public Drawing parseSAX(InputSource is) throws ParserConfigurationException, SAXException, IOException {
		
//		if (1==1) {
//			Stroke s = new Stroke(true);
//			for (String pth : testPaths) {
//				Log.d(VecGlobals.LOG_TAG, "test parsePath1:"+pth);
//				SVGStatic.parsePath1(s, pth);
//				Log.d(VecGlobals.LOG_TAG, "test parsePath:"+s.points.size());
//			}
//			//return null;
//		}
		
		XMLReader xmlreader;
		SVGSAXParser svgSAXHandler = new SVGSAXParser(this);
		//try {
			// create the factory
			SAXParserFactory factory = SAXParserFactory.newInstance();
			factory.setNamespaceAware(true);
			// create a parser
			SAXParser parser = factory.newSAXParser();
			// create the reader (scanner)
			xmlreader = parser.getXMLReader();
			// instantiate our handler
			//Log.d(VecGlobals.LOG_TAG, "ns aware: "+parser.isNamespaceAware());
			// assign our handler
			xmlreader.setContentHandler(svgSAXHandler);
			//xmlreader.setProperty("http://xml.org/sax/features/namespace-prefixes", true);
			// perform the synchronous parse
			xmlreader.parse(is);
			return makeDrawing(_parseContext.root);
		//} catch (Throwable e) {
			//Log.e(Globals.TAG, "XMLParserEX:"+e.getClass().getName()+":"+e.getMessage());
		//	e.printStackTrace();
			
		//	return null;
		//}
	}
	/*
	public Drawing parseXPP(XmlPullParser xpp) {
		SVGPullParser svgpp = new SVGPullParser(this);
		try {
			svgpp.parse(xpp);
			return makeDrawing(_parseContext.root);
		} catch (Throwable e) {
			//Log.e(Globals.TAG, "XMLParserEX:"+e.getClass().getName()+":"+e.getMessage());
			e.printStackTrace();
			
			return null;
		}
	}
	*/
	/*
	public Drawing parse(InputSource is) {
		try {
			XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
			factory.setNamespaceAware(true);
			XmlPullParser xpp = factory.newPullParser();
			_parseContext=new ParseContext();
			try {
				
				xpp.setInput( new InputStreamReader(is.getByteStream()) );
				int eventType = xpp.getEventType();
				while (eventType != XmlPullParser.END_DOCUMENT)
				{
					if (eventType == XmlPullParser.START_TAG)
					{
						SVGTagObject sto;
						if (TAG_SVG.equals(xpp.getName())) {
							sto = new SVGTagObject();
							sto.tagName = xpp.getName();
							sto.atts = getAttributes(xpp);
							//scope.clear();
							_parseContext.root=sto;
							//sto.parent=root;
						} else  {
							sto = new SVGTagObject();
							if (xpp.getPrefix()!=null) {
								sto.tagName=xpp.getPrefix()+":"+xpp.getName();
							} else {
								sto.tagName = xpp.getName();
							}
							sto.atts = getAttributes(xpp);
						
							//if (parentsto.style!=null) {// doesnt bubble style
							//	child.style=new HashMap<String, String>(parentsto.style);
							//} else {
							//	child.style=new HashMap<String, String>();
							//}
							//addStyles(child.style, child.atts.get(ATT_STYLE));
					
							sto.style=new HashMap<String, String>();
							for (SVGTagObject ancestor : _parseContext.scope) {
								sto.style.putAll(ancestor.style);
							}
							addStyles(sto.style, sto.atts.get(ATT_STYLE));
							SVGTagObject parent = _parseContext.scope.get(_parseContext.scope.size()-1);
							parent.children.add(sto);
							Log.d(DVGlobals.LOG_TAG, "add tag: "+sto.tagName);
							//sto.parent=parent;
						}
						_parseContext.scope.add(sto);
						if (sto.atts!=null) {
							String id = sto.atts.get(ATT_ID);
							if (id!=null && !"".equals(id)) {
								_parseContext.idMap.put(id, sto);
							}
						}
					} else if (eventType == XmlPullParser.END_TAG) {
						SVGTagObject parent = _parseContext.scope.get(_parseContext.scope.size()-1);
						_parseContext.scope.remove(parent);
					}
					eventType = xpp.next();
				}
				xpp=null;
				System.gc();
				Log.d(DVGlobals.LOG_TAG, "xml parse finished");
				return makeDrawing(_parseContext.root);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (XmlPullParserException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (XmlPullParserException e) {
			e.printStackTrace();
		}
		return null;
	}
	*/
	
	/* -----------------BUILD DRAWING --------------------------------------------------------------------- */
	private Drawing makeDrawing(SVGTagObject root) {
		//Log.d(VecGlobals.LOG_TAG, "drawing root tag"+root);
		if (root!=null) {
			try {
				Drawing d = new Drawing();
				d.size.set(parseInt(root.atts,ATT_WID,800),parseInt(root.atts,ATT_HGT,600));
				makeElements(root,d.elements);
				//Log.d(VecGlobals.LOG_TAG, "drawing elements"+d.elements.size());
				return d;
			} catch (Exception e) {
				//DebugUtil.logCall("svgparser: exception building drawing", e);
				e.printStackTrace();
			}
			return null;
		} else return null;
	}
	/*
	if (m!=null) {
		if (tfmStr!=null) {
		for (Stroke s : g.getStrokes()) {
			if (tfmStr.startsWith("translate")) {
				s.pen.strokeColour=Color.RED;
				s.pen.strokeWidth=1;
			} else {
				s.pen.strokeColour=Color.BLUE;
				s.pen.strokeWidth=1;
			}
		}
		}
	}
	*/
	private void makeElements(SVGTagObject parentsto, ArrayList<DrawingElement> elements) {
		
		for (SVGTagObject child:parentsto.children) {
				//Log.d(Globals.LOG_TAG, "parsing tag: "+parentsto.tagName+" id:"+parentsto.id);
				child = resolveHref(child);
				resolveClass(child);
				DrawingElement de = null;
				boolean skipThrough = false;
				if (TAG_DEFS.equals(child.tagName)) {
					//skipThrough=true;
					
				} else if (TAG_G.equals(child.tagName)) {
					//Log.d(Globals.LOG_TAG, "g: "+child.tagName+":"+child.id+":"+child.children.size());
					Group g = new Group();
					makeElements(child, g.elements);
					de=g;
				} else if (TAG_PATH.equals(child.tagName)) {
					Stroke s = new Stroke();
					s.type = Type.FREE;
					String pathStr = child.atts.get(ATT_D);
					//parsePath(s,pathStr);
					SVGStatic.parsePath1(s, pathStr);
					s.updateBoundsAndCOG();
					fillInStroke( child,  s);
					de=s;
				} else if (TAG_RECT.equals(child.tagName)) {
					Stroke s = new Stroke(true);
					s.type = Type.FREE;
					PathData tl = new PathData();
					PathData bl = new PathData();
					PathData tr = new PathData();
					PathData br = new PathData();
					float wid = parseFloat(child.atts.get(ATT_WID), 0);
					float hgt = parseFloat(child.atts.get(ATT_HGT), 0);
					tl.x = parseFloat(child.atts.get(ATT_X), 0);
					tl.y = parseFloat(child.atts.get(ATT_Y), 0);
					float rx = parseFloat(child.atts.get(ATT_RX), 0);
					float ry = parseFloat(child.atts.get(ATT_RY), 0);
					
					tr.x = tl.x+wid;
					tr.y = tl.y;
					br.x = tl.x+wid;
					br.y = tl.y+hgt;
					bl.x = tl.x;
					bl.y = tl.y+hgt;
					s.currentVec.add(bl);
					s.currentVec.add(br);
					s.currentVec.add(tr);
					s.currentVec.add(tl);
					s.currentVec.closed=true;
					
					s.updateBoundsAndCOG();
					fillInStroke( child,  s);
					s.pen.rounding=Math.min(rx, ry);
					de=s;
				} else if (TAG_CIRCLE.equals(child.tagName)) {
					Stroke s = new Stroke(true);
					PointVec curVec = s.currentVec;
					curVec.clear();
					curVec.closed=true;
					StrokeUtil.genCircle(curVec);
					float cx =  parseFloat(child.atts.get(ATT_CX), 0);
					float cy =  parseFloat(child.atts.get(ATT_CY), 0);
					float r =  parseFloat(child.atts.get(ATT_R), 0);
					//Log.d(VecGlobals.LOG_TAG, "circle:"+cx+":"+cy+":"+r);
					_useRectF.set(cx-r,cy-r,cx+r,cy+r);
					StrokeUtil.fitStroke(curVec,_useRectF);
					s.updateBoundsAndCOG();
					fillInStroke( child,  s);
					de=s;
				} else if (TAG_ELLIPSE.equals(child.tagName)) {
					Stroke s = new Stroke(true);
					PointVec curVec = s.currentVec;
					curVec.clear();
					curVec.closed=true;
					StrokeUtil.genCircle(curVec);
					float cx =  parseFloat(child.atts.get(ATT_CX), 0);
					float cy =  parseFloat(child.atts.get(ATT_CY), 0);
					float rx =  parseFloat(child.atts.get(ATT_RX), 0);
					float ry =  parseFloat(child.atts.get(ATT_RY), 0);
					
					_useRectF.set(cx-rx,cy-ry,cx+rx,cy+ry);
					StrokeUtil.fitStroke(curVec,_useRectF);
					s.updateBoundsAndCOG();
					fillInStroke( child,  s);
					de=s;
				} else if (TAG_LINE.equals(child.tagName)) {
					Stroke s = new Stroke(true);
					PointVec curVec = s.currentVec;
					PathData p = new PathData(parseFloat(child.atts.get(ATT_X1), 0),parseFloat(child.atts.get(ATT_Y1), 0));
					curVec.add(p);
					p = new PathData(parseFloat(child.atts.get(ATT_X2), 0),parseFloat(child.atts.get(ATT_Y2), 0));
					curVec.add(p);
					s.updateBoundsAndCOG();
					fillInStroke( child,  s);
					de=s;
				} else if (TAG_POLYGON.equals(child.tagName)) {
					Stroke s = new Stroke(true);
					String pathStr = child.atts.get(ATT_POINTS);
					SVGStatic.parsePoints(s,pathStr);
					s.currentVec.closed=true;
					s.updateBoundsAndCOG();
					fillInStroke( child,  s);
					de=s;
				} else if (TAG_POLYLINE.equals(child.tagName)) {
					Stroke s = new Stroke(true);
					String pathStr = child.atts.get(ATT_POINTS);
					SVGStatic.parsePoints(s,pathStr);
					s.currentVec.closed=false;
					s.updateBoundsAndCOG();
					fillInStroke( child,  s);
					de=s;
				} else if (TAG_TEXT.equals(child.tagName)) {
					de = parseText( child);
				} else if (TAG_FLOWROOT.equals(child.tagName)) {
					de = parseFlowText( child);
				} else {
					skipThrough=true;
				}
				if (de!=null) {
					de.setId(child.atts.get(ATT_ID));
					//if (TAG_TEXT.equals(child.tagName)) {
					//	Log.d(VecGlobals.LOG_TAG, "transform on element: "+child.id+":"+child.atts.get(ATT_TRANSFORM));
					//}
					applyTransformToDE(child, de);
					elements.add(de);
				}
				if (skipThrough) {
					//Log.d(Globals.LOG_TAG, "skip: "+child.tagName+":"+child.id+":"+child.children.size());
					for (SVGTagObject grandchild:child.children) {
						makeElements(grandchild,elements);
					}
				}
		}
		//Log.d(Globals.LOG_TAG, "makeElements: made "+elements.size()+" tag:"+parentsto.tagName+" id:"+parentsto.id);
	}
	
	private DrawingElement parseText( SVGTagObject obj) {
		DrawingElement de = null;
		//float fontSize = 10;
		if (obj.children.size()>0) {
			Group g = new Group();
			for (SVGTagObject child:obj.children) {
				if (TAG_TSPAN.equals(child.tagName)) {
					if (child.text==null) {continue;}
					Stroke s = new Stroke(true);
					s.type=Type.TEXT_TTF;
					s.text=child.text;
					PointVec pv = s.currentVec;
					String fontSizeStr = getStyleOrAtt(child,STYLE_FONT_SIZE);
					float fontSize=parseFloat(removeUnits(fontSizeStr), 10);
					//Log.d(VecGlobals.LOG_TAG,"fontSizeStr:"+fontSizeStr+":"+fontSize);
					String fontFamilyStr = getStyleOrAtt(child,STYLE_FONT_FAMILY);
					if (fontFamilyStr!=null) {s.fontName=fontFamilyStr.replaceAll(" ", "_");}
					String xattr = child.atts.get(ATT_X);
					//TODO need to support character spacing (more x & y values see:http://www.w3.org/TR/SVG/text.html#TextElementXAttribute)
					float x=0;
					if (xattr!=null) {
						String[] xsplit = xattr.split(" ");
						if (xsplit.length>0) {
							x = parseFloat(xsplit[0],0);
						}
					} 
					//float x = parseFloatx;
					float y = parseFloat(child.atts.get(ATT_Y),0);
					pv.add(new PathData(x,y+0));
					pv.add(new PathData(x+20,y+0));
					pv.add(new PathData(x+20,y-fontSize));
					pv.add(new PathData(x+0,y-fontSize));
					s.updateBoundsAndCOG();
					fillInStroke( child,  s );
					g.elements.add(s);
				}
			}
			g.updateBoundsAndCOG(false);
			de=g;
		} else {
			Stroke s = new Stroke(true);
			s.type=Type.TEXT_TTF;
			s.text=obj.text;
			PointVec pv = s.currentVec;
			String fontSizeStr = getStyleOrAtt(obj,STYLE_FONT_SIZE);
			float fontSize=parseFloat(removeUnits(fontSizeStr), 10);
			//Log.d(VecGlobals.LOG_TAG,"fontSizeStr:"+fontSizeStr+":"+fontSize);
			
			String fontFamilyStr = getStyleOrAtt(obj,STYLE_FONT_FAMILY);
			if (fontFamilyStr!=null) {s.fontName=fontFamilyStr.replaceAll(" ", "_");}
			float x = parseFloat(obj.atts.get(ATT_X),0);
			float y = parseFloat(obj.atts.get(ATT_Y),0);
			pv.add(new PathData(x,y+0));
			pv.add(new PathData(x+20,y+0));
			pv.add(new PathData(x+20,y-fontSize));
			pv.add(new PathData(x+0,y-fontSize));
			if (s.text==null) {
				return null;
			}
			s.updateBoundsAndCOG();
			fillInStroke( obj,  s );
			de=s;
		}
		return de;
		//Log.d(DVGlobals.LOG_TAG, "text: "+s.text+" :"+fontSize);
	}
	
	private DrawingElement parseFlowText( SVGTagObject obj ) {
		// just parses one para at the mo - text wont flow in renderer
		// assumes flowreigon is a rect
		ArrayList<SVGTagObject> flowRegions = obj.getChildrenByTagName(TAG_FLOWREIGON);
		if (flowRegions.size()>0) {
			Stroke s = new Stroke(true);
			s.type=Type.TEXT_TTF;
			SVGTagObject flowReigon = flowRegions.get(0);
			if (flowReigon.children.size()>0) {
				SVGTagObject t =flowReigon.children.get(0);
				if (TAG_RECT.equals(t.tagName)) {
					float x = parseFloat(t.atts.get(ATT_X),0);
					float y = parseFloat(t.atts.get(ATT_Y),0);
					float w = parseFloat(t.atts.get(ATT_WID),0);
					float h = parseFloat(t.atts.get(ATT_HGT),0);
					String fontSizeStr = getStyleOrAtt(flowReigon,STYLE_FONT_SIZE);//flowReigon.style.get(STYLE_FONT_SIZE);
					float fontSize=parseFloat(removeUnits(fontSizeStr), 10);
					//Log.d(VecGlobals.LOG_TAG,"fontSizeStr:"+fontSizeStr+":"+fontSize);
					String fontFamilyStr = getStyleOrAtt(flowReigon,STYLE_FONT_FAMILY);
					if (fontFamilyStr!=null) {s.fontName=fontFamilyStr.replaceAll(" ", "_");}
					//Log.d(VecGlobals.LOG_TAG,"FLOWREIGON fontSize: "+fontSizeStr+"=="+fontSize);
					PointVec pv = s.currentVec;
					pv.add(new PathData(x,y+0));
					pv.add(new PathData(x+20,y+0));
					pv.add(new PathData(x+20,y-fontSize));
					pv.add(new PathData(x+0,y-fontSize));
				}
			}
			ArrayList<SVGTagObject> flowParas = obj.getChildrenByTagName(TAG_FLOWPARA);
			if (flowParas.size()>0) {
				for (SVGTagObject para:flowParas) {
					if (para.text!=null) {
						s.text += ("".equals(s.text)?"":"\n")+para.text;
					}
				}
			}
			if (s.text==null) {
				return null;
			}
			//Log.d(Globals.LOG_TAG,"FLOWREIGON text:"+s.text);
			s.updateBoundsAndCOG();
			fillInStroke( obj,  s );
			return s;
		}
		
		return null;
	}
	
	private void fillInStroke(SVGTagObject child, Stroke s) {
		/*
		try {
			String colorStr = styles.get(styleStroke);
			Log.d("SVGW", "stroke colour:"+styleStroke+"="+colorStr+":"+styleStrokeOpacity+"="+styles.get(styleStrokeOpacity));
			int color = Color.BLACK;
			if (colorStr!=null && !"none".equals(colorStr)) {
				color = Color.parseColor(colorStr);
			}
			int opacity = (int)(parseFloat(styles.get(STYLE_STROKE_OPACITY),1)*255);
			Log.d("SVGW", "stroke colour parsed:"+color+":"+opacity);
			if (opacity<255) {
				int retVal = Color.argb(opacity, Color.red(color), Color.green(color), Color.blue(color));
				return retVal;
			} else {
				return color; 
			}
		} catch (Exception e) {
			return Color.BLACK;
		}
		*/
		if (s.pen==null) {
			s.pen = new Pen();
		}
		if (s.fill==null) {
			s.fill=new Fill();
		}
		Integer color = getColor(child,STYLE_STROKE,STYLE_STROKE_OPACITY);
		if (color==null) {
			s.pen.strokeColour=Color.BLACK;//Color.BLACK
		} else {
			s.pen.strokeColour=color;
		}
		String strokeWid = getStyleOrAtt(child,STYLE_STROKE_WIDTH);
		String stroke = getStyleOrAtt(child,STYLE_STROKE);
		if ((stroke==null || "none".equals(stroke))) {strokeWid=null;}
		else if (strokeWid==null) {strokeWid="1px";}
		if (strokeWid!=null ) {
			strokeWid = removeUnits(strokeWid);
			s.pen.strokeWidth=parseFloat(strokeWid, 1);
		} else {
			s.pen.strokeWidth=0;
		}
		String lcap = getStyleOrAtt(child,STYLE_STROKE_LINECAP);
		if (lcap!=null) {
			if ("butt".equals(lcap)) {
				s.pen.cap=Paint.Cap.BUTT;
			} else if ("round".equals(lcap)) {
				s.pen.cap=Paint.Cap.ROUND;
			} else if ("square".equals(lcap)) {
				s.pen.cap=Paint.Cap.SQUARE;
			} 
		}  
		String ljoin = getStyleOrAtt(child,STYLE_STROKE_LINEJOIN);
		if (ljoin!=null) {
			if ("miter".equals(ljoin)) {
				s.pen.join=Paint.Join.MITER;
			} else if ("round".equals(ljoin)) {
				s.pen.join=Paint.Join.ROUND;
			} else if ("bevel".equals(ljoin)) {
				s.pen.join=Paint.Join.BEVEL;
			} 
		}  
		String fcolorStr = getStyleOrAtt(child, STYLE_FILL);
		float fcolorOpacityVal = (parseFloat(getStyleOrAtt(child,STYLE_FILL_OPACITY),1));//(int)
		float opacity = parseFloat(getStyleOrAtt(child,STYLE_OPACITY),1);
		fcolorOpacityVal*=opacity;
		//int opacity = (int)(parseFloat(child.style.get(STYLE_FILL_OPACITY),1)*255);
		//s.pen.strokeColour=getAlphaColor(child.style,STYLE_STROKE,STYLE_STROKE_OPACITY);
		if ("none".equals(fcolorStr)) {
			s.fill.type=Fill.Type.NONE;
		} else if (fcolorStr!=null && fcolorStr.indexOf("url")==0) {
			setFillByUrl(s,fcolorStr,opacity);
		} else  {
			int color1=Color.BLACK;//Color.BLACK
			if (fcolorStr!=null) {
				if (fcolorStr.charAt(0)=='#') {
					color1 = parseColor(fcolorStr);
				} else {
					int idx = defcolors.indexOf(fcolorStr);
					if (idx>-1) {color1=mapcolors.get(idx);}
				}
			}
			//if (fcolorOpacityVal<1) {
				color1 = Color.argb((int)(fcolorOpacityVal*255), Color.red(color1), Color.green(color1), Color.blue(color1));
			//}
			s.fill.setColor(color1);
		}
		
		//String colorStr = child.style.get(STYLE_STROKE);
		//String colorOpacityStr = child.style.get(STYLE_STROKE_OPACITY);
		
	}

	private Integer parseColor(String fcolorStr) {
		Integer color1=null;
		try {
			color1=Color.parseColor(fcolorStr);
		} catch (RuntimeException e) {
			String trim = fcolorStr.toLowerCase().trim();
			//Log.d(VecGlobals.LOG_TAG, "color ex:"+trim);
			if (trim.length()==4) {// check for 3 color color string
				color1=Color.argb(
						255,
						Integer.parseInt(Character.toString(trim.charAt(1)),16)*16,
						Integer.parseInt(Character.toString(trim.charAt(2)),16)*16,
						Integer.parseInt(Character.toString(trim.charAt(3)),16)*16
						);
			} else {
				throw e;
			}
		}
		return color1;
	}
	private String getStyleOrAtt(SVGTagObject child, String tag) {
		while (child!=null) {// TODO : probably should disable the atts check for the parent tags - is it 
			String fcolorStr = child.style.get(tag);
			if (fcolorStr==null) {
				fcolorStr = child.atts.get(tag);
			}
			if (fcolorStr!=null) {
				return fcolorStr;
			} else {
				child=child.parent;
			}
		}
		return null;
	}
	private String removeUnits(String strokeWid) {// assumes all units in px
		if (strokeWid==null) {return null;}
		if (strokeWid.endsWith("px") || strokeWid.endsWith("pc") ||strokeWid.endsWith("mm")||strokeWid.endsWith("cm")||strokeWid.endsWith("in")) {
			//TODO prunes off units - need to apply scaling - ignored
			strokeWid=strokeWid.substring(0,strokeWid.length()-2);
		} else if (strokeWid.endsWith("%")) {//need to apply after parsing
			strokeWid=strokeWid.substring(0,strokeWid.length()-1);
		}
		return strokeWid;
	}

	private Integer getColor(SVGTagObject child,String ctag,String optag) {
		Integer color = null;
		if (child==null) {  return Color.BLACK;	}// || child.style==null
		String scolorStr = getStyleOrAtt(child,ctag);
		int scolorOpacityVal = (int)(parseFloat(getStyleOrAtt(child,optag),1)*255);
		
		//s.pen.strokeColour=getAlphaColor(child.style,STYLE_STROKE,STYLE_STROKE_OPACITY);
		if (scolorStr==null && !"none".equals(scolorStr)) {
			return null;
		} else {
			if (scolorStr.charAt(0)=='#') {
				color = parseColor(scolorStr);
			} else if (scolorStr.indexOf("url(")==0){
				String content = getUrlTagContent(scolorStr);
				//TODO this is a hack, the gradient isnt used need to add gradient support for stroke -  not sure if it works anyways
				if (content!=null && content.length()>2 && content.charAt(0)=='#') {
					Gradient g = getGradient(scolorOpacityVal/255f, content);
					if (g!=null && g.colors.length>0) {
						color=g.colors[0];
					}
				}
			} else {
				int idx = defcolors.indexOf(scolorStr);
				if (idx>-1) {color=mapcolors.get(idx);}
			}
			if (color!=null && scolorOpacityVal<255) {
				color = Color.argb(scolorOpacityVal, Color.red(color), Color.green(color), Color.blue(color));
			}
		}
		return color;
	}
	
	private void setFillByUrl(Stroke s, String url,float elementOpacity) {
		String content = getUrlTagContent(url);
		if (content!=null && content.length()>2 && content.charAt(0)=='#') {
			Gradient g = getGradient(elementOpacity, content);
			if (g!=null) {
				s.fill.setGradient(g);
			}
		}
	}

	private String getUrlTagContent(String url) {
		if (url.length()<5) {return null;}
		String content = url.substring("url(".length(),url.length()-1);
		return content;
	}

	private Gradient getGradient(float elementOpacity, String content) {
		Gradient g =null;
		SVGTagObject gtag = _parseContext.idMap.get(content.substring(1));
		if (gtag!=null ) {
			gtag=resolveHref(gtag);
			if ( gtag.children.size()>0) {
				if (gtag.tagName.equals(TAG_LINEARGRADIENT)) {
					g = new Gradient();
					g.type=Gradient.Type.LINEAR;
					getStops(gtag,g,elementOpacity);
					GradientData gd = new GradientData();
					gd.p1=new PointF();
					gd.p2=new PointF();
					// defaults to a horizontal line
					float x1 = parseFloat(gtag.atts.get(ATT_X1), 0);
					float x2 = parseFloat(gtag.atts.get(ATT_X2), 1);
					float y1 = parseFloat(gtag.atts.get(ATT_Y1), 0);
					float y2 = parseFloat(gtag.atts.get(ATT_Y2), 0);
					gd.p1.set(x1,y1);
					gd.p2.set(x2,y2);
					if (gtag.atts.get(ATT_GRADTRANS)!=null) {
						 TransformOperatorInOut t = getTransform(gtag.atts.get(ATT_GRADTRANS));
						 _pointUtil.mul3(gd.p1,gd.p1,t.matrix3);
						 _pointUtil.mul3(gd.p2,gd.p2,t.matrix3);
					}
					g.data=gd;
					//s.fill.setGradient(g);
				} else if (gtag.tagName.equals(TAG_RADIALGRADIENT)) {
					g = new Gradient();
					g.type=Gradient.Type.RADIAL;
					getStops(gtag,g,elementOpacity);
					GradientData gd = new GradientData();
					gd.p1=new PointF();
					gd.p2=new PointF();
					float cx = parseFloat(gtag.atts.get(ATT_CX), 0.5f);
					float cy = parseFloat(gtag.atts.get(ATT_CY), 0.5f);
					float r = parseFloat(gtag.atts.get(ATT_R), 1);
					gd.p1.set(cx,cy);
					gd.p2.set(cx+r,cy);
					if (gtag.atts.get(ATT_GRADTRANS)!=null) {
						TransformOperatorInOut t = getTransform(gtag.atts.get(ATT_GRADTRANS));
						 _pointUtil.mul3(gd.p1,gd.p1,t.matrix3);
						 _pointUtil.mul3(gd.p2,gd.p2,t.matrix3);
						 
					}
					g.data=gd;
					//s.fill.setGradient(g);
				}
			}
			
		}
		return g;
	}
	
	public void getStops(SVGTagObject gtag,Gradient g,float elementOpacity) {
		ArrayList<Integer> colours = new ArrayList<Integer>();
		ArrayList<Float> positions = new ArrayList<Float>();
		
		if (gtag!=null && gtag.children.size()>0) {
			for (SVGTagObject child : gtag.children) {
				if (TAG_STOP.equals(child.tagName)) {
					Integer color = getColor(child,STYLE_STOP_COLOR,STYLE_STOP_OPACITY);
					
					if (color!=null) {
						color=Color.argb((int)(Color.alpha(color)*elementOpacity), Color.red(color), Color.green(color), Color.blue(color));
						colours.add(color);
					} else {
						colours.add(Color.TRANSPARENT);//Color.BLACK
					}
					positions.add(parseFloat(child.atts.get(ATT_OFFSET),0.5f));
				}
			}
			int[] colors = new int[colours.size()];
			for (int c=0; c<colours.size(); c++) colors[c]=colours.get(c); 
			float[] poss = new float[positions.size()];
			for (int c=0; c<positions.size(); c++) poss[c]=positions.get(c); 
			g.colors=colors;
			g.positions=poss;
		}
	}
	/* -------------------  applyTransform -----------------------------------------------------------------------------------------------*/
	private void applyTransformToDE(SVGTagObject child, DrawingElement de) {
		if (child.atts.containsKey(ATT_TRANSFORM)) {
			String tfmStr = child.atts.get(ATT_TRANSFORM);
			TransformOperatorInOut t = getTransform(tfmStr);
//			if (t.rotateValue!=0) {
//				t.anchor.set(de.calculatedCentre);
//				t.generate();
//			}
			if (de instanceof Stroke) {
				applyTransform((Stroke)de,t);
			} else if (de instanceof Group) {
				for (Stroke de1 : ((Group)de).getAllStrokes()) {
					applyTransform(de1,t);
				}
			}
		}
	}

	private void applyTransform(DrawingElement de, TransformOperatorInOut t) {//double[][] matrix, PointF p 
		if (de instanceof Stroke) {
			Stroke s = (Stroke) de;
			s.updateBoundsAndCOG();
			//Log.d(DVGlobals.LOG_TAG, "apply transform: scal:"+m[0][0]+":"+m[0][1]+":"+m[1][0]+":"+m[1][1]+":"+m[2][0]+":"+m[2][1]);
			for (PointVec pv :s.points ) {
				/*
				for (int i=0;i<pv.size();i++) {
					_pointUtil.mul3(pv.get(i),pv.get(i),m);
					if (pv.beizer1!=null && i<pv.beizer1.size() && pv.beizer1.get(i)!=null) {
						_pointUtil.mul3(pv.beizer1.get(i),pv.beizer1.get(i),m);
					}
					if (pv.beizer2!=null && i<pv.beizer2.size() && pv.beizer2.get(i)!=null) {
						_pointUtil.mul3(pv.beizer2.get(i),pv.beizer2.get(i),m);
					}
				}
				*/
				//TransformOperatorInOut t = new TransformOperatorInOut();
				//t.matrix3=m;
				TransformController.transform(pv, pv, t);
			}
			/*
			if (s.fill.type==Fill.Type.GRADIENT) {
				GradientData gd = s.fill._gradient.data;
				_pointUtil.mul3(gd.p1,gd.p1,t.matrix3);
				_pointUtil.mul3(gd.p2,gd.p2,t.matrix3);
				
			}
			
			*/
			s.applyTransform(t, s);
			if (s.type==Type.TEXT_TTF) {
				s.textXScale*=t.scaleXValue;
			}
			/*
			if (s.pen.scalePen) {
				double xscale = (m[0][0]+m[1][1])/2;
				//s.textXScale*=xscale;
				s.pen.strokeWidth=pen.strokeWidth*(float)Math.abs(t.scaleValue);
				s.pen.glowWidth=pen.glowWidth*(float)Math.abs(t.scaleValue);
				s.pen.glowOffset.x=pen.glowOffset.x*(float)t.scaleValue;
				s.pen.glowOffset.y=pen.glowOffset.y*(float)t.scaleValue;
			}
			*/
			//Log.d(Globals.LOG_TAG, "transform applied:"+Arrays.toString(matrix));
		} else if (de instanceof Group) {
			applyTransform((Group) de, t);//matrix,p
		}
	}
	
	private TransformOperatorInOut getTransform(String string) {
		if (string!=null) {
			//Log.d(DVGlobals.LOG_TAG, "parsing transform:"+string);
			int lastpos = 0;
			//TransformOperatorInOut finalTrans = null;
			ArrayList<TransformOperatorInOut> stack = new ArrayList<TransformOperatorInOut>();
			//Log.d(VecGlobals.LOG_TAG, "transform :"+string);
			int pos = string.indexOf("(");
			while (pos>-1) {
				double mat[][] = null;
				//int indexOfOpen = string.indexOf("(");
				int indexOfClose = string.indexOf(")",pos);
				if (pos>-1 && indexOfClose>-1) {
					String type = string.substring(lastpos,pos).trim();
					String ctnt =  string.substring(pos+1,indexOfClose).trim();
					//Log.d(VecGlobals.LOG_TAG, "transform type:"+type+" ctnt:"+ctnt);
					String[] vals = null;
					if (ctnt.indexOf(",")>0) {
						vals = ctnt.split(",");
					} else {
						vals = ctnt.split(" ");
					}
					//if (vals==null) {
					//	Log.d(VecGlobals.LOG_TAG, "Couldnt parse transform:"+ctnt);
					//}
					mat=new double[3][3];
					mat[0][0]=1;mat[1][1]=1;mat[2][2]=1;
					TransformOperatorInOut t = new TransformOperatorInOut();
					stack.add(t);
					//Matrix m = new Matrix();
					t.matrix3=mat;
					if (TRANSFORM_TRANS.equals(type)) {
						mat[0][2]=parseFloat(vals[0], 0);
						double y = vals.length>1?parseFloat(vals[1], 0):0;
						t.ops.add(Trans.MOVE);
						mat[1][2]=y;//parseFloat(vals[1], 0);
						t.trans.set((float)mat[0][2],(float) y);
						//Log.d(VecGlobals.LOG_TAG, "transform trans:"+mat[0][2]+","+y);
					} else if (TRANSFORM_SCALE.equals(type)){
						mat[0][0]=parseFloat(vals[0], 0);
						mat[1][1]=parseFloat(vals[1], 0);
						t.ops.add(Trans.SCALE);
						t.scaleXValue = mat[0][0];
						t.scaleYValue = mat[1][1];
					} else if (TRANSFORM_SKEW.equals(type)){
						mat[0][1]=parseFloat(vals[0], 0);
						mat[1][0]=parseFloat(vals[1], 0);
						t.ops.add(Trans.SHEAR);
						t.skewXValue = mat[0][1];
						t.skewYValue = mat[1][0];
					} else if (TRANSFORM_ROTATE.equals(type)){
						t.rotateValue = parseFloat(vals[0], 0)*Math.PI/180f;
						if (vals.length>1) {
							t.anchor.x=parseFloat(vals[1], 0);
						}
						if (vals.length>2) {
							t.anchor.y=parseFloat(vals[2], 0);
						}
						t.ops.add(Trans.ROTATE);
						t.generate();
					} else if (TRANSFORM_MATRIX.equals(type)){
						mat[0][0]=parseFloat(vals[0], 0);
						mat[1][0]=parseFloat(vals[1], 0);
						mat[0][1]=parseFloat(vals[2], 0);
						mat[1][1]=parseFloat(vals[3], 0);
						mat[0][2]=parseFloat(vals[4], 0);
						mat[1][2]=parseFloat(vals[5], 0);
						//t.scaleXValue = mat[0][0]/mat[1][1];
						//t.scaleYValue = mat[1][0]/mat[0][1];
						// can recover scaling if noof diagonal factors - not sure how to recover the lot - complicated?
						if (mat[1][0]==0 && mat[0][1]==0) {
							t.scaleXValue = mat[0][0];
							t.scaleYValue = mat[1][1];
							// take the closest to 1
							t.choseScaleClosestTo1();
							t.ops.add(Trans.SCALE);
						} else {
							// TODO insert maths;
							// TODO this assumes no skew ? - need a check
							// ref: http://stackoverflow.com/questions/4361242/extract-rotation-scale-values-from-2d-transformation-matrix
							double rot1 = Math.atan2(-mat[0][1], mat[0][0]);
							double rot2 = Math.atan2(mat[1][0], mat[1][1]);
							//Log.d(VecGlobals.LOG_TAG, "parsed transform : rot:"+rot1+"="+rot2+" - "+Math.abs(rot1-rot2));
							if (Math.abs(rot1-rot2)<0.000001) {// valid rotation matrix
								t.scaleXValue =Math.sqrt(mat[0][0]*mat[0][0]+mat[0][1]*mat[0][1]);
								t.scaleYValue =Math.sqrt(mat[1][0]*mat[1][0]+mat[1][1]*mat[1][1]);
								t.choseScaleClosestTo1();
								t.ops.add(Trans.SCALE);
								
								// TODO rotation value
								//t.rotateValue = rot1;// -b/a
							}
						}
						if (mat[0][2]!=0 || mat[1][2]!=0) {
							t.ops.add(Trans.MOVE);
							t.trans.set((float)mat[0][2],(float)mat[1][2]);
						}
					}
					
				}
				lastpos=indexOfClose+1;
				pos = string.indexOf("(",indexOfClose);
				
			}
			if (stack.size()==1 ) {
				return stack.get(0);
			} else {
				ComposeTransformOperatorInOut ctio = new ComposeTransformOperatorInOut(stack);
				return ctio;
			}
		}
		return null;
	}



	
	
	

	
	private int parseInt(HashMap<String,String> atts,String att,int def) {
		if (atts!=null) {
			try {
				return Integer.parseInt(_parseContext.root.atts.get(att));
			} catch (NumberFormatException e) {
				return def;
			}
		} else {
			return def;
		}
	}
	
	private float parseFloat(HashMap<String,String> atts,String att,float def) {
		if (atts!=null) {
			try {
				return Float.parseFloat(_parseContext.root.atts.get(att).trim());
			} catch (NumberFormatException e) {
				return def;
			}
		} else {
			return def;
		}
	}
	
	public static float parseFloat(String data,float def) { 
		if (data!=null && data.length()>0) {
			try {
				return Float.parseFloat(data.trim());
			} catch (NumberFormatException e) {
				DebugUtil.logCall("!!!!!!!!!!!!!  floatparse: ex:"+data+" ;"+e.getMessage(),e);
				return def;
			}
		} else {
			return def;
		}
	}
	
	// process xlink:href
	private SVGTagObject resolveHref(SVGTagObject finalTag) {
		String xlinkhref = finalTag.atts.get(ATT_XLINK_HREF);
		if (xlinkhref!=null && xlinkhref.startsWith("#")) {
			String linkId = xlinkhref.substring(1);
			SVGTagObject parentTag = _parseContext.idMap.get(linkId);
			if (parentTag!=null) {
				SVGTagObject refTag = resolveHref(parentTag);
				resolveClass(refTag);
				parentTag = refTag.duplicate();
				String tagName = parentTag.tagName;
				
				if (TAG_USE.equals(finalTag.tagName)) {
					parentTag.overwrite(finalTag,true);
					parentTag.tagName=tagName;
				} else {
					parentTag.overwrite(finalTag,false);
				}
				//if (TAG_TEXT.equals(parentTag.tagName)) {
				//	parentTag.logTag();
				//}
				return parentTag;
			} else {
				DebugUtil.logCall("!!!!!!!!!!!!!  resolveHref: ex:"+" ;"+linkId, new Exception());
				return finalTag;
			}
		} else {
			return finalTag;
		}
	}
	
	void addStyles(HashMap<String, String> style, String att) {
		if (att!=null) {
			String[] split = att.split(";");
			for (String satt: split) {
				if (satt==null) {continue;}
				satt=satt.trim();
				int cpos = satt.indexOf(":");
				if (cpos<satt.length()-1) {
					style.put(satt.substring(0,cpos), satt.substring(cpos+1,satt.length()));
				}
			}
		}
	}
	
	public void parseStyleTag(SVGTagObject lastTag) {// called from parser
		if (lastTag.text!=null && !"".equals(lastTag.text)) {
			//Log.d(VecGlobals.LOG_TAG, "classParse:"+lastTag.text);
			String[] split = lastTag.text.split("\\}");
			for (String styleDef:split) {
				styleDef = styleDef.trim();
				String[] splitNameContent = styleDef.split("\\{");
				if (splitNameContent.length==2) {
					String nmTag = splitNameContent[0].trim();
					if (nmTag.indexOf(".")==0) {
						nmTag = nmTag.substring(1);
						HashMap<String, String> styleSet = new HashMap<String, String>();
						String classContent = splitNameContent[1].trim();
						//Log.d(VecGlobals.LOG_TAG, "class:"+nmTag+" = "+classContent);
						addStyles(styleSet, classContent);
						_parseContext.classMap.put(nmTag, styleSet);
					}
				}
			}
		}
	}
	
	private SVGTagObject resolveClass(SVGTagObject child) {
		String classStr = child.atts.get(ATT_CLASS);
		if (classStr!=null) {
			String[] classSplit = classStr.split(" ");
			HashMap<String, String> style = new HashMap<String, String>();
			for (String classNm : classSplit) {
				HashMap<String, String> classStyles = _parseContext.classMap.get(classNm.trim());
				if (classStyles!=null) {
					style.putAll(classStyles);
				}
			}
			style.putAll(child.style);
			child.style=style;
		}
		return child;
	}
	
}
