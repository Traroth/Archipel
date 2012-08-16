package org.archipel.internal.data;

import java.util.ArrayList;
import java.util.List;

import org.archipel.data.Data;

public class DataVector<D extends Data> {
	
	private List<D> vector;
	
	public DataVector() {
		vector = new ArrayList<D>();
	}

	public List<D> getVector() {
		return vector;
	}

	public void setVector(List<D> vector) {
		this.vector = vector;
	}

}
