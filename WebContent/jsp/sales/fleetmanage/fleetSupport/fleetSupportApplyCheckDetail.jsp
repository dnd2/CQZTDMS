<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.Constant" %>
<%@page import="java.util.List"%>
<%
	String contextPath = request.getContextPath();
	List  executePlans = (List)request.getAttribute("executePlans");
	List  attachList   = (List)request.getAttribute("attachList");
%>

<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>集团客户支持</title>
</head>
<body onload="showPactPart() ;">
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;集团客户支持 > 集团客户支持申请> 集团客户支持申请审核</div>
<form method="post" name = "fm" >
	<div><img class="nav" src="<%=contextPath%>/img/subNav.gif" />&nbsp;提报单位信息</div>
	<table width=100% border="0" align="center" cellpadding="1" cellspacing="1" class="table_query">
		<tr>
			<th colspan="4" align="left">&nbsp;经销商信息</th>
		</tr>
		<tr>
			<td class="table_query_2Col_label_7Letter">经销商名称：</td>
			<td><c:out value="${fleetMap.COMPANY_SHORTNAME}"/></td>
			<td class="table_query_2Col_label_7Letter">批售经理名称：</td>
			<td><c:out value="${fleetMap.NAME}"/></td>
		</tr>
		<tr>
			<td class="table_query_2Col_label_7Letter">批售经理手机：</td>
			<td><c:out value="${fleetMap.PACT_MANAGE_PHONE}"/></td>
			<td class="table_query_2Col_label_7Letter">批售经理邮箱：</td>
			<td><c:out value="${fleetMap.PACT_MANAGE_EMAIL}"/></td>
		</tr>
		<tr>
			<td class="table_query_2Col_label_7Letter">提报日期：</td>
			<td><c:out value="${fleetMap.SUBMIT_DATE}"/></td>
			<td></td>
			<td></td>
		</tr>
	</table>
	<div><img class="nav" src="<%=contextPath%>/img/subNav.gif" />&nbsp;集团客户信息</div>
	<table width=100% border="0" align="center" cellpadding="1" cellspacing="1" class="table_query">
		<tr>
			<th colspan="4" align="left">&nbsp;客户信息</th>
		</tr>
		<tr>
			<td>大客户代码：</td>
			<td><c:out value="${fleetMap.FLEET_CODE}"/></td>
			<td></td>
			<td></td>
		</tr>
		<tr>
			<td class="table_query_2Col_label_4Letter">客户名称：</td>
			<td><c:out value="${fleetMap.FLEET_NAME}"/></td>
			<td class="table_query_2Col_label_4Letter">客户类型：</td>
			<td>
				<script type='text/javascript'>
					writeItemValue('<c:out value="${fleetMap.FLEET_TYPE}"/>');
				</script>
			</td>
		</tr>
		<tr>
			<td class="table_query_2Col_label_4Letter">主营业务：</td>
			<td>
				<script type='text/javascript'>
					writeItemValue('<c:out value="${fleetMap.MAIN_BUSINESS}"/>');
				</script>
			</td>
			<td class="table_query_2Col_label_4Letter">资金规模：</td>
			<td>
				<script type='text/javascript'>
					writeItemValue('<c:out value="${fleetMap.FUND_SIZE}"/>');
				</script>
			</td>
		</tr>
		<tr>
			<td class="table_query_2Col_label_4Letter">人员规模：</td>
			<td>
				<script type='text/javascript'>
					writeItemValue('<c:out value="${fleetMap.STAFF_SIZE}"/>');
				</script>
			</td>
			<td class="table_query_2Col_label_4Letter">购车用途：</td>
			<td>
				<script type='text/javascript'>
					writeItemValue('<c:out value="${fleetMap.PURPOSE}"/>');
				</script>
			</td>
		</tr>
		<tr>
			<td class="table_query_2Col_label_4Letter">区域：</td>
			<td>
				<script type='text/javascript'>
					writeRegionName('<c:out value="${fleetMap.REGION}"/>');
				</script>
			</td>
			<td class="table_query_2Col_label_4Letter">邮编：</td>
			<td><c:out value="${fleetMap.ZIP_CODE}"/></td>
		</tr>
		<tr>
			<td class="table_query_2Col_label_4Letter">是否批售项目：</td>
		    <td align="left"><script type='text/javascript'>
				writeItemValue('<c:out value="${fleetMap.IS_PACT}"/>');
			  </script>
			  <input type="hidden" name="isPact" id="isPact" value="${fleetMap.IS_PACT}" />
			  </td>
	      	<td class="table_query_2Col_label_4Letter" id="pactPart">批售项目名称：</td>
		    <td align="left">${fleetMap.PACT_NAME}</td>
	      </tr>
		<tr>
			<td class="table_query_2Col_label_4Letter">详细地址：</td>
			<td><c:out value="${fleetMap.ADDRESS}"/></td>
		</tr>
