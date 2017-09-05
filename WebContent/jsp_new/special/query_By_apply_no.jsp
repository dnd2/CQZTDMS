<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%
	String contextPath = request.getContextPath();
	String apply_no = request.getParameter("apply_no");
%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
	<title>数据字典查询</title>

	<script type=text/javascript>
		var myPage;
		var title = null;
		var url = "<%=contextPath%>/SpecialAction/query_Byapplyno.json";
		var columns = [
			{header: "序号",align:'center',renderer:getIndex},
			{header: "选择", align:'center',dataIndex: 'CODE_ID',renderer:setSelect},
			{header: "申请单号",dataIndex: 'APPLY_NO',align:'center'},
			{header: "索赔单号",dataIndex: 'CLAIM_NO',align:'center'}
	      ];
		
		function setSelect(value, mata, record) {
		  var appno=record.data.APPLY_NO;//申请单号
		  var CLAIM_NO=record.data.CLAIM_NO;//索赔单号
		  var DEALER_CONTACT=record.data.DEALER_CONTACT;//联系人
		  var DEALER_PHONE=record.data.DEALER_PHONE;//联系人电话
		  var SUPPLY_CODE_DEALER=record.data.SUPPLY_CODE_DEALER;//供应商代码
		  var APPLY_MONEY=record.data.APPLY_MONEY;//申报金额
		  var VIN=record.data.VIN;
		   var MODEL_NAME=record.data.MODEL_NAME;//车型
		   var SPE_ID=record.data.SPE_ID;
		   var str="<input type='radio' name='checkId' onclick='checkRadio(\""+appno+"\",\""+CLAIM_NO+"\",\""+SPE_ID+
		   "\",\""+DEALER_CONTACT+"\",\""+DEALER_PHONE+"\",\""+SUPPLY_CODE_DEALER+"\",\""+APPLY_MONEY+"\",\""+VIN+"\",\""+MODEL_NAME+"\");' />";
		   return String.format(str);
		}
		function checkRadio(appno,CLAIM_NO,SPE_ID,DEALER_CONTACT,DEALER_PHONE,SUPPLY_CODE_DEALER,APPLY_MONEY,VIN,MODEL_NAME){
		if (parent.$('inIframe')) {
			parentContainer.myCheck(appno,CLAIM_NO,SPE_ID,DEALER_CONTACT,DEALER_PHONE,SUPPLY_CODE_DEALER,APPLY_MONEY,VIN,MODEL_NAME);
		}else{
			parent.myCheck(appno,CLAIM_NO,SPE_ID,DEALER_CONTACT,DEALER_PHONE,SUPPLY_CODE_DEALER,APPLY_MONEY,VIN,MODEL_NAME);
		}
		_hide();
	}
		function doInit() {
			loadcalendar();
			__extQuery__(1);
		}
		function choose(appno){
		   MyAlert(appno);
		   parentContainer.choose(appno);
		}
	</script>
</head>
<body>
	<div class="wbox">
		<div class="navigation">
			<img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置：售后服务管理&gt;特殊费用管理&gt;申请单号选择
		</div>
		<form method="post" name="fm" id="fm">
			<input type = "hidden" id = "CODE_TYPE" name ="CODE_TYPE" value = "${CODE_TYPE }" />
			<input type = "hidden" id = "NOT_CODE_ID" name ="NOT_CODE_ID" value = "${NOT_CODE_ID }" />
			<table class="table_query">
				<tr>
					<th width="100" align="left" colspan="4">
						<img class="nav" src="<%=contextPath%>/img/subNav.gif" /> 查询条件
					</th>
				</tr>
				<tr>
					<td align="center" colspan="4">
						<input class="cssbutton" type="button" value="查询" onclick="__extQuery__(1)" name="BtnQuery" id="queryBtn">
						&nbsp;&nbsp;
					</td>
				</tr>
			</table>
			<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
			<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
		</form>
	</div>
</body>
</html>

