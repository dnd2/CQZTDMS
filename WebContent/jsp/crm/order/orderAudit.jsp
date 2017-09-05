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
	function doSave(ifAgree) {
		var orderId = document.getElementById("orderId").value;
		var orderStatus = document.getElementById("orderStatus").value;
		var dutyType = document.getElementById("dutyType").value;
		var confirm="yes";
		if(ifAgree=='yes' && orderStatus=="60231003") {
			document.getElementById("if_agree").value = "yes";
			//var url = "<%=contextPath%>/crm/order/OrderManage/checkOrderNum.json?orderId="+orderId;
 			//makeSameCall(url, showInfo, "fm") ;
 			//function showInfo(json) {
 			//	orderCount=json.orderCount;
 			//	auditCount=json.auditCount;
 			//	}
 			//if(orderCount>auditCount){
 			 // confirm=window.confirm("修改前订车数量是:"+orderCount+",大于当前审核数量："+auditCount+",将提交区域经理审核！请确认！");
 			 //}
		} else {
			if(ifAgree=='yes'){
				document.getElementById("if_agree").value = "yes";
				}else{
				document.getElementById("if_agree").value = "no";
				}
		}
		
		if(dutyType=="10431003" || dutyType=="10431004" ){//区域经理审核 
			$('fm').action = "<%=contextPath%>/crm/order/OrderManage/doAreaAudit.do?orderId="+orderId+"&orderStatus="+orderStatus;
		}else{//销售经理审核
			if(confirm=="yes" || confirm==true){
			$('fm').action = "<%=contextPath%>/crm/order/OrderManage/doAudit.do?orderId="+orderId+"&orderStatus="+orderStatus;
			}else{
			return;
				}
		}
		$('fm').submit();
	}
