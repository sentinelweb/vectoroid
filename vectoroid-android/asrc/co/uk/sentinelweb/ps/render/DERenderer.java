package co.uk.sentinelweb.ps.render;
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

import android.graphics.PointF;
import co.uk.sentinelweb.ps.ParticleSystems.ParticleSystem.Particle;
import co.uk.sentinelweb.views.draw.model.DrawingElement;
import co.uk.sentinelweb.views.draw.model.Stroke;
import co.uk.sentinelweb.views.draw.render.VecRenderer.Operator;
import co.uk.sentinelweb.views.draw.util.OnAsyncListener;

/**
 * This is a Flyweight Object for updating the animations array in the Vectoroid Renderer
 * it stores the DrawingElement and Renderer.Operator in the particle renderObjects
 * @author robert
 *
 */
public class DERenderer extends ParticleRenderer {
		private static final String AGR_OPERATOR = "agrOperator";
		private static final String DE = "de";
		ArrayList<DrawingElement> deArray;
		int counter = -1;
		OnAsyncListener<DrawingElement> _cleaner= null;
		
		public DERenderer(ArrayList<DrawingElement> deArray) {// Vector3D col
			this.deArray = deArray;
		}
		public DERenderer() {// Vector3D col
		}
		public void set(ArrayList<Stroke> deArray) {
			this.deArray =new ArrayList<DrawingElement>();
			this.deArray.addAll(deArray);
		}
		public DERenderer(DrawingElement de) {// Vector3D col
			this.deArray = new ArrayList<DrawingElement>();
			this.deArray.add(de);
		}
		public void init(Particle pt) {
			counter++;
			DrawingElement drawingElement = deArray.get(counter);
			pt.renderObjects.put(DE, drawingElement);
			Operator agrOperator = new Operator();
			agrOperator.translate = new PointF();
			agrOperator.scale = new PointF(1, 1);
			pt.renderObjects.put(AGR_OPERATOR, agrOperator);
			pss.agr.animations.put(drawingElement, agrOperator);
		}

		public void render(Particle pt) {
			Operator operator = (Operator) pt.renderObjects.get(AGR_OPERATOR);
			operator.translate.set((float)pt.loc.x, (float)pt.loc.y);
			// ref: http://www.gamedev.net/topic/602569-simulate-z-axis-in-2d/
			float D = 10000;
			double scale = D/(D+(pt.loc.z));
			if (scale<0) {
				scale=0.01;
			} 
			operator.scale.set((float)scale,(float)scale);
		}

		public void cleanup(Particle pt) {
			DrawingElement drawingElement = (DrawingElement) pt.renderObjects.remove(DE);
			pss.agr.animations.remove(drawingElement);
			if (_cleaner!=null) {
				_cleaner.onAsync(drawingElement);
			}
		}
		
		public OnAsyncListener<DrawingElement> getCleaner() {
			return _cleaner;
		}
		public void setCleaner(OnAsyncListener<DrawingElement> cleaner) {
			this._cleaner = cleaner;
		}
		
	}

	