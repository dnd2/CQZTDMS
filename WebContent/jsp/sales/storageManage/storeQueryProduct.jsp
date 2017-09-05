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
<script type="text/javascript">
	var myPage;
	
	var url = "<%=contextPath%>/sales/storageManage/MyStore/getMyStore.json?COMMAND=1";
	var title = null;
	var columns = [
				{header: "序号", dataIndex: 'NUM', align:'center'},
				{header: "仓库名称", dataIndex: 'WAREHOUSE_NAME', align:'center'},
				{header: "仓库类型", dataIndex: 'WAREHOUSE_TYPE', align:'center',renderer:getItemValue},
				{header: "状态", dataIndex: 'STATUS', align:'center',renderer:getItemValue},
				{header: "备注", dataIndex: 'REMARK', align:'center'},
				{header: "操作", dataIndex: 'CAMPAIGN_ID', align:'center',renderer:myLink}
		      ];
	  //设置超链接
    function myLink(value,meta,record){
    	return String.format("<a href='#' onclick='modifyStore("+record.data.WAREHOUSE_ID+")'>[修改]</a>");
    }
	function showResult(json){
		turnQuery();
	}
	
	function turnQuery() {
		 __extQuery__(1);
	}
	function modifyStore(wareHouseID){
		//MyAlert("modifyStore=="+modifyStore);
		document.getElementById("fm").action= '<%=contextPath%>/sales/storageManage/MyStore/modInitMyStore.do?warehouseId='+wareHouseID;
		document.getElementById("fm").submit();
	}
	function addMyStore(){
		//MyAlert("addMyStore");
		document.getElementById("fm").action= '<%=contextPath%>/sales/storageManage/MyStore/addInitMyStore.do';
		document.getElementById("fm").submit();
	}

</script>

<body> 
	<div class="wbox">
	<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置：  整车销售 &gt; 经销商库存管理 &gt; 仓库维护</div>
	<form id="fm" name="fm" method="post">
				<div class="form-panel">
				<h2>仓库维护</h2>
				<div class="form-body">
		<input type="hidden" name="curPage" id="curPage" value="1" />
		<input type="hidden" id="dlrId" name="dlrId" value="" />
		<table class="table_query" border="0">
			<tr>
				<td class="right"><div class="right">仓库名称：</div></td>
				<td  >
      				<input name="warehouseName" class="middle_txt" type="text" id="warehouseName" class="SearchInput" value="" size="20" />
    			</td>
    			<td class="right"><div class="right">仓库类型：</div></td>
				<td >
					<script type="text/javascript">
						genSelBoxExp("warehouseType",<%=Constant.DEALER_WAREHOUSE_TYPE%>,"-1",true,"","onchange=''","false",'');
					</script>
    			</td>
			</tr>
			<tr>
				<td class="right"><div class="right">经销商：</div></td>
				<td>
      				<select name="dealer" id="dealer__"  class="u-select" >
      					<c:forEach var="dealerList_D" items="${dealerList }">
      						<option value="${dealerList_D.DEALER_ID }">${dealerList_D.DEALER_NAME }-${dealerList_D.DEALER_CODE }</option>
      					</c:forEach>
      				</select>
    			</td>
    			<!-- <td class="tblopt" id="lowDel__"><div class="right">代管经销商：</div></td>
				<td >
      				<div id="lowDel__L">
				<input type="hidden"  name="lowID" id="lowID" size="15" value=""/>
				<input type="text"  name="lowDel" id="lowDel" size="15" value="" readonly="readonly" />
				<input name="button2" type="button" class="mini_btn" onclick="showOrgDealer('lowDel', 'lowID', 'false', '', 'true','true');" value="..." />
				<input type="button" class="normal_btn" onclick="txtClr('lowDel');txtClr('lowID');" value="清 空" id="clrBtn" />
				</div>
    			</td> -->
    			<td class="right">状态：</td>
				<td>
					<script type="text/javascript">
						genSelBoxExp("warehouseStatus",<%=Constant.STATUS%>,"-1",true,"",'',"false",'');
					</script>
				</td>
			</tr>

			<tr>
				<td colspan="4" class="table_query_4Col_input" style="text-align: center">
					<input name="queryBtn" type="button" class="u-button u-query" onclick="__extQuery__(1)" value="查 询" /> &nbsp; 
					<input type="button" class="u-button u-submit" onclick="addMyStore();" value="新 增" />
				</td>
			</tr>

		</table>
		</div>
		</div>
			<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
			<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
	</form>
</div>
</body>
</html>