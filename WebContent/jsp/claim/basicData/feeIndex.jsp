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
	List feetype = (List)request.getAttribute("FEETYPE");
%>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>保养费用维护</title>
 <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
</head>
<body>
  <div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 售后服务管理&gt;索赔基础数据&gt;保养费用维护</div>


  <form name='fm' id='fm'>


  <table class="table_query">

       <tr>            
        <td class="table_query_2Col_label_6Letter">车型名称：</td>            
        <td>
			<input type="text" name="GROUP_NAME" id="GROUP_NAME" datatype="1,is_null,30" class="middle_txt" value=""/>
        </td>
        <td class="table_query_2Col_label_6Letter">车型代码：</td>
        <td><input type="text" name="GROUP_CODE" id="GROUP_CODE" datatype="1,is_null,30" class="middle_txt" value=""/></td>     
       </tr>
       <tr>            
        <td class="table_query_2Col_label_6Letter">是否设置费用：</td>            
        <td>
			<input type="checkbox" name="FLAG" value="true" />
        </td>    
       </tr>       
	   <tr>
             <td colspan="4" align="center">
             	<input id="queryBtn" class="normal_btn" type="button" name="button1" value="查询"  onclick="__extQuery__(1)"/>
				<input class="normal_btn" type="button" name="button1" value="下发"  onclick=""/>
			</td>
       </tr>
 	</table>
<div id="loader" style='position:absolute;z-index:200;background:#FFCC00;padding:1px;top:4px;display:none;display: none;'></div>
</form>
  <!--分页 begin -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
  <!--分页 end -->
<script type="text/javascript" >
var myPage;
	var url = "<%=request.getContextPath()%>/claim/basicData/FeeMain/feeQuery.json?COMMAND=1";
	var title = null;
	
	var columns = [
				{header: "车型代码",sortable: false,dataIndex: 'GROUP_CODE',align:'center'},
				{header: "车型名称",sortable: false,dataIndex: 'GROUP_NAME',align:'center'},				
       <% for(int i=0;i<feetype.size();i++){ 
    	 HashMap temp = (HashMap)feetype.get(i);
		  %>
		  	{header: "<%=temp.get("CODE_DESC")%>",sortable: false,dataIndex: "<%=temp.get("CODE_ID")%>",align:'center',renderer:amountFormat},
		  <%
   			 }	%>	
				{header: "操作",sortable: false,dataIndex: 'GROUP_CODE',renderer:myLink ,align:'center'}
		      ];

//设置超链接  begin <input type="text" name="GROUP_CODE" id="GROUP_CODE" datatype="1,is_digit_letter_cn,30" class="middle_txt" value=""/>
	function myInput(value,metaDate,record){
		return String.format("<input type='text' name='FEE' id='FEE' class='short_txt' datatype='0,is_yuan,10'  value='" + value + "' />");
	}      
	
	//修改的超链接设置
	function myLink(value,meta,record){
	    return String.format(
         "<a href=\"<%=contextPath%>/claim/basicData/FeeMain/feeUpdateInit.do?GROUP_ID="
			+ record.data.GROUP_ID + "\">[修改]</a>");
	}
	
//设置超链接 end
  //下发
  function sendAll(){
    fm.action ="<%=contextPath%>/claim/basicData/FeeMain/feeSendAll.do";
    fm.submit();    
  }
</script>
  </body>
</html>