<!--		<tr>-->
<!--			<th colspan="4" align="left">&nbsp;联系人信息</th>-->
<!--		</tr>-->
		<tr>
			<td class="table_query_2Col_label_5Letter">主要联系人：</td>
			<td><c:out value="${fleetMap.MAIN_LINKMAN}"/></td>
			<td class="table_query_2Col_label_4Letter">职务：</td>
			<td><c:out value="${fleetMap.MAIN_JOB}"/></td>
		</tr>
		<tr>
			<td class="table_query_2Col_label_5Letter">电话：</td>
			<td><c:out value="${fleetMap.MAIN_PHONE}"/></td>
			<td class="table_query_2Col_label_4Letter">电子邮件：</td>
			<td><c:out value="${fleetMap.MAIN_EMAIL}"/></td>
		</tr>
		<tr style="display:none;">
			<td class="table_query_2Col_label_5Letter">其他联系人：</td>
			<td><c:out value="${fleetMap.OTHER_LINKMAN}"/></td>
			<td class="table_query_2Col_label_4Letter">职务：</td>
			<td><c:out value="${fleetMap.OTHER_JOB}"/></td>
		</tr>
		<tr style="display:none;">
			<td class="table_query_2Col_label_5Letter">电话：</td>
			<td><c:out value="${fleetMap.OTHER_PHONE}"/></td>
			<td class="table_query_2Col_label_4Letter">电子邮件：</td>
			<td><c:out value="${fleetMap.OTHER_EMAIL}"/></td>
		</tr>
		<tr>
			<th colspan="4" align="left">&nbsp;需求说明</th>
		</tr>
		<tr>
		    <td align="right" class="table_query_2Col_label_5Letter">拜访时间：</td>
		    <td colspan="3"><c:out value="${fleetMap.VISIT_DATE}"/></td>
	      </tr>
<!--	      <tr>-->
<!--		    <td align="right">车系选择：</td>-->
<!--		    <td><c:out value="${fleetMap.GROUP_NAME}"/></td>-->
<!--		    <td align="right">数量：</td>-->
<!--		    <td><c:out value="${fleetMap.SERIES_COUNT}"/></td>-->
<!--	      </tr>-->
	<tr>
		<td class="table_query_2Col_label_5Letter" width="30%">市场信息：</td>
		<td colspan="3">
			${fleetMap.MARKET_INFO}
		</td>
		
    </tr>
    <tr>
		<td class="table_query_2Col_label_5Letter">配置要求：</td>
		<td colspan="3">
			${fleetMap.CONFIG_RQUIRE}
		</td>
		
    </tr>
    <tr>
		<td class="table_query_2Col_label_5Letter">大客户要求折让：</td>
		<td colspan="3">
			${fleetMap.FLEETREQ_DISCOUNT}
		</td>
		
    </tr>
    <tr>
		<td class="table_query_2Col_label_5Letter">其他竞争车型和优惠政策：</td>
		<td colspan="3">
			${fleetMap.OTHERCOMP_FAVORPOL}
		</td>
		
    </tr>
	
    <tr>
    <td colspan="4">
		<div id="showDiv" style="width:100%;">
			<table class="table_list" style="width:100%;border:1px solid #B0C4DE" id="showTab" border="1" cellpadding="1" bordercolor="#B0C4DE" >
				<tr>
					<th nowrap="nowrap" width="20%" ><center>预计车型编码</center></th>
					<th nowrap="nowrap" width="20%" ><center>预计车型名称</center></th>
					<th nowrap="nowrap" width="10%" ><center>车系</center></th>
					<th nowrap="nowrap" width="10%" ><center>预计数量</center></th>
					<th nowrap="nowrap" width="20%" ><center>说明</center></th>
				</tr>
				<c:forEach items="${tfrdMapList}" var="frdMap">
				<tr id="tr${frdMap.DETAIL_ID}">
					<td><input type="hidden" name="materialIds" value="${frdMap.MATERIAL_ID}" >${frdMap.MATERIAL_CODE}</td>
					<td>${frdMap.MATERIAL_NAME}</td>
					<td>${frdMap.GROUP_NAME}</td>
					<td> ${frdMap.AMOUNT}</td>
					<td>${frdMap.DISCRIBE}</td>
				</tr>
				</c:forEach>
			</table>
		</div>
	</td>
	</tr>
	      <tr >
	      <td align="left">备注：</td>
	      <td colspan="3" align="left">&nbsp;&nbsp;&nbsp;&nbsp;${fleetMap.REQ_REMARK}</td>
    	 </tr>
	</table>
	<div><img class="nav" src="<%=contextPath%>/img/subNav.gif" />&nbsp;购车意向</div>
