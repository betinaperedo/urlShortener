<!DOCTYPE html>
<html>
    <head>
       	<meta charset="utf-8">
	    <title>Sign in </title>
	    <meta name="viewport" content="width=device-width, initial-scale=1.0">
	    <meta name="description" content="">
	    <meta name="author" content="">	         	
    	<link href="static/urlShortener.css" rel="stylesheet"  TYPE="text/css"></link>
    	<script type="text/javascript">
  			function init() {
  				
  				var signInButton = document.getElementById("singnin");  				
  				signInButton.onclick  = function()  { singIn() }; 
  				var email = document.getElementById("email");
	  			var pass = document.getElementById("password");
  				function singIn(){
  					
  					var oReqSingIn = new XMLHttpRequest();
  					oReqSingIn.onload = function(){
  						var isError =oReqSingIn.responseText;
  						if(isError=="1"){
  							document.location.href="http://localhost:8080/urlshortener/index.jsp";
  						}else{
  							var error = document.getElementById("errorMsg");
  	  			    		error.className="form-signin-error";	
  						}
  			    		
  			    	}
  			    	
  					oReqSingIn.open("post", "/urlshortener/signin?email="+email.value+"&password="+pass.value, true);
  					oReqSingIn.send();
  				}
  				
  			}
  		</script>
	</head>
	
    <body onload="init();">
    <div class="form-signin"  >
	
        <h2 class="form-signin-heading">Please sign in</h2>
        <input type="text" class="input-block-level" id="email" placeholder="Email address">
        <input type="password" class="input-block-level" id="password" placeholder="Password">
        <label class="checkbox">
          <input type="checkbox" value="remember-me"> Remember me
        </label>
        <button class="btn btn-large btn-primary" id="singnin">Sign in</button>         
     		<h2 class="invisible" id="errorMsg">
     	     *The username and password you entered did not match our records.</h2>
     	     <a href="/joinus" class="join-us">Join Us</a>
		
    </div> 
  </body>
</html>