package co.uk.sentinelweb.views.draw.file.export.json.v2.string;
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
import java.util.ArrayList;

import android.graphics.PointF;
import co.uk.sentinelweb.views.draw.file.SaveFile;
import co.uk.sentinelweb.views.draw.file.export.json.v2.JSONConst;
import co.uk.sentinelweb.views.draw.file.export.json.v2.JSONParent;
import co.uk.sentinelweb.views.draw.file.export.json.v2.JSONUtil;
import co.uk.sentinelweb.views.draw.model.PointVec;

public class SJSONPointVec extends JSONParent {
	
	public SJSONPointVec(SaveFile _saveFile) {
		super(_saveFile);
	}
	
	public void toJSON(PointVec pv,Writer w) throws IOException{
			SJSON.beginObj(w);
			
			SJSON.writeBoolean(w, JSONConst.JSON_CLOSED, pv.closed);
			SJSON.sep(w);
			
			SJSON.writeBoolean(w, JSONConst.JSON_ISHOLE, pv.isHole);
			SJSON.sep(w);
			
			SJSON.writeLong(w, JSONConst.JSON_STARTTIME, pv.startTime);
			SJSON.sep(w);
			
			//boolean sep =false;
			//addPointStr(w,pv, JSONConst.JSON_POINTSSTR,false);
			//sep = addPointStr(w,pv.beizer1, JSONConst.JSON_BIEZER1STR,true);
			//addPointStr(w,pv.beizer2, JSONConst.JSON_BIEZER2STR,sep);
			
			SJSON.endObj(w);
	}
	/*
	private boolean addPointStr(Writer w,ArrayList<PointF> pv, String tag, boolean sep) throws IOException {
		if (pv!=null) {
			if (sep) {	SJSON.sep(w);	}
			SJSON.writeID(w, tag);
			SJSON.quote(w);
			for (int i=0;i<pv.size();i++) {
				JSONUtil.toPointStr(pv.get(i), w);
				if (i<pv.size()-1) w.append(" ");
			}
			SJSON.quote(w);
			return true;
		}
		return false;
	}
	*/
}
