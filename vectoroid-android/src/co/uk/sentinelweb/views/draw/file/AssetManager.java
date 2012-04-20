package co.uk.sentinelweb.views.draw.file;
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
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.util.Log;
import co.uk.sentinelweb.views.draw.VecGlobals;
import co.uk.sentinelweb.views.draw.file.SaveFile.Option;
import co.uk.sentinelweb.views.draw.file.export.json.JSONConst;
import co.uk.sentinelweb.views.draw.model.Asset;
import co.uk.sentinelweb.views.draw.util.Base64;
import co.uk.sentinelweb.views.draw.util.FileUtil;
import co.uk.sentinelweb.views.draw.util.OnAsyncListener;

public class AssetManager {
	private static final String ASSET_EXT = ".png";
	public static final String ASSETS_DIRECTORY = ".assets";
	public static final String TIMESTAMP = "yyyy-MM-dd-hh-mm-ss";
	SimpleDateFormat _timeStampFormat = new SimpleDateFormat(TIMESTAMP);
	File _assetsDir;
	
	private HashMap<String, Asset> _assetsCache = new  HashMap<String, Asset>();
	
	public AssetManager(SaveFile _saveFile) {
		super();
		this._assetsDir = new File(_saveFile.getDataDir(),ASSETS_DIRECTORY);
		if (!_assetsDir.exists()) {_assetsDir.mkdirs();}
	}

	public Asset makeAsset(Bitmap  b) {
		Asset a  = new Asset(_timeStampFormat.format(new Date()),b);
		_assetsCache.put(a.getName(),a);
		saveAssetBitmap(a);
		a._reloadListener=_reloadListener;
		return a;
	}
	
