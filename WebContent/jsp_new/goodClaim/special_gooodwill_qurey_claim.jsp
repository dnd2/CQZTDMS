<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.Constant" %>
<%
	String contextPath = request.getContextPath();
%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>选择索赔单号</title>
<script type="text/javascript">

</script>
</head>
<body>
	<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 选择索赔单号</div>
	<form method="post" name = "fm" id="fm">	
		<table  border="0" align="center" cellpadding="1" cellspacing="1" class="table_query">
			<th colspan="4"><img class="nav" src="<%=contextPath%>/img/subNav.gif" /> 选择索赔单号</th>
			
			<tr>
				<td align="right" nowrap="true">索赔单号：</td>
				<td align="left" nowrap="true">
					<input type="text" id="claim_no" class="middle_txt"  name="claim_no" maxlength="30"/>
				</td>
				
				<td align="right" nowrap="true">VIN：</td>
				<td align="left" nowrap="true">
					<input type="text" id="vin" class="middle_txt"  name="vin" maxlength="30"/>
				</td>
			</tr>
	
			<tr>
				<td colspan="4" align="center">
					<input class="normal_btn" type="button" value="查询" name="recommit" id="queryBtn" onclick="__extQuery__(1);" />
        		</td>
			</tr>
		</table>
	 <!-- 查询条件 end -->
	 <!--分页 begin -->
		<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
		<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
	 <!--分页 end -->
	</form>
<script type="text/javascript">
	
	var myPage;
    //查询路径
	var url = "<%=contextPath%>/GoodClaimAction/qureyAllClaim.json";
				
	var title = null;

	var columns = [
				{id:'action',header: "选择", width:'8%',sortable: false,dataIndex: 'ID',renderer:checkRadio},
				{header: "索赔单号", dataIndex: 'CLAIM_NO', style: 'text-align:center'},
				{header: "VIN", dataIndex: 'VIN', style: 'text-align:center'},
				{header: "工单号", dataIndex: 'RO_NO', style: 'text-align:center'},
				{header: "索赔类型", dataIndex: 'CLAIM_TYPE', style: 'text-align:center',renderer:getItemValue}
		      ];
	__extQuery__(1);
	function checkRadio(value,metaDate,record){
		return String.format("<input name='radio' type='radio' onclick='choose(this,\""+ record.data.CLAIM_NO +"\",\""+ record.data.VIN +"\")'/>");
	}
	
	function choose(obj,claim_no,vin){
		if(obj.checked){
			if (parent.$('inIframe')){
				parentContainer.choose_claim(claim_no,vin);
			}
		   _hide();
		}
	}
	
</script>
</body>
</html>