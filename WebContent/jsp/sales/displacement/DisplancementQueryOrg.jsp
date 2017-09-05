<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="com.infodms.dms.common.Constant" %>
<%@taglib uri="/jstl/cout" prefix="c" %>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>二手车置换审核记录查询</title>
<script type="text/javascript">
function doInit(){
	loadcalendar();  //初始化时间控件
}
<%
String contextPath = request.getContextPath();
%>
</script>
</head>
<body> 
	<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置：  整车销售 &gt; 客户信息管理 &gt; 二手车置换查询</div>
	<form id="fm" name="fm" method="post">
		<input type="hidden" name="curPage" id="curPage" value="1" />
		<input type="hidden" id="dlrId" name="dlrId" value="" />
		<table class="table_query" border="0">
			<tr>
		<td align="right" width="20%">二手车置换类型：</td>
		<td align="left" width="30%">
		<script type="text/javascript">
					genSelBoxExp("displacement_type",<%=Constant.DisplancementCarrequ_replace%>,"",false,"short_sel",'',"false",'');
			</script>
		</td>
				<td class="table_query_3Col_input" >
					<input type="hidden" name="vehicle_id" id="vehicle_id" />
					<input type="button" class="cssbutton" onclick="totalQuery();" value=" 查  询  " /> 
				</td>
			</tr>
		</table>
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
	</form>
<script type="text/javascript">
	var myPage;
	var columns;
	var url;
	var title = null;
	function totalQuery(){
		var a=<%=Constant.DisplancementCarrequ_replace_1%>;
		var b=document.getElementById("displacement_type").value;
		if(b==a){
			url= "<%=contextPath%>/sales/displacement/DisplacementCar/DisplacementQueryCekOrgCar.json?COMMAND=1";
			columns = [
						{header: "旧车底盘号 ", dataIndex: 'OLD_VIN', align:'center'},
						{header: "旧车品牌名称", dataIndex: 'OLD_BRAND_NAME', align:'center'},
						{header: "新车购车日期", dataIndex: 'NEW_SALES_DATE', align:'center'},
						{header: "新车底盘号", dataIndex: 'NEW_VIN', align:'center'},
						{header: "审核状态", dataIndex: 'OPERATE_STATUS', align:'center',renderer:getItemValue},
						{id:'action',header: "操作", walign:'center',idth:70,dataIndex: 'DISPLACEMENT_ID',renderer:myLink}
				      ];
			__extQuery__(1)
		}else{
			url= "<%=contextPath%>/sales/displacement/DisplacementCar/DisplacementQueryCekOrgCar.json?COMMAND=1";
			columns = [
						{header: "旧车底盘号 ", dataIndex: 'OLD_VIN', align:'center'},
						{header: "旧车品牌名称", dataIndex: 'OLD_BRAND_NAME', align:'center'},
						{header: "车辆报废时间", dataIndex: 'SCRAP_DATE', align:'center'},
						{header: "报废证明编号", dataIndex: 'SCRAP_CERTIFY_NO', align:'center'},
						{header: "审核状态", dataIndex: 'OPERATE_STATUS', align:'center',renderer:getItemValue},
						{id:'action',header: "操作", walign:'center',idth:70,dataIndex: 'DISPLACEMENT_ID',renderer:myLink}
				      ];
			__extQuery__(1)
		}
	}
	function myLink(DISPLACEMENT_ID){
        return String.format(
        		 "<a href=\"#\" onclick=\"chkArea(" + DISPLACEMENT_ID + ");\">[二手车置换审核]</a>");
    }

	function chkArea(DISPLACEMENT_ID) {
		var url = "<%=contextPath%>/sales/displacement/DisplacementCar/DisplacementQueryCekOrgQueryCar.do?DISPLACEMENT_ID="+DISPLACEMENT_ID ;
		$('fm').action=url;
		$('fm').submit();
		//makeCall(url, answer, {vehicleId: vehicle_id}) ;
	}

	
</script>    
</body>
</html>