<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.Constant"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>配件大类信息</title>
<% String contextPath = request.getContextPath(); %>
<script type="text/javascript">
	function doInit(){
   		loadcalendar();  //初始化时间控件
	}
</script>
</head>
<body>
<img src="<%=request.getContextPath()%>/img/nav.gif" />&nbsp;当前位置： 配件管理&gt;基本信息查询&gt;配件大类信息管理
<form name="fm" id="fm">
<!-- 查询条件 begin -->
<table class="table_query" width=100% border="0" align="center" cellpadding="1" cellspacing="1">
    <tr>
      <td class="table_query_2Col_label_6Letter">配件代码：</td>
      <td><input name="PART_CODE" type="text" id="PART_CODE"  class="middle_txt"/></td>
      <td class="table_query_2Col_label_6Letter">配件名称：</td>
      <td><input name="PART_NAME" type="text" id="PART_NAME"  class="middle_txt"/></td>
    </tr>
    <tr>
      <td class="table_query_2Col_label_6Letter">配件大类代码：</td>
      <td>
      	<input class="long_txt" id="PARTTYPE_CODE" name="PARTTYPE_CODE" value="" type="text"/>
        <input class="mark_btn" type="button" value="&hellip;" onclick="showPartType('PARTTYPE_CODE','PARTTYPE_ID','true')"/>
        <input class="normal_btn" type="button" value="清除" onclick="reset();"/>
        <input id="PARTTYPE_ID" name="PARTTYPE_ID" type="hidden" value="">
      </td>
      <td class="table_query_2Col_label_6Letter">是否需回运：</td>
      <td><select name="IS_RETURN">
      	  <option value="">请选择</option>
          <option value="<%=Constant.IS_NEED_RETURN%>">是</option>
          <option value="<%=Constant.IS_UNNEED_RETURN%>">否</option>
      </select>
      </td>
  	</tr>
   <tr>
            <td colspan="4" align="center">
            	<input id="queryBtn" class="normal_btn" type="button" name="button1" value="查询"  onclick="__extQuery__(1)"/>
			<input class="normal_btn" type="button" name="button1" value="新增"  onclick="add();"/>
		</td>
      </tr>
</table>
<!-- 查询条件 end -->
<!--分页 begin -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
<!--分页 end -->
</form>
<!--页面列表 begin -->
<script type="text/javascript" >
	var myPage;

	var url = "<%=contextPath%>/partsmanage/infoSearch/PartTypeSearch/queryPartTypeSearch.json";
				
	var title = null;

	var columns = [
				{header: "配件大类代码", dataIndex: 'PARTTYPE_CODE', align:'center'},
				{header: "配件大类名称", dataIndex: 'PARTTYPE_NAME', align:'center'},
				{header: "配件数量", dataIndex: 'PART_NUM', align:'center'},
				{header: "是否回运", dataIndex: 'IS_RETURN', align:'center'},
				{header: "是否大件", dataIndex: 'IS_MAX', align:'center'},
				{id:'action',header: "操作",sortable: false,dataIndex: 'ID',renderer:myLink ,align:'center'}
		      ];
		      
//设置超链接  begin      
	
	//设置超链接
	function myLink(value,meta,record)
	{
  		return String.format("<a href=\"#\" onclick='sel(\""+value+"\")'>[修改]</a>");
	}
	
	//详细页面
	function sel(value)
	{
	 window.location.href='<%=contextPath%>/partsmanage/infoSearch/PartTypeSearch/partTypeUpdateInit.do?Id='+value; 
	}
	
	//清除配件大类代码
	function reset(){
		document.getElementById("PARTTYPE_CODE").value = "";
	}

	
//设置超链接 end
  //新增
  function add(){
    fm.action ="<%=contextPath%>/partsmanage/infoSearch/PartTypeSearch/partTypeAddInit.do";
    fm.submit();    
  }	
</script>
<!--页面列表 end -->
</body>
</html>