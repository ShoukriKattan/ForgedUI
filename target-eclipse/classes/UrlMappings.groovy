class UrlMappings {

	static mappings = {
		
		"/login/auth" {
			controller = 'openId'
			action = 'auth'
		 }
		 "/login/openIdCreateAccount" {
			controller = 'openId'
			action = 'createAccount'
		 }
		
		"/$controller/$action?/$id?"{
			constraints {
				// apply constraints here
			}
		}

		name index: "/"(view:"/index")
		name betaTos:"/beta-tos"(view:"/beta_tos")		
		name ppolicy:"/privacy-policy"(view:"/privacy_policy")
		name contact:"/contact"(view:"/contact")
		name beta_install_instructions:"/beta-installation-instructions"(view:"/beta_installation_instructions")
		"500"(view:'/error')
	}
}
