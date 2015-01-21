package co.uk.sentinelweb.views.draw.model;
/*
Vectoroid API for Android
Copyright (C) 2010-12 Sentinel Web Technologies Ltd
All rights reserved.
 
This software is made available under a Dual Licence:
 
Use is permitted under LGPL terms for Non-commercial projects
  
see: LICENCE.txt for more information

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met:
    * Redistributions of source code must retain the above copyright
      notice, this list of conditions and the following disclaimer.
    * Redistributions in binary form must reproduce the above copyright
      notice, this list of conditions and the following disclaimer in the
      documentation and/or other materials provided with the distribution.
    * Neither the name of the Sentinel Web Technologies Ltd. nor the
      names of its contributors may be used to endorse or promote products
      derived from this software without specific prior written permission.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
DISCLAIMED. IN NO EVENT SHALL SENTINEL WEB TECHNOLOGIES LTD BE LIABLE FOR ANY
DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
(INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
(INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/
import java.util.ArrayList;

import android.graphics.PointF;
import android.graphics.RectF;
import co.uk.sentinelweb.views.draw.render.VecRenderObject;
import co.uk.sentinelweb.views.draw.render.VecRenderer;
import co.uk.sentinelweb.views.draw.util.PointUtil;


public class Drawing extends DrawingElement implements IDrawingElementCollection{
	
	public PointF size = new PointF( 640f, 480f );
	public ArrayList<DrawingElement> elements = new ArrayList<DrawingElement>();
	public ArrayList<Layer> layers = new ArrayList<Layer>();
	public Fill background;
	
	public Drawing() {
		background = new Fill();
	}
	// THIS DOES NOT CALL UPDATE (as its used for undo)
	public void copyFrom(Drawing d) {
		size.set(d.size);
		background=d.background.duplicate();
		id=d.id;
		elements.clear();
		for (DrawingElement de : d.elements) {
			elements.add(de);
		}
		layers.clear();
		for (Layer de : d.layers) {
			layers.add(de);
		}
		//setId(id);
	}
	
	// THIS DOES NOT CALL UPDATE (as its used for undo)
	public DrawingElement duplicate(boolean shallow) {
		Drawing d = new Drawing();
		d.id=id;
		d.size.set(this.size);
		d.background=this.background.duplicate();
		d.setId(this.getId());
		if (!shallow) {
			for (DrawingElement de : elements) {
				d.elements.add(de.duplicate());
			}
			for (Layer de : layers) {
				Layer duplicate = (Layer)de.duplicate();
				duplicate.setId(de.getId());
				d.layers.add(duplicate);
			}
		}
		return d;
	}
	
	@Override
	public void update(boolean deep, VecRenderer r, UpdateFlags flags) {
		if (deep) {
			for (DrawingElement de : elements) {
				de.update(deep,r,flags);
			}
			for (Layer de : layers) {
				de.update(deep,r,flags);
			}
		}
		//update(deep, r, UpdateFlags.FILLONLY);
		updateBoundsAndCOG( false );
		VecRenderObject dro = (VecRenderObject)r.getObject(this);
		if (dro!=null) {
			dro.update(this, flags);
		}
	}
	
	public void updateBoundsAndCOG(boolean deep) {
		this.calculatedCOG = new PointF(size.x/2,size.y/2);
		this.calculatedBounds = new RectF(0,0,size.x,size.y);
		this.calculatedDim = new PointF(this.calculatedBounds.width(),this.calculatedBounds.height());
		this.calculatedCentre = PointUtil.midpoint(this.calculatedBounds);
		//this.calculatedBounds.set(0,0,size.x,size.y);
		//this.calculatedCentre = PointUtil.midpoint(this.calculatedBounds);
		//this.calculatedCOG.set(calculatedCentre);
		//this.calculatedDim.set(size);
		/*
		this.calculatedCOG = new PointF(-1,-1);
		this.calculatedBounds = new RectF(10000,10000,-10000,-10000);
		float ctr=0;
		for (DrawingElement de : elements) {
			//if (de==null) {continue;}// do i want to do this?
			if (deep) {	
				de.updateBoundsAndCOG(deep);
			}//leave for the moment 
			//Log.d(Globals.LOG_TAG, "this.calculatedCOG:"+this.calculatedCOG+": _de:"+de.calculatedCOG);
			this.calculatedCOG.x += de.calculatedCOG.x;
			this.calculatedCOG.y += de.calculatedCOG.y;
			this.calculatedBounds.top=Math.min(this.calculatedBounds.top, de.calculatedBounds.top);
			this.calculatedBounds.left=Math.min(this.calculatedBounds.left, de.calculatedBounds.left);
			this.calculatedBounds.bottom=Math.max(this.calculatedBounds.bottom, de.calculatedBounds.bottom);
			this.calculatedBounds.right=Math.max(this.calculatedBounds.right, de.calculatedBounds.right);
			ctr++;
		}
		if (ctr>0) {
			this.calculatedCOG.x/=ctr;
			this.calculatedCOG.y/=ctr;
		}
		this.calculatedDim = new PointF(this.calculatedBounds.width(),this.calculatedBounds.height());
		this.calculatedCentre = PointUtil.midpoint(this.calculatedBounds);
		*/
	}
	
	public void computeBounds(RectF rect) {
		rect.set(1e8f,1e8f,-1e8f,-1e8f);
		for (DrawingElement de : elements) {
			if (de!=null && de.calculatedBounds!=null) {
				rect.top=Math.min(rect.top, de.calculatedBounds.top);
				rect.left=Math.min(rect.left, de.calculatedBounds.left);
				rect.bottom=Math.max(rect.bottom, de.calculatedBounds.bottom);
				rect.right=Math.max(rect.right, de.calculatedBounds.right);
			} //else {

		}
		
		for (Layer l : layers) {
			for (DrawingElement de : l.elements) {
				if (de!=null && de.calculatedBounds!=null) {
					rect.top=Math.min(rect.top, de.calculatedBounds.top);
					rect.left=Math.min(rect.left, de.calculatedBounds.left);
					rect.bottom=Math.max(rect.bottom, de.calculatedBounds.bottom);
					rect.right=Math.max(rect.right, de.calculatedBounds.right);
				} //else {
				//	Log.d(DVGlobals.LOG_TAG, "Drawing.computeBounds: "+de+" ; "+(de!=null?de.calculatedBounds:""));
				//}
			}
		}
	}
	
	@Override
	public ArrayList<Stroke> getAllStrokes() {
		ArrayList<Stroke> strokes = new ArrayList<Stroke>();
		for (DrawingElement de : elements) {
			if (de instanceof Group) {
				strokes.addAll(((Group) de).getAllStrokes());
			} else {
				strokes.add((Stroke)de);
			}
		}
		for (Layer de : layers) {
			strokes.addAll(de.getAllStrokes());
		}
		return strokes;
	}
	
	public ArrayList<Stroke> getStrokesByType(Stroke.Type t){
		ArrayList<Stroke> strokes = new ArrayList<Stroke>();
		for (DrawingElement de : elements) {
			if (de instanceof Stroke) {
				Stroke stroke = (Stroke)de;
				if (t==stroke.type) strokes.add((Stroke)de);
			} else {
				strokes.addAll(((Group)de).getStrokesByType(t));
			}
		}
		for (Layer de : layers) {
			strokes.addAll(de.getStrokesByType(t));
		}
		return strokes;
	}
	/**
	 * @return the id
	 */
	public String getId() {
		if (id==null) {id="Drawing_"+hashCode();}
		return id;
	}
	
	public Layer getLayer(String id) {
		if (id==null) {return null;}
		for (Layer de : layers) {
			if (id.equals(de.id)) {
				return de;
			}
		}
		return null;
	}
	/**
	 * Searches an element in the elements & layers first and then goes depth first through the elements and layers
	 * @return the drawingElement or null if not found or id is null
	 */
	@Override
	public DrawingElement findById(String id) {
		if (id==null) {return null;}
		// search the current group first
		for (DrawingElement de : elements) {
			if (id.equals(de.id)) {
				return de;
			}
		}
		for (DrawingElement de : layers) {
			if (id.equals(de.id)) {
				return ((Layer)de).findById(id);
			}
		}
		//then depth first
		for (DrawingElement de : elements) {
			if (de instanceof Group) {
				return ((Group)de).findById(id);
			}
		}
		for (DrawingElement de : layers) {
				return ((Layer)de).findById(id);
		}
		return null;
	}
	
	public IDrawingElementCollection getParent(DrawingElement child){
		if (elements.contains(child)) return this; 
		for (Layer de : layers) {
			if (de.elements.contains(child)) return this; 
		}
		for (DrawingElement de : elements) {
			if (de instanceof Group) {
				Group g = (Group)de;
				IDrawingElementCollection de1 = g.getParent(child);
				if (de1!=null) {
					return de1;
				}
			} 
		}
		for (Layer de : layers) {
			IDrawingElementCollection de1 = de.getParent(child);
				if (de1!=null) {
					return de1;
				}
		}
		return null;
	}
	public void applyTransform(TransformOperatorInOut t , DrawingElement tgtde) {
		Drawing tgt = (Drawing) tgtde;
		switch (t.axis) {
			case PRESERVE_ASPECT:tgt.size.set(size.x*(float)t.scaleValue,size.y*(float)t.scaleValue);break;
			case BOTH:tgt.size.set(size.x*(float)t.scaleXValue,size.y*(float)t.scaleYValue);break;
			case X:tgt.size.set(size.x*(float)t.scaleXValue,size.y);break;
			case Y:tgt.size.set(size.x,size.y*(float)t.scaleYValue);break;
		}
		
	}
	@Override
	public DrawingElement element() {
		return this;
	}
	
}