<!--	<table width=100% border="0" align="center" cellpadding="1" cellspacing="1" class="table_query">-->
<!--		<tr>-->
<!--			<th colspan="4" align="left">&nbsp;意向信息</th>-->
<!--		</tr>-->
<!--		<tr>-->
<!--			<td class="table_query_2Col_label_6Letter">预计采购日期：</td>-->
<!--			<td align="left">${intentMap.PURCHASE_DATE}</td>-->
<!--			<td class="table_query_2Col_label_6Letter">信息来源：</td>-->
<!--			<td align="left">${intentMap.INFO_GIVING_MAN}</td>-->
<!--		</tr>-->
<!--		<tr>-->
<!--			<td class="table_query_3Col_label_6Letter">竞争情况说明：</td>-->
<!--			<td  colspan="3" align="left">${intentMap.COMPETE_REMARK}</td>-->
<!--		</tr>-->
<!--		<tr>-->
<!--			<td class="table_query_2Col_label_6Letter">其他说明：</td>-->
<!--			<td  colspan="3" align="left">${intentMap.INFO_REMARK}</td>-->
<!--		</tr>-->
<!--	</table>-->
	<table width=100% border="0" align="center" cellpadding="1" cellspacing="1" class="table_query">
		<tr >
			<th colspan="11" align="left" width="100%">&nbsp;商务支持申请
			</th>
		</tr>
		<tr align="center">
			<td width="10%" align="center">意向车系</td>
			<td width="10%" align="center">数量</td>
			<td width="10%" align="center">实际成交价</td>
			<td width="5%" align="center">启票价</td>
			<td width="10%" align="center">当前促销</td>
<!--			<td width="10%" align="center">利润</td>-->
			<td width="10%" align="center">赠送、承诺</td>
			<td width="10%" align="center">市场开拓</td>
			<td width="10%" align="center">实际利润</td>
			<td width="10%" align="center">申请支持</td>
			<c:if test="${dutyType==10431001}"><td width="10%" align="center">审批意见</td></c:if>
			<td width="5%"  style="display:none;" align="center">操作</td>
		</tr>
		<tbody id="tbody1">
			<c:forEach  items="${supportInfoList}" var="listMap">
					<tr align="center" >
						<td>${listMap.GROUP_NAME} </td>
						<td>${listMap.AMOUNT}<input type="hidden" value="${listMap.AMOUNT}" name="amount"/></td>
						<td>${listMap.REAL_PRICE} <input type="hidden" value="${listMap.REAL_PRICE}" name="realprice"/><font size="5" color="red">-</font></td>
						<td>${listMap.PRICE}<input type="hidden" value="${listMap.PRICE}" name="price"/><font size="5" color="red">-</font></td>
						<td>${listMap.DEPOT_PRO_PRICE}<input type="hidden" value="${listMap.DEPOT_PRO_PRICE}" name="depotproprice"/><font size="3" color="red">+</font></td>
