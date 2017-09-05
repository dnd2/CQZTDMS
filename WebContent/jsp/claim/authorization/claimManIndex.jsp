<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java"  pageEncoding="UTF-8"%>
<%
	String contextPath = request.getContextPath();
%>
<%@taglib uri="/jstl/cout" prefix="c" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>监授权人员管理</title>
 <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
</head>
<body>
<div class="wbox">
  <div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 售后服务管理&gt;索赔授权管理&gt;授权人员管理</div>
<form name="fm" id="fm" method="post">
	<div class="form-panel">
		<h2><img src="<%=contextPath%>/jmstyle/img/search-ico.png" style="margin-bottom: -13px;"/>查询条件</h2>
			<div class="form-body">
<input type="hidden" name="levellist" id="levellist" value="<%=request.getAttribute("levellist")%>"/>
<input type="hidden" name="aduitFlag" id="aduitFlag" value="<%=request.getAttribute("aduitFlag") %>"/>
  <table class="table_query">
       <tr>            
        <td class="table_query_2Col_label_4Letter" style="text-align:right">授权级别：</td>            
        <td>
			<script type="text/javascript">
              var levellist = document.getElementById("levellist").value;
    	      var str="";
    	      str += "<select id='APPROVAL_LEVEL_CODE' name='APPROVAL_LEVEL_CODE'  class='u-select'>";
    	      str+= levellist;
    		  str += "</select>";
    		  document.write(str);
	       </script>
        </td>
        <td class="table_query_2Col_label_4Letter" style="text-align:right">姓名：</td>
        <td><input type="text" name="USER_NAME" id="USER_NAME" maxlength="15"  datatype="1,is_name,6" class="middle_txt" /></td>    
       </tr>
       <tr>
        <td colspan="4" style="text-align:center">
        	 <input  id="queryBtn" class="normal_btn" type="button" name="queryBtn" value="查询"  onclick="__extQuery__(1)"/>
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
var url = "<%=request.getContextPath()%>/claim/authorization/ClaimManMain/claimManQuery.json?COMMAND=1";
var title = null;

var columns = [
			{header: "序号",align:'center', renderer:getIndex, width:'7%'},
			{header: "操作",sortable: false,dataIndex: 'USER_ID',renderer:myLink ,align:'center'},
			{header: "员工姓名",sortable: false,dataIndex: 'NAME',align:'center'},
			{header: "职位",sortable: false,dataIndex: 'POSENAME',align:'center'},
			{header: "电话",sortable: false,dataIndex: 'PHONE',align:'center'},
			{header: "E-mail",sortable: false,dataIndex: 'EMAIL',align:'center'},
			{header: "授权级别",sortable: false,dataIndex: 'APPROVAL_LEVEL_NAME',align:'center'},
			{header: "授权代码",sortable: false,dataIndex: 'APPROVAL_LEVEL_CODE',align:'center'}
	      ];
//设置超链接  begin 
     
//修改的超链接设置
	function myLink(value,meta,record){
		return String.format("<a href=\"#\" onclick='sel(\""+value+"\")'>[修改]</a>");
	}	
	//设置超链接
	function sel(value)
	{
		var auditFlag = document.getElementById("aduitFlag").value;
		var url =  "<%=contextPath%>/claim/authorization/ClaimManMain/claimManUpdateInit.do?ID="+ value +"&auditFlag="+auditFlag;
		OpenHtmlWindow(url, 650, 350, '授权人员维护');
		<%-- var auditFlag = document.getElementById("aduitFlag").value;
  		fm.action = "<%=contextPath%>/claim/authorization/ClaimManMain/claimManUpdateInit.do?ID="+ value +"&auditFlag="+auditFlag;
  		fm.submit(); --%>
	}	
//设置超链接 end
</script>  

