<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib uri="/jstl/cout" prefix="c"%>
<!DOCTYPE html>
<html>
<%@ page import="com.infodms.dms.bean.AclUserBean"%>
<%@ page import="com.infodms.dms.common.Constant" %>
<%@ page import="com.infodms.dms.common.FileConstant"%>
<%
    AclUserBean user = (AclUserBean)request.getAttribute("user");
    String contextPath = request.getContextPath();
%>
<head>
	<meta charset="utf-8">
    <meta name="apple-mobile-web-app-capable" content="yes">
    <meta name="mobile-web-app-capable" content="yes">
    <meta name="format-detection"  content="telephone=no">
    <meta http-equiv="x-rim-auto-match" content="none">
    <meta http-equiv="pragma" content="no-cache">  
    <meta http-equiv="Cache-Control" content="no-cache, must-revalidate">  
    <meta http-equiv="expires" content="0"> 
    <meta name="viewport" content="width=device-width,initial-scale=1,minimum-scale=1,maximum-scale=1,user-scalable=no" />
    <jsp:include page="${contextPath}/common/jsp_head_new.jsp"/>
    <link rel="shortcut icon" href="<%=contextPath%>/img/favicon.ico" type="image/vnd.microsoft.icon">
<link rel="icon" href="<%=contextPath%>/img/favicon.ico"  type="image/vnd.microsoft.icon">
	<title>君马新能源DCS系统</title>

    <link href="<%=request.getContextPath()%>/jmstyle/css/master.css" rel="stylesheet" type="text/css">
    <script type="text/javascript" src="<%=request.getContextPath()%>/jmstyle/jslib/InfoMaster.js"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/jmstyle/jslib/jquery.mCustomScrollbar.concat.min.js"></script>
    <style>
    	.spanStyle{  
		      white-space: nowrap;  /*强制span不换行*/
		      display: inline-block;  /*将span当做块级元素对待*/
		      width: 32px;  /*限制宽度*/
		      overflow: hidden;  /*超出宽度部分隐藏*/
		      text-overflow: ellipsis;  /*超出部分以点号代替*/
		}
    </style>
</head>
<body class="u-main">
	<form action="" id="fm">
	    <input type="hidden" id="userId" name="userId" value="${userId}"/>
	    <input type="hidden" id="sessionId" name="sessionId" value="${sessionId}"/>
	    <input type="hidden" id="firstLogin" name="firstLogin" value="${firstLogin}"/>
	    <input type="hidden" id="tipDivContent" name="tipDivContent" value=""/>
	    <input type="hidden" id="dealerId" name="dealerId" value="${user.dealerId}"/>
	</form>
    <div class="u-wrap">
        <div class="u-header">
        	<div class="u-logo">
            	<img src="<%=request.getContextPath()%>/jmstyle/img/u-logo.png">
            </div>
        	<div class="u-title">
            	<img src="<%=request.getContextPath()%>/jmstyle/img/u-title.png">
            </div>
        	<div class="u-combox">
            	<div class="u-usr-ico"> 
                </div>
                <div class="u-usr-text">
                	<span class="u-usr-name">
	                	<a style="color:#FFFFFF; text-" href="<%=request.getContextPath()%>/common/UserManager/login.do">
		                	<c:choose>
		                    	<c:when test="${user.poseBusType == 10781004}">
		                           	${dealerName } | 
				                </c:when>
		                         <c:otherwise>${user.name }</c:otherwise>
		                    </c:choose>
		                </a>
                	</span>
                </div>
                <div class="u-logout">
                	<a href="<%=request.getContextPath()%>/common/UserManager/logout.do"><img src="<%=request.getContextPath()%>/jmstyle/img/u-logout.png" title="注销"></a>
                </div>
            </div>
        </div>
        <div class="u-bodyer">
        	<div class="u-left">
            	<div class="u-selbox">
                	<div class="u-sysdate"></div>
                </div>
                <div class="u-menu">
					<ul class="u-nav" id="u-menu-ul"></ul>
					<!--div class="u-menu-switch">
                    	<label class="u-menu-switch-btn">
                        	<input type="checkbox" id="menu-switch">
                            <span></span>
                        </label>
                    </div-->
                </div>
            </div>
            <div class="u-switcher">
            	<a href="javascript:void(0)"></a>
            </div>
        	<div class="u-space">
                <div class="tabs">  
                    <ul class="u-tab"></ul>
                </div>
            </div>        
        </div>
    </div>
    <script>
    	$(function() {
    		var switchStatus = false;
    		$('.u-switcher a').click(function() {
    			if (false === switchStatus) {
    				$(this).addClass('open').parent().addClass('u-switcher-hide').prev('.u-left').addClass('hide');
    				$('.u-bodyer').addClass('u-mini');
    			} else {
    				$(this).removeClass('open').parent().removeClass('u-switcher-hide').prev('.u-left').removeClass('hide');
    				$('.u-bodyer').removeClass('u-mini');
    			}
    						
    			switchStatus = !switchStatus;
    		});

             $(window).on('load', function() {
                $('.u-menu').mCustomScrollbar({
                    theme:"minimal-dark"
                });
            }); 
    	});
    </script>
</body>
<%-- 
<body>
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
--%>
</html>