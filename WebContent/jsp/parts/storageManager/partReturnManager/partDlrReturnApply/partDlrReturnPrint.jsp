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
        var url = '<%=contextPath%>/parts/purchaseOrderManager/PurchaseOrderChkManager/updatePrintDate.json';
        sendAjax(url, handleControl, 'fm');
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
    <input type="hidden" name="chkCode" value="${dataMap.mainMap.chkCode}"/>
    <c:forEach items="${allList}" var="subList" >
    <div name=thisblock id=thisblock style="width:21.5cm; height: 10.5cm;" class="PageNext">
        <TABLE border=0 border=0 bordercolor=black cellpadding=0 cellspacing=0 width="95%">
            <br><center><font size="+1"><b>
                北汽幻速汽车销售有限公司配件销售退货清单
            </b></font></center>
        </TABLE>
        <table border=0  cellpadding=0  cellspacing=0 width="100%" >
            <tr style="height: 18; line-height: 18px;">
                <td width="45%" nowrap>
                   购货单位：${dataMap.mainMap.ORG_NAME}
                </td>
                <td width="30%">
                    
                </td>
                <td width="29%" >
                   时间：${dataMap.mainMap.curDate}
                </td>
            </tr>
            <tr style="height: 18; line-height: 18px;">
                <td width="45%">
                   门市：${dataMap.mainMap.WH_NAME}
                </td>
                <td width="30%" nowrap>
                     
                </td>
                <td width="29%" nowrap >
                    单号：${dataMap.mainMap.RETURN_CODE}
                </td>
            </tr>
            <tr style="height: 18; line-height: 18px;">
            <td width="45%" nowrap >
            代码：${dataMap.mainMap.DEALER_CODE}
                </td>
                <td width="30%" nowrap>
                制单：${dataMap.mainMap.NAME}
                </td>
                <td width="29%">
                    类型：销售退货单
                </td>
            </tr>
        </table>
        <div  style="height:250px; width: 99%;" >
        <TABLE id="file" border=1 bordercolor=black cellpadding=0   cellspacing=0 width="100%" style="margin-top: 5px;" >
            <tr style="height: 19; line-height: 19px;">
                <td width=5% style="text-align: center" colspan=1>序号</td>
                <td width=12% style="text-align: center" colspan=1>配件代码</td>
                <td width=22% style="text-align: center" colspan=1>配件名称</td>
                <td width=15% style="text-align: center" colspan=1>件号</td>
                <td width=10% style="text-align: center" colspan=1>车型</td>
                <td width=5% style="text-align: center" colspan=1>数量</td>
                <td width=5% style="text-align: center" colspan=1>单位</td>
                <td width=9% style="text-align: center" colspan=1>单价</td>
                <td width=9% style="text-align: center" colspan=1>金额</td>
                <td width=10% style="text-align: center" colspan=1>货位</td>
            </tr>
            <c:forEach items="${subList}" var="data" varStatus="curSta">
            <tr style="height: 19; line-height: 19px;">
                    <td style="text-align: center">
                        <script language="javascript">
                            getIndex()
                        </script>
                    </td>
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
                            &nbsp;&nbsp;
                    </td>
                    <td style="text-align: center" >
                            ${data.RETURN_QTY}
                    </td>
                    <td style="text-align: center;" >
                            ${data.UNIT}
                    </td>
                    <td style="text-align: center" >
                            ${data.BUY_PRICE}
                    </td>
                    <td style="text-align: center" >
                            ${data.BUY_AMOUNT}
                    </td>
                    <td style="text-align: center">
                            ${data.LOC_NAME}
                    </td>
                </tr>
            </c:forEach>
        </TABLE>
    </div>
    <TABLE border=0  width="95%" >
		 <tr style="height: 18; line-height: 18px;">
         <td align="left" width="30%">总金额(含税)：${dataMap.mainMap.amount}元</td>
         </tr>
         
          <tr style="height: 18; line-height: 18px;">
          <td align="left" width="20%">财务：&nbsp;&nbsp;</td>
          <td align="left" width="20%">记账：&nbsp;&nbsp;</td>
          <td align="left" width="20%">收款：&nbsp;&nbsp;</td>
          <td align="left" width="20%">发货：&nbsp;&nbsp;</td>
          <td align="left" width="20%">提货：&nbsp;&nbsp;</td>
          </tr>
          
    </TABLE>
    <TABLE border=0  width="50%" >
		  <tr style="height: 18; line-height: 18px;">
          <td align="left" width="30%">地址：&nbsp;&nbsp;</td>
          <td align="left" width="20%">电话：&nbsp;&nbsp;</td>
          </tr>
    </TABLE>
    </div>
    </c:forEach>
    <br>
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