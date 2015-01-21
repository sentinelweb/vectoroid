package co.uk.sentinelweb.views.draw.util;
/*
Vectoroid API for Andrid
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

public class BoundsUtil {
	public static boolean checkBounds( RectF r, PointF p ) {
		return r.top<p.y && r.bottom>p.y && r.left<p.x && r.right>p.x;
	}
	
	public static boolean checkBounds( RectF r, RectF tst ) {// tests that tst in within the bounds of r
		return r.top<tst.top && r.bottom>tst.bottom && r.left<tst.left && r.right>tst.right;
	}
	
	public static boolean checkBoundsIntersect( RectF r, RectF tst ) {// tests that tst in within the bounds of r
		if (tst!=null) {
			return r.intersect(tst);
		} else {return false;}
	}
	public static boolean checkBoundsIntersect( RectF r, RectF tst ,float margin) {// tests that tst in within the bounds of r
		if (tst!=null) {
			return r.intersect(tst.left-margin,tst.top-margin,tst.right+margin,tst.bottom+margin);
		} else {return false;}
	}
}
