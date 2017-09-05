<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/DTD/xhtml1-transitional.dtd">
<%@page import="java.util.List"%>
<%@page import="java.util.Map"%>

<html xmlns="http://www.w3.org/1999/xhtml"><%@ page import="com.infodms.dms.common.Constant"%><%@ page import="com.infodms.dms.common.FileConstant"%><%@taglib uri="/jstl/cout" prefix="c"%>

<head>
<%
String contextPath=request.getContextPath();
%>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title> 运费系数维护</title>
</head>
<body>
<div class="wbox">
<div class="navigation"><img src="<%=contextPath %>/img/nav.gif" />&nbsp;当前位置：储运管理>基础管理>运费系数维护</div>
<form name="fm" method="post" id="fm">
<div class="form-panel">
	<h2><img src="<%=request.getContextPath()%>/jmstyle/img/search-ico.png" style="margin-bottom: -13px;"/>运费系数维护</h2>
	<div class="form-body">
	<!-- 查询条件 begin -->
	<table class="table_query" id="subtab">
	  <tr>
		<td class="right">车系：</td>  
		<td>
	  		<select name="GROUP_ID" id="GROUP_ID" class="u-select" >
	  			<option value="">-请选择-</option>
	  			<c:if test="${list_vchl!=null}">
					<c:forEach items="${list_vchl}" var="list">
						<option value="${list.GROUP_ID }">${list.GROUP_NAME }</option>
					</c:forEach>
				</c:if>
	  		</select>
     	 </td>
	  </tr> 
	  <tr align="center">
	  <td colspan="6" class="table_query_4Col_input" style="text-align: center">
	    	  <input type="button" id="queryBtn" class="u-button u-query" value="查询" onclick="__extQuery__(1);" />
	  		  <input type="reset" id="resetButton" class="u-button u-reset" value="重置"/> 	
	    	  <input type="button" id="queryBtn" class="u-button u-submit" value="新增" onclick="addFareRatio();" />   	
	    </td>
	  </tr>
	</table>
	</div>
</div>
<!-- 查询条件 end -->
<!--分页 begin -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
<!--分页 end -->
</form>
<!--页面列表 begin -->
<script type="text/javascript" >
	var myPage;
	//查询路径           
	var url = "<%=contextPath%>/sales/storage/storagebase/FareRatioSet/fareRatioQuery.json";
	var title = null; 
	var columns = [
				{header: "序号",align:'center',renderer:getIndex},
				{header: "车系代码",dataIndex: 'GROUP_CODE',align:'center'},
				{header: "车系名称",dataIndex: 'GROUP_NAME',align:'center'},
				{header: "运费系数", dataIndex: 'RATIO_NUM', align:'center'},
				{id:'action',header: "操作", walign:'center',idth:70,sortable: false,dataIndex: 'SET_ID',renderer:myLink}
		      ];
	//跳转新增页面
	function addFareRatio()
	{
		fm.action = "<%=contextPath%>/sales/storage/storagebase/FareRatioSet/addFareRatioInit.do";
		fm.submit();
	}
	//设置超链接
	function myLink(value,meta,record)
	{
  		return String.format("<a href='javascript:void(0);' onclick='sel(\""+record.data.SERIES_ID+"\", \""+record.data.RATIO_NUM+"\", \""+value+"\")'>[修改]</a><a href='javascript:void(0);' onclick='del(\""+value+"\")'>[删除]</a>");
	}
	
	//详细页面
	function sel(groupId,ratioNum,value)
	{
	 	window.location.href='<%=contextPath%>/sales/storage/storagebase/FareRatioSet/editFareRatioInit.do?groupId='+groupId+'&ratioNum='+ratioNum+'&setId='+value;  
	}
	//删除
	function del(value){
		MyConfirm("确定删除？",delFare,[value]);
	}
	function delFare(value){
		var url='<%=contextPath%>/sales/storage/storagebase/FareRatioSet/delFareRatio.json?setId='+value;
		makeNomalFormCall(url,fareBack,'fm','queryBtn'); 
	}
	function fareBack(json)
	{
		if(json.returnValue == 1)
		{
			parent.MyAlert("操作成功！");
			fm.action = "<%=contextPath%>/sales/storage/storagebase/FareRatioSet/fareRatioSetInit.do";
			fm.submit();
		}else{
			MyAlert("操作失败！请联系系统管理员！");
		}
	}
	function doInit(){
		//sub();
	}
</script>
</div>
</body>
</html>
