<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c"%>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />

<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>服务活动发布范围管理</title>
<% String contextPath = request.getContextPath(); %>
<script type="text/javascript">
//日历控件初始化
function doInit()
	{
	   loadcalendar();
	}
</script>
</head>

<body>	
<form method="post" name="fm" id="fm" enctype="multipart/form-data" >
	<input type="hidden" name="type" value="${type}">
	<div class="navigation">
			<img src="<%=contextPath %>/img/nav.gif" />&nbsp;当前位置： 售后服务管理&gt;服务活动管理&gt;服务活动经销商管理
	</div>
	<table class="table_query">
		<tr>
			<td width="10%" align="right">活动编号： </td>
			<td width="30%">
				<input name="activityCode" id="activityCode" value="" type="text" class="middle_txt" datatype="1,is_digit_letter,25" />
			</td>
			<td width="10%" align="right">活动名称：</td>
			<td width="30%">
				<input name="activityName" id="activityName" type="text" class="middle_txt" />
			</td>
		</tr>
		<tr>
			<td width="10%" align="right">查询区分：</td>
            <td width="30%">
            	<input type="radio" name="mySel" id="r1" checked onclick="areaQuery();"/>区域
            	&nbsp;&nbsp;
            	<input type="radio" name="mySel" id="r2" onclick="dealerQuery();"/>经销商
            </td>
           	<td width="10%" align="right">活动日期：</td>
			<td width="30%">
					<input class="short_txt" id="t1" name="startDate" datatype="1,is_date,10"
                         maxlength="10" group="t1,t2"/>
                  	<input class="time_ico" value=" " onclick="showcalendar(event, 't1', false);" type="button"/>
            		 至
            		 <input class="short_txt" id="t2" name="endDate" datatype="1,is_date,10"
                      maxlength="10" group="t1,t2"/>
             		<input class="time_ico" value=" " onclick="showcalendar(event, 't2', false);" type="button"/>
	       	</td>
		</tr>
		
		<tr>
			<td width="10%" align="right">主题编号：</td>
			<td width="30%">
				<input name="subjiectNO" id="subjiectNO" type="text" class="middle_txt" />
			</td>
			<td width="10%" align="right">主题名称：</td>
			<td width="30%">
				<input name="subjiectName" id="subjiectName" type="text" class="middle_txt" />
			</td>
		</tr>
		<tr id="div1" style="display:block">
			<td width="10%" align="right">所选区域：</td>
			<td width="30%" colspan="3">
				<input type="text" name="area_code" id="area_code" class="long_txt"/>
 				<input type="hidden" name="area_id" id="area_id"/>
				<input type="button" class="mini_btn" value="..." onclick="showOrg('area_code' ,'area_id' ,true,'');"/>
				<input type="button" class="normal_btn" value="清除" onclick="wrapOut();"/>
			</td>
		</tr>
		<tr id="div2" style="display:none">
			<td width="10%" align="right">所选经销商：</td>
			<td width="30%">
				<input type="text" name="dealer_code" id="dealer_code" class="long_txt"/>
				<input type="hidden" name="dealer_id" id="dealer_id"/>
				<input type="button" class="mini_btn" value="..." onclick="showOrgDealer('dealer_code','dealer_id',true,'',true,'','10771002');"/>
            	<input type="button" class="normal_btn" value="清除" onclick="wrapOut2();"/>
			</td>
			 <td width="10%" align="right">
		      <input type="button" name="down" id="down" class="normal_btn" value="模板下载" onclick="downDEL();"  >
		    </td>
		  <td width="30%"> 
	     	  <input type="file" name="uploadFile" id="uploadFile" style="width: 250px"  value="" />
	     	  <input type="button" class="cssbutton"  name="queryBtn" value="批量导入" onclick="confirmUpload()" />	   
	      </td>
		  
		</tr>
		<tr>
			<td colspan="4" align="center">
				<input class="normal_btn" type="button" name="button" value="查询"  onclick="mainQuery();"/>
			</td>
		</tr>
	</table>
<br />
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
</form>
 <br/>

