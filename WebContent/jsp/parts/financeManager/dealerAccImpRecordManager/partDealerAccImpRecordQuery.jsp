<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c"%>
<%@ page import="com.infodms.dms.common.Constant"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<%
    String contextPath = request.getContextPath();
%>
<title>资金导入历史查询</title>
<script type="text/javascript">
$(function(){
	__extQuery__(1);
	
});

var myPage;

var url = "<%=contextPath%>/parts/financeManager/dealerAccImpRecordManager/partDealerAccImpRecAction/partDealerAccImpRecSearch.json";
	
	var title = null;

	var columns = [
				{header: "序号", dataIndex: 'HISTRORY_ID',renderer:getIndex},
				{header: "服务商编码", dataIndex: 'CHILDORG_CODE',style:'text-align:left; padding-left:1%;'},
				{header: "服务商名称", dataIndex: 'CHILDORG_NAME',style:'text-align:left; padding-left:2%;' },
				{header: "资金类型", dataIndex: 'ACCOUNT_KIND',renderer:getItemValue,style:'text-align:left; padding-left:1%;'},
				{header: "导入类型", dataIndex: 'IMPORT_TYPE',renderer:importAmountType,style:'text-align:left; padding-left:1%;'},
				{header: "导入人", dataIndex: 'NAME',style:'text-align:left; padding-left:1%;'},
				{header: "导入日期", dataIndex: 'CREATE_DATE'},
				{header: "导入金额(元)", dataIndex: 'AMOUNT',style:'text-align:right; padding-right:2%;'} ,
				{header: "备注", dataIndex: 'REMARK',style:'text-align:left; padding-left:1%;'}
//				{id:'action',header: "操作",sortable: false,dataIndex: 'ID',renderer:myLink ,align:'center'}
		      ];
	     
	
//设置导入类型
function importAmountType(value,meta,record){
	var importType = record.data.IMPORT_TYPE;
	var fixcode = <%=Constant.FIXCODE_BALANCE_IMPORT%>;
	if(fixcode == importType){
		return String.format("余额导入");
	} else {
		return String.format("<font color = 'red' >发生额导入</font>");
	}
		
}


//选择服务商
function sel() {
	var parentOrgId = document.getElementById("parentOrgId").value;
       OpenHtmlWindow('<%=contextPath%>/parts/financeManager/dealerAccQueryManager/partDealerAccQueryAction/partDealerSelect.do?parentOrgId='+ parentOrgId, 700, 500);
	}

function setPartCode(dealerCodes) {
	document.getElementById("dealerCode").value = dealerCodes;
}

function getDate() {
	var dateS = "";
	var dateE = "";
	var myDate = new Date();
	var year = myDate.getFullYear(); //获取完整的年份(4位,1970-????)
	var moth = myDate.getMonth(); //获取当前月份(0-11,0代表1月)
	if (moth < 10) {
		if (0 < moth) {
			moth = "0" + moth;
		} else {
			year = myDate.getFullYear() - 1;
			moth = moth + 12;
			if (moth < 10) {
				moth = "0" + moth;
			}
		}
	}
	var day = myDate.getDate(); //获取当前日(1-31)
	if (day < 10) {
		day = "0" + day;
	}

	dateS = year + "-" + moth + "-" + day;

	moth = myDate.getMonth() + 1; //获取当前月份(0-11,0代表1月)
	if (moth < 10) {
		moth = "0" + moth;
	}

	dateE = myDate.getFullYear() + "-" + moth + "-" + day;

	document.getElementById("checkSDate").value = dateS;
	document.getElementById("checkEDate").value = dateE;
}
</script>

</head>
<body>
	<div class="wbox">
		<form method="post" name="fm" id="fm">
			<div class="navigation">
				<img src="<%=request.getContextPath()%>/img/nav.gif" />&nbsp;当前位置： 配件财务管理 &gt; 资金导入历史查询
				<input type="hidden" name="parentOrgId" id="parentOrgId" value="${parentOrgId }" />
				<input type="hidden" name="parentOrgCode" id="parentOrgCode" value="${parentOrgCode }" />
			</div>
			<div class="form-panel">
				<h2>
					<img src="<%=contextPath%>/jmstyle/img/search-ico.png" style="margin-bottom: -13px;" />查询条件
				</h2>
				<div class="form-body">
					<table class="table_query">
						<tr>
							<td class="right">服务商编码：</td>
							<td>
								<input class="middle_txt" type="text" name="dealerCode" id="dealerCode" />
							</td>
							<td class="right">导入日期：</td>
							<td>
								<input id="checkSDate" style="width: 80px;"  class="short_txt" name="checkSDate" datatype="1,is_date,10" maxlength="10" group="checkSDate,checkEDate" />
								<input class="time_ico" onclick="showcalendar(event, 'checkSDate', false);" value=" " type="button" />
								&nbsp;至&nbsp;
								<input id="checkEDate" style="width: 80px;"  class="short_txt" name="checkEDate" datatype="1,is_date,10" maxlength="10" group="checkSDate,checkEDate" />
								<input class="time_ico" onclick="showcalendar(event, 'checkEDate', false);" value=" " type="button" />
							</td>
							<td class="right">资金类型：</td>
							<td>
								<script type="text/javascript">
									genSelBoxExp("accountKind",
								<%=Constant.FIXCODE_CURRENCY%>
									,
								<%=Constant.FIXCODE_CURRENCY_01%>
									,
											true, "",
											"onchange=__extQuery__(1)",
											"false", "");
								</script>
							</td>
						</tr>
						<tr>
							<td class="right">备注：</td>
							<td>
								<input class="middle_txt" type="text" name="remark" id="remark" />
							</td>
							<td class="right">导入类型：</td>
							<td colspan="3">
								<select name="importType" class="u-select" style="width: 152px;" onchange="__extQuery__(1)">
									<option value="">-请选择-</option>
									<option value="<%=Constant.FIXCODE_BALANCE_IMPORT%>">余额导入
										</opt	ion>
									<option value="<%=Constant.FIXCODE_CHANGE_BALANCE_IMPORT%>">发生额导入</option>
								</select>
							</td>
						</tr>
						<tr>
							<td class="center" colspan="6">
								<input class="u-button" type="button" value="查 询" name="BtnQuery" id="queryBtn" onclick="__extQuery__(1)" />
								<input class="u-button" type="reset" value="重 置" />
						</td>
						</tr>
					</table>
				</div>
			</div>

			<!-- 查询条件 end -->
			<!--分页 begin -->
			<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
			<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
			<!--分页 end -->
		</form>
	</div>
</body>
</html>