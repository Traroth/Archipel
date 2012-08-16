package org.archipel.internal.node;

import org.archipel.graph.Graph;
import org.archipel.link.Link;

public abstract class Node implements Runnable {
	
	protected Graph graph;
	
	protected Thread thread;
	
	abstract public void start();
	
	abstract public void stop();

}
