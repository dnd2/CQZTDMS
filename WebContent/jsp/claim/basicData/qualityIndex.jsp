<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java"  pageEncoding="UTF-8"%>
<%
	String contextPath = request.getContextPath();
%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>索赔配件质保期维护</title>
</head>
<body>
<form name='fm' id='fm'>
  <div class="navigation">
  <img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 售后服务管理&gt;索赔基础数据&gt;索赔配件质保期维护</div>
   <table  class="table_query">
          <tr>
          	<td class="table_query_2Col_label_4Letter">车型名称：</td>
          	<td><input name="GROUP_NAME" type="text" id="GROUP_NAME"  datatype="1,is_null,100"  class="middle_txt" /></td>
          	<td class="table_query_2Col_label_4Letter">车型代码：</td>
          	<td><input name="GROUP_CODE" type="text" id="GROUP_CODE"  datatype="1,is_null,20"  class="middle_txt"/></td>
          	<td class="table_query_2Col_label_7Letter">质保期是否设置：</td>
          	<td align="left"><input type="checkbox" name="FLAG" value="true" /></td>          	
          </tr> 
		   <tr>    
		   <td colspan="6" align="center">
            <input class="normal_btn" type="button" name="button1" value="查询"  onClick="__extQuery__(1)"/>
           </td></tr>
       
  </table>
  <!--分页 begin -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
  <!--分页 end -->
</form>  
<script type="text/javascript" >
var myPage;
	var url = "<%=request.getContextPath()%>/claim/basicData/QualityMain/qualityQuery.json?COMMAND=1";
	var title = null;
	
	var columns = [
				{header: "车型名称",sortable: false,dataIndex: 'GROUP_NAME',align:'center'},				
				{header: "车型代码",sortable: false,dataIndex: 'GROUP_CODE',align:'center'},
				{header: "操作",sortable: false,dataIndex: 'GROUP_ID',renderer:myLink ,align:'center'}
		      ];

//设置超链接  begin      
	
	//修改的超链接设置
	function myLink(value,meta,record){
	    return String.format(
         "<a href=\"<%=contextPath%>/claim/basicData/QualityMain/qualityUpdateInit.do?GROUP_ID="
			+ value + "\">[修改]</a><a href=\"#\" onclick='selbyid(\""+value+"\")'>[明细]</a>");
	}
	
//设置超链接 end

//设置超链接
function mySelect(value,meta,record){
 		return String.format(
        "<a href=\"#\" onclick='selbyid(\""+record.data.ID+"\")'>["+ value +"]</a>");
}

//详细页面
function selbyid(value){
	OpenHtmlWindow('<%=contextPath%>/claim/basicData/QualityMain/qualityDetail.do?GROUP_ID='+value,900,500);
}
</script>
</body>
</html>