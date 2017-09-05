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
<title>授权规则维护</title>
 <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
</head>
<body>
  <div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 售后服务管理&gt;结算授权管理&gt;授权规则维护</div>


  <form name='fm' id='fm'>
<input type="hidden" name="wrmodelgrouplist" id="wrmodelgrouplist" value="<%=request.getAttribute("wrmodelgrouplist")%>"/>

  <table class="table_query">
       <tr>            
        <td class="table_query_3Col_label_6Letter">结算授权条件：</td>            
        <td>
			<input type="text" name="WRGROUP_ID" id="WRGROUP_ID" datatype="1,is_name,6" class="middle_txt" />
        </td>
       </tr>   
       <tr>
         <td colspan="4" align="center">
        	    <input  id="queryBtn" class="normal_btn" type="button" name="button1" value="查询"  onclick="__extQuery__(1)"/>
				<input class="normal_btn" type="button" name="button1" value="新增"  onclick="add();"/>
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
	var url = "<%=request.getContextPath()%>/claim/authorization/RuleMain2/ruleQuery.json?COMMAND=1";
	var title = null;
	
	var columns = [
				{header: "序号",sortable: false,align:'center',renderer:getIndex},
				{header: "授权条件",sortable: false,dataIndex: 'PRIOR_LEVEL',align:'center'},				
       <% for(int i=0;i<levellist.size();i++){ 
    	 HashMap temp = (HashMap)levellist.get(i);
		  %>
		  	{header: "<%=temp.get("APPROVAL_LEVEL_NAME")%>",sortable: false,dataIndex: "<%=temp.get("APPROVAL_LEVEL_CODE")%>",align:'center',renderer:myCheckbox},
		  	
		  <%
   			 }	%>	
				{header: "操作",sortable: false,dataIndex: 'RULE_ELEMENT',renderer:myLink ,align:'center'}
		      ];
//设置超链接  begin      
	
	//修改的超链接设置
	function myLink(value,meta,record){
		var wid = record.data.WRGROUP_ID;
	    return String.format(
         "<a href=\"<%=contextPath%>/claim/authorization/RuleMain2/ruleUpdateInit.do?ID="
			+ value + "&WID="+wid+"\">[修改]</a><a href=\"#\" onclick='sel(\""+value+"\")'>[删除]</a>");
	}
	//checkbox 格式化方法：
    function myCheckbox(value,meta,record){
        if(value != ''){
		    return String.format(
	         "<input type='checkbox' name='box1' id='box1' value='"+value+"' checked='true' disabled='disabled'/>");
        }else{
        	return String.format(
	         "<input type='checkbox' name='box1' id='box1' value='"+value+"' disabled='disabled'/>");
        }
	}
	//删除方法：
	function sel(str){
		MyConfirm("是否确认删除？",del,[str]);
	}  
	//删除
	function del(str){
		makeNomalFormCall('<%=contextPath%>/claim/authorization/RuleMain2/ruleDel.json?ID='+str,delBack,'fm','');
	}
	//删除回调方法：
	function delBack(json) {
		if(json.success != null && json.success == "true") {
			MyAlert("删除成功！");
			__extQuery__(1);
		} else {
			MyAlert("删除失败！请联系管理员！");
		}
	}	
	
	
//设置超链接 end
  //新增
  function add(){
    fm.action ="<%=contextPath%>/claim/authorization/RuleMain2/ruleAddInit.do";
    fm.submit();    
  }
</script>  
</body>
</html>