<!--						<td>${listMap.PROFIT} <input type="hidden" value="${listMap.PROFIT}" name="profit"/></td>-->
						<td>${listMap.GIVE_AND_ACCEPT} <input type="hidden" value="${listMap.GIVE_AND_ACCEPT}" name="gandaccept"/><font size="5" color="red">-</font></td>
						<td>${listMap.MARKET_DEVELOP} <input type="hidden" value="${listMap.MARKET_DEVELOP}" name="marketdevelop"/><font size="3" color="red">=</font></td>
						<td>${listMap.REAL_PROFIT} <input type="hidden" value="${listMap.REAL_PROFIT}" name="realprofit"/></td>
						<td>${listMap.REQUEST_SUPPORT}	<input type="hidden" value="${listMap.REQUEST_SUPPORT}" name="requestsupport"/></td>
						<c:if test="${dutyType==10431001}"><td><input id="auditmoney" name="auditmoney" type="text" class="SearchInput"  value="${listMap.AUDIT_MONEY}"size="3" maxlength="200" datatype="0,is_double,10"/></td></c:if>
						<td style="display:none;"><a href="#" onclick="delMaterial();">[删除]</a><input type='hidden' id="groupId" name="groupId"  value="${listMap.INTENT_SERIES}"></td>
					</tr>
				</c:forEach>
		</tbody>
	</table>
	
	<table width=100% border="0" align="center" cellpadding="1" cellspacing="1" class="table_query">
		<tr >
	      <td align="left">备注：</td>
	      <td  align="left">${fleetMap.SUPPORT_REMARK}</td>
    	 </tr>
	</table>
