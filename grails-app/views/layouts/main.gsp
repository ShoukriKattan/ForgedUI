<!doctype html>
<!--[if lt IE 7 ]> <html lang="en" class="no-js ie6"> <![endif]-->
<!--[if IE 7 ]>    <html lang="en" class="no-js ie7"> <![endif]-->
<!--[if IE 8 ]>    <html lang="en" class="no-js ie8"> <![endif]-->
<!--[if IE 9 ]>    <html lang="en" class="no-js ie9"> <![endif]-->
<!--[if (gt IE 9)|!(IE)]><!--> <html lang="en" class="no-js"><!--<![endif]-->
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
		<title><g:layoutTitle default="Grails"/></title>
		<meta name="viewport" content="width=device-width, initial-scale=1.0">
		<link rel="shortcut icon" href="${resource(dir: 'images', file: 'favicon.ico')}" type="image/x-icon">
		<link rel="apple-touch-icon" href="${resource(dir: 'images', file: 'apple-touch-icon.png')}">
		<link rel="apple-touch-icon" sizes="114x114" href="${resource(dir: 'images', file: 'apple-touch-icon-retina.png')}">
		<link rel="stylesheet" href="${resource(dir: 'css', file: 'style.css')}" type="text/css">
					
		<g:javascript src="jquery-1.6.1.js"></g:javascript>
		<g:javascript src="jquery.cycle.all.js"></g:javascript>
		<g:javascript src="jquery.easing.js"></g:javascript>
		<g:javascript src="jquery.tipsy.js"></g:javascript>
		<g:javascript src="jquery.prettyPhoto.js"></g:javascript>
		<g:javascript src="jquery-ui-1.8.16.custom.min.js"></g:javascript>
		<g:javascript src="jquery.lightbox-0.5.js"></g:javascript>
		<g:javascript src="jquery.zclip.js"></g:javascript>
		
		
		
		<g:layoutHead/>
        <r:layoutResources />
	</head>
	<body>
	
	<div id="mainWrapper">
	<!-- BEGIN WRAPPER -->
    <div id="wrapper">
		<!-- BEGIN HEADER -->
        <div id="header">
            <div id="logo"><a href="http://www.forgedui.com"><img src="${resource(dir: 'images', file: 'ForgedUI.png')}" alt="ForgedUI" /></a></div>
			<!-- BEGIN MAIN MENU 
			<?php if ( function_exists( 'wp_nav_menu' ) ){
					wp_nav_menu( array( 'theme_location' => 'main-menu', 'container_id' => 'mainMenu', 'container_class' => 'ddsmoothmenu', 'fallback_cb'=>'primarymenu') );
				}else{
					primarymenu();
			}?>
			-->
            <!-- END MAIN MENU -->
			<!-- BEGIN TOP SEARCH -->
			<div id="topSearch">
				<form id="searchform" action="<?php bloginfo('url'); ?>/" method="get">
					<input type="submit" value="" id="searchsubmit"/>
					<input type="text" id="s" name="s" value="type your search" />
				</form>
			</div>
			<!-- END TOP SEARCH -->
			<!-- BEGIN TOP SOCIAL LINKS -->
			<div id="topSocial">
			
				<ul>					
					<li><a href="http://www.twitter.com/ForgedUI" class="twitter" title="Follow Us on Twitter!"><img src="${resource(dir: 'images', file: 'boldy/images/ico_twitter.png')}" alt="Follow Us on Twitter!" /></a></li>					
					<li><a href="http://facebook.com/ForgedUI" class="twitter" title="Join Us on Facebook!"><img src="${resource(dir: 'images', file: 'boldy/images/ico_facebook.png')}" alt="Join Us on Facebook!" /></a></li>										
				</ul>
				 
			</div>	
			<!-- END TOP SOCIAL LINKS -->
        </div>
        <!-- END HEADER -->
		
		<!-- BEGIN CONTENT -->
		<div id="content">
			<g:layoutBody/>
		</div>
		   <!-- END CONTENT -->
	</div>
    <!-- END WRAPPER -->
	
	<!-- BEGIN FOOTER -->
	<div id="footer">
		<div id="footerWidgets">
			<div id="footerWidgetsInner">
				<!-- BEGIN FOOTER WIDGET -->
				
				<!-- END FOOTER WIDGETS -->
				<!-- BEGIN COPYRIGHT -->
				<div id="copyright">
						All Copyrights reserved BSP Softworks 
				</div>
				<!-- END COPYRIGHT -->						
				</div>
				
		</div>
	</div>	
	<!-- END FOOTER -->
</div>
	
	
		<!--  	
		<div id="grailsLogo" role="banner"><a href="http://grails.org"><img src="${resource(dir: 'images', file: 'grails_logo.png')}" alt="Grails"/></a></div>
		
		<div class="footer" role="contentinfo"></div>
		<div id="spinner" class="spinner" style="display:none;"><g:message code="spinner.alt" default="Loading&hellip;"/></div>
		<g:javascript library="application"/>
		
        <r:layoutResources />
        -->
	</body>
</html>