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

public class DrawingSet {
	ArrayList<String> _drawingIDs = new ArrayList<String>();
	String _id;
	String _name;
	String _templateID;
	DrawingSetTemplate _template;
	String _notes;
	long _dateCreated;
	long _dateModified;
	DrawingTemplate _defaultTemplate;
	
	/**
	 * @return the _drawingIDs
	 */
	public ArrayList<String> getDrawingIDs() {
		return _drawingIDs;
	}
	
	public boolean hasDrawings() {
		if (_drawingIDs == null) {return false;}
		else return _drawingIDs.size()>0;
	}
	/**
	 * @param _drawingIDs the _drawingIDs to set
	 */
	public void setDrawingIDs(ArrayList<String> _drawingIDs) {
		this._drawingIDs = _drawingIDs;
	}
	/**
	 * @return the _id
	 */
	public String getId() {
		return _id;
	}
	/**
	 * @param _id the _id to set
	 */
	public void setId(String _id) {
		this._id = _id;
	}
	/**
	 * @return the _name
	 */
	public String getName() {
		return _name;
	}
	/**
	 * @param _name the _name to set
	 */
	public void setName(String _name) {
		this._name = _name;
	}
	/**
	 * @return the _templateID
	 */
	public String getTemplateID() {
		return _templateID;
	}
	/**
	 * @param _templateID the _templateID to set
	 */
	public void setTemplateID(String _templateID) {
		this._templateID = _templateID;
	}
	/**
	 * @return the _template
	 */
	public DrawingSetTemplate getTemplate() {
		return _template;
	}
	/**
	 * @param _template the _template to set
	 */
	public void setTemplate(DrawingSetTemplate _template) {
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
	 * @return the _dateCreated
	 */
	public long getDateCreated() {
		return _dateCreated;
	}
	/**
	 * @param _dateCreated the _dateCreated to set
	 */
	public void setDateCreated(long _dateCreated) {
		this._dateCreated = _dateCreated;
	}
	/**
	 * @return the _dateModified
	 */
	public long getDateModified() {
		return _dateModified;
	}
	/**
	 * @param _dateModified the _dateModified to set
	 */
	public void setDateModified(long _dateModified) {
		this._dateModified = _dateModified;
	}
	/**
	 * @return the _defaultTemplate
	 */
	public DrawingTemplate getDefaultTemplate() {
		return _defaultTemplate;
	}
	/**
	 * @param _defaultTemplate the _defaultTemplate to set
	 */
	public void setDefaultTemplate(DrawingTemplate _defaultTemplate) {
		this._defaultTemplate = _defaultTemplate;
	}
	
	
	
}