<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="com.infodms.dms.common.Constant"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.Map"%>
<%@taglib uri="/jstl/fmt" prefix="fmt"%>
<%@taglib uri="/jstl/cout" prefix="c"%>
<%
	String contextPath = request.getContextPath();
%>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<script type="text/javascript">
	//获取选择框的值
	function getCode(value){
		var str = getItemValue(value);
		document.write(str);
	}
	//获取序号
	function getIdx(){
		document.write(document.getElementById("file").rows.length-2);
	}
	//获取序号
	function getIdx1(){
		document.write(document.getElementById("file2").rows.length-2);
	}
	//返回
	function goBack(){
		window.location.href = '<%=contextPath%>/parts/salesManager/carFactorySalesManager/PartReplacedDlrOrder/partDlrOrderCheckInit.do?flag=true';
	}
	//zhumingwei 2013-09-16
	//关闭
	function goClose(){
		_hide();
	}
	function doInit() {
		loadcalendar();
	}
	function query() {
		var url = "<%=contextPath%>/sales/rebatemanage/Rebate/dealerFinReturnDtlQuery.do";
		fm.action =url ;
		fm.submit();
	}
	function exportDetl()
	{
		document.fm.action="<%=contextPath%>/sales/rebatemanage/Rebate/exportOrderExcel1.do";
		document.fm.target="_self";
		document.fm.submit();
	}
	function detail(value,ver) {
		OpenHtmlWindow('<%=contextPath%>/sysmng/orgmng/DlrInfoMng/detailInit.do?COMPANY_ID='+value+'&ver='+ver,800,550);
    }
</script>
</head>

<body>
	<form name="fm" id="fm" method="post" enctype="multipart/form-data">
		<input type="hidden" name="STATE" id="STATE" value="${mainMap.STATE}" />
		<input type="hidden" name="dealerId" id="dealerId" value="${dealerId}" />
		<input type="hidden" name="orderCode" id="orderCode"
			value="${mainMap.ORDER_CODE}" />
		<div class="wbox">
			<div class="navigation">
				<img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置 ：系统管理  &gt; 组织管理  &gt; 经销商公司更名 &gt; 更名历史
			</div>
			<table class="table_query" bordercolor="#DAE0EE">

	  </table><c:if test="${length!=0}">
			<table id="file" class="table_list"
				style="border-bottom: 1px solid #DAE0EE">
				<tr>
					<th colspan="12" align="left"><img class="nav"
						src="<%=contextPath%>/img/subNav.gif" />经销商公司更名历史</th>
				</tr>
				<tr bgcolor="#FFFFCC">
					<td align="center" width="3%">序号</td>
					<td align="center" width="3%">操作</td>
					<td align="center" width="5%">状态</td>
					<td align="center" width="10%">更名前公司名称</td>
					<td align="center" width="10%">更名后公司名称</td>
					<td align="center" width="8%">公司代码</td>
<!-- 					<td align="center" width="8%">更名前公司简称</td> -->
<!-- 					<td align="center" width="8%">更名前后公司简称</td> -->
					<td align="center" width="8%">创建人</td>
					<td align="center" width="8%">版本号</td>
					<td align="center" width="8%">备注</td>

				</tr>
				<c:forEach items="${Dtl}" var="data">
					<tr class="table_list_row1">
						<td align="center"><script type="text/javascript">
       				getIdx();
				</script></td>
				<td><INPUT onclick="detail('${data.companyId}','${data.ver}')" class="long_btn" type="button" value="查看修改明细"></td>
						<td align="left"><c:out value="${data.status1}" /></td>
						<td align="left"><c:out value="${data.oldCompanyName}" /></td>
						<td align="left"><c:out value="${data.newCompanyName}" /></td>
						<td align="left">${data.companyCode}</td>
<%-- 						<td align="left"><c:out value="${data.oldCompanyShortname}" /></td> --%>
<%-- 						<td align="left">${data.newCompanyShortname}</td> --%>
						<td align="left"><c:out value="${data.createBy1}" /> </td>
						<td align="center">${data.ver}</td>
						<td align="center">${data.remark}</td>
					</tr>
				</c:forEach>
<!-- 				<tr bgcolor="#FFFFCC"> -->
<!-- 					<td align="center" width="3%">备注</td> -->
<!-- 					<td ><textarea></textarea></td> -->
<!-- 				</tr> -->
			</table></c:if>
			<c:if test="${length==0}">
				<div class='pageTips'>没有满足条件的数据</div></c:if>
			<table border="0" width="100%">
				<tr align="center" width="100%">
					<td>
<!-- 						<input class="normal_btn" type="button" value="查询"onclick="query()" />  -->
<%-- 						<c:if test="${length!=0}"> --%>
<!-- 						<input class="cssbutton" type="button" value="导出" name="BtnQuery" id="queryBtn"  onclick="exportDetl();"> -->
<%-- 						</c:if> --%>
						<input class="normal_btn" type="button"
						value="关 闭" onclick="goClose()" />
						</td>
				</tr>
			</table>
		</div>
	</form>
</body>
</html>

