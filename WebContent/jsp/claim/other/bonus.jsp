<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="com.infodms.dms.common.Constant"%>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<TITLE>正负激励</TITLE>
<% 
	String contextPath = request.getContextPath();
	String orderId = (String)request.getAttribute("orderId");
%>
<SCRIPT LANGUAGE="JavaScript">
	var myPage;
//查询路径
	var url = "<%=contextPath%>/claim/other/Bonus/queryDealer.json";
				
	var title = null;

	var columns = [
				{id:'id',header: "经销商代码", width:'10%', dataIndex: 'DEALER_CODE'},
				{header: "经销商名称", width:'15%', dataIndex: 'DEALER_NAME'},
				{header: "经销商简称", width:'15%', dataIndex: 'DEALER_SHORTNAME'},
				{header: "操作", width:'15%', dataIndex: 'DEALER_ID',renderer:twoLinks}
	      ];
		      
	//设置超链接  begin      
	function doInit()
	{
   		loadcalendar();
   		__extQuery__(1);
	}
	function twoLinks(value,metadata,record) {
	
		<%-- if(<%=Constant.chana_jc%>== parseInt($('CODE_ID').value)){
			return String.format("<a href=\"<%=contextPath%>/claim/other/Bonus/punishForward.do?DEALER_ID="
					+ value + "\">[罚款处理]</a>"+"<a href=\"<%=contextPath%>/claim/other/Bonus/punishHistoryForward.do?DEALER_ID="
					+ value + "\">[奖惩历史]</a>");

		}

		else{ --%>
		return String.format("<a href=\"<%=contextPath%>/claim/other/Bonus/punishForward.do?DEALER_ID="
			+ value + "\">[罚款处理]</a>"+"<a href=\"<%=contextPath%>/claim/other/Bonus/addPunishForward.do?DEALER_ID="
			+ value + "\">[奖励]</a>"+"<a href=\"<%=contextPath%>/claim/other/Bonus/punishHistoryForward.do?DEALER_ID="
			+ value + "\">[奖惩历史]</a>");
		}
	/* } */
	//单位代码渲染函数
	function dezero(value,metadata,record) {
		if (value==0){
			return "";
		}else {
			return value;
		}
	}
	//格式化时间为YYYY-MM-DD
	function formatDate(value,meta,record) {
		if (value==""||value==null) {
			return "";
		}else {
			return value.substr(0,10);
		}
	}
	//修改的超链接设置
	function myLink1(value,meta,record){
  		return String.format("<a href=\"<%=contextPath%>/feedbackmng/apply/ServiceCarApply/servicecarapplyUpdatePre.do?ORDER_ID="
			+ value + "\">[修改]</a>");
	}
	//工单的超链接
	function myLink(value){
        return String.format(
               "<a href='#' onclick='OpenHtmlWindow(\"<%=contextPath%>/feedbackmng/apply/ServiceCarApply/servicecarapplyDetail.do?ORDER_ID="+value+"\",800,500)'>["+value+"]</a>");
    }
	
	//全选checkbox
	function myCheckBox(value,metaDate,record){
		return String.format("<input type='checkbox' name='orderIds' value='" + value + "' />");
	}
	
	//设置超链接
	function mySelect(value,meta,record){
  		return String.format(
         "<a href=\"#\" onclick='sel(\""+record.data.orderId+"\")'>"+ value +"</a>");

	}
	
	//具体操作
	function sel(){
		MyAlert("超链接！");
	}
	
	//取得已经选择的checkbox，拼接成字符串，各项目以,隔开
	function getCheckedToStr(name) {
		var str="";
		var chk = document.getElementsByName(name);
		if (chk==null){
			return "";
		}else {
		var l = chk.length;
		for(var i=0;i<l;i++){        
			if(chk[i].checked)
			{            
			str = chk[i].value+","+str; 
			}
		}
			return str;
		}
	}
	//上报
	function submitId(){
	var str=getCheckedToStr("orderIds");
		if (str!=""){
		MyConfirm("确认上报？",submitApply,[str]);
		}else {
			MyAlert("请选择至少一条要上报的申请单！");
		}
	}
	//上报操作
	function submitApply (str) {
		
			makeNomalFormCall('<%=request.getContextPath()%>/feedbackmng/apply/ServiceCarApply/servicecarapplySubmit.json?orderIds='+str,returnBack0,'fm','queryBtn');
		
	}
	//上报回调函数
	function returnBack0(json){
		var avl = json.returnValue;
		if(avl==1){
			__extQuery__(1);
			MyAlert("上报成功！");
		}else{
			MyAlert("上报失败！请联系管理员！");
		}
	}
	//删除
	function deleteId(){
	var str=getCheckedToStr("orderIds");
		if (str!=""){
		MyConfirm("确认删除？",deleteApply,[str]);
		}else {
			MyAlert("请选择至少一条要删除的申请单！");
		}
	}
	//清空经销商框
		function clearInput(){
			var target = document.getElementById('dealerCode');
			target.value = '';
		}
	function deleteApply(str) {
		
			makeNomalFormCall('<%=request.getContextPath()%>/feedbackmng/apply/ServiceCarApply/servicecarapplyDelete.json?orderIds='+str,returnBack,'fm','queryBtn');
		
	}
	//删除回调函数
	function returnBack(json){
		var avl = json.returnValue;
		if(avl==1){
			__extQuery__(1);
			MyAlert("删除成功！");
		}else{
			MyAlert("删除失败！请联系管理员！");
		}
	}
	//回调函数
	function showResult(json){
		if(json.ACTION_RESULT == '1'){
			goBack();
			MyConfirm("新增成功！点击确认返回查询界面或者点击左边菜单进入其他功能！","window.location.href = '<%=request.getContextPath()%>/sysmng/orgmng/DlrInfoMng/queryAllDlrInfo.do'");
		}else if(json.ACTION_RESULT == '2'){
			MyAlert("新增失败！请重新载入或者联系系统管理员！");
		}
	}
</SCRIPT>

</HEAD>
<BODY onload="__extQuery__(1);loadcalendar();">
<div class="navigation"><img src="../../../img/nav.gif" />&nbsp;当前位置：售后服务管理&gt;特殊费用管理&gt;正负激励</div>
  <form method="post" name = "fm" id="fm">
	<div class="form-panel">
	<h2><img src="<%=contextPath%>/jmstyle/img/search-ico.png" style="margin-bottom: -13px;"/>查询条件</h2>
	<div class="form-body">
    <TABLE width="100%" class="table_query" >
          <tr>
           <td style="text-align: right;">经销商代码：
           <input type="hidden"  id="CODE_ID" value="${code}"/>
           </td>
           
            <td colspan="2" align="left" >
            	<input class="middle_txt" id="dealerCode" name="DEALER_CODE" onclick="showOrgDealer('dealerCode','','true','',true,'','10771002')" value="" type="text"/>
            	<input name="clrBtn" type="button" class="normal_btn" onclick="clearInput();" value="清除"/>       
            </td>
            <td style="text-align: right;">经销商名称：</td>
            <td><input name="DEALER_NAME" id="DEALER_NAME" value="" type="text" class="middle_txt" datatype="1,is_digit_letter_cn" /></td>
          </tr>

          <tr>
            <td colspan="6" style="text-align: center;" nowrap>
            	<input name="queryBtn" type="button" class="u-button u-query" onclick="__extQuery__(1)" value="查 询" id="queryBtn" /> &nbsp; 
				<input type="reset" class="u-button u-cancel" value="重 置"/> &nbsp; 
<!--             <input id="queryBtn" class="normal_btn" type="BUTTON" name="button1" value="批量导入"  onclick="goImport();" />
            <input id="queryBtn" class="normal_btn" type="BUTTON" name="button1" value="批量导出"  onclick="goExport();" /> -->
            </td>
          </tr>
  </table>
  </div>
  </div>
    <!-- 查询条件 end -->
  <!--分页 begin -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
  <!--分页 end --> 
  <div id="loader" style='position:absolute;z-index:200;background:#FFCC00;padding:1px;top:4px;display:none;display: none;'></div>
</form>


<br>

<SCRIPT LANGUAGE="JavaScript">

function goImport(){
	location = '<%=contextPath%>/claim/other/Bonus/ImportDealerByDlrInit.do' ;
}
function goExport(){
	location = '<%=contextPath%>/claim/other/Bonus/exportDealerByDlrInit.do' ;
}
</script>

</BODY>
</html>