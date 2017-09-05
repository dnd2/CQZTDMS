<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java"  pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.Constant"%>
<%
	String contextPath = request.getContextPath();
%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>配件人员类型设置</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
<script language="javascript" type="text/javascript">
	$(function(){
		var fixValue = $("fixValue").value;
		if(fixValue==1){
			columns = [
							{header: "序号", dataIndex: 'SEQUENCE_ID', renderer:getIndex,align:'center'},
							{header: "人员名称", dataIndex: 'NAME', style:'text-align: center;'},
							{header: "是否主管", dataIndex: 'IS_LEADER', align:'center', renderer:getCheckBox},
							{header: "是否直发计划员", dataIndex: 'IS_DIRECT', align:'center', renderer:getCheckBox1},
							{header: "是否审核专员", dataIndex: 'IS_CHKZY', align:'center', renderer:getCheckBox2},
							{header: "是否有效", dataIndex: 'STATE', align:'center',renderer:getItemValue},
							{id:'action',header: "操作",sortable: false,dataIndex: 'ID',renderer:myLink ,align:'center'}
					      ];
		}
		__extQuery__(1);
	});
	var myPage;
	
	var url = "<%=contextPath%>/parts/baseManager/partUserPostManager/partUserPostAction/partUserPostSearch.json";
	
	var title = null;
	
	var columns = [
				{header: "序号", dataIndex: 'SEQUENCE_ID', renderer:getIndex,align:'center'},
				{header: "人员名称", dataIndex: 'NAME', style:'text-align: center;'},
				{header: "是否主管", dataIndex: 'IS_LEADER', align:'center', renderer:getCheckBox},
				{header: "是否有效", dataIndex: 'STATE', align:'center',renderer:getItemValue},
				{id:'action',header: "操作",sortable: false,dataIndex: 'ID',renderer:myLink ,align:'center'}
		      ];
	     
	
	//设置超链接
	function myLink(value,meta,record)
	{
		var defineId = record.data.DEFINE_ID
		var state = record.data.STATE;
		var disableValue = <%=Constant.STATUS_DISABLE%>;
		if(disableValue == state){
			return String.format("<a href=\"#\" onclick='enableData(\""+defineId+"\")'>[有效]</a>&nbsp;<a href=\"#\" onclick='updateData(\""+defineId+"\")'>[保 存]</a>");
	    } else {
	    	return String.format("<a href=\"#\" onclick='cel(\""+defineId+"\")'>[失效]</a>&nbsp;<a href=\"#\" onclick='updateData(\""+defineId+"\")'>[保 存]</a>");
		}
			
	}

	//返回checkbook
	function getCheckBox(value,meta,record)
	{
		var defineId = record.data.DEFINE_ID
		var flagYes = <%=Constant.PART_BASE_FLAG_YES%>;
		if(flagYes == value){
			return String.format("<label class='u-checkbox'><input type='checkbox' id='isLeader_"+defineId+"' value=\""+value+"\" checked /><span></span></label>");
	    } else {
	    	return String.format("<label class='u-checkbox'><input type='checkbox' id='isLeader_"+defineId+"' value=\""+value+"\" /><span></span></label>");
		}
			
	}

	//返回checkbook
	function getCheckBox1(value,meta,record)
	{
		var defineId = record.data.DEFINE_ID
		var flagYes = <%=Constant.PART_BASE_FLAG_YES%>;
		if(flagYes == value){
			return String.format("<label class='u-checkbox'><input type='checkbox' id='isDirect_"+defineId+"' value=\""+value+"\" checked /><span></span></label>");
	    } else {
	    	return String.format("<label class='u-checkbox'><input type='checkbox' id='isDirect_"+defineId+"' value=\""+value+"\" /><span></span></label>");
		}
			
	}

	//返回checkbook
	function getCheckBox2(value,meta,record)
	{
		var defineId = record.data.DEFINE_ID
		var flagYes = <%=Constant.PART_BASE_FLAG_YES%>;
		if(flagYes == value){
			return String.format("<input type='checkbox' id='isChkZy_"+defineId+"' value=\""+value+"\" checked />");
	    } else {
	    	return String.format("<input type='checkbox' id='isChkZy_"+defineId+"' value=\""+value+"\" />");
		}
			
	}

	//保存设置
	function updateData(parms) {
		MyConfirm("确定更新设置?", function() {
			var obj = document.getElementById("isLeader_"+parms);
			var obj1 = document.getElementById("isDirect_"+parms);
			var obj2 = document.getElementById("isChkZy_"+parms);
			btnDisable();
			var isLeader = "no";
			var isDirect = "no";
			var isChkZy = "no";
			if(obj.checked)
			{
				isLeader = "yes";
			}
			if(obj1&&obj1.checked)
			{
				isDirect = "yes";
			}
			if(obj2&&obj2.checked)
			{
				isChkZy = "yes";
			}
	     	var url = '<%=contextPath%>/parts/baseManager/partUserPostManager/partUserPostAction/updatePartUserPost.json?updateParms='+parms+'&isLeader='+isLeader+'&curPage='+myPage.page+'&isDirect='+isDirect+'&isChkZy='+isChkZy;
	  		makeNomalFormCall(url,showResult,'fm');
		});
	}
	
	//设置失效：
	function cel(parms) {
		MyConfirm("确定失效该数据?", function() {
			btnDisable();
	     	var url = '<%=contextPath%>/parts/baseManager/partUserPostManager/partUserPostAction/celPartUserPost.json?disabeParms='+parms+'&curPage='+myPage.page;
	  		makeNomalFormCall(url,showResult,'fm');
		});
	}
	
	//设置有效：
	function enableData(parms) {
		MyConfirm("确定有效该数据?", function() {
			btnDisable();
	     	var url = '<%=contextPath%>/parts/baseManager/partUserPostManager/partUserPostAction/enablePartUserPost.json?enableParms='+parms+'&curPage='+myPage.page;
	     	makeNomalFormCall(url,showResult,'fm');
		});
	}
	
	function showResult(json) {
		btnEnable();
	    if (json.errorExist != null && json.errorExist.length > 0) {
	        layer.msg(json.errorExist, {icon: 2});
	    } else if (json.success != null && json.success == "true") {
	    	layer.msg("操作成功!", {icon: 1});
	    	__extQuery__(json.curPage);          
	    } else {
	        layer.msg("操作失败，请联系管理员!", {icon: 2});
	    }
	}

	//新增人员
	function addNew(){
		var fixValue = document.getElementById("fixValue").value;
		var url = '<%=contextPath%>/parts/baseManager/partUserPostManager/partUserPostAction/queryUsersInit.do?fixValue=' + fixValue;
		OpenHtmlWindow(url,700,500);
	}

	//返回
	function goBack(){
		btnDisable();
		location = '<%=contextPath%>/parts/baseManager/partUserPostManager/partUserPostAction/partUserPostInit.do' ;
	}

