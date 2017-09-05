<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">

<%@page import="com.infodms.dms.common.Constant"%>
<%@page import="com.infodms.dms.po.TtAsWrGatherBalancePO"%>
<%@page import="com.infodms.dms.util.CommonUtils"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />

<%@page import="java.util.List"%>
<%@page import="java.util.Map"%>
<%@page import="com.infodms.dms.po.TmDealerPO"%><html xmlns="http://www.w3.org/1999/xhtml"><%@ page import="com.infodms.dms.common.Constant"%><%@ page import="com.infodms.dms.common.FileConstant"%><%@taglib uri="/jstl/cout" prefix="c"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title></title>
<% 
String contextPath = request.getContextPath(); 
Map<String,Object> valueMap = (Map<String,Object>)request.getAttribute("valueMap");
List<Map<String,Object>> valueList = (List<Map<String,Object>>)request.getAttribute("valueList");

%>
</head>
<body>
<br/>
<center><strong><font size="4">重庆市嘉陵川江汽车制造有限公司-商品车运输单</font></strong></center>
<br/>
<table class="tabp2" align="center" width="750px" border="0">
<tr><td width="10%">发运日期：</td><td width="10%"><%=valueMap==null?"":CommonUtils.checkNull(valueMap.get("BILL_CRT_DATE")) %></td>
	<td width="10%">应到达日期：</td><td width="10%">${expectArriveDate }</td>
	<td width="10%">险种：</td><td width="10%"><%=valueMap==null?"":CommonUtils.checkNull(valueMap.get("CODE_DESC")) %></td>
	<td width="10%">保单号：</td><td width="10%"><%=valueMap==null?"":CommonUtils.checkNull(valueMap.get("POLICY_NO")) %></td>
	<td width="10%">运单号：</td><td width="10%"><%=valueMap==null?"":CommonUtils.checkNull(valueMap.get("BILL_NO")) %><input type="hidden" id="wayBillId" value="${valueMap.BILL_ID }"/></td>
</tr>
</table>
<table class="tabp2" align="center" width="750px" border="1">
<tr align="center"><td align="center"  class="tdp" width="12%">收车经销商</td><td class="tdp"><%=valueMap==null?"":CommonUtils.checkNull(valueMap.get("DEALER_NAME")) %></td>
	<td align="center"  class="tdp" width="8%">联系人</td><td class="tdp"><%=valueMap==null?"           ":CommonUtils.checkNull(valueMap.get("LINK_MAN")) %></td>
	<td align="center"  class="tdp" width="8%">电话</td><td class="tdp" width="12%"><%=valueMap==null?"           ":CommonUtils.checkNull(valueMap.get("PHONE")) %></td>
</tr>
<tr align="center"><td align="center"  class="tdp" width="12%">交车地址</td><td class="tdp"><%=valueMap==null?"":CommonUtils.checkNull(valueMap.get("ADDRESS")) %></td>
	<td align="center"  class="tdp" width="8%">地点</td><td class="tdp"><%=valueMap==null?"          ":CommonUtils.checkNull(valueMap.get("REGION_NAME")) %></td>
	<td align="center"  class="tdp" width="8%">手机</td><td class="tdp" width="12%"><%=valueMap==null?"           ":CommonUtils.checkNull(valueMap.get("TEL")) %></td>
