<%@page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
	String contextPath = request.getContextPath();
%>
<%@ page import="com.infodms.dms.common.Constant"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<script type="text/javascript">
  
</script>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>PIN码查看回复</title>
</head>
<body onload="__extQuery__(1);">
<div class="wbox">
	<div class="navigation"><font style="font-size: 13px;font-family:serif;"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 售后服务管理 &gt; 车辆信息管理管理&gt; PIN码查看回复</font></div>
	<form id="fm" name="fm" method="post">
	<input type="hidden" name="curPage" id="curPage" value="1" />
	<div class="form-panel">
	<h2><img src="<%=contextPath%>/jmstyle/img/search-ico.png" style="margin-bottom: -13px;"/>查询条件</h2>
	<div class="form-body">
	<table class="table_query">
		<tr >
				<td style="text-align:right">维修站名称：
				</td>
				<td >
					<input class="middle_txt"  type="text" name="dealer_name" id="dealer_name" maxlength="18"/>
				</td>
				<td style="text-align:right">VIN码：
				</td>
				<td >
					<input class="middle_txt"  type="text" name="vin" id="vin" maxlength="18"/>
				</td>
				<td style="text-align:right">单据编码：
				</td>
				<td >
					<input class="middle_txt"  type="text" name="pinNo" id="pinNo" maxlength="18"/>
				</td>
			 </tr>
			 <tr>
			 <td style="text-align:right">申请状态：
				</td>
				<td >
					<script type="text/javascript"> genSelBoxExp("status",<%=Constant.PIN_APPLY_STATUS%>,"","true","","","false",'<%=Constant.PIN_APPLY_STATUS_01%>');
					</script>
				</td>
				<td style="text-align:right">经销商：</td>
        		<td >
        	<input name="dealerCode" id="dealerCode" type="text" class="middle_txt" onclick="showOrgDealer('dealerCode','dealerId','true','','true','','10771002');" readonly="readonly" />
        	<input name="dealerId" id="dealerId" type="hidden" class="middle_txt" readonly="readonly" />
            <!-- <input class="mini_btn" type="button" id="dealerBtn" value="&hellip;" onclick="showOrgDealer('dealerCode','dealerId','true','','true','','10771002');"/>     -->
           	<input class="normal_btn" type="button" value="清空" onclick="oemTxt('dealerId','dealerCode');"/>
        </td>
				<td style="text-align:right">起始日期：</td>
				<td >
					<input class="middle_txt"  type="text" style="width:80px;" readonly="readonly" name="creatDate" id="creatDate" onclick="WdatePicker({maxDate:'#F{$dp.$D(\'outPlantDate\')}'})" />
					至：
					<input class="middle_txt"  type="text" style="width:80px;" readonly="readonly" name="outPlantDate" id="outPlantDate" onclick="WdatePicker({minDate:'#F{$dp.$D(\'creatDate\')}'})" />
				</td>
        </tr>
			 <tr >				
				 <td colspan="6"  style="text-align:center">
					<input name="queryBtn" type="button" class="u-button u-query" onclick="__extQuery__(1)" value="查 询" id="queryBtn" /> &nbsp; 
					<input type="reset" class="u-button u-cancel" value="重 置"/> &nbsp;
			   	 </td> 
			</tr>
	</table>
	</div>
	</div>
	<!--分页  -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
	<script type="text/javascript">
	var url = "<%=contextPath%>/afterSales/pinApply/PinApplyAction/pinFactoryList.json?flag=t";
	var myPage;
	//设置表格标题
	var title= null;

	var columns = [
	             	 
					{header: "序号",width:'10%',align:'center',  renderer:getIndex},
					{id:'action',header: "操作",width:'10%',align:'center',dataIndex: 'ID',renderer:myLink},
					{id:'action',header: "申请状态", width:'10%',align:'center', dataIndex: 'STATUS',renderer: getItemValue},
					{header: "单据编码", width:'10%',align:'center', dataIndex: 'PIN_NO'},//
					{header: "VIN号", width:'10%',align:'center', dataIndex: 'VIN'},
					{header: "经销商编码", width:'10%',align:'center', dataIndex: 'DEALER_CODE'},//
					{header: "经销商名称", width:'10%',align:'center', dataIndex: 'DEALER_NAME'},//
					{header: "审核人", width:'10%',align:'center', dataIndex: 'ANAME'},
					{header: "审核时间", width:'10%',align:'center', dataIndex: 'AUDITOR_TIME'},
					{header: "创建人", width:'10%',align:'center', dataIndex: 'NAME'},
					{header: "创建时间", width:'10%',align:'center', dataIndex: 'CREATE_DATE'}					
					
				  ];  
	function myLink(value,meta,record){
		var status=record.data.STATUS;
		if(status==96251002){
			return String.format("<a class=\"u-anchor\" href=\"###\" onclick=\"fmFind("+value+"); \">查看</a>"+
			           "<a class=\"u-anchor\" href=\"###\" onclick=\"hf("+value+"); \">回复</a>");
		}else{
			return String.format("<a class=\"u-anchor\" href=\"###\" onclick=\"fmFind("+value+"); \">查看</a>");
		}
	}  
	 function hf(){
		 var url = "<%=contextPath%>/afterSales/pinApply/PinApplyAction/hfpin.do";
		 OpenHtmlWindow(url, 800, 500, 'PIN码查看申请回复');
			
		<%-- var form = document.getElementById("fm");
		form.action =  "<%=contextPath%>/afterSales/pinApply/PinApplyAction/hfpin.do";
		form.submit(); --%>
	} 
	 function Back(){
		 __extQuery__(1);
	}
	 function fmFind(value){
			OpenHtmlWindow( "<%=contextPath%>/afterSales/pinApply/PinApplyAction/sepin.do?id="+value,900,450);
	} 
	 function oemTxt(a,b){
			document.getElementById(a).value="";
			document.getElementById(b).value="";
			} 
</script>
</form>
</div>
</body>
</html>