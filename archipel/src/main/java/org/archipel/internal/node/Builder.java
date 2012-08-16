package org.archipel.internal.node;

import org.archipel.data.Data;
import org.archipel.graph.Graph;
import org.archipel.link.Link;
import org.archipel.node.AbstractEntryNode;
import org.archipel.node.AbstractExitNode;

public class Builder<D extends Data> {
	
	private final void build(Constructable<D> node, Addable<D> node2, Graph graph) {
		Link<D> entry = node2.getEntry();
		if (entry == null) {
			entry = new Link<D>((Node)node2);
			node2.setEntry(entry);
		}
		node.getExits().add(entry);
	}
	
	public final void upBuild(Constructable<D> node, Addable<D> node2, Graph graph) {
		build(node, node2, graph);
		if (node2 instanceof AbstractExitNode<?>) {
			graph.addToExits((AbstractExitNode)node2);
		}
	}
	
	public final void downBuild(Constructable<D> node, Addable<D> node2, Graph graph) {
		build(node, node2, graph);
		if (node instanceof AbstractEntryNode<?>) {
			graph.addToEntries((AbstractEntryNode)node);
		}
	}

}
