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

import co.uk.sentinelweb.views.draw.file.SaveFile;
import co.uk.sentinelweb.views.draw.file.export.json.v2.JSONConst;
import co.uk.sentinelweb.views.draw.file.export.json.v2.JSONParent;
import co.uk.sentinelweb.views.draw.file.export.json.v2.simple.JSONFill;
import co.uk.sentinelweb.views.draw.file.export.json.v2.simple.JSONStatic;
import co.uk.sentinelweb.views.draw.model.Drawing;
import co.uk.sentinelweb.views.draw.model.DrawingElement;
import co.uk.sentinelweb.views.draw.model.Group;
import co.uk.sentinelweb.views.draw.model.Layer;
import co.uk.sentinelweb.views.draw.model.Stroke;

public class SJSONDrawing extends JSONParent{
	public static float version = 2;
	//JSONStroke jss;
	SJSONStroke jss;
	SJSONLayer ssl;
	SJSONGroup ssg;
	JSONFill jsf;
	public SJSONDrawing(SaveFile _saveFile) {
		super(_saveFile);
		 jsf = new JSONFill(_saveFile);
		 jss = new SJSONStroke(_saveFile);
		 ssl = new SJSONLayer(_saveFile);
		 ssg = new SJSONGroup(_saveFile);
	}
	
	public void toJSON(Drawing d,Writer w) throws IOException{
		SJSON.beginObj(w);
		
		SJSON.writeString(w,JSONConst.JSON_ID, d.getId());
		SJSON.sep(w);
		
		SJSON.writeFloat(w,JSONConst.JSON_VERSION, version);
		SJSON.sep(w);
		
		SJSON.writeID(w,JSONConst.JSON_SIZE);
		SJSON.writeJSON(w, JSONStatic.toJSON(d.size));
		SJSON.sep(w);
		
		SJSON.writeID(w,JSONConst.JSON_BG);
		SJSON.writeJSON(w, jsf.toJSON(d.background));
		SJSON.sep(w);
		
		SJSON.writeID(w,JSONConst.JSON_ELEMENTS);
		SJSON.beginArr(w);
		for (int i=0;i<d.elements.size();i++) {
			DrawingElement de = d.elements.get(i);
			if (de instanceof Group) {
				ssg.toJSON((Group)de,w,jss);
			} else if (de instanceof Stroke) {
				jss.toJSON( (Stroke)de, w);
				
			}
			SJSON.arraySep(w,d.elements,  i);
		}
		SJSON.endArr(w);
		SJSON.sep(w);
		
		SJSON.writeID(w,JSONConst.JSON_LAYERS);
		SJSON.beginArr(w);
		for (int i=0;i<d.layers.size();i++) {
			Layer l = d.layers.get(i);
			ssl.toJSON(l, w, ssg, jss );
			SJSON.arraySep(w,d.layers,  i);
		}
		SJSON.endArr(w);
		
		SJSON.endObj(w);
	}

}
