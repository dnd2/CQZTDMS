<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.exception.BizException" %>
<%@ page import="com.infoservice.mvc.context.ActionContext" %>
<%@ page import="com.infodms.dms.common.ErrorCodeConstant" %>
<%@ page import="com.infodms.dms.common.RightMessageConstant " %>
<%@ page import="com.infodms.dms.common.FileConstant"%>
<%@ page import="java.util.Calendar" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@taglib uri="/jstl/cout" prefix="c"%>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link href="<%=request.getContextPath()%>/style/content.css" rel="stylesheet" type="text/css" />
<link href="<%=request.getContextPath()%>/style/calendar.css" type="text/css" rel="stylesheet" />
<link href="<%=request.getContextPath()%>/style/page-info.css" rel="stylesheet" type="text/css" />
<!-- 新版CSS -->
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/style/new.css" />
<script type="text/javascript" src="<%=FileConstant.codeJsUrl%>"></script>
<script type="test/javascript" src="<%=request.getContextPath()%>/js/web/jquery-1.7.2.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/codeData.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/regionData.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/dict.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/framecommon/HashMap.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jslib/dialog_new.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jslib/DestoryPrototype.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/web/common.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/FormValidation.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jslib/calendar.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jslib/prototype.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/framecommon/default.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/framecommon/DialogManager.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/validate/validate.js" charset="UTF-8"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jslib/InfoAjax.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jslib/my-grid-pager.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/dateFormatter.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/msgformat.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/textBox.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/baseUtil/simpleData.js"></script>
<!-- 下拉树组件必须的js和css[开始] -->
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/select/css/index.css" />
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/select/ddlevelsfiles/ddlevelsmenu-base.css" />
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/select/ddlevelsfiles/ddlevelsmenu-topbar.css" />
<script type="text/javascript" src="<%=request.getContextPath()%>/select/ddlevelsfiles/ddlevelsmenu.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/selectTree.js"></script>
<!-- 下拉树组件必须的js和css[结束] -->
<script type="text/javascript" src="<%=request.getContextPath()%>/kindeditor/kindeditor.js" />
<script type="text/javascript" >
    var globalContextPath ='<%=(request.getContextPath())%>';
    var g_webAppName = '<%=(request.getContextPath())%>';   //全局webcontent应用名，避免js使用"<%=request.getContextPath()%>"
    var g_webAppImagePath = "<%=(request.getContextPath())%>"+"/images";   //全局图片路径
	function autoAlertException(){
		var str = null;
		<%
		Throwable exception = ActionContext.getContext().getException();
			if(exception!=null&& (exception instanceof BizException)){
				BizException biz = (BizException)exception;
				if(biz.getType()!=ErrorCodeConstant.SELF_DEAL_WITH_CODE){
		%>
					if(<%=biz.getErrCode()!=null %>){
						str = strAppend(str,"错误代码"+'<%=biz.getErrCode()%>'+":</br>");
					}
					if(<%=biz.getMessage()!=null %>){
						str = strAppend(str,'<%=biz.getMessage() %>');
					}
					MyAlert(str);
					return false;
			<%
			}}
			%>
				return true;
			
	}
</script>
<!-- 二级级联菜单 -->
<script language="javascript">
function toChangeMenu(obj,secondId){
	//首先清除二级菜单OPTIONS
	var x = document.getElementById(secondId);
	x.options.length=1;
	//获取一级菜单选中值
	var first = obj.options[obj.selectedIndex].value;
	var bArrayName = new Array();
	var bArrayId = new Array();
	//循环二级意向车型
    <c:forEach items="${menusBList}" var="obj" varStatus="s" >
        var obj_arr="${obj.PARENTID}";
        //当选中值的id为二级意向车型的PARRENTID,将该数据放入数组中以备二级菜单使用
        if(first==obj_arr){
        	bArrayName["${s.index}"] = "${obj.NAME}";
        	bArrayId["${s.index}"] = "${obj.MAINID}";
        }
    </c:forEach>
    //开始创建二级菜单
    if(first!=null ) {
    	var t = document.getElementById(secondId);  
	    for(var j=0;j<bArrayName.length;j++) {
	    	if(bArrayId[j]!=null && bArrayId[j]!="") {
	    		var n = new Option(bArrayName[j],bArrayId[j]);
		    	t.add(n);
	    	}
	    }
    }
}

