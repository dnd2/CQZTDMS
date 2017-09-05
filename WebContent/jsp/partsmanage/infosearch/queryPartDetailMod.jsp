<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>配件详细信息</title>
<% String contextPath = request.getContextPath(); %>
<script type="text/javascript">
	function doInit(){
   		__extQuery__(1);
	}
</script>
</head>

<body>
<div class="navigation">
  <img src="<%=request.getContextPath()%>/img/nav.gif" />&nbsp;当前位置： 配件管理&gt;基本信息查询&gt;配件基本信息管理</div>
<form method="post" name = "fm" id="fm">
<input type="hidden" name="partId" id="partId" value="<c:out value="${dealerInfo.PART_ID}"/>"/>
  <table width="101%" border="1" class="table_edit" cellpadding="0" cellspacing="0">
    <tr>
      <th colspan="6"><img src="<%=request.getContextPath()%>/img/subNav.gif" alt="" class="nav" /> 配件信息</th>
    </tr>
    <tr bgcolor="F3F4F8">
      <td class="table_query_3Col_label_6Letter">配件代码：</td>
      <td class="table_query_3Col_input">
		  <c:out value="${dealerInfo.PART_CODE}"/>
		  <input type="hidden" id="partId" value="<c:out value="${dealerInfo.PART_ID}"/>"/>
	  </td>
      <td class="table_query_3Col_label_6Letter">配件名称： </td>
      <td class="table_query_3Col_input">
	  	  <c:out value="${dealerInfo.PART_NAME}"/>
	  </td>
      <td class="table_query_3Col_label_6Letter">配件类型：</td>
      <td class="table_query_3Col_input">
      	  <c:out value="${dealerInfo.PART_TYPE}"/>
      </td>
    </tr>
    <tr >
      <td class="table_query_3Col_label_6Letter">单位：</td>
      <td class="table_query_3Col_input">
	  	  <c:out value="${dealerInfo.UNIT}"/>
	  </td>
      <td class="table_query_3Col_label_6Letter">最小包装数：</td>
      <td class="table_query_3Col_input">
	  	  <c:out value="${dealerInfo.MINI_PACK}"/>
	  </td>
      <td class="table_query_3Col_label_6Letter">停用： </td>
      <td class="table_query_3Col_input">
      		<c:out value="${dealerInfo.STOP_FLAG}"/>
      </td>
    </tr>
    <tr >
      <td class="table_query_3Col_label_6Letter">替代件：</td>
      <td class="table_query_3Col_input">
      		<c:out value="${dealerInfo.REPLACE_PART_ID}"/>
      </td>
      <td class="table_query_3Col_label_6Letter">替代关系：</td>
      <td class="table_query_3Col_input">
      		<c:out value="${dealerInfo.CHANGE_CODE}"/>
      </td>
      <td class="table_query_3Col_label_6Letter">单车用量：</td>
      <td class="table_query_3Col_input">
      		<c:out value="${dealerInfo.CAR_AMOUNT}"/>
      </td>
    </tr>
    <tr >
      <td class="table_query_3Col_label_6Letter">销售价格：</td>
      <td class="table_query_3Col_input">
	  		<c:out value="${dealerInfo.SALE_PRICE}"/>
	  </td>
      <td class="table_query_3Col_label_6Letter">销售指导价格：</td>
      <td class="table_query_3Col_input">
	  		<c:out value="${dealerInfo.CUSTOMER_PRICE}"/>
	  </td>
      <td class="table_query_3Col_label_6Letter">索赔价格：</td>
      <td class="table_query_3Col_input">
	  		<c:out value="${dealerInfo.CLAIM_PRICE}"/>
	  </td>
    </tr>
    <tr >
      <td class="table_query_3Col_label_6Letter">备注：</td>
      <td class="table_query_3Col_input">
      		<c:out value="${dealerInfo.REMARK}"/>
      </td>
      <td class="table_query_3Col_label_6Letter">是否回运：</td>
      <td class="table_query_3Col_input">
      		<select id="isReturn">
      			<c:if test="${dealerInfo.IS_RETURN == 0}">
      				<option value="0" selected>否</option>
      				<option value="1">是</option>
      			</c:if>
      			<c:if test="${dealerInfo.IS_RETURN == 1}">
      				<option value="0">否</option>
      				<option value="1" selected>是</option>
      			</c:if>
      		</select>
      </td>
       <td class="table_query_3Col_label_6Letter">是否新件：</td>
      <td class="table_query_3Col_input">
      		<select id="IS_NEW_PART">
      			<c:if test="${dealerInfo.IS_NEW_PART == 0}">
      				<option value="0" selected>否</option>
      				<option value="1">是</option>
      			</c:if>
      			<c:if test="${dealerInfo.IS_NEW_PART == 1}">
      				<option value="0">否</option>
      				<option value="1" selected>是</option>
      			</c:if>
      		</select>
      </td>
      <td class="table_query_3Col_label_6Letter"></td>
      <td class="table_query_3Col_input"></td>
    </tr>
    <tr>
      <td class="table_query_3Col_label_14Letter" colspan="6">
      	<input type="button" value="保存" onclick="modPart()" class="normal_btn"/>
      	<input type="button" name="BtnNo" value="取消" class="normal_btn" onClick="_hide();">
      </td>
    </tr>
  </table>
  </br>
  <table width="101%" border="1" class="table_edit" cellpadding="0" cellspacing="0">
  	<tr>
      <td class="table_query_2Col_label_6Letter"><input type="button" class="long_btn" value="新增供应商" onclick="addSup()"/></td>
  	</tr>
  </table>
