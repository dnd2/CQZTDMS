<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@page import="com.infodms.dms.common.Constant" %>
<%@taglib uri="/jstl/cout" prefix="c" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<%
    String contextPath = request.getContextPath();
%>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <meta http-equiv="X-UA-Compatible" content="IE=7">
    <jsp:include page="${contextPath}/common/jsp_head_new.jsp"/>
    <title>考核分析报表(本部)</title>
    <script language="JavaScript">
    
	    function getYearSelect(id, name, scope, value) {
	        var date = new Date();
	        var year = date.getFullYear();    //获取完整的年份
	        var str = "";
	        str += "<select  id='" + id + "' name='" + name + "'  style='width:55px;'>";
	        //str += "<option selected value=''>-请选择-</option>";
	        for (var i = (year - scope); i <= (year + scope); i++) {
	            if (value == "") {
	                if (i == year) {
	                    str += "<option  selected value =" + i + ">" + i + "</option >";
	                } else {
	                    str += "<option   value =" + i + ">" + i + "</option >";
	                }
	            } else {
	                str += "<option  " + (i == value ? "selected" : "") + "value =" + i + ">" + i + "</option >";
	            }
	        }
	        str += "</select> 年";
	        document.write(str);
	    }
	    
	    function getMonThSelect(id, name, value) {
	        var date = new Date();
	        var month = date.getMonth() + 1;
	        value = month;
	        var str = "";
	        str += "<select  id='" + id + "' name='" + name + "'  style='width:65px;' onchange='changeSeason(this);'>";
	        //str += "<option selected value=''>-请选择-</option>";
	        for (var i = 1; i <= 12; i++) {
	            if (value == "") {
	            	str += "<option  value =" + (i < 10 ? "0" + i : i) + ">" + (i < 10 ? "0" + i : i) + "</option >";
	            }else {
	                str += "<option " + (i == value ? 'selected' : '') + " value =" + (i < 10 ? "0" + i : i) + ">" + (i < 10 ? "0" + i : i) + "</option >";
	            }
	        }
	        str += "</select> 月";
	        document.write(str);
	    }

	    function changeDiv(obj){
	    	var value = obj.value;
	    	if(value == "fws"){
	    		$("dButton").disabled = "";
	    		$("dealerName").disabled = "";
	    		$("dealerName").style.cssText = "background-color: none;";
	    	}
	    	else
		    {
	    		$("dButton").disabled = "disabled";
	    		$("dealerName").disabled = "disabled";
	    		$("dealerName").style.cssText = "background-color: #E5E3E3;";
	    	}
	    	$("searchType").value = value;
	    }
	
    </script>
</head>
<body>
<div class="wbox">
<div class="navigation"><img src="<%=request.getContextPath()%>/img/nav.gif"/>&nbsp;当前位置： 报表管理&gt;配件报表&gt;本部销售报表&gt;配件拓展率报表
</div>
<form method="post" name="fm" id="fm" enctype="multipart/form-data">
<input type="hidden" name="searchType" id="searchType" value="prov">
    <table class="table_query" width=100% border="0" align="center" cellpadding="1" cellspacing="1" >
		<th colspan="6"><img src="<%=contextPath%>/img/subNav.gif" alt="" class="nav" />查询条件</th>
		<tr>
           <td width="20%"   align="right">查询日期：</td>
           <td width="30%">
           <script type="text/javascript">
               getYearSelect("SYear", "SYear", 1, '');
           </script>
           <script type="text/javascript">
           getMonThSelect("StMth", "StMth", '');
           </script>
           到
           <script type="text/javascript">
               getMonThSelect("EdMth", "EdMth", '');
           </script>
          </td>
           <td width="20%" align="right">服务商名称：</td>
           <td width="30%">
               <input class="middle_txt" type="text" readonly="readonly" id="dealerName" name="dealerName" disabled="disabled" style="background-color: #E5E3E3;"/>
               <input class="mark_btn" type="button" id="dButton" value="&hellip;" disabled="disabled"
                      onclick="showOrgDealer('dealerCode','dealerId','true','',true,false,false,'dealerName')"/>
               <input type="hidden" id="dealerId" name="dealerId"/>
               <input type="hidden" id="dealerCode" name="dealerCode"/>
               <input class="mini_btn" onclick="clearGyzxInput();" value="清除" type="button" name="clrBtn"/>
           </td>
	   </tr>
	   <tr>
	   	<td colspan="6" align="center">
	   	  <input type="radio" name="searchRadio" value="prov" checked onclick="changeDiv(this);"/>按大区、省份&nbsp;&nbsp;
          <input type="radio" name="searchRadio" value="gyzx" onclick="changeDiv(this);"/>按供应中心&nbsp;&nbsp;
          <input type="radio" name="searchRadio" value="fws" onclick="changeDiv(this);"/>按服务商
	   	</td>
	   </tr>
	   <tr>
        <td colspan="6" align="center">
            <input name="BtnQuery" id="queryBtn" class="normal_btn" type="button" value="查 询" onclick="queryRep()"/>
            <input class="normal_btn" type="button" value="导 出" onclick="exportRep()"/>
        </td>
    </tr>
        <tr>
            <td colspan="6" align="center">
                <font style="color:  red">购买金额以本部和供应中心出库为准,结算金额为在售后的最终结算金额;金额单位：(万元/不含税)</font>
            </td>
        </tr>
	</table>
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
</form>