</script>
<title>订单审核</title>
</head>
<body onunload='javascript:destoryPrototype();'>
	<div class="wbox">
		<div class="navigation">
			<img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置>潜客管理>日程管理>任务管理>订单修改/退订审核
		</div>
		<form id="fm" name="fm" method="post">
			<input type="hidden" name="curPage" id="curPage" value="1" /> 
			<input type="hidden" id="dlrId" name="dlrId" value="" />
			<input type="hidden" id="if_agree" name="if_agree" value="" />
			<input type="hidden" id="orderStatus" name="orderStatus" value="${orderStatus }" />
			<input type="hidden" id="orderId" name="orderId" value="${orderId }" />
			<input type="hidden" name="errorMsg" id="errorMsg" value="${errorMsg }" />
			<input type="hidden" id="dutyType" name="dutyType" value="${dutyType }" />
			
			<c:if test="${relation_code=='60581001' || relation_code=='60581002' }">
			<div id="oldcustomerList" >
			<b style="display: inline-block; float: left">朋友/老客户信息</b>
			<hr style="display: inline-block;">
			</div>
			</br>
			
			<table id="oldInfo" class="table_query" width="95%" align="center" >
				<tr>
					<td align="right" width="10%">介绍类型:</td>
					<td >
							<c:if test="${relation_code=='' || relation_code==null }">
								<input id="laosu" name="laosu" type="text" readonly="readonly" style="background-color: #EEEEEE;"
								class="short_txt" datatype="1,is_textarea,30" size="20" value="老客户介绍"></input>
							</c:if>
							<c:if test="${relation_code=='60581002'}">
								<input id="laosu" name="laosu" type="text" readonly="readonly" style="background-color: #EEEEEE;"
								class="short_txt" datatype="1,is_textarea,30" size="20" value="朋友介绍"></input>
							</c:if>
							<c:if test="${relation_code=='60581001'}">
								<input id="laosu" name="laosu" type="text" readonly="readonly" style="background-color: #EEEEEE;"
								class="short_txt" datatype="1,is_textarea,30" size="20" value="老客户介绍"></input>
							</c:if>
					</td>
				</tr>
				
				<tr id="Laohidden"   align="right"    >
						<td align="right" width="12%">
							朋友/老客户姓名：
						</td>
						<td width="12%" align="left" >
							<input id="old_customer_name" name="old_customer_name" type="text" readonly="readonly" style="background-color: #EEEEEE;"
								class="middle_txt" datatype="1,is_textarea,30" size="20"
								value="${oldCustomerName }" maxlength="60" />
						</td>
						<td align="right" width="12%">
							朋友/老客户电话：
						</td>
						<td width="12%" align="left" >
							<input id="old_telephone" name="old_telephone" readonly="readonly" style="background-color: #EEEEEE;"
								onchange="nameBlurChange111()" type="text" class="middle_txt"
								datatype="1,is_textarea,30" value="${oldTelephone }" size="20"
								maxlength="60" />
						</td>
						<c:if test="${relation_code!='60581002'}">
						<td align="right" width="12%" id="oldhao">
							老客户车架号：
						</td>
						<td width="17%" id="oldhao1" align="left" >
							<input id="old_vehicle_id" name="old_vehicle_id" readonly="readonly" style="background-color: #EEEEEE;"
								onchange="nameBlurChange11()" type="text" class="middle_txt"
								datatype="1,is_textarea,30" value="${oldVehicleId }" size="20"
								maxlength="60" />
						</td>
						</c:if>
					</tr>
			</table>
			</br>
			</c:if>
			
			<div id="customerList">
				<b style="display: inline-block; float: left">客户信息</b>
				<hr style="display: inline-block;">
			</div>
			</br>
			
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
						<textarea rows="1" cols="100" id="owner_address" name="owner_address" readonly="readonly" style="background-color: #EEEEEE;" >${ownerList.OWNER_ADDRESS }</textarea>
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
					
					<td align="right" width="15%">购置方式：</td>
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
				</c:forEach>
			</table>
			</br>
			</br>
			<div>
				<b style="display: inline-block; float: left">未确定车架号</b><hr style="display: inline-block;">
			</div>
			</br>
			</br>
			<table border=0 align=center id="noConfirmVehicleTable" bgcolor="rgb(218, 224, 238)" style="width:95%; margin-left: -15px; margin-top: -15px;" >
				<tr style="height: 23px; font-weight: bold; color:#39669f" id="yrow0">
					<td width="20%">车型</td><td width="5%">颜色</td><td width="4%">价格(元)</td><td width="3%">数量</td><td width="4%">总金额(元)</td><td width="4%">订金(元)</td><td width="4%">定金(元)</td><td width="6%">余款付款日期</td><td width="6%">交车日期</td><td width="4%">已交车数量</td>
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
						<input type="text" id="delivery_numberrow${x.index }" value="${table1List.DELIVERY_NUMBER }" name="delivery_numberrow${x.index }" readonly="readonly" style="background-color: #EEEEEE; width:90%; text-align: center"/>
						</td>
					</tr>
					</c:forEach>
			</table>
			
			</br>
			</br>
			</br>
			<div>
				<b style="display: inline-block; float: left">已确定车架号</b><hr style="display: inline-block;">
			</div>
			</br>
			</br>
			<table border=0 align=center id="ConfirmVehicleTable" bgcolor="rgb(218, 224, 238)" style="width:95%; margin-left: -15px; margin-top: -15px;" >
				<tr style="height: 23px; font-weight: bold; color:#39669f" id="yrow0">
					<td width="11%">VIN</td><td width="20%">车型</td><td width="5%">颜色</td><td width="4%">价格(元)</td><td width="3%">数量</td><td width="4%">总金额(元)</td><td width="4%">订金(元)</td><td width="4%">定金(元)</td><td width="6%">余款付款日期</td><td width="6%">交车日期</td><td width="4%">已交车数量</td>
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
						<input type="text" id="ydelivery_numberrow${x.index }" value="${table2List.DELIVERY_NUMBER }" name="ydelivery_numberrow${x.index }" readonly="readonly" style="background-color: #EEEEEE; width:90%; text-align: center"/>
						</td>
					</tr>
				</c:forEach>
			</table>
			</br>
			</br>
			<div>
				<b style="display: inline-block; float: left">修改/退单申请</b><hr style="display: inline-block;">
			</div>
			</br>
			</br>
			<table class="table_query" width="95%" align="center">
				<tr>
					<td align="left" width="11%">
					<c:if test="${orderStatus == '60231003'}">
						<input type="radio" readonly="readonly" disabled checked="checked" id="update_radio" name="group_radio">修改</input>
						<input type="radio" readonly="readonly" disabled id="return_radio" name="group_radio">退单</input>
					</c:if>
					<c:if test="${orderStatus == '60231004'}">
						<input type="radio" readonly="readonly" disabled id="update_radio" name="group_radio" onclick="updateClick()">修改</input>
						<input type="radio" readonly="readonly" disabled checked="checked" id="return_radio" name="group_radio" onclick="returnClick()">退单</input>
					</c:if>
					</td>
				</tr>
				<tr>
					<td align="left" colspan="1">修改/退单说明：</td>
					<td align="left" colspan="3">
						<textarea rows="5" cols="72" id="reason_remark" name="reason_remark"
						readonly="readonly" style="background-color: #EEEEEE;" >${reasonRemark }</textarea>
					</td>
				</tr>
			</table>
			
			</br>
			</br>
 			<div>
				<b style="display: inline-block; float: left">经理审核</b><hr style="display: inline-block;">
 			</div> 
 			</br> 
			</br>
			<table class="table_query" width="95%" align="center"> 
 				<!-- <tr> 
					<td align="right" width="5%">是否同意：</td>
					<td align="left" width="11%"> 
 					<select id="if_agree" name="if_agree"> 
						<option id="yes" value="yes">审核通过</option>
 						<option id="no" value="no">审核驳回</option>
 					</select>
 					</td> 
 					<td align="right" width="11%"></td> 
 					<td align="left" width="11%">
 					</td> 
 				</tr>  -->
				<tr> 
					<td align="right" colspan="1">审核意见：</td> 
 					<td align="left" colspan="3"> 
 						<textarea rows="5" cols="72" id="audit_remark" name="audit_remark"></textarea>
 					</td>
 				</tr> 
 			</table>
 			
			<table class="table_query" width="95%" align="center">
				<tr>
					<td colspan="3" align="center"><input name="queryBtn"
						type="button" class="normal_btn" id="tongguo" onclick="doSave('yes');"
						value="通过" />
						<input name="queryBtn"
						type="button" class="normal_btn" id="bohui" onclick="doSave('no');"
						value="驳回" />
						<input name="insertBtn"
						type="button" class="normal_btn" id="quxiao" onclick="javascript:history.go(-1);"
						value="取消" />
					</td>
				</tr>
			</table>
			<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
			<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
		</form>

	</div>
	<script type="text/javascript" > 
	genLocSel('dPro','dCity','dArea','','',''); // 加载省份城市和区县
</SCRIPT>

<script type="text/javascript">	
</script>

</body>
</html>