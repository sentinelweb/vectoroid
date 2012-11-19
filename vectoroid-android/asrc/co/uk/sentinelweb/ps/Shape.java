package co.uk.sentinelweb.ps;
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
import java.util.ArrayList;
/**
 * Shape drawing methods
 * mapping textures onto the shapes is of the form  float[][] ([start, end][x, y])
 * @author robm
 *
 */
public class Shape {
	//GL gl;
	//PApplet p;
	public Shape() {//GL gl
		super();
		//this.gl = gl;
	}
	/*
	public Shape(GL gl,PApplet p) {
		super();
		//this.gl = gl;
		//this.p=p;
	}
	public GL getGl() {		return gl;	}
	public void setGl(GL gl) {		this.gl = gl;	}
	public PApplet getP() {		return p;	}
	public void setP(PApplet p) {		this.p = p;	}
	*/

	public void drawSquareZero(float w, float h) {
		drawSquareZero(w,h,new float[][]{{0,0},{1,1}});
	}
	
	public void drawSquareZero(float w,float h,float[][] texCoords) {
//		gl.glBegin(GL.GL_QUADS);
//		gl.glTexCoord2f(texCoords[0][0], texCoords[1][1]); 
//		gl.glVertex2f(0,  h);
//		gl.glTexCoord2f(texCoords[0][0], texCoords[0][1]); 
//		gl.glVertex2f(0, 0);
//		gl.glTexCoord2f(texCoords[1][0], texCoords[0][1]); 
//		gl.glVertex2f( w, 0);
//		gl.glTexCoord2f(texCoords[1][0], texCoords[1][1]);
//		gl.glVertex2f( w,  h);
//		
//		gl.glEnd();  
	}
	
	public void drawSquare(float w,float h) {
		drawSquare(w,h,new float[][]{{0,0},{1,1}});
	}
	public void drawSquare(float w,float h,float[][] texCoords) {
//		gl.glBegin(GL.GL_QUADS);
//		gl.glTexCoord2f(texCoords[0][0], texCoords[1][1]); 
//		gl.glVertex2f(-w,  h);
//		gl.glTexCoord2f(texCoords[0][0], texCoords[0][1]); 
//		gl.glVertex2f(-w, -h);
//		gl.glTexCoord2f(texCoords[1][0], texCoords[0][1]); 
//		gl.glVertex2f( w, -h);
//		gl.glTexCoord2f(texCoords[1][0], texCoords[1][1]);
//		gl.glVertex2f( w,  h);
//		
//		gl.glEnd();
	}
	public void drawSegment(float length,float angle) {
		
		drawSegment( length, angle, new float[][]{{0,0},{1,1}}) ;
	}
	public void drawSegment(float length,float angle,float[][] texCoords) {
		//float oppAngle=(180-angle)/2f;
//		float oppLen = length*(float)Math.tan(angle);
//		gl.glBegin(GL.GL_TRIANGLE_STRIP);
//		gl.glTexCoord2f(texCoords[0][0], texCoords[0][1]); 
//		gl.glVertex2f(0,  0);
//		gl.glTexCoord2f(texCoords[1][0], texCoords[0][1]); 
//		gl.glVertex2f(length, 0);
//		gl.glTexCoord2f(texCoords[1][0], texCoords[1][1]);
//		gl.glVertex2f(length , oppLen );
//		gl.glEnd();
	}
	
	// assumes limit 0 .. 255
	public  void plotData(ArrayList<Integer> data,float dwidth, float dheight, float squareSize, boolean z) {
		float space = dwidth/data.size();
		float xspace = (z?0:1)*space;
		float zspace = (z?1:0)*space;
		for (int i=0;i<data.size();i++) {
			float top = dheight-data.get(i)*dheight/255-(dheight/2) ;
			//buffer.set(left, top, color( 0, 255, 255, 200 ));
			//float factor = 1f;
//			gl.glTranslatef(0, top, 0);
//			gl.glPushMatrix();
//			drawSquare(squareSize,squareSize);
//			gl.glPopMatrix();
//			//gl.glRotatef((float)mouseX/(float)width, 0f, 1f, 0f);
//			gl.glTranslatef(xspace, -1*top, zspace);
		}
	}
	
	public void drawTorus( float r, float R, int nsides, int rings) {
	    float ringDelta = 2.0f * (float) Math.PI / rings;
	    float sideDelta = 2.0f * (float) Math.PI / nsides;
	    float theta = 0.0f, cosTheta = 1.0f, sinTheta = 0.0f;
	    for (int i = rings - 1; i >= 0; i--) {
	      float theta1 = theta + ringDelta;
	      float cosTheta1 = (float) Math.cos(theta1);
	      float sinTheta1 = (float) Math.sin(theta1);
//	      gl.glBegin(GL.GL_QUAD_STRIP);
	      float phi = 0.0f;
	      for (int j = nsides; j >= 0; j--) {
	        phi += sideDelta;
	        float cosPhi = (float) Math.cos(phi);
	        float sinPhi = (float) Math.sin(phi);
	        float dist = R + r * cosPhi;
//	        gl.glNormal3f(cosTheta1 * cosPhi, -sinTheta1 * cosPhi, sinPhi);
//	        gl.glVertex3f(cosTheta1 * dist, -sinTheta1 * dist, r * sinPhi);
//	        gl.glNormal3f(cosTheta * cosPhi, -sinTheta * cosPhi, sinPhi);
//	        gl.glVertex3f(cosTheta * dist, -sinTheta * dist, r * sinPhi);
	      }
//	      gl.glEnd();
	      theta = theta1;
	      cosTheta = cosTheta1;
	      sinTheta = sinTheta1;
	    }
	  }
	
