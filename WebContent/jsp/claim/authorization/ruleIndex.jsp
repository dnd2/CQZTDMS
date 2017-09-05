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
<div class="wbox">
  <div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 售后服务管理&gt;索赔授权管理&gt;授权规则维护</div>
  <form name='fm' id='fm'>
  	<div class="form-panel">
		<h2><img src="<%=contextPath%>/jmstyle/img/search-ico.png" style="margin-bottom: -13px;"/>查询条件</h2>
			<div class="form-body">
  <table class="table_query">
       <tr>            
        <td style="text-align:right">索赔授权条件：</td>            
        <td>
			<input type="text" name="WRGROUP_ID" maxlength="25"  id="WRGROUP_ID" datatype="1,is_name,6" class="middle_txt" />
        </td>
       </tr>          
       <tr>
         <td colspan="2" style="text-align:center">
        	    <input  id="queryBtn" class="normal_btn" type="button" name="queryBtn" value="查询"  onclick="__extQuery__(1) ;"/>
				<input class="normal_btn" type="button" name="button1" value="新增"  onclick="add();"/>
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
<script type="text/javascript" >
var myPage;
	var url = "<%=request.getContextPath()%>/claim/authorization/RuleMain/ruleQuery.json?COMMAND=1";
	var title = null;
	
	var columns = [
				{header: "序号",sortable: false,align:'center',renderer:getIndex},
				{header: "操作",sortable: false,dataIndex: 'RULE_ELEMENT',renderer:myLink ,align:'center'},
				//{header: "车型组",sortable: false,dataIndex: 'WRGROUP_CODE',align:'center'},
				{header: "授权条件",sortable: false,dataIndex: 'PRIOR_LEVEL',align:'center'},				
       			<% for(int i=0;i<levellist.size();i++){ 
    	 			HashMap temp = (HashMap)levellist.get(i);
    	 			
    	 			if(i == levellist.size() - 1) {
		  		%>
		  				{header: "<%=temp.get("APPROVAL_LEVEL_NAME")%>",sortable: false,dataIndex: "<%=temp.get("APPROVAL_LEVEL_CODE")%>",align:'center',renderer:myCheckbox}
		  			<%
    	 			} else {
		  			%>
		  			{header: "<%=temp.get("APPROVAL_LEVEL_NAME")%>",sortable: false,dataIndex: "<%=temp.get("APPROVAL_LEVEL_CODE")%>",align:'center',renderer:myCheckbox},
				<%
    	 			}
				}	
				%>	
		      ];
//设置超链接  begin      
	
	//修改的超链接设置
	function myLink(value,meta,record){
		var wid = record.data.WRGROUP_ID;
	    return String.format(
         "<a  href='#' onclick='updateIt(" + value + "," + wid + ")'>[修改]</a><a  href=\"#\" onclick='sel(\""+value+"\")'>[删除]</a>");
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
		makeNomalFormCall('<%=contextPath%>/claim/authorization/RuleMain/ruleDel.json?ID='+str,delBack,'fm','');
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
	var url = "<%=contextPath%>/claim/authorization/RuleMain/ruleAddInit.do";
	OpenHtmlWindow(url, 850, 500, '授权规则新增');
    <%-- fm.action ="<%=contextPath%>/claim/authorization/RuleMain/ruleAddInit.do";
    fm.submit();     --%>
  }
  
  function updateIt(value, wid) {
		var url = "<%=contextPath%>/claim/authorization/RuleMain/ruleUpdateInit.do?ID=" + value + "&WID="+wid ;
		OpenHtmlWindow(url, 850, 500, '授权规则修改');
		
		/* fm.action = url ;
		fm.submit() ; */
	}
</script>  
</body>
</html>

