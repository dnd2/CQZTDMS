<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="com.infodms.dms.common.Constant"%>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>服务活动申请表大区审核</title>
<% 
	String contextPath = request.getContextPath();
	String orderId = (String)request.getAttribute("orderId");
%>
</head>
<body onload='doInit()'>
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 信息反馈管理&gt;信息反馈审批&gt;服务活动申请表大区审批</div>
<form id="fm" name="fm" method="post">
<input type="hidden" name="seriesList" id="seriesList" value="<%=request.getAttribute("seriesList")%>"/>
    <TABLE align=center width="95%" class=table_query >
	          <tr>
            <td class="table_query_2Col_label_6Letter">工单号：</td>
            <td><input id="ORDER_ID" name="ORDER_ID" value="" type="text" class="middle_txt" datatype="1,is_digit_letter,18"/></td>
            <td class="table_query_2Col_label_6Letter"> 经销商代码：</td>
            <td colspan="2" align="left" >
            	<input class="middle_txt" id="dealerCode" name="DEALER_CODE" value="" type="text"/>
            	<input class="mark_btn" type="button" value="&hellip;" onclick="showOrgDealer('dealerCode','','true','',true)"/>     
            </td>
          </tr>
          <tr>
          <td  class="table_query_2Col_label_6Letter"> 经销商名称：</td>
            <td ><input class="middle_txt" type="text" id="DEALER_NAME" name="DEALER_NAME" datatype="1,is_digit_letter_cn"  size="16" value=""/>            </td>
            <td  class="table_query_2Col_label_6Letter"> 活动名称：</td>
            <td ><input class="middle_txt" type="text" id="ACT_NAME" name="ACT_NAME" datatype="1,is_digit_letter_cn"   size="16" value=""/>            </td>
          </tr>
          <tr>
          <td class="table_query_2Col_label_6Letter">活动类型：</td>
            <td nowrap><script type="text/javascript">
	              genSelBoxExp("ACT_TYPE",<%=Constant.ACTIVITY_TYPE%>,"",true,"short_sel","","true",'');
	            </script></td>
             <td class="table_query_2Col_label_6Letter" >提报时间： </td>
            <td nowrap="nowrap">
            <input type="text" name="CON_APPLY_DATE_START" id="CON_APPLY_DATE_START_ID"  datatype="1,is_date,10" group="CON_APPLY_DATE_START_ID,CON_APPLY_DATE_END_ID" hasbtn="true" callFunction="showcalendar(event, 'CON_APPLY_DATE_START_ID', false);"/>
              至
  			<input type="text" name="CON_APPLY_DATE_END" id="CON_APPLY_DATE_END_ID"  datatype="1,is_date,10" group="CON_APPLY_DATE_START_ID,CON_APPLY_DATE_END_ID" hasbtn="true" callFunction="showcalendar(event, 'CON_APPLY_DATE_END_ID', false);"/>
  			</td>
          </tr>
           <tr>
            <td colspan="6" align="center" nowrap><input class="normal_btn" type="BUTTON" name="button1" value="查询"  onClick="__extQuery__(1);" /></td>
          </tr>
  </table>
    <!-- 查询条件 end -->
  <!--分页 begin -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
  <!--分页 end --> 

</form>


<br>
    <!-- 查询条件 end -->
  <!--分页 begin -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
  <!--分页 end --> 
<div id="loader" style='position:absolute;z-index:200;background:#FFCC00;padding:1px;top:4px;display:none;display: none;'></div>
<!--页面列表 begin -->
<script type="text/javascript" >
	var myPage;
//查询路径
	var url = "<%=contextPath%>/feedbackmng/approve/ServiceActivityApplyTeamAudit/applyQuery.json?RIGHT=TEAM";
				
	var title = null;

	var columns = [
			//{id:'chk',header: "<input type='checkbox' name='checkAll' onclick='selectAll(this,\"orderIds\")' />", width:'8%',sortable: false,dataIndex: 'orderId',renderer:myCheckBox},
				{id:'id',header: "经销商代码", width:'10%', dataIndex: 'dealerCode',renderer:dezero},
				{header: "经销商名称", width:'15%', dataIndex: 'dealerName'},
				{header: "工单号", width:'15%', dataIndex: 'orderId',renderer:myLink},
				{header: "申请类型", width:'10%', dataIndex: 'actType', renderer:getItemValue},
				{header: "联系人", width:'10%', dataIndex: 'linkMan'},
				{header: "经销商电话", width:'10%', dataIndex: 'tel'},
				{header: "提报日期", width:'10%', dataIndex: 'actDate',renderer:formatDate},
				{header: "申请状态", width:'15%', dataIndex: 'actStatus', renderer:getItemValue},
				{id:'action',width:'15%',header: "操作",sortable: false,dataIndex: 'orderId',renderer:myLink1 ,align:'center'}
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
  		return String.format("<a href=\"<%=contextPath%>/feedbackmng/approve/ServiceActivityApplyTeamAudit/serviceactivityapplyAuditPre.do?TYPE=TEAM&ORDER_ID="
			+ value + "\">[审批]</a>");
	}
	//工单的超链接
	function myLink(value){
        return String.format(
               "<a href='#' onclick='OpenHtmlWindow(\"<%=contextPath%>/feedbackmng/apply/ServiceActivityApply/serviceactivityapplyDetail.do?ORDER_ID="+value+"\",800,500)'>["+value+"]</a>");
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
		MyConfirm("确认上报？",submitApply);
	}
	function submitApply () {
		var str=getCheckedToStr("orderIds");
		makeNomalFormCall('<%=request.getContextPath()%>/feedbackmng/apply/ServiceActivityApply/serviceactivityapplySubmit.json?orderIds='+str,returnBack0,'fm','queryBtn');
	}
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
		MyConfirm("确认删除？",deleteApply);
	}
	function deleteApply() {
		var str=getCheckedToStr("orderIds");
		makeNomalFormCall('<%=request.getContextPath()%>/feedbackmng/apply/ServiceActivityApply/serviceactivityapplyDelete.json?orderIds='+str,returnBack,'fm','queryBtn');
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
		window.location.href = "<%=request.getContextPath()%>/feedbackmng/apply/ServiceActivityApply/serviceactivityapplyQuery.json";
	}
	function showButton(){//显示上报、删除按钮
		if(document.getElementsByName("orderIds")==null){
		document.getElementById("bt").style.visibility = "hidden";
		}else{
		document.getElementById("bt").style.visibility = "visible";
		}
	}
	
	
//设置超链接 end
	
</script>
<!--页面列表 end -->
</body>
</html>