package com.forgedui



class ForgedUICustomerProduct {

	static final String CUSTOMER_PRODUCT_STATUS_ACTIVE="active";
	static final String CUSTOMER_PRODUCT_STATUS_CANCELLED="cancelled";
	static final String CUSTOMER_PRODUCT_STATUS_DEACTIVATED="deactivated";
	
	ForgedUICustomer customer;
	
	ForgedUIProduct product;
			
	String state;
	
	Date dateCreated=new Date();
	Date lastUpdated=new Date();
	Date datePurchased;
	
	
    static constraints = {
    }
}