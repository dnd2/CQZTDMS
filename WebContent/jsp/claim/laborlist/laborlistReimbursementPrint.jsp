<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="com.infodms.dms.common.Constant"%>
<%@ page import="com.infodms.dms.po.TtLaborListPO"%>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<script type="text/javascript" src="<%=request.getContextPath()%>/js/web/print.js"></script>
<%@page import="java.util.List"%>
<%
	String contextPath = request.getContextPath();
	TtLaborListPO lpo = (TtLaborListPO)request.getAttribute("lpo");
%>
<%@taglib uri="/jstl/cout" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>       
<head>
<style media=print>
.Noprint {
	display: none;
}
 .STYLE1 {
	font-size: 30px;
	font-weight: bold;
	color: #000000;
 }
.STYLE2 {
	font-size: 14px;
	color: #000000;
}
.PageNext {
	page-break-after: always;
}

</style>
<script type="text/javascript">
//去除打印时的页眉和页脚
var HKEY_Root,HKEY_Path,HKEY_Key;    
HKEY_Root="HKEY_CURRENT_USER";    
HKEY_Path="\\Software\\Microsoft\\Internet Explorer\\PageSetup\\";
//设置网页打印的页眉页脚为空    
function PageSetup_Null(){   
   try{    
       var Wsh=new ActiveXObject("WScript.Shell");    
       HKEY_Key="header";    
       Wsh.RegWrite(HKEY_Root+HKEY_Path+HKEY_Key,"");    
       HKEY_Key="footer";    
       Wsh.RegWrite(HKEY_Root+HKEY_Path+HKEY_Key,"");    
   }catch(e){}    
}
function printsetup(){ 
  	// 打印页面设置
 	wb.execwb(8,1); 
} 
function printpreview(){ 
	// 打印页面预览 
	PageSetup_Null();
	wb.execwb(7,1); 
} 
function printit() {
	PageSetup_Null();
	wb.execwb(6,6);
}

function printsetup(){  
	// 打印页面设置    
	wb.execwb(8,1);    
}    
function printpreview(){   
	PageSetup_Null();
	// 打印页面预览      
	wb.execwb(7,1);    
}      
function printit(){
	PageSetup_Null();
	if($('contractNo').value=='空'){
		MyAlert('合同号为空,不能补录附件张数!');
		$('kpr').style.display='';
		return;
	}
	if($('count').value==''){
		MyAlert('请填写附件张数,否则无法打印!');
		$('kpr').style.display='';
		return;
	}else{
		if (confirm('确定打印吗?')){
			makeNomalFormCall("<%=contextPath%>/claim/laborlist/LaborListSummaryReport/changeNum.json?",showAuditValue,'fm','queryBtn');
		} 
	}   
}
function printpreview(){
	if($('contractNo').value=='空'){
		MyAlert('合同号为空,不能补录附件张数!');
		$('kpr').style.display='';
		return;
	}
}
function showAuditValue(json){
	if(json.ok=='ok'){
		wb.execwb(6,6);
	}else{
		MyAlert("打印失败!");
	}
}


</script>

<TITLE>售后服务管理</TITLE>

