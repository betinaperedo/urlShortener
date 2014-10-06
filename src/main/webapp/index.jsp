<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>

<script type="text/javascript">
  function init() {
	  
	var url = document.getElementById("urlInput");
	var button = document.getElementById("createUrlButton");
	var linkList=document.getElementById("linksList");
	
	var oReqList = new XMLHttpRequest();
	oReqList.onload  = function(){		 
		 var urlArr = JSON.parse(oReqList.responseText);		
		 createUrlList(urlArr);
	}
	oReqList.open("get", "/urlshortener/list", true);
	oReqList.send();
	
	function createUrlList(arr) {	    	    
	    for(var i = 0; i < arr.length; i++) {	
	    	var shorturl =arr[i].shortUrl;
	    	createComponet(shorturl) ;	    	
    	}
	    
	}
	
	function createComponet(shorturl) {		
		shorturlocation =document.location.pathname+shorturl;
		var di =document.createElement("div");
		di.className="divClass"      		
		var img =document.createElement("img");
		img.src="/urlshortener/img/delete.png"
		img.id="image";
		img.className="imgClass"; 
		var newLine = document.createElement("a");
		newLine.href=shorturlocation;
		newLine.innerText=shorturlocation;
		newLine.target="_blank";
		newLine.className="inputClass"; 
		
		di.appendChild(img);
		di.appendChild(newLine);
		linkList.appendChild(di);
		url.value="";
		img.onclick  = function()  { eliminar(di,shorturl) }; 
	}
	
	
	
	function eliminar(di,str){
		var oReqEl = new XMLHttpRequest();
    	oReqEl.onload = function(){
    		linkList.removeChild(di);
    	}
    	
    	oReqEl.open("post", "/urlshortener/remove?urlinput="+str, true);
    	oReqEl.send();
	}
	
	
    button.onclick = function() { 
    	
    	var oReq = new XMLHttpRequest();
    	oReq.onload = function(){
    		createComponet(this.responseText) ;	      		    		
    	}
    	oReq.open("post", "/urlshortener/create?urlinput="+encodeURIComponent(url.value), true);
    	oReq.send();
    
      console.log(url.value);
	};  
  }
</script>
<style type="text/css">

.myList{
width: 400px; 
}



.mybutton {
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

.divClass {
position: relative;
top: 20px;
left: 65px;
}

.imgClass {
position: relative;
top: 6px;
cursor: pointer;
}


.inputClass{
position: relative;
left: 30px;
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