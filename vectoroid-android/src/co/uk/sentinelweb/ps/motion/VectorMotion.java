package co.uk.sentinelweb.ps.motion;
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
import co.uk.sentinelweb.ps.Vector3D;

/**
 * This class can be used to use strokes as animation paths.
 * @author robert
 *
 */
public class VectorMotion extends Motion {
		float[] x;
		float[] y;
		float z;
		float length = -1;

		public VectorMotion(float[] x, float[] y, float z,int timerLength) {
			super(timerLength);
			this.x = x;
			this.y = y;
			this.z = z;
			length = getLength();
		}

		public boolean update(Particle pt) {
			// int index =
			// this.x.length-1>pt.counter?pt.counter:this.x.length-1;
			float index = ((pt.timeInCycle * (this.x.length - 2)) / 200f);
			// if (index>=this.x.length-1) {return false;}
			int div = (int) Math.floor(index / (this.x.length - 2));
			index = index % (this.x.length - 2);
			if (div % 2 == 1) {
				index = this.x.length - 2 - index;
			}
			pt.loc.z = (float) z;
			int stub = (int) Math.floor(index);
			// stub=Math.min(stub, this.x.length-2);
			float frac = index - stub;
			pt.loc.x = x[stub] + frac * (x[stub + 1] - x[stub]);
			pt.loc.y = y[stub] + frac * (y[stub + 1] - y[stub]);
			// pt.loc.z -= 2;
			pt.trails.enqueue(pt.loc.copy());
			float thetaVel = 4f;
			pt.rot.add(new Vector3D(thetaVel, 0, 0));
			pt.trailsRot.enqueue(pt.rot.copy());
			return true;
		}

		float getLength() {
			float len = 0;
			for (int i = 0; i < x.length - 1; i++) {
				len += Math.sqrt((x[i + 1] - x[i]) * (x[i + 1] - x[i]) + (y[i + 1] - y[i]) * (y[i + 1] - y[i]));
			}
			return len;
		}
	}

	