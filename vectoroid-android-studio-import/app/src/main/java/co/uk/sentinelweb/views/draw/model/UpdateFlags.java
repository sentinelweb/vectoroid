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
import java.util.Arrays;
import java.util.HashSet;

import co.uk.sentinelweb.views.draw.model.Stroke.Type;


public class UpdateFlags {
	// RM Need to have a NONE Type
	public enum UpdateType {PAINT,FILL,PATH,BOUNDS}
	public HashSet<Stroke.Type> paintTypes = null;
	public HashSet<Fill.Type> fillTypes = null;
	public HashSet<UpdateType> updateTypes = null;
	public boolean runListeners = true;
	public boolean expandText = true;
	
	public UpdateFlags() {
		super();
		this.paintTypes = new HashSet<Stroke.Type>(Arrays.asList(Stroke.Type.values()));
		this.fillTypes = new HashSet<Fill.Type>(Arrays.asList(Fill.Type.values()));
		this.updateTypes = new HashSet<UpdateType>(Arrays.asList(UpdateType.values()));
	}
	
	public UpdateFlags copy() {
		UpdateFlags u = new UpdateFlags();
		u.updateTypes.clear();
		u.updateTypes.addAll(updateTypes);
		u.fillTypes.clear();
		u.fillTypes.addAll(fillTypes);
		u.paintTypes.clear();
		u.paintTypes.addAll(paintTypes);
		u.runListeners=runListeners;
		return u;
	}
	/*
	public UpdateFlags set() {
		
	}
	*/
	public static UpdateFlags ALL = new UpdateFlags();
	
	public static UpdateFlags ALL_NOLISTENERS = new UpdateFlags();
	static {ALL_NOLISTENERS.runListeners=false;}
	
	public static UpdateFlags ALL_NOFILLANDLISTENERS = new UpdateFlags();
	static {
		ALL_NOFILLANDLISTENERS.updateTypes.remove(UpdateType.FILL);
		ALL_NOFILLANDLISTENERS.runListeners=false;
	}
	
	public static UpdateFlags FILLONLY = new UpdateFlags();
	static {
		FILLONLY.updateTypes.remove(UpdateType.PAINT);
		FILLONLY.updateTypes.remove(UpdateType.PATH);
		FILLONLY.updateTypes.remove(UpdateType.BOUNDS);
		FILLONLY.runListeners=false;
	}
	
	public static UpdateFlags PATHONLY = new UpdateFlags();
	static {
		PATHONLY.updateTypes.remove(UpdateType.PAINT);
		PATHONLY.updateTypes.remove(UpdateType.FILL);
		PATHONLY.updateTypes.remove(UpdateType.BOUNDS);
		PATHONLY.runListeners=false;
	}
	
	public static UpdateFlags PAINTONLY = new UpdateFlags();
	static {
		PAINTONLY.updateTypes.remove(UpdateType.FILL);
		PAINTONLY.updateTypes.remove(UpdateType.PATH);
		PAINTONLY.updateTypes.remove(UpdateType.BOUNDS);
		PAINTONLY.runListeners=false;
	}
	
	public static UpdateFlags BOUNDSONLY = new UpdateFlags();
	static {
		BOUNDSONLY.updateTypes.remove(UpdateType.FILL);
		BOUNDSONLY.updateTypes.remove(UpdateType.PATH);
		BOUNDSONLY.updateTypes.remove(UpdateType.PAINT);
		BOUNDSONLY.runListeners=false;
	}
	
	public static UpdateFlags PATHBOUNDSONLY = new UpdateFlags();
	static {
		PATHBOUNDSONLY.updateTypes.remove(UpdateType.FILL);
		PATHBOUNDSONLY.updateTypes.remove(UpdateType.PAINT);
		PATHBOUNDSONLY.runListeners=false;
	}
	
	public static UpdateFlags FILLPAINTONLY = new UpdateFlags();
	static {
		FILLPAINTONLY.updateTypes.remove(UpdateType.BOUNDS);
		FILLPAINTONLY.updateTypes.remove(UpdateType.PATH);
		FILLPAINTONLY.runListeners=false;
	}
	
	public static UpdateFlags ALL_FILL_GRADIENTONLY = new UpdateFlags();
	static {
		ALL_FILL_GRADIENTONLY.fillTypes.clear();
		ALL_FILL_GRADIENTONLY.fillTypes.add(Fill.Type.GRADIENT);
		ALL_FILL_GRADIENTONLY.runListeners=false;
	}
}