	public void saveAssetBitmap(Asset a) {
		try {
			File assetFile = getAssetFile(a);
			FileOutputStream out = new FileOutputStream(assetFile);
			Bitmap b = a.getBitmap();
			Log.d(VecGlobals.LOG_TAG, "saveAssetBitmap: "+b+":"+assetFile);
			if (b!=null) {
				b.compress(Bitmap.CompressFormat.PNG, 90,out);
				out.flush();
				out.close();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void loadAsset(Asset a) {
		File tmpImgFile = getAssetFile(a);
    	
    	int sampleSize = 1;
    	boolean loaded = false;
    	while (!loaded) {
			try {
				if (tmpImgFile.exists()) {
					Options o = new Options();
					o.inSampleSize=sampleSize;
					a.clearBitmap();
					a.setBitmap(BitmapFactory.decodeFile(tmpImgFile.getAbsolutePath(),o));
					loaded = true;
				} else {
					break;
				}
			} catch (OutOfMemoryError e) {
				sampleSize+=1;
				if (sampleSize>3) {	break;}
				System.gc();
				Log.d(VecGlobals.LOG_TAG, "loadTmpImage(): OutOfMemoryError recieved: trying new subsampling:"+sampleSize);
			}
    	}
	}

	public File getAssetFile(Asset a) {
		return new File(_assetsDir,a.getName() + ASSET_EXT);
	}
	private File getAssetFile(String s) {
		return new File(_assetsDir,s + ASSET_EXT);
	}
	
	public Asset loadAsset(String name) {
		Asset a = _assetsCache.get(name);
		if (_assetsCache.get(name)==null) {
			a = new Asset(name);
			_assetsCache.put(name, a);
		} 
		if (a.getBitmap()==null) {
			loadAsset( a);
		}
		a._reloadListener=_reloadListener;
		return a;
	}
	
	
	public JSONObject toJSON(Asset a) {
		 return toJSON( a,null);
	}
	
	public JSONObject toJSON(Asset a,ArrayList<Option> options) {
		JSONObject o = new JSONObject();
		try {
			o.put(JSONConst.JSON_NAME, a.getName());
			if (options!=null && options.contains(Option.EMBED_ASSET)) {
				StringWriter sw = new StringWriter();
				BufferedWriter bw=new BufferedWriter(sw);
				File assetFile = getAssetFile(a);
				encodeDataURL(assetFile, bw, "image/png");
				o.put(JSONConst.JSON_DATA, sw.toString());
			}
		} catch(JSONException e) {
			Log.d(VecGlobals.LOG_TAG, "JSON to asset", e);
		}
		return o;
	}

	public Asset fromJSON(JSONObject jsonObject) {
		String name;
		try {
			name = jsonObject.getString(JSONConst.JSON_NAME);
			return loadAsset( name );
		} catch (JSONException e) {
			Log.d(VecGlobals.LOG_TAG, "JSON from asset", e);
		}
		return null;
	}
	
	public void copyFrom(AssetManager oldAssetManager) {
		for (String s : oldAssetManager._assetsCache.keySet()) {
			//Asset a = oldAssetManager._assetsCache.get(s);
			//saveAssetBitmap( a) ;
			try {
				FileUtil.copy(oldAssetManager.getAssetFile(s), getAssetFile(s));
				_assetsCache.put(s,oldAssetManager._assetsCache.get(s));
			} catch (IOException e) {
			}
		}
	}
	
	public void cleanAssets() {
		File[] listFiles = _assetsDir.listFiles();
		if (listFiles!=null) {
			for (File f : listFiles) {
				f.delete();
			}
		}
	}
	
	public void encodeDataURL(File file, BufferedWriter outBuffer,String mimetype) {
		//TODO needs to been written so buffers are re-used - will cause OMem errors
		byte[] bBuf = new byte[1000];
		//ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
			//outBuffer.append("data:").append(mimetype).append(";filename=").append(file.getName()).append(";charset=utf-8").append(";base64,");
			outBuffer.append("data:").append(mimetype).append(";base64,");
			FileInputStream inStream = new FileInputStream(file);
			int read = -1;
			// the commented code below should work - output seems the same so i can only assume it a charset issue. further debugging finds erorrs a chars 1540,2878,4218, there are some A's so looks like some zeros are making their way in
			/*
			int remainder = 0;
			while ((read=inStream.read(bBuf,remainder,bBuf.length-remainder))>-1) {
				//baos.write(bBuf,0,read);
				int length = (read+remainder);
				//Log.d(Globals.LOG_TAG, "read:"+read+" remainder:"+remainder+" length: "+length+": new rem:"+(length-length%3)+" l-nr: "+(length-(length-length%3)));
				remainder = length%3;
				
				byte[] b64Buf = Base64.encodeBytesToBytes(bBuf,0,length-remainder,Base64.NO_OPTIONS);
				outBuffer.write(new String(b64Buf, 0, b64Buf.length, "utf-8"));
			}
			if (remainder>0) {
				byte[] b64Buf = Base64.encodeBytesToBytes(bBuf,read-remainder,read,Base64.NO_OPTIONS);
				outBuffer.write(new String(b64Buf,0,b64Buf.length, "utf-8"));
			}
			*/
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			while ((read=inStream.read(bBuf,0,bBuf.length))>-1) {baos.write(bBuf,0,read);}
			byte[] b64Buf = Base64.encodeBytesToBytes(baos.toByteArray(),0,baos.size(),Base64.NO_OPTIONS);
			outBuffer.write(new String(b64Buf,0,b64Buf.length, "utf-8"));
			baos.close();
			inStream.close();
		} catch (FileNotFoundException e) {
			Log.d(VecGlobals.LOG_TAG, "b64 encode asset", e);
		} catch (IOException e) {
			Log.d(VecGlobals.LOG_TAG, "b64 encode asset", e);
		}
	}
	
	public OnAsyncListener<Asset> _reloadListener =new OnAsyncListener<Asset>() {
		@Override
		public void onAsync(Asset request) {
			loadAsset(request);
		}
	};
}
