<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<%
    String contextPath = request.getContextPath();
%>
<title>配件打款等级</title>
<script type="text/javascript">
$(function(){
	__extQuery__(1);
});
var myPage;
var url = "<%=contextPath%>/parts/financeManager/dealerAccImpRecordManager/partDealerAccImpRecAction/partDealerAccQueryQuery.json";
var title = null;

var columns = [
    {header: "序号", dataIndex: 'HISTRORY_ID', renderer: getIndex},
    {id: 'action', header: "操作", sortable: false, dataIndex: 'HISTRORY_ID', renderer: myLink, align: 'center'},
    {header: "服务商编码", dataIndex: 'CHILDORG_CODE'},
    {header: "服务商名称", dataIndex: 'CHILDORG_NAME'},
    {header: "款项类型", dataIndex: 'ACCOUNT_PURPOSE', renderer: getItemValue, style: 'text-align:left; padding-left:1%;'},
    {header: "打款人", dataIndex: 'NAME'},
    {header: "打款日期", dataIndex: 'DK_DATE'},
    {header: "汇入银行", dataIndex: 'BANK_NAME'},
    {header: "打款金额(元)", dataIndex: 'AMOUNT'} ,
    {header: "流水号", dataIndex: 'PZ_NO'},
    {header: "备注", dataIndex: 'REMARK'},
    {header: "状态", dataIndex: 'STATUS2'}
];
function myLink(value, meta, record) {
    var status = record.data.STATUS;
    if(status=="0" || status=="3"){//未提及或已驳回
        var a1 = String.format("<a href='#' onclick='toEdit("+ value +")'>[修改]</a>");
        var a2= String.format("<a href='#' onclick='toSubmit("+ value +")'>[提交]</a>");
        var a3 = String.format("<a href='#' onclick='toDelete("+ value +")'>[作废]</a>");
        return a1+a2+a3;
    }else {
        return "已提交";
    };
}
//编辑
function toEdit(value){
    window.location.href="<%=request.getContextPath()%>/parts/financeManager/dealerAccImpRecordManager/partDealerAccImpRecAction/partDealerAccQueryEdit.do?hid="+value;
}

//提交
function toSubmit(value){
    MyConfirm("提交后将不能修改，确认提交?",toSubmit1,[value]);

}
function toSubmit1(value){
    makeCall("<%=request.getContextPath()%>/parts/financeManager/dealerAccImpRecordManager/partDealerAccImpRecAction/partDealerAccQueryMng.json",showFunc,{hid:value,opType:'SUB'});
}
//删除
function toDelete(value){
    MyConfirm("作废后将不再显示，确认作废?",toDelete1,[value]);
}
function toDelete1(value){
    makeCall("<%=request.getContextPath()%>/parts/financeManager/dealerAccImpRecordManager/partDealerAccImpRecAction/partDealerAccQueryMng.json",showFunc,{hid:value,opType:'DEL'});
}
//新增
function Add(){
    window.location.href="<%=request.getContextPath()%>/parts/financeManager/dealerAccImpRecordManager/partDealerAccImpRecAction/partDealerAccQueryAdd.do";
}
function showFunc(json){
    MyAlert(json.ACTION_RESULT, function(){
    	__extQuery__(1);
	    // window.location.reload();
    });
}
</script>

</head>
<body>
	<div class="wbox">
		<form method="post" name="fm" id="fm">
			<div class="navigation">
				<img src="<%=request.getContextPath()%>/img/nav.gif" />&nbsp;当前位置： 配件管理 &gt; 配件财务管理 &gt; 配件打款登记
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
							<td class="right">打款日期：</td>
							<td>
								<input id="checkSDate" style="width: 80px;" class="short_txt" name="checkSDate" datatype="1,is_date,10" maxlength="10" group="checkSDate,checkEDate" />
								<input class="time_ico" onclick="showcalendar(event, 'checkSDate', false);" value=" " type="button" />
								&nbsp;至&nbsp;
								<input id="checkEDate" style="width: 80px;" class="short_txt" name="checkEDate" datatype="1,is_date,10" maxlength="10" group="checkSDate,checkEDate" />
								<input class="time_ico" onclick="showcalendar(event, 'checkEDate', false);" value=" " type="button" />
							</td>
							<td class="right">流水号：</td>
							<td>
								<input class="middle_txt" type="text" name="pz_No" id="pz_No" />
							</td>
							<td class="right">备注：</td>
							<td>
								<input class="middle_txt" type="text" name="remark" id="remark" />
							</td>
						</tr>
						<tr>
							<td class="center" colspan="6">
								<input class="u-button" type="button" value="查 询" name="BtnQuery" id="queryBtn" onclick="__extQuery__(1)" />
								<input class="u-button" type="reset" value="重 置">
								<input class="u-button" type="button" value="新 增" name="BtnAdd" id="BtnAdd" onclick="Add();" />
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