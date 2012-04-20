package co.uk.sentinelweb.vectoroid.example.basic;

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
	private boolean _isLoading =false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE); 
		
		setContentView(R.layout.loader);
		
		    
		_adapterView =(AdapterView) findViewById(R.id.loader_coverflow);
	    // _isCover = _adapterView instanceof CoverFlow;
		findViewById(R.id.loader_sc_launch).setOnClickListener(new OnClickListener() {
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

	    	 SaveFile saveFile = (SaveFile)_coverImageAdapter.getItem(0);
	    	 //_nameText.setText(saveFile.getName());
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
	
	private void selectItem(int pos,boolean loadPreview) {
    	_selectedItemPosition = pos;
		SaveFile saveFile = (SaveFile)_coverImageAdapter.getItem(pos);
		//_nameText.setText(saveFile.getName());
		//_h.removeCallbacks(_loadPreviewRunnable);
		//if (loadPreview) {
		//	_h.postDelayed(_loadPreviewRunnable,200);
		//}
		_coverImageAdapter.setSelectedItemPosition(pos);
		_coverImageAdapter.notifyDataSetChanged();
	}
	
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
	private void openDrawing(int pos) {
		if (! _isLoading) {
			_isLoading=true;
			SaveFile saveFile = (SaveFile)_coverImageAdapter.getItem(_selectedItemPosition);
			//CardGlobals._saveFile = saveFile;
			Intent i = new Intent(CoverFlowActivity.this, VectoroidExampleActivity.class);
			i.setAction(VectoroidExampleActivity.INTENT_ACTION_LOAD_FILE);
			i.putExtra(VectoroidExampleActivity.INTENT_PARAM_LOAD_FILE_SET, saveFile.getSet().getId());
			i.putExtra(VectoroidExampleActivity.INTENT_PARAM_LOAD_FILE_DRAWING, saveFile.getSet().getDrawingIDs().get(0));
			startActivity(i);
		}
	}
	private void launchDrawing(int pos) {
		if (! _isLoading) {
			_isLoading=true;
			SaveFile saveFile = (SaveFile)_coverImageAdapter.getItem(_selectedItemPosition);
			//CardGlobals._saveFile = saveFile;
			Intent i = new Intent(SuprCardsConstants.INTENT_ACTION_LOAD_FILE);
			//PackageManager manager = getPackageManager();
			//Intent i = manager.getLaunchIntentForPackage(SuprCardsConstants.PACKAGE);
			i.setAction(SuprCardsConstants.INTENT_ACTION_LOAD_FILE);
			//i.setAction(VectoroidExampleActivity.INTENT_ACTION_LOAD_FILE);
			i.putExtra(VectoroidExampleActivity.INTENT_PARAM_LOAD_FILE_SET, saveFile.getSet().getId());
			i.putExtra(VectoroidExampleActivity.INTENT_PARAM_LOAD_FILE_DRAWING, saveFile.getSet().getDrawingIDs().get(0));
			startActivity(i);
		}
	}
	private void launchSuprCards() {
		PackageManager manager = getPackageManager();
		Intent i = manager.getLaunchIntentForPackage(SuprCardsConstants.PACKAGE);
		startActivity(i);
	}
	
	Runnable _finishRunnable = new Runnable() {
		@Override
		public void run() {
			
			//finish();
		}
	};
	
	/* (non-Javadoc)
	 * @see android.app.Activity#onPause()
	 */
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onRestart()
	 */
	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onResume()
	 */
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onStart()
	 */
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		_isLoading=false;
		checkApp();
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onStop()
	 */
	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
	}

	private void checkApp() {
		if (!SuprCardsConstants.appInstalled(this)) {
			_adapterView.setVisibility(View.GONE);
			findViewById(R.id.loader_msg).setVisibility(View.VISIBLE);
			findViewById(R.id.loader_sc_edit).setVisibility(View.GONE);
		} else {
			_adapterView.setVisibility(View.VISIBLE);
			findViewById(R.id.loader_msg).setVisibility(View.GONE);
			findViewById(R.id.loader_sc_edit).setVisibility(View.VISIBLE);
			initCover();
		}
	}
	
}
