function disableselect(e){
	return false;
}
function reEnable(){
	return true;
}
//body 标签中加入下列方法
//oncontextmenu="return false" 
//onselectstart="return false" 
//ondragstart="return false" 
//onbeforecopy="return false"
//oncopy=document.selection.empty() onselect=document.selection.empty()