</script>
</head>
<body>
<div class="wbox">
	<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置：配件管理 &gt; 基础信息管理 &gt; 配件基础信息维护 &gt; 配件人员类型设置 &gt; 维护</div>
	<form name='fm' id='fm'>
		<div class="form-panel">
			<h2><img class="panel-icon nav" src="<%=request.getContextPath()%>/img/subNav.gif" /> 查询条件</h2>
			<div class="form-body">
				<table class="table_query">
					<tr>
					<td class="right" width="10%" align="right">人员类型：</td>
					<td width="25%"><input type="text" id="fixName"  name="fixName" class="middle_txt" value="${fixName }" readonly="readonly" disabled="disabled"/>
						<input type="hidden"  name="fixValue" id="fixValue" value="${fixValue }"/>
					</td>
					<td class="right" width="10%" align="right">人员名称：</td>
					<td width="25%" ><input class="middle_txt" type="text" name="userName" id="userName"/></td>
					<td class="right" width="10%" align="right">是否有效：</td>
					<td >
						<script type="text/javascript">
						genSelBoxExp("STATE",<%=Constant.STATUS%>,"<%=Constant.STATUS_ENABLE%>",true,"short_sel u-select","","false",'');
					</script>
					</td>
				</tr>
				<tr>
					<td class="formbtn-aln" align="center" colspan="6" >
					<input class="u-button" type="button" value="查 询" name="BtnQuery" id="queryBtn" onclick="__extQuery__(1)"/>
					<input type="button" name="addBtn" id="addBtn" value="新增人员" onclick="addNew()"  class="u-button"/>
					<input type="button" name="backBtn" id="backBtn" value="返回" onclick="goBack()"  class="u-button"/>
					</td>
				</tr>
			</table>
			</div>
		</div>
		
		<!-- 查询条件 end -->
			<!--分页 begin -->
			<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
			<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
			<!--分页 end -->
	</form>
</div>	
</body>
</html>
