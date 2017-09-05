
/**
 * <p>Title: InfoFrame3.0.Cc.V01</p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2010</p>
 *
 * <p>Company: www.infoservice.com.cn</p>
 * <p>Date: 2010-1-29</p>
 *
 * @author andy 
 * @mail   andy.ten@tom.com
 * @version 1.0
 * @remark 页面默认引用js
 */
 

/*********全局变量定义区***********/

var g_webAppName = "/CQZTDMS";   //全局webcontent应用名，避免js使用"<%=request.getContextPath()%>"
var g_webAppImagePath = "/CQZTDMS/images";   //全局图片路径
var rowsPerPage = 10; //默认查询返回结果行数
var g_float = null;

/******** end ******************/

/**
 * added by andy.ten@tom.com 
 * 页面初始化
 */
if (typeof(window.attachEvent) != "undefined")
{
    window.attachEvent("onload", doLoadPage);
    window.attachEvent("onunload", doClose);
}else
{
    if (window.onload != null)
    {
        var oldOnload = window.onload;
        window.onload = function (e) 
        {
            doLoadPage();
            oldOnload(e);
        };
    }
    else
    {
        window.onload = doLoadPage;
    }

    if (window.onunload != null)
    {
        var oldOnunload = window.onunload;
        window.onunload = function (e) 
        {
        	oldOnunload(e);
            doClosePage();
        };
    }
    else
    {
        window.onunload = doClosePage;
    }
}

/**
 * added by andy.ten@tom.com 
 * onload初始化方法，页面覆盖doInit()方法
 */
function doLoadPage()
{
	try 
	{
		//g_float = new FloatWin();
	    setMustStyle();
	    setBtnStyle();
	    //setFixArea();
	    //校验输入域提示框
	    createTip();
		addListener();
        doInit();
    }
	
    catch (e)
    {}
}

/**
 * 校验提示框
 */
var validateConfig = new Object(
{
	divCount : 1,
	isOnBlur : true,
	timeOut : 3000
});
var validateobjarr = new Array();
var tipid = "checkMsgDiv";

function createTip() 
{
	var count = validateConfig.divCount;
	for(var n=0; n<count; n++) 
	{
		createTipDiv(tipid+""+n);
		validateobjarr.push(tipid+""+n);
	}
}

function createTipDiv(did) 
{
	var tip=document.createElement("div"); 
	tip.setAttribute("id",did); 
	tip.style.visibility = "hidden";
	tip.className = "tipdiv";
	
	var nt = "";
	nt += "  <table width=\"120\" height=\"28\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\"  id=\"checkMsgTable\">";
	  nt += "    <tr> ";
	  nt += "      <td  valign=\"bottom\"><img src="+g_webAppName+"/js/validate/alert_top.gif width=\"120\" height=\"6\"></td>";  
	  nt += "    </tr>";
	  nt += "    <tr> ";
	  nt += "      <td  valign=\"top\">";   
	  nt += "          <table width=\"120\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\"  id=\"checkMsgTable\" style=\"font:9pt 宋体;\" >";
	  nt += "            <tr> "; 
	  //nt += "                <td  width=\"8\" valign=\"bottom\"></td>";
	  nt += "                <td width=\"136\" valign=\"top\" id=\""+did+"_msg\" background="+g_webAppName+"/js/validate/alert_middle.gif  align=\"center\" style=\"font:9pt 宋体;\">test </td>";
	  //nt += "                <td  width=\"8\" valign=\"bottom\"></td>";  
	  nt += "            </tr>";  
	  nt += "          </table>";
	  nt += "      </td>"; 
	  nt += "    </tr>";
	  nt += "    <tr>"; 
	  nt += "      <td height=\"10\" valign=\"top\"><img src="+g_webAppName+"/js/validate/alert_bottom.gif width=\"120\" ></td>";
	  nt += "    </tr>";
	  nt += "  </table>";
  	tip.innerHTML = nt;
  	//alert(tip.innerHTML);
	document.body.appendChild(tip); 
}

/**
 * added by andy.ten@tom.com 
 * 设置页面input框必填项样式
 */
