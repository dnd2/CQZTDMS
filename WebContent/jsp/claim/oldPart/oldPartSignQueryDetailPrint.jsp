<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
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
<table width="750x"; align="center"  border="0" class="bigTable">
	<tr align="center">
		<td align="center" height="80" width="100%" ><span class="STYLE1" style="font-size: 40px">
			昌河汽车旧件回运单明细
		</span></td>
	</tr>
</table>
<form id="fm" name="fm"><input type="hidden" id="planId"/>
<table width="750x"; align="center"  border="0" class="bigTable"> 
 <thead style="display:table-header-group;"> 
 	<tr >
  	  <td height="20" colspan="10" align="center">
  	  
  	  <table class="tabp">
	        <tr>
	        
			    <td class="tdp">回运清单号：</td>
			    <td class="tdp" colspan="3">${list.RETURN_NO}</td>
			    <td class="tdp">制单人姓名：</td>
			    <td class="tdp" colspan="2">${list.DEALER_NAME}　</td>
			    <td class="tdp">制单日期：</td>
			    <td colspan="2" class="tdpright">
			    	<fmt:formatDate value="${list.CREATE_DATE}" pattern="yyyy-MM-dd"/>
			    </td>
	  		</tr>
	  		<tr >
			    <td class="tdp">生产厂家：</td>
			    <td class="tdp" colspan="3">
			    	${list.AREA_NAME }
			    　</td>
			    <td class="tdp">索赔单数量：</td>
			    <td class="tdp" colspan="2">${list.WR_AMOUNT}　</td>
			    <td class="tdp">配件数量：</td>
			    <td class="tdpright" colspan="2">${list.PART_AMOUNT}　</td>
	 		 </tr>
	  		<tr >
			    <td class="tdp"   nowrap="nowrap">货运方式：</td>
			    <td class="tdp" colspan="3">
					<script type="text/javascript">
	              		document.write(getItemValue('${list.TRANSPORT_TYPE }'))
	       			</script>
			    </td>
			    <td class="tdp" >货运单号：</td>
			    <td class="tdp"  colspan="2">${list.TRAN_NO}　</td>
			    <td class="tdp" >装箱总数：</td>
			    <td class="tdpright"  colspan="2">${list.PARKAGE_AMOUNT}　</td>
	  		</tr>
	  		<tr >
			    <td class="tdp">制单单位：</td>
			    <td class="tdpright"colspan="9">${list.DEALER_NAME}　</td>
	  		</tr>
	  		<tr >
	  		<td class="tdp">三包员电话：</td>
			    <td class="tdpright" colspan="9" nowrap="nowrap">${list.TEL}</td>
	  		</tr>
	  		<tr >
	  		<td class="tdp">旧件起止日期：</td>
			    <td class="tdpright" colspan="9" nowrap="nowrap">
			    <c:forEach var="list1" items="${list1}">
			    	${list1.WR_START_DATE}　
			    </c:forEach>
			    </td>
	  		</tr>
	  		<tr >
	  		<td class="tdp">服务商代码：</td>
			    <td class="tdpright" colspan="9" nowrap="nowrap">${list.DEALER_CODE}</td>
	  		</tr>
	  		<tr height="25">
	   			 <td class="tdp"   rowspan="2" height="39">备注：</td>
	   			 <td class="tdpright"  colspan="9" rowspan="2">
	   			 	<textarea rows="3" cols="89">${list.REMARK}</textarea>
	   			 </td>
	 		 </tr>
      </table><br>
   		</td></tr>
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