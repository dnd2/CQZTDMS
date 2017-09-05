<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page import="com.infodms.dms.common.Constant" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c" %>
<head>
    <%String contextPath = request.getContextPath();%>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <jsp:include page="${contextPath}/common/jsp_head_new.jsp"/>
    <title> 物流商管理 </title>
    <script type="text/javascript">
        var myPage;
        var url = g_webAppName + "/parts/baseManager/partCityMileage/CityMileageManage/addLogisticsCityQuery.json";
        var title = null;
        var columns = [
            {header: "序号", align: 'center', renderer: getIndex},
            {header: "省份", dataIndex: 'PROVINCE_NAME', align: 'center'},
            {header: "市县", dataIndex: 'CITY_NAME', align: 'center'},
            {header: "约定时效（天）", dataIndex: 'TRANS_CYCLE', align: 'center', renderer: cycleText},
            {header: "计费首重(元/1kg)", dataIndex: 'TRANS_PRICE', align: 'center', renderer: priceText},
            {header: "计费续重(元/kg)", dataIndex: 'TRANS_MINI', align: 'center', renderer: miniText},
            {header: "操作", sortable: false, dataIndex: 'CITY_ID', align: 'center', renderer: myButton}
        ];
        jQuery.noConflict();
        function myButton(value, metaDate, record) {
            return String.format("<input type='button' name='delBtn' class='normal_btn' value='删除' onclick='deleteTblRow(this);'/>");
        }
        function cycleText(value, metaDate, record) {
            var provinceId = record.data.CITY_ID;
            document.getElementById("cityIds").value = record.data.cityIds;
            return String.format("<input type='text' name='TRANS_CYCLE_" + provinceId + "' class='short_txt' value='" + value + "' onclick=''/>");
        }
        function priceText(value, metaDate, record) {
            var provinceId = record.data.CITY_ID;
            return String.format("<input type='text' name='FIRST_WEIGHT_" + provinceId + "' class='short_txt' value='" + value + "' onclick=''/>");
        }
        function miniText(value, metaDate, record) {
            var provinceId = record.data.CITY_ID;
            return String.format("<input type='text' name='ADDITIONAL_WEIGHT_" + provinceId + "' class='short_txt' value='" + value + "' onclick=''/>");
        }
        function regionFun() {
            showCityMileage4Part('', 'cityIds', true);
        }
        function showDisId(oldIds, disIds) {
            var ids;
            if (oldIds.length > 0) {
                ids = oldIds + "," + disIds;
            } else {
                ids = disIds;
            }
            document.getElementById("cityIds").value = ids.toString();
            __extQuery__(1);
        }
        function del(disId) {
            var dis = document.getElementById("cityIds").value;
            var array = new Array();
            var newDisIds = "";
            array = dis.split(",");
            var bl = 0;
            if (array.length > 0) {
                for (var i = 0; i < array.length; i++) {
                    if (array[i] != '' && array[i] != disId) {
                        newDisIds += array[i] + ",";
                        bl = 1;
                    }
                }
            }
            var dId = newDisIds.substr(0, newDisIds.length - 1);
            document.getElementById("cityIds").value = dId;
            __extQuery__(1);
        }
        function checkData() {
            var logiId = document.getElementById("LOGI_ID").value;//物流商简称
            var transType = document.getElementById("TRANS_TYPE").value;//发运方式
            var status = document.getElementById("STATUS").value;//状态
            var REMARK = document.getElementById("REMARK").value;
            var weightRatio = document.getElementById("WEIGHT_RATIO").value;//系数
            var minWeight = document.getElementById("MINI_WEIGHT").value;//最小发运重量
            if (REMARK.length > 500) {
                MyAlert("备注内容过长，最多输入500字符。！");
                return;
            }

            if (logiId == null || logiId == "") {
                MyAlert("承运物流必填！");
                return;
            }
            if (transType == null || transType == "") {
                MyAlert("发运方式必填！");
                return;
            }
            if (weightRatio == null || weightRatio == "") {
                MyAlert("系数必填！");
                return;
            }
            if (minWeight == null || minWeight == "") {
                MyAlert("最小发运重量必填！");
                return;
            }
            var dis = document.getElementById("cityIds").value;
            var array = new Array();
            array = dis.split(",");
            var bl = 0;
            if (array.length > 0) {
                for (var i = 0; i < array.length; i++) {
                    if (array[i] != '' || array[i].length > 0) {
                        bl += 1;
                    }
                }
            }
            if (bl == 0) {
                MyAlert("请选择负责区域！");
                return  false;
            }
            return true;
        }

        function add() {
            if (!submitForm("fm")) {
                return;
            }
            if (checkData() == true) {
                MyConfirm("确认?", addLogistics);
            }
        }
        function addLogistics() {
            btnDisable();
            var url_save = g_webAppName + "/parts/baseManager/partCityMileage/CityMileageManage/addLogistics.json";
            makeNomalFormCall(url_save, addLogisticsBack, 'fm', 'queryBtn');
        }
        function addLogisticsBack(json) {
            btnEable();
            if (json.returnValue == 1) {
                disabledButton(["saveButton", "goBack"], false);
                parent.MyAlert("操作成功！");
                window.history.back();
            } else if (json.returnValue == 2) {
                var errorInfo = "";
                for (var i = 0; i < json.disList.length; i++) {
                    errorInfo += json.disList[i].PROVINCE_NAME + "-" + json.disList[i].CITY_NAME + ",";
                    if (json.disList.length / 2 == 0) {
                        errorInfo += "<br>";
                    }
                }
                disabledButton(["saveButton", "goBack"], false);
                MyAlert("操作失败！以下地市已有物流商：<p>" + errorInfo + "</p>");
            } else {
                disabledButton(["saveButton", "goBack"], false);
                MyAlert("操作失败！请联系系统管理员！");
            }
        }

        function deleteTblRow(obj) {
            var idx = obj.parentElement.parentElement.rowIndex;
            var tbl = document.getElementById('myTable');
            tbl.deleteRow(idx);
        }
        function back() {
            window.location.href = g_webAppName + "/parts/baseManager/partCityMileage/CityMileageManage/cityMileageInit.do";
        }
        function exportExcelTemplate() {
            fm.action = "<%=contextPath%>/parts/baseManager/PartBaseQuery/exportExcelTemplate.do";
            fm.submit();
        }
    </script>