function toChangeMenu2(obj,secondId){
	//首先清除二级菜单OPTIONS
	var x = document.getElementById(secondId);
	x.options.length=1;
	//获取一级菜单选中值
	var first = obj.options[obj.selectedIndex].value;
	var bArrayName = new Array();
	var bArrayId = new Array();
	//循环二级意向车型
    <c:forEach items="${menusBList2}" var="obj" varStatus="s" >
        var obj_arr="${obj.PARENTID}";
        //当选中值的id为二级意向车型的PARRENTID,将该数据放入数组中以备二级菜单使用
        if(first==obj_arr){
        	bArrayName["${s.index}"] = "${obj.NAME}";
        	bArrayId["${s.index}"] = "${obj.MAINID}";
        }
    </c:forEach>
    //开始创建二级菜单
    if(first!=null ) {
    	var t = document.getElementById(secondId);  
	    for(var j=0;j<bArrayName.length;j++) {
	    	if(bArrayId[j]!=null && bArrayId[j]!="") {
	    		var n = new Option(bArrayName[j],bArrayId[j]);
		    	t.add(n);
	    	}
	    }
    }
}

function toChangeMenu3(obj,secondId){
	//首先清除二级菜单OPTIONS
	var x = document.getElementById(secondId);
	x.options.length=1;
	//获取一级菜单选中值
	var first = obj.options[obj.selectedIndex].value;
	var bArrayName = new Array();
	var bArrayId = new Array();
	//循环二级意向车型
    <c:forEach items="${menusBList3}" var="obj" varStatus="s" >
        var obj_arr="${obj.PARENTID}";
        //当选中值的id为二级意向车型的PARRENTID,将该数据放入数组中以备二级菜单使用
        if(first==obj_arr){
        	bArrayName["${s.index}"] = "${obj.NAME}";
        	bArrayId["${s.index}"] = "${obj.MAINID}";
        }
    </c:forEach>
    //开始创建二级菜单
    if(first!=null ) {
    	var t = document.getElementById(secondId);  
	    for(var j=0;j<bArrayName.length;j++) {
	    	if(bArrayId[j]!=null && bArrayId[j]!="") {
	    		var n = new Option(bArrayName[j],bArrayId[j]);
		    	t.add(n);
	    	}
	    }
    }
}

function toChangeMenuSelected(obj,secondId){
	//首先清除二级菜单OPTIONS
	var x = document.getElementById(secondId);
	x.options.length=0;
	//获取一级菜单选中值
	var first = obj.options[obj.selectedIndex].value;
	var bArrayName = new Array();
	var bArrayId = new Array();
	//循环二级意向车型
    <c:forEach items="${menusBList}" var="obj" varStatus="s" >
        var obj_arr="${obj.PARENTID}";
        //当选中值的id为二级意向车型的PARRENTID,将该数据放入数组中以备二级菜单使用
        if(first==obj_arr){
        	bArrayName["${s.index}"] = "${obj.NAME}";
        	bArrayId["${s.index}"] = "${obj.MAINID}";
        }
    </c:forEach>
    //开始创建二级菜单
    if(first!=null ) {
    	var t = document.getElementById(secondId);  
	    for(var j=0;j<bArrayName.length;j++) {
	    	if(bArrayId[j]!=null && bArrayId[j]!="") {
	    		var n = new Option(bArrayName[j],bArrayId[j]);
		    	t.add(n);
	    	}
	    }
    }
}
//手机验证方法
function validatemobile(mobile)
{
    if(mobile.length==0)
    {
       MyAlert('请输入手机号码！');
       return false;
    }
    if(mobile.length  != 11)
    {
        MyAlert('请输入有效的手机号码！');
        return false;
    }
    
    return true;
}
</script>
<div id="loader" style='position:absolute;z-index:200;background:#FFCC00;padding:1px;top:4px;display:none;display: none;'></div>
<%
	Calendar time=Calendar.getInstance();
	SimpleDateFormat formate = new SimpleDateFormat("yyyy,MM,dd,hh,mm,ss,HH");
	String sys_date = formate.format(time.getTime());
%>	
<input type="hidden" id="sys_date__" name="sys_date__" value="<%=sys_date %>">
