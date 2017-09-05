<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="com.infodms.dms.common.Constant"%>
<%@taglib uri="/jstl/cout" prefix="c"%>
<%
	String contextPath = request.getContextPath();
	
%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta http-equiv="X-UA-Compatible" content="IE=7">
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>配件销售退货出库</title>

</head>
<body onunload='javascript:destoryPrototype()' onload="__extQuery__(1);loadcalendar();">
<div class="wbox">
	<div class="navigation"> <img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置：
	 配件管理&gt;配件仓库管理  >配件退货管理&gt;销售退货出库
	</div>
<form name="fm" id="fm" method="post" >
	<table class="table_query" width=100% border="0" align="center" cellpadding="1" cellspacing="1" >
		<th colspan="4"><img src="<%=contextPath%>/img/subNav.gif" alt="" class="nav" />查询条件</th>
	    <tr >
	       <td width="20%" class="table_query_right" align="right"> 退货单号：</td>
	       <td width="30%"   align="left"><input class="middle_txt" type="text" name="RETURN_CODE" id="RETURN_CODE"/></td>
	       <td width="20%" class="table_query_right"  align="right">制单日期：</td>
	       <td width="30%" align="left">
           <div align="left">
           		<input name="startDate" id="t1" value="" type="text" class="short_txt" datatype="1,is_date,10" group="t1,t2">
           		<input name='button3' type='button' class='time_ico' value=" " title="点击选择时间" onclick="showcalendar(event, 't1', false);"/>
           		&nbsp;至&nbsp;
           		<input name="endDate" id="t2" value="" type="text" class="short_txt" datatype="1,is_date,10" group="t1,t2">
           		<input name='button3' type='button' class='time_ico' value=" " title="点击选择时间" onclick="showcalendar(event, 't2', false);"/>
            </div>
           </td>
      </tr>    
	  <tr>
	   <td   colspan="4" align="center">
	   		<input name="BtnQuery" id="queryBtn" class="normal_btn" type="button" value="查 询" onclick="__extQuery__(1);"/>
       </td>
      </tr>
	</table>
  
 
  <jsp:include page="${contextPath}/queryPage/orderHidden.html" />
  <jsp:include page="${contextPath}/queryPage/pageDiv.html" />
</form>
<script type="text/javascript" >
autoAlertException();//输出错误信息
	var myPage;

	var url = "<%=contextPath%>/parts/storageManager/partReturnManager/PartDlrReturnOutManager/queryPartDlrReturnApplyInfo.json";
				
	var title = null;

	var columns = [
				{header: "序号", align:'center',renderer:getIndex},
				{header: "退货单号", dataIndex: 'RETURN_CODE', align:'center'},
				{header: "退货单位", dataIndex: 'DEALER_NAME', align:'center'},
				{header: "制单单位", dataIndex: 'CREATE_DEALER', align:'center'},
				{header: "制单人", dataIndex: 'CREATE_NAME', align:'center'},
				{header: "制单日期", dataIndex: 'CREATE_DATE', align:'center',renderer:formatDate},
				{header: "提交日期", dataIndex: 'APPLY_DATE', align:'center',renderer:formatDate},
				{header: "审核日期", dataIndex: 'VERIFY_DATE', align:'center',renderer:formatDate},
				{header: "退货原因", dataIndex: 'REMARK', align:'center'},
				{id:'action',header: "操作",sortable: false,dataIndex: 'RETURN_ID',renderer:myLink ,align:'center'}
		      ];
	    
//设置超链接  begin    
	  
	
	//设置超链接
	function myLink(value,meta,record){
			return String.format("<a href=\"#\" onclick='outPartReturn(\""+value+"\")'>[出库]</a>");
	}

	//销售退货出库
	function outPartReturn(value)
	{
		window.location.href = '<%=contextPath%>/parts/storageManager/partReturnManager/PartDlrReturnOutManager/queryPartDlrReturnChkInit.do?returnId='+value;
	}
	
	//格式化日期
	function formatDate(value,meta,record){
		var output = value.substr(0,10);
		return output;
	}

//设置超链接 end
	
</script>
</div>
</body>
</html>
