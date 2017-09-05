<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java"  pageEncoding="UTF-8"%>
<%@page import="java.util.*"%>
<%@page import="com.infodms.dms.po.*"%>
<%@page import="java.util.HashMap"%>
<%@page import="com.infodms.dms.common.Constant"%>
<%@page import="com.infodms.dms.util.CommonUtils"%>
<%
	String contextPath = request.getContextPath();
	List<FsFileuploadPO> attachLs = request.getAttribute("lists") == null ? null : (LinkedList<FsFileuploadPO>)request.getAttribute("lists");
%>
<%@taglib uri="/jstl/cout" prefix="c" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>首页新闻</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
<script >
var __index = 1;
//初始化时间控件
function doInit(){
	loadcalendar();	
}
// 选择新闻接收人员
function getReceiveUser() {
	var viewNewsType = document.getElementById('viewNewsType').value;
	if(viewNewsType == '<%=Constant.VIEW_NEWS_type_2 %>') {
		OpenHtmlWindow(g_webAppName + '/claim/basicData/HomePageNews/oemUserChooseInit.do',1000,500);
	} else if(viewNewsType == '<%=Constant.VIEW_NEWS_type_1 %>') {
		var dealerType = document.getElementById('Msg_Type').value;
		OpenHtmlWindow(g_webAppName + '/claim/basicData/HomePageNews/dlrUserChooseInit.do?dealerType='+dealerType,1000,500);
	}
}
// 选择新闻抄送人员
function getReceiveOemUser() {
	OpenHtmlWindow(g_webAppName + '/claim/basicData/HomePageNews/oemUserChooseInit.do',1000,500);
}

function initUserList(dataArray){
	var tbody1 = document.getElementById("tbody1"); 
	__index = 1;
	var l = tbody1.rows.length;
	for(var i=0;i<l;i++) {
		tbody1.deleteRow();
	}
	if(dataArray != 'undefined') {
		for(var i=0;i<dataArray.length;i++) {
			var tr = document.createElement("tr");
			tr.className = 'table_list_row2';
			var td = document.createElement("td");
			td.innerHTML = __index++;
			tr.appendChild(td);
			
			td = document.createElement("td");
			td.innerHTML = "<a href=\"javascript:;\" onclick=\"removeTR(this)\">[删除]</a>";
			tr.appendChild(td);
			
			td = document.createElement("td");
			td.innerHTML = dataArray[i][3] + "<input type='hidden' name='recDealer' value='"+dataArray[i][0]+"'/>";
			tr.appendChild(td);
			
			td = document.createElement("td");
			td.innerHTML = dataArray[i][1];
			tr.appendChild(td);
			
			td = document.createElement("td");
			td.innerHTML = dataArray[i][2];
			td.align = 'left';
			tr.appendChild(td);
			
			tbody1.appendChild(tr);
		}
	}
}

function initOemUserList(dataArray) {
	var tbody1, inputName;
	var viewNewsType = document.getElementById('viewNewsType').value;
	if(viewNewsType == '<%=Constant.VIEW_NEWS_type_2 %>') {
		tbody1 = document.getElementById("tbody3"); 
		inputName = 'recOemUser';
	} else {
		tbody1 = document.getElementById("tbody2"); 
		inputName = 'recOemDealer';
	}
	__index = 1;
	var l = tbody1.rows.length;
	for(var i=0;i<l;i++) {
		tbody1.deleteRow();
	}
	if(dataArray != 'undefined') {
		for(var i=0;i<dataArray.length;i++) {
			var tr = document.createElement("tr");
			tr.className = 'table_list_row2';
			var td = document.createElement("td");
			td.innerHTML = __index++;
			tr.appendChild(td);
			
			td = document.createElement("td");
			td.innerHTML = "<a href=\"javascript:;\" onclick=\"removeTR(this)\">[删除]</a>";
			tr.appendChild(td);
			
			td = document.createElement("td");
			td.innerHTML = dataArray[i][3] + "<input type='hidden' name='"+inputName+"' value='"+dataArray[i][0]+"'/>";
			tr.appendChild(td);
			
			td = document.createElement("td");
			td.innerHTML = dataArray[i][1];
			tr.appendChild(td);
			
			td = document.createElement("td");
			td.innerHTML = dataArray[i][2];
			td.align = 'left';
			tr.appendChild(td);
			
			tbody1.appendChild(tr);
		}
	}
}

function removeTR(obj) {
	var s = obj.parentElement.parentElement.parentElement;
	s.removeChild(obj.parentElement.parentElement);
}

function callback() {
	MyAlert('sss');	
}

