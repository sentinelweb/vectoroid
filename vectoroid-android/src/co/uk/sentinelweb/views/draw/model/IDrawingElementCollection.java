package co.uk.sentinelweb.views.draw.model;

import java.util.ArrayList;

public interface IDrawingElementCollection {
	public abstract ArrayList<Stroke> getAllStrokes();
	public DrawingElement findById(String id);
	public DrawingElement element();
}