<script type="text/javascript" >
	var url = null;
	
	var title = null;

	var columns = null ;
    //主页面第一次查询
    function mainQuery(){
        if($('r1').checked==true){
        	url = "<%=contextPath%>/claim/serviceActivity/ServiceActivityManageDealer/mainQuery.json?flag=1";
        	columns = [
        	            {header: '序号',align:'center',renderer:getIndex},
        	            {header: "主题编号", dataIndex: 'SUBJECT_NO', align:'center'},
        	   			{header: "主题名称",dataIndex: 'SUBJECT_NAME' ,align:'center'},
        	   			{header: "活动编号", dataIndex: 'ACTIVITY_CODE', align:'center'},
        	   			{header: "活动名称",dataIndex: 'ACTIVITY_NAME' ,align:'center'},
        	   			{header: "活动开始日期 ",dataIndex: 'STARTDATE' ,align:'center'},
        	   			{header: "活动结束日期",dataIndex: 'ENDDATE' ,align:'center'},
        	   			{header: "区域数量",dataIndex: 'AREA_SUM' ,align:'center'},
        	   			{header: "操作",dataIndex: 'ACTIVITY_ID',align:'center',renderer:areaModify}
        	   	      ];
        	__extQuery__(1);
        }else if($('r2').checked==true){
        	url = "<%=contextPath%>/claim/serviceActivity/ServiceActivityManageDealer/mainQuery.json?flag=2";
        	columns = [
						{header: '序号',align:'center',renderer:getIndex},
						{id:'action',header: "选择<input type='checkbox' name='checkAll' onclick='selectAll(this,\"ACTIVITY_ID\")' />", width:'3%',align:'center',sortable: false,dataIndex: 'ACTIVITY_ID',renderer:myCheckBox},
						{header: "主题编号", dataIndex: 'SUBJECT_NO', align:'center'},
        	   			{header: "主题名称",dataIndex: 'SUBJECT_NAME' ,align:'center'},
        	   			{header: "活动编号", dataIndex: 'ACTIVITY_CODE', align:'center'},
        	   			{header: "活动名称",dataIndex: 'ACTIVITY_NAME' ,align:'center'},
        	   			{header: "活动开始日期 ",dataIndex: 'STARTDATE' ,align:'center'},
        	   			{header: "活动结束日期",dataIndex: 'ENDDATE' ,align:'center'},
        	   			{header: "经销商数量",dataIndex: 'DEALER_SUM' ,align:'center'},
        	   			{header: "操作",dataIndex: 'ACTIVITY_ID',align:'center',renderer:dealerModify}
        	   	      ];
        	__extQuery__(1);
        }else{
            MyAlert('查询区分为必选项!');
            return ;
        }   	
    }
    function myCheckBox(value,metaDate,record){
		var input2;
		input2='<input type="checkbox" id="ACTIVITY_ID" name="ACTIVITY_ID" value="'+value+'" />';
		return String.format(input2);
	}
	function fileVilidate()
	{
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
		var checked= document.getElementsByName('ACTIVITY_ID');
		var jude = false;
		for(var i = 0 ;i < checked.length ; i++)
		{
			if(checked[i].checked)
			{
			  jude = true;
			  break;
			}
		}
		if(!jude)
		{
		 	MyAlert("请选择导入的活动 ！");
		 	return false;
		}
		
		return true;
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
	function upload()
	{
		btnDisable();
		fm.action = "<%=contextPath%>/claim/serviceActivity/ServiceActivityManageDealer/serviceActivityManageDealerUploadAll.do";
		fm.submit();
	}
	function btnDisable()
	{
	    $$('input[type="button"]').each(function(button) {
	        button.disabled = true;
	    });

	}
  	//区域维护操作
    function areaModify(value,meta,record){
    	return String.format(
   	         "<a href=\"<%=contextPath%>/claim/serviceActivity/ServiceActivityManageDealer/areaModify.do?activityId="+ value + "&type="+${type}+" \">[维护区域]</a>");
    }
    //经销商维护操作
    function dealerModify(value,meta,record){
    	return String.format(
    	         "<a href=\"<%=contextPath%>/claim/serviceActivity/ServiceActivityManageDealer/serviceActivityManageDealerMaintionQuery.do?activityId="+ value + "&type="+${type}+" \">[维护经销商]</a>");
    }
	//区域单选按钮事件
	function areaQuery(){
		$('div2').style.display = 'none' ;
		$('div1').style.display = 'block';
	}
	//经销商单选按钮事件
	function dealerQuery(){
		$('div1').style.display = 'none' ;
		$('div2').style.display = 'block';
	}
	//区域内容清除操作
	function wrapOut(){
		$('area_code').value = '' ;
		$('area_id').value = '' ;
	}
	//经销商内容清除操作
	function wrapOut2(){
		$('dealer_code').value = '' ;
		$('dealer_id').value = '' ;
	}
</script>
</body>
</html>