</tr>
<tr align="center"><td align="center"  class="tdp" width="12%">组板号</td><td colspan="5" class="tdp"><%=valueMap==null?"":CommonUtils.checkNull(valueMap.get("BONO")) %></td>
</tr>
</table>
<table class="tabp2" align="center"  width="750px" border="1">
<tr>
<td align="center" class="tdp">序号</td>
<td align="center" class="tdp">汽车型号</td>
<td align="center"  class="tdp">发动机</td>
<td align="center"  class="tdp">颜色</td>
<td align="center" class="tdp">流水号</td>
<td align="center"  class="tdp">VIN号</td>
<td align="center"  class="tdp">票据号</td>
<!-- 
<td align="center"  class="tdp">承运车队</td>
<td align="center"  class="tdp">承运车号</td>
-->
</tr>
<% if(valueList!=null && valueList.size()>0){
		  for(int i=0;i<valueList.size();i++){
		    Map<String,Object> veMap = (Map<String,Object>)valueList.get(i);%>
		    <tr><td align="center"  class="tdp"><%=i+1%>&nbsp;</td>
		    <td align="center"  class="tdp"><%=CommonUtils.checkNull(veMap.get("MODEL_NAME"))%>&nbsp;</td>
		    <td align="center"  class="tdp"><%=CommonUtils.checkNull(veMap.get("ENGINE_NO"))%>&nbsp;</td>
		    <td align="center"  class="tdp"><%=CommonUtils.checkNull(veMap.get("COLOR_NAME"))%>&nbsp;</td>
		    <td align="center"  class="tdp"><%=CommonUtils.checkNull(veMap.get("SD_NUMBER"))%></td>
		    <td align="center"  class="tdp"><%=CommonUtils.checkNull(veMap.get("VIN"))%>&nbsp;</td>
		    <td align="center"  class="tdp"><%=CommonUtils.checkNull(veMap.get("INVOICE_NO"))%>&nbsp;</td>
		  <!--   <td align="center"  class="tdp"><%=CommonUtils.checkNull(veMap.get("CAR_TEAM"))%>&nbsp;</td>
		    <td align="center"  class="tdp"><%=CommonUtils.checkNull(veMap.get("CAR_NO"))%>&nbsp;</td>  -->
		    </tr>
	<%}} %>

</table>
<table width="750px" align="center" class="tabp2" border="1">
     <tr align="center">
		<td align="center"  class="tdp" width="12%">车队经办人</td>
		<td class="tdp"><%=valueMap==null?"":CommonUtils.checkNull(valueMap.get("NAME")) %></td>
		<td align="center"  class="tdp" width="12%">承运车队</td>
		<td class="tdp"><%=valueMap==null?"":CommonUtils.checkNull(valueMap.get("CAR_TEAM")) %></td>
		<td align="center"  class="tdp" width="12%">运输车号</td>
		<td class="tdp"><%=valueMap==null?"":CommonUtils.checkNull(valueMap.get("CAR_NO")) %></td>
		<td align="center"  class="tdp" width="12%">总数</td>
		<td class="tdp"><%=valueMap==null?"":CommonUtils.checkNull(valueMap.get("VEH_NUM")) %></td>
 	</tr>
 	<tr align="center">
		<td align="center" class="tdp" rowspan="2"  width="12%">说明备注</td>
		<td  class="tdp" colspan="5" rowspan="2" align="center"><%=Constant.WAYBILL_PRINT_REMARK%></td>
		<td align="center"  class="tdp" width="12%">收车人及单位</td>
		<td class="tdp" width="12%">&nbsp;</td>
 	</tr>
 	<tr>
		<td align="center"  class="tdp" width="12%">收车日期</td>
		<td class="tdp" width="12%">&nbsp;</td>
 	</tr>
</table>
<br/>
<table width="100%" cellpadding="1" align="center" onmouseover="kpr.style.display='';" >   
	<tr>    
		<td width="100%" height="25" colspan="3">   
		<object classid="CLSID:8856F961-340A-11D0-A96B-00C04FD705A2" height="0" id="wb" name="wb" width="3"></object>
			<div id="kpr" align="center">    
				<input class="ipt" type="button" value="打印" onclick="kpr.style.display='none';javascript:printit();"/>    
				<input class="ipt" type="button" value="打印页面设置" onclick="javascript:printsetup();" />    
				<input class="ipt" type="button" value="打印预览" onclick="kpr.style.display='none';javascript:printpreview();"/>    
			</div>
		</td>
	</tr>     
</table> 
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