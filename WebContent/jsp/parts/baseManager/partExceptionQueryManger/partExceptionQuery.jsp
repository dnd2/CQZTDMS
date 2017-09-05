<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@taglib uri="/jstl/cout" prefix="c" %>
<%@ page import="com.infodms.dms.common.Constant" %>
<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp"/>
<% String contextPath = request.getContextPath(); %>
<title>计划变更维护</title>
<script language="javascript" type="text/javascript">
$(function(){
	__extQuery__(1);
	
});
    var myPage;

    var url = "<%=contextPath%>/parts/baseManager/partExceptionQueryManger/partExceptionQuery/partExceptionQuerySearch.json";

    var title = null;

    var columns = [
        {header: "序号", dataIndex: 'SEQUENCE_ID', renderer: getIndex, style: 'text-align:left'},
        {id: 'action', header: "操作", sortable: false, dataIndex: 'ID', renderer: myLink, align: 'center'},
        {header: "配件编码", dataIndex: 'PART_OLDCODE', style: 'text-align:left'},
        {header: "配件名称", dataIndex: 'PART_CNAME', style: 'text-align:left'},
        {header: "件号", dataIndex: 'PART_CODE', style: 'text-align:left'},
        {header: "变更后编码", dataIndex: 'REPART_OLDCODE', style: 'text-align:left'},
        {header: "变更后名称", dataIndex: 'REPART_CNAME', style: 'text-align:left'},
        {header: "变更后件号", dataIndex: 'REPART_CODE', style: 'text-align:left'},
        {header: "替换类型", dataIndex: 'TYPE', style: 'text-align:left',  renderer: getItemValue},
        {header: "新增日期", dataIndex: 'CREATE_DATE', style: 'text-align:left'},
        {header: "修改日期", dataIndex: 'UPDATE_DATE', style: 'text-align:left'},
        {header: "修改人", dataIndex: 'ACNT', style: 'text-align:left'},
        {header: "是否有效", dataIndex: 'STATE', renderer: getItemValue}

    ];

    //设置超链接  begin

    //设置超链接
    function myLink(value, meta, record) {
        var partID = record.data.PART_ID;
        var replaceId = record.data.REPLACE_ID;
        var partOldcode = record.data.PART_OLDCODE;
        var rePartID = record.data.REPART_ID;
        var rePartCode = record.data.REPART_CODE;
        var rePartOldcode = record.data.REPART_OLDCODE;
        var rePartName = record.data.REPART_CNAME;
        var remark = record.data.REMARK;
        var disabeParms = (partID + "@@" + replaceId).toString();
        var enableParms = (partID + "@@" + rePartOldcode + "@@" + replaceId).toString();
        var parms = replaceId;
        var state = record.data.STATE;
        var disableValue = <%=Constant.STATUS_DISABLE%>;
        if (disableValue == state) {
            return String.format("<a href=\"#\" onclick='enableData(\"" + enableParms + "\")'>[有效]</a>");
        } else {
            return String.format("<a href=\"#\" onclick='formod(\"" + parms + "\")'>[修改]</a><a href=\"#\" onclick='cel(\"" + disabeParms + "\")'>[失效]</a>");
        }

    }

    //设置失效：
    function cel(parms) {
        MyConfirm("确定要有效?", function() {
            btnDisable();
            var url = '<%=contextPath%>/parts/baseManager/partExceptionQueryManger/partExceptionQuery/celPartExceptionQuery.json?disabeParms=' + parms + '&curPage=' + myPage.page;
            makeNomalFormCall(url, showResult, 'fm');
        }, []);
    }

    //设置有效：
    function enableData(parms) {
        MyConfirm("确定要有效?", function() {
            btnDisable();
            var url = '<%=contextPath%>/parts/baseManager/partExceptionQueryManger/partExceptionQuery/enablePartExceptionQuery.json?enableParms=' + parms + '&curPage=' + myPage.page;
            makeNomalFormCall(url, showResult, 'fm');
        }, []);
    }

    //删除设计
    function del(parms) {
        MyConfirm("确定要删除?", function() {
            btnDisable();
            var url = '<%=contextPath%>/parts/baseManager/partExceptionQueryManger/partExceptionQuery/delPartExceptionQuery.json?partId=' + parms;
            makeNomalFormCall(url, showResult, 'fm');
        }, []);
    }

    function showResult(json) {
        btnEnable();
        if (json.errorExist != null && json.errorExist.length > 0) {
            MyAlert("配件编码：【" + json.errorExist + "】替代件系统中已创建，不能重复创建！");
        } else if (json.success != null && json.success == "true") {
            __extQuery__(json.curPage);
            MyAlert("操作成功！");
        } else {
            MyAlert("操作失败，请联系管理员！");
        }
    }

    //新增
    function partAdd() {
        btnDisable();
        fm.action = "<%=contextPath%>/parts/baseManager/partExceptionQueryManger/partExceptionQuery/partExceptionQueryAddInit.do";
        fm.submit();
    }

    function formod(parms) {
        btnDisable();
        fm.action = "<%=contextPath%>/parts/baseManager/partExceptionQueryManger/partExceptionQuery/partExceptionQueryFormodInit.do?parms=" + parms;
        fm.submit();
    }
    //下载
    function exportPartExceptionExcel() {
        document.fm.action = "<%=contextPath%>/parts/baseManager/partExceptionQueryManger/partExceptionQuery/exportPartExceptionExcel.do";
        document.fm.target = "_self";
        document.fm.submit();
    }

