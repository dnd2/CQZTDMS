<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib uri="/jstl/cout" prefix="c"%>
<c:set value="${pageContext.request.contextPath}" var="contextPath" />
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<%@page import="com.infodms.dms.bean.AclUserBean"%>
<%@ page import="com.infodms.dms.common.Constant" %>
<%@ page import="com.infodms.dms.common.FileConstant"%>
<%
    AclUserBean user = (AclUserBean)request.getAttribute("user");
    String contextPath = request.getContextPath();
%>
<head>
	<meta http-equiv="X-UA-Compatible" content="IE=7" />
	<title>君马新能源DMS系统</title>
<link rel="stylesheet" href="<%=request.getContextPath()%>/js/zTreecommon/css/zTreeStyle.css" type="text/css">
<link rel="stylesheet" href="<%=request.getContextPath()%>/js/LigerUI/Source/lib/ligerUI/skins/Aqua/css/ligerui-all.css" type="text/css"/>
<style type="text/css">
	.global_set{
		float:right;
		border:1px solid #CCC;
		border-bottom:none;
		padding:1px;
		margin-top:7px;
	}	
	.global_set ul,.global_mail ul{
		float:left;
		padding-left:10px;
		margin-top:7px;
	}	
	.global_set li,.global_mail li{
		float:left;
		list-style-type:none;
		margin-right:8px;
	}
	.global_set li a,.global_mail li a{
		float:left;
		display:block;
		text-decoration:none;
		padding-left:4px;
	}
	.global_set	a:link,.global_set a:visited{
		color:#333;	
	}
	.global_set a:hover,.global_set	a:active{
		text-decoration:underline;
	}
	.global_set strong{ 
		color:#0066CC;
		font-weight:normal;
	}
	.global_set img{
		float:left;
	}
</style>
<link rel="stylesheet" href="<%=request.getContextPath()%>/style/menuCss/menuTree.css" type="text/css">
<link type="text/css" rel="stylesheet" href="<%=request.getContextPath()%>/style/system.css" />
<link type="text/css" rel="stylesheet" href="<%=request.getContextPath()%>/style/diaCnt.css" />
<script type="text/javascript">
var global_Path = "${contextPath}";
</script>
<%-- <script type="text/javascript" src="<%=request.getContextPath()%>/js/zTreecommon/jquery-1.4.4.min.js"></script> --%>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/LigerUI/Source/lib/jquery/jquery-1.5.2.min.js"></script>
<script type="text/javascript">
	var $jq = jQuery.noConflict();
</script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/zTreecommon/jquery.ztree.core-3.5.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/LigerUI/Source/lib/ligerUI/js/ligerui.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/menu1.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jslib/dialog_new.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jslib/DestoryPrototype.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jslib/prototype.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jslib/InfoAjax.js"></script>
</head>
<body id='indexBody'>
<form action="" id="fm">
    <input type="hidden" id="userId" name="userId" value="${userId}"/>
    <input type="hidden" id="sessionId" name="sessionId" value="${sessionId}"/>
    <input type="hidden" id="firstLogin" name="firstLogin" value="${firstLogin}"/>
    <input type="hidden" id="tipDivContent" name="tipDivContent" value=""/>
    <input type="hidden" id="dealerId" name="dealerId" value="${user.dealerId}"/>
</form>
	 <div id="layout1" style="width:100%">
        <div id='headDiv'>
        	<table width="98%" cellpadding="0" cellspacing="0" border="0">
            <tr>
                <td width="205"><img src="<%=request.getContextPath()%>/img/chana/LOGO-.jpg"/></td>
                <td valign="middle" nowrap="nowrap" >
                    <div  id="divtemp"></div>
                    <div  id="divtemp1"></div>
                    <!-- 如果是经销商就显示 -->
                    <div style="float: right;">
                    <div class="global_set">
                        <ul>
                            <!-- 经销商并且不是售后才显示 -->
                            <c:choose>
                           	<c:when test="${user.poseBusType == 10781004}">
	                            <li>
	                            	<div style="color: rgb(204, 102, 0);">${dealerName }</div>
	                            </li>
	                            <li>|</li>
		                    </c:when>
                            <c:otherwise>
	                            <li>
	                            	<div style="color: rgb(204, 102, 0);">${user.name }</div>
	                            </li>
                            	<li><span id="split">|</span></li>
                            </c:otherwise>
                            </c:choose>
			                <c:if test="${user.poseBusType == 10781004 && user.dealerType != 10771002}">
	                            <li>
									<span>
										<a id="tipSpan" href="javascript:void(0);" style="color:#CC6600" onclick="displayTipDiv()">待办事宜</a>
									</span>
	                                <div style="color:#CC6600;">
	                                    <div id="tipDiv" style="padding-top: 1%; padding-left: 2%; padding-right: 2%; line-height: 130%;font-size : 12pt ;border: solid #808080 1px; position: absolute; background-color: #FFFFFF; overflow: scroll; color: black; display: none;"></div>
	                                </div>
	                            </li>
	                            <li>|</li>
                            </c:if>
                            <li>
                                <!-- =====================================BEGIN 隐藏部分 ================================== -->
                                <div class="box" id="box" style="display:none;width:180px;position:absolute;">
                                    <iframe height="200" width="100%" frameborder="0" style="position:absolute;"></iframe>
                                    <div class="box_head" style="position:relative; cursor:pointer" onclick="hideBox()">
                                        <span id="boxTitle" class="left">信 息</span>
										<span class="right">
											<a class="roll" href="javascript:void(0)" onclick="hideBox();">&nbsp;</a>
										</span>
                                        <div style="clear:both"></div>
                                    </div>
                                    <div class="box_content" style="position:relative; height:233px;">
                                        <div style="padding:10px; " id="__box__"></div>
                                    </div>
                                </div>
                                <!-- =====================================END 隐藏部分 ================================== -->
                            </li>
                            <li>
                                <a id="a_notice" style="color:#CC6600" href="javascript:void(0)" onclick="queryNotice();">
                                <c:choose>
	                                <c:when test="${noticeSize!=null }">
	                                	<font color="red">消 息(
	                                		<label id="noticeNum">${noticeSize }</label>
	                                	)</font>
	                                </c:when>
	                                <c:otherwise>
	                                	消 息
	                                </c:otherwise>
                                </c:choose>
                                </a>
                            </li>
                            <li>|</li>
                            <li><a style="color:#CC6600" href="<%=request.getContextPath()%>/common/UserManager/logout.do">退 出</a></li>
                            <li>|</li>
                            <li><a style="color:#CC6600" href="<%=request.getContextPath()%>/common/UserManager/login.do">职位切换</a></li>
                        </ul>
                        <div style="clear:both;"></div>
                    </div>
                    </div>
                </td>
            </tr>
        </table>
        </div>
        <div style="float: left;width: 20%">
        	<div style="width:100%;overflow: auto;" id='menuTreeDiv'>
				<ul id="menuTree" class="ztree" style="width: 100%;padding: 0 0 0 0;"></ul>
            </ul>
        	</div>
        </div>
        <div style="float: left;width: 80%" id="framecenter"> 
        	<div title="首页新闻" style="width: 100%" >
                <iframe frameborder="0" name="home" id="home" src="<%=request.getContextPath()%>/welcome.jsp" width="100%"></iframe>
            </div>
        </div> 
        <div style="display: none">
        	<iframe name="displaytabNone" id="displaytabNone" src="<%=request.getContextPath()%>/welcome.jsp"></iframe>
        </div>
    </div>
</body>
</html>