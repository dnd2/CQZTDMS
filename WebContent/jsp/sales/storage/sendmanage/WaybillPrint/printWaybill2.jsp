<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">

<%@page import="com.infodms.dms.common.Constant"%>
<%@page import="com.infodms.dms.po.TtAsWrGatherBalancePO"%>
<%@page import="com.infodms.dms.util.CommonUtils"%>

<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<%@page import="java.util.List"%>
<%@page import="java.util.Map"%>
<%@page import="com.infodms.dms.po.TmDealerPO"%>
<html xmlns="http://www.w3.org/1999/xhtml"><%@ page import="com.infodms.dms.common.Constant"%><%@ page import="com.infodms.dms.common.FileConstant"%><%@taglib uri="/jstl/cout" prefix="c"%>
<head>
<link rel="Stylesheet" href="stylesheet.css" />
<script type="text/javascript">
	var d = new Date();
	var str = d.getFullYear() + "&nbsp;年&nbsp;" + (d.getMonth()+1) + "&nbsp;月&nbsp;" + d.getDate() + "&nbsp;日";
</script>
<title></title>
<%
	String contextPath = request.getContextPath();
	Map<String, Object> valueMap = (Map<String, Object>) request
			.getAttribute("valueMap");
	List<Map<String, Object>> valueList = (List<Map<String, Object>>) request
			.getAttribute("valueList");
%>
</head>
<body>
	<center>
		<%
			if (valueList != null && valueList.size() > 0) {
				for(int i = 0; i < valueList.size();i++) {
					if(i%12==0){
						if(i!=0) {
		 %>
		 </table>
		 </div>
		 <%
			 }	
		 if(i <= valueList.size() - 12){
		 %>
		 <div style="page-break-after: always; text-align: center;">
		 <%} %>
		<table width="800px" class="tab_printBillTitle">
			<tr>
				<td align="left" width="150"><image src="${contextPath}/img/jc/weichaimotor.jpg" style="width:150px;"/></td>
				<td align="left" width="200">&nbsp;</td>
				<td align="left" width="450"><span style="font-size: 18px; font-weight: bold; font-family: 宋体">商品车出门证</span></td>
			</tr>
			<tr height="26">
				<td align="left">编号：<c:out value="${NO}"></c:out></td>
				<td></td>
				<td class="right">日期：&nbsp;&nbsp;&nbsp;&nbsp;<script type="text/javascript">document.write(str);</script></td>
			</tr>
		</table>
		<table width="800px" class="tab_BillContent">
			<tr>
				<td colspan="7">经销商名称：<font color="red">（<c:out value="${dealerName }"></c:out>）</font> <br/>
				<div id="salesOrderId">发运申请号：
					<c:forEach items="${orderNOs}" var="item">
						<c:out value="${item}"></c:out>&nbsp;
					</c:forEach>
				</div></td>
			</tr>
			<tr>
				<td colspan="3" height="35" width="40%">
					&nbsp;签发部门：营销公司/销售部/整车物流处
				</td>
				<td width="13%">&nbsp;部门负责人：</td>
				<td width="17%"></td>
				<td width="13%">&nbsp;经办人：</td>
				<td width="17%"></td>
			</tr>
			<tr>
				<td colspan="3" height="35">&nbsp;承运单位(提车单位)：<c:out value="${logiName }"></c:out></td>
				<td>&nbsp;运输车辆<br />
				&nbsp;&nbsp;车牌号</td>
				<td></td>
				<td>&nbsp;承运单位<br />
				&nbsp;&nbsp;经办人：
				</td>
				<td></td>
			</tr>
			<!-- 
			<tr>
				<td colspan="7" height="50">&nbsp;门岗确认：</td>
			</tr>
			 -->
			</table>
			<table class="tab_printBill" style="border-top: none;">
			<tr>
				<td width="4%" style="border-top: none;"><strong>序号</strong></td>
				<td width="25%" style="border-top: none;"><strong>车型名称</strong></td>
				<td width="17%" style="border-top: none;"><strong>车辆识别码(VIN号)</strong></td>
				<td width="8%" style="border-top: none;"><strong>颜色</strong></td>
				<td width="4%" style="border-top: none;"><strong>数量</strong></td>
				<td width="8%" style="border-top: none;"><strong>运输方式</strong></td>
				<td width="34%" style="border-top: none;"><strong>备注</strong></td>
			</tr>
			<%}
					Map<String, Object> veMap = (Map<String, Object>) valueList.get(i);
			%>
			<tr>
				<td><%=i + 1%>&nbsp;</td>
				<td><%=CommonUtils.checkNull(veMap.get("MODEL_NAME"))%>&nbsp;</td>
				<td><%=CommonUtils.checkNull(veMap.get("VIN"))%>&nbsp;</td>
				<td><%=CommonUtils.checkNull(veMap.get("COLOR_NAME"))%>&nbsp;</td>
					<input type="hidden" name="orderNO" value="<%=CommonUtils.checkNull(veMap.get("ORDER_NO"))%>" />
				<td>1</td>
				<td><%=CommonUtils.checkNull(veMap.get("SENDTYPE"))%>&nbsp; </td>
				<td><%=CommonUtils.checkNull(veMap.get("REMARK"))%>&nbsp;</td>
			</tr>
		<%if(i == valueList.size() - 1 && i%12 != 0){
			%>
			 </table>
			 </div>
			<%
		}}}%>
		<br />
		<table width="100%" cellpadding="1" align="center"
			onmouseover="kpr.style.display='';">
			<tr>
				<td width="100%" height="25" colspan="3"><object
						classid="CLSID:8856F961-340A-11D0-A96B-00C04FD705A2" height="0"
						id="wb" name="wb" width="3"></object>
					<div id="kpr" align="center">
						<input class="ipt" type="button" value="打印"
							onclick="kpr.style.display='none';javascript:printit();" /> <input
							class="ipt" type="button" value="打印页面设置"
							onclick="javascript:printsetup();" /> <input class="ipt"
							type="button" value="打印预览"
							onclick="kpr.style.display='none';javascript:printpreview();" />
					</div></td>
			</tr>
		</table>
	</center>
	<script type="text/javascript">
