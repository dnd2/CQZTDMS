<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.Constant" %>
<%@ page import="com.infodms.dms.bean.AclUserBean" %>

<%
	String contextPath = request.getContextPath();
%>
<%@taglib uri="/jstl/cout" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>潜在客户管理(经销商端)</title>
<style>
.img {
	border: none
}
</style>
	<script language="JavaScript">
    var myPage;
    var url = "<%=contextPath%>/sales/customerInfoManage/SalesDailyReport/SeachDailyInit.json";		
	var title = null;
	var columns = [
				{id:'id',header: "经销商代码", width:'10%', dataIndex: 'DEALER_CODE'},
				{header: "经销商名称", width:'10%', dataIndex: 'DEALER_NAME'},
				{header: "是否提报", width:'10%', dataIndex: 'STATUS',renderer:getItemValue},
				{header: "创建时间", width:'10%', dataIndex: 'CREATE_DATE'},
			    {header: "操作",width:'10%',dataIndex: 'CONTENT_ID',renderer:myLink ,align:'center'}			
	      ];

  	//修改的超链接设置
function myLink(value,meta,record){
			var dutyType=document.getElementById("dutyType").value;
			if(record.data.STATUS==<%=Constant.DAILY_STATUS_CONFIRM%>){
				return String.format(
					"<a href=\"#\" onclick='dailyQuery(\""+value+"\")'>[查看]</a>"
					)
			}else{
				if(dutyType==<%=Constant.DUTY_TYPE_SMALLREGION%>){
				 return String.format(
					"<a href=\"#\" onclick='dailyQuery(\""+value+"\")'>[查看]</a><a href=\"#\" onclick='commitDaily(\""+value+"\")'>[提交]</a>"
					)	 
				}else if(dutyType==<%=Constant.DUTY_TYPE_DEALER%>){
					 return String.format(
					"<a href=\"#\" onclick='dailyQuery(\""+value+"\")'>[查看]</a><a href=\"#\" onclick='updateDaily(\""+value+"\")'>[修改]</a><a href=\"#\" onclick='deleteDaily(\""+value+"\")'>[删除]</a>"
					)
				}else{
					return String.format(
					"<a href=\"#\" onclick='dailyQuery(\""+value+"\")'>[查看]</a>"
					)
				}
				
			} 
    }
function updateDaily(contentId){
    location.href='<%=contextPath%>/sales/customerInfoManage/SalesDailyReport/dailyReportShowUpdate.do?contentId='+contentId;
 }	  
function commitDaily(value){
	MyConfirm("是否确认提交？",commitReport,[value]);
}
function commitReport(value){
	makeNomalFormCall('<%=contextPath%>/sales/customerInfoManage/SalesDailyReport/dailyCommit.json?contentId='+value,showResult,'fm','');
}

function deleteDaily(value){
	MyConfirm("是否确认删除？",deleteReport,[value]);
}

function deleteReport(value){
	makeNomalFormCall('<%=contextPath%>/sales/customerInfoManage/SalesDailyReport/dailyDelete.json?contentId='+value,showResult,'fm','');
}

function dailyQuery(contentId){
	//var dailyDate=document.getElementById("CON_APPLY_DATE_START_ID").value;
	location.href='<%=contextPath%>/sales/customerInfoManage/SalesDailyReport/dailyReportQuery.do?contentId='+contentId;
}
function dropCall(json) {
	if(json.flag!= null && json.flag== true) {
		MyAlert(" 删除成功！");
		__extQuery__(1);
	} else {
		MyAlert("删除失败！请联系管理员！");
	}
}	            
 function doInit() {
   		loadcalendar();
   		$("queryButton").click();
	}
//格式化时间为YYYY-MM-DD
 function formatDate(value,meta,record) {
		if (value==""||value==null) {
			return "";
		}else {
			return value.substr(0,10);
		}
	}
function requery() {
	$('customer_Name').value="";
	$('init_Level').value="";
	$('intent_Level').value="";
}	
	
function showResult(json){
		if(json.returnValue == '1'){
			window.parent.MyAlert("操作成功！");
			fm.action='<%=request.getContextPath()%>/sales/customerInfoManage/SalesDailyReport/DailyReportInit.do';
			fm.submit();
		}else{
			MyAlert("操作失败！请联系系统管理员！");
		}
	}
	
function clrTxt(txtId){
    document.getElementById(txtId).value = "";
   }
	
</script>
</head>

