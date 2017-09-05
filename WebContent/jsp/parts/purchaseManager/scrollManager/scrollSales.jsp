<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@taglib uri="/jstl/cout" prefix="c" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<%String contextPath = request.getContextPath();%>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <meta http-equiv="X-UA-Compatible" content="IE=7">
    <jsp:include page="${contextPath}/common/jsp_head_new.jsp"/>

    <title>滚动计划编制</title>
    <script type="text/javascript">
        function getYearSelect(id, name, scope, value) {
            var date = new Date();
            var year = date.getFullYear();    //获取完整的年份
            var month = date.getMonth() + 2;
            if(month>12){
                year=year+1;
            }
            var str = "";
            str += "<select  id='" + id + "' name='" + name + "'  style='width:55px;'>";
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
            str += "<select  id='" + id + "' name='" + name + "'  style='width:45px;'>";
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
    <form name='fm' id='fm' method="post" enctype="multipart/form-data">
        <div class="navigation">
            <img src="<%=contextPath%>/img/nav.gif"/>&nbsp;当前位置：总部采购管理&gt;月度（滚动）需求计划编制
        </div>
        <input id="updFlag" name="updFlag" type="hidden" value=""/>
        <input id="backUrl" name="backUrl" type="hidden" value="sales"/>
        <table class="table_query">
            <tr>
                <td width="10%" align="right">计划年月:</td>
                <td width="20%">
                    <script type="text/javascript">
                        getYearSelect("MYYEAR", "MYYEAR", 1, '');
                    </script>
                    <script type="text/javascript">
                        getMonThSelect("MYMONTH", "MYMONTH", '');
                    </script>
                <td width="10%" align="right"></td>
                <td width="20%"></td>
                <td align="right">计划单号：</td>
                <td><input name="planNo" id="planNo" value="" type="text" class="middle_txt"/></td>
            </tr>
            <tr id="tr1">
                <td colspan="6" align="center">
                    <input class="normal_btn" type="button" name="BtnQuery" id="queryBtn" onclick="__extQuery__(1);"
                           value="查 询"/>
                    <!-- <input class="normal_btn" type="button" name="button2" onclick="expDlt();" value="导出计划" />
                    <input class="normal_btn" type="button" name="button2" onclick="expErrDlt();" value="导出错误" /> -->
                    <input class="normal_btn" style="width:80px;" type="button" value="导入滚动计划" name="button5"
                           onclick="showUpload();">
                    <!--<input class="normal_btn" type="button" name="button3"onclick="del();" value="删 除"  />
                  <input class="normal_btn" type="button" name="button3"onclick="save();" value="保 存"  />
                  <input class="normal_btn" type="button" name="button3"onclick="delConfirm();" value="操作订单"  /> -->
                </td>
            </tr>
            <tr id="tr2" style="display: none;">
                <td colspan="6" align="center">
                    <input class="normal_btn" type="button" name="BtnQuery" id="queryBtn" onclick="__extQuery__(1);"
                           value="查 询"/>
                    <input class="normal_btn" type="button" value="导入补充计划" style="width:80px;" name="button5"
                           onclick="showUpload();">
                    <input class="normal_btn" type="button" name="button3" onclick="save();" value="保 存"/>
                    <!--  <input class="normal_btn" type="button" onclick="BConfirm();" name="button4" value="提 交" /> -->
                    <!-- <input class="normal_btn" type="button" onclick="allSubChk();" name="button5" value="一键提交" /> -->

                </td>
            </tr>
            
        </table>
        <!-- 批量导入 -->
        <div style="display:none ; heigeht: 5px" id="uploadDiv">
            <table class="table_query">
                <th colspan="6"><img class="nav" src="<%=request.getContextPath()%>/img/subNav.gif"/> 上传文件</th>
                <tr>
                    <td colspan="6" align="center">
                        <input type="button" class="normal_btn" value="下载模版" onclick="exportExcelTemplate()"/>
                        <font color="red"> 销售计划文件选择后,点 [ 确定 ] 按钮,完成上传操作：</font>
                        <input type="file" name="uploadFile" id="uploadFile" style="width: 250px"
                               datatype="0,is_null,2000"/>
                               <input type="button" id="upbtn" class="normal_btn" value="确 定" onclick="confirmUploadUpdate()"/>
                    </td>
                </tr>
                

            </table>
        </div>
        <!--分页 begin -->
        <jsp:include page="${contextPath}/queryPage/orderHidden.html"/>
        <jsp:include page="${contextPath}/queryPage/pageDiv.html"/>
        <!--分页 end -->
    </form>
</div>
<script type="text/javascript">
    var myPage;
    var url = "<%=contextPath%>/parts/purchaseManager/scrollManager/ScrollSales/queryScrollSalesMain.json";
    var title = null;
    var columns = [];
    function callBack(json) {
        var ps;
        //设置对应数据
        if (Object.keys(json).length > 0) {
            keys = Object.keys(json);
            for (var i = 0; i < keys.length; i++) {
                if (keys[i] == "ps") {
                    ps = json[keys[i]];
                    break;
                }
            }
        }
        //生成数据集
        if (ps.records != null) {
            var chk = 0;
            var chkObjs = document.getElementsByName("RADIO_SELECT");
            for (var i = 0; i < chkObjs.length; i++) {
                if (chkObjs[i].checked) {
                    chk = chkObjs[i].value;
                    break;
                }
            }
            if (chk == 1) {
                columns = [
                    {header: "序号", renderer: getIndex, align: 'center'},
                    {header: "操作", align: 'center', dataIndex: 'PLAN_CODE', renderer: checkLink},
                    {header: "计划单号", dataIndex: 'PLAN_CODE', align: 'center'},
                    {header: "计划种类", dataIndex: 'PLAN_NUMS', align: 'center'},
                    {header: "计划数量", dataIndex: 'PLAN_COUNT', align: 'center'},
                    {header: "创建日期", dataIndex: 'CREATE_DATE', align: 'center'}
                ];
            } else {
                columns = [
                    {header: "序号", renderer: getIndex, align: 'center'},
                    {header: "操作", align: 'center', dataIndex: 'PLAN_CODE', renderer: checkLink},
                    {header: "计划单号", dataIndex: 'PLAN_CODE', align: 'center'},
                    {header: "计划种类", dataIndex: 'PLAN_NUMS', align: 'center'},
                    {header: "计划数量", dataIndex: 'PLAN_COUNT', align: 'center'},
                    {header: "创建日期", dataIndex: 'CREATE_DATE', align: 'center'}

                ];
            }
            len = columns.length;

            $("_page").hide();
            $('myGrid').show();
            new createGrid(title, columns, $("myGrid"), ps).load();
            //分页
            myPage = new showPages("myPage", ps, url);
            myPage.printHtml();
        } else {
            $("_page").show();
            $("_page").innerHTML = "<div class='pageTips'>没有满足条件的数据!</div>";
            $("myPage").innerHTML = "";
            removeGird('myGrid');
            $('myGrid').hide();
            hiddenDocObject(1);
        }
    }
    function showUpload() {
        var uploadDiv = document.getElementById("uploadDiv");
        if (uploadDiv.style.display == "block") {
            uploadDiv.style.display = "none";
        } else {
            uploadDiv.style.display = "block";
        }
    }
    function exportExcelTemplate() {
        fm.action = "<%=contextPath%>/parts/purchaseManager/scrollManager/ScrollSales/exportExcelTemplate.do";
        fm.submit();
    }
    function delPlan(value) {
        MyConfirm('是否确定删除此计划单？', delPlanSure, [value]);

    }
    function delPlanSure(value) {

        var url = "<%=contextPath%>/parts/purchaseManager/scrollManager/ScrollSales/delPlan.json?planNo=" + value;

        sendAjax(url, getResult, 'fm');
    }


    function checkLink(value, meta, record) {
        var output = "<a href=\"#\" onclick='view(\"" + value + "\")'>[明细]</a>&nbsp;<a href=\"#\" onclick='subPlanNew(\"" + value +
                "\")'>[提交]</a>&nbsp;<a href=\"#\" onclick='delPlan(\"" + value + "\")'>[删除]</a>&nbsp;<a href=\"#\" onclick='expDlt(\"" + value +
                "\")'>[导出计划]</a>&nbsp;<a href=\"#\" onclick='expErrDlt(\"" + value + "\")'>[导出错误]</a>";
        return output;
    }
    //上传更新检查和确认信息
    function confirmUploadUpdate() {
        var upd = document.getElementById("updFlag");
     
      
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
        
            upd.value = "del";
            MyConfirm("确定新增滚动计划？", uploadUpdate, []);
        

    }

    function uploadUpdate(flag) {
        btnDisable();
        fm.action = g_webAppName + "/parts/purchaseManager/scrollManager/ScrollSales/impUpdateUpload.do";
        fm.submit();
    }


    function query() {
        __extQuery__(1);
    }

    //滚动计划提交

    //补充计划提交
    function BConfirm() {
        MyConfirm('是否提交该补充计划？', BCSubPlan);
    }

    function BCSubPlan() {
        btnDisable();
        var checkboxs = document.getElementsByName('cb');
        var PLAN_MONTH_ONE = "";
        var WEEK_ONE = "";
        var WEEK_TOW = "";
        var WEEK_THREE = "";
        var WEEK_FOUR = "";
        var SALES_REMARK = "";
        var ID = "";
        var flag = true;
        for (var i = 0; i < checkboxs.length; i++) {
            var id = checkboxs[i].value;
            if (checkboxs[i].checked) {
                flag = false;
                if (i + 1 === checkboxs.length) {
                    PLAN_MONTH_ONE += document.getElementById(id + 'PLAN_MONTH_ONE').value;
                    WEEK_ONE += document.getElementById(id + 'WEEK_ONE').value;
                    WEEK_TOW += document.getElementById(id + 'WEEK_TOW').value;
                    WEEK_THREE += document.getElementById(id + 'WEEK_THREE').value;
                    WEEK_FOUR += document.getElementById(id + 'WEEK_FOUR').value;
                    SALES_REMARK += document.getElementById(id + 'SALES_REMARK').value;
                    ID += id;
                } else {
                    PLAN_MONTH_ONE += document.getElementById(id + "PLAN_MONTH_ONE").value + ',';
                    WEEK_ONE += document.getElementById(id + 'WEEK_ONE').value + ',';
                    WEEK_TOW += document.getElementById(id + 'WEEK_TOW').value + ',';
                    WEEK_THREE += document.getElementById(id + 'WEEK_THREE').value + ',';
                    WEEK_FOUR += document.getElementById(id + 'WEEK_FOUR').value + ',';
                    SALES_REMARK += document.getElementById(id + 'SALES_REMARK').value + ',';
                    ID += id + ',';

                }
            }
        }
        SALES_REMARK = encodeURI(encodeURI(SALES_REMARK));
        if (flag) {
            btnEnable();
            MyAlert("未选择任何数据！");
            return;
        }
        var url = "<%=contextPath%>/parts/purchaseManager/scrollManager/ScrollSales/BCSub.json?PLAN_MONTH_ONE=" + PLAN_MONTH_ONE + "&WEEK_ONE=" + WEEK_ONE + "&WEEK_TOW=" + WEEK_TOW + "&WEEK_THREE=" + WEEK_THREE + "&WEEK_FOUR=" + WEEK_FOUR + "&ID=" + ID + "&SALES_REMARKS=" + SALES_REMARK;

        sendAjax(url, getResult, 'fm');
    }

    function subPlanNew(value) {
        MyConfirm("确定要提交需求", subPlanNewSure, [value]);
    }
    function subPlanNewSure(value) {
        var year = document.getElementById("MYYEAR").value;
        var month = document.getElementById("MYMONTH").value;
        var url = "<%=contextPath%>/parts/purchaseManager/scrollManager/ScrollSales/allSub.json?planNo=" + value + "," + year + "," + month;
        sendAjax(url, getResult, "fm");
    }
    function getResult(json) {
        btnEnable();
        if (json.s == 's') {
            MyAlert("操作成功!");

        } else {
            MyAlert(json.remark);
        }
        __extQuery__(1);
    }
    function btnDisable() {

        $$('input[type="button"]').each(function (button) {
            button.disabled = true;
        });
    }
    function btnEnable() {

        $$('input[type="button"]').each(function (button) {
            button.disabled = "";
        });
    }
    //导出明细
    function delConfirm() {
        var year = document.getElementById("MYYEAR").value;
        var month = document.getElementById("MYMONTH").value;
        OpenHtmlWindow(g_webAppName + "/jsp/parts/purchaseManager/scrollManager/chosePlan.jsp?way=del&year=" + year + "&month=" + month, 730, 390);
    }

    function getDelResult(json) {
        btnEnable();
        if (json.s == "s") {
            MyAlert("删除成功!");
            __extQuery__(1);
        } else {

            MyAlert(json.e);
        }

    }
    function expDlt(value) {
        document.getElementById("planNo").value = value;
        fm.action = "<%=contextPath%>/parts/purchaseManager/scrollManager/ScrollSales/expDtl.do";
        fm.target = "_self";
        fm.submit();
    }
    function expErrDlt(value) {
        document.getElementById("planNo").value = value;
        fm.action = "<%=contextPath%>/parts/purchaseOrderManager/PurchaseOrderInManager/exportErrorInfoExcel.do";
        fm.target = "_self";
        fm.submit();
    }
    function showPartInfo(obj) {
        //debugger;
        if (obj.value == '1') {
            document.getElementById("tr1").style.display = "block";
            document.getElementById("tr2").style.display = "none";
        } else {
            document.getElementById("tr1").style.display = "none";
            document.getElementById("tr2").style.display = "block";
        }
        __extQuery__(1);
    }

    function del() {

        MyConfirm('是否删除所选择的计划？', delNddes);
    }
    function delNddes() {
        var ID = "";
        btnDisable();
        var checkboxs = document.getElementsByName('cb');
        var flag = true;
        for (var i = 0; i < checkboxs.length; i++) {
            var id = checkboxs[i].value;
            if (checkboxs[i].checked) {
                flag = false;
                ID += id + ',';

            }
        }
        if (flag) {
            btnEnable();
            MyAlert("未选择任何数据！");
            return;
        }
        var url = "<%=contextPath%>/parts/purchaseManager/scrollManager/ScrollSales/del.json?ID=" + ID;
        sendAjax(url, getResult, 'fm');
    }


    function view(value) {
        var chk = 0;
        var chkObjs = document.getElementsByName("RADIO_SELECT");
        for (var i = 0; i < chkObjs.length; i++) {
            if (chkObjs[i].checked) {
                chk = chkObjs[i].value;
                break;
            }
        }
        window.location.href = "<%=contextPath%>/parts/purchaseManager/scrollManager/ScrollSales/ScrollSalesDtlInit.do?planNo=" + value + "&RADIO_SELECT=" + chk;
    }
</script>
</body>
</html>