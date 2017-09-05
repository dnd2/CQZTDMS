<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c" %>
<%
    String contextPath = request.getContextPath();
%>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <meta http-equiv="X-UA-Compatible" content="IE=7">
    <jsp:include page="${contextPath}/common/jsp_head_new.jsp"/>
    <title>发运信息查询</title>
</head>
<body onload="autoAlertException();loadcalendar();showMsg();">
<form name="fm" id="fm" method="post" enctype="multipart/form-data">
    <div id="div1" class="wbox">
        <div class="navigation"><img src="<%=contextPath %>/img/nav.gif" alt=""/>&nbsp;当前位置： 报表管理 &gt;配件报表&gt;本部销售报表&gt;发运信息统计
        </div>
        <table border="0" class="table_query">
            <th colspan="6" width="100%"><img class="nav" src="<%=contextPath %>/img/subNav.gif"/> 查询条件</th>
            <tr>
                <td width="10%" align="right">服务商编码：</td>
                <td width="20%">
                    <input type="text" class="middle_txt" id="orgCode" name="orgCode"/>
                </td>
                <td width="10%" align="right">服务商名称：</td>
                <td width="20%">
                    <input type="text" class="middle_txt" id="orgName" name="orgName"/>
                </td>
                <td width="10%" align="right">发运单号：</td>
                <td width="20%">
                    <input type="text" class="middle_txt" id="TransCode" name="TransCode"/>
                </td>
            </tr>
            <tr>
                <td width="10%" align="right">发运方式：</td>
                <td width="20%">
                    <select name="transType" id="transType" class="short_sel">
                        <option value="">-请选择-</option>
                        <c:if test="${transList!=null}">
                            <c:forEach items="${transList}" var="list">
                                <option value="${list.fixName }">${list.fixName }</option>
                            </c:forEach>
                        </c:if>
                    </select>
                </td>
                <td width="10%" align="right">承运物流：</td>
                <td width="20%">
                    <select name="transorg" id="transorg" class="short_sel">
                        <option value="">-请选择-</option>
                        <c:if test="${transOrg!=null}">
                            <c:forEach items="${transOrg}" var="list">
                                <option value="${list.fixName }">${list.fixName }</option>
                            </c:forEach>
                        </c:if>
                    </select>
                </td>
                <td width="10%" align="right">发运日期：</td>
                <td width="25%">
                    <input class="time_txt" id="SCREATE_DATE" name="SCREATE_DATE"
                           datatype="1,is_date,10" maxlength="10" value="${old}" style="width:65px"
                           group="SCREATE_DATE,ECREATE_DATE"/>
                    <input class="time_ico" value=" " onclick="showcalendar(event, 'SCREATE_DATE', false);" type="button"/>
                    至
                    <input class="time_txt" id="ECREATE_DATE" name="ECREATE_DATE" datatype="1,is_date,10" value="${now}"
                           style="width:65px"
                           maxlength="10" group="SCREATE_DATE,ECREATE_DATE"/>
                    <input class="time_ico" value=" " onclick="showcalendar(event, 'ECREATE_DATE', false);" type="button"/>
                </td>
            </tr>
            <tr>
                <td width="10%" align="right">库房：</td>
                <td width="20%">
                    <select name="whId" id="whId" class="short_sel">
                        <option value="">-请选择-</option>
                        <c:forEach items="${wareHouseList}" var="wareHouse">
                            <option value="${wareHouse.WH_ID}">${wareHouse.WH_CNAME}</option>
                        </c:forEach>
                    </select>
                </td>
            </tr>
            <tr>
                <td colspan="6" align="center">
                    <input type="radio" name="RADIO_SELECT" value="1" checked/>发运跟踪&nbsp;
                    <input type="radio" name="RADIO_SELECT" value="2"/>发运明细&nbsp;
                </td>
            </tr>
            <tr>
                <td colspan="6" align="center">
                    <input name="BtnQuery" id="queryBtn" class="normal_btn" type="button" value="查 询"
                           onclick="__extQuery__(1)"/>&nbsp;
                    <input name="BtnQuery" id="exportBtn" class="normal_btn" type="button" value="导 出"
                           onclick="exportExcel()"/>
                        <input type="hidden" name="excelType" id="excelType" value=""></input>
                        <input class="normal_btn" style="width: 80px;" type="button" id="BtnuploadTable" value="物流信息导入"
                               onclick="showUpload(1);"/>
                        <input class="normal_btn" style="width: 80px;" type="button" id="BtnuploadTable2" value="收货时间导入"
                               onclick="showUpload(2);"/>
                </td>
            </tr>
            <tr>
                <td colspan="6" align="center" style="color: red;font-weight: bold">
                    提示：此处的发运日期是指实际的出库日期，非发运计划生成日期。点击物流单号即可查看导入的物流发运信息。

                </td>
            </tr>
        </table>
        <table id="uploadTable" style="display: none">
            <tr>
                <td><font color="red">
                    <input type="button" class="normal_btn" value="下载模版" onclick="exportExcelTemplate(1)"/>
                    文件选择后,点&quot;确定&quot;按钮,完成上传操作：&nbsp;</font>
                    <input type="file" name="uploadFile" style="width: 250px" id="uploadFile" value=""/>
                    &nbsp;
                    <input type="button" id="upbtn" class="normal_btn" value="确定" onclick="uploadEx('1')"/></td>
            </tr>
        </table>
        <table id="uploadTable2" style="display: none">
            <tr>
                <td><font color="red">
                    <input type="button" class="normal_btn" value="下载模版" onclick="exportExcelTemplate(2)"/>
                    文件选择后,点&quot;确定&quot;按钮,完成上传操作：&nbsp;</font>
                    <input type="file" name="uploadFile2" style="width: 250px" id="uploadFile2" value=""/>
                    &nbsp;
                    <input type="button" id="upbtn2" class="normal_btn" value="确定" onclick="uploadEx('2')"/></td>
            </tr>
        </table>
    </div>
    <jsp:include page="${contextPath}/queryPage/orderHidden.html"/>
    <jsp:include page="${contextPath}/queryPage/pageDiv.html"/>
