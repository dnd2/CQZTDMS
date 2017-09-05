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
<title>运单查询 </title>
<script type="text/javascript">
function doInit(){
	  //初始化时间控件
	 genLocSel('txt1','','');//支持火狐
	//__extQuery__(1);
}
</script>
</head>
<body>
	<div class="navigation"><img src="<%=contextPath %>/img/nav.gif" />&nbsp;当前位置：储运管理>发运管理>运单查询
	</div>
<form id="fm" name="fm" method="post" >
<!-- 查询条件 begin -->
<table class="table_query" id="subtab">
<tr class="csstr" align="center">	
	<td class="right">选择经销商：</td>
		<td align="left">
			<input type="text" maxlength="20"   name="dealerName" readonly="readonly" class="middle_txt" size="15" value="" id="dealerName"/>
			<input name="button2" type="button" class="mini_btn" onclick="showOrgDealer('dealerCode','','true','','','','<%=Constant.DEALER_TYPE_DVS %>,<%=Constant.DEALER_TYPE_DP %>','dealerName');" value="..." />
			<input class="normal_btn" type="button" value="清空" onclick="clrTxt('dealerCode');clrTxt('dealerName');"/>
			<input type="hidden"  name="dealerCode" class="middle_txt" size="15" value="" id="dealerCode"/>
			
		</td>
		  <td class="right">运单号：</td> 
	  <td align="left">
		  <input type="text" maxlength="20"  id="ORDER_NO" name="ORDER_NO" datatype="1,is_digit_letter,30" maxlength="30" class="middle_txt" size="15" />
	  </td>	
	  	 <td class="right" nowrap="true">发运时间：</td>
   <td align="left" nowrap="true">
			<input name="sendStartDate" type="text" maxlength="20"  class="middle_txt" id="sendStartDate" readonly="readonly"/> 
			<input name="button" type="button" class="time_ico" onclick="showcalendar(event, 'sendStartDate', false);" />  	
             &nbsp;至&nbsp;
             <input name="sendEndDate" type="text" maxlength="20"  class="middle_txt" id="sendEndDate" readonly="readonly"/> 
			<input name="button" type="button" class="time_ico" onclick="showcalendar(event, 'sendEndDate', false);" /> 
		</td>	
 </tr>
 <tr class="csstr" align="center">	
 		<td class="right">省份：</td>  
		    <td align="left">
	  		<select class="u-select" id="txt1" name="PROVINCE" onchange="_genCity(this,'txt2')"></select>
     	 </td> 
     	 <td class="right">地级市：</td>  
		    <td align="left">
	  		<select class="u-select" id="txt2" name="CITY_ID"></select>
     	 </td>   
		  <td class="right">区县：</td>
		   	   <td align="left">
	  				<input type="text" maxlength="20"  id="COUNTY_ID" datatype="1,is_null,100" maxlength="100" name="COUNTY_ID" class="middle_txt"  size="15" />
			 </td> 
 </tr>
 <tr class="csstr" align="center">	
   <td class="right">产地：</td> 
	  <td align="left" >
		 <select name="YIELDLY" id="YIELDLY" class="selectlist">
				 <option value="">-请选择-</option>
				<c:if test="${list!=null}">
					<c:forEach items="${list}" var="list">
						<option value="${list.AREA_ID}">${list.AREA_NAME}</option>
					</c:forEach>
				</c:if>
	  		</select>
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
	  <td class="right">VIN：</td> 
	  <td align="left">
		  <input type="text" maxlength="20"  id="VIN" name="VIN" class="middle_txt" size="15" />
	  </td>	
 </tr> 
 <tr>
	 <td class="right">运单确认状态：</td>
	 	<td align="left">
	 		<script type="text/javascript">
	 			genSelBoxExp("isConfirm",1004,"",true,"u-select","","true",'');
	 		</script>
	 	</td> 
	 <td class="right" nowrap="true">运单确认时间：</td>
	 <td align="left" nowrap="true">
				<input name="confirmBeginDate" type="text" maxlength="20"  class="middle_txt" id="confirmBeginDate" readonly="readonly"/> 
				<input name="button" type="button" class="time_ico" onclick="showcalendar(event, 'confirmBeginDate', false);" />  	
	             &nbsp;至&nbsp;
	             <input name="confirmEndDate" type="text" maxlength="20"  class="middle_txt" id="confirmEndDate" readonly="readonly"/> 
				<input name="button" type="button" class="time_ico" onclick="showcalendar(event, 'confirmEndDate', false);" /> 
	 </td>	
 </tr>

  <tr align="center">
  <td colspan="6" class="table_query_4Col_input" style="text-align: center">
  		  <input type="reset" class="u-button u-reset" id="resetButton"  value="重置"/>
    	  <input type="button" id="queryBtn" class="u-button u-query" value="查询" onclick="_function(1)" />
    	  <input type="button" id="queryBtn" class="normal_btn"  value="导出" onclick="_function(2);" />
    	  <input type="button" id="queryBtn" class="normal_btn"  value="导出明细" onclick="explortDetail();" />
    	  <input type="hidden"  id="billIds" />
    	  </td>
  </tr>
</table>
<table class="table_list" style="margin-top: 1px; margin-bottom: 1px;">
			<tr class="table_list_row2">
				<td>运单总数：<span id="a1"></span></td>
				<td>里程总数：<span id="a2"></span></td>
				<td>平均里程数：<span id="a4"></span></td>
				<td>运费总数：<span id="a3"></span></td>
				<td>平均单台运费：<span id="a5"></span></td>
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
var url = "<%=contextPath%>/sales/storage/sendmanage/WaybillQuery/wayBillQuery.json";
							
