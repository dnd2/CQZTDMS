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
  <img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 售后服务管理&gt;索赔基础数据维护&gt;车系</div>
  <table>
  <tr><td><input type="hidden"  id="largess_type" name ="largess_type" value="${largess_type}"/><input type="hidden"  id="activityId" name ="activityId" value="${activityId}"/> </td>
  </tr>
   <tr>            
        <td style="color: #252525;width: 115px;text-align: right">车型代码：</td>            
        <td>
			<input  class="middle_txt" id="GROUP_CODE"  name="GROUP_CODE" type="text" datatype="1,is_null,27"/>
        </td>
        <td class="table_query_3Col_label_6Letter">车型名称：</td>
        <td><input type="text" name="GROUP_NAME" id="GROUP_NAME" datatype="1,is_null,30" class="middle_txt" value=""/></td>
        <td class="table_query_3Col_label_6Letter">&nbsp;</td>
        <td>&nbsp;</td>     
    </tr>   
    <br/> 
    <tr><td colspan="4" align="right"><input  class=normal_btn onclick=__extQuery__(1); align="right" value=查询 type=button name=button/> </td>
  
  </table>
  <!--分页 begin -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
  <!--分页 end -->
<script type="text/javascript" >
var myPage;
	var url = "<%=request.getContextPath()%>/claim/basicData/LaborPriceMain/open_group.json?COMM=1";
	var title = null;
	
	var columns = [
				{id:'action',header: "选择", width:'3%',align:'center',sortable: false,dataIndex: 'GROUP_ID',renderer:myCheckBox},
				{header: "车型代码",sortable: false,dataIndex: 'GROUP_CODE',align:'center'},				
				{header: "车型名称",sortable: false,dataIndex: 'GROUP_NAME',align:'center'}
		      ];
	__extQuery__(1);

	function myCheckBox(value,metaDate,record){
		var name = "'";
	    var obj =  ' <input type="radio" name="part" onclick="getpartcode('+name+record.data.GROUP_CODE+name+','+name+record.data.GROUP_NAME+name+','+name+record.data.GROUP_ID+name+');" />';
		return String.format(obj);
	}
	
	function getpartcode(GROUP_CODE,GROUP_NAME,GROUP_ID)
	{
		if (parent.$('inIframe')) 
		{
			parentDocument.getElementById('GROUP_CODE').value = GROUP_CODE;
			parentDocument.getElementById('GROUP_ID').value = GROUP_ID;
			parentDocument.getElementById('GROUP_NAME').value = GROUP_NAME;
		}
	   _hide();
	}
	
	
	
</script>
</form>
</body>
</html>