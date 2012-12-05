package co.uk.sentinelweb.vectoroid.explode;
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
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Toast;
import co.uk.sentinelweb.cards.SuprCardsConstants;
import co.uk.sentinelweb.cards.cover.CoverFlow;
import co.uk.sentinelweb.cards.cover.CoverFlowImageAdapter;
import co.uk.sentinelweb.views.draw.file.SaveFile;

public class CoverFlowActivity extends Activity {
	public static final String MARKET_HURL = "https://market.android.com/details?id=";
	
	AdapterView _adapterView;
	private CoverFlowImageAdapter _coverImageAdapter; 
	int _selectedItemPosition =-1;
	private boolean _isLoading = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE); 
		
		setContentView(R.layout.loader);
		    
		_adapterView =(AdapterView) findViewById(R.id.loader_coverflow);
	    // _isCover = _adapterView instanceof CoverFlow;
		findViewById(R.id.loader_sc_launch).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (SuprCardsConstants.appInstalled(CoverFlowActivity.this)) {
					launchSuprCards();
				} else {
					try {
						Intent myIntent = new Intent(Intent.ACTION_VIEW,Uri.parse(MARKET_HURL+SuprCardsConstants.PACKAGE));
						CoverFlowActivity.this.startActivity(myIntent); 
					} catch (Exception e) {
						Toast.makeText(CoverFlowActivity.this, "Cannot load market", 1000).show();  
					}
				}
			}
		});
		findViewById(R.id.loader_sc_edit).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (SuprCardsConstants.appInstalled(CoverFlowActivity.this)) {
					launchDrawing(_selectedItemPosition);
				} else {
					try {
						Intent myIntent = new Intent(Intent.ACTION_VIEW,Uri.parse(MARKET_HURL+SuprCardsConstants.PACKAGE));
						CoverFlowActivity.this.startActivity(myIntent); 
					} catch (Exception e) {
						Toast.makeText(CoverFlowActivity.this, "Cannot load market", 1000).show();
					}
				}
			}
		});
		OnClickListener onClickListener = new OnClickListener() {
			@Override
			public void onClick(View v) {
				openDrawing((String)v.getTag());
			}
		};
		findViewById(R.id.loader_img_hello).setOnClickListener(onClickListener);
		findViewById(R.id.loader_img_eye).setOnClickListener(onClickListener);
		initCover();
	}

	public void initCover() {
		_coverImageAdapter = new CoverFlowImageAdapter(this, SuprCardsConstants.getFileRepo(this));
		_adapterView.setAdapter(_coverImageAdapter);
	     
		CoverFlow cf = ((CoverFlow) _adapterView);
    	cf.setSpacing(-25);
    	cf.setAnimationDuration(1000);
    	
    	if(_coverImageAdapter.getCount()>0) {
	    	 _adapterView.setOnItemSelectedListener(_itemSelectedListener);
			 ((CoverFlow) _adapterView).setSelection(0, true);
			 _adapterView.setOnItemClickListener(_coverClickListener);
	    	 _selectedItemPosition=0;
	     }
	}
	
	private OnItemSelectedListener _itemSelectedListener = new OnItemSelectedListener() {
		@Override
		public void onNothingSelected(AdapterView<?> arg0) {

		}
		
		@Override
		public void onItemSelected(AdapterView<?> arg0, View arg1, int pos,	long arg3) {
			selectItem(pos,true);
		}
	};
	
	
	
	private OnItemClickListener _coverClickListener = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int pos,long id) {
			if (_selectedItemPosition!=pos) {
				selectItem(pos,true);
				Toast.makeText(CoverFlowActivity.this, "Press again to open", 500).show();
			} else {
				openDrawing(pos);
			}
		}
	};
	
	private void selectItem(int pos,boolean loadPreview) {
    	_selectedItemPosition = pos;
		_coverImageAdapter.setSelectedItemPosition(pos);
		_coverImageAdapter.notifyDataSetChanged();
	}
	
	private void openDrawing(int pos) {
		if (! _isLoading) {
			_isLoading=true;
			SaveFile saveFile = (SaveFile)_coverImageAdapter.getItem(_selectedItemPosition);
			Intent i = new Intent( SuprCardsExploderActivity.INTENT_ACTION_LOAD_FILE);
			i.putExtra(SuprCardsExploderActivity.INTENT_PARAM_LOAD_FILE_SET, saveFile.getSet().getId());
			i.putExtra(SuprCardsExploderActivity.INTENT_PARAM_LOAD_FILE_DRAWING, saveFile.getSet().getDrawingIDs().get(0));
			startActivity(i);
		}
	}
	private void openDrawing(String asset) {
		if (! _isLoading) {
			_isLoading=true;
			Intent i = new Intent( SuprCardsExploderActivity.INTENT_ACTION_LOAD_ASSET);
			i.putExtra(SuprCardsExploderActivity.INTENT_PARAM_LOAD_FILE_DRAWING, asset);
			startActivity(i);
		}
	}
	private void launchDrawing(int pos) {
		if (! _isLoading) {
			_isLoading=true;
			SaveFile saveFile = (SaveFile)_coverImageAdapter.getItem(_selectedItemPosition);
			Intent i = new Intent(SuprCardsConstants.INTENT_ACTION_LOAD_FILE);
			i.setAction(SuprCardsConstants.INTENT_ACTION_LOAD_FILE);
			i.putExtra(SuprCardsConstants.INTENT_PARAM_LOAD_FILE_SET, saveFile.getSet().getId());
			i.putExtra(SuprCardsConstants.INTENT_PARAM_LOAD_FILE_DRAWING, saveFile.getSet().getDrawingIDs().get(0));
			startActivity(i);
		}
	}
	
	private void launchSuprCards() {
		PackageManager manager = getPackageManager();
		Intent i = manager.getLaunchIntentForPackage(SuprCardsConstants.PACKAGE);
		startActivity(i);
	}

	@Override
	protected void onStart() {
		super.onStart();
		_isLoading=false;
		checkAppOrDisk();
		if ( _coverImageAdapter!=null)_coverImageAdapter.refresh(SuprCardsConstants.getFileRepo(this));
	}

	@Override
	protected void onStop() {
		super.onStop();
		
	}

	private void checkAppOrDisk() {
		boolean nofiles = !SuprCardsConstants.appInstalled(this) && 
				android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);
		if (nofiles) {
			_adapterView.setVisibility(View.GONE);
			findViewById(R.id.loader_msg).setVisibility(View.VISIBLE);
			findViewById(R.id.loader_sc_edit).setVisibility(View.GONE);
			findViewById(R.id.loader_sc_none_ctnr).setVisibility(View.VISIBLE);
			
		} else {
			_adapterView.setVisibility(View.VISIBLE);
			findViewById(R.id.loader_msg).setVisibility(View.GONE);
			findViewById(R.id.loader_sc_edit).setVisibility(View.VISIBLE);
			findViewById(R.id.loader_sc_none_ctnr).setVisibility(View.GONE);
			initCover();
		}
	}
	
}
