<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@page import="com.infodms.dms.common.Constant"%>
<%@page import="com.infodms.dms.util.CommonUtils"%><html xmlns="http://www.w3.org/1999/xhtml">
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<TITLE>罚款作业</TITLE>
</HEAD>
<BODY>
<div class="navigation">
  <img src="../../../img/nav.gif" />&nbsp;当前位置：售后服务管理&gt;其他功能&gt;罚款作业明细</div>

<form method="post" name="fm" id="fm" >

<table class="table_edit">
	<tr>
		<td class="table_edit_1Col_label_5Letter">生产基地：</td>
		<td align="left">
			<script type="text/javascript">
			genSelBoxContainStr("yieldly",<%=Constant.SERVICEACTIVITY_CAR_YIELDLY%>,"",true,"short_sel","","true",'<%=CommonUtils.checkNull(request.getAttribute("yieldly"))%>');
		    </script><font color="red">*</font>
		</td>
	</tr>
	<tr>
		<td class="table_edit_1Col_label_5Letter">罚款金额：</td>
		<td align="left"><input type='text' datatype="0,is_yuan"  name='FINE_SUM'  id='FINE_SUM' class="short_txt" />元</td>
	</tr>
	<tr>
		<td class="table_edit_1Col_label_5Letter">罚款原因：</td>
		<td >
		<input type='text' datatype="0,is_null,100"  name='FINE_REASON'  id='FINE_REASON' class="middle_txt" />
			</td>
	</tr>
	<tr>
		<td class="table_edit_1Col_label_5Letter">备 注：<input type="hidden"
			name="DEALER_ID" value="<%=request.getAttribute("DEALER_ID") %>" /></td>
		<td><input type='text'  name='REMARK'  id='REMARK'  datatype="1,is_digit_letter_cn,100"   class="middle_txt" />
		</td>
	</tr>
		<tr>
		<TD colspan="2" align="center"><input type="button"  name="button" onclick="confirmAdd()" value="确定" class="normal_btn" />

			<input type="button" name="Submit3" value="返回" onClick="history.back();"
			class="normal_btn"></td>
	</tr>
</table>
				
</form>

<script type="text/javascript">
	function confirmAdd() {
	    var yieldly = $('yieldly');	
	 	if (document.getElementById("FINE_SUM").value==0) {
	 		MyAlert("罚款金额不能为0！");
	 	} else if(!yieldly || !yieldly.value ||yieldly.value==''){
	 		MyAlert("奖惩必须加入产地！");
	 	}else {
			MyConfirm("是否增加？",confirmAdd0,[]);
		}
	}
//确定按钮事件
function confirmAdd0() {
	if(submitForm('fm')){
		var fm = document.getElementById('fm');
		fm.action='<%=request.getContextPath()%>/claim/other/Bonus/punish.do';
		fm.submit();
		}
}
</script>
</BODY>
</html>

