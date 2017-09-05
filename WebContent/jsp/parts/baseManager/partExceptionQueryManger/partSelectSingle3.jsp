<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java"  pageEncoding="UTF-8"%>
<%@taglib uri="/jstl/cout" prefix="c" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<%
	String contextPath = request.getContextPath();
%>
<head>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>设计变更维护</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
<script type="text/javascript" >
var myPage;
var url = "<%=request.getContextPath()%>/parts/baseManager/partExceptionQueryManger/partExceptionQuery/partExceptionQuerySelect.json?query=1&partOldId="+"${partOldId}";
var title = null;
var columns = [
			{header: "选择",sortable: false,dataIndex: 'PART_ID',align:'center',renderer:myLink},
			{header: "配件编码",sortable: false,dataIndex: 'PART_OLDCODE',style: 'text-align:left'},
			{header: "配件名称",sortable: false,dataIndex: 'PART_CNAME',style: 'text-align:left'},
			{header: "件号",sortable: false,dataIndex: 'PART_CODE',style: 'text-align:left'}
	      ];
function myLink(value,metadata,record){
	var partId = record.data.PART_ID;
	var partOldCode = record.data.PART_OLDCODE;
	var partCode = record.data.PART_CODE;
	var partCname = record.data.PART_CNAME;
	var text = "<input type='radio' name='part' onclick='selbyid(\""+partId+"\",\""+partOldCode+"\",\""+partCode+"\",\""+partCname+"\");'/>";
	return String.format(text);
}
var ID = "${ID}";
function selbyid(partId,partOldCode,partCode,partCname){
	if(ID!='' && (typeof parentContainer.LOC_ID != 'undefined') ){
		parentContainer.LOC_ID=ID;
	}
	parentContainer.setPartCode(partId,partOldCode,partCode,partCname);
	_hide();
}
</script> 
</head>
<body onload="__extQuery__(1);">
  <div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置：配件管理 > 基础信息管理 > 仓储相关信息维护 > 配件形态转换 &gt; 配件选择</div>
  <form name='fm' id='fm'>
  <table class="table_query">
       <tr>            
        <td width="20%" align="right">配件编码：</td>            
        <td width="30%">
			<input  class="middle_txt" id="partolcode"  name="partolcode" type="text" datatype="1,is_null,20"/>
        </td>
        <td width="20%" align="right">配件名称：</td>
        <td width="30%"><input type="text" name="partcname" id="partcname" datatype="1,is_null,30" class="middle_txt" value=""/></td>   
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
</form>
</body>
</html>