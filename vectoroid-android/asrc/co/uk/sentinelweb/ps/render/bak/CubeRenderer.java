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
import co.uk.sentinelweb.ps.ParticleSystems.ParticleSystem.Particle;
import co.uk.sentinelweb.ps.render.ParticleRenderer;
import co.uk.sentinelweb.ps.Vector3D;

public class CubeRenderer extends ParticleRenderer {
		Vector3D col = new Vector3D(1f, 0.5f, 0);

		public CubeRenderer() {
		}

		public CubeRenderer(Vector3D col) {
			this.col = col;
		}
		public void init(Particle pt) {
		}

		public void cleanup(Particle pt) {
		}

		public void render(Particle pt) {
			/*
			 * Shape.Cube cube =(Shape.Cube)pt.renderObjects.get("cube"); if
			 * (cube==null) { cube=s.new Cube( 20 , 5,
			 * 5,//Math.round(random(-100, 100)), 0, 0, 0 ); cube.initCube();
			 * pt.renderObjects.put("cube",cube); } float alpha=0.6f; float
			 * fadeOutTime = 30f; if (pt.timer<fadeOutTime) {
			 * alpha=0.6f*(pt.timer/fadeOutTime); } // gl.glPushMatrix(); //
			 * gl.glTranslatef(pt.loc.x, pt.loc.y, pt.loc.z); //
			 * gl.glRotatef(pt.rot.x, 1,0,0); // gl.glRotatef(pt.rot.y, 0,1,0);
			 * // gl.glRotatef(pt.rot.y, 0,0,1); // gl.glColor4f(col.x,
			 * col.y,col.z,alpha); cube.draw(); // gl.glPopMatrix();
			 */
		}

	}

	