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
			//loadcalendar();  //初始化时间控件
			__extQuery__(1);
	}
</script>
</head>
<body onbeforeunload="returnBefore();">
<div class="wbox">
	<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 基础信息管理 &gt; 配件基础信息维护 &gt; 配件条码打印 &gt; 配件选择</div>
  <form name='fm' id='fm'>
	  <div class="form-panel">
		  <h2>配件选择</h2>
		  <div class="form-body">
			 <table class="table_query">
				<tr>            
					<td class="table_query_2Col_label_4Letter">配件编码：
						<input id="PART_OLDCODE" name="PART_OLDCODE" type="hidden" />
						<input id="PART_DATA" name="PART_DATA" type="hidden" />
						<input id="oemMinPkg" name="oemMinPkg" type="hidden" />
						<input id="normalQty" name="normalQty" type="hidden" />
						<input id="locCode" name="locCode" type="hidden" />
					</td>            
					<td>
						<input  class="middle_txt" id="partolcode"  name="partolcode" type="text" datatype="1,is_null,20"/>
					</td>
					<td class="table_query_2Col_label_4Letter">配件名称：</td>
					<td><input type="text" name="partcname" id="partcname" datatype="1,is_null,30" class="middle_txt" value=""/></td>   
				</tr>
				<tr>
					<td align="right">库房：</td>
					<td colspan="3">
						<select id="WH_ID" name="WH_ID" class="short_sel u-select">
							<c:forEach items="${wareHouses}" var="wareHouse">
								<option value="${wareHouse.whId }">${wareHouse.whName }</option>
							</c:forEach>
						</select>
					</td>
				</tr>
				<tr>
					<td class="center" colspan="6" align="center">
							<input class="u-button u-query" type="button"  value="查 询" name="BtnQuery" id="queryBtn" onclick="__extQuery__(1)"/>&nbsp;&nbsp;
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
var url = "<%=request.getContextPath()%>/parts/baseManager/partBarCodePrt/partBarCodePrtAction/partQuery.json?query=1";
var title = null;
var columns = [
			{header: "选择",sortable: false,dataIndex: 'PART_ID',align:'center',renderer:myLink},
			{header: "配件编码",sortable: false,dataIndex: 'PART_OLDCODE',style: 'text-align: left;'},
			{header: "配件名称",sortable: false,dataIndex: 'PART_CNAME',style: 'text-align: left;'},
		//	{header: "配件ID",sortable: false,dataIndex: 'PART_ID',align:'center'},
			{header: "件号",sortable: false,dataIndex: 'PART_CODE',style: 'text-align: left;'}
		//	{header: "价格",sortable: false,dataIndex: 'STOCK_PRICE',align:'center'}
	      ];
function myLink(value,metadata,record){
	var partId = record.data.PART_ID;
	return String.format(
			"<input type='hidden' id='partId_"+partId+"' name='partId_"+partId+"' value='"+record.data.PART_ID+"'/>"+
			"<input type='hidden' id='oemMinPkg_"+partId+"' name='oemMinPkg_"+partId+"' value='"+record.data.OEM_MIN_PKG+"'/>"+
			"<input type='hidden' id='normalQty_"+partId+"' name='normalQty_"+partId+"' value='"+record.data.NORMAL_QTY+"'/>"+
			"<input type='hidden' id='locCode_"+partId+"' name='locCode_"+partId+"' value='"+record.data.LOC_CODE+"'/>"+
			"<input type='radio' id='"+record.data.PART_OLDCODE+"' name='"+record.data.PART_OLDCODE+"' value='"+record.data.PART_OLDCODE+"' onclick='selbyid(this, \""+partId+"\");'/>"
			);
}
function selbyid(obj, partId){
	$('#PART_OLDCODE')[0].value=obj.value;
	$('#PART_DATA')[0].value = document.getElementById("partId_"+partId).value;
	$('#oemMinPkg')[0].value = document.getElementById("oemMinPkg_"+partId).value;

	$('#normalQty')[0].value = document.getElementById("normalQty_"+partId).value;
	$('#locCode')[0].value = document.getElementById("locCode_"+partId).value;
	
	_hide();
}
function returnBefore()
{   var partOldcode = 'PART_OLDCODE';
    var partCname = 'PART_DATA';
    var oemMinPkg = 'min_package';

    var normalQty = 'normalQty';
    var locCode = 'locCode';
    
	var code = document.getElementById("PART_OLDCODE").value;
	var name = document.getElementById("PART_DATA").value;
	var oPkg = document.getElementById("oemMinPkg").value;

	var oNormalQty = document.getElementById("normalQty").value;
	var oLocCode = document.getElementById("locCode").value;
	
	if(code && code.length > 0)
		parentDocument.getElementById(partOldcode).value = code;
	if(name && name.length > 0)
	   	parentDocument.getElementById(partCname).value = name;
	if(oPkg && oPkg.length > 0)
	   	parentDocument.getElementById(oemMinPkg).value = oPkg;	
   	
	if(oNormalQty && oNormalQty.length > 0)
	   	parentDocument.getElementById(normalQty).value = oNormalQty;
	if(oLocCode && oLocCode.length > 0)
	   	parentDocument.getElementById(locCode).value = oLocCode;
   	
}
</script> 
</body>
</html>