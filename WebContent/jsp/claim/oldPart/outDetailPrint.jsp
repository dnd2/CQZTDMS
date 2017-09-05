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
<table width="800px"; align="center"  border="0" class="bigTable">
	<tr align="center">
	
		<td align="center" height="80" width="90%" ><span class="STYLE1" style="font-size: 26px;text-decoration:underline ">
			${baseBean.outTittle}
		</span></td>
	</tr>
</table>
<form id="fm" name="fm">
<table width="800px"; align="center"  border="0" class="bigTable">
		<tfoot style="display:table-header-group;">
		<c:if test="${baseBean.printTimes>0}">
		<tr>
					<td width="10%" align="left"  colspan="6"><span style="font-size: 25px">补打</span></td>
		</tr>
		</c:if>
		<tr>
					<td width="10%" align="left" style="font-size: 14px;" colspan="3">NO：${baseBean.outNo}</td>
					<td width="20%" align="right" style="font-size: 14px;" colspan="3">${baseBean.outTime}</td>
			  </tr>
          </table>
          </td>
		 </tr>
  		</tfoot>
 </table>
<table width="800px"; height="300px" align="center" border="0" class="bigTable"> 
 <thead style="display:table-header-group;"> 
 	<tr >
  	  <td height="20" colspan="10" align="center" valign="top">
  	  <table class="tabp">
	    <tr>
			    <td class="tdpsp" align="center" >出门单位</td>
			    <td class="tdpsp" colspan="3" >
			    ${baseBean.outCompany }
			    　</td>
			    <td class="tdpsp" align="center"   >联系电话</td>
			    <td class="tdpsp" colspan="3" >　&nbsp;${baseBean.outCompanyTel}　</td>
	 		 </tr>
	  		<tr >
			    <td class="tdpsp" align="center" >出库单位</td>
			    <td class="tdpsp"colspan="7" >${baseBean.outByName}</td>
	  		</tr>
	  		
    
 		<tr align="center" height="35px">
 				<td class="tdpsp"  align="center">序号</td> 
			    <td class="tdpsp"  align="center" colspan="2">配件名称</td>
			     <td class="tdpsp"  align="center">型号</td> 
			    <td class="tdpsp"  align="center">数量</td>
			    <td class="tdpsp"  align="center" colspan="3">备注</td> 
		</tr>

 <tbody>
	<c:set var="pageSize"  value="10000" />
  <c:forEach var="dList" items="${listBean}" varStatus="status">
	<tr style='${(status.count%pageSize==0) ? "page-break-after:always;":""}'  align="center">
	<td class="tdpsp" align="center" height="25px">${status.index+1}</td>
	    <td class="tdpsp" align="center" height="25px" colspan="2">${dList.partName}</td>
	     <td class="tdpsp" align="center" height="25px">${dList.modelName}</td>
	    <td class="tdpsp" align="center"  height="25px" >${dList.outNum}</td>
	    <td class="tdpsp" align="center" height="25px" colspan="3">${dList.outRemark}</td>
	    
	 </tr>
  </c:forEach>
  </table>
	<tfoot style="display: table-footer-group;">
		<tr>
		 <td height="20" colspan="11"  style="tdpsp:1px solid #000000;"> 
		  <br/>
          </td>
		 </tr>
  </tfoot>
</table>

<table width="800px"; align="center"  border="0" class="bigTable">
		<tfoot style="display:table-header-group;">
		<tr>
				 <tr>
					<td width="10%" align="center" style="font-size: 16px;">出门单位:</td>
					<td width="15%" align="right"  style="font-size: 16px;" colspan="2">&nbsp;</td>
					<td width="20%" align="center" style="font-size: 16px;">经办:</td>
					<td width="15%" align="right"  style="font-size: 16px;" colspan="2">&nbsp;</td>
					<td width="20%" align="center" style="font-size: 16px;">主管:</td>
					<td width="15%" align="right"  style="font-size: 16px;" colspan="2">&nbsp;</td>
			  </tr>
			  </tr>
          </table>
          </td>
		 </tr>
  		</tfoot>
 </table>
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