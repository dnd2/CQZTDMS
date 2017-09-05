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
<title> 发运组板查询 </title>
</head>

<body>
	<div class="navigation"><img src="<%=contextPath %>/img/nav.gif" />&nbsp;当前位置：储运管理>发运管理>发运组板查询
	</div>
<form name="fm" method="post" id="fm">
<div class="form-panel">
	<h2><img src="<%=request.getContextPath()%>/jmstyle/img/search-ico.png" style="margin-bottom: -13px;"/>发运组板查询</h2>
<div class="form-body">
<!-- 查询条件 begin -->
<table class="table_query" id="subtab">
<tr class="csstr" align="center"> 
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
	  <td class="right">发运结算省份：</td>  
		    <td align="left">
	  		<select class="u-select" id="txt1" name="jsProvince" onchange="_genCity(this,'txt2')"></select>
     	 </td> 	  
</tr>
  <tr class="csstr" align="center">     
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
<tr class="csstr" align="center">      	
	<td class="right">组板号：</td> 
	  <td align="left" >
		  <input type="text" maxlength="20"  id=BO_NO name="BO_NO" datatype="1,is_digit_letter,30" maxlength="30" class="middle_txt" size="15" />
	  </td>	
     	 <td class="right">发运结算区县：</td>
   	  <td align="left">
 			<select class="u-select" id="txt3" name="jsCounty"></select>
	 </td>
</tr>
<tr class="csstr" align="center">  
    
	  <td class="right" nowrap="true">组板日期：</td>
		<td align="left" nowrap="true">
			<input class="short_txt" readonly="readonly"  type="text" id="START_DATE" name="START_DATE" onFocus="WdatePicker({el:$dp.$('START_DATE'), maxDate:'#F{$dp.$D(\'END_DATE\')}'})"  style="cursor: pointer;width: 80px;"/>&nbsp;至&nbsp;
			<input class="short_txt" readonly="readonly"  type="text" id="END_DATE" name="END_DATE" onFocus="WdatePicker({el:$dp.$('END_DATE'), minDate:'#F{$dp.$D(\'START_DATE\')}'})"  style="cursor: pointer;width: 80px;"/>
		</td>	
	  	  <td class="right">&nbsp;&nbsp;</td>
		<td align="left">
			&nbsp;&nbsp;
		</td>
	   
</tr>
  <tr align="center">
  <td colspan="6" class="table_query_4Col_input" style="text-align: center">
    	  <input type="button" id="queryBtn" class="u-button u-query" value="查询" onclick="doQuery()" />
  		  <input type="reset" class="u-button u-reset" id="resetButton"  value="重置"/>
    	  <input type="button" id="printButton" class="normal_btn"  value="导出" onclick="exportToExcel();" />
    	     	 	
    </td>
  </tr>
</table>
</div>
</div>
		<table class="table_list" style="margin-top: 1px; margin-bottom: 1px;">
			<tr class="table_list_row2">
				<td>组板总数：<span id="a1"></span></td>
				<td>配车总数：<span id="a2"></span></td>
				<td>出库总数：<span id="a3"></span></td>
				<td>发运总数: <span id="a4"></span></td>
				<td>验收总数: <span id="a5"></span></td>
			</tr>
		</table>
<!-- 查询条件 end -->
<!--分页 begin -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
<!--分页 end -->
</form>
<!--页面列表 begin -->
<script type="text/javascript" ><!--
	var myPage;
	//查询路径           
	var url = "<%=contextPath%>/sales/storage/sendmanage/SendBoardSeach/sendBoardSeachQuery.json";
	var title = null;
	var columns = [
				{header: "序号",align:'center',renderer:getIndex},
				{header: "操作",dataIndex: 'BO_ID',sortable: false, align:'center',renderer:myLink},
				{header: "组板号",dataIndex: 'BO_NO',align:'center'},
				{header: "发运方式",dataIndex: 'DLV_SHIP_TYPE',align:'center',renderer:getItemValue},
 				{header: "承运商",dataIndex: 'LOGI_NAME',align:'center'},
				{header: "发运结算地",dataIndex: 'BAL_ADDR',align:'center'},
				{header: "组板日期",dataIndex: 'BO_DATE',align:'center'},
				{header: "组板数量",dataIndex: 'BO_NUM',align:'center'},
				//{header: "配车数量",dataIndex: 'ALLOCA_NUM',align:'center'},
				//{header: "出库数量",dataIndex: 'OUT_NUM',align:'center'},
				{header: "发运数量",dataIndex: 'SEND_NUM',align:'center'},
				{header: "验收数量",dataIndex: 'ACC_NUM',align:'center'},
				{header: "审核人",dataIndex: 'AUDIT_BY',align:'center'},
				{header: "审核备注",dataIndex: 'AUDIT_REMARK',align:'center'},
				{header: "审核时间",dataIndex: 'AUDIT_TIME',align:'center'}
		      ];
	//初始化    
	function doInit(){
		genLocSel('txt1','','');//支持火狐
		//__extQuery__(1);
	}
	function doQuery(){
		document.getElementById("a1").innerHTML = '';
		document.getElementById("a2").innerHTML = '';
		document.getElementById("a3").innerHTML = '';
		document.getElementById("a4").innerHTML = '';
		document.getElementById("a5").innerHTML = '';
		makeNomalFormCall("<%=contextPath%>/sales/storage/sendmanage/SendBoardSeach/sendBoardSeachQuery.json?common=1",function(json){
			document.getElementById("a1").innerHTML = json.valueMap.BO_NUM == null ? '0' : json.valueMap.BO_NUM;
			document.getElementById("a2").innerHTML = json.valueMap.ALLOCA_NUM == null ? '0' : json.valueMap.ALLOCA_NUM;
			document.getElementById("a3").innerHTML = json.valueMap.OUT_NUM == null ? '0' : json.valueMap.OUT_NUM;
			document.getElementById("a4").innerHTML = json.valueMap.SEND_NUM == null ? '0' : json.valueMap.SEND_NUM;
			document.getElementById("a5").innerHTML = json.valueMap.ACC_NUM == null ? '0' : json.valueMap.ACC_NUM;
		},'fm');
		__extQuery__(1);
	}
	//清空数据
	function clrTxt(txtId){
    	document.getElementById(txtId).value = "";
    }
    function myLink(value,meta,record){
        var link="<a href='javascript:void(0);' class='u-anchor' onclick='seachSend(\""+value+"\")'>查看</a>";
        <%--if(record.data.HANDLE_STATUS==<%=Constant.HANDLE_STATUS01%> && record.data.BO_NO.substring(0,2)!='ZT'){//未配车才能修改,自提单不能修改
        	link+="<a href='javascript:void(0);' onclick='updateSend(\""+value+"\")'>[修改]</a>";
        }--%>
  		return String.format(link);
    }
    function updateSend(value,orderId,boNo){
   	 	window.location.href="<%=contextPath%>/sales/storage/sendmanage/SendBoardSeach/updateSendBordSeachInit.do?Id="+value;
     }
    function seachSend(value,orderId,boNo){
   	 	
   	 	var urlss="<%=contextPath%>/sales/storage/sendmanage/SendBoardSeach/seachInit.do?Id="+value;
    	OpenHtmlWindow(urlss,1000,450);
     }
    function exportToExcel() {
    	fm.action = '<%=contextPath%>/sales/storage/sendmanage/SendBoardSeach/sendBoardSeachQuery.do?common=2' ;
		fm.submit();
	}
</script>
</body>
</html>
