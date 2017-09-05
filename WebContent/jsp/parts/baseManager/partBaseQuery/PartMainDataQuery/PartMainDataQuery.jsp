<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="com.infodms.dms.common.Constant" %>
<%@taglib uri="/jstl/cout" prefix="c" %>
<%
    String contextPath = request.getContextPath();

%>
<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp"/>
<title>配件主信息查询</title>

<script type="text/javascript">

    var myPage;

    var url = "<%=contextPath%>/parts/baseManager/partBaseQuery/partMainDataQuery/partMainDataQuery/queryPartBaseInfo.json";

    var title = null;

    var columns = [
        {header: "序号", style: "text-align: center", renderer: getIndex},
        {
            id: 'action',
            header: "操作",
            sortable: false,
            dataIndex: 'PART_ID',
            renderer: myLink,
            style: "text-align: center"
        },
        {header: "配件编码", dataIndex: 'PART_OLDCODE', style: "text-align: center", width: '3%'},
        {header: "配件名称", dataIndex: 'PART_CNAME', style: "text-align: center", width: '5%'},
        {header: "配件种类", dataIndex: 'PART_TYPE', style: "text-align: center", renderer: getItemValue},
        {header: "单位", dataIndex: 'UNIT', style: "text-align: center"},
        {header: "适用车型", dataIndex: 'MODEL_NAME', style: "text-align: center;"},
        {header: "是否协议包装", dataIndex: 'IS_PROTOCOL_PACK', style: "text-align: center;", renderer: getItemValue},
        {header: "是否批次包装", dataIndex: 'IS_MAG_BATCH', style: "text-align: center;", renderer: getItemValue},
        {header: "是否停用", dataIndex: 'IS_PART_DISABLE', style: "text-align: center;", renderer: getItemValue},
        {header: "停用日期", dataIndex: 'PART_DISABLE_DATE', style: "text-align: center;"},
        {header: "最小销售数量", dataIndex: 'MIN_SALE'},
        {header: "最大销售数量", dataIndex: 'MAX_SALE_VOLUME'},
        {header: "最小采购数量", dataIndex: 'MIN_PURCHASE'},
        {header: "是否可替代", dataIndex: 'IS_REPLACED', renderer: getItemValue},
        {header: "是否有效", dataIndex: 'STATE', style: "text-align: center", renderer: getItemValue},
//         {header: "${map.prciceName1}(元)", dataIndex: 'SALE_PRICE1', style: "text-align: center", width: '5%'},
//         {header: "${map.prciceName2}(元)", dataIndex: 'SALE_PRICE2', style: "text-align: center", width: '5%'},
//         {header: "${map.prciceName3}(元)", dataIndex: 'SALE_PRICE3', style: "text-align: center", width: '5%'},
//         {header: "${map.prciceName4}(元)", dataIndex: 'SALE_PRICE4', style: "text-align: center", width: '5%'},
//         {header: "${map.prciceName5}(元)", dataIndex: 'SALE_PRICE5', style: "text-align: center", width: '5%'},
//         {header: "${map.prciceName6}(元)", dataIndex: 'SALE_PRICE6', style: "text-align: center", width: '5%'},
//         {header: "${map.prciceName7}(元)", dataIndex: 'SALE_PRICE7', style: "text-align: center", width: '5%'},
        {header: "备注", dataIndex: 'REMARK', style: "text-align: center"}
    ];

    //设置超链接  begin


    //设置超链接
    function myLink(value, meta, record) {
        return String.format("<a href=\"#\" onclick='view(\"" + value + "\")'>[查看]</a>");
    }

    //详细页面
    function view(value) {
        disableAllClEl();
        OpenHtmlWindow('<%=contextPath%>/parts/baseManager/partBaseQuery/partMainDataQuery/partMainDataQuery/queryPartBaseDetail.do?flag=view&&partId=' + value, 1100, 480);
        enableAllClEl();
    }

    //清除供应商代码
    function reset() {
        document.getElementById("SUPPLIER_CODE").value = "";
    }

    function exportPartBaseExcel() {
        document.fm.action = "<%=contextPath%>/parts/baseManager/partBaseQuery/partMainDataQuery/partMainDataQuery/exportPartBaseExcel.do";
        document.fm.target = "_self";
        document.fm.submit();

    }

    function disableAllA() {
        var inputArr = document.getElementsByTagName("a");
        for (var i = 0; i < inputArr.length; i++) {
            inputArr[i].disabled = true;
        }
    }

    function enableAllA() {
        var inputArr = document.getElementsByTagName("a");
        for (var i = 0; i < inputArr.length; i++) {
            inputArr[i].disabled = false;
        }
    }
    function disableAllBtn() {
        var inputArr = document.getElementsByTagName("input");
        for (var i = 0; i < inputArr.length; i++) {
            if (inputArr[i].type == "button") {
                inputArr[i].disabled = true;
            }
        }
    }
    function enableAllBtn() {
        var inputArr = document.getElementsByTagName("input");
        for (var i = 0; i < inputArr.length; i++) {
            if (inputArr[i].type == "button") {
                inputArr[i].disabled = false;
            }
        }
    }
    function disableAllClEl() {
        disableAllA();
        disableAllBtn();
    }
    function enableAllClEl() {
        enableAllBtn();
        enableAllA();
    }
    function showPic(value, meta, record) {
        var picURl = record.data.PIC_URL;
        var partId = record.data.PART_ID;
        if (picURl != null) {
            return String.format("<img style='align:center;width: 30px;height: 30px' src='" + picURl + "' onclick='showBigPic(" + partId + ");'alt='点击看大图'/>");
        } else {
            return String.format("<img style='align:center;width: 30px;height: 30px' src='<%=request.getContextPath()%>/img/noPic.gif'/>");
        }
    }

    function showBigPic(value) {
        OpenHtmlWindow('<%=contextPath%>/parts/baseManager/partBaseQuery/partMainDataQuery/partMainDataQuery/ShowPartPic.do?partId=' + value, 800, 600);
    }
    
    $(function(){
    	__extQuery__(1); enableAllClEl();
    });
