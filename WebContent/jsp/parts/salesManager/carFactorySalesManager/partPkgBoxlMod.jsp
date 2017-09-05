<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c" %>
<%
    String contextPath = request.getContextPath();
%>
<head>
<title>装箱信息修改</title>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp"/>
<script language="javascript">
    var temp = "";
    function disabledAll() {
        jQuery("input[type=button]").attr('disabled', 'true');
        jQuery("a").attr('disabled', 'true');
    }
    function enableAll() {
        jQuery("input[type=button]").attr('disabled', 'false');
        jQuery("a").attr('disabled', 'false');
    }
    enableAll();

    function queryBoxInfo(obj) {
        jQuery(".dtl_pkgno").text(obj.value);
        var url = "<%=contextPath%>/parts/salesManager/carFactorySalesManager/PartPkg/queryBoxInfo.json?pkgNo=" + obj.value;
        makeNomalFormCall(url, getBoxResult, 'fm');
    }

    function getBoxResult(jsonObj) {
        if (jsonObj) {
            if (jsonObj.boxLen) {
                $("#flag")[0].value = "";
                $("#BOX_LEN")[0].value = jsonObj.boxLen;
                $("#BOX_WID")[0].value = jsonObj.boxWid;
                $("#BOX_HEI")[0].value = jsonObj.boxHei;
                $("#VOLUME")[0].value = jsonObj.boxVol;
                $("#BOX_WEI")[0].value = jsonObj.boxWei;
                $("#CH_WEIGHT")[0].value = jsonObj.boxCHWei;
                $("#EQ_WEIGHT")[0].value = jsonObj.boxEQWei;
            } else{
                $("#flag")[0].value = "0";
                $("#BOX_LEN")[0].value = "";
                $("#BOX_WID")[0].value = "";
                $("#BOX_HEI")[0].value = "";
                $("#VOLUME")[0].value = "";
                $("#BOX_WEI")[0].value = "";
                $("#CH_WEIGHT")[0].value = "";
                $("#EQ_WEIGHT")[0].value = "";
                $("#PICK_ORDER_ID")[0].value = "";
            }
        }
    }


    function checkSize(obj) {
        var patrn = /^((0\.[0-9]*[1-9][0-9]*)|([1-9][0-9]*\.[0-9]+)|([1-9][0-9]*))$/;
        if (!patrn.exec(obj.value)) {
            MyAlert("包装尺寸无效,请重新输入!");
            obj.value = "";
			$("#VOLUME")[0].value = "";
            $("#CH_WEIGHT")[0].value = "";
            $("#EQ_WEIGHT")[0].value = "";
            return;
        } else {
            if (obj.value.indexOf(".") >= 0) {
                var patrn = /^[0-9]{0,8}.[0-9]{0,2}$/;
                if (!patrn.exec(obj.value)) {
                    MyAlert("包装尺寸整数部分不能超过8位,且保留精度最大为2位!");
                    obj.value = "";						
                    $("#VOLUME")[0].value = "";
                    $("#CH_WEIGHT")[0].value = "";
                    $("#EQ_WEIGHT")[0].value = "";
                    return;
                }
            } else {
                var patrn = /^[0-9]{0,8}$/;
                if (!patrn.exec(obj.value)) {
                    MyAlert("包装尺寸整数部分不能超过8位!");
                    obj.value = "";						
                    $("#VOLUME")[0].value = "";
                    $("#CH_WEIGHT")[0].value = "";
                    $("#EQ_WEIGHT")[0].value = "";
                    return;
                }
            }
        }

		var box_len = $("#BOX_LEN")[0].value;
        var box_wid = $("#BOX_WID")[0].value;
        var box_hei = $("#BOX_HEI")[0].value;
        //体积（VOLUME） = 长*宽*高/1000000
        //折合重量（EQ_WEIGHT） = 长*宽*高/6000(跟承运商有关系，默认是6000)
        //单箱重量(BOX_WEI)
        //计费重量（CH_WEIGHT） = 跟箱子长、宽、高有关，三边斗大于60CM 取折合重量和重量取最大值。三边有任一边小于等于60CM，取单箱重量。
        if (box_len && box_wid && box_hei) {
            $("#VOLUME")[0].value = ((box_len * box_wid * box_hei) / (100 * 100 * 100)).toFixed(2);//体积
            var eqWeght = ((box_len * box_wid * box_hei) / 6000).toFixed(2);//折合重量
            $("#EQ_WEIGHT")[0].value = eqWeght;
            calWeight(box_len, box_wid, box_hei, $("#BOX_WEI")[0].value, eqWeght);
        }

    }
    /**
     * 计费重量（CH_WEIGHT） = 跟箱子长、宽、高有关，三边有一边大于60CM 取折合重量和重量取最大值。三边有都小于60CM，取单箱重量。
     * @param c 长
     * @param k 宽
     * @param g 高
     * @param dxzl  单箱重量
     * @param zhzl  折合重量
     */
    function calWeight(c, k, g, dxzl, zhzl) {
        //MyAlert(c + ":" + k + "：" + g + ":" + dxzl + ":" + zhzl);
        if (c >= 60 || k >= 60 || g > 60) {
            if (parseFloat(dxzl) >= parseFloat(zhzl)) {
                $("#CH_WEIGHT")[0].value = dxzl;
            } else {
                $("#CH_WEIGHT")[0].value = zhzl;
            }
        }
        if (c < 60 && k < 60 && g < 60) {
            $("#CH_WEIGHT")[0].value = dxzl;
        }
    }
    function accMul(arg1, arg2) {
        var m = 0, s1 = arg1.toString(), s2 = arg2.toString();
        try {
            m += s1.split(".")[1].length
        } catch (e) {
        }
        try {
            m += s2.split(".")[1].length
        } catch (e) {
        }
        return Number(s1.replace(".", "")) * Number(s2.replace(".", "")) / Math.pow(10, m)
    }

    function accDiv(arg1, arg2) {
        var t1 = 0, t2 = 0, r1, r2;
        try {
            t1 = arg1.toString().split(".")[1].length
        } catch (e) {
        }
        try {
            t2 = arg2.toString().split(".")[1].length
        } catch (e) {
        }
        with (Math) {
            r1 = Number(arg1.toString().replace(".", ""));
            r2 = Number(arg2.toString().replace(".", ""));
            return (r1 / r2) * pow(10, t2 - t1);
        }
    }
    function checkWeight(obj) {
        var patrn = /^((0\.[0-9]*[1-9][0-9]*)|([1-9][0-9]*\.[0-9]+)|([1-9][0-9]*))$/;
        if (!patrn.exec(obj.value)) {
            MyAlert("单箱重量无效,请重新输入!");
            obj.value = "";
            $("#EQ_WEIGHT")[0].value = "";
            return;
        } else {
            if (obj.value.indexOf(".") >= 0) {
                var patrn = /^[0-9]{0,8}.[0-9]{0,2}$/;
                if (!patrn.exec(obj.value)) {
                    MyAlert("单箱重量整数部分不能超过8位,且保留精度最大为2位!");
                    obj.value = "";
                    $("#EQ_WEIGHT")[0].value = "";
                    return;
                }
            } else {
                var patrn = /^[0-9]{0,8}$/;
                if (!patrn.exec(obj.value)) {
                    MyAlert("单箱重量整数部分不能超过8位!");
                    obj.value = "";
                    $("#EQ_WEIGHT")[0].value = "";
                    return;
                }
            }
        }

		var box_len = $("#BOX_LEN")[0].value;
        var box_wid = $("#BOX_WID")[0].value;
        var box_hei = $("#BOX_HEI")[0].value;
        //体积（VOLUME） = 长*宽*高/1000000
        //折合重量（EQ_WEIGHT） = 长*宽*高/6000(跟承运商有关系，默认是6000)
        //单箱重量(BOX_WEI)
        //计费重量（CH_WEIGHT） = 跟箱子长、宽、高有关，三边斗大于60CM 取折合重量和重量取最大值。三边有任一边小于等于60CM，取单箱重量。
        if (box_len && box_wid && box_hei) {
            $("#VOLUME")[0].value = ((box_len * box_wid * box_hei) / (100 * 100 * 100)).toFixed(2);//体积
            var eqWeght = ((box_len * box_wid * box_hei) / 6000).toFixed(2);//折合重量
            $("#EQ_WEIGHT")[0].value = eqWeght;
            calWeight(box_len, box_wid, box_hei, $("#BOX_WEI")[0].value, eqWeght);
        }
    }

    /* function selAll2(obj) {
        var cb = document.getElementsByName('cb');
        for (var i = 0; i < cb.length; i++) {
            if (obj.checked) {
                cb[i].checked = true;
            } else {
                cb[i].checked = false;
            }
        }
    }

    function selAll(obj) {
        var cb = document.getElementsByName('cb');
        var flag = true;
        for (var i = 0; i < cb.length; i++) {
            if (!cb[i].checked) {
                flag = false;
            }
        }
        $("allChk").checked = flag;
    } */

    <%-- function modDtl() {
        var partIds = document.getElementsByName("cb");
        var cnt = 0;
        for (var i = 0; i < partIds.length; i++) {
            if (partIds[i].checked) {
                cnt++;
                var pattern1 = /^[1-9][0-9]*$/;
                var salQty = document.getElementById("salQty_" + partIds[i].value).value;
                var pkgQty = document.getElementById("pkgQty_" + partIds[i].value).value;
                if (!pattern1.exec(pkgQty) && pkgQty != 0) {
                    MyAlert("第" + (i + 1) + "行，装箱数量不能为空且只能输入非负整数!");
                    return;
                }

                if (parseInt(pkgQty) > parseInt(salQty)) {
                    MyAlert("第" + (i + 1) + "行，装箱数量不能大于销售数量!");
                    return;
                }
            }
        }
        if (cnt == 0) {
            MyAlert("请选择要修改的配件！");
            return;
        }
        MyConfirm("确定修改明细?",confirmResult);
    }
    
    function confirmResult(){
    	 btnDisable();
         var url = "<%=contextPath%>/parts/salesManager/carFactorySalesManager/PartPkgResult/saveModify.json?";
         makeNomalFormCall(url, ajaxResult, 'fm');
    } --%>
    function modPkg() {
        if (!$("#PKG_NO")[0].value) {
            MyAlert("请输入装箱单号!");
            return;
        }

        if (!$("#BOX_LEN")[0].value) {
            MyAlert("请输入包装尺寸的长!");
            return;
        }

        if (!$("#BOX_WID")[0].value) {
            MyAlert("请输入包装尺寸的宽!");
            return;
        }

        if (!$("#BOX_HEI")[0].value) {
            MyAlert("请输入包装尺寸的高!");
            return;
        }

        if (!$("#BOX_WEI")[0].value) {
            MyAlert("请输入单箱重量!");
            return;
        }
        MyConfirm("确定修改?",updateIt);
    }
    
    function updateIt() {
    	btnDisable();
    	var pkgNo1=$("#PKG_NO")[0].value;
	    var url = "<%=contextPath%>/parts/salesManager/carFactorySalesManager/PartPkgResult/modPkg.json?newPkgNo="+pkgNo1;
	    makeNomalFormCall(url, ajaxResult, 'fm');
    }

    function ajaxResult(jsonObj) { 
        if (jsonObj != null) {
            var success = jsonObj.success;
            var error = jsonObj.error;
            var exceptions = jsonObj.Exception;
            if (success) {
                MyAlert(success);
                var pickOrderId = $("#pickOrderId")[0].value;
                window.location.href = "<%=contextPath%>/parts/salesManager/carFactorySalesManager/PartPkgResult/modifyOrderPage.do?pickOrderId=" + pickOrderId;
            } else if (error) {
                MyAlert(error);
            } else if (exceptions) {
                MyAlert(exceptions.message);
            }
        }
    }

    function goBack1() {
        var pickOrderId = $("#pickOrderId")[0].value;
        window.location.href = "<%=contextPath%>/parts/salesManager/carFactorySalesManager/PartPkgResult/modifyOrderPage.do?pickOrderId=" + pickOrderId;
    }

    function chgPkgNo(obj){
        var url = "<%=contextPath%>/parts/salesManager/carFactorySalesManager/PartPkg/queryBoxInfo.json?pkgNo=" + obj.value;
        makeNomalFormCall(url, xxx, 'fm');
    }

    function xxx(jsonObj) {
        if (jsonObj) {
            if (jsonObj.isOpen == "1") {
                if (!jsonObj.flag) {
                    MyAlert("箱号：<font style='color: red'>" + $("#PKG_NO")[0].value + '</font>不属于当前服务商!');
                    clrPkg("1");
                    return;
                }
            }
          /* if (jsonObj.pkgNo) {
                if (jsonObj.outId) {
                    MyAlert("箱号：<font style='color: red'>" + $("PKG_NO").value + '</font>已经出库不能重复使用!');
                    clrPkg("1");
                    return;
                } else {
                    if (jsonObj.pickOrderId !=${mainMap.PICK_ORDER_ID}) {
                        MyAlert("输入的箱号：<font style='color: red'>" + $("PKG_NO").value + "</font>属于另一个拣货单<font style='color: red'>" + jsonObj.pickOrderId + '</font>!');
                    } else {
                        MyAlert("输入的箱号：<font style='color: red'>" + $("PKG_NO").value + "</font>在本装箱单中<font style='color: red'>" + jsonObj.pickOrderId + '</font>已使用!');
                    }
                }
            } else {
                clrPkg("2");

            }*/
        }
    }
    //
    function clrPkg(flag) {
        //1:已出库清理箱号。2：未出库不清理箱号
        if (flag == "1") {
            $("#PKG_NO")[0].value = "";
        }
    }
   <%-- function viewDlrPkgNoDtlxx() {
        OpenHtmlWindow('<%=contextPath%>/parts/salesManager/carFactorySalesManager/PartPkgRePrint/dlrPKginit.do?dealerId=' +${dealerId}, 800, 500);
    } --%>
