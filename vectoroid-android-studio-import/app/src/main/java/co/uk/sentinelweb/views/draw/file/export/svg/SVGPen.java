package co.uk.sentinelweb.views.draw.file.export.svg;

import java.io.BufferedWriter;
import java.io.IOException;

import android.graphics.Matrix;
import android.util.Log;
import co.uk.sentinelweb.views.draw.VecGlobals;
import co.uk.sentinelweb.views.draw.file.SaveFile;
import co.uk.sentinelweb.views.draw.file.export.json.v3.SVGStatic;
import co.uk.sentinelweb.views.draw.model.Pen;
import co.uk.sentinelweb.views.draw.model.Stroke;
import co.uk.sentinelweb.views.draw.model.StrokeDecoration.DecorationPathData;
import co.uk.sentinelweb.views.draw.model.StrokeDecoration.Tip;
import co.uk.sentinelweb.views.draw.util.ColorUtil;

public class SVGPen extends SVGParent{
	public static final String SVG_BLUR_ID_PFX="blur_";
	public static final String SVG_ST_TIP_ID_PFX="stip_";
	public static final String SVG_END_TIP_ID_PFX="etip_";
	public static final String SVG_BG_TIP_ID_SFX="bg_";
	public static final String SVG_FGSTYLE_ID_PFX="fg_";
	public static final String SVG_BGSTYLE_ID_PFX="bg_";
	public SVGPen(SaveFile _saveFile) {
		super(_saveFile);
		
	}
	public void toSVGFGStyle(Stroke s, BufferedWriter outBuffer) {//,boolean marker
		try {
			Pen p = s.pen;
			outBuffer.append("stroke:").append(ColorUtil.toColorStringNoAlpha(p.strokeColour)).append(";");
			outBuffer.append("stroke-width:").append(SVGStatic.dp1f(p.strokeWidth)).append("px;");
			//outBuffer.append("stroke-opacity:").append(Float.toString(p.alpha/255f)).append(";");// TODO use alpha in colour
			outBuffer.append("stroke-opacity:").append(ColorUtil.toAlphaString(s.pen.strokeColour)).append(";");
			outBuffer.append("stroke-linecap:").append(s.pen.cap.toString().toLowerCase()).append(";");
			outBuffer.append("stroke-linejoin:").append(s.pen.join.toString().toLowerCase()).append(";");
			
		} catch (IOException e) {
			Log.d(VecGlobals.LOG_TAG, "toSVGFGStyle pen", e);
		}
	}
	public void toSVGBGStyle(Stroke s, BufferedWriter outBuffer) {
		try {
			Pen p = s.pen;
			outBuffer.append("stroke:").append(ColorUtil.toColorStringNoAlpha(p.glowColour)).append(";");
			outBuffer.append("stroke-width:").append(SVGStatic.dp1f(p.glowWidth)).append("px;");
			//outBuffer.append("stroke-opacity:").append(Float.toString(p.alpha/255f)).append(";");// TODO use alpha in colour
			outBuffer.append("stroke-opacity:").append(ColorUtil.toAlphaString(s.pen.glowColour)).append(";");
			outBuffer.append("stroke-linecap:").append(s.pen.cap.toString().toLowerCase()).append(";");
			outBuffer.append("stroke-linejoin:").append(s.pen.join.toString().toLowerCase()).append(";");
			outBuffer.append("filter:url(#");
			SVGStatic.writeID(outBuffer,s,SVG_BLUR_ID_PFX);		
			outBuffer.append(");");
		} catch (IOException e) {
			Log.d(VecGlobals.LOG_TAG, "toSVGBGStyle pen", e);
		}
	}
	public void toSVGDefs(Stroke s, BufferedWriter outBuffer) {
		try {
			if (s.pen.glowWidth>0) {
				outBuffer.append("<filter id=\"");
				SVGStatic.writeID(outBuffer,s,SVG_BLUR_ID_PFX);		
				outBuffer.append("\" ");
				outBuffer.append("x=\"").append("-50%\" ");//.append(SVGStatic.dp1f((s.pen.glowWidth>10?-50:-20)))
				outBuffer.append("y=\"").append("-50%\" ");//.append(SVGStatic.dp1f((s.pen.glowWidth>10?-50:-20)))
				outBuffer.append("width=\"").append("200%\" ");//.append(SVGStatic.dp1f((s.pen.glowWidth>10?200:140)))
				outBuffer.append("height=\"").append("200%\" ");//.append(SVGStatic.dp1f((s.pen.glowWidth>10?200:140)))
				outBuffer.append(">\n");
				outBuffer.append("   <feGaussianBlur in=\"SourceGraphic\" stdDeviation=\"").append(Float.toString(s.pen.glowWidth)).append("\"/>\n");
				outBuffer.append("</filter>\n");
			}
			outBuffer.append("<style><![CDATA[\n");
			outBuffer.append(".");
			SVGStatic.writeID(outBuffer,s,SVG_FGSTYLE_ID_PFX);		
			outBuffer.append("{");
			toSVGFGStyle( s,  outBuffer);
			outBuffer.append("}\n.");
			SVGStatic.writeID(outBuffer,s,SVG_BGSTYLE_ID_PFX);		
			outBuffer.append("{");
			toSVGBGStyle( s,  outBuffer);
			outBuffer.append("}\n");
			outBuffer.append("]]></style>\n");
			makeTip(s, outBuffer,  s.pen.startTip,true,true);
			makeTip(s, outBuffer,  s.pen.endTip,false,true);
			if (s.pen.glowWidth>0) {
				makeTip(s, outBuffer,  s.pen.startTip,true,false);
				makeTip(s, outBuffer,  s.pen.endTip,false,false);
			}
		} catch (IOException e) {
			Log.d(VecGlobals.LOG_TAG, "toSVGDefs pen", e);
		}
	}
	
	private void makeTip(Stroke s, BufferedWriter outBuffer, Tip startType,boolean start,boolean fg)	throws IOException {
		if (startType!=null && startType.type!=null) {
			DecorationPathData pd = startType.getTmplFloatArray();
			float[] trf = new float[pd.pts.length];
			Matrix m = new Matrix();
			float scale = s.pen.strokeWidth*startType.size;
			m.postRotate((startType.inside?-90:90)*(start?1:-1));
			m.postScale(scale,scale);
			m.mapPoints(trf, pd.pts);
			outBuffer.append("<marker id=\"");
			SVGStatic.writeID(outBuffer,s,(start?SVG_ST_TIP_ID_PFX:SVG_END_TIP_ID_PFX)+(fg?"":SVG_BG_TIP_ID_SFX));		
			outBuffer.append("\" markerUnits=\"userSpaceOnUse\" orient=\"auto\" ");
			outBuffer.append("style=\"overflow:visible;stroke-dasharray:0;");
			outBuffer.append("fill:").append(startType.filled?ColorUtil.toColorStringNoAlpha(s.pen.strokeColour):"none").append(";");
			outBuffer.append("opacity:").append(ColorUtil.toAlphaString(s.pen.strokeColour)).append(";");
			//toSVGFGStyle( s,  outBuffer);
			outBuffer.append("\" class=\"");
			SVGStatic.writeID(outBuffer,s,fg?SVGPen.SVG_FGSTYLE_ID_PFX:SVGPen.SVG_BGSTYLE_ID_PFX);
			outBuffer.append("\" >\n");
			outBuffer.append("<path ");
			outBuffer.append("d=\"");
			SVGStatic.toSVGPath(outBuffer, pd, trf);
			outBuffer.append("\" />\n");
			outBuffer.append("</marker>\n");
		}
	}
}
