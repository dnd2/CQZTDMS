<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c"%>

<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>服务活动-生产基地列表</title>
<% String contextPath = request.getContextPath();%>
<%
//List<TtAsActivityBean> CharactorPoList=(List<TtAsActivityBean>)request.getAttribute("CharactorPoList");
%>
<script language="JavaScript" type="text/javascript">
//修改活动
function modifyActivity(){
	 	var id="";
	 	if(document.FRM.groupId==null){
	 		MyAlert("无生产基地可选择!");
   	     return;
	 	}
	 	if(document.FRM.groupId.length==null){
	 		if(document.FRM.groupId.checked==true){
			id+=document.FRM.groupId.value+",";
		}
	 	}
	   for(var i =0; i<document.FRM.groupId.length;i++) {
		if(document.FRM.groupId[i].checked==true){
			id+=document.FRM.groupId[i].value+",";
		}
   }
   if(id.length<1){
 	  MyDivAlert("请选择！");
   }else{
      MyDivConfirm("是否确认增加？",sures,[id]);
   }
}
	function sures(id){
	    var activityId=document.getElementById("activityId").value; 
		document.FRM.action = "<%=contextPath%>/claim/serviceActivity/ServiceActivityManageProduceBase/serviceActivityManageProduceBaseOption.do?carYieldly=" + id+"&activityId="+activityId;
		document.FRM.submit();
	}

	function selectClick() {
    	if(document.FRM.groupId==null){
    	}
    	else if(document.FRM.groupId.length==null){
    	    document.FRM.groupId.checked=document.FRM.selectAll.checked;
    	}
        else if(document.FRM.groupId.length!=null){
        for(var i =0; i<document.FRM.groupId.length;i++) {
            document.FRM.groupId[i].checked=document.FRM.selectAll.checked;
        }
       }
    }
    //全选回显
	function xingzhi(){
		 var modelist=document.getElementById("Charactor").value;
			var modelNew=modelist.toString();
			if(!modelNew){return}
			var model=modelNew.split(",");
			for(var i=0;i<model.length;i++){
				document.getElementById(model[i]).checked="checked";
				document.getElementById(model[i]).disabled = 'disabled';
			}
		}
</script>
</head>

<body>
	<div class="navigation">
			<img src="<%=contextPath %>/img/nav.gif" />&nbsp;当前位置： 售后服务管理&gt;服务活动管理&gt;服务活动管理
	</div>
<form name="FRM" id="FRM" method="post">
	 <input type="hidden" name="Charactor" id="Charactor" value="<%=request.getAttribute("Charactor")%>"/>
	 <input type="hidden" name="activityId" id="activityId" value="<%=request.getAttribute("activityId") %>"/>
  <table class="table_list" style="border-bottom:1px solid #DAE0EE">
  	 <tr>
		  	<th colspan="3" align="left">
		  	  <img class="nav" src="<%=contextPath %>/img/subNav.gif" /> 生产基地列表 
			  <input type="button" name="bt_add" id="bt_edit_id" class="normal_btn" value="确定" onclick="modifyActivity();"/>
			  <input type="button" name="bt_close" id="bt_delete_id" class="normal_btn" value="关闭" onclick="_hide();" />
			</th>
	 </tr>
      <tr>
        <th>
       		 <input type="checkbox" name="selectAll"   onclick="selectClick();" />
        </th>
	      <th><b>生产基地</b></th>
      </tr>
	  <c:forEach var="CharactorPoList" items="${CharactorPoList}">
		  <tr class="table_list_row1">
			    <td><input type="checkbox" name="groupId" value="${CharactorPoList.areaId}" id="${CharactorPoList.areaId}"/></td>
			    <td><c:out value="${CharactorPoList.areaName}"></c:out></td>
	      </tr>
	  </c:forEach>
</table>
     <script language="JavaScript" type="text/javascript">
		 <!--
		 xingzhi();
		 //-->
	 </script> 
 </form>
</body>
</html>