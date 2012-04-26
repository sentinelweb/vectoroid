package co.uk.sentinelweb.example.cards.explode;
/*
SuprCards Explodr for Android
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
import java.io.File;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;
import co.uk.sentinelweb.cards.SuprCardsConstants;
import co.uk.sentinelweb.ps.ParticleSystems;
import co.uk.sentinelweb.ps.Vector3D;
import co.uk.sentinelweb.ps.motion.EpicycloidMotion;
import co.uk.sentinelweb.ps.motion.GotoMotion;
import co.uk.sentinelweb.ps.motion.JabMotion;
import co.uk.sentinelweb.ps.motion.PauseMotion;
import co.uk.sentinelweb.ps.motion.PunchMotion;
import co.uk.sentinelweb.ps.motion.ReturnZeroMotion;
import co.uk.sentinelweb.ps.motion.StandardMotion;
import co.uk.sentinelweb.ps.render.DERenderer;
import co.uk.sentinelweb.views.draw.file.FileRepository;
import co.uk.sentinelweb.views.draw.file.SaveFile;
import co.uk.sentinelweb.views.draw.model.Layer;
import co.uk.sentinelweb.views.draw.util.OnAsyncListener;
import co.uk.sentinelweb.views.draw.view.DisplayView;

public class SuprCardsExploderActivity extends Activity {
	public static final String INTENT_ACTION_LOAD_FILE = "co.uk.sentinelweb.vectoroid.example.LOAD_FILE";
	public static final String INTENT_ACTION_LOAD_ASSET = "co.uk.sentinelweb.vectoroid.example.LOAD_ASSET";
	public static final String INTENT_PARAM_LOAD_FILE_SET = "set";
	public static final String INTENT_PARAM_LOAD_FILE_DRAWING = "drawing";
	
	TextView _textView;
	Handler _handler;
	boolean _doAnimate = false;
	String _currentSet = null;
	
	ParticleSystems ps;
	DisplayView _displayView;
	
	int frameCounter = 0;
	long animStartTime = 0;
	
	/** 
	 * in the onCreate we just setup the UI elements and create the ParticleSystems Manager
	 */
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); 
		setContentView(R.layout.main);
		// the handler to post animations call to.
        _handler=new Handler();
        
        _displayView = (DisplayView) findViewById(R.id.main_grafik);
        _displayView.setOnClickListener(_toggleAnim);
        // the display view onLoad listener is state based as you can see.
        _displayView.setOnLoadListener(new OnAsyncListener<Integer>() {
			@Override
			public void onAsync(Integer request) {
				if (request==DisplayView.LOADSTATE_LOADED) {
					_textView.setText("Click to play ...");
				} else if (request==DisplayView.LOADSTATE_FAILED) {
					_textView.setText("Error :(");
				} else if (request==DisplayView.LOADSTATE_LOADING) {
					_textView.setText("Loading ...");
				} else if (request==DisplayView.LOADSTATE_UPDATING) {
					_textView.setText("Updating ...");
				}
			}
		});
        _textView =  (TextView)findViewById(R.id.main_text);
        _textView.setOnClickListener(_toggleAnim);
        _textView.setVisibility(View.GONE);
        // create a new ParticleSystems manager, there as no ParticleSystem s in it yet. 
        // They are add in the onclick listener
        this.ps = new ParticleSystems(_displayView.getRenderer());
    }
    
    

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onStart() {
		super.onStart();
		//Log.d(Globals.TAG, "SuprCardsExploderActivity:"+getPackageName());
		Intent i = getIntent();
		if (i!=null && INTENT_ACTION_LOAD_FILE.equals(i.getAction())) {
	    	String set = i.getStringExtra(INTENT_PARAM_LOAD_FILE_SET);
	    	if (set!=null && !set.equals(_currentSet)) {
		    	try {
		    		// The FileRepository is the location where the Vectoroid app saves its data.
		    		FileRepository fileRepo = SuprCardsConstants.getFileRepo(this);
					// Get the SaveFile this is the directory in the .saves directory.
		    		// Each SaveFile is a set, there are multiple drawing in each set, the set.json file lists the drawings in the set.
		    		// The SaveFile has all the functionality to load and save files to the FileRepository.
		    		SaveFile saveFile = fileRepo.getSaveFile( set, this );
		    		// Get the list of drawing IDS in the Set 
					ArrayList<String> drawingIDs = saveFile.getSet().getDrawingIDs();
		        	if (drawingIDs.size()>0) {
		        		// This is the drawing JSON file.
		        		File drawingFile = saveFile.getDrawingFile(drawingIDs.get(0));
		        		// Give it to DisplayView
						_displayView.setDrawingFile(drawingFile, saveFile);
		        		_textView.setText("Click to play ...");
		        		Toast.makeText(SuprCardsExploderActivity.this, "File: "+saveFile.getName(), 500).show();
		        	} 
					_currentSet=set;
				} catch (Exception e) {
					e.printStackTrace();
				}
	    	}
		} else if (i!=null && INTENT_ACTION_LOAD_ASSET.equals(i.getAction())) {
	    	String drawingID = i.getStringExtra(INTENT_PARAM_LOAD_FILE_DRAWING);
	    	try {
	    		// sets an asset
	    		_displayView.setAsset(drawingID);
	        	_textView.setText("Click to play ...");
	        	_currentSet=null;
			} catch (Exception e) {
				e.printStackTrace();
			}
	    }
	}
	
	@Override
	protected void onStop() {
		super.onStop();
		stopAnim();
	}
	
	/**
	 * _toggleAnim : this toggles starting or cancelling the animation
	 */
	OnClickListener _toggleAnim = new OnClickListener() {
		@Override
		public void onClick(View v) {
			
			if (_displayView.getDrawing()!=null) {
				_doAnimate=!_doAnimate;
				if (_doAnimate) {
					startAnim();
					_textView.setText("Playing ...");
					
				} else {
					stopAnim();
					_textView.setText("Click to play ...");
					//_textView.setVisibility(View.VISIBLE);
				}
			}	
		}
	};
	
	/**
	 * _psRunnable : This runnable runs by repeated calls to postDelayed the handler queues itself again until the Particle System is dead.
	 */
	Runnable _psRunnable = new Runnable() {
		@Override
		public void run() {
			frameCounter++;
			long start = SystemClock.uptimeMillis();
			// this updates the particle system
			ps.render();
			_displayView.invalidate();
			int frameTime = 1000/30;
			long msecs = SystemClock.uptimeMillis()-animStartTime;
			int fps = (int) ((frameCounter/(float)msecs)*1000f);
			if (!ps.dead()) {
				_handler.postDelayed(_psRunnable, frameTime);
				_textView.setText(fps +" fps");
			} else {
				_doAnimate=false;
				_textView.setText("Finished: "+fps +" fps");
			}
			long time = SystemClock.uptimeMillis()-start;
		}
	};
	
	/**
	 * This starts the animation creating the Particle system, Motions and render.
	 * It the post the handler message to _psRunnable
	 */
	private void startAnim() {
		animStartTime=SystemClock.uptimeMillis();  
		frameCounter=0;
		// We get the first layer - the is the drawing layer in SuprCards
		Layer l = _displayView.getDrawing().getLayer(SuprCardsConstants.DRAW_LAYER_NAME);
		// We get an arrayList of Motion objects (see below) for the particles to perform
		ArrayList<co.uk.sentinelweb.ps.motion.Motion> ms=getMotionList();
		// The drawingElement renederer renders a whole Layer.
		// More renderes to be completed for (strokes, etc)
		// This renderer animates relative to the topLeft of the element in the layer
		DERenderer ren = new DERenderer(l.elements);
		if (!ps.dead()) {ps.kill();	}// kill the exisiing particle systems if needed (it shouldn't)
		// ParticleSystem holds the particles see below for creating particles dynamically.
		ParticleSystems.ParticleSystem ps2 = ps.new ParticleSystem(
				l.elements.size(), 
				new Vector3D(0,0,0),
				ms, 
				ren, 1);
		ps.addSystem( ps2	);// add it
		// Enqueue the run loop (_psRunnable)
		_handler.postDelayed(_psRunnable, 200);
	}
	
	/**
	 * Stops the animation : kills the particle system if nessecary and removes the _psRunnable msg
	 */
	private void stopAnim() {
		if (!ps.dead()) {ps.kill();	}
		_handler.removeCallbacks(_psRunnable); 
	}
	
	public ArrayList<co.uk.sentinelweb.ps.motion.Motion> getMotionList() {
		Log.d(Globals.TAG, "getMotionList:Root");
		
		ArrayList<co.uk.sentinelweb.ps.motion.Motion> ms= new ArrayList<co.uk.sentinelweb.ps.motion.Motion>();
		//ms.add(new PauseMotion(2000));// particle stops nn ms
		ms.add(new PunchMotion(new Vector3D(40,40,40)));// punch random velocities in the system
		ms.add(new StandardMotion(3000));// continue whatever motion the particle has
		ms.add(new ReturnZeroMotion(12));// return to origin logarithmiaclly (no fixed time)
		ms.add(new PunchMotion(new Vector3D(100,10)));// PUNCH -X
		ms.add(new StandardMotion(3000));
		ms.add(new ReturnZeroMotion(12));
		ms.add(new JabMotion(new Vector3D(1,10)));// PUNCH -Y but for Acceleration
		ms.add(new StandardMotion(3000));
		// an epicycloid path - this has a fixed postion so we wrap it in a goto motion so there is no jump
		EpicycloidMotion epicycloidMotion = new EpicycloidMotion(8f,20f,5000);
		GotoMotion gmot = new GotoMotion(epicycloidMotion, 5000);
		ms.add(gmot);
		ms.add(epicycloidMotion);
		ms.add(new ReturnZeroMotion(4));
		ms.add(new PauseMotion(2000));
		return ms;
	}
	/*
	 // this is just an example of how to build and add particles.
	for (int i =  0;i< l.elements.size();i++) {
		DrawingElement de = l.elements.get(i);
		Particle p = ps2.new Particle(
				origin, 
				origin, 
				origin, 
				ms.get(0), 
				ren, 
				i
			);
		ps2.addParticle( p);
	}
	*/
	
	
}