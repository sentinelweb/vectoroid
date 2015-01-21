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

public class PunchMotion extends Motion {
	Vector3D velLimit;
	Vector3D velOffset;
	public PunchMotion() {
		super(1);
		useTimer=false;
	}
	public PunchMotion(Vector3D vel) {
		super(1);
		useTimer=false;
		this.velLimit = vel;
	}
	public PunchMotion(float vel) {
		super(1);
		useTimer=false;
		this.velLimit = new Vector3D(vel,vel,vel);	
	}
	public PunchMotion(Vector3D vel, Vector3D off) {
		super(1);
		useTimer=false;
		this.velLimit = vel;
		this.velOffset=off;
	}
	public boolean update(Particle pt) {
		if (velOffset==null) {
			pt.initVelocity(velLimit);
		} else {
			pt.initVelocity(velLimit,velOffset);
		}
		return false;
	}
}
	
	