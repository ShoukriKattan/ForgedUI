<html>
<head>
<title>ForgedUI - Product Download</title>
<meta name="layout" content="main" />
<style type="text/css" media="screen">
</style>
</head>
<body>

<script type="text/javascript">

$(document).ready(function(){

	var installInstShowing=false;
	var activationInstShowing=false;
	
	$('#detailed_install_instructions_toggle').click(function() {

		installInstShowing=!installInstShowing;

		if(installInstShowing){
			$("#detailed_install_instructions_toggle").html("hide");			
			}else{
				$("#detailed_install_instructions_toggle").html("show");
			}		
		
		$("#detailed_install_instructions").toggle();
		return false;
	})

	$('#detailed_activation_instructions_toggle').click(function() {

		activationInstShowing=!activationInstShowing;

		if(activationInstShowing){
			$('#detailed_activation_instructions_toggle').html("hide");		
			}else{
				$('#detailed_activation_instructions_toggle').html("show");
				}

		$("#detailed_activation_instructions").toggle();


			return false;
	})

	$('.copyActivation').zclip({
        path:'${resource(dir:'js',file:'ZeroClipboard.swf')}',
        copy:'${code}',
        afterCopy:function(){

        		alert("Activation code has been copied to your clipboard");
           }
    });
	
	$('.copyUpdateSiteURL').zclip({
        path:'${resource(dir:'js',file:'ZeroClipboard.swf')}',
        copy:'${downloadlink}',
        afterCopy:function(){

        		alert("Update Site URL has been copied to your clipboard");
           }
    });

	
	
});
</script>

<div>

<div id="installing">

<h3>Installing ForgedUI</h3>

<h3>For the Impatient</h3>
<p>
Download and install ForgedUI plugin from the following URL : <br/>

<textarea rows="1" cols="50">${downloadlink}</textarea>
 
<a href="#" class="copyUpdateSiteURL">Copy to clipboard</a> 
</p>

<h3>Detailed Instructions <a href="#" id="detailed_install_instructions_toggle" class="">
Show
</a></h3>

<div id="detailed_install_instructions" style="display: none">
<ol class="dep_list">
					
						<li><p>
						Open and login to your "Titanium Studio", click the "Help" menu on top and click "Install New Software"	<br/><br/>					
						<img src="${resource(dir:'images/beta_installation_screenshots',file:'Help_install_new_software.png')}" class="install_instructions_img" style="height: 278px; width: 612px;" />						 						
						</p></li>
						<li><p>						
						A new window pops up asking you to either choose an available update site or add a new one. <br/>
						Click the "Add" button then in the new Window Type "ForgedUI Updates" as the Repository name. <br/>
						
						Use the following URL as the update site URL the click 'OK'<br/><br/>	
						<textarea rows="1" cols="50">${downloadlink}</textarea>
 						<a href="#" class="copyUpdateSiteURL">Copy to clipboard</a>
 						
 						<br/>

						 
						
						<img src="${resource(dir:'images/beta_installation_screenshots',file:'new_update_site_name.png')}" class="install_instructions_img" style="height: 161px; width: 406px;" />
						</p>
						</li>
						<li>
						<p>
						Select both "Dependencies" and "ForgedUI Editor" groups , or select the 3 individual plugins "ForgedUIEditor", <br/>
						"Graphical Editing Framework GEF", as well as "Graphical Editing Framework Draw2D" then click next. <br/><br/> 
						<img src="${resource(dir:'images/beta_installation_screenshots',file:'beta_2_install_select_all.png')}" class="install_instructions_img" style="height: 572px; width: 660px;" />
						</p>						
						</li>
																		
						<li><p>
						A new screen appears asking you to confirm installation details, simply click "Next" <br/><br/>
						<img src="${resource(dir:'images/beta_installation_screenshots',file:'confirm_install_all.png')}" class="install_instructions_img" style="height: 573px; width:660px;" />						
						</p></li>
						
						<li><p>
						A new screen appears asking you to accept / reject licenses. Accept the license agrement and click "Finish" <br/><br/> 
						<img src="${resource(dir:'images/beta_installation_screenshots',file:'accept_forgedui_license.png')}" class="install_instructions_img" style="height: 381px; width: 631px;" />						
						</p></li>
						
						<li><p>
						Wait for the installation to complete. The progress window will close automatically when installation is completed. <br/><br/>
						<img src="${resource(dir:'images/beta_installation_screenshots',file:'Wait_For_Installation_To_complete.png')}" class="install_instructions_img" style="height: 205px; width: 450px;" />						
						</p></li>
						
						<li><p>
						When installation is completed, a new Window appears asking you Whether you need to restart to apply changes,<br/>
						 apply changes without restarting, or an option to defer changes and to do that later. 
						Choose "Restart Now"<br/><br/>
						<img src="${resource(dir:'images/beta_installation_screenshots',file:'restart_now_after.png')}" class="install_instructions_img" style="height: 136px; width: 440px;" />						
						</p></li>
					
						<li>You are now ready to start using ForgedUI editor</li>					
					



</ol>
</div>


</div>

<div id="activating">
<h3>Activating ForgedUI</h3>

<h3>For the impatient</h3>
<div>

Use activation code <br/>

<textarea rows="1" cols="50">${code}</textarea><a href="#" class="copyActivation">Copy to clipboard</a>

</div>

<h3>Detailed activation instructions <a href="#" id="detailed_activation_instructions_toggle">Show</a></h3>



<div id="detailed_activation_instructions" style="display: none;">
<ol>

<li>
<p>
Go to Titanium studio preferences by clicking "Window" then choosing "Preferences" on Windows,<br/> or by clicking "Titanium Studio" and then "Preferences" on Mac. <br/><br/>

<img src="${resource(dir:'images/beta_installation_screenshots',file:'ForgedUI_prefs.png')}" class="install_instructions_img" style="height: 311px; width: 555px;" />

</p>
</li>

<li>
<p>
From the left side tree pick "ForgedUI" then choose "Editor License". <br/><br/>

<img src="${resource(dir:'images/beta_installation_screenshots',file:'ForgedUI_prefs_Licenses.png')}" class="install_instructions_img" style="height: 529px; width: 623px;" />

</p>
</li>


<li>Copy and paste the following activation code then click on activate  <br/><br/> 
<textarea rows="1" cols="50">${code}</textarea><a href="#" class="copyActivation">Copy to clipboard</a>
</li>

<li>You will get a message indicating that your activation has been successful. </li>


</ol>
</div>
</div>
 


</div>

</body>
</html>