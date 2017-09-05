<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@page import="com.infodms.dms.common.Constant"%>
<%@page import="com.infodms.dms.common.Utility"%>
<%@page import="com.infodms.dms.util.CommonUtils"%>
<%@page import="java.util.*"%>
<%@taglib uri="/jstl/cout" prefix="c"%>
<%@taglib uri="/jstl/fmt" prefix="fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib uri="/WEB-INF/tld/change.tld" prefix="change"%>
<html>

	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
		<link href="<%=request.getContextPath()%>/style/content.css"
			rel="stylesheet" type="text/css" />
		<script type="text/javascript"
			src="<%=request.getContextPath()%>/js/dict.js"></script>
		<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
		<style>
@media print {
	INPUT {
		display: none
	}
}
</style>
		<style type="text/css">
#myTable {
	border-collapse: collapse;
}

#myTable td {
	border: 1px #000 solid;
}
</style>
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
		<title></title>
	</head>
	<form method="post" name="fm" id="fm">
	<body>
		<div align="center" >
			<table width="900px"  class="tab_edit"  id="tab1"  >
				<tr>
					<td  colspan="4" style="border-right: 0px;" align="right">
						<font size="2">结算单号：${BRO_NO}</font>
					</td>
				</tr>

				<tr>
					<td align="left" colspan="2"  >
						<font size="2">北汽银翔汽车有限公司：</font>
					</td>
					<td align="right" colspan="2" >
						<font size="2"> 一式三联，两联随发票寄出&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</font>
					</td>
				</tr>
				
				<tr>
					<td  colspan="4" style="border-right: 0px;" align="left">
						<font size="2">我司的维修结算费用明细如下：</font>
					</td>
				</tr>
				
				<tr>
				   <td align="center" >
					<font size="2">	材料</font>
					</td>
					<td>
						<font size="2">${BALANCE_PART_AMOUNT }</font>
					</td>
					
					<td  align="center">
					<font size="2">	维修工时费</font>
					</td>
					<td>
						<font size="2">${BALANCE_L_AMOUNT }</font>
					</td>
					<td >
				</tr>
				<tr>
					<td  colspan="4" style="border-right: 0px;" align="center">
						<font size="2">费用合计：${AMOUNT_SUM }</font>
					</td>
				</tr>
				<tr>
				<td colspan="4" style="border-right: 0px;" align="center">
				 <input type="text" name="APPROVAL_REMARKS" id="APPROVAL_REMARKS"  >
				 <input class="normal_btn" type="button" name="button1" id="queryBtn" value="同意" onclick="DealerUSh('${ID }',94761003)"/>
				  <input class="normal_btn" type="button" name="button1" id="queryBtn" value="不同意" onclick="DealerUSh('${ID }',94761001 )"/>
				</td>
				</tr>
			
	   </table>		
</div>
		
<script type="text/javascript">

 function   DealerUSh(id,STATUS)
{
    var APPROVAL_REMARKS = document.getElementById('APPROVAL_REMARKS').value;
    if(STATUS ==94761001 )
    {
       if(APPROVAL_REMARKS.length == 0 )
       {
          MyAlert('请输入审核备注');
          return false;          
       }
    }
    var url  = "<%=request.getContextPath()%>/claim/application/DealerNewKp/DealerUSh.do?id="+id+"&STATUS="+STATUS;
    fm.action = url;
	fm.submit();
    
     
}

var date =document.getElementById('createD').value;
var d = date.substr(0,16);
document.getElementById('createDate').innerHTML=d;
</script>
	</body>
	<script language="javascript">    
  
function printsetup(){    
// 打印页面设置    
wb.execwb(8,1);    
}    
function printpreview(){    
// 打印页面预览      
wb.execwb(7,1);    
}      
function printit()    
{    
if (confirm('确定打印吗？')){    
  
wb.execwb(6,6)    
}    
}    
</script>
</form>
	<OBJECT classid="CLSID:8856F961-340A-11D0-A96B-00C04FD705A2" height=0
		id=wb name=wb width=3></OBJECT>
</html>