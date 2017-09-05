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
var url = "<%=contextPath%>/ClaimAction/findYsqBaseInfo.json?query=true";
			
var title = null;

var columns = [
				{header: "序号",sortable: false,align:'center',renderer:getIndex},
				{id:'action',header: "选择",sortable: false,dataIndex: 'ID',renderer:mySelect,align:'center'},
				{header: "预授权单号", width:'15%', dataIndex: 'YSQ_NO'},
				{header: "VIN", width:'15%', dataIndex: 'VIN'},
				{header: "发动机号", width:'15%', dataIndex: 'ENGINE_NO'},
				{header: "里程", width:'15%', dataIndex: 'MILEAGE'},
				{header: "创建日期", width:'15%', dataIndex: 'CREATE_DATE'}
	      ];

		function mySelect(value,metaDate,record){
			 return String.format("<input type='radio' name='rd' onclick='setysqData(\""+record.data.ID+"\",\""+record.data.BC_PASS+"\",\""+record.data.MAX_ESTIMATE+"\",\""+record.data.VIN+"\")' />");
		}
		function setysqData(v1,BC_PASS,MAX_ESTIMATE,VIN){
			 //调用父页面方法
			if(v1==null||v1=="null"){
			 	v1 = "";
			}
			sendAjax('<%=contextPath%>/ClaimAction/queryYsqDataById.json?id='+v1+'&bc_pass='+BC_PASS+'&max_estimate='+MAX_ESTIMATE+'&vin='+VIN,backData,'fm');
		}
		function backData(json){
			if (parent.$('inIframe')) {
				 parentContainer.backYsqData(json.data,json.ysqPart,json.vrLevel,json.max_estimate);
				 if(json.bc_pass.length > 0 &&  json.bc_pass != '0'){
					 parentContainer.backBCqData(json.bc_pass,json.max_estimate);
			     }
	 		}else{
				parent.backYsqData(json.data,json.ysqPart,json.vrLevel,json.max_estimate);
				parent.backBCqData(json.bc_pass,json.max_estimate);
			}
	 		parent._hide();
		}
</script>
<!--页面列表 end -->
</head>
<body >
<div class="navigation">
<img src="../jsp_new/img/nav.gif" />&nbsp;当前位置：预授权单查询
</div>
<form name="fm" id="fm">
<input class="middle_txt" id="claim_type"  name="claim_type" maxlength="30" type="hidden" value="${claim_type }"/>
<input class="middle_txt" id="ro_no"  name="ro_no" maxlength="30" type="hidden" value="${ro_no }"/>
<input class="middle_txt" id="VIN"  name="VIN" maxlength="30" type="hidden" value="${vin }"/>
<!-- 查询条件 begin -->
<table width=100% border="0" align="center" cellpadding="1" cellspacing="1" class="table_query">
	<tr>
		<td width="10%" class="table_query_2Col_label_5Letter" nowrap="true">预授权单号：</td>
      	<td width="15%" nowrap="true">
      		<input class="middle_txt" id="ysq_no"  name="ysq_no" maxlength="30" type="text"/>
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