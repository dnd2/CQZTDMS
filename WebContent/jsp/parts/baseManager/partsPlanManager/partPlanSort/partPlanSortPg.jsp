<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" pageEncoding="UTF-8" %>
<%@page import="com.infodms.dms.common.Constant" %>
<%
    String contextPath = request.getContextPath();
%>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c" %>
<head>
    <title>配件变量维护</title>
    <jsp:include page="${path}/common/jsp_head_new.jsp"/>
</head>
<body>
<div class="wbox">
	<form name="fm" id="fm" method="post">
	    <input type="hidden" name="fixId" id="fixId" value="${fixId}"/>
	
	    <div class="navigation">
	        <img src="<%=request.getContextPath()%>/img/nav.gif"/>&nbsp;当前位置：基础数据管理 &gt; 计划相关参数维护 &gt; 配件分类维护
	    </div>
	    <table class="table_edit" id="tableEdit">
	        <th colspan="6" align="left"><img class="nav" src="<%=request.getContextPath()%>/img/subNav.gif"/> 设置信息</th>
	        <tr align="center">
	            <td>
	                <input type="button" class="u-button u-submit" value="增 加" onclick="addTblRow();"/>
	                <input class="cssbutton u-button" type="button" value="保 存" name="button1" onclick="saveData()"/>
	                <input type="hidden" id="ids" name="ids" value=""/>
	            </td>
	        </tr>
	    </table>
	    <table id="file" class="table_list" style="border-bottom:1px solid #DAE0EE">
	        <tr class="table_list_row0">
	            <td><input type="checkbox" disabled="disabled" checked="checked"/></td>
	            <td>序号</td>
	            <td>类型</td>
	            <td>安全系数</td>
	            <td>安全周期(天)</td>
	            <td>操作</td>
	        </tr>
	        <%
	            List<Map<String, Object>> pslist = (List<Map<String, Object>>) request.getAttribute("pslist");
	            for (int i = 0; i < pslist.size(); i++) {
	                Map<String, Object> map = pslist.get(i);
	                if (i % 2 == 0) {
	        %>
	        <tr class="table_list_row1">
	                <%
					}else{
				%>
	        <tr class="table_list_row2">
	            <%
	                }
	            %>
	            <td align="center">
	                <input type="checkbox" name="plan" disabled="disabled" checked="checked"/> <input type="hidden"
	                                                                                                  name="index"
	                                                                                                  value="<%=map.get("SORT_ID")%>"/>
	            </td>
	            <td align="center"><%=i + 1%>
	            </td>
	            <td align="center">
	                <input type="text" id="SORT_TYPE<%=map.get("SORT_ID")%>" class="long_txt"
	                       name="SORT_TYPE<%=map.get("SORT_ID")%>" value="<%=map.get("SORT_TYPE")%>"/>
	            </td>
	            <td align="center">
	                <input type="text" id="SAFTY_RATE<%=map.get("SORT_ID")%>" class="long_txt"
	                       name="SAFTY_RATE<%=map.get("SORT_ID")%>" onchange="dataTypeCheck2(this)"
	                       value="<%=map.get("SAFTY_RATE")%>"/>
	            </td>
	            <td align="center">
	                <input type="text" id="SAFTY_CYCLE<%=map.get("SORT_ID")%>" class="long_txt"
	                       name="SAFTY_CYCLE<%=map.get("SORT_ID")%>" onchange="dataTypeCheck(this)"
	                       value="<%=map.get("SAFTY_CYCLE")%>"/>
	            </td>
	            <td><input type="hidden" id="SORT_ID<%=map.get("SORT_ID")%>" name="SORT_ID<%=map.get("SORT_ID")%>"
	                       value="<%=map.get("SORT_ID")%>"/>
	                <%
	                    if ((map.get("STATE")).toString().equals(Constant.STATUS_ENABLE + "")) {
	                %>
	                <input type="button" class="cssbutton" value="失效"
	                       onclick="updateStateConfirm(<%=map.get("SORT_ID")%>,'disable');"/>
	                <%
	                } else {
	                %>
	                <input type="button" class="cssbutton" value="有效"
	                       onclick="updateStateConfirm(<%=map.get("SORT_ID")%>,'enable');"/>
	                <%
	                    }
	                %>
	            </td>
	        </tr>
	        <%
	            }
	        %>
	    </table>
	</form>
</div>

