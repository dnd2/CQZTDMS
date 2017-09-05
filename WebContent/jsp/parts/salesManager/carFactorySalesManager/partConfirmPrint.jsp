<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c"%>
<%
    String contextPath = request.getContextPath();
%>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<head>
    <title></title>

    <style media=print>
        .Noprint{display:none;}

        .p_next {page-break-after: always;}
    </style>
    <style type="text/css">
        body
        {
        	margin: 0px;
        	text-align: center;
        }
        
        td
        {
        	border: 1px solid black;
        }
        
        #mian_div
        {
        	width: 210mm;
        	height: 297mm;
        	margin: 0px;
        }
        #top_div
        {
        	width: 100%;
        	height: 15mm;
        	margin: 0px;
        	float: left;
        }
        #mid_div
        {
        	width: 100%;
        	height: 265mm;
        	margin: 0px;
        	float: left;
        }
        #lef_mid_div
        {
        	width: 15mm;
        	height: 100%;
        	margin: 0px;
        	float: left;
        }
        #cen_mid_div
        {
        	width: 175mm;
        	height: 100%;
        	margin: 0px;
        	float: left;
        }
        #top_cen_mid_div
        {
        	width: 100%;
        	height: 10mm;
        	margin: 0px;
        	float: left;
        	text-align: center;
        	font-size: 25px;
        	font-weight: bold;
        	line-height: 10mm;
        }
        #but_cen_mid_div
        {
        	width: 100%;
        	height: 255mm;
        	margin: 0px;
        	float: left;
        }
        #rig_mid_div
        {
        	width: 19mm;
        	height: 100%;
        	margin: 0px;
        	float: left;
        }
        #fot_div
        {
        	width: 100%;
        	height: 15mm;
        	margin: 0px;
        	float: left;
        }
    </style>
</head>
<script language="javascript">
    //获取选择框的值
    function getCode(value){
        var str = getItemValue(value);
        document.write(str);
    }
    var idx = 0;
    function getIndex(){
        idx+=1
        document.write(idx);
    }
    function printOrder(){
        //后续 添加 装箱单 打印 的信息
        var url = "<%=contextPath%>/parts/salesManager/carFactorySalesManager/PartPkg/printPkgOrder.json?";

        $('printBtn').disabled = true;
        //sendAjax(url, getResult, 'fm');
    }
    function getResult(jsonObj){
        $('printBtn').disabled = false;

        if(jsonObj!=null){
            var error = jsonObj.error;
            var exceptions = jsonObj.Exception;
            if(error){
                MyAlert(error);
                _hide();
                return;
            }else if(exceptions){
                MyAlert(exceptions.message);
                _hide();
                return;
            }
            window.print();
            _hide();
        }
    }
    function printpreview()
    {
        WebBrowser.execwb(7,1);   // 打印页面预览
    }

</script>

<OBJECT  id=WebBrowser  classid=CLSID:8856F961-340A-11D0-A96B-00C04FD705A2 style="display:none">
</OBJECT>

