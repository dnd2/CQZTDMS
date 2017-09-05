<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" pageEncoding="UTF-8" %>
<%@page import="java.util.List" %>
<%
    String contextPath = request.getContextPath();
%>
<%@ page import="java.util.Map" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c" %>
<head>
    <title>配件变量维护</title>
    <jsp:include page="${path}/common/jsp_head_new.jsp"/>
</head>
<body>
<div class="wbox">
    <div class="navigation">
       <img src="<%=request.getContextPath()%>/img/nav.gif"/>&nbsp;当前位置：配件管理 &gt; 基础信息管理 &gt; 配件基础信息维护 &gt; 配件变量维护 &gt; 维护
     </div>
    <form name="fm" id="fm" method="post">
        <input type="hidden" name="curPage" id="curPage" value="${curPage}"/>
        <input type="hidden" name="fixId" id="fixId" value="${fixId}"/>

        <div class="form-panel">
            <h2><img class="panel-icon nav" src="<%=request.getContextPath()%>/img/subNav.gif"/> 变量类型</h2>
            <div class="form-body">
                <table class="table_edit" id="tableEdit" name="tableEdit">
                    <tr>
                        <td align="right" align="right">变量类型：</td>
                        <td align="left">${fixGoupname} <input type="hidden" name="TYPEID" id="TYPEID" value="${fixGouptype }"/>
                            <input type="hidden"
                                name="TYPENAME" id="TYPENAME" value="${fixGoupname }"/>
                        </td>
                        <td><input type="button" class="normal_btn" name="queryBtn6" value="增加" onclick="addTblRow();"/></td>
                    </tr>
                </table>
            </div>
        </div>
        
        <table id="file" class="table_list">
            <tr class="table_list_row0 tb-list-title">
                <td><label class="u-checkbox"><input type="checkbox" onclick="selectAll()"/><span></span></label></td>
                <td>序号</td>
                <td>变量名称</td>
                <td>变量值</td>
                <td>排序号</td>
                <td>操作</td>
            </tr>
            <%
                List<Map<String, Object>> typelist = (List<Map<String, Object>>) request.getAttribute("typelist");
                for (int i = 0; i < typelist.size(); i++) {
                    Map<String, Object> map = typelist.get(i);
                    if (i % 2 == 0) {
            %>
            <tr class="table_list_row1 td-pd-1">
                    <%
                        }else{
                    %>
            <tr class="table_list_row2 td-pd-1">
                <%
                    }
                %>
                <td align="center"><label class="u-checkbox"><input type="checkbox" name="plan"/><span></span></label><input type="hidden" name="index"
                                                                            value="<%=map.get("FIX_VALUE")%>"/></td>
                <td align="center"><%=i + 1%>
                </td>
                <td align="center"><input type="text" id="FIX_NAME<%=map.get("FIX_VALUE")%>" class="long_txt middle_txt"
                                        name="FIX_NAME<%=map.get("FIX_VALUE")%>"
                                        value="<%=map.get("FIX_NAME")%>"/></td>
                <td align="center"><%=map.get("FIX_VALUE")%>
                </td>
                <td align="center"><input type="text" id="SORT_NO<%=map.get("FIX_VALUE")%>" class="long_txt middle_txt"
                                        name="SORT_NO<%=map.get("FIX_VALUE")%>"
                                        value="<%=map.get("SORT_NO")%>" readonly style="border: none"/></td>
                <td><input type="hidden" id="FIX_ID<%=map.get("FIX_VALUE")%>" name="FIX_ID<%=map.get("FIX_VALUE")%>"
                        value="<%=map.get("FIX_ID")%>"/>
                    <%
                        if ((map.get("STATE")).toString().equals("10011001")) {
                    %>
                    <input type="button" class="u-button" value="失效"
                        onclick="deleteRes(<%=map.get("FIX_ID")%>,'disable');"/></td>
                <%
                } else {
                %>
                <input type="button" class="u-button" value="有效"
                    onclick="deleteRes(<%=map.get("FIX_ID")%>,'enable');"/></td>
                <%
                    }
                %>
            </tr>
            <%
                }
            %>

        </table>
        <table width="100%" align="center">
            <tr>
                <td height="2"></td>
            </tr>
            <tr>
                <input type="hidden" id="ids" name="ids" value=""/>
                <td align="center"><input class="u-button" type="button" value="保存" name="button1" onclick="updateData();">
                    &nbsp; <input
                            class="u-button" type="button" value="返回" name="button1" onclick="history.back();"></td>
            </tr>
            <tr>
                <td height="1"></td>
            </tr>
        </table>
    </form>
