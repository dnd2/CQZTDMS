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
<title>维修回访更新(经销商端)</title>
<script type="text/javascript">
var globalContextPath ='<%=(request.getContextPath())%>';
var g_webAppName = '<%=(request.getContextPath())%>';   
var g_webAppImagePath = "<%=(request.getContextPath())%>"+"/images";

function doInit() {
   		loadcalendar();
   		if(${map.NO_VISIT_REASON != 8050002 }){
   		   $('table_NO_VISIT_REASON').style.display = 'none';
   		   $('table_SATISFIED').style.display = 'none'; 	
   		}
   		if(${map.SATISFIED != 10041002}){
   		    $('table_SATISFIED').style.display = 'none';
   		}
	}
//格式化时间为YYYY-MM-DD
 function formatDate(value,meta,record) {
		if (value==""||value==null) {
			return "";
		}else {
			return value.substr(0,10);
		}
	}
function doCusChange(value){
	var SATISFIED = $("SATISFIED").value;
	if(SATISFIED == <%=Constant.IF_TYPE_NO%>){
		$("table_SATISFIED").style.display = '';
	}
	else if(SATISFIED == <%=Constant.IF_TYPE_YES%>){
		$("table_SATISFIED").style.display = 'none';
		$('NO_SATISFIED').value = '';
        $('NO_SATISFIED_REASON').vlaue='';
	}	
}
function changeNoVisitReason(value){
    if(value == <%=Constant.NO_VISIT_REASON_2%> ){
       $('table_NO_VISIT_REASON').style.display = '';
    }else {
       $('table_NO_VISIT_REASON').style.display = 'none';
       $("table_SATISFIED").style.display = 'none';
       $('SATISFIED').value = '';
       $('IS_RECOMMEND').value = '';
       $('NO_SATISFIED').value = '';
       $('NO_SATISFIED_REASON').value = '';
    }
}
//格式化时间为YYYY-MM-DD
 function formatDate(value) {
		if (value==""||value==null) {
			return "";
		}else {
			return document.write(value.substr(0,10));
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
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 信息反馈管理 &gt; 信息反馈提报 &gt; 维修回访客户修改(经销商端)</div>
<form id="fm" name="fm" method="post">
<input id="CUS_ID" name="CUS_ID" type="hidden" value="${map.CUS_ID }"/>
<table class="table_query" border="0"> 
	<tr>
	  <td class="table_query_2Col_label_4Letter" nowrap="nowrap">客户名称：</td>
		<td class="table_query_2Col_input" nowrap="nowrap">
			<input class="middle_txt" type="text" datatype="0,is_letter_cn,10" maxlength="10"   id="CUSTOMER_NAME" name="CUSTOMER_NAME" value="${map.CUSTOMER_NAME}"/>
		</td>	 
	  <td class="table_query_2Col_label_4Letter">回访日期：</td>
				<td>
					<label id="div"></label>
					<input name="CON_END_DAY" type="text" id="CON_END_DAY" class="short_txt" value="${map.VISIT_DATE}"
					datatype="0,is_date,10" hasbtn="true"
					callFunction="showcalendar(event, 'CON_END_DAY', false);" />
		 </td>	 
	</tr>
	<tr>
		  <td class="table_query_2Col_label_4Letter" nowrap="nowrap">联系电话：</td>
		  <td class="table_query_2Col_input" nowrap="nowrap">
		  <input class="middle_txt"   maxlength="30" type="text" id="PHONE" name="PHONE" value="${map.PHONE }"/>
		  </td> 
		  <td class="table_query_2Col_label_4Letter" nowrap="nowrap">三包内外：</td>
		  <td class="table_query_2Col_input" nowrap="nowrap">
		  <script type="text/javascript">genSelBoxExp("THREE_GUARANTEES",<%=Constant.IF_TYPE%>,"<c:out value="${map.THREE_GUARANTEES}"/>",false,"short_sel","","false",'');</script>  
        </td>  
	</tr>
	<tr>
	  <td class="table_query_2Col_label_4Letter" nowrap="nowrap">选择车型：</td>
		<td class="table_query_2Col_input" nowrap="nowrap">
		  <select id='GROUP_CODE' name='GROUP_CODE'>
		     <option value="0">--请选择--</option> 
		       <c:forEach var="s" items="${list}">
              	 <c:choose>
              	    <c:when test="${s.groupId == map.GROUP_CODE}">
              	       <option value="${s.groupId}" selected="selected">${s.groupName}</option>
              	     </c:when>
              	     <c:otherwise>
              	         <option value="${s.groupId}" >${s.groupName}</option>     
              	     </c:otherwise>
              	 </c:choose>
              	</c:forEach>
		  </select>
        </td>
       <td class="table_query_2Col_label_4Letter" nowrap="nowrap">车牌号码：</td>
		<td class="table_query_2Col_input" nowrap="nowrap">
           		  <input class="middle_txt"   maxlength="30" type="text" id="LICENSE_NO" name="LICENSE_NO" value="${map.LICENSE_NO }"/>   
        </td>  
     </tr>
	<tr>
	 <td class="table_query_2Col_label_4Letter" nowrap="nowrap">维修项目：</td>
		<td class="table_query_2Col_input" nowrap="nowrap">
		   <script type="text/javascript">genSelBoxExp("REPAIR_ITEM",<%=Constant.REPAIR_ITEM%>,"<c:out value="${map.REPAIR_ITEM }"/>",true,"short_sel","","false",'');</script>
        </td>
      <td class="table_query_2Col_label_4Letter" nowrap="nowrap">回访结果：</td>
	  <td class="table_query_2Col_input" nowrap="nowrap">
	   <script type="text/javascript">genSelBoxExp("NO_VISIT_REASON",<%=Constant.NO_VISIT_REASON%>,"<c:out value="${map.NO_VISIT_REASON}"/>",true,"short_sel","onchange='changeNoVisitReason(this.value)'","false",'');</script>
	  </td>
	</tr>
</table>
<table id="table_NO_VISIT_REASON" class="table_query" border="0">
     <tr>
	   <td class="table_query_2Col_label_4Letter" nowrap="nowrap">是否满意：</td>
		<td class="table_query_2Col_input" nowrap="nowrap">
            <script type="text/javascript">genSelBoxExp("SATISFIED",<%=Constant.IF_TYPE%>,"<c:out value="${map.SATISFIED}"/>",true,"short_sel","onchange='doCusChange(this.value)'","false",'');</script>
        </td>
         <td class="table_query_2Col_label_4Letter" nowrap="nowrap">是否推荐:</td>
		<td class="table_query_2Col_input" nowrap="nowrap">
             <script type="text/javascript">genSelBoxExp("IS_RECOMMEND",<%=Constant.IF_TYPE%>,"<c:out value="${map.IS_RECOMMEND}"/>",true,"short_sel","","false",'');</script>
        </td> 
	</tr>
	
</table>
<table id="table_SATISFIED" class="table_query" border="0">
<tr>
	  <td class="table_query_2Col_label_4Letter" nowrap="nowrap">不满意项：</td>
		<td class="table_query_2Col_input" nowrap="nowrap">
		  <script type="text/javascript">genSelBoxExp("NO_SATISFIED",<%=Constant.NO_SATISFIED%>,"<c:out value="${map.NO_SATISFIED}"/>",true,"short_sel","","false",'');</script>	 	  
        </td> 
        <td class="table_query_2Col_label_5Letter" nowrap="nowrap">不满意内容:</td>
		<td class="table_query_2Col_input" nowrap="nowrap">
		 	  <textarea name='NO_SATISFIED_REASON' id='NO_SATISFIED_REASON'	  rows='2' cols='28'>${map.NO_SATISFIED_REASON }</textarea> 	 	  	 
        </td>
	</tr>
</table>
<table class="table_query" border="0">
   <tr>
	  <td class="table_edit_2Col_label_4Letter">备注：</td>
	  <td colspan="3" class="tbwhite">&nbsp;&nbsp;&nbsp;&nbsp;
			<textarea name='REMARK' id='REMARK'	  rows='2' cols='28'>${map.REMARK }</textarea>
		</td>
	</tr>
</table>	
<table border="0" cellpadding="0" cellspacing="0" width="100%">  
	<tr >
	 <td align="center">
		<input name="button2" type="button" class="normal_btn" onclick="save()" value="更 新"/>&nbsp;
		<input name="button" type="button" style="width:60px;height:16px;line-height:10px;background-color:#EEF0FC;border:1px solid #5E7692;color:#1E3988;" class="normal_btn" onclick="toGoBack()" value="取 消"/>
	 </td>
	</tr>
</table>
</form>
</body>
</html>
<script>
function save(){
	MyConfirm("是否确认修改？",update,'');
}
function update(){
  makeNomalFormCall('<%=contextPath%>/sysmng/usemng/RepairCustomerVisit/modfi.json',updateCall,'fm','');  
}
function updateCall(json){
  if(json.flag!= null && json.flag== true) {
		MyAlert("修改成功！");
		toGoBack();
	} else {
		MyAlert("修改成功！请联系管理员！");
	}
}

function toGoBack() {
	window.location = "<%=contextPath%>/sysmng/usemng/RepairCustomerVisit/queryInit.do";
}

</script>