package co.uk.sentinelweb.views.draw.util;

import android.graphics.Path;
import android.util.Log;
import co.uk.sentinelweb.views.draw.VecGlobals;
/* EXPERIMENTAL!!!! ARC CONSTRUCTION
 * Credit:
 * http://stackoverflow.com/questions/8201842/get-points-of-svg-xaml-arc
 * http://research.microsoft.com/en-us/um/people/awf/graphics/bres-ellipse.html
 */
public class ConicPlotter {
		boolean DEBUG = true;
		  // A xx + B xy + C yy + D x + E y + F = 0
		 Path p;
		  float A;
		  float B;
		  float C;
		  float D;
		  float E;
		  float F;
		 
//		  ConicPlotter();
//		  ConicPlotter(int A, int B, int C, int D, int E, int F);
//		  void assign(int A, int B, int C, int D, int E, int F);
//		  void assignf(double SCALE, double A, double B, double C, double D, double E, double F);
//		  
//		  // This is the routine which draws the conic, calling the plot member at each pixel.
//		  int draw(int xs, int ys, int xe, int ye);
//		 
//		  // User supplies this plot routine, which does the work.
//		  virtual void plot(int x, int y);
		
		 
		static int DIAGx[] = {999, 1,  1, -1, -1, -1, -1,  1,  1};
		static int DIAGy[] = {999, 1,  1,  1,  1, -1, -1, -1, -1};
		static int SIDEx[] = {999, 1,  0,  0, -1, -1,  0,  0,  1};
		static int SIDEy[] = {999, 0,  1,  1,  0,  0, -1, -1,  0};


		private static int odd(int n)
		{
		  return n&1;
		}


		int getoctant(float gx, float gy)
		{
		  // Use gradient to identify octant.
		  int upper = Math.abs(gx)>Math.abs(gy)?1:0;
		  if (gx>=0)                            // Right-pointing
		    if (gy>=0)                          //    Up
		      return 4 - upper;
		    else                                //    Down
		      return 1 + upper;
		  else                                  // Left
		    if (gy>0)                           //    Up
		      return 5 + upper;
		    else                                //    Down
		      return 8 - upper;
		}

