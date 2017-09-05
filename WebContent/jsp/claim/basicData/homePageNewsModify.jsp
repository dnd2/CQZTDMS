<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java"  pageEncoding="UTF-8"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Date"%>
<%@page import="com.infodms.dms.po.TmDealerPO"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.text.SimpleDateFormat" %>
<%@page import="com.infodms.dms.common.Constant"%>
<%@page import="com.infodms.dms.po.FsFileuploadPO"%>
<%@page import="java.util.LinkedList"%>
<%@page import="com.infodms.dms.util.CommonUtils"%>
<%
	String contextPath = request.getContextPath();
	List<FsFileuploadPO> attachLs = (LinkedList<FsFileuploadPO>)request.getAttribute("lists");
%>
<%@taglib uri="/jstl/cout" prefix="c" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<title>首页新闻</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
<script>
function doInit(){
	   loadcalendar();	
	   
 }
  KE.show({id : 'contents'});
</script>
</head>
<body>
<div class="navigation">
  <img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 售后服务管理&gt;个人信息管理&gt;首页新闻</div>
<form name='fm' id='fm' method="post">
<table  class="table_edit">
 <tr >
 	<td nowrap  class="table_add_2Col_label_6Letter">单据编码：
 	<input name="newsCode" value="${list.NEWS_CODE}" type="hidden" />
 	<input name="newsId" value="${list.NEWS_ID}" type="hidden" />
 	</td>
 	<td>${list.NEWS_CODE}</td>
 	<td nowrap  class="table_add_2Col_label_6Letter">发表单位：</td><td >${list.ORG_NAME}
 	<input type="hidden"  name="companyId" value="${list.ORG_ID}"/>
 	<input type="hidden"  name="companyName" value="${list.ORG_NAME}"/>
 	<input type="hidden"  name="date" value="${list.NEWS_DATE }"/> 
 	</td>
 </tr>
 <tr>
 	<td nowrap  class="table_add_2Col_label_6Letter">发表人：</td><td >${list.VOICE_PERSON}
 	<input type="hidden"  name="name" value="${list.NAME}"/></td>
 	 <td  class="table_add_2Col_label_6Letter">发表日期：</td><td id="newsDa"> </td>
 </tr>
  
 <tr>
 	<td nowrap  class="table_add_2Col_label_6Letter">失效日期：</td>
 	<td align="left">
		<input name="expiryDate" type="text" class="short_txt" id="expiryDate" value="${list.EXPIRY_DATE }" />
     	<input name="button" type="button" class="time_ico" onclick="showcalendar(event, 'expiryDate', false);" value=" " />
 	</td>
 	<c:if test="${11781001 == list.DUTY_TYPE}">
 	<td align="right"  class="table_add_2Col_label_6Letter">消息类型：</td>
 	<td >
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
                           <c:otherwise><option value="" >无</option></c:otherwise>	
                     </c:choose>
             </select>	
        </td>
        </c:if>
 </tr>
  <tr>
 	<td align="right" class="table_add_2Col_label_7Letter">存档：</td>
 	<td>
 	<c:choose>
 		<c:when test="${'1' eq list.ARCHIVE_FLAG}">
 		<input type="checkbox"  id="archive_flag"  name="archive_flag" value="1" checked/>
 		</c:when>
 		<c:when test="${'0' eq list.ARCHIVE_FLAG}">
 		<input type="checkbox"  id="archive_flag"  name="archive_flag" value="1" />
 		</c:when>
 	 </c:choose>	
 	</td>
 	<td align="right" class="table_add_2Col_label_6Letter">查看类型：</td>
 	<td>
 		<script type="text/javascript">
			genSelBoxExp("viewNewsType",<%=Constant.VIEW_NEWS_TYPE%>,"${list.DUTY_TYPE}",true,"short_sel","","true",'');
	    </script>
 	</td>
 </tr>
 <tr id="roletr" style="display:none;">
 	<td align="right">
 		指定角色：
 	</td>
 	<td colspan="3" align="left">
	 	<c:forEach items="${roleList }" var="list">
	 		<c:choose>
	 			<c:when test="${fn:contains(checkedrole,list.ROLE_ID)==true}">
	 				<input name="roles" type="checkbox" value="${list.ROLE_ID }" checked="checked"/>${list.ROLE_DESC }
	 			</c:when>
	 			<c:otherwise>
	 				<input name="roles" type="checkbox" value="${list.ROLE_ID }"/>${list.ROLE_DESC }
	 			</c:otherwise>
	 		</c:choose>
 			
 		</c:forEach>
	</td>
 </tr>
 <tr>
	 <td align="right"  id="q1">增加类别：</td>
	 <td  id="q2" colspan="3">		      
	     <input type="radio" name="mySel" id="r1" checked onclick="areaQuery();"/>区域
	     &nbsp;&nbsp;
	     <input type="radio" name="mySel" id="r2" onclick="dealerQuery();"/>经销商	    
	  </td>
 </tr>
 <tr id="div1" style="display:block">	
      <td id="deId2" class="table_add_2Col_label_6Letter" align="right">区域：</td>
      <td  id="deId3" align="left"  nowrap>
          <input name="orgCode" class="" type="text" id="orgCode" size="40" value="${codesorg}" />
		  <input name="orgSel" type="button" class="mini_btn" onClick="showOrg('orgCode' ,'orgId' ,true,'')" value="&hellip;" />
		  <input name="orgId" type="hidden" id="orgId" value="${list.ORG_TYPE_ID}"/>
		  <input class="cssbutton" type="button" value="清空" onClick="clrTxt();"/>
      </td>
 </tr>      
 <tr id="div2" style="display:none">      
      <td nowrap class="table_add_2Col_label_6Letter" id="deId">选择经销商：</td>
 		<td align="left" nowrap="true" id="deId1">
			<input class="middle_txt" id="dealerCode" value="${orgCodes}" name="dealerCode" type="text"/>
            <input name="showBtn" type="button" class="mini_btn" style="cursor: pointer;" onclick="showOrgDealer('dealerCode','','true','',true);" value="..." />        
            <input name="clrBtn" type="button" class="normal_btn" onclick="clearInput();" value="清除"/>
            <input type="hidden" name="DEALER_IDS" id="DEALER_IDS" value=""/>
		</td>
 </tr>
 <tr>
 	<td align="right">标题：</td>
 	<td><input name="title" type="text" value='${list.NEWS_TITLE}' class="middle_txt" /></td>
 	<td align="right">新闻类别：</td>
 	<td align="left" nowrap="true">
		<script type="text/javascript">
				 genSelBoxExp("NEWS_TYPE",<%=Constant.NEWS_TYPE%>,"${list.NEWS_TYPE}",true,"short_sel","","true",'');
		
	    </script>
	</td>
 </tr>
 <tr>
 	<td align="right">内容:</td>
 	<td colspan="4">
 		<textarea id="contents"  name="contents" cols="100" rows="10" style="width:740px;height:200px;">${list.CONTENTS }</textarea>
	</td>
 </tr>
