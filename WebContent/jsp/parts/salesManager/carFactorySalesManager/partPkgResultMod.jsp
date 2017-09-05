<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c" %>
<%
    String contextPath = request.getContextPath();
%>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp"/>
<head>
<title>装箱修改</title>
<script language="javascript">
    //初始化查询TABLE
    var myPage;
    var url = "<%=contextPath%>/parts/salesManager/carFactorySalesManager/PartPkgResult/queryPkgInfo.json";
    var title = null;
    var columns = [
        {header: "序号", align: 'center', renderer: getIndex},
        {header: "操作", dataIndex: 'PKG_NO', align: 'center', renderer: optLink},
        {header: "箱号", dataIndex: 'PKG_NO', align: 'center'},
        {header: "包装尺寸(长*宽*高)", dataIndex: 'BZCC', align: 'center'},
        {header: "体积(M<sup>3</sup>)", dataIndex: 'VOLUME', align: 'center'},
        {header: "单箱重量(KG)", dataIndex: 'WEIGHT', align: 'center'},
        {header: "计费重量(KG)", dataIndex: 'CH_WEIGHT', align: 'center'},
        {header: "折合重量(KG)", dataIndex: 'EQ_WEIGHT', align: 'center'}
    ];
    function optLink(value, meta, record) {
        var boxId_pkgNo = record.data.BOX_ID+","+record.data.PKG_NO;
        var flag = record.data.FLAG
        var modPkg = "<a href=\"#\"  onclick='modPkg(\"" + boxId_pkgNo + "\")'>[修改包装信息]</>";
        var modifyDlt = "<a href=\"#\" onclick='modifyDlt(\"" + value + "\")'>[修改装箱明细]</>"
        var delPkg = "<a href=\"#\" onclick='delPkg(\"" + value + "\")'>[取消装箱]</>";
        if(flag == '0'){
            return modPkg+modifyDlt+delPkg;
        }else if(flag == '1'){
            return modPkg;
        }else{
            return modPkg;
        }
    }

    function modPkg(pkgNo) {
        var pickOrderId = $("#pickOrderId1")[0].value;
        window.location.href = "<%=contextPath%>/parts/salesManager/carFactorySalesManager/PartPkgResult/modifyOrder.do?boxId_pkgNo=" + pkgNo + "&pickOrderId=" + pickOrderId;
    }

    function modifyDlt(pkgNo) {
        var pickOrderId = $("#pickOrderId1")[0].value;
        var whId = $("#whId")[0].value;
        window.location.href = "<%=contextPath%>/parts/salesManager/carFactorySalesManager/PartPkgResult/modifyDtlPage.do?pkgNo=" + pkgNo + "&pickOrderId=" + pickOrderId + "&whId=" + whId;
    }

    function delPkg(pkgNo) {
    	MyConfirm("确定取消?",confirmResult,[pkgNo]);
    }

    function confirmResult(pkgNo){
        var pickOrderId = $("#pickOrderId1")[0].value;
        var rollbackUrl = "<%=contextPath%>/parts/salesManager/carFactorySalesManager/PartPkgResult/rollbackOrder1.json?pkgNo=" + pkgNo + "&pickOrderId=" + pickOrderId;
        makeNomalFormCall(rollbackUrl, ajaxResult, 'fm');
    }

    function ajaxResult(jsonObj) {
        if (jsonObj != null) {
            var success = jsonObj.success;
            var error = jsonObj.error;
            var exceptions = jsonObj.Exception;
            if (success) {
                MyAlert(success);
                __extQuery__(1);
            } else if (error) {
                MyAlert(error);
            }/*  else if (exceptions) {
                MyAlert(exceptions.message);
            } */
        }
    }

    function reCallRefFn(id) {
        window.location.href = "<%=contextPath%>/parts/salesManager/carFactorySalesManager/PartPkgResult/modifyOrderPage.do?pickOrderId=" + id;
    }
    function disabledAll() {
        jQuery("input[type=button]").attr('disabled', 'true');
        jQuery("a").attr('disabled', 'true');
    }
    function enableAll() {
        jQuery("input[type=button]").attr('disabled', 'false');
        jQuery("a").attr('disabled', 'false');
    }
  
    function viewPkgDtl() {
        OpenHtmlWindow("<%=request.getContextPath()%>/parts/salesManager/carFactorySalesManager/PartPkg/partPkgDtlInit.do?pickOrderID=" +${pickOrderId}, 800, 450);
    }
    function viewNonePkgDtl() {
        OpenHtmlWindow("<%=request.getContextPath()%>/parts/salesManager/carFactorySalesManager/PartPkg/partNonePkgDtlInit.do?pickOrderID=" +${pickOrderId}, 800, 450);
    }
    
    function goback1(){
    	window.location.href = "<%=contextPath%>/parts/salesManager/carFactorySalesManager/PartPkgResult/init.do";
    }
    
    $(document).ready(function(){
    	__extQuery__(1);
    });
</script>
</head>
<body onunload='javascript:destoryPrototype();'>
<form name="fm" id="fm" method="post" enctype="multipart/form-data">
    <input name="pickOrderId1" id="pickOrderId1" value="${pickOrderId}" type="hidden"/>
    <input name="whId" id="whId" value="${whId}" type="hidden"/>
    <div class="wbox">
        <div class="navigation"><img src="<%=contextPath%>/img/nav.gif" alt=""/>&nbsp;当前位置: 配件管理 &gt 配件销售管理 &gt 装箱结果修改&gt 装箱修改</div>
        <div class="form-panel">
		     <h2><img class="panel-icon nav" src="<%=contextPath%>/img/subNav.gif"/>装箱信息
		     	&nbsp;&nbsp;<font style="color: red;font-weight: bold">拣货单：${pickOrderId}</font>
              </h2>
		     <div class="form-body">
        <table border="0" class="table_query">
            <tr>
                <td align="right">配件编码：</td>
                <td width="24%"><input type="text" id="PART_OLDCODE" name="PART_OLDCODE" class="middle_txt">
                <td align="right">配件名称：</td>
                <td width="24%"><input type="text" id="PART_CNAME" name="PART_CNAME" class="middle_txt">
                </td>
                <td align="right">箱号：</td>
                <td width="24%"><input class="middle_txt" type="text" id="PKG_NO" name="PKG_NO"/></td>
            </tr>

            <tr>
                <td class="center" colspan="6">
                    <input class="normal_btn" type="button" name="BtnQuery"
                           id="queryBtn" value="查 询" onclick="__extQuery__(1);"/>
                    &nbsp;
                    <input class="normal_btn" type="button" value="装 箱 明 细" name="printPrintInfo"
                           id="printPrintInfo" onclick="viewPkgDtl();"/>
                    &nbsp;
                    <input class="normal_btn" type="button" value="未装箱明细" name="printPrintInfo"
                           id="printPrintInfo" onclick="viewNonePkgDtl();"/>
                    <input class="normal_btn" type="button" value="返 回" id="goback" name="goback" onclick="goback1();"/>
                </td>
            </tr>
        </table>
        </div>
        </div>
        <jsp:include page="${contextPath}/queryPage/orderHidden.html"/>
        <jsp:include page="${contextPath}/queryPage/pageDiv.html"/>
    </div>
</html>
</form>
</body>
</html>