<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@taglib uri="/jstl/cout" prefix="c" %>
<%
	String contextPath = request.getContextPath();
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml"><%@ page import="com.infodms.dms.common.Constant"%><%@ page import="com.infodms.dms.common.FileConstant"%><%@taglib uri="/jstl/cout" prefix="c"%>
<head>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<script type="text/javascript">
	function txtClr(valueId) {
		document.getElementById(valueId).value = '' ;
	}
	var myPage;
	var url = "<%=contextPath%>/sales/storageManage/VehicleDispatchQuery/vehicleDispatchQueryOEM.json?COMMAND=1";
	var title = null;
	
	var columns = [
				{header: "批发号", dataIndex: 'TRANSFER_NO', class:'center'},
				{header: "调出经销商", dataIndex: 'OUT_DEALER', class:'center'},
				{header: "调入经销商", dataIndex: 'IN_DEALER', class:'center'},
				{header: "VIN", dataIndex: 'VIN', class:'center'},
				{header: "物料名称", dataIndex: 'MATERIAL_NAME', class:'center'},
				{header: "申请日期", dataIndex: 'APP_DATE', class:'center'},
				{header: "批发原因", dataIndex: 'TRANSFER_REASON', class:'center'},
				{header: "审批结果", dataIndex: 'CHECK_RES', class:'center',renderer:getItemValue},
				{header: "审批意见", dataIndex: 'CHECK_DESC', class:'center'}
		      ];
	function mySelect(value,meta,record){
	  	return String.format("<a href=\"#\" onclick='vehicleInfo(\""+value+"\")';>"+value+"</a>");
	}
	function vehicleInfo(value){
		OpenHtmlWindow('<%=contextPath%>/sales/storageManage/VehicleInfo/vehicleInfoQuery.do?vin='+value,700,500);
	}
