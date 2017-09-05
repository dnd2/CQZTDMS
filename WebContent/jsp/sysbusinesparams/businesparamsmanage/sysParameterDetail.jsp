<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.Constant"%>

<%
	String contextPath = request.getContextPath();
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>系统可变参数表维护</title>
<style>textarea.form-control{margin-left: 10px}</style>
</head>
<body> <!-- onunload='javascript:destoryPrototype();' --> 
<div class="wbox">
	<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置：  系统管理 &gt; 系统业务参数维护 &gt; 系统可变参数表维护</div>
	<form id="fm" name="fm" method="post">
		<div class="form-panel">
			<h2>系统可变参数表维护 - 修改</h2>
			<div class="form-body">
				<table class="table_query" border="0">
					<tr>
						<td width="20%"><div align="right">类型名称：</div></td>
						<td width="50%" >
							${sysParameter.TYPE_NAME }
						</td>
					</tr>
					<tr>	
						<td><div align="right">参数名称：</div></td>
						<td>
							${sysParameter.PARA_NAME }
						</td>
					</tr>
					<tr>	
						<td><div align="right">参数值：</div></td>
						<td>
							<input type="text" id="para_value" class="middle_txt" name="para_value" value="${sysParameter.PARA_VALUE }" size="20" maxlength="60" datatype="0,is_textarea,60"  />
						</td>
					</tr>
					<tr>
						<td><div align="right">备注：</div></td>
						<td>
							<textarea type="text" id="remark" class="form-control remark" name="remark" value="${sysParameter.REMARK }" size="40" maxlength="100" datatype="1,is_textarea,100"></textarea>
						</td>
					</tr>
					<tr>	
						<td class="center" colspan="4" align="center">
							<input type="hidden" id="para_id" name="para_id" value="${sysParameter.PARA_ID }" />
							<input type="button" class="u-button" onclick="editSubmit();" value="确 定" id="queryBtn" /> 
						</td>
					</tr>
				</table>
			</div>
		</div>
	</form>
</div>
<script type="text/javascript" >
    function editSubmit(){
        MyDivConfirm("是否修改?",editSubmitAction);
	}
    function editSubmitAction(){
    	makeNomalFormCall('<%=contextPath%>/sysbusinesparams/businesparamsmanage/SysParameterManage/editSysParmSubmit.json',showForwordValue,'fm');
	}
	function showForwordValue(){
		parentContainer.reloadAction();
		parent._hide();
	}
	    
 </script>    
</body>
</html>