</head>
<body onunload=''>
<div class="wbox">
    <div class="navigation"><img src="<%=contextPath %>/img/nav.gif"/>${path_location }物流商信息添加</div>
    <form method="post" name="fm" id="fm" enctype="multipart/form-data">
        <input type="hidden" name="LOGI_NAME" id="LOGI_NAME"/>
        <table class="table_edit">
            <tr>
                <th colspan="4"><img class="nav" src="<%=contextPath %>/img/subNav.gif"/> 基本信息</th>
            </tr>
            <tr>
                <td width="10%" height="25" align="right">承运物流：</td>
                <td align="left">
                    <select name="LOGI_ID" id="LOGI_ID" class="short_sel">
                        <option value="">请选择</option>
                        <c:if test="${listf!=null}">
                            <c:forEach items="${listwl}" var="list">
                                <option value="${list.fixValue }">${list.fixName }</option>
                            </c:forEach>
                        </c:if>
                    </select><span style="color: red">&nbsp;*</span>
                </td>
                <td width="20%" align="right">发运方式:</td>
                <td>
                    <select name="TRANS_TYPE" id="TRANS_TYPE" class="short_sel">
                        <option value="">请选择</option>
                        <c:if test="${listf!=null}">
                            <c:forEach items="${listf}" var="list">
                                <option value="${list.fixValue }">${list.fixName }</option>
                            </c:forEach>
                        </c:if>
                    </select><span style="color: red">&nbsp;*</span>
                </td>
            </tr>
            <tr>
                <td width="10%" height="25" align="right">系数：</td>
                <td align="left">
                    <input type="text" maxlength="20" class="middle_txt" name="WEIGHT_RATIO" id="WEIGHT_RATIO"
                           maxlength="10" value="6000"
                           datatype="0,is_null,10"/>
                </td>
                <td width="10%" height="25" align="right">最小发运重量(KG)：</td>
                <td align="left">
                    <input type="text" maxlength="20" class="middle_txt" name="MINI_WEIGHT" id="MINI_WEIGHT"
                           maxlength="10" value="1"
                           datatype="0,is_null,10"/>
                </td>
            </tr>
            <tr style="display: none">
                <td align="right">状态：</td>
                <td align="left">
                    <label>
                        <script type="text/javascript">
                            genSelBoxExp("STATUS", <%=Constant.STATUS%>, "", false, "short_sel", "", "false", '');
                        </script>
                    </label>
                </td>
            </tr>
            <tr>
                <td width="10%" align="right">备注：</td>
                <td colspan="3">
                    <textarea cols="50" rows="3" name="REMARK" datatype="1,is_textarea,500" id="REMARK"></textarea>
                </td>
            </tr>
            <tr/>
        </table>
        <table class="table_edit">
            <tr>
                <td align="center" colspan="4">
                    <input type="button" name="button1" id="saveButton" class="normal_btn" onclick="add();" value="保存"/>

                    <input type="button" name="button1" id="goBack" class="normal_btn" onclick="back();"
                           value="返回"/>
                </td>
            </tr>
        </table>

        <br/>
        <table class="table_info" border="0" id="file">
            <tr colspan="4">
                <th>
                    <img class="nav" src="<%=contextPath %>/img/subNav.gif"/><span>&nbsp;明细：</span>
                    <input type="button" class="normal_btn" onclick="regionFun();" value="新增"/>
                    <input type="hidden" id="cityIds" name="cityIds" value="${cityIds }"/>
                </th>
            </tr>
        </table>
        <c:if test="${!empty records}">
            <table class="table_list" id="myTable1"
                   style="border-bottom-color: rgb(218, 224, 238); border-bottom-width: 1px; border-bottom-style: solid;">
                <tbody>
                <tr class="table_list_th">
                    <th class="ascMask" style="text-align: center;">序号</th>
                    <th class="ascMask">省份</th>
                    <th class="ascMask">市县</th>
                    <th class="ascMask">约定时效（天）</th>
                    <th class="ascMask">计费首重(元/1kg)</th>
                    <th class="ascMask">计费续重(元/kg)</th>
                    <th class="ascMask">操作</th>
                </tr>
                <c:forEach items="${records}" var="map" varStatus="status">
                    <tr class="table_list_row1" style="background-color: rgb(253, 253, 253);">
                        <script type="text/javascript">document.getElementById("cityIds").value = "${map.cityIds}";</script>
                        <td align="center">${status.index+1 }</td>
                        <td align="center">${map.PROVINCE_NAME }</td>
                        <td align="center">${map.CITY_NAME }</td>
                        <td align="center">${map.TRANS_CYCLE }<input type="hidden" name="TRANS_CYCLE_${map.CITY_ID }"
                                                                     value="${map.TRANS_CYCLE }"/></td>
                        <td align="center">${map.TRANS_PRICE }<input type="hidden" name="FIRST_WEIGHT_${map.CITY_ID }"
                                                                     value="${map.TRANS_PRICE }"/></td>
                        <td align="center">${map.TRANS_MINI }<input type="hidden"
                                                                    name="ADDITIONAL_WEIGHT_${map.CITY_ID }"
                                                                    value="${map.TRANS_MINI }"/></td>
                        <td align="center"><input name="delBtn" class="normal_btn" onclick="del('${map.CITY_ID}')"
                                                  type="button" value="删除"/></td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </c:if>
        <jsp:include page="${contextPath}/queryPage/orderHidden.html"/>
        <jsp:include page="${contextPath}/queryPage/pageDiv.html"/>
    </form>
</div>
</body>
</html>
