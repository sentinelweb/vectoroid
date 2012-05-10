package co.uk.sentinelweb.views.draw.file.export.json.v3;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Writer;
import java.io.ObjectInputStream.GetField;
import java.util.ArrayList;
import java.util.HashMap;

import android.graphics.PointF;
import android.util.Log;
import co.uk.sentinelweb.views.draw.VecGlobals;
import co.uk.sentinelweb.views.draw.model.PointVec;
import co.uk.sentinelweb.views.draw.model.Stroke;
import co.uk.sentinelweb.views.draw.model.StrokeDecoration.DecorationPathData;
import co.uk.sentinelweb.views.draw.model.path.Arc;
import co.uk.sentinelweb.views.draw.model.path.Bezier;
import co.uk.sentinelweb.views.draw.model.path.PathData;
import co.uk.sentinelweb.views.draw.model.path.Quadratic;
import co.uk.sentinelweb.views.draw.util.DebugUtil;
import co.uk.sentinelweb.views.draw.util.PointUtil;

public class SVGStatic {
	public static void toSVG(PointF pointF, Writer outBuffer) throws IOException {
		outBuffer.append(dp3f(pointF.x)).append(",").append(dp3f(pointF.y));
	}
	public static String dp1f(float f)  {
		return Float.toString(Math.round(f*10)/10f);
	}
	public static String dp2f(float f)  {
		return Float.toString(Math.round(f*100)/100f);
	}
	public static String dp3f(float f)  {
		return Float.toString(Math.round(f*1000)/1000f);
	}
	public static PointF fromSVG(StringBuffer inBuffer) {
		return fromSVG( inBuffer,1f);
	}
	
