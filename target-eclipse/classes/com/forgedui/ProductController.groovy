package com.forgedui

import grails.plugins.springsecurity.Secured

class ProductController {

	def springSecurityService;
	
	def licensingService;
	
	
    def index = {
			
		
		
		def prince=springSecurityService.principal
		

				
		// Ok so we need to provide User with activation code 
		String code = licensingService.getLicensingCode((String)prince.id);
		
		String downloadLink=grailsApplication.config.forgedui.product.update.site.url;
		
		[user:prince , code : code , downloadlink:downloadLink]
		
	}
	
	
	
	
	
	
	
}
