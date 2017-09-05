<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c" %>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>常规订单提报</title>
<script type="text/javascript">
function doInit(){
	var choice_code = document.getElementById("orderYearWeek").value;
	showData(choice_code);
}
</script>
</head>
<body>
<div class="navigation"><img src="<%=request.getContextPath()%>/img/nav.gif" />&nbsp;当前位置：整车销售 > 销售订单管理 > 订单提报 > 常规订单提报</div>
<form method="POST" name="fm" id="fm">
  <table class="table_query" align=center width="95%">
	 <tr>
	    <td width="37%" align="right" nowrap>订单周度：   </td>
	    <td width="43%" class="table_query_2Col_input" nowrap>
		    <select name="orderWeek"  id="orderYearWeek" onchange="showData(this.value);">
		      <c:forEach items="${dateList}" var="po">
					<option value="${po.code}">${po.name}</option>
			  </c:forEach>
	        </select>
	        <span id="data_start" class="innerHTMLStrong"></span>
			<span id="data_end" class="innerHTMLStrong"></span>
        </td>
	    <td width="20%" align=left nowrap>&nbsp;</td>
    </tr> 
    <tr>
      <td align="right" nowrap></td>
      <td align="right" nowrap></td>
      <td align="left" nowrap></td>
    </tr>
	<tr>
	  <TD align="right" nowrap>业务范围：</TD>
		<TD class="table_query_2Col_input" nowrap>
			<select name="areaId">
				<c:forEach items="${areaList}" var="po">
					<option value="${po.AREA_ID}|${po.DEALER_ID}">${po.AREA_NAME}</option>
				</c:forEach>
            </select>
        </TD>
		<td align=left nowrap>&nbsp;</td>
	</tr> 
	<tr>
      <td align="right" nowrap>&nbsp;</td>
	   <td align="center" nowrap>
		   <input type="hidden" name="orderWeekGet" value=""> 
		   <input type="hidden" name="areaIdGet" value=""> 
		   <input id="queryBtn1" name="queryBtn" type=button class="cssbutton" onClick="doQuery();" value="查询">
	   </td>
	   <td>
	   </td>
	</tr>
  </table>
  
	<!--分页 begin -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
    <!--分页 end -->
</form>
<form name="form1" id="form1">
	<table>
		<tr>
			<td align="center">
		  		<input class="cssbutton" type="button" id="sub" value="提交" onclick="confirmSubmit();">
	     	</td>
		</tr>
	</table>
