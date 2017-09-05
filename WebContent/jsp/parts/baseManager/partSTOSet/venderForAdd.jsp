<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java"  pageEncoding="UTF-8"%>
<%
	String contextPath = request.getContextPath();
%>
<%@taglib uri="/jstl/cout" prefix="c" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>配件直发条件设置</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
<script language="javascript" type="text/javascript">
	function doInit(){
			__extQuery__(1);
	}
</script>
</head>
<body onbeforeunload="returnBefore();">
  <div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 基础信息管理 &gt; 配件基础信息维护 &gt; 配件直发条件设置 &gt; 供货商选择</div>
  <form name='fm' id='fm'>
  <table class="table_query">
       <tr>            
        <td width="20%"   align="right">供货商编码：
        <input id="vender_name" name="vender_name" type="hidden" />
  		<input id="vender_id" name="vender_id" type="hidden" />
        </td>            
        <td width="30%">
			<input  class="middle_txt" id="VEND_CODE"  name="VEND_CODE" type="text" datatype="1,is_null,20"/>
        </td>
        <td width="20%"   align="right">供货商名称：</td>
        <td width="30%"><input type="text" name="VEND_NAME" id="VEND_NAME" datatype="1,is_null,30" class="middle_txt" value=""/></td>   
       </tr>
       <tr>
        <td colspan="4" align="center">
        	    <input class="normal_btn" type="button"  value="查 询" name="BtnQuery" id="queryBtn" onclick="__extQuery__(1)"/>
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
var url = "<%=request.getContextPath()%>/parts/baseManager/partSTOSet/partSTOSetAction/partVendersQuery.json";
var title = null;
var columns = [
			{header: "选择",sortable: false,dataIndex: 'MAKER_ID',align:'center',renderer:myLink},
			{header: "供货商编码",sortable: false,dataIndex: 'MAKER_CODE',align:'center'},
			{header: "供货商名称",sortable: false,dataIndex: 'MAKER_NAME',align:'center'}
	      ];
function myLink(value,metadata,record){
	var venderId = record.data.MAKER_ID;
	var venderName = record.data.MAKER_NAME;
	return String.format(
			"<input type='hidden' id='vName_"+venderId+"' name='vName_"+venderId+"' value='"+venderName+"'/>"+
			"<input type='radio' id='vId_"+venderId+"' name='vId_"+venderId+"' value='"+venderId+"' onclick='selbyid(this);'/>"
			);
}
function selbyid(obj){
	$('vender_id').value=obj.value;
	$('vender_name').value = document.getElementById("vName_"+obj.value).value;
	_hide();
}
function returnBefore()
{   var venderId = 'venderId';
    var venderName = 'venderName';
	var id = document.getElementById("vender_id").value;
	var name = document.getElementById("vender_name").value;
	if(id && id.length > 0)
		parentDocument.getElementById(venderId).value = id;
	if(name && name.length > 0)
	   	parentDocument.getElementById(venderName).value = name;	
}
</script> 
</form>
</body>
</html>