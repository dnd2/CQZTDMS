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
    <title>配件销售月、年、季度报表</title>
    <script type="text/javascript">
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
    
    function getQuarterSeasonSelect(id, name, value) {
        var date = new Date();
        var month = date.getMonth();//获取当前月
        var tmp;
        if(month<3){
        	tmp = 1;
        }else if(month<6){
        	tmp = 2;
        }else if(month<9){
        	tmp = 3;
        }else{
        	tmp = 4;
        }
        var str = "";
        str += "<select  id='" + id + "' name='" + name + "'  style='width:65px;'  onchange='changeMon(this);'>";
        str += "<option selected value=''>-请选择-</option>";
        for (var i = 1; i <= 4; i++) {
            if (value == "") {
            	str += "<option  value =" + i + ">第" + convertToChinese(i) + "</option >";
                /*if (i == tmp) {
                    str += "<option selected value =" + i + ">第" + convertToChinese(i) + "</option >";
                } else {
                    str += "<option  value =" + i + ">第" + convertToChinese(i) + "</option >";
                }*/
            }
        }
        str += "</select> 季度";
        document.write(str);
    }
    
    function getMonThSelect(id, name, value) {
        var date = new Date();
        var month = date.getMonth() + 1;
        var str = "";
        str += "<select  id='" + id + "' name='" + name + "'  style='width:65px;' onchange='changeSeason(this);'>";
        str += "<option selected value=''>-请选择-</option>";
        for (var i = 1; i <= 12; i++) {
            if (value == "") {
            	str += "<option  value =" + (i < 10 ? "0" + i : i) + ">" + (i < 10 ? "0" + i : i) + "</option >";
                /*if (i == month) {
                    str += "<option selected value =" + (i < 10 ? "0" + i : i) + ">" + (i < 10 ? "0" + i : i) + "</option >";
                } else {
                    str += "<option  value =" + (i < 10 ? "0" + i : i) + ">" + (i < 10 ? "0" + i : i) + "</option >";
                }*/
            }
        }
        str += "</select> 月";
        document.write(str);
    }

    function convertToChinese(num){
    	var N = [
                 "零", "一", "二", "三", "四", "五", "六", "七", "八", "九"
             ];
        var str = num.toString();
        var len = num.toString().length;
        var C_Num = [];
        for(var i = 0; i < len; i++){
            C_Num.push(N[str.charAt(i)]);
        }
        return C_Num.join('');
    }

    function changeMon(obj){
        if(obj.value){
            $("MYMONTH").value="";
        }
    }
    
    function changeSeason(obj){
        if(obj.value){
            $("MYSEASON").value="";
        }
    }
    </script>
