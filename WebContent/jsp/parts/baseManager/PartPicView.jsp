<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c" %>
<%
    String contextPath = request.getContextPath();
%>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp"/>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <title>配件图片查看</title>
</head>
<script type="text/javascript">
    function resizeImage(obj) {
        if (obj.height > 100)obj.height = 500;
        if (obj.width > 100)obj.width = 800;
    }
</script>
<body>
<div class="wbox">
    <div class="navigation"><img src="<%=contextPath%>/img/nav.gif"/>&nbsp;当前位置：
        基础信息管理 &gt;配件基础信息维护&gt; 配件主数据维护&gt; 实物图片查看
    </div>
    <form name="fm" id="fm">
        <div style="color: red;font-weight: bold;font-size: 20px">配件编码： ${partOldCode} 配件名称：${partName}
            <input type="button" name="saveBtn" id="saveBtn" value="关 闭"
                   onclick="_hide();"
                   class="normal_btn"/>
        </div>
        <img src='${picId}' onload="resizeImage(this);">
    </form>
</div>
</body>
</html>
