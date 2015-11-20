package com.forgedui

import com.forgedui.LicensingService
import com.forgedui.ForgedUIUser
import grails.test.mixin.*
import org.junit.*

/**
 * See the API for {@link grails.test.mixin.services.ServiceUnitTestMixin} for usage instructions
 */
@TestFor(LicensingService)
class LicensingServiceTests {
		

    void testSomething() {
        
		
		
		//def user=ForgedUIUser.findByEmail("admin@forgedui.com")
		
		def user = new ForgedUIUser(id:1);
	
		LicensingService licensingService = new LicensingService();
			
		String code = licensingService.getLicensingCode(user);
		print code
    }
}
