<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java"  pageEncoding="UTF-8"%>
<%@page import="com.infodms.dms.common.Constant"%>
<%String contextPath = request.getContextPath();%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>故障法定名称维护</title>
</head>
<body>
<div class="wbox">
<form method="post" name='fm' id='fm'>
  <div class="navigation">
  <img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 售后服务管理&gt;索赔基本数据&gt;严重安全性能故障法定名称维护&gt;故障法定名称维护</div>
  <div class="form-panel">
		<h2><img src="<%=contextPath%>/jmstyle/img/search-ico.png" style="margin-bottom: -13px;"/>查询条件</h2>
			<div class="form-body">
   <table class="table_query">
          <tr>
            <td style="text-align:right">故障法定代码：</td>
            <td>
                 <input name="legalCode" id="legalCode" datatype="1,is_null,30"  type="text" class="middle_txt"/>
            </td>
            <td style="text-align:right">故障法定名称：</td>
            <td>
				<input name="legalName" id="legalName" datatype="1,is_null,30"  type="text" class="middle_txt"/>
            </td>
            <td style="text-align:right">故障模式名称：</td>
            	<td>
	            	<input name="failureName" id="failureName" datatype="1,is_null,30"  type="text" class="middle_txt"/>
            	</td>
          </tr>
		   <tr>    
			   <td colspan="6" style="text-align:center">
	            <input class="normal_btn" type="button" name="queryBtn" id="queryBtn" value="查询"  onclick="__extQuery__(1)"/>
	            <input class="normal_btn" type="button" value="新增" name="add" onclick="subFun();"/>
	            <input class="normal_btn" type="button" value="删除" name="OnDel" onclick="OnDel1();"/>
	            <!--  <input class="long_btn" type="button" value="失效模式下发" name="OnDel" onclick="OnDown('${userId}');"/>-->
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
<script type="text/javascript" >
var myPage;
	var url = "<%=request.getContextPath()%>/claim/basicData/FailureMaintainMain/FaultLegalQuery.json?COMMAND=1";
	var title = null;
	
	var columns = [
				{header: "序号",sortable: false,align:'center',renderer:getIndex},
				{header:"<input type=\"checkBox\" id=\"checkBoxAll\" name=\"checkBoxAll\" onclick='selectAll(this,\"recesel\")' />全选", align:'center',sortable:false, dataIndex:'FAULT_ID',width:'2%',renderer:checkBoxShow},
				{header: "操作",sortable: false,dataIndex: 'FAULT_ID',renderer:myLink ,align:'center'},
				{header: "故障法定代码",sortable: false,dataIndex: 'FAULT_CODE',align:'center'},				
				{header: "故障法定名称",sortable: false,dataIndex: 'FAULT_NAME',align:'center'},
				{header: "故障模式名称",sortable: false,dataIndex: 'FAULT_TYPE_NAME',align:'center'}
				//{header: "状态",sortable: false,dataIndex: 'STATUS',align:'center',renderer:getItemValue},
		      ];

//设置超链接  begin 
function OnDown(value){
	if(value!=1000002433){
		MyAlert("您不是SA账号，没有权限进行失效模式下发");
	}else{
		  location="<%=contextPath%>/claim/basicData/FailureMaintainMain/downFaultLegalInit.do";  
	}
}
function checkBoxShow(value,meta,record){
	return String.format("<input type='checkbox' id='recesel' name='recesel' value='" + record.data.FAULT_ID + "' />");
}

//新增
function subFun(){
    location="<%=contextPath%>/claim/basicData/FailureMaintainMain/FaultLegalAddInit.do";   
}

//添加配件信息和添加失效模式
function myLink(value,meta,record){
	return String.format("<a  href=\"<%=contextPath%>/claim/basicData/FailureMaintainMain/AddPartInfoInit.do?faultId="+ value + "\">[添加配件信息]</a>"+"<a href=\"<%=contextPath%>/claim/basicData/FailureMaintainMain/AddFailModeInit.do?faultId="+ value + "\">[添加失效模式]</a>");
}

//设置超链接 end

//删除
function OnDel1() {
	var allChecks = document.getElementsByName("recesel");
	var allFlag = false;
	for(var i = 0;i<allChecks.length;i++){
		if(allChecks[i].checked){
			allFlag = true;
		}
	}
	if(allFlag){
		MyConfirm("确认删除选中的故障?",changeSubmit);
	}else{
		MyAlert("请选择后再点击操作删除按钮!");
	}
}
function changeSubmit() {
	var url="<%=contextPath%>/claim/basicData/FailureMaintainMain/FaultLegalDel.json";
	makeNomalFormCall(url,showResult,'fm');
}
function showResult(json){
	var msg=json.msg;
	if(msg=='01'){
		MyAlert('删除成功!');
		__extQuery__(1);
	}else{
		MyAlert('操作失败！');
	}
}
</script>
</div>
</body>
</html>