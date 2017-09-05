<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="java.util.*"%>
<%@ page import="com.infodms.dms.common.Constant"%>
<%@taglib uri="/jstl/cout" prefix="c"%>
<%
	String contextPath = request.getContextPath();
%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>模块参数维护</title>
</head>
<body  onload="">
<div class="wbox">
	<div class="navigation"><img src="<%=contextPath %>/img/nav.gif" />当前位置>系统管理>系统业务参数维护>模块参数维护</div>
<form method="post" name="fm" id="fm">
   <table class="table_query" >
     
     <tr align="center">
         <td colspan="2"></td>
         <td colspan="2">
             <input id="queryBtn" name="BtnQuery" type="button" class="cssbutton" onclick="__extQuery__(1)" value="查询" />
             &nbsp;&nbsp;
             <input id="create" name="create" type="button" class="cssbutton" onClick="addPara();" value="新增" />
         </td>
         <td colspan="2"></td>
     </tr>
   </table>
  <!--分页 begin -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
  <!--分页 end --> 
</form>
</div>
<!--页面列表 begin -->
<script type="text/javascript" >
	var myPage;
    //查询路径
	var url ="<%=contextPath%>/sysbusinesparams/funcparameter/FuncParameterManager/getParaList.json";		
		
	var title = null;

	var columns = [
	   			{header: "序号",align:'center',renderer:getIndex},
	   			{header: "模块代码",dataIndex: 'FUNC_CODE',align:'center'},	
	   			{header: "模块名称",dataIndex: 'FUNC_NAME',align:'center'},
	   			{header: "参数名",dataIndex: 'PARA_NAME',align:'center'},
	   			{header: "参数类型",dataIndex: 'PARA_TYPE',align:'center'},
	   			{header: "控件ID",dataIndex: 'ELEMENT_ID',align:'center'},
	   			{header: "操作",  align:'center',sortable: false,dataIndex: 'PARA_ID',renderer:modifyPara}
	   	      ];

	/*修改参数配置*/
	function modifyPara(value,metaDate,record){
		var formatString="";
		formatString = "<a href='<%=contextPath%>/sysbusinesparams/funcparameter/FuncParameterManager/modifyParameterInit.do?funcId="+record.data.PARA_ID+"'>[修改]</a>";		
		return String.format(formatString);
	}

	/*新增参数配置*/
	function addPara(){
		fm.action="<%=contextPath%>/sysbusinesparams/funcparameter/FuncParameterManager/addParameter.do";
		fm.submit();		
	}
	
		 
</script>
<!--页面列表 end -->
</body>
</html>
