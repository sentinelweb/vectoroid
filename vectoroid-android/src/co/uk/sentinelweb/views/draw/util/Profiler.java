package co.uk.sentinelweb.views.draw.util;

import java.util.ArrayList;

import co.uk.sentinelweb.views.draw.VecGlobals;

import android.util.Log;

public class Profiler {
	long time= System.nanoTime();
	//HashMap<String,Long> _marks = new HashMap<String,Long>();
	//ArrayList<String> _marksOrder = new ArrayList<String>();
	ArrayList<String> _marks = new ArrayList<String>();
	ArrayList<Long> _marksTimes = new ArrayList<Long>();
	StringBuilder sb=new StringBuilder();
	
	public Profiler() {
		start();
	}

	public void start() {
		_marks.clear();
		_marksTimes.clear();
		time =System.nanoTime();
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
			//} else {
			//	sb.append(tm);
			//	sb.append("^");
			//}
			
			sb.append(" ");
			lasttm = tm;
		}
		sb.append("total:");
		sb.append((endTime-time)/1000);
		Log.d(VecGlobals.LOG_TAG,sb.toString());
	}
}
