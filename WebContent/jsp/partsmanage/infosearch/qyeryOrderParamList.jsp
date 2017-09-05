<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%
	String contextPath = request.getContextPath();
%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta http-equiv="X-UA-Compatible" content="IE=7">
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>配件采购订单查询</title>

</head>
<body>
<img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 配件管理&gt;基本信息查询&gt;配件订单业务参数
<form method="post" name="fm" id="fm">
	<table class="table_edit">
     <tr>
       <td class="table_query_2Col_label_6Letter">经销商代码：</td>
       <td class="table_query_2Col_input">
      	<input class="middle_txt" id="dealerCode" name="dealerCode" value="" type="text"/>
        <input class="mark_btn" type="button" value="&hellip;" onclick="showOrgDealer('dealerCode','dealerId','true','',true)"/>     
        <input class="normal_btn" type="button" value="清除" onclick="reset();"/>
        <input id="dealerId" name="dealerId" value="" type="hidden"/>
      </td>
      <td class="table_query_2Col_label_6Letter">经销商名称：</td>
      <td>
      	<input name="dealerName" type="text" id="dealerName"  class="middle_txt"/>
      </td>
  	</tr>
  	<tr>
      <td>&nbsp;</td>
      <td>&nbsp;</td>
      <td>&nbsp;</td>
      <td>&nbsp;</td>
      <td>&nbsp;</td>
      <td>&nbsp;</td>
      <td>&nbsp;</td>
      <td>&nbsp;</td>
      <td><input type="button" name="BtnQuery" id="queryBtn"  value="查询"  class="normal_btn" onClick="__extQuery__(1)">&nbsp;</td>
      <td><input type="button" name="BtnQuery" id="queryBtn"  value="新增"  class="normal_btn" onClick="addRule()" ></td>
  	</tr>
</table>
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
</form>
<form name="form1" style="display:none">
   <table class="table_list" id="table1" >
  	  <tr>
  	  	<th align="center">
    		<input class="normal_btn" type="button" value="修改" onclick="updateRule()">
       </th>
  	  </tr>
   </table>
  </form>
<script type="text/javascript" >

	document.form1.style.display = "none";
	var HIDDEN_ARRAY_IDS=['form1'];
	
	var myPage;

	var url = "<%=contextPath%>/partsmanage/infoSearch/PartsmanageOrderParamInfo/queryOrderParam.json";
				
	var title = null;

	var columns = [
	{id:'action',header: "<input type='checkbox' name='checkAll' onclick='selectAll(this,\"dealerIds\")' />", width:'8%',sortable: false,dataIndex: 'dealerId',renderer:myCheckBox},
				{header: "经销商代码", dataIndex: 'dealerCode', align:'center'},
				{header: "经销商简称", dataIndex: 'dealerName', align:'center'},
				{header: "折扣", dataIndex: 'discountRate', align:'center'},
				{header: "订单最大行数", dataIndex: 'orderMaxLines', align:'center'},
				{header: "周期内允许上报次数", dataIndex: 'allowSubmitTimes', align:'center'},
				{header: "周期类型", dataIndex: 'cycleType', align:'center',renderer:getItemValue},
				{header: "开始日/结束日/处理日", dataIndex: 'sehDate', align:'center'}
		      ];
		    
//设置超链接  begin      

	//全选checkbox
	function myCheckBox(value,metaDate,record)
	{
		return String.format("<input type='checkbox' name='dealerIds' value='" + value + "'/>");
	}
	
	//修改规则
	function updateRule()
	{
	
		var chk = document.getElementsByName("dealerIds");
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
             MyAlert("请选择要修改的规则！");
             return;
        }
  		fm.action = "<%=contextPath%>/partsmanage/infoSearch/PartsmanageOrderParamInfo/updateRule.do";
		fm.submit();
	}

	//新增规则
	function addRule(){
		fm.action = "<%=contextPath%>/partsmanage/infoSearch/PartsmanageOrderParamInfo/addRule.do";
		fm.submit();
	}
	
	//清除供应商代码
	function reset(){
		document.getElementById("SUPPLIER_CODE").value = "";
	}

	
//设置超链接 end
	
</script>
</body>
</html>