		public int draw(float xs, float ys, float xe, float ye)
		{
		  A *= 4;
		  B *= 4;
		  C *= 4;
		  D *= 4;
		  E *= 4;
		  F *= 4;

		  Log.d(VecGlobals.LOG_TAG,"ConicPlotter::draw -- A:"+A+" B:"+B+" C:"+C+" D:"+D+" E:"+E+" F:"+F);

		  // Translate start point to origin...
		  F = A*xs*xs + B*xs*ys + C*ys*ys + D*xs + E*ys + F;
		  D = D + 2 * A * xs + B * ys;
		  E = E + B * xs + 2 * C * ys;
		  
		  // Work out starting octant
		  int octant = getoctant(D,E);
		  
		  int dxS = SIDEx[octant]; 
		  int dyS = SIDEy[octant]; 
		  int dxD = DIAGx[octant];
		  int dyD = DIAGy[octant];

		  float d,u,v;
		  switch (octant) {
		  case 1:
		    d = A + B/2 + C/4 + D + E/2 + F;
		    u = A + B/2 + D;
		    v = u + E;
		    break;
		  case 2:
		    d = A/4 + B/2 + C + D/2 + E + F;
		    u = B/2 + C + E;
		    v = u + D;
		    break;
		  case 3:
		    d = A/4 - B/2 + C - D/2 + E + F;
		    u = -B/2 + C + E;
		    v = u - D;
		    break;
		  case 4:
		    d = A - B/2 + C/4 - D + E/2 + F;
		    u = A - B/2 - D;
		    v = u + E;
		    break;
		  case 5:
		    d = A + B/2 + C/4 - D - E/2 + F;
		    u = A + B/2 - D;
		    v = u - E;
		    break;
		  case 6:
		    d = A/4 + B/2 + C - D/2 - E + F;
		    u = B/2 + C - E;
		    v = u - D;
		    break;
		  case 7:
		    d = A/4 - B/2 + C + D/2 - E + F;
		    u =  -B/2 + C - E;
		    v = u + D;
		    break;
		  case 8:
		    d = A - B/2 + C/4 + D - E/2 + F;
		    u = A - B/2 + D;
		    v = u - E;
		    break;
		  default:
		    Log.d(VecGlobals.LOG_TAG,"FUNNY OCTANT\n");
		    return 1;
		  }
		  
		  float k1sign = dyS*dyD;
		  float k1 = 2 * (A + k1sign * (C - A));
		  float Bsign = dxD*dyD;
		  float k2 = k1 + Bsign * B;
		  float k3 = 2 * (A + C + Bsign * B);

		  // Work out gradient at endpoint
		  float gxe = xe - xs;
		  float gye = ye - ys;
		  float gx = 2*A*gxe +   B*gye + D;
		  float gy =   B*gxe + 2*C*gye + E;
		  
		  int octantcount = getoctant(gx,gy) - octant;
		  if (octantcount < 0)
		    octantcount = octantcount + 8;
		  else if (octantcount==0)
		    if((xs>xe && dxD>0) || (ys>ye && dyD>0) ||
		       (xs<xe && dxD<0) || (ys<ye && dyD<0))
		      octantcount +=8;

		  if (DEBUG)
		    Log.d(VecGlobals.LOG_TAG,"octantcount = "+octantcount);
		  
		  float x = xs;
		  float y = ys;
		  
		  while (octantcount > 0) {
		    if (DEBUG)
		    	 Log.d(VecGlobals.LOG_TAG,"-- "+octant+" -------------------------\n"); 
		    
		    if (odd(octant)>0) {
		      while (2*v <= k2) {
		        // Plot this point
		        plot(x,y);
		        
		        // Are we inside or outside?
		        if (DEBUG)  Log.d(VecGlobals.LOG_TAG,"x = "+x+" y ="+y+" d = "+y+"\n");
		        if (d < 0) {                    // Inside
		          x = x + dxS;
		          y = y + dyS;
		          u = u + k1;
		          v = v + k2;
		          d = d + u;
		        }
		        else {                          // outside
		          x = x + dxD;
		          y = y + dyD;
		          u = u + k2;
		          v = v + k3;
		          d = d + v;
		        }
		      }
		    
		      d = d - u + v/2 - k2/2 + 3*k3/8; 
		      // error (^) in Foley and van Dam p 959, "2nd ed, revised 5th printing"
		      u = -u + v - k2/2 + k3/2;
		      v = v - k2 + k3/2;
		      k1 = k1 - 2*k2 + k3;
		      k2 = k3 - k2;
		      int tmp = dxS; dxS = -dyS; dyS = tmp;
		    }
		    else {                              // Octant is even
		      while (2*u < k2) {
		        // Plot this point
		        plot(x,y);
		        if (DEBUG)  Log.d(VecGlobals.LOG_TAG,"x = "+x+" y = "+y+" d = "+d);
		        
		        // Are we inside or outside?
		        if (d > 0) {                    // Outside
		          x = x + dxS;
		          y = y + dyS;
		          u = u + k1;
		          v = v + k2;
		          d = d + u;
		        }
		        else {                          // Inside
		          x = x + dxD;
		          y = y + dyD;
		          u = u + k2;
		          v = v + k3;
		          d = d + v;
		        }
		      }
		      float tmpdk = k1 - k2;
		      d = d + u - v + tmpdk;
		      v = 2*u - v + tmpdk;
		      u = u + tmpdk;
		      k3 = k3 + 4*tmpdk;
		      k2 = k1 + tmpdk;
		      
		      int tmp = dxD; dxD = -dyD; dyD = tmp;
		    }
		    
		    octant = (octant&7)+1;
		    octantcount--;
		  }

		  // Draw final octant until we reach the endpoint
		  if (DEBUG)
			  Log.d(VecGlobals.LOG_TAG,"-- "+octant+" (final) -----------------" ); 
		    
		  if (odd(octant)>0) {
		    while (2*v <= k2) {
		      // Plot this point
		      plot(x,y);
		      if (x == xe && y == ye)
		        break;
		      if (DEBUG) Log.d(VecGlobals.LOG_TAG,"x = "+x+" y = "+y+" d = "+d);
		      
		      // Are we inside or outside?
		      if (d < 0) {                      // Inside
		        x = x + dxS;
		        y = y + dyS;
		        u = u + k1;
		        v = v + k2;
		        d = d + u;
		      }
		      else {                            // outside
		        x = x + dxD;
		        y = y + dyD;
		        u = u + k2;
		        v = v + k3;
		        d = d + v;
		      }
		    }
		  }
		  else {                                // Octant is even
		    while ((2*u < k2)) {
		      // Plot this point
		      plot(x,y);
		      if (x == xe && y == ye)
		        break;
		      if (DEBUG) Log.d(VecGlobals.LOG_TAG,"x = "+x+" y = "+y+" d = "+d);
		      
		      // Are we inside or outside?
		      if (d > 0) {                      // Outside
		        x = x + dxS;
		        y = y + dyS;
		        u = u + k1;
		        v = v + k2;
		        d = d + u;
		      }
		      else {                            // Inside
		        x = x + dxD;
		        y = y + dyD;
		        u = u + k2;
		        v = v + k3;
		        d = d + v;
		      }
		    }
		  }

		  return 1;
		}

		void plot(float x, float y)
		{
			Log.d(VecGlobals.LOG_TAG,"plot("+x+","+y+")");
			p.lineTo(x, y);
		}

		public ConicPlotter() { assign(0,0,0,0,0,0); }
		public ConicPlotter(float A, float B, float C, float D, float E, float F) { assign(A,B,C,D,E,F); }

		void assign(float A_, float B_, float C_, float D_, float E_, float F_)
		{
		  A = A_;
		  B = B_;
		  C = C_;
		  D = D_;
		  E = E_;
		  F = F_;
		}

		int rnd(double x) { return (x>=0.0)?(int)(x + 0.5):(int)(x - 0.5); }

//		void ConicPlotter::assignf(double scale,
//		                           double A_, double B_, double C_, double D_, double E_, double F_)
//		{
//		  A = rnd(A_ * scale);
//		  B = rnd(B_ * scale);
//		  C = rnd(C_ * scale);
//		  D = rnd(D_ * scale);
//		  E = rnd(E_ * scale);
//		  F = rnd(F_ * scale);
//		}
		
		public void calcImplicitEllipseEquation(float rx,float  ry, float fi, float cx, float cy)		{
			B = (float)Math.sin(2 * fi) * (ry * ry - rx * rx);
			A = (float)Math.pow(ry * Math.cos(fi),2) + (float)Math.pow(rx * Math.sin(fi),2);
			C =  (float)Math.pow(rx * Math.cos(fi),2) +  (float)Math.pow(ry * Math.sin(fi),2);
			D = -B * cy - 2 * A * cx;
			E = -2 * C * cy - B * cx;
			F = C * cy * cy + A * cx * cx + B * cx * cy - rx * rx * ry * ry;
		}


		public Path getP() {
			return p;
		}


		public void setP(Path p) {
			this.p = p;
		}
		
		
}
