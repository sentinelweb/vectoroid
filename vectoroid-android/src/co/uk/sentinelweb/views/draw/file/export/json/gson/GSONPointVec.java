package co.uk.sentinelweb.views.draw.file.export.json.gson;
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
import java.util.ArrayList;

import android.graphics.PointF;
import android.util.Log;
import co.uk.sentinelweb.views.draw.VecGlobals;
import co.uk.sentinelweb.views.draw.file.export.json.JSONConst;
import co.uk.sentinelweb.views.draw.file.export.json.JSONUtil;
import co.uk.sentinelweb.views.draw.model.PointVec;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

public class GSONPointVec {
	
	StringBuilder _useSb = new StringBuilder();
	
	public void toJSON(PointVec pv, Gson gson,JsonWriter writer) throws IOException {
		writer.beginObject();
		writer.name( JSONConst.JSON_CLOSED ).value(pv.closed); 
		writer.name( JSONConst.JSON_ISHOLE ).value(pv.isHole); 
		writer.name( JSONConst.JSON_STARTTIME ).value(pv.startTime); 
		addPointStr( pv, writer,JSONConst.JSON_POINTSSTR );
		if (pv.beizer1!=null) {
			addPointStr( pv.beizer1, writer , JSONConst.JSON_BIEZER1STR );
		}
		if (pv.beizer2!=null) {
			addPointStr( pv.beizer2, writer , JSONConst.JSON_BIEZER2STR );
		}
		writer.endObject();
	}
	
	private void addPointStr(ArrayList<PointF> pv, JsonWriter writer,String tag) throws IOException {
		if (pv!=null) {
			_useSb.delete(0, _useSb.length());
			for (int i=0;i<pv.size();i++) {
				JSONUtil.toPointStr(pv.get(i), _useSb);
				if (i<pv.size()-1) _useSb.append(" ");
			}
			//o.put(tag, _useSb.toString().trim());
			writer.name(tag).value(_useSb.toString().trim()); 
		}
	}
	
	public PointVec fromJSON(Gson gson,JsonReader reader) {
		PointVec pv = new PointVec();
		try {
			reader.beginObject();
			reader.peek();
			while (reader.hasNext()) {
				String name = reader.nextName();
				//Log.d(DVGlobals.LOG_TAG, "nextname:"+name);
				if (JSONConst.JSON_CLOSED.equals(name)) {
					pv.closed = reader.nextBoolean();
				} else if (JSONConst.JSON_ISHOLE.equals(name)) {
					pv.isHole = reader.nextBoolean();
				} else if (JSONConst.JSON_STARTTIME.equals(name)) {
					pv.startTime = reader.nextLong();
				} else if (JSONConst.JSON_POINTSSTR.equals(name)) {
					String[] jsPtsSplit = reader.nextString().split(" ");
					parsePointStr(pv, jsPtsSplit);
				} else if (JSONConst.JSON_BIEZER1STR.equals(name)) {
					String[] jsPtsSplit = reader.nextString().split(" ");
					pv.beizer1=new ArrayList<PointF>();
					parsePointStr(pv.beizer1, jsPtsSplit);
				} else if (JSONConst.JSON_BIEZER2STR.equals(name)) {
					String[] jsPtsSplit = reader.nextString().split(" ");
					pv.beizer2=new ArrayList<PointF>();
					parsePointStr(pv.beizer2, jsPtsSplit);
				} else if (JSONConst.JSON_TIMESTR.equals(name)) {	
					
				} else if (JSONConst.JSON_PRESSURESTR.equals(name)) {	
					
				} else {
					reader.skipValue();
				}
			}
			reader.endObject();
		} catch (IOException e) {
			Log.d(VecGlobals.LOG_TAG, "GSON from pointvec", e);
		}
		return pv;
	}
	
	private void parsePointStr(ArrayList<PointF> pvec, String[] jsPtsSplit) {
		for (int i=0;i<jsPtsSplit.length;i++) {
			PointF fromPointStr = JSONUtil.fromPointStr(jsPtsSplit[i]);
			//if (fromPointStr==null) {
			//	Log.d(DVGlobals.LOG_TAG, "null point:"+jsPtsSplit[i]+":");
			//} else {
				pvec.add(fromPointStr);
			//}
		}
	}
}
