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
<title>配件采购订单维护</title>
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
  <img src="<%=request.getContextPath()%>/img/nav.gif" />&nbsp;当前位置： 配件管理&gt;经销商配件采购&gt;配件采购订单预审核</div>
<form id="fm" name="fm" method="post">
<input type="hidden" name="orderId" id="orderId" value="<c:out value="${orderId}"/>"/>
<input type="hidden" name="status" id="status" value=""/>
<table class="table_edit">
   <tr>
  	   <th colspan="7"><img class="nav" src="<%=request.getContextPath()%>/img/subNav.gif" />审核操作</th> 
   </tr>  
   <tr> 
       <td class="table_query_3Col_label_4Letter">审核意见：</td>
       <td align=left>
         <textarea name="remark" id="remark" rows="2" cols="70" ></textarea>
         <input type="button" onclick="forwordPart(0)" class="normal_btn" style="width=8%" value="通过"/>
         <input type="button" onclick="forwordPart(1)" class="normal_btn" style="width=8%" value="驳回"/>
         <input type="button" onclick="javascript:history.go(-1);" class="normal_btn" style="width=8%" value="返回"/>
       </td>
   </tr>
</table>
<br>
<table class="table_edit" >
      <tr>
        <th colspan="6"><img src="<%=request.getContextPath()%>/img/subNav.gif" alt="" class="nav" /> 基本信息</th>
      </tr>
      <tr>
        <td class="table_query_3Col_label_6Letter">采购订单编号： </td>
        <td class="table_query_3Col_input">
        	<c:out value="${orderInfo.ORDER_NO}"/>
        </td>
        <td class="table_query_3Col_label_6Letter">要求到货时间：</td>
        <td class="table_query_3Col_input">
        	<c:out value="${orderInfo.REQUIRE_DATE}"/>
        </td>
        <td class="table_query_3Col_label_6Letter">运输类型：</td>
        <td class="table_query_3Col_input">
        	<script type="text/javascript">
        		writeItemValue(<c:out value="${orderInfo.TRANS_TYPE}"/>)
        	</script>
        </td>
      </tr>
      <tr>
        <td class="table_query_3Col_label_6Letter">供货方：</td>
        <td height="16%" >
        	<c:out value="${orderInfo.DC_NAME}"/>
        </td>
        <td class="table_query_3Col_label_6Letter">供货方资金明细：</td>
        <td width="17%">
        	<c:out value="${orderInfo.AMOUNT}"/>
        </td>
        <td class="table_query_3Col_label_6Letter">&nbsp;</td>
        <td class="table_query_3Col_input">&nbsp;</td>
      </tr>
      <tr>
        <td class="table_query_3Col_label_6Letter">备注：</td>
        <td height="16%" colspan="5" align="left">
        	<c:out value="${orderInfo.REMARK}"/>
        </td>
      </tr>
    </table>
<!--分页 begin -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
<!--分页 end -->
</form>
<!--页面列表 begin -->
<script type="text/javascript" >

	var myPage;
//查询路径
	var url = "<%=contextPath%>/partsmanage/purchase/PurchaseOrderSearch/forwordOrderList.json";
				
	var title = "配件信息";

	var columns = [
				{header: "配件号", dataIndex: 'partCode', align:'center'},
				{header: "配件名称", dataIndex: 'partName', align:'center'},
				{header: "单位", dataIndex: 'unit', align:'center'},
				{header: "最小包装数", dataIndex: 'miniPack', align:'center'},
				{header: "二级库存", dataIndex: 'paperQuantity', align:'center'},
				{header: "安全库存", dataIndex: 'safeQuantity', align:'center'},
				{header: "本库库存", dataIndex: 'secondPaperQuantity', align:'center'},
				{header: "安全库存", dataIndex: 'secondSafeQuantity', align:'center'},
				{header: "单价", dataIndex: 'salePrice', align:'center'},
				{header: "折扣", dataIndex: 'discountRate', align:'center'},
				{header: "折扣后价格", dataIndex: 'disPrice', align:'center'},
				{header: "订购数量", dataIndex: 'orderCount', align:'center'},
				{header: "汇总(元)", dataIndex: 'orderPrice', align:'center'},
				{header: "备注", dataIndex: 'remark', align:'center'}
				
		      ];  
		     
	function forwordPart(val)
	{
		if(val==1)
		{
			if(document.getElementById("remark").value==null || document.getElementById("remark").value == "")
			{
				MyAlert("请填写驳回意见！");
				fm.remark.focus();
				return;				
			}
		}
		
		$("status").value = val;
		fm.action = "<%=contextPath%>/partsmanage/purchase/PurchaseOrderSearch/forwordPartStatus.do";
		fm.submit();
	}	   

</script>
<!--页面列表 end -->
</body>
</html>