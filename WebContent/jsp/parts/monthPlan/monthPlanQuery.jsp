<%@ page import="com.infodms.dms.common.Constant" %>
<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c" %>
<%
    String contextPath = request.getContextPath();
%>
<head>
    <jsp:include page="${contextPath}/common/jsp_head_new.jsp"/>
    <title>经销商月度目标查询</title>
</head>
<body onload="dtlQuery();">
<form name="fm" id="fm" method="post" enctype="multipart/form-data">
    <div class="navigation"><img src="<%=request.getContextPath()%>/img/nav.gif">当前位置&gt;配件管理&gt;经销商月度目标&gt;经销商月度目标查询
    </div>
    <div class="form-panel">
        	<h2><img src="<%=contextPath%>/jmstyle/img/search-ico.png" style="margin-bottom: -13px;"/>查询条件</h2>
			<div class="form-body">
			
		    <table class="table_query">
		        <input type="hidden" id="queryType" name="queryType" value="0"/>
		        <input type="hidden" id="type" name="type" value="0"/>
		        <input type="hidden" id="flag" name="flag" value="1"/>
		        <tr>
		            <td class="right">年-月：</td>
		            <td align="left"><select name="year" id="year" class="mini-sel">
		                <%
		                    String year = (String) request.getAttribute("curYear");
		                    if (null == year || "".equals(year)) {
		                        year = "0";
		                    }
		                    int y = Integer.parseInt(year);
		                %>
		                <option value="<%=y-2 %>"><%=y - 2 %></option>
		                <option value="<%=y-1 %>"><%=y - 1 %></option>
		                <option value="<%=y %>" selected="selected"><%=y %></option>
		                <option value="<%=y+1 %>"><%=y + 1 %></option>
		                <option value="<%=y+2 %>"><%=y + 2 %></option>
		            </select>
		                <select name="month" id="month" class="mini-sel">
		                    <option value='1' <c:if test="${curMonth==1}">selected="selected"</c:if>>01</option>
		                    <option value='2' <c:if test="${curMonth==2}">selected="selected"</c:if>>02</option>
		                    <option value='3' <c:if test="${curMonth==3}">selected="selected"</c:if>>03</option>
		                    <option value='4' <c:if test="${curMonth==4}">selected="selected"</c:if>>04</option>
		                    <option value='5' <c:if test="${curMonth==5}">selected="selected"</c:if>>05</option>
		                    <option value='6' <c:if test="${curMonth==6}">selected="selected"</c:if>>06</option>
		                    <option value='7' <c:if test="${curMonth==7}">selected="selected"</c:if>>07</option>
		                    <option value='8' <c:if test="${curMonth==8}">selected="selected"</c:if>>08</option>
		                    <option value='9' <c:if test="${curMonth==9}">selected="selected"</c:if>>09</option>
		                    <option value='10' <c:if test="${curMonth==10}">selected="selected"</c:if>>10</option>
		                    <option value='11' <c:if test="${curMonth==11}">selected="selected"</c:if>>11</option>
		                    <option value='12' <c:if test="${curMonth==12}">selected="selected"</c:if>>12</option>
		                </select>
		                <select name="month2" id="month2" class="mini-sel">
		                    <option value='1' <c:if test="${curMonth==1}">selected="selected"</c:if>>01</option>
		                    <option value='2' <c:if test="${curMonth==2}">selected="selected"</c:if>>02</option>
		                    <option value='3' <c:if test="${curMonth==3}">selected="selected"</c:if>>03</option>
		                    <option value='4' <c:if test="${curMonth==4}">selected="selected"</c:if>>04</option>
		                    <option value='5' <c:if test="${curMonth==5}">selected="selected"</c:if>>05</option>
		                    <option value='6' <c:if test="${curMonth==6}">selected="selected"</c:if>>06</option>
		                    <option value='7' <c:if test="${curMonth==7}">selected="selected"</c:if>>07</option>
		                    <option value='8' <c:if test="${curMonth==8}">selected="selected"</c:if>>08</option>
		                    <option value='9' <c:if test="${curMonth==9}">selected="selected"</c:if>>09</option>
		                    <option value='10' <c:if test="${curMonth==10}">selected="selected"</c:if>>10</option>
		                    <option value='11' <c:if test="${curMonth==11}">selected="selected"</c:if>>11</option>
		                    <option value='12' <c:if test="${curMonth==12}">selected="selected"</c:if>>12</option>
		                </select>
		            </td>
		            <c:if test="${portalType==10021001}"><!-- 主机厂显示经销商 -->
			            <td class="right">选择经销商：</td>
			            <td>
			                <input type="text" class="middle_txt" name="dealerName" size="15" value="" id="dealerName"/>
			                <input type="hidden" class="middle_txt" name="dealerCode" size="15" value=""
			                       id="dealerCode"/>
			                <input name="button2" type="button" class="mini_btn"
			                       onclick="showOrgDealer('dealerCode','','true','','','','<%=Constant.DEALER_TYPE_DWR%>','dealerName');"
			                       value="..."/>
			                <input type="button" class="mini_btn" onclick="clrTxt('dealerCode');clrTxt('dealerName');"
			                       value="清 空" id="clrBtn"/>
			            </td>
		            </c:if>
		        </tr>
		        <tr>
		            <td colSpan='6'  class="center">
		                <input class="normal_btn" onclick="dtlQuery();" value=查询 type="button" name="queryBtn" id="queryBtn"/>
		                <input class="normal_btn" onclick="collQuery();" value="汇总查询" type="button" name="queryBtn1"/>
		                <input class="normal_btn" value="导出" type="button" name="queryBtn2" onclick="downloadFile();"/>
		            </td>
		        </tr>
		    </table>
		</div>
	</div>		    
    <jsp:include page="${contextPath}/queryPage/orderHidden.html"/>
    <jsp:include page="${contextPath}/queryPage/pageDiv.html"/>
    <div style="width: 99%; height: 30px;  " id="countDiv">
        <div style="width: 20%; height: 30px; float: left; background-color: #DAE0EE;"></div>
        <div style="width: 10%; height: 30px; float: left; background-color: #DAE0EE;"></div>
        <div style="width: 30%; height: 30px; line-height: 30px; float: left; text-align: right; background-color: #DAE0EE;font-weight: bolder;">
            总&nbsp;&nbsp;计：&nbsp;&nbsp;
        </div>
        <div style="width: 15%; height: 30px; line-height: 30px; float: left; text-align: right; background-color: #DAE0EE;"
             id="taskAmount">
        </div>
        <div style="width: 15%; height: 30px; line-height: 30px; float: left; text-align: right; background-color: #DAE0EE;"
             id="saleAmount">
        </div>
        <div style="width: 10%; height: 30px; line-height: 30px; float: left; text-align: center; background-color: #DAE0EE;"
             id="saleRate">
        </div>
    </div>
