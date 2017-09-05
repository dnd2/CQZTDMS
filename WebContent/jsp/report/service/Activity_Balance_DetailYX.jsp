<%-- 
创建时间 : 2010.08.27
             创建人:lishuai
             功能描述：索赔单审核，对一条结算单内的多条索赔单进行审核
--%>
<%@page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib uri="/jstl/cout" prefix="c"%>
<%
	String contextPath = request.getContextPath();
	TtAsWrClaimBalancePO balancePO = (TtAsWrClaimBalancePO)request.getAttribute("balancePO");
	Object authCode = request.getAttribute("authCode");//登陆人对应 授权代码（用于判断是否该人有审核权限）
	String isConfirm = (String)request.getAttribute("isConfirm");//是否进行收单操作  true: 是  false:不是
	String status = "";
	String auth_code = "";
	if(balancePO!=null){
		status = balancePO.getStatus().toString();
	}
	if(authCode!=null)
		auth_code = authCode.toString();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<%@page import="com.infodms.dms.common.Constant"%>
<%@page import="com.infodms.dms.po.TtAsWrClaimBalancePO"%>
<%@page import="com.infodms.dms.util.CommonUtils"%><html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	<title>结算室审核</title>
	<script type="text/javascript">
	    function doInit()
		{
		   loadcalendar(); 
		}
		
	</script>
</head>
<body onload="doInit();">
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />
         当前位置：售后服务管理&gt;报表管理&gt;<c:if test="${type == 'YX'}">营销处报表管理</c:if><c:if test="${type == 'JS'}">技术处报表管理</c:if>
</div>
<form method="post" name="fm" id="fm">
<input type="hidden" value="${type}" id="YX" name="YX"/>
<table align="center" class="table_query" border='0'>
	<tr>
		<td width="10%" align="right">活动日期：</td>
		<td width="30%">
		  <input class="short_txt" id="StratDate" name="StratDate" datatype="1,is_date,10"
              maxlength="10" group="StratDate,EndDate"/>
         <input class="time_ico" value=" " onclick="showcalendar(event, 'StratDate', false);" type="button"/>
           至
         <input class="short_txt" id="EndDate" name="EndDate" datatype="1,is_date,10"
             maxlength="10" group="StratDate,EndDate"/>
         <input class="time_ico" value=" " onclick="showcalendar(event, 'EndDate', false);" type="button"/>
       	</td>
		<td align="right" nowrap="true">维修站：</td>
        <td class="table_query_4Col_input" nowrap="nowrap">
			<input class="middle_txt" id="dealerName"  name="dealerName" type="text"/>
		</td>
		<td align="right" nowrap="true">主题名称：</td>
        <td class="table_query_4Col_input" nowrap="nowrap">
			<select style="width: 152px;" name="subject_id" id="subject_id">
				 <option value="" >
    				-请选择-
    			  </option>
	              <c:forEach var="list" items="${list}" >
 				  <option value="${list.subjectId}" >
    				<c:out value="${list.subjectName}"/>
    			  </option>
    			 </c:forEach>
             </select>
		</td>
	</tr>
	<tr>
		<td colspan="5" align="center">
			<input class="normal_btn" type="button" name="button1" id="queryBtn" value="查询" onclick="__extQuery__(1)"/>
			<input class="normal_btn" type="button" name="button1" id="queryBtn11" value="导出明细报表" onclick="imporAct()"/>
		</td>
	</tr>
	<tr>
	<td>
	</td>
	</tr>
</table>
	<!--分页  -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html"/>
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
	
<script type="text/javascript"><!--
		var myPage;
		var url = "<%=contextPath%>/report/service/ActivityBalanceDetail/ActivityBalanceDetailYX.json?COMMAND=1";
		var title = null;
		var columns = [
					{header: "序号",align:'center',renderer:getIndex},	
					{header: "维修站",dataIndex: 'DEALER_NAME',align:'center'},
					{header: "主题名称",dataIndex: 'SUBJECT_NAME',align:'center'},
					{header: "上报月份(元)",dataIndex: 'REPORT_DATE',align:'center'},
					{header: "结算总费用(元)",dataIndex: 'BALANCE_AMOUNT',align:'center',renderer:formatCurrency}
			      ];
	    function settime(d) 
		{
			document.getElementById('calendar').style.display = 'none';
		    document.getElementById(controlid).value = yy + "-" + (zerofill(mm + 1) > 9 ? zerofill(mm + 1) : '0' + zerofill(mm + 1)) ;
        }
      function imporAct()
      {
      	fm.action = "<%=contextPath%>/report/service/ActivityBalanceDetail/imporActivityBalance.do";
		fm.submit();
      }  
        
         
	  function is_date(handle)
	  {
		 var pattern=/^\d{4}(-)(1[012]|0?[1-9]){1,2}$/;
		 if (!pattern.exec(handle.value)) return "日期格式YYYY-MM"
	    
		  return true;
 	  }
</script>
</form>
</body>
</html>