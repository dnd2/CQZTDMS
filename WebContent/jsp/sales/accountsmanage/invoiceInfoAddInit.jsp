<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="com.infodms.dms.common.Constant"%>
<%@taglib uri="/jstl/cout" prefix="c" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<script type="text/javascript" src="<%=request.getContextPath() %>/js/orderNumberFormat.js"></script>

<title>发票管理</title>
<% String contextPath = request.getContextPath(); %>
<script type="text/javascript">

</script>
</head>
<body >
<div class="navigation"><img src="<%=request.getContextPath()%>/img/nav.gif" />&nbsp;当前位置：财务管理 &gt; 发票管理 &gt;发票管理</div>
<div class="form-panel">
	<h2><img src="<%=request.getContextPath()%>/jmstyle/img/search-ico.png" style="margin-bottom: -13px;"/>发票管理</h2>
<div class="form-body">
<form method="post" name="fm" id="fm">
<!-- 查询条件 begin -->
	<table class="table_query" >
		<tr>
			<!--<td width="13%"></td>
			<td align="right">订单周度：</td>
			<td align="left">
				<select name="orderYearWeek" id="orderYearWeek">
					<c:forEach items="${dateList}" var="list">
						<option value="${list.code}"><c:out value="${list.name}"/></option>
					</c:forEach>
				</select>
			</td>
			-->
			<td ></td>
