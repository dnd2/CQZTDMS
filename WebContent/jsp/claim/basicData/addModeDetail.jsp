<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>添加失效模式-新增</title>
<% String contextPath = request.getContextPath(); %>
</head>
<body onload = "__extQuery__(1);">
<div class="wbox">
     <div class="navigation">
         <img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 售后服务管理&gt;索赔基本数据&gt;严重安全性能故障法定名称维护&gt;添加失效模式-新增
     </div>
  <!-- 查询条件 begin -->
  <form method="post" name="fm" id="fm">
  <input type="hidden" id="faultId" name="faultId" value="<%=request.getAttribute("faultId") %>"/>
  <div class="form-panel">
		<h2><img src="<%=contextPath%>/jmstyle/img/search-ico.png" style="margin-bottom: -13px;"/>查询条件</h2>
			<div class="form-body">
  <table class="table_query">
      <tr>
        <td style="text-align:right"> 失效代码： </td>
        <td align="left">
	        <input type="text" class="middle_txt" size="25" id="failCode" name="failCode" datatype="1,is_null,20"/>
        </td>
        <td style="text-align:right"> 失效名称： </td>
        <td align="left">
	        <input type="text" class="middle_txt" size="25" id="failName" name="failName" datatype="1,is_null,20"/>
        </td>
      </tr>
      <tr>
      	<td colspan="4" style="text-align:center">
      		<input name="queryBtn" type="button" class="normal_btn" value="查询" onclick="__extQuery__(1);"/>
      		<input name="button" type="button" class="normal_btn" onclick="subChecked();" value="保 存" />
      		<input name="button" type="button" class="normal_btn" onclick="parent.window._hide();" value="关闭" />
      	</td>
      </tr>
</table>
</div>
</div>
  <!--分页 begin -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
  <!--分页 end --> 
  </form> 
<!-- 查询条件 end -->
<br/>
<!--页面列表 begin -->
<script type="text/javascript" >
    var myPage;
	//查询路径
	var url = "<%=request.getContextPath()%>/claim/basicData/FailureMaintainMain/failModeDetailQuery.json";
				
	var title = null;

	var columns = [
	            {id:'action',header: "<input type='checkbox' name='checkAll' onclick='selectAll(this,\"orderIds\")' />全选", width:'8%',sortable: false,dataIndex: 'ID',renderer:myCheckBox},
				{header: "失效代码", dataIndex: 'FAILURE_CODE', align:'center'},
				{header: "失效名称",dataIndex: 'FAILURE_NAME' ,align:'center'}
		      ];
    
//全选checkbox
function myCheckBox(value,metaDate,record){
	return String.format("<input type='checkbox' name='orderIds' value='" + value + "' />");
}

function subChecked() {
	var allChecks = document.getElementsByName("orderIds");
	var allFlag = false;
	for(var i = 0;i<allChecks.length;i++){
		if(allChecks[i].checked){
			allFlag = true;
		}
	}
	if(allFlag){
		var url="<%=contextPath%>/claim/basicData/FailureMaintainMain/insertFailModeDetail.json";
		makeNomalFormCall(url,showResult,'fm');
	}else{
		MyAlert("请选择失效模式代码!");
	}
}

function showResult(json){
	var flag=json.flag;
	if(flag==1){
		MyAlert('该失效模式已存在!');
	}else{
		MyAlert('添加成功!');
		__parent().__extQuery__(1);//返回列表也同时刷新页面
		_hide();
	}
}
</script>
</div>
<!--页面列表 end -->
</body>
</html>