<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />

<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>服务活动发布范围管理</title>
<% String contextPath = request.getContextPath(); %>
<script type="text/javascript">
//1.日历控件初始化;
//2.页面加载时执行查询;
function doInit()
	{
	   loadcalendar();
	   __extQuery__(1);
	}
//新增执行经销商
function addDealerCode(){
	var activityId=document.getElementById("activityId").value;
	OpenHtmlWindow("<%=contextPath%>/claim/serviceActivity/ServiceActivityManageDealer/serviceActivityManageDealerNoExitsQuery.do?activityId="+activityId,800,500);
}
</script>
</head>

<body>
	<div class="navigation">
		<img src="<%=contextPath %>/img/nav.gif" />&nbsp;当前位置： 售后服务管理&gt;服务活动管理&gt;服务活动经销商管理
	</div>
<br/>
  <form method="post" name="fm" id="fm" enctype="multipart/form-data" >
  <input type="hidden" name="type" value="${type}">
  		<input type='hidden'  name=activityId  id='activityId'  value='<%=request.getAttribute("activityId")%>' >
  <table  class="table_query">
  	<tr> 		  
  		  <td style="display:block">
  		          <b>执行经销商列表：</b>&nbsp;&nbsp;
		  		  <input type="button" name="bt_new" id="bt_new_id" class="normal_btn" value="新增"  onclick="addDealerCode();" />
		  </td>
		  <td>
		      <input type="button" name="down1" id="down1" class="normal_btn" value="查询" onclick="__extQuery__(1);"  >
		   </td>
		  <td class="table_query_input" colspan="2">&nbsp;&nbsp;&nbsp;&nbsp; 
	     	 <input type="file" name="uploadFile" id="uploadFile" style="width: 250px"  value="" />
	     	  <input type="button" class="cssbutton"  name="queryBtn" value="导入" onclick="confirmUpload()" />	   
	      </td>
		   <td>
		      <input type="button" name="down" id="down" class="normal_btn" value="模板下载" onclick="downDEL();"  >
		   </td>
	      	<td> 
	      		<input type="button" name="bt_return" id="normal_btn" class="normal_btn" value="返回" onclick="goBack();">
	      </td>
  	</tr>
  </table>
  <table class="table_list" style="border-bottom:1px solid #DAE0EE">
  	     <tr>
	   		 <th colspan="6" align="left">
	   		 		<img class="nav" src="<%=contextPath %>/img/subNav.gif" />执行经销商类表
	   		 </th>
   		 </tr>
  </table>
   <!--分页 begin -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
   <!--分页 end --> 
</form>
 <br/>
 <!--没有数据时 上报/删除按钮隐藏 开始  -->
<form name="form1" style="display:none">
<table id="bt" class="table_query">
  	<tr> 		  
  		  <td align="left">
		     <input type="button" name="bt_delete" id="bt_delete_id" class="normal_btn" value="删除" onclick="subChecked();">
	      </td>
  	</tr>
  </table>
</form>
<!--页面列表 begin -->
<script type="text/javascript" >
//没有数据时 上报/删除按钮隐藏 开始 
document.form1.style.display = "none";

var HIDDEN_ARRAY_IDS=['form1'];
//没有数据时 上报/删除按钮隐藏 结束
	var myPage;
//查询路径
	var url = "<%=contextPath%>/claim/serviceActivity/ServiceActivityManageDealer/serviceActivityManageDealerMaintionQuery.json";
				
	var title = null;

	var columns = [
				{id:'action',header: "<input type='checkbox' name='checkAll' onclick='selectAll(this,\"orderIds\")' />", width:'8%',sortable: false,dataIndex: 'DEALER_ID',renderer:myCheckBox},
				{header: "经销商代码", dataIndex: 'DEALER_CODE', align:'center'},
				{header: "经销商名称",dataIndex: 'DEALER_NAME' ,align:'center'},
				{header: "经销商电话",dataIndex: 'PHONE' ,align:'center'}
		      ];

	//全选checkbox
	function myCheckBox(value,metaDate,record){
		return String.format("<input type='checkbox' name='orderIds' value='" + value + "' />");
	}
	//删除开始
	function serviceActivityManageDealerDelete(str){
     var activityId=document.getElementById("activityId").value;
	makeNomalFormCall('<%=contextPath%>/claim/serviceActivity/ServiceActivityManageDealer/serviceActivityManageDealerDelete.json?dealerId='+str+'&activityId='+activityId,returnBack,'fm','queryBtn');
	}
	//取得已经选择的checkbox，拼接成字符串，各项目以,隔开
	function getCheckedToStr(name) {
		var str="";
		var chk = document.getElementsByName(name);
		var l = chk.length;
		for(var i=0;i<l;i++){        
			if(chk[i].checked)
			{            
			str = chk[i].value+","+str; 
			}
	}
	return str;
	}
	function  downDEL()
	{
		fm.action = '<%=contextPath%>/claim/basicData/ClaimBasicLabourPrice/downloadDearle.do';
	    fm.submit();
	}
	
	
	function confirmUpload()
	{
		if(fileVilidate()){
			MyConfirm("确定上传选中的文件?",upload,[]);
		}
	}
	
	function upload(){
		btnDisable();
		fm.action = "<%=contextPath%>/claim/serviceActivity/ServiceActivityManageDealer/serviceActivityManageDealerUpload.do";
		fm.submit();
	}
	
	function btnDisable(){

	    $$('input[type="button"]').each(function(button) {
	        button.disabled = true;
	    });

	}
	
	function fileVilidate(){
		var importFileName = $("uploadFile").value;
		if(importFileName==""){
		    MyAlert("请选择上传文件");
			return false;
		}
		var index = importFileName.lastIndexOf(".");
		var suffix = importFileName.substr(index+1,importFileName.length).toLowerCase();
		if(suffix != "xls" && suffix != "cvs"){
		MyAlert("请选择Excel格式文件");
			return false;
		}
		return true;
	}
	
	//删除回调函数
	function returnBack(json){
		var del = json.returnValue;
		if(del==1){
			__extQuery__(1);
			MyAlert("删除成功！");
		}else{
			MyAlert("删除失败！请联系管理员！");
		}
	}
	//删除结束
    //上报/删除确认 开始
	function subChecked() {
		var str="";
		var chk = document.getElementsByName("orderIds");
		var l = chk.length;
		var cnt = 0;
		for(var i=0;i<l;i++){        
		if(chk[i].checked)
		{            
		str = chk[i].value+","+str; 
		cnt++;
		}
		}
		if(cnt==0){
		        MyAlert("请选择！");
		        return;
		    }else{
		     MyConfirm("是否确认删除？",serviceActivityManageDealerDelete,[str]);
		    }
		} 
	//上报/删除确认 结束
	
	//返回
	function goBack(){
	 fm.action= "<%=contextPath%>/claim/serviceActivity/ServiceActivityManageDealer/serviceActivityManageDealerInit.do";
	 fm.submit();
	}
</script>
<!--页面列表 end -->
</body>
</html>