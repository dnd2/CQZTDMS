<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.Constant"%>
<%@taglib uri="/jstl/cout" prefix="c"%>
<% 
	String contextPath = request.getContextPath(); 
%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />

	<title>库存调整审核</title>
	<script language="javascript" type="text/javascript">
		$(function(){
			__extQuery__(1);
		});
		
		var myPage;
		var url = "<%=contextPath%>/parts/storageManager/stockNumManager/StockNumChkAction/queryStockNumCheck.json";
		var title = null;
		
		var columns = [
					{header: "序号", dataIndex: 'ABJUSTMENT_ID', renderer:getIndex,align:'center'},
					{id:'action',header: "操作",sortable: false,dataIndex: 'ID',renderer:myLink ,align:'center'},
					{header: "调整单号", dataIndex: 'ABJUSTMENT_CODE', align:'center'},
					{header: "调整类型", dataIndex: 'ABJUSTMENT_TYPE', align:'center',renderer:getItemValue},
					{header: "调整仓库", dataIndex: 'WH_CNAME', align:'center'},
					{header: "申请人", dataIndex: 'APPLY_NAME', align:'center'},
					{header: "申请时间", dataIndex: 'APPLY_DATE', align:'center'},
					{header: "调整状态", dataIndex: 'STATE', align:'center',renderer:getItemValue},
					{header: "审核状态", dataIndex: 'CHECK_STATE', align:'center', renderer: getItemValue},
					{header: "状态", dataIndex: 'STATUS', align:'center',renderer:getItemValue},
					{header: "创建日期", dataIndex: 'CREATE_DATE', align:'center'},
			      ];
		
		//设置超链接
		function myLink(value,meta,record)
		{
			var id = record.data.ABJUSTMENT_ID;
			var checkState = record.data.CHECK_STATE;
			var status = record.data.STATUS;
			var html = "<a href=\"#\" onclick='viewDetail(\""+id+"\")'>[查看]</a>&nbsp;";
			if(status == <%=Constant.STATUS_ENABLE%>){
				if(checkState == <%=Constant.PART_ABJUSTMENT_CHECK_01%>){
					html += "<a href=\"#\" onclick='checkApply(\""+id+"\")'>[审核]</a>&nbsp;";
				}
			}
			return String.format(html);
		}
		
		//查看
		function viewDetail(parms){
			document.fm.action="<%=contextPath%>/parts/storageManager/stockNumManager/StockNumChkAction/toAbjustmentDetail.do?abjustmentId=" + parms +"&actionType=view";
			document.fm.target="_self";
			document.fm.submit();
		}
		
		//修改
		function checkApply(parms){
			document.fm.action="<%=contextPath%>/parts/storageManager/stockNumManager/StockNumChkAction/toAbjustmentDetail.do?abjustmentId=" + parms +"&actionType=check";
			document.fm.target="_self";
			document.fm.submit();
		}
	</script>
</head>
<body>
	<div class="wbox">
		<div class="navigation">
			<img src="<%=request.getContextPath()%>/img/nav.gif" /> &nbsp;当前位置：
			配件仓库管理 &gt;库存调整管理&gt;库存调整申请
		</div>
		<form method="post" name="fm" id="fm">
			<div class="form-panel">
				<h2><img src="<%=contextPath%>/jmstyle/img/search-ico.png" style="margin-bottom: -13px;"/>查询条件</h2>
				<div class="form-body">
					<table class="table_query">
						<tr>
							<td class="right">申请单号：</td>
							<td>
								<input class="middle_txt" type="text" name="abjustmentCode" id="abjustmentCode" />
							</td>
							<td class="right">申请日期：</td>
							<td>
								<input id="applySDate" class="short_txt" style="width: 80px;" name="applySDate" datatype="1,is_date,10" maxlength="10" group="applySDate, applyEDate" />
								<input class="time_ico" onclick="showcalendar(event, 'applySDate', false);" value=" " type="button" />
								至 
								<input id="applyEDate" class="short_txt" style="width: 80px;" name="applyEDate" datatype="1,is_date,10" maxlength="10" group="applyEDate, applyEDate" />
								<input class="time_ico" onclick="showcalendar(event, 'applyEDate', false);" value=" " type="button" />
							</td>
							<td class="right">申请人：</td>
							<td>
								<input type="text" class="middle_txt" name="applyName" id="applyName">
							</td>
						</tr>
						<tr>
							<td class="right">调整仓库：</td>
							<td>
								<select name="whId" id="whId" class="u-select">
									<option value="">-请选择-</option>
									<c:if test="${WHList!=null}">
										<c:forEach items="${WHList}" var="list">
											<option value="${list.WH_ID }">${list.WH_CNAME }</option>
										</c:forEach>
									</c:if>
								</select>
							</td>
							<td class="right">调整类型：</td>
							<td>
								<script type="text/javascript">
							        genSelBoxExp("abjustmentType",<%=Constant.PART_ABJUSTMENT_TYPE%>,"",true,"","","false","");
								</script>
					  		</td>
							<td class="right">调整状态：</td>
							<td>
								<script type="text/javascript">
						        	genSelBoxExp("state",<%=Constant.PART_ABJUSTMENT_STATE%>,'<%=Constant.PART_ABJUSTMENT_STATE_02%>',true,"","","false","");
							  	</script>
					  		</td>
						</tr>
						<tr>
							<td colspan="6" class="center">
								<input class="u-button" type="button" value="查 询" name="BtnQuery" id="queryBtn" onclick="__extQuery__(1)" /> 
								<input class="u-button" type="reset" value="重 置" />
							</td>
						</tr>
					</table>
				</div>
			</div>
			<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
			<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
		</form>
	</div>
</body>
</html>