<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c" %>
<%@ page import="com.infodms.dms.common.Constant"%>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<TITLE>正负激励</TITLE>
<% 
	String contextPath = request.getContextPath();
	String dealerId = (String)request.getAttribute("DEALER_ID");
%>
<SCRIPT LANGUAGE="JavaScript">
	var myPage;
//查询路径
	var url = "<%=contextPath%>/claim/other/Bonus/punishHistory.json?DEALER_ID=<%=dealerId%>";
				
	var title = null;
	
	var columns = [
				{header: "操作", width:'15%', dataIndex: 'FINE_ID',renderer:twoLinks},
				{id:'id',header: "经销商编码", width:'10%', dataIndex: 'DEALER_CODE'},
				{header: "经销商名称", width:'10%', dataIndex: 'DEALER_NAME'},
				{header: "奖惩金额(元)", width:'1%', dataIndex: 'LABOUR_SUM'},
				{header: "奖惩原因", width:'15%', dataIndex: 'FINE_REASON'},
				{header: "厂家", width:'15%', dataIndex: 'AREA_NAME'},
				{header: "奖惩日期", width:'1%', dataIndex: 'PAY_DATE',renderer:formatDate},
				{header: "奖惩类型", width:'1%', dataIndex: 'FINE_TYPE',renderer:getItemValue},
				{header: "状态", width:'15%', dataIndex: 'PAY_STATUS',renderer:yourLink}
				
	      ];
	function yourLink(value,metadata,record){
		
		var str = getItemValue(value);
		if(record.data.PAY_STATUS==<%=Constant.PAY_STATUS_02%>){
			str = "&nbsp;<a href='#' onclick='queryInfo(\""+ record.data.CLAIMBALANCE_ID +"\")'>["+str+"]</a>";
		}
		
		return str;
		
	}
	
	//设置超链接  begin      
	function doInit()
	{
   		loadcalendar();
	}

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
	//返回
	function goBack(){
		window.location.href = "<%=request.getContextPath()%>/feedbackmng/apply/ServiceCarApply/servicecarapplyQuery.json";
	}
	
	function twoLinks(value,metadata,record) {
		var url = "<a href=\"###\" onclick=\"fmFind("+value+"); \">[查看]</a>&nbsp;";/* <a href=\"###\" onclick=\"deletefm("+value+"); \">[删除]</a> */
		
		return String.format(url);
	}
	function fmFind(value){
		OpenHtmlWindow(globalContextPath+"/claim/other/Bonus/queryDealerByDlrDetail.do?fineId="+value,900,520);
	} 
	function deletefm(value) {	    
		MyConfirm("确定删除？",del,[value]);
	}
//确定按钮事件
function del(value) {
		var url=globalContextPath+"/claim/other/Bonus/delqueryDealerByDlrDetail.json?fineId="
		+ value + "&dealerId="
		+ <%=dealerId%> + "";
		sendAjax(url,delInfo,'fm');  	
}
function delInfo(json){
	var msg=json.msg;
	if(msg=="00"){
		MyAlertForFun("操作成功！",Back);
	}else{
		MyAlert("操作失败！");
	}	
}
function Back() {
	__extQuery__(1);
}
	function queryInfo(val)
	{
		fm.action = "<%=contextPath%>/claim/authorization/BalanceMain/queryBalanceInfo.do?id="+val;
		fm.submit();
	}
	function oemTxt(a,b){
		document.getElementById(a).value="";
		document.getElementById(b).value="";
		}
</SCRIPT>

</HEAD>
<BODY onload="__extQuery__(1);">
<div class="navigation"><img src="../../../img/nav.gif" />&nbsp;当前位置：售后服务管理&gt;特殊费用管理&gt;正负激励&gt;奖惩历史</div>
  <form method="post" name = "fm" id="fm">
  <div class="form-panel">
	<h2><img src="<%=contextPath%>/jmstyle/img/search-ico.png" style="margin-bottom: -13px;"/>查询条件</h2>
	<div class="form-body">
   <TABLE align=center width="100%" class="table_query" border='0'>
          <tr>
          	<td style="text-align:right">厂家：</td>
            <td><select style="width: 152px;" class="u-select" name="yieldly" id="yieldly">
				 <option value="" >
    				-请选择-
    			  </option>
	              <c:forEach var="Area" items="${Area}" >
 				  <option value="${Area.areaId}" >
    				<c:out value="${Area.areaName}"/>
    			  </option>
    			 </c:forEach>
             </select></td>
            <td  style="text-align:right">奖惩时间：</td>
			<td >
				<input  type="text" readonly="readonly" class="short_txt" style="width: 80px;" name="deductStartDate" id="deductStartDate" />
				<input class="time_ico" type="button" onclick="showcalendar(event, 'deductStartDate', false);" value="&nbsp;" />
					至
				<input  type="text" readonly="readonly" class="short_txt" style="width: 80px;"  name="deductEndDate" id="deductEndDate" />
				<input class="time_ico" type="button" onclick="showcalendar(event, 'deductEndDate', false);" value="&nbsp;" />
				<input class="normal_btn" type="button" value="清空" onclick="oemTxt('deductStartDate', 'deductEndDate');"/> 
			</td> 
			<td style="text-align:right">奖惩状态：</td>
            <td><script type="text/javascript">
						genSelBoxExp("status",<%=Constant.PAY_STATUS%>,"",true,"","onchange='changeFleet(this.value)'","false",'');
				</script>
			</td>
          </tr>
          <tr>
            <td colspan="6" style="text-align: center;" nowrap>
            	<input name="queryBtn" type="button" class="u-button u-query" onclick="__extQuery__(1)" value="查 询" id="queryBtn" /> &nbsp; 
				<input type="reset" class="u-button u-cancel" value="重 置"/> &nbsp; 
            <input type="button" class="u-button u-query" name="back" value="返回" onclick="Back1();" />
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
</form>
<script type="text/javascript">
function Back1() {
	var fm = document.getElementById('fm');
	fm.action='<%=request.getContextPath()%>/claim/other/Bonus/bonusForward.do';
	fm.submit();
}
</script>
</BODY>
</html>