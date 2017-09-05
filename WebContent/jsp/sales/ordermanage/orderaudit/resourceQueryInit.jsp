<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.Constant"%>

<%
	String contextPath = request.getContextPath();
%>
<%@taglib uri="/jstl/cout" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>可利用资源查询</title>
<script type="text/javascript">
function txtClr(valueId) {
	document.getElementById(valueId).value = '' ;
}

</script>
</head>
<body onunload='javascript:destoryPrototype();' onload="__extQuery__(1);"> 
<div class="wbox">
	<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置：  整车销售 &gt;  销售订单管理  &gt;订单审核 &gt; 可利用资源查询</div>
	<form id="fm" name="fm" method="post">
		<input type="hidden" name="curPage" id="curPage" value="1" />
		<input type="hidden" id="dlrId" name="dlrId" value="" />
		<table class="table_query" border="0">
		<tr>
      <td align="right" nowrap >资源状态：</td>
      <td align="left" nowrap >
      	<select name="resStatus" class="short_sel">
			<option value="">-请选择-</option>
			<option value="0">有</option>
			<option value="1">无</option>
        </select>
      </td>
      <td align="right" nowrap >业务范围：</td>
      <td align="left" nowrap >
      	<select name="areaId" class="short_sel">
			<option value="">-请选择-</option>
			<c:forEach items="${areaList}" var="po">
				<option value="${po.AREA_ID}">${po.AREA_NAME}</option>
			</c:forEach>
        </select>
      </td>
	</tr>
    <tr>
       <td align="right" nowrap>
					<div align="right">
						物料选择组：
					</div>
				</td>
				<td >
					<input type="text" class="middle_txt" id="materialCode" class="middle_txt" name="materialCode"  value=""  />
					<input type="hidden" name="materialName" size="20" id="materialName" value="" />
       				<input type="button" id="bt1" value="..." class="mini_btn"  onclick="showMaterialGroup('materialCode','materialName','true','');" />
       				<input type="button" class="normal_btn" onclick="txtClr('materialCode');" value="清 空" id="clrBtn" />
				</td>
				 <td align="right" valign="top" nowrap="nowrap" 

class="table_info_3col_label_6Letter">库存组织：</td>
	    
	    <td align="left" valign="top" nowrap="nowrap" 

class="table_info_3col_input">
	    	<select name="warehouseId" onchange="houseChange();">
	    	<option value="">--请选择--</option>
				<c:forEach items="${wareHouseList}" 

var="po">
					<c:choose>
						<c:when 

test="${po.WAREHOUSE_ID == map.WAREHOUSE_ID}">
							<option 

value="${po.WAREHOUSE_ID}" selected="selected">${po.WAREHOUSE_NAME}

</option>
						</c:when>
						<c:otherwise>
							<option 

value="${po.WAREHOUSE_ID}">${po.WAREHOUSE_NAME}</option>
						</c:otherwise>
					</c:choose> 
				</c:forEach>
	      	</select>
	    </td>
				
				<td class="table_query_3Col_input">
					<input type="button" class="normal_btn" onclick="__extQuery__(1);" value=" 查  询  " id="queryBtn" />
					<input type="button" class="normal_btn" onclick="exports();" value=" 导  出  " id="downLoads" />
				</td>
        <td align="right">
             页面大小：<input name="pageSize" id="pageSize" type="text" class="mini_txt" value="10" datatype="0,isDigit,3"/>
        </td>

			</tr>
		</table>
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
		</form>
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
</div>

<script type="text/javascript">
	var myPage;
	
	var url = "<%=contextPath%>/sales/ordermanage/orderaudit/ResourceQuery/resourceQueryList.json?COMMAND=1";
	
	var title = null;
	
	var columns = [
				{header: "物料代码", dataIndex: 'MATERIAL_CODE', align:'center'},
				{header: "物料名称", dataIndex: 'MATERIAL_NAME', align:'center'},
				{header: "颜色", dataIndex: 'COLOR_NAME', align:'center'},
				{header: "库存数量", dataIndex: 'RESOURCE_AMOUNT', align:'center'},
				{header: "锁定资源数量", dataIndex: 'REQ_AMOUNT', align:'center',renderer:lockDetail},
				{header: "可用库存（库存数量-锁定资源数量）", dataIndex: 'AVA_STOCK', align:'center'},
				{header: "未满足常规订单数量", dataIndex: 'UNENTITY_STOCK', align:'center',renderer:unentityDetail}
		      ];
	function lockDetail(value,meta,record){
		var material_id=record.data.MATERIAL_ID;
		return String.format("<a href='#' onclick='lockDetailInfo(\""+ material_id +"\")'>"+value+"</a>");
	}
	function lockDetailInfo(material_id){
		var url="<%=contextPath%>/sales/ordermanage/orderaudit/ResourceQuery/resourceQueryDetail.do?material_id="+material_id;
		OpenHtmlWindow(url,1000,500);
	}
	
	//未满足常规订单
	function unentityDetail(value,meta,record){
		var material_id=record.data.MATERIAL_ID;
		return String.format("<a href='#' onclick='unentityDetailInfo(\""+ material_id +"\")'>"+value+"</a>");
	}
	
	function unentityDetailInfo(material_id){
		var url="<%=contextPath%>/sales/ordermanage/orderaudit/ResourceQuery/resourceQueryUnentityDetail.do?material_id="+material_id;
		OpenHtmlWindow(url,1000,500);
	}
	
	
	function exports(){
		if(submitForm('fm')){
			document.fm.action='<%=request.getContextPath()%>/sales/ordermanage/orderaudit/ResourceQuery/resourceDownLoad.json';
			document.fm.target="_self";
			document.fm.submit();
		}
	}
</script>    
</body>
</html>