</div>    
<script type="text/javascript">
    //提交
    function updateData() {
        var ids = document.getElementsByName("index");
        var strids = '';
        var flag = true;
        if (ids.length <= 0) {
            MyAlert("请添加项目，否则请点返回！");
            return;
        }
        for (i = 0, o = 1; i < ids.length - 1; i++, o++) {
            for (j = i + 1, n = o + 1; j < ids.length; j++, n++) {
                var oldname = document.getElementById("FIX_NAME" + ids[i].value).value;
                var newname = document.getElementById("FIX_NAME" + ids[j].value).value;
                if (oldname == '') {
                    flag = false;
                    MyAlert("第[" + o + "]行变量名为空，请验证！");
                    return;
                }
                if (oldname == newname) {
                    flag = false;
                    MyAlert("第[" + o + "]行与第[" + n + "]行的变量名称重复，请修改！");
                    return;
                }
            }
        }
        if (flag) {
            for (i = 0; i < ids.length; i++) {
                strids += ids[i].value + ",";
            }
            document.getElementById("ids").value = strids.substring(0, strids.length - 1);
            fm.action = '<%=contextPath%>/parts/baseManager/partsBaseManager/PartFixcodeQuery/update.do';
            fm.submit();
        }
    }
    function addTblRow() {
        var tbl = document.getElementById('file');
        var rowObj = tbl.insertRow(tbl.rows.length);
        var ids = document.getElementsByName("index");
        if (tbl.rows.length % 2 == 0) {
            rowObj.className = "table_list_row2 td-pd-1";
        }
        else {
            rowObj.className = "table_list_row1 td-pd-1";
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

        cell1.innerHTML = '<tr><td align="center" nowrap><label class="u-checkbox"><input type="checkbox" name="plan"/><span></span></label><input type="hidden" name="index" value="' + (id)
        + '"/></td>';
        cell2.innerHTML = '<td align="center" nowrap>' + (tbl.rows.length - 1) + '</td>';
        cell3.innerHTML = '<td align="center" ><INPUT  id="FIX_NAME' + (id) + '" class="long_txt middle_txt" name="FIX_NAME' + (id) + '"></td>';
        cell4.innerHTML = '<td align="center">  ' + (id) + ' </td>';
        cell5.innerHTML = '<td align="center"><INPUT id="SORT_NO' + (id) + '" class="long_txt middle_txt" name="SORT_NO' + (id) + '" value="' + (id) + '">  </td>';
        cell6.innerHTML = '<td><input type="button" class="u-button"  name="queryBtn" value="删除" onclick="deleteTblRow(this);" /></td></tr>';
    }
    //删除
    function deleteTblRow(obj) {
        var idx = obj.parentElement.parentElement.rowIndex;
        var tbl = document.getElementById('file');
        tbl.deleteRow(idx);
    }
    function deleteRes(value, value2) {
        layer.confirm( '确定要修改?', {btn: ['确定', '取消'], skin: 'layui-layer-rim', icon: 15}, function() {
            sendAjax('<%=contextPath%>/parts/baseManager/partsBaseManager/PartFixcodeQuery/partUpdateState.json?fixId=' + value + '&flag=' + value2, ajaxBack, 'fm');
        });
    }
    function ajaxBack(json) {
        if (json.success != null && json.success == 'success') {
            layer.msg('操作成功!');
            window.location.href = '<%=contextPath%>/parts/baseManager/partsBaseManager/PartFixcodeQuery/partFixcodeModInit.do?fixId=' +${fixId };
        } else {
            layer.msg('系统异常，请联系管理员!', {icon: 15});
        }
    }
    function deleteRes2() {
        var obj = document.getElementsByName('plan');

        layer.confirm( '你确定要删除该记录吗？', {btn: ['确定', '取消'], skin: 'layui-layer-rim', icon: 15}, function() {
            var flag = false;
            var a = document.getElementById("file");
            var arr = new Array();
            var k = 0;
            for (var i = 0; i < obj.length; i++) {
                if (obj.item(i).checked) {
                    flag = true;
                    arr[k++] = obj.item(i).parentElement.parentElement.rowIndex;
                }
            }
            for (var j = arr.length - 1; j >= 0; j--) {
                a.deleteRow(arr[j]);
            }

            if (!flag) {
                layer.msg("你未选中需要删除的记录，请重新选择！", {icon: 15});
            }
        });
    }
    //全选
    function selectAll() {
        var obj = document.getElementsByName('plan');
        for (var i = 0; i < obj.length; i++) {
            if (obj.item(i).checked) {
                obj.item(i).checked = false;
            }
            else {
                obj.item(i).checked = true;
            }
        }
    }
</script>
</body>
</html>
