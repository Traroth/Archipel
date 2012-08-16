package org.archipel.node;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.BlockingDeque;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.archipel.data.Data;
import org.archipel.graph.Graph;
import org.archipel.internal.data.DataVector;
import org.archipel.internal.data.Pilot;
import org.archipel.internal.node.Addable;
import org.archipel.internal.node.Builder;
import org.archipel.internal.node.Constructable;
import org.archipel.internal.node.Node;
import org.archipel.link.Link;

public abstract class AbstractNode<I extends Data, O extends Data> extends Node implements Constructable<O>, Addable<I> {
	
	Link<I> entry;
	
	List<Link<O>> exits = new ArrayList<Link<O>>();
	
	protected boolean stop = false;
	
	protected Logger logger = Logger.getLogger("org.archipel.AbstractNode");
	
	protected Builder<O> constructerO = new Builder<O>();
	
	protected Builder<I> adderI = new Builder<I>();
	
	public AbstractNode(Graph graph) {
		this.graph = graph;
		exits = new ArrayList<Link<O>>();
		thread = new Thread(this);
	}
	
	public void start() {
		for (Link<O> link : exits) {
			link.ignite();
		}
		thread.start();
	}
	
	@Override
	public void stop() {
		try {
			O killer = createO();
			Pilot pilot = new Pilot();
			pilot.setExtinguisher(true);
			killer.setPilot(pilot);
			for (Link<O> exit : exits) {
				exit.getQueue().putFirst(killer);
			}
		} catch (Exception e) {
			logger.log(Level.SEVERE, "Impossible to instanciate O",e);
		}
		this.stop=true;
	}
	
	@SuppressWarnings("unchecked")
	private O createO() throws InstantiationException, IllegalAccessException {
		Type type = getClass().getGenericSuperclass();
		Class<O> clazz=null;
		O instance=null;
		if (type instanceof ParameterizedType) {
            ParameterizedType paramType = (ParameterizedType)type;
            clazz = (Class<O>) paramType.getActualTypeArguments()[0];

            instance = clazz.newInstance();
		}
		
		
        return instance;
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
				DataVector<O> output = process(in);
				Iterator<O> iter = output.getVector().iterator();
				O data;
				for (Link<O> exit : exits) {
					try {
						if (iter.hasNext()) {
							data = iter.next();
							exit.getQueue().putFirst(data);
						} else {
							break;
						}
					} catch (InterruptedException ie) {
						logger.log(Level.WARNING,"Impossible to put a Data in an exit queue",ie);
					}
				}
			}
		}
	}
	
	public final void upAdd(Addable<O> node) {
		constructerO.upBuild(this, node, graph);
	}
	
	public final void downAdd(Constructable<I> node) {
		adderI.downBuild(node, this, graph);
	}
	
	@Override
	public Link<I> getEntry() {
		return entry;
	}

	@Override
	public void setEntry(Link<I> entry) {
		this.entry = entry;
	}

	@Override
	public List<Link<O>> getExits() {
		return exits;
	}

	@Override
	public void setExits(List<Link<O>> exits) {
		this.exits = exits;
	}
	
	abstract public DataVector<O> process(I input);
	
}
