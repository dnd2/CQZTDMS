<!DOCTYPE html PUBLIC "-//W3C//Dtd Xhtml 1.0 transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@page import="com.infodms.dms.common.Constant"%>
<head>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<% String contextPath = request.getContextPath(); %>
<body>
<div class="wbox">
<div id="loader" style='position:absolute;z-index:200;background:#FFCC00;padding:1px;top:4px;display:none;display: none;'></div>
<div class="navigation"><img src="<%=contextPath %>/img/nav.gif" width="11" height="11" />&nbsp;当前位置：服务活动选择 </div>
</div>
  <form  name="fm" id="fm" method="post">
  <input type="hidden" name="vin" id="vin" value="<%=request.getAttribute("vin")%>"/>
  <input type="hidden" name="inMileage" id="vin" value="<%=request.getAttribute("inMileage")%>"/>
   <!--查询条件begin-->
    <table class="table_query" border="0" >
      <tr>
        <td class="table_query_2Col_label_5Letter">活动代码：</td>
        <td align="left">
            <input name="ACTIVITY_CODE" id="ACTIVITY_CODE" type="text" class="middle_txt" size="5" />
        </td>
      <td class="table_query_2Col_label_5Letter">活动名称：</td>
        <td align="left">
            <input name="ACTIVITY_NAME" id="ACTIVITY_NAME" type="text" class="middle_txt" size="5" />
       </td>
       
      </tr>
      <tr>
       <td colspan="4" align="center"><input name="button" id="queryBtn" type="button" onclick="__extQuery__(1)" class="normal_btn"  value="查询" />
        <input name="button" id="queryBtn" type="button" onclick="_hide()" class="normal_btn"  value="关闭" />
        </td>
      </tr>
    </table>
    <!--查询条件end-->
      <jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
</form>
<script language="JavaScript" >
	var myPage;
	
	var url = "<%=contextPath%>/claim/dealerClaimMng/ClaimBillMaintainMain/queryActivity.json";
				
	var title = null;

	var columns = [
				{id:'action',header: "选择",sortable: false,dataIndex: 'activityId',renderer:mySelect,align:'center'},
				{header: "活动代码", dataIndex: 'activityCode', align:'center'},
				{header: "活动名称", dataIndex: 'activityName', align:'center'},
				{header: "活动次数", dataIndex: 'activityNum', align:'center'},
				{header: "活动类型", dataIndex: 'activityType', align:'center',renderer:getItemValue}
		      ];
	function myRen(value,metadata,record) {
		if (value==1){
			return "是";
		}else {
			return "否";
		}
	}	      
	function mySelect(value,metaDate,record){
		return String.format("<input type='radio' name='rd' onclick='setActivity(\""+record.data.activityId+"\",\""+record.data.activityCode+"\",\""+record.data.activityName+"\",\""+record.data.activityFee+"\",\""+record.data.isFixfee+"\",\""+record.data.isClaim+"\",\""+record.data.activityType+"\",\""+record.data.troubleDesc+"\",\""+record.data.troubleReason+"\",\""+record.data.repairMethod+"\",\""+record.data.appRemark+"\",\""+record.data.activityNum+"\")' />");
	}

	var d0 = "";
	
	var d1 = "";
	var d2 = "";
	var d3 = "";
	var d4 = "";
	var d5 = "";
	var d6 = "";
	
	var d7 = "";
	var d8 = "";
	var d9 = "";
	var d10 = "";
	var d11 = "";
	function setActivity(v0,v1,v2,v3,v4,v5,v6,v7,v8,v9,v10,v11){
		 //调用父页面方法
		if(v1==null||v1=="null"){
		 	v1 = "";
		 }
		 if(v2==null||v2=="null"){
		 	v2 = "";
		 }
		 if(v3==null||v3=="null"){
		 	v3 = "";
		 }
		 if(v4==null||v4=="null"){
		 	v4 = "";
		 }
		 if(v5==null||v5=="null"){
		 	v5 = "";
		 }
		 if(v6==null||v6=="null"){
		 	v6 = "";
		 }
		 d0 = v0;
		 d1 = v1;
		 d2 = v2;
		 d3 = v3;
		 d5 = v4;
		 d5 = v5;
		 d6 = v6;
		 d7 = v7;
		 d8 = v8;
		 d9 = v9;
		 d10 = v10;
		 d11 = v11;
		 if(d6 == "<%=Constant.SERVICEACTIVITY_TYPE_05 %>"){
			//为替换件设置默认值
			makeCall("<%=contextPath%>/claim/dealerClaimMng/ClaimBillMaintainMain/queryReplace.json",setData,{id:d0});
		 } else {
			 if (parent.$('inIframe')) {
	 			parentContainer.setActivity(d1,d2,d3,d4,d5,d6);
	 		 } else {
				parent.setActivity(d1,d2,d3,d4,d5,d6);
			 }
			 parent._hide();
		 }
 	}
	
	function setData(json){
		var labouritemList = json.labouritemList;
		var partsitemList = json.partsitemList;
		if (parent.$('inIframe')) {
 			parentContainer.setActivityData(labouritemList,partsitemList,d1,d2,d3,d4,d5,d6,d7,d8,d9,d10,d11);
 		 } else {
			parent.setActivityData(labouritemList,partsitemList,d1,d2,d3,d4,d5,d6,d7,d8,d9,d10,d11);
		 }
		 parent._hide();
	}
</script>
</body>
</html>
