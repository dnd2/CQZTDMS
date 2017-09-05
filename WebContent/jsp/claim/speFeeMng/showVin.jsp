<!DOCTYPE html PUBLIC "-//W3C//Dtd Xhtml 1.0 transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<% String contextPath = request.getContextPath(); %>
<body onload="__extQuery__(1)">
<div class="wbox">
<div id="loader" style='position:absolute;z-index:200;background:#FFCC00;padding:1px;top:4px;display:none;display: none;'></div>
<div class="navigation"><img src="<%=contextPath %>/img/nav.gif" width="11" height="11" />&nbsp;当前位置：VIN选择 </div>
</div>
  <form  name="fm" id="fm" method="post">
   <!--查询条件begin-->
    <table class="table_query" border="0" >
      <tr>
        <td class="table_query_2Col_label_5Letter"><div align="right"><span class="tabletitle">VIN：</span></div></td>
        <td align="left">
            <input name="VIN_PARAM" type="text" class="middle_txt" size="17" />
        </td>
        <td class="table_query_2Col_label_5Letter">车主姓名：</td>
        <td align="left">
            <input name="CUSTOMER" type="text" class="middle_txt" size="5" />
        </td>
         <td class=""><input name="button" id="queryBtn" type="button" onclick="__extQuery__(1)" class="normal_btn"  value="查询" />
        <input name="button" id="queryBtn" type="button" onclick="_hide()" class="normal_btn"  value="关闭" />
        </td>
      </tr>
    </table>
    <!--查询条件end-->
      <jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
</form>
<script language="JavaScript" >
	var myPage;
	
	var url = "<%=contextPath%>/claim/speFeeMng/OutFeeApplyManager/getDetailByVin.json?row_id="+<%=request.getAttribute("row_id")%>;
				
	var title = null;

	var columns = [
				{id:'action',header: "选择",sortable: false,dataIndex: 'vin',renderer:mySelect,align:'center'},
				{header: "VIN", dataIndex: 'vin', align:'center'},
				//{header: "车牌号", dataIndex: 'licenseNo', align:'center'},
				{header: "车系", dataIndex: 'seriesName', align:'center'},
				{header: "车型", dataIndex: 'modelName', align:'center'},
				{header: "发动机号", dataIndex: 'engineNo', align:'center'}
				
				//{header: "颜色", dataIndex: 'color', align:'center'},
				//{header: "生产日期", dataIndex: 'productDate', align:'center'},
				//{header: "购车日期", dataIndex: 'purchasedDate', align:'center'},
				//{header: "车主姓名", dataIndex: 'customerName', align:'center'},
				//{header: "车主身份证号", dataIndex: 'certNo', align:'center'},
				//{header: "车主联系电话", dataIndex: 'mobile', align:'center'},
				//{header: "车主地址", dataIndex: 'addressDesc', align:'center'}
				//{header: "变速箱号", dataIndex: 'gearboxNo', align:'center',hidden:true},
				//{header: "后桥号", dataIndex: 'rearaxleNo', align:'center',hidden:true}
				//{header: "分动器号", dataIndex: 'ADDRESS_DESC', align:'center',hidden:true}
				
		      ];
		      
	function mySelect(value,metaDate,record){
		 return String.format("<input type='radio' name='rd' onclick='setVIN(\""+<%=request.getAttribute("row_id")%>+"\",\""+record.data.vin+"\",\""+record.data.licenseNo+"\",\""+record.data.modelName+"\",\""+record.data.seriesName+"\",\""+record.data.rearaxleNo+"\",\""+record.data.gearboxNo+"\",\""+record.data.engineNo+"\",\""+record.data.color+"\",\""+record.data.productDate+"\",\""+record.data.purchasedDate+"\",\""+record.data.customerName+"\",\""+record.data.certNo+"\",\""+record.data.mobile+"\",\""+record.data.addressDesc+"\",\""+record.data.historyMile+"\",\""+record.data.modelId+"\",\""+record.data.brandName+"\")' />");
	}

	function setVIN(row_id,v1,v2,v3,v4,v5,v6,v7,v8,v9,v10,v11,v12,v13,v14,v15,v16,v17){
		 //调用父页面方法
		 if(v2==null||v2=="null"){
		 	v2 = "";
		 }
		 if(v3==null||v3=="null"){
		 	v3 = "";
		 }
		 if(v4=="null"||v4==null){
		 	v4 = "";
		 }
		 if(v5==null||v5=="null"){
		 	v5 = "";
		 }
		 if(v6=="null"||v6==null){
		 	v6 = "";
		 }
		 if(v7==null||v7=="null"){
		 	v7 = "";
		 }
		 if(v8==null||v8=="null"){
		 	v8 = "";
		 }
		 if(v9==null||v9=="null"){
		 	v9 = "";
		 }
		 if(v10==null||v10=="null"){
		 	v10 = "";
		 }
		 if(v11==null||v11=="null"){
		 	v11 = "";
		 }
		 if(v12==null||v12=="null"){
		 	v12 = "";
		 }
		 if(v13==null||v13=="null"){
		 	v13 = "";
		 }
		 if(v14==null||v14=="null"){
		 	v14 = "";
		 }
		 if(v15==null||v15=="null"){
		 	v15 = "";
		 }
		 if(v16==null||v16=="null"){
		 	v16 = "";
		 }
		 if(v17==null||v17=="null"){
		 	v17 = "";
		 }
 		parentContainer.setVIN(row_id,v1,v2,v3,v4,v5,v6,v7,v8,v9,v10,v11,v12,v13,v14,v15,v16,v17);
 		//关闭弹出页面
 		_hide();
	}

	
</script>
</body>
</html>