function setMustStyle(arrInputs)
{
    if(arrInputs && typeof(arrInputs) == "object" && arrInputs.length > 0)
    {
    	for(var n=0;n<arrInputs.length;n++)
    	{
    		if(!arrInputs[n].datatype) continue;
			var dateType = arrInputs[n].datatype;
			var must_s = false;
			var arr = dateType.split(",");
			if(arr && arr.length > 0)
			{
				if(arr[0] == 0)
					must_s = true;
			    //设置input框的maxlength属性
			    if(arr[2])
			    {
			    	var maxLen = arr[2];
			    	try
			    	{
			    		arrInputs[n].setAttribute("maxlength",maxLen);
			    	}catch(e)
			    	{}
			    }	
			}
			//屏蔽hidden域
			if(arrInputs[n].type != "hidden" && must_s == true)
			{
				var s = document.createElement("SPAN");
				with(s.style)
				{
					fontSize = "9pt";
                    color = "red";
                    width = 7;
                    paddingLeft = "2px";
                    height = "18px";
				}
				s.innerText = "*";
				//modify 2010-06-03 start
				arrInputs[n].parentElement.insertBefore(s,arrInputs[n].nextSibling);
				//modify 2010-06-03 end
			}
    	}
    }else
    {
		//设置input输入框must=true样式
		var inputs = document.getElementsByTagName("INPUT");
		if(inputs && inputs.length)
		{
			for(var i=0;i<inputs.length;i++)
			{
				if(!inputs[i].datatype) continue;
				var dateType = inputs[i].datatype;
				var must_s = false;
				var arr = dateType.split(",");
				if(arr && arr.length > 0)
				{
					if(arr[0] == 0)
						must_s = true;
				    //设置input框的maxlength属性
				    if(arr[2])
				    {
				    	var maxLen = arr[2];
				    	try
				    	{
				    		inputs[i].setAttribute("maxlength",maxLen);
				    	}catch(e)
				    	{}
				    }	
				}
				//屏蔽hidden域
				if(inputs[i].type != "hidden" && must_s == true)
				{
					var s = document.createElement("SPAN");
					with(s.style)
					{
						fontSize = "9pt";
	                    color = "red";
	                    width = 7;
	                    paddingLeft = "2px";
	                    height = "18px";
					}
					s.innerText = "*";
					//modify  2010-06-03 start
					inputs[i].parentElement.insertBefore(s,inputs[i].nextSibling);
					//modify  2010-06-03 end
				}
			}
		}
		//设置textarea输入框must=true样式
		var textareas = document.getElementsByTagName("TEXTAREA");
		if(textareas && textareas.length)
		{
			for(var i=0;i<textareas.length;i++)
			{
				
				if(!textareas[i].datatype) continue;
				var dateType = textareas[i].datatype;
				var must_s = false;
				var arr = dateType.split(",");
				if(arr && arr.length > 0)
				{
					if(arr[0] == 0)
						must_s = true;
				}
				//屏蔽hidden域
				if(textareas[i].style.display != "none" && must_s == true)
				{
					var s = document.createElement("SPAN");
					with(s.style)
					{
						fontSize = "9pt";
	                    color = "red";
	                    width = 7;
	                    paddingLeft = "2px";
	                    height = "18px";
					}
					s.innerText = "*";
					textareas[i].parentElement.appendChild(s);
				}
			}
		}
		//设置select框must=true样式
		var selects = document.getElementsByTagName("SELECT");
		if(selects && selects.length)
		{
			for(var i=0;i<selects.length;i++)
			{
				
				if(!selects[i].datatype) continue;
				var dateType = selects[i].datatype;
				var must_s = false;
				var arr = dateType.split(",");
				if(arr && arr.length > 0)
				{
					if(arr[0] == 0)
						must_s = true;
				}
				//屏蔽hidden域
				if(selects[i].style.display != "none" && must_s == true)
				{
					var s = document.createElement("SPAN");
					with(s.style)
					{
						fontSize = "9pt";
	                    color = "red";
	                    width = 7;
	                    paddingLeft = "2px";
	                    height = "18px";
					}
					s.innerText = "*";
					selects[i].parentElement.appendChild(s);
				}
			}
		}	
	}	
}

/**
 * added by andy.ten@tom.com 
 * 设置页面input框必填项样式
 */
