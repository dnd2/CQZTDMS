<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
	String contextPath = request.getContextPath();
%>
<html>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>客户服务资料审批表</title>
</head>
<script type="text/javascript" >
function doInit()
{
   loadcalendar();
}
</script>
<body>
  <div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 信息反馈管理&gt;信息反馈提报&gt;服务资料审批表</div>
  <form method="post" name = "fm" id="fm">
  <table class="table_query" >
  <tr>
      <td class="" align="right" nowrap>工单号：</td>
      <td class="" nowrap>
      <input name="orderId" value="" type="text" class="middle_txt" >
      </td>
      <td class="" align="right" nowrap> 创建时间 ：</td>    
      <td class="" nowrap >
          <input name="beginTime" id="beginTime" value="" type="text" class="short_txt" datatype="1,is_date,10" group="beginTime,endTime" hasbtn="true" callFunction="showcalendar(event, 'beginTime', false);">
         	&nbsp;至&nbsp;
          <input name="endTime" id="endTime" value="" type="text" class="short_txt" datatype="1,is_date,10" group="beginTime,endTime" hasbtn="true" callFunction="showcalendar(event, 'endTime', false);">
          </td>   
       <!--  <input class="short_txt"  type="text" id="t1" name="beginTime" datatype="1,is_date,10" group="t1,t2"  value="" />
	   <input class="time_ico" type="button" onClick="showcalendar(event, 't1', false);" value=" " />&nbsp;至&nbsp;
       <input class="short_txt"  type="text" id="t2" name="endTime" datatype="1,is_date,10" group="t1,t2" value="" />
       <input class="time_ico" type="button" onClick="showcalendar(event, 't2', false);" value=" " />-->
  </tr>
  <tr>      
      <td class="" nowrap >&nbsp;</td>
      <td class="" nowrap>&nbsp;</td>      
      <td class="" nowrap>
      <input class="normal_btn" type="button" name="button" value="查询"  onClick="__extQuery__(1);showButton();">
      <input class="normal_btn" type="BUTTON" name="addBtn" value="新增"  onClick="addServiceInfo();" >
      </td>
      <td class="">&nbsp;</td>
      <td class="">&nbsp;</td>
  </tr>	
  </table>
  <!--分页 begin -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
  <!--分页 end --> 
   <div id="loader" style='position:absolute;z-index:200;background:#FFCC00;padding:1px;top:4px;display:none;display: none;'></div>
   </form>
  <!--页面列表 begin -->
  <script type="text/javascript" >
    
    //查询路径
	var url = "<%=contextPath%>/feedbackmng/apply/ServiceInfoApplyManager/queryServiceInfo.json?commond=1";
				
	var title = null;

	var columns = [
				{id:'action',header: "<input type='checkbox' name='checkAll' onclick='selectAll(this,\"orderIds\")' />", width:'8%',sortable: false,dataIndex: 'ORDER_ID',renderer:myCheckBox},
				{id:'action',header: "申请单号",sortable: false,dataIndex: 'ORDER_ID',renderer:mySelect ,align:'center'},
				{header: "邮寄方式",sortable: false,dataIndex:'MAIL_TYPE',align:'center',renderer:getItemValue},
				{header: "创建时间",sortable: false,dataIndex:'CREATE_DATE',align:'center'},
				{header: "工单状态",sortable: false,dataIndex:'STATUS',align:'center',renderer:getItemValue},
				{id:'action',header: "操作",sortable: false,dataIndex: 'ORDER_ID',renderer:myLink ,align:'center'}
		      ];
		      
    //设置超链接  begin      
	
	//修改的超链接设置
	function myLink(value,meta,record){
  		return String.format("<a href='#' onclick='modifyServiceInfo(\""+ value +"\")'>[修改]</a>");
	}
	
	// 修改操作
	function modifyServiceInfo(value){
		fm.action = '<%=contextPath%>/feedbackmng/apply/ServiceInfoApplyManager/modifyServiceInfoInit.do?orderId=' + value;
	 	fm.submit();
	}
	
	//全选checkbox
	function myCheckBox(value,metaDate,record){
		return String.format("<input type='checkbox' name='orderIds' value='" + value + "' />");
	}
	
	//设置超链接
	function mySelect(value,meta,record){
  		return String.format(
         "<a href=\"#\" onclick='sel(\""+record.data.ORDER_ID+"\")'>["+ value +"]</a>");

	}
	
	//具体操作
	function sel(value){
		OpenHtmlWindow('<%=contextPath%>/feedbackmng/apply/ServiceInfoApplyManager/serviceInfoDetailView.do?orderId='+value,800,500);
	}
	
    //设置超链接 end
	
	// 显示下方的上报和删除按钮
	function showButton(){
		document.getElementById("bottomBtn").style.visibility= "visible";
	}
	
	// 新增服务资料审批表
    function addServiceInfo(){
    	disableBtn($("addBtn"));
    	window.location.href='<%=contextPath%>/feedbackmng/apply/ServiceInfoApplyManager/serviceInfoApplyAdd.do';
		
    }
	
	// 上报按钮
	function submitServiceInfo(){
		var chk = document.getElementsByName("orderIds");
		var l = chk.length;
		var cnt = 0;
		for(var i=0;i<l;i++){
			if(chk[i].checked){
		       cnt++;
			}
		 }
		if(cnt==0){
		    MyAlert("请选择！");
		    return ;
		}else{
			 
	    	 MyConfirm("确认上报？",submitConfirm);
		}
	   
		
	}
	
	// 上报确认
	function  submitConfirm(){
		makeNomalFormCall('<%=contextPath%>/feedbackmng/apply/ServiceInfoApplyManager/serviceInfoApplySubmit.json',showBack,'fm');
	}
	
	// 上报回调函数
	function showBack(json){
		var rtnValue = json.returnValue;
		if(rtnValue==1){
			__extQuery__(1);
			MyAlert("上报成功！");
		}else{
			MyAlert("上报失败！请联系管理员！");
		}
	}
	
	// 删除按钮
	function delServiceInfo(){
	   var cnt = 0;
		var l=document.fm.orderIds.length;
		var chk=document.getElementsByName("orderIds");
		for(var i=0;i<l;i++){
			if(chk[i].checked){
		       cnt++;
			}
		 }
		if(cnt==0){
		    MyAlert("请选择！");
		    return ;
		}else{
		    MyConfirm("确认删除？",deleteConfirm);
		}
	}
	
	// 删除确认
	function deleteConfirm(){
		makeNomalFormCall('<%=contextPath%>/feedbackmng/apply/ServiceInfoApplyManager/serviceInfoApplyDel.json',showResult,'fm');

	}
	// 删除回调函数
	function showResult(json){
		var rtnValue = json.returnValue;
		if(rtnValue==1){
			__extQuery__(1);
			MyAlert("删除成功！");
		}else{
			MyAlert("删除失败！请联系管理员！");
		}
	}
	
</script>
<!--页面列表 end -->
<form name="form1" style="display:none">
<table id='bt' class="table_list">
<tr><th align="left"><p>
	<INPUT class="normal_btn" type=button value='上报' name="submitBtn" onclick="submitServiceInfo()">&nbsp;
     <INPUT class="normal_btn" type=button value='删除' name="deleteBtn" onclick="delServiceInfo();">
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