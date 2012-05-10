package co.uk.sentinelweb.views.draw.file.export.json.v3.string;
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

import co.uk.sentinelweb.views.draw.file.SaveFile;
import co.uk.sentinelweb.views.draw.file.export.json.v3.JSONConst;
import co.uk.sentinelweb.views.draw.file.export.json.v3.JSONParent;
import co.uk.sentinelweb.views.draw.model.DrawingElement;
import co.uk.sentinelweb.views.draw.model.Group;
import co.uk.sentinelweb.views.draw.model.Stroke;

public class SJSONGroup extends JSONParent {
	public SJSONGroup(SaveFile _saveFile) {
		super(_saveFile);
	}
	
	public void toJSON(Group g,Writer w,SJSONStroke jss) throws IOException{
		SJSON.beginObj(w);
		SJSON.writeDE(w, g,JSONConst.JSON_EL_TYPE_GROUP);
		SJSON.writeID(w,JSONConst.JSON_ELEMENTS);
		SJSON.beginArr(w);
		for (int i=0;i<g.elements.size();i++) {
			DrawingElement de = g.elements.get(i);
			if (de instanceof Group) {
				toJSON((Group)de,w,jss);
			} else if (de instanceof Stroke) {
				/*
				JSONObject jsStroke = jss.toJSON( (Stroke)de);
				jsStroke.remove(JSONConst.JSON_EL_TYPE);
				SJSON.beginObj(w);
				SJSON.writeString( w,JSONConst.JSON_EL_TYPE,JSONConst.JSON_EL_TYPE_STROKE);
				SJSON.sep(w);
				SJSON.write( w, jsStroke.toString().substring(1) );
				*/
				jss.toJSON((Stroke)de, w);
			}
			//if (i<g.elements.size()-1) {SJSON.sep(w);}
			SJSON.arraySep(w,g.elements,  i);
		}
		SJSON.endArr(w);
		SJSON.endObj(w);
	}
}
