package co.uk.sentinelweb.views.draw.file.export.json.v3.simple;
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
import org.json.JSONObject;

import co.uk.sentinelweb.views.draw.file.SaveFile;
import co.uk.sentinelweb.views.draw.file.export.json.v3.JSONParent;
import co.uk.sentinelweb.views.draw.model.DrawingSetTemplate;
import co.uk.sentinelweb.views.draw.model.DrawingTemplate;

public class JSONTemplate extends JSONParent {
	public static float version = 1;
	// Set template
	private static final String JSON_ITEMS = "items";
	private static final String JSON_ITEMS_ORDER = "itemsOrder";
	private static final String JSON_DESCRIPTION = "description";
	private static final String JSON_HELPLINK = "helpLink";
	private static final String JSON_DOMAIN = "domain";
	private static final String JSON_COMPANY = "company";
	private static final String JSON_BG = "bg";
	private static final String JSON_DEFAULTSIZE = "defaultSize";
	private static final String JSON_DEFAULTFILL = "defaultFill";
	private static final String JSON_DEFAULTTEMPLATE = "defaultTmpl";
	private static final String JSON_VERSION = "version";
	private static final String JSON_NOTES = "notes";
	
	// drawing template
	private static final String JSON_TEMPLATE = "template";
	private static final String JSON_GUIDELINES = "guidelines";
	
	public JSONTemplate(SaveFile _saveFile) {
		super(_saveFile);
	}
	
	public JSONObject toJSONSetTmpl(DrawingSetTemplate tmpl) {
		JSONObject o = new JSONObject();
		
		return o;
	}
	
	public DrawingSetTemplate fromJSONSetTmpl(JSONObject o) {
		DrawingSetTemplate settmpl = new DrawingSetTemplate();
		
		return settmpl;
	}
	
	public JSONObject toJSONTmpl(DrawingTemplate tmpl) {
		JSONObject o = new JSONObject();
		
		return o;
	}
	
	public DrawingTemplate fromJSONTmpl(JSONObject o) {
		DrawingTemplate tmpl = new DrawingTemplate();
		
		return tmpl;
	}
}
