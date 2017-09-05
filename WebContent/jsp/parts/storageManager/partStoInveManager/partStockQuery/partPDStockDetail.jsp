<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" pageEncoding="UTF-8"%>
<%
    String contextPath = request.getContextPath();
%>
<%@ page import="com.infodms.dms.common.Constant"%>
<%@taglib uri="/jstl/cout" prefix="c"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>盘点封存详情</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<script type="text/javascript" >
$(function(){
	__extQuery__(1);
});

var myPage;
var showType = "pdfc";
var url = "<%=contextPath%>/parts/storageManager/partStoInveManager/stockQueryAction/showPDDetSearch.json?showType="+showType;
var title = null;
var columns = [
				{header: "序号", dataIndex: 'DTL_ID', renderer:getIndex},
				{header: "封存单号", dataIndex: 'CHANGE_CODE'},
				{header: "配件编码", dataIndex: 'PART_OLDCODE'},
				{header: "配件名称", dataIndex: 'PART_CNAME'},
				{header: "件号", dataIndex: 'PART_CODE'},
				{header: "业务类型", dataIndex: 'CHANGE_REASON',renderer:getItemValue},
				{header: "调整类型", dataIndex: 'CHANGE_TYPE',renderer:getItemValue},
				{header: "封存数量", dataIndex: 'UN_CLOSE_QTY',style:'text-align:right; padding-right:3%;'},
				{header: "备注", dataIndex: 'REMARK',style:'text-align:left;'},
				{header: "封存人", dataIndex: 'NAME',style:'text-align:left; padding-left:1%;'},
				{header: "封存日期", dataIndex: 'CREATE_DATE'}
				
	      	  ];

function searchInfo()
{
	__extQuery__(1);
}

//获取封存类型
   function getFCType(value, meta, record) {
       var str = "盘盈";
       if (4 == value) {
           str = "盘亏";
           return String.format("<font color='red'>" + str + "</font>");
       }
       return String.format(str);
   }

//获取出入库类型
   function getOptionType(value, meta, record) {
       var stateVal = "2";
       var str = "封存";
       if (stateVal == value) {
           str = "解封";
           return String.format("<font color='red'>" + str + "</font>");
       }
       return String.format(str);
   }

//导出
function exportPartPreeDetailExcel(){
	document.fm.action="<%=contextPath%>/parts/storageManager/partStoInveManager/stockQueryAction/exportPDDetExcel.do";
	document.fm.target="_self";
	document.fm.submit();
}

function getDate()
{
	var dateS = "";
	var dateE = "";
	var myDate = new Date();
    var year = myDate.getFullYear();   //获取完整的年份(4位,1970-????)
    var moth = myDate.getMonth();      //获取当前月份(0-11,0代表1月)
    if(moth < 10)
    {
    	if(0 < moth)
	    {
	    	moth = "0" + moth;
	    }
	    else
	    {
	    	year = myDate.getFullYear() - 1;
	    	moth = moth + 12;
	    	if(moth < 10)
		    {
	    		moth = "0" + moth;
		    }
	    }
    }
    var day = myDate.getDate();       //获取当前日(1-31)
    if(day < 10)
    {
    	day = "0" + day;
    }
    
    dateS = year + "-" + moth + "-" + day;

    moth = myDate.getMonth() + 1;	//获取当前月份(0-11,0代表1月)
    if(moth < 10)
    {
    	moth = "0" + moth;
    }
    
    dateE = myDate.getFullYear() + "-" + moth + "-" + day; 

    document.getElementById("checkSDate").value = dateS;
    document.getElementById("checkEDate").value = dateE;
}
</script> 
</head>
<body>
	<div class="navigation">
		<img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 基础信息管理 &gt; 配件基础信息查询 &gt; 配件库存查询 &gt; 盘点封存详情
	</div>
	<form name='fm' id='fm'>
		<input type="hidden" name="partId" id="partId" value="${partId }" />
		<input type="hidden" name="whId" id="whId" value="${whId }" />
		<div class="form-panel">
			<h2><img src="<%=contextPath%>/jmstyle/img/search-ico.png" style="margin-bottom: -13px;"/>查询条件</h2>
			<div class="form-body">
				<table class="table_query">
					<tr>
						<td width="20%" class="right">备注：</td>
						<td width="30%">
							<input class="middle_txt" id="remark" name="remark" type="text" value="" />
						</td>
						<td width="20%" class="right">封存人：</td>
						<td width="30%">
							<input class="middle_txt" id="fcName" name="fcName" type="text" value="" />
						</td>
					</tr>
					<tr>
						<td colspan="6" align="center">
							<input class="u-button" type="button" value="查 询" name="BtnQuery" id="queryBtn" onclick="searchInfo()" />
							<input class="u-button" type="reset" value="重 置">
							<input class="u-button" type="button" name="button1" value="关 闭" onclick="_hide();" />
						</td>
					</tr>
				</table>
			</div>
		</div>
		<!--分页 begin -->
		<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
		<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
		<!--分页 end -->
	</form>
</body>
</html>