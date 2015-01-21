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
import android.graphics.Shader;
import android.graphics.Shader.TileMode;
import co.uk.sentinelweb.views.draw.util.ColorUtil;

public class Gradient {
	public enum Type {LINEAR,RADIAL,SWEEP};
	public Type type=Type.LINEAR;
	public int[] colors;
	public float[] positions;
	public Shader.TileMode tile = TileMode.CLAMP;
	public GradientData data ;
	//public float radius;
	public Gradient duplicate() {
		Gradient newGrad = new Gradient();
		newGrad.type=type;
		if (colors!=null) {
			newGrad.colors=new int[colors.length];
			for (int i=colors.length-1;i>=0;i--) {newGrad.colors[i]=colors[i];}
		}
		if (positions!=null) {
			newGrad.positions=new float[positions.length];
			for (int i=positions.length-1;i>=0;i--) {newGrad.positions[i]=positions[i];}
		}
		newGrad.tile = tile;
		if (data!=null) {
			newGrad.data=data.duplicate();
		}
		//newGrad.radius=radius;
		return newGrad;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("[");
		sb.append(type);
		if (colors!=null && colors.length>0) {
			sb.append(" : ");
			sb.append(ColorUtil.toColorString(colors[0]));
			sb.append(" -> ");
			sb.append(ColorUtil.toColorString(colors[colors.length-1]));
			sb.append("(");
			sb.append(colors.length);
			sb.append(" colors )");
		}
		sb.append("]");
		return sb.toString();
	}
	
	
}
