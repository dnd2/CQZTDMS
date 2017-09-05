<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<%
	String contextPath = request.getContextPath();
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml"><%@ page import="com.infodms.dms.common.Constant"%><%@ page import="com.infodms.dms.common.FileConstant"%><%@taglib uri="/jstl/cout" prefix="c"%>
<head>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>经销商地址更改查询</title>
</head>
<body>
<div class="wbox">
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置：采购管理 &gt; 库存管理 &gt; 经销商地址更改查询</div>
<form id="fm" name="fm" method="post">
<input type="hidden" name="curPage" id="curPage" value="1" /> <input type="hidden" id="dlrId" name="dlrId" value="" />
<table class="table_query" border="0">
	<c:if test="${isFlag == 0 }">
		<tr>
				<td colspan="3">
					<strong>
						<font color="red">
<pre>发运地址审核流程:
	经销商发起申请C1,C2.C3---事业部分管销售经理审核----各基地业务人员审核---地址审核完成</pre>
						</font>
					</strong>
				</td>
		</tr>
	</c:if>
	<c:if test="${isFlag == 1 }">
		<tr>
				<td colspan="3">
					<strong>
						<font color="red">
<pre>发运地址审核流程:
	初审：各大区销售主管，复审：渠道部A网游默，B网熊英，其他姜磊</pre>
						</font>
					</strong>
				</td>
		</tr>
	</c:if>
	<tr>
		<td width="20%" class="tblopt">
		<div class="right">业务范围：</div>
		</td>
		<td>
			<select name="areaId" class="u-select">
				<option value="">-请选择-</option>
				<c:forEach items="${areaList}" var="po">
					<option value="${po.AREA_ID}|${po.DEALER_ID}">${po.AREA_NAME}</option>-->
					<!--<option value="${po.AREA_ID}">${po.AREA_NAME}</option>-->
				</c:forEach>
            </select>
		</td>
		<td></td>
	</tr>
	<tr>
		<td width="20%" class="tblopt">
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
		<td width="20%" class="tblopt">
		<div class="right">地址名称：</div>
		</td>
		<td width="39%"><input type="text" class="middle_txt" id="address" name="address" /></td>
		<td class="table_query_3Col_input">
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
	var url = "<%=contextPath%>/sales/storageManage/AddressAddQuery/changeList.json?COMMAND=1";
	var title = null;
	var columns = [
		            {header: "地址代码", dataIndex: 'ADD_CODE', align:'center'},
		            {header: "地址名称", dataIndex: 'ADDRESS', align:'center'},
		            {header: "限时类型", dataIndex: 'LIMIT_TYPE', align:'center',renderer:getItemValue},
					{header: "有效日期", dataIndex: 'START_TIME', align:'center',renderer:getLimitType},
					{header: "地址用途", dataIndex: 'ADDRESS_USE', align:'center'},
					{header: "联系人", dataIndex: 'LINK_MAN', align:'center'},
					{header: "电话", dataIndex: 'TEL', align:'center'},
					{header: "收货单位", dataIndex: 'RECEIVE_ORG', align:'center'},
					{header: "业务范围", dataIndex: 'AREA_NAME', align:'center'},
					{header: "备注", dataIndex: 'REMARK', align:'center'},
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
</script>
</body>
</html>