<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@page import="com.infodms.dms.util.CalendarUtil"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c" %>
<%
    String contextPath = request.getContextPath();
	int curMonth=CalendarUtil.getMonth();
%>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp"/>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <meta http-equiv="X-UA-Compatible" content="IE=7">
    <title>经销商月度目标导入</title>
</head>
<bodys>
<div class=navigation><img src="<%=request.getContextPath()%>/img/nav.gif">当前位置&gt;配件管理&gt;经销商月度目标&gt;经销商月度目标导入</div>
<form name="fm" id="fm" method="post" enctype="multipart/form-data">
    <table class="table_query">
        <tr>
            <td class="table_query_label" colspan="2">1、请选择要导入任务年月：
                <select name='year' id="year" onchange="createMonthOptions();" class="table_query_label">
                    <%
                        String year = (String) request.getAttribute("curYear");
                        if (null == year || "".equals(year)) {
                            year = "0";
                        }
                        int y = Integer.parseInt(year);
                    %>
                    <option value="<%=y %>"><%=y %></option>
                    <option value="<%=y+1 %>"><%=y + 1 %></option>
                </select>
                <select name='month' id="month" class="table_query_label">
                	<option value='1' <c:if test="${curMonth==1}">selected="selected"</c:if>>01</option>
                    <option value='2' <c:if test="${curMonth==2}">selected="selected"</c:if>>02</option>
                    <option value='3' <c:if test="${curMonth==3}">selected="selected"</c:if>>03</option>
                    <option value='4' <c:if test="${curMonth==4}">selected="selected"</c:if>>04</option>
                    <option value='5' <c:if test="${curMonth==5}">selected="selected"</c:if>>05</option>
                    <option value='6' <c:if test="${curMonth==6}">selected="selected"</c:if>>06</option>
                    <option value='7' <c:if test="${curMonth==7}">selected="selected"</c:if>>07</option>
                    <option value='8' <c:if test="${curMonth==8}">selected="selected"</c:if>>08</option>
                    <option value='9' <c:if test="${curMonth==9}">selected="selected"</c:if>>09</option>
                    <option value='10' <c:if test="${curMonth==10}">selected="selected"</c:if>>10</option>
                    <option value='11' <c:if test="${curMonth==11}">selected="selected"</c:if>>11</option>
                    <option value='12' <c:if test="${curMonth==12}">selected="selected"</c:if>>12</option>
                </select>
             </td>
        </tr>
        <tr>
            <td class="table_query_label" colspan="2">
                2、点“<font color="#FF0000">浏览</font>”按钮，找到您所要导入的经销商月度目标文件,请确定文件的格式为“<strong style="color: red">经销商代码—经销商名称-任务金额</strong>”：&nbsp;&nbsp;&nbsp;
            </td>
        </tr>
        <tr>
            <td class="table_query_input" colspan="2">&nbsp;&nbsp;&nbsp;&nbsp;
                <input type="file" name="uploadFile" id="uploadFile" style="width: 250px" value=""/></td>
        </tr>
        <tr>
            <td class="table_query_label" width="30%">3、选择好文件后, 点“<font color="#FF0000">确定</font>”按钮完成粘贴。</td>
            <td class="table_query_label" align="left" width="56%">
                <input type="button" class="u-button" name="queryBtn" value="确定" onclick="upload();"/>
                <input type="button" class="u-button" name="downloadBtn" value="导入模板下载" onclick="downloadFile();"/></td>
        </tr>
    </table>
</form>
</div>
<script type="text/javascript">

    function upload() {
        var filevalue = fm.uploadFile.value;
        if (filevalue == '') {
            MyAlert('导入文件不能空!');
            return false;
        }
        var fi = filevalue.substring(filevalue.length - 3, filevalue.length);
        if (fi != 'xls') {
            MyAlert('导入文件格式不对,请导入xls文件格式');
            return false;
        }
        
        MyConfirm('确认上传?',function(){
            $('#fm')[0].action = "<%=contextPath%>/parts/monthPlan/MonthPlanManager/monthPlanUpload.do";
            $("#fm")[0].submit();
        	
        });
    }
    //下载模板
    function downloadFile() {
        fm.action = '<%=contextPath%>/parts/monthPlan/MonthPlanManager/download.do';
        fm.submit();
    }

    function doInit() {
        createMonthOptions();
    }
    //创建月份OPTION
    function createMonthOptions() {
        var curyear =${curYear};
        var year = document.getElementById("year").value;
        var month =${curMonth};
        var obj = document.getElementById("month");
        clrOptions(obj);
        if (year != curyear) {
            month = 1;
        }
        for (var i = 1; i < 13; i++) {
            var opt = document.createElement("option");
            opt.value = i;
            opt.appendChild(document.createTextNode(i));
            if (i == month) {
                opt.selected = true;
            }
            obj.appendChild(opt);
        }
    }
    //清空月份OPTION
    function clrOptions(obj) {
        obj.options.length = 0;
    }
</script>
</body>
</html>
