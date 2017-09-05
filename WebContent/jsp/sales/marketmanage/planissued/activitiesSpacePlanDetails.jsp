<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="com.infodms.dms.common.Constant"%>
<%@ page import="java.util.*"%>
<%@taglib uri="/jstl/cout" prefix="c" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>活动执行方案提报</title>
<% String contextPath = request.getContextPath(); 
   List  executePlans = (List)request.getAttribute("executePlans");
   List  attachList   = (List)request.getAttribute("attachList");
%>
<script type="text/javascript">
</script>
</head>
<body>
<div class="navigation"><img src="<%=request.getContextPath()%>/img/nav.gif" />&nbsp;当前位置： 市场活动管理 &gt; 活动方案管理&gt; 活动方案明细</div>
<form method="post" name="fm" id="fm">
	<div><img class="nav" src="<%=contextPath%>/img/subNav.gif" />&nbsp;业务范围</div>
	<table class="table_query" align="center">
		<tr class= "tabletitle">
			<th align = "right" width="10%">选择业务范围：</th>
			<th align = "left" >
				<select name="areaId" class="short_sel" disabled="disabled">
					<c:forEach items="${areaList}" var="po">
					<c:choose>
					<c:when test="${po.AREA_ID==compaignMap.AREA_ID}" >
						<option value="${po.AREA_ID}" selected="selected">${po.AREA_NAME}</option>
					</c:when>
	          		<c:otherwise>
	          		<option value="${po.AREA_ID}">${po.AREA_NAME}</option>
	          		</c:otherwise>
				    </c:choose>
					</c:forEach>
				</select>
				<input type="hidden" name="dealerId" id="dealerId" />
				<input type="hidden" name="area_id" id="area_id" value="" 