</table>
<br />
<table class="table_info" border="0" id="file">
			<input type="hidden" name="fjids"/>
		<tr colspan="8">
			<th>
				<img class="nav" src="../../../img/subNav.gif" />&nbsp;附件列表：
					&nbsp;&nbsp;&nbsp;&nbsp;
				<span align="left"><input type="button" class="normal_btn"  onclick="showNewsUpload('<%=contextPath%>')" value ='添加附件'/></span>
			</th>
		</tr>
		<tr>
			<td width="100%" colspan="2">
				<jsp:include page="${contextPath}/uploadDiv.jsp" />
			</td>
		</tr>
		<%for(int i=0;i<attachLs.size();i++) { %>
    			<script type="text/javascript">
    			addUploadRowByDb('<%=CommonUtils.checkNull(attachLs.get(i).getFilename()) %>','<%=CommonUtils.checkNull(attachLs.get(i).getFjid()) %>','<%=CommonUtils.checkNull(attachLs.get(i).getFileurl())%>');
    			</script>
    	<%} %>
    	<tr>
    	<td align="center" >
    		<input class="normal_btn" type="button" name="ok" id="commitBtn" value="确定" onclick="save();"/> 
 			<input class="normal_btn" name="back" type="button" onclick="reBack();" value="返回"/>    	</td>
    	</tr>
