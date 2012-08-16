package org.archipel.graph;

import java.util.ArrayList;
import java.util.List;

import org.archipel.data.Data;
import org.archipel.node.AbstractEntryNode;
import org.archipel.node.AbstractExitNode;

public class Graph {
	
	private List<AbstractEntryNode<? extends Data>> entries;
	
	private List<AbstractExitNode<? extends Data>> exits;
	
	public Graph() {
		entries = new ArrayList<AbstractEntryNode<? extends Data>>();
		exits = new ArrayList<AbstractExitNode<? extends Data>>();
	}
	
	public void addToEntries(AbstractEntryNode<Data> node) {
		entries.add(node);
	}
	
	public void addToExits(AbstractExitNode<Data> node) {
		exits.add(node);
	}
	
	public void start() {
		for (AbstractEntryNode<? extends Data> entry : entries) {
			entry.start();
		}
	}
	
	public void stop() {
		for (AbstractEntryNode<? extends Data> entry : entries) {
			entry.stop();
		}
	}

}
