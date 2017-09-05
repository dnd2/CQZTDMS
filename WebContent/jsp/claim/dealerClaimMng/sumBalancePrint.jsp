<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="com.infodms.dms.common.Constant"%>
<%@page import="com.infodms.dms.po.TtAsWrGamefeePO"%>
<%@page import="java.util.List"%>
<%@page import="com.infodms.dms.po.TcCodePO"%>
<%@page import="com.infodms.dms.po.TtAsWrApplicationExtPO"%>
<%@page import="com.infodms.dms.util.CommonUtils"%>
<%@taglib uri="/jstl/cout" prefix="c"%>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<%  
	TtAsWrApplicationExtPO tawep = (TtAsWrApplicationExtPO)request.getAttribute("tawep");
%>
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<title>汇总结算单打印</title>

	</head>
<body>
	<br/>
	<center><strong><font size="6">汇总结算单</font></strong></center>
	<br/><br/>
		<table class="tabp" align="center">
			<!--<th colspan="6">
				<img class="nav" src="../../../img/subNav.gif" />
				基本信息
			</th>-->
			<tr>
				<td class="tdp">
						结算单号：
				</td>
				<td class="tdp">
				<script type="text/javascript">
				document.write(getItemValue('${tawep.claimType }'));
				</script>
				</td>
				<td class="tdp">
						制单日期：
				</td>
				<td class="tdp">
				${tawep.claimNo }
				</td>
				<td class="tdp">
						生产厂家：
				</td>
				<td class="tdp">
				<script type="text/javascript">
				document.write(DateUtil.Format("yyyy-MM-dd","${tawep.createDate }"));
				</script>
				</td>
			</tr>
			<tr>
				<td class="tdp">
						开票单位：
				</td>
				<td class="tdp">
				${tawep.dealerCode }
				</td>
				<td class="tdp">
						经销商代码：
				</td>
				<td class="tdp">
				${tawep.dealerName }
				</td>
				<td class="tdp">
						经销商名称：
				</td>
				<td class="tdp">
				<script type="text/javascript">
				document.write(getItemValue('${tawep.yieldly }'));
				</script>
				</td>
			</tr>
			<tr>
				<td class="tdp">
						省份：
				</td>
				<td class="tdp">
				${tawep.brandCode }
				</td>
				<td class="tdp">
						维修日期起：
				</td>
				<td class="tdp">
				${tawep.packageName }
				</td>
				<td class="tdp">
						维修日期止：
				</td>
				<td class="tdp">
				<script type="text/javascript">
				document.write(DateUtil.Format("yyyy-MM-dd","${tawep.guaranteeDate }"));
				</script>
				</td>
			</tr>
			<tr>
				<td class="tdp">
						工时费：
				</td>
				<td class="tdp">
				${tawep.vin }
				</td>
				<td class="tdp">
						材料费：
				</td>
				<td class="tdp">
				${tawep.engineNo }
				</td>
				<td class="tdp">
						保养费：
				</td>
				<td class="tdp">
				${tawep.licenseNo }
				</td>
			</tr>
			<tr>
				<td class="tdp">
						服务活动费：
				</td>
				<td class="tdp">
				${tawep.customerName }
				</td>
				<td class="tdp">
						救急费：
				</td>
				<td class="tdp">
				${tawep.customerPhone }
				</td>
				<td class="tdp">
						运费：
				</td>
				<td class="tdp">
				${tawep.customerAddress }
				</td>
			</tr>
			<tr>
				<td class="tdp">
						特殊费用：
				</td>
				<td class="tdp">
				<script type="text/javascript">
				document.write(DateUtil.Format("yyyy-MM-dd","${tawep.createDate}"));
				</script>
				</td>
				<td class="tdp">
						费用合计：
				</td>
				<td class="tdp">
				${tawep.inMileage }
				</td>
				<td class="tdp">
						
				</td>
				<td class="tdp">
				
				</td>
			</tr>
			<tr>
			<td  class="tdp">
			备注：
			</td>
			<td  colspan="6" class="tdp">
			
			</td>
			</tr>
			<tr>
				<td class="tdp">
						索赔单总数：
				</td>
				<td class="tdp">
				${tawep.customerName }
				</td>
				<td class="tdp">
						收单人：
				</td>
				<td class="tdp">
				${tawep.customerPhone }
				</td>
				<td class="tdp">
						收单时间：
				</td>
				<td class="tdp">
				${tawep.customerAddress }
				</td>
			</tr>
			<tr>
			<td  class="tdp">
			三包员联系电话：
			</td>
			<td  colspan="6" class="tdp">
			
			</td>
			</tr>
		</table>
		
	
		<table class="tabp"  align="center">
			<tr>
			<td colspan="6">
			<table class="tabp"  align="center">
			 <tr>
			 	<td class="tdp" width="4%" ><b>行号</b></td>
	      	  	<td class="tdp" width="7%" ><b>车系</b></td>
		      	<td class="tdp" width="9%" ><b>售前工时费</b></td>
		      	<td class="tdp" width="9%" ><b>售前材料费</b></td>
		      	<td class="tdp" width="9%" ><b>售后工时费</b></td>
		      	<td class="tdp" width="9%" ><b>售后材料费</b></td>
		      	<td class="tdp" width="6%" ><b>保养数</b></td>
		      	<td class="tdp" width="6%" ><b>保养费</b></td>
		      	<td class="tdp" width="9%" ><b>保养工时费</b></td>
		      	<td class="tdp" width="9%" ><b>保养材料费</b></td>
		      	<td class="tdp" width="9%" ><b>服务活动次数</b></td>
		      	<td class="tdp" width="9%" ><b>服务活动费用</b></td>
		      	<td class="tdp" width="9%" ><b>售前索赔单数</b></td>
		      	<td class="tdp" width="9%" ><b>售后索赔单数</b></td>
		  	 </tr>
		  	 <c:set var="pageSize"  value="2" />
			 <c:forEach var="myList1" items="${myList}" varStatus="s" >
			 <tr style='${(s.count%pageSize==0) ? "page-break-after:always;":""}'>
			 <td class="tdp" ><c:out value="${s.index+1}"></c:out></td>
			 <td class="tdp" ><c:out value="${myList1.partCode}"></c:out></td>
		 	 <td class="tdp" ><c:out value="${myList1.partName}"></c:out></td>
		  	 <td class="tdp" ><c:out value="${myList1.quantity}"></c:out></td>
		  	 <td class="tdp" ><c:out value="${myList1.wrLabourcode}"></c:out></td>
		  	 <td class="tdp" ><c:out value="${myList1.wrLabourname}"></c:out></td>
		  	 <td class="tdp" ><c:out value="${myList1.downPartName}"></c:out></td>
		  	 <td class="tdp" ><c:out value="${myList1.amount}"></c:out></td>
		  	 <td class="tdp" ><c:out value="${myList1.labourHours}"></c:out></td>
		  	 <td class="tdp" ><c:out value="${myList1.labourAmount}"></c:out></td>
		  	 <td class="tdp" ><c:out value="${myList1.remark}"></c:out></td>
		  	 <td class="tdp" ><c:out value="${myList1.remark}"></c:out></td>
		  	 <td class="tdp" ><c:out value="${myList1.remark}"></c:out></td>
		  	 <td class="tdp" ><c:out value="${myList1.remark}"></c:out></td>
		  	 </tr>
			 </c:forEach>
			 </table>
			 </td>
			</tr>
		</table>

</body>
<br/>
<br/>
<br/>
<table width="100%" cellpadding="1" align="center" onmouseover="kpr.style.display='';" >   
	<tr>    
		<td width="100%" height="25" colspan="3">   
		<object classid="CLSID:8856F961-340A-11D0-A96B-00C04FD705A2" height="0" id="wb" name="wb" width="3"></object>
			<div id="kpr" align="center">    
				<input class="ipt" type="button" value="打印" onclick="kpr.style.display='none';javascript:printit();"/>    
				<input class="ipt" type="button" value="打印页面设置" onclick="javascript:printsetup();" />    
				<input class="ipt" type="button" value="打印预览" onclick="kpr.style.display='none';javascript:printpreview();"/>    
			</div>
		</td>
	</tr>     
</table> 
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
</html>

