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
<title>咨询查询</title>
<script type="text/javascript">

	function doInit(){
   		loadcalendar();  //初始化时间控件
	}
</script>
</head>
<body>
	<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 客户关系管理 &gt; 投诉咨询管理 &gt;咨询查询</div>
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
				<td align="right" nowrap="true">状态：</td>
				<td align="left" nowrap="true">
					<script type="text/javascript">
						genSelBoxExp("status",<%=Constant.CONSULT_STATUS%>,"",true,"short_sel","","false",'');
					</script>
				</td>
			</tr>
			<tr>
				<td align="right" nowrap="true">咨询类型：</td>
				<td align="left" nowrap="true">
					<select id="type" name="type" class="short_sel">
						<option value=''>-请选择-</option>
						<c:forEach var="bc" items="${bclist}">
							<option value="${bc.CODEID}" title="${bc.CODEDESC}">${bc.CODEDESCVIEW}</option>
						</c:forEach>
					</select>
				</td>
				<td align="right" nowrap="true">省份：</td>
				<td align="left" nowrap="true">
					<select id="pro" name="pro" class="short_sel">
						<option value=''>-请选择-</option>
						<c:forEach var="pro" items="${proviceList}">
							<option value="${pro.REGION_CODE}" title="${pro.REGION_NAME}">${pro.REGION_NAME}</option>
						</c:forEach>
					</select>
				</td>
			</tr>
			<tr>
				<td align="right" nowrap="true">受理人：</td>
				<td align="left" nowrap="true">
					<input type="text" id="dealName" name="dealName"/>
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
				<td align="right" nowrap="true">咨询内容：</td>
				<td align="left" nowrap="true">
					<input type="text" id="cont" name="cont"/>
				</td>
			</tr>
	
			<tr>
				<td colspan="8" align="center">
					<input class="normal_btn" type="button" value="查询"  name="BtnQuery" id="queryBtn"  onclick="__extQuery__(1);" />
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
		fm.action = '<%=contextPath%>/customerRelationships/complaintConsult/ConsultSearch/consultSearchDownExcel.do';
		fm.submit();
	}
	var myPage;
    //查询路径
	var url = "<%=contextPath%>/customerRelationships/complaintConsult/ConsultSearch/queryConsultSearch.json";
				
	var title = null;

	var columns = [
				{header: "序号", dataIndex: 'center', renderer:getIndex},
				{id:'action',header: "操作",sortable: false,dataIndex: 'CPID',renderer:myLink},
				{header: "客户姓名",dataIndex: 'CTMNAME',align:'center'},
				{header: "联系电话", dataIndex: 'PHONE', align:'center'},
				{header: "受理日期",dataIndex: 'ACCDATE',align:'center'},
				{header: "受理人", dataIndex: 'ACCUSER', align:'center'},
				{header: "状态",dataIndex: 'CPSTATUS',align:'center',renderer:getItemValue},
				{header: "咨询内容", dataIndex: 'CONTENT', align:'center'},
				{header: "回复内容", dataIndex: 'CPCONTENT', align:'center'},
				{header: "业务类型",dataIndex: 'BIZTYPE',align:'center'}
		      ];

	function myLink(value,meta,record){
		return String.format("<input name='detailBtn' type='button' class='middle_btn' onclick='watchConsultSearch(\""+ value +"\")' value='查看'/>&nbsp;<input name='detailBtn' type='button' class='middle_btn' onclick='editConsult(\""+ value +"\")' value='编辑'/>&nbsp;<input name='detailBtn' type='button' class='middle_btn' onclick='updateConsult(\""+ value +"\")' value='处理'/>");	
	}
	function watchConsultSearch(value){		
		window.open('<%=contextPath%>/customerRelationships/complaintConsult/ConsultSearch/consultSearchWatch.do?openPage=1&id='+value);
	}
	function updateConsult(value){
		window.open('<%=contextPath%>/customerRelationships/complaintConsult/ConsultSearch/consultSearchUpdate.do?openPage=1&id='+value);
	}
	function editConsult(value){
		window.open('<%=contextPath%>/customerRelationships/complaintConsult/ConsultSearch/consultSearchEdit.do?openPage=1&id='+value);
	}
	function refreshData(){
		__extQuery__(1);
	}

</script>
</body>
</html>