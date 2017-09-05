<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="com.infodms.dms.common.Constant"%>
<%@taglib uri="/jstl/cout" prefix="c" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>订单审核</title>
<% String contextPath = request.getContextPath(); %>
<script type="text/javascript">
</script>
</head>
<body>
<div class="navigation"><img src="<%=request.getContextPath()%>/img/nav.gif" />&nbsp;当前位置： 销售订单管理 &gt; 订单审核 &gt;补充订单审核</div>
<form method="post" name="fm" id="fm">
<!-- 查询条件 begin -->
	<table class="table_query" >
		<tr>
			<td width="13%"></td>
			<td align="right">订单周度：</td>
			<td align="left">
				<select name="orderYearWeek" id="orderYearWeek">
					<c:forEach items="${dateList}" var="list">
						<option value="${list.code}"><c:out value="${list.name}"/></option>
					</c:forEach>
				</select>
			</td>
			<td align="right">选择订货方：</td>
			<td>
				<input type="text"  name="dealerCode" size="15" value="" id="dealerCode"/>
				<input name="button2" type="button" class="mini_btn" onclick="showOrgDealer('dealerCode','','true');" value="..." />
			</td>
			<td width="13%"></td>
		</tr>
		<tr>
			<td></td>
			<td align="right">业务范围：</td>
			<td align="left">
				<select name="areaId" id="areaId" class="short_sel">
					<option value="-1">-请选择-</option>
					<c:forEach items="${areaBusList}" var="list">
						<option value="${list.AREA_ID}"><c:out value="${list.AREA_NAME}"/></option>
					</c:forEach>
				</select><input type="hidden" name="area" id="area"/>
			</td>
			<td align="right">选择物料组：</td>
			<td>
				<input type="text"  name="groupCode" size="15" readonly="readonly" value="" />
				<input name="button3" type="button" class="mini_btn" onclick="showMaterialGroup('groupCode','','true','3');" value="..." />
			</td>
			<td></td>
		</tr>
		<tr>
			<td></td>
			<td align="right">订单类型：</TD>
			<td>
				<label>
					<script type="text/javascript">
						genSelBoxExp("orderType",<%=Constant.ORDER_TYPE%>,"<%=Constant.ORDER_TYPE_02%>",false,"short_sel","","false",'<%=Constant.ORDER_TYPE_01%>');
					</script>
				</label>
			</td>
			<td align="right">订单号码：</TD>
			<td><input type="text" name="orderNo"  value=""/></td>
			<td></td>
		</tr>
		<tr>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td align="right">
				<input type="hidden" name="orderIds" id="orderIds"/>
				<input type="hidden" name="vers" id="vers"/>
				<input type="hidden" name="flag" id="flag"/>
				<input type="hidden" name="remark" id="remark"/>
				<input type="hidden" name="isCheck" id="isCheck" value="${isCheck}"/>
				<input name="button2" type=button class="cssbutton" onClick="__extQuery__(1);toDisabled();" value="查询">
			</td>
			<td></td>
		</tr>
	</table>
<!-- 查询条件 end -->
<!--分页 begin -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
<!--分页 end -->
</form>
<!--<form id="form1" name="form1">
	<table class="table_list" id="table1">
		<tr class="table_list_row2">
			<td align="right" width="7%">
				审核描述：
			</td>
			<td align="left"><textarea name="checkRemark" id="checkRemark" cols="30" rows="3"></textarea>
				<input type="button" name="button1" class="cssbutton" onclick="toSubmitCheck('0');" value="审核完成"/>
				<span id="button"><input type="button" name="button3" class="cssbutton" onclick="toSubmitCheck('1');" value="审核驳回"/></span>
			</td>
		</tr>
	</table>
