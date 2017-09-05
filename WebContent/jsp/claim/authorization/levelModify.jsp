<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="com.infodms.dms.po.TtAsWrAuthinfoPO"%>
<%
	String contextPath = request.getContextPath();
	TtAsWrAuthinfoPO po = (TtAsWrAuthinfoPO)request.getAttribute("AUTHINFO");
%>

<%@page import="com.infodms.dms.common.Constant"%><html xmlns="http://www.w3.org/1999/xhtml">
<head>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>授权级别维护</title> 
 <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
</head>
<body>
<div class="wbox">
  <div class="navigation">
  <img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 售后服务管理&gt;索赔授权管理&gt;授权级别维护</div>
<form name='fm' id='fm'>
<div class="form-panel">
		<h2>基本信息</h2>
			<div class="form-body">
<input type="hidden" name="APPROVAL_LEVEL_TIER" id="APPROVAL_LEVEL_TIER" value="<%=po.getApprovalLevelTier()%>" />
<input type="hidden" name="TYPE" id="TYPE" value="<%=po.getType()%>" />
		  <table class="table_query">
			    <tr >
			      <td class="table_add_2Col_label_6Letter" style="text-align:right">授权级别编号：</td>
			      <td> 
			        <%=po.getApprovalLevelCode()==null?"":po.getApprovalLevelCode()%>
			      </td>
			    </tr>
			    <tr>
			    	<td  class="table_add_2Col_label_6Letter" style="text-align:right">授权级别名称： </td>
			      <td>
			        <input id="APPROVAL_LEVEL_NAME" name="APPROVAL_LEVEL_NAME" maxlength="10"  type="text" class="middle_txt" datatype="0,is_digit_letter_cn,10" value="<%=po.getApprovalLevelName()==null?"":po.getApprovalLevelName()%>"/>
			      </td>
			    </tr>
			<tr> 
		     	 <td colspan="2" style="text-align:center">
		        <input name="ok" type="button" class="normal_btn" id="commitBtn"  value="确定"  onclick="checkFormUpdate();"/>
		        <input name="back" type="button" class="normal_btn" value="返回" onclick="_hide() ;"/>
		        </td>
		    </tr>
		  </table>
		  </div>
		  </div>
	</form>
	</div>
<script>
	//表单提交方法：
	function checkForm(){
			document.getElementById("commitBtn").disabled = true ;
			makeNomalFormCall('<%=contextPath%>/claim/authorization/LevelMain/levelUpdate.json',showResult,'fm');			
	}
	function goBack(){
		history.go(-1);
	}
	function showResult(json){
		if(json.success != null && json.success=="true"){
			MyAlert("修改成功!");
			__parent().__extQuery__(1) ;
			_hide() ;
		}else{
			MAlert("修改失败，请联系管理员！");
		}
	}
	//表单提交前的验证：
	function checkFormUpdate(){
		if(!submitForm('fm')) {
			return false;
		}			
		MyConfirm("是否确认修改?",checkForm);
	}
</script>
</body>
</html>