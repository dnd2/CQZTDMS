<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" pageEncoding="UTF-8" %>
<%
    String contextPath = request.getContextPath();
%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <jsp:include page="${contextPath}/common/jsp_head_new.jsp"/>
    <title>平均销量权重设置</title>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <script language="javascript" type="text/javascript">
        //表单提交方法：
        function checkForm() {
            btnDisable();
            makeFormCall('<%=contextPath%>/parts/baseManager/partsPlanManager/avgSaleParmsSetAction/saveAvgSaleParmsSet.json', showResult, 'fm');
        }
        function showResult(json) {
            btnEnable();
            if (json.errorExist != null && json.errorExist.length > 0) {
                MyAlert(json.errorExist);
            } else if (json.success != null && json.success == "true") {
                window.location.href = "<%=contextPath%>/parts/baseManager/partsPlanManager/avgSaleParmsSetAction/avgSaleParmsSetInit.do";
            }
            else {
                disableBtn($("saveBtn"));
                MyAlert("保存失败，请联系管理员！");
            }
        }

        function checkData(obj) {
            var val = obj.value;
            if (isNaN(val)) {
                MyAlert("请输入数字!");
                obj.value = "";
                return;
            }
            if (0 > val) {
                MyAlert("所占权重不能小于 0");
                obj.value = "";
                return;
            }
            if (1 < val) {
                MyAlert("所占权重不能大于 1");
                obj.value = "";
                return;
            }
        }
        //表单提交前的验证：
        function checkFormUpdate() {
            var trVale = parseFloat(document.getElementById("threeWeight").value);
            var sixVale = parseFloat(document.getElementById("sixWeight").value);
            var twVale = parseFloat(document.getElementById("twelveWeight").value);
            var total = parseFloat(trVale + sixVale + twVale);
            if (1 != total) {
                MyAlert("所占权重总和必须为 1!");
                return false;
            }

            MyConfirm("确认保存设置?", checkForm);
        }

        //失效按钮
        function btnDisable() {

            $$('input[type="button"]').each(function (button) {
                button.disabled = true;
            });

        }

        //有效按钮
        function btnEnable() {

            $$('input[type="button"]').each(function (button) {
                button.disabled = "";
            });

        }
    </script>
</head>
<body>
<div class="wbox">
    <div class="navigation">
        <img src="<%=contextPath%>/img/nav.gif"/>&nbsp;当前位置：基础数据管理 &gt; 计划相关参数维护 &gt; 平均销量权重维护
    </div>
    <form name='fm' id='fm' method="post">
        <input type="hidden" name="deftId" id="deftId" value="${deftId}"/>
        <div class="form-panel">
            <h2><img class="panel-icon nav" src="<%=contextPath%>/img/subNav.gif"/> 设置信息</h2>
            <div class="form-body">
                <table class="table_edit">
                    <th colspan="6" width="100%"></th>
                    <tr>
                        <td width="10%" align="right">前三月权重：</td>
                        <td width="25%">
                            <input class="middle_txt" id="threeWeight" name="threeWeight" type="text" value="${threeWeight }"
                                onchange="checkData(this)"/>
                        </td>
                        <td width="10%" align="right">前六月权重：</td>
                        <td width="25%">
                            <input class="middle_txt" type="text" id="sixWeight" name="sixWeight" value="${sixWeight }"
                                onchange="checkData(this)"/>
                        </td>
                        <td width="10%" align="right">前12月权重：</td>
                        <td width="25%">
                            <input type="text" class="middle_txt" id="twelveWeight" name="twelveWeight" value="${twelveWeight }"
                                onchange="checkData(this)"/>
                        </td>
                    </tr>
                </table>
                <table class="table_edit" width="100%">
                    <tr>
                        <td class="center" align="center">
                            <input type="button" name="saveBtn" id="saveBtn" value="保 存" onclick="checkFormUpdate();"
                                class="u-button"/>
                        </td>
                    </tr>
                </table>
            </div>
        </div>
    </form>
</div>    

</body>
</html>
