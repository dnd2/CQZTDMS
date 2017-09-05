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
<link href="<%=request.getContextPath()%>/jsp/demo/Fixed.css" type="text/css" rel="stylesheet" />
<script src="<%=request.getContextPath()%>/jsp/demo/Fixed.js"></script>
<% String contextPath = request.getContextPath(); %>
<script type="text/javascript" src="<%=contextPath%>/js/jslib/CalendarZYW.js"></script>
<script type="text/javascript">
var Options = {
		cells  : 3
	}
	function doInit(){
		loadcalendar();  //初始化时间控件
	}

	function expotData(){
	   fm.action="<%=contextPath%>/report/dmsReport/ClaimQueryReport/expotDataClaimAccount.do";
       fm.submit();
	}
</script>
</head>
<body >
<div class="navigation">
<img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置：售后服务管理&gt;售后报表管理&gt;索赔结算总汇查询
</div>
<form name="fm" id="fm" method="post" enctype="multipart/form-data">
<!-- 查询条件 begin -->
<table width=100% border="0" align="center" cellpadding="1" cellspacing="1" class="table_query">
	<!-- 查询条件 -->
	<tr>
		
		<td class="table_query_2Col_label_5Letter" nowrap="true">结算状态：</td>
		<td>
			 <select style="width: 152px;" name="isInvoice" id="isInvoice">
				 <option value="" >-请选择-</option>
 				 <option value="0" >已审核</option>
 				 <option value="1" >已结算</option>
 				 <option value="2" >已开票</option>
 				 <option value="3" >已收票</option>
 				 <option value="4" >已验收</option>
 				 <option value="5" >已转账</option>
             </select>
		</td>
		<td class="table_query_2Col_label_5Letter" nowrap="true">服务站号：</td>
      	<td><input name="dealerCode" type="text" id="dealerCode"  class="middle_txt" maxlength="30"/></td>
      	<td class="table_query_2Col_label_5Letter" nowrap="true" >结算时间：</td>
	 	<td nowrap="nowrap"  align="left">
  		  <input class="short_txt" id="beginTime" name="beginTime" datatype="1,is_date,10"
                 value="${start}" maxlength="10" group="beginTime,endTime"/>
          <input class="time_ico" value=" " onclick="showcalendar(event, 'beginTime', false);" type="button"/>
          	至
          <input class="short_txt" id="endTime" name="endTime" datatype="1,is_date,10"
                 value="${end}" maxlength="10" group="beginTime,endTime"/>
          <input class="time_ico" value=" " onclick="showcalendar(event, 'endTime', false);" type="button"/>
        </td>
		<td width="15%"></td>
	</tr>
	<tr>
		<td class="table_query_2Col_label_5Letter" nowrap="true">结算编号：</td>
      	<td ><input name="balanceNo" type="text" id="balanceNo"  class="middle_txt" maxlength="30"/></td>
        <td class="table_query_2Col_label_6Letter" nowrap="true">结算人：</td>
      	<td ><input name="applyPersonName" type="text" id="applyPersonName" class="middle_txt" maxlength="30"/></td>
		<td class="table_query_2Col_label_5Letter" nowrap="true">服务站简称：</td>
		<td ><input name="dealerName" type="text" id="dealerName"  class="middle_txt" maxlength="30"/></td>
		<td ></td>
	</tr>
	<tr>
		
      	<td class="table_query_2Col_label_5Letter" nowrap="true" >转账时间：</td>
	 	<td nowrap="nowrap"  align="left">
  		  <input class="short_txt" id="transBeginTime" name="transBeginTime" datatype="1,is_date,10"
                 value="${start}" maxlength="10" group="transBeginTime,transEndTime"/>
          <input class="time_ico" value=" " onclick="showcalendar(event, 'transBeginTime', false);" type="button"/>
          	至
          <input class="short_txt" id="transEndTime" name="transEndTime" datatype="1,is_date,10"
                 value="${end}" maxlength="10" group="transBeginTime,transEndTime"/>
          <input class="time_ico" value=" " onclick="showcalendar(event, 'transEndTime', false);" type="button"/>
        </td>
		<td width="15%"></td>
	</tr>
    <tr>
    	<td align="center" colspan="7">
    		<input type="button" name="queryBtn" id="queryBtn"  value="查询"  class="normal_btn" onClick="doQuery();" >
    		<input type="button" name="btnQuery" id="btnQuery"  value="导出"  class="normal_btn" onClick="expotData();" >
    	</td>
    </tr>
    
    <tr>
    	<td align="center" colspan="7">
	    	<font color="red">总金额：</font> 
	    	<input type="text" class="short_txt" style="border: none" id="sumAmount" name="sumAmount" readonly />
	        <font color="red">总条数：</font>
	        <input type="text" class="short_txt" style="border: none" id="sumNum" name="sumNum" readonly />
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
	var url = "<%=contextPath%>/report/dmsReport/ClaimQueryReport/claimAccountQuery.json";
	var title = null;
	var columns = [
		{header: "序号",sortable: false,align:'center',renderer:getIndex},
		{header: "商家代码", dataIndex: 'DEALER_CODE', align:'center'},
		{header: "商家简称", dataIndex: 'DEALER_SHORTNAME', align:'center'},
		{header: "状态", dataIndex: 'STATUS', align:'center'},
		{header: "索赔月份", dataIndex: 'SUB_DATE', align:'center'},
		{header: "结算编号", dataIndex: 'BALANCE_NO', align:'center'},
		{header: "保养费", dataIndex: 'FREE_AMOUNT', align:'center'},
		{header: "PDI费", dataIndex: 'PDI_AMOUNT', align:'center'},
		{header: "索赔材料费", dataIndex: 'PART_AMOUNT', align:'center'},
		{header: "索赔工时费", dataIndex: 'LABOUR_AMOUNT', align:'center'},
		{header: "外出救援", dataIndex: 'OUT_AMOUNT', align:'center'},
		{header: "辅料", dataIndex: 'ACCESSORIES_AMOUNT', align:'center'},
		{header: "服务活动费", dataIndex: 'SERVICE_AMOUNT', align:'center'},
		{header: "其他金额", dataIndex: 'OTHER_AMOUNT', align:'center'},
		{header: "旧件运费", dataIndex: 'RETURN_AMOUNT', align:'center'},
		{header: "误判金额", dataIndex: 'FINE_AMOUNT', align:'center'},
		{header: "正负激励费用", dataIndex: 'ACC_AMOUNT', align:'center'},
		{header: "合计", dataIndex: 'SUM_AMOUNT', align:'center'},
		{header: "保养台次", dataIndex: 'BYTC', align:'center'},
		{header: "PDI台次", dataIndex: 'PDITC', align:'center'},
		{header: "索赔台次", dataIndex: 'SPTC', align:'center'},
		{header: "活动台次", dataIndex: 'HDTC', align:'center'},
		{header: "结算日期", dataIndex: 'CLAIM_DATE', align:'center'},
		{header: "结算人", dataIndex: 'CLAIM_BY', align:'center'},
		{header: "开票日期", dataIndex: 'KP_DATE', align:'center'},
		{header: "开票人", dataIndex: 'KP_BY', align:'center'},
		{header: "收票日期", dataIndex: 'SP_DATE', align:'center'},
		{header: "收票人", dataIndex: 'SP_BY', align:'center'},
		{header: "验票日期", dataIndex: 'YP_DATE', align:'center'},
		{header: "验票人", dataIndex: 'YP_BY', align:'center'},
		{header: "转款日期", dataIndex: 'ZZ_DATE', align:'center'},
		{header: "转款人", dataIndex: 'ZZ_BY', align:'center'}
	];

	function doQuery() {
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
			// ==================各个开关start by chenyu===================
			// 自定义每页条数
			var customPageSizeFlag = json.customPageSizeFlag;
			if(customPageSizeFlag){
				ps.customPageSizeFlag = customPageSizeFlag;
			}
			// ==================各个开关end by chenyu=====================
	        $("_page").hide();
	        $('myGrid').show();
	        new createGrid(title, columns, $("myGrid"), ps).load();
	        //分页
	        myPage = new showPages("myPage", ps, url);
	        myPage.printHtml();
	        hiddenDocObject(2);
			// 动态设置tablediv的高度
			setGridHeight();
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
	  //2015-05-08 ranke 冻结列
		if(Options){//冻结列使用
			document.getElementById('myGrid').style.overflowX = "hidden";
			frozenColumn();
			//setTimeout('frozenColumn()',500);
		}
	}
</script>
<!--页面列表 end -->
</html>