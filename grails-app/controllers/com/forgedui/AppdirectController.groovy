package com.forgedui

import java.awt.GraphicsConfiguration.DefaultBufferCapabilities;

import org.apache.commons.io.IOUtils;

import oauth.signpost.*;
import oauth.signpost.basic.*
import grails.plugin.asyncmail.AsynchronousMailService;

class AppdirectController {

	AsynchronousMailService asyncMailService;
	def licensingService;
	
	/*
	 * 
	 * http://devcenter.appdirect.com/event-payloads/api-error-codes
	 When the Software Vendor communicates a failure while processing information from AppDirect, one of the following error codes must be returned:
	 USER_ALREADY_EXISTS: this error code is typically used when the AppDirect admin tries to buy a subscription for an app he has already purchased directly from the Software Vendor. In that scenario, we'll show the user an error message and prompt him to link their accounts.
	 USER_NOT_FOUND: this error code is typically used when the AppDirect admin tries to unassign a user which is not found in the Software Vendor's account.
	 ACCOUNT_NOT_FOUND: this error code is typically used when the AppDirect admin tries to add/remove users from a account which cannot be found in the Software Vendor's records.
	 MAX_USERS_REACHED: this error code is typically used when the AppDirect admin tries to assign a user beyond the limits of the number of seats available. AppDirect will normally prevent that from happening using the read account info API and monitoring the app usage.
	 UNAUTHORIZED: this error code is returned when a user tries any action that is not authorized for that particular application. For example, if a non-admin tries to import an app to AppDirect. Another example where this code may be returned is if an application does not allow the original creator to be unsubscribed.
	 OPERATION_CANCELLED: the user manually interrupted the operation (clicking cancel on a account creation page, etc.).
	 CONFIGURATION_ERROR: the vendor endpoint is not currently configured
	 INVALID_RESPONSE: the vendor was unable to process the event fetched from AppDirect
	 UNKNOWN_ERROR: this error code may be used when none of the other error codes is appropriate.
	 */



	def index = {
		
		//355
		
		def activationCode=licensingService.getLicensingCode((String)17);
		
		println activationCode ;
		
		return;
		
		
	}



	def create={

		def event=callEventsAPI(params.token);

		if(event){

			// We dont care if customer is a new guy or no

			def customer = ForgedUICustomer.findByUuid((String) event.payload.company.uuid);

			//TODO: Should we care .. Is customer allowed to buy same product multiple times ?

			if(!customer){
				// create customer
				customer = new ForgedUICustomer(
						uuid:(String)event.payload.company.uuid,
						email : (String)event.payload.company.email,
						name : (String)event.payload.company.name,
						phone : (String)event.payload.company.phone,
						website:(String)event.payload.company.website);

				try{
					customer.save(failOnError:true);

				}catch(Exception e ){

					log.error(customer.errors);
					log.error(e);

					render (contentType:"text/xml"){
						result(){
							success('false')
							message("Errors creating account")
							errorCode('UNKNOWN_ERROR')
						}
					}

					return

				}


			}

			//let's add product here

			def product = ForgedUIProduct.findByProductId((String)event.payload.order.editionCode);

			if(!product){
				// fail

				render (contentType:"text/xml"){
					result(){
						success('false')
						message("Error : Product  ${(String)event.payload.order.editionCode} not found")
						errorCode('UNKNOWN_ERROR')
					}
				}

				return

			}

			// Ok product found and customer found ... party

			// based on product configuration ... When is expiry date


			def customerProduct = new ForgedUICustomerProduct(
					customer:customer,
					product:product,
					state:ForgedUICustomerProduct.CUSTOMER_PRODUCT_STATUS_ACTIVE,
					datePurchased:new Date());

			try{

				customerProduct.save(failOnError:true);


			}catch(Exception e ){

				log.error(e);

				log.error("Failed to save customer Product {$customerProduct.errors}")

				render (contentType:"text/xml"){
					result(){
						success('false')
						message("Error : Failed to assign produt to customer")
						errorCode('UNKNOWN_ERROR')
					}
				}

				return

			}

			// Now admin has to be created

			//lets create the customer . set the SSO and all

			try{

				def user = ForgedUIUser.findByEmail((String)event.creator.email);

				if(!user){

					user = new ForgedUIUser(
							email:(String)event.creator.email,
							username:(String)event.creator.email,
							password:'theSecretPassword',
							enabled:true,
							accountExpired:false,
							accountLocked:false,
							passwordExpired:false,
							product:customerProduct
							);


					// Add user to SSO :
					user.addToOpenIds(url: (String)event.creator.openId)

					//To do is necessary ?
					user.save(failOnError:true);

					// Add user roles to be configured
					for (roleName in grailsApplication.config.openid.registration.roleNames) {
						SecUserSecRole.create user, SecRole.findByAuthority(roleName)
					}
				}else{

					// user exists ... just assign product to user and lets do it

					user.setProduct(customerProduct);
					user.save(failOnError:true);

				}
				
				
				// Send email with activation code
				
				def creatorName=(String)event.creator.firstName;
				def creatorEmail=(String)event.creator.email;
				//def buyOrTry=product?.category=="standard"?" purchasing ":" trying ";
				def activationCode=licensingService.getLicensingCode((String)user.id);
				def msgTitle="ForgedUI activation";
				def msg ="<body>Dear $creatorName, <br/><br/> Thank you for activating ForgedUI. <br/> Your activation code is $activationCode <br/>To download, install, and activate ForgedUI please see the instructions on our website <a href=\"http://www.forgedui.com/support-2\">http://www.forgedui.com/support-2</a><br/> If you need help or have further inquiries do not hesitate to contact us at support@forgedui.com.<br/><br/> Thank you,<br/>ForgedUI Team</body>";
				
				
				
				asyncMailService.sendMail {
					// Mail parameters
					from 'ForgedUI Activation <admin@forgedui.com>'
					to creatorEmail;
					bcc 'orders@forgedui.com'
					subject msgTitle;
					html msg;					
					maxAttemptsCount: 3;   // Max 3 attempts to send, default 1
					delete: true;    // Mark message for delete after sent
					immediate: true;    // Send message immediately
					priority :10;   // If priority greater then message send faster
				}
				



				render (contentType:"text/xml"){
					result(){
						success('true')
						message("Account ${customerProduct.id} has been created successfully")
						accountIdentifier(customerProduct.id)
					}
				}

				return

			}catch(Exception e){

				log.error(e);

				render (contentType:"text/xml"){
					result(){
						success('false')
						message("Error : Failed to assign produt to customer")
						errorCode('UNKNOWN_ERROR')
					}
				}

				return
			}


		}else{

			// ! event
			render (contentType:"text/xml"){
				result(){
					success('false')
					message('The response received is invalid')
					errorCode('INVALID_RESPONSE')
				}
			}
		}
	}


