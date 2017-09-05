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
<title>年度任务确认</title>
<script type="text/javascript">
	function importSave(year,month,areaId,planType) {
		if(submitForm('frm')){
			    var url='<%=contextPath %>/sales/planmanage/YearTarget/YearTargetConfirm/yearTargetConfirmSearchList.do';
			    document.getElementById("year").value=year;
			    document.getElementById("areaId").value=areaId;
			    // document.getElementById("planType").value=planType;
			    var fsm = document.getElementById("frm");
				fsm.action = url;
				fsm.submit();
			}
	}

	var myPage;
    //查询路径
	var url = '<%=contextPath %>/sales/planmanage/YearTarget/YearTargetConfirm/yearTargetConfirmSummarySearch.json';		
	var title = null;
	var columns= [
				//{header: "业务范围", dataIndex: 'AREA_NAME', align:'center'},
				//{header: "任务类型", dataIndex: 'PLAN_TYPE', align:'center',renderer:getItemValue},
				{header: "年", dataIndex: 'PLAN_YM', align:'center'},
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
<div class="navigation"><img src="<%=contextPath %>/img/nav.gif" />&nbsp;当前位置>计划管理>年度任务>年度任务确认</div>
<div class="form-panel">
<h2>年度任务确认</h2>
<div class="form-body">
	<form method="post" name="fm"  id="fm">
	   <table class="table_query"  style="width: 100%;">
	   	   <tr>
			     <td class="right" style="display: none;"> 业务范围：</td>
			     <td style="display: none;">
			      <select name="buss_area" id="buss_area" onchange="getPlanVer();">
				       <c:forEach items="${areaBusList}" var="areaBusList" >
				       		<option value="${areaBusList.AREA_ID }">${areaBusList.AREA_NAME }</option>
					   </c:forEach>
			      </select>
			     </td>
		         <td  style="position: relative;left:50%;">
		             <input name="bt1" type="button" class="u-button u-query"  onclick="__extQuery__(1);" value="查询" />
		         </td>
	     </tr>
	   </table>
	</form>
</div>
</div>
<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
</div>
<form name="frm" id="frm" method="post">
<input type="hidden" name="year" id="year" value="" />
<input type="hidden" name="areaId" id="areaId" value="" />
<input type="hidden" name="planType" id="planType" value="" />
</form>
</body>
</html>


<%-- <%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/DTD/xhtml1-transitional.dtd">
<%@page import="java.util.List"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c"%>
<head>
<%
String contentPath=request.getContextPath();
%>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<link href="<%=contentPath %>/style/content.css" rel="stylesheet" type="text/css" />
<link href="<%=contentPath %>/style/calendar.css" type="text/css" rel="stylesheet" />
<script type="text/javascript" src="<%=contentPath %>/js/jslib/prototype.js"></script>
<script type="text/javascript" src="<%=contentPath %>/js/jslib/mootools.js"></script>
<script type="text/javascript" src="<%=contentPath %>/js/jslib/calendar.js"></script>
<title>年度目标确认</title>
<script language="JavaScript" src="<%=contentPath %>/js/ut.js"></script>

<script language="JavaScript">
<!--

	function searchSubmit(){
		if(!submitForm('fm')){
			return false;
		}
		$('fm').action = "<%=contentPath %>/sales/planmanage/YearTarget/YearTargetConfirm/yearTargetConfirmSearch.do";
    	$("fm").submit();
	}
//-->
</script>
</head>

<body>
	<div class="wbox">
	<div class="navigation"><img src="<%=contentPath %>/img/nav.gif" />&nbsp;当前位置>计划管理>年度目标>年度目标确认
	</div>
 <%
     List yearOptions=(List)request.getAttribute("options");
     if(null!=yearOptions&&yearOptions.size()>0){
 %>
<form name="fm" method="post" >
<table class="table_query">
  <tr>
    <td class="table_query_label" align="right" width="45%">选择年份：
      <select name='year'>
       <c:forEach items="${options}" var="option"  varStatus="steps">
			<option value="${option.KEYSTR }">${option.VALSTR }</option>
		</c:forEach>
      </select></td>
       <td width="10%">&nbsp;</td>
       <td class="table_query_label" >
	       <input type="button" class="cssbutton"  name="vdcConfirm" value="查询" onclick="searchSubmit();" />
	    </td>
  </tr>
</table>
</form>
</div>
<%
}else{
%>
<div class="wbox">
<table class="table_query">
  <tr>
    <td align="center">
		<font color="red">没有满足条件的数据</font>
    </td>
  </tr>
</table>
</div>
<%
}
%>
</body>
</html>
 --%>