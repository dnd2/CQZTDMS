<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>


<%
	String contextPath = request.getContextPath();
%>
<%@taglib uri="/jstl/cout" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml"><%@ page import="com.infodms.dms.common.Constant"%><%@ page import="com.infodms.dms.common.FileConstant"%><%@taglib uri="/jstl/cout" prefix="c"%>
<head>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<script type="text/javascript">
var myPage;
var url = "<%=contextPath%>/sales/storageManage/VehicleDispatchQuery/VehicleDispatchQueryDLR.json?COMMAND=1";
var title = null;
var columns = [
			{header: "批发号", dataIndex: 'TRANSFER_NO', class:'center'},
			{header: "调入经销商", dataIndex: 'DEALER_SHORTNAME', class:'center'},
			{header: "调出经销商", dataIndex: 'OUT_DEALER_NAME', class:'center'},
			{header: "VIN", dataIndex: 'VIN', class:'center'},
			{header: "物料名称", dataIndex: 'MATERIAL_NAME', class:'center'},
			{header: "申请日期", dataIndex: 'APP_DATE', class:'center'},
			{header: "批发原因", dataIndex: 'TRANSFER_REASON', class:'center'},
			{header: "审批状态", dataIndex: 'CHECK_STATUS', class:'center',renderer:getItemValue},
			{header: "审批意见", dataIndex: 'CHECK_DESC', class:'center'}
	      ];
function getBatchesDownLoad(){
	var fsm = document.getElementById("fm");
	fsm.action= "<%=contextPath%>/sales/storageManage/VehicleDispatchQuery/vhclBatchesDownLoad.json";
	fsm.submit();
}

function clrTxt(value){
	document.getElementById(value).value="";
}
</script>
<title>批发查询</title>
</head>
<body onunload='javascript:destoryPrototype();' > 
<div class="wbox">
	<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置：  整车销售 &gt; 库存管理 &gt; 批发查询</div>
	<form id="fm" name="fm" method="post">
		<input type="hidden" name="curPage" id="curPage" value="1" />
		<input type="hidden" id="dlrId" name="dlrId" value="" />
		<div class="form-panel">
		<h2>批发查询</h2>
		<div class="form-body">
			<table class="table_query" >
			<c:if test="${returnValue==2}">
			<tr>
			<td  colspan="4"><font color="red">注意事项:</font></td>
		</tr>
		<tr><td  colspan="4"><font color="red">1、不同大区经销商批发，需要车厂审核</font></td></tr>
		<tr><td  colspan="4"><font color="red">2、同一大区经销商批发，需要大区审核</font></td></tr>
		<tr><td  colspan="4"><font color="red">3、一、二级经销商之间批发，不需要审核</font></td></tr>
			</c:if>
				<tr>
					<td class="tblopt right" width="20%">批发申请日期：</td>
					<td width="20%">
						<div class="left">
		            		<input name="startDate" id="t1" value=""  type="text" class="short_txt" 
		            			onFocus="WdatePicker({el:$dp.$('t1'), maxDate:'#F{$dp.$D(\'t2\')}'})"  style="cursor: pointer;width: 80px;"/>
		            	至
		            		<input name="endDate" id="t2" value="" type="text" class="short_txt" 
		            			onFocus="WdatePicker({el:$dp.$('t2'), minDate:'#F{$dp.$D(\'t1\')}'})" style="cursor: pointer;width: 80px;"/>
	            		</div>	
					</td>
					
					<td width="20%" class="tblopt right">物料组选择：</td>
					<td  width="20%">
						<input type="text" name="materialCode" id="materialCode" size="10" value=""  class="middle_txt" onclick="showMaterialGroup('materialCode','','true','','true');" />
						<input type="button"  class="u-button"  onclick="clrTxt('materialCode')" value="清空" id="clrTxtBtn" />&nbsp;&nbsp;
					</td>
					
				</tr>
				<tr>
					
	    			 <td  class="tblopt right">批发号：</td>	
			       <td >
				   	  <input name="TRANSFER_NO" type="text" id="TRANSFER_NO" class="middle_txt" value="" size="20"  />
				   	</td>	
				   	<td width="20%" class="tblopt right">调入经销商：</td>
					<td  >
	      				<input name="inDealerCode" type="text" id="inDealerCode"  value="" size="20"  class="middle_txt"  onclick="showOrgDealer('inDealerCode', '', 'false', '', '<%= Constant.DEALER_LEVEL_01 %>', 'true', '<%=Constant.DEALER_TYPE_DVS %>', '');"/>
	    				<input type="button"  class="u-button"  onclick="clrTxt('inDealerCode')" value="清空" id="clrTxtBtn" />&nbsp;&nbsp;
	    			</td>
					
	    			
				</tr>
				<tr>
					<td width="20%" class="tblopt right">VIN：</td>
					<td style="position: relative;">
	      				<textarea id="vin" name="vin"  rows="2"  class="form-control" style="width:128px;position: relative;left:7px;"></textarea>
	    			</td>
	    				
			      <td width="20%" class="tblopt right">调出经销商：</td>
					<td  >
	      				<input name="outDealerCode" type="text" id="outDealerCode" value="" size="20"  class="middle_txt"  onclick="showOrgDealer('outDealerCode', '', 'false', '', '<%= Constant.DEALER_LEVEL_01 %>', 'true', '<%=Constant.DEALER_TYPE_DVS %>', '');"/>
	    				<input type="button"  class="u-button"  onclick="clrTxt('outDealerCode')" value="清空" id="clrTxtBtn" />&nbsp;&nbsp;
	    			</td>
				</tr>
				   	<tr>
					   	<td colspan="4" align="center">
					   		<div align="center">
							<input type="button"  class="u-button u-query"  onclick="__extQuery__(1);" value=" 查  询  " id="queryBtn" />&nbsp;&nbsp;
							<input type="button"  class="u-button u-submit"  onclick="getBatchesDownLoad();" value=" 下  载  " id="queryBtn" /> 
							</div>
						</td>	 
					</tr>
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