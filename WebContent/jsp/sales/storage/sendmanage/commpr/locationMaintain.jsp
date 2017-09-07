<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml"><%@ page import="com.infodms.dms.common.Constant"%><%@ page import="com.infodms.dms.common.FileConstant"%><%@taglib uri="/jstl/cout" prefix="c"%>

<%@taglib uri="/jstl/cout" prefix="c" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>在途位置维护</title>
<% String contextPath = request.getContextPath(); %>
<script type="text/javascript">
</script>
</head>
<body>
<div class="navigation"><img src="<%=request.getContextPath()%>/img/nav.gif" />&nbsp;当前位置：储运管理>发运管理>在途位置维护</div>
<form method="post" name="fm" id="fm">
<div class="form-panel">
	<h2><img src="<%=request.getContextPath()%>/jmstyle/img/search-ico.png" style="margin-bottom: -13px;"/>在途位置维护</h2>
<div class="form-body">
<!-- 查询条件 begin -->
	<table class="table_query" >
		<tr>
			<td class="right">交接单号：</td>
			<td align="left">
				<input type="text" maxlength="20"  name="shipNo" datatype="1,is_digit_letter,30" maxlength="30" id="shipNo" maxlength="20" class="middle_txt"/>
			</td>
			<td class="right">选择经销商：</td>
		<td align="left">
      		<input name="dealerName" type="text" maxlength="20"  id="dealerName" class="middle_txt" value=""  readonly="readonly"/>
            <input name="dlbtn" type="button" class="mini_btn" onclick="showOrgDealer('dealerCode', '', 'true', '', 'true','','<%=Constant.DEALER_TYPE_DVS %>,<%=Constant.DEALER_TYPE_DP %>','dealerName');" value="..." />
    		<input type="button" class="normal_btn" onclick="txtClr('dealerCode','dealerName');" value="清 空" id="clrBtn" />
			<input name="dealerCode" type="hidden" id="dealerCode" class="middle_txt" value="" />
		</td>
		</tr>
		<tr>
			<td class="right">承运商：</td>
			<td align="left">
		 	<select name="LOGI_NAME_SEACH" id="LOGI_NAME_SEACH" class="u-select" >
		 		<option value="">-请选择-</option>
				<c:if test="${list_logi!=null}">
					<c:forEach items="${list_logi}" var="list_logi">
						<option value="${list_logi.LOGI_ID}">${list_logi.LOGI_NAME}</option>
					</c:forEach>
				</c:if>
	  		</select>
	  		</td>
			<td class="right">交接日期：</td>
			<td align="left">
				<input class="short_txt" readonly="readonly"  type="text" id="startDate" name="startDate" onFocus="WdatePicker({el:$dp.$('startDate'), maxDate:'#F{$dp.$D(\'endDate\')}'})"  style="cursor: pointer;width: 80px;"/>&nbsp;至&nbsp;
				<input class="short_txt" readonly="readonly"  type="text" id="endDate" name="endDate" onFocus="WdatePicker({el:$dp.$('endDate'), minDate:'#F{$dp.$D(\'startDate\')}'})"  style="cursor: pointer;width: 80px;"/>
			</td>
		</tr>
		<tr>
			<td colspan="4" class="table_query_4Col_input" style="text-align: center">
				<input name="queryBtn" type=button class="u-button u-query" onClick="__extQuery__(1);" value="查询">
				<input type=reset class="u-button u-reset" value="重置">
			</td>
		</tr>
	</table>
	</div>
</div>
<!-- 查询条件 end -->
<!--分页 begin -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
<!--分页 end --> 
</form>
<!--页面列表 begin -->
<script type="text/javascript" >
	var myPage;
	//查询路径         
	var url = "<%=contextPath%>/sales/storage/sendmanage/OnTheWayAction/locationMaintainInit.json?query=query";
	var title = null;
	var columns = [
					{header: "序号", dataIndex: '', align:'center', renderer:getIndex},
	              	{header: "承运商",dataIndex: 'LOGI_NAME',align:'center'},
	                //{header: "交接单号",dataIndex: 'BILL_NO',align:'center'},
	                {
						header: "交接单号", dataIndex: 'BILL_NO', align:'center', 
						renderer: function(value, metaData, record) {
							var url = '<%=contextPath%>/sales/storage/sendmanage/DlvWayBillManage/showBillDetailInit.do?billId=' + record.data.BILL_ID;
							return "<a href='javascript:;' onclick='viewOrderInfo(\""+url+"\")'>"+value+"</a>";
						}
					},
	                {header: "经销商/收货仓库",dataIndex: 'DEALER_NAME',align:'center'},
	                {header: "详细地址",dataIndex: 'ADDRESS', style:'text-align:left;'},
	                {header: "交接日期",dataIndex: 'BILL_CRT_DATE',align:'center'},
					{header: "操作",sortable: false, dataIndex: 'BILL_ID', align:'center',renderer:myLink}
		      ];
	//超链接设置
    function myLink(value,meta,record){
    	return String.format("<a href='javascript:void(0);' class='u-anchor' onclick='toDetailCheck(\""+ value +"\")'>维护</a>");
	}
	//明细链接
	function toDetailCheck(value){
		window.location.href = '<%=contextPath%>/sales/storage/sendmanage/OnTheWayAction/locationMaintainChInit.do?&billId='+value;
	}
	
	function clrTxt(txtId){
    	document.getElementById(txtId).value = "";
    }
	function txtClr(txtId){
    	document.getElementById(txtId).value = "";
    }

	//初始化    
	function doInit(){
		  //初始化时间控件
	}
	//清空数据
	function txtClr(valueId1,valueId2) {
		document.getElementById(valueId1).value = '' ;
		document.getElementById(valueId2).value = '' ;
	}
	function viewOrderInfo(url)
	{
		OpenHtmlWindow(url,1000,450);
	}
</script>
<!--页面列表 end -->
</body>
</html>