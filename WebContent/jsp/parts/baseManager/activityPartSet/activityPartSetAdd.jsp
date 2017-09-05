<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" pageEncoding="UTF-8" %>
<%@ taglib uri="/jstl/cout" prefix="c" %>
<%@ page import="com.infodms.dms.common.Constant" %>
<%
    String contextPath = request.getContextPath();
%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <jsp:include page="${contextPath}/common/jsp_head_new.jsp"/>
    <title>活动配件明细设置</title>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <script language="javascript" type="text/javascript">
        function doInit() {
            var _this = document.getElementById("actPartType");
            hideImportBtn(_this);
            loadcalendar();  //初始化时间控件
        }
        //返回
        function goBack() {
            btnDisable();
            location = '<%=contextPath%>/parts/baseManager/activityPartSet/activityPartSetAction/activityPartSetInit.do';
        }

        //表单提交方法：
        function checkForm() {
            var obj = document.getElementsByName("partIds");
            var parts = [];
            var k = 0;
            for (var i = 0; i < obj.length; i++) {
                parts[k] = obj[i].value;
                k++;
            }
            btnDisable();
            var state = document.getElementById("state").value;
            var url = '';
            if ('<%=Constant.PART_ACTIVITY_TYPE_REPLACED_01 %>' == state) {
                var _this = document.getElementById("actPartType").value;
                if (_this == 95621002) {
                    url = '<%=contextPath%>/parts/baseManager/activityPartSet/activityPartSetAction/insertActivityReplacedPartSet.json?parts=' + parts + "&switch=95621002";
                } else {
                    url = "<%=contextPath%>/parts/baseManager/activityPartSet/activityPartSetAction/insertActivityReplacedPartSet.json?parts=" + parts;
                }

            } else {
                url = '<%=contextPath%>/parts/baseManager/activityPartSet/activityPartSetAction/insertActivityPartSet.json?parts=' + parts;
            }
            makeFormCall(url, showResult, 'fm');
        }
        function showResult(json) {
            btnEnable();
            if (json.errorExist != null && json.errorExist.length > 0) {
                MyAlert("配件：【" + json.errorExist + "】已添加，不能重复添加!");
            } else if (json.success != null && json.success == "true") {
                MyAlert("新增成功!");
                window.location.href = "<%=contextPath%>/parts/baseManager/activityPartSet/activityPartSetAction/activityPartSetInit.do";
            } else if (json.actCodeExist != null && json.actCodeExist.length > 0) {
                MyAlert(json.actCodeExist);
            }
            else {
                disableBtn($("saveBtn"));
                MyAlert("新增失败，请联系管理员!");
            }
        }
        //表单提交前的验证：
        function checkFormUpdate() {
            var describe = document.getElementById("describe").value;
            var startDate = document.getElementById("checkSDate").value;
            var endDate = document.getElementById("checkEDate").value;
            var actCode = document.getElementById("actCode").value;
            var state = document.getElementById("state").value;
            var dlrSelectVal = document.getElementById("dlrSelectVal");
            var dlrSelect = document.getElementById("dlrSelect");

            if (dlrSelect.checked) {
                dlrSelectVal.value = "1";
            } else {
                dlrSelectVal.value = "0";
            }
            if ("" == state || null == state) {
                MyAlert('请选择活动类型!');
                return false;
            }

            if ("" == actCode || null == actCode) {
                MyAlert('请填写活动编码!');
                return false;
            }

            if ("" == describe || null == describe) {
                MyAlert('请先填写活动描述信息!');
                return false;
            }

            if ("" == startDate || null == startDate) {
                MyAlert('请先设置活动开始日期!');
                return false;
            }

            if ("" == endDate || null == endDate) {
                MyAlert('请先设置活动结束日期!');
                return false;
            }

            var startDateFormat = new Date(startDate.replace("-", "/"));
            var endDateFormat = new Date(endDate.replace("-", "/"));

            if ((endDateFormat - startDateFormat) < 0) {
                MyAlert('活动结束日期要晚于开始日期!');
                return false;
            }

            var partIds = document.getElementsByName("partIds");
            if (null == partIds || partIds.length <= 0) {
                MyAlert('请至少选择一个新增的配件!');
                return false;
            } else {
                var PartIds = document.getElementsByName("partIds");
                if (null != PartIds && PartIds.length > 0) {
                    var idsSize = PartIds.length;
                    for (var i = 0; i < idsSize; i++) {
                        var isneedFlag = document.getElementsByName("isneedFlag_" + PartIds[i].value);
                        if (isneedFlag[0].checked == false && isneedFlag[1].checked == false) {
                            MyAlert('请选择是否需要回运!');
                            return false;
                        }
                    }
                }
            }
            MyConfirm("确认是否新增?", checkForm);
        }

        function showPart() {
            var state = document.getElementById("state").value;
            var url = '<%=contextPath%>/parts/baseManager/activityPartSet/activityPartSetAction/queryPartsForAddInit.do?state=' + state;
            OpenHtmlWindow(url, 700, 500);
        }

        function getModel(codes, ids, names) {
            var dlr_tab = $('dlr_tab');//服务商列表
            var dealerIds = document.getElementsByName("dealerIds");//服务商IDs
            var codes = codes.split(",");
            var ids = ids.split(",");
            var names = names.split(",");
            var dupCodes = "";

            var dealerId = new Array();
            if (null != dealerIds && dealerIds.length > 0) {
                for (var i = 0; i < dealerIds.length; i++) {
                    dealerId.push(dealerIds[i].value);
                }
            }

            if (null != ids && ids.length > 0 && ids[0] != "") {
                for (var i = 0; i < ids.length; i++) {
                    if (null != dealerId && dealerId.toString().indexOf(ids[i]) > -1) {
                        dupCodes += codes[i] + ",";
                    } else {
                        var idx = dlr_tab.rows.length;
                        var insert_row = dlr_tab.insertRow(idx);
                        if (idx % 2 == 0)
                            insert_row.className = 'table_list_row2';
                        else
                            insert_row.className = 'table_list_row1';
                        insert_row.insertCell(0);
                        insert_row.insertCell(1);
                        insert_row.insertCell(2);
                        insert_row.insertCell(3);

                        var cur_row = dlr_tab.rows[idx];
                        cur_row.cells[0].innerHTML = '<td style="text-align: center">' + (i + 1) + '</td>';
                        cur_row.cells[1].innerHTML = '<td style="text-align: center">' + codes[i] + '</td>';
                        cur_row.cells[2].innerHTML = '<td style="text-align: center">' + names[i] + '</td>';
                        cur_row.cells[3].innerHTML = '<td><input type="hidden" name="dealerIds" id="dealerId_' + ids[i] + '"  value=' + ids[i] + '>'
                                + '<input type="button" class="normal_btn" value="删 除" onclick="deleteTblRow(\'dlr_tab\',' + idx + ')"/>';
                    }
                }

            }
            if ("" != dupCodes) {
                MyAlert("服务商：【" + dupCodes.substr(0, dupCodes.length - 1) + "】不能被重复添加!");
            }

        }

        function setPartCode(partIdsNames) {
            var tab = $('add_tab');
            var prePartIds = document.getElementsByName("partIds");
            var rpPartNames = "";
            var partIdArr = new Array();
            if (null != prePartIds && prePartIds.length > 0) {
                var idsSize = prePartIds.length;
                for (var i = 0; i < idsSize; i++) {
                    partIdArr.push([prePartIds[i].value]);
                }
            }
            var strTemp;
            for (var i = 0; i < partIdsNames.length; i++) {
                strTemp = partIdsNames[i].toString();
                //定义一数组
                strsTemp = strTemp.split("@@"); //字符分割
                var partId = strsTemp[0];
                var partCode = strsTemp[1];
                var partOldcode = strsTemp[2];
                var partName = strsTemp[3];
                var repartOldcode = '';
                var repartName = '';
                if ('<%=Constant.PART_ACTIVITY_TYPE_REPLACED_01%>' == document.getElementById("state").value) {
                    repartOldcode = strsTemp[5];
                    repartName = strsTemp[4];
                }

                if (partIdArr.length > 0
                        && partIdArr.toString().indexOf(partId) > -1) {
                    rpPartNames = rpPartNames + partName + " ";
                } else {
                    var idx = tab.rows.length;
                    var insert_row = tab.insertRow(idx);
                    if (idx % 2 == 0)
                        insert_row.className = 'table_list_row2';
                    else
                        insert_row.className = 'table_list_row1';
                    insert_row.insertCell(0);
                    insert_row.insertCell(1);
                    insert_row.insertCell(2);
                    insert_row.insertCell(3);
                    if ('<%=Constant.PART_ACTIVITY_TYPE_REPLACED_01%>' == document.getElementById("state").value) {
                        insert_row.insertCell(4);
                        insert_row.insertCell(5);
                        insert_row.insertCell(6);
                        insert_row.insertCell(7);
                        insert_row.insertCell(8);
                        insert_row.insertCell(9);
                    } else {
                        insert_row.insertCell(4);
                        insert_row.insertCell(5);
                    }

                    var cur_row = tab.rows[idx];
                    cur_row.cells[0].innerHTML = idx;
                    cur_row.cells[1].innerHTML = partCode
                            + '<input type="hidden" name="partIds" value=' + partId + '><input type="hidden" name="partCode" value=' + partCode + '>';

                    cur_row.cells[2].innerHTML = partOldcode
                            + '<input type="hidden" name="partOldcode" value=' + partOldcode + '>';
                    cur_row.cells[3].align = "left";
                    cur_row.cells[3].innerHTML = partName
                            + '<input type="hidden"  name="partName" value=' + partName + '>';
                    if ('<%=Constant.PART_ACTIVITY_TYPE_REPLACED_01%>' == document.getElementById("state").value) {
                        cur_row.cells[4].innerHTML = repartOldcode + '<input type="hidden" name="repartOldcode" value=' + repartOldcode + '>';
                        cur_row.cells[5].align = "left";
                        cur_row.cells[5].innerHTML = repartName + '<input type="hidden"  name="repartName" value=' + repartName + '>';
                        cur_row.cells[6].innerHTML = '<input type="text" id=\"sugNum_'
                                + partId + '\" name=\"sugNum_' + partId
                                + '\" onchange="dataTypeCheck(this)" value="1" />';
                        cur_row.cells[7].innerHTML = '<input name="isneedFlag_' + partId + '" type="radio" checked value="10041001" />是' +
                                '<input name="isneedFlag_' + partId + '" type="radio" value="10041002" />否 ';
                        cur_row.cells[8].innerHTML = '<input id="isNormal_' + partId + '" name="isNormal_' + partId + '"  type="checkbox" onclick="myValue(this)" />';
                        cur_row.cells[9].innerHTML = '<input type="button" class="normal_btn" value="删 除" onclick="deleteTblRow(\'add_tab\',' + idx + ')"/>';
                    } else {
                        cur_row.cells[4].innerHTML = '<input type="text" id=\"sugNum_'
                                + partId + '\" name=\"sugNum_' + partId
                                + '\" onchange="dataTypeCheck(this)" value="1" />';
                        cur_row.cells[5].innerHTML = '<input type="button" class="normal_btn" value="删 除" onclick="deleteTblRow(\'add_tab\',' + idx + ')"/>';
                    }
                }
            }
            if ("" != rpPartNames) {
                MyAlert("配件：【" + rpPartNames + "】不能被重复添加!");
            }
        }

        function myValue(obj) {
            if (obj.value == '') {
                obj.value = '10041002';
            } else {
                obj.value = '10041001';
            }
        }
        //数据验证
        function dataTypeCheck(obj) {
            var value = obj.value;
            if (isNaN(value)) {
                MyAlert("请输入数字!");
                obj.value = "";
                return;
            }
            var re = /^[1-9]+[0-9]*]*$/;
            if (!re.test(obj.value)) {
                MyAlert("请输入正整数!");
                obj.value = "";
                return;
            }
        }

        function deleteTblRow(obj, rowNum) {
            var tbl = document.getElementById(obj);
            tbl.deleteRow(rowNum);
            var count = tbl.rows.length;
            for (var i = rowNum; i <= count; i++) {
                tbl.rows[i].cells[0].innerText = i;
                if (obj == 'add_tab') {
                    tbl.rows[i].cells[5].innerHTML = '<input type="button" class="normal_btn" value="删 除" onclick="deleteTblRow(\'' + obj + '\' ,' + i + ');" / > '
                } else {
                    tbl.rows[i].cells[3].innerHTML = '<input type="button" class="normal_btn" value="删 除" onclick="deleteTblRow(\'' + obj + '\' ,' + i + ');" / > '
                }
                if ((i + 1) % 2 == 0) {
                    tbl.rows[i].className = "table_list_row1";
                } else {
                    tbl.rows[i].className = "table_list_row2";
                }
            }
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

        function hideImportBtn(_this) {
            var uploadDiv = document.myIframe.uploadDiv;
            if (_this.value == 95621001) {
                $("saveBtn").show();
            } else {
                $("saveBtn").show();
            }

        }

        function HIDE_BAND_ACT_CODE(_this) {
            if (_this.value == 95621001) {
                $("band1").show();
                $("band2").show();
            } else {
                $("band1").hide();
                $("band2").hide();
            }
        }

        function showUpload() {
            var uploadDiv = document.myIframe.uploadDiv;
            if (uploadDiv.style.display == "block") {
                uploadDiv.style.display = "none";
            } else {
                uploadDiv.style.display = "block";
            }
        }
        function exportExcelTemplate() {
            fm.action = "<%=contextPath%>/parts/baseManager/activityPartSet/activityPartSetAction/exportExcelTemplate.do";
            fm.submit();
        }
        function deleteTabCell(_this) {
            if (<%=Constant.PART_ACTIVITY_TYPE_REPLACED_01 %> == parseInt(_this.value)
        )
            {
                $("repartCode").show();
                $("repartCname").show();
                $("isneedFlag").show();
                $("isNormal").show();
            }
        else
            {
                $("repartCode").hide();
                $("repartCname").hide();
                $("isneedFlag").hide();
                $("isNormal").hide();
            }
            var tb = document.getElementById('add_tab');
            var rowNum = tb.rows.length;
            for (i = 0; i < rowNum; i++) {
                if (i > 0) {
                    tb.deleteRow(i);
                    rowNum = rowNum - 1;
                    i = i - 1;
                }
            }
        }

        function checkdlrSelect() {
            var dlrSelect = document.getElementById("dlrSelect");
            var dlr_tab = document.getElementById("dlr_tab");

            if (dlrSelect.checked) {
                dlrSelect.checked = false;
                dlr_tab.style.display = "block";
            } else {
            }
        }

        function dlrSelectChecked() {
            var dlrSelect = document.getElementById("dlrSelect");
            var dlr_tab = document.getElementById("dlr_tab");
            if (dlrSelect.checked) {
                dlr_tab.style.display = "none";
            } else {
                dlr_tab.style.display = "block";
            }
        }
    </script>
</head>
<body>
<form name='fm' id='fm'>
    <div id="wbox" class="wbox">
        <div class="navigation">
            <img src="<%=contextPath%>/img/nav.gif"/>&nbsp;当前位置：基础数据管理 &gt;配件基础数据维护 &gt; 活动配件明细设置 &gt; 新增
        </div>
        <input type="hidden" id="dlrSelectVal" name="dlrSelectVal" value="1"/>
        <table class="table_edit">
            <tr>
                <td width="10%" align="right">活动编号：</td>
                <td width="30%">
                    <input class="long_txt" type="text"
                           name="actCode" id="actCode"/> <font color="red">*</font>
                </td>
                <td width="10%" align="right">活动描述：</td>
                <td width="30%">
                    <input class="long_txt" type="text"
                           name="describe" id="describe" value=""/> <font color="red">*</font>
                </td>
            </tr>
            <tr>
                <td width="10%" align="right">活动类型：</td>
                <td width="30%" align="left">
                    <select name="state" id="state" class="short_sel" onchange="deleteTabCell(this)">
                        <option value="">请选择</option>
                        <c:forEach items="${stateMap}" var="stateMap">
                            <option value="${stateMap.key}">${stateMap.value}</option>
                        </c:forEach>
                    </select><font color="red">*</font></td>
                <td width="10%" align="right">活动日期：</td>
                <td width="30%">
                    <input id="checkSDate" class="short_txt"
                           name="checkSDate" datatype="1,is_date,10" maxlength="10"
                           group="checkSDate,checkEDate" readonly="readonly"/>
                    <input class="time_ico"
                           onclick="showcalendar(event, 'checkSDate', false);" value=" "
                           type="button"/> 至&nbsp;
                    <input id="checkEDate" class="short_txt"
                           name="checkEDate" datatype="1,is_date,10" maxlength="10"
                           group="checkSDate,checkEDate" readonly="readonly"/>
                    <input class="time_ico"
                           onclick="showcalendar(event, 'checkEDate', false);" value=" "
                           type="button"/> <font color="red">*</font> &nbsp;</td>
            </tr>
            <tr style="display:none">
                <td width="10%" align="right">活动配件类型：</td>
                <td width="30%" align="left">
                    <select name="actPartType" id="actPartType"
                            class="short_sel" onchange="HIDE_BAND_ACT_CODE(this)">
                        <c:forEach items="${stateMapForPart}" var="stateMapForPart">
                            <option value="${stateMapForPart.key}">${stateMapForPart.value}</option>
                        </c:forEach>
                    </select></td>
                <td id=band1 width="10%" align="right" style="display:none">绑定库存配件活动编码：</td>
                <td id=band2 width="30%" align="left" style="display:none">
                    <select name="BAND_ACT_CODE" id="BAND_ACT_CODE"
                            class="short_sel">
                        <option value="">请选择</option>
                        <c:forEach items="${stateMaps}" var="stateMaps">
                            <option value="${stateMaps.ACTIVITY_CODE}">${stateMaps.ACTIVITY_CODE}</option>
                        </c:forEach>
                    </select>
                </td>
            </tr>
            <tr>
                <td width="10%" align="right">活动范围：</td>
                <td width="30%" align="left">
                    所有服务商<input type="checkbox" name="dlrSelect" id="dlrSelect" checked onclick="dlrSelectChecked();"/>
                    <input type="hidden" class="middle_txt" name="dealerName" size="15" value="" id="dealerName"/>
                    <input type="hidden" class="middle_txt" name="dealerCode" size="15" value=""
                           id="dealerCode"/>
                    <input type="button" name="saveBtn" id="saveBtn" value="指定服务商"
                           onclick="checkdlrSelect();showOrgDealer('dealerCode','','true','','','','<%=Constant.DEALER_TYPE_DWR%>','dealerName');"
                           class="long_btn"/>
                </td>
            </tr>
            <tr>
                <td align="center" colspan="4">
                    <input type="button"
                           name="saveBtn" id="saveBtn" value="保 存"
                           onclick="checkFormUpdate()" class="normal_btn"/>
                    <%--  <input type="button"
                             name="importBtn" id="importBtn" value="导 入"
                             onclick="showUpload();" class="normal_btn"/>--%>
                    <input
                            type="button" name="backBtn" id="backBtn" value="返 回"
                            onclick="goBack()" class="normal_btn"/>

                </td>
            </tr>
        </table>
        <table style="display: none;" class="table_list" id="dlr_tab" >
            <tr class="table_list_row1" style="background-color: #DAE0EE;">
                <td>序号</td>
                <td>服务商代码</td>
                <td>服务商名称</td>
                <td>操作</td>
            </tr>
        </table>

        <table class="table_list" id="add_tab">
            <tr class="table_list_row1" style="background-color: #DAE0EE;">
                <td>序号</td>
                <td>件号</td>
                <td>配件编码</td>
                <td>配件名称</td>
                <td id="repartCode" style="display:none">切换件编码</td>
                <td id="repartCname" style="display:none">切换件名称</td>
                <td>建议数量</td>
                <td id="isneedFlag" style="display:none">是否需要回运</td>
                <td id="isNormal" style="display:none">是否可用</td>
                <td><input type="button" value="选择配件" class="normal_btn" onclick="showPart();"/></td>
            </tr>
        </table>
        <iframe frameborder="0" name="myIframe" id="myIframe"
                src="<%=request.getContextPath()%>/jsp/parts/salesManager/carFactorySalesManager/VINUploadFile.jsp"
                height="100%" width="100%" scrolling="auto" align="middle">
        </iframe>
    </div>
</form>
</body>
</html>
