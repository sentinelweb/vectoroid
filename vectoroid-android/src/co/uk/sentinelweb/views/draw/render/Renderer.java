package co.uk.sentinelweb.views.draw.render;
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
import java.util.Collection;
import java.util.HashMap;

import android.content.Context;
import co.uk.sentinelweb.views.draw.model.DrawingElement;
import co.uk.sentinelweb.views.draw.model.Group;
import co.uk.sentinelweb.views.draw.model.Stroke;
import co.uk.sentinelweb.views.draw.model.UpdateFlags;

public abstract class Renderer {
	public Context c;
	public HashMap<DrawingElement,RenderObject> renderObjects;
	
	public abstract void update(DrawingElement de,UpdateFlags flags);
	public abstract void render(DrawingElement de);
	public abstract void setup();
	
	public abstract void setupViewPort();
	public abstract void revertViewPort();
	
	public Renderer(Context c) {
		this.c=c;
		renderObjects=new HashMap<DrawingElement,RenderObject>();
		
	}
	
	public void removeFromCache(Collection<DrawingElement> els) {
		if (els!=null) {
			for (DrawingElement de : els) {
				removeFromCache(de);
			}
		}
	}
	
	public void removeFromCache(DrawingElement oldStroke) {
		if (oldStroke instanceof Stroke) {
			renderObjects.remove(oldStroke);
		} else if (oldStroke instanceof Group) {
			renderObjects.remove(oldStroke);
			for (DrawingElement de : ((Group) oldStroke).elements) {
				removeFromCache(de);
			}
		}
	}
	
	public void dropCache() {
		renderObjects.clear();
	}
	
	public RenderObject getObject(DrawingElement de) {
		return null;
	}

}
