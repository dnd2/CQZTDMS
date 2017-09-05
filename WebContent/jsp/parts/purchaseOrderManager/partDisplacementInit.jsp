<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="/jstl/cout" %>
<%@page import="com.infodms.dms.common.Constant" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<%String contextPath = request.getContextPath();%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
<meta http-equiv="X-UA-Compatible" content="IE=8">
<jsp:include page="${contextPath}/common/jsp_head_new.jsp"/>
<title>配件货位移位</title>
</head>
<body onload="__extQuery__(1);">
<div class="wbox">
<div class="navigation">
	<img src="<%=request.getContextPath()%>/img/nav.gif"/>&nbsp;当前位置：配件管理&gt; 采购订单管理&gt; 配件货位移位
</div>
<form method="post" name="fm" id="fm" enctype="multipart/form-data">
    <input type="hidden" name="curPage" id="curPage"/>
    <div class="form-panel">
	<h2><img src="<%=contextPath%>/jmstyle/img/search-ico.png" style="margin-bottom: -13px;"/>查询条件</h2>
	<div class="form-body">
    <table class="table_query" >
       <tr>
           <td class="right">入库单号：</td>
           <td >
               <input class="middle_txt" type="text" id="IN_CODE" name="IN_CODE" value=""/>
           </td>
           <td class="right">入库日期：</td>
           <td >
           	<input name="sCreateDate" readonly="readonly" id="sCreateDate" value="${old}" type="text"  class="middle_txt" datatype="1,is_date,10" group="sCreateDate,eCreateDate" style="width: 80px;">
              <input name='button3' value=" " type='button' class='time_ico' title="点击选择时间" />
  				<label>至</label>
              <input name="eCreateDate" readonly="readonly" id="eCreateDate" value="${now}" type="text"  class="middle_txt" datatype="1,is_date,10" group="sCreateDate,eCreateDate" style="width:80px;">
              <input name='button3' value=" " type='button' class='time_ico' title="点击选择时间" />
           </td>
       </tr>
       <tr>
           <td class="center" colspan="4">
           	<input type="button" class="u-button" name="BtnQuery" id="queryBtn" onclick="__extQuery__(1);" value="查询"/>&nbsp;
<!--            	<input type="button" class="u-button" onclick="saveConfirmPurRcvQty()" value="验收" id="ys_button"/>&nbsp; -->
<!--            	<input type="button" class="normal_btn" onclick="exportPurRcv()" value="导出"/>&nbsp; -->
           	<input type="reset" class="u-button" onclick="btnEable();" value="重置"/>&nbsp;
           </td>
       </tr>
   </table>
   </div>
   </div>
   <jsp:include page="${contextPath}/queryPage/orderHidden.html"/>
   <jsp:include page="${contextPath}/queryPage/pageDiv.html"/>
</form>

<script type="text/javascript">
   var myPage;
   var url = '<%=contextPath%>/parts/purchaseOrderManager/PartDisplacement/getPoInInfo.json';
   var title = null;
   var columns = [
	{header: "序号", renderer: getIndex},
    {id: 'action',header: "操作", sortable: false, dataIndex: 'IN_ID', renderer: myLink, align: 'center'},
	{header: "入库单号",dataIndex:'IN_CODE', style:"text-align: center"},
	{header: "配件编码",dataIndex:'PART_OLDCODE', style:"text-align: center"},
	{header: "配件名称",dataIndex:'PART_CNAME', style:"text-align:center",renderer:subPartCnameText},
	{header: "配件件号",dataIndex:'PART_CODE', style:"text-align: center"},
	{header: "单位",dataIndex:'UNIT', style:"text-align: center"},
	{header: "批次号",dataIndex:'BATCH_NO', style:"text-align: center"},
	{header: "货位",dataIndex:'LOC_CODE', style:"text-align: center"},
	{header: "入库数量",dataIndex:'IN_QTY', style:"text-align: center"}, 
	{header: "供应商",dataIndex:'VENDER_NAME', style:"text-align: center"},
	{header: "配件类型",dataIndex:'PRODUCE_STATE', style:"text-align: center;",renderer:getItemValue},
	{header: "入库日期",dataIndex:'IN_DATE', style:"text-align: center"},
	{header: "状态",dataIndex:'STATE', style:"text-align: center",renderer:getItemValue}
   ];
    
	//操作链接生成
	function myLink(value,meta,record){
		var whId = record.data.WH_ID;
		return String.format("<a href=\"#\" onclick='displacement(\"" + value + "\",\""+whId+"\")'>[移位]</a>");
		
   	}
	
	function displacement(inId,whId) {
	    window.location.href = "<%=contextPath%>/parts/purchaseOrderManager/PartDisplacement/getInIdInfo.do?inId=" + inId+"&whId="+whId;
	}
	
   //截取配件名称
   function subPartCnameText(value,meta,record){
	   	var rs = value;
	   	if(rs.length>10){
	   		rs = value.substring(0,10)+"···";
	   	}
	   	return '<label title="'+value+'">'+rs+'</label>';
   }
   //导出
   function exportPurRcv(){
   		fm.action = "<%=contextPath%>/parts/purchaseOrderManager/PurchaseArrConfir/exportPurRcv.do";
		fm.submit();
   }
</script>
</div>
</body>
</html>