<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java"  pageEncoding="UTF-8"%>
<%
	String contextPath = request.getContextPath();
%>
<%@taglib uri="/jstl/cout" prefix="c" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>配件货位信息维护</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
<script language="javascript" type="text/javascript">
	function doInit(){
			__extQuery__(1);
	}
</script>
</head>
<body onbeforeunload="returnBefore();">
  <div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 基础信息管理 &gt; 配件基础信息维护 &gt; 配件货位维护 &gt; 库管员选择</div>
  <form name='fm' id='fm'>
  <table class="table_query">
       <tr>            
        <td class="table_query_2Col_label_4Letter">库管员：
        </td>            
        <td>
			<input  class="middle_txt" id="managerName"  name="managerName" type="text" datatype="1,is_null,20"/>
			<input id="managerId" name="managerId" type="hidden" />
			<input id="selectedName" name="selectedName" type="hidden" />
        </td>
        <td class="table_query_2Col_label_4Letter"></td>
        <td></td>   
       </tr>
       <tr>
        <td colspan="4" align="center">
        	    <input class="normal_btn" type="button" name="BtnQuery" id="queryBtn" value="查 询"  onclick="__extQuery__(1)"/>
				<input class="normal_btn" type="button" name="button1" value="关 闭"  onclick="_hide();"/>
        </td>
       </tr>       
 	</table>
 	<br/>
<!--分页 begin -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
<!--分页 end -->
<script type="text/javascript" >
var myPage;
var url = "<%=request.getContextPath()%>/parts/baseManager/partsBaseManager/PartLocation/partManagerSelect.json?query=1";
var title = null;
var columns = [
			{header: "选择",sortable: false,dataIndex: 'USER_ID',align:'center',renderer:myLink},
		//	{header: "用户ID",sortable: false,dataIndex: 'USER_ID',align:'center'},
			{header: "用户名称",sortable: false,dataIndex: 'NAME',align:'center'}
	      ];
function myLink(value,metadata,record){
	return String.format(
			"<input type='hidden' id='"+record.data.USER_ID+"' name='"+record.data.USER_ID+"' value='"+record.data.NAME+"'/>"+
			"<input type='radio'  value='"+record.data.USER_ID+"' onclick='selbyid(this);'/>"
			);
}
function selbyid(obj){
	$('managerId').value=obj.value;
	$('selectedName').value = document.getElementById(obj.value).value;
	btnDisable();
	_hide();
}
function returnBefore()
{   var whManId = 'whManId';
    var Name = 'whManName';
	var userId = document.getElementById("managerId").value;
	var selectedName = document.getElementById("selectedName").value;
	if(userId && userId.length > 0)
		parentDocument.getElementById(whManId).value = userId;
	if(selectedName && selectedName.length > 0)
	   	parentDocument.getElementById(Name).value = selectedName;	
}
//失效按钮
function btnDisable(){

    $$('input[type="button"]').each(function(button) {
        button.disabled = true;
    });

}

//有效按钮
function btnEnable(){

    $$('input[type="button"]').each(function(button) {
        button.disabled = "";
    });

}
</script> 
</form>
</body>
</html>