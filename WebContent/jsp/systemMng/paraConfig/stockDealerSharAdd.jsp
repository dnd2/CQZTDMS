<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
	String contextPath = request.getContextPath();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>共享库存经销商设定</title>
<style>
	.img{ border:none}
</style>
</head>

<body onunload='javascript:destoryPrototype()'>
	<div class="wbox">
	<div class="navigation">
		<img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 系统管理 &gt; 参数设定 &gt; 共享库存经销商设定
	</div>
	<form id="fm">
		<table class="table_query" border="0" style="border:1px solid #DAE0EE">
			<tr >
				<td align="right" >添加经销商：
					<input type="hidden" id="dlrId" name="dlrId" value="" />
					<input type="text" class="middle_txt" name="dlrName" id="dlrName" value="" readonly/>
					<input name="button" type="button" class="mark_btn" value="&hellip;" onclick="showOrgs('<%=contextPath %>')"/><font color="red">&nbsp;*</font>	
				</td>
				<td align="left" width="55%" style="padding-left:75px;">
					<input class="normal_btn" type="button" id="addBtn" value="确 定" onclick="addDlrWh();" />&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
					<input class="normal_btn" type="button" value="取 消" onclick="goBack();">		
				</td>
			</tr>
		</table>
	</form>	
</div>
<script type="text/javascript">
	function addDlrWh(){
		if($("dlrId").value == ""){
			MyAlert("添加的经销商不能为空！");
			return false;
		}
		
		MyConfirm("是否确认新增?",confirmAdd);
	}
	function confirmAdd(){
		makeNomalFormCall('<%=request.getContextPath()%>/sysmng/paramconfig/ShareDealerConfig/addShareDlr.json',showResult,'fm');
	}
	function showResult(json){
		if(json.ACTION_RESULT == '1'){
			window.location.href = '<%=request.getContextPath()%>/sysmng/paramconfig/ShareDealerConfig/queryShareDealer.do';
		}else if(json.ACTION_RESULT == '2'){
			MyAlert("保存失败！请确认新增的经销商是否已经存在！");
		}else if(json.ACTION_RESULT == '3'){
			MyAlert("保存失败！添加的共享经销商不能是自己！");
		}
	}
	function goBack(){
		window.location.href = "<%=contextPath%>/sysmng/paramconfig/ShareDealerConfig/queryShareDealer.do";
	}
</script>
</body>
</html>