<!--分页 begin -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
	
<!--分页 end -->
	<br>
	 <table class="table_edit">
       <tr >
         <td align="center" >
         	
         </td>
       </tr>
     </table>
    <br>
</form>
<script type="text/javascript">
	var myPage;

	var url = "<%=contextPath%>/partsmanage/infoSearch/PartInfoSearch/getDealerInfo.json";
				
	var title = null;

	var columns = [
				{header: "供应商代码", dataIndex: 'SUPPLIER_CODE', align:'center'},
				{header: "供应商名称", dataIndex: 'SUPPLIER_NAME', align:'center'},
				{header: "简称", dataIndex: 'SHORT_NAME', align:'center'},
				{header: "联系人", dataIndex: 'LINK_MAN', align:'center'},
				{header: "联系人电话", dataIndex: 'PHONE_NUMBER', align:'center'},
				{id:'action',header: "删除",sortable: false,dataIndex: 'RELATION_ID',renderer:myLink,align:'center'}
		      ];
		      

function addSup(){
	var partId = document.getElementById("partId").value;
	var url = '<%=contextPath%>/jsp/partsmanage/infosearch/qyerySupplierSearchPart.jsp?partId='+partId;
	window.location = url;
}

function myLink(value,meta,record) {
	return String.format("<a href='#' onclick='delSure(\""+record.data.RELATION_ID+"\")'>[删除]</a>");
}
function delSure(relationId) {
	var del = confirm("确认删除"); 
	if (del) {
		var url = '<%=contextPath%>/partsmanage/infoSearch/SupplierInfoSearch/delRelation.json?relationId='+relationId;
		makeNomalFormCall(url,handleDel,'fm');
	} 
}
//修改配件信息
function modPart() {
	var isReturn = document.getElementById("isReturn").value;
	var partId = document.getElementById("partId").value;
	var ispart = $('IS_NEW_PART').value;
	var url = '<%=contextPath%>/partsmanage/infoSearch/PartInfoSearch/modReturn.json?isReturn='+isReturn+'&IS_NEW_PART='+ispart;
	makeNomalFormCall(url,veiwParts,'fm');

}

function veiwParts(json){
	  parent.window._hide();
	   parentContainer.__extQuery__(1);
}
function handleDel() {
	__extQuery__(1);
}

</script>
<!--页面列表 end -->
</body>
</html>