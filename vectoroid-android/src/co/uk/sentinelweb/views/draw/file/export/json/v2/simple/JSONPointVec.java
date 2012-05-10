package co.uk.sentinelweb.views.draw.file.export.json.v2.simple;
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
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.graphics.PointF;
import android.util.Log;
import co.uk.sentinelweb.views.draw.VecGlobals;
import co.uk.sentinelweb.views.draw.file.SaveFile;
import co.uk.sentinelweb.views.draw.file.export.json.v2.JSONConst;
import co.uk.sentinelweb.views.draw.file.export.json.v2.JSONParent;
import co.uk.sentinelweb.views.draw.file.export.json.v2.JSONUtil;
import co.uk.sentinelweb.views.draw.model.PointVec;
import co.uk.sentinelweb.views.draw.model.path.Bezier;
import co.uk.sentinelweb.views.draw.model.path.PathData;

public class JSONPointVec extends JSONParent{
	public JSONPointVec(SaveFile _saveFile) {
		super(_saveFile);
	}

	StringBuilder _useSb = new StringBuilder();
	
	public JSONObject toJSON(PointVec pv) {
		JSONObject o = getJSONObject();
		try {
			o.put(JSONConst.JSON_CLOSED, pv.closed);
		//	o.put(JSONConst.JSON_ISHOLE, pv.isHole);
			o.put(JSONConst.JSON_STARTTIME, pv.startTime);

//			addPointStr(pv, o,JSONConst.JSON_POINTSSTR);
//			addPointStr(pv.beizer1, o,JSONConst.JSON_BIEZER1STR);
//			addPointStr(pv.beizer2, o,JSONConst.JSON_BIEZER2STR);
			/*
			if ( pv.time!=null) {
				JSONArray jsTmArr = new JSONArray();
				if ( pv.time!=null) 
				for (Long p : pv.time) {
					jsTmArr.put(JSONStatic.dp2f(p));
				}
				o.put(JSON_TIME, jsTmArr);
			}
			
			if ( pv.pressure!=null) {
				JSONArray jsPrArr = new JSONArray();
				for (Float p : pv.pressure) {
					jsPrArr.put(p);
				}
				o.put(JSON_PRESSURE, jsPrArr);
			}		
			*/
		} catch (JSONException e) {
			Log.d(VecGlobals.LOG_TAG, "JSON to pointvec", e);
		}
		return o;
	}
	
