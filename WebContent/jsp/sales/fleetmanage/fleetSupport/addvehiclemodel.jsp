<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>合格证的补办、更换申请表</title>
<% String contextPath = request.getContextPath(); %>
<script type="text/javascript">

	function doInit()
	{
   		__extQuery__(1);
	}
</script>
</head>
<body>

<div class="navigation"><img src="<%=request.getContextPath()%>/img/nav.gif" />&nbsp;当前位置： 车系选择</div>
  <form method="post" name="fm" id="fm">

  </form>
  <!--分页 begin -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
  <!--分页 end -->
<!--页面列表 begin -->
<script type="text/javascript" >

	var myPage;
//查询路径
	var url = "<%=contextPath%>/sales/fleetmanage/fleetSupport/FleetContractsMaintain/addVModel.json";
				
	var title = null;

	var columns = [
				{header: "选择", dataIndex: 'GROUP_ID', align:'center',renderer:mySelect},
				{header: "车系代码",sortable: false,dataIndex: 'GROUP_CODE',align:'center'},
				{header: "车系名称",sortable: false,dataIndex: 'GROUP_NAME',align:'center'}
		      ];
	
	function mySelect(value,metaDate,record)
	{
		return String.format("<input type='radio' name='rd' onclick='setVIN(\""+record.data.GROUP_ID+"\",\""+record.data.GROUP_NAME+"\")' />");
	}

	function setVIN(v1,v2){
		//调用父页面方法
 		parent.$('inIframe').contentWindow.getModel(v1,v2);
 		//关闭弹出页面
 		_hide();
	}
	
</script>
<!--页面列表 end -->
</body>
</html>
