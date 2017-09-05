<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@page import="com.infodms.dms.common.Constant"%>
<%@taglib uri="/jstl/cout" prefix="c"%>
<%@taglib uri="/jstl/fmt" prefix="fmt"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
<link href="<%=request.getContextPath()%>/style/content.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="<%=request.getContextPath()%>/js/dict.js"></script>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />

<style media=print>
.Noprint {
	display: none;
}
 .STYLE1 {
	font-size: 24px;
	font-weight: bold;
	color: #000000;
 }
.STYLE2 {
	font-size: 14px;
	color: #000000;
}
.p {
	page-break-after: always;
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
<title>旧件清单打印</title>
</head>
<body>
<form id="fm" name="fm"><input type="hidden" id="planId"/>
<table width="750x"; align="center"  border="0" class="bigTable"> 
 <thead style="display:table-header-group;"> 
<table width="750x"; align="center"  border="0" class="bigTable">
	<tr align="center">
		<td align="center" height="80" width="100%" ><span class="STYLE1">
			北汽旧件回运单明细
		</span></td>
	</tr>
 	<tr >
  	  <td height="20" colspan="10" align="center">
  	  
  	  <table class="tabp">
   	<table class="tabp" align="center">	
 		<tr align="center">
			    <td class="tdp" width="30" align="center">行号</td>
			    <td class="tdp" width="50" align="center">配件代码</td>
			    <td class="tdp" width="80" align="center">配件名称</td>
			    <td class="tdp" width="50" align="center">索赔单号</td> 
			    <td class="tdp" width="30" align="center">旧件数量</td>
			    <td class="tdp" width="30" align="center">回运数量</td>
			    <td class="tdp" width="150" align="center">供应商名称</td>
			     <td class="tdp" width="80" align="center">条码</td>
			    <td class="tdp" width="30" align="center">装箱单号</td>
		</tr>
 	<tbody>
	<c:set var="pageSize"  value="10000" />
  <c:forEach var="dList" items="${detailList}" varStatus="status">
	<tr style='${(status.count%pageSize==0) ? "page-break-after:always;":""}'  align="center">
	    <td class="tdp" align="center">${status.index+1}</td>
	    <td class="tdp" align="center" >${dList.PART_CODE}</td>
	    <td class="tdp" align="center">
	    	<script>
	    		var str='${dList.PART_NAME}';
	    		var s = '' ;
	    		for(var i = 0 ;i<str.length;i++){
					s+=str.substring(i,i+1);
					if((i+1)%12==0){
						s+='<br/>';
					}
	    		}
	    		document.write(s);
	    	</script>	
	    </td>
	    <td class="tdp" align="center">${dList.CLAIM_NO}</td> 
	    <td class="tdp" align="center">${dList.N_RETURN_AMOUNT}</td>
	    <td class="tdp" align="center">${dList.RETURN_AMOUNT}</td>
	    <!--<td class="tdp">${dList.CLAIM_DATE}</td> 维修时间 -->
	    <td class="tdp" align="center">${dList.PRODUCER_NAME}</td>
	     <td class="tdp" align="center">${dList.BARCODE_NO}</td>
	    <td class="tdp" align="center">${dList.BOX_NO}</td>
	 </tr>
  </c:forEach>
  </table>
	<tfoot style="display: table-footer-group;">
		<tr>
		 <td height="20" colspan="11"  style="tdp:1px solid #000000;"> 
		  <br/>
          </td>
		 </tr>
  </tfoot>
</table>
<br/>

<br/>

</form>
<script type="text/javascript">

var date =document.getElementById('createD').value;
var d = date.substr(0,10);
document.getElementById('createDate').innerHTML=d;
</script>
</body>
<table width="100%" cellpadding="1" align="center" onmouseover="kpr.style.display='';" >   
<tr>    
<td width="100%" height="25" colspan="3"><script language="javascript">    
  
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
<OBJECT classid="CLSID:8856F961-340A-11D0-A96B-00C04FD705A2" height=0 id=wb name=wb width=3></OBJECT>    
<div id="kpr" align="center">    
<input class="ipt" type=button name= button _print value="打印" onclick ="kpr.style.display='none';javascript :printit();">    
<input class="ipt" type=button name=button _setup value="打印页面设置" onclick =" javascript : printsetup();">    
<input class="ipt" type=button name=button_show value="打印预览" onclick="kpr.style.display='none';javascript:printpreview();">    
<input class="ipt" type=button name= button _fh value="关闭" onclick =" javascript:window.close();"> </td>    
</tr>   
</table>  
</html>