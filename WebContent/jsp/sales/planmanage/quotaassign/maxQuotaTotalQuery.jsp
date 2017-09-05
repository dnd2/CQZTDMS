<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c" %>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>最大配额总量查询</title>
</head>
<body>
<div class="navigation"><img src="<%=request.getContextPath()%>/img/nav.gif" />&nbsp;当前位置：整车销售 > 计划管理 > 配额调整 > 最大配额总量查询</div>
<form method="POST" name="fm" id="fm">
  <table width="85%" border="0" align="center" class="table_query">
    <tr>
      <TD width="35%" align=center nowrap><div align="right">选择配额月度：</div></td>
      <TD width="29%" align="left" nowrap>
      	  <select name="year">
	      	  <c:forEach items="${years}" var="po">
	      		<c:choose>
					<c:when test="${po == year}">
						<option value="${po}" selected="selected">${po}</option>
					</c:when>
					<c:otherwise>
						<option value="${po}">${po}</option>
					</c:otherwise>
				</c:choose> 
			  </c:forEach>
	      </select>
            年
           <select name="month">
	      	  <c:forEach items="${months}" var="po">
	      		<c:choose>
					<c:when test="${po == month}">
						<option value="${po}" selected="selected">${po}</option>
					</c:when>
					<c:otherwise>
						<option value="${po}">${po}</option>
					</c:otherwise>
				</c:choose> 
			  </c:forEach>
	      </select>
           月
      </TD>
    </tr>
    <tr>
      <TD width="35%" align=center nowrap><div align="right">选择业务范围：</div></td>
      <TD width="29%" align="left" nowrap>
      	  <select name="areaId">
      	  	<option value="">-请选择-</option>
			<c:forEach items="${areaList}" var="po">
				<option value="${po.AREA_ID}">${po.AREA_NAME}</option>
			</c:forEach>
          </select>
      </TD>
    </tr>
    <tr>
      <td align=right nowrap>选择物料组：</td>
      <td align=left nowrap>
	      <input type="text"  name="groupCode" size="15" id="groupCode" value="" />
	      <input type="hidden" name="groupName" size="20" id="groupName" value="" />
		  <input name="button3" type="button" class="cssbutton" onclick="showMaterialGroup('groupCode','groupName','true','4')" value="..." />
      </td>
      <td width="36%" align=left nowrap>
	      <input name="button23" type="button" class="cssbutton" onClick="doQuery();" value="查询">
	      <input name="button23" type="button" class="cssbutton" onClick="doExport();" value="下载">
      </td>
    </tr>
  </table>
  
	<!--分页 begin -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
    <!--分页 end -->
</form>
<script type="text/javascript">
	
	var myPage;
	//查询路径
	var url = "<%=request.getContextPath()%>/sales/planmanage/QuotaAssign/MaxQuotaTotalQuery/maxQuotaTotalQuery.json";
				
	var title = null;

	var columns;	
	
	function doInit(){
		getWeekList();
	}
	
	function getWeekList(){
		var year = document.getElementById("year").value;
		var month = document.getElementById("month").value;
		var url = "<%=request.getContextPath()%>/sales/planmanage/QuotaAssign/MaxQuotaTotalQuery/getWeekList.json";
		makeCall(url,showWeekList,{year:year,month:month});
	}
	
	function showWeekList(json){
		columns = [
				{header: "配置代码", dataIndex: 'GROUP_CODE', align:'center'},
				{header: "配置名称", dataIndex: 'GROUP_NAME', align:'center'},
				{header: "生产订单(P)", dataIndex: 'PLAN_AMOUNT', align:'center'},
				{header: "预留资源(R)", dataIndex: 'RESERVE_AMT', align:'center'},
				{header: "最大配额(P-R)", dataIndex: 'QUOTA_AMT', align:'center'}
		      ];
		for(var i=0;i<json.list.length;i++){
			columns.push({header: json.list[i].SET_WEEK+"周", dataIndex: 'W'+i, align:'center'});
		}
	}
	
	function doQuery(){
		getWeekList();
		__extQuery__(1);
	}
	
	function doExport(){
		$('fm').action= "<%=request.getContextPath()%>/sales/planmanage/QuotaAssign/MaxQuotaTotalQuery/maxQuotaTotalExport.json";
		$('fm').submit();
	}
</script>
</body>
</html>