	public static PointF fromSVG(StringBuffer inBuffer,float dimensionRatio) {
		
		return null;
	}
	public static void writeID(BufferedWriter out,Object de,String pfx) {
		try {
			out.append(pfx).append(Integer.toString(de.hashCode()));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void toSVGPath(Writer outBuffer, PointVec pv)	throws IOException {
		String lastCmd = "";
		for (int j=0;j<pv.size();j++) {
			PathData p = pv.get(j);
			if (j==0) {
				outBuffer.append("M");
				SVGStatic.toSVG(p, outBuffer);
			} else {
				switch (p.type) {
					case POINT:
						if (!"L".equals(lastCmd)) {
							outBuffer.append("L");
						}
						SVGStatic.toSVG(p, outBuffer);
						lastCmd="L";
						break;
					case BEZIER:
						Bezier b = (Bezier)p;
						if (!"C".equals(lastCmd)) {
							outBuffer.append("C");
						}
						SVGStatic.toSVG(b.control1, outBuffer);
						outBuffer.append(" ");
						SVGStatic.toSVG(b.control2, outBuffer);
						outBuffer.append(" ");
						SVGStatic.toSVG(p, outBuffer);
						lastCmd="C";
						break;
					case QUAD:
						Quadratic q = (Quadratic)p;
						if (!"Q".equals(lastCmd)) {
							outBuffer.append("Q");
						}
						SVGStatic.toSVG(q.control1, outBuffer);
						outBuffer.append(" ");
						SVGStatic.toSVG(p, outBuffer);
						lastCmd="Q";
						break;
					case ARC:
						Arc a = (Arc)p;
						if (!"A".equals(lastCmd)) {
							outBuffer.append("A");
						}
						SVGStatic.toSVG(a.r, outBuffer);
						outBuffer.append(" ");
						outBuffer.append(dp1f(a.xrot));
						outBuffer.append(" ");
						outBuffer.append(a.largeArc?'1':'0');
						outBuffer.append(" ");
						outBuffer.append(a.sweep?'1':'0');
						outBuffer.append(" ");
						SVGStatic.toSVG(p, outBuffer);
						lastCmd="A";
						break;
					
				}
			} 
			outBuffer.append(" ");
		}
		if (pv.closed) {outBuffer.append("Z");}
		outBuffer.flush();
	}
	
	public static void toSVGPath(BufferedWriter outBuffer, DecorationPathData p,float[] transformed)	throws IOException {
		for (int j=0;j<p.cmd.length;j++) {
			if (p.cmd[j]!='-') {outBuffer.append(p.cmd[j]);}
			outBuffer.append(dp1f(transformed[j*2])).append(",").append(dp1f(transformed[j*2+1]));
			outBuffer.append(" ");
		}
		if (p.closed) {outBuffer.append("Z");}
	}
	
	public static void toSVGFloarArray(BufferedWriter outBuffer, float[] array,float mult)	throws IOException {
		for (int j=0;j<array.length;j++) {
			outBuffer.append(dp1f(array[j]*mult));
			if (j<array.length-1) {outBuffer.append(",");}
		}
	}
	
	
	
	
	
/* -------------------  parsePath Points -----------------------------------------------------------------------------------------------*/
	
	public static void parsePoints(Stroke s, String pathStr) {
		if (pathStr!=null) {
			Log.d(VecGlobals.LOG_TAG,"parsePoints: data:"+pathStr);
			String[] split = pathStr.split(" ");
			for (String data : split) {
				if (data==null || "".equals(data)) {
					Log.d(VecGlobals.LOG_TAG,"parsePoints: badData:"+data);
					continue;
				}
				PathData p = new PathData(getPoint(data));
				if (p==null) {
					Log.d(VecGlobals.LOG_TAG,"parsePoints: null point");
					continue;
				} else {
					s.currentVec.add(p);
				}
			}
		}
	}
	static HashMap<Character,Integer> _pointsReqForCmd = new HashMap<Character,Integer>();
	static {
		_pointsReqForCmd.put('M',2);
		_pointsReqForCmd.put('m',2);
		_pointsReqForCmd.put('H',1);
		_pointsReqForCmd.put('h',1);
		_pointsReqForCmd.put('V',1);
		_pointsReqForCmd.put('v',1);
		_pointsReqForCmd.put('L',2);
		_pointsReqForCmd.put('l',2);
		_pointsReqForCmd.put('C',6);
		_pointsReqForCmd.put('c',6);
		_pointsReqForCmd.put('z',0);
		_pointsReqForCmd.put('Z',0);
		_pointsReqForCmd.put('S',4);
		_pointsReqForCmd.put('s',4);
		_pointsReqForCmd.put('Q',4);
		_pointsReqForCmd.put('q',4);
		_pointsReqForCmd.put('T',2);
		_pointsReqForCmd.put('t',2);
		_pointsReqForCmd.put('A',7);
		_pointsReqForCmd.put('a',7);
	}
	public static void parsePath1(Stroke s, String pathStr) {
		s.points.clear();
		char currentMode = 0;
		ArrayList<String> currentPointData = new ArrayList<String>();
		PointF lastPoint = new PointF();
		int pos=0;
		StringBuilder _useSB = new StringBuilder();
		char lastChar = 0;
		if (pathStr!=null && !"".equals(pathStr)) {
			//Log.d(DVGlobals.LOG_TAG, "parsePath1:"+pathStr);
			while (pos<pathStr.length()) {
				char charAt = pathStr.charAt(pos);
				boolean cmdChar = Character.isLetter(charAt) && charAt!='e';
				char setMode = currentMode;
				if (cmdChar) {
					setMode=charAt;
					if (currentMode==0) currentMode=setMode;
				}
				boolean isNumBreak = cmdChar || 
									charAt==' ' || 
									charAt=='\n' || 
									charAt==',' ||
									(lastChar!=' ' && lastChar!='e' && charAt=='-');
				if (isNumBreak && _useSB.length()>0) {
						currentPointData.add(_useSB.toString());
						_useSB.delete(0, _useSB.length());
				}
				if (currentPointData.size()==_pointsReqForCmd.get(currentMode)) {
					processPointData(s,currentMode,currentPointData,lastPoint);
					// assume l after m if not specified
					if (currentMode=='M' && setMode=='M') {setMode='L';}
					if (currentMode=='m' && setMode=='m') {setMode='l';}
					currentPointData.clear();
				}
				if (Character.isDigit(charAt) || charAt=='-' || charAt=='e' || charAt=='.' ) {
					_useSB.append(charAt);
				}
				lastChar=charAt;
				currentMode=setMode;
				pos++;
			}
			if (_useSB.length()>0) {
				currentPointData.add(_useSB.toString());
			}
			// process last
			if (currentPointData.size()==_pointsReqForCmd.get(currentMode)) {
				processPointData(s,currentMode,currentPointData,lastPoint);
				currentPointData.clear();
			}
		}
	}
	
	
	public static  void processPointData(Stroke s, char currentMode,
			ArrayList<String> parsedPathData, PointF lastPoint) {
//		StringBuilder _useSB = new StringBuilder();
//		for (String data : pd) {
//			_useSB.append(data);
//			_useSB.append("|");
//		}
//		Log.d(DVGlobals.LOG_TAG, currentMode+" - "+_useSB.toString());
		PointF p=null;
		PointF p1=null;
		PointF p2=null;
		
		PathData pathData=null;
		
		PointVec curpv=s.currentVec;
		switch (currentMode) {
			case 'v':
				pathData=new PathData();
				//p.y=parseFloat(data,0f)+p.y;
				pathData.set(lastPoint.x,lastPoint.y+parseFloat(parsedPathData.get(0),0f));
				curpv.add(pathData);lastPoint.set(pathData);
				break;
			case 'V':
				pathData=new PathData();
				//p.y=parseFloat(data,0f)+p.y;
				pathData.set(lastPoint.x,parseFloat(parsedPathData.get(0),lastPoint.y));
				curpv.add(pathData);lastPoint.set(pathData);
				break;
			case 'h':
				pathData=new PathData();
				//p.x=parseFloat(data,0f)+p.x;
				pathData.set(lastPoint.x+parseFloat(parsedPathData.get(0),lastPoint.x),lastPoint.y);
				curpv.add(pathData);lastPoint.set(pathData);
				break;
			case 'H':
				pathData=new PathData();
				//p.x=parseFloat(data,0f)+p.x;
				pathData.set(parseFloat(parsedPathData.get(0),lastPoint.x),lastPoint.y);
				curpv.add(pathData);lastPoint.set(pathData);
				break;
			case 'm': p=getPoint(parsedPathData.get(0), parsedPathData.get(1));p.set(lastPoint.x+p.x,lastPoint.y+p.y);
			case 'M': 
				if ( p==null ) {p=getPoint(parsedPathData.get(0), parsedPathData.get(1));}
				curpv=new PointVec();s.points.add(curpv);s.currentVec=curpv;curpv.add(new PathData(p));lastPoint.set(p);
				break;//currentMode=(currentMode=='m')?'l':'L';
			case 'l': p=getPoint(parsedPathData.get(0), parsedPathData.get(1));p.set(lastPoint.x+p.x,lastPoint.y+p.y);
			case 'L': 
				if ( p==null ) {p=getPoint(parsedPathData.get(0), parsedPathData.get(1));}
				curpv.add(new PathData(p));lastPoint.set(p);break;
			case 'c': 
				p1=getPoint(parsedPathData.get(0), parsedPathData.get(1));p1.set(lastPoint.x+p1.x,lastPoint.y+p1.y);
				p2=getPoint(parsedPathData.get(2), parsedPathData.get(3));p2.set(lastPoint.x+p2.x,lastPoint.y+p2.y);
				p=getPoint(parsedPathData.get(4), parsedPathData.get(5));p.set(lastPoint.x+p.x,lastPoint.y+p.y);
			case 'C': 
				if ( p==null ) {
					p1=getPoint(parsedPathData.get(0), parsedPathData.get(1));
					p2=getPoint(parsedPathData.get(2), parsedPathData.get(3));
					p=getPoint(parsedPathData.get(4), parsedPathData.get(5));
				}
				Bezier b = new Bezier(p, p1, p2);
				curpv.add(b);
				lastPoint.set(p);
				break;
			case 's': 
				p2=getPoint(parsedPathData.get(0), parsedPathData.get(1));p2.set(lastPoint.x+p2.x,lastPoint.y+p2.y);
				p=getPoint(parsedPathData.get(2), parsedPathData.get(3));p.set(lastPoint.x+p.x,lastPoint.y+p.y);
			case 'S': 
				if ( p==null ) {
					p2=getPoint(parsedPathData.get(0), parsedPathData.get(1));
					p=getPoint(parsedPathData.get(2), parsedPathData.get(3));
				}
				// from http://stackoverflow.com/questions/5287559/calculating-control-points-for-a-shorthand-smooth-svg-path-bezier-curve
				p1 = new PointF();
				PointF lastC2 = ((Bezier)curpv.get( curpv.size()-1)).control2;
				if (lastC2!=null) {
					p1.set(2*lastPoint.x-lastC2.x,2*lastPoint.y-lastC2.y);
				} else {
					p1.set(p);
				}
				Bezier b1 = new Bezier(p, p1, p2);
				curpv.add(b1);
				lastPoint.set(p);

				break;
			case 'q': 
				p1=getPoint(parsedPathData.get(0), parsedPathData.get(1));p1.set(lastPoint.x+p1.x,lastPoint.y+p1.y);
				p=getPoint(parsedPathData.get(2), parsedPathData.get(3));p.set(lastPoint.x+p.x,lastPoint.y+p.y);
			case 'Q': 
				if ( p==null ) {
					p1=getPoint(parsedPathData.get(0), parsedPathData.get(1));
					p=getPoint(parsedPathData.get(2), parsedPathData.get(3));
				}
				Quadratic q = new Quadratic(p, p1);
				curpv.add(q);
				lastPoint.set(p);
				break;
			case 't': 
				p=getPoint(parsedPathData.get(0), parsedPathData.get(1));p.set(lastPoint.x+p.x,lastPoint.y+p.y);
			case 'T': 
				if ( p==null ) {
					p=getPoint(parsedPathData.get(0), parsedPathData.get(1));
				}
				// from http://stackoverflow.com/questions/5287559/calculating-control-points-for-a-shorthand-smooth-svg-path-bezier-curve
				p1 = new PointF();
				PointF lastC1 = ((Quadratic)curpv.get( curpv.size()-1)).control1;
				if (lastC1!=null) {
					p1.set(2*lastPoint.x-lastC1.x,2*lastPoint.y-lastC1.y);
				} else {
					p1.set(p);
				}
				Quadratic q1 = new Quadratic(p, p1);
				curpv.add(q1);
				lastPoint.set(p);
				break;
			case 'Z': 
			case 'z': curpv.closed=true;
				break;
			case 'a':
				p=getPoint(parsedPathData.get(5), parsedPathData.get(6));p.set(lastPoint.x+p.x,lastPoint.y+p.y);
			case 'A': 
				if ( p==null ) {
					p=getPoint(parsedPathData.get(0), parsedPathData.get(1));
				}
				Arc a = new Arc(
						p, 
						parseFloat(parsedPathData.get(0), 0), 
						parseFloat(parsedPathData.get(1), 0), 
						parseFloat(parsedPathData.get(2), 0), 
						parseFloat(parsedPathData.get(3), 0)==1, 
						parseFloat(parsedPathData.get(4), 0)==1
				);
				curpv.add(a);
				lastPoint.set(p);
				Log.d(VecGlobals.LOG_TAG, "SVG Arc:"+":"+PointUtil.tostr(a)+":"+PointUtil.tostr(a.r)+":"+a.xrot+":"+a.largeArc+":"+a.sweep);
				break;
			default:Log.d(VecGlobals.LOG_TAG, currentMode+" not handled.");break;
				
		}
	}
	/*
	public static  void extendBz (PointVec pv) {
		if (pv.beizer1==null) {pv.beizer1=new ArrayList<PointF>();}
		while (pv.beizer1.size()<pv.size()) {
			pv.beizer1.add(null);
		}
		if (pv.beizer2==null) {pv.beizer2=new ArrayList<PointF>();}
		while (pv.beizer2.size()<pv.size()) {
			pv.beizer2.add(null);
		}
	}
	*/
	public static  PointF getPoint(String x,String y) {
		if (x!=null && y!=null) {
			return new PointF(parseFloat(x, 0f),parseFloat(y, 0f));
		}
		return null;
	}
	
	public static  PointF getPoint(String data) {
		if (data!=null) {
			String[] split = data.split(",");
			return new PointF(parseFloat(split[0], 0f),parseFloat(split[1], 0f));
		}
		return null;
	}
	private static float parseFloat(String data,float def) { 
		if (data!=null) {
			try {
				return Float.parseFloat(data);
			} catch (NumberFormatException e) {
				DebugUtil.logCall("!!!!!!!!!!!!!  floatparse: ex:"+data+" ;"+e.getMessage(),e);
				return def;
			}
		} else {
			return def;
		}
	}
}
