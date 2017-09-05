<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java"  pageEncoding="UTF-8"%>
<%@page import="com.infodms.dms.po.TtAsWrOtherfeePO"%>
<%@ page import="com.infodms.dms.common.Constant"%>
<%@taglib uri="/jstl/change" prefix="change" %>
<%
	String contextPath = request.getContextPath();
	TtAsWrOtherfeePO po = (TtAsWrOtherfeePO)request.getAttribute("otherfee");
%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>开票概率维护</title> 
 <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
</head>
<body>

  <div class="navigation">
  <img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置1： 售后服务管理&gt;索赔基础数据&gt;开票概率维护</div>
<form name='fm' id='fm'>
<input type="hidden" name="FEE_ID" id="FEE_ID" value="" />
		  <table class="table_add">
			    <tr >
			     <td class="table_add_2Col_label_6Letter">开票税率名称：</td>
			     <td><input id="INVOICE_NAME" name="INVOICE_NAME" type="text" class="middle_txt" value="${trPo.invoiceName }" datatype="0,is_null,30"/></td> 
			    </tr> 
			    <input id="INVOICE_NAME" name="ID" type="hidden" class="middle_txt" value="${trPo.id }" datatype="0,is_null,30"/>
			    <tr>
			      <td  class="table_add_2Col_label_6Letter">税率：</td>
			      <td><input id="TAX_RATE" name="TAX_RATE" type="text" class="middle_txt" value="${trPo.taxRate }" datatype="0,is_null,30"/></td> 
			    </tr> 	
			     <tr>
			      <td  class="table_add_2Col_label_6Letter">状态 ：</td>
			      <td>
			      <script type="text/javascript">
  					genSelBoxExp("STATUS",<%=Constant.STATUS%>,"",true,"short_sel","","false",'');
  				  </script> 
<!-- 			      <input id="STATUS" name="STATUS" type="text" class="middle_txt" value="" datatype="0,is_null,30"/></td>  -->
			    </tr> 		
			 <tr> 
		     	 <td colspan="2" align="center">
		        <input name="ok" type="button" id="commitBtn" class="normal_btn"  value="确定"  onclick="checkForm('<%=contextPath%>/claim/basicData/TreeGuaranteesStrategy/invoiceFeeUpdate.json');"/>
		        <input name="back" type="button" class="normal_btn" value="取消" onclick="JavaScript:history.back()"/>
		        </td>
		    </tr>
		  </table>
	</form>
<script>
//表单提交前的验证：
function checkForm(url){
	submitForm('fm') == true ? otherfeeUpdate(url) : "";
}
//表单提交方法：
function otherfeeUpdate(url){
	MyConfirm("是否确认修改？",updateOtherfee,[url]);
}
function updateOtherfee(url){
	disableBtn($("commitBtn"));
	makeNomalFormCall(url,updateBack,'fm','');
}

//修改回调方法：
function updateBack(json) {
	if(json.success != null && json.success=='true'){
		MyAlertForFun("修改成功",sendPage);
	}else{
		MyAlert("修改失败！请联系管理员");
	}
}
//页面跳转
function sendPage(){
	window.location = '<%=contextPath%>/claim/basicData/TreeGuaranteesStrategy/invoiceProbabilityMaintain.do';
}
</script>
</body>
</html>