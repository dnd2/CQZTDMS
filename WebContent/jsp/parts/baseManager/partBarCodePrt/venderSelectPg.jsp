<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java"  pageEncoding="UTF-8"%>
<%
	String contextPath = request.getContextPath();
%>
<%@taglib uri="/jstl/cout" prefix="c" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>配件条码打印</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
<script language="javascript" type="text/javascript">
	function doInit(){
			__extQuery__(1);
	}
</script>
</head>
<body onbeforeunload="returnBefore();">
<div class="wbox">
	<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 基础信息管理 &gt; 配件基础信息维护 &gt; 配件条码打印 &gt; 供应商选择</div>
  <form name='fm' id='fm'>
  <input id="partId" name="partId" type="hidden" value="${partId }"/>
  <div class="form-panel">
	  <h2>供应商选择</h2>
	  <div class="form-body">
		 <table class="table_query">
			<tr>            
				<td   align="right">供应商编码：
				</td>            
				<td>
					<input  class="middle_txt" id="venderCode"  name="venderCode" type="text" datatype="1,is_null,20"/>
				</td>
				<td   align="right">供应商名称：</td>
				<td>
				<input type="hidden" name="PART_VENDER" id="PART_VENDER"/>
				<input id="PART_DATA" name="PART_DATA" type="hidden" />
				<input type="text" name="partVender" id="partVender" datatype="1,is_null,30" class="middle_txt" value=""/>
				
				</td>   
			</tr>
			<tr>
				<td class="center" colspan="4" align="center">
						<input class="u-button u-query" type="button" value="查 询"  name="BtnQuery" id="queryBtn"  onclick="__extQuery__(1)"/>&nbsp;&nbsp;
						<input class="u-button" type="button" name="button1" value="关 闭"  onclick="_hide();"/>
				</td>
			</tr>       
		</table>  
	  </div>	
  </div>
<!--分页 begin -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
	</form>
</div>	
 
<!--分页 end -->
<script type="text/javascript" >
var myPage;
var url = "<%=request.getContextPath()%>/parts/baseManager/partBarCodePrt/partBarCodePrtAction/venderQuery.json?query=1";
var title = null;
var columns = [
			{header: "选择",sortable: false,dataIndex: 'PART_ID',align:'center',renderer:myLink},
			{header: "供应商编码",sortable: false,dataIndex: 'VENDER_CODE',style: 'text-align: left;'},
			{header: "供应商名称",sortable: false,dataIndex: 'VENDER_NAME',style: 'text-align: left;'}
		//	{header: "配件ID",sortable: false,dataIndex: 'PART_ID',align:'center'},
		//	{header: "件号",sortable: false,dataIndex: 'PART_CODE',align:'center'}
		//	{header: "价格",sortable: false,dataIndex: 'STOCK_PRICE',align:'center'}
	      ];
function myLink(value,metadata,record){
	return String.format(
			"<input type='hidden' id='"+record.data.VENDER_NAME+"' name='"+record.data.VENDER_NAME+"' value='"+record.data.VENDER_ID+"'/>"+
			"<input type='radio' id='"+record.data.VENDER_NAME+"' name='"+record.data.VENDER_NAME+"' value='"+record.data.VENDER_NAME+"' onclick='selbyid(this);'/>"
			);
}
function selbyid(obj){
	$('#PART_VENDER')[0].value=obj.value;
	$('#PART_DATA')[0].value = document.getElementById(obj.value).value;
	_hide();
}
function returnBefore()
{  var partVender = 'PART_VENDER';
    var venderData = 'PART_DATA2';
	var name = document.getElementById("PART_VENDER").value;
	var data = document.getElementById("PART_DATA").value;
	if(name && name.length > 0)
		parentDocument.getElementById(partVender).value = name;
	if(data && data.length > 0)
	   	parentDocument.getElementById(venderData).value = data;	
}
</script> 
</body>
</html>