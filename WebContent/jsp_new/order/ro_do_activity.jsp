<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.Constant" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c" %>
<%@taglib uri="/jstl/change" prefix="change" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title></title>
<% String contextPath = request.getContextPath(); %>
</head>
<body >
<div class="navigation">
<img src="../jsp_new/img/nav.gif" />&nbsp;当前位置：服务活动选择
</div>
<form name="fm" id="fm">
<!-- 查询条件 begin -->
<input class="middle_txt" id="vin" value="${vin }" name="vin" type="hidden"  />
<input class="middle_txt" id="in_mileage" value="${in_mileage }" name="in_mileage" type="hidden"  />
<input class="middle_txt" id="dealerCode" value="${dealerCode }" name="dealerCode" type="hidden"  /> 


<table width=100% border="0" align="center" cellpadding="1" cellspacing="1" class="table_query">
	<tr>
		<td width="12.5%"></td>
		<td width="10%" class="table_query_2Col_label_5Letter" nowrap="true">活动代码：</td>
      	<td width="15%" nowrap="true">
            <input name="activityCode" id="activityCode" type="text" class="middle_txt" maxlength="30" />
      	</td>
        <td width="10%" class="table_query_2Col_label_6Letter" nowrap="true">活动名称：</td>
      	<td width="15%" nowrap="true">
            <input name="activityName" id="activityName" type="text" class="middle_txt" maxlength="30" />
      	</td>
		<td width="10%" nowrap="true" class="table_query_2Col_label_5Letter"></td>
		<td width="15%" nowrap="true">
		</td>
		<td width="12.5%"></td>
	</tr>
	<tr>
    	<td align="center" colspan="8">
    		<input type="button" name="btnQuery" id="queryBtn" value="查询" class="normal_btn" onClick="__extQuery__(1);"/>
    		&nbsp;&nbsp;&nbsp;
    		<input type="reset"  name="bntReset" id="bntReset" value="重置" class="normal_btn" />
    		&nbsp;&nbsp;&nbsp;
    		<input type="reset"  name="bntClose" id="bntClose" value="关闭"  onclick="_hide();" class="normal_btn" />
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
<!-- 查询条件 end -->
<script type="text/javascript" >
	
   //查询路径
	var myPage;
	var url = "<%=contextPath%>/OrderAction/doActivity.json?query=true";
	var title = null;
	var columns = [
				{id:'action',header: "选择",sortable: false,dataIndex: 'ACTIVITY_ID',renderer:mySelect,align:'center'},
				{header: "活动代码", dataIndex: 'ACTIVITY_CODE', align:'center'},
				{header: "活动名称", dataIndex: 'ACTIVITY_NAME', align:'center'},
				{header: "活动次数", dataIndex: 'ACTIVITY_NUM', align:'center'},
				{header: "活动类型", dataIndex: 'ACTIVITY_TYPE', align:'center',renderer:getItemValue}
	      ];
		 function mySelect(value,metaDate,record){
			 return String.format("<input type='radio' name='rd' onclick='setData(\""+record.data.ACTIVITY_CODE+"\",\""+record.data.TEMPLET_ID+"\",\""+record.data.IS_RETURN+"\")' />");
		} 
	  function setData(activity_code,templet_id,is_return){
			 //调用父页面方法
			if(activity_code==null||activity_code=="null"){
				activity_code = "";
			 }
			 if(templet_id==null||templet_id=="null"){
				 templet_id = "";
			 }
			 if(is_return==null||is_return=="null"){
				 is_return = "";
			 }
	 		if (parent.$('inIframe')) {
	 			parentContainer.setActityData(activity_code,templet_id,is_return);
	 		} else {
				parent.setActityData(activity_code,templet_id,is_return);
			}
	 		parent._hide();
		} 
</script>
<!--页面列表 end -->
</html>