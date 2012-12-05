package co.uk.sentinelweb.cards;
/*
SuprCards for Android
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
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import co.uk.sentinelweb.views.draw.file.FileRepository;

public class SuprCardsConstants {

	static final String REPO_NAME = "XmasCards.sentinelweb";
	public static final String DRAW_LAYER_NAME = "Draw";
	public static final String CARD_NAME = "CardFront";
	public static final String PACKAGE = "co.uk.sentinelweb.cards";
	
	public static final String INTENT_ACTION_LOAD_FILE = "co.uk.sentinelweb.cards.LOAD_FILE";
	public static final String INTENT_PARAM_LOAD_FILE_SET = "set";
	public static final String INTENT_PARAM_LOAD_FILE_DRAWING = "drawing";
	
	private static FileRepository _fileRepo;
	
	public static FileRepository getFileRepo(Context c) {
		if (_fileRepo==null) {
			_fileRepo = FileRepository.getFileRepository( REPO_NAME);
		}
		return _fileRepo;
	}
	
	public static boolean appInstalled(Context c) {
		boolean appPresent = false;  
		try {
			PackageInfo p = c.getPackageManager().getPackageInfo(PACKAGE, 0); 
			if (p!=null) {
				appPresent=true;
			}
		} catch (NameNotFoundException e1) {
			
		}
		return appPresent;
	}

}
