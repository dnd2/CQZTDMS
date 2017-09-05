<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" pageEncoding="UTF-8"%>
<%
    String contextPath = request.getContextPath();
	
%>
<%@taglib uri="/jstl/cout" prefix="c"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>设计变更维护</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<script language="javascript" type="text/javascript">
$(document).ready(function() {
    __extQuery__(1);
});

var myPage;
var url = "<%=request.getContextPath()%>/parts/baseManager/partExceptionQueryManger/partExceptionQuery/partExceptionQuerySelect.json?query=1&partOldId="+"${partOldId}";
var title = null;
var columns = [
    {header: "选择", sortable: false, dataIndex: 'PART_ID', align: 'center', renderer: myLink},
    {header: "配件编码", sortable: false, dataIndex: 'PART_OLDCODE', style: 'text-align:left'},
    {header: "配件名称", sortable: false, dataIndex: 'PART_CNAME', style: 'text-align:left'},
    //	{header: "配件ID",sortable: false,dataIndex: 'PART_ID',align:'center'},
    {header: "件号", sortable: false, dataIndex: 'PART_CODE', style: 'text-align:left'}
    //	{header: "价格",sortable: false,dataIndex: 'STOCK_PRICE',align:'center'}
];
function myLink(value, metadata, record) {
	var id = record.data.PART_ID;
	var code = record.data.PART_OLDCODE;
	var name = record.data.PART_CNAME;
	var html = "<label class='u-radiobox'><input type='radio' id='"+code+"' name='partRadio' value='"+code+"' onclick=\"selbyid('"+id+"', '"+code+"', '"+name+"');\"/><span></span></label>";
	return String.format(html);
}
function selbyid(id, code, name){
// 	$('#PART_ID')[0].value=id;
// 	$('#PART_OLDCODE')[0].value=code;
//     $('#PART_DATA')[0].value = name;
//     var win = parent || top;
    parentDocument.getElementById('PART_ID${reType}').value = id;
    parentDocument.getElementById('PART_OLDCODE${reType}').value = code;
    parentDocument.getElementById('PART_DATA${reType}').value = name;
	_hide();
}

// function returnBefore(){  
// 	var partOldcode = 'PART_OLDCODE';
//     var partCname = 'PART_DATA';
//     var partId = 'PART_ID';
// 	var id = document.getElementById("PART_ID").value;
// 	var code = document.getElementById("PART_OLDCODE").value;
//     var name = document.getElementById("PART_DATA").value;
//     var parentDocument = parent.frames['inIframe'];
// 	if(id && id.length > 0)
// 		parentDocument.getElementById(partId).value = id;
// 	if(code && code.length > 0)
// 		parentDocument.getElementById(partOldcode).value = code;
// 	if(name && name.length > 0)
// 	   	parentDocument.getElementById(partCname).value = name;	
// }
</script>
</head>
<body>
	<div class="wbox">
		<div class="navigation">
			<img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 基础信息管理 &gt; 配件基础信息维护 &gt; 设计变更维护 &gt; 配件选择
		</div>
		<form name='fm' id='fm'>
			<div class="form-panel">
				<h2>设计变更维护 - 配件选择</h2>
				<div class="form-body">
					<table class="table_query">
						<tr>
							<td class="right" width="20%">
								配件编码：
								<input id="PART_ID" name="PART_ID" type="hidden" />
								<input id="PART_OLDCODE" name="PART_OLDCODE" type="hidden" />
								<input id="PART_DATA" name="PART_DATA" type="hidden" />
							</td>
							<td width="30%">
								<input class="middle_txt" id="partolcode" name="partolcode" type="text" datatype="1,is_null,20" />
							</td>
							<td class="right" width="20%">配件名称：</td>
							<td width="30%">
								<input type="text" name="partcname" id="partcname" datatype="1,is_null,30" class="middle_txt" value="" />
							</td>
						</tr>
						<tr>
							<td class="formbtn-aln" colspan="4" align="center">
								<input class="u-button u-query" type="button" value="查 询" name="BtnQuery" id="queryBtn" onclick="__extQuery__(1)" />
								<input class="u-button u-cancel" type="button" name="button1" value="关 闭" onclick="_hide();" />
							</td>
						</tr>
					</table>
				</div>
			</div>
			<!--分页 begin -->
			<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
			<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
			<!--分页 end -->
	</div>
	</form>
</body>
</html>