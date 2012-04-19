package co.uk.sentinelweb.views.draw.util;
/*
Vectoroid API for Andorid
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
import android.graphics.Color;

public class ColorUtil {
	public static String hex2(int val) {
		String a = Integer.toHexString(val);
		if (a.length()<2) {a="0"+a;}
		return a;
	}
	
	public static String toColorString(int bgColor) {
		return "#"+hex2(Color.alpha(bgColor))
				+hex2(Color.red(bgColor))
				+hex2(Color.green(bgColor))
				+hex2(Color.blue(bgColor));
	}
	
	public static String toColorStringNoAlpha(int bgColor) {
		return "#"+hex2(Color.red(bgColor))
				+hex2(Color.green(bgColor))
				+hex2(Color.blue(bgColor));
	}
	public static String toAlphaString(int bgColor) {
		return ""+(Color.alpha(bgColor)/255f);
	}
	
	
}