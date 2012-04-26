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
import java.io.Serializable;
public class Vector3D implements Serializable{
	public double x;
	  public double y;
	  public double z;

	  public Vector3D(double x_, double y_, double z_) {
	    x = x_; y = y_; z = z_;
	  }

	  public Vector3D(double x_, double y_) {
	    x = x_; y = y_; z = 0f;
	  }
	  
	  public Vector3D() {
	    x = 0f; y = 0f; z = 0f;
	  }

	  public void setX(double x_) {
	    x = x_;
	  }

	  public void setY(double y_) {
	    y = y_;
	  }

	  public  void setZ(double z_) {
	    z = z_;
	  }
	  
	  
	  public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	public double getZ() {
		return z;
	}

	/*
	  public void setXY(float x_, float y_) {
	    x = x_;
	    y = y_;
	  }
	  */
	  public void setXYZ(double x_, double y_, double z_) {
	    x = x_;
	    y = y_;
	    z = z_;
	  }

	  public void setXYZ(Vector3D v) {
	    x = v.x;
	    y = v.y;
	    z = v.z;
	  }
	  
	  public double magnitude() {
	    return  Math.sqrt(x*x + y*y + z*z);
	  }

	  public Vector3D copy() {
	    return new Vector3D(x,y,z);
	  }

	  public Vector3D copy(Vector3D v) {
	    return new Vector3D(v.x, v.y,v.z);
	  }
	  
	  public Vector3D add(Vector3D v) {
	    x += v.x;
	    y += v.y;
	    z += v.z;
	    return this;
	  }

	  public Vector3D sub(Vector3D v) {
	    x -= v.x;
	    y -= v.y;
	    z -= v.z;
	    return this;
	  }
	  public Vector3D add(double n) {
		    x += n;
		    y += n;
		    z += n;
		    return this;
		  }
	  public Vector3D mult(double n) {
	    x *= n;
	    y *= n;
	    z *= n;
	    return this;
	  }

	  public Vector3D div(double n) {
	    x /= n;
	    y /= n;
	    z /= n;
	    return this;
	  }
	  
	  /*public float dot(Vector3D v) {
	    //implement DOT product
	  }*/
	  
	  /*public Vector3D cross(Vector3D v) {
	    //implement CROSS product
	  }*/

	  public void normalize() {
		  double m = magnitude();
	    if (m > 0) {
	       div(m);
	    }
	  }

	  public void limit(double max) {
	    if (magnitude() > max) {
	      normalize();
	      mult(max);
	    }
	  }

	  public double heading2D() {
		  double angle =  Math.atan2(-y, x);
	    return -1*angle;
	  }

	  public Vector3D add(Vector3D v1, Vector3D v2) {
	    Vector3D v = new Vector3D(v1.x + v2.x,v1.y + v2.y, v1.z + v2.z);
	    return v;
	  }
	  //public Vector3D add(Vector3D v1) {		  return add(this,v1);	  }

	  public Vector3D sub(Vector3D v1, Vector3D v2) {
	    Vector3D v = new Vector3D(v1.x - v2.x,v1.y - v2.y,v1.z - v2.z);
	    return v;
	  }

	  public Vector3D div(Vector3D v1, double n) {
	    Vector3D v = new Vector3D(v1.x/n,v1.y/n,v1.z/n);
	    return v;
	  }

	  public Vector3D mult(Vector3D v1, double n) {
	    Vector3D v = new Vector3D(v1.x*n,v1.y*n,v1.z*n);
	    return v;
	  }

	  public double distance (Vector3D v1, Vector3D v2) {
		  double dx = v1.x - v2.x;
		  double dy = v1.y - v2.y;
		  double dz = v1.z - v2.z;
	    return (double) Math.sqrt(dx*dx + dy*dy + dz*dz);
	  }
	  public String toString() {
		  return this.x+":"+this.y+":"+this.z;
		  
	  }
	  public Vector3D cross(Vector3D v) {
		  return new Vector3D(
				  this.y*v.z  -this.z*v.y,
				  this.z*v.x - this.x*v.z,
				  this.x*v.y - this.y*v.x
		  );
	  }
	  
	  public double dot(Vector3D v){
		  return   this.x*v.x+this.y*v.y+this.z-v.z;
	  }
	 
	
}
