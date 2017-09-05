<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.Constant" %>
<%@ page import="com.infodms.dms.common.FileConstant"%>
<%@taglib uri="/jstl/cout" prefix="c" %>
<%
	String contextPath = request.getContextPath();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta http-equiv="X-UA-Compatible" content="IE=7" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />	
<title>供应商信息查看</title>
<script type="text/javascript">
//autoAlertException();//输出错误信息
// var myPage;

<%-- var url = "<%=contextPath%>/parts/baseManager/partsBaseManager/PartVenderManager/queryPartMakerInfo.json"; --%>
			
// var title = null;

// var columns = [
// 				{header: "序号",width:'10%',  renderer:getIndex},
// 				{header: "制造商编码", dataIndex: 'MAKER_CODE', align:'center'},
// 				{header: "制造商名称", dataIndex: 'MAKER_NAME', align:'center'},
// 				{header: "联系人", dataIndex: 'LINKMAN', align:'center'},
// 				{header: "联系电话", dataIndex: 'TEL', align:'center'},
// 				{header: "是否有效", dataIndex: 'STATE', align:'center',renderer:getItemValue}
// 	      ];

function goback(){
	window.location.href = '<%=contextPath%>/parts/baseManager/partsBaseManager/PartVenderManager/queryPartVenderInit.do';
}
</script>
</head>
<body> <!-- onunload='javascript:destoryPrototype()' This method is not defined -->
<div class="wbox">
	<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置：配件管理 &gt;
	基础信息管理 &gt; 配件基础信息维护 &gt; 供应商信息维护 &gt; 查看 </div>
	<form id="fm" name="fm" method="post">
	<input type="hidden" id="venderId" name="venderId" value="${venderInfo.VENDER_ID}"/>
	<div class="form-panel">	
		<h2><img class="panel-icon nav" src="<%=contextPath%>/img/subNav.gif" /> 供应商信息</h2>
		<div class="form-body">
			<table class="table_query">
			<tr>
				<td class="right">供应商编码：</td>
				<td >
					<input class="middle_txt input-disabled" type="text" name="VENDER_CODE" id="code" value="${venderInfo.VENDER_CODE}" disabled="disabled"/>
				</td>
				<td class="right">供应商名称：</td>
				<td><input type="text" class="middle_txt input-disabled input-medium" name="VENDER_NAME" id="name" value="${venderInfo.VENDER_NAME}" disabled="disabled"/>
				</td>
				<td class="right"  >国内/国外：</td>
				<td>
					<script type="text/javascript">
						genSelBox("IS_ABROAD",<%=Constant.IS_ABROAD_V%>,'${venderInfo.IS_ABROAD}',false,'u-select input-disabled','disabled="disabled"',"");
					</script>
				</td>	
			</tr>
			<tr>
				<td class="right">联系人：</td>
				<td><input class="middle_txt input-disabled" type="text" name="LINKMAN" value="${venderInfo.LINKMAN}" disabled="disabled"/></td>
				<td class="right">联系电话：</td>
				<td>
					<input type="text" class="middle_txt input-disabled" name="TEL" value="${venderInfo.TEL}" disabled="disabled"/>
				</td>
				<td class="right">传真：</td>
				<td>
					<input type="text" class="middle_txt input-disabled" name="FAX" value="${venderInfo.FAX}" disabled="disabled"/>
				</td>
			</tr>
				<tr>
				<td class="right">供应商类型：</td>
				<td>
				<script type="text/javascript">
						genSelBox("VENDER_TYPE",<%=Constant.VENDER_TYPE%>, '${venderInfo.VENDER_TYPE}',false,'u-select input-disabled','disabled="disabled"',"");
				</script>
				</td>
				<td class="right" nowrap="nowrap">开票类型：</td>
				<td>
					<script type="text/javascript">
					genSelBox("dlrInvTpe",<%=Constant.DLR_INVOICE_TYPE%>,'${venderInfo.INV_TYPE}',false,'u-select input-disabled','disabled="disabled"',"");
					</script>
				</td>
				<td class="right">是否有效：</td>
				<td>
				<script type="text/javascript">
				genSelBox("STATE",<%=Constant.STATUS%>,'${venderInfo.STATE}',false,'u-select input-disabled','disabled="disabled"',"");
				</script>
			</td>
			</tr>
			<tr>
				<td class="right">地址：</td>
				<td colspan="5"><input class="middle_txt input-long-txt input-disabled" type="text" name="ADDR" value="${venderInfo.ADDR}" disabled="disabled"/></td>
				</tr>
			</table>
			<table class="table_edit tb-button-set">
			<tr>
				<td align="center">
					<input type="button" name="saveBtn" id="saveBtn" value="返回" onclick="goback();"  class="u-button"/>
				</td>
			</tr>
		</table>
		</div>
	</div>
	
<!--   <table class="table_edit"> -->
<%-- 		<th colspan="6"><img class="nav" src="<%=contextPath%>/img/subNav.gif" /> 制造商信息</th> --%>
<!--   </table> -->
<%--   <jsp:include page="${contextPath}/queryPage/orderHidden.html" /> --%>
<%--   <jsp:include page="${contextPath}/queryPage/pageDiv.html" /> --%>
  </form>
</div>
</body>
</html>
