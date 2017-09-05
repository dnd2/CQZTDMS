<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="com.infodms.dms.common.Constant"%>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<%@page import="java.util.List"%>
<%
	String contextPath = request.getContextPath();
%>
<%@taglib uri="/jstl/cout" prefix="c" %>
<%@ taglib uri="/jstl/fmt" prefix="fmt" %>       
<head>
<script type="text/javascript">
function doInit(){
   	loadcalendar();  //初始化时间控件
}
function feeType()
{
	if('0' == '${feettype}')
	{
		document.getElementById('feetable').style.display=''; 
		document.getElementById('fetable').style.display=''; 
	}
	if('1' == '${jude}')
	{
		document.getElementById('judeTable').style.display='';
	}
}
</script>
<TITLE>售后服务管理</TITLE>

</HEAD>
<BODY onload="feeType();">

<div class="navigation"><img src="../../../img/nav.gif" />&nbsp;当前位置：售后服务管理 &gt;维修登记 &gt;维修工单登记 &gt;配件三包判定</div>
<form method="post" name ="fm" id="fm">
	<table class="table_edit">
		<th colspan="6">
			<img src="../../../img/nav.gif" />&nbsp;车辆基本信息
		</th>
		<tr>
			<td width="15%" align="right" >VIN：</td>
			<td width="15%">${map.VIN}</td>
			<td width="15%" align="right">发动机号：</td>
			<td width="15%">${map.ENGINE_NO}</td>
			<td width="15%" align="right">牌照号：</td>
			<td width="15%">${map.LICENSE_NO}</td>		
		</tr>
		<tr>
			<td align="right" >产地：</td>
			<td>
			${map.AREA_NAME}
			</td>
			<td align="right">车型：</td>
			<td>${map.MODEL_NAME}</td>
			<td align="right">三包策略：</td>
			<td>${map.GAME_NAME}</td>
		</tr>
		<tr>
			<td align="right" >工单开始日期：</td>
			<td>${now}</td>
			<td align="right" >购车日期：</td>
			<td>${map.SALES_DATE}
			</td>
			<td align="right" >行驶天数：</td>
			<td>${days}</td>
		</tr>
		<tr>
			<td align="right" >工单里程：</td>
			<td>${mile}</td>
			<td align="right" >历史保养次数：</td>
			<td>${map.FREE_TIMES}</td>
			<td align="right" >保养状态：</td>
			<td>${flag}</td>
		</tr>
		<tr>
			<td align="right" >车型组名称：</td>
			<td>${map.WRGROUP_NAME}</td>
			<td align="right" >车辆状态：</td>
			<td>
				<script>
					writeItemValue(${map.LIFE_CYCLE});
				</script>
			</td>
		</tr>
	</table>
	<br />
	<table class="table_edit" width="100%">
		<th>
			<img src="../../../img/nav.gif" />&nbsp;维修配件三包期判定
		</th>
	</table>
	<table width="95%">
		<tr align="center">
			<td>是否三包</td>
			<td>新件代码</td>
			<td>新件名称</td>
			<td>大类名称</td>
			<td>三包月份</td>
			<td>三包里程</td> 
		</tr>
		<c:forEach var="part" items="${nlist}">
			<c:if test="${part.isWarranty==yes}">
				<tr bgcolor="#cceedd" align="center"> 
					<td><strong>√</strong></td>
					<td>${part.partCode}</td>
					<td>${part.partName}</td>
					<td>${part.typeName}</td>
					<td>${part.claimMonth}</td>
					<td>${part.claimMelieage}</td> 
				</tr>
			</c:if>	
			<c:if test="${part.isWarranty!=yes}">
				<tr bgcolor="#e8e8e8" align="center">
					<td>X</td>
					<td>${part.partCode}</td>
					<td>${part.partName}</td>
					<td>${part.typeName}</td>
					<td>${part.claimMonth}</td>
					<td>${part.claimMelieage}</td> 
				</tr>
			</c:if>
		</c:forEach>
	</table>
	<br/>
	<br/>
	<br/>
	<table class="table_edit" id="judeTable" style="display: none" width="95%">
		<th>
			<span style="color: red;">该车是非三包车</span>
		</th>
	</table>
	
	<table class="table_edit"  style="border: #ccc 1px solid " width="100%">
	<tr>
	<td>
	<table class="table_edit" id="feetable" style="display: none" width="95%">
		<th>
			<img src="../../../img/nav.gif" />&nbsp;三包期判定--代步费
		</th>
	</table>
	<table id="fetable" style="display: none" width="95%">
		<tr align="center">
			<td>维修工单号</td>
			<td>VIN</td>
			<td>累计时限</td>
			<td>预警等级</td>
			<td>下次累计时限</td>
			<td>下次预警等级</td>
			<td>当前里程</td>
			<td>车主姓名</td>
			 
		</tr>
			<tr bgcolor="#cceedd" align="center"> 
				<td>${asWrFeeWarrantyPO.roNo}</td>
				<td>${asWrFeeWarrantyPO.vin}</td>
				<td>${asWrFeeWarrantyPO.warCountDays}</td>
				<td>   <script type='text/javascript'>
				       var activityType=getItemValue('${asWrFeeWarrantyPO.warLevel}');
				       document.write(activityType) ;
				     </script></td>
				 <td>${feedays}</td>
				<td><script type='text/javascript'>
				       var activityType=getItemValue('${Fee_LEVEL}');
				       document.write(activityType) ;
				     </script></td>     
				<td>${asWrFeeWarrantyPO.warMileage}</td>
				<td>${asWrFeeWarrantyPO.ctmName}</td> 
			</tr>
	</table>
	</td>
	</tr>
	<tr>
	<td>
	<table class="table_edit" width="100%">
		<th>
			<img src="../../../img/nav.gif" />&nbsp;三包期判定--三包车辆维修时间
		</th>
	</table>
	<table width="95%">
		<tr align="center">
			<td>VIN</td>
			<td>三包结束时间</td>
			<td>三包月份</td>
			<td>三包里程</td>
			<td>累计维修天数</td>
			<td>预警等级</td>
			<td></td>
			 
		</tr>
		<c:forEach var="vinlist" items="${vinlist}">
				<tr bgcolor="#cceedd" align="center"> 
					<td>${vinlist.vin}</td>
					<td><fmt:formatDate  value="${vinlist.wrEndDate}" pattern="yyyy-MM-dd" /></td>
					<td>${vinlist.wrMonth}</td>
					<td>${vinlist.wrMileage}</td>
					<td>${vinlist.curDays}</td> 
					<td><script type='text/javascript'>
				       var activityType=getItemValue('${pepair_LEVEL}');
				       document.write(activityType) ;
				     </script></td> 
				</tr>
		</c:forEach>
	</table>
	</td>
	</tr>
	<tr>
	<td>
	<table class="table_edit" width="95%">
		<th>
			<img src="../../../img/nav.gif" />&nbsp;三包期判定--三包车辆配件累计维修次数--配件
		</th>
	</table>
	<table width="95%">
		<tr align="center">
			<td>VIN</td>
			<td>三包结束时间</td>
			<td>配件代码</td>
			<td>配件名称</td>
			<td>累计维修次数</td>
			<td>预警等级</td>
			 <td>下次累计维修次数</td>
			<td>下次预警等级</td>
		</tr>
		<c:forEach var="codeslist" items="${codeslist}">
				<tr bgcolor="#cceedd" align="center"> 
					<td>${codeslist.vin}</td>
					<td><fmt:formatDate  value="${codeslist.wrEndDate}" pattern="yyyy-MM-dd" /></td>
					<td>${codeslist.partCode}</td>
					<td>${codeslist.partName}</td>
					<td>${codeslist.curTimes}</td> 
					<td><script type='text/javascript'>
				       var activityType=getItemValue('${codeslist.level}');
				       document.write(activityType) ;
				     </script></td>
					<td>${codeslist.nextCurTimes}</td>
					<td><script type='text/javascript'>
				       var activityType=getItemValue('${codeslist.nextLevel}');
				       document.write(activityType) ;
				     </script></td>
				</tr>
		</c:forEach>
		<c:forEach var="codeslistnull" items="${codeslistnull}">
				<tr bgcolor="#cceedd" align="center"> 
					<td>${codeslistnull.vin}</td>
					<td><fmt:formatDate  value="${codeslistnull.wrEndDate}" pattern="yyyy-MM-dd" /></td>
					<td>${codeslistnull.partCode}</td>
					<td>${codeslistnull.partName}</td>
					<td>${codeslistnull.curTimes}</td> 
					<td><script type='text/javascript'>
				       var activityType=getItemValue('${codeslistnull.level}');
				       document.write(activityType) ;
				     </script></td>
					<td></td>
					<td></td>
				</tr>
		</c:forEach>
	</table>
	</td>
	</tr>
	<tr>
	<td>
	<table class="table_edit" width="95%">
		<th>
			<img src="../../../img/nav.gif" />&nbsp;三包期判定--三包车辆配件累计维修次数--工时
		</th>
	</table>
	
	<table width="95%">
		<tr align="center">
			<td>VIN</td>
			<td>三包结束时间</td>
			<td>工时代码</td>
			<td>工时名称</td>
			<td>当前里程</td>
			<td>累计维修次数</td>
			<td>预警等级</td>
			<td>下次累计维修次数</td>
			<td>下次预警等级</td>
			 
		</tr>
		<c:forEach var="labcodeslist" items="${labcodeslist}">
				<tr bgcolor="#cceedd" align="center"> 
					<td>${labcodeslist.vin}</td>
					<td><fmt:formatDate  value="${labcodeslist.wrEndDate}" pattern="yyyy-MM-dd" /></td>
					<td>${labcodeslist.partCode}</td>
					<td>${labcodeslist.partName}</td>
					<td>${labcodeslist.curMileage}</td>
					<td>${labcodeslist.curTimes}</td> 
					<td><script type='text/javascript'>
				       var activityType=getItemValue('${labcodeslist.level}');
				       document.write(activityType) ;
				     </script></td>
					<td>${labcodeslist.nextCurTimes}</td>
					<td><script type='text/javascript'>
				       var activityType=getItemValue('${labcodeslist.nextLevel}');
				       document.write(activityType) ;
				     </script></td>
				</tr>
		</c:forEach>
		<c:forEach var="labcodeslistnull" items="${labcodeslistnull}">
				<tr bgcolor="#cceedd" align="center"> 
					<td>${labcodeslistnull.vin}</td>
					<td><fmt:formatDate  value="${labcodeslistnull.wrEndDate}" pattern="yyyy-MM-dd" /></td>
					<td>${labcodeslistnull.partCode}</td>
					<td>${labcodeslistnull.partName}</td>
					<td>${labcodeslistnull.curMileage}</td>
					<td>${labcodeslistnull.curTimes}</td> 
					<td><script type='text/javascript'>
				       var activityType=getItemValue('${labcodeslistnull.level}');
				       document.write(activityType) ;
				     </script></td>
					<td></td>
					<td></td>
				</tr>
		</c:forEach>
	</table>
	</td>
	</tr>
	
	<tr>
	<td>
	<table class="table_edit" width="95%">
		<th>
			<img src="../../../img/nav.gif" />&nbsp;三包期判定--三包车辆配件累计维修次数--部位
		</th>
	</table>
	<table width="95%">
		<tr align="center">
			<td>VIN</td>
			<td>三包结束时间</td>
			<td>部位代码</td>
			<td>部位名称</td>
			<td>当前里程</td>
			<td>累计维修次数</td>
			<td>预警等级</td>
			<td>下次累计维修次数</td>
			<td>下次预警等级</td>
			 
		</tr>
		<c:forEach var="poslist" items="${poslist}">
				<tr bgcolor="#cceedd" align="center"> 
					<td>${poslist.vin}</td>
					<td><fmt:formatDate  value="${poslist.wrEndDate}" pattern="yyyy-MM-dd" /></td>
					<td>${poslist.partCode}</td>
					<td>${poslist.partName}</td>
					<td>${poslist.curMileage}</td>
					<td>${poslist.curTimes}</td> 
					<td><script type='text/javascript'>
				       var activityType=getItemValue('${poslist.level}');
				       document.write(activityType) ;
				     </script></td>
					<td>${poslist.nextCurTimes}</td>
					<td><script type='text/javascript'>
				       var activityType=getItemValue('${poslist.nextLevel}');
				       document.write(activityType) ;
				     </script></td>
				</tr>
		</c:forEach>
		<c:forEach var="posslistnull" items="${posslistnull}">
				<tr bgcolor="#cceedd" align="center"> 
					<td>${posslistnull.vin}</td>
					<td><fmt:formatDate  value="${posslistnull.wrEndDate}" pattern="yyyy-MM-dd" /></td>
					<td>${posslistnull.partCode}</td>
					<td>${posslistnull.partName}</td>
					<td>${posslistnull.curMileage}</td>
					<td>${posslistnull.curTimes}</td> 
					<td><script type='text/javascript'>
				       var activityType=getItemValue('${posslistnull.level}');
				       document.write(activityType) ;
				     </script></td>
				     <td></td>
				     <td></td>
				</tr>
		</c:forEach>
	</table>
	</td>
	</tr>
	<table width="100%">
		<tr>
			<td>
				<font color=red>
					*校验以录入工单里程为准！如果工单里程发生变化，请再次校验以作参考！
				</font>
			</td>
		</tr>
		<tr>
			<td colspan="4" align="center">
				<input type="button" value="关闭" class="normal_btn" onclick="window.close();"/>
			</td>
		</tr>
	</table>
	</table>
</form>
<script type="text/javascript">
function goBack(){
	location = '<%=contextPath%>/claim/laborlist/LaborListAction/firstUrlInit.do' ;
}
</script>
</BODY>
</html>