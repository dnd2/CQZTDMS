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
   <div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 售后服务管理&gt;索赔旧件管理&gt;旧件回运设置</div>
  <form name='fm' id='fm'>
  <div class="form-panel">
		<h2><img src="<%=contextPath%>/jmstyle/img/search-ico.png" style="margin-bottom: -13px;"/>查询条件</h2>
			<div class="form-body">
  <TABLE class="table_query">
       <tr>            
        <td style="color: #252525;width: 115px;text-align: right">配件代码：</td>            
        <td>
			<input  class="middle_txt" id="PART_CODE"  name="PART_CODE" type="text" datatype="1,is_null,27"/>
        </td>
        <td class="table_query_3Col_label_6Letter"  style="text-align: right">配件名称：</td>
        <td><input type="text" name="PART_NAME" id="PART_NAME" datatype="1,is_null,30" class="middle_txt" value=""/></td>
        </tr>
        <tr>
        <td class="table_query_3Col_label_6Letter"  style="text-align: right">件号：</td>
        <td><input type="text" name="PART_NO" id="PART_NO" datatype="1,is_null,30" class="middle_txt" value=""/></td>   
        <td class="table_query_3Col_label_6Letter"  style="text-align: right">是否回运：</td>
        <td> <script type="text/javascript">
		genSelBoxExp("IS_RETURN",<%=Constant.IS_RETURN%>,"",true,"","","false",'');
 		</script></td>
       </tr>  
       <tr>
       <td colspan="4" style="text-align: center">
        	    <input  id="queryBtn" class="normal_btn" type="button" name="queryBtn" value="查询"  onclick="__extQuery__(1)"/>
        	<input  id="queryBtn" class="normal_btn" type="button" name="button1" value="批量修改"  onclick="batchChang()"/>
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
	var url = "<%=request.getContextPath()%>/claim/oldPart/ClaimOldPartApporoveStorageManager/partReturnQuery.json";
	var title = null;
	
	var columns = [
				{header:"<input type=\"checkBox\" id=\"checkBoxAll\" name=\"checkBoxAll\" onclick='selectAll(this,\"recesel\")' />全选", align:'center',sortable:false, dataIndex:'partId',width:'2%',renderer:checkBoxShow},
				{header: "操作",sortable: false,dataIndex: 'partId',renderer:myLink ,align:'center'},
				{header: "配件代码",sortable: false,dataIndex: 'partCode',align:'center'},
				{header: "配件名称",sortable: false,dataIndex: 'partName',align:'center'},	
				{header: "件号",sortable: false,dataIndex: 'partNo',align:'center'},	
				{header: "是否回运",sortable: false,dataIndex: 'isReturn',align:'center',renderer:getItemValue}
		      ];
//设置超链接  begin      
	
	//修改的超链接设置
	function myLink(value,meta,record){
	    return String.format(
        "<a href='#' onClick='updatePart("+value+","+record.data.isReturn+");'>[修改]</a>");
	}
		//设置复选框
function checkBoxShow(value,meta,record){
	return String.format("<input type='checkbox' id='recesel' name='recesel' value='" + record.data.partId + "' />");
}
	//修改方法：
	function updatePart(str,type){
		MyConfirm("是否确认修改？",update,[str,type]);
	}  
	//修改
	function update(str,type){
		makeNomalFormCall("<%=request.getContextPath()%>/claim/oldPart/ClaimOldPartApporoveStorageManager/partReturnUpdate.json?ID="+str+"&type="+type,refreshPage11,'fm','');
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
	function batchChang(){
	var allChecks = document.getElementsByName("recesel");
	var allFlag = false;
	var ids ="";
	for(var i = 0;i<allChecks.length;i++){
		if(allChecks[i].checked){
			allFlag = true;
			ids = allChecks[i].value+","+ids;
		}
	}
		if(ids!=""){
		ids=ids.substring(0,ids.length-1);
		}
	if(allFlag){
			MyConfirm("确认批量审批?",changeSubmit,[ids]);
	}else{
		MyAlert("请选择数据后再点击操作批量修改按钮！");
	}
	}
	function changeSubmit(ids) {
	var url="<%=request.getContextPath()%>/claim/oldPart/ClaimOldPartApporoveStorageManager/partReturnUpdate.json?ID="+ids;
	makeNomalFormCall(url,showResult22,'fm');
}
function showResult22(json){
	if(json.success != null && json.success == "true") {
			MyAlert("批量修改成功！");
			__extQuery__(1);
		} else {
			MyAlert("修改失败！请联系管理员！");
		}
}
</script>  