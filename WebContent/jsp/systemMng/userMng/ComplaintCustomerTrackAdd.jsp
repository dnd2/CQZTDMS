<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.Constant" %>
<%
	String contextPath = request.getContextPath();
%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>投诉跟踪新增(经销商端)</title>
<script type="text/javascript">
var globalContextPath ='<%=(request.getContextPath())%>';
var g_webAppName = '<%=(request.getContextPath())%>';   
var g_webAppImagePath = "<%=(request.getContextPath())%>"+"/images";

function doInit() {
   		loadcalendar();
	}
//格式化时间为YYYY-MM-DD
 function formatDate(value,meta,record) {
		if (value==""||value==null) {
			return "";
		}else {
			return value.substr(0,10);
		}
	}

</script>
<style>
.img {
	border: none
}
</style>
</head>

<body onload="doInit()">
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 信息反馈管理 &gt; 信息反馈提报 &gt; 投诉客户新增(经销商端)</div>
<form id="fm" name="fm" method="post">
<input id="COMPANY_ID" name="COMPANY_ID" type="hidden" value=""/>
<table class="table_query" border="0"> 
	<tr>
	  <td class="table_query_2Col_label_4Letter" nowrap="nowrap">客户名称：</td>
		<td class="table_query_2Col_input" nowrap="nowrap">
			<input class="middle_txt" type="text" datatype="0,is_letter_cn,100" maxlength="100"   id="CUSTOMER_NAME" name="CUSTOMER_NAME"/>
		</td>	 
	  <td class="table_query_2Col_label_4Letter">跟踪日期：</td>
				<td>
					<label id="div"></label>
					<input name="track_date" type="text" id="track_date" class="short_txt"
					datatype="0,is_date,10" hasbtn="true"
					callFunction="showcalendar(event, 'track_date', false);" />
		 </td>	 
	</tr>
	<tr>
		  <td class="table_query_2Col_label_4Letter" nowrap="nowrap">联系电话：</td>
		  <td class="table_query_2Col_input" nowrap="nowrap">
		  <input class="middle_txt"   maxlength="500" type="text" id="PHONE" name="PHONE" />
		  </td>  
	</tr>
	<tr>
	  <td class="table_query_2Col_label_4Letter" nowrap="nowrap">车型：</td>
		<td class="table_query_2Col_input" nowrap="nowrap">
		  <select id='GROUP_CODE' name='GROUP_CODE'>
		     <option value="0">--请选择--</option> 
		       <c:forEach var="s" items="${list}">
              	 <option value="${s.groupId}">${s.groupName}</option> 
              	</c:forEach>
		  </select>
        </td>
       <td class="table_query_2Col_label_4Letter" nowrap="nowrap">车牌号码：</td>
		<td class="table_query_2Col_input" nowrap="nowrap">
           		  <input class="middle_txt"   maxlength="500" type="text" id="LICENSE_NO" name="LICENSE_NO" />   
        </td>  
     </tr>
	<tr>
      <td class="table_query_2Col_label_4Letter" nowrap="nowrap">频次：</td>
	  <td class="table_query_2Col_input" nowrap="nowrap">
	    <select name="TIMES">
	      <option value="1">1次</option>
	      <option value="2">2次</option>
	      <option value="3">3次</option>
	    </select>
	  </td>
	</tr>
	<tr>
	   <td class="table_query_2Col_label_4Letter" nowrap="nowrap">抱怨记录：</td>
		<td class="table_query_2Col_input" nowrap="nowrap">
		  	<input class="middle_txt"   maxlength="100" type="text" id="COMPLAINT_RECORD" name="COMPLAINT_RECORD" />	   
        </td>
         <td class="table_query_2Col_label_3Letter" nowrap="nowrap">受理人:</td>
		<td class="table_query_2Col_input" nowrap="nowrap">
		  	<input class="middle_txt"   maxlength="100" type="text" id="DEAL_WITH" name="DEAL_WITH" />	 
        </td> 
	</tr>
	<tr>
	  <td class="table_query_2Col_label_4Letter" nowrap="nowrap">客户要求：</td>
		<td class="table_query_2Col_input" nowrap="nowrap">
		  	<input class="middle_txt"   maxlength="100" type="text" id="CUS_REQUEST" name="CUS_REQUEST" />	
		</td> 
	</tr>
	 <tr>
        <td class="table_query_2Col_label_4Letter" nowrap="nowrap">处理结果:</td>
		<td class="table_query_2Col_input" nowrap="nowrap">
		 	  <input class="middle_txt"   maxlength="100" type="text" id="RESULT" name="RESULT" />	 
        </td>
	</tr>
</table>
<table border="0" cellpadding="0" cellspacing="0" width="100%">  
	<tr >
	 <td align="center">
		<input name="button2" type="button" class="normal_btn" onclick="save()" value="保 存"/>&nbsp;
		<input name="button" type="button" style="width:60px;height:16px;line-height:10px;background-color:#EEF0FC;border:1px solid #5E7692;color:#1E3988;" class="normal_btn" onclick="toGoBack()" value="取 消"/>
	 </td>
	</tr>
</table>
</form>
<script language="javascript">
function save(){
	MyConfirm("是否确认新增？",insert,'');
}
function insert(){
  makeNomalFormCall('<%=contextPath%>/sysmng/usemng/ComplaintCustomerTrack/add.json',insertCall,'fm','');  
}
function insertCall(json){
  if(json.flag!= null && json.flag== true) {
		MyAlert("新增成功！");
		toGoBack();
	} else {
		MyAlert("新增失败！请联系管理员！");
	}
}

function toGoBack() {
	window.location = "<%=contextPath%>/sysmng/usemng/ComplaintCustomerTrack/queryInit.do";
}

</script>
</body>
</html>
