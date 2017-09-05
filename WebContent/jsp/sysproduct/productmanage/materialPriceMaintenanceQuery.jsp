<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.Constant" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c" %>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<head>
<%
String contentPath=request.getContextPath();
%>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>物料价格维护</title>
</head>
<body>
<div class="navigation"><img src="<%=request.getContextPath()%>/img/nav.gif" />&nbsp;当前位置：系统管理 > 产品维护 > 物料价格维护(车厂价)</div>
<form method="POST" name="fm" id="fm">
  	<table class="table_query" border="0">
		<tr>
			<td class="table_query_3Col_label_6Letter" nowrap="nowrap">物料代码：</td>
			<td class="table_query_4Col_input" nowrap="nowrap">
				<input name="materialCode" maxlength="30" value="" datatype="1,is_noquotation,30" id="materialCode" type="text" class="middle_txt" />
			</td>
			<td class="table_query_3Col_label_6Letter" nowrap="nowrap">物料名称：</td>
			<td class="table_query_4Col_input" nowrap="nowrap">
				<input name="materialName" maxlength="30" value="" datatype="1,is_noquotation,30" id="materialName" type="text" class="middle_txt" />
			</td>
			<td class="table_query_3Col_label_6Letter" nowrap="nowrap">物料状态：</td>
			<td class="table_query_4Col_input" nowrap="nowrap">
				<script type="text/javascript">
			      	genSelBoxExp("status",<%=Constant.STATUS%>,"",true,"short_sel","","false",'');
			    </script>
			</td>
		</tr>
		<tr>
			<td class="table_query_3Col_label_6Letter" nowrap="nowrap">所属物料组：</td>
			<td class="table_query_4Col_input" nowrap="nowrap">
				<input type="text" name="groupCode" size="20" id="groupCode" value="" />
				<input name="button3" type="button" class="mini_btn" onclick="showMaterialGroup('groupCode','','false','4','true')" value="..." />
				<input class="normal_btn" type="button" value="清空" onclick="clrTxt('groupCode');"/>
			</td>
	  
		    <td class="table_query_3Col_label_6Letter" nowrap="nowrap">产地：</td>
			<td class="table_query_4Col_input" nowrap="nowrap">
				 <select name="YIELDLY" id="YIELDLY" class="short_sel">
				<c:if test="${list!=null}">
					<c:forEach items="${list}" var="list">
						<option value="${list.AREA_ID}">${list.AREA_NAME}</option>
					</c:forEach>
				</c:if>
		  	</select>
			</td>		
		</tr>
		<tr>
			<td colspan="6" class="table_query_4Col_input" style="text-align: center">
				<input name="queryBtn" type="button" class="normal_btn" onclick="__extQuery__(1);" value="查 询" id="queryBtn" /> &nbsp; 
				<input type="button" class="normal_btn" onclick="importmaterialPrice();" value="导入"/> &nbsp; 
				<input type="button" class="normal_btn" onclick="resourcesAuditDown();" value="导出"/> &nbsp; 
				<input name="button2" type="button" class="normal_btn" onclick="window.location.href='<%=request.getContextPath()%>/sysproduct/productmanage/MaterialManage/materialManageAddPre.do'" value="新 增" style="display:none;"/>
			</td>
		</tr>
	</table>
  
	<!--分页 begin -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	
    <!--分页 end -->
</form>
<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
<script type="text/javascript">

	var myPage;
	//查询路径
	var url = "<%=request.getContextPath()%>/sysproduct/productmanage/MaterialPriceMaintenance/materialManageQuery.json";;
				
	var title = null;

	var columns = [
					{header: "序号", align:'center', renderer:getIndex},
					{header: "物料代码", dataIndex: 'MATERIAL_CODE', align:'center'},
					{header: "物料名称", dataIndex: 'MATERIAL_NAME', align:'center'},
					{header: "物料价格", dataIndex: 'VHCL_PRICE', align:'center',renderer:formatCurrency},
					{header: "状态", dataIndex: 'STATUS', align:'center', renderer:getItemValue},
					{header: "操作", dataIndex: 'MATERIAL_ID', align:'center', renderer:upDate}
			      ];	

    function importmaterialPrice(){
        

    	$('fm').action = "<%=contentPath %>/sysproduct/productmanage/MaterialPriceMaintenance/materialManageImportInit.do";
    	$("fm").submit();
       } 
    function resourcesAuditDown(){
        var areaId=document.getElementById("YIELDLY").value;
		if(areaId==""){
			MyAlert("导出数据前需选择产地");
			return;
		}
    	$('fm').action = "<%=contentPath %>/sysproduct/productmanage/MaterialPriceMaintenance/resourcesAuditDown.do";
    	$("fm").submit();
       } 
	function clrTxt(txtId){
    	document.getElementById(txtId).value="";
    }
    //修改
    function upDate(value,metaDate,record){
        return String.format("<a href='#' title='修改物料价格' onclick='updatePrice("+value+")'>[修改价格]</a>");
    }
    function updatePrice(matId){
    	OpenHtmlWindow('<%=contentPath%>/sysproduct/productmanage/MaterialPriceMaintenance/editMatInit.do?matId='+matId,500,200);
    }
    function _seah(){
    	__extQuery__(1);
    }
</script>
</body>
</html>