	public class RandomShape {
		int listId = 0;
		public RandomShape(int numSides,float maxLen) {
			gen( numSides, maxLen);
		}
		public void gen(int numSides,float maxLen) {
//		 	this.listId = gl.glGenLists(1);
//		  	gl.glNewList(this.listId, GL.GL_COMPILE_AND_EXECUTE);
			for (int i=0;i<numSides;i++){
				Vector3D vertex = new Vector3D((float)Math.random(),(float)Math.random(),(float)Math.random());
//				gl.glTexCoord3f(vertex.x,vertex.y,vertex.z); 
				vertex=vertex.mult(maxLen);
//				gl.glVertex3f(vertex.x,vertex.y,vertex.z);
			}
//			gl.glEndList();
		}
		public void draw(){
//			  gl.glBegin(GL.GL_QUADS);
//			  		gl.glCallList(this.listId);
//			  gl.glEnd();
		}
	}
	
	public class Cube {
		  //properties
		  public int w, h, d;
		  public int shiftX, shiftY, shiftZ;
		  	
		  int listId=0;
		  public Cube() {
			  	init(
			    		(int)Math.round(Math.random()*200-100), 
			    		(int)Math.round(Math.random()*200-100), 
			    		(int)5,//Math.round(random(-100, 100)),
			    		(int)Math.round(Math.random()*20-10), 
			    		(int)Math.round(Math.random()*20-10), 
			    		(int)Math.round(Math.random()*20-10)
			    );
		  }
		  
		  public Cube(int w, int h, int d, int shiftX, int shiftY, int shiftZ){
			  init(w, h, d, shiftX, shiftY, shiftZ);
		  }

		private void init(int w, int h, int d, int shiftX, int shiftY, int shiftZ) {
			this.w = w;
		    this.h = h;
		    this.d = d;
		    this.shiftX = shiftX;
		    this.shiftY = shiftY;
		    this.shiftZ = shiftZ;
		    initCube();
		}

		public   void draw(){
//			  gl.glPushMatrix();
//				  gl.glBegin(GL.GL_QUADS);
//				  		gl.glCallList(this.listId);
//				  gl.glEnd();
//			  gl.glPopMatrix();
		  }
		  
		public   void initCube(){
//			  this.listId = gl.glGenLists(1);
//			  gl.glNewList(this.listId, GL.GL_COMPILE_AND_EXECUTE);
//			 	  gl.glVertex3i(-w/2 + shiftX, -h/2 + shiftY, -d/2 + shiftZ); 
//				  gl.glVertex3i(w + shiftX, -h/2 + shiftY, -d/2 + shiftZ); 
//				  gl.glVertex3i(w + shiftX, h + shiftY, -d/2 + shiftZ); 
//				  gl.glVertex3i(-w/2 + shiftX, h + shiftY, -d/2 + shiftZ); 
//		
//				  //back face
//				  gl.glVertex3i(-w/2 + shiftX, -h/2 + shiftY, d + shiftZ); 
//				  gl.glVertex3i(w + shiftX, -h/2 + shiftY, d + shiftZ); 
//				  gl.glVertex3i(w + shiftX, h + shiftY, d + shiftZ); 
//				  gl.glVertex3i(-w/2 + shiftX, h + shiftY, d + shiftZ);
//		
//				  //left face
//				  gl.glVertex3i(-w/2 + shiftX, -h/2 + shiftY, -d/2 + shiftZ); 
//				  gl.glVertex3i(-w/2 + shiftX, -h/2 + shiftY, d + shiftZ); 
//				  gl.glVertex3i(-w/2 + shiftX, h + shiftY, d + shiftZ); 
//				  gl.glVertex3i(-w/2 + shiftX, h + shiftY, -d/2 + shiftZ); 
//		
//				  //right face
//				  gl.glVertex3i(w + shiftX, -h/2 + shiftY, -d/2 + shiftZ); 
//				  gl.glVertex3i(w + shiftX, -h/2 + shiftY, d + shiftZ); 
//				  gl.glVertex3i(w + shiftX, h + shiftY, d + shiftZ); 
//				  gl.glVertex3i(w + shiftX, h + shiftY, -d/2 + shiftZ); 
//		
//				  //top face
//				  gl.glVertex3i(-w/2 + shiftX, -h/2 + shiftY, -d/2 + shiftZ); 
//				  gl.glVertex3i(w + shiftX, -h/2 + shiftY, -d/2 + shiftZ); 
//				  gl.glVertex3i(w + shiftX, -h/2 + shiftY, d + shiftZ); 
//				  gl.glVertex3i(-w/2 + shiftX, -h/2 + shiftY, d + shiftZ); 
//		
//				  //bottom face
//				  gl.glVertex3i(-w/2 + shiftX, h + shiftY, -d/2 + shiftZ); 
//				  gl.glVertex3i(w + shiftX, h + shiftY, -d/2 + shiftZ); 
//				  gl.glVertex3i(w + shiftX, h + shiftY, d + shiftZ); 
//				  gl.glVertex3i(-w/2 + shiftX, h + shiftY, d + shiftZ); 
//			  gl.glEndList();
		  }
	}
}
