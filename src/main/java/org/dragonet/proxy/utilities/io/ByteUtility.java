/*
 * GNU LESSER GENERAL PUBLIC LICENSE
 *                       Version 3, 29 June 2007
 *
 * Copyright (C) 2007 Free Software Foundation, Inc. <http://fsf.org/>
 * Everyone is permitted to copy and distribute verbatim copies
 * of this license document, but changing it is not allowed.
 *
 * You can view LICENCE file for details. 
 *
 * @author The Dragonet Team
 */
package org.dragonet.proxy.utilities.io;

public class ByteUtility {
	// vars

	// constructor
	public ByteUtility() {

	}

	// public
	public static String bytesToHexString(byte[] data) {
		StringBuilder sb = new StringBuilder();
		String sTemp;
		for (int i = 0; i < data.length; i++) {
			sTemp = Integer.toHexString(0xFF & data[i]);
			if (sTemp.length() < 2) {
				sb.append(0);
			}
			sb.append(sTemp.toUpperCase()).append(", ");
		}
		return sb.toString();
	}

	// private

}
