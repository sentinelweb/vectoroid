package co.uk.sentinelweb.views.draw.util;

import java.util.ArrayList;
import java.util.HashMap;

import co.uk.sentinelweb.views.draw.VecGlobals;

import android.util.Log;

public class Profiler {
	long time= System.nanoTime();
	//HashMap<String,Long> _marks = new HashMap<String,Long>();
	//ArrayList<String> _marksOrder = new ArrayList<String>();
	ArrayList<String> _marks = new ArrayList<String>();
	ArrayList<Long> _marksTimes = new ArrayList<Long>();
	StringBuilder sb=new StringBuilder();
	
	HashMap<String,Record> _accumTimes = new HashMap<String,Record>();
	class Record {
		long acctime=0;
		int count =0;
		public float getAvg () {
			return acctime/(float)count;
		}
	}
	public Profiler() {
		start();
	}

	public Profiler start() {
		_marks.clear();
		_marksTimes.clear();
		time =System.nanoTime();
		return this;
	}
	
	public void mark(String mark) {
		//if (!_marks.containsKey(mark)) {
			_marks.add(mark);
		//} else {
		//	_marks.put(mark,_marks.get(mark)-SystemClock.uptimeMillis());
		//}
		//if (!_marksOrder.contains(mark)) {
			_marksTimes.add(System.nanoTime());
		//}
	}
	public void logAccum(String tag, int marksBackStart,int marksBackEnd) {
		Long start = getPos(marksBackStart);
		Long end = getPos(marksBackEnd);
		if (start!=null && end !=null) {
			if (_accumTimes.containsKey(tag)) {
				Record r = _accumTimes.get(tag);
				r.acctime += (end-start);
				r.count++;
			} else {
				Record r = new Record();
				r.acctime = (end-start);
				r.count++;
				_accumTimes.put(tag, r);
			}
		} else {
			Log.d(VecGlobals.LOG_TAG, "Profiler.logAccum: tag:"+tag+" st:"+marksBackStart+"="+start+" end:"+ marksBackEnd+"="+end+" len:"+_accumTimes.size());
		}
	}

	// if 0 then currentTime
	// if 1 then last mark
	// if _marksTimes.size() then first mark
	// if == _marksTimes.size() +1 then start
	// if > _marksTimes.size() +1 then null
	private Long getPos(int marksBack) {
		Long tm = null;
		if (marksBack==0) {
			tm = System.nanoTime();
		} else if (_marksTimes.size()>=marksBack && marksBack>0) {
			tm=_marksTimes.get(_marksTimes.size()-marksBack);
		} else if (_marksTimes.size()+1==marksBack){
			tm=time;
		}
		return tm;
	}
	
	public void dump(String tag) {
		sb.delete(0,sb.length());
		sb.append(tag);
		sb.append(">");
		long endTime = System.nanoTime();
		long lasttm=time;
		for (int i=0;i<_marks.size();i++) {
			sb.append(_marks.get(i));
			sb.append(":");
			Long tm = _marksTimes.get(i);
			//if (tm>time) {
				sb.append((tm-lasttm)/1000);
				sb.append("us");
				
			//} else {
			//	sb.append(tm);
			//	sb.append("^");
			//}
			
			sb.append(" ");
			lasttm = tm;
		}
		sb.append("total:");
		sb.append((endTime-time)/1000);
		sb.append("us");
		Log.d(VecGlobals.LOG_TAG,sb.toString());
	}
	
	public void clearAccum() {
		Log.d(VecGlobals.LOG_TAG,"---- clear accum -----------");
		_accumTimes.clear();
	}
	
	public void dumpAccum(String tag) {
		sb.delete(0,sb.length());
		sb.append(tag);
		sb.append(" acc> ");
		long endTime = System.nanoTime();
		for (String key : _accumTimes.keySet()) {
			sb.append("\n");
			sb.append(key);
			sb.append(" : ");
			sb.append(_accumTimes.get(key).getAvg()/1000);
			sb.append(" av us");
			sb.append(" : ");
			sb.append(_accumTimes.get(key).acctime/1000);
			sb.append(" tot us");
			sb.append(" : ");
			sb.append(_accumTimes.get(key).count);
			sb.append(" cnt");
			//} else {
			//	sb.append(tm);
			//	sb.append("^");
			//}
		}
//		sb.append("total:");
//		sb.append((endTime-time)/1000);
//		sb.append("us");
		Log.d(VecGlobals.LOG_TAG,sb.toString());
	}
}