</form>
<script type="text/javascript">
    var myPage;
    var url = "<%=contextPath%>/report/partReport/partSalesReport/PartTransInfoRep/query.json";
    var title = null;
    var columns = null;
    var len = 0;

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
                    chk = i;
                    break;
                }
            }
            if (chk == 0) {//发运跟踪
                columns = [
                    {header: "序号", align: "center", renderer: getIndex},
                    {header: "服务商编码", dataIndex: "DEALER_CODE", align: "center"},
                    {header: "服务商名称", dataIndex: "DEALER_NAME", style: 'text-align:left'},
                    {header: "发运日期", dataIndex: "CREATE_DATE", align: "center"},
                    {header: "发运单号", dataIndex: "TRPLAN_CODE", align: "center", renderer: myLink},
                    {header: "运输方式", dataIndex: "TRANS_TYPE", align: "center"},
                    {header: "装箱数量", dataIndex: "PKG_NUMS", align: "center"},
                    {header: "发货数量", dataIndex: "PKG_QTY", align: "center"},
                    {header: "体积", dataIndex: "VOLUME", align: "center"},
                    {header: "实际重量", dataIndex: "WEIGHT", align: "center"},
                    {header: "折合重KG", dataIndex: "EQ_WEIGHT", align: "center"},
                    {header: "计费重量</p>KG", dataIndex: "CH_WEIGHT", align: "center"},
                    {header: "订单类型", dataIndex: "ORDER_TYPE", align: "center"},
                    {header: "要求到货</p>天数", dataIndex: "ARRIVE_DAYS", align: "center"},
                    {header: "要求到货</p>时间", dataIndex: "REQUIRE_DATE", align: "center"},
                    {header: "系统确认</p>收货时间", dataIndex: "ARRIVAL_DATE", align: "center"},
                    {header: "实际到货</p>时间", dataIndex: "ARR_DATE2", align: "center", renderer: remark5},
                    {header: "超期后预</p>计到货时间", dataIndex: "ARR_DATE", align: "center", renderer: remark},
                    {header: "运输单位", dataIndex: "TRANSPORT_ORG", align: "center"},
                    {header: "在途情况", dataIndex: "OTW_DES", align: "center", renderer: remark2},
                    {header: "备注", dataIndex: "REMARK1", align: "center", renderer: remark3},
                    {header: "超期原因", dataIndex: "REMARK2", align: "center", renderer: remark4}
                ];

            } else {//明细
                columns = [
                    {header: "序号", align: "center", renderer: getIndex},
                    {header: "服务商编码", dataIndex: "DEALER_CODE"},
                    {header: "服务商名称", dataIndex: "DEALER_NAME", style: 'text-align:left'},
                    {header: "订单号", dataIndex: "ORDER_CODE", align: "center"},
                    {header: "销售单", dataIndex: "SO_CODE", align: "center"},
                    {header: "拣货单", dataIndex: "PICK_ORDER_ID", align: "center"},
                    {header: "出库日期", dataIndex: "CREATE_DATE", align: "center"},
                    {header: "发运单号", dataIndex: "TRPLAN_CODE", align: "center"},
                    {header: "配件编码", dataIndex: "PART_OLDCODE", align: "center"},
                    {header: "配件名称", dataIndex: "PART_CNAME", align: "center", style: 'text-align:left'},
                    {header: "出库数量", dataIndex: "OUTSTOCK_QTY", align: "center"},
                    {header: "单价", dataIndex: "SALE_PRICE", align: "center"},
                    {header: "出库金额", dataIndex: "SALE_AMOUNT", align: "center", style: 'text-align:right'},
                    {header: "箱号", dataIndex: "PKG_NO", align: "center"}
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
    function exportExcel() {
        fm.action = "<%=contextPath%>/report/partReport/partSalesReport/PartTransInfoRep/exportExcel.do";
        fm.submit();
    }
    function remark(value, meta, record) {
        var trplanId = record.data.TRPLAN_ID;
        var output = "<input class='time_txt' id='ARR_DATE_" + trplanId + "' name='ARR_DATE_" + trplanId + "' datatype='1,is_date,10' value='" + value + "'" +
                " style='width:65px;margin-right:5px' maxlength='10' onclick='saveRemark(" + trplanId + ",this)'/>" +
                "<input class='time_ico' onclick='showDate(event, \"ARR_DATE_" + trplanId + "\", false);' type='button'/>";
        return String.format(output);
        //return String.format("<input type='text' style='width:50px' id=ARR_DATE_" + trplanId + " name=ARR_DATE_" + trplanId + " value='" + value + "' onchange='saveRemark(" + trplanId + ",this)' >")
    }
    function remark2(value, meta, record) {
        var trplanId = record.data.TRPLAN_ID;
        return String.format("<input type='text' style='width:100px' id=OTW_DES_" + trplanId + " name=OTW_DES_" + trplanId + " value='" + value + "' onchange='saveRemark2(" + trplanId + ",this)'>")
    }
    function remark3(value, meta, record) {
        var trplanId = record.data.TRPLAN_ID;
        return String.format("<input type='text' style='width:100px' id=REMARK1_" + trplanId + " name=REMARK1_" + trplanId + " value='" + value + "' onchange='saveRemark3(" + trplanId + ",this)'>")
    }
    function remark4(value, meta, record) {
        var trplanId = record.data.TRPLAN_ID;
        return String.format("<input type='text' style='width:100px' id=REMARK2_" + trplanId + " name=REMARK2_" + trplanId + " value='" + value + "' onchange='saveRemark4(" + trplanId + ",this)'>")
    }
    function remark5(value, meta, record) {
        var trplanId = record.data.TRPLAN_ID;

        var output = "<input type='text' id='REMARK5_" + trplanId + "' name='REMARK5_" + trplanId + "' datatype='1,is_date,10' value='" + value + "'" +
                " style='width:65px;margin-right:5px;position:relative' maxlength='10' onchange='saveRemark5(" + trplanId + ",this)'/>" +
                "<input class='time_ico' onclick='showDate(event, \"REMARK5_" + trplanId + "\", false);' type='button'/>";

        //return String.format("<input type='text' style='width:100px' id=REMARK5_" + trplanId + " name=REMARK5_" + trplanId + " value='" + value + "' onchange='saveRemark5(" + trplanId + ",this)'>")
        return String.format(output);
    }


    function showDate(event, controlid1, addtime1, startdate1, enddate1, hiddY) {
        tmp = controlid = controlid1;
        addtime = addtime1;
        startdate = startdate1 ? parsedate(startdate1) : false;
        enddate = enddate1 ? parsedate(enddate1) : false;
        currday = $F(controlid) ? parsedate($F(controlid)) : today;

        hh = currday.getHours();
        ii = currday.getMinutes();
        var p = getposition($(controlid));
        document.getElementById('calendar').style.display = 'block';
        document.getElementById('calendar').style.left = (p['x'] - 635) + 'px';
        document.getElementById('calendar').style.top = (p['y'] + 22) + 'px';
        _cancelBubble(event);
        refreshcalendar(currday.getFullYear(), currday.getMonth());
        if (lastcheckedyear != false) {
            document.getElementById('calendar_year_' + lastcheckedyear).className = 'default';
            document.getElementById('calendar_year_' + today.getFullYear()).className = 'today';
        }
        if (lastcheckedmonth != false) {
            document.getElementById('calendar_month_' + lastcheckedmonth).className = 'default';
            document.getElementById('calendar_month_' + (today.getMonth() + 1)).className = 'today';
        }
        document.getElementById('calendar_year_' + currday.getFullYear()).className = 'checked';
        document.getElementById('calendar_month_' + (currday.getMonth() + 1)).className = 'checked';
        document.getElementById('hourminute').style.display = addtime ? '' : 'none';
        lastcheckedyear = currday.getFullYear();
        lastcheckedmonth = currday.getMonth() + 1;


        if (hiddY) {

            document.getElementById("year").innerHTML = "";
            document.getElementById("hspan").innerHTML = "";
            hiddYEAR = true;
        } else {
            document.getElementById("hspan").innerHTML = "-  ";
            hiddYEAR = false;
        }
    }


    function saveRemark(tranplanId, obj) {
        var updateUrl = "<%=contextPath%>/report/partReport/partSalesReport/PartTransInfoRep/updateTransRemark.json?tranPlanId=" + tranplanId + "&ARR_DATE=" + obj.value;
        sendAjax(updateUrl, result, 'fm');
    }
    function saveRemark2(tranplanId, obj) {
        var updateUrl = "<%=contextPath%>/report/partReport/partSalesReport/PartTransInfoRep/updateTransRemark.json?tranPlanId=" + tranplanId + "&OTW_DES=" + obj.value;
        sendAjax(updateUrl, result, 'fm');
    }
    function saveRemark3(tranplanId, obj) {
        var updateUrl = "<%=contextPath%>/report/partReport/partSalesReport/PartTransInfoRep/updateTransRemark.json?tranPlanId=" + tranplanId + "&REMARK1=" + obj.value;
        sendAjax(updateUrl, result, 'fm');
    }
    function saveRemark4(tranplanId, obj) {
        var updateUrl = "<%=contextPath%>/report/partReport/partSalesReport/PartTransInfoRep/updateTransRemark.json?tranPlanId=" + tranplanId + "&REMARK2=" + obj.value;
        sendAjax(updateUrl, result, 'fm');
    }
    function saveRemark5(tranplanId, obj) {
        MyAlert(obj.value);
        if (obj) {
            var updateUrl = "<%=contextPath%>/report/partReport/partSalesReport/PartTransInfoRep/updateTransRemark.json?tranPlanId=" + tranplanId + "&REMARK3=" + obj.value;
            sendAjax(updateUrl, result, 'fm');
        }

    }
    function result(jsonObj) {
        if (jsonObj != null) {
            var success = jsonObj.success;
            var error = jsonObj.error;
            var exceptions = jsonObj.Exception;
            if (success) {
                MyAlert(success);
                __extQuery__(1);
            } else if (error) {
                MyAlert(error);
            } else if (exceptions) {
                MyAlert(exceptions.message);
            }
        }
    }


    //批量导入
    function showUpload(type) {
        if (type == 1) {
            if ($("uploadTable").style.display == "none") {
                $("uploadTable").style.display = "block";
            } else {
                $("uploadTable").style.display = "none";
            }
        } else {
            if ($("uploadTable2").style.display == "none") {
                $("uploadTable2").style.display = "block";
            } else {
                $("uploadTable2").style.display = "none";
            }
        }
        excelType(type);
    }

    function excelTypeValue(type) {
        document.getElementById("excelType").value = type;
    }

    //下载模板
    function exportExcelTemplate(type) {
        excelTypeValue(type)
        fm.action = "<%=contextPath%>/report/partReport/partSalesReport/PartTransInfoRep/exportExcelTemplate.do";
        fm.submit();
    }

    //导入xls
    function uploadEx(type) {
        var fileValue;

        excelTypeValue(type)
        if (type == 1) {
            fileValue = document.getElementById("uploadFile").value;
        } else {
            fileValue = document.getElementById("uploadFile2").value;
        }
        if (fileValue == "") {
            MyAlert("请选择文件!");
            return;
        }
        var fi = fileValue.substring(fileValue.length - 3, fileValue.length);
        if (fi != 'xls') {
            MyAlert('导入文件格式不对,请导入xls文件格式');
            return false;
        }

        if (confirm("确定导入?")) {
            fm.action = "<%=contextPath%>/report/partReport/partSalesReport/PartTransInfoRep/uploadPartLogExcel.do?";
            fm.submit();
        }

    }
    function showMsg() {
        if (${OK}) {
            if ('${OK}' == "2") {
                MyAlert("导入成功!");
            }
        }
    }
    function myLink(trplanCode) {
        return String.format("<a href=\"#\" style='color:red' onclick='showlogistics(\"" + trplanCode + "\")'>" + trplanCode + "</a>");
    }

    function showlogistics(trplanCode) {
        OpenHtmlWindow("<%=contextPath%>/report/partReport/partSalesReport/PartTransInfoRep/queryLogisticsInfoInit.do?trplanCode=" + trplanCode, 1000, 450);
    }
</script>

</body>
</html>