<!--			<td align="right">选择区域：</td>-->
<!--			<td align="left">-->
<!--				<input type="text" id="orgCode" style="width:100px" name="orgCode" value="" size="15" class="middle_txt" readonly="readonly" />-->
<!--				<input name="obtn" id="obtn"  class="mini_btn" type="button" value="&hellip;" onclick="showOrg('orgCode','orgId' ,'true','${orgId}');"/>-->
<!--				<input type="hidden" id="orgId" name="orgId" >-->
<!--				<input class="normal_btn" type="button" value="清空" onclick="clrTxt('orgCode');clrTxt('orgId');"/>-->
<!--			</td>-->
			<td align="right"><div align="right">选择经销商：</div></td>
			<td colspan="1">
				<input type="text" class="middle_txt"  name="dealerCode" size="15" value="" id="dealerCode" onclick="showOrgDealer('dealerCode','','true', '');"/>
				<%-- <c:if test="${dutyType==10431004}">
            	 	<input name="button2" id="dbtn" class="normal_btn" type="button" value="&hellip;" onclick="showOrgDealer('dealerCode','','true', '');" />
           		 </c:if>
            	<c:if test="${dutyType!=10431004}">
            		<input name="button2" id="dbtn" class="normal_btn" type="button" value="&hellip;" onclick="showOrgDealer('dealerCode','','true', '','true');" />
        		</c:if> --%>
				<input class="normal_btn" type="button" value="清空" onclick="clrTxt('dealerCode');"/>
			</td>
			<td align="right"><div align="right">选择物料组：</div></td>
			<td>
				<input type="text" class="middle_txt"  id="groupCode" name="groupCode" size="15" readonly="readonly" value="" onclick="showMaterialGroup('groupCode','groupName','true','4');"/>
				<input type="hidden" name="groupName" size="20" id="groupName" value="" />
				<!-- <input name="button3" type="button" class="normal_btn" onclick="showMaterialGroup('groupCode','groupName','true','4');" value="..." /> -->
				<input class="normal_btn" type="button" value="清空" onclick="clrTxt('groupCode');"/>
			</td>
			<td ></td>
		</tr>
		<tr>
			<td></td>
			<td align="right"><div align="right">订单类型：</div></TD>
			<td>
				<label>
					<script type="text/javascript">
						genSelBoxExp("orderTypeSel",<%=Constant.ORDER_TYPE%>,"<%=Constant.ORDER_TYPE_01%>",true,"u-select","","false",'<%=Constant.ORDER_TYPE_02%>');
					</script>
				</label>
			</td>
			<td align="right"><div align="right">批售订单号：</div></TD>
			<td><input type="text" class="middle_txt"  name="orderNo"  value=""/></td>
			<td></td>
		</tr>
		<tr>
			<td></td>
			<td align="right"><div align="right">状态：</div></TD>
			<td>
				<label>
					<script type="text/javascript">
						genSelBoxExp("reqStatus",<%=Constant.ORDER_REQ_STATUS%>,"",true,"u-select","","false",'<%=Constant.ORDER_REQ_STATUS_02%>,<%=Constant.ORDER_REQ_STATUS_03%>,<%=Constant.ORDER_REQ_STATUS_04%>,<%=Constant.ORDER_REQ_STATUS_06%>,<%=Constant.ORDER_REQ_STATUS_07%>,<%=Constant.ORDER_REQ_STATUS_08%>');
					</script>
				</label>
			</td>
			<td align="right"><div align="right">提报日期：</div></td>
     	 <td align="left" colspan="3">
        <input class="middle_txt" style="width:100px" type="text" id="t1" name="startDate" datatype="1,is_date,10"   value="" onFocus="WdatePicker({el:$dp.$('t1'), maxDate:'#F{$dp.$D(\'t2\')}'})"  style="cursor: pointer;width: 80px;"/>
       &nbsp;至&nbsp;
        <input class="middle_txt"  style="width:100px" type="text" id="t2" name="endDate" datatype="1,is_date,10"  value=""onFocus="WdatePicker({el:$dp.$('t2'), minDate:'#F{$dp.$D(\'t1\')}'})"  style="cursor: pointer;width: 80px;"/>
        
      	</td>
			<td></td>
		</tr>
		<tr>
			<td></td>
			<td></td>
            <td></td>
            <td align="center">
                <input type="hidden" name="isCheck" id="isCheck" value="${isCheck}"/>
                  <input type="hidden" name="userId" id="userId" value="${userId}"/>
              	 <input type="hidden" name="sessionId" id="sessionId" value="${sessionId}"/>
                <input name="queryBtn" type=button class="normal_btn" onClick="__extQuery__(1);" value="查询">
               <!--  <input name="button2" type=button class="normal_btn" onClick="getDetail();" value="下载"> -->
            </td>
            <td></td>
        <td align="right">
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
</div>
</div>
<!--页面列表 begin -->
<script type="text/javascript" >
	var myPage;
	var curstr;
	//查询路径           
	var url = "<%=contextPath%>/sales/accountsmanage/InvoiceManage/invoiceManageQuery.json";
	var title = null;
	var columns = [
			    {header: "操作",sortable: false, dataIndex: 'ORDER_ID', align:'center',renderer:myLink},
				{header: "开票单位代码",dataIndex: 'DEALER_CODE',align:'center'},
				{header: "开票单位名称",dataIndex: 'DEALER_NAME',align:'center'},
				{header: "采购单位名称",dataIndex: 'ORDER_DEALER_NAME',align:'center'},
				{id:'action',header: "批售订单号", dataIndex: 'ORDER_NO', align:'center',renderer:myDetail},
				{header: "发运申请单号", dataIndex: 'DLVRY_REQ_NO', align:'center'},
				{header: "提报日期", dataIndex: 'RAISE_DATE', align:'center'},
				//{header: "订单周度", dataIndex: 'ORDER_WEEK', align:'center',renderer:myText},
				{header: "订单类型", dataIndex: 'ORDER_TYPE', align:'center',renderer:getItemValue},
				{header: "状态", dataIndex: 'REQ_STATUS', align:'center'},
				{header: "保留数量", dataIndex: 'RESERVE_AMOUNT', align:'center'},
				{header: "开票数量", dataIndex: 'BILL_AMOUNT', align:'center'},
				{header: "资金类型", dataIndex: 'FUND_TYPE', align:'center'},
				{header: "订单总价", dataIndex: 'REQ_TOTAL_PRICE', align:'center',renderer:myformat},
				
		      ];
	//超链接设置
	//设置金钱格式
    function myformat(value,metaDate,record){
        return String.format(amountFormat(value));
    }
	function myLink(value,meta,record){
		var orderType = record.data.ORDER_TYPE;
		var reqId = record.data.REQ_ID;
		var historyCount = history.length ;
  		return String.format("<a href='#' class='u-anchor' onclick='toDetailCheck(\""+ value +"\",\""+ orderType +"\",\""+ reqId +"\",\"" + historyCount + "\")'>[开票]</a><input type='hidden' name='ver' value='"+record.data.VER+"'/>");
	}

	function getDetail(){
		$('fm').action= '<%=request.getContextPath()%>/sales/ordermanage/orderaudit/OrderResourceReserveFirst/orderResourceReserveQueryLoad.json';
	 	$('fm').submit();
		}
	function myDetail(value,meta,record){
		return String.format("<a href='#' onclick='orderDetailInfo(\""+ value +"\")'>"+value+"</a>");
	}
	function orderDetailInfo(value){
		OpenHtmlWindow('<%=contextPath%>/sales/ordermanage/orderdetail/OrderDetailInfoQuery/orderDetailInfoQuery.do?&orderNo='+value,700,500);
	}
	//调整链接
	function toDetailCheck(value1, value2, value3, value4){
		var sessinId=$("sessionId").value;
		var userId=$("userId").value;
		curstr=new Array();
		curstr[0]=value1;
		curstr[1]=value2;
		curstr[2]=value3;
		curstr[3]=value4;
		var lockUrl='<%=request.getContextPath()%>/util/ResourceLock/checkIsOper.json?&billId='+value3;
		makeNomalFormCall(lockUrl,lockReturn,'fm');
		
	}
	function lockReturn(json){
		if(json.flag=="1"){
			MyAlert("有其他人操作");
			return;
		}else{
			value1=curstr[0];
			value2=curstr[1];
			value3=curstr[2];
			value4=curstr[3];
			document.fm.action= '<%=request.getContextPath()%>/sales/accountsmanage/InvoiceManage/invoiceManageDetailQuery.do?orderId='+value1+'&orderType='+value2+'&reqId='+value3+'&historyCount='+value4;
			document.fm.submit();
		}
	}
	//设置年周
	function myText(value,meta,record){
		var data = record.data;
  		return String.format(data.ORDER_YEAR+"."+value);
	}
	//初始化    
	function doInit(){
		//loadcalendar();  //初始化时间控件
		if('${dealerCode}')
			document.getElementById("dealerCode").value='${dealerCode}';
		if('${areaId}')
			document.getElementById("areaId").value='${areaId}';
		if('${groupCode}')
			document.getElementById("groupCode").value='${groupCode}';
		//if('${orderTypeSel}')
		//	document.getElementById("orderTypeSel").value='${orderTypeSel}';
		if('${orderNo}')
			document.getElementById("orderNo").value='${orderNo}';
		if('${reqStatus}')
			document.getElementById("reqStatus").value='${reqStatus}';
		if('${orgCode}')
			document.getElementById("orgCode").value='${orgCode}';
		if('${startDate}')
			document.getElementById("startDate").value = '${startDate}' ;
		if('${endDate}')
			document.getElementById("endDate").value = '${endDate}' ;
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
		__extQuery__(1);
	}
	function clrTxt(txtId){
    	document.getElementById(txtId).value = "";
    }
</script>
<!--页面列表 end -->
</body>
</html>