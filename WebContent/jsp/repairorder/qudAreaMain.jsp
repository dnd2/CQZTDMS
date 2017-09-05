<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java"  pageEncoding="UTF-8"%>
<%
	String contextPath = request.getContextPath();
%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>

<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>质损区域维护</title>
 <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
 <meta http-equiv="pragma" content="no-cache"> 
<meta http-equiv="cache-control" content="no-cache"> 
<meta http-equiv="expires" content="0"> 
</head>
<body>
  <div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 售后服务管理&gt;索赔预授权&gt;质损区域维护</div>
  <form name='fm' id='fm'>
  <TABLE class="table_query">
       <tr>            
        <td style="color: #252525;width: 115px;text-align: right">质损大类代码：</td>            
        <td>
			<input  class="middle_txt" id="QUD_CODE" maxlength="25"  name="QUD_CODE" type="text" />
        </td>
        <td class="table_query_3Col_label_6Letter">质损大类名称：</td>
        <td><input type="text" name="QUD_NAME" id="QUD_NAME" maxlength="25"  class="middle_txt" value=""/></td>
        <td class="table_query_3Col_label_6Letter">&nbsp;</td>
        <td>&nbsp;</td>     
       </tr>  
        <tr>            
        <td style="color: #252525;width: 115px;text-align: right">质损小类代码：</td>            
        <td>
			<input  class="middle_txt" id="QUD_SON_CODE" maxlength="25"  name="QUD_SON_CODE" type="text" />
        </td>
        <td class="table_query_3Col_label_6Letter">质损小类名称：</td>
        <td><input type="text" name="QUD_SON_NAME" id="QUD_SON_NAME" maxlength="25"  class="middle_txt" value=""/></td>
        <td class="table_query_3Col_label_6Letter">&nbsp;</td>
        <td>&nbsp;</td>     
       </tr>  
       <tr>
       <td colspan="4" align="center">
        	    <input  id="queryBtn" class="normal_btn" type="button" name="button1" value="查询"  onclick="__extQuery__(1)"/>
        	    <input  id="add" class="normal_btn" type="button" name="button2" value="新增"  onclick="location='qudForward.do?flag=0';"/>
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
	var url = "<%=request.getContextPath()%>/repairOrder/RoMaintainMain/qudQuery.json";
	var title = null;
	
	var columns = [
				{header: "序号",sortable: false,align:'center',renderer:getIndex},
				{header: "质损大类代码",sortable: false,dataIndex: 'qudCode',align:'center'},
				{header: "质损大类名称",sortable: false,dataIndex: 'qudName',align:'center'},	
				{header: "质损小类代码",sortable: false,dataIndex: 'qudSonCode',align:'center'},
				{header: "质损小类名称",sortable: false,dataIndex: 'qudSonName',align:'center'},	
				{header: "操作",sortable: false,dataIndex: 'qudId',renderer:myLink ,align:'center'}
		      ];
	
	//修改的超链接设置
	function myLink(value,meta,record){
	    return String.format(
         "<a href=\"<%=contextPath%>/repairOrder/RoMaintainMain/qudForward.do?flag=1&ID="
			+ value + "\">[修改]</a><a href=\"#\" onclick='sel(\""+value+"\")'>[删除]</a>");
	}
	//删除方法：
	function sel(str){
		MyConfirm("是否确认删除？",del,[str]);
	}  
	//删除
	function del(str){
		makeNomalFormCall('<%=contextPath%>/repairOrder/RoMaintainMain/qudDel.json?ID='+str,delBack,'fm','');
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