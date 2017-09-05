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
  <img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 售后服务管理&gt;索赔基本数据&gt;失效模式维护</div>
  <div class="form-panel">
		<h2><img src="<%=contextPath%>/jmstyle/img/search-ico.png" style="margin-bottom: -13px;"/>查询条件</h2>
			<div class="form-body">
   <table class="table_query">
          <tr>
            <td style="text-align:right">失效模式代码：</td>
            <td>
                 <input name="modeCode" id="modeCode"  type="text" class="middle_txt"/>
            </td>
            <td style="text-align:right">失效模式名称：</td>
            <td>
				<input name="modeName" id="modeName"  type="text" class="middle_txt"/>
            </td>
          </tr>
		   <tr>    
			   <td colspan="4" style="text-align:center">
	            <input class="normal_btn" type="button" name="queryBtn" id="queryBtn" value="查询"  onclick="__extQuery__(1)"/>
	            <input class="normal_btn" type="button" value="新增" name="add" onclick="subFun();"/>
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
	var url = "<%=request.getContextPath()%>/claim/basicData/FailureMaintainMain/FaultModeQuery.json";
	var title = null;
	
	var columns = [
				{header: "序号",sortable: false,align:'center',renderer:getIndex},
				{header: "操作",sortable: false,dataIndex: 'ID',renderer:myLink ,align:'center'},
				//{header:"<input type=\"checkBox\" id=\"checkBoxAll\" name=\"checkBoxAll\" onclick='selectAll(this,\"recesel\")' />全选", align:'center',sortable:false, dataIndex:'FAULT_ID',width:'2%',renderer:checkBoxShow},
				{header: "失效模式代码",sortable: false,dataIndex: 'FAILURE_CODE',align:'center'},				
				{header: "失效模式名称",sortable: false,dataIndex: 'FAILURE_NAME',align:'center'}
		      ];
var idValue=1;
function checkBoxShow(value,meta,record){
	return String.format("<input type='checkbox' id='recesel' name='recesel' value='" + record.data.FAULT_ID + "' />");
}

//新增
function subFun(){
	var url ="<%=contextPath%>/claim/basicData/FailureMaintainMain/faultModeAdd.do";   
	OpenHtmlWindow(url, 650, 380, '首保信息新增');
   <%--  location="<%=contextPath%>/claim/basicData/FailureMaintainMain/faultModeAdd.do";    --%>
}

//添加配件信息和添加失效模式
function myLink(value,meta,record){
	return String.format("<a  href='#' onclick='delModel(" + value + ")'>[删除]</a>");
	/* return String.format("<input type='button' class ='normal_btn' name='recesel' value='删除' onclick='delModel("+value+")'/>"); */
}

//设置超链接 end
function delModel(value){
	idValue= value;
	MyConfirm("是否确认删除?",changeSubmit);
}
function changeSubmit() {
	var url="<%=contextPath%>/claim/basicData/FailureMaintainMain/FaultModeDel.json?id="+idValue;
	makeNomalFormCall(url,showResult,'fm');
}
function showResult(json){
	if(json.msg){
		MyAlertForFun('删除成功!',__extQuery__(1));
	}else{
		MyAlert('操作失败,请联系DCS系统运维团队，问题提报热线:023-67543333');
	}
}
</script>
</div>
</body>
</html>