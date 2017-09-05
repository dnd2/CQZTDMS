<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c" %>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>经销商价格查询</title>
<script type="text/javascript">
function doInit(){
}   
</script>
</head>
<body>
<div class="navigation"><img src="<%=request.getContextPath()%>/img/nav.gif" />&nbsp;当前位置：整车销售 > 销售订单管理 >订单审核 > 经销商价格查询</div>
<form method="POST" name="fm" id="fm">
  <table class="table_query" align=center width="95%">
	<tr>
	 	<td align="right" nowrap>经销商:</td>
		<td>
			<input type="text" class="middle_txt" name="dealerCode" size="15" value="" id="dealerCode"/>
            <c:if test="${10431001 eq dutyType}"><%--用户为车场端登录 查询经销商时显示所有--%>
			    <input name="button2" type="button" class="mini_btn" onclick="showOrgDealer('dealerCode','','false','','true');" value="..." />
            </c:if>
            <c:if test="${10431003 eq dutyType}"><%--用户为大区端登录 查询经销商时显示当前区的--%>
			    <input name="button2" type="button" class="mini_btn" onclick="showOrgDealer3('dealerCode','','false','${orgId}','true');" value="..." />
            </c:if>
            <c:if test="${10431004 eq dutyType}"><%--用户为小区端登录 查询经销商时显示当前小区的--%>
			    <input name="button2" type="button" class="mini_btn" onclick="showOrgDealer4('dealerCode','','false','${orgId}','true');" value="..." />
            </c:if>
			<input class="normal_btn" type="button" value="清空" onclick="clrTxt('dealerCode');"/>
		</td>		
		<td align="right" nowrap>车型代码:</td>
		<td align="left" nowrap><input type="text" class="middle_txt" id="modelCode" name="modelCode" /></td>
	</tr> 
	<tr>
	   <td align="right" nowrap colspan="5">
	   	<input id="queryBtn" name="button22" type=button class="cssbutton" onClick="query();" value="查询">
           页面大小：<input name="pageSize" id="pageSize" type="text" class="mini_txt" value="10" datatype="0,isDigit,3"/>
       </td>

	</tr>
  </table>
  
	<!--分页 begin -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
    <!--分页 end -->
</form>
<script type="text/javascript">

	var myPage;
	//查询路径
	var url = "<%=request.getContextPath()%>/sales/ordermanage/orderaudit/DealerPriceQueryOEM/dealerPriceOEMQuery.json";
	var title = null;
	var columns = [
				{header: "经销商代码", dataIndex: 'PARTY_NUMBER', align:'center'},
				{header: "经销商名称", dataIndex: 'PARTY_NAME', align:'center'},
				//{header: "车型代码", dataIndex: 'MODEL_CODE', align:'center'},
				//{header: "车型名称", dataIndex: 'MODEL_NAME', align:'center'},
				{header: "配置代码", dataIndex: 'CATEGORIES', align:'center'},
				{header: "配置名称", dataIndex: 'GROUP_NAME', align:'center'},
				{header: "价格", dataIndex: 'OPERAND', align:'center' }
				//{header: "价格生成日期", dataIndex: 'CREATE_DATE', align:'center'}
		      ];		         

	function clrTxt(txtId){
    	document.getElementById(txtId).value = "";
    }
	
	function query() {
		var dealerCode = document.getElementById('dealerCode');
		
		if(dealerCode.value == null || dealerCode.value.trim() == "") {
			MyAlert("经销商必须选择!");
			return false;
		}
		__extQuery__(1);
	}
</script>
</body>
</html>
