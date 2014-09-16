<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>

<script type="text/javascript">

  function init() {
	var url = document.getElementById("urlInput");
	var button = document.getElementById("createUrlButton");
	var linkList = document.getElementById("linksList");
    button.onclick = function() { 
    	
    	var oReq = new XMLHttpRequest();
    	oReq.onload = function(){
    		var di =document.createElement("div");
    		var but = document.createElement("button");
    		but.value="Eliminar";
    		
    		linkList.appendChild(but);
    		var newLine = document.createElement("a");
    		newLine.href=this.responseText;
    		newLine.innerText=this.responseText;
    		newLine.target="_blank";
    		linkList.appendChild(but);
    		linkList.appendChild(newLine);
    		linkList.appendChild(di);
    		url.value="";
    		but.onClick  = function()  { eliminar(di,this.responseText) }; 
    			
    		
    		
    	}
    	
    	function eliminar(di,str){
    		var oReq = new XMLHttpRequest();
        	oReq.onload = function(){
        		linkList.removeChild(di);
        	}
        	oReq.open("post", "/urlshortener/delete?urlinput="+str, true);
    	}
    	
    	oReq.open("post", "/urlshortener/create?urlinput="+url.value, true);
    	oReq.send();
    	
      // Crear un request AJAX, incluir el parametro url.value
      // En la function que toma el response, crear un <a> y setearle
      // el href.
      // Despues agregar el <a> como hijo del body.
    	
      console.log(url.value);
	};  
  }
</script>
<style type="text/css">

.myList{

width: 400px; 
background-color: red;
}

.myList a{
display: block;
}

.mybutton{
cursor: default!important;
display: inline-block;
font-weight: bold;
height: 29px;
line-height: 29px;
min-width: 54px;
padding: 0 8px;
text-align: center;
text-decoration: none!important;
-webkit-border-radius: 2px;
border-radius: 2px;
-webkit-user-select: none;
letter-spacing: normal;
word-spacing: normal;
text-transform: none;
text-indent: 0px;
text-shadow: none;
border: 1px solid gainsboro;
border-color: rgba(0, 0, 0, 0.1);
color: #444!important;
font-size: 11px;
}

.mybuttonDelete {
cursor: default!important;
display: inline-block;
font-weight: bold;
text-align: center;
text-decoration: none!important;
-webkit-border-radius: 2px;
border-radius: 2px;
-webkit-user-select: none;
letter-spacing: normal;
word-spacing: normal;
text-transform: none;
text-indent: 0px;
text-shadow: none;
border: 1px solid gainsboro;
border-color: rgba(0, 0, 0, 0.1);
color: #444!important;
font-size: 11px;
}


</style>

<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Creating a new url shortener</title>
</head>
<body onload="init();">
	Add a url <input type="text" id="urlInput" style="color:#777;font-size:15px;font-weight:bold;">
	<input id="createUrlButton"  type="submit" value="createshort" class="mybutton" >
	<div id="linksList" class="myList"> </div>
</body>
</html>