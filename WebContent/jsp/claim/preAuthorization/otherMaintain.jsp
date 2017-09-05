<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="com.infodms.dms.common.Constant"%>
<%@ page import="com.infodms.dms.po.TtAsWrApplicationPO"%>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<%@page import="java.util.List"%>
<%
	String contextPath = request.getContextPath();
%>
<%@taglib uri="/jstl/cout" prefix="c" %>
<head>
<TITLE>预授权监控其他项目维护</TITLE>
<script type="text/javascript">
var myPage;
	//查询路径
	var url = "<%=contextPath%>/claim/preAuthorization/OtherMaintain/otherQuery.json";
				
	var title = null;

	var columns = [
					{header: "项目代码", width:'20%', dataIndex: 'itemCode'},
					{header: "项目名称", width:'10%', dataIndex: 'feeName'},
					{header: "项目描述", width:'15%', dataIndex: 'itemDesc'},
					{header: "操作", width:'5%', dataIndex: 'id',renderer:myLink}
		      ];
		      
	//设置超链接  begin      
	function doInit(){
   		loadcalendar();
	}
	//修改的超链接设置
	function myLink(value,meta,record){
	    return String.format(
         "<a href=\"#\" onclick='sel(\""+value+"\")'>[删除]</a><input type=\"hidden\" id=\"ids\" name=\"ids\" value=\""+value+"\">");
	}
	//删除方法：
	function sel(str){
		MyConfirm("是否确认删除？",del,[str]);
	}  
	//删除
	function del(str){
		makeNomalFormCall('<%=contextPath%>/claim/preAuthorization/OtherMaintain/otherMaintainDel.json?ID='+str,delBack,'fm','');
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
</script>
</HEAD>
<BODY onload="doInit()">
<div class="navigation">
<img src="../../../img/nav.gif" />&nbsp;当前位置：售后服务管理&gt;索赔预授权&gt;预授权监控其他项目维护</div>
  
  <form method="post" name = "fm" id="fm">
<input type="hidden" name="ids" id="ids" value=""/>
<input type="hidden" name="OTHERFEE" id="OTHERFEE" value="<%=request.getAttribute("OTHERFEE")%>" />
  <TABLE align=center  class="table_query" >
          <tr>
            <td  class="table_query_2Col_label_5Letter">项目名称：</td>
            <td >
            <select  id="ITEM_CODE" name="ITEM_CODE">
			<script type="text/javascript">
			document.write(document.getElementById('OTHERFEE').value);
	        </script>
	        </select>
 		    </td> 
            <td  class="table_query_2Col_label_5Letter">项目描述：</td>
            <td colspan="3">
            	<input name="ITEM_DESC" type="text" id="ITEM_DESC"  class="middle_txt"  datatype="1,is_digit_letter_cn,100"   />
      		</td> 
			</tr>
         	<tr>
         	<td colspan="6" align="center">
         	<input class="normal_btn"  type="button" value="查询" name="commit" onClick="__extQuery__(1);"/>
         	<input class="normal_btn"  type="button" value="新增" name="add" onClick="location='otherMaintainAddForward.do';"/>
         	 </td>
        	</tr>
 	</table>
  	<!-- 查询条件 end -->
  <!--分页 begin -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
  <!--分页 end --> 
</form>
<form name="form1" style="display:none">
<table id='bt' class="table_list">
<!-- <tr><th align="center"><p>
    <input class="normal_btn" type=button value='结算' onclick='submitId()' name=modify />
  </p></th>
  </tr> -->
</table>
</form>
</BODY>
<SCRIPT LANGUAGE="JavaScript">
	document.form1.style.display = "none";
		
	var HIDDEN_ARRAY_IDS=['form1'];
</script>

</html>