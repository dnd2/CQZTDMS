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

<%  
	TtAsWrApplicationExtPO tawep = (TtAsWrApplicationExtPO)request.getAttribute("tawep");
	String currentPartCode = "";
%>
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<title>索赔单打印</title>


	</head>
<body>
<div id="topContainer" style="" >
	<br/>
	<center><strong><font size="4">索赔申请单(<script type="text/javascript">document.write(getItemValue(<c:out value="${tawep.claimType }"/>));</script>)</font></strong></center>
	<br/>
	<form method="post" name="fm" id="fm">
	<input type="hidden" name="codeId" value="${codeId }"/>
	<table class="tabp2" width=750px;>
		<table class="tabp2" align="center">
			<!--<th colspan="6">
				<img class="nav" src="../../../img/subNav.gif" />y
				基本信息
			</th>-->
			<tr>
				<td class="tdp" nowrap>
						索赔单类型：
				</td>
				<td class="tdp" id="claimTypename">
				<script type="text/javascript">
				document.write(getItemValue('${tawep.claimType }'));
				</script>
				</td>
				<td class="tdp" nowrap>
						索赔单号：
				</td>
				<td class="tdp">
				${tawep.claimNo }
				</td>
				<td class="tdp" nowrap>
						制单日期：
				</td>
				<td class="tdp">
<!--				<script type="text/javascript">-->
<!--				document.write(DateUtil.Format("yyyy-MM-dd","${tawep.createDate }"));-->
<!--				</script>-->
				<!--现在替换成审核通过日期<fmt:formatDate value="${tawep.createDate }" pattern="yyyy-MM-dd"/>-->
				<fmt:formatDate value="${tawep.auditingDate }" pattern="yyyy-MM-dd"/>
				</td>
			</tr>
			<tr>
				<td class="tdp" nowrap>
						经销商代码：
				</td>
				<td class="tdp">
				${tawep.dealerCode }
				</td>
				<td class="tdp">
						经销商名称：
				</td>
				<td class="tdp">
				${tawep.dealerName }
				</td>
				<td class="tdp">
						生产厂家：
				</td>
				<td class="tdp">
				<script type="text/javascript">
				document.write(getItemValue('${tawep.yieldly }'));
				</script>
				</td>
			</tr>
			<tr>
				<td class="tdp">
						车型：
				</td>
				<td class="tdp">
				${tawep.modelCode }
				</td>
				<td class="tdp" nowrap>
						车型状态：
				</td>
				<td class="tdp">
				${tawep.packageCode }
				</td>
				<td class="tdp" nowrap>
						购车日期：
				</td>
				<td class="tdp">
<!--				<script type="text/javascript">-->
<!--				document.write(DateUtil.Format("yyyy-MM-dd","${tawep.guaranteeDate }"));-->
<!--				</script>-->
				 <c:if test="${tawep.claimType!=10661007 }">
				 	<fmt:formatDate value="${tawep.guaranteeDate }" pattern="yyyy-MM-dd"/>
				 </c:if>
				</td>
			</tr>
			<tr>
				<td class="tdp">
						VIN：
				</td>
				<td class="tdp">
				${tawep.vin }
				</td>
				<td class="tdp" nowrap>
						发动机号：
				</td>
				<td class="tdp">
				${tawep.engineNo }
				</td>
				<td class="tdp" nowrap>
						牌照号：
				</td>
				<td class="tdp">
				${tawep.licenseNo }
				</td>
			</tr>
			<tr>
				<td class="tdp" nowrap>
						送修人信息：
				</td>
				<td class="tdp">
				${tawep.customerName }
				</td>
				<td class="tdp" nowrap>
						用户电话：
				</td>
				<td class="tdp">
				<c:if test="${tawep.claimType!=10661007 }">
					${tawep.customerPhone }
				</c:if>
				</td>
				<td class="tdp" nowrap>
						用户地址：
				</td>
				<td class="tdp">
				<c:if test="${tawep.claimType!=10661007 }">
					${tawep.customerAddress }
					</c:if>
				</td>
			</tr>
			<tr>
				<td class="tdp" nowrap>
						维修日期：
				</td>
				<td class="tdp">
