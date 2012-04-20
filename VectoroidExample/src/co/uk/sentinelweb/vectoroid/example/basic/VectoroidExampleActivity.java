package co.uk.sentinelweb.vectoroid.example.basic;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.content.Intent;
import android.graphics.PointF;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.TextView;
import co.uk.sentinelweb.cards.SuprCardsConstants;
import co.uk.sentinelweb.views.draw.file.SaveFile;
import co.uk.sentinelweb.views.draw.model.DrawingElement;
import co.uk.sentinelweb.views.draw.model.Layer;
import co.uk.sentinelweb.views.draw.render.ag.AndGraphicsRenderer;
import co.uk.sentinelweb.views.draw.util.PointUtil;
import co.uk.sentinelweb.views.draw.view.DisplayView;

public class VectoroidExampleActivity extends Activity {
	public static final String INTENT_ACTION_LOAD_FILE = "co.uk.sentinelweb.vectoroid.example.LOAD_FILE";
	public static final String INTENT_PARAM_LOAD_FILE_SET = "set";
	public static final String INTENT_PARAM_LOAD_FILE_DRAWING = "drawing";
	
	DisplayView dv;
	TextView t;
	SaveFile s;
	Handler h;
	boolean doAnimate = false;
	String currentSet = null;
	/** Called when the activity is first created. */
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); 
		setContentView(R.layout.main);
        h=new Handler();
        
        dv = (DisplayView) findViewById(R.id.main_grafik);
        dv.setOnClickListener(_toggleAnim);
        
        t =  (TextView)findViewById(R.id.main_text);
        t.setOnClickListener(_toggleAnim);
        
    }
    
    

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		Intent i = getIntent();
		
		if (i!=null && INTENT_ACTION_LOAD_FILE.equals(i.getAction())) {
	    	String set = i.getStringExtra(INTENT_PARAM_LOAD_FILE_SET);
	    	if (set!=null && !set.equals(currentSet)) {
		    	String drawingID = i.getStringExtra(INTENT_PARAM_LOAD_FILE_DRAWING);
		    	try {
		    		SaveFile sf =SuprCardsConstants.getFileRepo(this).getSaveFile( set, this );
					s = sf;
					ArrayList<String> drawingIDs = s.getSet().getDrawingIDs();
		        	if (drawingIDs.size()>0) {
		        		dv.setDrawingFile(s.getDrawingFile(drawingIDs.get(0)), s);
		        		t.setText("Click to play ...");
		        	} 
					currentSet=set;
				} catch (Exception e) {
					e.printStackTrace();
				}
	    	}
			//i.setAction(Intent.ACTION_VIEW);
	    } 
	}
	/* (non-Javadoc)
	 * @see android.app.Activity#onStop()
	 */
	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
	}
	class Motion {
		//PointF tr = new PointF(1,1);
		//PointF acc = new PointF(0,0);
		//PointF sc = new PointF(1,1);
		//PointF sacc = new PointF(1,1);
		boolean out = true;
		AndGraphicsRenderer.Operator operator =  new AndGraphicsRenderer.Operator();
		AndGraphicsRenderer.Operator operatorAcc =  new AndGraphicsRenderer.Operator();
		Motion () {
			operator.translate=new PointF();
			operator.scale=new PointF();
			operatorAcc.translate=new PointF();
			operatorAcc.scale=new PointF(1,1);
			//operator.rotation = 0f;
			operatorAcc.rotation = 0f;
		}
		public void set(float trx,float try1,float scx,float scy){//,float rot
			operator.translate.set(trx,try1);
			operator.scale.set(scx,scy);
			//operator.rotation = rot;
		}
		public void resetAcc() {
			operatorAcc.translate.set(0,0);
			if (operatorAcc.scale!=null) {operatorAcc.scale.set(1,1);}
			if (operatorAcc.rotation!=null) {	operatorAcc.rotation = 0f;	}
		}
		public void inc() {
			PointUtil.addVector(operatorAcc.translate, operatorAcc.translate,operator.translate);
			if (operatorAcc.scale!=null) PointUtil.addVector(operatorAcc.scale,operatorAcc.scale,operator.scale);
			if (operator.rotation!=null && operatorAcc.rotation!=null) {	operatorAcc.rotation += operator.rotation;	}
			//PointUtil.addVector(operatorAcc.scale,operatorAcc.scale,operator.scale);// dont do this
		}
	}
	
	int ctr = 0;
	int loop = 50;
	long startTime = 0;
	float range=20;
	float scaleRange = 0.05f;
	HashMap<DrawingElement,Motion> elMotion = new HashMap<DrawingElement,Motion>();
	
	Runnable _animateRunnable = new Runnable() {
		@Override
		public void run() {
			ctr++;
			Layer l = dv.getDrawing().getLayer(SuprCardsConstants.DRAW_LAYER_NAME);
			for (int i=0; i< l.elements.size();i++) {//i<6 &&
				DrawingElement s = l.elements.get(i);
				Motion m = elMotion.get(s);
				if (m==null) {
					m=new Motion();
					float newscale=(float)(Math.random()*scaleRange)-scaleRange/2f;
					m.set((float) (Math.random()*range-range/2), (float) (Math.random()*range-range/2),newscale, newscale);//, 360f/loop
					elMotion.put(s, m);
				}
				//StrokeUtil.translate(s, m.tr, dv.getRenderer());
				m.inc();
				dv.getRenderer().animations.put(s, m.operatorAcc);
				if (ctr%(loop/2)==0) {
					//StrokeUtil.translate(s, new PointF(-loop*m.tr.x,-loop*m.tr.y), dv.getRenderer());
					if (m.out) {
						m.set(-m.operator.translate.x, -m.operator.translate.y, -m.operator.scale.x, -m.operator.scale.y);//, 360f/loop
						m.out = false;
					} else {
						m.resetAcc();
						float newscale=(float)(Math.random()*scaleRange)-scaleRange/2f;
						m.set((float) (Math.random()*range-range/2), (float) (Math.random()*range-range/2), newscale, newscale);//, 360f/loop
						m.out = true;
					}
					t.setText((int) (ctr/((SystemClock.uptimeMillis()-startTime)/1000f)) +" fps");
				}
			}
			dv.invalidate();
			//long time = SystemClock.uptimeMillis()-start;
			int frameTime = 1000/30;
			h.postDelayed(_animateRunnable, frameTime);
			//if (ctr%(loop)!=0) {
			//	if (time<frameTime) {
			//		h.postDelayed(_animateRunnable, frameTime-time);
			//	} else {
			//		h.post(_animateRunnable);
			//	}
			//} else {
			//	h.postDelayed(_animateRunnable, 500);
			//}
		}
	};
	OnClickListener _toggleAnim = new OnClickListener() {
		@Override
		public void onClick(View v) {
			if (dv.getDrawing()!=null) {
				doAnimate=!doAnimate;
				if (doAnimate) {
					startAnim();
					t.setText("Playing ...");
				} else {
					stopAnim();
					t.setText("Click to play ...");
				}
			}
		}
	};
	private void startAnim() {
		startTime=SystemClock.uptimeMillis();
		ctr=0;
		h.postDelayed(_animateRunnable, 200);
	}
	private void stopAnim() {
		h.removeCallbacks(_animateRunnable);
	}
}