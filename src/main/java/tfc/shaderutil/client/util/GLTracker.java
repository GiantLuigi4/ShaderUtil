package tfc.shaderutil.client.util;

import java.util.HashMap;

public class GLTracker {
	private static final HashMap<Integer, Integer> buffersBound = new HashMap<>();
	public static int lvx, lvy, rvx, rvy;
	
	public static int getBound(int target) {
		return buffersBound.getOrDefault(target, 0);
	}
	
	public static int[] getTargets() {
		int[] bindings = new int[buffersBound.size()];
		Integer[] keySet = buffersBound.keySet().toArray(new Integer[0]);
		for (int i = 0; i < keySet.length; i++) bindings[i] = keySet[i];
		return bindings;
	}
	
	public static int[] getBindings() {
		int[] bindings = new int[buffersBound.size()];
		Integer[] keySet = buffersBound.values().toArray(new Integer[0]);
		for (int i = 0; i < keySet.length; i++) bindings[i] = keySet[i];
		return bindings;
	}
	
	public static void setBound(int target, int id) {
//		if (id == 0) buffersBound.remove(target); else
		if (!buffersBound.containsKey(target)) buffersBound.put(target, id);
		else buffersBound.replace(target, id);
	}
	
	public static int lvx() {
		return lvx;
	}
	
	public static void lvx(int lvx) {
		GLTracker.lvx = lvx;
	}
	
	public static int lvy() {
		return lvy;
	}
	
	public static void lvy(int lvy) {
		GLTracker.lvy = lvy;
	}
	
	public static int rvx() {
		return rvx;
	}
	
	public static void rvx(int rvx) {
		GLTracker.rvx = rvx;
	}
	
	public static int rvy() {
		return rvy;
	}
	
	public static void rvy(int rvy) {
		GLTracker.rvy = rvy;
	}
}
