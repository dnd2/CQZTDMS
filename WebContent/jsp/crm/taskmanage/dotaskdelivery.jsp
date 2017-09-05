<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.Constant"%>
<%@ page import="java.util.List"%>
<%@taglib uri="/jstl/cout" prefix="c"%>

<%
	String contextPath = request.getContextPath();
    int yes = Constant.IF_TYPE_YES;
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>

<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<script type="text/javascript" src="<%=request.getContextPath()%>/js/crm/customer/common.js"></script>

<script type="text/javascript">
	function doInit(){
		var msg = document.getElementById("errorMsg").value;
		if(msg!=null&&msg!="") {
			MyAlert(msg);
			msg="";
		}
		loadcalendar();   //初始化时间控件
	}
	function txtClr(valueId) {
		document.getElementById(valueId).value = '' ;
	}
</script>
<title>订单任务</title>
</head>
<body onunload='javascript:destoryPrototype();'>
	<div class="wbox" id="show">
		<div class="navigation">
			<img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置>潜客管理>日程管理>任务管理>交车任务
		</div>
		<form id="fm" name="fm" method="post">
			<input type="hidden" name="curPage" id="curPage" value="1" /> 
			<input type="hidden" id="dlrId" name="dlrId" value="" />
			<input type="hidden" name="customerId" id="customerId" value="${customerId }" />
			<input type="hidden" name="taskId" id="taskId" value="${taskId }" />
			<input type="hidden" name="errorMsg" id="errorMsg" value="${errorMsg }" />
			
			<table class="table_query" width="95%" align="center">
			<c:forEach items="${customerList }" var="customerList">
				<tr>
					<td align="right" width="12%">客户编码：</td>
					<td><input id="customer_code" name="customer_code" type="text" readonly="readonly" style="background-color: #EEEEEE;"
						class="middle_txt" datatype="1,is_textarea,30" size="20" value="${customerList.CUSTOMER_CODE }"
						maxlength="60" /></td>
					<td align="right" width="14%">客户姓名：</td>
					<td><input id="customer_name" name="customer_name" type="text" readonly="readonly" style="background-color: #EEEEEE;"
						class="middle_txt" datatype="1,is_textarea,30" size="20" value="${customerList.CUSTOMER_NAME }"
						maxlength="60" /></td>
					<td align="right" width="14%">联系电话：</td>
					<td width="12%"><input id="telephone" name="telephone" readonly="readonly" style="background-color: #EEEEEE;"
						type="text" class="middle_txt" datatype="1,is_textarea,30" value="${customerList.TELEPHONE }"
						size="20" maxlength="60" /></td>
					<td align="right" width="6%">客户地址：</td>
					<td width="12%"><input id="address" name="address" readonly="readonly" style="background-color: #EEEEEE;"
						type="text" class="middle_txt" datatype="1,is_textarea,30" value="${customerList.ADDRESS }"
						size="20" maxlength="60" /></td>
				</tr>
				
				<tr>
					<td align="right" width="6%">客户证件名：</td>
					<td><input id="paper_name" name="paper_name" type="text" readonly="readonly" style="background-color: #EEEEEE;"
						class="middle_txt" datatype="1,is_textarea,30" size="20" value="${customerList.CODE_DESC }"
						maxlength="60" /></td>
					<td align="right" width="6%">客户证件号：</td>
					<td><input id="paper_no" name="paper_no" type="text" readonly="readonly" style="background-color: #EEEEEE;"
						class="middle_txt" datatype="1,is_textarea,30" size="20" value="${customerList.PAPER_NO }"
						maxlength="60" /></td>
					<td width="3%" align="right"></td>
					<td width="22%"></td>
					<td align="right" width="15%"></td>
					<td width="25%"></td>
				</tr>
				</c:forEach>
			</table>
			</br>
			<div>
				<b style="display: inline-block; float: left">车主信息</b><hr style="display: inline-block;">
			</div>
			</br>
			<table class="table_query" width="95%" align="center">
			<c:forEach items="${ownerList }" var="ownerList">
				<tr>
					<td align="right" width="13%">车主姓名：</td>
					<td><input id="owner_name" name="owner_name" type="text" readonly="readonly" style="background-color: #EEEEEE;"
						class="middle_txt" datatype="1,is_textarea,30" size="20" value="${ownerList.OWNER_NAME }"
						maxlength="60" /></td>
					<td align="right" width="8%">车主电话：</td>
					<td><input id="owner_phone" name="owner_phone" type="text" readonly="readonly" style="background-color: #EEEEEE;"
						class="middle_txt" datatype="1,is_textarea,30" size="20" value="${ownerList.OWNER_PHONE }"
						maxlength="60" /></td>
					<td align="right" width="12%">证件类型：</td>
					<td width="25%"><input id="owner_paper_type" name="owner_paper_type" type="text" readonly="readonly" style="background-color: #EEEEEE;"
						class="middle_txt" datatype="1,is_textarea,30" size="20" value="${ownerList.PAPER_TYPE }"
						maxlength="60" /></td>
					<td align="right" width="16%">证件号码：</td>
					<td><input id="owner_paper_no" name="owner_paper_no" type="text" readonly="readonly" style="background-color: #EEEEEE;"
						class="middle_txt" datatype="1,is_textarea,30" size="20" value="${ownerList.OWNER_PAPER_NO }"
						maxlength="60" /></td>
				</tr>
				<tr>
					<td align="right" width="12%">&nbsp;省份：</td>
					<td align="left" width="24%" colspan="3">
					<input id="dPro" name="dPro" type="text" readonly="readonly" style="background-color: #EEEEEE;"
						class="short_txt" datatype="1,is_textarea,30" size="20" value="${ownerList.PRO }"
						maxlength="60" /> 城市：<input id="dCity" name="dCity" type="text" readonly="readonly" style="background-color: #EEEEEE;"
						class="short_txt" datatype="1,is_textarea,30" size="20" value="${ownerList.CITY }"
						maxlength="60" /> 区县：<input id="dArea" name="dArea" type="text" readonly="readonly" style="background-color: #EEEEEE;"
						class="short_txt" datatype="1,is_textarea,30" size="20" value="${ownerList.AREA }"
						maxlength="60" /></td>
					<td>
					</td>
					<td align="right" width="10%"></td>
					<td>
					</td>
				</tr>
				<tr>
					<td align="right" colspan="1">详细地址：</td>
					<td align="left" colspan="10">
						<textarea rows="2" cols="100" id="owner_address" name="owner_address" readonly="readonly" style="background-color: #EEEEEE;" >${ownerList.OWNER_ADDRESS }</textarea>
					</td>
				</tr>
				<tr>
					<td align="right" width="13%">新产品预售：</td>
					<td><input id="new_product_sale" name="new_product_sale" type="text" readonly="readonly" style="background-color: #EEEEEE;"
						class="short_txt" datatype="1,is_textarea,30" size="20" value="${ownerList.PRODUCT_SALE }"
						maxlength="60" />
					</td>
					<td align="right" width="15%">意向等级：</td>
					<td width="25%"><input id="intent_type" name="intent_type" type="text" readonly="readonly" style="background-color: #EEEEEE;"
						class="short_txt" datatype="1,is_textarea,30" size="20" value="${ownerList.CTM_RANK }"
						maxlength="60" /></td>
					<td align="right" width="8%">销售流程进度：</td>
					<td><input id="sales_progress" name="sales_progress" type="text" readonly="readonly" style="background-color: #EEEEEE;"
						class="short_txt" datatype="1,is_textarea,30" size="20" value="${ownerList.SALES_PROGRESS }"
						maxlength="60" /></td>
					<td align="right" width="13%"></td>
					<td></td>
				</tr>
				<tr>
					
					<td align="right" width="15%">成交类型：</td>
					<td width="20%"><input id="deal_type" name="deal_type" type="text" readonly="readonly" style="background-color: #EEEEEE;"
						class="middle_txt" datatype="1,is_textarea,30" size="20" value="${ownerList.DEAL_TYPE }"
						maxlength="60" /></td>
					<td align="right" width="15%">试乘试驾：</td>
					<td  width="25%">
						<input id="test_driving" name="test_driving" type="text" readonly="readonly" style="background-color: #EEEEEE;"
						class="short_txt" datatype="1,is_textarea,30" size="20" value="${ownerList.IF_DRIVE }"
						maxlength="60" />
					</td>
					<td align="right" width="14%"></td>
					<td>
					</td>
					<td></td>
					<td></td>
				</tr>
				<%-- <tr>
					 <td align="center" colspan="8">
						<a href="#" id="FOLLOW_ID" name="FOLLOW_ID" onclick='followOpenInfo("${customerId}")'>客户资料维护</a>
					</td>
				</tr> --%>
				</c:forEach>
			</table>
			</br>
			</br>
			<div>
				<b style="display: inline-block; float: left">未确定车架号</b><hr style="display: inline-block;">
			</div>
			</br>
			</br>
			<center>
			<table border=0  id="noConfirmVehicleTable"  style="width:95%; margin-left: -15px; margin-top: -15px;background-color: #DAE0EE;" >
				<tr style="height: 23px; font-weight: bold; color:#39669f" id="row0">
					<td width="20%">&nbsp;&nbsp;&nbsp;车型</td><td width="5%">颜色</td><td width="4%">价格(元)</td><td width="3%">数量</td><td width="4%">总金额(元)</td><td width="4%">订金(元)</td><td width="4%">定金(元)</td><td width="6%">余款付款日期</td><td width="6%">交车日期</td><td width="4%">已交车数量</td>
				</tr>
					<c:forEach items="${table1List }" var="table1List" varStatus="x">
					<tr style="height: 23px;background-color: white;border: 0; border-color:#44BBBB; ">
						<td style="text-align: center">
						<input type="text" class="middle_txt" id="materialCoderow${x.index }" name="materialCoderow${x.index }"  value="${table1List.GROUP_NAME }" style="background-color: #EEEEEE; width:97%" readonly="readonly"/>
						</td>
						<td style="text-align: center">
						<input type="text" class="short_txt" id="colorrow${x.index }" name="colorrow${x.index }"  value="${table1List.COLOR }" style="background-color: #EEEEEE; width:90%" readonly="readonly"/>
						</td>
						<td style="text-align: center">
						<input type="text" id="pricerow${x.index }" name="pricerow${x.index }" value="${table1List.PRICE }" style="background-color: #EEEEEE; width:90%" readonly="readonly"/>
						</td>
						<td style="text-align: center">
						<input type="text" id="numrow${x.index }" name="numrow${x.index }" value="${table1List.NUM }" style="background-color: #EEEEEE; width:90%" readonly="readonly"/>
						</td>
						<td style="text-align: center">
						<input type="text" id="amountrow${x.index }" name="amountrow${x.index }" value="${table1List.AMOUNT }" style="background-color: #EEEEEE; width:90%" readonly="readonly" />
						</td>
						<td style="text-align: center">
						<input type="text" id="depositrow${x.index }" name="depositrow${x.index }" value="${table1List.DEPOSIT }" style="background-color: #EEEEEE; width:90%" readonly="readonly"/>
						</td>
						<td style="text-align: center">
						<input type="text" id="earnestrow${x.index }" name="earnestrow${x.index }" value="${table1List.EARNEST }" style="background-color: #EEEEEE; width:90%" readonly="readonly"/>
						</td>
						<td style="text-align: center">
						<input type="text" class="short_txt" id="pre_pay_daterow${x.index }" name="pre_pay_daterow${x.index }" value="${table1List.BALANCE_DATE }" style="background-color: #EEEEEE; width:90%" readonly="readonly"></input>
						</td>
						<td style="text-align: center">
						<input type="text" class="short_txt" id="pre_delivery_daterow${x.index }" name="pre_delivery_daterow${x.index }" value="${table1List.DELIVERY_DATE }" style="background-color: #EEEEEE; width:90%" readonly="readonly"></input>
						</td>
						<td style="text-align: left">
						<!-- 此处只有顾问登陆时才有保存按钮 -->
						<c:if test="${adviserLogon=='yes' }">
						<input type="text" id="delivery_numberrow${x.index }" value="${table1List.DELIVERY_NUMBER }" name="delivery_numberrow${x.index }" readonly="readonly" style="background-color: #EEEEEE; width:30px"/><input type="button" onclick="delv('',${customerId },${table1List.DETAIL_ID},'inpuId','',${table1List.NUM },${table1List.DELIVERY_NUMBER })" value="交车" style="margin-left: 5px" />
						</c:if>
						<c:if test="${adviserLogon!='yes' }">
						<input type="text" id="delivery_numberrow${x.index }" value="${table1List.DELIVERY_NUMBER }" name="delivery_numberrow${x.index }" readonly="readonly" style="background-color: #EEEEEE; width:30px"/>
						</c:if>
						</td>
					</tr>
					</c:forEach>
			</table>
			</center>
			</br>
			</br>
			</br>
			<div>
				<b style="display: inline-block; float: left">已确定车架号</b><hr style="display: inline-block;"/>
			</div>
			</br>
			</br>
			<center>
			<table border=0   id="ConfirmVehicleTable"  style="width:95%; margin-left: -15px; margin-top: -15px;background-color: #DAE0EE;" >
				<tr style="height: 23px; font-weight: bold; color:#39669f" id="yrow0">
					<td width="11%">&nbsp;&nbsp;&nbsp;VIN</td><td width="16%">车型</td><td width="5%">颜色</td><td width="4%">价格(元)</td><td width="3%">数量</td><td width="5%">总金额(元)</td><td width="4%">订金(元)</td><td width="4%">定金(元)</td><td width="6%">余款付款日期</td><td width="6%">交车日期</td><td width="4%">已交车数量</td>
				</tr>
					<c:forEach items="${table2List }" var="table2List" varStatus="x">
					<tr style="height: 23px;background-color: white;border: 0; border-color:#44BBBB; ">
						<td style="text-align: center">
						<input type="text" class="middle_txt" id="yvinrow${x.index }" name="yvinrow${x.index }"  value="${table2List.VIN }" style="background-color: #EEEEEE; width:95%"   readonly="readonly"/>
						</td>
						<td style="text-align: center">
						<input type="text" class="middle_txt" id="ymaterialCoderow${x.index }" name="ymaterialCoderow${x.index }"  value="${table2List.MATERIAL_NAME }" style="background-color: #EEEEEE; width:97%" readonly="readonly"/>
						</td>
						<td style="text-align: center">
						<input type="text" class="short_txt" id="ycolorrow${x.index }" name="ycolorrow${x.index }"  value="${table2List.COLOR }" style="background-color: #EEEEEE; width:90%" readonly="readonly"/>
						</td>
						<td style="text-align: center">
						<input type="text" id="ypricerow${x.index }" name="ypricerow${x.index }" value="${table2List.PRICE }" style="background-color: #EEEEEE; width:90%" readonly="readonly"/>
						</td>
						<td style="text-align: center">
						<input type="text" id="ynumrow${x.index }" name="ynumrow${x.index }" value="${table2List.NUM }" style="background-color: #EEEEEE; width:90%" readonly="readonly"/>
						</td>
						<td style="text-align: center">
						<input type="text" id="yamountrow${x.index }" name="yamountrow${x.index }" value="${table2List.AMOUNT }" style="background-color: #EEEEEE; width:90%" readonly="readonly" />
						</td>
						<td style="text-align: center">
						<input type="text" id="ydepositrow${x.index }" name="ydepositrow${x.index }" value="${table2List.DEPOSIT }" style="background-color: #EEEEEE; width:90%" readonly="readonly"/>
						</td>
						<td style="text-align: center">
						<input type="text" id="yearnestrow${x.index }" name="yearnestrow${x.index }" value="${table2List.EARNEST }" style="background-color: #EEEEEE; width:90%" readonly="readonly"/>
						</td>
						<td style="text-align: center">
						<input type="text" class="short_txt" id="ypre_pay_daterow${x.index }" name="ypre_pay_daterow${x.index }" value="${table2List.BALANCE_DATE }" style="background-color: #EEEEEE; width:90%" readonly="readonly"></input>
						</td>
						<td style="text-align: center">
						<input type="text" class="short_txt" id="ypre_delivery_daterow${x.index }" name="ypre_delivery_daterow${x.index }" value="${table2List.DELIVERY_DATE }" style="background-color: #EEEEEE; width:90%" readonly="readonly"></input>
						</td>
						<td style="text-align: left">
						<!-- 此处只有顾问登陆时才有保存按钮 -->
						<c:if test="${adviserLogon=='yes' }">
						<input type="text" id="ydelivery_numberrow${x.index }" value="${table2List.DELIVERY_NUMBER }" name="ydelivery_numberrow${x.index }" readonly="readonly" style="background-color: #EEEEEE; width:30px"/><input type="button" onclick="doDelivery(${table2List.VEHICLE_ID},${customerId },${table2List.DETAIL_ID},this,${table2List.NUM },${table2List.DELIVERY_NUMBER })" value="交车" style="margin-left: 5px" id="deliveryButton${x.index }" />
						</c:if>
						<c:if test="${adviserLogon!='yes' }">
						<input type="text" id="ydelivery_numberrow${x.index }" value="${table2List.DELIVERY_NUMBER }" name="ydelivery_numberrow${x.index }" readonly="readonly" style="background-color: #EEEEEE; width:30px"/>
						</c:if>
						</td>
					</tr>
				</c:forEach>
			</table>
			</center>
			</br>
			</br>
			<table class="table_query" width="95%" align="center">
				<tr>
					<td colspan="3" align="center">
						<input name="insertBtn"
						type="button" class="normal_btn" id="quxiao" onclick="doBackTaskInit();"
						value="取消" />
					</td>
				</tr>
			</table>
			<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
			<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
		</form>

	</div>
	<script type="text/javascript"> 
	//VIN选择页面
	function delv(vechile_id,qkId,qkOrderDetailId,inputId,moderId,colorTdId,hiddenmoderId){
		// MyAlert("vechile_id=="+vechile_id+" qkId=="+qkId+" moderId=="+moderId+"  hiddenmoderId=="+hiddenmoderId);
		var orderDetailId=qkOrderDetailId;
		var delvFlag=judgeIfAbleDelvy(orderDetailId);
		if(!delvFlag){
			MyAlert("订单状态不对，无法交车!!!");
			return;
		}
		var telephone=$("telephone").value;
		var flag=judgeIfAbleOderDate(telephone);
		if(!flag){
			MyAlert("DCRC未录入该客户今日到店客流信息，不能做交车！！！");
			return;
		}
		if(hiddenmoderId>=colorTdId) {
			MyAlert("已交车数量已为实际数量，不能交车！");
			return false;
		} else {
			OpenHtmlWindow("<%=contextPath%>/crm/delivery/DelvManage/toVinList.do?inputId="+inputId+"&moderId="+moderId+"&qkId="+qkId+"&qkOrderDetailId="+qkOrderDetailId,800,600);
		}
	}
	
	//交车页面+交车
	function doDelivery(vehicle_id,qkid,qkOrderDetailId,obj,num,renum) {
		var scrollHeight = document.getElementById("show").offsetHeight;//获取显示页面div的高度
		var orderDetailId=qkOrderDetailId;
		var delvFlag=judgeIfAbleDelvy(orderDetailId);
		if(!delvFlag){
			MyAlert("订单状态不对，无法交车!!!");
			return;
	   }
		var telephone=$("telephone").value;
		var flag=judgeIfAbleOderDate(telephone);
		if(!flag){
			MyAlert("DCRC未录入该客户今日到店客流信息，不能做交车！！！");
			return ;
		}
		if(renum>=num) {
			MyAlert("已交车数量已为实际数量，不能交车！");
			return false;
		} else {
			obj.disabled = true;
			var url = "<%=contextPath%>/crm/delivery/DelvManage/addDelvData.json?vehicle_id="+vehicle_id+"&qkId="+qkid+"&qkOrderDetailId="+qkOrderDetailId;
		}
		makeFormCall(url, showInfo, "fm") ;
		addMaskLayer(scrollHeight);//添加蒙层
		function showInfo(json) {
//  			if(json.ps[0]!=null) {
 				MyAlert("交车成功!");
				$('fm').action = "<%=contextPath%>/crm/taskmanage/TaskManage/doTaskDeliveryInit.do?orderId=null&taskId="+qkOrderDetailId+"&customerId="+qkid;
				$('fm').submit();
				_hide();
//  			} else {
//  				MyAlert("交车失败,请联系管理员！");
//  			}
		}
	}
	function followOpenInfo(customerId){
		var context=null;
		var openUrl = "<%=request.getContextPath()%>/crm/customer/CustomerManage/ctmUpdateInit.do?ctmId="+customerId;
		var parameters="fullscreen=yes, toolbar=yes, menubar=no, scrollbars=no, resizable=yes, location=no, status=yes";
		window.open (openUrl, "newwindow");
	}
	function doBackTaskInit() {
		$('fm').action = "<%=contextPath%>/crm/taskmanage/TaskManage/doInit.do";
		$('fm').submit();
	}
</script>

</body>
</html>