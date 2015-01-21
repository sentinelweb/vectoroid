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
import java.nio.channels.FileChannel;

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
	public static void copy(InputStream in, File dst) throws IOException { 
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
	 /**
	  * from: http://stackoverflow.com/questions/6540906/android-simple-export-and-import-of-sqlite-database
     * Creates the specified <code>toFile</code> as a byte for byte copy of the
     * <code>fromFile</code>. If <code>toFile</code> already exists, then it
     * will be replaced with a copy of <code>fromFile</code>. The name and path
     * of <code>toFile</code> will be that of <code>toFile</code>.<br/>
     * <br/>
     * <i> Note: <code>fromFile</code> and <code>toFile</code> will be closed by
     * this function.</i>
     * 
     * @param fromFile
     *            - FileInputStream for the file to copy from.
     * @param toFile
     *            - FileInputStream for the file to copy to.
     */
    public static void copyFile(FileInputStream fromFile, FileOutputStream toFile) throws IOException {
        FileChannel fromChannel = null;
        FileChannel toChannel = null;
        try {
            fromChannel = fromFile.getChannel();
            toChannel = toFile.getChannel();
            fromChannel.transferTo(0, fromChannel.size(), toChannel);
        } finally {
            try {
                if (fromChannel != null) {
                    fromChannel.close();
                }
            } finally {
                if (toChannel != null) {
                    toChannel.close();
                }
            }
        }
    }
}