<!--				<script type="text/javascript">-->
<!--				document.write(DateUtil.Format("yyyy-MM-dd","${tawep.createDate}"));-->
<!--				</script>-->
				<fmt:formatDate value="${tawep.createDate}" pattern="yyyy-MM-dd"/>
				</td>
				<td class="tdp" nowrap>
						行驶里程：
				</td>
				<td class="tdp">
				${tawep.inMileage }
				</td>
				<td class="tdp" nowrap>
						维修次数：
				</td>
				<td class="tdp">
				${tawep.repairTimes }
				</td>
			</tr>
			<tr>
				<td class="tdp" nowrap>审核时间：</td>
				<td colspan="5" class="tdp">
					<fmt:formatDate value='${tawep.auditingDate}' pattern="yyyy-MM-dd"/>
				</td>
			</tr>
			<tr>
				<td class="tdp" nowrap>接待员：</td>
				<td colspan="5" class="tdp">
					${tawep.serviceAdvisor }
				</td>
			</tr>
		</table>
		
		<%if (tawep.getClaimType().intValue()==Constant.CLA_TYPE_06.intValue())  {%>
		<table class="tabp2"  align="center">
			<!--<th colspan="6">
				<img class="nav" src="../../../img/subNav.gif" />
				服务活动
			</th>-->
			<tr>
				<td class="tdp">
						服务活动代码：
				</td>
				<td class="tdp">
				${tawep.campaignCode }
				</td>
				<td class="tdp">
						服务活动名称：
				</td>
				<td class="tdp">
				${tawep.campaignName }
				</td>
				<td class="tdp">
						总申报费用：
				</td>
				<td class="tdpright">
				${tawep.repairTotal}
				</td>
			</tr>
			<tr>
				<td class="tdp" style="display:none">
					服务活动内容：
				</td>
				<td class="tdpright" style="display:none">
				<input type="text" />
				</td>
			</tr>
		</table>
		<%} %>
		
		<%if (tawep.getClaimType().intValue()==Constant.CLA_TYPE_02.intValue())  {%>
		<table class="tabp2"  align="center">
			<!-- <th colspan="6">
				<img class="nav" src="../../../img/subNav.gif" />
				免费保养
			</th>-->
			<tr>
				<td class="tdp">
						保养次数：
				</td>
				<td class="tdp">
				${tawep.freeTimes}
				</td>
				<td class="tdp">
						总申报费用：
				</td>
				<td colspan="3" class="tdp">
				${tawep.repairTotal}
				</td>
			</tr>
			<tr>
				<td  class="tdp">
					授权说明：
				</td>
				<td colspan="3" class="tdp">
				<c:out value="${tawep.foreAuthContent }"/> 
				</td>
				<td  class="tdp">
					授权人：
				</td>
				<td  class="tdp">
				${tawep.foreAuthPerson }
				</td>
			</tr>
		</table>
		<%} %>
		
		<%if (tawep.getClaimType().intValue()==Constant.CLA_TYPE_09.intValue())  {%>
		<table class="tabp2"  align="center">
			<!--<th colspan="6">
				<img class="nav" src="../../../img/subNav.gif" />
				外出维修
			</th>-->
			<tr>
				<td class="tdp">
						外出开始时间：
				</td>
				<td class="tdp">
<!--				<script type="text/javascript">-->
<!--				document.write(DateUtil.Format("yyyy-MM-dd hh:mm:ss","${tawep.startTime}"));-->
<!--				<fmt:formatDate value="${tawep.startTime}" pattern="yyyy-MM-dd hh:mm:ss"/>-->
<!--				</script>-->
					<fmt:formatDate value="${tawep.startTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td class="tdp">
						外出结束时间：
				</td>
				<td class="tdp">
<!--				<script type="text/javascript">-->
<!--				document.write(DateUtil.Format("yyyy-MM-dd hh:mm:ss","${tawep.endTime}"));-->
<!--				</script>-->
				<fmt:formatDate value="${tawep.endTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td class="tdp">
						派车车牌号：
				</td>
				<td class="tdpright">
				${tawep.outLicenseno}
				</td>
			</tr>
			<tr>
				<td class="tdp">
						出差人：
				</td>
				<td class="tdp">
				${tawep.outPerson}
				</td>
				<td class="tdp">
						出差目的地：
				</td>
				<td class="tdp">
				${tawep.outSite}
				</td>
				<td class="tdp">
						外出总里程：
				</td>
				<td class="tdpright">
				${tawep.outMileages}
				</td>
			</tr>
			<tr>
				<td class="tdp">
						${QT004}:
				</td>
				<td class="tdp">
				${tawep.roomCharge }
				</td>
				<td class="tdp">
						${QT003}:
				</td>
				<td class="tdp">
				${tawep.eatupFee }
				</td>
				<td class="tdp">
						${QT002}:
				</td>
				<td class="tdpright">
				${tawep.transportation }
				</td>
			</tr>
			<tr>
				<td class="tdp">
						${QT001}:
				</td>
				<td class="tdp">
				${tawep.faxFee }
				</td>
				<td class="tdp">
						${QT005}:
				</td>
				<td class="tdp">
				${tawep.subsidiesFee }
				</td>
				<td class="tdp">
						总外出费用：
				</td>
				<td class="tdpright">
				${tawep.roomCharge+tawep.eatupFee+tawep.transportation+tawep.faxFee+tawep.subsidiesFee}
				</td>
			</tr>
			<tr>
				<td class="tdp">
						总申报材料费：
				</td>
				<td class="tdp">
				${tawep.partAmount}
				</td>
				<td class="tdp">
						总申报工时费：
				</td>
				<td class="tdp">
				${tawep.labourAmount}
				</td>
				<td class="tdp">
						总申报费用：
				</td>
				<td class="tdpright">
				${tawep.repairTotal}
				</td>
			</tr>
			<tr>
			<td  class="tdp">
				故障现象描述：
			</td>
			<td colspan="5" class="tdpright">
					${tawep.troubleDesc}
			</td>			
			</tr>
			<tr>
			<td  class="tdp">
				故障原因：
			</td>
			<td colspan="5" class="tdpright">
					${tawep.troubleReason}
			</td>			
			</tr>
			<tr>
			<td  class="tdp">
				维修措施：
			</td>
			<td colspan="5" class="tdpright">
					${tawep.repairMethod}
			</td>			
			</tr>
			<tr>
				<td  class="tdp">
					外出申请备注：
				</td>
				<td colspan="5" class="tdpright">
					${tawep.remark}
				</td>
			</tr>
			<tr>
				<td  class="tdp">
					授权说明：
				</td>
				<td colspan="5"  class="tdpright">
				<c:out value="${tawep.authContent }"/>
				</td>
			</tr>
			<tr>
			<table class="tabp2"  align="center">
			 <tr>
		 	<td></td>
			 	<td class="tdp" nowrap><b>行号</b></td>
	      	  	<td class="tdp" width="70px" nowrap><b>新件代码</b></td>
		      	<td class="tdp" width="30px" nowrap><b>新件名称</b></td>
		      	<td class="tdp" width="30px" nowrap><b>换件数量</b></td>
		      	<td class="tdp" width="50px" nowrap><b>作业代码</b></td>
		      	<td class="tdp" width="50px" nowrap><b>工时名称</b></td>
		      	
		      	<c:if test="${codeId==80081001}">
					<td class="tdp" width="90px" nowrap><b>顾客问题</b></td>
				</c:if>
				
		      	<td class="tdp" width="50px" nowrap><b>旧件名称</b></td>
		      	<td class="tdp" nowrap><b>配件费</b></td>
		      	<td class="tdp" nowrap><b>工时数</b></td>
		      	<td class="tdp" nowrap><b>工时费</b></td>
		      	<td class="tdpright" width="80px" nowrap><b>申请备注</b></td>
		  	 </tr>
		  	 <c:set var="pageSize"  value="15" />
		  	 <c:if test="${myList!=null}">
			 <c:forEach var="myList1" items="${myList}" varStatus="s" >
			 	<tr style='${(s.count%pageSize==0) ? "page-break-after:always;":""}' >
			 <td><input type="hidden" id="PART_ID" class="partCode" value="${myList1.partId}"/>
			 <input type="hidden" id="FIRST_PART"  value="${myList1.firstPart}"/>
			 <input type="hidden" id="LABOUR_AMOUNT" value="${myList1.labourAmount}"/>
			 <input type="hidden"  value="${myList1.labourHours}"/></td>
			 <td class="tdp"><c:out value="${s.index+1}"></c:out></td>
			 <td class="tdp">
			 <c:out value="${myList1.partCode}"></c:out></td>
		 	 <td class="tdp"><c:out value="${myList1.partName}"></c:out></td>
		  	 <td class="tdp"><fmt:formatNumber value="${myList1.quantity}" pattern="##"/></td>
		  	 <td class="tdp"><c:out value="${myList1.wrLabourcode}"></c:out></td>
		  	 <td class="tdp"><c:out value="${myList1.wrLabourname}"></c:out></td>
		  	 
		  	 <c:if test="${codeId==80081001}">
		  	 	<td class="tdp"><c:out value="${myList1.troubleType}"></c:out></td>
			 </c:if>
		  	 
		  	 <td class="tdp"><c:out value="${myList1.downPartName}"></c:out></td>
		  	 <td class="tdp"><c:out value="${myList1.amount}"></c:out></td>
		  	 <td class="tdp"><c:out value="${myList1.labourHours}"></c:out></td>
		  	 <td class="tdp"><c:out value="${myList1.labourAmount}"></c:out></td>
		  	 <td class="tdpright"><c:out value="${myList1.remark}"></c:out></td>
		  	 </tr>
			 </c:forEach>
			 </c:if>
			 </table>
			</tr>
		</table>
		<%} %>
		
		<%if ((tawep.getClaimType().intValue()==Constant.CLA_TYPE_01.intValue())||(tawep.getClaimType().intValue()==Constant.CLA_TYPE_07.intValue()))  {%>
		<table class="tabp2"  align="center">
			<!--<th colspan="6">
				<img class="nav" src="../../../img/subNav.gif" />
				一般维修
			</th>-->
			<tr>
			<td class="tdp">
				故障现象描述：
			</td>
			<td colspan="5"  class="tdp">
				${tawep.troubleDesc}
			</td>
			</tr>
			<tr>
			<td  class="tdp">
				故障原因：
			</td>
			<td colspan="5" class="tdpright">
					${tawep.troubleReason}
			</td>			
			</tr>
			<tr>
			<td  class="tdp">
				维修措施：
			</td>
			<td colspan="5" class="tdpright">
					${tawep.repairMethod}
			</td>			
			</tr>
			<tr>
				<td  class="tdp">
					备注：
				</td>
				<td colspan="5"  class="tdp">
					${tawep.remark}
				</td>
			</tr>
			<tr>
				<td  class="tdp" >
					授权说明：
				</td>
				<td  class="tdp" colspan="5">
				<c:out value="${tawep.authContent }"/>
				</td>
			</tr>
			<tr>
				<td class="tdp">
						授权人：
				</td>
				<td class="tdp">
					${tawep.authPerson }
				</td>
				<td class="tdp"">
						总申报工时数：
				</td>
				<td class="tdp">
				${tawep.labourHours}
				</td>
				<td class="tdp">
						总申报材料费：
				</td>
				<td  class="tdpright">
				${tawep.partAmount}
				</td>
			</tr>
			<tr>
				<td class="tdp">
						总申报费用：
				</td>
				<td class="tdp">
				${tawep.repairTotal}
				</td>
				<td class="tdp">
						追加工时数：
				</td>
				<td  class="tdp">
				${tawep.appendlabourNum}
				</td>
				<td class="tdp">
						追加工时费：
				</td>
				<td  class="tdpright">
				${tawep.appendlabourAmount}
				</td>
			</tr>
			<tr>
			<table class="tabp2"  align="center">
			 <tr>
			 	<td></td>
			 	<td class="tdp" nowrap><b>行号</b></td>
	      	  	<td class="tdp" width="35px" nowrap><b>新件代码</b></td>
		      	<td class="tdp" width="25px" nowrap><b>新件名称</b></td>
		      	<td class="tdp" width="30px" nowrap><b>换件数量</b></td>
		      	<td class="tdp" width="50px" nowrap><b>作业代码</b></td>
		      	<td class="tdp" width="60px" nowrap><b>工时名称</b></td>
		      	
		      	<c:if test="${codeId==80081001}">
					<td class="tdp" nowrap width="50px"><b>顾客问题</b></td>
				</c:if>
				
		      	<td class="tdp" width="50px" nowrap><b>旧件名称</b></td>
		      	<td class="tdp" nowrap><b>配件费</b></td>
		      	<td class="tdp" nowrap><b>工时数</b></td>
		      	<td class="tdp" nowrap><b>工时费</b></td>
		      	<td class="tdpright" width="55px" nowrap><b>申请备注</b></td>
		  	 </tr>
		  	 <c:set var="pageSize"  value="15" />
		  	 <c:if test="${myList!=null}">
			 <c:forEach var="myList1" items="${myList}" varStatus="status">
			 <tr style='${(status.count%pageSize==0) ? "page-break-after:always;":""}' >
			 <td><input type="hidden" id="PART_ID" class="partCode" value="${myList1.partId}"/>
			 <input type="hidden" id="FIRST_PART"  value="${myList1.firstPart}"/>
			 <input type="hidden" id="LABOUR_AMOUNT" value="${myList1.labourAmount}"/>
			 <input type="hidden"  value="${myList1.labourHours}"/></td>
			 <td class="tdp"><c:out value="${status.index+1}"></c:out></td>
			 <td class="tdp">
			 <c:out value="${myList1.partCode}"></c:out></td>
		 	 <td class="tdp"><c:out value="${myList1.partName}"></c:out></td>
		  	 <td class="tdp"><fmt:formatNumber value="${myList1.quantity}" pattern="##"/></td>
		  	 <td class="tdp"><c:out value="${myList1.wrLabourcode}"></c:out></td>
		  	 <td class="tdp"><c:out value="${myList1.wrLabourname}"></c:out></td>
		  	 
		  	 <c:if test="${codeId==80081001}">
		  	 	<td class="tdp"><c:out value="${myList1.troubleType}"></c:out></td>
			 </c:if>
			 
		  	 <td class="tdp"><c:out value="${myList1.downPartName}"></c:out></td>
		  	 <td class="tdp"><c:out value="${myList1.amount}"></c:out></td>
		  	 <td class="tdp"><c:out value="${myList1.labourHours}"></c:out></td>
		  	 <td class="tdp" id="LABOUR_AMOUNT"></td>
		  	 <td class="tdpright"><c:out value="${myList1.remark}"></c:out></td>
		  	 </tr>
			 </c:forEach>
			 </c:if>
			 </table>
			 </tr>
		</table>
		<%} %>
		</table>
