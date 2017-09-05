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
	   fm.action="<%=contextPath%>/report/dmsReport/ClaimQueryReport/expotDataPartMonthlyClaimTOP20.do";
       fm.submit();
	}
	function showCarModel(){
		OpenHtmlWindow('<%=contextPath%>/claim/oldPart/EmergencyDevice/showCarType.do',800,500);
	}
	function showCarRebate(){
		OpenHtmlWindow('<%=contextPath%>/claim/oldPart/EmergencyDevice/showCarRebate.do',800,500);
	}
</script>
</head>
<body >
<div class="navigation">
<img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置：售后服务管理&gt;售后报表管理&gt;月索赔零件数量TOP20查询
</div>
<form name="fm" id="fm" method="post">
<!-- 查询条件 begin -->
<table width=100% border="0" align="center" cellpadding="1" cellspacing="1" class="table_query">
	<!-- 查询条件 -->
	<tr>
		<td class="table_query_2Col_label_6Letter" nowrap="true">零件名：</td>
      	<td><input name="partName" type="text" id="partName" class="middle_txt" maxlength="30"/></td>
		
		<td class="table_query_2Col_label_5Letter" nowrap="true">零件号：</td>
      	<td><input name="partCode" type="text" id="partCode"  class="middle_txt" maxlength="30"/></td>
      	<td width="5%"class="table_query_2Col_label_5Letter" nowrap="true" >索赔时间：</td>
		 <td nowrap="nowrap">
          	<div align="left">
          		  <input class="short_txt" id="beginTime" name="beginTime" datatype="1,is_date,10"
                         value="${start}" maxlength="10" group="beginTime,endTime"/>
                  <input class="time_ico" value=" " onclick="showcalendar(event, 'beginTime', false);" type="button"/>
                  	至
                  <input class="short_txt" id="endTime" name="endTime" datatype="1,is_date,10"
                         value="${end}" maxlength="10" group="beginTime,endTime"/>
                  <input class="time_ico" value=" " onclick="showcalendar(event, 'endTime', false);" type="button"/>
          	</div>
          </td>
	</tr>
	<tr>
		<td width="10%"  class="table_query_2Col_label_5Letter" nowrap="true">车型：</td>
		<td width="10%">
				<input type="text" id="cars" onclick="showCarModel()" class="middle_txt" name="cars" readonly="readonly">
		<input type="hidden" id="carModel"  name="carModel">
		</td>
		<td width="10%"  class="table_query_2Col_label_5Letter" nowrap="true">车系：</td>
		<td>
			<input type="text" id="carRebate"  name="carRebate" class="middle_txt" maxlength="30" datatype="0,is_null,60" readonly="readonly">
			<input name="dlbtn" type="button" class="mini_btn" onclick="CarsRebateGroup('','carRebate','false','','');" value="..." />
            <input type="button" class="normal_btn" onclick="txtClr('carRebate')" value="清 空" id="clrBtn" /> 
		</td>
	</tr>
    <tr>
    	<td align="center" colspan="6">
    		<input type="button" name="queryBtn" id="queryBtn"  value="查询"  class="normal_btn" onClick="doQuery();" >
    		<input type="button" name="btnQuery" id="btnQuery"  value="导出"  class="normal_btn" onClick="expotData();" >
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
	var url = "<%=contextPath%>/report/dmsReport/ClaimQueryReport/partMonthlyClaimTOP20Query.json";
	var title = null;
	var columns = [
		{header: "序号",sortable: false,align:'center',renderer:getIndex},
		{header: "主损件代码", dataIndex: 'PART_CODE', align:'center'},
		{header: "主损件名称", dataIndex: 'PART_NAME', align:'center'},
		{header: "主损件数量", dataIndex: 'QUANTITY', align:'center'},
		{header: "金额", dataIndex: 'AMOUNT', align:'center'},
		{header: "数量占比", dataIndex: 'SLZB', align:'center'},
		{header: "备注", dataIndex: 'REMARK', align:'center', renderer: myLink}
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
	}

	//设置超链接
    function myLink(value, meta, record) {
        var partCode = record.data.PART_CODE;
        if(partCode == null){
	        return String.format(value);
        }else{
            var dtl = "<a href=\"#\" onclick='detailQuery(\"" + partCode + "\")'>[明细查看]</a>";
        	return String.format(dtl);
        }
    }

    function detailQuery(partCode){
    	var buttonFalg = "disabled";
    	var beginTime = document.getElementById("beginTime").value;
    	var endTime = document.getElementById("endTime").value;
    	var carRebate = document.getElementById("carRebate").value;
    	OpenHtmlWindow("<%=contextPath%>/report/dmsReport/ClaimQueryReport/partMonthlyClaimTOP20Dtl.do?partCode="+partCode+"&beginTime="+beginTime+"&endTime="+endTime+"&carRebate="+carRebate,800,500);
    }

    function txtClr(value){
    	document.getElementById(value).value = "";
    }
    function showSalesDisId(value){
    	document.getElementById('carRebate').value = value;
    }  
</script>
<!--页面列表 end -->
</html>