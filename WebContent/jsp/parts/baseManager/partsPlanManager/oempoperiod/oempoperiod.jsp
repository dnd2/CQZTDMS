<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" pageEncoding="UTF-8"%>
<%
    String contextPath = request.getContextPath();
%>
<%@ page import="com.infodms.dms.common.Constant"%>
<%@taglib uri="/jstl/cout" prefix="c"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
    <title>平均销量权重设置</title>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <script language="javascript" type="text/javascript">
        //表单提交方法：
        function checkForm() {
            btnDisable();
            makeFormCall('<%=contextPath%>/parts/baseManager/partsPlanManager/OemPartPOPeriod/saveoemPartPOPeriod.json', showResult, 'fm');
        }
        function showResult(json) {
            btnEnable();
            if (json.errorExist != null && json.errorExist.length > 0) {
                MyAlert(json.errorExist);
            } else if (json.success != null && json.success == "true") {
                MyAlert("修改成功!");
                if('${TYPE}'==<%=Constant.PART_PERIOD_TYPE_ORDER_02%>){
	                window.location.href = "<%=contextPath%>/parts/baseManager/partsPlanManager/OemPartPOPeriod/oemPartPOPeriodInit.do";
                }else{
                	window.location.href = "<%=contextPath%>/parts/baseManager/partsPlanManager/OemPartPOPeriod/oemRepartPOPeriodInit.do";
                }
            }
            else {
                disableBtn($("saveBtn"));
                MyAlert("保存失败，请联系管理员！");
            }
        }

        function checkData(obj)
        {
            var val = obj.value;
            if (isNaN(val)) {
                MyAlert("请输入数字!");
                obj.value = "";
                return;
            }
            var re = /^((0)|([1-9]+[0-9]*]*))$/;
    	    if (!re.test(obj.value)) {
    	        MyAlert("请输入正整数!");
    	        obj.value = "";
    	        return;
    	    }
        }
        //表单提交前的验证：
        function checkFormUpdate() {
            var trVale = parseFloat(document.getElementById("days").value);
          /*  var sixVale = parseFloat(document.getElementById("sixWeight").value);
            var twVale = parseFloat(document.getElementById("twelveWeight").value);
            var total = parseFloat(trVale + sixVale + twVale);
            if( 1 < total)
            {
                MyAlert("所占权重总和不能大于 1!");
                return false;
            }*/
            MyConfirm("确认保存设置?", checkForm);
        }
        //失效按钮
        function btnDisable(){

            $$('input[type="button"]').each(function(button) {
                button.disabled = true;
            });

        }
        //有效按钮
        function btnEnable(){
            $$('input[type="button"]').each(function(button) {
                button.disabled = "";
            });

        }
    </script>
</head>
<body>
<form name='fm' id='fm' method="post">
    <input type="hidden" name="deftId" id="deftId" value="${DEF_ID}" />
    <div class="wbox">
        <div class="navigation">
            <img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置：<c:if test="${TYPE==95601001}">基础信息管理 &gt; 销售相关信息维护 &gt; 切换件回运有效期维护</c:if><c:if test="${TYPE==95601002}">基础数据管理 &gt; 计划相关参数维护 &gt; 订单有效期维护</c:if>
        </div>
        <table class="table_edit">
            <th colspan="6"><img class="nav" src="<%=contextPath%>/img/subNav.gif" /> 设置信息</th>
            <tr>
                <c:if test="${TYPE==95601001}">
                <td width="10%"   align="right"> 切换件回运有效期(天)：</td></c:if>
                <c:if test="${TYPE==95601002}">
                <td width="10%"   align="right"> 订单有效期(天)：</td></c:if>
                <td width="20%">
                    <input class="middle_txt" id="days" name="days" type="text" value="${DAYS}" onchange="checkData(this)"/>天
                </td>
                <td width="10%"  align="left"> </td>
                <td width="20%">
                    <input type="button" name="saveBtn" id="saveBtn" value="保 存" onclick="checkFormUpdate();" class="normal_btn" />
                </td>
            </tr>
        </table>
    </div>
</form>
</body>
</html>
