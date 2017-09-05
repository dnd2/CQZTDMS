<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="com.infodms.dms.common.Constant" %>
<%@taglib uri="/jstl/cout" prefix="c" %>
<%
    String contextPath = request.getContextPath();
%>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp"/>
<head>
    <title>配件发运查询</title>
    <script language="javascript">
        var myObjArr = [];
        //初始化查询TABLE
        var myPage;
        var url = g_webAppName + "/parts/salesManager/carFactorySalesManager/PartTransWait/queryData.json";
        var title = null;
        var columns = [
            {header: "序号", align: 'center', renderer: getIndex},
            //{id: 'action', header: "操作", sortable: false, dataIndex: 'PICK_ORDER_ID', align: 'center', renderer: myLink},
            {header: "采购订单号", dataIndex: 'ORDER_CODE', style: 'text-align:left'},
            {header: "BO单号", dataIndex: 'BO_CODE', style: 'text-align:left'},
            {header: "销售订单号", dataIndex: 'SO_CODE', style: 'text-align:left'},
            {header: "拣货单号", dataIndex: 'PICK_ORDER_ID', style: 'text-align:center'},
            {header: "发运单号", dataIndex: 'TRPLAN_CODE', style: 'text-align:center'},
            {header: "经销商代码", dataIndex: 'DEALER_CODE', align: 'center'},
            {header: "经销商名称", dataIndex: 'DEALER_NAME', style: 'text-align:left'},
            {header: "装箱日期", dataIndex: 'CREATE_DATE', align: 'center', renderer: checkDateOver},
            {header: "箱号", dataIndex: 'PKG_NO', style: 'text-align:left'},
            {header: "包装尺寸(长*宽*高)", dataIndex: 'LENGTH', style: 'text-align:left', renderer: setSize},
            {header: "体积", dataIndex: 'VOLUME', style: 'text-align:right'},
            {header: "实际重量", dataIndex: 'WEIGHT', style: 'text-align:right'},
            {header: "折合重量", dataIndex: 'EQ_WEIGHT', style: 'text-align:right'},
            {header: "计费重量", dataIndex: 'CH_WEIGHT', style: 'text-align:right'},
//             {header: "整车挑箱日期", dataIndex: 'CONFIRM_DATE', style: 'text-align:left'},
//             {header: "整车发运单号", dataIndex: 'ASS_NO', style: 'text-align:left'},
//             {header: "是否已随车", dataIndex: 'ISVIN', align: 'center', renderer: getItemValue},
            {header: "承运物流", dataIndex: 'TRANSPORT_ORG', align: 'center'},
            {header: "发运方式", dataIndex: 'TRANS_TYPE', align: 'center'},
            {header: "发运日期", dataIndex: 'TRANS_DATE', align: 'center'}
        ];
        function setSize(value, meta, record) {
            var l = record.data.LENGTH;
            var w = record.data.WIDTH;
            var h = record.data.HEIGHT;
            var text = l + " * " + w + " * " + h;
            return String.format(text);
        }
        function checkDateOver(value, meta, record) {
            var v = record.data.ISOVER;//预警颜色（装箱完成超过3天红色）
            var text = value;
            if (v == "<%=Constant.IF_TYPE_YES%>") {
                text = "<div style='width:100%;height:100%;background-color: red;'>" + value + "</div>";
            }
            return String.format(text);
        }
        function exportExcel() {
            document.fm.action = g_webAppName + "/parts/salesManager/carFactorySalesManager/PartTransWait/exportExcel.do";
            document.fm.target = "_self";
            document.fm.submit();
        }
        $(document).ready(function(){
        	__extQuery__(1);
        });

    </script>
</head>

