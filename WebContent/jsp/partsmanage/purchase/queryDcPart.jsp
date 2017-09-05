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
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta http-equiv="X-UA-Compatible" content="IE=7">
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>配件采购订单维护</title>
</head>
<script language="JavaScript">

	//初始化方法
	function doInit()
	{
		loadcalendar();  //初始化时间控件
		__extQuery__(1); 
	}

</script>
<body>
<div class="navigation">
<img src="<%=request.getContextPath()%>/img/nav.gif" />&nbsp;当前位置： 配件管理 &gt; 配件采购 &gt;配件采购订单维护&gt;备件供货查询</div>
<form name="fm" id="fm" method="post">
   <table class="table_edit" >
  	 <tr>
    	 <th colspan="6"><img class="nav" src="<%=request.getContextPath()%>/img/subNav.gif" /> 基本信息</th>
     </tr>
     <tr>
       <td class="table_query_2Col_label_4Letter">配件代码：</td>
       <td class="table_query_2Col_input">
       	  <input type="text" name="partCode" id="partCode" class="middle_txt" />
       </td>
       <td class="table_query_2Col_label_4Letter">配件名称：</td>
       <td class="table_query_2Col_input">
       	  <input type="text" name="partName" id="partName" class="middle_txt" />
       </td>
     </tr>
     <tr>
       <td class="table_query_2Col_label_4Letter">供货方：</td>
       <td class="table_query_2Col_input">
       		<select name="dcId" id="dcId" class="short_sel">
		        <option value="">-请选择-</option>
				 <c:forEach items="${dcList}" var="dc">
				 	<option value="<c:out value="${dc.DC_ID}"/>" 
				 		<c:if test="${dcId == dc.DC_ID}">
                             selected
                        </c:if>
                    ><c:out value="${dc.DC_NAME}"/></option>
				 </c:forEach> 
            </select>
        </td> 
        <td>    
       		&nbsp;
       	</td>
       	<td class="table_query_2Col_input">    
       		<input type="button" id="queryBtn" onclick="__extQuery__(1);" class="normal_btn" style="width=8%" value="查询"/>
       	</td>
     </tr>
    </table>
    <jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
	<table class="table_edit">
		<tr>
		  <td align="center"><input type="button" name="BtnNo" value="关闭" class="normal_btn" onclick="_hide()">
          </td>
        </tr>
	</table>
</form>
<script language="JavaScript">
	var myPage;

	var url = "<%=contextPath%>/partsmanage/purchase/PurchaseOrderSearch/queryDCPartStock.json";
				
	var title = null;

	var columns = [
				{header: "配件号", dataIndex: 'PART_CODE', align:'center'},
				{header: "配件名称", dataIndex: 'PART_NAME', align:'center'},
				{header: "单位", dataIndex: 'UNIT', align:'center'},
				{header: "最小包装数", dataIndex: 'MINI_PACK', align:'center'},
				{header: "有无库存", dataIndex: 'QUANTITY', align:'center'}
		      ];
	
</script>
</body>
</html>