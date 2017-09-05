<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<%@ page import="java.util.List"%>
<%@ page import="java.util.Map"%>

<%
	String contextPath = request.getContextPath();
	Map<String,Object> vehicleInfo = (Map<String,Object>)request.getAttribute("vehicleInfo");
	List<Map<String,Object>> storageChangeList = (List<Map<String,Object>>)request.getAttribute("storageChangeList");
	List<Map<String,Object>> salesList = (List<Map<String,Object>>)request.getAttribute("salesList");
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml"><%@ page import="com.infodms.dms.common.Constant"%><%@ page import="com.infodms.dms.common.FileConstant"%><%@taglib uri="/jstl/cout" prefix="c"%>
<head>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>详细车籍查询</title>
<script type="text/javascript">
	function doInit(){ 
    showmsg();
   }	
 </script>
</head>
<body onunload='javascript:destoryPrototype();'>
<div class="wbox">
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;详细车籍查询</div>
<table width="85%" align="center" class="table_query">
  <tr class="tabletitle">
    <td>车辆属性</td>
  </tr>
</table>

<table class="table_list">
	<tr>
		<th width="20%" class="right">VIN：</th>
		<th width="30%" align="left">${vehicleInfo.VIN }</th>
		<th class="right">发动机号：</th>
		<th align="left">${vehicleInfo.ENGINE_NO }</th>
	</tr>
	<tr>
		<th class="right">车系：</th>
		<th align="left">${vehicleInfo.SERIES_NAME }</th>
		<th class="right">车型：</th>
		<th align="left">${vehicleInfo.MODEL_NAME }</th>
	</tr>
	<tr>

		<th class="right">物料编号：</th>
		<th align="left">${vehicleInfo.MATERIAL_CODE }</th>
		<th class="right">物料名称：</th>
		<th align="left">${vehicleInfo.MATERIAL_NAME }</th>
	</tr>
	<tr>
		<th class="right">颜色：</th>
		<th align="left" colspan="3">${vehicleInfo.COLOR }</th>
	</tr>
	<c:if test="${!empty vehicleOrderNo}">
		<tr>
			<th align='right'>ERP销售批售单号：</th>
			<th align="left" >${vehicleOrderNo.ERP_ORDER }</th>
			<th align='right'>送车交接单号：</th>
			<th align="left" >${vehicleOrderNo.SENDCAR_ORDER_NUMBER }</th>
		</tr>
		<tr>
			<th align='right'>DMS发运批售单号：</th>
			<th align="left" colspan="3">${vehicleOrderNo.DELIVERY_NO }</th>
		</tr>
	</c:if>
</table>
<%
 	if(null != storageChangeList && storageChangeList.size()>0){
 %>
 <br>
 <table width="85%" align="center" class="table_query">
  <tr class="tabletitle">
    <td>库存状态变更日志</td>
  </tr>
</table>
<table class="table_list" id="storageId">
	<tr class=tabletitle>
		<th>库存变更类型</th>
		<th>审核人</th>
		<th>变更时间 </th>
		<th>变更描述 </th>
		<th>相关单据号 </th>
		<th>仓库名称 </th>
		<th>对应经销商 </th>
	</tr>
	<c:forEach items="${storageChangeList}" var="storageChangeList" varStatus="vstatus">
		<tr class="<c:if test='${vstatus.index%2==0}'>table_list_row1</c:if><c:if test='${vstatus.index%2!=0}'>table_list_row2</c:if>">
			<td>
				<script>document.write(getItemValue(${storageChangeList.CHANGE_NAME }));</script>
			</td>
			<td>
			${storageChangeList.CHECK_NAME }
			</td>
			<td>
			${storageChangeList.CHANGE_DATE }
			</td>
			<td>
			${storageChangeList.CHANGE_DESC }
			</td>
			<td>
			${storageChangeList.DOC_NO }
			</td>
			<td>
			${storageChangeList.WAREHOUSE_NAME }
			</td>
			<td>
			${storageChangeList.DEALER_SHORTNAME }
			</td>
		</tr>
	</c:forEach>
</table>
<%
    }

	if(null != salesList && salesList.size()>0){
%>
 <br>
 <table width="85%" align="center" class="table_query">
  <tr class="tabletitle">
    <td>车辆销售状态日志</td>
  </tr>
</table>
<table class="table_list" id="salesId">
	<tr class=tabletitle>
		<th>改变名称</th>
		<th>改变时间 </th>
		<th>改变描述 </th>
		<th>相关单据号 </th>
	</tr>
	<c:forEach items="${salesList}" var="salesList" varStatus="vstatus">
		<tr class="<c:if test='${vstatus.index%2==0}'>table_list_row1</c:if><c:if test='${vstatus.index%2!=0}'>table_list_row2</c:if>">
			<td>
			<div align="center">
				<script>document.write(getItemValue(${salesList.CHANGE_NAME }));</script>
			</div>
			</td>
			<td>
			<div align="center">${salesList.CHANGE_DATE }</div>
			</td>
			<td>
			<div align="center">${salesList.CHANGE_DESC }</div>
			</td>
			<td>
			<div align="center">${salesList.DOC_NO }</div>
			</td>
		</tr>
	</c:forEach>
</table>

<%
	}
%>
<table  class="table_query">
    <tr align="center">      
      <td>
      	<input class="normal_btn" type="button" name="button" value="关闭"  onclick="_hide();" />
      </td>
  	</tr>	
   </table>  
</div>
<script type="text/javascript">
    function showmsg(){
    	var rowNum = document.getElementById("storageId").rows.length;//行
    	//var colNum = document.getElementById("storageId").rows[0].cells.length//列 
    	for(var i=1;i<rowNum ;i++){
    		var cell = document.getElementById('storageId').rows[i].cells[2];
    	    var cellHTML = cell.innerHTML;
    	    var cellText = cell.innerText;
    	    if(cellText.length > 16){
    	    document.getElementById("storageId").rows[i].cells[2].innerText = cellText.substr(0,16) + "...";
    	    document.getElementById("storageId").rows[i].cells[2].title = cellText;
    	    }
        }

        var s_r = document.getElementById("salesId").rows.length;
        for (var j=1;j<s_r;j++){
        	var cell = document.getElementById('salesId').rows[i].cells[2];
    	    var cellHTML = cell.innerHTML;
    	    var cellText = cell.innerText;
    	    if(cellText.length > 16){
    	    document.getElementById("salesId").rows[i].cells[2].innerText = cellText.substr(0,16) + "...";
    	    document.getElementById("salesId").rows[i].cells[2].title = cellText;
    	    }
        }
   }	
 </script>
</body>
</html>