/>
				
			</th>
		</tr>
	</table>
	<div><img class="nav" src="<%=contextPath%>/img/subNav.gif" />&nbsp;方案信息</div>
	<table id="table1" width=100% border="0" align="center" cellpadding="1" cellspacing="1" class="table_query">
		<tr>
			<th colspan="5" align="left">&nbsp;方案说明</th>
		</tr>
		<tr>
			<td align="right">经销商代码：</td>
			<td align="left">${map2.ORG_CODE}</td>
			<td align="right">经销商名称：</td>
			<td align="left">${map2.ORG_NAME}</td>
			<td></td>
		</tr>
		<tr>
			<td align="right">品牌：</td>
			<td align="left">${map2.GROUP_NAME}</td>
			<td align="right">活动车型：</td>
			<td align="left">${map2.PROD_NAME}</td>
			<td width="20%"></td>
		</tr>
		<tr>
			<td align="right">车厂方案编号：</td>
			<td align="left">${map2.CAMPAIGN_NO}</td>
			<td align="right">方案名称：</td>
			<td align="left">${map2.CAMPAIGN_NAME}</td>
			<td></td>
		</tr>
		<tr>
			<td align="right">计划开始日期：</td>
			<td align="left">${map2.START_DATE}</td>
			<td align="right">计划结束日期：</td>
			<td align="left">${map2.END_DATE}</td>
			<td></td>
		</tr>
		<tr>
			<td align="right">活动主题：</td>
			<td colspan="4" align="left">${map2.CAMPAIGN_SUBJECT}</td>
		</tr>
		<tr>
			<td align="right">活动对象：</td>
			<td colspan="4" align="left">${map2.CAMPAIGN_OBJECT}</td>
		</tr>
		<tr>
			<td align="right">活动目的：</td>
			<td colspan="4" align="left">
				<textarea name="campaignPurpose" id="campaignPurpose" rows="4" cols="70" disabled="disabled">${map2.CAMPAIGN_PURPOSE}</textarea>
			</td>
		</tr>
		<tr>
			<td align="right">活动要求：</td>
			<td colspan="4" align="left">
				<textarea name="campaignNeed" id="campaignNeed" rows="4" cols="70" disabled="disabled">${map2.CAMPAIGN_NEED}</textarea>
			</td>
		</tr>
		<tr>
			<td align="right">活动主要内容：</td>
			<td colspan="4" align="left">
				<textarea name="campaignDesc" id="campaignDesc" rows="4" cols="70" disabled="disabled">${map2.CAMPAIGN_DESC}</textarea>
			</td>
		</tr>
		<tr>
			<th colspan="5" align="left">&nbsp;区域说明：</th>
		</tr>
		<tr>
			<td align="right">活动地点说明：</td>
			<td colspan="4" align="left">
				<textarea name="execAddDesc" id="execAddDesc" rows="4"   cols="70">${map3.EXEC_ADD_DESC}</textarea>
			</td>
		</tr>
		<tr>
		<td align="right">区域建议及整改意见：</td>
		<td colspan="4" align="left">
				<textarea name="adviceDesc" id="adviceDesc" rows="4"  cols="70">${map3.ADVICE_DESC}</textarea>
		</td>
		</tr>
	</table>
	<div><img class="nav" src="<%=contextPath%>/img/subNav.gif" />&nbsp;方案范围</div>
	<table width=100% border="0" align="center" cellpadding="1" cellspacing="1" class="table_list">
		<tr>
			<th colspan="5" align="left">活动方案下发范围
				<input type="button" class="cssbutton" name="button1" onClick="toAddDealer();" value="新增"/>
			</th>
		</tr>
		<tr>
		<td colspan="5" align="left">&nbsp;活动经销商范围</td>
		</tr>
		<tr>
		<th>区域</th>
		<th>省份</th>
		<th>经销商代码</th>
		<th>经销商简称</th>
		<th>操作</th>
		</tr>
		<tbody id="dealerTable">
		<% if(executePlans!=null&&executePlans.size()!=0){ %>
    	<c:forEach items="${executePlans}" var="epList">
       		<tr class="table_list_row" id="<c:out value="${epList.DEALER_ID}"/>">
            	<td><c:out value="${epList.ORG_NAME}"/></td>
            	<td><SCRIPT type="text/javascript">writeRegionName('<c:out value="${epList.PROVINCE_ID}"/>')</SCRIPT></td>
            	<td><c:out value="${epList.DEALER_CODE}"/><input type="hidden" name="dlrIds" value="<c:out value="${epList.DEALER_ID}"/>"/></td>
	            <td><c:out value="${epList.DEALER_SHORTNAME}"/></td>	
	            <td><input type='button' class='normal_btn' onclick='delRow(<c:out value="${epList.DEALER_ID}"/>)' value='删除'/></td>
           	 	<td id="1"></td>
        	</tr>
    	</c:forEach>
       <%} %>
    	</tbody>
	</table>
	<table id="table1" width=100% border="0" align="center" cellpadding="1" cellspacing="1" class="table_query">
		<tr>
			<th colspan="5" align="left">&nbsp;方案及总结附件</th>
		</tr>
		<tr align="center">
			<td>附件名称</td>
			<td>操作</td>
		</tr>
		<c:forEach items="${list4}" var="list4">
			<tr class="table_list_row2" align="center">
				<td>${list4.FILENAME}</td>
				<td><a target="_blank" href="${list4.FILEURL}">[下载]</a></td>
			</tr>
		</c:forEach>
	</table>
	<table width=100% border="0" align="center" cellpadding="1" cellspacing="1" class="table_query">
		<tr>
			<td colspan="4" align="center">
				<input type="hidden" name="campaignId" id="campaignId" value="${map2.CAMPAIGN_ID}"/>
				<input type="hidden" name="delDlrIds" id="delDlrIds" value=""/>
				<input type="hidden" name="delAttachs" id="delAttachs" value=""/>
				<input type="hidden" name="dealerIds" id="dealerIds" value=""/>
				<input type="button" class="cssbutton" name="saveBtn" onClick="saveModify();" value="保存"/>
				<input type="button" class="cssbutton" name="assignBtn" onClick="toAssign();" value="下发"/>
				<input type="button" class="cssbutton" name="backBtn" onClick="history.back();" value="返回"/>
			</td>
		</tr>
	</table>
