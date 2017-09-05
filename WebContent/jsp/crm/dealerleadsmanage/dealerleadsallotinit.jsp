<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.Constant"%>
<%@taglib uri="/jstl/cout" prefix="c"%>
<%
	String contextPath = request.getContextPath();
    int yes = Constant.IF_TYPE_YES;
    String dealerId = request.getParameter("dealerId");
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>

<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<script type="text/javascript">
	function doInit(){
		loadcalendar();   //初始化时间控件
	}
	function txtClr(valueId) {
		document.getElementById(valueId).value = '' ;
	}
</script>

<title>销售线索分派</title>
</head>
<body onunload='javascript:destoryPrototype();' onload="__extQuery__(1);"> 
<div class="wbox">
	<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置>潜客管理>经销商线索管理>线索分派</div>
	<form id="fm" name="fm" method="post">
		<input type="hidden" name="curPage" id="curPage" value="1" />
		<input type="hidden" id="dlrId" name="dlrId" value="" />
		<input type="hidden" name="leadsCode" id="leadsCode" value="" />
		<input type="hidden" name="leadsCode2" id="leadsCode2" value="" />
		<input type="hidden" name="leadsGroup2" id="leadsGroup2" value="" /> 
		<input type="hidden" name="leadsCodeGroup2" id="leadsCodeGroup2" value="" />   
		<table class="table_query" width="95%" align="center">
		<c:if test="${returnValue==2}">
		<tr>
			<td  colspan="6">
			</td>
		</tr>
		</c:if>
		    <tr>
		      <td align="right" width="10%">客户姓名：</td>
		      <td> 
		      	<input id="customer_name" name="customer_name" type="text" class="middle_txt" datatype="1,is_textarea,30" size="20" maxlength="60" />
		      </td>
		      <td align="right" width="15%">联系电话：</td>
		      <td width="25%">
		        	<input id="telephone" name="telephone" type="text" class="middle_txt" datatype="1,is_textarea,30" size="20" maxlength="60" />
		      </td>
		      <td width="8%" align="right">导入时间从：</td>
		      <td width="22%" >
					<div align="left">
	            		<input name="startDate" id="startDate" value="" type="text" class="short_txt" datatype="1,is_date,10" group="startDate,endDate" hasbtn="true" callFunction="showcalendar(event, 'startDate', false);" />
	            		&nbsp;到&nbsp;
	            		<input name="endDate" id="endDate" value="" type="text" class="short_txt" datatype="1,is_date,10" group="startDate,endDate" hasbtn="true" callFunction="showcalendar(event, 'endDate', false);" />
            		</div>	
			  </td>
		    </tr>
		    <tr>
		      <td colspan="6" align="center"><input name="queryBtn" type="button" class="normal_btn" onclick="__extQuery__(1);" value="查询" /></td>
		    </tr>
  	</table>
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
	</form>
	
