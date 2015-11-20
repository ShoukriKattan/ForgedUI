package com.forgedui.openid

import com.forgedui.SecUser

class OpenID {

	String url

	static belongsTo = [user: SecUser]

	static constraints = {
		url unique: true
	}
}
