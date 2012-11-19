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
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import android.content.Context;
import android.util.Log;
import co.uk.sentinelweb.views.draw.VecGlobals;
import co.uk.sentinelweb.views.draw.controller.FontController;
import co.uk.sentinelweb.views.draw.util.FileUtil;

public class FileRepository {
	//private static final String APP_DIR_DEFAULT = "DFTest";
	public static String FR_TMP_FILE = "tmp";
	//public static String _base = null;
	//static FileRepository _fileRepository;
	/*
	public static FileRepository getFileRepository(Context c)  {
		if (_fileRepository==null ) {
			if ( _base==null) {
				if (c!=null) {
					_base = c.getPackageName();
				} else throw new RuntimeException("No base is set: FileRepository._base=xxx or pass non-null Context");
			} //
			_fileRepository = new FileRepository( MASTER_DIR, _base );  
		}
		return _fileRepository;
	}
	*/
	public static FileRepository getFileRepository( String s)  {
		FileRepository fileRepository = null;
		try {
			fileRepository = new FileRepository( VecGlobals.MASTER_DIR, s );
		} catch (Exception e) {
			e.printStackTrace();
		}
		return fileRepository;  
	}
	public static FileRepository getFileRepository()  {
		FileRepository fileRepository = null;
		try {
			fileRepository = new FileRepository( VecGlobals.MASTER_DIR, null );
		} catch (Exception e) {
			e.printStackTrace();
		}
		return fileRepository;  
	}
	public static final String SAVES_DIRECTORY = ".saves";
	public static final String RAW_DIRECTORY = "raw";
	public static final String TTF_DIRECTORY = ".ttfonts";
	public static final String OUTPUT_DIRECTORY = "output";
	public static final String TEMPLATES_DIRECTORY = ".templates";
	
	private FontController _fontController ;
	
	public enum Directory {HOME,APP,SAVES,RAW,TTF,OUTPUT,TEMPLATE,APP_TEMPLATE};
	private static File extDir = android.os.Environment.getExternalStorageDirectory();
	public static File getExternalDirectory() {
		return extDir;
	}
	private  File appDir = null;
	private  String appName = null;
	private  File appSavesDir;
	//private  File templatesDir;
	private  File appRawDir;
	private  File appOutputDir;
	private  String masterName = null;
	private  File masterTTFDir;
	private  File masterDir;
	private  File masterTemplatesDir;
	private  File appTemplatesDir;
	
	public FileRepository(String homeName,String appName) {
		super();
		this.masterName = homeName;
		this.appName = appName;
		checkDirectories();
		_fontController = new FontController(this);
	}
	/**
	 * @return the _home
	 */
	public  String getMasterDirName() {
		return masterName;
	}
	/**
	 * @return the _home
	 */
	public  String getAppDirName() {
		return appName;
	}
	/**
	 * Check the SD scard is mounted
	 * @return
	 */
	public  boolean sdMounted() {
		String sdcardState = android.os.Environment.getExternalStorageState(); 
		if (sdcardState.contentEquals(android.os.Environment.MEDIA_MOUNTED)){ 
		    return true;
        } 
        return false; 
	} 
	
	/**
	 * Checks external storage is available
	 * requires permission: 	&lt;uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"/&gt;
	 * @return
	 */
	public  boolean checkDisk() {
		if (extDir==null) {
			 extDir = android.os.Environment.getExternalStorageDirectory();
		}
		return sdMounted();
	}
	
	/**
	 * Check all required directories are created
	 * @return
	 */
	public  boolean checkDirectories() {
		if (checkDisk() ) {
			if (masterDir==null) {
				masterDir = new File(getExternalDirectory(),getMasterDirName());
				if (!masterDir.exists()) {
					masterDir.mkdirs();
				}
			} else {return false;}
			masterTTFDir = checkMasterChildExist( TTF_DIRECTORY, masterTTFDir);
			masterTemplatesDir = checkMasterChildExist( TEMPLATES_DIRECTORY, masterTemplatesDir);

			if (appName!=null ) {
				if (appDir==null) {
					appDir = new File(getExternalDirectory(),getAppDirName());
					if (!appDir.exists()) {
						appDir.mkdirs();
					}
				} else {return false;}
				appSavesDir = checkAppChildExist( SAVES_DIRECTORY, appSavesDir);
				appRawDir = checkAppChildExist( RAW_DIRECTORY, appRawDir);
				appOutputDir = checkAppChildExist( OUTPUT_DIRECTORY, appOutputDir);
				appTemplatesDir = checkAppChildExist( TEMPLATES_DIRECTORY, appTemplatesDir);
			} 

			return true;
		}
		return false;
	}
	