</HEAD>
<BODY>
<OBJECT id=WebBrowser classid=CLSID:8856F961-340A-11D0-A96B-00C04FD705A2 height=0 width=0 VIEWASTEXT> </OBJECT> 
<form method="post" name ="fm" id="fm">
<table width="756px;"  align="center"  border="0" class="tabptest">
<thead style=""> 
 	<tr style="width:100%">
 		<td width="10%" nowrap="nowrap">联系人:</td>
		<td width="90%" height="10" colspan="10" align="center">
			<table >
				<tr align="center">
					<td align="center" height="30" width="100%" colspan="6">
						<H2>费 用 类 报 销 单(合 同)</H2>
					</td>
				</tr>
			</table>
		</td>
	</tr>
	<tr >
		<td height="10" colspan="11" align="left">
			<table class="tabptest">
				<tr style="width:100%">
					<td width="25%"  align="left" nowrap="nowrap">电话:</td>
					<td align="center"  width="15%"><font size="3">${date }</font></td>
					<td width="20%" align="center" nowrap="nowrap">
					<font size="3" >&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;${regionName }</font></td>
					<td nowrap="nowrap"><font size="3" width="20%" >${dealerCode }</font>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="hidden" name="ID" value="${ID }" /><input type="hidden" name=contractNo value="${contractNo }" /> </td>
					<td nowrap="nowrap"><font size="3" width="20%">附件张数:</font><input type="text" onkeyup="this.value=this.value.replace(/\D/g,'')" onafterpaste="this.value=this.value.replace(/\D/g,'')" id="count" name="count" value=""/>
					</td>
					
				</tr>
			</table>
		</td>
	</tr>
 	<tr>
		<td colspan="11" align="center">
			<table class="tabptest">
				<tr>
					<td class="tdp">付款期限</td>
					<td class="tdpLeft">急(5日内)</td>
					<td class="tdpLeft">缓急(10日内)</td>
					<td class="tdpLeft">正常</td>
					<td align="CENTER"  width="60%" nowrap="nowrap"><font size="2">报销单位：${name }</font></td>
				</tr>
				<tr>
					<td colspan="7" height="6"></td>
				</tr>
			</table>
		</td>
	</tr>
	<tr align="center">
		<td colspan="2" height="20" class="tdpLeft" style="border-top-width: 2px;">合同名称</td>
		<td colspan="2" height="20" class="tdpLeft" style="border-top-width: 2px;">合同号</td>
		<td colspan="2" height="20" class="tdpLeft" style="border-top-width: 2px;" nowrap="nowrap">合同总价(元)</td>
		<td colspan="2" height="20" class="tdp" style="BORDER-RIGHT: medium none" style="border-top-width: 2px;">已付金额(元)</td>
	</tr>
	<tr align="center">
		<td colspan="2" height="20" class="tdpLeft">长安汽车技术服务合同 </td>
		<td colspan="2" height="20" class="tdpLeft">${contractNo } </td>
		<td colspan="2" height="20" class="tdpLeft"></td>
		<td colspan="2" height="20" class="tdp" style="BORDER-RIGHT: medium none"></td>
	</tr>