</div>
<script type="text/javascript" > 
	
	var myPage;
	var url = "<%=contextPath%>/crm/dealerleadsmanage/DlrLeadsManage/dlrAllotQuery.json";
				
	var title = null;
	var controlCols = "<input type=\"checkbox\" name=\"conAll\" onclick=\"selectAll(this,'leadsGroup')\"/>全选&nbsp;&nbsp;"
		+ "<input type=\"button\" value=\"批量分派\" class=\"normal_btn\" onclick=\"batchAuditingConfirm();\"/>";
	var columns = [
				{header: "序号", renderer:getIndex, align:'center'},
				{header: controlCols,align:'center',renderer:createCheckbox},
				{header: "线索编码", dataIndex: 'LEADS_CODE', align:'center'},
				{header: "线索来源", dataIndex: 'LEADS_ORIGIN', align:'center'},
				{header: "导入时间", dataIndex: 'CREATE_DATE', align:'center'},
				{header: "客户姓名", dataIndex: 'CUSTOMER_NAME', align:'center'},
				{header: "联系电话", dataIndex: 'TELEPHONE', align:'center'},
				{header: "意向车型", dataIndex: 'SERIES_NAME', align:'center'},
				{header: "销售顾问", dataIndex: 'ADVISER', align:'center'},
				{header: "备注", dataIndex: 'REMARK', align:'center'},
				{header: "操作",sortable: false,dataIndex: 'LEADS_ALLOT_ID',renderer:myLink}
		      ];
	function myLink(value,meta,record){
		
		return String.format("<a href=\"#\" id=\"LEADS_ALLOT_ID\" name=\"LEADS_ALLOT_ID\" onclick='checkDetailUrl(\""+record.data.LEADS_ALLOT_ID+"\",\""+record.data.LEADS_CODE+"\")'>[分派]</a>");
	}   
	function checkDetailUrl(leadsAllotId,leadsCode){
		document.getElementById("leadsCode2").value = leadsAllotId;
		document.getElementById("leadsCode").value = leadsCode;
		showAdviserByLeadsnew('dealerCode','','true',leadsAllotId,'','${dealerId}');
		//window.location.href="<%=contextPath%>/crm/dealerleadsmanage/DlrLeadsManage/dlrLeadsAllotByDlrInit.do?leadsAllotId="+leadsAllotId;
	}
	
	/**
	 * 通用选择顾问界面(销售线索分派用)
	 * inputCode : 回填页面经销商Code域id
	 * inputId   : 回填页面经销商Id域id 
	 * orgId     : 组织id，如有，则表示根据orgId过滤下级所有经销商，否则不过滤
	 */
	function showAdviserByLeadsnew(inputCode ,inputId , isMulti, leadsCode, leadsGroup,dealerId, orgId, isAllLevel, isAllArea,isDealerType,inputName)
	{
		if(!inputCode){ inputCode = null;}
		if(!inputId){ inputId = null;}
		if(!isMulti){ isMulti = null;}
		if(!orgId || orgId == 'false' || orgId == 'true'){ orgId = null;}
		if(!isAllLevel){ isAllLevel = null;}
		if(!isAllArea){ isAllArea = null;}
		if(!isDealerType){ isDealerType = null;}
		if(!inputName){ inputName = null;}
		OpenHtmlWindow(g_webAppName+'/dialog/showAdviserByLeads.jsp?INPUTCODE='+inputCode+"&INPUTID="+inputId+"&ISMULTI="+isMulti+"&ORGID="+orgId+"&ISALLLEVEL="+isAllLevel+"&ISALLAREA="+isAllArea+"&isDealerType="+isDealerType+"&inputName="+inputName+"&leadsCode="+leadsCode+"&leadsGroup="+leadsGroup+"&dealerId="+dealerId,450,200);
	}
	 
	//生成批量审核使用的选择框
	function createCheckbox(value,meta,record){
  	  		//var claimStatus = record.data.STATUS;
  	  		//var authcodeval = record.data.AUTH_CODE;
			resultStr = String.format("<input type=\"checkbox\" name=\"leadsGroup\" value=\""+record.data.LEADS_ALLOT_ID+"\"/><input type=\"hidden\" name=\"leadsCodez\" value=\""+record.data.LEADS_CODE+"\"/>");
		return resultStr;
	}
	 //批量审批
	function batchAuditingConfirm(){
		var selectArray = document.getElementsByName('leadsGroup');
		var selectArray2 = document.getElementsByName('leadsCodez');
		if(isSelectCheckBox(selectArray)){
			//MyConfirm("是否批量分派?",batchAuditing,[selectArray]);
			//数组转为字符串
			var leadsGroup = '';
			var leadsCodeGroup='';
			for(var i=0;i<selectArray.length;i++) {
				if(selectArray[i].checked) {
					leadsGroup = leadsGroup + selectArray[i].value + ",";
					leadsCodeGroup = leadsCodeGroup + selectArray2[i].value + ",";
				}
			}
			var leadsCode = selectArray2[0].value;
			leadsCodeGroup=leadsCodeGroup.substring(0,leadsCodeGroup.length-1);
			leadsGroup=leadsGroup.substring(0,leadsGroup.length-1);
			document.getElementById("leadsGroup2").value = leadsGroup;
			document.getElementById("leadsCode").value = leadsCode;
			document.getElementById("leadsCodeGroup2").value = leadsCodeGroup;
			showAdviserByLeads('dealerCode','','true','',leadsGroup,'${dealerId}');
		}else{
			MyAlert("请选择要分派的销售线索！");
		}
	}
	function batchAuditing(selectArray)
	{
		//数组转为字符串
		var leadsGroup = '';
		for(var i=0;i<selectArray.length;i++) {
			if(selectArray[i].checked) {
				leadsGroup = leadsGroup + selectArray[i].value + ",";
			}
		}
		leadsGroup=leadsGroup.substring(0,leadsGroup.length-1);
		window.location.href="<%=contextPath%>/crm/dealerleadsmanage/DlrLeadsManage/dlrLeadsAllotByDlrInit.do?leadsGroup="+leadsGroup; 
	}
	//检测是否选择了checkBox
	function isSelectCheckBox(cbArray){
		if(cbArray!=null && cbArray.length>0){
			for(var i=0;i<cbArray.length;i++){
				if(cbArray[i].checked)
					return true;
			}
		}else{
			return false;
		}
		return false;
	}
	function returnFunction(code){
		MyAlert("code=="+code);
		var leadsAllotId = document.getElementById("leadsCode2").value;
		var leadsCode = document.getElementById("leadsCode").value;
		var leadsGroup = document.getElementById("leadsGroup2").value;
		var leadsCodeGroup= document.getElementById("leadsCodeGroup2").value;
		if(leadsAllotId==null||leadsAllotId==''){
			$('fm').method = "post" ;
			$('fm').action= "<%=contextPath%>/crm/dealerleadsmanage/DlrLeadsManage/dlrLeadsAllotByDlr.do?leadsAllotId=&leadsGroup="+leadsGroup+"&adviserId="+code+"&leadsCode="+leadsCode+"&leadsCodeGroup="+leadsCodeGroup;
			$('fm').submit();
		} else {
			$('fm').method = "post" ;
			$('fm').action= "<%=contextPath%>/crm/dealerleadsmanage/DlrLeadsManage/dlrLeadsAllotByDlr.do?leadsAllotId="+leadsAllotId+"&leadsGroup=&adviserId="+code+"&leadsCode="+leadsCode;
			$('fm').submit();
		}
		MyAlert('执行成功!');
	}
	genLocSel('dPro','dCity','dArea','','',''); // 加载省份城市和区县
	_setDate_("reportStartDate", "reportEndDate", "1", "0") ;
</SCRIPT>
</script>    
</body>
</html>