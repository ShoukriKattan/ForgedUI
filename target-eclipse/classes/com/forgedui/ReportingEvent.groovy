package com.forgedui

class ReportingEvent {

	ForgedUIUser user;
	
	Date date;
	
	String type;
	
	String data;
	
	static mapping = { data type: 'text' }
	
    static constraints = {
		
    }
}
