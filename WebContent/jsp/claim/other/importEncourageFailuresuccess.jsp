<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="java.util.List"%>
<%@ page import="com.infodms.dms.common.Constant" %>
<%@ page import="java.util.Map"%>
<%@ page import="java.math.BigDecimal"%>
<%@taglib uri="/jstl/cout" prefix="c" %>
<%
	String contextPath = request.getContextPath();
%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>正负激励导</title>
</head>
<body >
<div class="wbox">
	<div class="navigation"><img src="<%=contextPath %>/img/nav.gif" />当前位置&gt;售后服务管理&gt;其他功能&gt;正负激励导入</div>
<table class="table_list" width="85%" align="center" border="0">		
	   <tr>
	      <th style="width:10%" >补贴编号</th>
		    <th style="width:10%" align="center">产地</th>
		    <th style="width:10%">经销商代码</th>
            <th style="width:20%">经销商名称</th>
            <th style="width:10%">奖惩类型</th>
            <th style="width:5%">奖惩劳务费</th>
            <th style="width:5%">奖惩材料费</th>
            <th style="width:20%">奖惩原因</th>
            <th style="width:10%">补贴类型</th>
       </tr>
<c:forEach items="${list}" var="aa">
<tr>
<TD>${aa.LABOUR_BH }</TD>
<TD>${aa.AREA_NAME }</TD>
<TD>${aa.DEALER_CODE }</TD>
<TD>${aa.DEALER_NAME }</TD>
<TD>
  <script type='text/javascript'>
		       var proCode=getItemValue('${aa.FINE_TYPE}');
		       document.write(proCode) ;
		     </script>
</TD>
<TD>${aa.LABOUR_SUM }</TD>
<TD>${aa.DATUM_SUM }</TD>
<TD>${aa.FINE_REASON }</TD>
<TD>${aa.REMARK }</TD>
</tr>

</c:forEach>

  	
</table>
<form>
</form>
<form  name="frm" id="frm">
<table class="table_query" width="85%" align="center" border="0"  id="roll">	
	<tr align="center" class="table_list_row1">
		<th colspan="6">
			<div align="center">
				<input class="cssbutton" type="button" id="savebtn" name='saveResButton' onclick='isSave();' value='确认导入' />
				<input class="cssbutton" type='button' name='saveResButton' onclick='history.back();' value='返回' />
			</div>
		</th>	
  	</tr>
</table>
</form>
<script type="text/javascript">
 function isSave(){
          if(submitForm('frm')){
      	    MyConfirm("是否确认保存信息?",importSave);
          }
       }
function importSave() {
		document.getElementById("savebtn").disabled=true;
		frm.action = "<%=contextPath%>/claim/other/Bonus/importExcel.do";
		frm.submit();
}
</script>
</body>
</html>
