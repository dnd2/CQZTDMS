<!DOCTYPE html PUBLIC "-//W3C//Dtd Xhtml 1.0 transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<% String contextPath = request.getContextPath(); %>
<body onload="__extQuery__(1)">
<div class="wbox">
<div id="loader" style='position:absolute;z-index:200;background:#FFCC00;padding:1px;top:4px;display:none;display: none;'></div>
<div class="navigation"><img src="<%=contextPath %>/img/nav.gif" width="11" height="11" />&nbsp;当前位置：旧件入库修改 </div>
</div>
  <form  name="fm" id="fm" method="post">
  <input type="hidden" name="partCode" id="partCode" value="<%=request.getAttribute("partCode") %>"/>
   <!--查询条件begin-->
    <table class="table_query" border="0" >
   
   		 <tr>
      		<td class="table_query_3Col_label_7Letter">索赔单号： </td>
            <td align="left">
				<input id="claim_no" name="claim_no" value="" type="text" class="middle_txt" maxlength="20">
			</td>
			<td class="table_query_3Col_label_7Letter">VIN： </td>
            <td align="left">
				<input id="VIN" name="VIN" value="" type="text" class="middle_txt" maxlength="20">
			</td>
       </tr>
       
       
       
   
   
        <tr>
        <td colspan="4" align="center">
        <input class="normal_btn" type="button" id="qryButton" name="qryButton" value="查询"  onClick="__extQuery__(1);">
        <input name="button" id="queryBtn" type="button" onclick="_hide()" class="normal_btn"  value="关闭" />
        </td>
      </tr>
    </table>
    <!--查询条件end-->
    <jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
</form>
<script language="JavaScript" >
	var myPage;
	
	var url = "<%=contextPath%>/claim/specialExpenses/SpecialExpensesManage/showClaimNoData.json";
				
	var title = null;

	var columns = [
				{id:'action',header: "选择",sortable: false,dataIndex: 'ID',renderer:mySelect,align:'center'},
				{header: "索赔单号", dataIndex: 'CLAIM_NO', align:'center'},
				{header: "VIN", dataIndex: 'VIN', align:'center'},
				{header: "工单号", dataIndex: 'RO_NO', align:'center'},
				{header: "索赔单类型", dataIndex: 'CLAIM_TYPE', align:'center',renderer:getItemValue}
		      ];
		      
	function mySelect(value,metaDate,record){
		 return String.format("<input type='radio' name='rd' onclick='setClaimNo(this,\""+record.data.CLAIM_NO+"\")' />");
	}

	function setClaimNo(obj,claimNo){
		if(claimNo=="null"||claimNo==null){
			claimNo = "";
		}
		if(obj.checked){
			if (parent.$('inIframe')) 
			{
				parentContainer.setClaimNo(claimNo);
			}
		   _hide();
		}
	}
</script>
</body>
</html>
