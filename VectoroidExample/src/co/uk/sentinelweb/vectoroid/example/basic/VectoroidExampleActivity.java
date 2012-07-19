package co.uk.sentinelweb.vectoroid.example.basic;
 /*
Vectoroid Example for Android
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

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ProgressBar;
import android.widget.TextView;
import co.uk.sentinelweb.views.draw.util.OnAsyncListener;
import co.uk.sentinelweb.views.draw.view.DisplayView;

public class VectoroidExampleActivity extends Activity {
	DisplayView dv;
	TextView t;
	ProgressBar progress;
	String currentAssetName = "hello.json";
	/** Called when the activity is first created. */
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); 
		setContentView(R.layout.main);
		progress = (ProgressBar) findViewById(R.id.main_progress);
		
        dv = (DisplayView) findViewById(R.id.main_grafik);
        dv.setOnLoadListener(_drawingLoadListener);
        dv.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (currentAssetName.equals("hello.json")) {
					currentAssetName="Vectoroid_robot.svg";
				} else {
					currentAssetName="hello.json";
				}
				loadAsset(currentAssetName);
			}
		});
        t =  (TextView)findViewById(R.id.main_text);
        loadAsset(currentAssetName);
    }

	OnAsyncListener<Integer> _drawingLoadListener = new OnAsyncListener<Integer>(){
		@Override
		public void onAsync(Integer request) {
			if (request==DisplayView.LOADSTATE_LOADED ||request==DisplayView.LOADSTATE_FAILED) {
				progress.setVisibility(View.GONE);
			} else {
				progress.setVisibility(View.VISIBLE);
			}
		}
	};
	
	private void loadAsset(String name) {
		try {
			//if (name.endsWith("json")) {
				dv.setAsset(name);
//			} else {
//				InputStream is = getResources().getAssets().open(name);
//				InputSource isc = new InputSource(is);
//				SVGParser svgp = new SVGParser();
//				Drawing d = svgp.parseSAX(isc);
//				d.update(true, dv.getRenderer(), UpdateFlags.ALL);// shouldnt be nessecary - test
//				dv.setDrawing(d,false);
//			}
			t.setText(name);
		} catch (Exception e) {
			e.printStackTrace();
			t.setText("Error ..."+e.getMessage());
		}
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
		
	}
	/* (non-Javadoc)
	 * @see android.app.Activity#onStop()
	 */
	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
	}
	
}