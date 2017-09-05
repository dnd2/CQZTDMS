<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@page import="com.infodms.dms.common.Constant"%>
<%@page import="com.infodms.dms.common.Utility"%>
<%@page import="com.infodms.dms.util.CommonUtils"%>
<%@page import="java.util.*"%>
<%@taglib uri="/jstl/cout" prefix="c"%>
<%@taglib uri="/jstl/fmt" prefix="fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %> 
<html>
<%
List<Map<String,Object>> list =(List) request.getAttribute("list");
%>
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
p {
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
<div id="kpr" align="center" class="Noprint">     
<input class="ipt" type=button name= button _print value="打印" onclick ="javascript :printit();">    
<input class="ipt" type=button name=button _setup value="打印页面设置" onclick =" javascript : printsetup();">    
<input class="ipt" type=button name=button_show value="打印预览" onclick="javascript:printpreview();">    
<input class="ipt" type=button name= button _fh value="关闭" onclick =" javascript:window.close();"></div>

<body>
<table width="800px"; align="center"  border="0" class="bigTable">
	<tr align="center">
		<td align="center" height="40" width="100%" style="font-size: 24px; font-weight: bold;"><span class="STYLE1">
			关联件明细
		</span></td>
	</tr>
    <tr>
      <td colspan="6">
      <table class="tab_printsep" align="center" width="800px">
          <tr align="center">
            <td width="5%" align="center">序号</td>
            <td width="20%" align="center">索赔单号</td>
            <td width="20%" align="center">配件代码</td>
            <td width="30%" align="center">配件名称</td>
            <td width="5%" align="center">数量</td>
            <td width="20%" align="center">对应主因件</td>
          </tr>
            <c:set var="pageSize"  value="10000" />
            <c:forEach var="dList" items="${list}" varStatus="status">
              <tr   align="center">
                <td align="center">${status.index+1}</td>
                <td align="center">${dList.CLAIM_NO}</td>
                <td align="center" >${dList.PART_CODE}</td>
                <td align="center">${dList.PART_NAME}</td>
                <td align="center">${dList.SIGN_AMOUNT}</td>
                <td align="center">${dList.MAIN_PART_CODE}</td>
              </tr>
            </c:forEach>
        </table></td>
    </tr>
</table>
<script type="text/javascript">

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
<OBJECT classid="CLSID:8856F961-340A-11D0-A96B-00C04FD705A2" height=0 id=wb name=wb width=3></OBJECT>    
</html>