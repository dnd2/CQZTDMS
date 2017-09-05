var activemenu = null;
function JatoolsMenu(owner,menu)
{
this.owner = owner;
this.menu = menu;
this.menu.style.position='absolute'
this.menuClick = function (event) 
{
if(activemenu != null && event.srcElement != activemenu.owner)
{
activemenu.menu.style.display = 'none';
activemenu = null;
}
}
document.attachEvent("onclick",this.menuClick); 
this.show = function ()
{
var x = this.owner.offsetLeft;
var y = this.owner.offsetTop+this.owner.offsetHeight ;
this.menu.style.top=y+"px";
this.menu.style.left=x+"px";
this.menu.style.display = 'block';
activemenu = this;
}
}
