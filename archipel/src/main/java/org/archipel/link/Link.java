package org.archipel.link;

import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;

import org.archipel.data.Data;
import org.archipel.internal.node.Node;

public class Link<D extends Data> {
	
	protected Node out;
	
	protected BlockingDeque<D> queue;
	
	public Link(Node out) {
		queue = new LinkedBlockingDeque<D>();
		this.out = out;
	}

	public BlockingDeque<D> getQueue() {
		return queue;
	}

	public void setQueue(BlockingDeque<D> queue) {
		this.queue = queue;
	}

	public Node getOut() {
		return out;
	}

	public void setOut(Node out) {
		this.out = out;
	}
	
	public void ignite() {
		out.start();
	}
	
	public void extinguish() {
		out.stop();
	}
	
	public boolean isEmpty() {
		return queue.isEmpty();
	}

}
