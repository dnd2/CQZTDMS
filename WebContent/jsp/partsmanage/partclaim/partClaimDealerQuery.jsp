<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="com.infodms.dms.common.Constant"%>
<%@taglib uri="/jstl/cout" prefix="c" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>配件索赔查询</title>
<% String contextPath = request.getContextPath(); %>
<script type="text/javascript">
	function doInit(){
   		loadcalendar();  //初始化时间控件
	}
</script>
</head>
<body>
<img src="<%=request.getContextPath()%>/img/nav.gif" />&nbsp;当前位置： 配件管理&gt;配件索赔&gt;配件索赔查询
<form name="fm" id="fm">
<!-- 查询条件 begin -->
<table class="table_edit">
    <tr>
      <td class="table_query_4Col_label_5Letter">索赔单号：</td>
      <td class="table_query_4Col_input">
      	<input name="claimNo" type="text" id="claimNo"  class="middle_txt"/>
      </td>
      <td class="table_query_4Col_label_4Letter">货运单号：</td>
      <td class="table_query_4Col_input" nowrap="nowrap">
      	<input name="doNo" type="text" id="doNo"  class="middle_txt"/>
      </td>
    </tr>
    <tr>
      <td class="table_query_4Col_label_5Letter">索赔日期：</td>
      <td class="table_query_4Col_input">
      	<div align="left">
        	<input name="beginDate" id="t1" value="" type="text" class="short_txt" datatype="1,is_date,10" group="t1,t2" hasbtn="true" callFunction="showcalendar(event, 't1', false);">
           		&nbsp;至&nbsp;
           	<input name="endDate" id="t2" value="" type="text" class="short_txt" datatype="1,is_date,10" group="t1,t2" hasbtn="true" callFunction="showcalendar(event, 't2', false);">
        </div>
      </td>
    	<td class="table_query_4Col_label_6Letter">审核状态：</td>
      	<td class="table_query_2Col_input">
	           <script type="text/javascript">     
 				  genSelBoxExp("checkStatus",<%=Constant.PART_CLAIM_STATUS%>,"",false,"short_sel","","false",'');
			   </script>
	    </td>
    </tr>
    <tr>
    	<td></td><td></td><td></td><td></td>
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
				//{header: "索赔经销商", dataIndex: 'DEALER_NAME', align:'center'},
				{header: "货运订单号", dataIndex: 'DO_NO', align:'center'},
				{header: "签收日期", dataIndex: 'SIGN_DATE', align:'center'},
				{header: "索赔日期", dataIndex: 'APPLY_DATE', align:'center'},
				{header: "签收明细项", dataIndex: 'SIGN_COUNT', align:'center'},
				{header: "索赔明细项", dataIndex: 'CLAIM_COUNT', align:'center'},
				{header: "审批状态", dataIndex: 'STATUS', align:'center', renderer:getItemValue},
				{id:'action', header: "操作", sortable: false, dataIndex: 'CLAIM_ID', renderer:oper, align:'center'}
		      ];

	//点击明细
	function oper(value,meta,record) {
		return String.format("<a href=\"#\" onclick='detail(\""+value+"\", \""+record.data.CLAIM_NO+"\", \""+record.data.SIGN_NO+"\")'>[明细]</a>");
	}
	function detail1(claimId, claimNo, signNo) {
		fm.method = 'post';
		fm.action = '<%=contextPath%>/partsmanage/partclaim/PartClaimCheck/partClaimQueryDetail.do?claimId=' + claimId + '&claimNo=' + claimNo + '&signNo=' + signNo;
		fm.submit();
	}
	function detail(claimId, claimNo, signNo) {
		OpenHtmlWindow('<%=contextPath%>/partsmanage/partclaim/PartClaimCheck/partClaimQueryDetail.do?claimId=' + claimId + '&claimNo=' + claimNo + '&signNo=' + signNo,700,550);
	}
</script>
</body>
</html>