	private void addPointStr(ArrayList<PointF> pv, JSONObject o,String tag) throws JSONException {
		if (pv!=null) {
			_useSb.delete(0, _useSb.length());
			for (int i=0;i<pv.size();i++) {
				JSONUtil.toPointStr(pv.get(i), _useSb);
				if (i<pv.size()-1) _useSb.append(" ");
			}
			o.put(tag, _useSb.toString().trim());
		}
	}
	/*
	private void addJSONArray(ArrayList<PointF> pv, JSONObject o,String tag) throws JSONException {
		if (pv!=null) {
			JSONArray jsPtsArr = new JSONArray();
			for (PointF p : pv) {
				jsPtsArr.put(JSONStatic.toJSON(p));
			}
			o.put(tag, jsPtsArr);
		}
	}
	*/
	public PointVec fromJSON(JSONObject object) {
		PointVec pv = new PointVec();
		try {
			ArrayList<PointF> pts = new ArrayList<PointF>();
			ArrayList<PointF> bz1 = null;
			ArrayList<PointF> bz2 = null;
			//ArrayList<PointF> pr = null;
			//ArrayList<PointF> tm = null;
			
			if (object.has(JSONConst.JSON_CLOSED)) {pv.closed = object.getBoolean(JSONConst.JSON_CLOSED);}
//			if (object.has(JSONConst.JSON_ISHOLE)) {pv.isHole = object.getBoolean(JSONConst.JSON_ISHOLE);}
			if (object.has(JSONConst.JSON_STARTTIME)) {pv.startTime = object.getLong(JSONConst.JSON_STARTTIME);}
			if (object.has(JSONConst.JSON_POINTS)) {
				JSONArray jsPts = object.getJSONArray(JSONConst.JSON_POINTS);
				for (int i=0;i<jsPts.length();i++) {
					pv.add(JSONStatic.fromJSON(jsPts.getJSONObject(i)));
				}
			} else if (object.has(JSONConst.JSON_POINTSSTR)) {
				String jsPts = object.getString(JSONConst.JSON_POINTSSTR);
				String[] jsPtsSplit = jsPts.split(" ");
				for (int i=0;i<jsPtsSplit.length;i++) {
					PointF fromPointStr = JSONUtil.fromPointStr(jsPtsSplit[i]);
					if (fromPointStr==null) {
						Log.d(VecGlobals.LOG_TAG, "null point:"+jsPtsSplit[i]+":");
					} else {
						pts.add(fromPointStr);
					}
				}
			} 
			
			if (object.has(JSONConst.JSON_BIEZER1)) {
				JSONArray jsPts = object.getJSONArray(JSONConst.JSON_BIEZER1);
				bz1=new ArrayList<PointF>();
				bz1=fromArray( jsPts);//pv,
			} else if (object.has(JSONConst.JSON_BIEZER1STR)) {
				String jsPts = object.getString(JSONConst.JSON_BIEZER1STR);
				String[] jsPtsSplit = jsPts.split(" ");
				bz1=new ArrayList<PointF>();
				for (int i=0;i<jsPtsSplit.length;i++) {
					bz1.add(JSONUtil.fromPointStr(jsPtsSplit[i]));
				}
			} 
			if (object.has(JSONConst.JSON_BIEZER2)) {
				JSONArray jsPts = object.getJSONArray(JSONConst.JSON_BIEZER2);
				bz2=fromArray(jsPts);
			} else if (object.has(JSONConst.JSON_BIEZER2STR)) {
				String jsPts = object.getString(JSONConst.JSON_BIEZER2STR);
				String[] jsPtsSplit = jsPts.split(" ");
				bz2=new ArrayList<PointF>();
				for (int i=0;i<jsPtsSplit.length;i++) {
					bz2.add(JSONUtil.fromPointStr(jsPtsSplit[i]));
				}
			} 
			/*
			if (object.has(JSON_PRESSURE) && pv.pressure!=null) {
				JSONArray jsPts = object.getJSONArray(JSON_PRESSURE);
				pv.pressure.clear();
				if (jsPts!=null) {
					for (int i=0;i<jsPts.length();i++) {
						try {
							pv.pressure.add((float)jsPts.getDouble(i));
						} catch (Exception e) {
							//pv.pressure.add(0.5);
						}
					}
				}
			}
			if (object.has(JSON_TIME) && pv.time!=null) {
				JSONArray jsPts = object.getJSONArray(JSON_TIME);
				pv.time.clear();
				if (jsPts!=null) {
					for (int i=0;i<jsPts.length();i++) {
						pv.time.add(jsPts.getLong(i));
					}
				}
			}
			*/
			for (int i=0;i<pts.size();i++) {
				PointF p = pts.get(i);
				if (bz1!=null && bz1.size()>i && bz2!=null && bz2.size()>i) {  
					//Log.d(VecGlobals.LOG_TAG, "PointVec tst: "+pv.size()+":"+bz1.size()+":"+bz2.size()+":"+i);
					PointF c1 = bz1.get(i);
					PointF c2 = bz2.get(i);
					if (c1!=null && c2!=null) {
						//Log.d(VecGlobals.LOG_TAG, "PointVec bz made: ");
						Bezier b = new Bezier(p,c1,c2);
						pv.add(b);
					} else {
						PathData pd = new PathData(p);
						pv.add(pd);
					}
				} else {
					PathData pd = new PathData(p);
					pv.add(pd);
				}
			}
		} catch (JSONException e) {
			Log.d(VecGlobals.LOG_TAG, "JSON from pointvec", e);
		}
		return pv;
	}
	
	private ArrayList<PointF> fromArray( JSONArray jsPts) throws JSONException {//ArrayList<PointF> pvec,
		if (jsPts!=null) {
			ArrayList<PointF> arr=new ArrayList<PointF>();
			for (int i=0;i<jsPts.length();i++) {
				if (!jsPts.isNull(i)) {
					arr.add(JSONStatic.fromJSON(jsPts.getJSONObject(i)));
				} else {
					arr.add(null);
				}
			}
			return arr;
		}
		return null;
	}

}
