package co.uk.sentinelweb.views.draw.file.export.json.v3.gson;

import java.io.IOException;
import java.util.HashMap;

import android.graphics.RectF;
import co.uk.sentinelweb.views.draw.file.export.json.v3.JSONConst;
import co.uk.sentinelweb.views.draw.model.DrawingElement;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

public class GSONDrawingElement {
	public static boolean fromJSON(Gson gson,JsonReader reader,DrawingElement de,String name) {
		try {
			if (JSONConst.JSON_ID.equals(name)) {
				de.setId(reader.nextString());return true;
			} else if (JSONConst.JSON_VISIBLE.equals(name) || "visble".equals(name)) {
				de.visible = reader.nextBoolean();return true;
			} else if (JSONConst.JSON_LOCKED.equals(name)) {
				de.locked = reader.nextBoolean();return true;
			} else if (JSONConst.JSON_CLASS.equals(name)) {
				de.className = reader.nextString();return true;
			} else if (JSONConst.JSON_CLIPRECT.equals(name)) {
				de.clipRect = gson.fromJson(reader, RectF.class);return true;
			} else if (JSONConst.JSON_OPACITY.equals(name)) {
				de.opacity = (float)reader.nextDouble();return true;
			} else if (JSONConst.JSON_NAMESPACED.equals(name)) {
				HashMap<String,HashMap<String,Object>> fld = gson.fromJson(reader,new TypeToken<HashMap<String, HashMap<String, Object>>>(){}.getType());
				if (fld!=null) {
					for (String nameSpace:fld.keySet()) {
						de.setNameSpaced(nameSpace, fld.get(nameSpace));
					}
				}
				return true;
			} else {
				return false;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}
}
