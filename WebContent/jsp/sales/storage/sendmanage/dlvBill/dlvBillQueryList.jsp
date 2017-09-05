<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/DTD/xhtml1-transitional.dtd">
<%@page import="java.util.List"%>
<%@page import="java.util.Map"%>

<html xmlns="http://www.w3.org/1999/xhtml"><%@ page import="com.infodms.dms.common.Constant"%><%@ page import="com.infodms.dms.common.FileConstant"%><%@taglib uri="/jstl/cout" prefix="c"%>

<head>
<%
String contextPath=request.getContextPath();
%>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>发运交接单查询</title>
</head>

<body onload="doInit();">
	<div class="navigation"><img src="<%=contextPath %>/img/nav.gif" />&nbsp;当前位置：储运管理>发运管理>发运交接单查询
	</div>
<form name="fm" method="post" id="fm">
<div class="form-panel">
	<h2><img src="<%=request.getContextPath()%>/jmstyle/img/search-ico.png" style="margin-bottom: -13px;"/>发运交接单查询</h2>
<div class="form-body">
<!-- 查询条件 begin -->
<table class="table_query" id="subtab">
<tr>
		<td class="right">选择经销商：</td>
		<td align="left">
	     		<input name="dealerName" type="text" maxlength="20"  id="dealerName" class="middle_txt" value=""  readonly="readonly"/>
	           <input name="dlbtn" type="button" class="mini_btn" onclick="showOrgDealer('dealerCode', '', 'true', '', 'true','','<%=Constant.DEALER_TYPE_DVS %>,<%=Constant.DEALER_TYPE_DP %>','dealerName');" value="..." />
	   		<input type="button" class="normal_btn" onclick="txtClr('dealerCode','dealerName');" value="清 空" id="clrBtn" />
			<input name="dealerCode" type="hidden" id="dealerCode" class="middle_txt" value="" />
		</td>	
		<td class="right">发运仓库：</td> 
		<td align="left">
			<select name="YIELDLY" id="YIELDLY" class="u-select" >
			<option value="">--请选择--</option>
				<c:if test="${list!=null}">
					<c:forEach items="${list}" var="list">
						<option value="${list.AREA_ID}">${list.AREA_NAME}</option>
					</c:forEach>
				</c:if>
	  		</select>
		</td>
		<td class="right">发运结算省份：</td>  
		<td align="left">
			<select class="u-select" id="txt1" name="jsProvince" onchange="_genCity(this,'txt2')"></select>
		</td>
	</tr>
	<tr>    
		<td class="right">批售或调拨单号：</td> 
		<td align="left" >
			<input type="text" maxlength="20"  id="orderNo" name="orderNo" datatype="1,is_digit_letter,30" maxlength="30" class="middle_txt" size="15" />
		</td>
		<td class="right">发运方式：</td> 
		<td align="left">
		   <label>
				<script type="text/javascript">
						genSelBoxExp("TRANSPORT_TYPE",<%=Constant.TT_TRANS_WAY%>,"",true,"u-select","","false",'');
					</script>
			</label>
		</td>
		<td class="right">发运结算城市：</td>  
		<td align="left">
	  		<select class="u-select" id="txt2" name="jsCity" onchange="_genCity(this,'txt3')"></select>
		</td>
	</tr>
	<tr>  			
		<td class="right">交接单号：</td> 
		<td align="left" >
			<input type="text" maxlength="20"  id="billNo" name="billNo" datatype="1,is_digit_letter,30" maxlength="30" class="middle_txt" size="15" />
		</td>	
		<td class="right">承运商：</td> 
		<td align="left">
			<select name="LOGI_NAME" id="LOGI_NAME" class="u-select" >
				<option value="">-请选择-</option>
				<c:if test="${list_logi!=null}">
					<c:forEach items="${list_logi}" var="list_logi">
						<option value="${list_logi.LOGI_ID}">${list_logi.LOGI_NAME}</option>
					</c:forEach>
				</c:if>
		  	</select>
		</td> 
		<td class="right">发运结算区县：</td>
	   	<td align="left">
	 			<select class="u-select" id="txt3" name="jsCounty"></select>
		</td>
	</tr>
	<tr>  	
		<td class="right">组板号：</td> 
		<td align="left" >
			<input type="text" maxlength="20"  id="BO_NO" name="BO_NO" datatype="1,is_digit_letter,30" maxlength="30" class="middle_txt" size="15" />
		</td>
		<td class="right">状态：</td> 
		<td align="left" >
			<label>
				<script type="text/javascript">
						genSelBoxExp("BILL_STATUS",<%=Constant.WAYBILL_STATUS%>,"",true,"u-select","","false",'');
				</script>
			</label>
		</td>
		<td class="right"></td>
		<td align="left"></td>	
	</tr>
	<tr>	 
	  	<td class="right">交接日期：</td>
		<td align="left">
			<input class="short_txt" readonly="readonly"  type="text" id="START_DATE" name="START_DATE" onFocus="WdatePicker({el:$dp.$('START_DATE'), maxDate:'#F{$dp.$D(\'END_DATE\')}'})"  style="cursor: pointer;width: 80px;"/>&nbsp;至&nbsp;
			<input class="short_txt" readonly="readonly"  type="text" id="END_DATE" name="END_DATE" onFocus="WdatePicker({el:$dp.$('END_DATE'), minDate:'#F{$dp.$D(\'START_DATE\')}'})"  style="cursor: pointer;width: 80px;"/>
		</td>	
		<td class="right">最后交车日期：</td>
		<td align="left">
			<input class="short_txt" readonly="readonly"  type="text" id="LAST_START_DATE" name="LAST_START_DATE" onFocus="WdatePicker({el:$dp.$('LAST_START_DATE'), maxDate:'#F{$dp.$D(\'LAST_END_DATE\')}'})"  style="cursor: pointer;width: 80px;"/>&nbsp;至&nbsp;
			<input class="short_txt" readonly="readonly"  type="text" id="LAST_END_DATE" name="LAST_END_DATE" onFocus="WdatePicker({el:$dp.$('LAST_END_DATE'), minDate:'#F{$dp.$D(\'LAST_START_DATE\')}'})"  style="cursor: pointer;width: 80px;"/>
		</td>	
		<td class="right"></td>
		<td align="left"></td>
	</tr>
	<tr align="center">
	  <td colspan="6" class="table_query_4Col_input" style="text-align: center">
    	  <input type="button" id="queryBtn" class="u-button u-query" value="查询" onclick="doQuery()" />
  		  <input type="reset" class="u-button u-reset" id="resetButton"  value="重置"/>
    	  <input type="button" id="saveButton" class="normal_btn"  value="导出" onclick="downLoad();" /> 	
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
	var url = "<%=contextPath%>/sales/storage/sendmanage/DlvWayBillManage/DlvBillManageQuery.json";
	var title = null;
	var columns = [
				{header: "序号",align:'center',renderer:getIndex},
				{
					header: "交接单号", dataIndex: 'BILL_NO', align:'center', 
					renderer: function(value, metaData, record) {
						var url = '<%=contextPath%>/sales/storage/sendmanage/DlvWayBillManage/showBillDetailInit.do?billId=' + record.data.BILL_ID;
						return "<a href='javascript:;' onclick='viewOrderInfo(\""+url+"\")'>"+value+"</a>";
					}
				},
				//{header: "交接单号",dataIndex: 'BILL_NO',align:'center'},
				{header: "订单号",dataIndex: 'ORDER_NO',align:'center'},
 				{header: "发运仓库",dataIndex: 'WH_NAME',align:'center'},
				{header: "经销商",dataIndex: 'DEALER_NAME',align:'center'},
				{header: "发运方式",dataIndex: 'SHIP_NAME',align:'center'},
				{header: "承运商",dataIndex: 'LOGI_NAME',align:'center'},
				{header: "发运结算地",dataIndex: 'BAL_ADDR',align:'center'},
				{header: "组板号",dataIndex: 'BO_NO',align:'center'},
				{header: "交接量",dataIndex: 'VEH_NUM',align:'center'},
				{header: "最晚发运日期",dataIndex: 'DLV_FY_DATE',align:'center'},
				{header: "最晚到货日期",dataIndex: 'DLV_JJ_DATE',align:'center'},
				{header: "状态",dataIndex: 'STATUS_NAME',align:'center'},
				{header: "交接日期",dataIndex: 'BILL_CRT_DATE',align:'center'},
				{header: "最后交车时间",dataIndex: 'LAST_CAR_DATE',align:'center'}
		      ];
	//初始化    
	function doInit(){
		genLocSel('txt1','','');//支持火狐
		//__extQuery__(1);
	}
	function doQuery(){
		__extQuery__(1);
	}
	function viewOrderInfo(url)
	{
		OpenHtmlWindow(url,1000,450);
	}
	//清空数据
	function clrTxt(txtId){
    	document.getElementById(txtId).value = "";
    }
	//查询结果导出
    function downLoad() 
	{
		document.getElementById('fm').action= "<%=contextPath%>/sales/storage/sendmanage/DlvWayBillManage/DlvBillManageQuery.do?common=1";
		document.getElementById('fm').submit();
	}
	
</script>
</body>
</html>
