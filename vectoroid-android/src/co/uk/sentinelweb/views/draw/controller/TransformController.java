package co.uk.sentinelweb.views.draw.controller;
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
import android.util.Log;
import co.uk.sentinelweb.views.draw.VecGlobals;
import co.uk.sentinelweb.views.draw.model.Drawing;
import co.uk.sentinelweb.views.draw.model.DrawingElement;
import co.uk.sentinelweb.views.draw.model.Group;
import co.uk.sentinelweb.views.draw.model.PointVec;
import co.uk.sentinelweb.views.draw.model.Stroke;
import co.uk.sentinelweb.views.draw.model.TransformOperatorInOut;
import co.uk.sentinelweb.views.draw.model.UpdateFlags;
import co.uk.sentinelweb.views.draw.render.ag.AndGraphicsRenderer;

public class TransformController {
	public static void transform(DrawingElement de,DrawingElement de1,  TransformOperatorInOut t, AndGraphicsRenderer r) {
		//if (DVGlobals._isDebug) Log.d(DVGlobals.LOG_TAG,"transform de:"+de.hashCode()+":"+de1.hashCode());
		boolean updateAfter = de instanceof Drawing || de instanceof Group;
		if (!de.getClass().equals(de1.getClass())) {throw new RuntimeException("Only matching types please.");}
		ArrayList<Stroke> processVec= new ArrayList<Stroke>();
		ArrayList<Stroke> processVecTmp= new ArrayList<Stroke>();
		if (de instanceof Drawing) {
			processVec.addAll(((Drawing)de).getAllStrokes());
			processVecTmp.addAll(((Drawing)de1).getAllStrokes());
		} else if (de instanceof Group){
			processVec.addAll(((Group)de).getStrokes());
			processVecTmp.addAll(((Group)de1).getStrokes());
		} else if (de instanceof Stroke) {
			processVec.add((Stroke)de);
			processVecTmp.add((Stroke)de);
		}
		for (int l=processVec.size()-1;l>=0;l--) {
			Stroke original = processVec.get(l);
			Stroke tmp = processVecTmp.get(l);
			transform(original,tmp, t);
			if (!updateAfter) {tmp.update(false, r, UpdateFlags.ALL);}
		}
		if (updateAfter) {
			de1.update(true, r, UpdateFlags.ALL);
		}
	}
	// note for points transform this will need a point selection
	// might be best to make another method
	public static void transform(Stroke sin, Stroke sout, TransformOperatorInOut t) {
		PointVec pv = null;
		PointVec pvtmp = null;
		for (int j=sin.points.size()-1;j>=0;j--) {
				pv = sin.points.get(j);
				pvtmp = sout.points.get(j);
			transform(pv, pvtmp, t);
			
			if (pv.beizer1!=null) {
				transform(pv.beizer1, pvtmp.beizer1, t);
			}
			if (pv.beizer2!=null) {
				transform(pv.beizer2, pvtmp.beizer2, t);
			}
		}
		sin.applyTransform(t,sout);
	}
	
	
	public static void transform(ArrayList<PointF> points,ArrayList<PointF> pointsOut, TransformOperatorInOut t) {
		for (int j=points.size()-1;j>=0 ;j--) {
			PointF pt2 = pointsOut.get(j);
			if (pt2!=null) {
				PointF pt = points.get(j);
				//if (j==points.size()-1) {
				//	Log.d(DVGlobals.LOG_TAG,"transform in:"+PointUtil.tostr(pt)+":"+pt.hashCode()+":"+t.scaleValue);
				//}
				t.operate(pt,pt2);
				//if (j==points.size()-1) {
			//		Log.d(DVGlobals.LOG_TAG,"transform out:"+PointUtil.tostr(pt2)+":"+pt.hashCode()+":"+t.scaleValue);
				//}
			}
		}
	}
}