function checkDealer(){
	var obj = $('viewNewsType');
	if(obj.value==<%=Constant.VIEW_NEWS_type_1%>){
		$('recDlrTab').style.display = '';
		$('xxlx').style.display = '';
		$('roletr').style.display = '';
		$('recOemTab').style.display = 'none';
	}
	else if(obj.value == "<%=Constant.VIEW_NEWS_type_2 %>") {
		$('recDlrTab').style.display = 'none';
		$('recOemTab').style.display = '';
		$('xxlx').style.display = 'none';
		$('roletr').style.display = 'none';
	}
	else
	{
		$('recDlrTab').style.display = 'none';
		$('recOemTab').style.display = 'none';
		$('xxlx').style.display = 'none';
		$('roletr').style.display = 'none';
	}
}

KE.show({id : 'contents'});
</script>
</head>
<body>
<div class="navigation">
  <img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 个人信息管理&gt;首页新闻&gt;接收人信息</div>
<form name='fm' id='fm' method="post">
 <table class="table_list" style='display:none'>
	<tr id="roletr" >
		<td align="left">
			不发送角色：
			<c:forEach items="${roleList }" var="list">
				<c:choose>
					<c:when test="${list.SELECT_STATUS == 1}">
						<input type="checkbox" name='recDealerRole' value="${list.ROLE_ID }" checked/>${list.ROLE_DESC }
					</c:when>
					<c:otherwise>
						<input type="checkbox" name='recDealerRole' value="${list.ROLE_ID }"/>${list.ROLE_DESC }
					</c:otherwise>
				</c:choose>
			</c:forEach>
		</td>
	</tr>
  <tr id="msgtypeid1" >
 	<th colspan="4" align="left">
 		<img src="<%=request.getContextPath()%>/img/nav.gif" />
 		查看类型：
 		<script type="text/javascript">
			genSelBoxExp("viewNewsType",<%=Constant.VIEW_NEWS_TYPE%>,"${list.DUTY_TYPE}",false,"short_sel","onchange=\"checkDealer()\"","true",'');
	    </script>
	    <span id="xxlx">
 		&nbsp;
 		消息类型：
 		<select name="Msg_Type" id="Msg_Type">
	 	<c:choose>
			<c:when test="${'11991003'eq list.MSG_TYPE}">
				<option value="<%=Constant.MSG_TYPE_3 %>" selected="selected">经销商公用消息</option>
			 	<option value="<%=Constant.MSG_TYPE_2 %>">经销商售后消息</option>
			 	<option value="<%=Constant.MSG_TYPE_1 %>">经销商销售消息</option>
     		</c:when> 
			<c:when test="${'10771002'eq list.MSG_TYPE}">
				<option value="<%=Constant.MSG_TYPE_3 %>" >经销商公用消息</option>
			 	<option value="<%=Constant.MSG_TYPE_2 %>" selected="selected">经销商售后消息</option>
			 	<option value="<%=Constant.MSG_TYPE_1 %>">经销商销售消息</option>
         	</c:when> 
          	<c:when test="${'10771001,10771003,10771004' eq list.MSG_TYPE}">
            	<option value="<%=Constant.MSG_TYPE_3 %>" >经销商公用消息</option>
			 	<option value="<%=Constant.MSG_TYPE_2 %>">经销商售后消息</option>
				<option value="<%=Constant.MSG_TYPE_1 %>"  selected="selected">经销商销售消息</option>
           	</c:when> 
         	<c:otherwise>
             	<option value="<%=Constant.MSG_TYPE_3 %>">经销商公用消息</option>
			 	<option value="<%=Constant.MSG_TYPE_2 %>">经销商售后消息</option>
			 	<option value="<%=Constant.MSG_TYPE_1 %>">经销商销售消息</option>
            </c:otherwise>	
        </c:choose>
 		</select>
 		</span>
 		&nbsp;
 		新闻类别：
 		<script type="text/javascript">
			 genSelBoxExp("NEWS_TYPE",<%=Constant.NEWS_TYPE%>,"${list.NEWS_TYPE }",true,"short_sel","","true",'');
	    </script>
 	</td>
 </tr>
 <tr>
 	<td colspan="4" align="left">&nbsp;</td>
 </tr>
 </table>
 <!-- 经销商端用户选择 -->
 <table class="table_list" id="recDlrTab">
 <tr>
	<th colspan="4" align="left">
		<img src="<%=request.getContextPath()%>/img/nav.gif" />&nbsp;新闻接收方&nbsp;
		<input type="button" style='display:none' class="cssbutton" value="选 择" onclick="getReceiveUser()"/>
	</th> 
 </tr>
 <tr>
 	<td align="left" colspan="4" valign="top">
 		<div style="height: 120px; overflow-y: scroll; overflow-x: hidden;">
	 		<table class="table_list">
	 			<tr>
	 				<th>序号</th>
	 				<th>经销商类型</th>
	 				<th>经销商代码</th>
	 				<th>经销商名称</th>
	 			</tr>
	 			<tbody id="tbody1">
	 				<c:forEach items="${recDealerList }" var="list" varStatus="status">
	 					<tr class="table_list_row2">
	 						<td>${status.index + 1 }</td>
	 						<td>${list.DEALER_TYPE }<input type='hidden' name='recDealer' value='${list.DEALER_ID }'/></td>
	 						<td>${list.DEALER_CODE }</td>
	 						<td>${list.DEALER_NAME }</td>
	 					</tr>
	 					<c:if test="${status.last == true }">
	 						<script>index = ${status.index + 1};</script>
	 					</c:if>
	 				</c:forEach>
	 			</tbody>
	 		</table>
 		</div>
	</td>
 </tr>
 <tr>
	<th colspan="4" align="left">
		<img src="<%=request.getContextPath()%>/img/nav.gif" />&nbsp;新闻抄送方&nbsp;
		<input type="button" style='display:none' class="cssbutton" value="选 择" onclick="getReceiveOemUser()"/>
	</th> 
 </tr>
  <tr>
 	<td align="left" colspan="4" valign="top">
 		<div style="height: 120px; overflow-y: scroll; overflow-x: hidden;">
	 		<table class="table_list">
	 			<tr>
	 				<th>序号</th>
	 				<th>用户名</th>
	 				<th>职位类型</th>
	 				<th>职位名称</th>
	 			</tr>
	 			<tbody id="tbody2">
	 				<c:forEach items="${recOemDealerList }" var="list" varStatus="status">
	 					<tr class="table_list_row2">
	 						<td>${status.index + 1 }</td>
	 						<td>${list.NAME }<input type='hidden' name='recOemDealer' value='${list.USER_ID }'/></td>
	 						<td>${list.POSE_TYPE }</td>
	 						<td>${list.POSE_NAME }</td>
	 					</tr>
	 					<c:if test="${status.last == true }">
	 						<script>index = ${status.index + 1};</script>
	 					</c:if>
	 				</c:forEach>
	 			</tbody>
	 		</table>
 		</div>
	</td>
 </tr>
