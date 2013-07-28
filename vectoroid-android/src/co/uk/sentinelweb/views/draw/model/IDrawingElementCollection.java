package co.uk.sentinelweb.views.draw.model;

import java.util.ArrayList;
import co.uk.sentinelweb.views.draw.model.Stroke;
public interface IDrawingElementCollection {
	public abstract ArrayList<Stroke> getAllStrokes();
	public DrawingElement findById(String id);
	public DrawingElement element();
	public abstract ArrayList<Stroke> getStrokesByType(Stroke.Type t);
}
