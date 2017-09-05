<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="com.infodms.dms.common.Constant"%>
<%@taglib uri="/jstl/cout" prefix="c"%>
<%
	String contextPath = request.getContextPath();
	
%>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta http-equiv="X-UA-Compatible" content="IE=7">
<title>配件价格查询</title>
</head>
<body onload="__extQuery__(1);">
<form name="fm" id="fm" method="post">
  <div class="wbox">
    <div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置：基础信息管理 &gt; 配件基础信息查询 &gt; 配件价格查询 </div>
    <input type="hidden" name="orgType" id="orgType" value="OEM"/>
  </div>
	<table class="table_query">
		<th colspan="6"><img src="<%=contextPath%>/img/subNav.gif" alt="" class="nav" /> 查询条件</th>
	    <tr>
	      <td width="10%" align="right" >配件编码：</td>
	      <td width="20%"  ><input class="middle_txt" type="text"  name="PART_OLDCODE"  /></td>
	      <td width="10%" align="right"  >配件名称：</td>
	      <td  width="20%"><input class="middle_txt" type="text"  name="PART_CNAME" /></td>
          <td width="10%" align="right" >件号：</td>
          <td width="20%"  ><input class="middle_txt" type="text" name="PART_CODE"  /></td>
      </tr>
      <c:if test="${poseBusType != 10781011 }">
      <tr>
				<td align="right">结算基地：</td>
	            <td>
	                <script type="text/javascript">
	                    genSelBoxExp("PART_IS_CHANGHE", <%=Constant.PART_IS_CHANGHE%>, "95411001", true, "short_sel", "", "false", '');
	                </script>
	            </td>
                <td>&nbsp;</td>
            </tr>
            </c:if>
    <tr>
    	<td   align="center" colspan="6" >
    	<input class="normal_btn" type="button" value="查 询" name="BtnQuery" id="queryBtn" onclick="__extQuery__(1);"/>
   	   </td>    
	  </tr>
	</table>
  <jsp:include page="${contextPath}/queryPage/orderHidden.html" />
  <jsp:include page="${contextPath}/queryPage/pageDiv.html" />

</form>
</body>

<script type="text/javascript" >

	autoAlertException();
	var myPage;

	var url = "<%=contextPath%>/parts/baseManager/partBaseQuery/partSaleManager/PartSalePriceActon/queryPartSaleCustomerPrice.json";
				
	var title = null;

	var columns = [
				{header: "序号",  style: 'text-align:right',renderer:getIndex},
				{header: "配件编码", dataIndex: 'PART_OLDCODE',  style: 'text-align:left'},
				{header: "配件名称", dataIndex: 'PART_CNAME',  style: 'text-align:left'},
                {header: "件号", dataIndex: 'PART_CODE',  style: 'text-align:left'},
                {header: "结算基地", dataIndex: 'PART_IS_CHANGHE', style: "text-align: center", renderer: getItemValue},
                {header: '${map.prciceName1}(元)', dataIndex: 'SALE_PRICE1',  style: 'text-align:right'}
			/*	{header: "修改日期", dataIndex: 'UPDATE_DATE',  style: 'text-align:right'},
				{header: "修改人", dataIndex: 'NAME',  style: 'text-align:right'}*/
	//			{id:'action',header: "操作",sortable: false,dataIndex: 'PRICE_ID',renderer:myLink , style: 'text-align:right'}
		      ];
  
	  
	//导出销售价格信息
	function loadPartPriceExcel(){
		fm.action = "<%=contextPath%>/parts/baseManager/partBaseQuery/partSaleManager/PartSalePriceActon/exportPartPriceExcelOEM.do";
		fm.submit();
	}
	
	function myLink(value,meta,record)
	{
  		return String.format("<a href=\"#\" onclick='viewPriceList(\""+value+"\")'>[查看价格列表]</a>");
	}
	
	//查看价格列表
	function viewPriceList(){
		fm.action='<%=contextPath%>/parts/baseManager/partBaseQuery/partSaleManager/PartSalePriceActon/viewPriceListInit.do';
		fm.submit();
	}
	
</script>
</html>
