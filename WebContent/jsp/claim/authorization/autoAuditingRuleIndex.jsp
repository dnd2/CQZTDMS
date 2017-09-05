<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java"  pageEncoding="UTF-8"%>
<%@page import="com.infodms.dms.common.Constant"%>
<%
	String contextPath = request.getContextPath();
%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>索赔自动审核规则管理</title>
 <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
</head>
<body onload="__extQuery__(1)">
<form name='fm' id='fm'>
  <div class="navigation">
  <img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 售后服务管理&gt;索赔授权管理&gt;索赔自动审核规则管理</div>
</form>
  <!--分页 begin -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
  <!--分页 end -->  
</body>
</html>
<script type="text/javascript">
var myPage;
	var url = "<%=request.getContextPath()%>/claim/authorization/AutoAuditingRuleMain/autoAuditingRuleQuery.json?COMMAND=1";
	var title = null;
	
	var columns = [
				{header: "规则名称",sortable: false,dataIndex :'REMARK',align:'center'},
				{header: "规则类型",sortable: false,dataIndex: 'AUTO_TYPE',align:'center',renderer:getItemValue},
				{header: "需要授权级别",sortable: false,dataIndex :'AUTH_DESC',align:'center',renderer:displayAuth},				
				{header: "状态",sortable: false,dataIndex: 'STATUS',align:'center',renderer:getStatusValue},
				{header: "操作",sortable: false,dataIndex: 'AUTO_ID',renderer:myLink ,align:'center'}
		      ];

//设置超链接  begin      
	
	//修改的超链接设置
	function myLink(value,meta,record){
	    return String.format(
         "<input type='button' class='normal_btn' name='DO' id='DO' value='启用' "+record.data.DO+" onclick='goDo(\""+value+"\",\"<%=Constant.STATUS_ENABLE%>\")'/>"+
         "<input type='button' class='normal_btn' name='DO' id='DO' value='停用' "+record.data.UNDO+" onclick='goDo(\""+value+"\",\"<%=Constant.STATUS_DISABLE%>\")'/>");
	}
	//格式化状态列的显示：
	function getStatusValue(value,meta,record){
		var sy = '<%=Constant.STATUS_ENABLE%>';  //有效
		var sn = '<%=Constant.STATUS_DISABLE%>'; //无效
		if(value == sy){
			return String.format("启用");
		}else if(value == sn){
			return String.format("停用");
		}
		
	}

	//展示预售权
	function displayAuth(value,meta,record){
		var res = value;
		if(record.data.AUTO_TYPE!='<%=Constant.CLAIM_RULE_TYPE_03%>'){
			res = '无';
		}else{
			res = value+' <a href="#" onclick="updateAuthInfo('+record.data.AUTO_ID+');">[修改]</a>';
		}
		return String.format(res);
	}

	//修改授权项目
	function updateAuthInfo(autoId){
		var tarUrl = '<%=contextPath%>/claim/authorization/AutoAuditingRuleMain/updateAutoRuleAuthInit.do?&ID='+ autoId;
		var width=500;
		var height=250;
		
		OpenHtmlWindow(tarUrl,width,height);
	}
	
	//设置超链接 end
	//提交方法：
	function goDo(v,s){
		MyConfirm("是否确认修改？",del,[v,s]);
	}  
	//设置方法
	function del(v,s){
		makeNomalFormCall('<%=contextPath%>/claim/authorization/AutoAuditingRuleMain/autoAuditingRuleUpdate.json?AUTO_ID='+v+'&STATUS='+s,delBack,'fm','');
	}
	//设置后的回调方法：
	function delBack(json) {
	if(json.success != null && json.success == "true") {
		MyAlert("设置成功！");
		__extQuery__(1);
	} else {
		MyAlert("设置失败！请联系管理员！");
	}
}   
</script>