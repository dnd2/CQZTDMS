<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/DTD/xhtml1-transitional.dtd">
<%@page import="java.util.List"%>
<%@page import="java.util.Map"%>

<%@page import="java.util.LinkedList"%>
<html xmlns="http://www.w3.org/1999/xhtml"><%@ page import="com.infodms.dms.common.Constant"%><%@ page import="com.infodms.dms.common.FileConstant"%><%@taglib uri="/jstl/cout" prefix="c"%>

<head>
<%
String contextPath=request.getContextPath();
%>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title> 运费设定 </title>

</head>

<body>
	<div class="navigation"><img src="<%=contextPath %>/img/nav.gif" />&nbsp;当前位置：储运管理>储运基础数据> 运费设定>运费设定信息修改</div>

<form name="fm" method="post" id="fm">
<div class="form-panel">
	<h2>运费系数维护</h2>
	<div class="form-body">
	<table class="table_query">
	   <tr>
	    <td class="right" width="15%">车系：</td>  
		    <td align="left">
	  		<select id="GROUP_ID" class="u-select" onblur="myBlur(this);" disabled="disabled">
	  			<c:if test="${list_vchl!=null}">
					<c:forEach items="${list_vchl}" var="list">
						<option value="${list.GROUP_ID }">${list.GROUP_NAME }</option>
					</c:forEach>
				</c:if>
	  		</select>
	  		<input type="hidden" name="GROUP_ID" id="GROUP_IDS" value="${groupId}"/>
     	 </td> 
     	  <td class="right" width="15%">运费系数：</td>  
		    <td align="left">
	  		<input type="text" name="RATIO_NUM" id="RATIO_NUM" datatype="0,isMoney,20" value="${ratioNum}" class="middle_txt"/>
     	 	</td> 
     	 </tr>
    <tr> 
      	<td colspan="4" class="table_query_4Col_input" style="text-align: center">
      		<input type="hidden" id="SET_ID" name="SET_ID" value="${setId}" />
			<input type="button"  class="normal_btn" id="saveButton" onclick="addReservoir()" value="保存"/>&nbsp;&nbsp;
			<input type="button" class="normal_btn" id="goBack"  onclick="back();" value="返回"/>
	   	</td>
	  </tr>
	</table>
	</div>
</div>
	<!-- 基本信息end -->
</form>
<!--页面列表 begin -->
<script type="text/javascript" >
//初始化    
function doInit(){
	var obj = document.getElementById("GROUP_ID");
	var objIds = document.getElementById("GROUP_IDS");
	//MyAlert(objIds.value);
	obj.value = objIds.value;
	
}
//添加
function addReservoir()
{
	if(submitForm('fm')) {
		var groupId=document.getElementById("GROUP_ID");
		var ratioNum=document.getElementById("RATIO_NUM");
		if(groupId.value==""){
			showMyErrMsg(groupId,'此处不能为空！');
			return;
		}else if(ratioNum.value==""){
			showMyErrMsg(ratioNum,'此处不能为空！');
			return;
		}
		MyConfirm("确认修改该信息！",updateFareSet,"");
	}
	
}
function updateFareSet()
{
	disabledButton(["saveButton","goBack"],true);
	makeNomalFormCall("<%=contextPath%>/sales/storage/storagebase/FareRatioSet/editFareRatio.json",updateFareSetBack,"fm"); 
}

function updateFareSetBack(json)
{
	if(json.returnValue == 1)
	{
		parent.MyAlert("操作成功！");
		fm.action = "<%=contextPath%>/sales/storage/storagebase/FareRatioSet/fareRatioSetInit.do";
		fm.submit();
	}
	else
	{
		disabledButton(["saveButton","goBack"],false);
		MyAlert("操作失败！请联系系统管理员！");
	}
}
function back(){
	fm.action = "<%=contextPath%>/sales/storage/storagebase/FareRatioSet/fareRatioSetInit.do";
	fm.submit();
}
</script>
</body>
</html>