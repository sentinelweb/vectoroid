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
import java.util.Collection;

import org.json.JSONObject;

import co.uk.sentinelweb.views.draw.file.export.json.v3.JSONConst;
import co.uk.sentinelweb.views.draw.model.DrawingElement;

public class SJSON {
	public static final String BEGIN_OBJ = "{";
	public static final String END_OBJ = "}";
	
	public static final String BEGIN_ARR = "[";
	public static final String END_ARR = "]";
	
	public static final String SEPARATOR = ",";
	
	public static final String QUOTE = "\"";
	
	public static final String OBJ_DELIM = ":";
	
	public static void beginObj(Writer w) throws IOException {
		w.append(BEGIN_OBJ);
	}
	public static void endObj(Writer w) throws IOException {
		w.append(END_OBJ);
	}
	public static void beginArr(Writer w) throws IOException {
		w.append(BEGIN_ARR);
	}
	public static void endArr(Writer w) throws IOException {
		w.append(END_ARR);
	}
	public static void sep(Writer w) throws IOException {
		w.append(SEPARATOR);
	}
	public static void quote(Writer w) throws IOException {
		w.append(QUOTE);
	}
	public static void writeString(Writer w,String id,String value) throws IOException {
		w.append(QUOTE).append(id).append(QUOTE).append(OBJ_DELIM).append(JSONObject.quote(value));
	}
	public static void writeInt(Writer w,String id,int val) throws IOException {
		w.append(QUOTE).append(id).append(QUOTE).append(OBJ_DELIM).append(Integer.toString(val));
	}
	public static void writeLong(Writer w,String id,long val) throws IOException {
		w.append(QUOTE).append(id).append(QUOTE).append(OBJ_DELIM).append(Long.toString(val));
	}
	public static void writeFloat(Writer w,String id,float val) throws IOException {
		w.append(QUOTE).append(id).append(QUOTE).append(OBJ_DELIM).append(Float.toString(val));
	}
	public static void writeBoolean(Writer w,String id,boolean val) throws IOException {
		w.append(QUOTE).append(id).append(QUOTE).append(OBJ_DELIM).append(Boolean.toString(val));
	}
	public static void writeID(Writer w,String id) throws IOException {
		w.append(QUOTE).append(id).append(QUOTE).append(OBJ_DELIM);
	}
	public static void writeJSON(Writer w,JSONObject o) throws IOException {
		w.append(o.toString());
	}
	public static void write(Writer w,String jsonFragment) throws IOException {
		w.append(jsonFragment);
	}
	public static void arraySep(Writer w, Collection<?> c,  int i) throws IOException {
		if (i<c.size()-1) {SJSON.sep(w);}
	}
	public static void writeDE(Writer w,DrawingElement de,String type) throws IOException {
		writeString( w,JSONConst.JSON_EL_TYPE,type);
		sep(w);
		if (de.getId()!=null && !"".equals(de.getId())) {
			writeString( w,JSONConst.JSON_ID,de.getId());
			sep(w);
		}
		if (!de.visible) {
			writeBoolean( w,JSONConst.JSON_VISIBLE,de.visible);
			sep(w);
		}
		if (de.locked) {
			writeBoolean( w,JSONConst.JSON_LOCKED,de.locked);
			sep(w);
		}
	}
}
