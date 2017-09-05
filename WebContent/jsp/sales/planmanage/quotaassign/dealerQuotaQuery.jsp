<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="com.infodms.dms.common.Constant" %>
<%@taglib uri="/jstl/cout" prefix="c" %>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>配额查询</title>
</head>
<body>
<div class="navigation"><img src="<%=request.getContextPath()%>/img/nav.gif" />&nbsp;当前位置：整车销售 > 计划管理 > 配额分配 > 配额查询</div>
<form method="POST" name="fm" id="fm">
  <table class="table_query" align=center width="95%">
    <tr>
      <td align=right nowrap>&nbsp;</td>
      <td align=right nowrap>
	      <input type="radio" name="dateType" value="1" class="nobgcolor" checked="checked" />
	       选择配额月份：
      </td>
      <td colspan="3" align=left nowrap>
	      <select name="year1">
		      <c:forEach items="${years}" var="po">
		      	<c:choose>
					<c:when test="${po == curYear}">
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
					<c:when test="${po == curMonth}">
						<option value="${po}" selected="selected">${po}</option>
					</c:when>
					<c:otherwise>
						<option value="${po}">${po}</option>
					</c:otherwise>
				</c:choose> 
			  </c:forEach>
	      </select>
		  月
	  </td>
    </tr>
    <tr>
      <td align=right nowrap width="20%">&nbsp;</td>
      <td align=right nowrap width="19%">
	      <input type="radio" name="dateType" value="2" class="nobgcolor" />
	       选择配额周度：
      </td>
      <td colspan="3" align=left nowrap>
	      <select name="year2">
		      <c:forEach items="${years}" var="po">
		      	<c:choose>
					<c:when test="${po == curYear}">
						<option value="${po}" selected="selected">${po}</option>
					</c:when>
					<c:otherwise>
						<option value="${po}">${po}</option>
					</c:otherwise>
				</c:choose> 
			  </c:forEach>
	      </select>
		  年
		  <select name="week">
		      <c:forEach items="${weeks}" var="po">
		      	<c:choose>
					<c:when test="${po == curWeek}">
						<option value="${po}" selected="selected">${po}</option>
					</c:when>
					<c:otherwise>
						<option value="${po}">${po}</option>
					</c:otherwise>
				</c:choose> 
			  </c:forEach>
	      </select>
		  周
	  </td>
    </tr>
    <tr>
	  <td align=right nowrap>&nbsp;</td>
      <td align=right nowrap>选择业务范围：</td>
      <td width="17%" align="left" nowrap>
      	<select name="areaIds" id="areaIds" class="short_sel">
      		<option value="">--请选择--</option>
      		<c:forEach items="${areaIds }" var="area" >
      			<option value="${area.AREA_ID }">${area.AREA_NAME }</option>
      		</c:forEach>
      	</select>
	  </td>
      <td width="4%" align="left" nowrap>&nbsp;</td>
      <td width="40%">
      </td>
    </tr>
	<tr>
	  <td align=right nowrap>&nbsp;</td>
      <td align=right nowrap>选择物料组：</td>
      <td width="17%" align="left" nowrap>
      	<input type="text" class="middle_txt" name="groupCode" size="20" id="groupCode" value="" />
      	<input type="hidden" name="groupName" size="20" id="groupName" value="" />
		<input name="button3" type="button" class="mini_btn" onclick="showMaterialGroup('groupCode','groupName','true','4')" value="..." />
		<input class="normal_btn" type="button" value="清空" onclick="clrTxt('groupCode');"/>
	  </td>
      <td width="4%" align="left" nowrap>&nbsp;</td>
      <td width="40%"></td>
    </tr>
    <tr>
	  <td align=right nowrap>&nbsp;</td>
      <td align=right nowrap>去零（去掉配额为0的数据）：</td>
      <td width="17%" align="left" nowrap>
      	<input type="radio" name="isPassZero" id="yes_zero" value="<%=Constant.IF_TYPE_YES%>" ><label for="yes_zero">是</label>
      	<input type="radio" name="isPassZero" id="no_zero" value="<%=Constant.IF_TYPE_NO%>" checked ><label for="no_zero">否</label>
	  </td>
      <td width="4%" align="left" nowrap>&nbsp;</td>
      <td width="40%">
      	  <input name="queryBtn" type=button class="cssbutton" onClick="__extQuery__(1);" value="查询">
          <input name="button2" type=button class="cssbutton" onClick="doExport();"value="下载"> 
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
	var url = "<%=request.getContextPath()%>/sales/planmanage/QuotaAssign/DealerQuotaQuery/dealerQuotaQuery.json";;
				
	var title = null;

	var columns = [
					{header: "配额周度", dataIndex: 'QUOTA_DATE', align:'center'},
					{header: "经销商代码", dataIndex: 'ORG_CODE', align:'center'},
					{header: "经销商名称", dataIndex: 'ORG_NAME', align:'center'},
					{header: "产品组代码", dataIndex: 'GROUP_CODE', align:'center'},
					{header: "产品组名称", dataIndex: 'GROUP_NAME', align:'center'},
					{header: "配额数量", dataIndex: 'QUOTA_AMT', align:'center'}
			      ];	
		
	function doExport(){
		$('fm').action= "<%=request.getContextPath()%>/sales/planmanage/QuotaAssign/DealerQuotaQuery/dealerQuotaExport.do";
		$('fm').submit();
	}
	
	function clrTxt(txtId){
    	document.getElementById(txtId).value="";
    }
</script>
</body>
</html>
