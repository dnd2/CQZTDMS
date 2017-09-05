<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java"  pageEncoding="UTF-8"%>
<%
	String contextPath = request.getContextPath();
%>
<%@ page import="com.infodms.dms.common.Constant"%>
<%@taglib uri="/jstl/cout" prefix="c" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>现在BO汇总查询</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
<script language="javascript" type="text/javascript">
	function doInit(){
		getDate();
		__extQuery__(1);
	}
</script>
</head>
<body>
  <div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置：  配件销售管理  &gt; 现场BO汇总查询</div>
  <form name='fm' id='fm'>
  <input type="hidden" name="parentOrgId" id="parentOrgId" value="${parentOrgId }"/>
  <input type="hidden" name="parentOrgCode" id="parentOrgCode" value="${parentOrgCode }"/>

  <table class="table_query">
       <tr>            
        <td width="10%"   class="right">订货单位：
        </td>            
        <td width="20%">
			<input  class="middle_txt" id="dealerName"  name="dealerName" type="text" value="" />
        </td>
		<td width="10%"   class="right">BO日期：</td>
		<td width="25%"><input id="checkSDate" class="short_txt"
			name="checkSDate" datatype="1,is_date,10" maxlength="10"
			group="checkSDate,checkEDate" style="width:80px;"/> 
			<input class="time_ico" value=" " type="button" />
			至 
			<input id="checkEDate"
			class="short_txt" name="checkEDate" datatype="1,is_date,10"
			maxlength="10" group="checkSDate,checkEDate"style="width:80px;" /> 
			<input class="time_ico" value=" " type="button" />
		</td>
        <td width="10%"   class="right">配件编码：</td>
		<td width="20%">
		  <input class="middle_txt" id="partOldcode"  name="partOldcode" type="text" value=""/>
		</td>
       </tr>
       <tr>
         <td colspan="6" class="center">
          <input class="normal_btn" type="button" value="查 询" name="BtnQuery" id="queryBtn" onclick="__extQuery__(1)"/>
          <input class="normal_btn" type="button" value="导 出" onclick="exportLocBoCntExcel()"/>
		  <input class="normal_btn" type="button" name="button1" value="关 闭"  onclick="_hide();"/>
         </td>
       </tr>       
 	</table>
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
<script type="text/javascript" >
	var myPage;
	var url = "<%=contextPath%>/parts/salesManager/BOManager/locBOHndAction/locBOCountSearch.json";
	var title = null;
	var columns = [
					{header: "序号", dataIndex: 'DEALER_ID', renderer:getIndex},
					{header: "订货单位", dataIndex: 'DEALER_NAME',style:'text-align:left; padding-left:8%;'},
					{header: "销售单位", dataIndex: 'SELLER_NAME',style:'text-align:left; padding-left:8%;'},
					{header: "现场BO总数", dataIndex: 'BO_TOTAL_QTY',style:'text-align:right; padding-right:6%;'}
					
		      	  ];

	//汇总下载
	function exportLocBoCntExcel(){
		document.fm.action="<%=contextPath%>/parts/salesManager/BOManager/locBOHndAction/exportLocBoCountExcel.do";
		document.fm.target="_self";
		document.fm.submit();
	}

	function getDate(){
		var dateS = "";
		var dateE = "";
		var myDate = new Date();
	    var year = myDate.getFullYear();   //获取完整的年份(4位,1970-????)
	    var moth = myDate.getMonth();      //获取当前月份(0-11,0代表1月)
	    if(moth < 10){
	    	if(0 < moth){
		    	moth = "0" + moth;
		    }else{
		    	year = myDate.getFullYear() - 1;
		    	moth = moth + 12;
		    	if(moth < 10)
			    {
		    		moth = "0" + moth;
			    }
		    }
	    }
	    var day = myDate.getDate();       //获取当前日(1-31)
	    if(day < 10){
	    	day = "0" + day;
	    }
	    
	    dateS = year + "-" + moth + "-" + day;

	    moth = myDate.getMonth() + 1;	//获取当前月份(0-11,0代表1月)
	    if(moth < 10){
	    	moth = "0" + moth;
	    }
	    
	    dateE = myDate.getFullYear() + "-" + moth + "-" + day; 

	    document.getElementById("checkSDate").value = dateS;
	    document.getElementById("checkEDate").value = dateE;
	}
</script> 
</form>
</body>
</html>