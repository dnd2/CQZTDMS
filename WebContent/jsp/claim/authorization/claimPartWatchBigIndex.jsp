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
<title>监控配件大类维护</title>
 <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
</head>
<body>
  <div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 售后服务管理&gt;索赔授权管理&gt;监控配件大类维护</div>


  <form name='fm' id='fm'>
<input type="hidden" name="wrmodelgrouplist" id="wrmodelgrouplist" value="<%=request.getAttribute("wrmodelgrouplist")%>"/>

  <TABLE class="table_query">

       <tr>            
        <td style="color: #252525;width: 115px;text-align: right">配件大类代码：</td>            
        <td>
			<input  class="middle_txt" id="PARTTYPE_CODE"  name="PARTTYPE_CODE" type="text" datatype="1,is_null,27"/>
        </td>
        <td class="table_query_3Col_label_6Letter">配件大类名称：</td>
        <td><input type="text" name="PARTTYPE_NAME" id="PARTTYPE_NAME" datatype="1,is_null,30" class="middle_txt" value=""/></td>
        <td class="table_query_3Col_label_6Letter">&nbsp;</td>
        <td>&nbsp;</td>     
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
  </body>
</html>
<script type="text/javascript" >
var myPage;
	var url = "<%=request.getContextPath()%>/claim/authorization/ClaimPartWatchMain/claimPartWatchBigQuery.json?COMMAND=1";
	var title = null;
	
	var columns = [
				{header: "配件大类代码",sortable: false,dataIndex: 'PART_BIGTYPE_CODE',align:'center'},
				{header: "配件大类名称",sortable: false,dataIndex: 'PART_BIGTYPE_NAME',align:'center'},				
       <% for(int i=0;i<levellist.size();i++){ 
    	 HashMap temp = (HashMap)levellist.get(i);
		  %>
		  	{header: "<%=temp.get("APPROVAL_LEVEL_NAME")%>",sortable: false,dataIndex: "<%=temp.get("APPROVAL_LEVEL_CODE")%>",align:'center',renderer:myCheckbox},
		  <%
   			 }	%>	
				{header: "操作",sortable: false,dataIndex: 'ID',renderer:myLink ,align:'center'}
		      ];
//设置超链接  begin      
	
	//修改的超链接设置
	function myLink(value,meta,record){
	    return String.format(
         "<a href=\"<%=contextPath%>/claim/authorization/ClaimPartWatchMain/claimPartBigWatchUpdateInit.do?ID="
			+ value + "\">[修改]</a><a href=\"#\" onclick='sel(\""+value+"\")'>[删除]</a>");
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
		makeNomalFormCall('<%=contextPath%>/claim/authorization/ClaimPartWatchMain/claimPartBigWatchDel.json?ID='+str,delBack,'fm','');
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
    fm.action ="<%=contextPath%>/claim/authorization/ClaimPartWatchMain/claimPartWatchBigAddInit.do";
    fm.submit();    
  }
</script>  