<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@page import="com.infodms.dms.common.Constant" %>
<%@taglib uri="/jstl/cout" prefix="c" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<%String contextPath = request.getContextPath();%>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <meta http-equiv="X-UA-Compatible" content="IE=7">
    <jsp:include page="${contextPath}/common/jsp_head_new.jsp"/>
    <title>有效占用</title>
    <script language="JavaScript">
        //初始化方法
        function doInit() {
            loadcalendar();  //初始化时间控件
        }
    </script>
</head>
<body onunload='javascript:destoryPrototype()' onload="__extQuery__(1);">
<div class="wbox">
<div class="navigation"><img src="<%=request.getContextPath()%>/img/nav.gif"/>&nbsp;当前位置：报表管理&gt;配件报表&gt;本部仓储报表&gt;配件待出库明细</div>
<form method="post" name="fm" id="fm" enctype="multipart/form-data">
    <input type="hidden" name="curPage" id="curPage"/>
    <input type="hidden" name="partId" id="partId"/>
    <table class="table_query" width=100% border="0" align="center" cellpadding="1" cellspacing="1" >
		<th colspan="6"><img src="<%=contextPath%>/img/subNav.gif" alt="" class="nav" />查询条件</th>
	    <tr>
	    	<td width="10%" align="right">配件编码：</td>
            <td width="20%"><input class="middle_txt" type="text" name="PART_OLDCODE" id="PART_OLDCODE"/></td>
           	<td width="10%" align="right">配件名称：</td>
            <td width="20%"><input class="middle_txt" type="text" name="PART_CNAME" id="PART_CNAME"/></td>
            <td width="10%" align="right">件号：</td>
            <td width="20%"><input class="middle_txt" type="text" name="PART_CODE" id="PART_CODE"/></td>
      </tr>
	  <tr>
	   <td   colspan="6" align="center">
	   		<input name="BtnQuery" id="queryBtn" class="normal_btn" type="button" value="查 询" onclick="__extQuery__(1);"/>
            <input class="normal_btn" type="button" value="导出" onclick="expPurOrderInExcel();"/>
       </td>
      </tr>
	</table>
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
</form>

<script type="text/javascript">
autoAlertException();//输出错误信息
var myPage;
var url = "<%=contextPath%>/report/partReport/partStockReport/PurOrderInReport/queryPartsYxzyData.json";

var title = null;

var columns = [
    {header: "序号", align:'center',renderer:getIndex},
    {header: "销售单号", dataIndex: 'SO_CODE',  align: 'center'},
    {header: "经销商编码", dataIndex: 'DEALER_CODE',  align: 'center'},
    {header: "经销商名称", dataIndex: 'DEALER_NAME',  align: 'center'},
    {header: "配件编码", dataIndex: 'PART_OLDCODE', style: 'text-align:left'},
    {header: "配件名称", dataIndex: 'PART_CNAME', style: 'text-align:left'},
    {header: "件号", dataIndex: 'PART_CODE', style: 'text-align:left'},
    {header: "销售数量", dataIndex: 'QTY', style: 'text-align:left'},//renderer: getItemValue
    {header: "创建日期", dataIndex: 'CREATE_DATE', align: 'center'},
    {header: "占用类型", dataIndex: 'BTYPE', align: 'center'}
];

var len = columns.length;

//格式化日期
function formatDate(value,meta,record){
	var output = value.substr(0,10);
	return output;
}

//导出
function expPurOrderInExcel() {
    fm.action = "<%=contextPath%>/report/partReport/partStockReport/PurOrderInReport/expPartsYxzyDataExcel.do";
    fm.target = "_self";
    fm.submit();
}
</script>
</div>
</body>
</html>