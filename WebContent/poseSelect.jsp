<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib uri="/jstl/cout" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<%
String path = request.getContextPath();
%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<link rel="shortcut icon" href="<%=request.getContextPath()%>/img/favicon.ico" type="image/vnd.microsoft.icon">
<link rel="icon" href="<%=request.getContextPath()%>/img/favicon.ico"  type="image/vnd.microsoft.icon">
	<title>君马新能源DCS系统</title>
<link href="<%=request.getContextPath()%>/style/content.css" rel="stylesheet" type="text/css" />
<style>body{background: #f6f8fa}</style>
</head>
<body>
	<div class="user-box">
        <div class="logo">
            <img src="<%=request.getContextPath()%>/jmstyle/img/logo_small.png" alt="">
        </div>
        <div class="user-panel">
            <div class="user-panel-heading">
                <h3 class="user-panel-title">职位选择</h3>
            </div>
            <div class="user-panel-body">
				<form id="fm" name="fm" method="post">
					<input value='' type='hidden' class="normal_btn" name='deptId' />
					<input value='' type='hidden' class="normal_btn" name='poseId' />
					<c:forEach items="${poseList}" var="item" varStatus="s">
						<div class="user-role">
							<a href="javascript:void(0)" class="user-avatar">
								<c:choose>
									<c:when test="${s.count==1}">
										<img src="<%=request.getContextPath()%>/jmstyle/img/user_1.png" alt="">
									</c:when>
									<c:otherwise>
										<img src="<%=request.getContextPath()%>/jmstyle/img/user_2.png" alt="">	
									</c:otherwise>
								</c:choose>
							</a>
							<div class="user-role-body">
								<h5>
									<p>${item.poseName}</p>
									<strong>部门：</strong>${item.poseCode}
								</h5>
								<a class="user-entrance" href="javascript:void(0)" onclick="menuShow('${item.orgId}','${item.poseId}','${item.poseType}','${item.poseBusType}');"><i class="icon-circle-arrow-right"></i></a>
							</div>
						</div>
						<c:if test="${s.count>0 && s.last==false}"><hr></c:if>	
					</c:forEach>
				</form>	
			</div>
		</div>
		<div class="user-panel-bottom">	
			<a href="javascript:goOut();" title="退出"><i class="icon-remove-sign"></i></a>
		</div>
    </div>

<%--
<form id="fm" name="fm" method="post">
<input value='' type='hidden' class="normal_btn" name='deptId' />
<input value='' type='hidden' class="normal_btn" name='poseId' />
<br />
<br />
<table width="100%" cellpadding="0" cellspacing="0">
  <tr>
    <td align="center" >
    <table cellspacing="0" cellpadding="0">
      <tr> 
        <td width="635" height="110" align="center" valign="bottom" background="<%=path%>/img/chana/login1.jpg">
        </td>
      </tr>
      <tr>
      	<td align="center">
      		<table cellpadding="1" cellspacing="0" class=table_list>  
      		<tr>
		         <th class="center"><strong>职位名称</strong></th>
		         <th class="center"><strong>所属部门</strong></th>
		         <th class="center"><strong>职位选择</strong></th>
		      </tr>
            <c:forEach items="${poseList}" var="item">
				<tr class=table_list_row1>
					<td class="center">${item.poseName}</td>
					<td class="center">${item.poseCode}</td>
					<td class="center">
					    <a href="#" name='poseSelect' onclick="menuShow('${item.orgId}','${item.poseId}','${item.poseType}','${item.poseBusType}');">[职位选择]</a>
						 <input value='职位选择' type='button' class="normal_btn" name='poseSelect' onclick="menuShow('${item.orgId}','${item.poseId}','${item.poseType}','${item.poseBusType}');"/> 
					</td>
				</tr>
			</c:forEach>
			</table>
		 </td>
	   </tr>	 	
       <tr>
        	<td height="22" background="<%=path%>/img/chana/login3.jpg" style="border: 1px #DAE0EE solid;" align="right">
        		<input value='退 出' type='button' class="u-button u-cancel" style="height:10px" name='logout' onclick="goOut();" />&nbsp;&nbsp;
        	</td>
       </tr>
      </table>
   </td>
  </tr>
</table>   
</form>
--%>
<script type="text/javascript">
    //modified by andy.ten@tom.com 增加POSE_TYPE参数
	function menuShow(i,j,poseType,poseBusType) {
		var fm=document.fm;
		fm.deptId.value=i;
		fm.poseId.value=j;
		fm.action = "<%=path%>/common/MenuShow/menuDisplay.do?deptId="+i+"&poseId="+j+"&poseType="+poseType+"&poseBusType="+poseBusType;
		fm.submit();
	}
	function goOut() {
		window.location.href = "<%=path%>/common/UserManager/logout.do";
	}

	$(function() {
		var body = $('.user-panel-body');

		if ( $('#fm').height() < 300 ) {
			body.css('height', 'auto');
		}
	});
</script>
</body>
</html>