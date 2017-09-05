<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="java.util.List"%>
<%@page import="java.util.HashMap"%>
<%
	String contextPath = request.getContextPath();
%>
<%@taglib uri="/jstl/cout" prefix="c" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>监控配件维护</title>
</head>
<body>
  <div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置：售后服务管理&gt;索赔预授权&gt;预授权监控配件维护</div>

  <form name='fm' id='fm'>
	  <input type="hidden" name="wrmodelgrouplist" id="wrmodelgrouplist" value="<%=request.getAttribute("wrmodelgrouplist")%>"/>
	  <table class="table_query">
       <tr>            
        <td class="table_query_2Col_label_5Letter">配件代码：</td>            
        <td>
			<input  class="middle_txt" id="PART_CODE"  name="PART_CODE" type="text" datatype="1,is_null,27"/>
        </td>
        <td class="table_query_2Col_label_5Letter">配件名称：</td>
        <td><input type="text" name="PART_NAME" id="PART_NAME" datatype="1,is_digit_letter_cn,30" class="middle_txt" value=""/></td>
       </tr>
       <tr>            
        <td style="color: #252525;width: 115px;text-align: right">索赔配件车型组：</td>            
        <td>
		   <script type="text/javascript">
              var wrmodelgrouplist=document.getElementById("wrmodelgrouplist").value;
    	      var str="";
    	      str += "<select id='WRGROUP_ID' name='WRGROUP_ID'  datatype='0,is_null,18' class='short_sel'>";
    	      str+=wrmodelgrouplist;
    		  str += "</select>";
    		  document.write(str);
	       </script>
        </td>
       </tr> 
       <tr>            
        <td colspan="4" align="center">
        	    <input  id="queryBtn" class="normal_btn" type="button" name="button1" value="查询"  onclick="__extQuery__(1)" />
				<input class="normal_btn" type="button" name="button1" value="新增"  onclick="add();" />
				<input class="normal_btn" type="button" name="button1" value="下发"  onclick="toDispatch();"/>
        </td>
       </tr>       
 	</table>
<div id="loader" style='position:absolute;z-index:200;background:#FFCC00;padding:1px;top:4px;display:none;display: none;'></div>
</form>
  <!--分页 begin -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
  <!--分页 end -->
  
<script type="text/javascript">
	var myPage;
	var url = "<%=request.getContextPath()%>/claim/preAuthorization/PartMaintain/claimPartWatchQuery.json?COMMAND=1";
	var title = null;
	
	var columns = [
				{header: "车型组代码",sortable: false,dataIndex: 'WRGROUP_CODE',align:'center'},
				{header: "车型组名称",sortable: false,dataIndex: 'WRGROUP_NAME',align:'center'},
				{header: "配件代码",sortable: false,dataIndex: 'PART_CODE',align:'center'},
				{header: "配件名称",sortable: false,dataIndex: 'PART_NAME',align:'center'},				
				{header: "操作",sortable: false,dataIndex: 'ID',renderer:myLink ,align:'center'}
		      ];
	
	//修改的超链接设置
	function myLink(value,meta,record){
	    return String.format("<a href=\"#\" onclick='sel(\""+value+"\")'>[删除]</a>");
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
		makeNomalFormCall('<%=contextPath%>/claim/preAuthorization/PartMaintain/claimPartWatchDel.json?ID='+str,delBack,'fm','');
	}
	//删除回调方法：
	function delBack(json) {
		if(json.success != null && json.success == true) {
			MyAlert("删除成功！");
			__extQuery__(1);
		} else {
			MyAlert("删除失败！请联系管理员！");
		}
	}	
	
	
//设置超链接 end
  //新增
  function add(){
    fm.action ="<%=contextPath%>/claim/preAuthorization/PartMaintain/claimPartWatchAddForward.do";
    fm.submit();    
  }
  function toDispatch(){
		//查询可下发的配件
		OpenHtmlWindow('<%=contextPath%>/claim/preAuthorization/PartMaintain/toDispatchQueryInit.do',700,550);
  }
</script> 
  </body> 
</html>