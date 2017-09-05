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
	      <td align="right" nowrap="nowrap">&nbsp;</td>
	      <td align="right" nowrap="nowrap">配额类型：</td>
	      <c:if test="${dutyType==10431001}">
	      	<td colspan="3" align="left" nowrap="nowrap">
	      	  <script type="text/javascript">
	      	  	genSelBoxExp("quotaType",<%=Constant.QUOTA_TYPE%>,"",false,"short_sel","onchange='showType(this.options[this.options.selectedIndex].value);'","false",'');
	      	  </script>
	      </td>
	      </c:if>
	        <c:if test="${dutyType==10431003}">
		        <td colspan="3" align="left" nowrap="nowrap">
		      	  <select id="quotaType" name="quotaType" class="short_sel"  onchange="showType(this.options[this.options.selectedIndex].value);">
		      	  <option value="11061001" selected="selected">区域配额</option>
		      	    <option value="11061002">经销商配额</option>
		      	  </select>
		      	</td>
	        </c:if>
	        <c:if test="${dutyType==10431004}">
		        <td colspan="3" align="left" nowrap="nowrap">
		      	  <select id="quotaType" name="quotaType" class="short_sel"  onchange="showType(this.options[this.options.selectedIndex].value);">
		      	    <option value="11061002 ">经销商配额</option>
		      	  </select>
		      	</td>
	        </c:if>
	      
      </tr>
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
      <td align="right" nowrap="nowrap">&nbsp;</td>
      <td align="right" nowrap="nowrap">选择业务范围：</td>
      <td align="left" nowrap="nowrap">
	      <select name="areaId">
      	  	  <option value="">-请选择-</option>
			  <c:forEach items="${areaList}" var="po">
			  	<option value="${po.AREA_ID}">${po.AREA_NAME}</option>
			  </c:forEach>
	      </select>
      </td>
      <td align="left" nowrap="nowrap">&nbsp;</td>
      <td></td>
    </tr>
	<tr>
	  <td align=right nowrap>&nbsp;</td>
      <td align=right nowrap>选择物料组：</td>
      <td width="17%" align="left" nowrap>
      	<input type="text" name="groupCode" size="20" id="groupCode" value="" />
      	<input type="hidden" name="groupName" size="20" id="groupName" value="" />
		<input name="button3" type="button" class="mini_btn" onclick="showMaterialGroup('groupCode','groupName','true','4')" value="..." />
		<input class="mark_btn" type="button" value="清空" onclick="clrTxt('groupCode');"/>
	  </td>
      <td width="4%" align="left" nowrap>&nbsp;</td>
      <td width="40%"></td>
    </tr>
<!--    <tr>-->
<!--      <td align=right nowrap>&nbsp;</td>-->
<!--      <td align=right nowrap>选择销售组织：</td>-->
<!--      <td width="17%" align="left" nowrap>-->
<!--        <input type="text" name="orgCode" size="20" value="" id="orgCode"/>-->
<!--        <input type="hidden" name="orgId" size="20" value="" id="orgId"/>-->
<!--		<input class="mini_btn" id="dlbtn1" name="dlbtn1" type="button" onclick="showOrg('orgCode','orgId','true', '${orgId}')" value="..." />-->
<!--		<input class="mark_btn" type="button" value="清空" onclick="clrTxt('orgCode');"/>-->
<!--      </td>-->
<!--      <td align="left" nowrap>&nbsp;</td>-->
<!--      <td></td>-->
<!--    </tr>-->
    <tr>
      <td align="right" nowrap>&nbsp;</td>
      <td align="right" nowrap>选择经销商：</td>
      <td align="left">
	      <input type="text" name="dealerCode" size="20" value="" id="dealerCode"/>
	      <c:if test="${dutyType==10431001}">
	      	<input class="mini_btn" id="dlbtn2" name="dlbtn2" type="button" onclick="showOrgDealer('dealerCode','','true', '${orgId}')" value="..." />
	      </c:if>
	      <c:if test="${dutyType==10431002}">
	      	<input class="mini_btn" id="dlbtn2" name="dlbtn2" type="button" onclick="showOrgDealer3('dealerCode','','true', '${orgId}')" value="..." />
	      </c:if>
	      <c:if test="${dutyType==10431003}">
	      	<input class="mini_btn" id="dlbtn2" name="dlbtn2" type="button" onclick="showOrgDealer3('dealerCode','','true', '${orgId}')" value="..." />
	      </c:if>
	      <c:if test="${dutyType==10431004}">
	      	<input class="mini_btn" id="dlbtn2" name="dlbtn2" type="button" onclick="showOrgDealer6('dealerCode','','true', '${orgId}')" value="..." />
	      </c:if>
		  <input class="mark_btn" type="button" value="清空" onclick="clrTxt('dealerCode');"/>
	  </td>
      <td align=left>&nbsp;</td>
    </tr>
    <tr>
      <td nowrap colspan="5" align="right">
      	  <input name="button2" type=button class="cssbutton" onClick="totalQuery();" value="汇总查询">
          <input name="button2" type=button class="cssbutton" onClick="detailQuery();" value="明细查询">
          <input name="button2" type=button class="cssbutton" onClick="totalDownload();"value="汇总下载">
          <input name="button2" type=button class="cssbutton" onClick="detailDownload();"value="明细下载">
          页面大小：<input name="pageSize" id="pageSize" type="text" class="mini_txt" value="10" datatype="0,isDigit,3"/>
      </td>
    </tr>
  </table>
  
	<!--分页 begin -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
    <!--分页 end -->
