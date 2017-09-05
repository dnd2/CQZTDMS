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
        <td align="right"><div align="right"><span class="tabletitle">VIN：</span></div></td>
        <td align="left">
            <input name="vin" type="text" class="middle_txt" size="17" />
        </td>
        <td align="right">车主姓名：</td>
        <td align="left">
            <input name="customerName" type="text" class="middle_txt" size="5" />
        </td>
        <td class=""><input name="button" id="queryBtn" type="button" onclick="__extQuery__(1)" class="normal_btn"  value="查询" /></td>
      </tr>
    </table>
    <!--查询条件end-->
      <jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
</form>
<script language="JavaScript" >
	var myPage;
	
	var url = "<%=contextPath%>/feedbackmng/apply/StandardVipApplyManager/showVinList.json?commond=1";
				
	var title = null;

	var columns = [
				{id:'action',header: "选择",sortable: false,dataIndex: 'VIN',renderer:mySelect ,align:'center'},
				{header: "VIN", dataIndex: 'VIN', align:'center'},
				{header: "车系", dataIndex: 'GROUP_NAME', align:'center'},
				{header: "发动机号", dataIndex: 'ENGINE_NO', align:'center'},
				{header: "颜色", dataIndex: 'COLOR', align:'center'},
				{header: "生产日期", dataIndex: 'PRODUCT_DATE', align:'center'},
				{header: "购车日期", dataIndex: 'PURCHASED_DATE', align:'center'},
				{header: "车主姓名", dataIndex: 'CUSTOMER_NAME', align:'center'},
				{header: "车主身份证号", dataIndex: 'CERT_NO', align:'center'},
				{header: "车主联系电话", dataIndex: 'MOBILE', align:'center'},
				{header: "车主地址", dataIndex: 'ADDRESS_DESC', align:'center'}
				
		      ];
		      
	function mySelect(value,metaDate,record){
		 return String.format("<input type='radio' name='rd' onclick='setVIN(\""+record.data.VIN+"\",\""+record.data.GROUP_NAME+"\",\""+record.data.ENGINE_NO+"\",\""+record.data.COLOR+"\",\""+record.data.PRODUCT_DATE+"\",\""+record.data.PURCHASED_DATE+"\",\""+record.data.CUSTOMER_NAME+"\",\""+record.data.CERT_NO+"\",\""+record.data.MOBILE+"\",\""+record.data.ADDRESS_DESC+"\",\""+record.data.HISTORY_MILE+"\")' />");
	}

	function setVIN(v1,v2,v3,v4,v5,v6,v7,v8,v9,v10,v11){
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
 		parentContainer.getVIN(v1,v2,v3,v4,v5,v6,v7,v8,v9,v10,v11);
 		//关闭弹出页面
 		_hide();
	}

	
</script>
</body>
</html>
