package com.forgedui

class ForgedUIProduct {

	String name;
	String productId;
	String category;
	String description;	
	String pvtDescription;
	
	int numUsers;
		
	Date dateCreated = new Date();
	Date lastUpdated =new Date();
		
	
	
    static constraints = {    
	name(unique:true)
	productId(unique:true)
	}
}