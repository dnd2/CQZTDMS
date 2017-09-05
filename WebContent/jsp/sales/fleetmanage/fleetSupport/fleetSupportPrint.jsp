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
<title class="noneprint">集团客户信息</title>
<script type="text/javascript">
function showPactPart() {
	var iIsPact = document.getElementById("isPact").value ;
	var oPactPart = document.getElementById("pactPart") ;
	
	if(iIsPact == <%=Constant.IF_TYPE_YES%>) {
		oPactPart.style.display = "inline" ;
	} else {
		oPactPart.style.display = "none" ;
	}
}


function printsetup(){
    //  打印页面设置
    var wb =$('wb');
    wb.execwb(8,1);   
}
function myPrint(){
	window.print();
}
function printView(){
	var wb =$('wb');
	wb.execwb(7,1);
}
</script>
<style media="print">  
            .noprint { display : none; }  ;
</style>
</head>
<body >
<!--<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;集团客户支持 > 集团客户支持查询> 集团客户打印信息</div>-->
<form method="post" name = "fm" >
<!-- 去掉页眉的控件 -->

 <!-- 打印设置的控件-->
 <OBJECT classid="CLSID:8856F961-340A-11D0-A96B-00C04FD705A2" height="0" id="wb" name="wb" width="0"></OBJECT>
     <div style="height: 20px;">&nbsp;</div>    
    <div style="height: 20px;">经销商信息</div>
	<table width="99%"  align="center"  border="1" cellspacing="0" cellpadding="0"  bordercolor="#4F4F4F"  >
		<tr height="25px;">
			<td align="right" width="14%">经销商名称：</td>
			<td align="left" width="20%"><c:out value="${fleetMap.COMPANY_SHORTNAME}"/></td>
			<td align="right" width="13%">批售经理名称：</td>
			<td align="left" width="20%"><c:out value="${fleetMap.NAME}"/></td>
			<td align="right" width="13%">批售经理手机：</td>
			<td align="left" width="20%"><c:out value="${fleetMap.PACT_MANAGE_PHONE}"/> </td>
		</tr>
		<tr height="25px">
			<td align="right">批售经理邮箱：</td>
			<td align="left"><c:out value="${fleetMap.PACT_MANAGE_EMAIL}"/>&nbsp;</td>
			<td align="right">信息提报日期：</td>
			<td align="left"><c:out value="${fleetMap.SUBMIT_DATE}"/></td>
			<td align="right">&nbsp; </td>
			<td align="left"> &nbsp; </td>
		</tr>
	</table>
	<div style="height: 20px;">&nbsp;</div>
	<div style="height: 20px;">客户信息</div>
	<table width="99%" border="1" align="center"  cellspacing="0"  cellpadding="0"  bordercolor="#4F4F4F">
		<tr height="25px">
			<td align="right" width="14%">大客户代码：</td>
			<td align="left" width="20%"><c:out value="${fleetMap.FLEET_CODE}"/>&nbsp;</td>
			<td align="right" width="13%">客户名称：</td>
			<td align="left" width="20%"><c:out value="${fleetMap.FLEET_NAME}"/> &nbsp;</td>
			<td align="right" width="13%">客户类型：</td>
			<td align="left" width="20%">
				<script type='text/javascript'>
					writeItemValue('<c:out value="${fleetMap.FLEET_TYPE}"/>');
				</script>
				&nbsp;
			</td>
		</tr>
		<tr height="25px">
			<td align="right">主营业务：</td>
			<td align="left">
				<script type='text/javascript'>
					writeItemValue('<c:out value="${fleetMap.MAIN_BUSINESS}"/>');
				</script>
				&nbsp;
			</td>
			<td align="right">资金规模：</td>
			<td align="left">
				<script type='text/javascript'>
					writeItemValue('<c:out value="${fleetMap.FUND_SIZE}"/>');
				</script>
				&nbsp;
			</td>
			<td align="right">人员规模：</td>
			<td align="left">
				<script type='text/javascript'>
					writeItemValue('<c:out value="${fleetMap.STAFF_SIZE}"/>');
				</script>
				&nbsp;
			</td>
		</tr>
		<tr height="25px">
			
			<td align="right">购车用途：</td>
			<td align="left">
				<script type='text/javascript'>
					writeItemValue('<c:out value="${fleetMap.PURPOSE}"/>');
				</script>
				&nbsp;
			</td>
			<td align="right">区域：</td>
			<td align="left">
				<script type='text/javascript'>
					writeRegionName('<c:out value="${fleetMap.REGION}"/>');
				</script>
				&nbsp;
			</td>
			<td align="right">邮编：</td>
			<td align="left"><c:out value="${fleetMap.ZIP_CODE}"/>&nbsp;</td>
		</tr>
		<tr height="25px">
			<td align="right">详细地址：</td>
			<td align="left"><c:out value="${fleetMap.ADDRESS}"/> &nbsp;</td>
			<td align="right">主要联系人：</td>
			<td align="left"><c:out value="${fleetMap.MAIN_LINKMAN}"/> &nbsp;</td>
			<td align="right">职务：</td>
			<td align="left"><c:out value="${fleetMap.MAIN_JOB}"/> &nbsp;</td>
		</tr>
		<tr height="25px">
			<td align="right">联系电话：</td>
			<td align="left"><c:out value="${fleetMap.MAIN_PHONE}"/> &nbsp;</td>
			<td align="right">电子邮件：</td>
			<td align="left"><c:out value="${fleetMap.MAIN_EMAIL}"/>&nbsp;</td>
			<td align="right">&nbsp;</td>
			<td align="left">&nbsp;</td>
		</tr>
	</table>
	<div style="height: 20px;">&nbsp;</div>
	<div>需求说明</div>
	<table width="99%" border="1" align="center"  cellspacing="0"  cellpadding="0"  bordercolor="#4F4F4F">
		<tr height="25px">
		    <td align="right"  width="20%">拜访时间：</td>
		    <td colspan="2" align="left" width="30%"><c:out value="${fleetMap.VISIT_DATE}"/>&nbsp;</td>
		    <td align="right" width="20%">市场信息：</td>
			<td  colspan="2" width="30%">
				${fleetMap.MARKET_INFO}&nbsp;
			</td>
	      </tr>
    <tr height="25px">
		<td align="right">配置要求：</td>
		<td  colspan="2" align="left">
			${fleetMap.CONFIG_RQUIRE}&nbsp;
		</td>
		<td align="right">大客户要求折让：</td>
		<td colspan="2"  align="left">
			${fleetMap.FLEETREQ_DISCOUNT}&nbsp;
		</td>
    </tr>
    <tr height="25px">
		<td align="right">其他竞争车型和优惠政策：</td>
		<td colspan="2" align="left">
			${fleetMap.OTHERCOMP_FAVORPOL}&nbsp;
		</td>
		<td align="right">&nbsp;</td>
		<td colspan="2" align="left">&nbsp;</td>
    </tr>
	</table>
	<div style="height: 20px;">&nbsp;</div>
	<div style="height: 20px;">预计车型</div>
			<table  width="99%" border="1" align="center"  cellspacing="0"  cellpadding="0"  bordercolor="#4F4F4F">
				<tr height="25px">
					<th nowrap="nowrap" width="20%" ><center>预计车型编码</center></th>
					<th nowrap="nowrap" width="20%" ><center>预计车型名称</center></th>
					<th nowrap="nowrap" width="10%" ><center>车系</center></th>
					<th nowrap="nowrap" width="10%" ><center>预计数量</center></th>
					<th nowrap="nowrap" width="20%" ><center>说明</center></th>
				</tr>
				<c:forEach items="${tfrdMapList}" var="frdMap">
				<tr id="tr${frdMap.DETAIL_ID}" height="25px">
					<td><input type="hidden" name="materialIds" value="${frdMap.MATERIAL_ID}" >${frdMap.MATERIAL_CODE}</td>
					<td>${frdMap.MATERIAL_NAME}&nbsp;</td>
					<td>${frdMap.GROUP_NAME}&nbsp;</td>
					<td> ${frdMap.AMOUNT}&nbsp;</td>
					<td>${frdMap.DISCRIBE}&nbsp;</td>
				</tr>
				</c:forEach>
				<tr height="25px">
				 	<td align="left" width="20%">备注：</td>
	      			<td colspan="5" align="left" width="80%"style="TABLE-LAYOUT:fixed;WORD-BREAK:break-all">&nbsp;&nbsp;&nbsp;&nbsp;${fleetMap.REQ_REMARK}</td>
	     		 </tr>
			</table>
	<div style="height: 20px;">&nbsp;</div>
	<div style="height: 20px;">商务支持申请</div>
		<table width="99%" border="1" align="center"  cellspacing="0"  cellpadding="0"  bordercolor="#4F4F4F">
		<tr align="center" height="25px">
			<td width="10%" align="center" align="right">意向车系</td>
			<td width="10%" align="center" align="right">数量</td>
			<td width="10%" align="center" align="right">实际成交价</td>
			<td width="5%" align="center" align="right">启票价</td>
			<td width="10%" align="center" align="right">当前促销价</td>
			<td width="10%" align="center" align="right">赠送、承诺</td>
			<td width="10%" align="center" align="right">市场开拓</td>
			<td width="10%" align="center" align="right">实际利润</td>
			<td width="10%" align="center" align="right">申请支持</td>
			<td width="20%" align="center" align="right">审批金额</td>
		</tr>
		<tbody id="tbody1">
			<c:forEach  items="${supportInfoList}" var="listMap">
					<tr align="center" height="25px" >
						<td>${listMap.GROUP_NAME}&bsp; </td>
						<td>${listMap.AMOUNT}<input type="hidden" value="${listMap.AMOUNT}" name="amount"/>&nbsp;</td>
						<td>${listMap.REAL_PRICE} <input type="hidden" value="${listMap.REAL_PRICE}" name="realprice"/>&nbsp;</td>
						<td>${listMap.PRICE}<input type="hidden" value="${listMap.PRICE}" name="price"/>&nbsp;</td>
						<td>${listMap.DEPOT_PRO_PRICE}<input type="hidden" value="${listMap.DEPOT_PRO_PRICE}" name="depotproprice"/>&nbsp;</td>
						<td>${listMap.GIVE_AND_ACCEPT} <input type="hidden" value="${listMap.GIVE_AND_ACCEPT}" name="gandaccept"/>&nbsp;</td>
						<td>${listMap.MARKET_DEVELOP} <input type="hidden" value="${listMap.MARKET_DEVELOP}" name="marketdevelop"/>&nbsp;</td>
						<td>${listMap.REAL_PROFIT} <input type="hidden" value="${listMap.REAL_PROFIT}" name="realprofit"/>&nbsp;</td>
						<td>${listMap.REQUEST_SUPPORT}</td>
						<td>${listMap.AUDIT_MONEY}&nbsp;</td>
					</tr>
				</c:forEach>
			<tr height="40px;">
		      <td align="right" width="10%">备注：</td>
		      <td  align="left" colspan="11" style="TABLE-LAYOUT:fixed;WORD-BREAK:break-all" width="90%">${fleetMap.SUPPORT_REMARK}&nbsp;</td>
    	 	</tr>
		</tbody>
	</table>
	<br/>
	<div>审核意见</div>
	<br/>
	<table width="99%" border="1" align="center"  cellspacing="0"  cellpadding="0"  bordercolor="#4F4F4F">
		<tr>
			<td>外部审核人</td>
			<td>外部审核意见</td>
			<td>内部审核人</td>
			<td>内部审核意见</td>
			<td>大客户系审核人</td>
			<td>大客户系审核意见</td>
		</tr>
		<tr>
			<td align="left" width="15%"style="TABLE-LAYOUT:fixed;WORD-BREAK:break-all">${smallAudit.USER_NAME}&nbsp;</td>
			<td align="left" width="15%"style="TABLE-LAYOUT:fixed;WORD-BREAK:break-all">${smallAudit.AUDIT_REMARK}&nbsp;</td>
			<td  align="left" width="15%"style="TABLE-LAYOUT:fixed;WORD-BREAK:break-all" height="60px;">${largeAudit.USER_NAME}&nbsp;</td>
			<td align="left" width="15%"style="TABLE-LAYOUT:fixed;WORD-BREAK:break-all">${smallAudit.AUDIT_REMARK}&nbsp;</td>
			<td align="left" width="15%"style="TABLE-LAYOUT:fixed;WORD-BREAK:break-all">&nbsp;</td>
			<td align="left" width="20%"style="TABLE-LAYOUT:fixed;WORD-BREAK:break-all">&nbsp;</td>
		</tr>
	</table>
	<div>&nbsp;</div>
	<table width=99% border="0" align="center" cellpadding="1" cellspacing="1"  style="background-color: white;" class="table_query" >
		<tr align="center">
			<td >
			<input class="cssbutton  noprint " name="button2" type="button" onclick="printsetup();"  value ="打印设置" />
			<input class="cssbutton  noprint" name="button2" type="button" onclick="printView();"  value ="打印预览" />
			<input class="cssbutton  noprint" name="button2" type="button" onclick="myPrint();"   value ="打印" />
			<input class="cssbutton   noprint" name="button2" type="button" onclick="window.close();"  value ="关闭" />
			</td>
		</tr>
	</table>
</form>
</body>
</html>