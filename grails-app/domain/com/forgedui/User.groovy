package forgedui

import java.util.Date;

class User {

	String name;
	String email;
	String phone;
	String country;
	String company;

	// Experience using Titanium 1-5
	String tiExperience="";

	// All 1 to 4 :

	String iPhoneDevExp="";
	String iPadDevExp="";
	String androidMobiDevExp="";
	String androidTabletDevExp="";

	String iosObjCExp="";
	String androidSDKExp="";
	String androidNDKExp="";

	Date dateCreated = new Date();
	
    Date lastUpdated = new Date();

	static constraints = {

		name(size:5..100, blank:false, unique:true)
		email(email:true, blank:false,unique:true)
		phone(matches: /^[0-9]{0,14}$/,blank:false)
		country(blank:false)
	}
}
