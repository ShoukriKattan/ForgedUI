package com.forgedui

import grails.converters.JSON;

import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.codec.binary.Base64;
import org.codehaus.groovy.grails.commons.ConfigurationHolder;

class LicensingController {

	def licensingService;



	def index = { }

	def activate={

		// We need to authenticate here
		// Also get the user
		// Also check if he licensed or activated before
		// And if he did we need to deny him his basic human need

		println "Request paramaters are ${params}";


		//lets go verify parameters

		def ip = request.getRemoteAddr();

		def data= licensingService.activate(ip,params);

		render data as JSON

	}

	def codegen={

		def ip = request.getRemoteAddr();

		def data= licensingService.codegen(ip,params);

		render data as JSON
	}
}