</form>
<script type="text/javascript">
	function doInit(){
		setDate() ;
		showType(document.getElementById("quotaType").value);
	}
	
	var myPage;
	//查询路径
	var url;
				
	var title = null;

	var columns;		
	
	var calculateConfig;         
	
	function totalQuery(){
		calculateConfig = {bindTableList:"myTable",subTotalColumns:"MODEL_NAME|MODEL_NAME",totalColumns:"MODEL_NAME"};
		url = "<%=request.getContextPath()%>/sales/planmanage/QuotaAssign/OemQuotaQuery/oemQuotaTotalQuery.json";
		columns = [
				{header: "产品组代码", dataIndex: 'GROUP_CODE', align:'center'},
				{header: "产品组名称", dataIndex: 'GROUP_NAME', align:'center'},
				{header: "车型代码", dataIndex: 'MODEL_CODE', align:'center'},
				{header: "车型名称", dataIndex: 'MODEL_NAME', align:'center'},
				{header: "配额数量", dataIndex: 'QUOTA_AMT', align:'center'}
		      ];
		__extQuery__(1);
	}
	
	function detailQuery(){
		calculateConfig = {};
		url = "<%=request.getContextPath()%>/sales/planmanage/QuotaAssign/OemQuotaQuery/oemQuotaDetailQuery.json";
		var type = document.getElementById("quotaType").value;
		if(type == '<%=Constant.QUOTA_TYPE_01%>'){
			columns = [
					{header: "配额周度", dataIndex: 'QUOTA_DATE', align:'center'},
					{header: "区域代码", dataIndex: 'ORG_CODE', align:'center'},
					{header: "区域名称", dataIndex: 'ORG_NAME', align:'center'},
					{header: "产品组代码", dataIndex: 'GROUP_CODE', align:'center'},
					{header: "产品组名称", dataIndex: 'GROUP_NAME', align:'center'},
					{header: "配额数量", dataIndex: 'QUOTA_AMT', align:'center'}
			      ];
		}else if(type == '<%=Constant.QUOTA_TYPE_03%>'){
			columns = [
					{header: "配额周度", dataIndex: 'QUOTA_DATE', align:'center'},
					{header: "车厂代码", dataIndex: 'ORG_CODE', align:'center'},
					{header: "车厂名称", dataIndex: 'ORG_NAME', align:'center'},
					{header: "产品组代码", dataIndex: 'GROUP_CODE', align:'center'},
					{header: "产品组名称", dataIndex: 'GROUP_NAME', align:'center'},
					{header: "配额数量", dataIndex: 'QUOTA_AMT', align:'center'}
			      ];
		}else{
			columns = [
					{header: "配额周度", dataIndex: 'QUOTA_DATE', align:'center'},
					{header: "经销商代码", dataIndex: 'ORG_CODE', align:'center'},
					{header: "经销商名称", dataIndex: 'ORG_NAME', align:'center'},
					{header: "产品组代码", dataIndex: 'GROUP_CODE', align:'center'},
					{header: "产品组名称", dataIndex: 'GROUP_NAME', align:'center'},
					{header: "配额数量", dataIndex: 'QUOTA_AMT', align:'center'}
			      ];
		}
		__extQuery__(1);
	}
	
	function myLink1(value,meta,record){
		return String.format("<a href='#'>"+value+"</a>");
	}
	
	function myLink2(value,meta,record){
		return String.format("<a href='#'>"+value+"</a>");
	}
	
	function totalDownload(){
		$('fm').action= "<%=request.getContextPath()%>/sales/planmanage/QuotaAssign/OemQuotaQuery/oemQuotaTotalExport.json";
		$('fm').submit();
	}
	
	function detailDownload(){
		$('fm').action= "<%=request.getContextPath()%>/sales/planmanage/QuotaAssign/OemQuotaQuery/oemQuotaDetailExport.json";
		$('fm').submit();
	}
	
	function showType(arg){
		if(arg == '<%=Constant.QUOTA_TYPE_01%>'||arg == '<%=Constant.QUOTA_TYPE_03%>'){
			//document.getElementById("orgCode").disabled = false;
			//document.getElementById("dlbtn1").disabled = false;
			document.getElementById("dealerCode").disabled = true;
			document.getElementById("dlbtn2").disabled = true;
		}else{
			//document.getElementById("orgCode").disabled = true;
			//document.getElementById("dlbtn1").disabled = true;
			document.getElementById("dealerCode").disabled = false;
			document.getElementById("dlbtn2").disabled = false;
		}
	}
	
	function clrTxt(txtId){
    	document.getElementById(txtId).value="";
    }
	
	function setDate() {
    	var oYear = document.getElementById("year1") ;
    	var oMonth = document.getElementById("month") ;
    	var sDate = document.getElementById("sys_date__").value ;
    	
    	var iYear = parseInt(sDate.split(",")[0]) ;
    	var iMonth = parseInt(sDate.split(",")[1]) ;
    	
    	var iCurrentMonth = iMonth + 1 ;
    	
    	if(iCurrentMonth > 12) {
    		iYear += 1 ;
    		iMonth = iCurrentMonth - 12 ;
    	}
    	
    	oYear.options.value = iYear ;
    	oMonth.options.value = iMonth ;
    }
</script>
</body>
</html>
