<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<%
	String contextPath = request.getContextPath();
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml"><%@ page import="com.infodms.dms.common.Constant"%><%@ page import="com.infodms.dms.common.FileConstant"%><%@taglib uri="/jstl/cout" prefix="c"%>
<head>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>地址查询</title>
</head>
<body>
<div class="wbox">
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置：采购管理 &gt; 库存管理 &gt; 地址查询</div>
<form id="fm" name="fm" method="post">
<input type="hidden" name="curPage" id="curPage" value="1" /> <input type="hidden" id="dlrId" name="dlrId" value="" />
<table class="table_query" border="0">
	<tr>
		<td class="tblopt">
		<div class="right">业务范围：</div>
		</td>
		<td>
			<select name="areaId" class="u-select">
				<option value="">-请选择-</option>
				<c:forEach items="${areaList}" var="po">
					<!--<option value="${po.AREA_ID}|${po.DEALER_ID}">${po.AREA_NAME}</option>-->
					<option value="${po.AREA_ID}">${po.AREA_NAME}</option>
				</c:forEach>
            </select>
		</td>
		<td class="tblopt">
			<div class="right">状态：</div>
		</td>
		<td>
			<select name="status" class="u-select">
				<option value="">-请选择-</option>
				<option value="10011001">有效</option>
				<option value="10011002">无效</option>
				<option value="13531002">待审核</option>
			    <option value="13531003">驳回</option>
			    <option value="13531005">初审完成</option>
			    <option value="13531006">待物流部审核</option>
 			</select>
		</td>
		<td></td>
	</tr>
	<tr>
		<td class="tblopt" nowrap="nowrap" >
			<div class="right">时限：</div>
		</td>
		<td>
				<script type="text/javascript">
					genSelBoxExp("limit",<%=Constant.ADDRESS_TIME_LIMIT%>,"${addressInfo.LIMIT_TYPE }",true,"u-select","onchange=setLimitTimeDisplay();","false",'');
				</script>
		</td>
		<td class="tblopt">
			<div class="right">地址名称：</div>
		</td>
		<td><input type="text" class="middle_txt" id="address" name="address" /></td>
		<td></td>
	</tr>
	<tr>
		<td class="tblopt" nowrap="nowrap" >
			<div class="right">选择经销商：</div>
		</td>
			<td>
				<input type="text" class="middle_txt" style="width:100px" name="dealerCode" readonly="readonly" size="15" value="" id="dealerCode"/>
				<input name="dealerBtn" type="button" class="mini_btn" onclick="showOrgDealer('dealerCode','dealerIds','true');" value="..." />
				<input type="hidden" name="dealerIds" id="dealerIds" value="" />
				<input class="normal_btn" type="button" value="清空" onclick="clrTxt('dealerCode');clrTxt('dealerIds');"/>
			</td>
			<td class="tblopt" nowrap="nowrap" >
				<div class="right">选择区域：</div>
			</td>
			<td >
				<input type="text" id="orgCode" style="width:100px" name="orgCode" value="" size="15" class="middle_txt" readonly="readonly" />
				<input name="obtn" id="obtn"  class="mini_btn" type="button" value="&hellip;" onclick="showOrg('orgCode','orgId' ,'true','${orgId}');"/>
				<input type="hidden" id="orgId" name="orgId" />
				<input class="normal_btn" type="button" value="清空" onclick="clrTxt('orgCode');clrTxt('orgId');" />
			</td>
		<td></td>
	</tr>
	<tr>
		<td></td>
		<td></td>
		<td></td>
		<td></td>
		<td class="table_query_3Col_input">
			<input type="hidden" id="areaIdStr" name="areaIdStr" value="${areaIdStr }" />
			<input type="button" class="normal_btn" onclick="__extQuery__(1);" value="查 询" id="queryBtn" />
		</td>
	</tr>
</table>
<jsp:include page="${contextPath}/queryPage/orderHidden.html" /> 
<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
</form>
</div>
<script type="text/javascript">
	var myPage;
	var url = "<%=contextPath%>/sales/storageManage/AddressAddQuery/oemAddressQuery.json?COMMAND=1";
	var title = null;
	var columns = [
					{header: "组织", dataIndex: 'ROOT_ORG_NAME', align:'center'},
					{header: "所属单位", dataIndex: 'DEALER_UNIT', align:'center'},
					{header: "地址代码", dataIndex: 'ADD_CODE', align:'center'},
		            {header: "地址名称", dataIndex: 'ADDRESS', align:'center'},
		            {header: "限时类型", dataIndex: 'LIMIT_TYPE', align:'center',renderer:getItemValue},
					{header: "有效日期", dataIndex: 'START_TIME', align:'center',renderer:getLimitType},
					{header: "地址用途", dataIndex: 'ADDRESS_USE', align:'center'},
					{header: "联系人", dataIndex: 'LINK_MAN', align:'center'},
					{header: "电话", dataIndex: 'TEL', align:'center'},
					{header: "收货单位", dataIndex: 'RECEIVE_ORG', align:'center'},
					{header: "备注", dataIndex: 'REMARK', align:'center'},
					{header: "业务范围", dataIndex: 'AREA_NAME', align:'center'},
					{header: "状态", dataIndex: 'STATUS', align:'center',renderer:getItemValue},
					{header: "初审人", dataIndex: 'LOW_AUDIT', align:'center'},
					{header: "初审日期", dataIndex: 'LOW_CHECK_DATE', align:'center'},
					{header: "总部审核人", dataIndex: 'FACTORY_AUDIT', align:'center'},
					{header: "总部审核日期", dataIndex: 'FACTORY_CHECK_DATE', align:'center'},
					{header: "操作", dataIndex: 'ID', align:'center',renderer:itLink}
			      ];
	
	function getLimitType(value,metaDate,record) {
		var sEndTime = record.data.END_TIME ;
		var sLimitType = record.data.LIMIT_TYPE ;
		var sReturnValue = "-" ;
		
		if(sLimitType == <%=Constant.ADDRESS_TIME_LIMIT_PERP %>) {
			sReturnValue = "-" ;
		} else if(sLimitType == <%=Constant.ADDRESS_TIME_LIMIT_TEMP %>) {
			sReturnValue = value + "至" + sEndTime ;
		}
		
		return String.format(sReturnValue);
	}
	
	function itLink(value,metaDate,record) {
		var url = "<%=contextPath%>/sales/storageManage/DealerAddressCheck/addressDtlQuery.do?id=" + value ;
		var link = "<a href=" + url + ">[明细]</a>" ;
		return String.format(link);
	}
	
	function clrTxt(txtId){
    	document.getElementById(txtId).value = "";
    }
</script>
</body>
</html>