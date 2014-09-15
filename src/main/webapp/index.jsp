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
    		var newLine = document.createElement("a");
    		newLine.href=this.responseText;
    		newLine.innerText=this.responseText;
    		newLine.target="_blank";
    		linkList.appendChild(newLine);
    		
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
border: 1px solid;
width: 400px; 
background-color: red;
}

.myList a{
display: block;
}
</style>

<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Creating a new url shortener</title>
</head>
<body onload="init();">
	Add a url <input type="text" id="urlInput">
	<input id="createUrlButton"  type="submit" value="createshort" >
	<div id="linksList" class="myList"> </div>
</body>
</html>