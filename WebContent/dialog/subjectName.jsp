<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java"  pageEncoding="UTF-8"%>
<%@taglib uri="/jstl/cout" prefix="c" %>
<%
	String contextPath = request.getContextPath();
	Long userId = (Long)request.getAttribute("logonUser");
	
%>

<%@page import="com.infodms.dms.common.Constant"%><html xmlns="http://www.w3.org/1999/xhtml">
<head>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>首页新闻查询</title>
<script type="text/javascript">
	function doInit(){
   		loadcalendar();  //初始化时间控件
	}
</script>
</head>
<body>
<form name='fm' id='fm' method="post">
  <div class="navigation">
  <img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 售后服务管理&gt;服务活动&gt;主题选择</div>
  <table>
   <tr>            
        <td style="color: #252525;width: 115px;text-align: right">主题编号：<input type="hidden" value="<%= request.getParameter("type") %>"  name="type"/> </td>            
        <td>
			<input  class="middle_txt" id="subjectCode"  name="subjectCOde" type="text" datatype="1,is_null,27"/>
        </td>
        <td class="table_query_3Col_label_6Letter">主题名称：</td>
        <td><input type="text" name="subjectName" id="subjectName" datatype="1,is_null,30" class="middle_txt" value=""/></td>
        <td class="table_query_3Col_label_6Letter">&nbsp;</td>
        <td>&nbsp;</td> <td colspan="4" align="right"><input  class=normal_btn onclick=__extQuery__(1); align="right" value=查询 type=button name=button/> </td>
  </table>
  <!--分页 begin -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
  <!--分页 end -->
<script type="text/javascript" >
var myPage;
	var url = "<%=request.getContextPath()%>/claim/serviceActivity/ServiceActivityManage/open_subject.json";
	var title = null;
	
	var columns = [
				{id:'action',header: "选择", width:'3%',align:'center',sortable: false,dataIndex: 'SUBJECT_ID',renderer:myCheckBox},
				{header: "主题编号",sortable: false,dataIndex: 'SUBJECT_NO',align:'center'},				
				{header: "主题名称",sortable: false,dataIndex: 'SUBJECT_NAME',align:'center'}
		      ];
	__extQuery__(1);

	function myCheckBox(value,metaDate,record){
		var input2;
		var name = "'";
		input2='<input type="radio" id="checkId" name="checkId" onclick="getpartcode('+name+record.data.SUBJECT_ID+name+','+name+record.data.SUBJECT_NO+name+','+name+record.data.ACTIVITY_TYPE+name+');"  />';
		return String.format(input2);
	}
	
	function getpartcode(MAKER_CODE,MAKER_NAME,ACTIVITY_TYPE)
	{
		if (parent.$('inIframe')) 
		{
			parentDocument.getElementById('subjectId').value = MAKER_CODE;
			parentDocument.getElementById('subjectName').value = MAKER_NAME;
			parentDocument.getElementById('activityType').value = ACTIVITY_TYPE;
			if(ACTIVITY_TYPE!="10561005"){
				parentContainer.hideReplce();
			}
			if(ACTIVITY_TYPE=="10561005"){
				parentContainer.showReplce();
			}
		}
	   _hide();
	   parentContainer.openTime(MAKER_CODE);
	}
</script>
</form>
</body>
</html>