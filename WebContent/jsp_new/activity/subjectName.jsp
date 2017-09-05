<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c" %>
<%@taglib uri="/jstl/change" prefix="change" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title></title>
<% String contextPath = request.getContextPath(); %>
<!--页面列表 begin -->

<script type="text/javascript" >

	var myPage;
	
	var url = "<%=contextPath%>/ActivityAction/openSubject.json";//url查询
	var title = null;//头标
	var columns = [
		{header: "序号",sortable: false,align:'center',renderer:getIndex},
		{id:'action',header: "选择", width:'3%',align:'center',sortable: false,dataIndex: 'SUBJECT_ID',renderer:myRadioCheck},
		{header: "主题编号",sortable: false,dataIndex: 'SUBJECT_NO',align:'center'},				
		{header: "主题名称",sortable: false,dataIndex: 'SUBJECT_NAME',align:'center'}
	];
	function myRadioCheck(value,metaDate,record){
		var subject_id=record.data.SUBJECT_ID;
		var subject_no=record.data.SUBJECT_NO;
		var subject_name=record.data.SUBJECT_NAME;
		var str="<input type='radio' name='checkId' onclick='checkRadio(\""+subject_id+"\",\""+subject_no+"\",\""+subject_name+"\");' />";
		return String.format(str);
	}
	function checkRadio(subject_id,subject_no,subject_name){
		if (parent.$('inIframe')) {
			parentContainer.myRadio(subject_id,subject_no,subject_name);
		}else{
			parent.myRadio(subject_id,subject_no,subject_name);
		}
		_hide();
	}
</script>
<!--页面列表 end -->
</head>
<body >
<div class="navigation">
<img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置：售后服务管理&gt;服务活动管理&gt;服务活动主题
</div>
<form name="fm" id="fm">
<!-- 查询条件 begin -->
<table width=100% border="0" align="center" cellpadding="1" cellspacing="1" class="table_query">
	<tr>
		<td width="12.5%"></td>
		<td width="10%" class="table_query_2Col_label_5Letter" nowrap="true">主题编号：</td>
      	<td width="15%" nowrap="true">
      		<input type="text"  name="subject_no" id="subject_no" class="long_txt" maxlength="30"/>
      	</td>
        <td width="10%" class="table_query_2Col_label_6Letter" nowrap="true">主题名称：</td>
      	<td width="15%" nowrap="true">
	      	<input type="text"  name="subject_name" id="subject_name" class="long_txt" maxlength="30"/>
      	</td>
		<td width="10%" nowrap="true" class="table_query_2Col_label_5Letter"></td>
		<td width="15%" nowrap="true">
		</td>
		<td width="12.5%"></td>
	</tr>
	<tr>
    	<td align="center" colspan="8">
    		<input type="button" name="btnQuery" id="queryBtn" value="查询" class="normal_btn" onClick="__extQuery__(1);"/>
    		&nbsp;&nbsp;&nbsp;&nbsp;
    		<input type="reset"  name="bntReset" id="bntReset" value="重置" class="normal_btn" />
    	</td>
    </tr>
</table>
<!-- 查询条件 end -->

<!--分页 begin -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
<!--分页 end -->
</form>
</body>
</html>