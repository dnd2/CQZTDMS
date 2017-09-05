<!DOCTYPE html PUBLIC "-//W3C//Dtd Xhtml 1.0 transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" import="com.infodms.dms.common.Constant" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<% String contextPath = request.getContextPath(); %>
<body onload="__extQuery__(1)">
<div class="wbox">
<div id="loader" style='position:absolute;z-index:200;background:#FFCC00;padding:1px;top:4px;display:none;display: none;'></div>
<div class="navigation"><img src="<%=contextPath %>/img/nav.gif" width="11" height="11" />&nbsp;当前位置：供应商选择 </div>
</div>
  <form  name="fm" id="fm" method="post">
  <input type="hidden" name="partCode" id="partCode" value="${partCode}" />
   <input type="hidden" name="partCodeTemp" id="partCodeTemp" value="${partCodeTemp}" />
  <input type="hidden" name="yieldly" id="yieldly" value="${yieldly}" />
    <input type="hidden" name="id" id="id" value="${id}" />
   <!--查询条件begin-->
    <table class="table_query" border="0" >
      <tr>
        <td class="table_query_2Col_label_8Letter"><div align="right"><span  class="tabletitle">配件供应商代码：</span></div></td>
        <td align="left">
            <input name="SUPPLIER_CODE" id="SUPPLIER_CODE" type="text" class="middle_txt" maxlength="10" />
        </td>
        <td class="table_query_2Col_label_8Letter">配件供应商名称：</td>
        <td align="left">
            <input name="SUPPLIER_NAME" id="SUPPLIER_NAME" type="text" class="middle_txt" maxlength="25" />
            <input name="count" id="count" type="hidden" value="1" class="middle_txt"/>
        </td>
      </tr>
        <tr>
        <td colspan="4" align="center"><input name="button" id="queryBtn" type="button" onclick="isCheck(1);" class="normal_btn"  value="查询" />
        <input name="button" id="queryBtn" type="button" onclick="_hide()" class="normal_btn"  value="关闭" />
        <input name="button" style="display: none" id="queryBtn" type="button" onclick="isCheck(2);" class="normal_btn"  value="查询所有" />
        </td>
      </tr>
    </table>
    <!--查询条件end-->
    <jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
</form>
<script language="JavaScript" >
	var myPage;
	
	var url = "<%=contextPath%>/claim/oldPart/ClaimOldPartOutStorageManager/querySupplier1.json";
				
	var title = null;

	var columns = [
				{id:'action',header: "选择",sortable: false,dataIndex: 'supplierId',renderer:mySelect,align:'center'},
				{header: "配件供应商代码", dataIndex: 'supplierCode', align:'center',renderer:myLink1},
				{header: "配件供应商名称", dataIndex: 'supplierName', align:'center',renderer:myLink2}
		      ];
      
	function myLink1(value,metaDate,record){
		var link="";
		var is_del=record.data.isDel;
		if(is_del==1){
			link+="<span style='color: red;'>"+value+"<span/>";
		}else{
			link+="<span>"+value+"<span/>";
		}
		return String.format(link);
	}
	function myLink2(value,metaDate,record){
		var is_del=record.data.isDel;
		var link="";
		if(is_del==1){
			link+="<span style='color: red;'>"+value+"<span/>";
		}else{
			link+="<span>"+value+"<span/>";
		}
		return String.format(link);
	}
	function mySelect(value,metaDate,record){
		 return String.format("<input type='radio' name='rd' onclick='setSupplier(\""+record.data.supplierCode+"\",\""+record.data.supplierName+"\")' />");
	}

	function setSupplier(v1,v2){
		 //调用父页面方法
		 if(v1==null||v1=="null"){
		 	v1 = "";
		 }
		 if(v2==null||v2=="null"){
		 	v2 = "";
		 }
		 var id = $('id').value;
		 if(id=="" || id==null||id=="null"){
			 if (parent.$('inIframe')) {
		 			parentContainer.setSupplier(v1,v2);
		 		} else {
					parent.setSupplier(v1,v2);
				}
			 }else{
				 if (parent.$('inIframe')) {
			 			parentContainer.setSupplier(v1,v2,id);
			 		} else {
						parent.setSupplier(v1,v2,id);
					}
				 }
 		
 		//关闭弹出页面
 		parent._hide();
	}

	function isCheck(count){
		$('count').value=count;
		__extQuery__(1);
	}

</script>
</body>
</html>
