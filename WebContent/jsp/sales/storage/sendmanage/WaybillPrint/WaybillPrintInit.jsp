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
<title>运单生成管理 </title>
<script type="text/javascript">
function doInit(){
	  //初始化时间控件
	genLocSel('txt1','','');//支持火狐
	//__extQuery__(1);
}
</script>
</head>
<body>
	<div class="navigation"><img src="<%=contextPath %>/img/nav.gif" />&nbsp;当前位置：储运管理>发运管理>运单打印管理
	</div>
<form id="fm" name="fm" method="post" >
<!-- 查询条件 begin -->
<table class="table_query" id="subtab">
<tr class="csstr" align="center">	
	<td class="right">选择经销商：</td>
		<td align="left">
			<input type="text" maxlength="20"   name="dealerCode" class="middle_txt" size="15" value="" id="dealerCode"/>
			<input type="hidden"  name="dealerName" class="middle_txt" size="15" value="" id="dealerName"/>
			<input name="button2" type="button" class="mini_btn" onclick="showOrgDealer('dealerCode','','true','','','','<%=Constant.DEALER_TYPE_DVS %>,<%=Constant.DEALER_TYPE_DP %>','dealerName');" value="..." />
			<input class="normal_btn" type="button" value="清空" onclick="clrTxt('dealerCode');clrTxt('dealerName');"/>
		</td>
		  <td class="right">运单号：</td> 
	  <td align="left">
		  <input type="text" maxlength="20"  id="ORDER_NO" datatype="1,is_digit_letter,30" maxlength="30" name="ORDER_NO" class="middle_txt" size="15" />
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
 <!--<tr class="csstr" align="center">	
	
		   <td class="right">地点：</td> 
	  <td align="left">
		  <input type="text" maxlength="20"  id="address" name="address" class="middle_txt" size="15" />
	  </td>	 
 </tr>-->
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

		 <!--<td class="right">地点：</td> 
	  <td align="left">
		  <input type="text" maxlength="20"  id="address" name="address" class="middle_txt" size="15" />
	  </td>	-->
 </tr>
<tr>
<td class="right">是否打印：</td>
  <td align="left">
  		  <select id="isPrint" name="isPrint" class="selectlist">
  		  		<option value="">-请选择-</option>
  		  		<option value="WDY">未打印</option>
  		  		<option value="YDY">已打印</option>
  		  </select>
    	  </td>
    	  <td class="right">产地：</td>
		   	   <td align="left">
	  				 <select name="YIELDLY" id="YIELDLY" class="selectlist"  onchange="getGroup();">
					 <option value="">--请选择--</option>
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
  </tr>
  <tr class="csstr" align="center">
 	<td class="right">运单确认状态：</td>
 	<td align="left">
 		<script type="text/javascript">
 			genSelBoxExp("isConfirm",1004,"",true,"u-select","","true",'');
 		</script>
 	</td> 
 	</tr>
  <tr align="center">
  <td colspan="6" class="table_query_4Col_input" style="text-align: center">
  		  <input type="reset" class="u-button u-reset" id="resetButton"  value="重置"/>
    	  <input type="button" id="queryBtn" class="u-button u-query" value="查询" onclick="doQuery();" /></td>
  </tr>
</table>
<table class="table_list" style="margin-top: 1px; margin-bottom: 1px;" style="display:none;">
			<tr class="table_list_row2" align="center">
				<td align="left">订单总数：<span id="a1"></span></td>
			</tr>
		</table>
<!-- 查询条件 end -->
<!--分页 begin -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
<!--分页 end -->
</form>
<script type="text/javascript" >
var myPage;
//查询路径           
var url = "<%=contextPath%>/sales/storage/sendmanage/WaybillPrint/WaybillPrintQuery.json";
							
var title = null;
var columns = [
       		{header: "序号", align:'center', renderer:getIndex,width:'7%'},
			{header: "运单号",dataIndex: 'BILL_NO',align:'center'},
			{header: "发运经销商代码",dataIndex: 'DEALER_CODE',align:'center'},
			{header: "发运经销商名称",dataIndex: 'DEALER_NAME',align:'center'},
		//	{header: "发票号",dataIndex: 'INVOICE',align:'center'},
			{header: "发运省市",dataIndex: 'SEND_CITY',align:'center'},
			{header: "承运商",dataIndex: 'LOGI_NAME',align:'center'},
			{header: "运单数量",dataIndex: 'VEH_NUM',align:'center'},
			{header: "操作",dataIndex: 'BILL_ID',sortable: false, align:'center',renderer:myLink}
	      ];
	      
	      
