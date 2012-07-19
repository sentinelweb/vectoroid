package co.uk.sentinelweb.views.draw.file.export.svg;

import java.io.BufferedWriter;
import java.io.IOException;

import android.graphics.PointF;
import android.util.Log;
import co.uk.sentinelweb.views.draw.VecGlobals;
import co.uk.sentinelweb.views.draw.file.SaveFile;
import co.uk.sentinelweb.views.draw.file.export.json.v3.SVGStatic;
import co.uk.sentinelweb.views.draw.model.Drawing;
import co.uk.sentinelweb.views.draw.model.Fill;
import co.uk.sentinelweb.views.draw.model.Fill.Type;
import co.uk.sentinelweb.views.draw.model.Stroke;
import co.uk.sentinelweb.views.draw.util.ColorUtil;
import co.uk.sentinelweb.views.draw.util.PointUtil;

public class SVGFill extends SVGParent{
	public static final String SVG_GRAD_ID_PFX="grad_";
	public static final String SVG_CLIP_ID_PFX="clip_";
	PointF _usePoint1 = new PointF();
	PointF _usePoint2 = new PointF();
	
	public SVGFill(SaveFile _saveFile) {
		super(_saveFile);
	}
	
	public void toSVGStyle(Stroke s, BufferedWriter outBuffer) {
		try {
			Fill f = s.fill;
			if (f.type==Type.COLOUR_STROKE) {
				outBuffer.append("fill:").append(ColorUtil.toColorStringNoAlpha(s.pen.strokeColour)).append(";");
				outBuffer.append("fill-opacity:").append(ColorUtil.toAlphaString(s.pen.strokeColour)).append(";");
			} else {
				makeStyle(s, outBuffer, f);
			}
		} catch (IOException e) {
			Log.d(VecGlobals.LOG_TAG," fill style to SVG", e);
		}
	}
	public void toSVGStyle(Drawing d, BufferedWriter outBuffer) {
		try {
			makeStyle(d, outBuffer, d.background);
		} catch (IOException e) {
			Log.d(VecGlobals.LOG_TAG," fill style to SVG", e);
		}
	}
	private void makeStyle(Object o, BufferedWriter outBuffer, Fill f) throws IOException {
		switch (f.type) {
			case COLOUR:
				outBuffer.append("fill:").append(ColorUtil.toColorStringNoAlpha(f._color)).append(";");
				outBuffer.append("fill-opacity:").append(ColorUtil.toAlphaString(f._color)).append(";");
				break;
			case NONE:
				outBuffer.append("fill:none;");
				break;
			case GRADIENT:
				outBuffer.append("fill:url(#");
				SVGStatic.writeID(outBuffer,o,SVG_GRAD_ID_PFX);
				outBuffer.append(");");
				break;
			case BITMAP:
				outBuffer.append("fill:none;");
				break;
		}
	}
	
	public void toSVGDefs(Stroke s, BufferedWriter outBuffer) {
		try {
			Fill fill = s.fill;
			switch (fill.type) {
				case GRADIENT:
					if (fill._gradient.data!=null && fill._gradient.data.p1!=null) {
						_usePoint1.set(fill._gradient.data.p1);
					} else {_usePoint1.set(s.calculatedBounds.left, s.calculatedBounds.top);}
					if (fill._gradient.data!=null && fill._gradient.data.p2!=null) {
						_usePoint2.set(fill._gradient.data.p2);
					} else {_usePoint2.set(s.calculatedBounds.right, s.calculatedBounds.top);}
				makeDefs(s, outBuffer, fill);
				break;
				default:
					makeDefs(s, outBuffer, fill);
				break;
			}
		} catch (IOException e) {
			Log.d(VecGlobals.LOG_TAG," fill style to SVG", e);
		}
	}
	public void toSVGDefs(Drawing d, BufferedWriter outBuffer) {
		try {
			Fill fill = d.background;
			switch (fill.type) {
				case GRADIENT:
					if (fill._gradient.data!=null && fill._gradient.data.p1!=null) {
						_usePoint1.set(fill._gradient.data.p1);
					} else {_usePoint1.set(0, 0);}
					if (fill._gradient.data!=null && fill._gradient.data.p2!=null) {
						_usePoint2.set(fill._gradient.data.p2);
					} else {_usePoint2.set(d.size.x, d.size.y);}
				makeDefs(d, outBuffer, fill);
				break;
				default:
					makeDefs(d, outBuffer, fill);
				break;
			}
		} catch (IOException e) {
			Log.d(VecGlobals.LOG_TAG," fill style to SVG", e);
		}
	}

	private void makeDefs(Object s, BufferedWriter outBuffer, Fill fill) throws IOException {
		switch (fill.type) {
		case GRADIENT:
			String type=null;
			StringBuffer attrs=new StringBuffer();
			switch (fill._gradient.type) {
			case LINEAR:
				type="linearGradient";
				attrs.append("x1=\"").append(Math.round(_usePoint1.x*10)/10).append("\" ");
				attrs.append("y1=\"").append(Math.round(_usePoint1.y*10)/10).append("\" ");
				attrs.append("x2=\"").append(Math.round(_usePoint2.x*10)/10).append("\" ");
				attrs.append("y2=\"").append(Math.round(_usePoint2.y*10)/10).append("\" ");
				break;
			case RADIAL:
				type="radialGradient";
				float radius = PointUtil.dist(_usePoint1, _usePoint2);
				attrs.append("cx=\"").append(Math.round(_usePoint1.x*10)/10).append("\" ");
				attrs.append("cy=\"").append(Math.round(_usePoint1.y*10)/10).append("\" ");
				attrs.append("r=\"").append(Math.round(radius*10)/10).append("\" ");
				attrs.append("fx=\"").append(Math.round(_usePoint1.y*10)/10).append("\" ");
				attrs.append("fy=\"").append(Math.round(_usePoint1.y*10)/10).append("\" ");
				break;
			case SWEEP:
				// not supported?
			}
			if (type!=null) {
				outBuffer.append("<").append(type).append(" id=\"");
				SVGStatic.writeID(outBuffer,s,SVG_GRAD_ID_PFX);
				outBuffer.append("\" ").append("gradientUnits=\"userSpaceOnUse\" ");
				outBuffer.append(attrs.toString()).append(">\n");
				for (int i=0;i<fill._gradient.colors.length;i++) {
					outBuffer.append("<stop offset=\"").append(Integer.toString(Math.round(fill._gradient.positions[i]*100))).append("\" ");
					outBuffer.append("stop-color=\"").append(ColorUtil.toColorStringNoAlpha(fill._gradient.colors[i])).append("\" ");
					outBuffer.append("stop-opacity=\"").append(ColorUtil.toAlphaString(fill._gradient.colors[i])).append("\" ");
					outBuffer.append("/>\n");
				}
				outBuffer.append("</").append(type).append(">\n"); 
			}
			break;
		case BITMAP:
			outBuffer.append("<clipPath id=\"");
			SVGStatic.writeID(outBuffer,s,SVG_CLIP_ID_PFX);
			outBuffer.append("\">\n");
			outBuffer.append("<use xlink:href=\"#");
			SVGStatic.writeID(outBuffer,s,SVGStroke.SVG_PATH_ID_PFX);
			outBuffer.append("\" />\n");
			outBuffer.append("</clipPath>\n");
			
			break;
		}
			
	}
}
