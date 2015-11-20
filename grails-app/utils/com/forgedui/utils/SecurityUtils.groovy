/**
 * 
 */
package com.forgedui.utils

import java.security.MessageDigest;

import org.apache.commons.codec.binary.Base64;

/**
 * @author shoukry
 *
 */
class SecurityUtils {

	public static String doHash(String stringToHash){

		MessageDigest md = MessageDigest.getInstance("MD5");

		byte[] dst = md.digest(stringToHash.getBytes());

		return Base64.encodeBase64URLSafeString(dst);
	}
}
