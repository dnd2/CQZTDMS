<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="com.infodms.dms.common.Constant"%>
<%@taglib uri="/jstl/cout" prefix="c" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>配件出入库明细查询</title>
<% String contextPath = request.getContextPath(); %>
<script type="text/javascript">
	function doInit(){
		loadcalendar();  //初始化时间控件
		__extQuery__(1);
	}
</script>

</head>
<body>
<img src="<%=request.getContextPath()%>/img/nav.gif" />&nbsp;当前位置： 配件管理&gt;基本信息查询&gt;经销商库存明细查询
<form name="fm" id="fm">
<input name="partId" type="hidden" value="<c:out value="${partId}"/>" />
<input name="dealerId" type="hidden" value="<c:out value="${dealerId}"/>" />
<!-- 查询条件 begin -->
<table class="table_edit">
    <tr>
    <td class="table_query_2Col_label_6Letter">出入库时间：</td>
      <td class="table_query_2Col_input">
      	<input name="beginTime" id="t1" value="" type="text" class="short_txt" datatype="1,is_date,10" group="t1,t2" hasbtn="true" callFunction="showcalendar(event, 't1', false);">
      	至
      	<input name="endTime" id="t2" value="" type="text" class="short_txt" datatype="1,is_date,10" group="t1,t2" hasbtn="true" callFunction="showcalendar(event, 't2', false);">
      </td>
      <td class="table_query_2Col_label_6Letter">类型：</td>
      <td class="table_query_2Col_input">
      	<script type="text/javascript">
 			genSelBoxExp("doStatus",<%=Constant.DO_STATUS%>,"",true,"short_sel","","false",'');
		</script>
      </td>
    </tr>
  	<tr>
      <td class="">&nbsp;</td>
      <td class="">&nbsp;</td>
      <td class="">&nbsp;</td>
      <td class="">&nbsp;</td>
      <td class="">&nbsp;</td>
      <td class="">&nbsp;</td>
      <td class="table_query_2Col_label_6Letter">
      	<input type="button" name="BtnQuery" id="queryBtn"  value="查询"  class="normal_btn" onClick="__extQuery__(1)" >
      </td>
  	</tr>
</table>
<!-- 查询条件 end -->
<!--分页 begin -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
<!--分页 end -->
</form>
<form name="form1">
   <table class="table_list" id="table1" >
  	  <tr>
  	  <td>&nbsp;</td>
      <td>&nbsp;</td>
      <td><input class="normal_btn" type="button" value="关 闭" onclick="_hide();"></td>
      <td>&nbsp;</td>
      <td>&nbsp;</td>
    </tr>
   </table>
  </form>
<!--页面列表 begin -->
<script type="text/javascript" >

	var myPage;

	var url = "<%=contextPath%>/partsmanage/infoSearch/DealerDlrstockInfo/queryPartDlrmoveDetailInfo.json";
				
	var title = null;

	var columns = [
				{header: "配件号", dataIndex: 'PART_CODE', align:'center'},
				{header: "配件名称", dataIndex: 'PART_NAME', align:'center'},
				{header: "类型", dataIndex: 'DO_STATUS', align:'center',renderer:getItemValue},
				{header: "单位", dataIndex: 'UNIT', align:'center'},
				{header: "数量", dataIndex: 'QUANTITY', align:'center'},
				{header: "时间", dataIndex: 'DO_DATE', align:'center'}
				
		      ];
		      
//设置超链接  begin      

//设置超链接 end
	
</script>
<!--页面列表 end -->
</body>
</html>