<body>
<form name="fm" id="fm" method="post" enctype="multipart/form-data">

    <div class="wbox">
        <div class="navigation"><img src="<%=contextPath%>/img/nav.gif" alt=""/>&nbsp;当前位置: 配件管理 &gt; 总部销售管理 &gt;配件发运查询
        </div>
        <div class="form-panel">
        	<h2><img src="<%=contextPath%>/jmstyle/img/search-ico.png" style="margin-bottom: -13px;"/>查询条件</h2>
			<div class="form-body">
	        <table border="0" class="table_query">
	            <tr>
	                <td class="right">采购订单号：</td>
	                <td><input class="middle_txt" type="text" id="ORDER_CODE" name="ORDER_CODE"/></td>
	                <td class="right">销售订单号：</td>
	                <td><input type="text" id="SO_CODE" name="SO_CODE" class="middle_txt">
	                </td>
	                <td class="right">装箱日期：</td>
	                <td>
	                    <input name="SstartDate" type="text" class="short_txt" id="SstartDate" value="${old }" style="width:80px;"/>
	                    <input name="button" value=" " type="button" class="time_ico" />
	                    至
	                    <input name="SendDate" type="text" class="short_txt" id="SendDate" value="${now }"  style="width:80px;"/>
	                    <input name="button" value=" " type="button" class="time_ico" />
	                </td>
	            </tr>
	            <tr>
	                <td class="right">经销商代码：</td>
	                <td><input type="text" id="DEALER_CODE" name="DEALER_CODE" class="middle_txt">
	                </td>
	                <td class="right">经销商名称：</td>
	                <td><input class="middle_txt" type="text" id="DEALER_NAME" name="DEALER_NAME"/></td>
	                <%-- <td width="10%" class="right">是否已随车：</td>
	                <td width="20%">
	                    <script type="text/javascript">
	                        genSelBoxExp("isSC", <%=Constant.IF_TYPE%>, "", true, "", "onchange='__extQuery__(1)'", "false", "");
	                    </script>
	                </td> --%>
	                <td class="right">发运日期：</td>
	                <td>
	                    <input name="fstartDate" type="text" class="short_txt" id="fstartDate" value="${old }"  style="width:80px;"/>
	                    <input name="button" value=" " type="button" class="time_ico" />
	                   	 至
	                    <input name="fsendDate" type="text" class="short_txt" id="fsendDate" value="${now }"  style="width:80px;"/>
	                    <input name="button" value=" " type="button" class="time_ico" />
	                </td>
	            </tr>
	            <tr>
	                <td width="10%" class="right">承运物流：</td>
	                <td width="20%">
	                    <select name="transportOrg" id="transportOrg" onclick="" class="u-select">
	                        <option value="">--请选择--</option>
	                        <c:forEach items="${listc}" var="obj">
	<%--                             <option value="${obj.fixName}">${obj.fixName}</option> --%>
								<option value="${obj.LOGI_CODE}">${obj.LOGI_FULL_NAME}</option>
	                        </c:forEach>
	                    </select>
	                </td>
	                <td width="10%" class="right">发运方式：</td>
	                <td width="20%">
	                    <select name="transType" id="transType" onclick="" class="u-select">
	                        <option value="">--请选择--</option>
	                        <c:forEach items="${listf}" var="obj">
	<%--                             <option value="${obj.fixName}">${obj.fixName}</option> --%>
								<option value="${obj.TV_ID}">${obj.TV_NAME}</option>
	                        </c:forEach>
	                    </select>
	                </td>
	                <td width="10%" class="right">箱号：</td>
	                <td><input type="text" id="PKGNO" name="PKGNO" class="middle_txt">
	                </td>
	            </tr>
	            <tr>
	                <td width="10%" class="right">是否已发运：</td>
	                <td width="20%">
	                    <script type="text/javascript">
	                        genSelBoxExp("isFY", "<%=Constant.IF_TYPE%>", "<%=Constant.IF_TYPE_NO%>", true, "", "onchange='__extQuery__(1)'", "false", "");
	                    </script>
	                </td>
	                <td width="10%" class="right">发运单号：</td>
	                <td><input type="text" id="TRPLAN_CODE" name="TRPLAN_CODE" class="middle_txt">
	                </td>
	                <td width="10%" class="right"> </td>
	                <td></td>
	            </tr>
	            <tr>
	                <td colspan="6" class="center">
	                    <input name="BtnQuery" id="queryBtn" class="normal_btn" type="button" value="查 询"
	                           onclick="__extQuery__(1);"/>
	                    <input name="BtnQuery" id="exportBtn" class="normal_btn" type="button" value="导出"
	                           onclick="exportExcel();"/>
	                </td>
	            </tr>
	        </table>
    		</div>
    	</div>
    </div>
    <jsp:include page="${contextPath}/queryPage/orderHidden.html"/>
    <jsp:include page="${contextPath}/queryPage/pageDiv.html"/>
</html>
</form>
</body>
</html>