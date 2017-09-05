<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c" %>
<%@ taglib prefix='fmt' uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%
    String contextPath = request.getContextPath();
%>
<head>
	<jsp:include page="${contextPath}/common/jsp_head_new.jsp"/>
	<!-- 
        <script type="text/javascript" src="<%=request.getContextPath()%>/js/web/jquery-1.7.2.min.js"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery.qrcode.min.js"></script>   
     -->
    <script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery.qrcode.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/js/qrcode.js"></script>
    
    <title>入库二维码打印</title>
    <metahttp-equiv="X-UA-Compatible" content="IE=EmulateIE9"/>
    <!-- 
    <meta http-equiv="X-UA-Compatible" content="IE=9">
     -->
    <style media=print>
        .Noprint {
            display: none;
        }

        .p_next {
            page-break-after: always;
        }
    </style>
    <style type="text/css">
        html,body{font-size:18px;margin:0px;height:100%;}
        .mesWindow{border:#666 1px solid;background:#fff;}
        .mesWindowTop{border-bottom:#eee 1px solid;margin-left:4px;padding:3px;font-weight:bold;text-align:left;font-size:12px;}
        .mesWindowContent{margin:4px;font-size:16px;}
        .mesWindow .close{height:15px;width:28px;border:none;cursor:pointer;text-decoration:underline;background:#fff}
    </style>
    <style type="text/css">
        .tableHead {
            border-spacing: 0px;
            border-collapse: collapse;
            border-spacing: 0;
            border-left: 1px solid #888;
            border-top: 1px solid #888;
        }

        .tableHead td {
        /*
            height: 8mm;
            line-height: 8mm;*/
            /*border: 1px solid black;*/
        }

        .tableBody {
            border-collapse: collapse;
            border-spacing: 0px;
        }

        .tableBody td {
            white-space: nowrap;
            /*
           	height: 5mm;  */
            overflow: hidden;

            /*line-height: 8mm;*/
            /*border: 1px solid black;*/
        }

        #mian_div {
            width: 85mm;
            /*height: 280mm; margin: 0 auto;*/
            margin: 0mm;
            padding: 0mm;
            /*border: 0px solid black;*/
        }

        #cen_mid_div {
            /* width: 85mm; */
            width:100%;
            border: 0mm solid red;
           	margin:0mm;
           	padding: 0mm;
            position: absolute;
        }
 

        /*table {*/
            /*border-collapse: collapse;*/
            /*border-spacing: 0;*/
            /*border-left: 1px solid #888;*/
            /*border-top: 1px solid #888;*/
            /*!*background: #efefef;*!*/
        /*}*/

        th, td {
            border-right: 1px solid #888;
            border-bottom: 1px solid #888;
            /*padding: 5px 15px;*/
        }

        th {
            font-weight: bold;
            /*background: #ccc;*/
        }

        .tableBottom td {
            border-right: 0px;
            border-bottom: 0px;
        }
        .table1{
        	text-align:center;
        	padding: 0mm 0mm 0mm 1mm;
        	margin:2mm 0mm 8mm 0mm;
        	float: left;
        	width: 79mm;
        	height: 39mm;
        	font-size: 14px;
        }
        .table2{
        	text-align:center;
        	padding: 0mm 0mm 0mm 1mm;
        	margin:0mm 0mm 0mm 0mm;
        	float: left;
        	width: 79mm;
        	height: 39mm;
        	font-size: 14px;
        }
        .table3{
        	text-align:center;
        	padding: 2mm 0mm 0mm 1mm;
        	margin:5mm 0mm 8mm 0mm;
        	float: left;
        	width: 79mm;
        	height: 41mm;
        	font-size: 14px;
        }
    </style>
</head>
<script language="javascript">
	jQuery.noConflict();
    //获取选择框的值
    function getCode(value) {
        var str = getItemValue(value);

        document.write(str);
    }

    var idx = 0;
    function getIndex() {
        idx += 1;
        document.write(idx);
    }

    /* function printWithAlert() {
        document.all.WebBrowser.ExecWB(6, 1);
    }
    function printWithoutAlert() {
        document.all.WebBrowser.ExecWB(6, 6);
    }
    function printSetup() {
        document.all.WebBrowser.ExecWB(8, 1);
    }
    function printPrieview() {
        document.all.WebBrowser.ExecWB(7, 1);
    }
    function printImmediately() {
        document.all.WebBrowser.ExecWB(6, 6);
        window.close();
    }
    var HKEY_Root, HKEY_Path, HKEY_Key;
    HKEY_Root = "HKEY_CURRENT_USER";
    HKEY_Path = "\\Software\\Microsoft\\Internet Explorer\\PageSetup\\";
    //设置网页打印的页眉页脚为空
    function PageSetup_Null() {
        try {
            var Wsh = new ActiveXObject("WScript.Shell");
            HKEY_Key = "header";
            Wsh.RegWrite(HKEY_Root + HKEY_Path + HKEY_Key, "");
            HKEY_Key = "footer";
            Wsh.RegWrite(HKEY_Root + HKEY_Path + HKEY_Key, "");
        }
        catch (e) {
        }
    }
    //设置网页打印的页眉页脚为默认值
    function PageSetup_Default() {
        try {
            var Wsh = new ActiveXObject("WScript.Shell");
            HKEY_Key = "footer";
            Wsh.RegWrite(HKEY_Root + HKEY_Path + HKEY_Key, "&b&p/&P");
            //HKEY_Key="footer";
            //Wsh.RegWrite(HKEY_Root+HKEY_Path+HKEY_Key,"&u&b&d");
        }
        catch (e) {
        }
    }
    PageSetup_Null(); */
    
    //根据数据生成二维码
    function getQrcode(data,inId){
    	jQuery("#"+inId).qrcode({ 
    	    render: "canvas", //table方式 或者  canvas（html5） 方式
    	    width: 105, //宽度 
    	    height:105, //高度 
    	    text: data, //任意内容 
    	    typeNumber  : -1,      //计算模式  
    	    correctLevel    : 0,//纠错等级  
    	    background      : "#ffffff",//背景颜色  
    	    foreground      : "#000000" //前景颜色  

    	}); 
    }
    
    //上一页
    function toPreviousT(curPage,totalPages){
    	if(curPage<1){
    		MyAlert('当前已是第一页！');
    		return
    	}
    	location = '<%=contextPath%>/parts/planManager/PartPlanManager/toPurcharOrderQrPrint.do?inId=${inId}&curPage='+curPage;
    }
    //下一页
    function toNextT(curPage,totalPages){
    	if(curPage>totalPages){
    		MyAlert('当前已是最后一页！');
    		return
    	}
    	location = '<%=contextPath%>/parts/planManager/PartPlanManager/toPurcharOrderQrPrint.do?inId=${inId}&curPage='+curPage;
    }
    
</script>
<body style="text-align:center;margin:0px;padding:0px;">

<form name="fm" id="fm" method="post" enctype="multipart/form-data">
    <div id="mian_div" name="mian_div">
        <div id="cen_mid_div" name="cen_mid_div">
        
        	<TABLE border=0 cellpadding=0 cellspacing=0 class="Noprint page-print-buttons" align="center" width=100%>
                <tr style="border: 0px;">
                    <td style="border: 0px;padding-bottom:10px">
                        <input type=button value="打印">
                        <input type=button value="打印设置">
                        <input type=button value="打印预览">
                    </td>
                </tr>
                
                <tr style="border: 0px;">
                    <td style="border: 0px;">
                    	<input type=button value="上一页" onClick="toPreviousT('${ps.curPage-1}','${ps.totalPages }')">
                        <label style="font-size: 10px;">第${ps.curPage }页,共${ps.totalPages }页,共${ps.totalRecords }条</label>
                        <input type=button value="下一页" onClick="toNextT('${ps.curPage+1}','${ps.totalPages }')">
                    </td>
                </tr>
                		
            </TABLE>
        
            <c:forEach items="${qrList}" var="list" varStatus="vStatus">
            	<c:if test="${vStatus.count%2==1 }"> 
            	<div style="width: 82mm;margin:5mm 4mm 0mm 2mm;height:85mm;  page-break-after: always; display: block;">
            	</c:if>
            	 
            	<table <c:if test="${vStatus.count%2==1 && vStatus.count ==1}">class="table1"</c:if>
            		   <c:if test="${vStatus.count%2==0 }">class="table2"</c:if>
            		   <c:if test="${vStatus.count%2==1 && vStatus.count !=1 }">class="table3"</c:if>
            		     cellpadding="0" cellspacing="0" border="0" >
	                <tr>
	                   <td style="border: 0px;text-align: left;width:30mm;height:11mm;" rowspan="2">&nbsp;</td>
	                   <td style="border: 0px;text-align: left;width:49mm;"></td>
	                   
	                </tr>
	                <tr>
	                   <td style="border: 0px;text-align: left;height:9mm;">&nbsp;</td>                 
	                </tr>
	                 <tr>
	                   <td style="border: 0px;" colspan="2"><img src="<%=contextPath%>/img/logox.png"></td>     
	                   <td style="border: 0px;"></td>  
	                </tr>
	                <tr>
	                   <td  style="border: 0px;text-align: left;" rowspan="6"><div id="ORDER_CODE_${vStatus.index}"></div></td>
	                   <td style="border: 0mm;text-align: left;padding-left:2mm;"  >${list.PART_OLDCODE }</td>
	                </tr>
	                <tr>
	                   <td style="border: 0mm;text-align: left;padding-left:2mm;"  >${list.PART_CODE }</td>
	                </tr>
	                <tr>
	                   <td style="border: 0mm;text-align: left;padding-left:2mm;"  >${list.PART_CNAME }</td>
	                </tr>
	                <tr>
	                   <td style="border: 0mm;text-align: left;padding-left:2mm;"  >${list.PART_ENAME }</td>
	                </tr>	           
	                <tr>
	                   <td style="border: 0mm;text-align: left;padding-left:2mm;"　>${list.IN_DATE }&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;${list.QTY}&nbsp;&nbsp;${list.UNIT }/PCS</td>
	                </tr>
	                <tr>
	                   <td style="border: 0mm;text-align: left;padding-left:2mm;"  >${list.VENDER_NAME }<br></td>
	                </tr>
	                <tr>	              
	                   <td style="border: 0mm;text-align: left;padding-left:2mm;">${list.ADDR }</td>     
	                   <td style="border: 0mm;text-align: left;padding-left:2mm;">${list.TEL }</td>  
	                </tr>
	            </table>
	            
	            <script type="text/javascript">
			    	getQrcode('${list.STOCK_IN}||${list.IN_CODE}||${list.PART_OLDCODE}||${list.PART_CODE}||${list.PO_ID}||${list.IN_DATE}||${list.VENDER_CODE}||${list.QTY}||${list.PART_UNIONQ_CODE}','ORDER_CODE_${vStatus.index}');
			    </script>
			    <c:if test="${vStatus.last==true || vStatus.count%2==0 }">
            		</div>
            	</c:if>
			  
            </c:forEach>
            
            
            <TABLE border=0 cellpadding=0 cellspacing=0 class="Noprint page-print-buttons" align="center" width=100%>
                <tr style="border: 0px;">
                    <td style="border: 0px;padding-bottom:10px">
                        <input type=button value="打印">
                        <input type=button value="打印设置">
                        <input type=button value="打印预览">
                    </td>
                </tr>
                
                <tr style="border: 0px;">
                    <td style="border: 0px;">
                    	<input type=button value="上一页" onClick="toPreviousT('${ps.curPage-1}','${ps.totalPages }')">
                        <label style="font-size: 10px;">第${ps.curPage }页,共${ps.totalPages }页,共${ps.totalRecords }条</label>
                        <input type=button value="下一页" onClick="toNextT('${ps.curPage+1}','${ps.totalPages }')">
                    </td>
                </tr>
                		
            </TABLE>
        </div>
    </div>
</form>
</body>
</html>