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

public class RibbonRenderer extends ParticleRenderer {
		int width = 10;
		int res = 3;
		boolean head = false;
		Vector3D colouriserMax = null;
		Vector3D colouriserMin = null;
		Vector3D colour = null;

		public RibbonRenderer(int res, boolean head, int width) {
			this.res = res;
			this.head = head;
			this.width = width;
			int maxPos = (int) (Math.random() * 3);
			colouriserMax = new Vector3D(maxPos == 0 ? 1 : 0, maxPos == 1 ? 1 : 0, maxPos == 2 ? 1 : 0);
			int minPos = (int) (Math.random() * 3);
			while (minPos == maxPos) {
				minPos = (int) (Math.random() * 3);
			}
			colouriserMin = new Vector3D(minPos == 0 ? 1 : 0, minPos == 1 ? 1 : 0, minPos == 2 ? 1 : 0);
		}

		public void init(Particle pt) {
		}

		public void cleanup(Particle pt) {
		}

		public void render(Particle pt) {
			Vector3D last = null;
			Vector3D lastRot = null;
			float alpha = 0.8f;
			float fadeOutTime = 50f;
			if (pt.timer < fadeOutTime) {
				alpha = 0.8f * (pt.timer / fadeOutTime);
			}
			// gl.glColor4f(1f, (float)pt.index/pt.ps.particles.size(), 0,
			// alpha);
			Vector3D col = colouriserMax.copy().add(colouriserMin.copy().mult((float) pt.index / pt.ps.particles.size()));
			// gl.glColor4f(col.x,col.y,col.z, alpha);
			if (head) {
				// gl.glPushMatrix();
				// gl.glTranslatef(pt.loc.x, pt.loc.y, pt.loc.z);
				// glu.gluSphere(glu.gluNewQuadric(), 10, 10, 8);
				// gl.glPopMatrix();
			}
			float alphaFactor = 1.0f / pt.trails.size();
			float alphaMultiplier = 1.0f;
			for (int i = 0; i < pt.trails.size(); i += res) {
				// gl.glPushMatrix();
				Vector3D theLoc = pt.trails.get(i);
				Vector3D theRot = pt.trailsRot.get(i);
				// gl.glColor4f(col.x,col.y,col.z, alpha*alphaMultiplier);

				if (last != null && i + 10 < pt.trails.size()) {
					// gl.glBegin(GL.GL_TRIANGLE_STRIP);
					// gl.glVertex3f(theLoc.x+width*(float)LookupTable.sinD(theRot.x),
					// theLoc.y+width*(float)LookupTable.cosD(theRot.x),
					// theLoc.z);
					// gl.glVertex3f(theLoc.x, theLoc.y, theLoc.z);
					// gl.glVertex3f(last.x+width*(float)LookupTable.sinD(lastRot.x),
					// last.y+width*(float)LookupTable.cosD(lastRot.x), last.z);
					// gl.glVertex3f(last.x, last.y, last.z);
					// gl.glEnd();
				}
				last = theLoc;
				lastRot = theRot;
				// gl.glPopMatrix();
				alphaMultiplier -= alphaFactor;
			}
		}
	}

	