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
   <style type="text/css">
html,body{font-size:12px;margin:0px;height:100%;}
.mesWindow{border:#666 1px solid;background:#fff;}
.mesWindowTop{border-bottom:#eee 1px solid;margin-left:4px;padding:3px;font-weight:bold;text-align:left;font-size:12px;}
.mesWindowContent{margin:4px;font-size:12px;}
.mesWindow .close{height:15px;width:28px;border:none;cursor:pointer;text-decoration:underline;background:#fff}
</style>
<style media=print>   
.Noprint{display:none;}  .PageNext{page-break-after: always;}   
</style>
</head>  
<script language="javascript">
    //获取选择框的值
    function getCode(value) {
        var str = getItemValue(value);

        document.write(str);
    }
    function getIndex() {
        document.write(document.getElementById("file").rows.length - 1);
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
<body>
<form name="fm" id="fm" method="post" enctype="multipart/form-data">
    <input type="hidden" name="chkCode" value="${dataMap.mainMap.chkCode}"/>
    <input type=textarea name=datapackager style="display:none" value=''>

    <div name=thisblock id=thisblock>
        <TABLE border=0 bordercolor=black  cellspacing=0 width="91%">
            <br>
            <center><font size="+1"><b>
                北汽幻速汽车销售有限公司配件验货明细
            </b></font></center>
        </TABLE>
        <table border=0  cellpadding=3 align="center" cellspacing=0 width="91%" >
            <tr align="center">
                <td   >
                    制单人：
                </td>
                <td >
                  ${dataMap.mainMap.createBy}
                </td>
                <td>
                     &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;
                </td>
                <td >
                    制单日期:
                </td>
                <td >
                   ${dataMap.mainMap.curDate}
                </td>
            </tr>
        </table>
        <TABLE id="file" border=1 bordercolor=black  align="center" cellspacing=0 width="91%">
            <tr align="center">
                <td colspan=1  width="1%"> 序号</td>
                <td colspan=1 width="5%">配件编码</td>
                <td colspan=1 width="5%">配件名称</td>
                <td colspan=1 width="5%">件号</td>
                <td colspan=1 width="2%">单位</td>
                <td colspan=1 width="3%">货位</td>
                <td colspan=1 width="5%">验货数量</td>
                <td colspan=1 width="5%">实收数量</td>
                <td colspan=1 width="10%">供货商</td>
                <td colspan=1 width="10%">进货厂家</td>
                <td colspan=1 width="10%">验货单号</td>
                <td colspan=1 width="5%">计划员</td>
                <td colspan=1 width="5%">库管员</td>
                <td colspan=1 width="5%">备注</td>
            </tr>
            <c:forEach items="${dataMap.detailList}" var="data" varStatus="curSta">
                <c:if test="${curSta.count%45==0}">
			    <tr align="center" class="PageNext"> 
			    </c:if>
			    <c:if test="${curSta.count%45!=0}">
			    <tr align="center"> 
			    </c:if>
                    <td>
                        <script language="javascript">
                            getIndex()
                        </script>
                    </td>
                    <td align="left">
                            ${data.PART_OLDCODE}
                    </td>
                    <td align="left">
                            ${data.PART_CNAME}
                    </td>
                    <td align="left">
                            ${data.PART_CODE}
                    </td>

                    <td>
                            ${data.UNIT}
                    </td>
                    <td>
                            ${data.LOC_CODE}
                    </td>
                    <td>
                            ${data.GENERATE_QTY}
                    </td>
                    <td>
                            &nbsp;${data.YSSL}
                    </td>
                    <td align="left">
                        &nbsp;  ${data.VENDER_NAME}
                    </td>
                    <td align="left">
                        &nbsp; ${data.MAKER_NAME}
                    </td>
                    <td align="left">
                        &nbsp;${data.CHK_CODE}
                    </td>
                <td align="left">
                    &nbsp;${data.PLANER}
                </td>
                <td align="left">
                    &nbsp;${data.WHMAN}
                </td>
                    <td>
                        &nbsp;${data.NOTE}
                    </td>
                </tr>
            </c:forEach>
        </TABLE>
    </div>
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
</html>