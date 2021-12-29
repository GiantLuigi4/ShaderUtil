package tfc.shaderutil.client.api;

import com.mojang.blaze3d.platform.GlStateManager;
import tfc.shaderutil.client.util.GLTracker;



/**
 * this is used to keep track of which buffer is bound before it was instanced and quickly rebind it at any given moment
 * this is here for mod compatibility, use it
 *
 * usage:
 * 		FBOBinder binder = new FBOBinder();
 * 		myFrameBuffer.beginWrite();
 * 		// draw	stuff
 * 		myFrameBuffer.endWrite();
 * 		binder.rebind();
 */
public class FBOBinder {
	private final int[] targets;
	private final int[] bindings;
	
	private final int lmx, lmy, rmx, rmy;
	
	public FBOBinder() {
		targets = GLTracker.getTargets();
		bindings = GLTracker.getBindings();
		
		lmx = GLTracker.lvx;
		lmy = GLTracker.lvy;
		rmx = GLTracker.rvx;
		rmy = GLTracker.rvy;
	}
	
	public void rebind() {
		for (int i = 0; i < targets.length; i++) {
			int targ = targets[i];
			int binding = bindings[i];
			GlStateManager._glBindFramebuffer(targ, binding);
		}
		GlStateManager._viewport(lmx, lmy, rmx, rmy);
	}
}
