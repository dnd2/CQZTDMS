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
<title>经销商手动录入VIN维护</title>
 <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
</head>
<body>
   <div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 售后服务管理&gt;预授权管理&gt;经销商手动输入VIN维护</div>
  <form name='fm' id='fm'>
  <TABLE class="table_query">
       <tr>            
        <td style="color: #252525;width: 115px;text-align: right">经销商代码：</td>            
        <td>
			<input  class="middle_txt" id="DEALER_CODE"  name="DEALER_CODE" type="text" datatype="1,is_null,10"/>
        </td>
        <td class="table_query_3Col_label_6Letter">经销商名称：</td>
        <td><input type="text" name="DEALER_NAME" id="DEALER_NAME" datatype="1,is_null,30" class="middle_txt" value=""/></td>
        </tr>
        <tr>
        <td  class="table_query_3Col_label_6Letter">是否允许手输：</td>
        <td > <script type="text/javascript">
		genSelBoxExp("IS_SCAN",<%=Constant.IF_TYPE%>,"",true,"short_sel","","false",'');
 		</script></td><td colspan="2"></td>
       </tr>  
       <tr>
       <td colspan="6" align="center">
        	    <input  id="queryBtn" class="normal_btn" type="button" name="button1" value="查询"  onclick="__extQuery__(1)"/>
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
	var url = "<%=request.getContextPath()%>/claim/basicData/ClaimVenderPrice/dealerQuery.json";
	var title = null;
	
	var columns = [
				{header: "经销商代码",sortable: false,dataIndex: 'DEALER_CODE',align:'center'},
				{header: "经销商名称",sortable: false,dataIndex: 'DEALER_NAME',align:'center'},	
				{header: "是否允许手输",sortable: false,dataIndex: 'ISSCAN',align:'center'},	
				{header: "操作",sortable: false,dataIndex: 'DEALER_ID',renderer:myLink ,align:'center'}
		      ];
//设置超链接  begin      
	
	//修改的超链接设置
	function myLink(value,meta,record){
	    return String.format(
        "<a href='#' onClick='updateDealer("+value+","+record.data.IS_SCAN+");'>[修改]</a>");
	}
	//修改方法：
	function updateDealer(str,type){
		MyConfirm("是否确认修改？",update,[str,type]);
	}  
	//修改
	function update(str,type){
		makeNomalFormCall("<%=request.getContextPath()%>/claim/basicData/ClaimVenderPrice/dealerModefy.json?ID="+str+"&type="+type,refreshPage11,'fm','');
	}
	//修改回调方法：
	function refreshPage11(json) {
		if(json.success != null && json.success == "true") {
			MyAlert("修改成功！");
			__extQuery__(1);
		} else {
			MyAlert("修改失败！请联系管理员！");
		}
	}	
	
</script>  