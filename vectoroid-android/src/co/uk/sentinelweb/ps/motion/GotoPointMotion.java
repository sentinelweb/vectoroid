package co.uk.sentinelweb.ps.motion;

import co.uk.sentinelweb.ps.ParticleSystems.ParticleSystem.Particle;
import co.uk.sentinelweb.ps.Vector3D;
import co.uk.sentinelweb.ps.motion.Motion;

/**
 * Goto a point - uses a distance ratio to calculate velocity and a damping factor for the v change
 * damping : 0.002 overdamped - 0.007 underdamped.
 * @author robert
 *
 */
public class GotoPointMotion extends Motion {
		float divisor = 8;
		Vector3D tgt;
		Vector3D acc;
		Vector3D vecBuff = new Vector3D();
		Vector3D vecBuff1 = new Vector3D();
		public GotoPointMotion(Vector3D tgt,Vector3D acc,int damping) {
			super(1);
			this.tgt=tgt;
			this.acc=acc;
			this.divisor = damping;
			useTimer=false;
		}
		
		public boolean update(Particle pt) {
			vecBuff.setXYZ(// target velocity
				(tgt.x-pt.loc.x)*acc.x,
				(tgt.y-pt.loc.y)*acc.y,
				(tgt.z-pt.loc.z)*acc.z 
			);
			pt.vel.setXYZ(
					pt.vel.x+(vecBuff.x-pt.vel.x/divisor),
					pt.vel.y+(vecBuff.y-pt.vel.y/divisor),
					pt.vel.z+(vecBuff.z-pt.vel.z/divisor)
			);
			//pt.vel.setXYZ((tgt.x-pt.loc.x)/divisor,(tgt.y-pt.loc.y)/divisor,(tgt.z-pt.loc.z)/divisor);
			pt.loc.add(pt.vel);
			return !(Math.abs(tgt.x-pt.loc.x)<10 && Math.abs(tgt.y-pt.loc.y)<10 && Math.abs(tgt.z-pt.loc.z)<10); 
		}
	}