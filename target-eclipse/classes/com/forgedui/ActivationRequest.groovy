package com.forgedui

import java.util.Date;

class ActivationRequest {
	
	ForgedUIUser user;
	
	String ipAddress;	
	String osname;
	String osarch;
	String osuser;
	String osversion;
	Date dateCreated=new Date();
	Date lastUpdated=new Date();
	
	

    static constraints = {
    }
}