</form>
<script type="text/javascript">
	document.form1.style.display = "none";
	var HIDDEN_ARRAY_IDS=['form1'];
	
	var myPage;
	//查询路径
	var url = "<%=request.getContextPath()%>/sales/ordermanage/orderreport/GeneralOrderReport/generalOrderReoprtQuery.json";
				
	var title = null;
	var columns = [
				{header: "周度", dataIndex: 'QUOTA_DATE', align:'center'},
				{header: "车系代码", dataIndex: 'SERIES_CODE', align:'center'},
				{header: "车系名称", dataIndex: 'SERIES_NAME', align:'center'},
				{header: "配置代码", dataIndex: 'GROUP_CODE', align:'center'},
				{header: "配置名称", dataIndex: 'GROUP_NAME', align:'center'},
				{header: "配额数量", dataIndex: 'QUOTA_AMT', align:'center'},
				{header: "最大提报量", dataIndex: 'MAX_AMOUNT', align:'center',renderer:quotaNO},
				{header: "最小提报量", dataIndex: 'MIN_AMOUNT', align:'center'},
				{header: "已提报数量", dataIndex: 'YTB', align:'center',renderer:ytbAmount},
				{header: "本次提报数量", dataIndex: 'WTB', align:'center',renderer:bcAmount},
				{id:'action',header: "操作",sortable: false,dataIndex: 'GROUP_ID',renderer:myLink ,align:'center'}
		      ];		         
	function quotaNO(value){
		return String.format("<input type='hidden'  maxlength='100'  name='quotaNO' value='" + value + "' />"+value+"");
	}
	function ytbAmount(value){
		return String.format("<input type='hidden'  maxlength='100'  name='ytbAmount' value='" + value + "' />"+value+"");
	}
	//修改的超链接
	function myLink(value,meta,record){
		var year = record.data.QUOTA_YEAR;
		var week = record.data.QUOTA_WEEK;
		var dealerId = record.data.DEALER_ID;
		var areaId = record.data.AREA_ID;
  		return String.format("<a href='#' onclick='loginMod(\""+ value +"\",\""+ year +"\",\""+ week +"\",\""+ dealerId +"\",\""+ areaId +"\")'>[修改]</a>");
	}
	
	//修改的超链接设置
	function loginMod(arg1, arg2, arg3, arg4, arg5){
		$('fm').action= '<%=request.getContextPath()%>/sales/ordermanage/orderreport/GeneralOrderReport/generalOrderReoprtAddPre.do?groupId='+arg1+'&year='+arg2+'&week='+arg3+'&dealerId='+arg4+"&areaId="+arg5;
	 	$('fm').submit();
	}
	
	function showTable(){
		document.getElementById('table1').style.display="inline";	
	}

	function confirmSubmit(){
		if(checkReportNOs()){
			var orderWeek_ = document.getElementsByName("orderWeek");
			var areaId_ = document.getElementsByName("areaId");
			var orderWeek = orderWeek_[0].value;
			var areaId = areaId_[0].value;
			OpenHtmlWindow('<%=request.getContextPath()%>/sales/ordermanage/orderreport/GeneralOrderReport/showOrderDetail.do?orderWeek='+orderWeek+'&areaId='+areaId,700,500);
		}
	}
	//-------------------------------------
	function checkReportNOs(){
		var quotaNOs = 0; 		//配额数量
		var ytbAmounts = 0; 	//已提报数量
		var commitAmounts = 0;  //本次提报数量
		var quotaNO = document.getElementsByName("quotaNO");
		var ytbAmount = document.getElementsByName("ytbAmount");
		var commitAmount = document.getElementsByName("commitAmount");
		for(var i = 0 ;i <quotaNO.length; i++){
			if(Number(ytbAmount[i].value)+Number(commitAmount[i].value)-Number(quotaNO[i].value)>0){
				MyAlert("提报总量大于配额最大提报数量，无法提报");
				return false;
				break;
			}else{
				return true;
			}
		}
	}
	
	function confirmSubmit1(){
		MyConfirm("是否确认提交?",orderSubmit);
	}
	
	function orderSubmit(){
		makeNomalFormCall('<%=request.getContextPath()%>/sales/ordermanage/orderreport/GeneralOrderReport/generalOrderReoprtSubmitCVS.json',showResult,'fm');
	}
	
	function showResult(json){
		if(json.returnValue == '1'){
			MyAlert("本次提交已成功！");
			var objSub=document.form1.sub;
			objSub.disabled = (objSub.disabled == "" ? "disabled" : "");
			__extQuery__(1);
		}else if(json.returnValue == '2'){
			MyAlert("提交失败！您没有待提报的订单！");
		}else if(json.returnValue == '3'){
			var sGroupCode = json.groupStr ;
			
			MyAlert("车型配置为“" + sGroupCode + "”的“已提报数量”与“本次提报数量”之和不能小于对应车型状态的“最小提报量”！");
		} else if(json.returnValue == '4') {
			MyAlert("各个配置的“已提报数量”与“本次提报数量”之和不能大于对应配置的“最大提报量”！");
		}
	}
	function bcAmount(value){
		return String.format("<input type='hidden'  maxlength='100'  name='commitAmount' value='"+value+"' />"+value+"  ");
	}
	function doQuery(){
		document.getElementById("orderWeekGet").value = document.getElementById("orderWeek").value;
		document.getElementById("areaIdGet").value = document.getElementById("areaId").value;
		__extQuery__(1);
		
		toCheck() ;
	}

	function toCheck() {
		makeNomalFormCall('<%=request.getContextPath()%>/sales/ordermanage/orderreport/GeneralOrderReport/isNullOrder.json',toCalculate,'fm');
	}

	function toCalculate(json){
		flag = json.flag ;
		
		if(flag == "1") {
			document.getElementById('sub').disabled = true ;
		} else {
			document.getElementById('sub').disabled = false ;
		}
	}
	function showData(choice_code){
		var data_start = "";
		var data_end = "";
		<c:forEach items="${dateList}" var="list">
			var code = "${list.code}";
			if(choice_code+"" == code+""){
				data_start = "${list.date_start}";
				data_end = "${list.date_end}";
			}
		</c:forEach>
		if(data_start){
			document.getElementById("data_start").innerHTML = data_start+"  至  ";
			document.getElementById("data_end").innerHTML = data_end;
		}
	}
</script>
</body>
</html>
