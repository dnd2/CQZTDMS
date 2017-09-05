<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c" %>
<%@taglib uri="/jstl/change" prefix="change" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>工单</title>
<% String contextPath = request.getContextPath(); %>
<script type="text/javascript" src="<%=contextPath%>/js/jslib/CalendarZYW.js"></script>
<!--页面列表 begin -->

<script type="text/javascript" >

var myPage;
//查询路径
var url = "<%=contextPath%>/OrderAction/showInfoBylicenseNo.json?query=true";
			
var title = null;

var columns = [
				{header: "序号",sortable: false,align:'center',renderer:getIndex},
				{id:'action',header: "选择",sortable: false,dataIndex: 'ID',renderer:mySelect,align:'center'},
				{header: "工单号", dataIndex: 'RO_NO', align:'center'},
				{header: "车牌号", dataIndex: 'LICENSE', align:'center'},
				{header: "VIN", dataIndex: 'VIN', align:'center'}
	      ];
		function mySelect(value,metaDate,record){
			 return String.format("<input type='radio' name='rd' onclick='setData(\""+record.data.LICENSE+"\",\""+record.data.VIN+"\")' />");
		}
		function setData(v1,v2){
			 //调用父页面方法
			if(v1==null||v1=="null"){
			 	v1 = "";
			 }
			 if(v2==null||v2=="null"){
			 	v2 = "";
			 }
	 		if (parent.$('inIframe')) {
	 			parentContainer.backBylicenseNo(v1,v2);
	 		} else {
				parent.backBylicenseNo(v1,v2);
			}
	 		parent._hide();
		}
</script>
<!--页面列表 end -->
</head>
<body >
<div class="navigation">
<img src="../jsp_new/img/nav.gif" />&nbsp;当前位置：维修工单信息根据发动机号查询
</div>
<form name="fm" id="fm">
<!-- 查询条件 begin -->

<table width=100% border="0" align="center" cellpadding="1" cellspacing="1" class="table_query">
	<tr>
		<td width="22.5%"></td>
		<td width="10%" class="table_query_2Col_label_5Letter" nowrap="true">发动机号：</td>
      	<td width="15%" nowrap="true">
            <input name="license_no" id="license_no" type="text" value="${license_no }" class="middle_txt" maxlength="30" />
      	</td>
		<td width="10%" nowrap="true" class="table_query_2Col_label_5Letter"></td>
		<td width="15%" nowrap="true">
		</td>
		<td width="22.5%"></td>
	</tr>
	<tr>
    	<td align="center" colspan="8">
    		<input type="button" name="btnQuery" id="queryBtn" value="查询" class="normal_btn" onClick="__extQuery__(1);"/>
    		&nbsp;&nbsp;&nbsp;
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