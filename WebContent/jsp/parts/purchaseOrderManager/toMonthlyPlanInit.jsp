<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="/jstl/cout" %>
<%@page import="com.infodms.dms.common.Constant" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<%
    String contextPath = request.getContextPath();
%>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <jsp:include page="${contextPath}/common/jsp_head_new.jsp"/>
    <title></title>
    <script language="JavaScript">
        function getYearSelect(id, name, scope, value) {
            var date = new Date();
            var year = date.getFullYear();    //获取完整的年份
            var month = date.getMonth() + 2;
            if(month>12){
                year=year+1;
            }
            var str = "";
            str += "<select  id='" + id + "' name='" + name + "'  style='width:80px;' class='u-select'>";
            str += "<option selected value=''>-请选择-</option>";
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
            var month = date.getMonth() + 2;
            if(month>12){
                month=month-12;
            }
            var str = "";
            str += "<select  id='" + id + "' name='" + name + "'  style='width:45px;' s class='u-select'>";
            str += "<option selected value=''>-请选择-</option>";
            for (var i = 1; i <= 12; i++) {
                if (value == "") {
                    if (i == month) {
                        str += "<option selected value =" + (i < 10 ? "0" + i : i) + ">" + (i < 10 ? "0" + i : i) + "</option >";
                    } else {
                        str += "<option  value =" + (i < 10 ? "0" + i : i) + ">" + (i < 10 ? "0" + i : i) + "</option >";
                    }
                } else {
                    str += "<option " + (i == value ? "selected" : "") + "value =" + (i < 10 ? "0" + i : i) + ">" + (i < 10 ? "0" + i : i) + "</option >";
                }
            }
            str += "</select> 月";
            document.write(str);
        }
    </script>
</head>
<body onload="__extQuery__(1);">
<div class="wbox">
    <div class="navigation">
    	<img src="<%=request.getContextPath()%>/img/nav.gif"/>&nbsp;当前位置：配件管理&gt; 采购计划管理&gt; 月度需求计划查询
    </div>
    <form method="post" name="fm" id="fm" enctype="multipart/form-data">
    <div class="form-panel">
				<h2><img src="<%=contextPath%>/jmstyle/img/search-ico.png" style="margin-bottom: -13px;"/>查询条件</h2>
				<div class="form-body">
        <input type="hidden" name="curPage" id="curPage"/>
        <table class="table_query" >
            <tr>
                <td class="right">计划单号：</td>
                <td >
                    <input class="middle_txt" type="text" id="plan_no" name="plan_no" value=""/>
                </td>               
                <td class="right">计划年月:</td>
                <td >
                    <script type="text/javascript">
                        getYearSelect("MYYEAR", "MYYEAR", 1, '');
                    </script>
                    <script type="text/javascript">
                        getMonThSelect("MYMONTH", "MYMONTH", '');
                    </script>
                </td>             
            </tr>         
            <tr>
                <td class="center" colspan="6">
                	<input type="button" class="u-button" name="BtnQuery" id="queryBtn" onclick="__extQuery__(1);" value="查询"/>&nbsp;
                	<input type="reset" class="u-button " onclick="btnEable();" value="重置"/>&nbsp;
                </td>
            </tr>
        </table>
        </div>
        </div>
        <jsp:include page="${contextPath}/queryPage/orderHidden.html"/>
        <jsp:include page="${contextPath}/queryPage/pageDiv.html"/>
    </form>

    <script type="text/javascript">
       // autoAlertException();//输出错误信息
        var myPage;
        var url = '<%=contextPath%>/parts/purchaseOrderManager/PurchaseArrConfir/getMonthlyPlanHz.json';
        var title = null;
        var columns = [
				{header: "序号", renderer: getIndex},
				{header: "操作",dataIndex:'TID',renderer: myLink, style:"text-align: center"},
				{header: "计划单号",dataIndex:'PLAN_NO', style:"text-align: center"},
				{header: "计划 类型",dataIndex:'PLAN_TYPES', style:"text-align: center",renderer:getItemValue},
 				{header: "计划日期",dataIndex:'MONTH_DATE', style:"text-align: center"},
 				{header: "创建时间",dataIndex:'CREATE_DATE', style:"text-align: center"},
				{header: "创建人",dataIndex:'NAME', style:"text-align: center"},
 				{header: "状态",dataIndex:'STATUS', style:"text-align: center"}

            ];
        
        //操作链接生成
        function myLink(value,meta,record){
        	var tid = record.data.TID;
        	var str = "<a href=\"#\" onclick='view(\"" + tid + "\")'>[查看]</a>&nbsp;";
        	return str;
        }
        //查看
        function view(value) {
            location = "<%=contextPath%>/parts/purchaseOrderManager/PurchaseArrConfir/toRollingPlanySelect.do?planId="+value+"&planTypes=<%=Constant.PART_PURCHASE_PLAN_TYPE_01%>";
        }
       
    </script>
</div>
</body>
</html>