</form>
<script type="text/javascript">
	//页面初始化
	function doInit(){
		totalPrice();
	}
	//费用合计
	function totalPrice(){
		var planCost1 = document.getElementsByName("planCost1");
		var realCost1 = document.getElementsByName("realCost1");
		var itemCost1 = document.getElementsByName("itemCost1");
		var planCost2 = document.getElementsByName("planCost2");
		var realCost2 = document.getElementsByName("realCost2");
		var itemCost2 = document.getElementsByName("itemCost2");
		var totalPrice1 = 0;
		var totalPrice2 = 0;
		var totalPrice3 = 0;
		var totalPrice4 = 0;
		var totalPrice5 = 0;
		var totalPrice6 = 0;
		for (var i=0; i<planCost1.length; i++){  
			totalPrice1 += Number(planCost1[i].value);  
			totalPrice2 += Number(realCost1[i].value);
			totalPrice3 += Number(itemCost1[i].value);
		}
		for (var i=0; i<planCost2.length; i++){  
			totalPrice4 += Number(planCost2[i].value);
			totalPrice5 += Number(realCost2[i].value);
			totalPrice6 += Number(itemCost2[i].value); 
		}
		document.getElementById("totalPrice1").innerText = amountFormat(totalPrice1);
		document.getElementById("totalPrice2").innerText = amountFormat(totalPrice2);
		document.getElementById("totalPrice3").innerText = amountFormat(totalPrice3);
		document.getElementById("totalPrice4").innerText = amountFormat(totalPrice4);
		document.getElementById("totalPrice5").innerText = amountFormat(totalPrice5);
		document.getElementById("totalPrice6").innerText = amountFormat(totalPrice6);
	}
	//返回方法
	function toBack(){
		$('fm').action='<%=request.getContextPath()%>/sales/marketmanage/planissued/ActivitiesSpacePlanMake/activitiesPlanMakeInit.do';
		$('fm').submit();
	}
	 //初始化
    function doInit(){
   		loadcalendar();  //初始化时间控件
	}
	//选择活动车型
	function toSelect(){
		OpenHtmlWindow('<%=contextPath%>/sales/marketmanage/planissued/ActivitiesPlanMake/queryModelInit.do',800,450);
	}
	
	//保存更新后的信息
	function saveModify(){
		var length = document.getElementById("dealerTable").rows.length;
	    if(length==0){
	    	MyAlert("请选择活动经销商范围");
	    	return;
	    }else{
	      if(submitForm('fm')){
	    	MyConfirm("是否确认保存？", confirmUpdate);
	       }
	    }
	}
	
	//确认更新
	function confirmUpdate(){
	   
		makeNomalFormCall('<%=contextPath%>/sales/marketmanage/planissued/ActivitiesSpacePlanMake/updateExecutePlan.json',showResult,'fm');
	}
	
	
	//回调函数
	function showResult(json){
		if(json.returnValue == '1'){
			window.parent.MyAlert("操作成功！");
			$('fm').action='<%=contextPath%>/sales/marketmanage/planissued/ActivitiesSpacePlanMake/activitiesPlanMakeInit.do';
			$('fm').submit();
		}else{
			MyAlert("操作失败！请联系系统管理员！");
		}
	}
	
	
	//清除功能
	function toClear(value1,value2){
		document.getElementById(value1).value="";
		document.getElementById(value2).value="";
	}
	
	
	//选择经销商
	function toAddDealer(){
		var    areaId=document.getElementById("areaId").value;
		var campaignId = document.getElementById("campaignId").value;
		OpenHtmlWindow('<%=contextPath%>/sales/marketmanage/planissued/ActivitiesPlanMake/queryDealerInit.do?&campaignId='+campaignId+'&areaId='+areaId,800,450);
	}
	
	
	//接收子窗口传回的值
	function getDealerInfo(dealerIds,str){
	 	var ids = document.getElementById("dealerIds").value = dealerIds;
	 	var arrIDs = ids.split(",");
	 	var arrInfo = str.split("@");
	 	for(var i = 0;i<arrIDs.length;i++){
	 		var arr = arrInfo[i].split(",");
	 		var code = arr[0];
	 		var name = arr[1];
	 		var org = arr[2];
	 		var prn = arr[3];
	 		if(prn==null||prn.indexOf("null")==0){
	 			prn = "";
	 		}
	 		var trNode = document.getElementById(arrIDs[i]);
	 		if(trNode!=null){
	 			var trID = trNode.id;
	 			if(trID.indexOf(arrIDs[i])==0){
            		MyAlert("该经销商已存在，请选择其他经销商！");
		    		return ;
		    	}else{
		    		addRow(arrIDs[i],name,code,org,prn);
		    	}
	 		}else{
	 			addRow(arrIDs[i],name,code,org,prn);
	 		}
	 		
	 	}
	}
	
	//根据取到的值动态生成表格
	function addRow(dealerId,dealerShortname,dealerCode,orgName,province){
	    var addTable = document.getElementById("dealerTable");
		var rows = addTable.rows;
		var length = rows.length;
		var insertRow = addTable.insertRow(length);
		insertRow.className = "table_list_row";
		insertRow.id = dealerId;
		insertRow.insertCell(0);
		insertRow.insertCell(1);
		insertRow.insertCell(2);
		insertRow.insertCell(3);
		insertRow.insertCell(4);
		insertRow.insertCell(5);
		addTable.rows[length].cells[0].innerHTML =  "<td nowrap='nowrap'>"+orgName+"</td>";
		addTable.rows[length].cells[1].innerHTML =  "<td nowrap='nowrap'>"+province+"</td>";
		addTable.rows[length].cells[2].innerHTML =  "<td nowrap='nowrap'>"+dealerCode+"<input type='hidden' name='dlrIds' value='"+dealerId+"'/></td>";
		addTable.rows[length].cells[3].innerHTML =  "<td nowrap='nowrap'>"+dealerShortname+"</td>";
		addTable.rows[length].cells[4].innerHTML =  "<td nowrap='nowrap'><input type='button' class='normal_btn' onclick='delRow("+dealerId+")' value='删除'/></td>";
		addTable.rows[length].cells[5].innerHTML =  "<td id='0'></td>";
		
	}
	
	
	// 删除表格
	function delRow(dealerId){
	    var tbodyNode=document.getElementById("dealerTable");
	    var trNode=document.getElementById(dealerId);
	    var flag = trNode.lastChild.id;
	    var delDlrIds = document.getElementById("delDlrIds").value;
	    
	    if(flag.indexOf("1")==0){
	    	delDlrIds = delDlrIds+","+dealerId;
	    	document.getElementById("delDlrIds").value = delDlrIds;
	        
	    }
		tbodyNode.removeChild(trNode);
		
    }
	
	
	//删除附件
	function delAttach(value){
  		var fjId = value;
  		var delAttachs = document.getElementById("delAttachs").value;
		document.getElementById(value).style.display="none";
		delAttachs=delAttachs+","+fjId;
		document.getElementById("delAttachs").value=delAttachs;
	}
	
	
	//下发
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
	  //删除表
    function delAllRows(){
    	 var tbodyNode=document.getElementById("dealerTable");
        var rowscount=tbodyNode.rows.length;
    	for(i=rowscount-1;i >= 0; i--){
        	tbodyNode.deleteRow(i);
    		  }
        }
	
	//确认下发
	function confirmAssign(){
		makeNomalFormCall('<%=contextPath%>/sales/marketmanage/planissued/ActivitiesSpacePlanMake/assignExecutePlan.json',showResult,'fm');
	}
	
</script>
</body>
</html>