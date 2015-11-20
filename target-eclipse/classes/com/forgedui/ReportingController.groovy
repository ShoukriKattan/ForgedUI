package com.forgedui

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.bouncycastle.LICENSE;

import com.forgedui.utils.SecurityUtils;

class ReportingController {

	def licensingService

	def index() {

		def user = ForgedUIUser.get("6");
		
		String code = licensingService.getLicensingCode(user);
		
		println (code)
		
		
	}


	def receive(){

		String userId = params.user;
		String fileName=params.fname;
		String receivedToken = params.token;

		// Verify signature and then upload if legal :

		def localTok ="[${userId}][reportingSecretTokenABCD]";

		String local =SecurityUtils.doHash((String)localTok); 
				
		
		if(local.equals(receivedToken)){

			log.info("All is ok .. Hash matches... Copying file "+userId+"_"+fileName);

			File uploadFolder = new File(grailsApplication.config.forgedui.reports.upload.folder.path);

			
			if(userId=='anonymous'){
				def ip = request.getRemoteAddr();								
				
				userId= userId+"_"+ip;				
			}
			
			//create lock file

			File lockFile = new File(uploadFolder,userId+"_"+fileName+".lck");
			lockFile.createNewFile();

			InputStream is =null;
			FileOutputStream fos=null;
			try{
				is =request.inputStream;
				fos = new FileOutputStream(new File(uploadFolder,userId+"_"+fileName));

				IOUtils.copyLarge(is, fos);

				// Done copying delete lock file
				lockFile.delete();
			}finally{
				IOUtils.closeQuietly(is);
				IOUtils.closeQuietly(fos);

			}


			render status: HttpServletResponse.SC_CREATED

		}else{

			render status: HttpServletResponse.SC_BAD_REQUEST
		}




	}

}
