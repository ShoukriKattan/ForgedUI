import java.util.Date;



import com.forgedui.ForgedUICustomer;
import com.forgedui.ForgedUIProduct;
import com.forgedui.SecRole;

class BootStrap {

	
    def init = { servletContext ->
		
						
		
		def productsList = ForgedUIProduct.list();
		int productListSize=productsList.size();

		def userRole = SecRole.findByAuthority('ROLE_USER') ?: new SecRole(authority: 'ROLE_USER').save(failOnError: true)
		
		if(productListSize==0){
			
			// Add the freaking role :
			
			

			ForgedUIProduct freeProduct = new ForgedUIProduct(
					name:"ForgedUI Free Edition",
					productId:"single_usr_trial",
					category:"trial",
					description:"",
					pvtDescription:"",
					numUsers:1
					);
				
				
				ForgedUIProduct standard = new ForgedUIProduct(
					name:"ForgedUI Standard Edition",
					productId:"single_usr_std",
					category:"standard",
					description:"",
					pvtDescription:"",
					numUsers:1
					);
				
				ForgedUIProduct multi = new ForgedUIProduct(
					name:"ForgedUI Developer 10 pack Edition",
					productId:"ten_pack_std",
					category:"standard",
					description:"",
					pvtDescription:"",
					numUsers:10
					);
				
				freeProduct.save(failOnError:true);
				standard.save(failOnError:true);
				multi.save(failOnError:true);
				
		}
		
    }
    def destroy = {
    }
}
