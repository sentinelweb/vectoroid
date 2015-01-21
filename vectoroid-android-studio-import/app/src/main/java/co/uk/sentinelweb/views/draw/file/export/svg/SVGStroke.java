package co.uk.sentinelweb.views.draw.file.export.svg;

import java.io.BufferedWriter;
import java.io.IOException;

import android.util.Log;
import co.uk.sentinelweb.views.draw.VecGlobals;
import co.uk.sentinelweb.views.draw.file.AssetManager;
import co.uk.sentinelweb.views.draw.file.SaveFile;
import co.uk.sentinelweb.views.draw.file.SaveFile.Option;
import co.uk.sentinelweb.views.draw.file.export.json.v3.SVGStatic;
import co.uk.sentinelweb.views.draw.model.Fill;
import co.uk.sentinelweb.views.draw.model.PointVec;
import co.uk.sentinelweb.views.draw.model.Stroke;
import co.uk.sentinelweb.views.draw.model.Stroke.Type;
import co.uk.sentinelweb.views.draw.model.StrokeDecoration;
import co.uk.sentinelweb.views.draw.model.StrokeDecoration.BreakType;

public class SVGStroke extends SVGParent{
	public static final String SVG_STROKE_ID_PFX="stroke_";
	public static final String SVG_PATH_ID_PFX="path_";
	
	SVGPen sp;
	SVGFill sf;
	
	public SVGStroke(SaveFile _saveFile) {
		super(_saveFile);
		sp = new SVGPen(_saveFile);
		sf = new SVGFill(_saveFile);
	}

