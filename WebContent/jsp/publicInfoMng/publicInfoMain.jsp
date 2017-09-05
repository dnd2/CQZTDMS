<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="com.infodms.dms.common.Constant"%>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<%@page import="java.util.List"%>
<%
	String contextPath = request.getContextPath();
%>
<%@taglib uri="/jstl/cout" prefix="c" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<script type="text/javascript">
function doInit(){
   	loadcalendar();  //初始化时间控件
}
</script>
<TITLE>售后服务管理</TITLE>

</HEAD>
<BODY>

<div class="navigation"><img src="../../img/nav.gif" />&nbsp;当前位置：售后服务管理&gt;公共信息管理&gt;
	客户信息查询</div>
    <form method="post" name ="fm" id="fm">
    <table  class="table_query">
		<tr>
			<td class="table_query_2Col_label_6Letter">客户姓名：</td>
		    <td><input type='text'  class="middle_txt" name="ctmName"  id='ctmNameId' value=""/></td>
		    <td class="table_query_2Col_label_6Letter">客户电话：</td>
		    <td><input type='text'  class="middle_txt" name="mainPhone"  id='mainPhoneId' value=""/></td>
		</tr>
		<tr>	
			<td class="table_query_2Col_label_6Letter">客户类型：</td>
			<td>
				<script type="text/javascript">
					genSelBoxExp("ctmType",<%=Constant.CUSTOMER_TYPE%>,"",true,"short_sel","","false",'');
		    	</script>
			</td>
			<td class="table_query_2Col_label_6Letter">客户性别：</td>
			<td align="left">
				<script type="text/javascript">
					genSelBoxExp("sex",<%=Constant.GENDER_TYPE%>,"",true,"short_sel","","false",'');
				</script>
			</td>
		</tr>
		<tr>
			<td class="table_query_2Col_label_6Letter">客户星级：</td>
			<td colspan="3"><script type="text/javascript">
					genSelBoxExp("guestStars",<%=Constant.GUEST_STARS%>,"",true,"short_sel",'',"false",'');
				</script>
			</td>
     	</tr>
		<tr>
			<td align="right">购车日期：</td>
			<td align="left" >
				<input type="text" name="dateStart" id="dateStart" value="" type="text" class="short_txt" 
		             datatype="1,is_date,10" group="dateStart,dateEnd" 
		             hasbtn="true" callFunction="showcalendar(event, 'dateStart', false);"/>
		                                 至
		 			<input type="text" name="dateEnd" id="dateEnd" 
		 			value="" type="text" class="short_txt" datatype="1,is_date,10" 
		 			group="dateStart,dateEnd" 
		 			hasbtn="true" callFunction="showcalendar(event, 'dateEnd', false);"/>
			</td>
			<td class="table_query_2Col_label_6Letter">VIN：</td>
			<td><input type="text" class="middle_txt" name="vin" id='vin' value=""/></td>
		</tr>
		<tr>
			<td class="table_query_2Col_label_6Letter">车系：</td>
			<td align="left">
				<select id="series" name="series" class="short_sel" onchange="queryModel();">
					<option value=''>-请选择-</option>
					<c:forEach var="ser" items="${seriesList}">
						<option value="${ser.seriesId}" title="${ser.seriesName}">${ser.seriesName}</option>
					</c:forEach>
				</select>
			</td>
			<td class="table_query_2Col_label_6Letter">车型：</td>
			<td align="left">
				<div id="model_div">
					<script type="text/javascript">
						$('model_div').innerHTML = '<select class="short_sel"><option>-请选择-</option></select>' ;
						function showModel(json){
							$('model_div').innerHTML = json.modelStr;
						}
					</script>
				</div>
			</td>
		</tr>
		<tr>
			<td colspan="4" align="center">
				<input type="button" value="查询" class="normal_btn" onclick="__extQuery__(1)"/>
			</td>
		</tr>
	</table>
	
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" /> 
</form>
<script type="text/javascript">
var url = '<%=contextPath%>/publicInfoMng/PublicInfoMng/mainQuery.json' ;
var title = null ;
var columns = [
				{header:'序号',width:'6%',align:'center',renderer:getIndex},
                {header: "客户名称",dataIndex: 'CTM_NAME',align:'center'},
			    {header: "联系电话", dataIndex: 'MAIN_PHONE', align:'center'},
				{header: "性别", dataIndex: 'SEX', align:'center',renderer:getItemValue},
				{header: "客户类型",dataIndex: "CTM_TYPE",align:'center',renderer:getItemValue},
				{header: "客户星级", dataIndex: 'GUEST_STARS', align:'center',renderer:getItemValue},
				{header: "车系", dataIndex: 'SERIES_NAME', align:'center'},
				{header: "车型", dataIndex: 'MODEL_NAME', align:'center'},
				{header: "VIN", dataIndex: 'VIN', align:'center'},
				{header: "购车日期", dataIndex: 'PURCHASED_DATE', align:'center'},
				{header: "生产基地", dataIndex: 'YIELDLY', align:'center',renderer:getItemValue},
              	{header:'操作',width:'6%',align:'center',renderer:myHandler}
               ];
function myHandler(value,meta,record){
	return '<a href="<%=contextPath%>/publicInfoMng/PublicInfoMng/showDetail.do?id='+record.data.CTM_ID+'">[明细]</a><a href="<%=contextPath%>/publicInfoMng/PublicInfoMng/showHistory.do?vin='+record.data.VIN+'">[维修历史]</a>' ;
}
//车型下拉列表
function queryModel(){
	var id=$('series').value ;
	if(id){
		var url = '<%=contextPath%>/customerRelationships/customerInfo/customerManage/queryModel.json?id='+id ;
		makeNomalFormCall(url,showModel,'fm');
	}
	else
		$('model_div').innerHTML = '<select class="short_sel"><option>-请选择-</option></select>' ;
}
</script>
</BODY>
</html>