</table>
<!-- 主机厂用户选择 -->
<table class="table_list" id="recOemTab" style="display:none;">
 <tr>
	<th colspan="4" align="left">
		<img src="<%=request.getContextPath()%>/img/nav.gif" />&nbsp;新闻接收方&nbsp;
		<input type="button" style='display:none' class="cssbutton" value="选 择" onclick="getReceiveUser()"/>
	</th> 
 </tr>
 <tr>
 	<td align="left" colspan="4" valign="top">
 		<div style="height: 120px; overflow-y: scroll; overflow-x: hidden;">
	 		<table class="table_list" id="userlist">
	 			<tr>
	 				<th>序号</th>
	 				<th>用户名</th>
	 				<th>职位类型</th>
	 				<th>职位名称</th>
	 			</tr>
	 			<tbody id="tbody3">
	 				<c:forEach items="${recOemUserList }" var="list" varStatus="status">
	 					<tr class="table_list_row2">
	 						<td>${status.index + 1 }</td>
	 						<td>${list.NAME }<input type='hidden' name='recOemUser' value='${list.USER_ID }'/></td>
	 						<td>${list.POSE_TYPE }</td>
	 						<td>${list.POSE_NAME }</td>
	 					</tr>
	 					<c:if test="${status.last == true }">
	 						<script>index = ${status.index + 1};</script>
	 					</c:if>
	 				</c:forEach>
	 			</tbody>
	 		</table>
 		</div>
	</td>
 </tr>
</table>
<br />
<table class="table_info" border="0" id="file" style='display:none'>
 <tr>
 	<td height="23"></td>
	<td height="23"></td>
 	<td colspan="2" align="center">
 		<input class="normal_btn" type="button" name="ok" id="commitBtn" value="发布" onClick="save();"/> 
 		<input class="normal_btn" name="back" type="button" onClick="reBack();" value="返回"/>
 	</td>
 </tr>
</table>


<br />
</form>
<script>
checkDealer();
function reBack(){
	location="<%=contextPath%>/claim/basicData/HomePageNews/mainNews.do";
}


function clearInput(){
	var target = document.getElementById('dealerCode');
	target.value = '';
}

function save(){
    if(document.getElementById("NEWS_TYPE")==null
    	    ||document.getElementById("NEWS_TYPE").value==''){
    	MyAlert("请选择新闻类别！");
    	return false;
    }

    if(document.getElementById("contents")==null
    	    ||document.getElementById("contents").value==''){
    	MyAlert("内容必填！");
    	return false;
    }
    
	if(!submitForm('fm')) {
		return false;
	}
	makeNomalFormCall('<%=contextPath%>/claim/basicData/HomePageNews/saveNews.json',function(json){
		if(json.Exception) {
			MyAlert(json.Exception.message);
		} else {
			MyUnCloseAlert('发布成功!',function() {
				window.location.href = "<%=contextPath%>/claim/basicData/HomePageNews/mainNews.do";
			});
		}
	}, 'fm');
}
//区域单选按钮事件
function areaQuery(){
	$('div2').style.display = 'none';
	$('div1').style.display = 'block';
}
//经销商单选按钮事件
function dealerQuery(){
	$('div1').style.display = 'none';
	$('div2').style.display = 'block';
}
</script>
</body>
</html>