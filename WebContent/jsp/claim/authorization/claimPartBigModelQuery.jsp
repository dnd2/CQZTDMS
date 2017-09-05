<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java"  pageEncoding="UTF-8"%>
<%
	String contextPath = request.getContextPath();
%>
<%@taglib uri="/jstl/cout" prefix="c" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>监控配件大类维护</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
</head>
<body onbeforeunload="returnBefore();">
  <div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 售后服务管理&gt;索赔授权管理&gt;监控配件大类维护</div>
  <form name='fm' id='fm'>
  <table class="table_query">
       <tr>            
        <td class="table_query_2Col_label_4Letter">配件大类代码：
        <input id="PARTTYPE_CODE" name="PARTTYPE_CODE" type="hidden" />
  		<input id="PARTTYPE_NAME" name="PARTTYPE_NAME" type="hidden" />
  		<input id="PARTTYPE_ID" name="PARTTYPE_ID" type="hidden" />
        </td>            
        <td>
			<input  class="middle_txt" id="parttypecode"  name="parttypecode" type="text" datatype="1,is_null,20"/>
        </td>
        <td class="table_query_2Col_label_4Letter">配件大类名称：</td>
        <td><input type="text" name="parttypename" id="parttypename" datatype="1,is_null,30" class="middle_txt" value=""/></td>   
       </tr>
       <tr>
        <td colspan="4" align="center">
        	    <input  id="queryBtn1" class="normal_btn" type="button" name="button1" value="查询"  onclick="__extQuery__(1)"/>
				<input class="normal_btn" type="button" name="button1" value="关闭"  onclick="_hide();"/>
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
var url = "<%=request.getContextPath()%>/claim/authorization/ClaimPartWatchMain/claimPartBigSelect.json?query=1";
var title = null;
var columns = [
			{header: "选择",sortable: false,dataIndex: 'ID',align:'center',renderer:myLink},
			{header: "配件大类代码",sortable: false,dataIndex: 'PARTTYPE_CODE',align:'center'},
			{header: "配件大类名称",sortable: false,dataIndex: 'PARTTYPE_NAME',align:'center'}				
	      ];
function myLink(value,metadata,record){
	return String.format(
			"<input type='hidden' id='"+record.data.PARTTYPE_CODE+"' name='"+record.data.PARTTYPE_CODE+"' value='"+record.data.PARTTYPE_NAME+"'/>"+
			"<input type='hidden' id='"+record.data.ID+"' name='"+record.data.ID+"' value='"+record.data.ID+"'/>"+
			"<input type='radio' id='"+record.data.PARTTYPE_CODE+"' name='"+record.data.PARTTYPE_CODE+"' codeId='"+record.data.ID+"' value='"+record.data.PARTTYPE_CODE+"' onclick='selbyid(this);'/>"
			);
}
function selbyid(obj){
	$('PARTTYPE_CODE').value=obj.value;
	$('PARTTYPE_NAME').value = document.getElementById(obj.value).value;
	$('PARTTYPE_ID').value = document.getElementById(obj.codeId).value;
	_hide();
}

function returnBefore()
{   var partTypeCode = 'PARTTYPE_CODE';
    var partTypeName = 'PARTTYPE_NAME';
    var partTypeId = 'PARTTYPE_ID';
	var code = document.getElementById("PARTTYPE_CODE").value;
	var name = document.getElementById("PARTTYPE_NAME").value;
	var id = document.getElementById("PARTTYPE_ID").value;
	if(code && code.length > 0)
		parentDocument.getElementById(partTypeCode).value = code;
	if(name && name.length > 0)
	   	parentDocument.getElementById(partTypeName).value = name;	
	if(id && id.length > 0)
	   	parentDocument.getElementById(partTypeId).value = id;
}
</script>  
</form>
</body>
</html>