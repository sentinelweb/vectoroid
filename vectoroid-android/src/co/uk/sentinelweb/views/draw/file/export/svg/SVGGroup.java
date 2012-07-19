package co.uk.sentinelweb.views.draw.file.export.svg;

import java.io.BufferedWriter;
import java.io.IOException;

import android.util.Log;
import co.uk.sentinelweb.views.draw.VecGlobals;
import co.uk.sentinelweb.views.draw.file.SaveFile;
import co.uk.sentinelweb.views.draw.file.export.json.v3.SVGStatic;
import co.uk.sentinelweb.views.draw.model.DrawingElement;
import co.uk.sentinelweb.views.draw.model.Group;
import co.uk.sentinelweb.views.draw.model.Stroke;

public class SVGGroup extends SVGParent{
	public static final String SVG_GRP_ID_PFX="group_";
	SVGStroke ss;
	public SVGGroup(SaveFile _saveFile) {
		super(_saveFile);
		ss=new SVGStroke(_saveFile);
	}

	public void toSVG(Group g, BufferedWriter outBuffer) {
		try {
			outBuffer.append("<g");
			outBuffer.append(" id=\"");
			SVGStatic.writeID(outBuffer,g,SVG_GRP_ID_PFX);
			outBuffer.append("\">");
			for (DrawingElement de : g.elements) {
				if (de instanceof Group) {
					toSVG((Group)de, outBuffer);
				} else if (de instanceof Stroke) {
					ss.toSVG((Stroke)de, outBuffer);
				}
			}
			outBuffer.append("</g>");
		} catch (IOException e) {
			Log.d(VecGlobals.LOG_TAG, "Group to SVG ", e);
		}
	}

}
