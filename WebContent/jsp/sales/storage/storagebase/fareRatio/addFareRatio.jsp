<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/DTD/xhtml1-transitional.dtd">
<%@page import="java.util.List"%>
<%@page import="java.util.Map"%>


<%@page import="com.infodms.dms.util.CommonUtils"%><html xmlns="http://www.w3.org/1999/xhtml"><%@ page import="com.infodms.dms.common.Constant"%><%@ page import="com.infodms.dms.common.FileConstant"%><%@taglib uri="/jstl/cout" prefix="c"%>

<head>
<%
String contextPath=request.getContextPath();
%>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title> 运费系数新增 </title>
</head>
<body>
<div class="navigation"><img src="<%=contextPath %>/img/nav.gif" />&nbsp;当前位置：储运管理>储运基础数据>运费系数设定>运费系数信息添加</div>
<form name="fm" method="post" id="fm">
<div class="form-panel">
	<h2>运费系数维护</h2>
	<div class="form-body">
	<table class="table_query">
	   <tr>
	    <td class="right" width="15%">车系：</td>  
		    <td>
	  		<select name="GROUP_ID" id="GROUP_ID" class="u-select" onblur="myBlur(this);">
	  			<c:if test="${list_vchl!=null}">
					<c:forEach items="${list_vchl}" var="list">
						<option value="${list.GROUP_ID }">${list.GROUP_NAME }</option>
					</c:forEach>
				</c:if>
	  		</select>
     	 </td> 
     	 <td class="right" width="15%">运费系数：</td>  
		 <td>
	  		<input type="text" name="RATIO_NUM" id="RATIO_NUM" value="1" datatype="0,isMoney,20" class="middle_txt"/>
     	 </td> 
     </tr>
     <tr> 
      	<td colspan="4" class="table_query_4Col_input" style="text-align: center">
			<input type="button"  class="normal_btn" id="saveButton" onclick="addReservoir()" value="保存"/>&nbsp;&nbsp;
			<input type="button" class="normal_btn" id="goBack"  onclick="back();" style="width=8%" value="返回"/>
	   	</td>
	  </tr>
	</table>
	</div>
</div>
	<!-- 基本信息end -->
</form>
<!--页面列表 begin -->
<script type="text/javascript" >
//添加
function addReservoir()
{
	if(submitForm('fm')) {
		var ratioNum=document.getElementById("RATIO_NUM");
		if(ratioNum.value==""){
			showMyErrMsg(ratioNum,'此处不能为空！');
			return;
		}
		if(!submitForm("fm")){
			return;
		}
		MyConfirm("确认添加该信息！",addFareRatio);
	}	
}
function addFareRatio()
{ 
	disabledButton(["saveButton","goBack"],true);
	makeNomalFormCall("<%=contextPath%>/sales/storage/storagebase/FareRatioSet/addFareRatio.json",addFareSetBack,'fm','queryBtn'); 
}
function addFareSetBack(json)
{
	if(json.returnValue == 1)
	{
		parent.MyAlert("操作成功！");
		fm.action = "<%=contextPath%>/sales/storage/storagebase/FareRatioSet/fareRatioSetInit.do";
		fm.submit();
	}else if(json.returnValue == 2){
		disabledButton(["saveButton","goBack"],false);
		MyAlert("该车系已设定运费系数，无法添加！");
	}else{
		disabledButton(["saveButton","goBack"],false);
		MyAlert("操作失败！请联系系统管理员！");
	}
}
function back(){
	fm.action = "<%=contextPath%>/sales/storage/storagebase/FareRatioSet/fareRatioSetInit.do";
	fm.submit();
}
function doInit(){
	//changeCheckBox();
}

</script>
</body>
</html>
