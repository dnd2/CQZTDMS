<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c" %>
<%@ taglib prefix='fmt' uri="http://java.sun.com/jsp/jstl/fmt" %>
<%
    String contextPath = request.getContextPath();
%>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp"/>
<head>
    <title></title>
    <style media=print>
        .Noprint{display:none;}

        .PageNext{page-break-after: always;}
    </style>
    <style type="text/css">
        td{font-size: 14px}
        div{margin-right: 0.2px;margin-left: 0.2px;margin-top: 0.2px;margin-bottom: 0.2px}
    </style>
</head>
<script language="javascript">
    //获取选择框的值
    function getCode(value) {
        var str = getItemValue(value);

        document.write(str);
    }
    var idx = 0;
    function getIndex(){
        idx+=1
        document.write(idx);
    }

    function printChkOrder() {

        //点击打印按钮之后就要更新打印时间
      //  var url = '<%=contextPath%>/parts/purchaseOrderManager/PurchaseOrderChkManager/updatePrintDate.json';
      //  sendAjax(url, handleControl, 'fm');
        handleControl();
    }

    function handleControl() {
    	printWithAlert();
    }
    function doSupp() {
        parentContainer.__extQuery__(1);
    }
    function printWithAlert() {         
   	 document.all.WebBrowser.ExecWB(6,1);      
   	 }       
   	 function printWithoutAlert() {        
   	   document.all.WebBrowser.ExecWB(6,6);       
   	 }     
   	 function printSetup() {         
   	 document.all.WebBrowser.ExecWB(8,1);       
   	 }      
   	 function printPrieview() {         
   	 document.all.WebBrowser.ExecWB(7,1);       
   	 }       
   	function printImmediately() {         
   	document.all.WebBrowser.ExecWB(6,6);        
   	 window.close();       
   	 }
   	 var HKEY_Root,HKEY_Path,HKEY_Key; 
   	HKEY_Root="HKEY_CURRENT_USER"; 
   	HKEY_Path="\\Software\\Microsoft\\Internet Explorer\\PageSetup\\"; 
   	//设置网页打印的页眉页脚为空 
   	function PageSetup_Null() 
   	{ 
   	try 
   	{ 
   	var Wsh=new ActiveXObject("WScript.Shell"); 
   	HKEY_Key="header"; 
   	Wsh.RegWrite(HKEY_Root+HKEY_Path+HKEY_Key,""); 
   	HKEY_Key="footer"; 
   	Wsh.RegWrite(HKEY_Root+HKEY_Path+HKEY_Key,""); 
   	} 
   	catch(e) 
   	{} 
   	} 
   	//设置网页打印的页眉页脚为默认值 
   	function PageSetup_Default() 
   	{ 
   	try 
   	{ 
   	var Wsh=new ActiveXObject("WScript.Shell"); 
   	HKEY_Key="footer"; 
   	Wsh.RegWrite(HKEY_Root+HKEY_Path+HKEY_Key,"&b&p/&P"); 
   	//HKEY_Key="footer"; 
   	//Wsh.RegWrite(HKEY_Root+HKEY_Path+HKEY_Key,"&u&b&d"); 
   	} 
   	catch(e) 
   	{} 
   	} 
   	PageSetup_Default();
