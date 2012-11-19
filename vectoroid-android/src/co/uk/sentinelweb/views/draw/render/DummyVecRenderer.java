package co.uk.sentinelweb.views.draw.render;

import co.uk.sentinelweb.views.draw.model.DrawingElement;
import co.uk.sentinelweb.views.draw.model.UpdateFlags;

/**
 * this is a dummy renderer that does nothing. for cases when you just want to use strokes operations - and no renderer.
 * @author robert
 *
 */
public class DummyVecRenderer extends VecRenderer {

	@Override
	public void update(DrawingElement de, UpdateFlags flags) {
		// TODO Auto-generated method stub

	}

	@Override
	public void render(DrawingElement de) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setup() {
		// TODO Auto-generated method stub

	}

	@Override
	public void setupViewPort() {
		// TODO Auto-generated method stub

	}

	@Override
	public void revertViewPort() {
		// TODO Auto-generated method stub

	}

}
