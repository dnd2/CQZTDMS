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
<title> 发运组板明细查询 </title>
</head>

<body>
	<div class="navigation"><img src="<%=contextPath %>/img/nav.gif" />&nbsp;当前位置：储运管理>发运管理> 发运组板明细查询
	</div>
<form name="fm" method="post" id="fm">
<!-- 查询条件 begin -->
<table class="table_query" id="subtab">
<tr class="csstr" align="center"> 
<td class="right" width="15%">产地：</td> 
	  <td align="left">
		 <select name="YIELDLY" id="YIELDLY" class="selectlist" >
		 <option value="">--请选择--</option>
				<c:if test="${list!=null}">
					<c:forEach items="${list}" var="list">
						<option value="${list.AREA_ID}">${list.AREA_NAME}</option>
					</c:forEach>
				</c:if>
	  		</select>
		</td> 
	<td class="right">组板号：</td> 
	  <td align="left" >
		  <input type="text" maxlength="20"  id=BO_NO name="BO_NO" class="middle_txt" size="15" />
	  </td>	 
		</tr>
  <tr class="csstr" align="center">  
   <td class="right" nowrap="true">组板日期：</td>
		<td align="left" nowrap="true">
			<input name="RAISE_STARTDATE" type="text" maxlength="20"  class="middle_txt" id="RAISE_STARTDATE" readonly="readonly"/> 
			<input name="button" type="button" class="time_ico" onclick="showcalendar(event, 'RAISE_STARTDATE', false);" />  	
             &nbsp;至&nbsp;
             <input name="RAISE_ENDDATE" type="text" maxlength="20"  class="middle_txt" id="RAISE_ENDDATE" readonly="readonly"/> 
			<input name="button" type="button" class="time_ico" onclick="showcalendar(event, 'RAISE_ENDDATE', false);" /> 
		</td>	
	<td class="right">承运商：</td> 
	  <td align="left">
		 <select name="LOGI_NAME" id="LOGI_NAME" class="selectlist" >
		 	<option value="">-请选择-</option>
				<c:if test="${list_logi!=null}">
					<c:forEach items="${list_logi}" var="list_logi">
						<option value="${list_logi.LOGI_ID}">${list_logi.LOGI_NAME}</option>
					</c:forEach>
				</c:if>
	  		</select>
	  </td>
</tr>
<tr class="csstr" align="center">  
    <td class="right">发运方式：</td> 
	  <td align="left">
		   <label>
				<script type="text/javascript">
						genSelBoxExp("TRANSPORT_TYPE",<%=Constant.TRANSPORT_TYPE%>,"",true,"selectlist","","false",'');
					</script>
			</label>
	  </td>	
	  	  <td class="right">选择经销商：</td>
		<td align="left">
			<input type="text" maxlength="20"   name="dealerName" readonly="readonly" class="middle_txt" size="15" value="" id="dealerName"/>
			<input name="button2" type="button" class="mini_btn" onclick="showOrgDealer('dealerCode','','true','','','','','dealerName');" value="..." />
			<input class="normal_btn" type="button" value="清空" onclick="clrTxt('dealerCode');clrTxt('dealerName');"/>
			<input type="hidden"  name="dealerCode" class="middle_txt" size="15" value="" id="dealerCode"/>
			
		</td>
	   
</tr>
  <tr align="center">
  <td colspan="6" class="table_query_4Col_input" style="text-align: center">
  		  <input type="reset" class="u-button u-reset" id="resetButton"  value="重置"/>
    	  <input type="button" id="queryBtn" class="u-button u-query" value="查询" onclick="doQuery()" />
    	  <!-- <input type="button" id="printButton" class="normal_btn"  value="导出" onclick="exportToExcel();" /> -->
    	     	 	
    </td>
  </tr>
</table>
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
<script type="text/javascript" >
	var myPage;
	//查询路径           
	var url = "<%=contextPath%>/sales/storage/sendmanage/SendBoardSeachMs/sendBoardSeachQuery.json";
	var title = null;
	var columns = [
				{header: "序号",align:'center',renderer:getIndex},
				{header: "组板号",dataIndex: 'BO_NO',align:'center'},
				//{header: "是否有零售",dataIndex: 'HAVE_RETAIL',align:'center'},
				//{header: "组板人",dataIndex: 'NAME',align:'center'},
				{header: "组板时间",dataIndex: 'BO_DATE',align:'center'},
				{header: "发运经销商",dataIndex: 'DEALER_NAME',align:'center'},
				{header: "发运省份",dataIndex: 'P_NAME',align:'center'},
				{header: "发运城市",dataIndex: 'C_NAME',align:'center'},
				{header: "发运区县",dataIndex: 'D_NAME',align:'center'},
				{header: "组板数量",dataIndex: 'BO_NUM',align:'center'},
				{header: "配车数量",dataIndex: 'ALLOCA_NUM',align:'center'},
				{header: "出库数量",dataIndex: 'OUT_NUM',align:'center'},
				{header: "发运数量",dataIndex: 'SEND_NUM',align:'center'},
				{header: "验收数量",dataIndex: 'ACC_NUM',align:'center'}
		      ];
	//初始化    
	function doInit(){
		//日期控件初始化
		//__extQuery__(1);
	}
	function doQuery(){
		document.getElementById("a1").innerHTML = '';
		document.getElementById("a2").innerHTML = '';
		document.getElementById("a3").innerHTML = '';
		document.getElementById("a4").innerHTML = '';
		document.getElementById("a5").innerHTML = '';
		makeNomalFormCall("<%=contextPath%>/sales/storage/sendmanage/SendBoardSeachMs/sendBoardSeachQuery.json?common=1",function(json){
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
    function exportToExcel() {
    	fm.action = '<%=contextPath%>/sales/storage/sendmanage/SendBoardSeachMs/sendBoardSeachQuery.do?common=2' ;
		fm.submit();
	}
</script>
</body>
</html>
