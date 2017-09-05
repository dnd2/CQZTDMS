<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c"%>

<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>服务活动-车型列表</title>
<% String contextPath = request.getContextPath();%>
<%
//List<TtAsActivityBean> ActivityBeanList=(List<TtAsActivityBean>)request.getAttribute("ActivityBeanList");//活动工时
//List<TtAsActivityBean> ActivityPartsList=(List<TtAsActivityBean>)request.getAttribute("ActivityPartsList");//活动配件
//List<TtAsActivityBean> ActivityNetItemList=(List<TtAsActivityBean>)request.getAttribute("ActivityNetItemList");//活动其它项目
%>
<script language="javascript" type="text/javascript">
	//新增查询 活动工时 
	function OpenItemWorkHours(){
		OpenHtmlWindow('<%=contextPath%>/claim/serviceActivity/ServiceActivityManageItem/serviceActivityManageItemWorkHoursQuery.do?activityId='+<%=request.getAttribute("activityId") %>,800,500);
	}
	//新增查询 配件 
	function OpenItemParts(){
		OpenHtmlWindow('<%=contextPath%>/claim/serviceActivity/ServiceActivityManageItem/serviceActivityManageItemPartsQuery.do?activityId='+<%=request.getAttribute("activityId") %>,800,500);
	}
	//新增查询 活动其它项目 
	function OpenItemOthers(){
		OpenHtmlWindow('<%=contextPath%>/claim/serviceActivity/ServiceActivityManageItem/serviceActivityManageItemOthersQuery.do?activityId='+<%=request.getAttribute("activityId") %>,800,500);
	}
	/*
	           功能：工时删除方法
	  	参数：action : "del"删除
	  	描述:取得已经选择的checkbox，拼接成字符串，各项目以,隔开
	*/
	function optionsWork(id) {
		MyDivConfirm("是否确认删除？",optionsChanage,[id]);
	}
	function optionsChanage(id) {
		makeNomalFormCall('<%=request.getContextPath()%>/claim/serviceActivity/ServiceActivityManageItem/deleteItemWorkHoursOption.json?itemId='+id+'&activityId='+<%=request.getAttribute("activityId") %>,delBack,'fm','queryBtn');
	}
	/*
		 功能：配件删除方法
		参数：action : "del"删除
		描述: 取得已经选择的checkbox，拼接成字符串，各项目以,隔开
	*/
	function optionsParts(id) {
		MyDivConfirm("是否确认删除？",Parts,[id]);
			
	}
	function Parts(id){
		makeNomalFormCall('<%=request.getContextPath()%>/claim/serviceActivity/ServiceActivityManageItem/deleteItemPartsOption.json?partsId='+id+'&activityId='+<%=request.getAttribute("activityId") %>,delBack,'fm','queryBtn');
	}
	/*
		功能：活动其它项目 删除方法
		参数：action : "del"删除
		描述:取得已经选择的checkbox，拼接成字符串，各项目以,隔开
	*/
	function optionsOthers(id) {
		MyDivConfirm("是否确认删除？",options,[id]);
	}
	function options(id){
		makeNomalFormCall('<%=request.getContextPath()%>/claim/serviceActivity/ServiceActivityManageItem/deleteItemOthersOption.json?id='+id+'&activityId='+<%=request.getAttribute("activityId") %>,delBack,'fm','queryBtn');
	}
//删除回调方法
function delBack(json) {
	if(json.success != null && json.success == "true") {
		MyDivAlert("删除成功！");
		goBack();
	} else {
		MyDivAlert("删除失败！请联系管理员！");
	}
}
 //返回配件列表页面;
 //返回活动工时列表;
 //返回配件列表页面;
function goBack(){
	//var partsId = document.getElementById("partsId").value;
	//var itemId = document.getElementById("itemId").value;
	//var id = document.getElementById("id").value;
	fm.action = "<%=contextPath%>/claim/serviceActivity/ServiceActivityManageItem/serviceActivityManageItemQuery.do?activityId="+<%=request.getAttribute("activityId") %>;
	fm.submit();
	}
</script>
</head>

<body>
	<div class="navigation">
			<img src="<%=contextPath %>/img/nav.gif" />&nbsp;当前位置： 售后服务管理&gt;服务活动管理&gt;服务活动管理
	</div>
