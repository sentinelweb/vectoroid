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

/**
 * Motions are designed to be stateless objects - if you store state in a Motion 
 * then you need to create a new Motion for each Particle in the System
 * @author robert
 */

public abstract class Motion {
	
	/**
	 * @param timerLength time (ms) the motion stays active setting timerLength=1.
	 */
	public Motion(int timerLength) {
		this.timerLength = timerLength;
	}
	/**
	 * Weather to use timer or not - a motion doesn't have to be time based it might.
	 */
	public boolean useTimer = true;
	public int timerLength=1;
	
	/**
	 * The main looping function for the motion - update the particle loc,vel,acc here.
	 * 
	 * @param p particle.
	 * @return true to continue, false to stop (will go to next motion if available).
	 */
	public abstract boolean update(Particle p);
	
	/**
	 * Called when the motion is activated on the particle.
	 * 
	 * @param pt particle
	 */
	public void init(Particle pt){};
	
	/**
	 * It's good form to remove any object you added to the particles renderObjects.
	 * 
	 * Called when the motion is deactivated.
	 * 
	 * @param pt particle
	 */
	public void cleanup(Particle pt){};
}