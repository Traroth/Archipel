package org.archipel.internal.node;

import java.util.List;

import org.archipel.data.Data;
import org.archipel.link.Link;

public interface Constructable<O extends Data> {
	
	public void upAdd(Addable<O> addable);
	
	public List<Link<O>> getExits();
	
	public void setExits(List<Link<O>> exits);

}