<!--	<div><img class="nav" src="<%=contextPath%>/img/subNav.gif" />&nbsp;意向附件</div>-->
<!--	<table width=100% border="0" align="center" cellpadding="1" cellspacing="1" class="table_info">-->
<!--		<tr>-->
<!--	        <th colspan="3" align="left">附件列表：<input type="hidden" id="fjids" name="fjids"/>-->
<!--				<span>-->
<!--					<input type="button" class="cssbutton"  onclick="showUpload('<%=contextPath%>')" value ='添加附件'/>-->
<!--				</span>-->
<!--			</th>-->
<!--		</tr>-->
<!--		-->
<!--		<tr>-->
<!--			<td width="100%" colspan="2"><jsp:include page="${contextPath}/uploadDiv.jsp" /></td>-->
<!--  		</tr>-->
<!--  		<table id="attachTab" class="table_info">-->
<!--  		<% if(attachList!=null&&attachList.size()!=0){ %>-->
<!--  		<c:forEach items="${attachList}" var="attls">-->
<!--		    <tr class="table_list_row1" id="${attls.FJID}">-->
<!--		    <td><a target="_blank" href="${attls.FILEURL}">${attls.FILENAME}</a></td>-->
<!--		    <td colspan="2"><input type=button onclick="delAttach('${attls.FJID}')" class="normal_btn" value="删 除"/></td>-->
<!--		    </tr>-->
<!--		</c:forEach>-->
<!--		<%} %>-->
<!--		</table>-->
<!--	</table>-->
	<table width=100% border="0" align="center" cellpadding="1" cellspacing="1" class="table_query">
		<tr align="center">
			<td colspan="5">
				<input type="hidden" name="groupIds" id="Ids"/>
				<input type="hidden" name="prices" id="prices"/>
				<input type="hidden" name="depotproprices" id="depotproPrices"/>
				<input type="hidden" name="profits" id="profits"/>
				<input type="hidden" name="gandaccepts" id="gandaccepts"/>
				<input type="hidden" name="marketdevelops" id="marketdevelops"/>
				<input type="hidden" name="realprices" id="realprices"/>
				<input type="hidden" name="realprofits" id="realprofits"/>
				<input type="hidden" name="requestsupports" id="requestsupports"/>
				<input type="hidden" name="amounts" id="amounts"/>
				<input type="hidden" name="auditmoneys" id="auditmoneys"/>
				<input type="hidden" name="delAttachs" id="delAttachs" value=""/>
				<input type="hidden" name="fleetId" id="fleetId" value="${fleetMap.FLEET_ID}"/>
			</td>
		</tr>
	</table>
	<c:if test="${checkList!=null}">
		<div><img class="nav" src="<%=contextPath%>/img/subNav.gif" />&nbsp;审核信息</div>
		<table width=100% border="0" align="center" cellpadding="1" cellspacing="1" class="table_query">
			<tr>
			<th colspan="5" align="left">&nbsp;审核记录</th>
			</tr>
			<tr align="center">
				<td>审核部门</td>
				<td>审核时间</td>
				<td>审核人</td>
				<td>审核意见</td>
				<td>审核结果</td>
			</tr>
			<c:forEach items="${checkList}" var="checkList">
				<tr align="center">
					<td>${checkList.ORG_NAME}</td>
					<td>${checkList.AUDIT_DATE}</td>
					<td>${checkList.USER_NAME}</td>
					<td>${checkList.AUDIT_REMARK}</td>
					<td>${checkList.CHECK_STATUS}</td>
				</tr>
			</c:forEach>
		</table>
	</c:if>
	<div><img class="nav" src="<%=contextPath%>/img/subNav.gif" />&nbsp;审核意见</div>
	<table width=100% border="0" align="center" cellpadding="1" cellspacing="1" class="table_query">
		<tr>
			<td class="table_query_2Col_label_6Letter">审核意见：</td>
			<td align="left">
				<textarea name="remark" id="remark" rows="4" cols="60"></textarea><font color="red">*</font>
			</td>
		</tr>
		<tr align="left">
			<td></td>
			<td>
				<input type="hidden" name="groupIds" id="Ids"/>
				<input type="hidden" name="remarks" id="remarks"/>
				<input type="hidden" name="originalPrices" id="originalPrices"/>
				<input type="hidden" name="discounts" id="discounts"/>
				<input type="hidden" name="discountPrices" id="discountPrices"/>
				<input type="hidden" name="amounts" id="amounts"/>
				<input type="hidden" name="fleetId" id="fleetId" value="${fleetMap.FLEET_ID}"/>
				<input type="hidden" name="intentId" id="intentId" value="${intentId}"/>
				<input type="hidden" name="flag" id="flag"/>
				<input class="cssbutton" name="button1" type="button" onclick="toSubmit('0');" value ="通过" />
				<input class="cssbutton" name="button3" type="button" onclick="toSubmit('1');" value ="驳回" />
				<input class="cssbutton" name="button2" type="button" onclick="toBack();" value ="返回" />
			</td>
		</tr>
	</table>
