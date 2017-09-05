<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java"  pageEncoding="UTF-8"%>
<%@page import="com.infodms.dms.common.Constant"%>
<%
	String contextPath = request.getContextPath();
%>
<%@taglib uri="/jstl/cout" prefix="c" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<%
%>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>监控配件维护</title>
 <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
</head>
<body>
<div class="wbox">
  <div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 售后服务管理&gt;索赔基础数据&gt;三包有效期易损件维护</div>
  <form name='fm' id='fm'>
  <div class="form-panel">
		<h2><img src="<%=contextPath%>/jmstyle/img/search-ico.png" style="margin-bottom: -13px;"/>查询条件</h2>
			<div class="form-body">
  <TABLE class="table_query">
       <tr>            
        <td style=";text-align: right">配件代码：</td>            
        <td>
			<input  class="middle_txt" id="PART_CODE"  name="PART_CODE" type="text" />
        </td>
        <td class="table_query_3Col_label_6Letter" style="text-align:right">配件名称：</td>
        <td><input type="text" name="PART_NAME" id="PART_NAME"  class="middle_txt" value=""/></td>
        <td class="table_query_3Col_label_6Letter" style="text-align:right">三包类型：</td>
        <td> <script type="text/javascript">
		genSelBoxExp("PART_WAR_TYPE",<%=Constant.PART_WR_TYPE%>,"",true,"","","false",'');
 		</script></td>
        <td class="table_query_3Col_label_6Letter">&nbsp;</td>
        <td>&nbsp;</td>     
       </tr>  
       <tr>
       <td colspan="6" style="text-align:center">
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
	var url = "<%=request.getContextPath()%>/repairOrder/RoMaintainMain/partQuery.json";
	var title = null;
	
	var columns = [
				{header: "序号",align:'center', renderer:getIndex, width:'7%'},
				{header: "操作",sortable: false,dataIndex: 'partId',renderer:myLink ,align:'center'},
				{header: "配件代码",sortable: false,dataIndex: 'partCode',align:'center'},
				{header: "配件名称",sortable: false,dataIndex: 'partName',align:'center'},	
				{header: "三包类型",sortable: false,dataIndex: 'partWarType',align:'center',renderer:getItemValue}
		      ];
//设置超链接  begin      
	
	//修改的超链接设置
	function myLink(value,meta,record){
		return String.format("<a  href='#' onclick='updateIt(" + value + ")'>[修改]</a>");
	    <%-- return String.format(
         "<a href=\"<%=contextPath%>/repairOrder/RoMaintainMain/partModForword.do?ID="
			+ value + "\">[修改]</a>"); --%>
	}
	
	function updateIt(value) {
		var url = "<%=contextPath%>/repairOrder/RoMaintainMain/partModForword.do?ID=" + value;
		OpenHtmlWindow(url, 800, 380, '易损件修改');
		
		/* fm.action = url ;
		fm.submit() ; */
	}
	//删除方法：
	function sel(str){
		MyConfirm("是否确认删除？",del,[str]);
	}  
	//删除
	function del(str){
		makeNomalFormCall('<%=contextPath%>/repairOrder/RoMaintainMain/partDel.json?ID='+str,delBack,'fm','');
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
	
</script>  