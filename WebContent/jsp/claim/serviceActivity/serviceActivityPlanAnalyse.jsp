<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c"%>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />

<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>服务活动计划分析</title>
<% String contextPath = request.getContextPath(); %>
<script type="text/javascript">
function doInit()
	{
	   loadcalendar();
	}
</script>

<% 
//List<TtAsActivityPO> list=(List<TtAsActivityPO>)request.getAttribute("list"); 
%>
</head>

<body>
	<div class="navigation">
			<img src="<%=contextPath %>/img/nav.gif" />&nbsp;当前位置： 售后服务管理&gt;服务活动管理&gt;服务活动计划分析
	</div>
  <form method="post" name = "fm" id="fm">
	  	  <table class="table_query">
		    <tr>
			      <td class="table_query_2Col_label_6Letter" >经销商代码：</td>
			      <td align="left">
					        <input class="middle_txt" id="dealerCode" style="cursor: pointer;" name="dealerCode" type="text" />
			                <input class="mini_btn" type="button" value="&hellip;" onclick="showOrgDealer('dealerCode','','true','',true)" />
			                <input name="button2" type="button" class="normal_btn" onclick="clr();" value="清除"/>
			      </td>
			      <td class="table_query_2Col_label_6Letter" >经销商名称：</td>
			      <td align="left">
					        <input class="middle_txt" id="dealerName" style="cursor: pointer;" name="dealerName" type="text" />
			      </td>
		    </tr>
		    <tr>
			     <td class="table_query_2Col_label_5Letter" >活动名称：</td>
			      <td colspan="2" align="left">
			          <input type="text"   class="middle_txt"  style="cursor: pointer;" id="activity_name" name="activity_name"  />
			          <input type="hidden" id="activityId" name="activityId"/>	
				      <input type="button" class="mini_btn" value="..." onclick="openQueryName();"/>
				      <input type="button" class="normal_btn" value="清除" onclick="clrName();"/>
	              </td>
		    </tr>
			<tr>
			    <td height="25" colspan="5" align="center"> 
			 	   <input type="button" name="BtnQuery"  value="查询"  class="normal_btn" onclick="__extQuery__(1);"/>
			    </td>
			</tr>
		  </table>
<!--分页 begin -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
  <!--分页 end --> 
</form>
 <br/>
<!--页面列表 begin -->
<script type="text/javascript" >
	var myPage;
//查询路径
 var url = "<%=contextPath%>/claim/serviceActivity/ServiceActivityPlanAnalyse/"
	          +"serviceActivityPlanAnalyseQuery.json";
				
	var title = null;

	var columns = [
		{header: "执行经销商代码", dataIndex: 'DEALER_CODE', align:'center'},
		{header: "执行经销商名称",dataIndex: 'DEALER_NAME' ,align:'center'},
		{header: "计划数量 ",dataIndex: 'BN' ,align:'center'},
		{header: "完成计划内数量",dataIndex: 'CN' ,align:'center'},
		{header: "完成计划外数量",dataIndex: 'EN' ,align:'center'},
		{header: "完成率",dataIndex: 'PERS' ,align:'center' ,renderer:completeRates} 
		      ];
	//完成率
	function completeRates(value,metaDate,record){
    var str=value.toString();
    var per="";
      if(""!=str){
        per=str.substring(0,6)+" % ";
       }
    return per;
  }
  	//清除方法
 function clr() {
	document.getElementById('dealerCode').value = "";
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
<!--页面列表 end -->
</body>
</html>