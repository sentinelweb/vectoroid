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
import co.uk.sentinelweb.views.draw.model.UpdateFlags.UpdateType;
import co.uk.sentinelweb.views.draw.render.VecRenderObject;
import co.uk.sentinelweb.views.draw.render.VecRenderer;
import co.uk.sentinelweb.views.draw.render.ag.AndGraphicsRenderer;
import co.uk.sentinelweb.views.draw.util.PointUtil;

public class Group extends DrawingElement  implements IDrawingElementCollection{	
	public ArrayList<DrawingElement> elements;
	//private ArrayList<Stroke> _substrokes;
	public Group() {
		super();
		elements=new ArrayList<DrawingElement>();
	}

	@Override
	public DrawingElement duplicate(boolean shallow) {
		Group g = new Group();
		return copyTo(g,shallow);
	}

	protected DrawingElement copyTo(Group g,boolean shallow) {
		if (!shallow) {
			for (DrawingElement de : elements) {
				DrawingElement duplicate = de.duplicate();
				g.elements.add(duplicate);
				//duplicate.update();
			}
		}
		g.locked=locked;
		g.visible=visible;
		return g;
	}
	//public void clearCached() {
	//	_substrokes=null;
	//}
	@Override
	public void update(boolean deep, VecRenderer r, UpdateFlags flags) {
		//updateBoundsAndCOG(deep);
		//_substrokes =  getStrokes(this);
		if (deep) {
			for (DrawingElement de : elements) {
				if (de instanceof Stroke) {
					((Stroke)de).update(true, r,flags);
				} else {
					((Group)de).update(true, r, flags);
				}
			}
			if (flags.updateTypes.contains(UpdateType.BOUNDS)) {
				updateBoundsAndCOG(true);
			}
		} else {
			if (flags.updateTypes.contains(UpdateType.BOUNDS)) {
				updateBoundsAndCOG(false);
			}
		}
		if (flags.runListeners && updateListener!=null ) {
			updateListener.onAsync(this);
		}
	}
	/*
	public void updateBoundsAndCOG() {
		updateBoundsAndCOG(false);
	}
	*/
	public void updateBoundsAndCOG(boolean deep) {
		this.calculatedCOG = new PointF(-1,-1);
		this.calculatedBounds = new RectF(1e8f,1e8f,-1e8f,-1e8f);
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
	}
	
	@Override
	public ArrayList<Stroke> getAllStrokes() {
		return getAllStrokes(this);
	}
	
	public static ArrayList<Stroke> getAllStrokes(Group g) {
		ArrayList<Stroke> strokes = new ArrayList<Stroke>();
		for (DrawingElement de : g.elements) {
			if (de instanceof Stroke) {
				strokes.add((Stroke)de);
			} else {
				strokes.addAll(((Group)de).getAllStrokes());
			}
		}
		return strokes;
	}
	
	public int getDepth() {
		int depth = 1;
		for (DrawingElement de : elements) {
			if (de instanceof Group) {
				depth = Math.max(depth, ((Group)de).getDepth()+1);
			}
		}
		return depth;
	}
	
	/**
	 * @return the id
	 */
	public String getId() {
		if (id==null) {id="Group_"+hashCode();}
		return id;
	}

	/**
	 * Searches an element in the group first and then goes depth first
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
		//then depth first
		for (DrawingElement de : elements) {
			if (de instanceof Group) {
				DrawingElement res =  ((Group)de).findById(id);
				if (res!=null)  return res;
			}
		}
		return null;
	}
	
	public void applyTransform(TransformOperatorInOut t , DrawingElement tgtde) {// stub
		Group tgt = (Group) tgtde;
	}
	@Override
	public DrawingElement element() {
		return this;
	}
}