<form name="fm" id="fm" method="post">
  <input type="hidden" name="activityId" id="activityId" value="<%=request.getAttribute("activityId") %>">
  <table width=95% border=0 class="table_list" style="border-bottom:1px solid #DAE0EE">
    <tbody>
     <tr>
		  <th colspan="4" align="left">
			  <img class="nav" src="<%=contextPath %>/img/subNav.gif" /> 活动工时 
		      <input type="button" name="time_add"  class="normal_btn" value="新增" onclick="OpenItemWorkHours();"  />
	      </th>
      </tr>
      <tr>
	      <th height="22" align="center" class="zi"><b>工时代码</b></th>
	      <th height="22" align="center" class="zi"><b>工时名称</b></th>
	      <th height="22" align="center" class="zi"><b>工时数</b></th>
	      <th height="22" align="center" class="zi"><b>操作</b></th>
	  </tr>
	  <c:forEach var="ActivityBeanList" items="${ActivityBeanList}">
		         <tr class="table_list_row1">
		              <input type="hidden" name="itemId" id="itemId" value="${ActivityBeanList.itemId}"></input>
				      <td><c:out value="${ActivityBeanList.itemCode}"></c:out></td>
				      <td><c:out value="${ActivityBeanList.itemName}"></c:out></td>
				      <td><c:out value="${ActivityBeanList.normalLabor}"></c:out></td> 
				      <td>
				           <input type="button" name="time_delete" class="normal_btn" value="删除" onclick="optionsWork('${ActivityBeanList.itemId}');">
				      </td>            
	             </tr>
	</c:forEach>
    </tbody>
  </table>
  <br>
  <table width=95% border=0 class="table_list" style="border-bottom:1px solid #DAE0EE">
      <tr>
        <th colspan="4" align="left">
	         <img class="nav" src="<%=contextPath %>/img/subNav.gif" /> 活动配件 
	      	 <input type="button" name="time_add3" class="normal_btn" value="新增" onclick="OpenItemParts();"  />
      	</th>
      </tr>
      <tr>
	      <th width="26%" ><b>配件代码</b></th>
	      <th width="26%" ><b>配件名称</b></th>
	      <th width="34%" ><b>配件数量</b></th>
	      <th width="14%" ><b>操作</b></th>
	  </tr>
	   <c:forEach var="ActivityPartsList" items="${ActivityPartsList}">
		<tr>
		 <input type="hidden" name="partsId" id="partsId" value="${ActivityPartsList.partsId}"></input>
		  <td><c:out value="${ActivityPartsList.partNo}"></c:out></td>
		  <td><c:out value="${ActivityPartsList.partName}"></c:out></td>
		  <td><c:out value="${ActivityPartsList.partQuantity}"></c:out></td>
		  <td><input type="button" name="time_delete" class="normal_btn" value="删除" onclick="optionsParts('${ActivityPartsList.partsId}');"></td>     
		</tr>
      </c:forEach>
  </table>
   <br>
  <table width=95% border=0 class="table_list" style="border-bottom:1px solid #DAE0EE">
      <tr>
	      <th colspan="3" align="left">
		        <img class="nav" src="<%=contextPath %>/img/subNav.gif" /> 活动其它项目 
		        <input type="button" name="time_add2" class="normal_btn" value="新增" onclick="OpenItemOthers();"/>
	      </th>
      </tr>
      <tr>
	      <th width="36%" height="22" align="center" class="zi"><b>项目代码</b></th>
	      <th width="50%" height="22" align="center" class="zi"><b>项目名称</b></th>
	      <th width="14%" height="22" align="center" class="zi"><b>操作</b></th>
	  </tr>
	   <c:forEach var="ActivityNetItemList" items="${ActivityNetItemList}">
			<tr>
			 <input type="hidden" name="id" id="id" value="${ActivityNetItemList.id}"></input>
			  <td><c:out value="${ActivityNetItemList.itemCodes}"></c:out></td>
			  <td><c:out value="${ActivityNetItemList.itemDesc}"></c:out></td>
			  <td><input type="button" name="time_delete" class="normal_btn" value="删除" onclick="optionsOthers('${ActivityNetItemList.id}');"></td>     
			</tr>
	      </c:forEach>
  </table>
<br>
  <table class="table_query">
	  <tr>
	  		<td align="center"><input type="button" name="return1" onclick="parent.window._hide();"  class="normal_btn" value="关闭"/></td>
	  </tr>
  </table>
  </form>
</body>
</html>