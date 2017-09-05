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
    <title>配件周报表</title>
    <script language="JavaScript">

        //初始化方法
        function doInit() {
            loadcalendar();  //初始化时间控件
        }

    </script>
</head>
<body onunload='javascript:destoryPrototype();'>
<div class="wbox">
    <div class="navigation"><img src="<%=contextPath%>/img/nav.gif"/>&nbsp;当前位置： 报表管理 > 配件销售报表 >
        BO周报表
    </div>
    <form method="post" name="fm" id="fm" enctype="multipart/form-data">
        <input type="hidden" name="curPage" id="curPage"/>
        <table class="table_query">
            <th colspan="6" width="100%"><img src="<%=contextPath%>/img/subNav.gif" alt="" class="nav"/>查询条件</th>
            <tr>
                <td width="10%" align="right">配件编码：</td>
                <td width="20%"><input class="middle_txt" type="text" name="PARTOLD_CODE" id="PARTOLD_CODE"/></td>
                <td width="10%"   align="right">计划员：</td>
	            <td width="20%">
	                <select id="PLANER_NAME" name="PLANER_NAME" class="short_sel">
	                    <option value="">-请选择-</option>
	                    <c:forEach items="${planerList}" var="planerList">
	                        <option value="${planerList.USER_NAME }">${planerList.USER_NAME }</option>
	                    </c:forEach>
	                </select>
	            </td>
                <td width="10%" align="right">配件类型：</td>
                <td width="20%">
                    <script type="text/javascript">
                    genSelBoxExp("PART_TYPE", <%=Constant.PART_BASE_PART_TYPES %>, "", true, "short_sel", "", "false", '');
                    </script>
                </td>
            </tr>
            <tr>
                <td width="10%" align="right">库房：</td>
	            <td width="20%">
	                <select id="WH_NAME" name="WH_NAME" class="short_sel">
	                    <option value="">-请选择-</option>
	                    <c:forEach items="${wareHouses}" var="wareHouse">
	                        <option value="${wareHouse.whName }">${wareHouse.whName }</option>
	                    </c:forEach>
	                </select>
	            </td>
	            <td width="10%"   align="right">BO生成日期：</td>
                <td width="20%">
                    <input name="startDate" id="t1" value="" type="text" class="short_txt" datatype="0,is_date,10"
                           group="t1,t2">
                    <input name='button3' value=" " type='button' class='time_ico' title="点击选择时间"
                           onclick="showcalendar(event, 't1', false);"/>
                    至
                    <input name="endDate" id="t2" value="" type="text" class="short_txt" datatype="0,is_date,10"
                           group="t1,t2">
                    <input name='button3' value=" " type='button' class='time_ico' title="点击选择时间"
                           onclick="showcalendar(event, 't2', false);"/>
                </td>
            </tr>
            <tr>
                <td colspan="6" align="center">
                    <input name="BtnQuery" id="queryBtn" class="normal_btn" type="button" value="查询"
                           onclick="__extQuery__(1);"/>
                    <input class="normal_btn" type="button" value="导出" onclick="expPartBoCycleExcel();"/>
                </td>
            </tr>
        </table>
        <jsp:include page="${contextPath}/queryPage/orderHidden.html"/>
        <jsp:include page="${contextPath}/queryPage/pageDiv.html"/>
    </form>

    <script type="text/javascript">
        autoAlertException();//输出错误信息
        var myPage;
        var url = "<%=contextPath%>/report/partReport/partSalesReport/BoCycleReport/queryPartBoCycle.json";

        var title = null;

        var columns = [
            {header: "序号", align: 'center', renderer: getIndex},
            {header: "配件类型", dataIndex: 'PART_TYPE', align:'center',renderer: getItemValue},
            {header: "供应商", dataIndex: 'VENDER_NAME', style: 'text-align:left'},
            {header: "库房", dataIndex: 'WH_NAME', align: 'center'},
            {header: "配件编码", dataIndex: 'PART_OLDCODE',  style: 'text-align:left'},
            {header: "配件名称", dataIndex: 'PART_CNAME',  style: 'text-align:left'},
            {header: "件号", dataIndex: 'PART_CODE',  style: 'text-align:left'},
            {header: "单位", dataIndex: 'UNIT', align: 'center'},
            {header: "BO项数", dataIndex: 'BOCNT', align: 'center'},
            {header: "近一个月未满足BO数量", dataIndex: 'MTH_ODDQTY', align: 'center'},
            {header: "近一周未满足数量", dataIndex: 'WEK_ODDQTY', align: 'center'},
            {header: "近一周BO项数", dataIndex: 'WEK_BOCNT', align: 'center'},
            {header: "在途数量", dataIndex: 'OR_QTY', align: 'center'},
            {header: "采购订单编号", dataIndex: 'ORDER_CODE', align: 'center'},
            {header: "订单编制时间", dataIndex: 'CREATE_DATE', align:'center'},
            {header: "计划员", dataIndex: 'NAME', align: 'center'},
            {header: "BO说明", dataIndex: 'BO_NOTE', style: 'text-align:right'}
        ];

        //导出
        function expPartBoCycleExcel() {
            fm.action = "<%=contextPath%>/report/partReport/partSalesReport/BoCycleReport/expPartBoCycleExcel.do";
            fm.target = "_self";
            fm.submit();
        }

      //格式化日期
    	function formatDate(value,meta,record){
    		var output = value.substr(0,10);
    		return output;
    	}
    </script>
</div>
</body>
</html>