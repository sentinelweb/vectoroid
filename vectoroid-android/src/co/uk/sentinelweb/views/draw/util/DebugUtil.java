package co.uk.sentinelweb.views.draw.util;
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
import android.util.Log;
import co.uk.sentinelweb.views.draw.VecGlobals;

public class DebugUtil {
	public static StringBuffer _useStringBuffer = new StringBuffer();
	public static void logMemory(String label) {
		long usedMem = Runtime.getRuntime().totalMemory()-Runtime.getRuntime().freeMemory();
		String umemK = (usedMem/1024)+"K";
		String tmemK = (Runtime.getRuntime().totalMemory()/1024)+"K";
		float pcfree = (Runtime.getRuntime().freeMemory()*100/Runtime.getRuntime().totalMemory());
		Log.d(VecGlobals.LOG_TAG, label+": memory: " +	"used: used:"+umemK+": total:"+tmemK+":"+pcfree+" % free.");
	}
	public static void logCall(String label, Throwable e) {
		logCall( label,  e,5);
	}
	public static void logCall(String label, Throwable e,int limit) {
		StackTraceElement ste[] = e.getStackTrace();
		int cnt = 0;
		clear();
		for (StackTraceElement st:ste) {
			_useStringBuffer.append(st.getClassName());
			_useStringBuffer.append(".");
			_useStringBuffer.append(st.getMethodName());
			_useStringBuffer.append(":");
			_useStringBuffer.append(st.getLineNumber());
			_useStringBuffer.append(",\n");
			cnt++;
			if (limit>-1 && cnt>limit) break;
		}
		Log.d(VecGlobals.LOG_TAG, label+": stack: " +	_useStringBuffer.toString());
	}
	
	public static void clear() {
		if (_useStringBuffer.length()>0) {_useStringBuffer.delete(0, _useStringBuffer.length()-1);}
		
	}
}
