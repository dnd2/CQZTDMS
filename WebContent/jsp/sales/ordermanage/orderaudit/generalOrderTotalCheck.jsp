<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="com.infodms.dms.common.Constant"%>
<%@ page import="java.util.*" %>
<%@taglib uri="/jstl/cout" prefix="c" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>订单审核</title>
<% String contextPath = request.getContextPath();  %>
<script type="text/javascript">
function txtClr(valueId) {
	document.getElementById(valueId).value = '' ;
}

</script>
</head>
<body>
<div class="navigation"><img src="<%=request.getContextPath()%>/img/nav.gif" />&nbsp;当前位置： 销售订单管理 &gt; 订单审核&gt; 常规订单审核</div>
<form method="post" name="fm" id="fm">
<!-- 查询条件 begin -->
	<table class="table_query">
		<tr>
			<td align="right" ><input type="hidden" name="area" id="area"/></td>
			<td align="right" nowrap>订单周度：</td>
			<td align="left" nowrap>
				<select name="orderYearWeek" id="orderYearWeek" onchange="showData(this.value);" class="short_sel">
					<c:forEach items="${dateList}" var="map" varStatus="s">					
					    <c:choose>
					      <c:when test="${tpa.paraValue == s.index}">
   						     <option value="${map.code}"  selected="selected"><c:out value="${map.name}"/></option>
   						  </c:when>
   						  <c:otherwise>
   						     <option value="${map.code}" ><c:out value="${map.name}"/></option> 						     
   						  </c:otherwise>  
   						</c:choose>    
 					</c:forEach>
				</select>
				
			</td>
			<td align="left" nowrap class="innerHTMLStrong">
				<span id="data_start"></span>
				<span id="data_end"></span>
			</td>
			
			<td align="right" width="20%" nowrap>选择经销商：</td>
			<td align="left" width="35%" nowrap>
				<input type="text" class="middle_txt" name="dealerCode" size="15" value="" id="dealerCode"/>
				<!--  <input name="button2" type="button" class="mini_btn" onclick="showOrgDealer('dealerCode','','true');" value="..." />-->
				<c:if test="${dutyType==10431001}">
			      	<input class="mini_btn" id="dlbtn2" name="button2" type="button" onclick="showOrgDealer('dealerCode','','true', '${orgId}')" value="..." />
			     </c:if>
			      <c:if test="${dutyType==10431002}">
			      	<input class="mini_btn" id="dlbtn2" name="button2" type="button" onclick="showOrgDealer3('dealerCode','','true', '${orgId}')" value="..." />
			      </c:if>
			      <c:if test="${dutyType==10431003}">
			      	<input class="mini_btn" id="dlbtn2" name="button2" type="button" onclick="showOrgDealer3('dealerCode','','true', '${orgId}')" value="..." />
			      </c:if>
			      <c:if test="${dutyType==10431004}">
			      	<input class="mini_btn" id="dlbtn2" name="button2" type="button" onclick="showOrgDealer('dealerCode','','true', '${orgId}')" value="..." />
			      </c:if>
				<input type="button" class="normal_btn" onclick="txtClr('dealerCode');" value="清 空" id="clrBtn" />
			</td>
		</tr>
		<tr>
			<td align="right"></td>
			<td align="right">业务范围：</td>
			<td align="left">
				<select name="areaId" id="areaId" class="short_sel">
					<option value="-1">-请选择-</option>
					<c:forEach items="${areaBusList}" var="list">
						<option value="${list.AREA_ID}"><c:out value="${list.AREA_NAME}"/></option>
					</c:forEach>
				</select>
			</td>
			<td></td>
			<td align="right">销售订单号：</TD>
			<td align="left"><input type="text" class="middle_txt" name="orderNo"  value="" size="22"/></td>
			
		</tr>
		<tr>
			<td align="center" nowrap>&nbsp;</td>
			<td></td>
			<td></td>
			<td></td>
			<td align="center">
				<input type="hidden" name="groupId" id="groupId"/>
				<input type="hidden" name="orderId" id="orderId"/>
				<input type="hidden" name="ver" id="ver"/>
				<input type="hidden" name="orderYear" id="orderYear"/>
				<input type="hidden" name="orderWeek" id="orderWeek"/>
				
			</td>
			<td align="left" nowrap>
				<input type="button" name="button1" class="cssbutton" onclick="totailQuery();toDisabled1();" value="批量查询" id="queryBtn1" /> 
				<input type="button" name="button1" class="cssbutton" onclick="detailQuery();toDisabled2();" value="逐单查询" id="queryBtn1" />
                页面大小：<input name="pageSize" id="pageSize" type="text" class="mini_txt" value="10" datatype="0,isDigit,3"/>
            </td>
		</tr>   
	</table>