</script>
<title>批发查询</title>
</head>
<body onunload='javascript:destoryPrototype();' > 
<div class="wbox">
	<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置：  整车销售 &gt; 经销商库存管理 &gt; 车辆批发查询</div>
	<form id="fm" name="fm" method="post">
		<input type="hidden" name="curPage" id="curPage" value="1" />
		<input type="hidden" id="dlrId" name="dlrId" value="" />
		<div class="form-panel">
		<h2>批发查询</h2>
		<div class="form-body">
			<table class="table_query" border="0">
				<tr>
					<td class="tblopt right" height="40px">物料组选择：</td>
					<td>
						<input type="text" name="materialCode" id="materialCode" size="10" value="" class="middle_txt" onclick="showMaterialGroup('materialCode','','false','','true')"/>
	       				<input type="button" class="normal_btn" onclick="txtClr('materialCode');" value="清 空" id="clrBtn" /> 
					</td>
					<td class="tblopt right">批发申请日期：</td>
					<td>
						<div class="left">
		            		<input name="startDate" id="t1" value="" type="text" class="short_txt" 
		            			onFocus="WdatePicker({el:$dp.$('t1'), maxDate:'#F{$dp.$D(\'t2\')}'})"  style="cursor: pointer;width: 80px;"/>
		            		至
		            		<input name="endDate" id="t2" value="" type="text" class="short_txt"
		            			onFocus="WdatePicker({el:$dp.$('t2'), minDate:'#F{$dp.$D(\'t1\')}'})" style="cursor: pointer;width: 80px;"/>
	            		</div>	
					</td>
				</tr>
				<tr>
					<td class="tblopt right">调出经销商：</td>
					<td >
	      				<input name="outDealerCode" type="text" id="outDealerCode" class="middle_txt" onclick="showOrgDealer('outDealerCode', '', 'false', '', '<%= Constant.DEALER_LEVEL_01 %>', 'true', '<%=Constant.DEALER_TYPE_DVS %>', '');"  value="" size="20"  />
	                  <!--<c:if test="${dutyType==10431001}">
	                    <input class="mini_btn" id="dlbtn2" name="dlbtn2" type="button" 
	                    	onclick="showOrgDealer('outDealerCode', '', 'false', '', '<%= Constant.DEALER_LEVEL_01 %>', 'true', '<%=Constant.DEALER_TYPE_DVS %>', '');" value="..." />
	                  </c:if>
	                  <c:if test="${dutyType==10431002}">
	                    <input class="mini_btn" id="dlbtn2" name="dlbtn2" type="button" 
	                    	onclick="showOrgDealer('outDealerCode', '', 'false', '', '<%= Constant.DEALER_LEVEL_01 %>', 'true', '<%=Constant.DEALER_TYPE_DVS %>', '');" value="..." />
	                  </c:if>
	                  <c:if test="${dutyType==10431003}">
	                    <input class="mini_btn" id="dlbtn2" name="dlbtn2" type="button" 
	                    	onclick="showOrgDealer('outDealerCode', '', 'false', '', '<%= Constant.DEALER_LEVEL_01 %>', 'true', '<%=Constant.DEALER_TYPE_DVS %>', '');" value="..." />
	                  </c:if>
	                  <c:if test="${dutyType==10431004}">
	                    <input class="mini_btn" id="dlbtn2" name="dlbtn2" type="button" 
	                    	onclick="showOrgDealer('outDealerCode', '', 'false', '', '<%= Constant.DEALER_LEVEL_01 %>', 'true', '<%=Constant.DEALER_TYPE_DVS %>', '');" value="..." />
	                  </c:if>-->
	                  <input class="normal_btn" type="button" value="清空" onclick="txtClr('outDealerCode');"/>
	    			</td>
	    			<td class="tblopt right">批发号：</td>
				    <td >
				   	  <input name="TRANSFER_NO" type="text" id="TRANSFER_NO" class="middle_txt" value="" size="20"  />
				   	</td>
				</tr>
				<tr>
					<td class="right">调入经销商：</td>
					<td >
	      				<input name="inDealerCode" type="text" id="inDealerCode" class="middle_txt" onclick="showOrgDealer('inDealerCode', '', 'false', '', '<%= Constant.DEALER_LEVEL_01 %>', 'true', '<%=Constant.DEALER_TYPE_DVS %>', '');" value="" size="20"  />
	                  <!--<c:if test="${dutyType==10431001}">
	                    <input class="mini_btn" id="dlbtn2" name="dlbtn2" type="button" 
	                    	onclick="showOrgDealer('inDealerCode', '', 'false', '', '<%= Constant.DEALER_LEVEL_01 %>', 'true', '<%=Constant.DEALER_TYPE_DVS %>', '');" value="..." />
	                  </c:if>
	                  <c:if test="${dutyType==10431002}">
	                    <input class="mini_btn" id="dlbtn2" name="dlbtn2" type="button" 
	                    	onclick="showOrgDealer('inDealerCode', '', 'false', '', '<%= Constant.DEALER_LEVEL_01 %>', 'true', '<%=Constant.DEALER_TYPE_DVS %>', '');" value="..." />
	                  </c:if>
	                  <c:if test="${dutyType==10431003}">
	                    <input class="mini_btn" id="dlbtn2" name="dlbtn2" type="button" 
	                    	onclick="showOrgDealer('inDealerCode', '', 'false', '', '<%= Constant.DEALER_LEVEL_01 %>', 'true', '<%=Constant.DEALER_TYPE_DVS %>', '');" value="..." />
	                  </c:if>
	                  <c:if test="${dutyType==10431004}">
	                    <input class="mini_btn" id="dlbtn2" name="dlbtn2" type="button" 
	                    	onclick="showOrgDealer('inDealerCode', '', 'false', '', '<%= Constant.DEALER_LEVEL_01 %>', 'true', '<%=Constant.DEALER_TYPE_DVS %>', '');" value="..." />
	                  </c:if>-->
	                  <input class="normal_btn" type="button" value="清空" onclick="txtClr('inDealerCode');"/>
	    			</td>
	    			<td class="tblopt right">VIN：</td>
					<td>
	      				<textarea id="vin" name="vin"  rows="2"  class="form-control" style="width:150px;"></textarea>
	    			</td>
				</tr>
				<tr>
				  	 <td colspan="4" class="center">
						<input type="button"  class="u-button u-query"   onclick="__extQuery__(1);" value=" 查  询  " id="queryBtn" /> 
					</td> 		   
				</tr>
			</table>
		</div>
		</div>
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	</form>
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
</div>
</body>
</html>