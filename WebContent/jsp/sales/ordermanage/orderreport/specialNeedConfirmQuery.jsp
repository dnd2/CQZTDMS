<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="com.infodms.dms.common.Constant"%>
<%@taglib uri="/jstl/cout" prefix="c" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>订单提报</title>
<% String contextPath = request.getContextPath(); %>
<script type="text/javascript">
</script>
</head>
<body>
<div class="navigation"><img src="<%=request.getContextPath()%>/img/nav.gif" />&nbsp;当前位置： 销售订单管理 &gt; 订单提报 &gt;订做车需求确认</div>
<form method="post" name="fm" id="fm">
<!-- 查询条件 begin -->
	<table class="table_query" >
		<tr>
			<td width="13%"></td>
			<td align="right" width="23%">需求提报日期：</td>
			<td align="left" width="50%">
				<input class="short_txt"  type="text" id="t1" name="startDate" datatype="1,is_date,10" group="t1,t2"  value="" />
				<input class="time_ico" type="button" onClick="showcalendar(event, 't1', false);" value="&nbsp;" />&nbsp;至&nbsp;
				<input class="short_txt"  type="text" id="t2" name="endDate" datatype="1,is_date,10" group="t1,t2" value="" />
				<input class="time_ico" type="button" onClick="showcalendar(event, 't2', false);" value="&nbsp;" />
			</td>
			<td>&nbsp;</td> 
		</tr>
		<tr>
			<td></td>
			<td align="right">业务范围：</td>
			<td align="left">
				<select name="areaId" id="areaId" class="short_sel">
					<option value="-1">-请选择-</option>
					<c:forEach items="${areaList}" var="list">
						<option value="${list.AREA_ID}"><c:out value="${list.AREA_NAME}"/></option>
					</c:forEach>
				</select><input type="hidden" name="area" id="area"/>
			</td>
		</tr>
		<tr>
			<td></td>
			<td></td>
			<td align="center">
				<input name="queryBtn" type=button class="cssbutton" onClick="__extQuery__(1);" value="查询">
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
	var url = "<%=contextPath%>/sales/ordermanage/orderreport/SpecialNeedConfirm/specialNeedConfirmQuery.json";
	var title = null;
	var columns = [
			    {header: "业务范围",dataIndex: 'AREA_NAME',align:'center'},
				{header: "需求提报日期", dataIndex: 'REQ_DATE', align:'center'},
				{header: "订做车改装说明", dataIndex: 'REFIT_DESC', align:'center'},
				{header: "提报数量", dataIndex: 'AMOUNT', align:'center'},
				{header: "操作",sortable: false, dataIndex: 'REQ_ID', align:'center',renderer:myLink}
		      ];
	//超链接设置
	function myLink(value,meta,record){
		var data  = record.data;
  		return String.format("<a href='#' onclick='toDetailCheck(\""+ value +"\",\""+ data.DEALER_ID +"\")'>[确认]</a>");
	}
	//明细链接
	function toDetailCheck(value,dealer_id){
		$('fm').action= '<%=contextPath%>/sales/ordermanage/orderreport/SpecialNeedConfirm/specialNeedDetailConfirmInit.do?&reqId='+value+'&dealerId='+dealer_id;
	 	$('fm').submit();
	}
	//初始化    
	function doInit(){
		loadcalendar();  //初始化时间控件
		var area = "";
		<c:forEach items="${areaList}" var="list">
			var areaId = <c:out value="${list.AREA_ID}"/>
			if(area==""){
				area = areaId;
			}else{
				area = areaId+','+area;
			}
		</c:forEach>
		document.getElementById("area").value=area;
	}
</script>
<!--页面列表 end -->
</body>
</html>