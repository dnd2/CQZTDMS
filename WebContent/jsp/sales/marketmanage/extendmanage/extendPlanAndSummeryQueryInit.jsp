<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="com.infodms.dms.common.Constant"%>
<%@ page import="java.util.*"%>
<%@taglib uri="/jstl/cout" prefix="c" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<script type="text/javascript" src="<%=request.getContextPath()%>/js/funccommon/activityfunc.js"></script>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>区域外拓计划及总结查询</title>
<% String contextPath = request.getContextPath();  %>
</head>
<body>
<div class="navigation"><img src="<%=request.getContextPath()%>/img/nav.gif" />&nbsp;当前位置： 市场活动管理 &gt; 区域外拓计划管理 &gt; 区域外拓计划及总结查询</div>
<form method="post" name="fm" id="fm">
<!-- 查询条件 begin -->
	<table class="table_query" >
	<tr>
		<tr>
			<td align="right">活动方式：</td>
			<td align="left">
                <script type="text/javascript">
                    genSelBoxExp("planType",1330, "", true, "", "", "false", '')
                </script>
			</td>
			<td align="right">活动地点：</td>
			<td align="left">
				<input name="place" type="text" class="middle_txt" id="place" maxlength="60">
			</td>
			<td></td>
		</tr>
		<tr>
			<td align="right">执行负责人：</td>
			<td align="left">
				<input type="text" class="middle_txt" name="charge" id="charge" maxlength="10"/>
			</td>
			<td align="right">活动时间：</td>
			<td align="left">
				<input class="short_txt"  type="text" id="startDate" name="beginDate" group="beginDate,endDate" datatype="1,is_date,10"/>
				<input class="time_ico" type="button" onClick="showcalendar(event, 'beginDate', false);" value="&nbsp;" />
				<input class="short_txt"  type="text" id="endDate" name="endDate" group="beginDate,endDate" datatype="1,is_date,10"/>
				<input class="time_ico" type="button" onClick="showcalendar(event, 'endDate', false);" value="&nbsp;" />
			</td>
			<td></td>
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
                  <input class="normal_btn" type="button" value="清空" onclick="txtClr('dealerCode');"/>
              </td>
             <td colspan="3" align="middle">
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
	var url = "<%=contextPath%>/sales/marketmanage/extendmanage/ExtendPlanAndSummeryQuery/doSearch.json";
	var title = null;
	var columns = [
				{header: "大区",dataIndex: 'ROOT_ORG_NAME',align:'center'},
				{header: "省系",dataIndex: 'PQ_ORG_NAME',align:'center'},
				{header: "经销商简称",dataIndex: 'DEALER_SHORTNAME',align:'center'},
				{header: "活动方式",dataIndex: 'PLAN_TYPE',align:'center',renderer:getItemValue},
				{header: "执行负责人", dataIndex: 'CHARGE', align:'center'},
				{header: "联系电话",dataIndex: 'TEL', align:'center'},
				{header: "活动地点",dataIndex: 'PLACE',align:'center'},
				{header: "活动日期",dataIndex: 'BEGIN_DATE',align:'center',renderer:myText},
				{header: "预计客流数",dataIndex: 'PRE_GUEST_NUM',align:'center'},
				{header: "实际客流数",dataIndex: 'ACT_GUEST_NUM',align:'center'},
				{header: "预计建卡数",dataIndex: 'PRE_CARD_NUM',align:'center'},
				{header: "实际建卡数",dataIndex: 'ACT_CARD_NUM',align:'center'},
				{header: "预计成交量",dataIndex: 'PRE_DEAL_NUM',align:'center'},
				{header: "实际成交量",dataIndex: 'ACT_DEAL_NUM',align:'center'},
				{header: "成交率(客流)",dataIndex: 'GUEST_RATE',align:'center'},
				{header: "成交率(建卡)",dataIndex: 'CARD_RATE',align:'center'},
				{header: "成交率(成交)",dataIndex: 'DEAL_RATE',align:'center'},
				{header: "状态",dataIndex: 'STATUS',align:'center',renderer:myGetItemValue},
				{id:'action',header: "附件", dataIndex: 'PLAN_ID', align:'center',renderer:myLink},
				{id:'action',header: "操作", dataIndex: 'PLAN_ID', align:'center',renderer:myLink2}
		      ];
    function myGetItemValue(value,meta,record){
           if(value==0){
               return String.format("未提报");
           }else if(value==1){
               return String.format("经销商已提报");
           }else if(value==2){
               return String.format("已驳回");
           }
    }
    //设置文本
    function myText(value,meta,record){
    	return String.format(value+"~"+record.data.END_DATE);
    }
    //设置超链接
    function myLink(value,meta,record){
            return String.format("<a href='#' onclick='goAttach("+record.data.PLAN_ID + ")'>[查看]</a>");
    }
    //设置超链接
    function myLink2(value,meta,record){
        //外拓计划处于经销商已提报状态时才有驳回链接
        if(record.data.STATUS==1){
            return String.format("<a href='#' onclick='doReject("+record.data.PLAN_ID + ")'>[驳回]</a>");
        }
        return "";
    }
    //初始化
    function doInit(){
    	//_setDate_("startDate", "endDate", "3", "1") ;
    	__extQuery__(1);
   		loadcalendar();  //初始化时间控件
	}
    function goAttach(planId){
        OpenHtmlWindow('<%=contextPath%>/sales/marketmanage/extendmanage/ExtendPlanAndSummeryQuery/attachPre.do?planId='+planId,600,400);
    }

    function doReject(planId){
        if(confirm("是否确认驳回?")){
             makeNomalFormCall('<%=contextPath%>/sales/marketmanage/extendmanage/ExtendPlanAndSummeryQuery/doReject.json?planId='+planId,showResult,"fm2");
        }
    }
    function showResult(json){
        if(json.returnValue==1) {
            MyAlert("驳回成功");
    	    __extQuery__(1);
        }
    }
	function clrTxt(txtId){
    	document.getElementById(txtId).value="";
    }
</script>
<form name="fm2" id="fm2"></form>
</body>
</html>