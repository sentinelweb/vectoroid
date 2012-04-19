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
import android.graphics.PointF;
import android.graphics.RectF;
import co.uk.sentinelweb.views.draw.render.ag.AndGraphicsRenderer;
import co.uk.sentinelweb.views.draw.util.OnAsyncListener;

public abstract class DrawingElement {
	public static int UPD_NO_FILL=1;
	public static int UPD_FILL_NO_BITMAP=2;
	public static int UPD_FILL_GRADIENT_ONLY=4;
	
	protected String id = null;
	
	public RectF calculatedBounds ;
	public PointF calculatedCOG;
	public PointF calculatedCentre;
	public PointF calculatedDim;
	
	public boolean locked = false;
	public boolean visible = true;
	
	OnAsyncListener updateListener;
	
	public DrawingElement duplicate() {
		return duplicate(false);
	}
	public abstract DrawingElement duplicate(boolean shallow);
	public abstract void update(boolean deep,AndGraphicsRenderer r,UpdateFlags flags);
	//protected abstract void updateBoundsAndCOG();
	protected abstract void updateBoundsAndCOG(boolean deep);
	
	public OnAsyncListener<?> getUpdateListener() {
		return updateListener;
	}

	public void setUpdateListener(OnAsyncListener<?> updateListener) {
		this.updateListener = updateListener;
	}

	/**
	 * @return the locked
	 */
	public boolean isLocked() {
		return locked;
	}

	/**
	 * @param locked the locked to set
	 */
	public void setLocked(boolean locked) {
		this.locked = locked;
	}

	/**
	 * @return the id
	 */
	public String getId() {
		if (id==null) {id=""+hashCode();}
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the visible
	 */
	public boolean isVisible() {
		return visible;
	}

	/**
	 * @param visible the visible to set
	 */
	public void setVisible(boolean visible) {
		this.visible = visible;
	}
	
	public String getInfo() {
		StringBuilder sb = new StringBuilder(150);
		sb.append("Locked : ");
		sb.append(locked);
		sb.append("\n");
		sb.append("Visible : ");
		sb.append(visible);
		sb.append("\n");
		return sb.toString();
	}
}
