<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.Constant" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c" %>
<%@taglib uri="/jstl/change" prefix="change" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>售后报表</title>
<% String contextPath = request.getContextPath(); %>
<script type="text/javascript" src="<%=contextPath%>/js/jslib/CalendarZYW.js"></script>
<script type="text/javascript">

	function doInit(){
		loadcalendar();  //初始化时间控件
	}

	function expotData(){
	   fm.action="<%=contextPath%>/ClearTransferAction/TransferWithoutTaxExport.do";
       fm.submit();
	}
</script>
</head>
<body >
<div class="navigation">
<img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置：售后服管理&gt;索赔结算管理&gt;服务站已结账不含税金额
</div>
<form name="fm" id="fm" method="post" enctype="multipart/form-data">
<!-- 查询条件 begin -->
<table width=100% border="0" align="center" cellpadding="1" cellspacing="1" class="table_query">
	<!-- 查询条件 -->
	<tr>
      	<td class="table_query_2Col_label_5Letter" nowrap="true" >转账时间：</td>
	 	<td nowrap="nowrap"  align="left">
  		  <input class="short_txt" id="beginTime" name="beginTime" datatype="1,is_date,10"
                 value="${start}" maxlength="10" group="beginTime,endTime"/>
          <input class="time_ico" onclick="showcalendar(event, 'beginTime', false);" type="button"/>
          	至
          <input class="short_txt" id="endTime" name="endTime" datatype="1,is_date,10"
                 value="${end}" maxlength="10" group="beginTime,endTime"/>
          <input class="time_ico" onclick="showcalendar(event, 'endTime', false);" type="button"/>
        </td>
		<td width="15%"></td>
	</tr>
    <tr>
    	<td align="center" colspan="7">
    		<input type="button" name="btnQuery" id="queryBtn"  value="查询"  class="normal_btn" onClick="doQuery();" >
    		<input type="button" name="btnQuery" id="btnQuery"  value="导出"  class="normal_btn" onClick="expotData();" 

>
    	</td>
    </tr>
</table>
<!-- 查询条件 end -->
<!--分页 begin -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
<!--分页 end -->
</form>
</body>
<!--页面列表 begin -->
<script type="text/javascript" >
	var myPage;
	var url = "<%=contextPath%>/ClearTransferAction/TransferWithoutTaxQuery.json";
	var title = null;
	var columns = [
		{header: "序号",sortable: false,align:'center',renderer:getIndex},
		{header: "经销商代码", dataIndex: 'DEALER_CODE', align:'center'},
		{header: "经销商简称", dataIndex: 'DEALER_SHORTNAME', align:'center'},
		{header: "结算编号", dataIndex: 'CLEARING_NUMBER', align:'center'},
		{header: "转账日期", dataIndex: 'TRANSFER_TICKETS_DATE', align:'center'},
		{header: "发票批号", dataIndex: 'LABOUR_RECEIPT', align:'center'},
		{header: "发票号码", dataIndex: 'PART_RECEIPT', align:'center'},
		{header: "金额", dataIndex: 'AMOUNT_OF_MONEY', align:'center'},
		{header: "税额", dataIndex: 'TAX_RATE_MONEY', align:'center'},
		{header: "合计", dataIndex: 'AMOUNT_SUM', align:'center'}
		
	
	];
    
	function doQuery() {
	 var beginTime =document.getElementById("beginTime").value;
	 var endTime =document.getElementById("endTime").value;
	   if(""==beginTime  || ""==endTime){
	      MyAlert("提示：请选择转账时间！");
	      return;
	   }
	    //执行查询
	    __extQuery__(1);
	}
	
	function callBack(json) {
	    var ps;
	    //设置对应数据
	    if (Object.keys(json).length > 0) {
	        keys = Object.keys(json);
	        for (var i = 0; i < keys.length; i++) {
	            if (keys[i] == "ps") {
	                ps = json[keys[i]];
	                break;
	            }
	        }
	    }

	    //生成数据集
	    if (ps.records != null) {
	        $("_page").hide();
	        $('myGrid').show();
	        new createGrid(title, columns, $("myGrid"), ps).load();
	        //分页
	        myPage = new showPages("myPage", ps, url);
	        myPage.printHtml();
	        hiddenDocObject(2);
	    } else {
	        $("_page").show();
	        $("_page").innerHTML = "<div class='pageTips'>没有满足条件的数据</div>";
	        $("myPage").innerHTML = "";
	        removeGird('myGrid');
	        $('myGrid').hide();
	        hiddenDocObject(1);
	    }
	    if (null != json) {
	        $("sumAmount").value = json.sumAmount;
	        $("sumNum").value = json.sumNum;
	    }
	}
</script>
<!--页面列表 end -->
</html>