</form>
</div>
<script type="text/javascript">
    var myPage;
    //查询路径
    var url;
    var title = null;
    var columns;
    function dtlQuery() {
        var queryType = document.getElementById("queryType").value;
        var year = document.getElementById("year").value;
        var month = document.getElementById("month").value;
        var month2 = document.getElementById("month2").value;
        var type = document.getElementById("type").value;

        document.getElementById("flag").value = "1";

        url = "<%=contextPath%>/parts/monthPlan/MonthPlanManager/planQuery.json";
        if (queryType == '0' || month == '') {
            if ("" == year || "" == month || "" == month2) {
                MyAlert("请选择查询的年月!");
                return;
            }
            else if (parseInt(month2) < parseInt(month)) {
                MyAlert("截止月应大于起始月!");
                return;
            }
            columns = [
                {header: "序号", align: 'center', renderer: getIndex},
                {header: "年月", dataIndex: 'TASK_MONTH', align: 'center', style: 'width: 20%;'},
                {header: "经销商代码", dataIndex: 'DEALER_CODE', align: 'center', style: 'width: 10%;'},
                {header: "经销商名称", dataIndex: 'DEALER_NAME', style: 'width: 30%; text-align: left;'},
                {header: "任务金额(元)", dataIndex: 'AMOUNT', align: 'center', style: 'width: 15%;text-align: right;'},
                {
                    header: "完成金额(元)",
                    dataIndex: 'SELLMONEY',
                    align: 'center',
                    style: 'width: 15%;text-align: right;'
                },
                {header: "完成率", dataIndex: 'RATIO', align: 'center', style: 'width: 10%;'}
            ];
            normalAmountQuery();
        }
        __extQuery__(1);
    }

    function collQuery() {
        url = "<%=contextPath%>/parts/monthPlan/MonthPlanManager/planCountQuery.json";
        var year = document.getElementById("year").value;
        var month = document.getElementById("month").value;
        var month2 = document.getElementById("month2").value;
        var type = document.getElementById("type").value;

        document.getElementById("flag").value = "2";
        if ("" == year || "" == month || "" == month2) {
            MyAlert("请选择查询的年月!");
            return;
        }
        else if (parseInt(month2) < parseInt(month)) {
            MyAlert("截止月应大于起始月!");
            return;
        }
        else {
            columns = [
                {header: "起止年月", dataIndex: 'TASK_MONTH', align: 'center', style: 'width: 20%;'},
                {header: "经销商代码", dataIndex: 'DEALER_CODE', align: 'center', style: 'width: 10%;'},
                {
                    header: "经销商名称",
                    dataIndex: 'DEALER_NAME',
                    align: 'center',
                    style: 'width: 30%; text-align: left;'
                },
                {header: "任务金额(元)", dataIndex: 'AMOUNT', align: 'center', style: 'width: 15%;;text-align: right;'},
                {
                    header: "完成金额(元)",
                    dataIndex: 'SELLMONEY',
                    align: 'center',
                    style: 'width: 15%;;text-align: right;'
                },
                {header: "完成率", dataIndex: 'RATIO', align: 'center', style: 'width: 10%;'}
            ];
            countAmountQuery();
            __extQuery__(1);
        }
    }

    function normalAmountQuery() {
        var url = "<%=contextPath%>/parts/monthPlan/MonthPlanManager/normalCountQuery.json";
        makeNomalFormCall(url, countResult, 'fm');
    }

    function countAmountQuery() {
        var url = "<%=contextPath%>/parts/monthPlan/MonthPlanManager/countAmountQuery.json";
        makeNomalFormCall(url, countResult, 'fm');
    }

    function countResult(json) {
        if (null != json) {
            document.getElementById("taskAmount").innerText = json.amountTotal == undefined ? 0 : json.amountTotal;
            document.getElementById("saleAmount").innerText = json.saleTotal == undefined ? 0 : json.saleTotal;
            document.getElementById("saleRate").innerText = json.rate == undefined ? 0 : json.rate;
        }
    }

    //清空
    function clrTxt(txtId) {
        document.getElementById(txtId).value = "";
    }

    //下载模板
    function downloadFile() {
        var flag = document.getElementById("flag").value;
        fm.action = '<%=contextPath%>/parts/monthPlan/MonthPlanManager/expExcel.do?flag=' + flag;
        fm.submit();
    }
</script>
</body>
</html>
