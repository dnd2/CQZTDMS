<!DOCTYPE html PUBLIC "-//W3C//Dtd Xhtml 1.0 transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<% String contextPath = request.getContextPath(); %>
<body onload="__extQuery__(1)">
<div class="wbox">
<div id="loader" style='position:absolute;z-index:200;background:#FFCC00;padding:1px;top:4px;display:none;display: none;'></div>
<div class="navigation"><img src="<%=contextPath %>/img/nav.gif" width="11" height="11" />&nbsp;当前位置：旧件选择 </div>
</div>
  <form  name="fm" id="fm" method="post">
  <input type="hidden" name="GROUP_ID" id="GROUP_ID" value="<%=request.getAttribute("GROUP_ID") %>"/>
  <input type=hidden name="DOWN_PART_CODE" id="DOWN_PART_CODE" value="<%=request.getAttribute("PartCode") %>"/>
 	<input type="hidden" name="yiedily" id="yiedily" value="${yiedily }"/>
 	<input type="hidden" name="vin" id="vin" value="${vin }"/>
   <!--查询条件begin-->
    <table class="table_query" border="0" >
      <tr>
        <td align="right"><div align="right"><span class="tabletitle">配件代码：</span></div></td>
        <td align="left">
            <input name="PART_CODE" id="PART_CODE" type="text" class="middle_txt" size="17" />
        </td>
        <td align="right">配件名称：</td>
        <td align="left">
            <input name="PART_NAME" id="PART_NAME" type="text" class="middle_txt" size="5" />
        </td>
         </tr>
        <tr  style="display: none">
        <td class="table_query_2Col_label_8Letter">旧件供应商名称：</td>
        <td align="left">
            <input name="SUPPLIER_NAME" id="SUPPLIER_NAME" type="text" class="middle_txt" size="5" />
        </td>
        </tr>
        <tr>
        <td align="center" colspan="4"><input name="button" id="queryBtn" type="button" onclick="__extQuery__(1)" class="normal_btn"  value="查询" />
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
	
	var url = "<%=contextPath%>/claim/dealerClaimMng/ClaimBillMaintainMain/queryPartCode1.json";
				
	var title = null;

	var columns = [
				{id:'action',header: "选择",sortable: false,dataIndex: 'partId',renderer:mySelect,align:'center'},
				{header: "配件代码", dataIndex: 'partCode', align:'center'},
				{header: "配件名称", dataIndex: 'partName', align:'center'},
				{header: "配件单价", dataIndex: 'claimPriceParam', align:'center'}
				//{header: "旧件供应商名称", dataIndex: 'supplierName', align:'center'}
				//{header: "发动机号", dataIndex: 'engineNo', align:'center'}
				
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
		 return String.format("<input type='radio' name='rd' onclick='setDownPartCode(\""+record.data.partId+"\",\""+record.data.partCode+"\",\""+record.data.partName+"\",\""+record.data.claimPrice+"\",\""+record.data.supplierCode+"\",\""+record.data.supplierName+"\")' />");
	}

	function setDownPartCode(v1,v2,v3,v4,v5,v6){
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
		 if(v6==null||v6=="null"){
		 	v6 = "";
		 }
		 var oldPartCode = document.getElementById("DOWN_PART_CODE").value;
		if (parent.$('inIframe')) {
 			parentContainer.setDownPartCode(v1,v2,v3,v4,v5,v6,oldPartCode);
 		} else {
			parent.setDownPartCode(v1,v2,v3,v4,v5,v6,oldPartCode);
		}
 		//parentContainer.setDownPartCode(v1,v2,v3,v4,v5,v6);
 		//关闭弹出页面
 		_hide();
	}

	
</script>
</body>
</html>