function setBtnStyle(arrInputs)
{
    //设置input输入框hasbtn=true样式
    if(arrInputs && typeof(arrInputs) == "object" && arrInputs.length > 0)
    {
    	for(var n=0;n<arrInputs.length;n++)
    	{
    	    var imgObj=document.createElement("IMG");
		    imgObj.src = g_webAppImagePath+"/default/btn.gif";
	   		imgObj.id  = "srtImg";
		    imgObj.width = 15;
		    imgObj.height = 15;
		    imgObj.title = "点击按钮";
	        var btn = document.createElement("SPAN");
	        btn.appendChild(imgObj);
		    if(arrInputs[n].callFunction)
		    {
	      		btn.onclick=new Function(arrInputs[n].callFunction);
	    	}
		    /**
		     * 对于多个input按钮添加的位置问题修改
		     */
		    //inputs[i].parentElement.appendChild(btn);
		    arrInputs[n].parentElement.insertBefore(btn,arrInputs[n].nextSibling);
    	}
    }else
    {
		var inputs = document.getElementsByTagName("INPUT");
		if(inputs && inputs.length)
		{
			for(var i=0;i<inputs.length;i++)
			{
				//屏蔽hidden域
				if(inputs[i].type != "hidden" && inputs[i].hasbtn == "true")
				{
					var imgObj=document.createElement("IMG");
				    imgObj.src = g_webAppImagePath+"/default/btn.gif";
		    		imgObj.id  = "srtImg";
				    imgObj.width = 15;
				    imgObj.height = 15;
				    imgObj.title = "点击按钮";
	                var btn = document.createElement("SPAN");
	                btn.appendChild(imgObj);
				    if(inputs[i].callFunction)
				    {
			      		btn.onclick=new Function(inputs[i].callFunction);
			    	}
			    	
				    /**
				     * 对于多个input按钮添加的位置问题修改
				     */
				    //inputs[i].parentElement.appendChild(btn);
				    inputs[i].parentElement.insertBefore(btn,inputs[i].nextSibling);
				}
				
			}
		}
	}			
}

/**
 * 设置页面固定区域值
 * 固定区域段：<div id="fixarea"></div>
 * 查询条件固定区域：<input type="text" id="querycondition" name="querycondition">
 */
function setFixArea()
{
	var div = document.createElement("DIV");
	div.id = "fixarea";
	div.name = "fixarea";
	div.style.display = "none";
	document.body.appendChild(div);
	
    var inputCond = document.createElement("INPUT");
    inputCond.id = "querycondition";
    inputCond.name = "querycondition";
    var inputAction = document.createElement("INPUT");
    inputAction.id = "queryaction";
    inputAction.name = "queryaction";
    div.appendChild(inputCond);
    div.appendChild(inputAction);
    
}
//onunload方法，页面覆盖doClose()方法
function doClosePage()
{
	try 
	{
        doClose();
        //window.close();
    }
    catch (e)
    {}
}
function doInit()
{}
function doClose()
{}

/**
 * added by andy.ten@tom.com 
 * 设置页面折叠框
 */
function onTitleClick(id)
{
//	alert(window.event.srcElement.innerHTML);
	var display = document.getElementById(id).style.display;
	if (display == "none")
	{
		document.getElementById(id).style.display = "";
		window.event.srcElement.src = g_webAppImagePath + "/default/expand.gif";
	}
	else if (display == "")
	{
		document.getElementById(id).style.display = "none";
		window.event.srcElement.src = g_webAppImagePath + "/default/collapse.gif";
	}
}


/**
  *FORM表单提交方法
  *add by zhaojinyu 2010-02-01
  *VERSION:1.0
  *formId:FORMID
  *method：方法名 
  */
function doFormSubmit(formId ,method)
{
	  if(!formId)
	  {
	     alert("FormId不能为空！");
	     return false;
	  }
	  if(!method)
	  {
	     alert("method不能为空！");
	     return false;
	  }
	  
	  //form表单提交前回调方法
	  doBeforeSubmit(formId);
	  if(submitForm(formId))
	  {
	  	 document.getElementById(formId).method = method;
	  	 document.getElementById(formId).submit();
	  }
	  //form表单提交后回调方法
	  doAfterSubmit(formId);
	  
}


 /**
  *FORM表单提交之前的方法
  *add by zhaojinyu 2010-02-01
  *VERSION:1.0
  *formId:FORMID
  */
function doBeforeSubmit(formId)
{
  
}
 /**
  *FORM表单提交之后的方法
  *add by zhaojinyu 2010-02-01
  *VERSION:1.0
  *formId:FORMID
  */
function doAfterSubmit(formId)
{

} 