</script>
</head>
<body>
<form name="fm" id="fm" method="post" enctype="multipart/form-data">
    <div class="navigation"><img src="<%=contextPath%>/img/nav.gif"/>&nbsp;当前位置：基础数据管理 >配件基础数据查询> 配件主信息查询</div>
    <input type="hidden" id="STATE" name="STATE" value="<%=Constant.STATUS_ENABLE%>"/>
	<div class="form-panel">
		<h2><img src="<%=contextPath%>/jmstyle/img/search-ico.png" style="margin-bottom: -13px;"/>查询条件</h2>
	    <div class="form-body">
		<table class="table_query">
	        <tr>
	            <td class="right">配件编码：</td>
	            <td><input class="middle_txt" type="text" id="PART_OLDCODE" name="PART_OLDCODE"/></td>
	            <td class="right">配件名称：</td>
	            <td><input class="middle_txt" type="text" id="PART_CNAME" name="PART_CNAME"/></td>
	            <td class="right">车型：</td>
	            <td><input class="middle_txt" type="text" id="cartType" name="cartType"/></td>
	        </tr>
	        <tr>
	            <td class="center" colspan="6">
	                <input class="u-button u-query" type="button" name="BtnQuery" id="queryBtn" value="查 询" onclick="__extQuery__(1)"/>
	                <input class="u-button" type="button" name="BtnExportPartBaseExcel" value="导 出" onclick="exportPartBaseExcel()"/>
	            </td>
	        </tr>
	    </table>
	    </div>
    </div>
    <jsp:include page="${contextPath}/queryPage/orderHidden.html"/>
    <jsp:include page="${contextPath}/queryPage/pageDiv.html"/>
</form>
</body>
</html>
