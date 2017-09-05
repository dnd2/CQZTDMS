<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java"  pageEncoding="UTF-8"%>
<%@page import="com.infodms.dms.po.TtAsWrOtherfeePO"%>
<%@taglib uri="/jstl/change" prefix="change" %>
<%@ page import="com.infodms.dms.common.Constant" %>
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
  <img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 售后服务管理&gt;索赔基础数据&gt;时效参数新增</div>
<form name='fm' id='fm' method="post" action="<%=contextPath%>/claim/application/ClaimBillStatusTrack/addOrUpdateTimeParameter.do">
<input type="hidden" name="FEE_ID" id="FEE_ID" value="" />
		  <table class="table_add">
			    <tr >
			     <td class="table_add_2Col_label_6Letter">参数名称：</td>
			     <td><input id="INVOICE_NAME" name="PARAMETER_NAME" type="text" class="middle_txt" value="${po.parameterName }" datatype="0,is_null,30"/></td> 
			      <td class="table_query_2Col_label_8Letter" >超时时间（天）：</td>
			     <td><input id="TIME_OUT" name="TIME_OUT" type="text" class="middle_txt" value="${po.timeout }" datatype="0,is_null,3"/></td> 
			    </tr> 
			    <input id="INVOICE_NAME" name="ID" type="hidden" class="middle_txt" value="${po.id }" datatype="0,is_null,30"/>
			    <tr>
			      <td  class="table_query_2Col_label_6Letter">参数代码：</td>
			      <td><input id="TAX_RATE" name="PARAMETER_CODE" type="text" class="middle_txt" value="${po.parameterCode }" datatype="0,is_null,30"/></td> 
			    	<td  class="table_query_2Col_label_6Letter">金额：</td>
			      <td><input id="TAX_RATE" name="AMOUNT" type="text" class="middle_txt" value="${po.amount }" datatype="0,is_double,10"/></td> 
			    </tr> 	
			   <tr>
			      <td  class="table_add_2Col_label_6Letter">参数类别：</td>
			      <td>
			      		<script type="text/javascript">
							genSelBoxExp("PARAMETER_TYPE",<%=Constant.PARAMETER_TYPE%>,"${po.parameterType}",true,"short_sel",'',"false","");
						</script>
			      
			      </td> 
			    	<td  class="table_add_2Col_label_6Letter">参数状态：</td>
			      <td>	
			      <script type="text/javascript">
							genSelBoxExp("PARAMETER_STATUS",<%=Constant.STATUS%>,"${po.parameterStatus }",true,"short_sel",'',"false","");
						</script>
				   </td> 
			    </tr> 	
			 <tr align="center"> 
		     	 <td colspan="6" align="center">
		        <input name="ok" type="button" id="commitBtn" class="normal_btn"  value="确定"  onclick="checkForm('<%=contextPath%>/claim/application/ClaimBillStatusTrack/addOrUpdateTimeParameter.do');"/>
		        <input name="back" type="button" class="normal_btn" value="取消" onclick="JavaScript:history.back()"/>
		        </td>
		    </tr>
		  </table>
	</form>
	<form action="" method="post" id="fm1" name="fm1">
	
	</form>
<script>
//表单提交前的验证：
function checkForm(url){
	submitForm('fm') == true ? otherfeeUpdate(url) : "";
}
//表单提交方法：
function otherfeeUpdate(url){
	MyConfirm("是否确认修改？",submitForm,[url]);
}
function updateOtherfee(url){
	disableBtn($("commitBtn"));
	makeNomalFormCall(url,updateBack,'fm','');
}
function submitForm(url){
	var PARAMETER_TYPE = document.getElementsByName("PARAMETER_TYPE")[0].value;
	var PARAMETER_STATUS = document.getElementsByName("PARAMETER_STATUS")[0].value;
	if(PARAMETER_TYPE==""){
		MyAlert("请选择参数类型");
		return false;
	}if(PARAMETER_STATUS==""){
		MyAlert("请选择参数状态");
		return false;
	}
	
	var url="<%=contextPath%>/claim/application/ClaimBillStatusTrack/checkParametertype.json?parameterType="+PARAMETER_TYPE;
	var TIME_OUT=$("TIME_OUT").value;
	var re = /^\+?[1-9]?[1-9]?[1-9]/;
	if(!re.test(TIME_OUT)){
		MyAlert("超时时间必须为三位数字");
		return false;
	}
	
	sendAjax(url,function(json){
		
			if(json.succ=="0"||json.succ==0){
				document.getElementById("fm").submit();
			}else{
				MyAlert("该参数类别已有数据，请选择其他参数类别");
			}
	},'fm1');	
	
// 	makeNomalFormCall(url,updateBack,'fm','');
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