</table>
<br />
</form>
<script>
var date;
var date1;
date = $('date').value
date1= date.substr(0,10);
$('newsDa').update(date1);
//清空经销商框
function clearInput(){
	var target = document.getElementById('dealerCode');
	target.value = '';
}

function reBack(){
	location="<%=contextPath%>/claim/basicData/HomePageNews/mainNews.do";
}
function save(){
    if(document.getElementById("NEWS_TYPE")==null
    	    ||document.getElementById("NEWS_TYPE").value==''){
    	MyAlert("请选择新闻类别！");
    	return false;
    }
    if($('')){
        }
    if(document.getElementById("dealerCode")==null
    	    &&document.getElementById("dutyType").value==''){
    	MyAlert("请选择经销商或者区域！");
    	return;
    }
    
    if(document.getElementById('viewNewsType').value == '<%=Constant.VIEW_NEWS_type_2 %>') {
		var rolesobj = document.getElementsByName("roles");
		var checkedroles = '';
		for(var i=0;i<rolesobj.length;i++) {
			if(rolesobj[i].checked == true) {
				checkedroles += rolesobj[i].value + ',';
			}
		}
		if(checkedroles == '') {MyAlert('请选择指定角色'); return;}
	}
    
	fm.action="<%=contextPath%>/claim/basicData/HomePageNews/saveNews.do"
	fm.submit();
}
function clrTxt(){
	$('orgCode').value="";
	$('orgId').value="";
}

//如果不是选着经销商端 就隐藏
function doCusChange(value){

	if(value==<%=Constant.VIEW_NEWS_type_1%>){
		$('deId').style.display="block";
		$('deId1').style.display="block";
		$('deId2').style.display="block";
		$('deId3').style.display="block";
		$('roletr').style.display = "none";
	}
	else if(value == "<%=Constant.VIEW_NEWS_type_2 %>") {
		$('deId').style.display="none";
		$('deId1').style.display="none";
		$('deId2').style.display="none";
		$('deId3').style.display="none";
		$('roletr').style.display = "block";
	}
	else{
		$('deId').style.display="none";
		$('deId1').style.display="none";
		$('deId2').style.display="none";
		$('deId3').style.display="none";
		$('roletr').style.display = "none";
	}
}
if($('viewNewsType').value==<%=Constant.VIEW_NEWS_type_1%>){
	$('deId').style.display="block";
	$('deId1').style.display="block";
	$('deId2').style.display="block";
	$('deId3').style.display="block";
}
else if($('viewNewsType').value == "<%=Constant.VIEW_NEWS_type_2 %>") {
	$('deId').style.display="none";
	$('deId1').style.display="none";
	$('deId2').style.display="none";
	$('deId3').style.display="none";
	$('roletr').style.display = "block";
}
else{
	$('deId').style.display="none";
	$('deId1').style.display="none";
	$('deId2').style.display="none";
	$('deId3').style.display="none";
}
  //区域单选按钮事件
	function areaQuery(){
		$('div2').style.display = 'none' ;
		$('div1').style.display = 'block';
	}
	//经销商单选按钮事件
	function dealerQuery(){
		$('div1').style.display = 'none' ;
		$('div2').style.display = 'block';
	}  
</script>
</body>
</html>