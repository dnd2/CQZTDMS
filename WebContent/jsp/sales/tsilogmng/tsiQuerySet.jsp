<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml"><%@ page import="com.infodms.dms.common.Constant"%><%@ page import="com.infodms.dms.common.FileConstant"%><%@taglib uri="/jstl/cout" prefix="c"%>
<%@ page import="java.util.*"%>
<%@taglib uri="/jstl/cout" prefix="c" %>
<%@ taglib uri="/WEB-INF/tld/fmt.tld" prefix="fmt" %> 
<%
	String contextPath = request.getContextPath();
%>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title></title>
</head>
<body>
  <div class=navigation><img src="<%=request.getContextPath()%>/img/nav.gif">&nbsp;选定接口表
</div>
  <form method="post" name="fm" id="fm">
  <TABLE class=table_query >
     <tr>
     	<td class="tab_info_label" align="right">表名：</td>
		<td class="tab_info_value">
			 <input class="middle_txt" type="text" maxlength="20"  maxlength="30"  id="tabName" name="tabName"/>
		</td>
		<td class="tab_info_label" align="right">接口描述：</td>
		<td class="tab_info_value">
			 <input class="middle_txt" type="text" maxlength="20"  maxlength="30"  id="tableName" name="tableName"/>
		</td>
     </tr>
     <tr>
	 	<td align="center" colspan="4">
	 	<input name="button" type="button" class="normal_btn" onclick="__extQuery__(1);"  value="查询" />
	 	<input name="button" type="button" class="normal_btn" onclick="_hide();"  value="关闭" />
	 	</td>
 	</tr>
</TABLE>
<!-- 查询条件 end -->
  <!--分页 begin -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
 <!--分页 end --> 
</form> 
<br/>
<!--页面列表 begin -->
<script type="text/javascript" >
var myPage;
//查询路径
	var url = "<%=contextPath%>/sales/tsimng/TsiLogManager/querySetQuery.json";
	var title = null;
	var columns = [
			{header: "序号",renderer:getIndex},
			{id:'action',header: "选择", width:'8%',renderer:myCheckBox},
			{header: "表描述",dataIndex: 'COMMENTS',style:'text-align:left;'},
			{header: "表名称",sortable: false,dataIndex: 'TABLE_NAME',style:'text-align:left;'}
	      ];
	
	function myCheckBox(value,metaDate,record){
		return String.format("<input type='radio' id='code' name='code' value='" + value + "'  onclick='setForRedioValue(\""+record.data.COMMENTS+"\",\""+record.data.TABLE_NAME+"\");'/>");
	}
	
	function setForRedioValue(desc,name){
		if(parent.document.getElementById('inIframe'))
		{
			parent.document.getElementById('inIframe').contentWindow.document.getElementById("tableDesc").value = desc;
			parent.document.getElementById('inIframe').contentWindow.document.getElementById("tableName").value = name;	
		}
	   _hide();
	}
	__extQuery__(1);

	</script>  
</body>
</html>