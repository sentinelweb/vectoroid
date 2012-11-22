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
import java.util.HashMap;

import co.uk.sentinelweb.ps.motion.Motion;
import co.uk.sentinelweb.ps.render.ParticleRenderer;
import co.uk.sentinelweb.views.draw.render.VecRenderer;
import co.uk.sentinelweb.views.draw.render.ag.AndGraphicsRenderer;

/** 
 * Particle system.
 * This was heavily adapted from a processing example:
 * @see http://processing.org/learning/topics/multipleparticlesystems.html
 */
public class ParticleSystems {
	ArrayList<ParticleSystem> psystems;
	public VecRenderer agr;

	public ParticleSystems(VecRenderer agr) {// OpenGL ogl, PApplet p
		super();
		psystems = new ArrayList<ParticleSystem>();
		this.agr = agr;
		// this.ogl=ogl;
		// this.gl = ogl.gl;
		// this.glu = new GLU();
		// this.p=p;
		// this.s = new Shape(this.gl);
	}

	public void render() {
		// gl.glPushMatrix();

		for (int i = psystems.size() - 1; i >= 0; i--) {
			ParticleSystem psys = psystems.get(i);
			psys.run();
			if (psys.dead()) {
				psystems.remove(i);
			}
		}
		// gl.glPopMatrix();
	}

	public void setAcc(Vector3D acc) {
		for (int i = 0; i < psystems.size(); i++) {
			ParticleSystem ps = psystems.get(i);
			for (int j = 0; j < ps.particles.size(); j++) {
				ps.particles.get(j).acc = acc.copy();
			}
		}
	}

	public void addSystem(ParticleSystem ps) {
		psystems.add(ps);
	}

	public boolean dead() {
		return psystems == null || psystems.size() == 0;
	}
	
	public void kill() {
		for (int i = psystems.size() - 1; i >= 0; i--) {
			ParticleSystem psys = psystems.get(i);
			psys.kill();
		}
		psystems.clear();
	}
	
	public VecRenderer getVectoroidRenderer() {
		return agr;
	}
	/*********
	 * ParticleSystem
	 * ***********************************************************
	 * ***************************************************
	 */

	public class ParticleSystem {
		//public HashMap<Motion,ArrayList<Particle>> particleMotion = new HashMap<Motion, ArrayList<Particle>>();
		ArrayList<Motion> motions;
		ParticleRenderer renderer;
		public ArrayList<Particle> particles; // An arraylist for all the particles
		Vector3D origin; // An origin point for where particles are birthed
		int trailLength = 50;
		Long renderTime = null;
		int motionIndex=0;
		public ParticleSystem(ArrayList<Motion> motions) {
			this.motions=motions;
			init(0, new Vector3D(), null);
		}
		
		public ParticleSystem(Motion m, ParticleRenderer ren) {
			init(0, origin, m, ren);
		}
		
		public ParticleSystem(int num, Vector3D origin) {
			init(num, origin,(Motion) null, null);
		}

		public ParticleSystem(int num, Vector3D origin, Motion m, ParticleRenderer ren) {
			init(num, origin, m, ren);
		}
		
		public ParticleSystem(int num, Vector3D origin, Motion m, ParticleRenderer ren, int trailLength) {
			this.trailLength = trailLength;
			motions=new ArrayList<Motion>();
			motions.add(m);
			init(num, origin,  ren);
		}
		
		public ParticleSystem(int num, Vector3D origin, ArrayList<Motion> motions, ParticleRenderer ren, int trailLength) {
			this.trailLength = trailLength;
			//this.timerLength = timerLength;
			init(num, origin, motions, ren);
		}
		
		public ParticleSystem(int num, Vector3D origin, ArrayList<Motion> motions, ParticleRenderer ren) {
			init(num, origin, motions, ren);
		}
		
		/* ********** init ********************** */
		private void init(int num, Vector3D v, ArrayList<Motion> motions, ParticleRenderer ren) {
			this.motions=motions;
			init( num,  v,  ren);
		}
		