<body onload="">
<form name="fm" id="fm" method="post"  enctype="multipart/form-data">
<input name="pickOrderId" id="pickOrderId" value="${dataMap.pickOrderId}" type="hidden" />
<c:forEach items="${dataMap.detailList}" var="data"  varStatus="countter">
  <div id="mian_div">
    <div id="top_div">
    </div>
    <div id="mid_div">
      <div id="lef_mid_div">
      </div>
      <div id="cen_mid_div">
        <div id="top_cen_mid_div">
            <img src="<%=contextPath%>/img/bq_log.gif" style="float: left;" />
            	随车装箱单&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
        </div>
        <div id="but_cen_mid_div">
          <table border="1px" cellpadding="0" cellspacing="0" height="40mm" width="100%" style="color: black; ">
            <tr style="font-weight: bold;height: 8mm; line-height: 8mm;">
              <td colspan="8" align="center" style="font-size: 18px;">
              	随车装箱单抬头信息
              </td>
            </tr>
            <tr style="font-weight: bold; height: 12mm; line-height: 12mm;">
              <td>
                                         拣货单号
              </td>
              <td colspan="7" align="center" style="font-size: 30px; font-weight: normal;">
                &nbsp;${dataMap.pickOrderId}
              </td>
            </tr>
            <tr style="font-weight: bold; height: 6mm; line-height: 6mm;">
              <td>
                                           发货日期
              </td>
              <td>
                &nbsp;${dataMap.date}
              </td>
              <td>
                	发货工厂
              </td>
              <td style="width: 18mm;">
                &nbsp;${dataMap.whName}
              </td>
              <td>
                	收货工厂
              </td>
              <td style="width: 18mm;">
              &nbsp;
              </td>
              <td>
            	  订单类型名称		
              </td>
              <td>
              	&nbsp;${dataMap.orderType}
              </td>
            </tr>
            <tr style="font-weight: bold; height: 8mm; line-height: 8mm;">
              <td>
              	经销商代码	
              </td>
              <td colspan="2">
             &nbsp;${dataMap.dealerCode}
              </td>
              <td colspan="3">
              	经销商名称
              </td>
              <td colspan="2">
              	&nbsp;${dataMap.dealerName}			
              </td>
            </tr>
            <tr style="font-weight: bold; height: 6mm; line-height: 6mm;">
              <td>
              	制表日期	
              </td>
              <td colspan="2">
              &nbsp;${dataMap.createDate}
              </td>
              <td colspan="3">
              	制表人
              </td>
              <td colspan="2">
              &nbsp;${dataMap.creater}		
              </td>
            </tr>
          </table>
          <table border="1px" cellpadding="0" cellspacing="0" height="220mm" width="100%" style="color: black; border-top: 0px;">
            <tr style="font-weight: bold; height: 8mm; line-height: 8mm;">
              <td colspan="9" align="center" style="font-size: 18px; border-top: 0px;">
              	随车装箱单细节信息
              </td>
            </tr>
            <tr style="font-weight: bold; height: 8mm; line-height: 8mm; text-align: center;">
              <td style="width: 8mm;">
                                           序号
              </td>
              <td style="width: 20mm;">
               	 箱号	
              </td>
              <td style="width: 35mm;">
				配件号
              </td>
              <td style="width: 40mm;">
				配件名称
              </td>
              <td style="width: 10mm;">
            	 数量		
              </td>
              <td style="width: 13mm;">
            	单价		
              </td>
              <td style="width: 17mm;">
            	 金额		
              </td>
              <td style="width: 25mm;">
              	备注
              </td>
            </tr>
            <c:forEach items="${data}" var="data" >
            <tr style=" height: 8mm; line-height: 8mm; border: 1px solid black; text-align: center;">
              <td>
              <script language="javascript">
                   getIndex();
               </script>
              </td>
              <td>
               &nbsp;${data.PKG_NO}
              </td>
              <td style="width:35mm; overflow: hidden; white-space: nowrap; text-overflow:ellipsis;">
              &nbsp;${data.PART_CODE}
              </td>
              <td style="width: 40mm; overflow: hidden; white-space: nowrap; text-overflow:ellipsis;">
              	&nbsp;${data.PART_CNAME}
              </td>
              <td>
              &nbsp;${data.PKG_QTY}
              </td>
              <td style="text-align: right;">
              ${data.BUY_PRICE}&nbsp;
              </td>
              <td style="text-align: right;">
              ${data.PKG_AMOUNT}&nbsp;
              </td>
              <td style="width: 25mm; overflow: hidden; white-space: nowrap; text-overflow:ellipsis;">
              &nbsp;${data.REMARK}
              </td>
            </tr>
            </c:forEach>
            <c:if test="${countter.last}" >
            <%
            Object dtlListAct = request.getAttribute("dtlListAct");
            int count = 0;
            if(null != dtlListAct)
            {
            	count = Integer.parseInt(dtlListAct.toString());
            }
            int fyNum = 20; //
            int cyclNum = fyNum - count;
            for(int i = 0; i < cyclNum; i ++)
            {
           %>
           <tr style=" height: 8mm; line-height: 8mm; border: 1px solid black; text-align: center;">
              <td>
              &nbsp;
              </td>
              <td>
              &nbsp;
              </td>
              <td style="width:35mm; overflow: hidden; white-space: nowrap; text-overflow:ellipsis;">
              &nbsp;
              </td>
              <td style="width:40mm; overflow: hidden; white-space: nowrap; text-overflow:ellipsis;">
              	&nbsp;
              </td>
              <td>
              &nbsp;
              </td>
              <td>
               &nbsp;
              </td>
              <td>
               &nbsp;
              </td>
              <td style="width:25mm; overflow: hidden; white-space: nowrap; text-overflow:ellipsis;">
             	 &nbsp;
              </td>
            </tr>
           <%
            }
            %>
            </c:if>
          </table>
          <table border="0px" cellpadding="0" cellspacing="0" width="100%" height="30mm" style=" margin-top: 2mm;">
            <tr style="height: 10mm; line-height: 10mm; font-weight: bold; text-align: right;">
              <td style="border:  0px; ">
                	系统员：___________________
              </td>
            </tr>
            <tr style="height: 10mm; line-height: 10mm; font-weight: bold; text-align: right;">
              <td style="border:  0px; ">
                	装箱员：___________________
              </td>
            </tr>
          </table>
        </div>
      </div>
      <div id="rig_mid_div">
      </div>
    </div>
    <div id="fot_div">
    <c:if test="${countter.last}" >
    <TABLE border=0 cellpadding=0 cellspacing=0 class="Noprint" align="center" width=100%>
        <tr style="border: 0px;">
            <td style="border: 0px;">
            	<input type=button id="printBtn" class="txtToolBarButton" value="打印"  onClick="printOrder()">
                <input type=button id="printBtn2" class="txtToolBarButton" value="预览" onClick="printpreview()">
            </td>
        </tr>
    </TABLE>
    </c:if>
    </div>
  </div>
  </c:forEach>
</form>
</body>
</html>
