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
	<meta http-equiv="X-UA-Compatible" content="IE=9">
	<script type="text/javascript" src="<%=request.getContextPath()%>/js/web/jquery-1.7.2.min.js"></script>
	<jsp:include page="${contextPath}/common/jsp_head_new.jsp"/>
    <script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery-barcode-2.0.2.min.js"></script>
    <title>ASN收货单</title>
    <style media=print>
        .Noprint {display: none;}
        .p_next {page-break-after: always;}
    </style>
    <style type="text/css">
        html,body{margin:0px;height:100%;}
        .mesWindow{border:#666 1px solid;background:#fff;}
        .mesWindowTop{border-bottom:#eee 1px solid;margin-left:4px;padding:3px;font-weight:bold;text-align:left;}
        .mesWindowContent{margin:4px;}
        .mesWindow .close{height:15px;width:28px;border:none;cursor:pointer;text-decoration:underline;background:#fff}
        
        /*--------------打印start-------------------*/
        #mian_div{
        	width:100%;
        	height: 100%;
        	font-family: 微软雅黑;
        	font-size: 15px;
        }
        .top_cen_mid_div{
        	width: 210mm;
        	height: 40mm;
        	margin: 0 auto;
        }
        .cen_mid_div{
        	width: 210mm;
        	height: 148mm;
        	margin: 0 auto;
        }
        .tableHead {
            border-spacing: 0px;
            border-collapse: collapse;
            border-spacing: 0;
            border-left: 1px solid #888;
            border-top: 1px solid #888;
        }

        .tableHead td {
            height: 10mm;
            line-height: 10mm;
            /*border: 1px solid black;*/
        }

        .tableBody {
            border-collapse: collapse;
            border-spacing: 0px;
        }

        .tableBody td {
            white-space: nowrap;
            height: 8mm;
            overflow: hidden;

            /*line-height: 8mm;*/
            /*border: 1px solid black;*/
        }
        table tr td{
        	font-size: 14px;
        }
        /*--------------打印end---------------------*/
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

   /*  function printWithAlert() {
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
    
    //根据数据生成条形码
    function getBarcode(data,inId,widthT){
    	if(widthT==null || widthT==''){
    		widthT = 1;
    	}
    	var settings = {
             output:"css",
             bgColor: "#FFFFFF",
             color: "#000000",
             barWidth: widthT,
             barHeight: "40",
             moduleSize: "5",
             posX: "1",
             posY: "1",
             addQuietZone: false,
             marginHRI: "1",
             showHRI: false
        };
    	jQuery("#"+inId).barcode(data,"code128",settings);
    }
</script>
<body style="text-align:center;margin:0px;padding:0px;">
	<form name="fm" id="fm" method="post" enctype="multipart/form-data">
		<!-- 按钮  -->
		<table border=0 cellpadding=0 cellspacing=0 class="Noprint page-print-buttons" align="center" width=100%>
            <tr style="border: 0px;">
                <td style="border: 0px;">
                    <input type=button value="打印">
                    <input type=button value="打印设置">
                    <input type=button value="打印预览">
                </td>
            </tr>
        </table>
        <div id="mian_div" name="mian_div">
			<c:forEach items="${listPo}" var="po" varStatus="v">
				<div class="cen_mid_div" <c:if test="${v.count%2==0 && v.last==false}">style="page-break-after:always;"</c:if>>
					
					<c:if test="${v.count%2==0 && v.last==false}">
						<div class="top_cen_mid_div" align="center;"></div>
					</c:if>
					
		            <!-- 内容  -->
		            <div style="width: 100%;">
		            	<table  border="0px" cellpadding="1" cellspacing="0" align="center" width="100%">
			                <tr style="height: 15mm; line-height: 15mm;">
			                    <td align="left" style="border: 0px; border-bottom: 1px solid #000;width: 70mm;" colspan="2"><%-- C${po.PRINT_XH } --%></td>
			                    <td align="center" style="border: 0px; border-bottom: 1px solid #000;width: 70mm; font-size: 16px;font-weight: bold;" colspan="2">君马新能源备件-分包单</td>
			                    <td align="left" style="border: 0px; border-bottom: 1px solid #000;width: 70mm;" colspan="2" id="CON_ID_${po.POLINE_ID}">
		                    		<script type="text/javascript">
			                    		getBarcode('${po.POLINE_ID}','CON_ID_${po.POLINE_ID}',2);
			                    	</script>
			                    </td>
			                </tr>
			                
			                <tr style="height: 10mm; line-height: 10mm;">
			                    <td align="left" style="border: 0px;border-bottom: 1px solid #000;width: 20mm;">供应商代码:</td>
			                    <td align="left" style="border: 0px;border-bottom: 1px solid #000;width: 50mm;">${po.VENDER_CODE }</td>
			                    <td align="left" style="border: 0px;border-bottom: 1px solid #000;width: 20mm;">确认日期:</td>
			                    <td align="left" style="border: 0px;border-bottom: 1px solid #000;width: 50mm;">${po.CREATE_DATE }</td>
			                    <td align="center" style="border: 0px;border-bottom: 1px solid #000;width: 70mm; font-size: 14px;" colspan="2">${po.ORDER_ID }</td>
			                </tr>
			                
			                <tr style="height: 10mm; line-height: 10mm;">
			                    <td align="left" style="border: 0px;width: 20mm;">供应商名称:</td>
			                    <td align="left" style="border: 0px;width: 50mm;line-height: 18px;">${po.VENDER_NAME}</td>
			                    <td align="left" style="border: 0px;width: 20mm;">打印日期:</td>
			                    <td align="left" style="border: 0px;width: 50mm;">${po.PRINT_DATE}</td>
			                    <td align="left" style="border: 0px;width: 20mm;">货位编码:</td>
			                    <td align="left" style="border: 0px;width: 50mm;">${po.LOC_CODE}</td>
			                </tr>
			            </table>
			            
			            <table class="tableHead" border="1px" cellpadding="0" cellspacing="0" width="100%">
			                <tr>
			                   <td>备件编码</td>
			                   <td>编码/件号</td>
			                   <td style="width: 40mm">备件名称</td>
			                   <td>单位</td>
			                   <td>采购数量</td>
			                   <td>中包装量</td>
			                   <td>装箱量</td>
			                   <td>包装</td>
			                   <td>备注</td>
			                </tr>
			                
		                	<tr>
			                    <td rowspan="2" style="padding-left: 1px;padding-right: 1px;">
			                    	<div id="PART_OLDCODE_${po.PART_OLDCODE}">
			                    		${po.PART_OLDCODE}
			                    	</div>
			                    </td>
			                    <td>${po.PART_OLDCODE}</td>
			                    <td rowspan="2">${po.PART_CNAME}</td>
			                    <td rowspan="2">${po.UNIT}</td>
			                    <td rowspan="2">${po.BUY_QTY}</td>
			                    <td rowspan="2">${po.MIDDLE_PACKAGE }</td>
			                    <td rowspan="2">${po.BOX_NUM }</td>
			                    <td>${po.PALLET_BOX }</td>
			                    <td rowspan="2">${po.REMARK }</td>
			                </tr>
			                <tr>
			                    <td>${po.PART_CODE}</td>
			                    <td>${po.BOXZ }箱-${po.SYG }${po.UNIT }</td>			                    
			                </tr>
			                
			            </table>
			            
			            <table class="tableBottom" width="100%" style="border-top: 0px;border-left: 0px;">
			                <tr style="height: 10mm; line-height: 10mm;  text-align: right;">
			                    <td align="left">制单员:</td>
			                    <td align="left" width="150px;"></td>
			                    <td align="left">收货员:</td>
			                    <td align="left" width="150px;"></td>
			                    <td align="left">包装员:</td>
			                    <td align="left" width="150px;"></td>
			                    <td align="right"></td>
			                </tr>
			            </table>
		           	</div>
				</div>
			</c:forEach>
		</div>
	</form>
	
</body>
</html>