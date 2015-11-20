package com.forgedui

import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

import org.apache.commons.codec.binary.Base64;



import com.forgedui.ForgedUIUser;
import com.forgedui.utils.SecurityUtils;


class LicensingService {

	static transactional = true

	static final String URL_ENCODING_VALUE="UTF-8";

	def grailsApplication;

	def serviceMethod() {
	}

	def activate(String ip , Map params){


		String fuiKey = grailsApplication.config.forgedui.licensing.sharedkey;
		String serverKey=grailsApplication.config.forgedui.licensing.serverkey;

		def osname=params.osname;
		def osarch=params.osarch;
		def osuser=params.osuser;
		def osversion=params.osversion;

		def userId=params.username;

		def hash=params.hash;


		//First step verify message auth


		// Let's verify the post

		// First verify the hash :

		StringBuilder urlBuilder = new StringBuilder();
		urlBuilder.append("username=");
		urlBuilder.append(URLEncoder.encode(userId, URL_ENCODING_VALUE));
		urlBuilder.append("&osuser=");
		urlBuilder.append(URLEncoder.encode(osuser, URL_ENCODING_VALUE));
		urlBuilder.append("&osname=");
		urlBuilder.append(URLEncoder.encode(osname, URL_ENCODING_VALUE));
		urlBuilder.append("&osarch=");
		urlBuilder.append(URLEncoder.encode(osarch, URL_ENCODING_VALUE));
		urlBuilder.append("&osversion=");
		urlBuilder.append(URLEncoder.encode(osversion, URL_ENCODING_VALUE));
		urlBuilder.append("&hash=");

		String toHash="[${userId}][${osuser}][${osname}][${osarch}][${osversion}][${fuiKey}]";


		println "to Hash ${toHash} " ;

		//let's verify the hash :

		String calculatedHash = doHash(toHash.toString());

		if(calculatedHash!=hash) {

			return ['status':"failure"]

		}

		// Ok hash matches .. this msg is original .. lets proceed

		// This user is activating the plugin to see what are the features and is this user is a free or paid

		//load the User

		def user = ForgedUIUser.get((String)userId);

		if(user){

			// does user has a product

			ForgedUICustomerProduct userProduct = user?.product;

			if(userProduct){

				// Ok this guy has a product
				// What is the product status ?

				def productState = userProduct.state;

				// if active ?

				if(productState == ForgedUICustomerProduct.CUSTOMER_PRODUCT_STATUS_ACTIVE){
					// If and only if active


					ActivationRequest activationRequest = new ActivationRequest(
							user:user,
							ipAddress:ip,
							osname:osname,
							osarch:osarch,
							osuser:osuser,
							osversion:osversion

							);

					activationRequest.save();

					// What is the product type

					def productCategory = userProduct?.product?.category;

					if(productCategory=="standard"){
						// Only if standard

						// now lets see if this guy is honoring our policies

						// Fow now lets not do anything serious other than keeping logs

						// Also on product side we need to keep a timestamp and check only every timestamp



						// Just say its active and let's go

						def toSign ="[${userId}_full][${serverKey}]";

						String signature =doHash(toSign.toString());

						return ['status':"success",'username':userId,'licenseType':"full",'signature':signature]


					}else{
						//Fallback to trial mode
					}



				}else{
					// Product is either cancelled or expired
					// Fallback to demo

					return ['status':"failure"]

				}


			}else{

				// This guy does not have a product assigned to him ... Fail or fallback to demo
				return ['status':"failure"]
			}

		}else{

			//User is not even in DB .. What should we do here ?
			return ['status':"failure"]

		}

		// get his product


		//is this a trial


		return ['status':"success",'username':userId,'licenseType':"demo"]

	}


	def codegen(String ip , Map params){


		String fuiKey = grailsApplication.config.forgedui.licensing.sharedkey;
		String serverKey=grailsApplication.config.forgedui.licensing.serverkey;


		def macAddr = params.mcadd;
		def osname=params.osname;
		def osarch=params.osarch;
		def osuser=params.osuser;
		def osversion=params.osversion;

		def userId=params.account;

		def hash=params.sec;

		def timeString = params.time;



		//First step verify message auth


		// Let's verify the post

		// First verify the hash :

		StringBuilder urlBuilder = new StringBuilder();
		urlBuilder.append("username=");
		urlBuilder.append(URLEncoder.encode(userId, URL_ENCODING_VALUE));
		urlBuilder.append("&osuser=");
		urlBuilder.append(URLEncoder.encode(osuser, URL_ENCODING_VALUE));
		urlBuilder.append("&osname=");
		urlBuilder.append(URLEncoder.encode(osname, URL_ENCODING_VALUE));
		urlBuilder.append("&osarch=");
		urlBuilder.append(URLEncoder.encode(osarch, URL_ENCODING_VALUE));
		urlBuilder.append("&osversion=");
		urlBuilder.append(URLEncoder.encode(osversion, URL_ENCODING_VALUE));
		urlBuilder.append("&hash=");


		//hash("[" + user + "][" + osUserName
		//				+ "][" + osName + "][" + osArch + "][" + osVersion + "]["
		// + fuiKey + "][" + currentTimeString + "]");
		String toHash="[${userId}][${osuser}][${osname}][${osarch}][${osversion}][${fuiKey}][${timeString}]";


		println "CodeGen Params ${toHash} " ;

		//let's verify the hash :

		String calculatedHash = doHash(toHash.toString());

		if(calculatedHash!=hash) {

			return ['status':"failure"]

		}

		// Ok hash matches .. this msg is original .. lets proceed

		// This user is activating the plugin to see what are the features and is this user is a free or paid

		//load the User

		def user = ForgedUIUser.get((String)userId);

		if(user){

			// does user has a product

			ForgedUICustomerProduct userProduct = user?.product;

			if(userProduct){

				// Ok this guy has a product
				// What is the product status ?

				def productState = userProduct.state;

				// if active ?

				if(productState == ForgedUICustomerProduct.CUSTOMER_PRODUCT_STATUS_ACTIVE){
					// If and only if active



					CodeGenRequest codeGenRequest = new CodeGenRequest(

							macAddr: macAddr,
							osarch: osarch,
							osname: osname,
							osuser: osuser,
							osversion: osversion,
							userId: userId,
							timeString: timeString,

							);

					codeGenRequest.save();

					// What is the product type

					def productCategory = userProduct?.product?.category;

					if(productCategory=="standard"){
						// Only if standard

						// now lets see if this guy is honoring our policies

						// Fow now lets not do anything serious other than keeping logs

						// Also on product side we need to keep a timestamp and check only every timestamp



						// Just say its active and let's go

						def toSign ="[success][${serverKey}][${timeString}]";

						String signature =doHash(toSign.toString());

						return ['status':"success",'signature':signature]


					}else{
						//Fallback to trial mode
					}



				}else{
					// Product is either cancelled or expired
					// Fallback to demo

					return ['status':"failure"]

				}


			}else{

				// This guy does not have a product assigned to him ... Fail or fallback to demo
				return ['status':"failure"]
			}

		}else{

			//User is not even in DB .. What should we do here ?
			return ['status':"failure"]

		}

		// get his product


		//is this a trial


		return ['status':"failure"]

	}


	String getLicensingCode(String id){


		String key=grailsApplication.config.forgedui.licensing.sharedkey;

		// secure the data using the shared key

		String msg=id+"_"+key;

		String signedMsg = doHash(msg);

		String code = id+"_"+signedMsg;

		return code;
	}


	private static String doHash(String stringToHash){

		return SecurityUtils.doHash(stringToHash);
	}

}
