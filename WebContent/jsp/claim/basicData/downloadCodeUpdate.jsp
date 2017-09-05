<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java"  pageEncoding="UTF-8"%>
<%@page import="com.infodms.dms.po.TmBusinessChngCodePO"%>
<%
	String contextPath = request.getContextPath();
	TmBusinessChngCodePO po = (TmBusinessChngCodePO)request.getAttribute("businesscode");
%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>修改代码</title> 
 <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
</head>
<body>

  <div class="navigation">
  <img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 售后服务管理&gt;索赔基础数据&gt;下发代码维护</div>
<form name='fm' id='fm'>
<input type="hidden" name="BUSINESS_CODE_ID" id="BUSINESS_CODE_ID" value="<%=po.getBusinessCodeId()%>" />
		  <table class="table_edit">
			    <tr >
			     <td class="table_edit_3Col_label_4Letter">类别：</td>
			     <td align="left"> 
		              <script type='text/javascript'>
				      	 	var typeCode = getItemValue('<%=po.getTypeCode()%>');
				       		document.write(typeCode) ;
					 </script> 
			      </td>
			      <td class="table_edit_3Col_label_4Letter">代码：</td>
			      <td align="left"> 
			        <%=po.getCode()==null?"":po.getCode()%>
			      </td>
			      <td  class="table_edit_3Col_label_4Letter">描述： </td>
			      <td align="left">
			        <input id="CODE_NAME" name="CODE_NAME" type="text" class="middle_txt" datatype="0,is_null,150" value="<%=po.getCodeName()==null?"":po.getCodeName()%>"/>
			      </td>
			    </tr>
			<tr> 
		     	 <td colspan="8" align="center">
		        <input name="ok" type="button" class="normal_btn" id="commitBtn"  value="确定"  onclick="downloadUpdate();"/>
		        <input name="back" type="button" class="normal_btn" value="取消" onclick="JavaScript:history.back()"/>
		        </td>
		    </tr>
		  </table>
	</form>
</body>
</html>
<script>
<%--//修改方法：checkForm('<%=contextPath%>/claim/basicData/DownloadcodeMain/downloadcodeUpdate.do');--%>
//表单提交前的验证：
function checkForm(){
	submitForm('fm') == true ? downloadConfirm() : "";
}
//表单提交方法：
function downloadConfirm(){
	MyConfirm("是否确认修改？",downloadUpdate);
}
function downloadUpdate(){
	disableBtn($("commitBtn"));
	makeNomalFormCall('<%=contextPath%>/claim/basicData/DownloadcodeMain/downloadcodeUpdate.json',updateBack,'fm','');
}
//修改后的回调方法：
function updateBack(json) {
	if(json.success != null && json.success == "true") {
	    	MyAlertForFun("修改成功 !",sendPage);
	} else {
		MyAlert("修改失败！请联系管理员！");
	}
}
//页面跳转
function sendPage(){
	window.location = '<%=contextPath%>/claim/basicData/DownloadcodeMain/downloadcodeInit.do';
}
</script>