	def change={

		def event=callEventsAPI(params.token);

		if(event){

			if(event.payload.account.accountIdentifier=='dummy-account'){

				render (contentType:"text/xml"){
					result(){
						success('true')
						message("success")
					}
				}


				return;
			}

			int identifier = Integer.parseInt((String)event.payload.account.accountIdentifier);

			def productCode = (String)event.payload.order.editionCode;

			def customerProduct=ForgedUICustomerProduct.get(identifier);

			if(customerProduct){

				def product = ForgedUIProduct.findByProductId(productCode);

				if(product){

					try{

						customerProduct.state=ForgedUICustomerProduct.CUSTOMER_PRODUCT_STATUS_ACTIVE;
						customerProduct.product=product;

						customerProduct.save(failOnError:true);

						render (contentType:"text/xml"){
							result(){
								success('true')
								message("success")
							}
						}

						return

					}catch(Exception e){

						log.error("Failed to cancel account {identifier}",e);

						render (contentType:"text/xml"){
							result(){
								success('false')
								message("Failed to cancel account ${identifier}")
								errorCode('UNKNOWN_ERROR')
							}
						}

						return
					}

				}else{

					render (contentType:"text/xml"){
						result(){
							success('false')
							message("Failed to update to product  ${productCode} . Product DNE")
							errorCode('UNKNOWN_ERROR')
						}
					}

					return
				}




			}else{

				//Customer DNE

				render (contentType:"text/xml"){
					result(){
						success('false')
						message("Account ${identifier} does not exist")
						errorCode('UNKNOWN_ERROR')
					}
				}




			}
		}else{
			render (contentType:"text/xml"){
				result(){
					success('false')
					message('The response received is invalid')
					errorCode('INVALID_RESPONSE')
				}
			}
		}


	}



