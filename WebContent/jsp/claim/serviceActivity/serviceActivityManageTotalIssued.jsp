<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib uri="/jstl/cout" prefix="c"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />

<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>服务活动计划下发</title>
<% 
//List<TtAsActivityPO> list=(List<TtAsActivityPO>)request.getAttribute("list");
%>
<% String contextPath = request.getContextPath(); %>

</head>

<body>
	<div class="navigation">
		<img src="<%=contextPath %>/img/nav.gif" />&nbsp;当前位置： 售后服务管理&gt;服务活动管理&gt;服务活动计划全量下发
	</div>
<form method="post" name="fm" id="fm">
	<table class="table_query">
	       <tr>
	              <td class="table_query_3Col_label_6Letter">经销商代码：</td>
	              <td align="left">
	                  <input type="hidden" id="dealerIds" name="dealerIds" value=""/>
		              <input class="middle_txt" id="dealerCode" style="cursor: pointer;" name="dealerCode" type="text" datatype="0,is_null,300" />
				      <input class="mini_btn" type="button" value="&hellip;" onclick="showOrgDealer('dealerCode','dealerIds','true','',true)" />
				      <input name="button2" type="button" class="normal_btn" onclick="clr();" value="清除"/>
  				</td>
  				<td class="table_query_3Col_label_6Letter">活动名称： </td>
	              <td align="left">
		            <input type="text"   class="middle_txt"  style="cursor: pointer;" id="activity_name" name="activity_name"  />
			          <input type="hidden" id="activityId" name="activityId"/>	
				      <input type="button" class="mini_btn" value="..." onclick="openQueryName();"/>
				      <input type="button" class="normal_btn" value="清除" onclick="clrName();"/>
			       </td>
            </tr>
            <tr>
				  <td colspan="11" align="center">
	                  <input type="button" name="BtnQuery"  value="全量下发"  class="long_btn" size="12" onclick="checkTotalIssued();">
	              </td>
		    </tr>
	</table>
<script type="text/javascript">
 //清除方法
 function clr() {
	document.getElementById('dealerCode').value = "";
  }
 //全量下发
   function checkTotalIssued(){
	         if(!submitForm('fm')) {
					return false;
				}
			 //disableBtn($("commitBtn"));//点击按钮后，按钮变成灰色不可用;
		     MyConfirm("是否确认全量下发？",TotalIssued);
   }
	function TotalIssued(){
			makeNomalFormCall("<%=contextPath%>/claim/serviceActivity/ServiceActivityManageTotalIssued/TotalIssued.json",showForwordValue,'fm','queryBtn');
		}
	//关闭子页面并刷新父页面
	function showForwordValue(json){
		if(json.returnValue == '1')
		{
			MyAlert("全量下发成功！");
			//__extQuery__(1);
		}else
		{   
			if(json.returnValue != '1'){
				MyAlert("全量下发失败---"+json.returnValue+"经销商已经下发！");
			}
		}
	}
 function openQueryName(){
	var url = "<%=request.getContextPath()%>/jsp/claim/serviceActivity/serviceActivityShowQuery.jsp?flag=1";
	OpenHtmlWindow(url,900,500);
	}
  function clrName(){
    document.getElementById('activity_name').value = '';
  }
  function showName(activity_id,activity_code,activity_name){
  //MyAlert('activity_id:'+activity_id+'|activity_code:'+activity_code+'|activity_name:'+activity_name);
    document.getElementById('activity_name').value = activity_name;
    document.getElementById('activityId').value = activity_id;
  }
</script>
</form>
<br/>
</body>
</html>