<script type="text/javascript">
var myPage;
var baseUrl = "<%=contextPath%>/report/partReport/partSalesReport/PartCheckAnalysisReport/";
var url = baseUrl+"query.json";

var title = null;
var columns = null;

function queryRep()
{
	var searchType = $("searchType").value;
	if("prov" == searchType)
	{
		columns = [
		    		{header: "序号", align:"center", renderer: getIndex},
		        	{header: "年月", dataIndex: "MONTH_NO", align:"center"},
		        	{header: "销售员", dataIndex: "NAME", style: 'text-align:left'},
		        	{header: "大区服务区经理", dataIndex: "REG_USER", style: 'text-align:left'},
		        	{header: "大区", dataIndex: "ROOT_ORG_NAME", align:"center"},
		        	{header: "省份", dataIndex: "DREGION_NAME", style: 'text-align:left'},
		        	{header: "任务金额", dataIndex: "AMOUNT", style: 'text-align:right'},
		        	{header: "完成金额", dataIndex: "P_AMOUNT", style: 'text-align:right'},
		        	{header: "完成率", dataIndex: "P_RTO", style: 'text-align:right'},
		        	{header: "辖区结算金额", dataIndex: "B_A", style: 'text-align:right'},
		        	{header: "拓展率", dataIndex: "TZ_RATIO", style: 'text-align:right'}
		];
	}
	else if("gyzx" == searchType)
	{
		columns = [
		    		{header: "序号", align:"center", renderer: getIndex},
		        	{header: "年月", dataIndex: "MONTH_NO", align:"center"},
		        	{header: "供应中心代码", dataIndex: "PARENTORG_CODE", align:"center"},
		        	{header: "供应中心名称", dataIndex: "PARENTORG_NAME", style: 'text-align:left'},
		        	{header: "辐射区域", dataIndex: "REGION_NAME", style: 'text-align:left'},
		        	{header: "任务金额", dataIndex: "AMOUNT", style: 'text-align:right'},
		        	{header: "购买金额", dataIndex: "P_AMOUNT", style: 'text-align:right'},
		        	{header: "完成率", dataIndex: "P_RTO", style: 'text-align:right'},
		        	{header: "辐射区域结算金额", dataIndex: "B_A", style: 'text-align:right'},
		        	{header: "拓展率", dataIndex: "TZ_RATIO", style: 'text-align:right'}
		];
	}
	else
	{
		columns = [
		    		{header: "序号", align:"center", renderer: getIndex},
		        	{header: "年月", dataIndex: "MONTH_NO", align:"center"},
		        	{header: "服务商代码", dataIndex: "DEALER_CODE", align:"center"},
		        	{header: "服务商名称", dataIndex: "DEALER_NAME", style: 'text-align:left'},
		        	{header: "任务金额", dataIndex: "AMOUNT", style: 'text-align:right'},
		        	{header: "购买金额", dataIndex: "P_AMOUNT", style: 'text-align:right'},
		        	{header: "完成率", dataIndex: "P_RTO", style: 'text-align:right'},
		        	{header: "结算金额", dataIndex: "B_A", style: 'text-align:right'},
		        	{header: "拓展率", dataIndex: "TZ_RATIO", style: 'text-align:right'}
		];
	}
	__extQuery__(1);
}