function myLink(value,meta,record){
	
	///var link="<a href='javascript:void(0);' onclick='printWayBill(\""+value+"\")'>[打印]</a>";
	
	var link="<a href='javascript:void(0);' onclick='printWayBill(\""+value+"\")'>[打印交接及点检单]</a><a href='javascript:void(0);' onclick='printWayBill2(\""+value+"\")'>[打印出门证]</a>";
	
	
	
	
	if(record.data.IS_CONFIRM!=<%=Constant.IF_TYPE_YES%>){
		if(record.data.IS_CONFIRM==<%=Constant.IF_TYPE_NO%>){
			link+="<a href='javascript:void(0);' id='qr"+value+"' onclick='WayBillConfirm(\""+value+"\")'>[运单确认]</a>";
		}
		if(record.data.C_COUNT==0){
			link+="<a href='javascript:void(0);' id='qx"+value+"' onclick='PrintCancle(\""+value+"\")'>[运单取消]</a>";
		}
	}
	return String.format(link);
}


function doQuery(){
	document.getElementById("a1").innerHTML = '';
	makeNomalFormCall("<%=contextPath%>/sales/storage/sendmanage/WaybillPrint/WaybillPrintQuery.json?common=1",function(json){
		document.getElementById("a1").innerHTML = json.valueMap.VEH_NUM == null ? '0' : json.valueMap.VEH_NUM;
	},'fm');
	__extQuery__(1);
}
//打印结算汇总单明细 
function printWayBill(val){
	var tarUrl = "<%=contextPath%>/sales/storage/sendmanage/WaybillPrint/WayBillPrint.do?op=1&wayBillId="+val;
	window.open(tarUrl,'','left=0,top=0,width='+screen.availWidth +'- 10,height='+screen.availHeight+'-50,toolbar=no,resizable=yes,menubar=no,scrollbars=yes,location=no');
}

function printWayBill2(val){
	var tarUrl = "<%=contextPath%>/sales/storage/sendmanage/WaybillPrint/WayBillPrint.do?op=2&wayBillId="+val;
	window.open(tarUrl,'','left=0,top=0,width='+screen.availWidth +'- 10,height='+screen.availHeight+'-50,toolbar=no,resizable=yes,menubar=no,scrollbars=yes,location=no');
}
//运单取消
function PrintCancle(value){
	    if(document.getElementById("qx"+value).disabled) {
	    	return;
	    }
		MyConfirm("警告！取消运单将取消所有该运单对应组板下的所有运单！确定取消?",printCancle,[value]);	
} 
function printCancle(value){
	document.getElementById("qx"+value).disabled = true;
	makeNomalFormCall("<%=contextPath%>/sales/storage/sendmanage/WaybillPrint/WaybillPrintCancle.json?billID="+value,function(json){
		if(json.returnValue==1){//成功
			MyAlert("取消运单成功！");
			__extQuery__(1);
		}else if(json.returnValue==2){
			MyAlert("取消运单失败！该运单对应组板下其他运单已有运单确认！");
			document.getElementById("qx"+value).disabled = false;
		}else{
			MyAlert("取消运单失败！");
			document.getElementById("qx"+value).disabled = false;
		}		
	},'fm');
}
//运单确认
function WayBillConfirm(value){
	if(document.getElementById("qr"+value).disabled) {
    	return;
    }
	MyConfirm("运单确认?",WayBillConfirmMain,[value]);	
} 
function WayBillConfirmMain(value){
	try{
		document.getElementById("qr"+value).disabled = true;
		document.getElementById("qx"+value).disabled = true;
	}catch(e){}
	makeNomalFormCall("<%=contextPath%>/sales/storage/sendmanage/WaybillPrint/WayBillConfirmMain.json?billID="+value,function(json){
		if(json.returnValue==1){//成功
			MyAlert("运单确认成功！");
			__extQuery__(1);
		}else{
			MyAlert("运单确认失败，请避免重复操作！");
			try{
				document.getElementById("qr"+value).disabled = false;
				document.getElementById("qx"+value).disabled = false;
			}catch(e){}
		}		
	},'fm');
}
//清空数据
function clrTxt(txtId){
	document.getElementById(txtId).value = "";
}
</script>
</body>

</html>