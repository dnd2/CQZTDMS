<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib uri="/jstl/cout" prefix="c"%>
<%@taglib uri="/jstl/fmt" prefix="fmt"%>
<%@page import="java.util.*"%>
<% 
	List<Map<String,Object>> list  = (List)request.getAttribute("listBean");
%>
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
<body >
<c:choose>
	<c:when test="${!empty listBean}">
<form id="fm" name="fm">
<table width="900px;" align="center"  border="0" class="bigTable"> 
 <thead style="display:table-header-group;"> 
 <tr >
 	<br/>
 	<br/>
 	<br/>
 	<td align="center" height="90" colspan="11" width="100%" style="font-size: 30px; font-weight: bold;">
 		<span class="STYLE1">索赔件退赔单</span>
	</td>
     </tr>
	<tr>
		<td>
     		<img alt="北汽银翔" width="80px;"  height="40px;" src="<%=request.getContextPath()%>/img/bqyx.png">
     	<td>
	</tr>
		
     <tr >
       <td  colspan="11"  style="font-weight: bold; text-align: right;" >
			退赔单号：<%=list.get(0).get("RANGE_NO") %><span id="showRmark" style="color: red;display: none;font-weight: bold;" >【补单】</span>
		</td>
     </tr>
     <tr>
       <td align="center" class="tdp" colspan="2">供应商名称</td>
       <td align="center" class="tdp" colspan="4"><%=list.get(0).get("SUPPLY_NAME") %></td>
       <td align="center" class="tdp" colspan="2">供应商代码</td>
       <td align="center" class="tdp" colspan="3"><%=list.get(0).get("SUPPLY_CODE") %></td>
     </tr>
        <tr >
            <td align="center" width="5%" class="tdp">序号</td>
            <td align="center" width="17%" class="tdp">零部件代码</td>
            <td align="center" width="18%" class="tdp">零部件名称</td>
            <td align="center" width="5%" class="tdp">单位</td>
            <td align="center" width="5%" class="tdp">数量</td>
            <td align="center" width="10%" class="tdp">工时金额</td>
            <td align="center" width="10%" class="tdp">关联损失</td>
            <td align="center" width="10%" class="tdp">外出费用</td>
            <td align="center" width="10%" class="tdp">材料合计</td>
            <td align="center" width="10%" class="tdp">小计</td>
            <td align="center" nowrap="true" class="tdp">备注</td>
       </tr>
        <c:set var="pageSize"  value="10000" />
        <c:set var="num1"  value="0" />
        <c:set var="labour"  value="0" />
        <c:set var="related"  value="0" />
        <c:set var="out"  value="0" />
         <c:set var="part"  value="0" />
        <c:set var="small"  value="0" />
     <c:forEach var="dList" items="${listBean}" varStatus="status">
	<tr align="center">
	  	<td class="tdp" align="center" >${status.index+1}</td> 
	  	
	    <td class="tdp" align="center"  >${dList.PART_CODE} </td>
	    
	     <td class="tdp" align="center"  >${dList.PART_NAME}</td>
	     
	  	<td class="tdp" align="center"  >  ${dList.PART_UNIT} </td>
	  	 
	    <td class="tdp" align="center" >${dList.PART_QUANTITY}</td>
	    	 
	    <td class="tdp" align="center">${dList.LABOUR_AMOUNT} </td>
	     
	    <td class="tdp" align="center" >${dList.RELATED_LOSSES} </td>
	     
	    <td class="tdp" align="center" >${dList.OUT_AMOUNT}</td>
	    <td class="tdp" align="center" > ${dList.PART_AMOUNT}</td>
	    <td class="tdp" align="center" >${dList.SMALL_AMOUNT} </td>
	    <td align="center"  class="tdp"  nowrap="true" >${dList.REMARK}</td>
	    
	    <c:set var="num1"  value="${dList.PART_QUANTITY+num1}" />
        <c:set var="labour"  value="${dList.LABOUR_AMOUNT+labour}" />
        <c:set var="related"  value="${dList.RELATED_LOSSES+related}" />
        <c:set var="out"  value="${dList.OUT_AMOUNT+out}" />
         <c:set var="part"  value="${dList.PART_AMOUNT+part}" />
        <c:set var="small"  value="${dList.SMALL_AMOUNT+small}" />
	 </tr>
  </c:forEach>
   <tr >
       		<td align="center" class="tdp" colspan="2">合计</td>
      	 	<td align="center" class="tdp" colspan="2"></td>
            <td align="center" id="num1" class="tdp">${num1 }</td>
            <td align="center" id="labour" class="tdp">${labour }</td>
            <td align="center" id="related" class="tdp">${related }</td>
            <td align="center" id="out" class="tdp">${out }</td>
            <td align="center" id="part" class="tdp">${part }</td>
            <td align="center" id="small" class="tdp">${small }</td>
            <td align="center"  class="tdp"></td>
     </tr>
      <tr >
       		<td align="center" class="tdp" colspan="2">备注</td>
      	 	<td align="left" class="tdp" colspan="9">
      	 	<%-- <% List<Map<String,Object>> listRemark  = (List)request.getAttribute("listRemark");
      	 	if(listRemark!=null && listRemark.size()>0){
      	 		for(int i=0;i<list.size();i++){
      	 		if("null".equalsIgnoreCase(String.valueOf(listRemark.get(0).get("REMARK"+i+"")))||"".equalsIgnoreCase(String.valueOf(listRemark.get(0).get("REMARK"+i+"")))||String.valueOf(listRemark.get(0).get("REMARK"+i+""))==null){ %>
      	 			&nbsp;
      	 			<%} else{%>
      	 		<%=listRemark.get(0).get("REMARK"+i+"") %>
      	 			<br/>
      	 	<%	}}
      	 	}
      	 	%> --%>
      	 	<c:forEach items="${listOhers }" var="t">
      	 		次因件代码:<span>${t.PART_CODE}</span>
      	 		次因件名称:<span>${t.PART_NAME}</span>
      	 		次因件数量:<span>${t.PART_NUM}</span>
      	 		<br/>
      	 	</c:forEach>
      	 	</td>
     </tr>
      <tr>
       		<td align="left" style="height: 40px;" class="tdp" colspan="2">制单人：</td>
            <td align="left" style="height: 40px;" class="tdp"colspan="2">库管员：</td>
            <td align="left" style="height: 40px;" class="tdp"colspan="2">供应商：</td>
            <td align="left" style="height: 40px;" colspan="2" class="tdp">售后服务部：</td>
            <td align="left" style="height: 40px;" colspan="3" class="tdp">财务部：</td>
     </tr>
      <tr >
       		<td align="right" class="tdp" colspan="2">年&nbsp;&nbsp;&nbsp;&nbsp;月&nbsp;&nbsp;&nbsp;&nbsp;日</td>
            <td align="right"  class="tdp"colspan="2">年&nbsp;&nbsp;&nbsp;&nbsp;月&nbsp;&nbsp;&nbsp;&nbsp;日</td>
            <td align="right"  class="tdp"colspan="2">年&nbsp;&nbsp;&nbsp;&nbsp;月&nbsp;&nbsp;&nbsp;&nbsp;日</td>
            <td align="right"  colspan="2" class="tdp">年&nbsp;&nbsp;&nbsp;&nbsp;月&nbsp;&nbsp;&nbsp;&nbsp;日</td>
            <td align="right"  colspan="3" class="tdp">年&nbsp;&nbsp;&nbsp;&nbsp;月&nbsp;&nbsp;&nbsp;&nbsp;日</td>
     </tr>
  <tr style="width:100%; text-align: left;font-size: 14px;">
    <td colspan="10" align="left">
	备注：1、此单为供应商在北汽银翔退取索赔件的现场确认凭证及扣款依据。<br>
      &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;2、此单供应商签字确认后，此批索赔件所有权为供应商所有，北汽幻速不再负责保管工作。<br>
      &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;3、此单一式五份，白联财务留存，红、蓝联售后服务部留存，绿联供应商带走，黄联用为开出门条凭证。<br></td>
