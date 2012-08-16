package org.archipel.node;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
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

public abstract class AbstractEntryNode<O extends Data> extends Node implements Constructable<O> {
	
	List<Link<O>> exits = new ArrayList<Link<O>>();
	
	protected Builder<O> constructerO = new Builder<O>();
	
	protected boolean stop = false;
	
	//protected boolean extinguish = false;
	
	protected Logger logger = Logger.getLogger("org.archipel.AbstractEntryNode");
	
	public AbstractEntryNode(Graph graph) {
		this.graph = graph;
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
		while (!stop) {
			DataVector<O> output = process();
			
			List<O> list = output.getVector();
			if (list==null || list.size()==0) {
				stop=true;
			} else {
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
		stop();
	}
	
	public List<Link<O>> getExits() {
		return exits;
	}

	public void setExits(List<Link<O>> exits) {
		this.exits = exits;
	}
	
	public final void upAdd(Addable<O> node) {
		constructerO.downBuild(this, node, graph);
	}

	abstract public DataVector<O> process();

}