</form>
<table class="tabp2" align="center">
		<tfoot style="display: table-footer-group;">
		<tr>
		  <td align="center" height="20" colspan="11"  style="tdp:1px solid #000000;">
		  <br/>

		  <table class="100%" align="center">
	           <tr>
					<td width="10%" align="left">服务站盖章:</td>
					<td width="15%" align="right">&nbsp;</td>
					<td width="15%" align="right">&nbsp;</td>
					<td width="20%" align="left" colspan="4">客户签字:</td>
			  </tr>
          </table>
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
	  $$('.partCode').each(function(e){
		    var partCode = e.value ;
		    var firstPart = e.next().value;
		    var labourAmount = e.next(1).value
		    var labourHour = e.next(2).value
			if(partCode!=firstPart){
				if($('codeId').value==80081001){
					e.up().next(10).innerText = 0;
					e.up().next(9).innerText = 0;
				}else{
					e.up().next(9).innerText = 0;
					e.up().next(8).innerText = 0;
				}
				
			}
			else{
				if($('codeId').value==80081001){
					e.up().next(10).innerText = labourAmount;
					e.up().next(9).innerText = labourHour;
				}else{
					e.up().next(9).innerText = labourAmount;
					e.up().next(8).innerText = labourHour;
				}
			}

		  });
</script> 
</div>
<script type="text/javascript">
	$('topContainer').style.height=document.viewport.getHeight();
</script>
</body>

</html>

