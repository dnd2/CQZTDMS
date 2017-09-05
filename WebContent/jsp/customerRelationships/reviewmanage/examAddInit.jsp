<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@page import="com.infodms.dms.common.Constant"%>
<%@taglib uri="/jstl/cout" prefix="c"%>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>我的客户回访</title>
<%
	String contextPath = request.getContextPath();
%>
</head>
<body>
	<div class="navigation">
		<img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置：
		客户关系管理&gt;回访管理&gt;题库管理 &gt;编辑题库
	</div>
	<form name='fm' id='fm'>
		<input type="hidden" name="EX_ID" id="EX_ID" value="${EX_ID}" /> <input
			type="hidden" name="saveFlag" id="saveFlag" value="" />
		<table width="100%" align="center" cellpadding="2" cellspacing="1"
			class=table_query>
			<tbody>
				<tr class=cssTable>
					<td style="WIDTH: 17%" align="right">题库名称：</td>
					<td style="WIDTH: 609px" width="609"><input id="EX_NAME"
						style="WIDTH: 580px" maxlength="50" value="" name="EX_NAME"
						datatype="1,is_digit_letter_cn,30" />
					</td>
				</tr>
				<tr class=cssTable>
					<td style="WIDTH: 17%" align="right">题库说明：</td>
					<td><textarea id="EX_DESCRIPTION"
							style="HEIGHT: 119px; WIDTH: 580px" maxlength="10" rows="3"
							name="EX_DESCRIPTION"></textarea> <br />
						<span style="FONT-FAMILY: 宋体; COLOR: red">题库说明最多允许输入50个汉字。</span>
						<br /></td>
				</tr>
				<tr>
					<td colspan="2" align="center"><input name="btnSave"
						type="button" class="cssbutton" " id="btnSave"
						onclick="saveExam();" value="保存" /> <input id="queryBtn2"
						class="cssbutton" onclick="javascript:history.go(-1)" value="返回"
						type="button" name="button2" /></td>
				</tr>
				
			</tbody>
		</table>

		<!--分页 end -->
		<!--页面列表 begin -->
<script type="text/javascript">

	//保存
	function saveExam(){
		var exName = document.fm.EX_NAME.value;
	    if(null==exName||"".indexOf(exName)==0){
	       MyAlert("请输入题库名称！");
	       return false;
	    }
	    var exDes = document.fm.EX_DESCRIPTION.value;
	    if(null==exDes||"".indexOf(exDes)==0||exDes.length>50){
	       MyAlert("请输入题库说明或者说明太长！");
	       return false;
	    }
	    var url= "<%=contextPath%>/customerRelationships/reviewmanage/ReviewManage/addSaveFact.json";
		makeNomalFormCall(url,showResult,'fm');
	}
	function showResult(json){
		var msg=json.msg;
		if(msg=='01'){
			MyAlertForFun("保存成功",closeSave);
		}else if(msg=='02'){
			MyAlert('该题库已存在，请修改题库名称！');
		}else{
			MyAlert('保存失败,请联系管理员');
		}
	}
	function closeSave(){
		fm.action = '<%=contextPath%>/customerRelationships/reviewmanage/ReviewManage/examManageInit.do';
		fm.submit();
	}
	
</script>
	</form>
</body>
</html>