<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" pageEncoding="UTF-8" %>
<%@taglib uri="/jstl/cout" prefix="c" %>
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
            var status = document.getElementById("status").value;
            if (status == '已关闭') {
                document.getElementById("_addBtn").disabled = true;
                document.getElementById("_saveBtn").disabled = true;
            }
            var value = document.getElementById("actpartType");
            HIDE_BAND_ACT_CODE(value);
            loadcalendar();  //初始化时间控件
            __extQuery__(1);
        }
    </script>
</head>
<body>
<form name='fm' id='fm'>
    <input type="hidden" name="desId" id="desId" value="${desId}"/>
    <input type="hidden" name="prvDescribe" id="prvDescribe" value="${describe}"/>
    <input type="hidden" name="status" id="status" value="${status}"/>
    <input type="hidden" id="dlrSelectVal" name="dlrSelectVal" value="${allDlr}"/>

    <div class="wbox">
        <div class="navigation"><img src="<%=contextPath%>/img/nav.gif"/>&nbsp;当前位置：基础信息管理 &gt; 配件基础信息维护 &gt; 活动配件明细设置
            &gt; 维护
        </div>
        <table class="table_query">
            <th colspan="6"><img class="nav" src="<%=request.getContextPath()%>/img/subNav.gif"/> 设置条件
            </th>
            <tr>
                <td width="10%" align="right">活动编号：</td>
                <td width="15%">
                    <input class="long_txt" type="text"
                           name="actCode" id="actCode" value="${actCode}"/> <font
                        color="red">*</font></td>
                <td width="10%" align="right">活动描述：</td>
                <td width="20%">
                    <input class="long_txt" type="text"
                           name="describe" id="describe" value="${describe}"/>
                    <font color="red">*</font></td>
            </tr>
            <td width="10%" align="right">活动类型：</td>
            <td width="20%" align="left">
                <script type="text/javascript">
                    genSelBoxExp("state", <%=Constant.PART_ACTIVITY_TYPE%>, '${actTpye}', false, "short_sel", "", "false", '');
                </script>
            </td>
            <td width="10%" align="right">活动日期：</td>
            <td width="22%">
                <input id="checkSDate" class="short_txt"
                       name="checkSDate" datatype="1,is_date,10" maxlength="10"
                       group="checkSDate,checkEDate" value="${startDate}"
                       readonly="readonly"/>
                <input class="time_ico"
                       onclick="showcalendar(event, 'checkSDate', false);"
                       value=" "
                       type="button"/> 至&nbsp;
                <input id="checkEDate"
                       class="short_txt"
                       name="checkEDate"
                       datatype="1,is_date,10"
                       maxlength="10"
                       group="checkSDate,checkEDate"
                       value="${endDate}"
                       readonly="readonly"/>
                <input class="time_ico"
                       onclick="showcalendar(event, 'checkEDate', false);" value=" "
                       type="button"/> <font color="red">*</font></td>
            </tr>
            <tr>
                <td width="10%" align="right">活动配件类型：</td>
                <td width="20%" align="left">
                    <script type="text/javascript">
                        genSelBoxExp("actpartType", <%=Constant.ACTIVITY_PART_TYPE%>, '${partType}', false, "short_sel", "onchange='HIDE_BAND_ACT_CODE(this)'", "false", '');
                    </script>
                </td>
                <td id=band1 width="10%" align="right" style="display:none">新件回运绑&nbsp;&nbsp;&nbsp;</p>定活动编码：</td>
                <td id=band2 width="20%" align="left" style="display:none">
                    <select name="BAND_ACT_CODE" id="BAND_ACT_CODE"
                            class="short_sel">
                        <c:choose>
                            <c:when test="${bandAct ne ''}">
                                <c:forEach items="${stateMaps}" var="stateMaps" varStatus="_varStatus">
                                    <c:if test="${bandAct eq stateMaps.ACTIVITY_CODE}">
                                        <option value="${stateMaps.ACTIVITY_CODE}"
                                                selected>${stateMaps.ACTIVITY_CODE}</option>
                                    </c:if>
                                    <c:if test="${bandAct ne stateMaps.ACTIVITY_CODE}">
                                        <option value="${stateMaps.ACTIVITY_CODE}">${stateMaps.ACTIVITY_CODE}</option>
                                    </c:if>
                                </c:forEach>
                            </c:when>
                            <c:otherwise>
                                <option value="">--请选择--</option>
                                <c:forEach items="${stateMaps}" var="stateMaps">
                                    <option value="${stateMaps.ACTIVITY_CODE}">${stateMaps.ACTIVITY_CODE}</option>
                                </c:forEach>
                            </c:otherwise>
                        </c:choose>
                    </select>
                </td>
            </tr>
            <tr>
                <td width="10%" align="right">活动范围：</td>
                <td width="30%" align="left">
                    所有服务商<input type="checkbox" name="dlrSelect" id="dlrSelect" onclick="dlrSelectChecked();"/>
                    <script type="text/javascript">
                        if ('${allDlr}' == '1') {
                            document.getElementById("dlrSelect").checked = true;
                        }
                    </script>
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
                    <input type="button" name="addBtn" id="_addBtn"
                           value="新增配件" onclick="addNew()" class="normal_btn"/>
                    <input type="button" name="saveBtn" id="_saveBtn"
                           value="保 存" onclick="saveData()" class="normal_btn"/>
                    <input type="button" name="backBtn" id="backBtn"
                           value="返 回" onclick="goBack()" class="normal_btn"/>
                </td>
            </tr>
            <tr>
                <td colspan="4" style="text-align: center;color: red">
                    提示：新件回运绑定活动编码是指装车配件切换时服务商未使用完的配件随指定的库存切换活动一起回运。
                </td>
            </tr>
        </table>

    </div>
    <!-- 查询条件 end -->
    <!--分页 begin -->
    <jsp:include page="${contextPath}/queryPage/orderHidden.html"/>
    <jsp:include page="${contextPath}/queryPage/pageDiv.html"/>

    <table style="display: block;" class="table_list" class="tab_list" id="dlr_tab">
        <th colspan="4" align="left"><img class="nav" src="<%=request.getContextPath()%>/img/subNav.gif"/>参与服务商
        </th>
        <tr class="table_list_row1" style="background-color: #DAE0EE;">
            <td>序号</td>
            <td>服务商代码</td>
            <td>服务商名称</td>
            <td>操作</td>
        </tr>
        <c:forEach items="${aList}" var="list" varStatus="status">
            <c:if test="${status.count%2 == 0}">
                <tr class="table_list_row2" style="BACKGROUND-COLOR: #f7f7f7">
            </c:if>
            <c:if test="${status.count%2 != 0}">
                <tr class="table_list_row1">
            </c:if>
            <td>${status.count}</td>
            <td>${list.DEALER_CODE}</td>
            <td align="left">${list.DEALER_NAME}</td>
            <td><input type="hidden" name="dealerIds" id="dealerId_'${list.DEALER_ID}'"
                       value='${list.DEALER_ID} '>
                <input type="button" class="normal_btn" value="删 除"
                       onclick="deleteTblRow('dlr_tab', '${status.count}')"/>
            </td>
            </tr>
        </c:forEach>
    </table>
    <!--分页 end -->