</script>
<OBJECT  id=WebBrowser  classid=CLSID:8856F961-340A-11D0-A96B-00C04FD705A2 style="display:none">
</OBJECT>
<body onload="">
<form name="fm" id="fm" method="post" enctype="multipart/form-data">
    <c:forEach items="${allList}" var="subList" >
    <div name=thisblock id=thisblock style="width:21.5cm; height: 10.5cm;" class="PageNext">
        <TABLE border=0 border=0 bordercolor=black cellpadding=0 cellspacing=0 width="95%">
            <br><center><font size="+1"><b>
                领&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;件&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;单
            </b></font></center>
        </TABLE>
        <table border=0  cellpadding=0  cellspacing=0 width="100%" >
            <tr style="height: 22; line-height: 22px;">
                <td width="30%" nowrap >
                    日期：&nbsp;${mainMap.CREATE_DATE}
                </td>
                <td width="30%" nowrap>
                    采购订单号：&nbsp;${mainMap.PUR_ORDER_CODE}
                </td>
                <td width="30%" nowrap>
                    领件单号：&nbsp;${mainMap.ORDER_CODE}
                </td>
            </tr>
        </table>
        <div  style="height:250px; width: 99%;" >
        <TABLE id="file" border=1 bordercolor=black cellpadding=0   cellspacing=0 width="100%" style="margin-top: 5px;" >
            <tr style="height: 22; line-height: 22px;">
                <td width=12% style="text-align: center" colspan=1>配件编码</td>
                <td width=22% style="text-align: center" colspan=1>配件名称</td>
                <td width=15% style="text-align: center" colspan=1>配件件号</td>
                <td width=5% style="text-align: center" colspan=1>单位</td>
                <td width=5% style="text-align: center" colspan=1>数量</td>
                <td width=8% style="text-align: center" colspan=1>实领数量</td>
                <td width=10% style="text-align: center" colspan=1>领用日期</td>
                <td width=8% style="text-align: center" colspan=1>室</td>
                <td width=10% style="text-align: center" colspan=1>保管员</td>
                <td width=5% style="text-align: center" colspan=1>备注</td>
            </tr>
            <c:forEach items="${subList}" var="data" varStatus="curSta">
            <tr style="height: 22; line-height: 22px;">
                    <td style="text-align: center">
                            ${data.PART_OLDCODE}
                    </td>
                    <td style="text-align: center" >
                    	<div style="width:190px; overflow: hidden; white-space: nowrap; text-overflow:ellipsis;">
                            ${data.PART_CNAME}
                        </div>
                    </td>
                    <td style="text-align: center" >
                    	<div style="width:130px; overflow: hidden; white-space: nowrap; text-overflow:ellipsis;">
                            ${data.PART_CODE}
                        </div>
                    </td>
                    <td style="text-align: center" >
                            ${data.UNIT}
                    </td>
                    <td style="text-align: center;" >
                            ${data.BUY_QTY}
                    </td>
                    <td style="text-align: center" >
                    &nbsp;
                    </td>
                    <td style="text-align: center" >
                    &nbsp;
                    </td>
                    <td style="text-align: center" >
                            ${data.ROOM}
                    </td>
                    <td style="text-align: center">
                            ${data.WHMAN}
                    </td>
                    <td style="text-align: center">
                        &nbsp;
                    </td>
                </tr>
            </c:forEach>
        </TABLE>
    </div>
    <TABLE border=0px align="center" width="95%" style="margin-top: 25px;">
		<tr style="height: 25; line-height: 25px;">
			 <td align="left" width="33%">
				仓储中心领导：&nbsp;
			</td>
			 <td align="left" width="33%">
				配件处领导：&nbsp;
			</td>
			 <td align="left" width="33%">
				配件计划员：&nbsp;
			</td>
		</tr>
	</table>
	<TABLE border=0px align="center" width="95%">
		<tr style="height: 25; line-height: 25px;">
			 <td align="left" width="25%">
				仓储中心领导：&nbsp;
			</td>
			 <td align="left" width="25%">
				配件处领导：&nbsp;
			</td>
			 <td align="left" width="25%">
				配件计划员：&nbsp;
			</td>
			 <td align="left" width="25%">
				库房签收：&nbsp;
			</td>
		</tr>
	</TABLE>
    </div>
    </c:forEach>
    <table align="center" class="Noprint">  
  <tr>  
    <td align="center">
      <input type=button value="打印" onClick="printChkOrder()" >
      <input type=button value="打印设置" onClick="printSetup()">
      <input type=button value="打印预览" onClick="printPrieview()">
    </td>  
  </tr>
  </table>
</form>
</body>
<script type="text/javascript">
	var divArr = document.getElementsByName("thisblock");
	divArr[(divArr.length-1)].className="";
</script>
</html>
