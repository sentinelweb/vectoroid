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
 * This is a transition motion to place particles in the right position to start the next motion
 * or just goto a target point
 * 
 * in a FIXED time
 * @author robert
 *
 */
public class GotoMotion extends Motion {
	
	private static final String TARGET = "target";
	private static final String ORIGIN = "origin";
	Motion nextMotion ;
	Vector3D point;
	
	public GotoMotion(Motion nextMotion,int timerLength) {
		super(timerLength);
		this.nextMotion = nextMotion;
	}
	
	public GotoMotion(Vector3D point,int timerLength) {
		super(timerLength);
		this.point = point;
	}
	
	@Override
	public void init(Particle pt) {
		pt.renderObjects.put(ORIGIN,pt.loc.copy());
		if (nextMotion!=null) {
			Particle testParticle = pt.duplicate(true);
			testParticle.timeInCycle=0;
			nextMotion.update(testParticle);
			pt.renderObjects.put(TARGET,testParticle.loc);
		} else if (point!=null) {
			pt.renderObjects.put(TARGET,point);
		}
	}

	@Override
	public void cleanup(Particle pt) {
		pt.renderObjects.remove(TARGET);
		pt.renderObjects.remove(ORIGIN);
	}
	
	@Override
	public boolean update(Particle p) {
		Vector3D tgt = (Vector3D)p.renderObjects.get(TARGET);
		Vector3D org = (Vector3D)p.renderObjects.get(ORIGIN);
		float sc = ((p.timeInCycle)/(float)timerLength);
		p.loc.setXYZ(compute(tgt.x, org.x, sc),compute(tgt.y, org.y, sc),compute(tgt.z, org.z, sc));
		return true;
	}

	private double compute(double tgt, double org, float sc) {
		return org+(tgt-org)*sc;
	}

}
