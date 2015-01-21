package co.uk.sentinelweb.views.draw.model;
/*
Vectoroid API for Android
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
import java.util.ArrayList;

public class DrawingTemplate {
	Drawing _template;
	String _notes;
	ArrayList<GuideLine> _guideLines;
	
	public static class GuideLine{
		public boolean horizontal;
		public float position;

		public GuideLine(boolean horizontal, float position) {
			this.horizontal = horizontal;
			this.position = position;
		}
	}

	/**
	 * @return the _template
	 */
	public Drawing getTemplate() {
		return _template;
	}

	/**
	 * @param _template the _template to set
	 */
	public void setTemplate(Drawing _template) {
		this._template = _template;
	}

	/**
	 * @return the _notes
	 */
	public String getNotes() {
		return _notes;
	}

	/**
	 * @param _notes the _notes to set
	 */
	public void setNotes(String _notes) {
		this._notes = _notes;
	}

	/**
	 * @return the _guideLines
	 */
	public ArrayList<GuideLine> getGuideLines() {
		return _guideLines;
	}

	/**
	 * @param _guideLines the _guideLines to set
	 */
	public void setGuideLines(ArrayList<GuideLine> _guideLines) {
		this._guideLines = _guideLines;
	}
	
	
}
