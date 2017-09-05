<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%
	String contextPath = request.getContextPath();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />

<title>推荐人部门维护</title>
<style>
.img {
	border: none
}
</style>
</head>

<body onunload='javascript:destoryPrototype()' onload="__extQuery__(1)">
<form name="fm" method="post">
<div class="wbox">
<div class="navigation"><img src="<%=contextPath %>/img/nav.gif" />&nbsp;当前位置：
系统管理 &gt; 组织管理 &gt;推荐人部门维护</div>
<table class="table_query" border="0">
	<tr>
		<td nowrap="nowrap" align="center">部门名称：
		 <input type="hidden" name="curPage" id="curPage" value="1" />
		 <input id="DEPT_NAME" name="DEPT_NAME"  class="middle_txt" datatype="1,is_null,30" type="text"  />&nbsp;
		</td>
		<td align="left" width="350">
		<input  class="normal_btn" type="button" value="查 询" onclick="__extQuery__(1)" id="queryBtn" />
		<input class="normal_btn" type="button" value="新 增"  onclick="window.location.href='<%=contextPath%>/jsp/systemMng/orgMng/cmdDptAdd.jsp'" />
		</td>
	</tr>
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
</table>
<jsp:include page="${contextPath}/queryPage/pageDiv.html" /></div>
</form> 

<script type="text/javascript">
	var url = "<%=contextPath%>/sysmng/orgmng/DlrDepPro/queryAllDep.json";
	var title= null;
		var columns = [  
						{header: "序号",renderer:getIndex}, //设置序号的方式
						{header: "部门代码", orderCol:"DEPT_CODE",dataIndex:'deptCode'},
						{header: "部门名称", orderCol:"DEPT_NAME",dataIndex:'deptName'}, 
						{header: "操作", dataIndex: 'dlrDeptId', renderer:myLink}
					  ];
	//Ajax返回调用函数 设置字段、列名属性参数
     function myLink(value ){
	   var str = "";
       str += "<a href=\"<%=contextPath%>/sysmng/orgmng/DlrDepPro/findDeptAndEmp.do?dlrDeptId="+value+"\">[维护]</a>"
        +"<a href='#' onclick='transfer("+value+")'>[删除]</a>" ;
       return String.format(str);
    } 
    function transfer(value)
    {		
         MyConfirm("是否确认删除?",deleteRs,[value]);
    }
    function deleteRs(value)
    { 
    	makeFormCall("<%=contextPath%>/sysmng/orgmng/DlrDepPro/deptDelete.json?dlrDeptId="+value,showResult,'fm');
    }
    function showResult(json)
    {
    	if(json.ACTION_RESULT=='1')
    	{
    	 	window.location.href = "<%=request.getContextPath()%>/sysmng/orgmng/DlrDepPro/queryAllDep.do";
    	}
    	if(json.ACTION_RESULT=='0')
		{ 
			MyAlert("部门删除失败！");
		}
    } 
</script>
</body>
</html>