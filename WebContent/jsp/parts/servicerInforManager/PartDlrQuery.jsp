<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="com.infodms.dms.common.Constant" %>
<%@taglib uri="/jstl/cout" prefix="c" %>
<%
    String contextPath = request.getContextPath();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <jsp:include page="${contextPath}/common/jsp_head_new.jsp"/>
    <title>服务商提报车型维护</title>
    <script type="text/javascript">
        function downloadFunc() {
            var url = "<%=contextPath%>/parts/baseManager/PartDealerManager/PartDlrMng/dealerInfoDownload.json";
            document.fm.action = url;
            document.fm.submit();
        }
    </script>
</head>

<body onload="__extQuery__(1);">
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif"/>&nbsp;当前位置： 基础信息管理 &gt; 配件基础信息维护 &gt; 服务商提报车型维护</div>
<form id="fm" name="fm" method="POST" enctype="multipart/form-data">
    <input id="COMPANY_ID" name="COMPANY_ID" type="hidden"/>
    <input type="hidden" name="curPage" id="curPage" value="${curPage}"/>
    <table class="table_query" border="0">
        <tr>
            <td width="10%" align="right">服务商代码：</td>
            <td width="20%" class="table_query_4Col_input"><input
                    name="DEALER_CODE" maxlength="30" datatype="1,is_noquotation,30" id="DEALER_CODE" type="text"
                    class="middle_txt" value=""/></td>
            <td width="10%" align="right">服务商简称：</td>
            <td width="20%" class="table_query_4Col_input"><input
                    name="DEALER_NAME" maxlength="30" datatype="1,is_noquotation,75" id="DEALER_NAME" type="text"
                    class="middle_txt"/></td>
            <td width="10%" align="right">服务商状态：</td>
            <td width="20%" class="table_query_4Col_input">
                <label>
                    <script type="text/javascript">
                        genSelBoxExp("DEALERSTATUS", <%=Constant.DLR_SERVICE_STATUS%>, "<%=Constant.DLR_SERVICE_STATUS_02 %>", true, "short_sel", '', "false", '');
                    </script>
                </label>
            </td>
        <tr align="center">
            <td colspan="6" class="table_query_4Col_input" style="text-align: center">
                <input name="queryBtn" type="button" class="normal_btn" onclick="__extQuery__(1)" value="查 询"
                       id="queryBtn"/>
                <input type="button" class="normal_btn" id="downloadIt" name="downloadIt" onclick="downloadFunc() ;"
                       value="导 出"/>
                <input class="normal_btn" type="button" id="BtnuploadTable2" value="批量导入" onclick="showUpload2();"/>
                <%-- <input name="BtnSetting" type="button" class="long_btn" onclick="setType()" value="设置服务商类型"
                        id="BtnSetting"/>--%>
            </td>
        </tr>
    </table>
    <table id="uploadTable2" style="display: none">
        <tr>
            <td style="color: red">
                <input type="button" class="normal_btn" value="下载模版" onclick="downloadFunc()"/>
                文件选择后,点&quot;确定&quot;按钮,完成上传操作：&nbsp;此处导入是覆盖导入，请慎重操作
                <input type="file" name="uploadFile" style="width: 250px" id="uploadFile" value=""/>
                &nbsp;
                <input type="button" id="upbtn" class="normal_btn" value="确定" onclick="uploadEx('2')"/></td>
        </tr>
    </table>
</form>
<jsp:include page="${contextPath}/queryPage/orderHidden.html"/>
<jsp:include page="${contextPath}/queryPage/pageDiv.html"/>
<script>
    var myPage;
    var url = "<%=contextPath%>/parts/baseManager/PartDealerManager/PartDlrMng/queryServicerInfo.json";
    var title = null;
    var columns = [
        {header: "序号", dataIndex: 'RECORD_ID', renderer: getIndex},
        {id: 'action', header: "操作", walign: 'center', dataIndex: 'DEALER_ID', renderer: myLink},
        {header: "服务商代码", width: '10%', style: 'text-align: left;', dataIndex: 'DEALER_CODE'},
        {header: "服务商简称", width: '20%', style: 'text-align: left;', dataIndex: 'DEALER_NAME'},
        {header: "配件车型", width: '10%', dataIndex: 'CARTYPE'}
    ];

    function myLink(dealer_id) {
        return String.format("<a href=\"#\" onclick='showDiv(\"" + dealer_id + "\")'>[维护]</a>");
    }

    function showDiv(dealerId) {
        OpenHtmlWindow("<%=contextPath%>/parts/baseManager/PartDealerManager/PartDlrMng/queryServicerInfoDetail.do?DEALER_ID=" + dealerId, 1000, 450);
    }

    //设置类型
    function setType() {
        var serviceType = document.getElementById('PDEALERTYPE').value;
        if (serviceType == "") {
            MyAlert("请选择服务商类型");
            return;
        }
        var count = 0;
        var s = "";
        var ss = document.getElementsByName('setType');
        for (var i = 0; i < ss.length; i++) {
            if (ss[i].checked) {
                count++;
                s = s + ss[i].value + "@@";
            }
        }
        if (count == 0) {
            MyAlert("请至少勾选一项");
            return;
        }

        var url = '<%=contextPath%>/parts/baseManager/PartDealerManager/PartDlrMng/resetServiceType.json?serviceType=' + serviceType + '&dealerId=' + s + '&curPage=' + myPage.page;
        makeNomalFormCall(url, showResult, 'fm');
        //fm.submit();
    }

    //回调方法
    function showResult(json) {
        if (json.returnValue == 1) {
            MyAlert("操作成功!");
            __extQuery__(json.curPage);
        } else {
            MyAlert("操作失败！请联系系统管理员！");
        }
    }

    //设置类型复选框
    function typeCheckBox(value, meta, record) {
        var dealerId = record.data.DEALER_ID;
        var s = String.format("<input type='checkbox' name = 'setType' id='setType' value='" + dealerId + "'/>");
        return s;
    }

    function clrTxt(txtId) {
        document.getElementById(txtId).value = "";
    }

    function updateDlrCarType(dealerId, carTypeIds, mcurPage) {
        var url = g_webAppName + "/parts/baseManager/PartDealerManager/PartDlrMng/updateDLRCarType.json?dealerId=" + dealerId + "&carTypeIds=" + carTypeIds + "&mcurPage=" + mcurPage;
        makeNomalCall(url, getResult, []);
    }
    function getResult(json) {
        /* if (json.success) {
         MyAlert(json.success);
         } else {
         MyAlert(json.errorMsg);
         }*/
        __extQuery__(1);
    }
    function showUpload2() {
        $("BtnuploadTable2").style.cssText = "background-color:blue";
        if ($("uploadTable2").style.display == "none") {
            $("uploadTable2").style.display = "block";
        } else {
            $("uploadTable2").style.display = "none";
            $("BtnuploadTable2").style.cssText = "background-color:none";
        }
        $("uploadTable").style.display = "none";
        $("BtnuploadTable").style.cssText = "background-color:none";
    }
    function uploadEx(flag) {
        var fileValue = document.getElementById("uploadFile").value;

        if (fileValue == '') {
            MyAlert('导入文件不能空!');
            return;
        }
        var fi = fileValue.substring(fileValue.length - 3, fileValue.length);
        if (fi != 'xls') {
            MyAlert('导入文件格式不对,请导入xls文件格式');
            return;
        }
        fm.action = "<%=contextPath%>/parts/baseManager/PartDealerManager/PartDlrMng/uploadExcel.do";
        fm.submit();
    }
</script>
</body>
</html>
