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

import android.graphics.Path;
import android.graphics.PointF;

public class PointVec extends ArrayList<PointF> {
	public ArrayList<PointF> beizer1;
	public ArrayList<PointF> beizer2;
	public ArrayList<Float> pressure;
	public ArrayList<Long> time;
	public boolean closed;
	public boolean isHole;
	
	public Path startTip = null;
	public Path endTip = null;
	//public Path path = null;// TODO move path rendering to here
	public long startTime = -1;
	
	public PointVec duplicate(){
		PointVec newpv = new PointVec();
		newpv.closed = closed;
		newpv.isHole = isHole;
		newpv.startTime = startTime;
		
		for (int i=size()-1;i>=0;i--) {
			PointF p = get(i);
			newpv.add(0,new PointF(p.x,p.y));
		}
		if (beizer1!=null) {
			newpv.beizer1=new ArrayList<PointF>();
			for (int i=beizer1.size()-1;i>=0;i--) {
				PointF p = beizer1.get(i);
				newpv.beizer1.add(0,p==null?null:new PointF(p.x,p.y));
			}
		}
		if (beizer2!=null) {
			newpv.beizer2=new ArrayList<PointF>();
			for (int i=beizer2.size()-1;i>=0;i--) {
				PointF p = beizer2.get(i);
				newpv.beizer2.add(0,p==null?null:new PointF(p.x,p.y));
			}
		}
		if (pressure!=null) {
			newpv.pressure=new ArrayList<Float>();
			for (int i=pressure.size()-1;i>=0;i--) {
				Float pressurev = pressure.get(i);
				newpv.pressure.add(0,pressurev);
			}
		}
		if (time!=null) {
			newpv.time=new ArrayList<Long>();
			for (int i=time.size()-1;i>=0;i--) {
				Long timev = time.get(i);
				newpv.time.add(0,timev);
			}
		}
		return newpv;
	}
	
	public void reverse() {
		ArrayList<PointF> tmpArr=new ArrayList<PointF>();
		ArrayList<ArrayList<PointF>> arrsToRev = new ArrayList<ArrayList<PointF>>();
		arrsToRev.add(this);
		arrsToRev.add(beizer1);
		arrsToRev.add(beizer2);
		//arrsToRev.add(pressure);
		
		for (int j=0;j<arrsToRev.size();j++) {
			ArrayList<PointF> arrRev = arrsToRev.get(j);
			if (arrRev==null) {continue;}
			tmpArr.clear();
			tmpArr.addAll(arrRev);
			//for (PointF p : arrRev) {tmpArr.add(p);}
			arrRev.clear();
			for (int i=tmpArr.size()-1;i>=0;i--) {
				arrRev.add(tmpArr.get(i));
			}
		}
		//arrsToRev.add(time);
		if (pressure!=null) {
			ArrayList<Float> tmpArr2 = new ArrayList<Float>();
			tmpArr2.addAll(pressure);
			for (int i=tmpArr2.size()-1;i>=0;i--) {
				pressure.add(tmpArr2.get(i));
			}
		}
		//arrsToRev.add(time);
		if (time!=null) {
			ArrayList<Long> tmpArr1 = new ArrayList<Long>();
			tmpArr1.addAll(time);
			for (int i=tmpArr1.size()-1;i>=0;i--) {
				time.add(tmpArr1.get(i));
			}
		}
	}
}