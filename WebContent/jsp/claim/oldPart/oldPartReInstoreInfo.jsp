<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java"  pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.Constant"%>
<%@page import="java.util.HashMap"%>
<%
	String contextPath = request.getContextPath();
%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>修改授权人员管理</title> 
 <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
</head>
<body>
  <div class="navigation">
  <img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 售后服务管理&gt;索赔旧件管理 &gt;索赔旧件供应商修改</div>
<form name='fm' id='fm'>
 <input type="hidden" name="yieldly" id="yieldly" value="${yieldly }" />
		  <table class="table_edit">
			    <tr >
			      <td class="table_edit_2Col_label_7Letter">配件代码：</td>
			      <td>
			      	<input type="hidden" name="partCode" id="partCode" value='${bean.partCode }'/>
			      	<input type="hidden" name="claimId" id="claimId" value='${bean.claimId }'/>
			      	${bean.partCode }
			      </td>
			    </tr>
			    <tr>
			    	<td  class="table_edit_2Col_label_7Letter">配件名称： </td>
			       <td>
					 ${bean.partName }			       
			       </td>
			    </tr>
			     <tr >
			      <td class="table_edit_2Col_label_7Letter">索赔单号：</td>
			      <td>
			      	${bean.claimNo }
			      </td>
			    </tr>
			    <tr>
			    	<td  class="table_edit_2Col_label_7Letter">修改旧件数量： </td>
			       <td>
					 ${bean.returnAmount }			       
			       </td>
			    </tr>
			      <tr >
			      <td class="table_edit_2Col_label_7Letter">供应商代码：</td>
			      <td>
			      <input id="supply_code" name="supply_code" value="${bean.producerCode }" type="text" class="middle_txt" readonly="readonly" >
			       <input  name="addsupply" value="......" type="button" class="normal_btn" onclick="addSupply('${bean.partCode }');" >
			      </td>
			    </tr>
			    <tr>
			    	<td  class="table_edit_2Col_label_7Letter">供应商名称： </td>
			       <td>
					 <input id="supply_name" name="supply_name" value="${bean.producerName }" type="text" class="long_txt" readonly="readonly" ></td>
			    </tr>
			<tr> 
		     	 <td colspan="6" align="center">
		        <input name="ok" type="button" class="normal_btn" id="commitBtn"  value="确定"  onclick="checkFormUpdate()"/>
		        <input name="back" type="button" class="normal_btn" value="返回" id="commitBtn2" onclick="backTos();"/>
		        </td>
		    </tr>
		  </table>
<script>
function backTos(){
	var yieldly = $('yieldly').value;
	if(yieldly == '<%=Constant.PART_IS_CHANGHE_01 %>'){
	 fm.action= "<%=contextPath%>/claim/oldPart/ClaimOldPartOutStorageManager/oldPartSupplyModPer.do";
	}else if(yieldly == '<%=Constant.PART_IS_CHANGHE_02 %>'){
	 fm.action= "<%=contextPath%>/claim/oldPart/ClaimOldPartOutStorageManager/oldPartSupplyModPer2.do";
	 }
 	 fm.method="post";
     fm.submit();
	}
function checkFormUpdate(){

MyConfirm("确定修改供应商？",checkForm,[]);
}
	//表单提交方法：
	function checkForm(){
			disableBtn($("commitBtn"));
			disableBtn($("commitBtn2"));
			makeFormCall('<%=contextPath%>/claim/oldPart/ClaimOldPartOutStorageManager/supplierSave.json',showResult,'fm');			
	}

	function showResult(json){
		if(json.success != null && json.success == "true"){
			MyAlert("修改成功!");
			if(json.yieldly == '<%=Constant.PART_IS_CHANGHE_01 %>'){
				window.location.href = "<%=contextPath%>/claim/oldPart/ClaimOldPartOutStorageManager/oldPartSupplyModPer.do";
			}else{
				window.location.href = "<%=contextPath%>/claim/oldPart/ClaimOldPartOutStorageManager/oldPartSupplyModPer2.do";
			}
		}else{
			MyAlert("修改失败，请联系管理员！");
			$("commitBtn").disabled=false;
			$("commitBtn2").disabled=false;
		}
	}
	function addSupply(code){
		OpenHtmlWindow('<%=contextPath%>/claim/oldPart/ClaimOldPartOutStorageManager/selectSupplierForward.do?partCode='+code+'&yieldly='+$("yieldly").value,800,430);	
	}
function setSupplier(code,name){
	$('supply_code').value=code;
	$('supply_name').value=name;
	}
</script>
	</form>
</body>
</html>