	public void toSVG(Stroke de, BufferedWriter outBuffer) {
		try {
			outBuffer.append("<g");
				outBuffer.append(" id=\"");
				SVGStatic.writeID(outBuffer,de,SVG_STROKE_ID_PFX);
				outBuffer.append("\" ");
				outBuffer.append("style=\"");
				outBuffer.append("fill-rule:").append(de.holesEven?"evenodd":"nonzero").append(";");
				outBuffer.append("\">\n");// end style
			outBuffer.append("\n<defs>\n");
			if (de.type==Type.LINE  || de.type==Type.FREE) {
				outBuffer.append("<path ");
				outBuffer.append(" id=\"");
				SVGStatic.writeID(outBuffer,de,SVG_PATH_ID_PFX);
				outBuffer.append("\" ");
				outBuffer.append("d=\"");
				for (int i=0;i<de.points.size();i++) {
					PointVec pv = de.points.get(i);
					SVGStatic.toSVGPath(outBuffer, pv);
					// end points
				}
				outBuffer.append("\" ");
				outBuffer.append(">\n");
				outBuffer.append("</path>\n");
			}  else if (de.type==Type.TEXT_TTF) {
				outBuffer.append("<text x=\"").append(SVGStatic.dp1f(de.calculatedBounds.left)).append("\"  y=\"").append(SVGStatic.dp1f(de.calculatedBounds.bottom)).append("\" " );
				outBuffer.append(" id=\"");
				SVGStatic.writeID(outBuffer,de,SVG_PATH_ID_PFX);
				outBuffer.append("\" ");
				outBuffer.append("style=\"font-family:");
				if (de.fontName!=null && !"".equals(de.fontName)) {
					outBuffer.append(de.fontName).append(",");
				}
				outBuffer.append("Verdana,sans-serif; font-syle:normal; font-size:").append(Integer.toString(Math.round(de.calculatedBounds.height()))).append("px;\"");
				outBuffer.append(">").append(replaceML(de)).append("</text>\n");
			}
			sf.toSVGDefs(de, outBuffer);
			sp.toSVGDefs(de, outBuffer);
			
			outBuffer.append("</defs>\n");
			//if (de.type==Type.LINE  || de.type==Type.FREE) {
				if (de.pen.glowWidth!=0) {
					outBuffer.append("<use xlink:href=\"#");
					SVGStatic.writeID(outBuffer,de,SVG_PATH_ID_PFX);
					outBuffer.append("\" style=\"");
					//sp.toSVGBGStyle(de, outBuffer);
					outBuffer.append("fill:none;");
					if (de.pen.breakType!=BreakType.SOLID) {
						outBuffer.append("stroke-dasharray:");
						SVGStatic.toSVGFloarArray(outBuffer, StrokeDecoration.getBreakTypeArray(de.pen.breakType),de.pen.strokeWidth);
						outBuffer.append(";");
					}
					if (de.pen.startTip!=null && de.pen.startTip.type!=co.uk.sentinelweb.views.draw.model.StrokeDecoration.Tip.Type.NONE) {
						outBuffer.append("marker-start:url(#");
						SVGStatic.writeID(outBuffer,de,SVGPen.SVG_ST_TIP_ID_PFX+SVGPen.SVG_BG_TIP_ID_SFX);
						outBuffer.append(");");
					}
					if (de.pen.endTip!=null && de.pen.endTip.type!=co.uk.sentinelweb.views.draw.model.StrokeDecoration.Tip.Type.NONE) {
						outBuffer.append("marker-end:url(#");
						SVGStatic.writeID(outBuffer,de,SVGPen.SVG_END_TIP_ID_PFX+SVGPen.SVG_BG_TIP_ID_SFX);
						outBuffer.append(");");
					}
					outBuffer.append("\" class=\"");
					SVGStatic.writeID(outBuffer,de,SVGPen.SVG_BGSTYLE_ID_PFX);
					outBuffer.append("\" />\n");
				}
				if (de.fill.type==Fill.Type.BITMAP && de.fill._bitmapFill!=null) {
					outBuffer.append("<image x=\"").append(SVGStatic.dp1f(de.calculatedBounds.left)).append("px\" ");
					outBuffer.append("y=\"").append(SVGStatic.dp1f(de.calculatedBounds.top)).append("px\" ");
					outBuffer.append("width=\"").append(SVGStatic.dp1f(de.calculatedBounds.width())).append("px\" ");
					outBuffer.append("height=\"").append(SVGStatic.dp1f(de.calculatedBounds.height())).append("px\" ");
					outBuffer.append("preserveAspectRatio=\"none\" ");
					outBuffer.append("style=\"clip-path:url(#");
					SVGStatic.writeID(outBuffer,de,SVGFill.SVG_CLIP_ID_PFX);
					outBuffer.append(");opacity:").append(Float.toString(de.fill._bitmapAlpha/255f)).append(";");
					outBuffer.append("\" ");
					outBuffer.append("xlink:href=\"");
					if (_saveFile._options.contains(Option.EMBED_ASSET)) {
						_saveFile.getAssetManager().encodeDataURL(_saveFile.getAssetManager().getAssetFile(de.fill._bitmapFill), outBuffer, "image/png");
					} else {
						outBuffer.append("./"+AssetManager.ASSETS_DIRECTORY+"/"+_saveFile.getAssetManager().getAssetFile(de.fill._bitmapFill).getName());
					}
					outBuffer.append("\" />\n");
					//y=\"300" width="150px" height="200px" preserveAspectRatio="none" style="clip-path:url(#MyClip);" xlink:href="./.assets/2011-06-21-01-46-35.png" />
				}
				outBuffer.append("<use xlink:href=\"#");
				SVGStatic.writeID(outBuffer,de,SVG_PATH_ID_PFX);
				outBuffer.append("\" style=\"");
				//sp.toSVGFGStyle(de, outBuffer,(de.pen.startType!=null && de.pen.startType.type!=null));
				sf.toSVGStyle(de, outBuffer);
				if (de.pen.startTip!=null && de.pen.startTip.type!=co.uk.sentinelweb.views.draw.model.StrokeDecoration.Tip.Type.NONE) {
					outBuffer.append("marker-start:url(#");
					SVGStatic.writeID(outBuffer,de,SVGPen.SVG_ST_TIP_ID_PFX);
					outBuffer.append(");"); 
				}
				if (de.pen.endTip!=null && de.pen.endTip.type!=co.uk.sentinelweb.views.draw.model.StrokeDecoration.Tip.Type.NONE) {
					outBuffer.append("marker-end:url(#");
					SVGStatic.writeID(outBuffer,de,SVGPen.SVG_END_TIP_ID_PFX);
					outBuffer.append(");");
				}
				if (de.pen.breakType!=BreakType.SOLID) {
					outBuffer.append("stroke-dasharray:");
					SVGStatic.toSVGFloarArray(outBuffer, StrokeDecoration.getBreakTypeArray(de.pen.breakType),de.pen.strokeWidth);
					outBuffer.append(";");
				}
				outBuffer.append("\" class=\"");
				SVGStatic.writeID(outBuffer,de,SVGPen.SVG_FGSTYLE_ID_PFX);
				outBuffer.append("\" />\n");
			//} else if (de.type==Type.TEXT_TTF) {
				//outBuffer.append("<text x=\"").append("").append("\"  y=\"").append("\" " );
				//outBuffer.append("style=\"font-family:").append(de.fontName).append(",Verdana,sans-serif; font-syle:normal;font-size:").append("").append("px;");
				//stroke: #0000ff;fill:#00ff00;">Bjork</text>);
				
			//}  
			outBuffer.append("</g>\n");
			
		} catch (IOException e) {
			Log.d(VecGlobals.LOG_TAG, "SVG to stroke", e);
		}
	}

	private String replaceML(Stroke de) {
		return de.text.replaceAll("&", "&amp;").replaceAll("\n", "<br/>");
	}
	
}
