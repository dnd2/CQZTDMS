<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%> 
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="java.util.*"%>
<%@ page import="com.infodms.dms.common.Constant"%>
<%@ page import="com.infodms.dms.util.CalendarUtil"%>
<%@taglib uri="/jstl/cout" prefix="c"%>
<%
	String contextPath = request.getContextPath();
%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>杂项入库</title>
</head>
<body onload="__extQuery__(1);">
<div class="wbox">
<div class="navigation"><img src="<%=request.getContextPath()%>/img/nav.gif" />&nbsp;当前位置：系统管理&gt;系统业务参数维护&gt; 日志查询</div>
<form method="post" name ="fm" id="fm">
    <table class="table_query" >
	    <tr id="groupId">
	        <td width="20%" align="right">姓名：</td>
	        <td width="30%">
	        	<select id="id" name="id" class="short_sel" >
					<option value=''>-请选择-</option>
					<c:forEach var="seat" items="${seats}">
						<option value="${seat.ID}" title="${seat.NAME}">${seat.NAME}</option>
					</c:forEach>
				</select>
	        </td>
            <td width="20%" align="right">登入时间:</td>
            <td width="30%" align="left" bgcolor="#FFFFFF">&nbsp;
                <input id="checkSDate" class="short_txt" name="checkSDate" datatype="1,is_date,10" maxlength="10" group="checkSDate,checkEDate"/>
                <input class="time_ico" onclick="showcalendar(event, 'checkSDate', false);" value=" " type="button" />
                &nbsp;至&nbsp;
                <input id="checkEDate" class="short_txt" name="checkEDate" datatype="1,is_date,10" maxlength="10" group="checkSDate,checkEDate"/>
                <input class="time_ico" onclick="showcalendar(event, 'checkEDate', false);" value=" " type="button" />
            </td>
        </tr>
	
        <tr>
            <td align='center' colspan="4">
            <input name="queryBtn" id="queryBtn" type="button" class="normal_btn" onclick="__extQuery__(1);" value="查 询"/>
            </td>
        </tr>
	</table>
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
</form>
</div>

<script language="javascript">
	loadcalendar();  //初始化时间控件
	var myPage;
	var url = "<%=contextPath%>/sysbusinesparams/businesparamsmanage/CRMSeatLogManager/getMainList.json";
	var title = null;
	var columns = [
                {header: "姓名", dataIndex: 'NAME', align:'center'},
                {header: "分机号", dataIndex: 'SE_EXT', align:'center'},
                {header: "是否管理员", dataIndex: 'SE_IS_MANAMGER', align:'center',renderer:getItemValue},
                {header: "座席级别", dataIndex: 'SE_LEVEL', align:'center',renderer:getItemValue},
                {header: "登入时间", dataIndex: 'LOGIN_DATE', align:'center'},
                {header: "登出时间", dataIndex: 'LOGOUT_DATE', align:'center'},
                {header: "日期", dataIndex: 'LOG_DAYS', align:'center'},
                {header: "IP地址", dataIndex: 'IP', align:'center'},
                {header: "主机名", dataIndex: 'HOSTNAME', align:'center'}
                //{header: "操作",  align:'center',sortable: false,dataIndex: 'MISC_ORDER_ID',renderer:miscView}
		      ];
        
</script>

</body>
</html>