</head>
<body onunload='javascript:destoryPrototype();'loadcalendar();">
<div class="wbox">
    <div class="navigation"><img src="<%=contextPath%>/img/nav.gif"/>&nbsp;当前位置： 报表管理&gt;配件报表&gt;本部销售报表&gt;
        配件销售月、季度、年报表
    </div>
    <form method="post" name="fm" id="fm" enctype="multipart/form-data">
        <input type="hidden" name="flag" id="flag" value="${flag }"/>
        <table class="table_query">
            <th colspan="6" width="100%"><img src="<%=contextPath%>/img/subNav.gif" alt="" class="nav"/>查询条件</th>
            <tr >
	            <td width="10%"   align="right">查询日期：</td>
                <td width="20%">
                <script type="text/javascript">
                    getYearSelect("MYYEAR", "MYYEAR", 1, '');
                </script>
                <script type="text/javascript">
                getQuarterSeasonSelect("MYSEASON", "MYSEASON", '');
                </script>
                <script type="text/javascript">
                    getMonThSelect("MYMONTH", "MYMONTH", '');
                </script>
                </td>
                
			  <c:if test="${flag eq 1 }">
			        <td width="10%"   align="right">供应中心代码：</td>
                    <td width="20%">
                    <input class="middle_txt" type="text" readonly="readonly" id="dealerCode" name="dealerCode" value="${venderCode }"/>
                    </td>
			  </c:if>
			  <c:if test="${flag eq 0 }">
                    <td width="10%"   align="right">供应中心代码：</td>
                    <td width="20%">
                    <input class="middle_txt" type="text" id="dealerCode" name="dealerCode" value="${venderCode }"/>
                 </td>
              </c:if>
	           
	           <c:if test="${flag eq 1 }">
                    <td width="10%"   align="right">供应中心：</td>
                    <td width="20%">
                    <input class="middle_txt" type="text" readonly="readonly" id="dealerName" name="dealerName" value="${venderName }"/>
                    <input type="hidden" id="dealerId" name="dealerId" value="${venderId }"/>
                    </td>
                 </c:if>
                 <c:if test="${flag eq 0 }">
                    <td width="10%"   align="right">供应中心：</td>
                    <td width="20%">
                    <input class="middle_txt" type="text"  id="dealerName" name="dealerName"/>
                    <input class="mark_btn" type="button" value="&hellip;"
                           onclick="showGyzx('dealerId','dealerName','dealerCode');"/>
                    <input class="mini_btn" onclick="clearGyzxInput();" value="清除" type="button" name="clrBtn"/>
                    <input type="hidden" id="dealerId" name="dealerId" value=""/>
                 </td>
                 </c:if>
            </tr>
            
            <tr>
                <td colspan="6" align="center">
                    <input name="BtnQuery" id="queryBtn" class="normal_btn" type="button" value="查询"
                           onclick="__extQuery__(1);"/>
                    <input class="normal_btn" type="button" value="导出" onclick="expPartSalesGyzxExcel();"/>
                </td>
            </tr>
        </table>
        <jsp:include page="${contextPath}/queryPage/orderHidden.html"/>
        <jsp:include page="${contextPath}/queryPage/pageDiv.html"/>
    </form>

    <script type="text/javascript">
        autoAlertException();//输出错误信息
        var myPage;
        var url = "<%=contextPath%>/report/partReport/partSalesReport/PartSalesReport/queryPartSalesGyzx.json";

        var title = null;
        //var calculateConfig = {totalColumns:"BO_RATE"};

        var columns = [
            {header: "序号", align: 'center', renderer: getIndex},
            {header: "供应中心代码", dataIndex: 'DEALER_CODE', align:'center'},
            {header: "供应中心", dataIndex: 'DEALER_NAME', align:'center'},
            {header: "采购金额(不含税)", dataIndex: 'BUY_AMOUNT', style: 'text-align:right'},
            {header: "采购数量", dataIndex: 'BUY_QTY', align:'center'},
            {header: "BO总项数", dataIndex: 'BOZXS', align: 'center'},
            {header: "销售总项数", dataIndex: 'SALZXS', align: 'center'},
            {header: "BO率", dataIndex: 'BO_RATE', align: 'center',renderer:boRateInput},
            {header: "销售金额(不含税)", dataIndex: 'SALES_AMOUNT', style: 'text-align:right'},
            {header: "销售数量", dataIndex: 'SALES_QTY', align: 'center'},
            {header: "库存金额(不含税)", dataIndex: 'STOCK_AMOUNT', style: 'text-align:right'}
        ];

        function boRateInput(value, metaDate, record){
            var boXs = record.data.BOZXS;
            var dhXs = record.data.SALZXS;
            if(boXs==0){
            	return '<input type="hidden" value="' + 0 + '"/>'+0+"%";
            }
            if(dhXs==0&&boXs>0){
            	return '<input type="hidden" value="' + 100 + '"/>'+100+"%";
            }
            var jsJg = (boXs/dhXs)*100;
            var boRate;
            if(jsJg.toString().indexOf(".")>-1){
            	boRate=jsJg.toFixed(2);
            }else{
            	boRate = jsJg;
            }
            return '<input type="hidden" value="' + boRate + '"/>'+boRate+"%";
        }

        //导出
        function expPartSalesGyzxExcel() {
            fm.action = "<%=contextPath%>/report/partReport/partSalesReport/PartSalesReport/expPartSalesGyzxExcel.do";
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
        		
        		var flag = $("flag").value;
        		if(flag==0){
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
           		    var cell11 = rowObj.insertCell(10);
           		    
           		    cell1.innerHTML = '<tr><td></td>';
           		    cell2.innerHTML = '<tr><td></td>';
           		    cell3.innerHTML = '<td><strong>合计：</strong></td>';
           		    cell4.innerHTML = '<td><strong>'+json.allBuyAmount+'</strong></td>';
           		    cell5.innerHTML = '<td><strong>'+json.allbuyQty+'</strong></td>';
           		    cell6.innerHTML = '<td><strong>'+json.allBoZxs+'</strong></td>';
           		    cell7.innerHTML = '<td><strong>'+json.allXsZxs+'</strong></td>';
           		    cell8.innerHTML = '<td><strong>'+json.allBoRate+'</strong></td>';
           		    cell9.innerHTML = '<td><strong>'+json.allSalesAmount+'</strong></td>';
           		    cell10.innerHTML = '<td><strong>'+json.allSalQty+'</strong></td>';
           		    cell11.innerHTML = '<td><strong>'+json.allStockAmount+'</strong></td>';
           		    
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
    </script>
</div>
</body>
</html>