<body onload='doInit();'>
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 整车销售 &gt; 客户信息管理 &gt; 日报表(经销商端)</div>
<form method="post" name = "fm" id="fm">
<input type="hidden" name="curPage" id="curPage" value="1" />
<input type="hidden" name="dutyType" id="dutyType" value="${dutyType}" />
<input id="COMPANY_ID" name="COMPANY_ID" type="hidden"/>
<table class="table_query" border="0">
	<tr align="center">		
				
				<td align="right" width="12%">请选择提报状态： </td>
				<td align="left" width="10%">
					<select name="dailyAudit" class="short_sel" id="areaId">
    					<option value="0">---请选择---</option>
									<option value="<%=Constant.DAILY_STATUS_UNCONFIRM %>">未提交</option>
									<option value="<%=Constant.DAILY_STATUS_CONFIRM %>">已提交</option>
					</select>
				</td>
				
				<%
					AclUserBean user=(AclUserBean)request.getAttribute("logonUser");
					if(!user.getDutyType().equals(Constant.DUTY_TYPE_DEALER.toString())){
					%>
					<td align="right" width="10%">选择经销商：</td>
				<td colspan="1" width="25%" align="left">
				<input type="hidden" name="dealerId" size="15" value="" id="dealerId"/>
				<input type="text" class="middle_txt"  name="dealerCodes" size="15" value="" id="dealerCodes"/>
				<!--  <input name="button2" type="button" class="mini_btn" onclick="showOrgDealer('dealerCodes','','true');" value="..." />-->
				<c:if test="${dutyType==10431001}">
			      	<input class="mini_btn"  name="button2" type="button" onclick="showOrgDealer('dealerCodes','','true', '${orgId}')" value="..." />
			     </c:if>
			      <c:if test="${dutyType==10431002}">
			      	<input class="mini_btn"  name="button2" type="button" onclick="showOrgDealer3('dealerCodes','','true', '${orgId}')" value="..." />
			      </c:if>
			      <c:if test="${dutyType==10431003}">
			      	<input class="mini_btn"  name="button2" type="button" onclick="showOrgDealer3('dealerCodes','','true', '${orgId}')" value="..." />
			      </c:if>
			      <c:if test="${dutyType==10431004}">
			      	<input class="mini_btn"  name="button2" type="button" onclick="showOrgDealer('dealerCodes','','true', '${orgId}')" value="..." />
			      </c:if>
				<input class="normal_btn" type="button" value="清空" onclick="clrTxt('dealerCodes');"/>
			</td>
					<%
					}else{
				 %>
				 	<td width="30%"></td>
				 <%
				 		}
				  %>
				 
		 <td class="table_query_2Col_label_5Letter" align="right">提报时间：</td>
               <td  nowrap="nowrap" align="left">
              <input type="text" name="CON_APPLY_DATE_START" id="CON_APPLY_DATE_START_ID"  datatype="1,is_date,10" group="CON_APPLY_DATE_START_ID,CON_APPLY_DATE_END_ID" hasbtn="true" callFunction="showcalendar(event, 'CON_APPLY_DATE_START_ID', false);" value="${date}"/>
              至
  			<input type="text" name="CON_APPLY_DATE_END" id="CON_APPLY_DATE_END_ID"  datatype="1,is_date,10" group="CON_APPLY_DATE_START_ID,CON_APPLY_DATE_END_ID" hasbtn="true" callFunction="showcalendar(event, 'CON_APPLY_DATE_END_ID', false);" value="${date}"/>
  			</td>
	</tr>
	<tr>
		<td align="center" colspan="6">
            <input class="normal_btn" type="button" name="button1" value="查询" id="queryButton" onclick="__extQuery__(1);" />
            <%
					AclUserBean logonUser=(AclUserBean)request.getAttribute("logonUser");
					if(logonUser.getDutyType().equals(Constant.DUTY_TYPE_DEALER.toString())){
					%>			
			<input class="normal_btn" type="button" value="新 增" onclick="window.location.href='<%=contextPath%>/sales/customerInfoManage/SalesDailyReport/addDailyReportInit.do'" value="新 增" />
			<%
			}
			 %>
		</td>
	</tr>
</table>
  <!-- 查询条件 end -->
  <!--分页 begin -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
  <!--分页 end --> 
  <div id="loader" style='position:absolute;z-index:200;background:#FFCC00;padding:1px;top:4px;display:none;display: none;'></div>
</form>
</body>
</html>
