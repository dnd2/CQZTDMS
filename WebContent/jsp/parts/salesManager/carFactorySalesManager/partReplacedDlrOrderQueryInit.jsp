<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="com.infodms.dms.common.Constant" %>
<%@taglib uri="/jstl/cout" prefix="c" %>
<%
    String contextPath = request.getContextPath();
%>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp"/>
<head>
    <title></title>
</head>
<script language="javascript">
    //初始化查询TABLE
    var myPage;
    var url = "<%=contextPath%>/parts/salesManager/carFactorySalesManager/PartReplacedDlrOrder/replacedPartDlrOrderQuery.json?ORDER_TYPE=" +<%=Constant.CAR_FACTORY_SALES_MANAGER_ORDER_TYPE_10%>;
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
                    chk = i;
                    break;
                }
            }
            if (chk == 0) {//
                columns = [
                    {header: "序号", align: 'center', renderer: getIndex},
                    {
                        id: 'action',
                        header: "操作",
                        sortable: false,
                        dataIndex: 'ORDER_ID',
                        renderer: myLink,
                        align: 'center'
                    },
                    {header: "活动编码", dataIndex: 'ACTIVITY_CODE', style: 'text-align:center'},
                    {header: "订单号", dataIndex: 'ORDER_CODE', style: 'text-align:center'},
                    {header: "订单类型", dataIndex: 'ORDER_TYPE', style: 'text-align:center', renderer: getItemValue},
                    {header: "订货单位编码", dataIndex: 'DEALER_CODE', style: 'text-align:center'},
                    {header: "订货单位", dataIndex: 'DEALER_NAME', style: 'text-align:left'},
                    {header: "订货人", dataIndex: 'BUYER_NAME', style: 'text-align:left'},
                    {header: "制单日期", dataIndex: 'CREATE_DATE', style: 'text-align:center'},
                    {header: "提交日期", dataIndex: 'SUBMIT_DATE', style: 'text-align:center'},
//                    {header: "驳回原因", dataIndex: 'REBUT_REASON', style: 'text-align:left'},
                    {header: "订单状态", dataIndex: 'STATE', style: 'text-align:center', renderer: getItemValue}
                ];

            } else if (chk == 1) {//明细
                columns = [
                    {header: "序号", align: 'center', renderer: getIndex},
                    {header: "订单号", dataIndex: 'ORDER_CODE', align: 'center'},
                    {header: "订货单位代码", dataIndex: 'DEALER_CODE', style: 'text-align:center'},
                    {header: "订货单位", dataIndex: 'DEALER_NAME', style: 'text-align:left'},
                    {header: "配件编码", dataIndex: 'PART_OLDCODE', style: 'text-align:left'},
                    {header: "配件名称", dataIndex: 'PART_CNAME', style: 'text-align:left'},
                    {header: "切换件编码", dataIndex: 'PART_OLDCODE2', align: 'center'},
                    {header: "切换件名称", dataIndex: 'PART_CNAME2', align: 'center'},
                    {header: "申请数量", dataIndex: 'BUY_QTY', align: 'center'},
                    {header: "发出数量", dataIndex: 'CHECK_QTY', align: 'center'},
                    {header: "回运数量", dataIndex: 'BACKHAUL_QTY', align: 'center'},
                    {header: "切换件回运数量", dataIndex: 'NBACK_QTY', align: 'center'},
                    {header: "返回数量", dataIndex: 'WAREHOUSING_QTY', align: 'center'},
                    {header: "切换件返回数量", dataIndex: 'NINSTOCK_QTY', align: 'center'},
                    {header: "差异数量", dataIndex: 'DIFF_QTY', align: 'center'},
                    {header: "状态", dataIndex: 'STATE', align: 'center', renderer: getItemValue}
                ];
            } else {
                columns = [
                    {header: "序号", align: 'center', renderer: getIndex},
                    {header: "订货单位代码", dataIndex: 'DEALER_CODE', style: 'text-align:center'},
                    {header: "订货单位", dataIndex: 'DEALER_NAME', style: 'text-align:left'},
                    {header: "切换件编码", dataIndex: 'PART_OLDCODE', align: 'center'},
                    {header: "切换件名称", dataIndex: 'PART_CNAME', align: 'center'},
                    {header: "总发出数量", dataIndex: 'OUTSTOCK_QTY', align: 'center'},
                    {header: "总返回数量", dataIndex: 'PART_NUM', align: 'center'},
                    {header: "差异数量", dataIndex: 'DIFF_QTY', align: 'center'}
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
    function myLink(value, meta, record) {
        return String.format("<a href=\"#\" onclick='detailOrder(\"" + value + "\",\"" + record.data.ORDER_CODE + "\")'>[查看]</a>");
    }
    function checkLink(value, meta, record) {
        if (record.data.STATE ==<%=Constant.CAR_FACTORY_SALES_MANAGER_ORDER_STATE_01%>) {
            return String.format("<input id='cb' name='cb' type='checkbox' onclick='ck(this)' value='" + value + "' />");
        }
        return String.format('<img src="<%=contextPath%>/img/close.gif" />');
    }
    function ck(obj) {
        var cb = document.getElementsByName("cb");
        var flag = true;
        for (var i = 0; i < cb.length; i++) {
            if (!cb[i].checked) {
                flag = false;
            }
        }
        $('cbAll').checked = flag;
    }
    function ckAll(obj) {
        var cb = document.getElementsByName("cb");
        for (var i = 0; i < cb.length; i++) {
            if (cb[i].disabled) {
                continue;
            }
            cb[i].checked = obj.checked;
        }
    }

    //查看
    function detailOrder(value, code) {
        var buttonFalg = "disabled";
        OpenHtmlWindow("<%=contextPath%>/parts/salesManager/carFactorySalesManager/PartReplacedDlrOrder/orderDetailQuery.do?orderId=" + value + "&&orderCode=" + code + "&buttonFalg=" + buttonFalg, 900, 400);
    }

    function exportEx() {
        fm.action = "<%=contextPath%>/parts/salesManager/carFactorySalesManager/PartReplacedDlrOrder/exportToExcel.do";
        fm.submit();
    }
    function showPartInfo(obj) {
        if (obj.value == '2') {
            document.getElementById("part").style.display = "";
            document.getElementById("x1").style.display = "";
            document.getElementById("x3").style.display = "";
            __extQuery__(1);
        } else if (obj.value == '1') {
            document.getElementById("part").style.display = "none";
            document.getElementById("x1").style.display = "";
            document.getElementById("x3").style.display = "";
            __extQuery__(1);
        } else {
            document.getElementById("part").style.display = "";
            document.getElementById("x1").style.display = "none";
            document.getElementById("x3").style.display = "none";
            __extQuery__(1);
        }

    }
    function clrTxt(txtId) {
        document.getElementById(txtId).value = "";
    }
    function txtClr(txtId) {
        document.getElementById(txtId).value = "";
    }
</script>
<body onload="loadcalendar();">
<form name="fm" id="fm" method="post" enctype="multipart/form-data">
    <input id="ORDER_TYPE" name="ORDER_TYPE" type="hidden"
           value="<%=Constant.CAR_FACTORY_SALES_MANAGER_ORDER_TYPE_10%>"/>

    <div class="wbox">
        <div class="navigation">
            <img src="<%=contextPath%>/img/nav.gif"/>&nbsp;当前位置： 配件管理&gt;配件采购管理&gt;切换订单查询
        </div>
        <table border="0" class="table_query">
            <th colspan="6">
                <img class="nav"
                     src="<%=contextPath%>/img/subNav.gif"/>查询条件
            </th>
            <c:if test="${oemFlag == 10041001}">
                <tr id="x1">
                    <td align="right">订单号：</td>
                    <td align="left">
                        <input class="middle_txt"
                               type="text" id="ORDER_CODE" name="ORDER_CODE"/></td>
                    <td align="right">选择大区：</td>
                    <td>
                        <input type="text" id="orgCode" name="orgCode" value="" class="middle_txt"/>
                        <input name="obtn" id="obtn" class="mini_btn" type="button" value="&hellip;"
                               onclick="showOrg('orgCode','' ,'true','${orgId}');"/>
                        <input class="mini_btn" type="button" value="清空" onclick="clrTxt('orgCode');"/>
                    </td>
                    <td align="right">提交日期：</td>
                    <td align="left">
                        <input class="time_txt"
                               id="SSUBMIT_DATE" name="SSUBMIT_DATE" style="width: 65px"
                               datatype="1,is_date,10" maxlength="10" value="${old}"
                               group="SSUBMIT_DATE,ESUBMIT_DATE"/>
                        <input class="time_ico" value=" "
                               onclick="showcalendar(event, 'SSUBMIT_DATE', false);"
                               type="button"/> 至
                        <input class="time_txt" id="ESUBMIT_DATE"
                               name="ESUBMIT_DATE" datatype="1,is_date,10" style="width: 65px"
                               maxlength="10" value="${now}" group="SSUBMIT_DATE,ESUBMIT_DATE"/> <input
                            class="time_ico" value=" "
                            onclick="showcalendar(event, 'ESUBMIT_DATE', false);"
                            type="button"/></td>
                </tr>
                <tr id="x2">
                    <td align="right">订货单位编码：</td>
                    <td width="24%"><input class="middle_txt" type="text"
                                           id="DEALER_CODE" name="DEALER_CODE"/></td>
                    <td align="right">订货单位：</td>
                    <td align="left">
                        <input class="middle_txt"
                               type="text" id="DEALER_NAME" name="DEALER_NAME"/></td>
                    <td align="right">选择订货单位：</td>
                    <td align="left">
                        <input type="text" class="middle_txt" name="dealerName" size="15" value="" id="dealerName"/>
                        <input type="hidden" class="middle_txt" name="dealerCode" size="15" value=""
                               id="dealerCode"/>
                        <input name="button2" type="button" class="mini_btn"
                               onclick="showOrgDealer('dealerCode','','true','','','','<%=Constant.DEALER_TYPE_DWR%>','dealerName');"
                               value="..."/>
                        <input type="button" class="mini_btn" onclick="txtClr('dealerCode');txtClr('dealerName');"
                               value="清 空" id="clrBtn"/>
                    </td>
                </tr>
                <tr id="x3">
                    <td align="right">订单状态：</td>
                    <td align="left"><select name="state" id="state"
                                             class="short_sel">
                        <option selected value=''>-请选择-</option>
                        <c:forEach items="${stateMap}" var="stateMap">
                            <option value="${stateMap.key}">${stateMap.value}</option>
                        </c:forEach>
                    </select>
                    </td>
                    <td align="right">活动编码：</td>
                    <td align="left">
                        <input class="middle_txt"
                               type="text" id="ACTI_CODE" name="ACTI_CODE"/></td>
                </tr>
            </c:if>
            <c:if test="${oemFlag != 10041001}">
                <tr>
                    <td align="right">订单号：</td>
                    <td align="left"><input class="middle_txt"
                                            type="text" id="ORDER_CODE" name="ORDER_CODE"/></td>
                    <td align="right">制单日期：</td>
                    <td align="left">
                        <input class="time_txt"
                               id="SCREATE_DATE" name="SCREATE_DATE" datatype="1,is_date,10"
                               maxlength="10" value="${old}" style="width: 65px"
                               group="SCREATE_DATE,ECREATE_DATE"/> <input class="time_ico" value=" "
                                                                          onclick="showcalendar(event, 'SCREATE_DATE', false);"
                                                                          type="button"/> 至
                        <input class="time_txt" id="ECREATE_DATE"
                               name="ECREATE_DATE" datatype="1,is_date,10" value="${now}"
                               style="width: 65px" maxlength="10"
                               group="SCREATE_DATE,ECREATE_DATE"/>
                        <input class="time_ico" value=" "
                               onclick="showcalendar(event, 'ECREATE_DATE', false);"
                               type="button"/></td>
                    <td align="right">订单状态：</td>
                    <td align="left"><select name="state" id="state"
                                             class="short_sel">
                        <option selected value=''>-请选择-</option>
                        <c:forEach items="${stateMap}" var="stateMap">
                            <option value="${stateMap.key}">${stateMap.value}</option>
                        </c:forEach>
                    </select></td>
                </tr>

            </c:if>
            <tr id="part" style="display: none">
                <td align="right" align="right">配件编码：</td>
                <td><input class="middle_txt" type="text" name="PART_OLDCODE" id="PART_OLDCODE"/></td>
                <td align="right" align="right">配件名称：</td>
                <td><input class="middle_txt" type="text" name="PART_CNAME" id="PART_CNAME"/></td>
                <!-- 
                <td  align="right" align="right">件号：</td>
                <td ><input class="middle_txt" type="text" name="PART_CODE" id="PART_CODE"/></td>
                 -->
            </tr>
            <tr>
                <td colspan="6" align="center">
                    <input type="radio" name="RADIO_SELECT" value="1" checked onclick="showPartInfo(this);"/>切换单&nbsp;
                    <input type="radio" name="RADIO_SELECT" value="2" onclick="showPartInfo(this);"/>明细&nbsp;
                    <c:if test="${oemFlag == 10041001}">
                        <input type="radio" name="RADIO_SELECT" value="3" onclick="showPartInfo(this);"/>按单位汇总&nbsp;
                    </c:if>
                </td>
            </tr>
            <tr>
                <td colspan="6" align="center"><input name="BtnQuery"
                                                      id="queryBtn" class="normal_btn" type="button" value="查 询"
                                                      onclick="__extQuery__(1);"/>
                    <!--                     <input class="normal_btn" type="button" value="新 增" onclick="addOrder()"/> -->
                    <input class="normal_btn" type="button" value="导 出" onclick="exportEx()"/>
                </td>
            </tr>
        </table>
        <jsp:include page="${contextPath}/queryPage/orderHidden.html"/>
        <jsp:include page="${contextPath}/queryPage/pageDiv.html"/>
    </div>
</form>
</body>

</html>
