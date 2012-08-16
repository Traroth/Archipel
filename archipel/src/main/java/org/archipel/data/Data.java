package org.archipel.data;

import org.archipel.internal.data.Pilot;

public abstract class Data {

	protected Pilot pilot = new Pilot();

	public Pilot getPilot() {
		return pilot;
	}

	public void setPilot(Pilot pilot) {
		this.pilot = pilot;
	}
	
}
