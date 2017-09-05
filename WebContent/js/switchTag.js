var currentTag = 1;
function fivetag(obj,id){
	selectMenu(obj);
	for (var i =1,j; j=document.getElementById("tag_content_"+i); i++){
		j.style.display="none";
	}
	document.getElementById("tag_content_"+id).style.display="block";
	currentTag++;
	if (currentTag>2)
	currentTag=1;
}
function selectMenu(obj){
	var lia = document.getElementById("menulist").getElementsByTagName("li");
	var lialen = lia.length;
	for(i=0; i<lialen; i++){
		if(lia[i].getElementsByTagName("a")[0].className=="curMenu")
		lia[i].getElementsByTagName("a")[0].className = "";
	}
	obj.className = "curMenu";
}
function init(){
	var menulist = document.getElementById("menulist");
	//setInterval('fivetag(menulist.getElementsByTagName("li")[currentTag-1].getElementsByTagName("a")[0],currentTag)',1000);
}