</script>
</head>
<body>
<form name="fm" id="fm" method="post" enctype="multipart/form-data">
    <input name="pickOrderId" id="pickOrderId" value="${pickOrderId}" type="hidden"/>
    <input name="pkgNo" id="pkgNo" value="${pkgNo}" type="hidden"/>
    <input name="dealerId" id="dealerId" value="${dealerId}" type="hidden"/>
    <input name="boxId" id="boxId" value="${boxId}" type="hidden"/>
    <input name="flag" id="flag" type="hidden"/>

    <div class="wbox">
        <div class="navigation"><img src="<%=contextPath %>/img/nav.gif"/>&nbsp;当前位置: 配件管理 > 配件销售管理 > 装箱结果修改> 装箱信息修改</div>
		<div class="form-panel">
		     <h2><img class="panel-icon nav" src="<%=contextPath%>/img/subNav.gif"/>装箱信息修改</h2>
		     <div class="form-body">
	        <table class="table_query">
	            <tr>
	                <td class="right">装箱号：</td>
	                <td width="25%">
	                    <input  type="text" id="PKG_NO" name="PKG_NO" class="middle_txt"
	                           value="${map.PKG_NO}" onchange="chgPkgNo(this);"/>
	                    <%--<input type="button" class="mini_btn" value="View" onclick="viewDlrPkgNoDtlxx();"/>--%>
	                </td>
	                <td class="right">包装尺寸：</td>
	                <td width="25%">
	                    长<input style="width: 50px;height: 15px;border:1px solid #a6b2c8" type="text" id="BOX_LEN" name="BOX_LEN"
	                            value="${map.LENGTH }"
	                            onblur="checkSize(this)"/>
	                    宽<input style="width: 50px;height: 15px;border:1px solid #a6b2c8" type="text" id="BOX_WID" name="BOX_WID"
	                            value="${map.WIDTH }"
	                            onblur="checkSize(this)"/>
	                    高<input style="width: 50px;height: 15px;border:1px solid #a6b2c8" type="text" id="BOX_HEI" name="BOX_HEI"
	                            value="${map.HEIGHT }"
	                            onblur="checkSize(this)"/>CM
	                </td>
	                <td class="right">体积：</td>
	                <td width="24%">
	                    <input type="text" style="border:0px;background-color:#F3F4F8;" id="VOLUME" name="VOLUME"
	                           value="${map.VOLUME }"/>M<sup>3</sup>
	                </td>
	            </tr>
	            <tr>
	                <td class="right">单箱重量：</td>
	                <td width="25%">
	                    <input class="middle_txt" type="text" id="BOX_WEI" name="BOX_WEI" onblur="checkWeight(this)"
	                           value="${map.WEIGHT }"/>KG
	                </td>
	                <td class="right">折合重量：</td>
	                <td width="24%">
	                    <input type="text" style="border:0px;background-color:#F3F4F8;" id="EQ_WEIGHT" name="EQ_WEIGHT"
	                           value="${map.EQ_WEIGHT }"/>KG
	                </td>
	                <td class="right">计费重量：</td>
	                <td width="25%">
	                    <input type="text" style="border:0px;background-color:#F3F4F8;" id="CH_WEIGHT" name="CH_WEIGHT"
	                           value="${map.CH_WEIGHT }"/>KG
	                </td>
	            </tr>
	            <tr align="center">
	                <td colspan="6" class="center">
	                    <input class="normal_btn" type="button" value="修 改" id="save" name="save" onclick="modPkg();"/>
	                    <input class="normal_btn" type="button" value="返 回" onclick="goBack1();"/>
	                </td>
	            </tr>
	        </table>
	        </div>
	        </div>
    </div>
</form>
</body>
</html>