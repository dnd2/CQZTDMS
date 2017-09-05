<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@page import="com.infodms.dms.common.Constant"%>
<%@page import="com.infodms.dms.util.CommonUtils"%>
<%@taglib uri="/jstl/cout" prefix="c"%>
<%@taglib uri="/jstl/fmt" prefix="fmt"%>
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
<title>开票通知单打印</title>
</head>

<body>
<form id="fm" name="fm"><input type="hidden" id="planId"/>
<table width="756px;"  align="center"  border="0" class="bigTable"> 
 <thead style="display:table-header-group;"> 
 	<tr >
  	  <td height="20" colspan="15" align="center">
  	  <table >
  	    <tr align="center">
	          <td align="center" height="80" width="100%" colspan="6"><span class="STYLE1">开票通知单</span></td>
	    </tr>
  	  </table>
  	  <table class="tabp">
	      
	        <tr >
			    <td class="tdp">结算单号：</td>
			    <td class="tdp">${list.BALANCE_NO }</td>
			    <td class="tdp">制单日期：</td>
			    <td class="tdp">
				  	<fmt:formatDate value='${audit.authTime}' pattern='yyyy-MM-dd'/> 
			    　</td>
			    <td class="tdp">制单人姓名：</td>
			   <td class="tdpright">${audit.authPersonName}　</td>
	  		</tr>
	  		<tr >
			    <td class="tdp">生产厂商：</td>
			    <td class="tdp">
			    	<script type="text/javascript">
				              		document.write(getItemValue('${list.YIELDLY}'))
				    </script>
			    　</td>
			    <td class="tdp">经销商代码：</td>
			    <td class="tdp">${list.NEW_DEALER_CODE }</td>
			    <td class="tdp">经销商名称：</td>
			    <td class="tdpright">${list.DEALER_NAME1 }</td>
	  		</tr>
	  		<tr >
			    <td class="tdp"   nowrap="nowrap">开票单位：</td>
			    <td class="tdp" >${list.KP_DEALER_NAME } </td>
			    <td class="tdp">结算时间起：</td>
			    <td class="tdp"><fmt:formatDate value='${list.START_DATE }' pattern='yyyy-MM-dd'/></td>
			    <td class="tdp">结算时间止：</td>
			    <td class="tdpright"><fmt:formatDate value='${list.END_DATE }' pattern='yyyy-MM-dd'/></td>
	 		 </tr>
	  		<tr >
			    
			    <td class="tdp" >总结算费用：</td>
			    <td class="tdp" >${list.BALANCE_AMOUNT }　</td>
			    <td class="tdp" >财务扣款：</td>
			    <td class="tdpright" >${list.FINANCIAL_DEDUCT }　</td>
	   			 <td colspan='2' class="tdpright">&nbsp;</td>
	  		</tr>
	  		<tr >
	   			 <td class="tdp"   >开票金额：</td>
	   			 <td class="tdpright" colspan="5" >　
	   			 	<script>
					  if(parseFloat(${list.NOTE_AMOUNT })<0)
						  document.write(0);
					  else
						  document.write(${list.NOTE_AMOUNT });
					</script>
	   			 </td>
	 		</tr>
	 		<tr >
	   			 <td class="tdp"   >备注：</td>
	   			 <td class="tdpright" colspan="5" >${list.REMARK }　</td>
	 		</tr>
	 		<tr >
	   			 <td class="tdp"   >财务备注：</td>
	   			 <td class="tdpright"  colspan="5" >${list.FUNANCIAL_REMARK }　</td>
	 		</tr>
      </table><br>
   		</td></tr>
 		<tr>
			    <td class="tdp" width="20px">行号</td>
			    <td class="tdp" width="30px">车系</td>
			    <td class="tdp" width="30px">售前工时费</td>
			    <td class="tdp" width="30px">售前材料费</td>
			    <td class="tdp" width="30px">售后工时费</td>
			    <td class="tdp" width="30px">售后材料费</td>
			    <td class="tdp" width="30px">保养数</td>
			    <td class="tdp" width="30px">保养费</td>
			    <td class="tdp" width="30px">保养工时费</td>
			    <td class="tdp" width="30px">保养材料费</td>
			    <td class="tdp" width="30px">服务活动次数</td>
			    <td class="tdp" width="30px">服务活动费用</td>
			    <td class="tdp" width="50px">售前索赔单数</td>
			    <td class="tdp" width="50px">售后索赔单数</td>
			    <td class="tdp" width="50px">费用合计</td>
		</tr>

	</thead>
 	<tbody>
	<c:set var="pageSize"  value="20" />
  <c:forEach var="dList" items="${detail}" varStatus="status">
	<tr style='${(status.count%pageSize==0) ? "page-break-after:always;":""}'>
	    <td class="tdp" >${status.index+1 }</td>
			    <td class="tdp" width="20px">${dList.SERIES_NAME}</td>
			    <td class="tdp" width="30px">${dList.BEFORE_LABOUR_AMOUNT}</td>
			    <td class="tdp" width="30px%">${dList.BEFORE_PART_AMOUNT} </td>
			    <td class="tdp" width="30px">${dList.AFTER_LABOUR_AMOUNT}</td>
			    <td class="tdp" width="30px">${dList.AFTER_PART_AMOUNT}</td>
			    <td class="tdp" width="30px">${dList.FREE_CLAIM_COUNT}</td>
			    <td class="tdp" width="30px">${dList.FREE_CLAIM_AMOUNT}</td>
			    <td class="tdp" width="30px">${dList.FREE_LABOUR_AMOUNT}</td>
			    <td class="tdp" width="30px">${dList.FREE_PART_AMOUNT}</td>
			    <td class="tdp" width="30px">${dList.SERVICE_CLAIM_COUNT}</td>
			    <td class="tdp" width="30px">${dList.SERVICE_FIXED_AMOUNT}</td>
			    <td class="tdp" width="50px">${dList.BEFORE_CLAIM_COUNT}</td>
			    <td class="tdp" width="50px">${dList.AFTER_CLAIM_COUNT}</td>
			    <td class="tdp" width="50px">${dList.TOTAL_AMOUNT }</td>
	 </tr>
  </c:forEach>
	</tbody>
	<tfoot style="display: table-footer-group;">
		<tr>
		  <td height="20" colspan="15"  style="tdp:1px solid #000000;">
		  <br/>
<!--		  <table width="100%" class="sign">-->
<!--				<tr>-->
<!--				<td></td>-->
<!--				</tr>-->
<!--			</table>-->
			<hr>
		  <table class="100%" align="center">
	           <tr>
<!--					<td width="10%" align="right">&nbsp;</td>-->
					<td width="40%" align="left">维修站签章:</td>
					
			  </tr>
          </table>
          </td>
		 </tr>
  </tfoot>
</table>
<br/>

<br/>

</form>
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
</script>    
<OBJECT classid="CLSID:8856F961-340A-11D0-A96B-00C04FD705A2" height=0 id=wb name=wb width=3></OBJECT>    
<div id="kpr" align="center">    
<input class="ipt" type=button name= button _print value="打印" onclick ="kpr.style.display='none';javascript :printit();">    
<input class="ipt" type=button name=button _setup value="打印页面设置" onclick =" javascript : printsetup();">    
<input class="ipt" type=button name=button_show value="打印预览" onclick="kpr.style.display='none';javascript:printpreview();">    
<input class="ipt" type=button name= button _fh value="关闭" onclick =" javascript:window.close();"> </td>    
</tr>   
</table>  
</html>