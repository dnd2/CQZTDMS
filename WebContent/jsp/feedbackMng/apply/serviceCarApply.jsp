<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="com.infodms.dms.common.Constant"%>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<head>
<title>服务车申请表</title>
<% 
	String contextPath = request.getContextPath();
	String orderId = (String)request.getAttribute("orderId");
%>
</head>
<body onload='doInit()'>
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 信息反馈管理&gt;信息反馈提报 &gt; 服务车申请表</div>
<form id="fm" name="fm" method="post">
<input type="hidden" name="seriesList" id="seriesList" value="<%=request.getAttribute("seriesList")%>"/>
<table class="table_query" >	
	 <tr>
            <td  class="table_query_2Col_label_5Letter">工单号：</td>
            <td ><input id="ORDER_ID" name="ORDER_ID" value="" type="text" class="middle_txt" datatype="1,is_digit_letter,18">            </td>
            <td  class="table_query_2Col_label_5Letter">车型：</td>
            <td><script type="text/javascript">
              var seriesList=document.getElementById("seriesList").value;
    	      var str="";
    	      str += "<select id='MODEL_ID' name='MODEL_ID'>";
    	      str+=seriesList;
    		  str += "</select>";
    		  document.write(str);
	        </script></td>
          </tr>
          <tr>
            <td class="table_query_2Col_label_5Letter" >创建时间： </td>
            <td nowrap="nowrap">
            <input type="text" name="CON_APPLY_DATE_START" id="CON_APPLY_DATE_START_ID"  datatype="1,is_date,10" group="CON_APPLY_DATE_START_ID,CON_APPLY_DATE_END_ID" hasbtn="true" callFunction="showcalendar(event, 'CON_APPLY_DATE_START_ID', false);"/>
              至
  			<input type="text" name="CON_APPLY_DATE_END" id="CON_APPLY_DATE_END_ID"  datatype="1,is_date,10" group="CON_APPLY_DATE_START_ID,CON_APPLY_DATE_END_ID" hasbtn="true" callFunction="showcalendar(event, 'CON_APPLY_DATE_END_ID', false);"/>
          </tr>
         
		 
	<tr>
            <td colspan="4" align="center" nowrap><input class="normal_btn" type="button" name="button" value="查询"  onClick="__extQuery__(1);">
			<input class="normal_btn" type="BUTTON" name="button1" value="新增"  onClick="location='newServiceCarApplyforward.do';"></td>
          </tr>
</table>
  <!-- 查询条件 end -->
  <!--分页 begin -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
  <!--分页 end --> 
 <div id="loader" style='position:absolute;z-index:200;background:#FFCC00;padding:1px;top:4px;display:none;display: none;'></div>
</form>

<!--页面列表 begin -->
<script type="text/javascript" >
	
	var myPage;
//查询路径
	var url = "<%=contextPath%>/feedbackmng/apply/ServiceCarApply/servicecarapplyQuery.json?queryType=submit";
				
	var title = null;

	var columns = [
				{id:'chk',header: "<input type='checkbox' name='checkAll' onclick='selectAll(this,\"orderIds\")' />", width:'8%',sortable: false,dataIndex: 'orderId',renderer:myCheckBox},
					{id:'id',header: "申请单号", width:'15%', dataIndex: 'orderId',renderer:myLink},
					{header: "拟购车型", width:'11%', dataIndex: 'modelName'},
					{header: "申请车型市场价", width:'11%', dataIndex: 'saleAmount',renderer:dezero},
					{header: "联系人", width:'7%', dataIndex: 'linkMan'},
					{header: "创建日期", width:'10%', dataIndex: 'createDate',renderer:formatDate},
					{header: "工单状态", width:'15%', dataIndex: 'appStatus', renderer:getItemValue},
					{id:'action', width:'5%',header: "操作",sortable: false,dataIndex: 'orderId',renderer:myLink1 ,align:'center'}
		      ];
		      
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
		if (record.data.appStatus==<%=Constant.SERVICE_APPLY_ACTIVE_STATUS_UNREPORT%>
		||record.data.appStatus==<%=Constant.SERVICE_APPLY_ACTIVE_AREA_STATUS_REJECT%>
		||record.data.appStatus==<%=Constant.SERVICE_APPLY_ACTIVE_SERVICE_STATUS_REJECT%>
		||record.data.appStatus==<%=Constant.SERVICE_APPLY_ACTIVE_CAR_ENTER_STATUS_REJECT%>) {
  		return String.format("<a href=\"<%=contextPath%>/feedbackmng/apply/ServiceCarApply/servicecarapplyUpdatePre.do?ORDER_ID="
			+ value + "\">[修改]</a>");
		}else {
			return "";
		}
	}
	//工单的超链接
	function myLink(value){
        return String.format(
               "<a href='#' onclick='OpenHtmlWindow(\"<%=contextPath%>/feedbackmng/apply/ServiceCarApply/servicecarapplyDetail.do?ORDER_ID="+value+"\",800,500)'>["+value+"]</a>");
    }
	
	//全选checkbox
	function myCheckBox(value,metaDate,record){
		if  (record.data.appStatus==<%=Constant.SERVICE_APPLY_ACTIVE_STATUS_UNREPORT%>
		||record.data.appStatus==<%=Constant.SERVICE_APPLY_ACTIVE_AREA_STATUS_REJECT%>
		||record.data.appStatus==<%=Constant.SERVICE_APPLY_ACTIVE_SERVICE_STATUS_REJECT%>
		||record.data.appStatus==<%=Constant.SERVICE_APPLY_ACTIVE_CAR_ENTER_STATUS_REJECT%>) {
			return String.format("<input type='checkbox' name='orderIds' value='" + value + "' />");
		}else {
			return String.format("<input disabled='true' type='checkbox' name='orderIds' value='" + value + "' />");
		}
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
	function showButton(){//显示上报、删除按钮
		if(document.getElementsByName("orderIds")==null){
		document.getElementById("bt").style.visibility = "hidden";
		}else{
			if(document.getElementsByName("orderIds").length!=0) {
				document.getElementById("bt").style.visibility = "visible";
			}else {
				document.getElementById("bt").style.visibility = "hidden";
			}
		}
	}
	
	
//设置超链接 end
	
</script>
<!--页面列表 end -->
<form name="form1" style="display:none">
<table id='bt' class="table_list">
<tr><th align="left"><p>
    <INPUT class="normal_btn" type=button value='上报' onclick='submitId()' name=modify >
    &nbsp;<INPUT class="normal_btn" onclick='deleteId()' type=button value='删除' name=modify >
  </p></th>
  </tr>
</table>
</form>
</body>
<script type="text/javascript">
	document.form1.style.display = "none";
		
	var HIDDEN_ARRAY_IDS=['form1'];
	
</script>
</html>