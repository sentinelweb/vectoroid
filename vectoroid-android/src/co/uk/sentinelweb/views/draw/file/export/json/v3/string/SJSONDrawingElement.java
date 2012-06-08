package co.uk.sentinelweb.views.draw.file.export.json.v3.string;

import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import co.uk.sentinelweb.views.draw.file.export.json.v3.JSONConst;
import co.uk.sentinelweb.views.draw.model.DrawingElement;

public class SJSONDrawingElement {
	public static void writeDE(Writer w,DrawingElement de,String type) throws IOException {
		if (type!=null) {
			SJSON.writeString( w,JSONConst.JSON_EL_TYPE,type);
			SJSON.sep(w);
		}
		if (de.getId()!=null && !"".equals(de.getId())) {
			SJSON.writeString( w,JSONConst.JSON_ID,de.getId());
			SJSON.sep(w);
		}
		if (!de.visible) {
			SJSON.writeBoolean( w,JSONConst.JSON_VISIBLE,de.visible);
			SJSON.sep(w);
		}
		if (de.locked) {
			SJSON.writeBoolean( w,JSONConst.JSON_LOCKED,de.locked);
			SJSON.sep(w);
		}
		if (de.className!=null) {
			SJSON.writeString( w,JSONConst.JSON_CLASS,de.className);
			SJSON.sep(w);
		}
		SJSON.writeFloat( w,JSONConst.JSON_OPACITY,de.opacity);
		SJSON.sep(w);
		if (de.clipRect!=null) {
			try {
				JSONObject jsonRect = new JSONObject();
				jsonRect.put("top", de.clipRect.top);
				jsonRect.put("bottom", de.clipRect.bottom);
				jsonRect.put("left", de.clipRect.left);
				jsonRect.put("right", de.clipRect.right);
				SJSON.writeID( w,JSONConst.JSON_CLIPRECT);
				SJSON.writeJSON(w, jsonRect);
				SJSON.sep(w);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		HashMap<String,HashMap<String,Object>> nameSpaced = de.getNameSpaced();
		if (nameSpaced!=null && !nameSpaced.isEmpty()) {
			JSONObject jsonNameSpaced = new JSONObject();
			for (String name : nameSpaced.keySet()) {
				HashMap<String,Object> nsmap = nameSpaced.get(name);
				try {
					JSONObject jnsmap = new JSONObject();
					for (String nameInner:nsmap.keySet()) {
						jnsmap.put(nameInner, nsmap.get(nameInner));
					}
					jsonNameSpaced.put(name,jnsmap);
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
			SJSON.writeID( w,JSONConst.JSON_NAMESPACED);
			SJSON.writeJSON(w, jsonNameSpaced);
			SJSON.sep(w);
		}
	
	}

}