<!-- 查询条件 end -->
<!--分页 begin -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
<!--分页 end --> 
</form>
<form name="form1" id="form1" style="display:none">
	<table class="table_list">
		<tr class="table_list_row2">
			<td align="left">
				<input type="button" name="button1" class="cssbutton" onclick="toCheckSubmit();" value="审核完成"/>
			</td>
		</tr>
	</table>
</form>
<!--页面列表 begin -->
<script type="text/javascript" >
	document.form1.style.display = "none";
	var HIDDEN_ARRAY_IDS=['form1'];

	var myPage;
	var url;
	var title =null;
	var columns;
	function totailQuery(){
		url = "<%=contextPath%>/sales/ordermanage/orderaudit/GeneralOrderCheck/generalOrderCheckQuery.json";
	 	columns = [
					{header: "订单周度",dataIndex: 'ORDER_WEEK',align:'center',renderer:myText},
					{header: "配置代码",dataIndex: 'GROUP_CODE',align:'center'},
					{header: "配置名称", dataIndex: 'GROUP_NAME', align:'center'},
					{header: "生产计划数量", dataIndex: 'PLAN_AMOUNT', align:'center'},
					{header: "可用生产计划", dataIndex: 'AMOUNT', align:'center'},
					{header: "配额数量", dataIndex: 'QUOTA_AMT', align:'center'},
					{header: "常规订单数量", dataIndex: 'ORDER_AMOUNT', align:'center'},
					{header: "已审核数量", dataIndex: 'CHECK_AMOUNT', align:'center'},
					{header: "当前库存", dataIndex: 'STOCK_AMOUNT', align:'center'},
					{header: "操作",sortable: false, dataIndex: 'GROUP_ID', align:'center',renderer:myLink}
			      ];
		__extQuery__(1);
	}
	function detailQuery(){
		url = "<%=contextPath%>/sales/ordermanage/orderaudit/GeneralOrderCheck/generalOrderDetailQuery.json";
	 	columns = [
					{header: "经销商代码",dataIndex: 'DEALER_CODE',align:'center'},
					{header: "经销商名称",dataIndex: 'DEALER_NAME',align:'center'},
					{id:'action',header: "销售订单号", dataIndex: 'ORDER_NO', align:'center',renderer:myDetail},
					{header: "提报日期", dataIndex: 'RAISE_DATE', align:'center'},
					{header: "订单周度", dataIndex: 'ORDER_WEEK', align:'center',renderer:myText},
					{header: "订单类型", dataIndex: 'ORDER_TYPE', align:'center',renderer:getItemValue},
					{header: "提报数量", dataIndex: 'ORDER_AMOUNT', align:'center'},
					{header: "审核数量", dataIndex: 'CHECK_AMOUNT', align:'center',renderer:myInput},
					{header: "待审核数量", dataIndex: 'WAIT_AMOUNT', align:'center',renderer:myInput2},
					{header: "操作",sortable: false, dataIndex: 'ORDER_ID', align:'center',renderer:myLink2}
			      ];
		__extQuery__(1);
	}
	function toDisabled1(){
		document.getElementById("form1").style.display = "inline";
	}
	function toDisabled2(){
		document.getElementById("form1").style.display = "none";
	}
	//设置已审核数量
	function myInput(value,meta,record){
  		return String.format("<span>"+value+"</span>");
	}
	//设置待审核数量
	function myInput2(value,meta,record){
  		return String.format("<span>"+value+"</span>");
	}
	//设置订单年周
	function myText(value,meta,record){
		var data = record.data;
		return String.format(data.ORDER_YEAR+"."+ value);
	}
	function myDetail(value,meta,record){
		return String.format("<a href='#' onclick='orderDetailInfo(\""+ value +"\")'>"+value+"</a>");
	}
	function orderDetailInfo(value){
		OpenHtmlWindow('<%=contextPath%>/sales/ordermanage/orderdetail/OrderDetailInfoQuery/orderDetailInfoQuery.do?&orderNo='+value,700,500);
	}
	//超链接设置
	function myLink(value,meta,record){
  		return String.format("<a href='#' onclick='searchServiceInfo(\""+ record.data.ORDER_YEAR +"\",\""+ record.data.ORDER_WEEK +"\",\""+ value +"\")'>[调整]</a>");
	}
	function myLink2(value,meta,record){
		return String.format("<input type='hidden' name='ver"+value+"' id='ver"+value+"' value='"+record.data.VER+"'/><a href='#' onclick='searchServiceInfo2("+ value +")'>[调整]</a><a href='#' onclick='searchServiceInfo3("+ value +")'>[审核完成]</a><a href='#' onclick='searchServiceInfo4("+ value +")'>[驳回]</a>");
	}
	//调整超链接
	function searchServiceInfo(orderYear,orderWeek,value){
		document.getElementById("groupId").value = value;
		document.getElementById("orderYear").value = orderYear;
		document.getElementById("orderWeek").value = orderWeek;
		$('fm').action= '<%=contextPath%>/sales/ordermanage/orderaudit/GeneralOrderCheck/generalOrderTotalDlrQuery.do';
	 	$('fm').submit();
	}
	var rowObjNum = null;
	var rowObj = null;
	function searchServiceInfo2(value){
		rowObjNum = window.event.srcElement.parentElement.rowIndex;
		rowObj = window.event.srcElement.parentElement.parentElement;
		OpenHtmlWindow('<%=contextPath%>/sales/ordermanage/orderaudit/GeneralOrderCheck/generalOrderDetailAuditInit.do?&orderId='+value,900,500);
	}
	function searchServiceInfo3(value){
		document.getElementById("orderId").value=value;
		var ver = document.getElementById("ver"+value).value;
		document.getElementById("ver").value = ver;
		MyConfirm("确认审核完成？",putSubmitForword);
	}
	function searchServiceInfo4(value){
		document.getElementById("orderId").value=value;
		var ver = document.getElementById("ver"+value).value;
		document.getElementById("ver").value = ver;
		MyConfirm("确认要驳回？",putRejectForword);
	}
	//审核完成提示
	function toCheckSubmit(){
		MyConfirm("确认审核完成？",putForword);
	}
	//审核完成提交
	function putForword(){
		makeNomalFormCall("<%=contextPath%>/sales/ordermanage/orderaudit/GeneralOrderCheck/generalOrderCheckSubmit.json",showForwordValue,'fm','queryBtn1');
	}
	//审核完成提交
	function putSubmitForword(){
		makeNomalFormCall('<%=contextPath%>/sales/ordermanage/orderaudit/GeneralOrderCheck/generalOrderDetailCheckSubmit.json',showForwordValue,'fm','queryBtn1');
	}
	//驳回完成提交
	function putRejectForword(){
		makeNomalFormCall('<%=contextPath%>/sales/ordermanage/orderaudit/GeneralOrderCheck/generalOrderDetailRejectSubmit.json',showForwordValue,'fm','queryBtn1');
	}
	//回调函数
	function showForwordValue(json){
		if(json.returnValue == '1'){
			MyAlert("审核成功！");
			__extQuery__(1);
		}else if(json.returnValue == '2'){
			MyAlert("数据已被修改！");
			__extQuery__(1);
		}else if(json.returnValue == '3'){
			MyAlert("驳回成功！");
			__extQuery__(1);
		}else{
			MyAlert("审核失败！请联系系统管理员！");
		}
	}
	//页面初始化
	function doInit(){
		var area = "";
		<c:forEach items="${areaBusList}" var="list">
			var areaId = <c:out value="${list.AREA_ID}"/>
			if(area==""){
				area = areaId;
			}else{
				area = areaId+','+area;
			}
		</c:forEach>
		document.getElementById("area").value=area;
		var flag =<%=request.getAttribute("flag")%>;
		if(Number(flag)==1){
			__extQuery__(1);
		}
		var choice_code = document.getElementById("orderYearWeek").value;
		showData(choice_code);
	}

	function showData(choice_code){
		var sMyDate = choice_code.split("-") ;
		document.getElementById("orderYear").value = sMyDate[0] ;
		document.getElementById("orderWeek").value = sMyDate[1] ;
		
		var data_start = "";
		var data_end = "";
		<c:forEach items="${dateList}" var="map">
			var code = "${map.code}";
			if( ""+choice_code == ""+code ){
				data_start = "${map.date_start}";
				data_end = "${map.date_end}";
			}
		</c:forEach>
		if(data_start){
			document.getElementById("data_start").innerHTML = data_start+"  至  ";
			document.getElementById("data_end").innerHTML = data_end;
		}
	}
</script>
<!--页面列表 end -->
</body>
</html>