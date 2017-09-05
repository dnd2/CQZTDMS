<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ taglib uri="/jstl/cout" prefix="c" %>
<%@ taglib uri="/jstl/change" prefix="change" %>
<head> 
<%  
	String contextPath = request.getContextPath(); 
%>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<script type="text/javascript" src="<%=contextPath%>/js/jslib/jquery-1.7.2.js"></script>
<script type="text/javascript" src="<%=contextPath%>/js/jslib/CalendarZYW.js"></script>
<title>售后管理人员通讯录新增</title>
<script type="text/javascript">
	function sureInsert(){
		var user_name=$("#user_name").val();
		var user_phone=$("#user_phone").val();
		var area=$("#area").val();
		if(""==user_name){
			MyAlert("提示：用户名称必须填写！");
			return;
		}
		if(""==user_phone){
			MyAlert("提示：用户电话必须填写！");
			return;
		}
		if(""==area){
			MyAlert("提示：大区必须选择！");
			return;
		}
		var url="<%=contextPath%>/MainTainAction/addMailListSure.json";
		$("#sure").attr("disabled",true);
		makeNomalFormCall1(url,function(json){
			if(json.succ=="1"){
				MyAlert("提示：保存成功！");
				var url='<%=contextPath%>/MainTainAction/mailListTemp.do';
				window.location.href=url;
			}else{
				MyAlert("提示：保存失败！");
				$("#sure").attr("disabled",false);
			}
		},"fm");
	}
</script>
</head>
<body>
<div class="navigation">
<img src="../jsp_new/img/nav.gif" />&nbsp;当前位置：售后服务管理&gt;索赔基础数据&gt;售后管理人员通讯录新增
</div>
<form name="fm" id="fm" method="post">
<table border="0" cellpadding="1" cellspacing="1" class="table_edit" width="100%" style="text-align: center;">
	<th colspan="8">
		<img class="nav" src="../jsp_new/img/subNav.gif"/>基本信息
	</th>
	<tr>
		<td width="12.5%"></td>
		<td nowrap="true" width="10%" >用户名称：</td>
    	<td nowrap="true" width="10%" >
    		<input class="middle_txt" id="user_name"  value="${t.USER_NAME }" name="user_name" type="text" maxlength="30" />
    	</td>
		<td nowrap="true" width="10%" >用户电话：</td>
    	<td nowrap="true" width="10%" >
    		<input class="middle_txt" id="user_phone" value="${t.USER_PHONE }"  name="user_phone" type="text" maxlength="30" />
    	</td>
    	<td nowrap="true" width="10%" >大区选择：</td>
    	<td nowrap="true" width="10%" >
    	   <select name="area" id="area">
   		     <option value="">-请选择-</option>
   		    <c:forEach items="${listpo}" var="list">
   		       <option value="${list.ROOT_ORG_NAME }">
   		          ${list.ROOT_ORG_NAME }
   		        </option>
   		    </c:forEach>
   		   </select>
    	
    	</td>
    	<td width="12.5%"></td>
	</tr>
	<tr>
	    <td width="12.5%"></td>
	    <td nowrap="true" width="10%">岗位名称：</td>
	    <td>
	      <input class="middle_txt" name="position_name" id="position_name"  type="text" maxlength="30"/>
	    </td>
	    <td width="10%">岗位职责:</td>
    	<td width="10%" rowspan="1" colspan="2" align="left">
    	   <textarea rows="5" cols="5" style="width: 300px" id="position_duty" name="position_duty" ></textarea>
    	</td>
    	<td width="12.5%"></td>
    	<td width="12.5%"></td>
	</tr>
</table>
<br>
<table width=100% border="0" cellspacing="0" cellpadding="0">
		<tr>
			<td height="12" align=left width="33%">&nbsp;</td>
            	<td height="12" align=center width="33%" nowrap="true">
               	&nbsp;&nbsp;
               	<input type="button" class="normal_btn" id="sure" onclick="sureInsert();"  style="width=8%" value="确定" />&nbsp;&nbsp;
				<input type="button" id="back" onClick="history.back();" class="normal_btn"  style="width=8%" value="返回"/>
   			</td>
           	<td height="12" align=center width="33%">
     			</td>
		</tr>
</table>
</form>
</body>
</html>