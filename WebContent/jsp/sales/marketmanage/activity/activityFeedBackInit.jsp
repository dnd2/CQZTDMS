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
<title>市场活动反馈查询</title>
<% String contextPath = request.getContextPath();  %>
</head>
<body>
<div class="navigation"><img src="<%=request.getContextPath()%>/img/nav.gif" />&nbsp;当前位置： 市场活动管理 &gt; 市场活动管理 &gt; 市场活动反馈查询</div>
<form method="post" name="fm" id="fm">
	<table class="table_query" >
	<tr>
		<tr>
			<td align="right">活动编号：</td>
			<td align="left">
				<input name="campaignNo" type="text" class="middle_txt" id="campaignNo" maxlength="25">
			</td>
			<td align="right">活动名称：</td>
			<td align="left">
				<input name="campaignName" type="text" class="middle_txt" id="campaignName" maxlength="25">
			</td>
			<td></td>
			<td></td>
		</tr>
		<tr>
			<td align="right">活动主题：</td>
			<td align="left">
				<input type="text" class="middle_txt" name="campaignSubject" id="campaignSubject" maxlength="25"/>
			</td>
			<td align="right">活动时间：</td>
			<td align="left">
				<input class="short_txt"  type="text" id="startDate" name="startDate" group="startDate,endDate" datatype="1,is_date,10"/>
				<input class="time_ico" type="button" onClick="showcalendar(event, 'startDate', false);" value="&nbsp;" />
				<input class="short_txt"  type="text" id="endDate" name="endDate" group="startDate,endDate" datatype="1,is_date,10"/>
				<input class="time_ico" type="button" onClick="showcalendar(event, 'endDate', false);" value="&nbsp;" />
			</td>
			<td></td>
			<td width="20%" align="left">
				<input name="queryBtn" type="button" class="cssbutton" onClick="__extQuery__(1);" value="查询">
			</td>
		</tr>
	</table>
<!--分页 begin -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
<!--分页 end -->
</form>
<!--页面列表 begin -->
<script type="text/javascript" >
	var myPage;
	//查询路径
	var url = "<%=contextPath%>/sales/marketmanage/activity/ActivityFeedBack/doSearch.json";
	var title = null;
	var columns = [
				{header: "活动编号",dataIndex: 'ACTIVITY_CODE',align:'center'},
				{header: "活动名称", dataIndex: 'ACTIVITY_NAME', align:'center'},
				{header: "活动主题",dataIndex: 'ACTIVITY_THEME', align:'center'},
				{header: "活动时间",dataIndex: 'START_DATE',align:'center',renderer:myText},
				{header: "活动方案状态",dataIndex: 'STATUS',align:'center',renderer:getItemValue},
				{id:'action',header: "操作", dataIndex: 'ACTIVITY_ID', align:'center',renderer:myLink}
		      ];
    //设置文本
    function myText(value,meta,record){
    	return String.format(value+"~"+record.data.END_DATE);
    }
   //设置超链接
    function myLink(value,meta,record){
    	var str="";
    	if(record.data.STATUS==91921007){
    		str="<a href='#' onclick='makePlan("+record.data.ACTIVITY_ID+")'>[生成反馈]</a>";
    	}else if(record.data.STATUS==91921009){
    		str="<a href='#' onclick='makePrint("+record.data.ACTIVITY_ID+")'>[打印]</a>/<a href='#' onclick='markPrint("+record.data.ACTIVITY_ID+")'>[标记打印]</a>";
    	}else if(record.data.STATUS==91921012){
    		str="<a href='#' onclick='makeSuccess("+record.data.ACTIVITY_ID+")'>[核销成功]</a>/<a href='#' onclick='makeDefeat("+record.data.ACTIVITY_ID+")'>[核销失败]</a>";
    	}
    	return String.format(str);
    }
    function makePlan(activityId){
        document.fm.action= '<%=contextPath%>/sales/marketmanage/activity/ActivityFeedBack/dtlPre.do?activityId='+activityId;
        document.fm.submit();
    }
    function makePrint(activityId){
        document.fm.action= '<%=contextPath%>/sales/marketmanage/activity/ActivityFeedBack/dtlPrintPre.do?activityId='+activityId;
        document.fm.target="_blank";
        document.fm.submit();
    }
    function makeSuccess(activityId){
   	 if(confirm("确认财务已经核销成功？")){
				var urls='<%=contextPath%>/sales/marketmanage/activity/ActivityFeedBack/makeSuccess.json?activityId='+activityId;;
				makeFormCall(urls, successReturn, "fm") ;
			}
    }
     function makeDefeat(activityId){
   	 	if(confirm("确认财务已经核销失败？")){
				var urls='<%=contextPath%>/sales/marketmanage/activity/ActivityFeedBack/makeDefeat.json?activityId='+activityId;;
				makeFormCall(urls, successReturn, "fm") ;
			}
    }
    
    function successReturn(json){
    	if(json.subFlag == 'success') {
			MyAlert("操作成功!") ;
			__extQuery__(1);
		} else {
			MyAlert("操作失败!") ;
		}
    
    }
   
      function markPrint(activityId){
   		 if(confirm("确认标记为已打印？")){
				var urls='<%=contextPath%>/sales/marketmanage/activity/ActivityFeedBack/markPrint.json?activityId='+activityId;;
				makeFormCall(urls, markReturn, "fm") ;
			}
    }
     function markReturn(json){
    	if(json.subFlag == 'success') {
			MyAlert("操作成功!") ;
			__extQuery__(1);
		} else {
			MyAlert("操作失败!") ;
		}
    
    }
    //初始化
    function doInit(){
    	//_setDate_("startDate", "endDate", "3", "1") ;
    	__extQuery__(1);
   		loadcalendar();  //初始化时间控件
	}
</script>
<!--页面列表 end -->
</body>
</html>