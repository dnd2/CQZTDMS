var lastX;
var lastY;
var dragobj ;
function drag(){
if(event.button==1 && dragobj){
dragobj.scrollLeft -=(event.clientX-lastX);
dragobj.scrollTop -=(event.clientY-lastY);
lastX=event.clientX;
lastY=event.clientY;
return false;
}
}
function dblClick(e)
{
if(e.candrag == 'true')
{
e.candrag = 'false';
e.style.cursor='default';
}
else
{
e.candrag = 'true';
e.style.cursor ='hand';
}
}
function grab(e){
if(e.candrag=='true')
{
dragobj = e;
document.onmousemove = drag;
document.onmouseup = drop;
lastX=event.clientX;
lastY=event.clientY;
}
}
function drop(){
dragobj = null;
document.onmousemove = null;
document.onmouseup = null;
}
