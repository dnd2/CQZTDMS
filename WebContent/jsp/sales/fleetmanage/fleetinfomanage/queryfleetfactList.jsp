<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>批售项目</title>
<% String contextPath = request.getContextPath(); %>
<script type="text/javascript">

	function doInit(){
   		loadcalendar();  //初始化时间控件
	}

</script>
</head>
<body>

<div class="navigation"><img src="<%=request.getContextPath()%>/img/nav.gif" />&nbsp;
当前位置： 集团客户管理&gt;集团客户信息管理&gt;批售项目&nbsp;&nbsp;</div>
  
  <form method="post" name="fm" id="fm">
   <!-- 查询条件 begin -->
    <table class="table_query" >
          <tr>
            <td class="table_query_2Col_label_4Letter">项目编号：</td>
            <td class="table_query_2Col_input">
            	<input name="factNo" id="factNo" type="text" size="20"  class="middle_txt" value="" >
            </td>
			<td align="right">项目名称：</td>
			<td align="left">
				<input name="factName" id="factName" type="text" size="20"  class="middle_txt" value="" >
			</td>
          </tr>                  
          <tr>
            <td align="right" nowrap>&nbsp;</td>
            <td colspan="6">&nbsp;</td>
            <td align="right" nowrap>
            	<input class="normal_btn" type="button" name="queryBtn" id="queryBtn" value="查询"  onClick="__extQuery__(1);">
				<input class="normal_btn" type="button" name="button1" value="新增"  onClick="addFact()">
			</td>
            <td colspan="2" align="left" >&nbsp;</td>
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
	var url = "<%=contextPath%>/sales/fleetmanage/fleetInfoManage/FleetFact/queryFleetFactList.json";
				
	var title = null;

	var columns = [
				{header: "序号", dataIndex: 'NUM', align:'center'},
				{header: "项目编号", dataIndex: 'PACT_NO', align:'center'},
				{header: "项目名称", dataIndex: 'PACT_NAME', align:'center'},
				{header: "状态", dataIndex: 'STATUS', align:'center',renderer:getItemValue},
				{header: "说明", dataIndex: 'REMART', align:'center'},
				{header: "操作",sortable: false,dataIndex: 'PACT_ID',renderer:myLink ,align:'center'}
		      ];
		      
//设置超链接  begin      
	
	//跳转新增页面
	function addFact()
	{
		$('fm').action= "<%=contextPath%>/sales/fleetmanage/fleetInfoManage/FleetFact/addFleetFact.do";
		$('fm').submit();
	}
	
	//修改的超链接
	function myLink(value,meta,record)
	{
  		return String.format("<a href='#' onclick='updateSpecialExpenses(\""+record.data.PACT_ID+"\")'>[查看]</a>");
	}
	
	//修改的超链接设置
	function updateSpecialExpenses(val1)
	{
		$('fm').action= '<%=contextPath%>/sales/fleetmanage/fleetInfoManage/FleetFact/editFleetFact.do?id=' + val1;
	 	$('fm').submit();
	}
//设置超链接 end
	
</script>
<!--页面列表 end -->


</body>
</html>