</thead>
<tbody>
	<tr align="center">
		<td width="10%" rowspan="2" height="50" class="tdpLeft" ><font size="4">本次报销摘要</font></td>
		<td width="25%" class="tdpLeft">三包费</td>
		<td  rowspan="2" width="20%" height="50"  class="tdpLeft"><font size="4">本次报销金额</font></td>
		<td width="10%" class="tdpLeft" style="BORDER-RIGHT: medium none">小写(元)：</td>
		<td  colspan="4" width="60%" class="tdpLeft" style="BORDER-RIGHT: medium none" style="BORDER-RIGHT: medium none"><font size="3">￥${amount }</font></td>
	</tr>
	<tr align="center">
		<td class="tdpLeft"></td>
		<td class="tdpLeft" style="BORDER-RIGHT: medium none"> 大写(元)：</td>
		<td colspan="4" class="tdpLeft" style="BORDER-RIGHT: medium none" ><font size="3">${bigAmount }</font></td>
	</tr>
	<tr align="center">
		<td colspan="3"  height="20" class="tdpLeft" style="border-top-width: 2px;"> 本次报销合同目标内容</td>
		<td colspan="5" class="tdp" style="BORDER-RIGHT: medium none" style="border-top-width: 2px;">完成情况</td>
	</tr>
	<tr align="center">
		<td colspan="3" height="20" align="center" class="tdpLeft" style="border-bottom-width: 4px;">完成每次售后维修工作</td>
		<td colspan="5" class="tdp" style="BORDER-RIGHT: medium none" style="border-bottom-width: 4px;">完成</td>
	</tr>
	<tr>
		<td colspan="6" height="6"></td>
	</tr>
	<tr>
		<td colspan="6" height="6"></td>
	</tr>
	<tr>
		<td colspan="6" height="6"></td>
	</tr>
	<tr style="width:100%">
		<td colspan="3" align="left"  width="50%" height="50"><font size="3">部门(项目组)经办：</font></td>
		<td colspan="3" align="left"  width="50%"><font size="3">令号签署及盖章：</font></td>
	</tr>
	<tr>
		<td colspan="6" height="6"></td>
	</tr>
	<tr>
		<td colspan="6" height="6"></td>
	</tr>
	<tr>
		<td colspan="6" height="6"></td>
	</tr>
	<tr style="width:100%">
		<td colspan="3" align="left"  width="50%" height="50"><font size="3">部门(项目组)复核验收：</font></td>
		<td colspan="3" align="left"  width="50%" height="50"><font size="3">分管副总审批：</font></td>
	</tr>
	<tr>
		<td colspan="6" height="6"></td>
	</tr>
	<tr>
		<td colspan="6" height="6"></td>
	</tr>
	<tr>
		<td colspan="6" height="6"></td>
	</tr>
	<tr style="width:100%">
		<td colspan="3" align="left" height="30" width="50%"><font size="3">单位领导(总监)：</font></td>
		<td colspan="3" align="left" height="30" width="50%"><font size="3">财务副总审批：</font></td>
	</tr>
	<tr>
		<td colspan="6" height="6"></td>
	</tr>
	<tr>
		<td colspan="6" height="6"></td>
	</tr>
	<tr>
		<td colspan="6" height="6"></td>
	</tr>
	<tr style="width:100%">
		<td colspan="3" align="left" height="30" width="50%"><font size="3">财务部门付款审批：</font></td>
		<td colspan="3" align="left" height="30" width="50%"><font size="3">总裁审批：</font></td>
	</tr>
	<tr>
		<td colspan="6" height="6"></td>
	</tr>
	<tr>
		<td colspan="6" height="6"></td>
	</tr>
	<tr>
		<td colspan="6" height="6"></td>
	</tr>

</tbody>

</table>

<b><hr size="4" color="000000" width="830px;"><b/>


<table width="830px;"  align="center"  border="0" class="">
<thead style="width:100%"> 
	<tr>
		<td height="20" class="tdpLeft" style="BORDER-RIGHT: medium none;BORDER-LEFT: medium none;BORDER-TOP: medium none;">收款单位：</td>
		<td class="" ></td>
		<td class="tdpLeft" style="BORDER-RIGHT: medium none;BORDER-LEFT: medium none;BORDER-TOP: medium none;"> 开户行：</td>
		<td class="" ></td>
		<td class="tdpLeft" style="BORDER-RIGHT: medium none;BORDER-LEFT: medium none;BORDER-TOP: medium none;">帐号：</td>
		<td class="" ></td>
	</tr>
</thead>
</table>
</form>
<script type="text/javascript">
</script>
</BODY>
<table class="table_edit">
	<tr>
		<td colspan="4" align="center">
			<OBJECT classid="CLSID:8856F961-340A-11D0-A96B-00C04FD705A2" height=0 id=wb name=wb width=3></OBJECT>    
			<div id="kpr" align="center">    
				<input class="ipt" type=button name= button _print value="打印" onclick ="kpr.style.display='none';javascript :printit();">    
				<input class="ipt" type=button name=button _setup value="打印页面设置" onclick =" javascript : printsetup();">    
				<input class="ipt" type=button name=button_show value="打印预览" onclick="kpr.style.display='none';javascript:printpreview();">    
				<input class="ipt" type=button name= button _fh value="关闭" onclick =" javascript:window.close();"> 
			</div> 
		</td>
	</tr>  
</table>
</html>