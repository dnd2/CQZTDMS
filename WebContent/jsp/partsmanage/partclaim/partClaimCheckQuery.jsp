<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>配件索赔审核</title>
<% String contextPath = request.getContextPath(); %>
<script type="text/javascript">
	function doInit(){
   		loadcalendar();  //初始化时间控件
	}
</script>
</head>
<body>
<img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 配件管理&gt;配件索赔&gt;配件索赔审核
<form name="fm" id="fm">
<!-- 查询条件 begin -->
<table width=100% border="0" align="center" cellpadding="1" cellspacing="1" class="table_query">
	<tr>
		<td class="table_query_2Col_label_5Letter">索赔单号：</td>
      	<td><input name="claimNo" type="text" id="claimNo"  class="middle_txt"/></td>
      	<td class="table_query_2Col_label_6Letter">货运单号：</td>
      	<td><input name="doNo" type="text" id="doNo"  class="middle_txt"/></td>
    </tr>
    <tr>
      	<td class="table_query_2Col_label_5Letter">索赔日期：</td>
      	<td><div align="left">
        		<input name="beginDate" id="t1" value="" type="text" class="short_txt" datatype="1,is_date,10" group="t1,t2" hasbtn="true" callFunction="showcalendar(event, 't1', false);">
           			&nbsp;至&nbsp;
           		<input name="endDate" id="t2" value="" type="text" class="short_txt" datatype="1,is_date,10" group="t1,t2" hasbtn="true" callFunction="showcalendar(event, 't2', false);">
        	</div>
      	</td>
      	<td class="table_query_2Col_label_6Letter">经销商名称：</td>
      	<td><input name="dealerName" type="text" id="dealerName" class="middle_txt" disabled/></td>
	</tr>
    <tr>
    	<td class="table_query_2Col_label_5Letter"><input type="radio" id="odtype" name="odtype" value="0" onclick="checkType(this.value)" checked="checked">区域：</td>
      	<td>
          	<input name="orgCode" type="text" id="orgCode" class="middle_txt" value=""/>
			<input name="orgSel" id="orgSel" type="button" class="mark_btn" onclick="showOrg('orgCode' ,'orgId' ,true,'')" value="&hellip;" />
		  	<input class="normal_btn" type="button" id="orgRe" value="清除" onclick="resetOrg()"/>
		  	<input name="orgId" type="hidden" id="orgId"/>	
      	</td>
    	<td class="table_query_2Col_label_6Letter"><input type="radio" id="odtype" name="odtype" value="1" onclick="checkType(this.value)">经销商代码：</td>
      	<td>
      		<input class="middle_txt" id="dealerCode" name="dealerCode" value="" type="text" disabled/>
        	<input class="mark_btn" id="deaBtn" type="button" value="&hellip;" disabled onclick="showOrgDealer('dealerCode','','true','',true)"/>
        	<input class="normal_btn" type="button" id="deaRe" value="清除" onclick="resetDealer()" disabled/>     
     	</td>
    </tr>
    <tr>
    	<td></td><td></td><td></td>
      	<td class="table_query_4Col_label_4Letter">
      		<input type="button" name="BtnQuery" id="queryBtn"  value="查询"  class="normal_btn" onClick="__extQuery__(1)" >
      	</td>
    </tr>
</table>
<!-- 查询条件 end -->
<!--分页 begin -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
<!--分页 end -->
</form>
<!--页面列表 begin -->
<script type="text/javascript" >
	var myPage;
	var url = "<%=contextPath%>/partsmanage/partclaim/PartClaimCheck/partClaimCheckQuery.json";
				
	var title = null;

	var columns = [
				{header: "索赔单号", dataIndex: 'CLAIM_NO', align:'center'},
				{header: "索赔经销商", dataIndex: 'DEALER_NAME', align:'center'},
				{header: "货运订单号", dataIndex: 'DO_NO', align:'center'},
				{header: "签收日期", dataIndex: 'SIGN_DATE', align:'center'},
				{header: "索赔日期", dataIndex: 'APPLY_DATE', align:'center'},
				{header: "签收明细项", dataIndex: 'SIGN_COUNT', align:'center'},
				{header: "索赔明细项", dataIndex: 'CLAIM_COUNT', align:'center'},
				{id:'action', header: "操作", sortable: false, dataIndex: 'CLAIM_ID', renderer:oper, align:'center'}
		      ];

	//点击审核
	function oper(value,meta,record) {
		return String.format("<a href=\"#\" onclick='check(\""+value+"\", \""+record.data.CLAIM_NO+"\")'>[审核]</a>");
	}
	function check(claimId, claimNo) {
		fm.method = 'post';
		fm.action = '<%=contextPath%>/partsmanage/partclaim/PartClaimCheck/partClaimDetail.do?claimId=' + claimId + '&claimNo=' + claimNo;
		fm.submit();
	}
	
	//清除组织代码
	function resetOrg() {
		document.getElementById("orgCode").value = "";
	}
	
	//清除经销商代码
	function resetDealer() {
		document.getElementById("dealerCode").value = "";
	}
	
	function checkType(val) {
		if (val==0) {
			document.getElementById("dealerCode").disabled = true;
			document.getElementById("deaBtn").disabled = true;
			document.getElementById("deaRe").disabled = true;
			document.getElementById("dealerName").disabled = true;
			document.getElementById("orgCode").disabled = false;
			document.getElementById("orgSel").disabled = false;
			document.getElementById("orgRe").disabled = false;
		} else {
			document.getElementById("orgCode").disabled = true;
			document.getElementById("orgSel").disabled = true;
			document.getElementById("orgRe").disabled = true;
			document.getElementById("dealerCode").disabled = false;
			document.getElementById("deaBtn").disabled = false;
			document.getElementById("deaRe").disabled = false;
			document.getElementById("dealerName").disabled = false;
		}
	}
	
</script>
</body>
</html>