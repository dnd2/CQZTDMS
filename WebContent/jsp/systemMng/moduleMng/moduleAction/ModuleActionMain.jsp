<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c"%>
<%
	String contextPath = request.getContextPath();
%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>模块操作</title>
</head>
<body onload="__extQuery__(1);">
<div class="wbox">
<div class="navigation"><img src="<%=request.getContextPath()%>/img/nav.gif" />&nbsp;当前位置：系统管理&gt;权限管理&gt; 模块操作维护</div>
<form method="post" name ="fm" id="fm">
    <table class="table_query" >
	    <tr id="groupId">
	        <td width="10%" align="center">模块名称：</td>
	        <td width="20%"><input class="middle_txt" type="text" id="moduleName" name="moduleName" jset="para"></td>
	        <td width="10%" align="center">&nbsp;</td>
	        <td width="20%">&nbsp;</td>
	        <td width="10%" align="center">&nbsp;</td>
	        <td width="20%">&nbsp;</td>	        
        </tr>
        <tr>
            <td colspan=2></td>
            <td align='center' colspan=2>
            <input name="queryBtn" id="queryBtn" type="button" class="normal_btn" onclick="__extQuery__(1);" value="查 询"/>
            <input name="addBtn" id="addBtn" type="button" class="normal_btn" onclick="add();" value="新 增"/>
            </td>
            <td colspan=2></td>
        </tr>
	</table>
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
</form>
</div>

<script language="javascript">
	loadcalendar();  //初始化时间控件
	var myPage;
	var url = "<%=contextPath%>/sysmng/modulemng/ModuleAction/getModuleActionList.json";
	var title = null;
	var columns = [
				{header: "序号", align:'center', renderer:getIndex},
                {header: "模块名称", dataIndex: 'FUNC_NAME', align:'center'},
                {header: "操作代码", dataIndex: 'ACTION_CODE', align:'center'},
                {header: "操作名称", dataIndex: 'ACTION_NAME', align:'center'},
                {header: "操作",  align:'center',sortable: false,dataIndex: 'ACTION_ID',renderer:showAction}
		      ];

    //设置超链接  begin	
	function showAction(value, meta, record) {
          var actionId = record.data.ACTION_ID;
          var formatString = "<a href='<%=contextPath%>/sysmng/modulemng/ModuleAction/ActionModifyInit.do?actionId="+actionId+"'>[修改]</a>";          
          return String.format(formatString);
    }
    //新增方法
	function add() {
		fm.action="<%=contextPath%>/sysmng/modulemng/ModuleAction/ActionAddInit.do";
		fm.submit();
  }
        
</script>

</body>
</html>