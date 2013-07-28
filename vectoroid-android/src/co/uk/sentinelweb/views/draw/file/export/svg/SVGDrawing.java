package co.uk.sentinelweb.views.draw.file.export.svg;


import java.io.BufferedWriter;
import java.util.HashMap;

import android.util.Log;
import co.uk.sentinelweb.views.draw.VecGlobals;
import co.uk.sentinelweb.views.draw.controller.FontController.Font;
import co.uk.sentinelweb.views.draw.file.SaveFile;
import co.uk.sentinelweb.views.draw.file.SaveFile.Option;
import co.uk.sentinelweb.views.draw.model.Drawing;
import co.uk.sentinelweb.views.draw.model.DrawingElement;
import co.uk.sentinelweb.views.draw.model.Fill.Type;
import co.uk.sentinelweb.views.draw.model.Group;
import co.uk.sentinelweb.views.draw.model.Layer;
import co.uk.sentinelweb.views.draw.model.Stroke;
import co.uk.sentinelweb.views.draw.util.FileUtil;

public class SVGDrawing extends SVGParent{
	SVGGroup sg;
	SVGFill sf;
	HashMap<String, String> _assestIds = new HashMap<String, String>();
	public SVGDrawing(SaveFile _saveFile) {
		super(_saveFile);
		sg=new SVGGroup(_saveFile);
		sf = new SVGFill(_saveFile);
	}
	public void toSVG(Drawing d,	BufferedWriter outBuffer) {
		try {
			outBuffer.append("<?xml version=\"1.0\" standalone=\"no\"?>"+
				"<!DOCTYPE svg PUBLIC \"-//W3C//DTD SVG 1.1//EN\" \"http://www.w3.org/Graphics/SVG/1.1/DTD/svg11.dtd\">\n");

			outBuffer.append("<svg");
			outBuffer.append(" width=\"").append(Float.toString(d.size.x)).append("px\"");
			outBuffer.append(" height=\"").append(Float.toString(d.size.y)).append("px\""); 
			outBuffer.append(" version=\"1.1\" xmlns=\"http://www.w3.org/2000/svg\"");
			outBuffer.append(" xmlns:xlink=\"http://www.w3.org/1999/xlink\" ");
			outBuffer.append(">\n");
			outBuffer.append("<defs>\n");
			outBuffer.append("<style><![CDATA[\n");
			if (_saveFile!=null && _saveFile._assetList!=null) {// TODO 150612 : check reload assets?
				for (Object o :_saveFile._assetList) {
					if (o instanceof Font) {
						Font f = (Font)o;
						outBuffer.append("@font-face {\nfont-family: ").append(f._fontName).append(";\n");
						outBuffer.append("src: url('");
						if (_saveFile._options.contains(Option.EMBED_ASSET)) {
							_saveFile.getAssetManager().encodeDataURL(f._fontFile, outBuffer, "font/ttf");
						} else {
							outBuffer.append(FileUtil.getRelativePath(f._fontFile,_saveFile.getDataDir()));
						}
						outBuffer.append("')  format('truetype');\n}\n");
					}
				}
			}
			outBuffer.append("]]></style>\n");
			sf.toSVGDefs(d, outBuffer);
			outBuffer.append("</defs>\n");
			if (d.background.type!=Type.NONE) {
				outBuffer.append("<rect id=\"background\" x=\"0\" y=\"0\" " );
				outBuffer.append(" width=\"").append(Float.toString(d.size.x)).append("px\"");
				outBuffer.append(" height=\"").append(Float.toString(d.size.y)).append("px\""); 
				outBuffer.append(" style=\"");
				sf.toSVGStyle(d, outBuffer);
				outBuffer.append("\" />\n");
			}
			//drawingJSON.put(JSON_BG, jsf.toJSON(d.background));
			//JSONArray jsElements = new JSONArray();
			for (DrawingElement de : d.elements) {
				if (de instanceof Group) {
					sg.toSVG((Group)de, outBuffer);
				} else if (de instanceof Stroke) {
					sg.ss.toSVG((Stroke)de, outBuffer);
				}
				outBuffer.flush();
			}
			for (Layer de : d.layers) {
				sg.toSVG(de, outBuffer);
				outBuffer.flush();
			}
			outBuffer.append("</svg>\n");
			outBuffer.flush();
		} catch (Exception e) {
			Log.d(VecGlobals.LOG_TAG, "SVG Drawing to drawing", e);
		}
	}
}
