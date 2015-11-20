package com.forgedui.jobs

import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

import org.apache.commons.io.IOUtils;

import com.forgedui.ForgedUIUser;
import com.forgedui.ReportingEvent;


class LoadReportsJob {

	def grailsApplication;

	static triggers = {                                                                            
		simple name: 'loadReportsJobTrigger', startDelay: 60000, repeatInterval: 3600000, repeatCount :-1
	}

	// This job should not run multiple times concurrently
	def concurrent = false

	def group ="Reporting"

	def reportingService

	def execute() {

		SimpleDateFormat eventDateTimeFormat = new SimpleDateFormat("yyyy-MM-dd_HH_mm_ss");
		eventDateTimeFormat.setTimeZone(TimeZone.getTimeZone("GMT"));

		log.info("Reporting job running")

		File uploadFolder = new File(grailsApplication.config.forgedui.reports.upload.folder.path);

		File[] reportFiles = uploadFolder.listFiles(new FileFilter(){
					boolean accept(File file) {

						String fileName = file.getName();
						//file should be a .csv and there should be no lock file (Which means csv is still being processed)
						if(!fileName.startsWith("anonymous") && fileName.endsWith(".csv") && !new File(uploadFolder,fileName+".lck").exists()){

							return true;
						}

						return false;
					}
				});




		reportFiles.each() { file ->


			String fileName = file.getName();

			String userId=fileName.substring(0,fileName.indexOf('_'));
						

			boolean hasErrors = false

			// For each file ..
			// Process the file :

			FileInputStream fis = null;

			try{

				def user = ForgedUIUser.get(userId);
								
				fis = new  FileInputStream(file);

				def lines = IOUtils.readLines(fis,"UTF-8")

				lines.each(){ line ->

					String[] fields = line.split(",");

					Date eventDate = eventDateTimeFormat.parse(fields[0].replaceAll("\"",""));

					String eventType=fields[1].replaceAll("\"","");

					// Store the full fields too :

					ReportingEvent re=new ReportingEvent( user:user,date:eventDate,type: eventType,data: line);

					re.save();

				}

			}catch(Exception e ){

				hasErrors=true;

			}finally{

				IOUtils.closeQuietly(fis);

			}

			if(!hasErrors){
				// File processed successfully
				// discard

				file.delete();

			}else{

				// Move file somewhere else or rename its extension for us to look at it

				File renameToFile = new File(uploadFolder,fileName+".err")

				file.renameTo(renameToFile);
			}

		}

	}
}
