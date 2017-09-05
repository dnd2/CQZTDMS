<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="com.infodms.dms.common.Constant"%>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@page import="java.util.List"%>
<%
	String contextPath = request.getContextPath();
%>
<%@taglib uri="/jstl/cout" prefix="c" %>
<head>
<title>质量信息跟踪卡打印</title>
<script>
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
//self.moveTo(0,0)
//self.resizeTo(screen.availwidth,screen.availheight)
</script>
<style>
 @media print
 {   
  .Noprint{DISPLAY:none;}   
  .PageNext{PAGE-BREAK-AFTER:always}   
 } 
 .STYLE1 {
	font-size: 26px;
	font-weight: bold;
	color: #000000;
 }
.STYLE2 {
	font-size: 15px;
	color: #000000;
}
.STYLE3 {font-size: 16px}
</style>
</head>
<body>
<object id=wb 
        height=0 
        width=0     
        classid=CLSID:8856F961-340A-11D0-A96B-00C04FD705A2    
        name=wb>
</object>
<table width=2100 align="center" border="2" cellspacing="0" bordercolor="#000000">
<tbody valign="top">
	<table align="center" width="99%" border="2" cellspacing="0" bordercolor="#000000">
		<tr align="center">
			<td align="center" width="99%" height="50" colspan="6"><span class="STYLE1">长安轿车质量信息处理传递跟踪卡</span></td>
		</tr>
		<tr>
			<td colspan="6" height="6"></td>
		</tr>
		<tr>
			<td width=300><span class="STYLE2">单据编码：</span></td>
			<td width=450><span class="STYLE2">${info.billNo}</span></td>
			<td width=300><span class="STYLE2">信息发出单位编号：</span></td>
			<td width=400><span class="STYLE2">${info.dealerCode}</span></td>
			<td width=300><span class="STYLE2">客户服务部编号：</span></td>
			<td width=350><span class="STYLE2">${info.dealerCode}</span></td>
		</tr>
		<tr>
			<td><span class="STYLE2">投诉类别：</span></td>
			<td>
				<span class="STYLE2">
					<script>
						document.write(getItemValue('${info.complainSort}'));
					</script>
				</span>
			</td>
			<td><span class="STYLE2">信息发出单位：</span></td>
			<td><span class="STYLE2">${info.dealerName}</span></td>
			<td><span class="STYLE2">联系电话：</span></td>
			<td><span class="STYLE2">${info.tel}</span></td>
		</tr>
		<tr>
			<td><span class="STYLE2">信息类别：</span></td>
			<td>
				<span class="STYLE2">
					<script>
						document.write(getItemValue('${info.informationSort}'));
					</script>
				</span>
			</td>
			<td><span class="STYLE2">发出时间：</span></td>
			<td><span class="STYLE2"><fmt:formatDate value='${info.currentDate}' pattern="yyyy-MM-dd"/></span></td>
			<td><span class="STYLE2">车型：</span></td>
			<td><span class="STYLE2">${info.modelCode}</span></td>
		</tr>
		<tr>
			<td><span class="STYLE2">车辆识别号：</span></td>
			<td><span class="STYLE2">${info.vin}</span></td>
			<td><span class="STYLE2">发动机号：</span></td>
			<td><span class="STYLE2">${info.engineNo}</span></td>
			<td><span class="STYLE2">出厂日期：</span></td>
			<td><span class="STYLE2"><fmt:formatDate value='${info.factoryDate}' pattern="yyyy-MM-dd"/></span></td>
		</tr>
		<tr>
			<td><span class="STYLE2">购车日期：</span></td>
			<td><span class="STYLE2"><fmt:formatDate value='${info.purchasedDate}' pattern="yyyy-MM-dd"/></span></td>
			<td><span class="STYLE2">行驶里程：</span></td>
			<td><span class="STYLE2">${info.mileage}</span></td>
			<td><span class="STYLE2">客户姓名：</span></td>
			<td><span class="STYLE2">${info.customName}</span></td>
		</tr>
		<tr>
			<td><span class="STYLE2">联系电话：</span></td>
			<td><span class="STYLE2">${info.phone}</span></td>
			<td><span class="STYLE2">问题部位：</span></td>
			<td>
				<span class="STYLE2">
					<script>
						document.write(getItemValue('${info.questionPart}'));
					</script>
				</span>
			</td>
			<td><span class="STYLE2">故障性质：</span></td>
			<td>
				<span class="STYLE2">
					<script>
						document.write(getItemValue('${info.faultNatrue}'));
					</script>
				</span>
			</td>
		</tr>
		<tr>
			<td><span class="STYLE2">变速器类别：</span></td>
			<td>
				<span class="STYLE2">
					<script>
						document.write(getItemValue('${info.gearboxNatrue}'));
					</script>
				</span>
			</td>
			<td><span class="STYLE2">车辆用途：</span></td>
			<td><span class="STYLE2">${info.vhclUse}</span></td>
			<td><span class="STYLE2">故障发生时的车速情况：</span></td>
			<td><span class="STYLE2">${info.speedCase}</span></td>
		</tr>
		<tr>
			<td><span class="STYLE2">故障发生时的路面情况：</span></td>
			<td><span class="STYLE2">${info.roadCase}</span></td>
			<td><span class="STYLE2">故障发生时的装载情况：</span></td>
			<td><span class="STYLE2">${info.loadingCase}</span></td>
			<td><span class="STYLE2">故障发生时的车辆改装情况：</span></td>
			<td><span class="STYLE2">${info.adaptCase}</span></td>
		</tr>
		<tr>
			<td><span class="STYLE2">故障零部件生产厂家：</span></td>
			<td><span class="STYLE2">${info.produceFactroy}</span></td>
			<td><span class="STYLE2">完整发动机号：</span></td>
			<td><span class="STYLE2">${info.completeEngineno}</span></td>
			<td><span class="STYLE2"></span></td>
			<td><span class="STYLE2"></span></td>
		</tr>
		<tr>
			<td><span class="STYLE2">故障原因及处理意见：</span></td>
			<td colspan="5"><span class="STYLE2">${info.faultOpinion}</span></td>
		</tr>
		<tr>
			<td><span class="STYLE2">配套件损坏情况(件名及供应商等):</span></td>
			<td colspan="5"><span class="STYLE2">${info.damageCase}</span></td>
		</tr>
		<tr>
			<td><span class="STYLE2">损失估计(元):</span></td>
			<td colspan="5"><span class="STYLE2">${info.damagePrice}</span></td>
		</tr>
		<tr>
			<td><span class="STYLE2">技术服务处处理意见及处理人，处理时间：</span></td>
			<td colspan="5"><span class="STYLE2">${info.processOpinion}&nbsp;&nbsp;${info.processPerson}&nbsp;&nbsp;<fmt:formatDate value='${info.processDate}' pattern="yyyy-mm-dd"/></span></td>
		</tr>
		<tr>
			<td colspan="6" height="8"></td>
		</tr>
		<tr>
			<td><span class="STYLE2">结果：</span></td>
			<td colspan="5">${info.result}</td>
		</tr>
		<tr>
			<td><span class="STYLE2">登记人：</span></td>
			<td><span class="STYLE2">${info.booker}</span></td>
			<td><span class="STYLE2">回访单位：</span></td>
			<td><span class="STYLE2">${info.backUnit}</span></td>
			<td><span class="STYLE2">回访日期：</span></td>
			<td><span class="STYLE2"><fmt:formatDate value='${info.backDate}' pattern="yyyy-MM-dd"/></span></td>
		</tr>
		<tr>
			<td colspan="2"><span class="STYLE2">质量信息回复及改进情况：</span></td>
			<td colspan="4"><span class="STYLE2">(以下由长安公司内部职能部门填写)</span></td>
		</tr>
		<tr>
			<td><span class="STYLE2">围绕原因分析所开展的工作：</span></td>
			<td colspan="5"><span class="STYLE2">${info.caseWork}</span></td>
		</tr>
		<tr>
			<td><span class="STYLE2">问题原因：</span></td>
			<td colspan="2"><span class="STYLE2">${info.questionCase}</span></td>
			<td><span class="STYLE2">结论：</span></td>
			<td colspan="2"><span class="STYLE2">${info.conntion}</span></td>
		</tr>
		<tr>
			<td><span class="STYLE2">责任单位：</span></td>
			<td colspan="2"><span class="STYLE2">${info.dutyUnit}</span></td>
			<td><span class="STYLE2">临时措施：</span></td>
			<td colspan="2"><span class="STYLE2">${info.tempStep}</span></td>
		</tr>
		<tr>
			<td><span class="STYLE2">永久措施：</span></td>
			<td colspan="2"><span class="STYLE2">${info.forveStep}</span></td>
			<td><span class="STYLE2">建议：</span></td>
			<td colspan="2"><span class="STYLE2">${info.suggest}</span></td>
		</tr>
		<tr>
			<td><span class="STYLE2">改进情况客户服务部意见：</span></td>
			<td colspan="5"><span class="STYLE2">${info.serviceOpinion}</span></td>
		</tr>
		<tr>
			<td colspan="6" height="6"></td>
		</tr>
	</table>
  
	<table align="center" width="99%" class="Noprint">
		<tr>
			<td align="center" width="99%" height="30">
				<input type="button" name="prtBut" value="打印" onclick="printit();" />
				<input type="button" name="prtSetBut" value="打印设置" onclick="printsetup();" />
				<input type="button" name="prtViwBut" value="打印预览" onclick="printpreview();" />
			</td>
		</tr>
  	</table>
</tbody>
</table>
</body>
</html>
















