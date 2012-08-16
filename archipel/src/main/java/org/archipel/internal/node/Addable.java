package org.archipel.internal.node;

import org.archipel.data.Data;
import org.archipel.link.Link;

public interface Addable<I extends Data> {
	
	public void downAdd(Constructable<I> constructable);
	
	public Link<I> getEntry();
	
	public void setEntry(Link<I> entry);

}