</script>
</head>
<body>
<div class="wbox">    
    <form method="post" name="fm" id="fm">
        <div class="navigation"><img src="<%=request.getContextPath()%>/img/nav.gif"/>&nbsp;当前位置：
            配件管理 &gt; 基础信息管理 &gt; 配件基础信息维护 &gt; 设计变更维护
        </div>
        <div class="form-panel">
			<h2><img src="<%=contextPath%>/jmstyle/img/search-ico.png" style="margin-bottom: -13px;"/>查询条件</h2>
            <div class="form-body">
                <table class="table_query">
                    <tr>
                        <td class="right" width="20%" align="right">配件编码：</td>
                        <td width="30%"><input class="middle_txt" type="text" name="PART_CODE" id="PART_CODE"/></td>
                        <td class="right" width="20%" align="right">件号：</td>
                        <td width="30%"><input class="middle_txt" type="text" name="ORDER_ID" id="ORDER_ID"/></td>
                    </tr>
                    <tr>
                        <td class="right" width="20%" align="right">配件名称：</td>
                        <td width="30%"><input class="middle_txt" type="text" name="PART_NAME" id="PART_NAME"/></td>
                        <td class="right" width="20%" align="right">是否有效：</td>
                        <td width="30%">
                            <script type="text/javascript">
                                genSelBoxExp("stateValue", <%=Constant.STATUS%>, "<%=Constant.STATUS_ENABLE %>", true, "short_sel u-select", "onchange= __extQuery__(1)", "false", '');
                            </script>
                        </td>
                    </tr>
                    <tr>
                        <td class="formbtn-aln" align="center" colspan="6">
                            <input class="u-button" type="button" value="查 询" name="BtnQuery" id="queryBtn" onclick="__extQuery__(1)"/>
                            <input class="u-button" type="button" value="重 置" onclick="reset()"/>
                            <input class="u-button" type="button" value="新 增" onclick="partAdd()"/>
                            <input class="u-button" type="button" value="导 出" onclick="exportPartExceptionExcel()"/>
                        </td>
                    </tr>
                </table>    
            </div>
        </div>        

    <!-- 查询条件 end -->
    <!--分页 begin -->
    <jsp:include page="${contextPath}/queryPage/orderHidden.html"/>
    <jsp:include page="${contextPath}/queryPage/pageDiv.html"/>
    <!--分页 end -->
    </form>
</div>
</body>
</html>