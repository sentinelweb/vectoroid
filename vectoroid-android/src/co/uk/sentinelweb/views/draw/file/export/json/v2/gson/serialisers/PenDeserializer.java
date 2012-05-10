package co.uk.sentinelweb.views.draw.file.export.json.v2.gson.serialisers;
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
import java.lang.reflect.Type;
import java.util.Map.Entry;
import java.util.Set;

import co.uk.sentinelweb.views.draw.file.SaveFile;
import co.uk.sentinelweb.views.draw.file.export.json.v2.JSONConst;
import co.uk.sentinelweb.views.draw.model.Pen;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
/*     *************************** NOT USED ******************************* */
public class PenDeserializer implements JsonDeserializer<Pen> {
	SaveFile _saveFile = null;
	
	public PenDeserializer(SaveFile _saveFile) {
		super();
		this._saveFile = _saveFile;
	}

	@Override
	public Pen deserialize(JsonElement el, Type type, JsonDeserializationContext cxt) throws JsonParseException {
		//return BreakType.valueOf(el.getAsString());
		JsonObject penJS = el.getAsJsonObject();
		Pen p = new Pen();
		Set<Entry<String, JsonElement>> penEntrySet = penJS.entrySet();
		for (Entry<String, JsonElement> e:penEntrySet) {
			if (JSONConst.JSON_STR_COL.equals(e.getKey())) {
				p.strokeColour = e.getValue().getAsInt();
			} else if (JSONConst.JSON_STR_WID.equals(e.getKey())) {
				p.strokeWidth = e.getValue().getAsFloat();
			}
		}
		return p;
	}

}
