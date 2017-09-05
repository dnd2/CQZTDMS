<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="java.util.List"%>
<%@page import="com.infodms.dms.po.TtAsWrApplicationExtPO"%> 
<%@taglib uri="/jstl/cout" prefix="c"%>
<%@taglib uri="/jstl/fmt" prefix="fmt"%>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />

<%@page import="java.text.DecimalFormat" %>
<%
DecimalFormat   df  =   new  DecimalFormat("##0.00");
%>
<style media=print>
    /* 应用这个样式的在打印时隐藏 */
    .Noprint {
     display: none;
    }
   
    /* 应用这个样式的，从那个标签结束开始另算一页，之后在遇到再起一页，以此类推 */
    .PageNext {
     page-break-after: always;
    }
   </style>

<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<title>昌河汽车特约服务商上报封面</title>
	</head>
<body onload="init();">
  <object classid="CLSID:8856F961-340A-11D0-A96B-00C04FD705A2" height="0" id="wb" name="wb" width="3"></object>
<div id="topContainer" style="" >
<br/>
<center>
  <strong><font size="5px">昌河汽车特约服务商上报封面(
  <script type="text/javascript">
 	 writeItemValue('${yieldly}')
  </script>
			)
  </font></strong>
</center>
<br/>
<form method="post" name="fm" id="fm">

<center>
  <table class="tab_printssa" width="800px"    >
    <tr   >
     	<td align="center" width="20%" > 打印日期</td>
     	<td colspan="5" width="80%" align="right">${now }</td>
    </tr>
     <tr >
     	<td colspan="6" align="right">&nbsp;</td>
    </tr>
     <tr >
     	<td align="center" width="20%"> 服务站代码</td>
     	<td colspan="2" width="30%" align="center">${bean.dealerCode } </td>
     	<td align="center" width="15%"> 服务站简称</td>
     	<td colspan="2" width="35%" align="center"> ${bean.dealerName }</td>
    </tr>
     <tr >
     	<td colspan="6" align="center">&nbsp;</td>
    </tr>
      <tr >
     	<td align="center" width="20%"> 索赔单上报日期</td>
     	<td colspan="5" width="80%" align="center">${starDate } 至 ${endDate } </td>
    </tr>
     <tr  >
     	<td align="center" width="20%"> 上报科目</td>
     	<td  width="15%" align="center"> 数量</td>
     	<td  width="15%" align="center"> 单位</td>
     	<td align="center" width="20%"> 金额</td>
     	<td colspan="2" width="30%" align="center">单位 </td>
    </tr>
     <tr  >
     	<td align="center" width="20%"> 强保定检</td>
     	<td  width="15%" align="center">${bean.baoyCount } </td>
     	<td  width="15%" align="center"> 份</td>
     	<td align="center" width="20%">${bean.freeMPrice } </td>
     	<td colspan="2" width="30%" align="center">元 </td>
    </tr>
     <tr  >
     	<td align="center" width="20%"> 售前维修</td>
     	<td  width="15%" align="center"> ${bean.shouqCount }</td>
     	<td  width="15%" align="center"> 份</td>
     	<td align="center" width="20%">${bean.shouqAmount } </td>
     	<td colspan="2" width="30%" align="center">元 </td>
    </tr>
     <tr  >
     	<td align="center" width="20%"> 正常维修</td>
     	<td  width="15%" align="center">${bean.normalCount } </td>
     	<td  width="15%" align="center"> 份</td>
     	<td align="center" width="20%"> ${bean.normalAmount }</td>
     	<td colspan="2" width="30%" align="center">元 </td>
    </tr>
     <tr  >
     	<td align="center" width="20%"> 特殊服务</td>
     	<td  width="15%" align="center"> ${bean.specialCount }</td>
     	<td  width="15%" align="center"> 份</td>
     	<td align="center" width="20%">${bean.specialAmount } </td>
     	<td colspan="2" width="30%" align="center">元 </td>
    </tr>
     <tr  >
     	<td align="center" width="20%"> 外派服务</td>
     	<td  width="15%" align="center">${bean.outCount } </td>
     	<td  width="15%" align="center"> 份</td>
     	<td align="center" width="20%">${bean.outAmount } </td>
     	<td colspan="2" width="30%" align="center">元 </td>
    </tr>
     <tr  >
     	<td align="center" width="20%"> 厂家活动</td>
     	<td  width="15%" align="center">${bean.activityCount } </td>
     	<td  width="15%" align="center"> 份</td>
     	<td align="center" width="20%">${bean.activityAmount } </td>
     	<td colspan="2" width="30%" align="center">元 </td>
    </tr>
     <tr  >
     	<td align="center" width="20%"> 运费</td>
     	<td  width="15%" align="center">${bean.transCount } </td>
     	<td  width="15%" align="center"> 份</td>
     	<td align="center" width="20%"> ${bean.transAmount }</td>
     	<td colspan="2" width="30%" align="center">元 </td>
    </tr>
     <tr  >
     	<td align="center" width="20%">其他费用</td>
     	<td  width="15%" align="center">${bean.otherCount } </td>
     	<td  width="15%" align="center"> 份</td>
     	<td align="center" width="20%">${bean.otherAmount } </td>
     	<td colspan="2" width="30%" align="center">元 </td>
    </tr>
     <tr  >
     	<td align="center" width="20%"> 合计</td>
     	<td  width="15%" align="center">
     	 <fmt:formatNumber type="number" value="${bean.countAmount }"  maxFractionDigits="2"/>
     	 </td>
     	<td  width="15%" align="center"> 份</td>
     	<td align="center" width="20%">
     	<fmt:formatNumber type="number" value="${bean.amountTotal }"  maxFractionDigits="2"/>
     	  </td>
     	<td colspan="2" width="30%" align="center">元 </td>
    </tr>
      <tr >
     	<td colspan="6" align="right">&nbsp;</td>
    </tr>
     <tr  >
     	<td align="center" width="20%"> 劳务费总计</td>
     	<td  width="15%" align="center">
     
     	<fmt:formatNumber type="number" value="${bean.labourPrice }"  maxFractionDigits="2"/>
     	 </td>
     	<td  width="15%" align="center"> 元</td>
     	<td align="center" width="20%"> 材料费总计</td>
     	<td align="center" width="15%">
     	<fmt:formatNumber type="number" value="${bean.partPrice }"  maxFractionDigits="2"/> </td>
     	<td  width="15%" align="center">元 </td>
    </tr>
 </table>
          </td>
          </tr>
          <tr>
   <td colspan="8" >
    </td></tr>

  </table>
  <br>
    <p class="Noprint">
		<input type=button name= button_print class="normal_btn" value="返回" onclick ="history.back();">
    </p>
</center>
</form>
<script language="javascript">    
  
	function printsetup()
	{       
		wb.execwb(8,1);    // 打印页面设置 
	}    
	function printpreview()
	{    
		wb.execwb(7,1);   // 打印页面预览       
	}      
	function printit()    
	{    
		if(confirm('确定打印吗？'))
		{    
			wb.execwb(6,6)    
		}    
	} 
	  
</script> 
</div>
</body>

</html>

