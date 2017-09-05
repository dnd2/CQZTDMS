<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java"  pageEncoding="UTF-8"%>
<%@page import="com.infodms.dms.common.Constant"%>
<%@page import="com.infodms.dms.po.TtAsWrFaultLegalPO"%>
<%String contextPath = request.getContextPath();
TtAsWrFaultLegalPO po = (TtAsWrFaultLegalPO)request.getAttribute("faultList");
%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>添加失效模式</title>
</head>
<body onload="__extQuery__(1)">
<div class="wbox">
<form method="post" name='fm' id='fm'>
  <input type="hidden" id="faultId" name="faultId" value="<%=request.getAttribute("faultId") %>"/>
  <div class="navigation">
  <img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 售后服务管理&gt;索赔基本数据&gt;严重安全性能故障法定名称维护&gt;添加失效模式</div>
    <div class="form-panel">
		<h2>基本信息</h2>
			<div class="form-body">
   <table class="table_query">
          <tr>
            <td style="text-align:right">故障法定代码：</td>
            <td>
                 <%=po.getFaultCode()%>
            </td>
            <td style="text-align:right">故障法定名称：</td>
            <td>
				<%=po.getFaultName()%>
            </td>
          </tr>
  </table>
  </div>
  </div>
  <div class="form-panel">
		<h2><img src="<%=contextPath%>/jmstyle/img/search-ico.png" style="margin-bottom: -13px;"/>查询条件</h2>
			<div class="form-body">
  <table class="table_query">
          <tr>
            <td  style="text-align:right">失效代码：</td>
            <td>
                 <input name="failCode" id="failCode" datatype="1,is_null,30"  type="text" class="middle_txt"/>
            </td>
            <td  style="text-align:right">失效名称：</td>
            <td>
				<input name="failName" id="failName" datatype="1,is_null,30"  type="text" class="middle_txt"/>
            </td>
          </tr>
		   <tr>    
			   <td colspan="4"  style="text-align:center">
		            <input class="normal_btn" type="button" name="queryBtn" id="queryBtn" value="查询"  onclick="__extQuery__(1)"/>
		            <input name="buttonImport" type="button" class="normal_btn" onclick="addDetails();" value="新增" />
		            <input class="normal_btn" type="button" value="删除" name="del" onclick="onDel();"/>
		            <input name="back" type="button" class="normal_btn" value="返回" onclick="onBack();"/>
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
	var url = "<%=request.getContextPath()%>/claim/basicData/FailureMaintainMain/queryFailMode.json";
	var title = null;
	
	var columns = [
				{header: "序号",sortable: false,align:'center',renderer:getIndex},
				{header:"<input type=\"checkBox\" id=\"checkBoxAll\" name=\"checkBoxAll\" onclick='selectAll(this,\"recesel\")' />全选", align:'center',sortable:false, dataIndex:'id',width:'2%',renderer:checkBoxShow},
				{header: "失效代码",sortable: false,dataIndex: 'failureModeCode',align:'center'},				
				{header: "失效名称",sortable: false,dataIndex: 'failureModeName',align:'center'}
		      ];

//设置超链接  begin      
function checkBoxShow(value,meta,record){
	return String.format("<input type='checkbox' id='recesel' name='recesel' value='" + record.data.id + "' />");
}

//新增
function addDetails(){
	var faultId=document.getElementById("faultId").value;
	OpenHtmlWindow("<%=contextPath%>/claim/basicData/FailureMaintainMain/addModeQuery.do?faultId="+faultId,800,500);
}

//返回
function onBack(){
    location="<%=contextPath%>/claim/basicData/FailureMaintainMain/FaultLegalInit.do";   
}
//设置超链接 end

//删除
function onDel() {
	var allChecks = document.getElementsByName("recesel");
	var allFlag = false;
	for(var i = 0;i<allChecks.length;i++){
		if(allChecks[i].checked){
			allFlag = true;
		}
	}
	if(allFlag){
		MyConfirm("确认删除选中的失效模式?",changeSubmit);
	}else{
		MyAlert("请选择后再点击操作删除按钮!");
	}
}
function changeSubmit() {
	var url="<%=contextPath%>/claim/basicData/FailureMaintainMain/failModeDel.json";
	makeNomalFormCall(url,showResult,'fm');
}
function showResult(json){
	var msg=json.msg;
	if(msg=='01'){
		MyAlert('删除成功!');
		__extQuery__(1);
	}else{
		MyAlert('操作失败,请联系DCS系统运维团队，问题提报热线:023-67543333');
	}
}
</script>
</div>
</body>
</html>