</tr>
</table>
<br/>

</form>
</c:when>
<c:otherwise>
	<div align="center">
		<font color="red" size="10">没有退配单信息</font>
	</div>
</c:otherwise>
</c:choose>
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
function showRamrk(){
	var showRmark=document.getElementById("showRmark");
	var buttonshow=document.getElementById("buttonshow");
	if(showRmark.style.display=="none"){
		showRmark.style.display="";
		buttonshow.value="补单备注(开)";
	}else{
		showRmark.style.display="none";
		buttonshow.value="补单备注(关)";
	}
}
</script>    
<OBJECT classid="CLSID:8856F961-340A-11D0-A96B-00C04FD705A2" height=0 id=wb name=wb width=3></OBJECT>    
<div id="kpr" align="center">    
<input class="ipt" type=button name= button _print value="打印" onclick ="kpr.style.display='none';javascript :printit();">    
<input class="ipt" type=button name=button _setup value="打印页面设置" onclick =" javascript : printsetup();">    
<input class="ipt" type=button name=button_show value="打印预览" onclick="kpr.style.display='none';javascript:printpreview();">    
<input class="ipt" id="buttonshow" type=button name=button_show value="补单备注" onclick="showRamrk();">    
<input class="ipt" type=button name= button _fh value="关闭" onclick =" javascript:window.close();"> </td>    
</tr>   
</table>  
</html>