		private void init(int num, Vector3D v, Motion m, ParticleRenderer ren) {
			motions=new ArrayList<Motion>();
			if (m!=null) {
				motions.add(m);
			}
			init( num,  v, ren);
		}
		
		private void init(int num, Vector3D v,  ParticleRenderer ren) {
			particles = new ArrayList<Particle>(); // Initialize the arraylist
			if (ren!=null) {
				ren.pss=ParticleSystems.this;
				this.renderer = ren;
			}
			//this.motion = m;
			origin = v.copy(); // Store the origin point
			for (int i = 0; i < num; i++) {
				// m,ren <!-- it possible to have diffrerent renderers and
				// motions
				// for each particle but you would init the particle system your
				// self if you wanted to do this
				particles.add(new Particle(origin, motions.get(0), ren, i, this));
				//ArrayList<Particle> motionArray = new ArrayList<ParticleSystems.ParticleSystem.Particle>();
				//motionArray.addAll(particles);
				//particleMotion.put(motions.get(0),motionArray);
			}
		}
		
		public Particle newParticle(ParticleRenderer renderer) {
			renderer.pss=ParticleSystems.this;
			Particle p=new Particle(origin, motions.get(0), renderer, particles.size(), this);
			//renderer.init(p);
			particles.add(p);
			return p;
		}
		
		public void run() {
			// Cycle through the ArrayList backwards b/c we are deleting
			//Log.d(Globals.TAG,"run ..");
			for (int i = particles.size() - 1; i >= 0; i--) {
				Particle p = particles.get(i);
				p.run();

				if (p.dead()) {
					int index = motions.indexOf(p.motion);
					if (index<motions.size()-1){
						int j = index+1;
						Motion nextMotion = motions.get(j);
						//Log.d(Globals.TAG," motion change: "+p.motion.getClass().getSimpleName()+" >" +nextMotion.getClass().getSimpleName());
						p.continueMotion(nextMotion);
					} else {
						removeParticle(i);
					}
				}
			}
		}

		public long getTime() {
			if (renderTime!=null) {
				return renderTime;
			} else {
				return System.currentTimeMillis();
			}
		}
		public void setTime(long t) {renderTime=t;}
		public void removeParticle(int i) {
			//Log.d(Globals.TAG,"dead ..");
			Particle rem = particles.remove(i);
			rem.cleanup();
			
		}

		public void addParticle(Particle p) {
			particles.add(p);
		}

		// A method to test if the particle system still has particles
		public boolean dead() {
			if (particles.isEmpty()) {
				return true;
			} else {
				return false;
			}
		}
		
		public void kill() {
			for (int i = particles.size() - 1; i >= 0; i--) {
				Particle p = particles.get(i);
				p.cleanup();
			}
			particles.clear();
		}
		/*********
		 * Particle
		 * *****************************************************************
		 * *********************************************
		 */

		// A simple Particle class
		public class Particle {
			private static final int DEF_TRAIL_LENGTH = 0;
			private static final int DEF_TIMER_LENGTH = 0;
			
			public int trailLength = DEF_TRAIL_LENGTH;
			public int timerLength = DEF_TIMER_LENGTH;
			
			public Vector3D loc;
			public Vector3D vel;
			public Vector3D acc;
			public Vector3D rot;
			public int index = 0;
			public int timer;
			public int timeInCycle = 0;
			public RingBuffer<Vector3D> trails;
			public RingBuffer<Vector3D> trailsRot;
			public long startTime = getTime();

			public long lastUpdateTime = startTime;
			public ParticleSystem ps;
			
			Motion motion;
			ParticleRenderer ren;
			
			public HashMap<String, Object> renderObjects = new HashMap<String, Object>();

			public Particle(Vector3D a, Vector3D v, Vector3D l, Motion m, ParticleRenderer ren, int index) {// ,
				init(a, v, l, m, ren, index);
			}

			public Particle(Vector3D l, Motion m, ParticleRenderer ren, int index, ParticleSystem ps) {
				init(acc, vel, l, m, ren, index);
			}

