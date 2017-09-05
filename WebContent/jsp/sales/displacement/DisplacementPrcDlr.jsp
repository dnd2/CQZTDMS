<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.Constant"%>
<%@ page import="com.infodms.dms.util.CommonUtils"%>
<%@ page import="com.infodms.dms.po.TtDealerActualSalesPO"%>
<%@ page import="com.infodms.dms.po.TtCustomerPO"%>
<%@taglib uri="/jstl/cout" prefix="c"%>
<%@ page import="java.util.Map"%>
<%@ page import="java.util.List"%>

<%
	String contextPath = request.getContextPath();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<script type="text/javascript">
	function myOnLoad(){
		loadcalendar();   //初始化时间控件
	}
	
	function isSubmit() {
		if(submitForm("fm")) {
			MyConfirm("确认操作？", Submit) ;
		}
	}
	
	function Submit(){
		$('fm').action="<%=contextPath%>/sales/displacement/DisplacementCarPrice/dlrPrcOpera.do";
		$('fm').submit();
	}
	
	function returnSubmit(){
			history.back();
		}
	function txtClr(valueId) {
		document.getElementById(valueId).value = '' ;
	}
	
	function dlrDealer(value) {
		var sValueStr = document.getElementById("delDlr").value ;
		
		var iLen = sValueStr.length ;
		
		if(!iLen) {
			sValueStr += value ;
		} else {
			sValueStr += "," + value ;
		}
		
		document.getElementById("delDlr").value = sValueStr ;
	}
	
	function clearHid(value) {
		document.getElementById(value).value = "" ;
	}
	
	function query() {
		__extQuery__(1);
		clearHid("delDlr") ;
	}
</script>
<title>二手车置换经销商资格维护</title>
</head>
<body onload="myOnLoad();">
<div class="wbox">
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 整车销售 &gt; 二手车置换 &gt; 二手车置换经销商资格维护</div>
<form id="fm" name="fm" method="post">
<input type="hidden" name="dealerId2" size="15" id="dealerId2" value="" />
<input type="hidden" value="<%=Constant.DisplancementCarrequ_replace_1%>" name="displacement_type"></input>
<div id="divcompany_table" style="">
<table class="table_edit" align="center" id="company_table">
	<tr>
		<td width="15%" align="right">置换资格：</td>
		<td width="30%" align="left">
		<script type="text/javascript">
					genSelBoxExp("DISPLACEMENT_PRC",<%=Constant.DisplancementCarrequ_zige%>,"",false,"short_sel",'onchange=\"query();\"',"false",'');
			</script>
		</td>
		<td width="15%" align="right">选择经销商：</td>
		<td width="30%" align="left">
				<input type="text" class="middle_txt" style="width:100px" name="dealerCode" size="15" value="" id="dealerCode" readonly="readonly"/>
				<input name="button2" type="button" class="mini_btn" onclick="showOrgDealer('dealerCode','dlrId','true','',true);" value="..." />
				<input type="hidden" name="dlrId" id="dlrId" value="" />
				<input class="normal_btn" type="button" value="清空" onclick="clearHid('dealerCode');clearHid('dlrId');"/>
		</td>
		<td></td>
	</tr>
	<tr>
		<td width="15%" align="right"></td>
		<td width="30%" align="left"></td>
		<td width="15%" align="right"></td>
		<td width="30%" align="left"></td>
		<td>
			<input type="hidden" name="delDlr" id="delDlr" value="" />
			<input type="hidden" name="areas" id="areas" value="${areas }" />
			<input type="button" class="normal_btn" name="queryBtn" id="queryBtn" value="查询" onclick="query();" />
		</td>
	</tr>
</table>
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" /> 
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
</div>
</form>
<form id="form1" name="form1">
	<table  class="table_edit" align="center" >
	<tr class="cssTable">
		<td align="center" colspan="4">
			<input type="button" name="mySubmit"  class="normal_btn" value="提交" onclick="isSubmit();" />
			<input type="button" name="mySubmit"  class="normal_btn" value="返回" onclick="returnSubmit();" />

		</td>
	</tr>
</table>
</form>
</div>
<script type="text/javascript">
	var myPage;
	
	var url = "<%=contextPath%>/sales/displacement/DisplacementCarPrice/DisplacementPrcDelaerShow.json?COMMAND=1";
	
	var title = null;
	
	var columns = [
	            {id:'check',header: "<input type='checkbox' name='checkAll' onclick='selectAll(this,\"dealerIds\");' />", width:'6%',sortable: false,dataIndex: 'DEALER_ID',renderer:myCheckBox}, //<input type='checkbox' name='checkAll' onclick='selectAll(this,\"vehicleIds\")' />
	            {header: "组织", dataIndex: 'ROOT_ORG_NAME', align:'center'},
	            {header: "经销商代码", dataIndex: 'DEALER_CODE', align:'center'},
				{header: "经销商名称", dataIndex: 'DEALER_NAME', align:'center'}
		      ];

	document.form1.style.display = "none";
	
	var HIDDEN_ARRAY_IDS=['form1'];
	
	function myCheckBox(value,metaDate,record){
		var isChecked = record.data.ISCHECKED ;
		
		if(isChecked == 1) {
			return String.format("<input type=\"checkbox\" name='dealerIds' value='" + value + "' onclick='dlrDealer(this.value)' checked='checked' />");
		} else {
			return String.format("<input type=\"checkbox\" name='dealerIds' value='" + value + "' />");
		}
	}
</script>
</body>
</html>