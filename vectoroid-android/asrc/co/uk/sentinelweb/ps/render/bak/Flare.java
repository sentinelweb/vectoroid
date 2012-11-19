package co.uk.sentinelweb.ps.render.bak;
/*
Vectoroid for Android
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
import java.util.Vector;

import co.uk.sentinelweb.ps.Shape;
import co.uk.sentinelweb.ps.Vector3D;


import android.graphics.Color;

public class Flare {

//	GL gl;
//	GLU glu;
//	OpenGL ogl ;
//	PApplet p;
	Shape s;
	
	static boolean initialised =false;
	int newFlareCounter=0;
	public boolean dead=false;
	static String[] files1 = {
			"blob.png",
			"circle.png",
			"crown_inv.png",
			"crown.png",
			"crown2.png",
			"square.png",
			"sun.png",
			"star.png",
			"sunhole.png"};
	static String[] files = {
			"blob.png",
			"circle.png",
			"star.png"};
	Vector<FlareElement> flares;
	int length = 300;
	
	public Flare(int length) {//OpenGL ogl, PApplet p,
//		this.ogl=ogl;
//		this.gl = ogl.gl;
//		this.glu = new GLU();
//		this.p=p;
		this.s = new Shape();//this.gl
		
		this.flares = new Vector<FlareElement>();
		this.length=length;
		
//		gl.glEnable(GL.GL_TEXTURE_2D);                              // Enable Texture Mapping
//		gl.glEnable(GL.GL_BLEND);
//		gl.glShadeModel(GL.GL_SMOOTH);                              // Enable Smooth Shading
//        gl.glClearColor(0.0f, 0.0f, 0.0f, 0.5f);                    // Black Background
//        gl.glClearDepth(1.0f);                                      // Depth Buffer Setup
//        //gl.glHint(GL.GL_PERSPECTIVE_CORRECTION_HINT, GL.GL_NICEST);  // Really Nice Perspective Calculations
//        gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA);       
//        //gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE);                  // Set The Blending Function For Translucency
//        gl.glEnable(GL.GL_ALPHA_TEST);
//        gl.glAlphaFunc(GL.GL_GREATER, 0);
//        gl.glDepthMask(false);
		if (!this.initialised) {
			for (int i=0;i<files.length;i++) {
//				ogl.loadJTexture(files[i], "/resources/flare/"+files[i], true);
			}
			this.initialised=true;
		}
	}
	
	void addElement() {
		addElement(-1);
	}
	void addElement(int type) {
		int index = (int)(Math.random()*2);//p.random(files.length)
		//Color color = new Color((float)Math.random(),(float)Math.random(),(float)Math.random());
		int color = Color.argb(255,(int)(Math.random()*255),(int)(Math.random()*255),(int)(Math.random()*255));
			//new Color((float)Math.random(),(float)Math.random(),(float)Math.random());
		//System.out.println(index+":"+color);
		Vector3D size= new Vector3D(10,10,0);
		Vector3D growth=new Vector3D(0.5f,0.5f,0);
		if (type==-1) {type = (int)Math.floor(Math.random()*2);}//p.random(2)
		switch(type) {
			case 0:break;
			case 1:
				 size= new Vector3D(100,2,0);
				 growth=new Vector3D(0,0,0);
				 break;
		}
		flares.add(
				new FlareElement(
						files[index], //ogl.getJText( files[index])
						new Vector3D(0,0,(float)(-1+(Math.random()*2))),//p.random(2)
						color,
						this,
						size,
						growth
				)
		);
	}
	void update() {
		if (newFlareCounter%20==0) {
			addElement();
		}
		newFlareCounter++;
		Vector<FlareElement> rem = new Vector<FlareElement>();
		for (FlareElement f: flares) {
			f.update();
			if (f.frames>length) {rem.add(f);}
		}
		flares.removeAll(rem);
		if (flares.size()==0) {
			dead=true;
		}
	}
	void render() {

		for (FlareElement f: flares) {
			f.render();
			
		}
	}
	
	class FlareElement {
		//Texture t;
		String t;
		Vector3D rotationvel;
		int c;
		Vector3D rotation=new Vector3D();
		Vector3D size=new Vector3D();
		Vector3D growth=new Vector3D(0.5f,0.5f,0);
		Flare f;
		int frames = 0;
		float alpha = 0.0f;
		//Texture t
		public FlareElement(String t, Vector3D rotation, int c,Flare f,Vector3D initialSize,Vector3D growth) {
			super();
			this.t = t;
			this.rotationvel = rotation;
			this.c = c;
			this.f=f;
			this.size=initialSize;
			if (growth!=null) {this.growth=growth;}
		}
		void update() {
			rotation.add(rotationvel);
			size.add(growth);
			frames++;
		}
		void render() {
			//gl.glPushMatrix();
				if (frames < 40 && alpha<0.97) {
					alpha += 0.03;
				}
				if (frames > f.length-30 ) {
					alpha -= 0.03;
				}
				//gl.glColor4f(c.getRed()/255f, c.getGreen()/255f, c.getBlue()/255f, alpha);
				//t.bind();
				//t.enable();
				
				//gl.glRotatef(rotation.x, 1, 0, 0);
				//gl.glRotatef(rotation.y, 0, 1, 0);
				//gl.glRotatef(rotation.z, 0, 0, 1);
				s.drawSquare((float)size.x, (float)size.y);
			//gl.glPopMatrix();
		}
	}
}
