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
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class FileUtil {
	public static void copy(File src, File dst) throws IOException { 
		InputStream in = new FileInputStream(src); 
		OutputStream out = new FileOutputStream(dst);
		// Transfer bytes from in to out 
		byte[] buf = new byte[1024]; 
		int len; 
		while ((len = in.read(buf)) > 0) { 
			out.write(buf, 0, len); 
		} 
		out.flush();
		in.close(); 
		out.close(); 
	} 
	
	public static void deleteWithChildren(File imgDir) {
		deleteRecursive(imgDir);
	}

	public static CharSequence getRelativePath(File tgt, File local) {
		if (!local.isDirectory()) {
			local=local.getParentFile();
		}
		String[] tgtSplit=tgt.getAbsolutePath().split("/");
		String[] localSplit=local.getAbsolutePath().split("/");
		boolean go=true;
		StringBuffer basePath = new StringBuffer();
		int ctr = 0;
		while (go) {
			basePath.append("/");
			if (tgtSplit.length>ctr && localSplit.length>ctr && tgtSplit[ctr].equals(localSplit[ctr])) {
				basePath.append(tgtSplit[ctr]);
				ctr++;
			}else {
				go=false;
			}
		}
		File bpFile = new File(basePath.toString());
		StringBuffer relPath = new StringBuffer(tgt.getAbsolutePath());
		
		//String lpStr = local.getAbsolutePath();
		relPath.delete(0, basePath.toString().length()-1);
		while (!local.equals(bpFile)) {
			relPath.insert(0,"../");
			local=local.getParentFile();
		}
		return relPath.toString();
	}
	
	public static boolean deleteIfExists(File f) {
		if (f!=null && f.exists()) {
			return f.delete();
		} else {
			return false;
		}
	}
	public static void deleteRecursive(File root) {
    	if (root.exists() && root.isDirectory()) {
    		File[] list = root.listFiles();
    		
    		if (list!=null) {
	    		for (File f : list) {
	    			if (f.isFile()) {
	    				f.delete();
	    			} else if (f.isDirectory()) {
	    				deleteRecursive(f);
	    			}
	    		}
    		}
    	}
    	root.delete();
	}
}