</form>
--><!--页面列表 begin -->
<script type="text/javascript" >
	//document.form1.style.display = "none";
	//var HIDDEN_ARRAY_IDS=['form1'];
	var myPage;
	//查询路径           
	var url = "<%=contextPath%>/sales/ordermanage/orderaudit/NastyOrderCheck/nastyOrderCheckQuery.json";
	var title = null;
	var columns = [
				//{id:'action',header: "<input type='checkbox' name='checkAll' onclick='selectAll(this,\"orderId\")' />", width:'8%',sortable: false,dataIndex: 'ORDER_ID',renderer:myCheckBox},
				{header: "订货方代码",dataIndex: 'DEALER_CODE',align:'center'},
				{header: "订货方名称",dataIndex: 'DEALER_NAME',align:'center'},
				{header: "开票方名称",dataIndex: 'DEALER_NAME2',align:'center'},
				{id:'action',header: "订单号码", dataIndex: 'ORDER_NO', align:'center',renderer:myDetail},
				{header: "提报日期", dataIndex: 'RAISE_DATE', align:'center'},
				{header: "订单周度", dataIndex: 'ORDER_WEEK', align:'center',renderer:myText},
				{header: "订单类型", dataIndex: 'ORDER_TYPE', align:'center',renderer:getItemValue},
				{header: "提报数量", dataIndex: 'ORDER_AMOUNT', align:'center'},
				{header: "审核数量", dataIndex: 'CHECK_AMOUNT', align:'center',renderer:myInput},
				{header: "操作",sortable: false, dataIndex: 'ORDER_ID', align:'center',renderer:myLink}
		      ];
	var rowObjNum = null;
	var rowObj = null;
	//超链接设置
	function myLink(value,meta,record){
  		return String.format("<a href='#' onclick='toDetailCheck(\""+ value +"\")'>[调整]</a><input type='hidden' name='ver' value='"+record.data.VER+"'/>");
	}
	function myDetail(value,meta,record){
		return String.format("<a href='#' onclick='orderDetailInfo(\""+ value +"\")'>"+value+"</a>");
	}
	function orderDetailInfo(value){
		OpenHtmlWindow('<%=contextPath%>/sales/ordermanage/orderdetail/OrderDetailInfoQuery/orderDetailInfoQuery.do?&orderNo='+value,700,500);
	}
	//设置已审核数量
	function myInput(value,meta,record){
  		return String.format("<span>"+value+"</span>");
	}
	//设置待审核数量
	function myInput2(value,meta,record){
  		return String.format("<span>"+value+"</span>");
	}
	//调整链接
	var rowObj = null;
	function toDetailCheck(value){
		rowObjNum = window.event.srcElement.parentElement.parentElement.rowIndex;
		rowObj = window.event.srcElement.parentElement.parentElement;
		var orderType = $('fm').orderType.value;
		rowObj = window.event.srcElement.parentElement.parentElement;
		OpenHtmlWindow('<%=contextPath%>/sales/ordermanage/orderaudit/NastyOrderCheck/nastyOrderDetailCheckQuery.do?&orderId='+value+'&orderType='+orderType,700,500);
	}
	//全选checkbox
	function myCheckBox(value,metaDate,record){
		return String.format("<input type='checkbox' name='orderId' value='" + value + "'/>");
	}
	//设置年周
	function myText(value,meta,record){
		var data = record.data;
  		return String.format(data.ORDER_YEAR+"."+value);
	}
	//设置订做车订单驳回按钮隐藏
	function toDisabled(){
		document.getElementById("table1").style.display="inline";
		var orderType = document.getElementsByName("orderType");
		for(var i=0;i<orderType.length;i++){
			if(orderType[i].value==<%=Constant.ORDER_TYPE_03%>){
				document.getElementById("button").style.display="none";
			}else{
				document.getElementById("button").style.display="inline";
			}
		}
	}
	//初始化    
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
		document.getElementById("table1").style.display="none";
	}
	//提交校验
	function toSubmitCheck(value){
		var cnt = 0;
		var orderIds ='';
		var vers ='';
		var orderId = document.getElementsByName("orderId");
		var ver = document.getElementsByName("ver");
		for(var i=0 ;i< orderId.length; i++){
			if(orderId[i].checked){
				cnt++;
				orderIds = orderId[i].value + ',' + orderIds;
				vers = ver[i].value + ',' + vers;
			}
		}
		if(cnt==0){
			MyAlert("请选择！");
            return false;
		}
		document.getElementById("flag").value=value;
		document.getElementById("orderIds").value=orderIds;
		document.getElementById("vers").value=vers;
		document.getElementById("remark").value=document.getElementById("checkRemark").value;
		if(value==0){
			MyConfirm("确认审核通过？",putForword);
		}
		if(value==1){
			MyConfirm("确认审核驳回？",putForword);
		}
	}
	//审核提交
	function putForword(){
		makeNomalFormCall("<%=contextPath%>/sales/ordermanage/orderaudit/NastyOrderCheck/NastyOrderCheckSubmit.json",showForwordValue,'fm','queryBtn');
	}
	//回调函数
	function showForwordValue(json){
		if(json.returnValue == '1'){
			window.parent.MyAlert("操作成功！");
			$('fm').action= '<%=contextPath%>/sales/ordermanage/orderaudit/NastyOrderCheck/nastyOrderCheckInit.do';
		 	$('fm').submit();
		}else if(json.returnValue == '2'){
			window.parent.MyAlert("数据已被修改，操作失败！");
			$('fm').action= '<%=contextPath%>/sales/ordermanage/orderaudit/NastyOrderCheck/nastyOrderCheckInit.do';
		 	$('fm').submit();
		}else{
			MyAlert("操作失败！请联系系统管理员！");
		}
	}
	function toQuery(){
		__extQuery__(1);
		toDisabled();
	}
</script>
<!--页面列表 end -->
</body>
</html>