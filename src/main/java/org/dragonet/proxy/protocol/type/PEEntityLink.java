package org.dragonet.proxy.protocol.type;

/**
 * Created on 2017/10/21.
 */
public class PEEntityLink {
	// vars
	public long eidFrom;
	public long eidTo;
	public int type;
	public boolean bool;

	// constructor
	public PEEntityLink() {

	}

	// public
	public PEEntityLink(long eidFrom, long eidTo, int type, boolean bool) {
		this.eidFrom = eidFrom;
		this.eidTo = eidTo;
		this.type = type;
		this.bool = bool;
	}

	// private

}