//去除打印时的页眉和页脚
var HKEY_Root,HKEY_Path,HKEY_Key;    
HKEY_Root="HKEY_CURRENT_USER";    
HKEY_Path="\\Software\\Microsoft\\Internet Explorer\\PageSetup\\";
//设置网页打印的页眉页脚为空    
function PageSetup_Null()   
{   
   try{    
       var Wsh=new ActiveXObject("WScript.Shell");    
       HKEY_Key="header";    
       Wsh.RegWrite(HKEY_Root+HKEY_Path+HKEY_Key,"");    
       HKEY_Key="footer";    
       Wsh.RegWrite(HKEY_Root+HKEY_Path+HKEY_Key,"");    
   }catch(e){}    
}
</script>
	<script language="javascript">    
  
	function printsetup()
	{       
		wb.execwb(8,1);    // 打印页面设置 
	}    
	
	function printpreview()
	{    
		wb.execwb(7,1);   // 打印页面预览       
	}  
	    
	function printit()    
	{    
		if(confirm('确定打印吗？'))
		{    
			var wayBillId=document.getElementById("wayBillId").value;
			var url = "<%=contextPath%>/sales/storage/sendmanage/WaybillPrint/updateWayBill.json";
	    	makeCall(url,updatePint,{wayBillId:wayBillId});
		}    
	}
	function updatePint(json){
		if(json.returnValue==1){
			wb.execwb(6,6)  ;
		}else if(json.returnValue==3){
			MyAlert("打印异常，请重试！");
		}
		
	}
	

	
</script>
</body>
</html>