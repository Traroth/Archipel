package org.archipel.node;

import java.util.Iterator;
import java.util.concurrent.BlockingDeque;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.archipel.data.Data;
import org.archipel.graph.Graph;
import org.archipel.internal.node.Addable;
import org.archipel.internal.node.Builder;
import org.archipel.internal.node.Constructable;
import org.archipel.internal.node.Node;
import org.archipel.link.Link;

public abstract class AbstractExitNode<I extends Data> extends Node implements Addable<I> {
	
	Link<I> entry;
	
	protected Builder<I> adderI = new Builder<I>();
	
	protected boolean stop = false;
	
	protected Logger logger = Logger.getLogger("org.archipel.AbstractExitNode");
	
	public void start() {
		thread.start();
	}
	
	@Override
	public void stop() {
		this.stop=true;
	}
	
	@Override
	public void run() {
		BlockingDeque<I> queue = entry.getQueue();
		while (!stop) {
			I in = null;
			if (queue.size()>0) {
				in = entry.getQueue().removeLast();
			}
			if (in != null) {
				if (in.getPilot().isExtinguisher()) {
					stop();
					break;
				}
				process(in);
			}
		}
	}
	
	public AbstractExitNode(Graph graph) {
		this.graph = graph;
		thread = new Thread(this);
	}
	
	@Override
	public Link<I> getEntry() {
		return entry;
	}

	@Override
	public void setEntry(Link<I> entry) {
		this.entry = entry;
	}
	
	public final void downAdd(Constructable<I> node) {
		adderI.upBuild(node, this, graph);
	}
	
	abstract public void process(I input);

}
