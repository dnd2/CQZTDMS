<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="com.infodms.dms.common.Constant"%>
<%@taglib uri="/jstl/cout" prefix="c" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>入库出库报表</title>
<% String contextPath = request.getContextPath(); %>
<script type="text/javascript">

	function doInit()
	{
		//todayDate("endDate","yyyy-MM-dd hh:mm");
   		loadcalendar();  //初始化时间控件
	}
	
	function txtClr(v1,v2,v3){
		document.getElementById(v1).value = "";
		document.getElementById(v2).value = "";
		document.getElementById(v3).value = "";
	}
</script>
</head>
<body>
<div id="loader" style='position:absolute;z-index:200;background:#FFCC00;padding:1px;top:4px;display:none;display: none;'></div>
<div class="navigation"><img src="<%=request.getContextPath()%>/img/nav.gif" />&nbsp;当前位置： 报表管理&gt;整车销售报表&gt;入库出库报表</div>
  <form method="post" name="fm" id="fm">
   <!-- 查询条件 begin -->
  <table class="table_query">
	  	<tr>
	  	    <td align="right" nowrap="true">日期：</td>
		<td align="left" nowrap="true">
			<input name="START_DATE" type="text" class="short_time_txt" id="START_DATE" readonly="readonly"/> 
			<input name="button" value=" " type="button" class="time_ico" onclick="showcalendar(event, 'START_DATE', false);" />  	
             &nbsp;至&nbsp;
             <input name="END_DATE" type="text" class="short_time_txt" id="END_DATE" readonly="readonly"/> 
			<input name="button" value=" " type="button" class="time_ico" onclick="showcalendar(event, 'END_DATE', false);" /> 
		</td>
		<td align="right" width="15%">产地：</td> 
	  <td align="left">
		 <select name="YIELDLY" id="YIELDLY" class="selectlist" >
		 <option value="">--请选择--</option>
				<c:if test="${list!=null}">
					<c:forEach items="${list}" var="list">
						<option value="${list.AREA_ID}">${list.AREA_NAME}</option>
					</c:forEach>
				</c:if>
	  		</select>
		</td> 
	<td align="right">统计类型：</td> 
	  <td align="left">
		 <label>
				<script type="text/javascript">
						genSelBoxExp("INOUT_TYPE",<%=Constant.INOUT_TYPE%>,"<%=Constant.INOUT_TYPE_01%>",false,"selectlist","","false",'');
					</script>
			</label>
	  </td>
          </tr>
            <tr>
	            <td align="center" colspan="5">
	            	<input type="reset" class="cssbutton" id="resetButton"  value="重置"/>
	            	<input class="normal_btn" type="button" id="queryBtn" name="button1" value="查询"  onclick="_function(1);"/>
	            	<input name="normal_btn" type=button class="cssbutton" onclick="_function(2);" value="下载"/>
	            </td>
          </tr>
         
  </table>
</form>

<!--页面列表 begin -->
<script type="text/javascript" >
function _function(_type){
	  if(_type==1){
		  var fm = document.getElementById("fm");
			fm.target="_blank";
		  fm.action = "<%=contextPath%>/report/storge/InAndOutReport/inAndOutReportInfo.do";
		  fm.submit();
	  }
	  if(_type==2){
		  	var fm = document.getElementById("fm");
		  	fm.action="<%=contextPath%>/report/storge/InAndOutReport/inAndOutReportInfo.do?common=1";  
		   	fm.submit();
		  }
	}
</script>
<!--页面列表 end -->

</body>
</html>