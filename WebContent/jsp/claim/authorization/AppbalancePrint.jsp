<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@page import="com.infodms.dms.common.Constant"%>
<%@page import="com.infodms.dms.common.Utility"%>
<%@page import="com.infodms.dms.util.CommonUtils"%>
<%@page import="java.util.*"%>
<%@taglib uri="/jstl/cout" prefix="c"%>
<%@taglib uri="/jstl/fmt" prefix="fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %> 
<%@ taglib uri="/WEB-INF/tld/change.tld" prefix="change"%>
<html>
<%
Map<String, Object> list =(Map<String, Object>) request.getAttribute("list");
%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
<link href="<%=request.getContextPath()%>/style/content.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="<%=request.getContextPath()%>/js/dict.js"></script>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<style>
@media print{
INPUT {display:none}
}
</style>
<style type="text/css">
#myTable {
   
    border-collapse:collapse;
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
<title>旧件清单打印</title>
</head>
<div id="kpr" align="center" class="Noprint">     
<input class="ipt" type=button name= button _print value="打印" onclick ="javascript :printit();">    
<input class="ipt" type=button name=button _setup value="打印页面设置" onclick =" javascript : printsetup();">    
<input class="ipt" type=button name=button_show value="打印预览" onclick="javascript:printpreview();">    
<input class="ipt" type=button name= button _fh value="关闭" onclick =" javascript:window.close();"></div>

<body>
  <p>
<div>
<table width="800px"; align="center"  border="0" class="bigTable">
	<tr align="center">
		<td align="center" height="40" width="100%" style="font-size: 24px; font-weight: bold;"><span class="STYLE1">
			北汽幻速索赔${name}清单
		</span></td>
	</tr>
    <tr>
      <td colspan="8">
      <table class="tab_printsep" id="myTable" align="center" width="800px">
          <tr align="center">
            <td width="5%" align="center">序号</td>
            <td width="15%" align="center">索赔单号</td>
            <td width="15%" align="center">索赔类型</td>
            <td width="15%" align="center">总费用</td>
            <td width="15%" align="center">材料费</td>
            <td width="15%" align="center">工时费</td>
            <td width="15%" align="center">保养费</td> 
            <td width="15%" align="center"nowrap="true">辅料费</td>
           <td width="15%" align="center" nowrap="true">补偿费</td>
            <td width="15%" align="center" nowrap="true">活动或外派费</td>
         </tr>
            <% int i = 1;  %>
            <c:set var="pageSize"  value="10000" />
            <c:forEach var="AppList" items="${AppList}" varStatus="status">
              <tr   align="center">
                <td align="center"><%= i %></td>
                 <td align="center" nowrap="true">${AppList.CLAIM_NO}</td>
                <td align="center">
                	<change:change type="1066" val="${AppList.CLAIM_TYPE}"/>
               </td>
                <td align="center">${AppList.BALANCE_AMOUNT}</td>
                <td align="center">${AppList.BALANCE_PART_AMOUNT}</td>
                <td align="center" >${AppList.BALANCE_LABOUR_AMOUNT}</td>
                <td align="center" >${AppList.FREE_M_PRICE}</td>
               <td align="center" >${AppList.ACCESSORIES_PRICE}</td>
                <td align="center" >
                <c:if test="${AppList.WINTER_MONEY==0}">
                  ${AppList.COMPENSATION_MONEY}
                </c:if> 
                <c:if test="${AppList.WINTER_MONEY!=0}">
                  ${AppList.WINTER_MONEY}
                </c:if>
                </td>
                <td align="center" >${AppList.BALANCE_NETITEM_AMOUNT}</td>
               <% i++; %>
                </td>
              </tr>
            </c:forEach>
            
            <c:forEach var="FList" items="${FList}" varStatus="status">
              <tr   align="center">
                <td align="center"><%= i %></td>
                 <td align="center" nowrap="true">${FList.FEE_NO}</td>
                <td align="center">
                	特殊费用
               </td>
                <td align="center">${FList.DECLARE_SUM}</td>
                <td align="center">${FList.DECLARE_SUM}</td>
                <td align="center" >0</td>
                <td align="center" >0</td>
               <td align="center" >0</td>
                <td align="center" >0</td>
                <td align="center" >0</td>
               <% i++; %>
                </td>
              </tr>
            </c:forEach>
            <c:forEach var="FList" items="${FList1}" varStatus="status">
              <tr   align="center">
                <td align="center"><%= i %></td>
                 <td align="center" nowrap="true">${FList.CLAIM_NO}</td>
                <td align="center">
                	二次入库
               </td>
                <td align="center">${FList.AMOUNT}</td>
                <td align="center">${FList.AMOUNT}</td>
                <td align="center" >0</td>
                <td align="center" >0</td>
               <td align="center" >0</td>
                <td align="center" >0</td>
                <td align="center" >0</td>
               <% i++; %>
              </tr>
            </c:forEach>
            <c:forEach items="${CounterList}" var="Counter"  varStatus="status">
              <tr align="center">
               <td align="center"><%= i %></td>
               <td align="center">${Counter.CLAIM_NO }</td>
               <td align="center">反索赔</td>
               <td align="center">${Counter.BALANCE_AMOUNT}</td>
                <td align="center">${Counter.BALANCE_PART_AMOUNT}</td>
                <td align="center" >${Counter.BALANCE_LABOUR_AMOUNT}</td>
                <td align="center" >${Counter.FREE_M_PRICE}</td>
               <td align="center" >${Counter.ACCESSORIES_PRICE}</td>
                <td align="center" >${Counter.COMPENSATION_MONEY}</td>
                <td align="center" >${Counter.BALANCE_NETITEM_AMOUNT}</td>
                <% i++; %>
              </tr>
            </c:forEach>
	        <tr align="center">
	    	<td width="35%" align="center" colspan="3" nowrap="true">合计</td>
	    	<td width="15%" align="center">${AppListTemp.BALANCE_AMOUNT_COUNT+mapF.DECLARE_SUM+mapF1.DECLARE_SUM+Countermap.BALANCE_AMOUNT}</td>
	        <td width="15%" align="center">${AppListTemp.BALANCE_PART_AMOUNT_COUNT+mapF.DECLARE_SUM+Countermap.BALANCE_PART_AMOUNT}</td>
	        <td width="15%" align="center">${AppListTemp.BALANCE_LABOUR_AMOUNT_COUNT+Countermap.BALANCE_LABOUR_AMOUNT}</td>
 	        <td width="15%" align="center">${AppListTemp.FREE_M_PRICE_COUNT}</td>
 	        <td width="15%" align="center">${AppListTemp.ACCESSORIES_PRICE_COUNT+Countermap.ACCESSORIES_PRICE}</td>
	        <td width="15%" align="center">
	          <c:if test="${AppListTemp.WINTER_MONEY_COUNT==0}">
	                ${AppListTemp.COMPENSATION_MONEY_COUNT+Countermap.COMPENSATION_MONEY}
	          </c:if>
	          <c:if test="${AppListTemp.WINTER_MONEY_COUNT!=0}">
	                ${AppListTemp.WINTER_MONEY_COUNT}
	          </c:if>
	        </td> 
	        <td width="15%" align="center">${AppListTemp.BALANCE_NETITEM_AMOUNT_COUNT+Countermap.BALANCE_NETITEM_AMOUNT}</td>
        </table></td>
    </tr>
        </table>
        </td>
    </tr>
</table>
</div>
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