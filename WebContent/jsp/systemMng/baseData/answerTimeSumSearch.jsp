<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.Constant" %>
<%
	String contextPath = request.getContextPath();
%>
<jsp:include page="${contextPath}/common/globalVariable.jsp" />
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link href="<%=contextPath%>/style/content.css" rel="stylesheet" type="text/css" />
<link href="<%=contextPath%>/style/calendar.css" type="text/css" rel="stylesheet" />
<link href="<%=contextPath%>/style/page-info.css" rel="stylesheet" type="text/css" />
<link href="<%=contextPath%>/style/dtree1.css" rel="stylesheet"	type="text/css" />
<script type="text/javascript" src="<%=contextPath%>/js/jslib/mootools.js"></script>
<script type="text/javascript" src="<%=contextPath %>/js/jslib/my-grid-pager.js"></script>
<script type="text/javascript" src="<%=contextPath %>/js/validate/validate.js"></script>
<script type="text/javascript" src="<%=contextPath %>/js/web/mtcommon.js"></script>
<script type="text/javascript" src="<%=contextPath %>/js/web/function_tree.js" charset="UTF-8"></script>
<script type="text/javascript" src="<%=contextPath%>/js/web/dtree.js"></script>
<script type="text/javascript" src="<%=contextPath%>/js/jslib/calendar2.js"></script>
<title>响应时间汇总查询</title>
</head>

<body onload="loadcalendar()">
<div class="wbox">
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 系统管理 &gt; 系统运行情况 &gt;响应时间汇总查询</div>
<form id="fm" name="fm" method="post">
<input id="SECTION"  type="hidden" name="SECTION" value=""/> <!-- 链接 -->
<input id="TYPE"  type="hidden" name="TYPE" value=""/> <!-- 菜单层级 -->
<input type="hidden" id="orderCol" name="orderCol" value="" />
<input type="hidden" id="order" name="order" value="" />
<input type="hidden" name="tree_root_id" id="tree_root_id" value=""/>
<table class="table_query" border="0">
	<tr>
		<td class="table_query_2Col_label_4Letter" nowrap="nowrap">查询时间：</td>
			<td   nowrap="nowrap">
				<input id="strTime" datatype="1,is_date,20" group="strTime,endTime"  name="STRTIME" class="short_txt" type="text" readonly="readonly" />
				<input class="time_ico" type="button" onclick="showcalendar(event, 'strTime', false);" value="&nbsp;" />至
				<input id="endTime" datatype="1,is_date,20" name="ENDTIME" class="short_txt" type="text" readonly="readonly"/>
				<input class="time_ico" type="button" onclick="showcalendar(event, 'endTime', false);" value="&nbsp;" />
			</td>
			<td class="table_query_2Col_label_4Letter" nowrap="nowrap" >功能类型：</td>
			<td class="table_info_2col_input" nowrap="nowrap">
				<span class="table_query_2Col_input">
		          <select onchange="selType(this,<%=Constant.SYS_USER_SGM%>,<%=Constant.SYS_USER_DEALER%>)" class="short_sel" name="funType" id="funType">
		            <option value="10021002" selected="selected">经销商</option>
		            <option value="10021001">SGM</option>
		          </select>
		        </span>
			</td>
			<td class="table_query_2Col_label_4Letter" nowrap="nowrap" >功能选择：</td>
			<td nowrap="nowrap" >
			<input class="middle_txt" id="FUNNAME" onblur="isCloseTreeDiv(event,this,'dtree')" name="FUNNAME" onclick="showFUNC()" style="cursor: pointer;" readonly="readonly" type="text"  />
			</td>
			<td><input class="normal_btn" id="queryBtn" type="button" value="查 询" onclick="__extQuery__(1);"/></td>
	</tr>
</table>
</form>
</div>
<div id="_page" style="margin-top:15px;display:none;"></div>
<div id="myGrid" ></div>
<div id="myPage" class="pages"></div>
<div id="loader" style='position:absolute;z-index:200;background:#FFCC00;padding:1px;top:4px;display:none;display: none;'></div>
<script type="text/javascript" >
validateConfig.isOnBlur = false;
var func_tree_url = "<%=contextPath%>/sysmng/systemRunSituation/AnswerTime/initFunTree.json";

	var url = "<%=contextPath%>/sysmng/systemRunSituation/AnswerTime/AnsertTimeSearch.json?COMMAND=1";

	function selType(obj,sgmCode,dealerCode) {
		if(obj.value == sgmCode) {
			$('FUNNAME').value="";
			$('SECTION').value="";
			sendAjax(func_tree_url,createTree,'fm');
		}else if(obj.value == dealerCode) {
			$('FUNNAME').value="";
			$('SECTION').value="";
			sendAjax(func_tree_url,createTree,'fm');
		}
	}
	
	var myPage;
	function __extQuery__(page){
		if($('FUNNAME').value != ""){
			$("queryBtn").disabled = "disabled";
			showMask();
			sendAjax(url+(url.lastIndexOf("?") == -1?"?":"&")+"curPage="+page,callBack,'fm');
		}else{
			showErrMsg('FUNNAME','请选择功能','27');
			return;
		}
	}
	var title = '响应时间汇总查询';
	
	var columns = [
					{header: "序号", align:'center', renderer:getIndex,width:30},
					{header: "功能", width:120 ,sortable: true, dataIndex: 'fucName',orderCol:"FUNC_NAME"},
					{header: "Action", width:120 ,sortable: true, dataIndex: 'acName',orderCol:"ACTION_DESC"},
					{header: "平均响应时间（毫秒）", width:150 ,sortable: true, dataIndex: 'avgTime',orderCol:"AVG_RESPONSE_TIME"},
					{header: "最短响应时间（毫秒）", width:150 ,sortable: true, dataIndex: 'minTime',orderCol:"MIN_RESPONSE_TIME"},
					{header: "最长响应时间（毫秒）", width:150 , sortable: true, dataIndex: 'maxTime',orderCol:"MAX_RESPONSE_TIME"}
				  ];

	
   function  decide(){
	   
   }
</script>
</body>
</html>