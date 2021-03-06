<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml"><%@ page import="com.infodms.dms.common.Constant"%><%@ page import="com.infodms.dms.common.FileConstant"%><%@taglib uri="/jstl/cout" prefix="c"%>

<%@ page import="java.util.*"%>
<%@taglib uri="/jstl/cout" prefix="c" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>经销商仓库维护</title>
</head>
<script type="text/javascript">
function txtClr(valueId) {
	document.getElementById(valueId).value = '' ;
}
</script>
<% String contextPath = request.getContextPath();  %>
	<script>
		function modMyStore(){
			var warehouseName=document.getElementById("warehouseName").value;
			if(warehouseName==null || warehouseName==''){
				MyAlert("请输入仓库名称!");
				return;
			}
			
			if(submitForm('fm')){
				document.getElementById("fm").action= '<%=contextPath%>/sales/storageManage/MyStore/modMyStore.do';
				document.getElementById("fm").submit();
			}
		}
		
		function toBack(){
			document.getElementById("fm").action= '<%=contextPath%>/sales/storageManage/MyStore/detailMyStore.do';
				document.getElementById("fm").submit();
		}
	</script>
	
<body > 
	<div class="wbox">
	<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置：  整车销售 &gt; 经销商库存管理 &gt; 仓库修改维护</div>
	<form id="fm" name="fm" method="post">
				<div class="form-panel">
				<h2>仓库维护</h2>
				<div class="form-body">
		<input type="hidden" id="warehouseId" name="warehouseId" value="${tmDealerWarehousePO.warehouseId}" />
		<table class="table_query" border="0">
			<tr>
				<td class="right">仓库名称：</td>
				<td>
      				<input name="warehouseName" datatype="0,is_textarea,30"  class="middle_txt" type="text" id="warehouseName" class="SearchInput" value="${tmDealerWarehousePO.warehouseName}" size="20" />
    			</td>
    			<td  class="right">仓库类型：</td>
				<td>
					<script type="text/javascript">
						genSelBoxExp("warehouseType",<%=Constant.DEALER_WAREHOUSE_TYPE%>,"${tmDealerWarehousePO.warehouseType}",false,"","onchange='setShow(this.value)'","false",'');
					</script>
    			</td>
			</tr>
			<tr>
				<td class="right">经销商：</td>
				<td >
      				<select name="dealer" id="dealer__" onchange="changeFleet(this.value)" class="u-select">
      					<c:forEach var="dealerList_D" items="${dealerList }">
      						<c:if test="${tmDealerWarehousePO.dealerId == dealerList_D.DEALER_ID}">
      							<option selected value="${dealerList_D.DEALER_ID }">${dealerList_D.DEALER_NAME }-${dealerList_D.DEALER_CODE }</option>
      						</c:if>
      						<c:if test="${tmDealerWarehousePO.dealerId != dealerList_D.DEALER_ID}">
      							<option value="${dealerList_D.DEALER_ID }">${dealerList_D.DEALER_NAME }-${dealerList_D.DEALER_CODE }</option>
      						</c:if>
      					</c:forEach>
      				</select>
    			</td>
    			<!-- <td width="20%" class="tblopt" id="lowDel__"><div class="right">代管经销商：</div></td>
				<td width="39%" >
      			<div id="lowDel__L">
      			<input type="hidden"  name="lowID" id="lowID" size="15" value=""/>
				<input type="text"  name="lowDel" id="lowDel" size="15" value="${dealerPo1.dealerCode}" readonly="readonly" />
				<input name="button2" type="button" class="mini_btn" onclick="showOrgDealer('lowDel', 'lowID', 'false', '', 'true');" value="..." />
				<input type="button" class="normal_btn" onclick="txtClr('lowDel');" value="清 空" id="clrBtn" />
				</div>
    			</td> -->
    			<td class="right">是否关闭：</td>
				<td >
					<script type="text/javascript">
						genSelBoxExp("warehouseStatus",<%=Constant.STATUS%>,"${tmDealerWarehousePO.status}",false,"",'',"false",'');
					</script>
    			</td>
			</tr>
			<tr>

    			<td class="right">备注：</td>
				<td >
				<textarea id="remark"  name="remark" rows="3" cols="20" class="form-control"  datatype="1,is_textarea,2000">${tmDealerWarehousePO.remark}</textarea>
    			</td>
			</tr>
			<tr>
				<td colspan="4" class="center">
					<input type="button" class="u-button u-submit" name="button3" onClick="modMyStore();" value="保存"/>
					<input type="button" class="u-button u-reset" name="button4" onClick="toBack();" value="返回"/>
				</td>
			</tr>
		</table>
		</div>
		</div>
	</form>

	</div>
</body>
</html>
		