<script type="text/javascript">

    //数据验证
    function dataTypeCheck(obj) {
        var value = obj.value;
        if (isNaN(value)) {
            MyAlert("请输入数字!");
            obj.value = "";
            return;
        }
        var re = /^([1-9]+[0-9]*]*)$/;
	    if (!re.test(obj.value)) {
	        MyAlert("请输入正整数!");
	        obj.value = "";
	        return;
	    }
    }

    function dataTypeCheck2(obj)
	{
		var value = obj.value;
		value = value + "";
		value = parseFloat(value.replace(new RegExp(",","g"),""));
	    if (isNaN(value) || "" == value) {
	        MyAlert("请输入数字!");
	        obj.value = "";
	        return;
	    }
	    if(0 > value)
	    {
		    MyAlert("安全系数不能小于 0");
		    obj.value = "";
		    return;
	    }
	    obj.value = addKannma(value.toFixed(2));
	}

    //提交
    function saveData() {
        var ids = document.getElementsByName("index");
        var strids = '';
        var flag = true;
        if (ids.length <= 0) {
            MyAlert("请先添加计划分类选项!");
            return;
        }
        for (i = 0, m = 1; i < ids.length; i++, m++) {
            var oldname = document.getElementById("SORT_TYPE" + ids[i].value).value;
            if (oldname == '') {
                flag = false;
                MyAlert("第[" + m + "]行计划类型为空!");
                return;
            }
            if (ids.length > 1) {
                for (j = i + 1, n = m + 1; j < ids.length; j++, n++) {

                    var newname = document.getElementById("SORT_TYPE" + ids[j].value).value;

                    if (oldname == newname) {
                        flag = false;
                        MyAlert("第[" + m + "]行与第[" + n + "]行计划类型重复!");
                        return;
                    }
                }
            }
            var safeRate = document.getElementById("SAFTY_RATE" + ids[i].value).value;
            var safeCycle = document.getElementById("SAFTY_CYCLE" + ids[i].value).value;
            if ("" == safeRate || null == safeRate) {
                MyAlert("第[" + (i + 1) + "]行安全系数为空!");
                flag = false;
                return false;
            }
            else if ("" == safeCycle || null == safeCycle) {
                MyAlert("第[" + (i + 1) + "]行安全周期为空!");
                flag = false;
                return false;
            }
        }
        if (flag) {
            for (i = 0; i < ids.length; i++) {
                strids += ids[i].value + ",";
            }
            document.getElementById("ids").value = strids.substring(0, strids.length - 1);
            MyConfirm("确定保存设置信息?", savePlanSort, []);
        }
    }

    function savePlanSort() {
        btnDisable();
        var url = "<%=contextPath%>/parts/baseManager/partsPlanManager/partPlanSortAction/savePlanSortInfos.json";
        sendAjax(url, getResult, 'fm');
    }


    //有效、无效操作确认
    function updateStateConfirm(sortId, opType) {
        var str = "";
        if ("disable" == opType) {
            str = "确定失效该计划分类?"
        }
        else {
            str = "确定有效该计划分类?"
        }
        MyConfirm(str, updateState, [sortId, opType]);
    }

    function updateState(sortId, opType) {
        btnDisable();
        var url = "<%=contextPath%>/parts/baseManager/partsPlanManager/partPlanSortAction/updatePlanSortInfos.json?sortId=" + sortId + "&opType=" + opType;
        sendAjax(url, getResult, 'fm');
    }

    function getResult(json) {
        btnEnable();
        if (null != json) {
            if (json.errorExist != null && json.errorExist.length > 0) {
                MyAlert(json.errorExist);
            } else if (json.success != null && json.success == "true") {
                MyAlert("保存成功!");
                window.location.href = "<%=contextPath%>/parts/baseManager/partsPlanManager/partPlanSortAction/partPlanSortInit.do";
            } else {
                MyAlert("保存失败，请联系管理员!");
            }
        }
    }

    function addTblRow() {
        var tbl = document.getElementById('file');
        var rowObj = tbl.insertRow(tbl.rows.length);
        var ids = document.getElementsByName("index");
        if ((tbl.rows.length + 1) % 2 == 0) {
            rowObj.className = "table_list_row2";
        }
        else {
            rowObj.className = "table_list_row1";
        }
        var id = 0;
        for (i = 0; i < ids.length; i++) {
            id = ids[i].value;
        }
        id = parseInt(id) + 1;
        //MyAlert(id);
        var cell1 = rowObj.insertCell(0);
        var cell2 = rowObj.insertCell(1);
        var cell3 = rowObj.insertCell(2);
        var cell4 = rowObj.insertCell(3);
        var cell5 = rowObj.insertCell(4);
        var cell6 = rowObj.insertCell(5);

        cell1.innerHTML = '<tr><td align="center" nowrap><input type="checkbox" name="plan" disabled="disabled" checked="checked"/><input type="hidden" name="index" value="' + (id)
                + '"/></td>';
        cell2.innerHTML = '<td align="center" nowrap>' + (tbl.rows.length - 1) + '</td>';
        cell3.innerHTML = '<td align="center" ><INPUT id=SORT_TYPE' + (id) + ' class=long_txt name=SORT_TYPE' + (id) + ' value=""></td>';
        cell4.innerHTML = '<td align="center" ><INPUT id=SAFTY_RATE' + (id) + ' onchange="dataTypeCheck(this)" class=long_txt name=SAFTY_RATE' + (id) + ' value=""></td>';
        cell5.innerHTML = '<td align="center" ><INPUT id=SAFTY_CYCLE' + (id) + ' onchange="dataTypeCheck(this)" class=long_txt name=SAFTY_CYCLE' + (id) + ' value=""></td>';
        cell6.innerHTML = '<td><input type="button" class="cssbutton"  name="queryBtn" value="删除" onclick="deleteTblRow(this);" /></td></TR>';

    }
    //删除
    function deleteTblRow(obj) {
        var rowNum = obj.parentElement.parentElement.rowIndex;
        var tbl = document.getElementById('file');
        tbl.deleteRow(rowNum);
        var count = tbl.rows.length;
        for (var i = rowNum; i <= count; i++) {
            tbl.rows[i].cells[1].innerText = i;
            tbl.rows[i].cells[5].innerHTML = '<td><input type="button" class="cssbutton"  name="queryBtn" value="删除" onclick="deleteTblRow(this);" /></td></TR>';
            if ((i + 1) % 2 == 0) {
                tbl.rows[i].className = "table_list_row1";
            } else {
                tbl.rows[i].className = "table_list_row2";
            }
        }
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
</script>
</body>
</html>
