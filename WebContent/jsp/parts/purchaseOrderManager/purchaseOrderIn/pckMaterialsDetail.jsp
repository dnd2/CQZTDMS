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
    <title>包装材料出入库明细报表</title>
    <script language="JavaScript">

        //初始化方法
        function doInit() {
           // loadcalendar();  //初始化时间控件
        }

    </script>
</head>
<body onload="__extQuery__(1);"> <!-- onunload='javascript:destoryPrototype()' -->
<div class="wbox">
<div class="navigation"><img src="<%=request.getContextPath()%>/img/nav.gif"/>&nbsp;当前位置： 报表管理&gt;配件报表&gt;本部仓储报表&gt;包装材料出入库明细报表</div>
<form method="post" name="fm" id="fm" enctype="multipart/form-data">
    <input type="hidden" name="curPage" id="curPage"/>
    <input type="hidden" name="partId" id="partId"/>
    <div class="form-panel">
        <h2><img src="<%=contextPath%>/jmstyle/img/search-ico.png" class="panel-query-title"/>查询条件</h2>
        <div class="form-body">
            <table class="table_query" width=100% border="0" align="center" cellpadding="1" cellspacing="1" >
                <tr>
                    <td class="right" width="10%" align="right">规格：</td>
                    <td width="20%" ><input class="middle_txt" type="text" name="Specification" id="Specification"/></td>
                    <td class="right" width="10%" align="right">名称：</td>
                    <td width="20%" ><input class="middle_txt" type="text" name="c_name" id="c_name"/></td>
                </tr>
                <tr>    
                    <td class="right" width="10%" align="right">日期：</td>
                    <td width="20%" align="left" colspan="3">
                        <input class="time_txt middle_txt calendar-input-long" id="SCREATE_DATE" name="SCREATE_DATE" datatype="1,is_date,10" maxlength="10" value="${start}" group="SCREATE_DATE,ECREATE_DATE"/>
                        <input class="time_ico" value=" " type="button"/>至
                        <input class="time_txt middle_txt calendar-input-long" id="ECREATE_DATE" name="ECREATE_DATE" datatype="1,is_date,10" value="${end}" maxlength="10" group="SCREATE_DATE,ECREATE_DATE"/>
                        <input class="time_ico" value=" " type="button"/>
                    </td>
                </tr>
                <tr>
                    <td class="txt-center" colspan="6" align="center">
                            <input name="BtnQuery" id="queryBtn" class="u-button u-query" type="button" value="查 询" onclick="__extQuery__(1);"/>
                            <input class="u-button" type="button" value="导出" onclick="expPurOrderInExcel();"/>
                        <c:choose>
                        <c:when test="${buttonFalg == 1}">
                            <input class="u-button u-cancel" type="button" value="关 闭" onclick="goClose()"/>
                        </c:when>
                        <c:otherwise>
                        </c:otherwise>
                    </c:choose>
                    </td>
                </tr>
            </table>
        </div>
    </div>
    
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
</form>

<script type="text/javascript">
//autoAlertException();//输出错误信息
var myPage;
var url = "<%=contextPath%>/report/partReport/partStockReport/PurOrderInReport/queryPckMaterialsDetail.json";

var title = null;

var columns = [
    {header: "序号", align:'center',renderer:getIndex},
    {header: "规格", dataIndex: 'PACK_SPEC',  align: 'center'},
    {header: "名称", dataIndex: 'PACK_NAME',  align: 'center'},
    {header: "出入库类型", dataIndex: 'FLAG', align: 'center'},
    {header: "数量", dataIndex: 'QTY', align: 'center'},
    {header: "备注", dataIndex: 'REMARK', align: 'center'},
    {header: "操作人", dataIndex: 'NAME', align: 'center'},
    {header: "日期", dataIndex: 'CREATE_DATE', align: 'center'}//renderer: getItemValue
];

var len = columns.length;

//格式化日期
function formatDate(value,meta,record){
	var output = value.substr(0,10);
	return output;
}

//导出
function expPurOrderInExcel() {
    fm.action = "<%=contextPath%>/report/partReport/partStockReport/PurOrderInReport/expPckMaterialsDetailExcel.do";
    fm.target = "_self";
    fm.submit();
}

//关闭
function goClose(){
	_hide();
}

</script>
</div>
</body>
</html>