	private File checkMasterExist() {
		if (!sdMounted()) {return null;}
		if (masterDir==null) {
			masterDir =  new File (getExternalDirectory(),masterName);
		}
		if (!masterDir.exists()) {
			masterDir.mkdirs();
		}
		return masterDir;
	}
	private File checkMasterChildExist(String child,File theDir) {
		if (!sdMounted()) {return null;}
		if (masterDir==null|| !masterDir.exists()) {
			masterDir =  new File (getExternalDirectory(),masterName);
			if (!masterDir.exists()) {
				masterDir.mkdirs();
			}
		}
		if (theDir==null) {
			theDir = new File (masterDir,child);
			if (!theDir.exists()) {
				theDir.mkdirs();
			}
		}
		return theDir;
	}
	
	private File checkAppExist() {
		if (!sdMounted()) {return null;}
		if (appDir==null&& appName!=null) {
			appDir =  new File (getExternalDirectory(),appName);
		}
		if (!appDir.exists()) {
			appDir.mkdirs();
		}
		return appDir;
	}
	
	private File checkAppChildExist(String child,File theDir) {
		if (!sdMounted()) {return null;}
		if (appDir==null|| !appDir.exists()) {
			appDir =  new File (getExternalDirectory(),appName);
			if (!appDir.exists()) {
				appDir.mkdirs();
			}
		}
		if (theDir==null) {
			theDir = new File (appDir,child);
			if (!theDir.exists()) {
				theDir.mkdirs();
			}
		}
		return theDir;
	}
	
	public File getDirectory(Directory d) {
		switch(d) {
			case HOME:return checkMasterExist();
			case APP:return appName!=null?checkAppExist():null;
			case TTF:return checkMasterChildExist( TTF_DIRECTORY, masterTTFDir);
			case TEMPLATE:return checkMasterChildExist( TEMPLATES_DIRECTORY,masterTemplatesDir);
			case APP_TEMPLATE:return appName!=null?checkAppChildExist( TEMPLATES_DIRECTORY,appTemplatesDir):null;
			case SAVES:return appName!=null?checkAppChildExist(SAVES_DIRECTORY,appSavesDir):null;
			case RAW:return appName!=null?checkAppChildExist( RAW_DIRECTORY ,appRawDir):null;
			case OUTPUT:return appName!=null?checkAppChildExist( OUTPUT_DIRECTORY, appOutputDir):null;
		}
		return null;
	}
	
	public SaveFile getSaveFile(String name, Context c) throws Exception{
		SaveFile s = new SaveFile(name,this);//,c
		return s;
	}
	
	public ArrayList<SaveFile> getFiles( Context c){
		File savesDir = getDirectory(Directory.SAVES);
		if (savesDir!=null) {
			String[] fileNames = savesDir.list();
			ArrayList<SaveFile> files = new ArrayList<SaveFile>();
			if (fileNames!=null) {
				for (String dir : fileNames) {
					if (!FR_TMP_FILE.equals(dir) ) {
						try {
							files.add(new SaveFile(dir, this));//,c
						} catch (Exception e) {
						}
					}
				}
			}
			return files;
		}
		return null;
	}
	
	public SaveFile makeNewFile(Context c, String name) throws Exception {
		SaveFile s = new SaveFile(name,this);//, c
		s.saveSet();
		return s;
	}

	public FontController getFontController() {
		return _fontController;
	}

	public void setFontController(FontController _fontController) {
		this._fontController = _fontController;
	}
	public File makeNoMedia(File dir) {
		File noMedia = new File(dir,SaveFile.NOMEDIA_EXT);
		if (!noMedia.exists()) {
			try {
				noMedia.createNewFile();
				return noMedia;
			} catch (IOException e) {
				Log.d(VecGlobals.LOG_TAG, "makeNoMedia",e);
			}
		}
		return null;
	}
	public static void checkFontAssetInstalled(String fileName) {
		FileRepository fileRepository = getFileRepository();
		FontController _fontController2 = fileRepository._fontController;
		String substring = fileName.substring(0,fileName.lastIndexOf("."));
		_fontController2._ttfontFiles.containsKey(substring);
		//TODO move to a listener
		/*
		if (!_fontController2._ttfontFiles.containsKey(substring)) {
			try {
				InputStream is = a.getResources().getAssets().open(fileName);
				FileUtil.copy(is, new File(fileRepository.getDirectory(Directory.TTF),fileName));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		*/
	}
}
