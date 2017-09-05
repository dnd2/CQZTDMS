<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.Constant"%>
<%@ page import="com.infodms.dms.util.CommonUtils"%>
<%@ page import="java.util.Map"%>
<%@ page import="java.util.List"%>
<%@taglib uri="/jstl/cout" prefix="c" %>
<%
	String contextPath = request.getContextPath();
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<script type="text/javascript">

</script>
<style type="text/css" >
 .mix_type{width:100px;}
 .min_type{width:176px;}
 .mini_type{width:198px;}
 .long_type{width:545px;}
 .xlong_type{width:305px}
</style>
<title>实销信息上报</title>
</head>
<body onunload='javascript:destoryPrototype();' onload="loadcalendar();"> 
<div class="wbox">
	<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置：  潜客管理 &gt; 客户管理 &gt;试乘试驾修改 </div>
	<form id="fm" name="fm" method="post">
		<input type="hidden" name="curPage" id="curPage" value="1" />
		<input type="hidden" id="ctmId" name="ctmId" value="${ctmId}"/>
		<input type="hidden" id="decorationId" name="driveId" value="${tp.drivingId}"/>
		<table class="table_query" border="0">
		<tr>
				<td width="20%" class="tblopt"><div align="right">证件号码：</div></td>
				<td width="30%" >
      				<input type="text" id="idNo" name="idNo" class="min_type" size="20" value="${tp.cardNo}"/>
    			</td>
    			<td width="20%" class="tblopt"><div align="right">试驾时间：</div></td>
				<td width="30%" >
      			  <input name="driveDate" type="text" id="driveDate"  class="min_type" style="width:160px;" readonly value="${driveDate}"   />
        		  <input class="time_ico" type="button" onClick="showcalendar(event, 'driveDate', false);" />
    			</td>
			</tr>
			<tr>
				<td width="20%" class="tblopt"><div align="right">试驾车型：</div></td>
				<td width="30%" >
						<select id="intentVehicleA" onchange="toChangeMenuSelected(this,'intentVehicleB')" style="width: 60px">
						<c:forEach items="${menusAList }" var="blist">
							<c:if test="${upSeriesCode == blist.MAINID }" >
								<option id="all" value="${blist.MAINID }" selected="selected">${blist.NAME }</option>
							</c:if>
							<c:if test="${upSeriesCode != blist.MAINID }" >
								<option id="all" value="${blist.MAINID }">${blist.NAME }</option>
							</c:if>
						</c:forEach>
					</select>
					
					<select id="intentVehicleB" name="driveModel" style="width: 120px">
						<c:forEach items="${menusABList2 }" var="blist">
							<c:if test="${tp.drivingVechile == blist.MAINID }" >
								<option id="all" value="${blist.MAINID }" selected="selected">${blist.NAME }</option>
							</c:if>
							<c:if test="${tp.drivingVechile != blist.MAINID }" >
								<option id="all" value="${blist.MAINID }">${blist.NAME }</option>
							</c:if>
						</c:forEach>
					</select>
<!--      				 <input name="driveModel" type="text" id="driveModel"  class="min_type" value="${tp.drivingVechile}"  />-->
    			</td>
    			<td width="20%" class="tblopt"><div align="right">试驾专员：</div></td>
				<td width="30%" >
				  <input name="driveMan" type="text" id="driveMan"  class="min_type"   value="${tp.drivingMan}"  />
        		 
    			</td>
			</tr>
			<tr>
<!--				<td width="20%" class="tblopt"><div align="right">试驾路线：</td>-->
<!--				<td width="30%" >-->
<!--      				<script type="text/javascript">-->
<!--		                genSelBoxExp("driveRoad",6006,"${tp.drivingRoad}",false,"","","false",'');-->
<!--		            </script> -->
<!--    			</td>-->
				<td width="20%" class="tblopt"><div align="right">初始里程：</td>
				<td width="30%"  >
      				<input type="text" id="firstRoad" name="firstRoad" class="min_type" size="20" value="${tp.firstMile}" />
    			</td>
    			<td width="20%" class="tblopt"><div align="right">结束里程：</td>
				<td width="30%" >
      				<input type="text" id="endRoad" name="endRoad" class="min_type" size="20" value="${tp.endMile}" />
    			</td>
			</tr>
			<tr>
				<td colspan="2"></td>
				<td class="table_query_3Col_input" colspan="2" align="center">
					<center>
					<input type="button" class="normal_btn" onclick="updateDrive();" value="修 &nbsp; 改" id="queryBtn" /> 
					</center>
				</td>
			</tr>
		</table>
		
</form>
	
</div>
<script type="text/javascript">

	function updateDrive(){
		makeFormCall('<%=contextPath%>/crm/customer/CustomerManage/driveUpdate.json', addResult, "fm") ;
	}
	//数据回写
	function addResult(json){
		_hide();
		parent.$('inIframe').contentWindow.drivingTableAdd(json);
	}

</script>    
</body>
</html>