</form>
<script type="text/javascript">
	//审核校验
	function toSubmit(value){
		document.getElementById("flag").value=value;
		var str='';
		if($('remark').value==null||""==$('remark').value){
				MyAlert("审核意见不能为空！！！");
				return ;
		}
		if(value==0){
			str="确认审核通过？";
			
			toJudge();
		}else{
			str="确认驳回？"
		}
		MyConfirm(str,toConfirm);
	}
	//申请提交
	function toConfirm(){
		makeNomalFormCall('<%=request.getContextPath()%>/sales/fleetmanage/fleetSupport/FleetSupportApplyCheck/fleetSupportApplyCheckConfirm.json',showResult,'fm');
	}
	//返回
	function toBack(){
		$('fm').action='<%=request.getContextPath()%>/sales/fleetmanage/fleetSupport/FleetSupportApplyCheck/fleetSupportApplyCheckInit.do';
		$('fm').submit();
	}
	//回调方法
	function showResult(json){
		if(json.returnValue == '1'){
			window.parent.MyAlert("操作成功！");
			$('fm').action='<%=request.getContextPath()%>/sales/fleetmanage/fleetSupport/FleetSupportApplyCheck/fleetSupportApplyCheckInit.do';
			$('fm').submit();
		}else{
			MyAlert("操作失败！请联系系统管理员！");
		}
	}
	function showPactPart() {
		var iIsPact = document.getElementById("isPact").value ;
		var oPactPart = document.getElementById("pactPart") ;
		
		if(iIsPact == <%=Constant.IF_TYPE_YES%>) {
			oPactPart.style.display = "inline" ;
		} else {
			oPactPart.style.display = "none" ;
		}
	}
	//删除产品链接
	function delMaterial(){	
	  	document.getElementById("tbody1").deleteRow(window.event.srcElement.parentElement.parentElement.rowIndex-2);  
	}
	//校验
	function toJudge(){
		var groupIds ='';
		var prices='';
		var amounts = '';
		var depotproprices ='';
		var profits='';
		var gandaccepts='';
		var marketdevelops='';
		var realprices='';
		var realprofits='';
		var requestsupports='';
		var auditmoneys='';
		var groupId = document.getElementsByName("groupId");
		var price = document.getElementsByName("price");
		var amount = document.getElementsByName("amount");
		var depotproprice = document.getElementsByName("depotproprice");
		//var profit = document.getElementsByName("profit");
		var gandaccept = document.getElementsByName("gandaccept");
		var marketdevelop = document.getElementsByName("marketdevelop");
		var realprice = document.getElementsByName("realprice");
		var realprofit = document.getElementsByName("realprofit");
		var requestsupport = document.getElementsByName("requestsupport");
		var auditmoney=document.getElementsByName("auditmoney");
		for(var i=0 ;i< groupId.length; i++){
			if(price[i].value.trim()==""){
				MyAlert("价格不能为空！");
				return false;
			}
			if(amount[i].value.trim()==""){
				MyAlert("数量不能为空！");
				return false;
			}
			if(depotproprice[i].value.trim()==""){
				MyAlert("当前销价不能为空！");
				return false;
			}
		//	if(profit[i].value.trim()==""){
		//		MyAlert("利润不能为空！");
		//		return false;
		//	}
			if(marketdevelop[i].value.trim()==""){
				MyAlert("市场开拓不能为空！");
				return false;
			}
			if(gandaccept[i].value.trim()==""){
				MyAlert("赠送、承诺不能为空！");
				return false;
			}
			if(realprice[i].value.trim()==""){
				MyAlert("实际成交价不能为空！");
				return false;
			}
			if(realprofit[i].value.trim()==""){
				MyAlert("实际利润不能为空！");
				return false;
			}
			if(requestsupport[i].value.trim()==""){
				MyAlert("申请支持不能为空！");
				return false;
			}
			
			if(auditmoney.length>0){
				if(auditmoney[i].value.trim()==""){
					MyAlert("审批金额不能为空！");
					return false;
				}
			}
			
			groupIds = groupId[i].value + ',' + groupIds;
			prices=price[i].value+','+prices
			amounts = amount[i].value + ',' + amounts;
			depotproprices = depotproprice[i].value + ',' + depotproprices;
			//profits = profit[i].value + ',' + profits;
			gandaccepts = gandaccept[i].value + ',' + gandaccepts;
			marketdevelops = marketdevelop[i].value + ',' + marketdevelops;
			realprices = realprice[i].value + ',' + realprices;
			realprofits = realprofit[i].value + ',' + realprofits;
			requestsupports = requestsupport[i].value + ',' + requestsupports;
			if(auditmoney.length>0){
				auditmoneys = auditmoney[i].value + ',' + auditmoneys;
			}
		}
		document.getElementById("Ids").value=groupIds;
		document.getElementById("prices").value=prices;
		document.getElementById("amounts").value=amounts;
		document.getElementById("depotproprices").value=depotproprices;
		//document.getElementById("profits").value=profits;
		document.getElementById("gandaccepts").value=gandaccepts;
		document.getElementById("marketdevelops").value=marketdevelops;
		document.getElementById("realprices").value=realprices;
		document.getElementById("realprofits").value=realprofits;
		document.getElementById("requestsupports").value=requestsupports;
		if(auditmoney.length>0){
			document.getElementById("auditmoneys").value=auditmoneys;
		}
	}
	
	function delAttach(value){
	    var fjId = value;
	    var delAttachs = document.getElementById("delAttachs").value;
	    document.getElementById(value).style.display = "none";
	    delAttachs = delAttachs + "," + fjId;
	    document.getElementById("delAttachs").value = delAttachs;
	}
</script>
</body>
</html>