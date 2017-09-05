function FixWidth(selectObj)
{
    var newSelectObj = document.createElement("select");
    newSelectObj = selectObj.cloneNode(true);
    newSelectObj.selectedIndex = selectObj.selectedIndex;
    newSelectObj.id="newSelectObj";
  
    var e = selectObj;
    var absTop = e.offsetTop;
    var absLeft = e.offsetLeft;
    while(e = e.offsetParent)
    {
        absTop += e.offsetTop;
        absLeft += e.offsetLeft;
    }
    with (newSelectObj.style)
    {
        position = "absolute";
        top = absTop + "px";
        left = absLeft + "px";
        width = "auto";
    }
   
    var rollback = function(){ RollbackWidth(selectObj, newSelectObj); };
    if(window.addEventListener)
    {
        newSelectObj.addEventListener("blur", rollback, false);
        newSelectObj.addEventListener("change", rollback, false);
       // newSelectObj.addEventListener("click", rollback, false);
    }
    else
    {
        newSelectObj.attachEvent("onblur", rollback);
        newSelectObj.attachEvent("onchange", rollback);
       // newSelectObj.attachEvent("onclick", rollback);
    }
   
    selectObj.style.visibility = "hidden";
    document.body.appendChild(newSelectObj);
   
    var newDiv = document.createElement("div");
    with (newDiv.style)
    {
        position = "absolute";
        top = (absTop-10) + "px";
        left = (absLeft-10) + "px";
        width = newSelectObj.offsetWidth+20;
        height= newSelectObj.offsetHeight+20;;
        background = "transparent";
        //background = "green";
    }
    document.body.appendChild(newDiv);
    newSelectObj.focus();
    var enterSel="false";
    var enter = function(){enterSel=enterSelect();};
    newSelectObj.onmouseover = enter;
   
    var leavDiv="false";
    var leave = function(){leavDiv=leaveNewDiv(selectObj, newSelectObj,newDiv,enterSel);};
    newDiv.onmouseleave = leave;
}

function RollbackWidth(selectObj, newSelectObj)
{
    selectObj.selectedIndex = newSelectObj.selectedIndex;
    selectObj.style.visibility = "visible";
    if(document.getElementById("newSelectObj") != null){
       document.body.removeChild(newSelectObj);
    }
}

function removeNewDiv(newDiv)
{
    document.body.removeChild(newDiv);
}

function enterSelect(){
  return "true";
}

function leaveNewDiv(selectObj, newSelectObj,newDiv,enterSel){
 if(enterSel == "true" ){
  RollbackWidth(selectObj, newSelectObj);
  removeNewDiv(newDiv);
 }
}