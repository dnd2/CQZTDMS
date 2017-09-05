<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="com.infodms.dms.common.Constant"%>
<%@page import="java.util.Map"%>
<%@taglib uri="/jstl/cout" prefix="c"%>
<%@taglib uri="/jstl/fmt" prefix="fmt" %>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<title>汇总结算单打印</title>

	</head>
<body>
	<br/>
	<center><strong><font size="6">汇总结算单</font></strong></center>
	<br/><br/>
		<table class="tabp2" align="center">
			<!--<th colspan="6">
				<img class="nav" src="../../../img/subNav.gif" />
				基本信息
			</th>-->
			<tr>
				<td class="tdp">
						结算单号：
				</td>
				<td class="tdp">
				${tawep.BALANCE_NO }
				</td>
				<td class="tdp">
						制单日期：
				</td>
				<td class="tdp">
				${tawep.CREATEDATE }
				</td>
				<td class="tdp">
						生产厂家：
				</td>
				<td class="tdpright">
				<script type="text/javascript">
				document.write(getItemValue('${tawep.YIELDLY }'));
				</script>
				</td>
			</tr>
			<tr>
				<td class="tdp">
						开票单位：
				</td>
				<td class="tdp">
				${tawep.INVOICE_MAKER }
				</td>
				<td class="tdp">
						经销商代码：
				</td>
				<td class="tdp">
				${tawep.DEALER_CODE }
				</td>
				<td class="tdp">
						经销商名称：
				</td>
				<td class="tdpright">
				${tawep.DEALER_NAME }
				</td>
			</tr>
			<tr>
				<td class="tdp">
						省份：
				</td>
				<td class="tdp">
				${tawep.PROVINCE_NAME }
				</td>
				<td class="tdp">
						维修日期起：
				</td>
				<td class="tdp">
				<fmt:formatDate value="${tawep.START_DATE }" pattern="yyyy-MM-dd"/>
				</td>
				<td class="tdp">
						维修日期止：
				</td>
				<td class="tdpright">
				<fmt:formatDate value="${tawep.END_DATE }" pattern="yyyy-MM-dd"/>
				</td>
			</tr>
			<tr>
				<td class="tdp">
						工时费：
				</td>
				<td class="tdp">
				${tawep.LABOUR_AMOUNT }
				</td>
				<td class="tdp">
						材料费：
				</td>
				<td class="tdp">
				${tawep.PART_AMOUNT }
				</td>
				<td class="tdp">
						保养费：
				</td>
				<td class="tdpright">
				${tawep.FREE_AMOUNT }
				</td>
			</tr>
			<tr>
				<td class="tdp">
						服务活动费：
				</td>
				<td class="tdp">
				${tawep.SERVICE_AMOUNT }
				</td>
				<td class="tdp">
						救急费：
				</td>
				<td class="tdp">
				${tawep.OTHER_AMOUNT }
				</td>
				<td class="tdp">
						运费：
				</td>
				<td class="tdpright">
				${tawep.RETURN_AMOUNT }
				</td>
			</tr>
			<tr>
				<td class="tdp">
						特殊费用：
				</td>
				<td class="tdp">
				${tawep.DECLARE_SUM1 }
				</td>
				<td class="tdp">
						费用合计：
				</td>
				<td class="tdp">
				${tawep.BALANCE_AMOUNT }
				</td>
				<td class="tdp">
						
				</td>
				<td class="tdpright">
				
				</td>
			</tr>
			<tr>
			<td  class="tdp">
			备注：
			</td>
			<td  colspan="6" class="tdpright">
			${map.REMARK}
			</td>
			</tr>
			<tr>
				<td class="tdp">
						索赔单总数：
				</td>
				<td class="tdp">
				${tawep.CLAIM_COUNT }
				</td>
				<td class="tdp">
						收单人：
				</td>
				<td class="tdp">
					<!--<c:if test="${tawep.AUTH_STATUS>11861002}">${tawep.AUTH_PERSON_NAME }</c:if>-->
				</td>
				<td class="tdp">
						收单时间：
				</td>
				<td class="tdpright">
					<!--<c:if test="${tawep.AUTH_STATUS>11861002}"><fmt:formatDate value="${tawep.AUTH_TIME }" pattern="yyyy-MM-dd"/></c:if>-->
				</td>
			</tr>
			<tr>
			<td  class="tdp">
			三包员联系电话：
			</td>
			<td  colspan="6" class="tdp">
			${tawep.CLAIMER_TEL }
			</td>
			</tr>
		</table>
		
	
		<table class="tabp2"  align="center">
			<tr>
			<td colspan="6">
			<table class="tabp2"  align="center">
			 <tr>
			 	<td class="tdp" width="4%" ><b>行号</b></td>
	      	  	<td class="tdp" width="7%" ><b>车系</b></td>
		      	<td class="tdp" width="8%" ><b>售前工时费</b></td>
		      	<td class="tdp" width="8%" ><b>售前材料费</b></td>
		      	<td class="tdp" width="8%" ><b>售后工时费</b></td>
		      	<td class="tdp" width="8%" ><b>售后材料费</b></td>
		      	<td class="tdp" width="6%" ><b>保养数</b></td>
		      	<td class="tdp" width="6%" ><b>保养费</b></td>
		      	<td class="tdp" width="8%" ><b>保养工时费</b></td>
		      	<td class="tdp" width="8%" ><b>保养材料费</b></td>
		      	<td class="tdp" width="8%" ><b>服务活动次数</b></td>
		      	<td class="tdp" width="8%" ><b>服务活动费用</b></td>
		      	<td class="tdp" width="8%" ><b>售前索赔单数</b></td>
		      	<td class="tdpright" width="8%" ><b>售后索赔单数</b></td>
		  	 </tr>
		  	 <c:set var="pageSize"  value="2" />
		  	 <c:if test="${myList!=null}">
			 <c:forEach var="myList1" items="${myList}" varStatus="s" >
			 <tr style='${(s.count%pageSize==0)}'>
			 <td class="tdp" ><c:out value="${s.index+1}"></c:out></td>
			 <td class="tdp" ><c:out value="${myList1.SERIES_NAME}"></c:out></td>
			 <td class="tdp" ><c:out value="${myList1.BEFORE_LABOUR_AMOUNT}"></c:out></td>
		 	 <td class="tdp" ><c:out value="${myList1.BEFORE_PART_AMOUNT}"></c:out></td>
		  	 <td class="tdp" ><c:out value="${myList1.AFTER_LABOUR_AMOUNT}"></c:out></td>
		  	 <td class="tdp" ><c:out value="${myList1.AFTER_PART_AMOUNT}"></c:out></td>
		  	 <td class="tdp" ><c:out value="${myList1.FREE_CLAIM_COUNT}"></c:out></td>
		  	 <td class="tdp" ><c:out value="${myList1.FREE_CLAIM_AMOUNT}"></c:out></td>
		  	 <td class="tdp" ><c:out value="${myList1.FREE_LABOUR_AMOUNT}"></c:out></td>
		  	 <td class="tdp" ><c:out value="${myList1.FREE_PART_AMOUNT}"></c:out></td>
		  	 <td class="tdp" ><c:out value="${myList1.SERVICE_CLAIM_COUNT}"></c:out></td>
		  	 <td class="tdp" ><c:out value="${myList1.SERVICE_AMOUNT}"></c:out></td>
		  	 <td class="tdp" ><c:out value="${myList1.BEFORE_CLAIM_COUNT}"></c:out></td>
		  	 <td class="tdp" ><c:out value="${myList1.AFTER_CLAIM_COUNT}"></c:out></td>
		  	 </tr>
			 </c:forEach>
			 </c:if>
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

