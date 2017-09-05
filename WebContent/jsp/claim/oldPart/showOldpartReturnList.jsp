<!DOCTYPE html PUBLIC "-//W3C//Dtd Xhtml 1.0 transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<% String contextPath = request.getContextPath(); %>
<body onload="__extQuery__(1)">
<div class="wbox">
<div id="loader" style='position:absolute;z-index:200;background:#FFCC00;padding:1px;top:4px;display:none;display: none;'></div>
<div class="navigation"><img src="<%=contextPath %>/img/nav.gif" width="11" height="11" />&nbsp;当前位置：新件选择 </div>
</div>
  <form  name="fm" id="fm" method="post">
  	<input type=hidden name="COMMAND" value="1"/>
   <!--查询条件begin-->
    <table class="table_query" border="0" >
      <tr>
      	<td class="table_query_2Col_label_8Letter"><div align="right"><span class="tabletitle">旧件清单号：</span></div></td>
        <td align="left">
            <input name="RETURN_NO" id="RETURN_NO" type="text" class="middle_txt" size="17" />
        </td>
        <td class="table_query_2Col_label_8Letter"><div align="right"><span class="tabletitle">配件代码：</span></div></td>
        <td align="left">
            <input name="PART_CODE" id="PART_CODE" type="text" class="middle_txt" size="17" />
        </td>
        <tr>
        <td colspan="4" align="center"><input name="button" id="queryBtn" type="button" onclick="__extQuery__(1)" class="normal_btn"  value="查询" />
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
	var returnIds = parentContainer.document.getElementsByName('RETURN_DETAIL_ID');
	var arrayObj = new Array();
	for(var i = 0;i<returnIds.length;i++){
		arrayObj[i] = returnIds[i].value;
	}
	
	var url = "<%=contextPath%>/claim/oldPart/ClaimOldPartReturnApplyManager/selectOldPartReturnListForward.json?RETURN_DETAIL_ID="+arrayObj;
				
	var title = null;

	var columns = [
				{id:'action',header: "选择",sortable: false,dataIndex: 'ID',renderer:mySelect,align:'center'},
				{header: "索赔清单号", dataIndex: 'RETURN_NO', align:'center'},
				{header: "配件代码", dataIndex: 'PART_CODE', align:'center'},
				{header: "配件名称", dataIndex: 'PART_NAME', align:'center'},
				{header: "索赔单号", dataIndex: 'CLAIM_NO', align:'center'},
				{header: "条码", dataIndex: 'BARCODE_NO', align:'center'},
				{header: "抵扣原因", dataIndex: 'DEDUCT_REMARK', align:'center',renderer:getItemValue}
				//{header: "旧件供应商名称", dataIndex: 'supplierName', align:'center'}
				//{header: "预授权", dataIndex: 'fore', align:'center'}
		      ];
		      
	function mySelect(value,metaDate,record){
		 //var vin = parentDocument.getElementById('vin').value;
		 return String.format("<input type='radio' name='rd' onclick='setMainPartCode(\""+record.data.RETOLDPART_ID+"\",\""+record.data.RETURN_NO+"\",\""+record.data.PART_CODE+"\",\""+record.data.PART_NAME+"\",\""+record.data.CLAIM_NO+"\",\""+record.data.BARCODE_NO+"\",\""+record.data.DEDUCT_REMARK+"\")' />");
	}

	function setMainPartCode(v1,v2,v3,v4,v5,v6,v7){
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
		 if(v7==null||v7=="null"){
			 v7 = "";
		 }
 		//parentContainer.setMainPartCode(v1,v2,v3,v4,v5,v6);
 		if (parent.$('inIframe')) {
 			parentContainer.setMainPartCode(v1,v2,v3,v4,v5,v6,v7);
 		} else {
			parent.setMainPartCode(v1,v2,v3,v4,v5,v6,v7);
		}
 		//if(parentContainer.cloMainPart==1) {
 			//关闭弹出页面
 			_hide();
 		 //}
	}
	
	function queryRulePart(partCode,vin){
		 var url="<%=contextPath%>/claim/dealerClaimMng/ClaimBillMaintainMain/queryPartCodeFromRuleListByVinAndPartCode.json?partCode="+partCode+"&vin="+vin;
		 makeNomalFormCall(url,queryPartResult,"fm");
	}
	function queryPartResult(json){
		var rs = json.ps;
		if(part==null){
			MyAlert('该配件没有对应规则，请联系管理员维护通用三包规则！');
			_hide();
			return;
		}
		setMainPartCode(rs.RETOLDPART_ID,rs.RETURN_NO,rs.PART_CODE,rs.PART_NAME,rs.CLAIM_NO,rs.BARCODE_NO,rs.DEDUCT_REMARK);
	}
</script>
</body>
</html>
