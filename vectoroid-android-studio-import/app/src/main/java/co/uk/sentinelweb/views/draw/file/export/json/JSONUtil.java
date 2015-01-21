package co.uk.sentinelweb.views.draw.file.export.json;
/*
Vectoroid API for Andorid
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
import java.io.IOException;
import java.io.Writer;

import android.graphics.PointF;
import android.util.Log;
import co.uk.sentinelweb.views.draw.VecGlobals;

public class JSONUtil {
	public static StringBuilder _useSb = new StringBuilder();
	
	public static PointF fromPointStr(String s) {
		if ("n".equals(s) || "null".equals(s)) {return null;}
		int idx = s.indexOf(",");
		if (idx>0) {// && idx<s.length()-1
			return new PointF(
				Float.parseFloat(s.substring(0,idx)),
				Float.parseFloat(s.substring(idx+1,s.length()))
			);
		} else {
			Log.d(VecGlobals.LOG_TAG, "fromPointStr:incorrect format:"+s);
			return null;
		}
	}

	public static String dp1f(float f) {
		return Float.toString(Math.round(f*10)/10f);
	}

	public static String dp2f(float f) {
		return Float.toString(Math.round(f*100)/100f);
	}

	public static void toPointStr(PointF pointF,StringBuilder sb) {
		if (pointF==null) {sb.append("n");return ;}
		//sb.delete(0, _useSb.length());
		sb.append(dp2f(pointF.x));
		sb.append(",");
		sb.append(dp2f(pointF.y));
		//return _useSb.toString();
	}

	public static void toPointStr(PointF pointF,Writer w) throws IOException {
		if (pointF==null) {w.append("n");return ;}
		//sb.delete(0, _useSb.length());
		w.append(dp2f(pointF.x));
		w.append(",");
		w.append(dp2f(pointF.y));
		//return _useSb.toString();
	}
}
