<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="com.infodms.dms.common.Constant"%>
<%@ page import="java.util.*"%>
<%@taglib uri="/jstl/cout" prefix="c" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>活动方案下发</title>
<% String contextPath = request.getContextPath();  %>
<!-- <input type="button" class="cssbutton" name="button1" onClick="toAddDealer();" value=""/> -->
<script type="text/javascript">
</script>
</head>
<body>
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 市场活动管理 &gt; 活动方案下发&gt; 活动方案制定</div>
<form method="post" name="fm" id="fm">	
	<div><img class="nav" src="<%=contextPath%>/img/subNav.gif" />&nbsp;方案范围</div>
	<table width=100% border="0" align="center" cellpadding="1" cellspacing="1" class="table_list">
		<tr>
			<th colspan="5" align="left">活动方案下发范围
			<input type="hidden"  name="orgId" size="15" value=""  id="orgId" class="middle_txt" datatype="1,is_noquotation,500" />
			<input type="hidden"  name="orgCode" size="15" value=""  id="orgCode" class="middle_txt" datatype="1,is_noquotation,500" />
		<input name="orgbu"  id="orgbu" type="button" class="cssbutton" onclick="showAllOrgs('orgCode','orgId','true','','${areaId}')" value="新增" />
			</th>
		</tr>
		<tr>
		<th>区域编码</th>
		<th>区域</th>
		<th>操作</th>
		</tr>
		<tbody id="dealerTable">
    	</tbody>
	</table>
	<div><img class="nav" src="<%=contextPath%>/img/subNav.gif" />&nbsp;方案附件</div>
	<table width=100% border="0" align="center" cellpadding="1" cellspacing="1" class="table_list">
		<tr>
	        <th colspan="3" align="left">附件列表：<input type="hidden" id="fjids" name="fjids"/>
				<span>
					<input type="button" class="cssbutton"  onclick="showUpload('<%=contextPath%>')" value ='添加附件'/>
				</span>
			</th>
		</tr>
		<tr>
			<td width="100%" colspan="2"><jsp:include page="${contextPath}/uploadDiv.jsp" /></td>
  		</tr>
	</table>
	<table width=100% border="0" align="center" cellpadding="1" cellspacing="1" class="table_query">
		<tr>
			<td colspan="4" align="center">
				<input type="hidden" name="campaignId" id="campaignId" value="${campaignId}"/>
				<input type="hidden" name="dealerIds" id="dealerIds" value=""/>
				<input type="button" class="cssbutton" name="saveBtn" onClick="toSave();" value="保存"/>
				<input type="button" class="cssbutton" name="assignBtn" onClick="toAssign();" value="下发"/>
			</td>
		</tr>
	</table>
</form>
<script type="text/javascript">

    //初始化
    function doInit(){
   		loadcalendar();  //初始化时间控件
	}
	//选择经销商
	function toAddDealer(){
		var campaignId = document.getElementById("campaignId").value;
		OpenHtmlWindow('<%=contextPath%>/sales/marketmanage/planissued/ActivitiesPlanMake/queryDealerInit.do?&campaignId='+campaignId,800,450);
	}
	//清除功能
	function toClear(){
		document.getElementById("campaignModel").value="";
		document.getElementById("modelId").value="";
	}
	
	//接收子窗口传回的值
	function getDealerInfo(dealerIds,str){
	 	var ids = document.getElementById("dealerIds").value = str;
	 	var arrIDs = ids.split(',');
	 	var arrInfo = dealerIds.split('@,');
	 	for(var i = 0;i<arrIDs.length;i++){
	 		var arr = arrInfo[i].split(",");
	 		var code = arr[0];
	 		var name = arr[1];
	 		var org = arr[2];
	 		var trNode = document.getElementById(arrIDs[i]);
	 		if(trNode!=null){
	 			var trID = trNode.id;
	 			if(trID.indexOf(arrIDs[i])==0){
            		MyAlert("该区域已存在，请选择其他区域！");
		    	}else{
		    		addRow(arrIDs[i],name,code,org,prn);
		    	}
	 		}else{
	 			addRow(arrIDs[i],name,code);
	 		}
	 	}
	 	return;
	 	
	}
	function myAddRow(){
		var orgCode=document.getElementById("orgCode").value;
		var url="<%=contextPath%>/sales/marketmanage/planissued/ActivitiesSpacePlanMake.json?orgCode="+orgCode;
		makeFormCall(url,orgCodeBack,'fm');
	}
	function orgCodeBack(json){
		MyAlert(11);
	}
	//根据取到的值动态生成表格
	function addRow(orgid,orgName,orgcode){
	    var addTable = document.getElementById("dealerTable");
		var rows = addTable.rows;
		var length = rows.length;
		var insertRow = addTable.insertRow(length);
		insertRow.className = "table_list_row";
		insertRow.id = orgid;
		insertRow.insertCell(0);
		insertRow.insertCell(1);
		insertRow.insertCell(2);
		addTable.rows[length].cells[0].innerHTML =  "<td nowrap='nowrap'>"+orgcode+"</td>";
		addTable.rows[length].cells[1].innerHTML =  "<td nowrap='nowrap'>"+orgName+"<input type='hidden' name='dlrIds' id='"+orgid+"' value='"+orgid+"'/></td>";
		addTable.rows[length].cells[2].innerHTML =  "<td nowrap='nowrap'><input type='button' class='normal_btn' onclick='delRow("+orgid+")' value='删除'/></td>";
	}
	
	// 删除表格
	function delRow(orgid){
	    var tbodyNode=document.getElementById("dealerTable");
	    var trNode=document.getElementById(orgid);
		tbodyNode.removeChild(trNode);
		
    }
	
	// 保存按钮ACTION
	function toSave(){
	   // var length = document.getElementById("dealerTable").rows.length;
	   //MyAlert(document.getElementById("orgCode").value="");
	    if(document.getElementById("orgCode").value==""){
	    	MyAlert("请选择活动经销商范围");
	    	return;
	    }else{
	      if(submitForm('fm')){
	    	MyConfirm("是否确认保存？", confirmAdd);
	      }
	    }
		
	}
	
	// 确认保存
	function confirmAdd(){
		makeNomalFormCall('<%=contextPath%>/sales/marketmanage/planissued/ActivitiesPlanMake/saveExecutePlan.json',showResult,'fm');
	
	}
	
	// 下发按钮ACTION
	function toAssign(){
		var length = document.getElementById("dealerTable").rows.length;
	    if(length==0){
	    	MyAlert("请选择活动经销商范围");
	    	return;
	    }else{
	       if(submitForm('fm')){
			  MyConfirm("是否确认下发？", confirmAssign);
		   }
		}
	}
	
	// 确认下发
	function confirmAssign(){
		makeNomalFormCall('<%=contextPath%>/sales/marketmanage/planissued/ActivitiesPlanMake/assignPlan.json',showResult,'fm');
	
	}
	
	
	
	// 回调方法
	function showResult(json){
		if(json.returnValue == '1'){
			window.parent.MyAlert("操作成功！");
			$('fm').action='<%=contextPath%>/sales/marketmanage/planissued/ActivitiesPlanMake/activitiesPlanMakeInit.do';
			$('fm').submit();
		}else{
			MyAlert("操作失败！请联系系统管理员！");
		}
	}
	
</script>
</body>
</html>