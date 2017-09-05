<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="com.infodms.dms.common.Constant"%>
<%@page import="com.infodms.dms.po.TtAsWrGamefeePO"%>
<%@page import="java.util.List"%>
<%@page import="com.infodms.dms.po.TcCodePO"%>
<%@page import="com.infodms.dms.po.TtAsWrApplicationExtPO"%>
<%@taglib uri="/jstl/cout" prefix="c"%>
<%@taglib uri="/jstl/fmt" prefix="fmt"%>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<title>索赔单打印</title>
	</head>
<body>
<div id="topContainer" style="" >
	<br/>
	<center><strong><font size="4">经销商抽查明细</font></strong></center>
	<br/>
	<form method="post" name="fm" id="fm">
	<table class="tabp2" width=750px;>
		<table class="tabp2" align="center">
			<tr>
				<td class="tdp" nowrap>抽单编号：</td>
				<td class="tdp">${list.CHECK_NO }</td>
				<td class="tdp">抽单日期：</td>
				<td class="tdp"><fmt:formatDate value="${list.CHECK_DATE }" pattern="yyyy-MM-dd"/></td>
				<td class="tdp" nowrap>抽单数量：</td>
				<td class="tdp">${list.CHECK_COUNT }</td><!-- <fmt:formatDate value="${tawep.auditingDate }" pattern="yyyy-MM-dd"/> -->
			</tr>
			<tr>
				<td class="tdp">开票号：</td>
				<td class="tdp">${list.BALANCE_NO }</td>
				<td class="tdp">结算时间段：</td>
				<td class="tdp"><fmt:formatDate value="${list.START_DATE }" pattern="yyyy-MM-dd"/>至<fmt:formatDate value="${list.END_DATE }" pattern="yyyy-MM-dd"/></td>
				<td class="tdp">基地：</td>
				<td class="tdp">
					<script type="text/javascript">
						document.write(getItemValue('${list.YIELDLY }'));
					</script>
				</td>
			</tr>
			<tr>
				<td class="tdp" nowrap>经销商代码：</td>
				<td class="tdp">${list.DEALER_CODE }</td><!-- <script type="text/javascript">document.write(getItemValue('${tawep.claimType }'));</script> -->
				<td class="tdp" nowrap>经销商名称：</td>
				<td class="tdp">${list.DEALER_NAME }</td>
				<td class="tdp"></td>
				<td class="tdp"></td>
			</tr>
			<tr>
				<td class="tdp">分销中心：</td>
				<td class="tdp">${list.ROOT_ORG_NAME }</td>
				<td class="tdp">省份：</td>
				<td class="tdp">${list.REGION_NAME }</td>
				<td class="tdp"></td>
				<td class="tdp"></td>
			</tr>
		</table>
		<table class="tabp2"  align="center">
			<tr>
			<table class="tabp2"  align="center">
			 <tr>
			 	<!-- <td></td> -->
			 	<td class="tdp"><b>序号</b></td>
	      	  	<!-- <td class="tdp"><b>生产基地</b></td> -->
		      	<td class="tdp"><b>索赔类型</b></td>
		      	<td class="tdp"><b>索赔单号</b></td>
		      	<td class="tdp"><b>VIN</b></td>
		      	<td class="tdp"><b>车型配置</b></td>
		      	<td class="tdp"><b>金额</b></td>
		  	 </tr>
		  	 <c:set var="pageSize"  value="15" />
		  	 <c:if test="${detail!=null}">
			 <c:forEach var="detail" items="${detail}" varStatus="status">
			 <tr style='${(status.count%pageSize==0) ? "page-break-after:always;":""}' >
			 <!-- <td>
				 <input type="hidden" id="PART_ID" class="partCode" value="${detail.partId}"/>
				 <input type="hidden" id="FIRST_PART"  value="${detail.firstPart}"/>
				 <input type="hidden" id="LABOUR_AMOUNT" value="${detail.labourAmount}"/>
				 <input type="hidden"  value="${detail.labourHours}"/>
			 </td> -->
			 <td class="tdp"><c:out value="${status.index+1}"></c:out></td>
			 <!-- <td class="tdp"><script type="text/javascript">document.write(getItemValue('${detail.YIELDLY }'));</script></td> -->
		 	 <td class="tdp"><script type="text/javascript">document.write(getItemValue('${detail.CLAIM_TYPE }'));</script></td>
		 	 <td class="tdp"><c:out value="${detail.CLAIM_NO}"></c:out></td>
		 	 <td class="tdp"><c:out value="${detail.VIN}"></c:out></td>
		 	 <td class="tdp"><c:out value="${detail.MATERIAL_CODE}"></c:out></td>
		  	 <td class="tdp"><fmt:formatNumber value="${detail.BALANCE_AMOUNT}" pattern="##"/></td>
		  	 </tr>
			 </c:forEach>
			 </c:if>
			 </table>
			 </tr>
		</table>
		</table>
</form>
<table class="tabp2" align="center">
		<tfoot style="display: table-footer-group;">
		<tr>
		  <td align="center" height="20" colspan="11"  style="tdp:1px solid #000000;">
		  <br/>
          </td>
		 </tr>
  		</tfoot>
  		</table>
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
	function printsetup(){       
		wb.execwb(8,1);    // 打印页面设置 
	}    
	function printpreview(){    
		wb.execwb(7,1);   // 打印页面预览       
	}      
	function printit()    {    
		if(confirm('确定打印吗？')){    
			wb.execwb(6,6)    
		}    
	} 
	  $$('.partCode').each(function(e){
		    var partCode = e.value ;
		    var firstPart = e.next().value;
		    var labourAmount = e.next(1).value
		    var labourHour = e.next(2).value
			if(partCode!=firstPart){
				e.up().next(9).innerText = 0;
				e.up().next(8).innerText = 0;
			}
			else{
				e.up().next(9).innerText = labourAmount;
				e.up().next(8).innerText = labourHour;
			}
		  });
</script> 
</div>
<script type="text/javascript">
	$('topContainer').style.height=document.viewport.getHeight();
</script>
</body>

</html>