	def cancel={

		// We are either single or multi user ...
		// If product is cancelled ... Lets simply set state to cancelled

		def event=callEventsAPI(params.token);

		if(event){

			if(event.payload.account.accountIdentifier=='dummy-account'){

				render (contentType:"text/xml"){
					result(){
						success('true')
						message("success")
					}
				}


				return;
			}

			int identifier = Integer.parseInt((String)event.payload.account.accountIdentifier);

			def customerProduct=ForgedUICustomerProduct.get(identifier);

			if(customerProduct){

				customerProduct.state=ForgedUICustomerProduct.CUSTOMER_PRODUCT_STATUS_CANCELLED;

				try{

					customerProduct.save(failOnError:true);

					render (contentType:"text/xml"){
						result(){
							success('true')
							message("success")
						}
					}

					return

				}catch(Exception e){

					log.error("Failed to cancel account {identifier}",e);

					render (contentType:"text/xml"){
						result(){
							success('false')
							message("Failed to cancel account ${identifier}")
							errorCode('UNKNOWN_ERROR')
						}
					}

					return
				}


			}else{

				//Customer DNE

				render (contentType:"text/xml"){
					result(){
						success('false')
						message("Account ${identifier} does not exist")
						errorCode('UNKNOWN_ERROR')
					}
				}




			}
		}else{
			render (contentType:"text/xml"){
				result(){
					success('false')
					message('The response received is invalid')
					errorCode('INVALID_RESPONSE')
				}
			}
		}

	}

	def notification={

		def event=callEventsAPI(params.token);

		if(event){

			if(event.payload.account.accountIdentifier=='dummy-account'){

				render (contentType:"text/xml"){
					result(){ success('true') }
				}


				return;
			}

			int identifier = Integer.parseInt((String)event.payload.account.accountIdentifier);

			// use email to check if the guy exists :
			def customerProduct = ForgedUICustomerProduct.get(identifier);

			if(customerProduct){

				// What is this event : Activation or de-activation?

				def noticeType=(String)event.payload.notice.type;

				boolean stat=true;
				def state = null;

				switch(noticeType){
					case "REACTIVATED":
						state=ForgedUICustomerProduct.CUSTOMER_PRODUCT_STATUS_ACTIVE;
						break;
					case "DEACTIVATED":
						state=ForgedUICustomerProduct.CUSTOMER_PRODUCT_STATUS_DEACTIVATED;
						break;
					case "UPCOMING_INVOICE":
					default:
						log.warn "Ignoring received event ${identifier} Event Type ${noticeType}"
				}



				try{

					if(state){

						customerProduct.state=state;

						customerProduct.save(failOnError:true);

					}

				}catch(Exception e){
					stat=false;
				}

				render (contentType:"text/xml"){
					result(){ success(stat) }
				}

			}else{
				//Customer DNE
				render (contentType:"text/xml"){
					result(){
						success('false')
						message("Account ${identifier} does not exist")
						errorCode('UNKNOWN_ERROR')
					}
				}


			}
		}else{
			render (contentType:"text/xml"){
				result(){
					success('false')
					message('The response received is invalid')
					errorCode('INVALID_RESPONSE')
				}
			}
		}
	}


	def assign={


		// We are either single or multi user ...
		// If product is cancelled ... Lets simply set state to cancelled

		def event=callEventsAPI(params.token);

		if(event){

			if(event.payload.account.accountIdentifier=='dummy-account'){

				render (contentType:"text/xml"){
					result(){ success('true') }
				}

				return;
			}

			int identifier = Integer.parseInt(((String)event.payload.account.accountIdentifier));

			def email =(String)event.payload.user.email;

			def customerProduct=ForgedUICustomerProduct.get(identifier);

			if(customerProduct){

				// Can we still assign ?
				// Find all users who are assigned this product (User Product) ....
				def users = ForgedUIUser.findAllByProduct(customerProduct);

				if(users.size()<customerProduct.product.numUsers){


					try{

						def shouldCreateGroup=false
						def user = ForgedUIUser.findByUsername(email);

						if(!user){

							// See if user exists ..

							// if exists simply just re-assign him

							user = new ForgedUIUser(
									email:(String)event.payload.user.email,
									username:(String)event.payload.user.email,
									password:'theSecretPassword',
									enabled:true,
									accountExpired:false,
									accountLocked:false,
									passwordExpired:false,
									product:customerProduct
									);


							// Add user to SSO :
							user.addToOpenIds(url: (String)event.payload.user.openId)

							shouldCreateGroup=true;


						}else{
							// if user exists already simply re-assign him to product

							user.product=customerProduct;
						}


						//To do is necessary ?
						user.save(failOnError:true);


						if(shouldCreateGroup){
							// Add user roles
							for (roleName in grailsApplication.config.openid.registration.roleNames) {
								SecUserSecRole.create user, SecRole.findByAuthority(roleName)
							}
						}


						render (contentType:"text/xml"){
							result(){ success('true') }
						}

						return

					}catch(Exception e){

						log.error("Failed to assign user to account {identifier}",e);

						render (contentType:"text/xml"){
							result(){
								success('false')
								message("Failed to assign user to account ${identifier}")
								errorCode('UNKNOWN_ERROR')
							}
						}

						return
					}

				}else{
					// Max limit reached error

					render (contentType:"text/xml"){
						result(){
							success('false')
							message("Failed to assign user to account ${identifier} . User Limit reached")
							errorCode('MAX_USERS_REACHED')
						}
					}

					return;

				}





			}else{

				//Account to be assigned does not exist

				render (contentType:"text/xml"){
					result(){
						success('false')
						message("Account ${identifier} does not exist")
						errorCode('ACCOUNT_NOT_FOUND')
					}
				}




			}
		}else{
			render (contentType:"text/xml"){
				result(){
					success('false')
					message('The response received is invalid')
					errorCode('INVALID_RESPONSE')
				}
			}
		}

	}


