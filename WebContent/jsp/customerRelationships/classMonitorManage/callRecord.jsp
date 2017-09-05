<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.Constant" %>
<%
	String contextPath = request.getContextPath();
%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>通话记录查询</title>
<script type="text/javascript">

	function doInit(){
   		loadcalendar();  //初始化时间控件
	}
</script>
</head>
<body onload="__extQuery__(1)">
	<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 客户关系管理 &gt; 班长管理 &gt;通话记录查询</div>
	<form method="post" name = "fm" id="fm">
		<table  border="0" align="center" cellpadding="1" cellspacing="1" class="table_query">
			<th colspan="4"><img class="nav" src="<%=contextPath%>/img/subNav.gif" />通话记录查询</th>
			
			<tr>
				<td align="right" nowrap="true">工号：</td>
				<td align="left" nowrap="true">
					<input type="text" id="account" name="account"/>
				</td>
				<td align="right" nowrap="true">分机号码：</td>
				<td align="left" nowrap="true">
					<input type="text" id="ext" name="ext"/>
				</td>
			</tr>
			
			<tr>
				<td align="right" nowrap="true">呼入时间：</td>
				<td align="left" nowrap="true">
					<input class="short_txt" id="dateStartOne" name="dateStartOne" datatype="1,is_date,10"
                           maxlength="10" group="dateStartOne,dateStartTwo"/>
                    <input class="time_ico" value=" " onclick="showcalendar(event, 'dateStartOne', false);" type="button"/>
                	    至
                    <input class="short_txt" id="dateStartTwo" name="dateStartTwo" datatype="1,is_date,10"
                           maxlength="10" group="dateStartOne,dateStartTwo"/>
                    <input class="time_ico" value=" " onclick="showcalendar(event, 'dateStartTwo', false);" type="button"/>
				</td>
				<td align="right" nowrap="true">主叫号码：</td>
				<td align="left" nowrap="true">
					<input type="text" id="number" name="number"/>
				</td>
			</tr>
			
			<tr>
				<td align="right" nowrap="true">来电类型：</td>
				<td align="left" nowrap="true">
				<script type="text/javascript">
		    			genSelBoxExp("inComeType",<%=Constant.CALL_TYPE%>,"",true,"short_sel","","false",'');
		    	</script>
				</td>
				<td align="right" nowrap="true">呼电类型：</td>
				<td align="left" nowrap="true">
				<script type="text/javascript">
		    			genSelBoxExp("callType",<%=Constant.CALL_IN_OUT_TYPE%>,"",true,"short_sel","","false",'');
		    	</script>
				</td>
			</tr>
			<tr>
				<td align="right" nowrap="true">服务质量：</td>
				<td align="left" nowrap="true">
				<script type="text/javascript">
		    			genSelBoxExp("point",<%=Constant.GRADE_TYPE%>,"",true,"short_sel","","false",'');
		    	</script>
				</td>
				<!-- 艾春 2013.11.30 添加通话记录查询条件 -->
				<td align="right" nowrap="true">未接电话类型：</td>
				<td align="left" nowrap="true">
					<select id="missedType" name="missedType" class="short_sel">
						<option value="">-请选择-</option>
						<option value="0">已接电话</option>
						<option value="1">坐席未接</option>
						<option value="2">坐席全忙</option>
					</select>
				</td>
				<!-- 艾春 2013.11.30 添加通话记录查询条件 -->
			</tr>
			
			<tr>
				<td align="right" nowrap="true">是否坐席：</td>
				<td align="left" nowrap="true">
				<script type="text/javascript">
		    			genSelBoxExp("if_type",<%=Constant.IF_TYPE%>,"",true,"short_sel","","false",'');
		    	</script>
				</td>
			</tr>
			<tr>
				<td colspan="8" align="center">
					<input class="normal_btn" type="button" value="查询" name="recommit" id="queryBtn" onclick="__extQuery__(1);" />
					&nbsp;
          			<input id="downExcel" name="downExcel" type="button" value="转Excel" class="normal_btn" onclick="downExcelQuery();" />
        		</td>
			</tr>
		</table>
		
	 <!-- 查询条件 end -->
	 <!--分页 begin -->
		<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
		<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
	 <!--分页 end -->
	</form>
<script type="text/javascript">
	function downExcelQuery(){
		fm.action = '<%=contextPath%>/customerRelationships/classMonitorManage/callRecord/callRecordDownExcel.do';
		fm.submit();
	}
	
	var myPage;
    //查询路径
	var url = "<%=contextPath%>/customerRelationships/classMonitorManage/callRecord/queryCallRecord.json";
				
	var title = null;

	var columns = [
				{header: "主叫号码",dataIndex: 'CRNUMBER',align:'center'},
				{header: "开始时间", dataIndex: 'STADATE', align:'center'},
				{header: "结束时间",dataIndex: 'ENDDATE',align:'center'},
				{header: "通话时长", dataIndex: 'TALKTIME', align:'center'},
				{header: "振铃时长", dataIndex: 'RINGTIME', align:'center'},
				{header: "等待时长", dataIndex: 'WAITTIME', align:'center'},
				{header: "工号",dataIndex: 'SEATSNO',align:'center'},
				{header: "坐席名",dataIndex: 'SEATSNAME',align:'center'},
				{header: "分机号", dataIndex: 'EXT', align:'center'},
				{header: "呼叫类型", dataIndex: 'CALLTYPE', align:'center',renderer:getItemValue},
				{header: "来电类型",dataIndex: 'INCOMETYPE',align:'center',renderer:getItemValue},
				{header: "服务质量",dataIndex: 'POINTS',align:'center',renderer:getItemValue},
				{id:'action',header: "播放",sortable: false,dataIndex: 'CR_RECORDADDR',renderer:myLink}
		      ];

	function myLink(value,meta,record){
	    if(value.length > 2)
	    {
	    	return String.format("<input name='detailBtn' type='button' class='normal_btn' onclick='viewDetail(\""+ value +"\")' value='播放'/>");
	    }else
	    {
	    	return "";
	    }
	}
	
	function viewDetail(value){	
		window.location.href = "<%=contextPath%>/customerRelationships/classMonitorManage/callRecord/callRecordReplay.do?url="+value;
	}
	//页面跳转：
	function sendPage(){
		window.location.href = "<%=contextPath%>/customerRelationships/classMonitorManage/callRecord/callRecordInit.do";
	}
</script>


</body>
</html>