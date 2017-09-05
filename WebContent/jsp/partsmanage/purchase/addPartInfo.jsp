<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!-- created by lishuai103@yahoo.com.cn 20100612 配件采购订单明细 -->
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="com.infodms.dms.common.Constant"%>
<%@taglib uri="/jstl/cout" prefix="c" %>
<%
	String contextPath = request.getContextPath();
%>
<head>

<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>配件选择</title>
</head>
<script language="JavaScript">

	//初始化方法
	function doInit()
	{
		__extQuery__(1);
	}

</script>
<body>
<div class="navigation">
<img src="<%=request.getContextPath()%>/img/nav.gif" />&nbsp;当前位置： 配件管理 &gt; 配件采购 &gt;配件选择 </div>
<form name="fm" id="fm" method="post">
<input type="hidden" name="dcId" value="<c:out value="${dc.DC_ID}"/>"/>
   <table class="table_edit" >
   <c:if test="${dc != null}">
     <tr>
     	<td class="table_query_3Col_label_4Letter">供货方：</td>
        <td class="table_query_3Col_input">
			<c:out value="${dc.DC_NAME}"/>
        </td> 
        <td>&nbsp;</td>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
     </tr>
   </c:if>
   <c:if test="${signNo != ''}">
   <!-- 索赔页面点击增加配件 -->
		<tr>
     	<td class="table_query_3Col_label_4Letter">签收单号：</td>
        <td class="table_query_3Col_input">
			<c:out value="${signNo}"/>
        </td> 
        <td class="table_query_3Col_input">
			<input type="hidden" value="${signNo}" id="signNo" name="signNo"/>
			<input type="hidden" value="${claimId}" id="claimId" name="claimId"/>
        </td> 
     </tr>
   </c:if>
     <tr>
     	<td class="table_query_3Col_label_4Letter">配件代码：</td>
        <td class="table_query_3Col_input">
       	  <input type="text" name="partCode" id="partCode" class="middle_txt" />
        </td>
        <td class="table_query_3Col_label_4Letter">配件名称：</td>
        <td class="table_query_3Col_input">
       	  <input type="text" name="partName" id="partName" class="middle_txt" />
        </td>
        <!-- hidden -->
        <input type="hidden" name="flag" id="flag" class="middle_txt" value="<c:out value="${flag}"/>" />
        <input type="hidden" name="orderId" id="orderId" class="middle_txt" value="<c:out value="${orderId}"/>" />
        <!-- hidden End-->
        <td>&nbsp;</td>
       	<td class="table_query_3Col_input">    
       		<input type="button" id="queryBtn" onclick="__extQuery__(1);" class="normal_btn" style="width=8%" value="查询"/>
       	</td>
     </tr>
    </table>
    <jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
	<br>
	<table class="table_edit">
		<tr>
		  <td align="center">
		    <input type="button" name="BtnNo" value="确认" class="normal_btn" onclick="addItemPart()">
		  	<input type="button" name="BtnNo" value="关闭" class="normal_btn" onclick="parent._hide()">
          </td>
        </tr>
	</table>
</form>
<script language="JavaScript">
	var myPage;
	var url = "<%=contextPath%>/partsmanage/purchase/PurchaseOrderSearch/queryDCPart.json";
				
	var title = null;
	var dcId = document.getElementById("dcId").value;
	if (dcId) {
			var columns = [
				{header: "<input type='checkbox' name='checkAll' onclick='selectAll(this,\"partIds\")' />", width:'8%',sortable: false,dataIndex: 'PART_ID',renderer:myCheckBox},
				{header: "配件号", dataIndex: 'PART_CODE', align:'center'},
				{header: "配件名称", dataIndex: 'PART_NAME', align:'center'},
				{header: "单位", dataIndex: 'UNIT', align:'center'},
				{header: "最小包装数", dataIndex: 'MINI_PACK', align:'center'},
				{header: "有无库存", dataIndex: 'QUANTITY', align:'center'}
		      ];
	} else {
		//签收页面 索赔页面点击新增配件
		var columns = [
				{header: "<input type='checkbox' name='checkAll' onclick='selectAll(this,\"partIds\")' />", width:'8%',sortable: false,dataIndex: 'PART_ID',renderer:myCheckBox},
				{header: "配件号", dataIndex: 'PART_CODE', align:'center'},
				{header: "配件名称", dataIndex: 'PART_NAME', align:'center'},
				{header: "单位", dataIndex: 'UNIT', align:'center'},
				{header: "最小包装数", dataIndex: 'MINI_PACK', align:'center'}
		      ];
	}

	
	//全选checkbox
	function myCheckBox(value,metaDate,record)
	{
		return String.format("<input type='checkbox' name='partIds' value='" + value + "'/>");
	}
	
	//保存前校验是否选择配件信息
	function addItemPart(){
		var chk = document.getElementsByName("partIds");
		var l = chk.length;
		var cnt = 0;
		for(var i=0;i<l;i++)
		{
			if(chk[i].checked)
			{
				cnt++;
			}
		}
		if(cnt==0)
		{
			MyDivAlert("请选择配件信息");
			return;
		}
        
		MyDivConfirm("确认添加配件信息？",putForword);
	}
	
	//保存数据到临时表
	function putForword() {
		var flag = document.getElementById("flag").value;
		var orderId = document.getElementById("orderId").value;
		if (flag == '1') {
			//索赔申请页面
			var signNo = document.getElementById("signNo").value;
			var claimId = document.getElementById("claimId").value;
			var url = "<%=contextPath%>/partsmanage/common/PartClaimItemMemory/addPartClaimItem.json?signNo=" +  signNo + "&claimId=" + claimId;
			makeNomalFormCall(url,showPartValue,'fm');
		} else if(flag == 2)
		{
			makeNomalFormCall("<%=contextPath%>/partsmanage/purchase/PurchaseOrderSearch/addDoNoNullPart.json?orderId=" + orderId,showPartValue,'fm','queryBtn');
		}else
		{
			//采购订单新增配件页面
			makeNomalFormCall("<%=contextPath%>/partsmanage/common/PartMemory/addPartInfoSet.json",showPartValue,'fm','queryBtn');
		}
	}
	
	//回调方法
	function showPartValue(json){
		parent._hide();
		showPart(json);
	}
	
	//刷新父页面
	function showPart(json){
		if (parent.$('inIframe')) {
			parentContainer.addPartInfo(json);
		} else {
			parent.addPartInfo(json);
		}
	}
	
</script>
</body>
</html>