function callBack(json){
	var ps;
	
	//设置对应数据
	if(Object.keys(json).length>0){
		keys = Object.keys(json);
		for(var i=0;i<keys.length;i++){
		   if(keys[i] =="ps"){
			   ps = json[keys[i]];
			   break;
		   }
		}
	}
	
	//生成数据集
	if(ps.records != null){
		$("_page").hide();
		$('myGrid').show();
		new createGrid(title,columns, $("myGrid"),ps).load();			
		//分页
		myPage = new showPages("myPage",ps,url);
		myPage.printHtml();
		hiddenDocObject(2);
		var searchType = $("searchType").value;
		if("gyzx" == searchType)
		{
			var tbl = $("myTable");
			var rowObj = tbl.insertRow(tbl.rows.length);
			rowObj.className = "table_list_row1";
			var cell1 = rowObj.insertCell(0);
			    var cell2 = rowObj.insertCell(1);
			    var cell3 = rowObj.insertCell(2);
			    var cell4 = rowObj.insertCell(3);
			    var cell5 = rowObj.insertCell(4);
			    var cell6 = rowObj.insertCell(5);
			    var cell7 = rowObj.insertCell(6);
			    var cell8 = rowObj.insertCell(7);
			    var cell9 = rowObj.insertCell(8);
			    var cell10 = rowObj.insertCell(9);
			    cell1.innerHTML = '<tr><td></td>';
			    cell2.innerHTML = '<td><strong>合计：</strong></td>';
			    cell3.innerHTML = '<td></td>';
			    cell4.innerHTML = '<td></td>';
			    cell5.innerHTML = '<td></td>';
			    cell6.innerHTML = '<td><strong>'+json.totalAmount+'</strong></td>';
			    cell7.innerHTML = '<td><strong>'+json.totalPAmount+'</strong></td>';
			    cell8.innerHTML = '<td><strong>'+json.totalPRto+'</strong></td>';
			    cell9.innerHTML = '<td><strong>'+json.totalBA+'</strong></td>';
			    cell10.innerHTML = '<td><strong>'+json.totalTZRatio+'</strong></td>';
		}
		else if("fws" == searchType)
		{
			var tbl = $("myTable");
			var rowObj = tbl.insertRow(tbl.rows.length);
			rowObj.className = "table_list_row1";
			var cell1 = rowObj.insertCell(0);
			    var cell2 = rowObj.insertCell(1);
			    var cell3 = rowObj.insertCell(2);
			    var cell4 = rowObj.insertCell(3);
			    var cell5 = rowObj.insertCell(4);
			    var cell6 = rowObj.insertCell(5);
			    var cell7 = rowObj.insertCell(6);
			    var cell8 = rowObj.insertCell(7);
			    var cell9 = rowObj.insertCell(8);
			    var cell10 = rowObj.insertCell(9);
			    cell1.innerHTML = '<tr><td></td>';
			    cell2.innerHTML = '<td><strong>合计：</strong></td>';
			    cell3.innerHTML = '<td></td>';
			    cell4.innerHTML = '<td></td>';
			    cell5.innerHTML = '<td><strong>'+json.totalAmount+'</strong></td>';
			    cell6.innerHTML = '<td><strong>'+json.totalPAmount+'</strong></td>';
			    cell7.innerHTML = '<td><strong>'+json.totalPRto+'</strong></td>';
			    cell8.innerHTML = '<td><strong>'+json.totalBA+'</strong></td>';
			    cell9.innerHTML = '<td><strong>'+json.totalTZRatio+'</strong></td>';
		}
	}else{
		$("_page").show();
		$("_page").innerHTML = "<div class='pageTips'>没有满足条件的数据!</div>";
		$("myPage").innerHTML = "";
		removeGird('myGrid');
		$('myGrid').hide();
		hiddenDocObject(1);
	}
}

//导出
function exportRep() {
    fm.action = baseUrl+"export.do";
    fm.target = "_self";
    fm.submit();
}

function clearGyzxInput() {
    document.getElementById("dealerId").value = '';
    document.getElementById("dealerCode").value = '';
    document.getElementById("dealerName").value = '';
}

function showGyzx(venderId,venderName,venderCode) {
    if (!venderName) {
    	venderName = null;
    }
    if (!venderId) {
    	venderId = null;
    }
    OpenHtmlWindow("<%=contextPath%>/jsp/parts/salesManager/carFactorySalesManager/gyzxSelect.jsp?venderName=" + venderName + "&venderId=" + venderId+"&venderCode="+venderCode, 730, 390);
}

</script>
</div>
</body>
</html>
