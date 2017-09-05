<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="com.infoservice.mvc.context.ActionContext" %>
<%
	String contextPath = request.getContextPath();
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	ActionContext act = ActionContext.getContext();
%>
<%@taglib uri="/jstl/cout" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>收购意向新增</title>
</head>
<body onunload='javascript:destoryPrototype()' >
<div class="wbox">
<div class="navigation"><img src="<%=request.getContextPath()%>/img/nav.gif" />&nbsp;当前位置：
	  系统管理 &gt; 基础数据维护 &gt;车型配置信息导入
</div>
<form id="fm" method="post" enctype="multipart/form-data">
	<input type="hidden" name="curPage" id="curPage" value="1" />
	<input type="hidden" name="falt" id="falt" value="" />
	<input type="hidden" name="startDate" value="<%=act.getOutData("startDate") %>"/>
	<input type="hidden" name="endDate" value="<%=act.getOutData("endDate") %>"/>
	<input type="hidden" name="operator" value="<%=act.getOutData("operator") %>"/>
	<input type="hidden" name="exception" id="exception" value="<%=act.getException()==null?null:act.getException().toString() %>"/>
<table class="table_query" border="0">
	<tr>
		<td class="table_query_label" width="25%" nowrap="nowrap" align="left">1.点"<font color="red">Browse</font>"按钮,找到您所要上载的文件：</td>
		<td class="table_query_input" width="50%"  nowrap="nowrap">
			<input type="file" name="importFile" style="width:250px" id="importFile" />
		</td>
	</tr>
	<tr>
		<td class="table_query_label" width="15%" nowrap="nowrap">2.选择文件之后,点"<font color="red">确定</font>"按钮上传：</td>
		<td align="left">
			<input class="normal_btn" type="button" id="addBtn" value="确定"  onclick="confirmLoad();" />
		</td>
	</tr>
</table>
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
</form>
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
</div>
  <% if(act.getOutData("errorList")!=null){ %>
	<table class="table_list2" style="border-bottom:1px solid #DAE0EE" >
		<tr class="table_list_th">
			<td colspan="5" style="text-align:left"><img class="nav" src="<%=contextPath %>/img/subNav.gif" />&nbsp;错误列表</td>
		</tr>
	    <th>序号</th>		
		<th>行号</th>
		<th>车型代码</th>		
		<th>标准配置</th>		
		<th>错误信息</th>
		<c:forEach items="${errorList}" var="vmBean" varStatus="status">
		<tr class="table_list_row1">
		    <td>${status.count }</td>
			<td>${vmBean.row }</td>
			<td>${vmBean.modelCode }</td>
			<td>${vmBean.standardPkg }</td>
			<td>${vmBean.mark }</td>
		</tr>
	</c:forEach>
	</table>
	<%} %>
	  <% if(("ok").equals(act.getOutData("goback"))){ %>
		<table border='0' width = '100%'>
			<tr>
				<td nowrap='nowrap' colspan='6' align='center'>
						 <input class="normal_btn" id="backBtn" name="backBtn"  type="button" value="返 回" onclick="goback();"/>
				</td>		
			</tr>
		</table>
			<%} %>
<script type="text/javascript" >
var errorSize = <%=act.getOutData("errorSize") %>;
var falt = <%=act.getOutData("falt") %>;
var exception = $F('exception');


//设置列字段  
	var url = "<%=request.getContextPath()%>/sysmng/sysData/CarTypeInfo/queryCar.json?COMMAND=1";
    var title = '导入列表';
	//设置列名属性
	var columns = [
					{header: "序号", width:'35', renderer:getIndex}, //设置序号的方式
					{header: "车型代码",sortable: true, dataIndex: 'modelCode'},
					{header: "标准配置", sortable: true, dataIndex: 'standardPkg'}
			      ];


	window.onload = function(){
	var excp = autoAlertException();
		if(excp){
		    if(falt ==1 ){ 
				if(errorSize==null){
				//MyAlert(errorList);
					__extQuery__(1);
				}
			}
			}
	}
   function confirmLoad() {
    	if(vilidate()){
    		MyConfirm("确认增加?",addSpecialCar);
    	}
    }
	function vilidate(){
		var importFileName = $("importFile").value;
		if(importFileName==""){
		    MyAlert("请选择上传文件");
			return;
		}
		var index = importFileName.lastIndexOf(".");
		var suffix = importFileName.substr(index+1,importFileName.length).toLowerCase();
		if(suffix != "txt"){
		MyAlert("请选择txt格式文件");
			return;
			}
			return true;
	}
	    function addSpecialCar(){
    		disableBtn($("addBtn"));
    		$('fm').action = "<%=request.getContextPath() %>/sysmng/sysData/CarTypeInfo/carTypeImport.do?abc="+new Date();
    		$("fm").submit();
	}

	function goback(){
		fm.action = "<%=request.getContextPath()%>/sysmng/sysData/CarTypeInfo/carTypeImport.do";
		fm.submit();

	}
	
</script>
</body>
</html>