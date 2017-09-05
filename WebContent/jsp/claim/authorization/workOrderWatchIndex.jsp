<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java"  pageEncoding="UTF-8"%>
<%@page import="java.util.List"%>
<%@page import="java.util.HashMap"%>
<%
	String contextPath = request.getContextPath();
%>
<%@taglib uri="/jstl/cout" prefix="c" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<%
	List levellist = (List)request.getAttribute("LEVELLIST");
%>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>工单预授权设置</title>
 <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
</head>
<body>
<div class="wbox">
  <div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 售后服务管理&gt;索赔授权管理&gt;工单预授权设置</div>
  <form name='fm' id='fm'>
  <div class="form-panel">
		<h2><img src="<%=contextPath%>/jmstyle/img/search-ico.png" style="margin-bottom: -13px;"/>查询条件</h2>
			<div class="form-body">
  <TABLE class="table_query">
       <tr>            
        <td style="text-align: right;">维修类型：</td>
        <td><input type="text" name="CODE_DESC" id="CODE_DESC" maxlength="25" class="middle_txt" value=""/></td>
       </tr>  
       <tr>
       <td colspan="4" style="text-align: center;">
        	    <input  id="queryBtn" class="normal_btn" type="button" name="queryBtn" value="查询"  onclick="__extQuery__(1)"/>
        		<input id="reset" class="normal_btn" type="reset" value="重置"/>
        </td>
       </tr>    
 	</table>
 	</div>
 	</div>
<div id="loader" style='position:absolute;z-index:200;background:#FFCC00;padding:1px;top:4px;display:none;display: none;'></div>
</form>
  <!--分页 begin -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
  <!--分页 end -->
  </div>
  </body>
</html>
<script type="text/javascript" >
		      var myPage;
var url = "<%=request.getContextPath()%>/claim/authorization/ClaimPartWatchMain/workOrderWatchQuery.json";
var title = null;

var columns = [
	{header: "序号",renderer:getIndex,align:'center'},
	{header: "操作",sortable: false,dataIndex:'NUM',align:'center',renderer:myLink },
			{header: "维修类型",sortable: false,dataIndex: 'CODE_DESC',align:'center'},
			 <% for(int i=0;i<levellist.size();i++){ 
    	 	HashMap temp = (HashMap)levellist.get(i);
    	 	
    	 	if(i == levellist.size() - 1) {
		  	%>
		  	{header: "<%=temp.get("APPROVAL_LEVEL_NAME").toString()%>",sortable: false,dataIndex: "<%="S"+temp.get("APPROVAL_LEVEL_CODE").toString()%>",
		  	align:'center',renderer:myCheckbox}
		  	<%} else {%>
		  	{header: "<%=temp.get("APPROVAL_LEVEL_NAME").toString()%>",sortable: false,dataIndex: "<%="S"+temp.get("APPROVAL_LEVEL_CODE").toString()%>",
			  	align:'center',renderer:myCheckbox},
		  	<%
		  	}
   			 }	%>	
	      ];
	function myLink(value,meta,record){
		return String.format("<a href='#' onclick='updateIt(" + value + ")'>[修改]</a>");
	}
	
	function updateIt(value) {
		var url = "<%=contextPath%>/claim/authorization/ClaimPartWatchMain/workOrderUpdateInit.do?NUM=" + value;
		OpenHtmlWindow(url, 650, 380, '工单预授权修改');
		
		/* fm.action = url ;
		fm.submit() ; */
	}
	//checkbox 格式化方法：
    function myCheckbox(value,meta,record){
        if(value != ''){
		    return String.format(
	         "<input type='checkbox' name='box1' id='box1' value='"+value+"' checked='checked' disabled='disabled'/>");
        }else{
        	return String.format(
	         "<input type='checkbox' name='box1' id='box1' value='"+value+"' disabled='disabled'/>");
        }
	}      
		      
</script>  