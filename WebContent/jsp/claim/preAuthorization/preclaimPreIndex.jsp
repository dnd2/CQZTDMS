<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.infodms.dms.common.Constant"%>
<%
	String contextPath = request.getContextPath();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<script>
	function doInit(){
   		loadcalendar();  //初始化时间控件
	}
</script>
<title>索赔预申请维护</title>
</head>
<body>
	<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 售后服务管理&gt;索赔预授权&gt;索赔预授权工单申请</div>
  	<form id="fm" name="fm">
  	  <input type="hidden" name="curPage" id="curPage" value="1" />
	<table class="table_query">
       <tr>
         <td class="table_query_2Col_label_4Letter" align="right">申请日期： </td>
         <td>
        		<input name="beginTime" id="t1" value="" type="text" class="short_txt" datatype="1,is_date,10" group="t1,t2" hasbtn="true" callFunction="showcalendar(event, 't1', false);"/>
        		&nbsp;至&nbsp;
        		<input name="endTime" id="t2" value="" type="text" class="short_txt" datatype="1,is_date,10" group="t1,t2" hasbtn="true" callFunction="showcalendar(event, 't2', false);"/>
		</td>       
         <td class="table_query_2Col_label_5Letter" align="right">预授权单号：</td>
         <td><input name="ORDER_ID" id="ORDER_ID" datatype="1,is_null,30"  type="text" class="middle_txt"/></td>

       </tr>
       <tr>
         <td class="table_query_2Col_label_4Letter" align="right">VIN：</td>
<%--         <td><input type="text" name="VIN" id="VIN" datatype="1,is_digit_letter,17"  class="middle_txt"/> </td>--%>
			 <td><textarea name="VIN" cols="18" rows="3" ></textarea></td>
	         <td class="table_query_2Col_label_5Letter">工单号：</td>
	         <td><input name="RO_NO" id="RO_NO" datatype="1,is_null,30"  type="text" class="middle_txt"/></td>
       </tr>
	   <tr>
           <td colspan="4"  align="center">
           <input class="normal_btn" type="button" name="button1" value="查询"  onclick="__extQuery__(1)"/>
		   <input class="normal_btn" type="button" name="button1" value="新增"  onclick="window.location.href='<%=contextPath%>/claim/preAuthorization/PreclaimPreMain/preclaimAddInit.do'"/></td>
      </tr>
  </table>
<div id="loader" style='position:absolute;z-index:200;background:#FFCC00;padding:1px;top:4px;display:none;display: none;'></div>    
</form>
  <!--分页 begin -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
  <!--分页 end --> 
<script type="text/javascript" >
var myPage;
	var url = "<%=request.getContextPath()%>/claim/preAuthorization/PreclaimPreMain/preclaimPreQuery.json?COMMAND=1";

	var title = null;
	
	var columns = [
				{header: "序号",sortable: false,dataIndex: 'ID',renderer:getIndex ,align:'center'},
				{header: "VIN",sortable: false,dataIndex: 'VIN',align:'center'},
				{header: "预授权单号",sortable: false,dataIndex: 'FO_NO',align:'center'},
				{header: "工单号",sortable: false,dataIndex: 'RO_NO',align:'center'},
				{header: "申请日期",sortable: false,dataIndex: 'APPROVAL_DATE',align:'center',renderer:getDate},
				{header: "申请人",sortable: false,dataIndex: 'APPROVAL_PERSON',align:'center'},
				{header: "申请数量",sortable: false,dataIndex: 'CNUM',align:'center'},
				{header: "状态",sortable: false,dataIndex: 'REPORT_STATUS',align:'center',renderer:getItemValue},
				{header: "操作",sortable: false,dataIndex: 'ID',renderer:myLink ,align:'center'}
		      ];

//设置超链接  begin      
	
	//操作的超链接设置
	function myLink(value,meta,record){
		var ss = record.data.REPORT_STATUS;
		if(ss == <%=Constant.PRE_AUTH_STATUS_01%>){
		    return String.format(
	         "<a href=\"<%=contextPath%>/claim/preAuthorization/PreclaimPreMain/preclaimUpdateInit.do?ID="
				+ value + "\">[修改]</a><a href=\"#\" onclick='subChecked(\""+value+"\")'>[上报]</a>");
		}else{
		    return String.format("<a href=\"#\" onclick='showDetail(\""+value+"\")'>[明细]</a>");			
		}
	}
	//日期格式化：
	function getDate(value,metaDate,record){
		return String.format(value.substring(0,10));
	}
		
	//详细页面
	function showDetail(value){
		OpenHtmlWindow('<%=contextPath%>/claim/preAuthorization/PreclaimPreMain/preclaimDetail.do?ID='+value,900,500);
	}	
	
//设置超链接 end


/*
  	上报
*/
function subChecked(value) {
   MyConfirm("是否确认上报？",sub,[value]);
}
//上报
function sub(value){
	makeNomalFormCall('<%=request.getContextPath()%>/claim/preAuthorization/PreclaimPreMain/preclaimCommit.json?ID='+value,submitBack,'fm','');
}
//上报回调方法：
function submitBack(redata) {
	if(redata.success != null && redata.success == "true") {
		MyAlert("上报成功！");
		__extQuery__(1);
	} else {
		MyAlert("上报失败！请联系管理员！");
	}
}
</script>
  </body>
</html>