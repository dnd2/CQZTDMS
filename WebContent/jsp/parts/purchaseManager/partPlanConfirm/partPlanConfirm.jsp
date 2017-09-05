<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="com.infodms.dms.common.Constant" %>
<%@taglib uri="/jstl/cout" prefix="c" %>
<%
    String contextPath = request.getContextPath();

%>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp"/>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <TITLE>计划审核</TITLE>
    <SCRIPT type=text/javascript>
        var myPage;

        var url = "<%=contextPath%>/parts/purchaseManager/partPlanConfirm/PartPlanConfirm/queryPartPlanConfirm.json";

        var title = null;

        var columns = [
            {header: "序号", align: 'center', renderer: getIndex},
            {header: "计划单号", dataIndex: 'PLAN_CODE', align: 'center'},
            {header: "计划员", dataIndex: 'PLANER_NAME', align: 'center'},
            {header: "制单日期", dataIndex: 'CREATE_DATE', align: 'center'},
            {header: "库房", dataIndex: 'WH_NAME', align: 'center'},
            {header: "计划年月", dataIndex: 'YEAR_MONTH', align: 'center'},
            {header: "计划类型", dataIndex: 'PLAN_TYPE', align: 'center', renderer: getItemValue},
            {header: "项数", dataIndex: 'XS', align: 'center'},
            {header: "总数量", dataIndex: 'SUM_QTY', align: 'center'},
            {header: "总金额", dataIndex: 'AMOUNT', style: 'text-align:right'},
            {header: "状态", dataIndex: 'STATE', align: 'center', renderer: getItemValue},
            {header: "审核日期", dataIndex: 'CHECK_DATE', align: 'center'},
            {header: "审核人", dataIndex: 'CHECK_NAME', align: 'center'},
            {id: 'action', header: "操作", sortable: false, dataIndex: 'PLAN_ID', renderer: myLink, align: 'center'}
        ];

        function myLink(value, meta, record) {
            return String.format("<a href=\"#\" onclick='confirmDetail(\"" + value + "\",this)'>[确认]</a><a href=\"#\" onclick='disableOrder(\"" + value + "\")'>[作废]</a>");
        }


        function confirmDetail(value) {
            disableAllStartBtn();
            window.location.href = "<%=contextPath%>/parts/purchaseManager/partPlanConfirm/PartPlanConfirm/partPlanView.do?planId=" + value;
        }

      //作废
      function disableOrder(value) {
      	if(confirm("确定作废?")){
      		btnDisable();
      	    var url = '<%=contextPath%>/parts/purchaseManager/partPlanConfirm/PartPlanConfirm/disableOrder.json?planId=' + value+'&curPage='+myPage.page;
      		sendAjax(url,getResult,'fm');
      	}
      }
      

      function getResult(jsonObj) {
          btnEable();
          if (jsonObj != null) {
              var success = jsonObj.success;
              var exceptions = jsonObj.Exception;
              if (success) {
                  MyAlert(success);
                  __extQuery__(jsonObj.curPage);
              } else if (exceptions) {
                  MyAlert(exceptions.message);
              }
          }
      }
      
        function getYearSelect(id, name, scope, value) {
            var date = new Date();
            var year = date.getFullYear();    //获取完整的年份
            var str = "";
            str += "<select  id='" + id + "' name='" + name + "'  style='width:73px;'>";
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
            var month = date.getMonth() + 1;
            var str = "";
            str += "<select  id='" + id + "' name='" + name + "'  style='width:73px;'>";
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
        function doQuery() {
            var msg = "";
            if (document.getElementById("MYYEAR").value != "") {
                if (document.getElementById("MYMONTH").value == "") {
                    msg += "请选择计划月!</br>";
                }
            }
            if (document.getElementById("MYMONTH").value != "") {
                if (document.getElementById("MYYEAR").value == "") {
                    msg += "请选择计划年!</br>";
                }
            }
            if (msg != "") {
                MyAlert(msg);
                return;
            }
            __extQuery__(1);
        }
        function exportExcel() {
            fm.action = "<%=contextPath%>/parts/purchaseManager/partPlanConfirm/PartPlanConfirm/exportExcel.do";
            fm.submit();
        }
        function disableAllStartBtn() {
            var inputArr = document.getElementsByTagName("a");
            for (var i = 0; i < inputArr.length; i++) {
                inputArr[i].disabled = true;
            }
        }
        function enableAllStartBtn() {
            var inputArr = document.getElementsByTagName("a");
            for (var i = 0; i < inputArr.length; i++) {
                inputArr[i].disabled = false;
            }
        }
    </script>
</HEAD>
<BODY onload="__extQuery__(1);enableAllStartBtn()">
<form name="fm" id="fm" method="post" enctype="multipart/form-data">
    <div class="wbox">
        <div class="navigation"><img src="<%=contextPath%>/img/nav.gif"/>&nbsp;当前位置>配件管理>采购计划管理>计划确认</div>
        <table class="table_query">
            <tr>
                <th width="100%" align="left" colspan="6"><img class="nav" src="<%=contextPath%>/img/subNav.gif"/> 查询条件
                </th>
            </tr>
            <tr>
                <td width="10%"   align="right">计划年月:</td>
                <td width="20%" align="left">
                    <script type="text/javascript">
                        getYearSelect("MYYEAR", "MYYEAR", 1, '');
                    </script>
                    <script type="text/javascript">
                        getMonThSelect("MYMONTH", "MYMONTH", '');
                    </script>
                </td>
                <td width="10%"   align="right">计划单号:</td>
                <td width="20%" align="left">
                    <INPUT class="middle_txt" type="text" id="PLAN_CODE" name="PLAN_CODE"></td>
                <td width="10%"   align="right">计划类型:</td>
                <td width="20%" align="left">
                    <script type="text/javascript">
                        genSelBoxExp("PLAN_TYPE", <%=Constant.PART_PURCHASE_PLAN_TYPE%>, "", true, "short_sel", "", "false", '');
                    </script>
                </td>
            </td>
            </tr>
            <tr>
                <td width="10%"   align="right">计划员:</td>
                <td width="20%">
                    <%--  <input class="phone_txt" type="text" id="PLANER_NAME" name="PLANER_NAME" /></td>--%>
                    <select id="planerId" name="planerId" class="short_sel">
                        <option value="">-请选择-</option>
                        <%--  <c:forEach items="${planerList}" var="planerList">
                              <option value="${planerList.USER_ID }">${planerList.USER_NAME }</option>
                          </c:forEach>--%>
                        <c:forEach items="${planerList}" var="planerList">
                            <c:choose>
                                <c:when test="${curUserId eq planerList.USER_ID}">
                                    <option selected="selected"  value="${planerList.USER_ID }">${planerList.USER_NAME }</option>
                                </c:when>
                                <c:otherwise>
                                    <option value="${planerList.USER_ID }">${planerList.USER_NAME }</option>
                                </c:otherwise>
                            </c:choose>
                        </c:forEach>
                    </select>
                <td width="10%"   align="right">制单日期:</td>
                <td width="20%" align="left">
                    <input class="short_txt" id="SCREATE_DATE" name="SCREATE_DATE" datatype="1,is_date,10"
                           maxlength="10" group="SCREATE_DATE,ECREATE_DATE"/>
                    <input class="time_ico" value=" " onclick="showcalendar(event, 'SCREATE_DATE', false);" type="button"/>
                    至
                    <input class="short_txt" id="ECREATE_DATE" name="ECREATE_DATE" datatype="1,is_date,10"
                           maxlength="10" group="SCREATE_DATE,ECREATE_DATE"/>
                    <input class="time_ico" value=" " onclick="showcalendar(event, 'ECREATE_DATE', false);" type="button"/></td>
                <td width="10%"   align="right">库房:</td>
                <td width="20%" align="left">
                    <select name="WH_ID" id="WH_ID" class="short_sel">
                        <option selected value=''>-请选择-</option>
                        <c:forEach items="${wareHouseList}" var="wareHouse">
                            <option value="${wareHouse.WH_ID}">${wareHouse.WH_NAME}</option>
                        </c:forEach>
                    </select></td>

            </tr>
            <tr>
                <td width="10%"   align="right">生成方式:</td>
                <td width="20%" align="left">
                    <script type="text/javascript">
                        genSelBoxExp("CREATE_TYPE", <%=Constant.PART_PURCHASE_PLAN_CREATE_TYPE %>, "", true, "short_sel", "", "false", '');
                    </script>
                </td>
                <td width="10%"   align="right">审核日期:</td>
                <td width="20%" align="left">
                    <input class="short_txt" id="SSUBMIT_DATE" name="SSUBMIT_DATE" datatype="1,is_date,10"
                           maxlength="10" group="SSUBMIT_DATE,ESUBMIT_DATE"/>
                    <input class="time_ico" value=" " onclick="showcalendar(event, 'SSUBMIT_DATE', false);" type="button"/>
                    至
                    <input class="short_txt" id="ESUBMIT_DATE" name="ESUBMIT_DATE" datatype="1,is_date,10"
                           maxlength="10" group="SSUBMIT_DATE,ESUBMIT_DATE"/>
                    <input class="time_ico" value=" " onclick="showcalendar(event, 'ESUBMIT_DATE', false);" type="button"/></td>
            </tr>
            <tr>
                <td align="center" colspan="6"><input class="normal_btn" type="button" name="BtnQuery" id="queryBtn" value="查询" 
                                                      onclick="__extQuery__(1)">
                    &nbsp;
                    <input class="normal_btn" type="button" value="导 出" name="button2" onclick="exportExcel()"/></td>
            </tr>
        </table>
        <jsp:include page="${contextPath}/queryPage/orderHidden.html"/>
        <jsp:include page="${contextPath}/queryPage/pageDiv.html"/>

    </div>
</form>
</BODY>
<script type="text/javascript">
    loadcalendar();  //初始化时间控件
</script>
</html>
