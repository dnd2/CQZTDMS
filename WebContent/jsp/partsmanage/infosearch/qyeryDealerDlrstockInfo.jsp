<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>经销商库存明细查询</title>
<% String contextPath = request.getContextPath(); %>
<script type="text/javascript">
	function doInit(){
		
	}
</script>

</head>
<body>
<img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 配件管理&gt;基本信息查询&gt;经销商库存明细查询
<form name="fm" id="fm">
<!-- 查询条件 begin -->
<table width=100% border="0" align="center" cellpadding="1" cellspacing="1" class="table_query">
	 <tr>
      <td class="table_query_2Col_label_7Letter"><input type="radio" id="odtype" name="odtype" value="0" onclick="checkType(this.value)" checked="checked">区域：</td>
      <td>
          <input name="orgCode" type="text" id="orgCode" class="long_txt" value=""/>
		  <input name="orgSel" id="orgSel" type="button" class="mark_btn" onclick="showOrg('orgCode' ,'orgId' ,true,'')" value="&hellip;" />
		  <input class="normal_btn" type="button" id="orgRe" value="清除" onclick="resetOrg();"/>
		  <input name="orgId" type="hidden" id="orgId"/>	
      </td>
      <td>&nbsp;</td>
      <td>&nbsp;</td>
    </tr>
    <tr>
      <td class="table_query_2Col_label_7Letter"><input type="radio" id="odtype" name="odtype" value="1" onclick="checkType(this.value)">经销商代码：</td>
      <td>
      	<input class="long_txt" id="dealerCode" name="dealerCode" value="" type="text" disabled/>
        <input class="mark_btn" id="deaBtn" type="button" value="&hellip;" disabled onclick="showOrgDealer('dealerCode','','true','',true)"/>
        <input class="normal_btn" type="button" id="deaRe" value="清除" onclick="resetDealer();" disabled/>     
      </td>
      <td class="table_query_2Col_label_7Letter">经销商名称：</td>
      <td>
      	<input name="dealerName" type="text" id="dealerName" class="middle_txt" disabled/>
      </td>
    </tr>
    <tr>
      <td class="table_query_2Col_label_7Letter">配件号：</td>
      <td>
      	<input name="partCode" type="text" id="partCode" class="middle_txt"/>
      </td>
      <td class="table_query_2Col_label_7Letter">配件名称：</td>
      <td>
      	<input name="partName" type="text" id="partName" class="middle_txt"/>
      </td>
  	</tr>
  	<tr>
      <td>&nbsp;</td>
      <td>&nbsp;</td>
      <td>&nbsp;</td>
      <td>&nbsp;</td>
      <td>&nbsp;</td>
      <td align="right">
      	<input type="button" name="BtnQuery" id="queryBtn"  value="查询"  class="normal_btn" onClick="__extQuery__(1)" >
      </td>
  	</tr>
</table>
<!-- 查询条件 end -->
<!--分页 begin -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
<!--分页 end -->
</form>
<!--页面列表 begin -->
<script type="text/javascript" >
	var myPage;

	var url = "<%=contextPath%>/partsmanage/infoSearch/DealerDlrstockInfo/queryDealerDlrstockInfo.json";
				
	var title = null;

	var columns = [
				{header: "经销商", dataIndex: 'DEALER_NAME', align:'center'},
				{header: "配件号", dataIndex: 'PART_CODE', align:'center',renderer:myLink},
				{header: "配件名称", dataIndex: 'PART_NAME', align:'center'},
				{header: "单位", dataIndex: 'UNIT', align:'center'},
				{header: "账面库存", dataIndex: 'PAPER_QUANTITY', align:'center'},
				{header: "实际库存", dataIndex: 'ACTUAL_QUANTITY', align:'center'},
				{header: "销售价格", dataIndex: 'SALE_PRICE', align:'center'},
				{header: "替代配件", dataIndex: 'REPLACE_PART_ID', align:'center'},
				{header: "最小包装", dataIndex: 'MINI_PACK', align:'center'}
		      ];
		      
//设置超链接  begin      
	
	//设置超链接
	function myLink(value,meta,record)
	{
  		return String.format("<a href=\"#\" onclick='sel(\"" + record.data.PART_ID + "\",\""+record.data.DEALER_ID+"\")'>" + value + "</a>");
	}
	
	//详细页面
	function sel(value1,value2)
	{
		OpenHtmlWindow('<%=contextPath%>/partsmanage/infoSearch/DealerDlrstockInfo/queryPartDlrmoveDetail.do?partId='+value1+'&dealerId='+value2,800,500);
	}
	
	//选择组织或经销商查询
	function checkType(val){
		if(val==0)
		{
			document.getElementById("dealerCode").disabled = true;
			document.getElementById("deaBtn").disabled = true;
			document.getElementById("deaRe").disabled = true;
			document.getElementById("dealerName").disabled = true;
			document.getElementById("orgCode").disabled = false;
			document.getElementById("orgSel").disabled = false;
			document.getElementById("orgRe").disabled = false;
			document.getElementById("dealerCode").value = "";
		}
		else
		{
			document.getElementById("orgCode").disabled = true;
			document.getElementById("orgSel").disabled = true;
			document.getElementById("orgRe").disabled = true;
			document.getElementById("dealerCode").disabled = false;
			document.getElementById("deaBtn").disabled = false;
			document.getElementById("deaRe").disabled = false;
			document.getElementById("dealerName").disabled = false;
			document.getElementById("orgCode").value = "";
		}
		
	}
	
	//清除组织代码
	function resetOrg()
	{
		document.getElementById("orgCode").value = "";
	}
	
	//清除经销商代码
	function resetDealer()
	{
		document.getElementById("dealerCode").value = "";
	}

	
//设置超链接 end
	
</script>
<!--页面列表 end -->
</body>
</html>