	def unassign={


		// We are either single or multi user ...
		// If product is cancelled ... Lets simply set state to cancelled

		def event=callEventsAPI(params.token);

		if(event){


			if(event.payload.account.accountIdentifier=='dummy-account'){

				render (contentType:"text/xml"){
					result(){
						success('true')
						message("success")
					}
				}

				return;
			}

			int identifier = Integer.parseInt((String)event.payload.account.accountIdentifier);
			def email =(String)event.payload.user.email;

			def customerProduct=ForgedUICustomerProduct.get(identifier);

			if(customerProduct){


				// Ok we need to unassign user from this Account (Customer Product)


				def user = ForgedUIUser.findByUsername(email);

				if(user){

					// Do we delete him or shut him down ?
					// isnt it better to delete him ?
					// Or else we have to modify the above code to check if dude exists

					try{

						user.product=null;

						user.save(failOnError:true)

						render (contentType:"text/xml"){
							result(){
								success('true')
								message("success")
							}
						}

						return

					}catch(Exception e){

						log.error("Failed to unassign user  ${email}",e);

						render (contentType:"text/xml"){
							result(){
								success('false')
								message("Failed to unassign user  ${email}")
								errorCode('UNKNOWN_ERROR')
							}
						}

						return
					}


				}else{
					// Could not unassign user . dude not found

					render (contentType:"text/xml"){
						result(){
							success('false')
							message("User ${email} does not exist")
							errorCode('USER_NOT_FOUND')
						}
					}

				}




			}else{

				//Account to be assigned does not exist

				render (contentType:"text/xml"){
					result(){
						success('false')
						message("Account ${identifier} does not exist")
						errorCode('ACCOUNT_NOT_FOUND')
					}
				}

			}
		}else{
			render (contentType:"text/xml"){
				result(){
					success('false')
					message('The response received is invalid')
					errorCode('INVALID_RESPONSE')
				}
			}
		}

	}



	Object callEventsAPI(String token){

		def urlS=grailsApplication.config.appdirect.events.api.url+token;

		def consumer = new oauth.signpost.basic.DefaultOAuthConsumer(grailsApplication.config.appdirect.oauth.key, grailsApplication.config.appdirect.oauth.secret)

		URL url = new URL(urlS);

		HttpURLConnection request = (HttpURLConnection) url.openConnection();
		consumer.sign(request);
		request.connect();

		int code = request.getResponseCode();
		
		
		if(code==200){

			InputStream is =request.getInputStream();

			def xmlString = IOUtils.toString(is);
	
			// save the event

			def appDirectEvent = new AppDirectEvent();

			appDirectEvent.token= token;
			appDirectEvent.xml=xmlString;

			appDirectEvent.save();

			org.apache.commons.io.IOUtils.closeQuietly(is);

			def data= new XmlSlurper().parseText(xmlString);

			data
		}else{

			
		//return new XmlSlurper().parseText("<event><creator><email>kattan.shoukry@gmail.com</email><firstName>Shoukri</firstName><language>en</language><lastName>Kattan</lastName><openId>https://www.appdirect.com/openid/id/1856a7bf-e6bf-4441-a471-9602277c5fd6</openId><uuid>1856a7bf-e6bf-4441-a471-9602277c5fd6</uuid></creator><flag>DEVELOPMENT</flag><marketplace><baseUrl>https://www.appdirect.com</baseUrl><partner>APPDIRECT</partner></marketplace><payload><company><country>CA</country><name>ForgedUI</name><phoneNumber>+16479983660</phoneNumber><uuid>99b5e2cd-32a6-4117-b379-f720e6633608</uuid><website>http://www.forgedui.com</website></company><order><editionCode>single_usr_bundle</editionCode><pricingDuration>MONTHLY</pricingDuration></order></payload><type>SUBSCRIPTION_ORDER</type></event>");
		null
		}
	}
}
