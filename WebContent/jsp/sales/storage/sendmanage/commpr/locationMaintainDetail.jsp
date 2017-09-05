<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml"><%@ page import="com.infodms.dms.common.Constant"%><%@ page import="com.infodms.dms.common.FileConstant"%><%@taglib uri="/jstl/cout" prefix="c"%>
<%@ page import="com.infodms.dms.common.Constant" %>
<%@taglib uri="/jstl/cout" prefix="c" %>
<%@taglib uri="/jstl/fmt" prefix="fmt" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>在途位置维护</title>
</head>
<body >
<div class="navigation"><img src="<%=request.getContextPath()%>/img/nav.gif" />&nbsp;当前位置：储运管理>发运管理>在途位置维护</div>
<form method="POST" name="fm" id="fm">
	<table class="table_query">
			<tr>
        		<th nowrap="" align="left" colspan="6"><img src="<%=request.getContextPath()%>/img/subNav.gif" class="nav"> 发运单信息</th>
            </tr>
			<tr>
				<td class="right">承运商名称：</td>
				<td align="left">${logiName}</td>
				<td class="right">发运单号：</td>
				<td align="left">${billNo}</td>
				<td class="right">发运时间：</td>
				<td align="left">${billCrtDate}</td>
			</tr>
	</table>
	<br/>
	<table class=table_list style="border-bottom:1px solid #DAE0EE" >  
		<tr class=cssTable >
			<th nowrap="nowrap">车系</th>
			<th nowrap="nowrap">车型配置</th>
			<th nowrap="nowrap">VIN</th>
			<th nowrap="nowrap">发运申请号</th>
<!-- 			<th nowrap="nowrap">生成发运申请时间</th> -->
			<th nowrap="nowrap">发运地址</th>
		</tr>
		<c:forEach items="${list}" var="po" varStatus="i">
    		<tr class="table_list_row2">
		      <td align="center">${po.SERIES_NAME}</td>
		      <td align="center">${po.PACKAGE_NAME}</td>
		      <td align="center">
		         <a href="#" onclick="showlog(${po.DTL_ID});">${po.VIN}</a>
		      </td>
		      <td align="center">${po.ORDER_NO}</td>
<%-- 		      <td align="center">${po.SO_DATE}</td> --%>
		      <td align="center">${po.ADDRESS}</td>
		    </tr>
    	</c:forEach>
	</table>
	<br />
	<table class=table_query>
		<tr>
        	<th nowrap="" align="left" colspan="6"><img src="<%=request.getContextPath()%>/img/subNav.gif" class="nav"> 物流信息维护</th>
        </tr>
		<tr class=cssTable>
			<td width="8%" class="right" nowrap="nowrap">车牌号：</td>
			<td width="50%" align="left">
				${chepaiNo}
			</td>
			<td width="8%" class="right">司机：</td>
			<td width="50%" align="left">${siji}</td>
			<td width="8%" class="right" nowrap="nowrap">联系方式：</td>
			<td width="50%" align="left">
				${tel}
			</td>
		</tr>
	</table>
	<br/>
<%-- 	<table class=table_list style="border-bottom:1px solid #DAE0EE" >  
		<tr>
        	<th nowrap="" align="left" colspan="6"><img src="<%=request.getContextPath()%>/img/subNav.gif" class="nav"> 位置信息</th>
        </tr>
		<tr class=cssTable >
			<th nowrap="nowrap">序号</th>
			<th nowrap="nowrap">日期</th>
			<th nowrap="nowrap">位置</th>
			<!-- <th nowrap="nowrap">操作</th> -->
		</tr>
    	<c:forEach items="${attachList}" var="checkList">
    		<tr class="table_list_row2">
    			<td align="center">${index+1}</td>
		      	<td align="center">${checkList.ZT_DATE}</td>
	          	<td align="center"><c:out value="${checkList.ZT_ADDRESS}"/></td>
	          	<!-- <td align="center">--</td>  -->
	          </tr>
    	</c:forEach>
	</table> --%>
<!-- 	<br/> -->
	<table class=table_query>
		<tr class=cssTable >
			<td colspan="6" class="table_query_4Col_input" style="text-align: center">
				<input type="button" name="button3" class="normal_btn" onclick="toBack(${COMMO });" value="返回" id="queryBtn3" /> 
			</td>
		</tr>
	</table>
</form>
<script type="text/javascript">
//初始化    
function doInit(){
	  //初始化时间控件
}
//返回
function toBack(vp){
	if(vp==2){
		window.location.href = '<%=request.getContextPath()%>/sales/storage/sendmanage/CommprManage/locationMaintainQueryInitDRL.do';
	}else if(vp==1){
		window.location.href = '<%=request.getContextPath()%>/sales/storage/sendmanage/CommprManage/locationMaintainQueryInit.do';
	}else {
		window.location.href = '<%=request.getContextPath()%>/sales/storage/sendmanage/CommprManage/dLocationMaintainQueryInit.do';
	}
	
}
function showlog(dtl_id){
	OpenHtmlWindow('<%=request.getContextPath()%>/sales/storage/sendmanage/OnTheWayAction/showbindCarlog.do?dtl_id='+dtl_id,800,300);
}
</script>
</body>
</html>