</form>
<script type="text/javascript">
    var myPage;

    var url = "<%=contextPath%>/parts/baseManager/activityPartSet/activityPartSetAction/queryActivityPartSetDtl.json";

    var title = null;
    var status = document.getElementById("status").value;
    if (status == '已关闭') {
        var columns = [
            {header: "序号", dataIndex: 'DEFT_ID', renderer: getIndex, align: 'center'},
            {header: "件号", dataIndex: 'PART_CODE', style: 'text-align:center'},
            {header: "配件编码", dataIndex: 'PART_OLDCODE', renderer: inputText, style: 'text-align:center'},
            {header: "配件名称", dataIndex: 'PART_NAME', style: 'text-align:left'},
            {header: "切换件编码", dataIndex: 'REPART_OLDCODE', style: 'text-align:left'},
            {header: "切换件名称", dataIndex: 'REPART_NAME', style: 'text-align:left'},
            {header: "建议数量", dataIndex: 'SPEC_QTY', renderer: sugNumberText},
            {header: "是否需要回运", renderer: getRadio},
            {header: "是否可用", renderer: getCheckBox}
        ];

    } else {
        var columns = [
            {header: "序号", dataIndex: 'DEFT_ID', renderer: getIndex, align: 'center'},
            {header: "件号", dataIndex: 'PART_CODE', style: 'text-align:center'},
            {header: "配件编码", dataIndex: 'PART_OLDCODE', renderer: inputText, style: 'text-align:center'},
            {header: "配件名称", dataIndex: 'PART_NAME', style: 'text-align:left'},
            {header: "切换件编码", dataIndex: 'REPART_OLDCODE', style: 'text-align:left'},
            {header: "切换件名称", dataIndex: 'REPART_NAME', style: 'text-align:left'},
            {header: "建议数量", dataIndex: 'SPEC_QTY', renderer: sugNumberText},
            {header: "是否需要回运", renderer: getRadio},
            {header: "是否可用", renderer: getCheckBox},
            {id: 'action', header: "操作", sortable: false, dataIndex: 'ID', renderer: myLink, align: 'center'}
        ];
    }


    //设置超链接
    function myLink(value, meta, record) {
        var defineId = record.data.DEF_ID;
        return String.format("<a href=\"#\" onclick='delData(\"" + defineId + "\")'>[删掉]</a>");
    }

    //格式化时间为YYYY-MM-DD
    function formatDate(value, meta, record) {
        if (value == "" || value == null) {
            return "";
        } else {
            return value.substr(0, 10);
        }
    }

    function inputText(value, meta, record) {
        var activityType = record.data.ACTIVITY_TYPE;
        var actCode = record.data.ACTIVITY_CODE;
        var partType = record.data.PART_TYPE;
        var isneedFlag = record.data.ISNEED_FLAG;
        var partId = record.data.PART_ID;
        return value + "<input type='hidden' id='activityType' name='activityType' value='" + activityType + "' />" +
                "<input type='hidden' id='actCode' name='actCode' value='" + actCode + "' />" +
                "<input type='hidden' id='isneedFlag' name='isneedFlag' value='" + isneedFlag + "' />" +
                "<input type='hidden' id='partId' name='partId' value='" + partId + "' />" +
                "<input type='hidden' id='partType' name='partType' value='" + partType + "' />";
    }

    //返回 text
    function sugNumberText(value, meta, record) {
        var defineId = record.data.DEF_ID;
        var str = "<input type='hidden' name='defineIds' value=\"" + defineId + "\" /><input type='text' id='sugNum_" + defineId + "' name='sugNum_" + defineId + "' onchange='dataTypeCheck(this)' value=\"" + value + "\" />"
        return String.format(str);

    }
    //返回 text
    function getRadio(value, meta, record) {
        var isneedFlag = record.data.ISNEED_FLAG;
        var partId = record.data.PART_ID;
        var defineId = record.data.DEF_ID;
        var str;
        if (isneedFlag == 10041001) {
            str = "<input name='isneedFlag_" + defineId + "' type='radio' value='10041001' checked = 'checked'/>是" + "<input name='isneedFlag_" + defineId + "' type='radio' value='10041002' />否";
        } else {
            str = "<input name='isneedFlag_" + defineId + "' type='radio' value='10041001' />是" + "<input name='isneedFlag_" + defineId + "' type='radio' value='10041002' checked = 'checked'/>否";
        }
        return String.format(str);

    }
    function getCheckBox(value, meta, record) {
        var isneedFlag = record.data.IS_NORMAL;
        var partId = record.data.PART_ID;
        var defineId = record.data.DEF_ID;
        var str;
        if (isneedFlag == 10041001) {
            str = "<input name='isNormal_" + defineId + "' type='checkbox' value='10041001' checked />";
        } else {
            str = "<input name='isNormal_" + defineId + "' type='checkbox' value='10041001' />";
        }
        return String.format(str);

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

    //保存设置
    function saveData() {
        var describe = document.getElementById("describe").value;
        var startDate = document.getElementById("checkSDate").value;
        var endDate = document.getElementById("checkEDate").value;
        var actCode = document.getElementById("actCode").value;
        var dlrSelectVal = document.getElementById("dlrSelectVal");
        var dlrSelect = document.getElementById("dlrSelect");

        if (dlrSelect.checked) {
            dlrSelectVal.value = "1";
        } else {
            dlrSelectVal.value = "0";
        }
        if ("" == describe || null == describe) {
            MyAlert('请先填写活动描述信息!');
            return false;
        }

        if ("" == actCode || null == actCode) {
            MyAlert('请先填写活动编码!');
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

        var defIdsArr = document.getElementsByName("defineIds");
        if (null == defIdsArr || 0 > defIdsArr.length) {
            MyAlert('请至少选中一个活动配件!');
            return false;
        }

        if (confirm("确定保存设置?")) {
            btnDisable();
            var url = '<%=contextPath%>/parts/baseManager/activityPartSet/activityPartSetAction/updateActivityPartSet.json?curPage=' + myPage.page;
            makeFormCall(url, showResult, 'fm');
        }
    }

    //删除数据
    function delData(defineId) {
        if (confirm("确定删除该数据?")) {
            btnDisable();
            var url = '<%=contextPath%>/parts/baseManager/activityPartSet/activityPartSetAction/delActivityPartSet.json?defineId=' + defineId + '&curPage=' + myPage.page;
            makeFormCall(url, showResult, 'fm');
        }
    }

    function showResult(json) {
        btnEnable();
        if (json.errorExist != null && json.errorExist.length > 0) {
            MyAlert(json.errorExist);
        } else if (json.success != null && json.success == "true") {
            MyAlert("操作成功!");
            document.getElementById("prvDescribe").value = document.getElementById("describe").value;
            __extQuery__(json.curPage);
        } else {
            MyAlert("操作失败，请联系管理员!");
        }
    }

    //新增配件
    function addNew() {
        var describe = document.getElementById("describe").value;
        if ("" == describe || null == describe) {
            MyAlert('请先填写活动描述信息!');
            return false;
        }
        var desId = document.getElementById("desId").value;
        var activityType = document.getElementById("activityType").value;
        var url = '<%=contextPath%>/parts/baseManager/activityPartSet/activityPartSetAction/queryPartsInit.do?desId=' + desId + '&activityType=' + activityType;
        OpenHtmlWindow(url, 700, 500);
    }

    //返回
    function goBack() {
        btnDisable();
        location = '<%=contextPath%>/parts/baseManager/activityPartSet/activityPartSetAction/activityPartSetInit.do';
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
                    co
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
</script>
</body>
</html>
