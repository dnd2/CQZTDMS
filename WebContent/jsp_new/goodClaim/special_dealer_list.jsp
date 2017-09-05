<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c" %>
<%@taglib uri="/jstl/change" prefix="change" %>
<%@ page import="com.infodms.dms.common.Constant"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>质保手册</title>
<% String contextPath = request.getContextPath(); %>
<script type="text/javascript" src="<%=contextPath%>/js/jslib/CalendarZYW.js"></script>


<script type="text/javascript" >

var myPage;
//查询路径
var url = "<%=contextPath%>/GoodClaimAction/specialDealerList.json?query=true";
			
var title = null;

var columns = [
				{header: "序号",sortable: false,align:'center',renderer:getIndex},
				{id:'action', width:'5%',header: "操作",sortable: false,dataIndex: 'SPE_ID',renderer:myLink,align:'center'},
				{header: "状态", dataIndex: 'STATUS', align:'center',renderer:getItemValue},
				{header: "申请单号", dataIndex: 'APPLY_NO', align:'center'},
				{header: "VIN", dataIndex: 'VIN', align:'center'},
				{header: "申请金额", dataIndex: 'APPLY_MONEY', align:'center'},
				{header: "上报时间", dataIndex: 'REPORT_DATE', align:'center'},
				{header: "结算单号", dataIndex: 'SETMENT_NO', align:'center'}
	      ];
	
	function myLink(value,meta,record){
		var status=record.data.STATUS;
		var special_type=record.data.SPECIAL_TYPE;
		var url="";
		if(20331001==status){
			url+="<a href='#' onclick='del(\""+value+"\",\""+special_type+"\");'>[删除]</a>";
			var urlUpdate='<%=contextPath%>/GoodClaimAction/updateSpeInit.do?id='+value+'&special_type='+special_type;
			url+="<a href='"+urlUpdate+"');'>[修改]</a>";
		}
		if(20331011==status){
			var urlUpdate='<%=contextPath%>/GoodClaimAction/printSpeInit.do?id='+value+'&special_type='+special_type;
			url+="<a target='_blank' href='"+urlUpdate+"');'>[打印]</a>";
		}
		if(20331004==status || 20331006==status || 20331008==status || 20331013==status){
			var urlUpdate='<%=contextPath%>/GoodClaimAction/updateSpeInit.do?id='+value+'&special_type='+special_type;
			url+="<a href='"+urlUpdate+"');'>[修改]</a>";
		}
		var urlView='<%=contextPath%>/GoodClaimAction/viewSpe.do?id='+value+'&special_type='+special_type;
		url+="<a href='"+urlView+"');'>[明细]</a>";
		return String.format(url);
	}
	function del(id,special_type){
		var urlDel='<%=contextPath%>/GoodClaimAction/delSpe.json?&id='+id+'&special_type='+special_type;
		sendAjax(urlDel,function(json){
			if(json.succ=="1"){
				MyAlert("提示：删除成功！");
				__extQuery__(1);
			}else{
				MyAlert("提示：删除失败！");
			}
		},'fm');
	}
	function add(spe_type){
  	  window.location.href='<%=contextPath%>/GoodClaimAction/addspecialDealerInit.do?';
    }
</script>
<!--页面列表 end -->
</head>
<body onload="__extQuery__(1);loadcalendar();">
<div class="navigation">
<img src="../jsp_new/img/nav.gif" />&nbsp;当前位置：售后服务管理&gt;特殊费用管理&gt;善意索赔查询
</div>
<form name="fm" id="fm">
<!-- 查询条件 begin -->
<div class="form-panel">
<h2><img src="<%=contextPath%>/jmstyle/img/search-ico.png" style="margin-bottom: -13px;"/>查询条件</h2>
<div class="form-body">
<table  class='table_query'>
	<tr>
		<td width="5%" style="text-align: right;">申请单号：</td>
      	<td width="10%" align="left">
      		<input class="middle_txt" id="APPLY_NO"  name="APPLY_NO" maxlength="30" type="text"/>
      	</td>
        <td width="5%" style="text-align: right;">单据状态：</td>
      	<td width="10%" align="left">
<%--       		<select id="STATUS" name="STATUS">
      			<option value="">请选择</option>
      			<option value="<%=Constant.SPE_STATUS_01%>">已保存</option>
      			<option value="<%=Constant.SPE_STATUS_02%>">审核中</option>
      			<option value="<%=Constant.SPE_STATUS_13%>">审核退回</option>
      			<option value="<%=Constant.SPE_STATUS_14%>">审核通过</option>
      			<option value="<%=Constant.SPE_STATUS_15%>">审核拒绝</option>
      		</select> --%>
      		<script type="text/javascript">
	              genSelBoxExp("STATUS",<%=Constant.SPE_STATUS%>,"",true,"","","false",'<%=Constant.SPE_STATUS_09%>,<%=Constant.SPE_STATUS_10%>,<%=Constant.SPE_STATUS_11%>,<%=Constant.SPE_STATUS_12%>');
	    	</script>
      	</td>
		<td width="5%" style="text-align: right;">上报时间：</td>
		<td width="10%" align="left">
			<input class="middle_txt"  type="text" readonly="readonly" name="creatDate" id="creatDate" />
					<input class="time_ico" type="button" onclick="showcalendar(event, 'creatDate', false);" value="&nbsp;" />
		</td>
	</tr>
	<tr>
		<td width="5%" style="text-align: right;">结算单号：</td>
      	<td width="10%" align="left">
      		<input class="middle_txt" id="SETMENT_NO"  name="SETMENT_NO" maxlength="30" type="text"/>
      	</td>
	</tr>
	<tr>
    	<td style="text-align: center;" colspan="8">
    		<input name="queryBtn" type="button" class="u-button u-query" onclick="__extQuery__(1)" value="查 询" id="queryBtn" /> &nbsp; 
			<input type="reset" class="u-button u-cancel" value="重 置"/> &nbsp; 
			<input type="button" class="u-button u-submit" onclick="add();" value="新 增" />
    	</td>
    </tr>
</table>
</div>
</div>
<!-- 查询条件 end -->

<!--分页 begin -->
			<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
			<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
<!--分页 end -->
</form>
</body>
</html>