var title = null;
var columns = [
       		{header: "序号", align:'center', renderer:getIndex,width:'7%'},
			{header: "运单号",dataIndex: 'BILL_NO',align:'center'},
			{header: "车系",dataIndex: 'SER_NAME',align:'center'},
			{header: "发运经销商名称",dataIndex: 'DEALER_NAME',align:'center'},
			{header: "承运商",dataIndex: 'LOGI_NAME',align:'center'},
			{header: "发运省份",dataIndex: 'SEND_CITY',align:'center'},
			{header: "发运城市",dataIndex: 'SEND_CITY1',align:'center'},
			{header: "发运地区",dataIndex: 'SEND_CITY2',align:'center'},
			{header: "车队",dataIndex: 'CAR_TEAM',align:'center'},
			{header: "车牌",dataIndex: 'CAR_NO',align:'center'},
			{header: "运单数量",dataIndex: 'VEH_NUM',align:'center'},
			{header: "运单时间",dataIndex: 'BILL_CRT_DATE',align:'center'},
			{header: "运单确认时间",dataIndex: 'CONFIRM_DATE',align:'center'},
			{header: "里程数",dataIndex: 'DISTANCE',align:'center'},
			{header: "运费",dataIndex: 'CG_AMOUNT',align:'center',renderer:myCg},
			{header: "操作",dataIndex: 'BILL_ID',sortable: false, align:'center',renderer:myLink}
	      ];
function _function(_type){
	  if(_type==1){
		  tgSum();
		__extQuery__(1);
	  }
	  if(_type==2){
		  		fm.action='<%=contextPath%>/sales/storage/sendmanage/WaybillQuery/wayBillQuery.do?common=2';  
		   		fm.submit();
		  }
	}
//统计数量和
function tgSum(){
	document.getElementById("a1").innerHTML = '';
	document.getElementById("a2").innerHTML = '';
	document.getElementById("a3").innerHTML = '';
	document.getElementById("a4").innerHTML = '';
	document.getElementById("a5").innerHTML = '';
	
	makeNomalFormCall("<%=contextPath%>/sales/storage/sendmanage/WaybillQuery/wayBillQuery.json?common=1",function(json){
		document.getElementById("a1").innerHTML = json.valueMap.VEH_NUM == null ? '0' : json.valueMap.VEH_NUM;
		document.getElementById("a2").innerHTML = json.valueMap.DISTANCE == null ? '0' : json.valueMap.DISTANCE;
		document.getElementById("a4").innerHTML = json.valueMap.AVG_DISTANCE == null ? '0' : json.valueMap.AVG_DISTANCE;
		document.getElementById("a3").innerHTML = formatCurrency(json.valueMap.CG_AMOUNT == null ? '0' : json.valueMap.CG_AMOUNT);
		document.getElementById("a5").innerHTML = formatCurrency(json.valueMap.AVG_AMOUNT == null ? '0' : json.valueMap.AVG_AMOUNT);
		
	},'fm');
}
//格式化钱格式
function formatMoney(dataStr){
	var str = formatCurrency(dataStr);
	document.write(str);
}
function myLink(value,meta,record){
	//var lgId=record.data.LOGI_ID;
	//var areaId=record.data.AREA_ID;
	//var link="<a href='javascript:void(0);' onclick='printWayBillNew(\""+value+"\",\""+lgId+"\",\""+areaId+"\")'>[查看]</a>";
	var serId=record.data.SER_ID;
	var link="<a href='javascript:void(0);' onclick='printWayBill(\""+value+"\",\""+serId+"\")'>[查看]</a>";
	return String.format(link);
}
function myCg(value,meta,record){
	var veh_num=record.data.VEH_NUM;
	//该地址没设定
	if(veh_num==0){
		value="无效";
	}else{
		if(value==0){
			value="无里程设定";
		} else{
			value=formatCurrency(value);
		}
	}
	return String.format(value);
}
//查看结算汇总单明细 
function printWayBillNew(val,lgId,areaId){
	document.getElementById("billIds").value="";
	document.getElementById("billIds").value=val+"-"+lgId+"-"+areaId;
	var url = "<%=contextPath%>/sales/storage/sendmanage/SendBillSettlement/checkSendBill.json";
	makeCall(url, showResult,{billIds:val}) ;
}
//查看结算汇总单明细 
function printWayBill(val,ser_id){
	var tarUrl = "<%=contextPath%>/sales/storage/sendmanage/WaybillQuery/waybillDetailQuery.do?wayBillId="+val+"&ser_id="+ser_id;
	//window.open(tarUrl,'','left=100,right=200,top=0,width='+(screen.availWidth-100) +',height='+screen.availHeight+'-50,toolbar=no,resizable=yes,menubar=no,scrollbars=yes,location=no');
	OpenHtmlWindow(tarUrl,800,600);
}
function showResult(json){
	if(json.str != ""){
		MyAlert(json.str);
	}else{
		var billIDs=document.getElementById("billIds").value;
	 	window.open("<%=contextPath%>/sales/storage/sendmanage/SendBillSettlement/toCreateSettleAdvice.do?billIDs="+billIDs+"&common=2");
	}
}
function PrintCancle(value){
	
		MyConfirm("确定取消运单?",printCancle,[value]);
		
	
} 
function printCancle(value){
	 var fm = document.getElementById('fm');
	    fm.action = "<%=contextPath%>/sales/storage/sendmanage/WaybillPrint/WaybillPrintCancle.do?billID="+value;
	    fm.submit();
}

//清空数据
function clrTxt(txtId){
	document.getElementById(txtId).value = "";
}

//导出明细
function explortDetail(){
	fm.action='<%=contextPath%>/sales/financemanage/InvoiceManage/queryDetail.do?common=2';  
	fm.submit();
}
</script>
</body>
</html>