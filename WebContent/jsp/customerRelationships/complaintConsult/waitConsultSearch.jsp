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
<title>待处理咨询查询</title>
<script type="text/javascript">

	function doInit(){
   		loadcalendar();  //初始化时间控件
	}
</script>
</head>
<body>
	<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 客户关系管理 &gt; 投诉咨询管理 &gt;待处理咨询查询</div>
	<form method="post" name = "fm" id="fm">
		<table  border="0" align="center" cellpadding="1" cellspacing="1" class="table_query">
			<th colspan="4"><img class="nav" src="<%=contextPath%>/img/subNav.gif" />查询条件</th>
			
			<tr>
				<td align="right" nowrap="true">VIN号码：</td>
				<td align="left" nowrap="true">
					<input type="text" id="vin" name="vin"/>
				</td>
				<td align="right" nowrap="true">客户姓名：</td>
				<td align="left" nowrap="true">
					<input type="text" id="name" name="name"/>
				</td>
			</tr>
			
			<tr>
				<td align="right" nowrap="true">联系电话：</td>
				<td align="left" nowrap="true">
					<input type="text" id="tele" name="tele"/>
				</td>
				<td align="right" nowrap="true">受理人：</td>
				<td align="left" nowrap="true">
					<input type="text" id="accuser" name="accuser"/>
				</td>
			</tr>
			<tr>
				<td align="right" nowrap="true">受理日期：</td>
				<td align="left" nowrap="true">
					<input class="short_txt" id="dateStart" name="dateStart" datatype="1,is_date,10"
                           maxlength="10" group="dateStart,dateEnd"/>
                    <input class="time_ico" value=" " onclick="showcalendar(event, 'dateStart', false);" type="button"/>
                	    至
                    <input class="short_txt" id="dateEnd" name="dateEnd" datatype="1,is_date,10"
                           maxlength="10" group="dateStart,dateEnd"/>
                    <input class="time_ico" value=" " onclick="showcalendar(event, 'dateEnd', false);" type="button"/>
				</td>
			</tr>
	
			<tr>
				<td colspan="8" align="center">
					<input class="normal_btn" type="button" value="查询" name="recommit" id="queryBtn" onclick="__extQuery__(1);" />
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

	var myPage;
    //查询路径
	var url = "<%=contextPath%>/customerRelationships/complaintConsult/WaitConsultSearch/queryWaitConsultSearch.json";
				
	var title = null;

	var columns = [
				{header: "序号", dataIndex: 'center', renderer:getIndex},
				{id:'action',header: "操作",sortable: false,dataIndex: 'CPID',renderer:myLink},
				{header: "客户姓名",dataIndex: 'CTMNAME',align:'center'},
				{header: "联系电话", dataIndex: 'PHONE', align:'center'},
				{header: "受理日期",dataIndex: 'ACCDATE',align:'center'},
				{header: "受理人", dataIndex: 'ACCUSER', align:'center'},
				{header: "状态",dataIndex: 'DEALMODE',align:'center',renderer:getItemValue},
				{header: "咨询内容", dataIndex: 'CONTENT', align:'center'},
				{header: "业务类型",dataIndex: 'BIZTYPE',align:'center'}
		      ];

	function myLink(value,meta,record){
		if(record.data.DEALMODE == <%=Constant.CONSULT_STATUS_FINISH%>){
			return String.format("<input name='detailBtn' type='button' class='normal_btn' onclick='watchConsultSearch(\""+ value +"\")' value='查看'/>");
		}else{
			return String.format("<input name='detailBtn' type='button' class='normal_btn' onclick='watchConsultSearch(\""+ value +"\")' value='查看'/><input name='detailBtn' type='button' class='normal_btn' onclick='updateConsult(\""+ value +"\")' value='处理'/>");
		}
	}
	function watchConsultSearch(value){		
		window.open('<%=contextPath%>/customerRelationships/complaintConsult/WaitConsultSearch/waitConsultSearchWatch.do?id='+value) ;
	}
	function updateConsult(value){
		window.open('<%=contextPath%>/customerRelationships/complaintConsult/WaitConsultSearch/waitConsultSearchUpdate.do?id='+value) ;
	}
	function refreshData(){
		__extQuery__(1);
	}

</script>
</body>
</html>