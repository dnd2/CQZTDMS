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
var url = "<%=contextPath%>/ClaimAction/findRoBaseInfo.json?query=true";
			
var title = null;

var columns = [
				{header: "序号",sortable: false,align:'center',renderer:getIndex},
				{id:'action',header: "选择",sortable: false,dataIndex: 'ID',renderer:mySelect,align:'center'},
				{header: "工单号", width:'15%', dataIndex: 'RO_NO'},
				{header: "VIN", width:'15%', dataIndex: 'VIN'},
				{header: "发动机号", width:'15%', dataIndex: 'ENGINE_NO'},
				{header: "里程", width:'15%', dataIndex: 'IN_MILEAGE'},
				{header: "购车日期", width:'15%', dataIndex: 'GUARANTEE_DATE'},
				{header: "预警等级", width:'15%', dataIndex: 'WARNING_LEVEL'}
	      ];

		function mySelect(value,metaDate,record){
			 return String.format("<input type='radio' name='rd' onclick='setData(\""+record.data.RO_NO+"\",\""+record.data.VIN+"\",\""+record.data.ENGINE_NO+"\",\""+record.data.IN_MILEAGE+"\",\""+record.data.COLOR+"\",\""+record.data.GUARANTEE_DATE+"\",\""+record.data.WARNING_LEVEL+"\",\""+record.data.WRGROUP_ID+"\",\""+record.data.RO_PACKAGE_ID+"\",\""+record.data.SERIES+"\",\""+record.data.RO_MODEL_ID+"\",\""+record.data.DEALER_SHORTNAME+"\",\""+record.data.PACKAGE_NAME+"\",\""+record.data.CAM_CODE+"\")' />");
		}
		function setData(v1,v2,v3,v4,v5,v6,v7,v8,v9,v10,v11,v12,v13,v14){
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
			if(v7==null||v7=="null"){
			 	v7 = "";
			}
			if(v8==null||v8=="null"){
			 	v8 = "";
			}
			if(v9==null||v9=="null"){
			 	v9 = "";
			}
			if(v10==null||v10=="null"){
			 	v10 = "";
			}
			if(v11==null||v11=="null"){
			 	v11 = "";
			}
			if(v12==null||v12=="null"){
			 	v12 = "";
			}
			if(v13==null||v13=="null"){
			 	v13 = "";
			}
			if(v14==null||v14=="null"){
			 	v14 = "";
			}
			if (parent.$('inIframe')) {
				parentContainer.backBaseInfoData(v1,v2,v3,v4,v5,v6,v7,v8,v9,v10,v11,v12,v13,v14);
	 		} else {
				parent.backBaseInfoData(v1,v2,v3,v4,v5,v6,v7,v8,v9,v10,v11,v12,v13,v14);
			}
	 		parent._hide();
		}
</script>
<!--页面列表 end -->
</head>
<body >
<div class="navigation">
<img src="../jsp_new/img/nav.gif" />&nbsp;当前位置：售后服务管理&gt;维修工单登记&gt;维修工单查询
</div>
<form name="fm" id="fm">
<input class="middle_txt" id="repairtypecode"  name="repairtypecode" maxlength="30" type="hidden" value="${repairtypecode }"/>
<input class="middle_txt" id="claimType"  name="claimType" maxlength="30" type="hidden" value="${claimType }"/>
<!-- 查询条件 begin -->
<table width=100% border="0" align="center" cellpadding="1" cellspacing="1" class="table_query">
	<tr>
		<td width="10%" class="table_query_2Col_label_5Letter" nowrap="true">工单号：</td>
      	<td width="15%" nowrap="true">
      		<input class="middle_txt" id="ro_no"  name="ro_no" maxlength="30" type="text"/>
      	</td>
        <td width="10%" class="table_query_2Col_label_5Letter" nowrap="true">VIN：</td>
      	<td width="15%" nowrap="true">
      		<input class="middle_txt" id="vin"  name="vin" maxlength="30" type="text"/>
      	</td>
		<td width="25%" class="table_query_2Col_label_5Letter" nowrap="true">开始时间：</td>
      	<td width="45%" nowrap="true">
      	      <input name="beginTime" type="text" id="beginTime"  readonly="readonly" onfocus="calendar();" class="short_txt"/>-至- <input name="endTime" type="text" id="endTime"  readonly="readonly" onfocus="calendar();" class="short_txt"/>
      	</td>
	</tr>
	<tr>
		<td width="10%" class="table_query_2Col_label_5Letter" nowrap="true">车牌号：</td>
      	<td width="15%" nowrap="true">
      		<input class="middle_txt" id="license"  name="license" maxlength="30" type="text"/>
      	</td>
        <td width="10%" class="table_query_2Col_label_5Letter" nowrap="true">用户姓名：</td>
      	<td width="15%" nowrap="true">
      		<input class="middle_txt" id="owner_name"  name="owner_name" maxlength="30" type="text"/>
      	</td>
	</tr>
	<tr>
    	<td align="center" colspan="6">
    		&nbsp;&nbsp;&nbsp;
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
</html>