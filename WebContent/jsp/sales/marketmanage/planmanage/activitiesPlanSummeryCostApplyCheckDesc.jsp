<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="com.infodms.dms.common.Constant"%>
<%@ page import="java.util.*"%>
<%@taglib uri="/jstl/cout" prefix="c" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<script type="text/javascript" src="<%=request.getContextPath()%>/js/funccommon/activityfunc.js"></script>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>市场活动总结费用申请(经销商)</title>
<% String contextPath = request.getContextPath();  %>
</head>
<body>
<div class="navigation"><img src="<%=request.getContextPath()%>/img/nav.gif" />&nbsp;当前位置： 市场活动管理 &gt; 活动方案管理 &gt; 市场活动总结费用申请(经销商)</div>
<table class="table_query">
    <tr>
        <td align="right">审核意见:</td>
        <td>
            <textarea cols="50" rows="20" id="checkDesc" name="checkDesc"></textarea>
            <script>
                function doInit(){
                    document.getElementById("checkDesc").value='${checkDesc}';
                }
            </script>
        </td>
    </tr>
</table>

</body>
</html>