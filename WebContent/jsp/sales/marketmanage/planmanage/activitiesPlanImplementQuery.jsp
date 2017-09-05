<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="com.infodms.dms.common.Constant"%>
<%@ page import="java.util.*"%>
<%@taglib uri="/jstl/cout" prefix="c" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>市场活动计划查询</title>
<% String contextPath = request.getContextPath();  %>
<script type="text/javascript">
</script>
</head>
<body>
<div class="navigation"><img src="<%=request.getContextPath()%>/img/nav.gif" />&nbsp;当前位置： 市场活动管理  &gt; 活动方案管理   &gt; 市场活动计划查询</div>
<form method="post" name="fm" id="fm">
<!-- 查询条件 begin -->
	<table class="table_query" >
		<tr>
			<td align="right">活动编号：</td>
			<td align="left">
				<input name="campaignNo" type="text" class="middle_txt" id="campaignNo" maxlength="25">
			</td>
			<td align="right">活动名称：</td>
			<td align="left">
				<input name="campaignName" type="text" class="middle_txt" id="campaignName" maxlength="25">
			</td>
            <td width="20%" align="left"></td>
		</tr>
		<tr>
              <td align="right" nowrap>选择经销商：</td>
              <td align="left">
                  <input type="text" name="dealerCode" size="20" value="" id="dealerCode"/>
                  <c:if test="${dutyType==10431001}">
                    <input class="mini_btn" id="dlbtn2" name="dlbtn2" type="button" onclick="showOrgDealer('dealerCode','','true', '${orgId}')" value="..." />
                  </c:if>
                  <c:if test="${dutyType==10431002}">
                    <input class="mini_btn" id="dlbtn2" name="dlbtn2" type="button" onclick="showOrgDealer3('dealerCode','','true', '${orgId}')" value="..." />
                  </c:if>
                  <c:if test="${dutyType==10431003}">
                    <input class="mini_btn" id="dlbtn2" name="dlbtn2" type="button" onclick="showOrgDealer3('dealerCode','','true', '${orgId}')" value="..." />
                  </c:if>
                  <c:if test="${dutyType==10431004}">
                    <input class="mini_btn" id="dlbtn2" name="dlbtn2" type="button" onclick="showOrgDealer6('dealerCode','','true', '${orgId}')" value="..." />
                  </c:if>
                  <input class="normal_btn" type="button" value="清空" onclick="clrTxt('dealerCode');"/>
              </td>
			<td align="right">计划时间：</td>
			<td align="left">
				<input class="short_txt"  type="text" id="startDate" name="startDate" group="startDate,endDate" datatype="1,is_date,10"/>
				<input class="time_ico" type="button" onClick="showcalendar(event, 'startDate', false);" value="&nbsp;" />
				<input class="short_txt"  type="text" id="endDate" name="endDate" group="startDate,endDate" datatype="1,is_date,10"/>
				<input class="time_ico" type="button" onClick="showcalendar(event, 'endDate', false);" value="&nbsp;" />
			</td>
              <td width="20%" align="left">
                    <input name="queryBtn" type="button" class="cssbutton" onClick="__extQuery__(1);" value="查询">
              </td>
		</tr>
	</table>
<!-- 查询条件 end -->
<!--分页 begin -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
<!--分页 end -->
</form>
<!--页面列表 begin -->
<script type="text/javascript" >
	var myPage;
	//查询路径
	var url = "<%=contextPath%>/sales/marketmanage/planmanage/ActivitiesPlanImplementQuery/doSearch.json";
	var title = null;
	var columns = [
				{header: "活动编号",dataIndex: 'CAMPAIGN_NO',align:'center'},
				{header: "活动名称",dataIndex: 'CAMPAIGN_NAME',align:'center'},
				{header: "经销商代码",dataIndex: 'DEALER_CODE',align:'center'},
				{header: "经销商名称",dataIndex: 'DEALER_SHORTNAME',align:'center'},
                {header: "活动类型",dataIndex: 'PLAN_TYPE',renderer:getItemValue,align:'center'},
                {header: "项目名称",dataIndex: 'PROJECT_NAME',align:'center'},
                {header: "计划时间",dataIndex: 'BEGIN_DATE',renderer:myText,align:'center'},
                {header: "总费用",dataIndex: 'ALL_COST',align:'center'},
                {header: "公司支持",dataIndex: 'COMPANY_COST',align:'center'},
                {header: "费用类型",dataIndex: 'COST_TYPE',align:'center',renderer:getItemValue},
                {header: "来场客流数",dataIndex: 'TO_PLACE_COUNT',align:'center'},
                {header: "来店来电数",dataIndex: 'TO_TEL_STORE_COUNT',align:'center'},
                {header: "建卡数",dataIndex: 'CREATE_CARDS_COUNT',align:'center'},
                {header: "订单数",dataIndex: 'ORDER_COUNT',align:'center'},
                {header: "交车数",dataIndex: 'TURN_CAR_COUNT',align:'center'},
                {header: "变更申请类型及状态",dataIndex: 'REQ_STATUS',align:'center',renderer:myGetItemValue},
                {header: "变更申请提报日期",dataIndex: 'REQ_DATE',align:'center',renderer:myGetItemValue3},
                {header: "变更申请提报批次",dataIndex: 'REQ_NUM',align:'center',renderer:myGetItemValue4},
                {header: "活动总结类型及状态",dataIndex: 'SUMMERY_STATUS',align:'center',renderer:myGetItemValue2}
		      ];
    function myGetItemValue(value,meta,record){
         if (value==0) {
             return String.format("未进行变更申请");
         } else {
             return getItemValue(record.data.CHNG_TYPE)+'<font color="red">|</font>'+getItemValue(value);
         }
    }
    function myGetItemValue2(value,meta,record){
         if (value==0) {
             return String.format("未进活动总结");
         } else {
             return getItemValue(record.data.SUMMERY_COST_TYPE)+'<font color="red">|</font>'+getItemValue(value);
         }
    }
    function myGetItemValue3(value,meta,record){
         if (value==0) {
             return String.format("未进行变更申请");
         } else {
             return String.format(record.data.REQ_DATE);
         }
    }
    function myGetItemValue4(value,meta,record){
         if (value==0) {
             return String.format("未进行变更申请");
         } else {
             return String.format(record.data.REQ_NUM);
         }
    }
    //设置文本
	function myText(value,meta,record){
    	return String.format(value+"~"+record.data.END_DATE);
    }
    //初始化
    function doInit(){
    	__extQuery__(1);
   		loadcalendar();  //初始化时间控件
	}
	function clrTxt(txtId){
    	document.getElementById(txtId).value="";
    }
</script>
<!--页面列表 end -->
</body>
</html>