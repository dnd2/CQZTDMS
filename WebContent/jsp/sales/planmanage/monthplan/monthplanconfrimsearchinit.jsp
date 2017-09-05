<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="java.util.List"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c"%>
<%
	String contextPath = request.getContextPath();
%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>月度任务确认</title>
<script type="text/javascript">
	//确认下发
	function importSave(year,month,areaId,planType) {
		/* if(submitForm('frm')){ */
			    var url='<%=contextPath %>/sales/planmanage/MonthTarget/MonthTargetConfirm/monthTargetConfirmSearch.do';
			    document.getElementById("year").value=year;
			    document.getElementById("month").value=month;
			    // document.getElementById("areaId").value=areaId;
			    //document.getElementById("planType").value=planType;
			    var fsm = document.getElementById("frm");
				fsm.action = url;
				fsm.submit();
			/* } */
	}

	var myPage;
    //查询路径
	var url = '<%=contextPath %>/sales/planmanage/MonthTarget/MonthTargetConfirm/monthTargetConfirmSummarySearch.json';		
	var title = null;
	var columns= [
				//{header: "业务范围", dataIndex: 'AREA_NAME', align:'center'},
				//{header: "任务类型", dataIndex: 'PLAN_TYPE', align:'center',renderer:getItemValue},
				{header: "年月", dataIndex: 'PLAN_YM', align:'center'},
				{header: "任务总量", dataIndex: 'AMOUNT', align:'center'},
				{header: "状态", dataIndex: 'CODE_DESC', align:'center'},
				{id:'action',header: "操作",sortable: false,dataIndex: 'AREA_ID',renderer:myLink ,align:'center'}
		        ];
		       
    //修改的超链接
	function myLink(value,meta,record){
  		return String.format("<a href='#' onclick='importSave(\""+ record.data.PLAN_YEAR +"\",\""+ record.data.PLAN_MONTH +"\",\""+ value +"\",\""+ record.data.PLAN_TYPE +"\")'>[确认下发]</a>");
	}
</script>
</head>
<body>
<div class="wbox">
	<div class="navigation"><img src="<%=contextPath %>/img/nav.gif" />&nbsp;当前位置>计划管理>月度任务>月度任务确认</div>
	<form method="post" name="fm"  id="fm">
		<div class="form-panel">
		 <h2>月度任务确认</h2>
			<div class="form-body">
			    <table class="table_query"  style="width: 100%;">
			   	   <tr>
					     <td align="right" style="display: none;" width="20%"> 业务范围：</td>
					     <td style="display: none;" width="60%">
					      <select name="buss_area" id="buss_area" onchange="getPlanVer();">
						       <c:forEach items="${areaBusList}" var="areaBusList" >
						       		<option value="${areaBusList.AREA_ID }">${areaBusList.AREA_NAME }</option>
							   </c:forEach>
					      </select>
					     </td>
				         <td  style="position: relative;left:50%;">
				             <input name="bt1" type="button" class="u-button u-query" onclick="__extQuery__(1);" value="查询" />
				         </td>
			     </tr>
			   </table>
		   </div>
	   </div>
	</form>
<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
</div>
<form name="frm" id="frm" method="post">
<input type="hidden" name="year" id="year" value="" />
<input type="hidden" name="month" id="month" value="" />
<input type="hidden" name="areaId" id="month" value="" />
<input type="hidden" name="planType" id="planType" value="" />
</form>

</body>
</html>
