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
    <title>库存盘点</title>
    <script language="JavaScript">

        //初始化方法
        function doInit() {
            loadcalendar();  //初始化时间控件
        }

    </script>
</head>
<body onunload='javascript:destoryPrototype()' onload="__extQuery__(1);">
<div class="wbox">
<div class="navigation"><img src="<%=request.getContextPath()%>/img/nav.gif"/>&nbsp;当前位置： 报表管理&gt;配件报表&gt;本部仓储报表&gt;库存盘点(本部)
</div>
<form method="post" name="fm" id="fm" enctype="multipart/form-data">
    <input type="hidden" name="expFlag" value="0"/>
    <table class="table_query" width=100% border="0" align="center" cellpadding="1" cellspacing="1" >
		<th colspan="6"><img src="<%=contextPath%>/img/subNav.gif" alt="" class="nav" />查询条件</th>
       <tr>
            <td width="10%" align="right">配件编码：</td>
            <td width="20%"><input name="PART_OLDCODE" type="text" class="middle_txt" id="PART_OLDCODE"/></td>
            <td width="10%" align="right">配件名称：</td>
            <td width="20%"><input name="PART_CNAME" type="text" class="middle_txt" id="PART_CNAME"/></td>
            <td width="10%" align="right">配件件号：</td>
            <td width="20%"><input name="PART_CODE" type="text" class="middle_txt" id="PART_CODE"/></td>
            
        </tr>
        
        <tr>
             <td width="10%" class="table_query_right" align="right">盘点日期：</td>
             <td width="20%">
                <input name="balBeginTime" id="balBeginTime" value="${old }" type="text" class="short_txt" datatype="1,is_date,10"
                       group="balBeginTime,balEndTime">
                <input name='button3' value=" " type='button' class='time_ico' title="点击选择时间"
                       onclick="showcalendar(event, 'balBeginTime', false);"/>
                至
                <input name="balEndTime" id="balEndTime" value="${now }" type="text" class="short_txt" datatype="1,is_date,10"
                       group="balBeginTime,balEndTime">
                <input name='button3' value=" " type='button' class='time_ico' title="点击选择时间"
                       onclick="showcalendar(event, 'balEndTime', false);"/>
            </td>
        </tr>
        
	  <tr>
	   <td   colspan="6" align="center">
	   		<input name="BtnQuery" id="queryBtn" class="normal_btn" type="button" value="查 询" onclick="__extQuery__(1);"/>
            <input class="normal_btn" type="button" value="导出" onclick="expPartCheckExcel();"/>
       </td>
      </tr>
	</table>
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
</form>

<script type="text/javascript">
autoAlertException();//输出错误信息
var myPage;
var url = "<%=contextPath%>/report/partReport/partStockReport/PartCheckReport/queryPartCheck.json";

var title = null;

var columns = [
    {header: "序号", align:'center',renderer:getIndex},
    {header: "配件编码", dataIndex: 'PART_OLDCODE', style: 'text-align:left'},
    {header: "配件名称", dataIndex: 'PART_CNAME', style: 'text-align:left'},
    {header: "配件件号", dataIndex: 'PART_CODE', style: 'text-align:left'},
    {header: "单位", dataIndex: 'UNIT', align: 'center'},
    {header: "盘点数量", dataIndex: 'DIFF_QTY', align: 'center'},
    {header: "盘点结果", dataIndex: 'CHECK_RESULT', align: 'center'},
    {header: "盘点日期", dataIndex: 'CREATE_DATE', align: 'center'}
];

var len = columns.length;

//格式化日期
function formatDate(value,meta,record){
	var output = value.substr(0,10);
	return output;
}

//导出
function expPartCheckExcel() {
    fm.action = "<%=contextPath%>/report/partReport/partStockReport/PartCheckReport/expPartCheckExcel.do";
    fm.target = "_self";
    fm.submit();
}

</script>
</div>
</body>
</html>