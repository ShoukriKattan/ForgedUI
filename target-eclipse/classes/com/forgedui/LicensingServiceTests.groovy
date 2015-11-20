package com.forgedui

import com.forgedui.LicensingService
import com.forgedui.ForgedUIUser
import grails.test.mixin.*
import org.junit.*
import  grails.test.GroovyPagesTestCase

/**
 * See the API for {@link grails.test.mixin.services.ServiceUnitTestMixin} for usage instructions
 */

class LicensingServiceTests extends GroovyTestCase {
		
	
	static transactional = true
	
	def licensingService;

    void testSomething() {
        
			
		def user=ForgedUIUser.get("1")
		
		assertNotNull(user);
		
		String code = licensingService.getLicensingCode(user);
				
		println code
    }
}