			private void init(Vector3D a, Vector3D v, Vector3D l, Motion m, ParticleRenderer ren, int index) {
				acc = a!=null?a.copy():new Vector3D();
				vel = v!=null?v.copy():new Vector3D();
				loc = l!=null?l.copy():new Vector3D();
				rot = new Vector3D();
				ps=ParticleSystem.this;
				
				this.index = index;
				this.motion = m;
				this.ren = ren;
				if (this.ren != null) {
					this.ren.init(this);
				} //else if (ParticleSystem.this.renderer != null) {
				//	ParticleSystem.this.renderer.init(this);
				//}
				if (m!=null) {
					timerLength = motion.timerLength;
					timer = timerLength;
				}
				if (ParticleSystem.this != null) {
					trailLength = ParticleSystem.this.trailLength;
					
				}
				trails = new RingBuffer<Vector3D>(trailLength);
				trailsRot = new RingBuffer<Vector3D>(trailLength);
			}
			public void initVelocity(float size) {
				vel = new Vector3D(
						(float) (Math.random()*2*size - size), 
						(float) (Math.random()*2*size - size), 
						(float) (Math.random()*2*size - size)
					);
			}
			public void initVelocity(Vector3D size) {
				vel = new Vector3D(
						(float) (Math.random()*2*size.x - size.x), 
						(float) (Math.random()*2*size.y - size.y), 
						(float) (Math.random()*2*size.z - size.z)
					);
			}
			public void initVelocity(Vector3D size,Vector3D offset) {
				vel = new Vector3D(
						(float) (Math.random()*size.x + offset.x), 
						(float) (Math.random()*size.y + offset.y), 
						(float) (Math.random()*size.z + offset.z)
					);
			}
			public void initAcc(float size) {
				acc = new Vector3D(
						(float) (Math.random()*2*size - size), 
						(float) (Math.random()*2*size - size), 
						(float) (Math.random()*2*size - size)
					);
			}
			public void initAcc(Vector3D size) {
				acc = new Vector3D(
						(float) (Math.random()*2*size.x - size.x), 
						(float) (Math.random()*2*size.y - size.y), 
						(float) (Math.random()*2*size.z - size.z)
					);
			}
			public Motion getMotion() {
				return motion;
			}

			public void setMotion(Motion m) {
				this.motion = m;
			}

			void run() {
				update();
				render();
			}

			// Method to update location
			public void update() {
				Motion m = this.motion ;//!= null ? this.motion : ps.motion;
				timeInCycle = (int) (getTime() - startTime) ;				
				//Log.d(Globals.TAG, "update:"+timeInCycle+":"+timer+":"+timerLength+":"+(timerLength - timeInCycle));				
				if (m.update(this)) {
					if (m.useTimer){
						timer = timerLength - timeInCycle;
					}
				} else  {
					timer = 0;
				}
				lastUpdateTime = getTime();
			}

			// Method to display
			public void render() {
				if (ren != null) {
					ren.render(this);
				} else {
					ParticleSystem.this.renderer.render(this);
				}
			}
			
			public void continueMotion( Motion motion ) {
				this.motion.cleanup(this);
				this.motion=motion;
				this.motion.init(this);
				initTimers(this.motion);
			}

			public void initTimers(Motion motion) {
				timerLength = motion.timerLength;
				timer = timerLength;
				timeInCycle=0;
				startTime = getTime();
			}
			
			public boolean dead() {
				if (timer <= 0.0) {
					return true;
				} else {
					return false;
				}
			}

			public void cleanup() {
				motion.cleanup(this);
				if (this.ren != null) {
					this.ren.cleanup(this);
				} else if (ParticleSystem.this.renderer != null) {
					ParticleSystem.this.renderer.cleanup(this);
				}
			}
			
			public Particle duplicate(boolean shallow) {
				if (shallow) {
					Particle particle = new Particle(acc, vel, loc, null, null, index);
					particle.initTimers(motion);
					return particle;
				} else {
					Particle particle = new Particle(acc, vel, loc, motion, ren, index);
					return particle;
				}
			}
		}
	}
}
