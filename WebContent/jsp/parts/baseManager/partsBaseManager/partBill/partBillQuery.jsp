<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.infodms.dms.common.Constant"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%
    String contextPath = request.getContextPath();
			String curPage = (String) request.getAttribute("curPage");
%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>经销商开票信息查询</title>
<script type="text/javascript" >
$(function(){
	__extQuery__(1);
});
   
var myPage;
var url = "<%=contextPath%>/parts/baseManager/partsBaseManager/PartBillManager/queryPartBillInfo.json";
var curPage = <%=curPage%>;
if(curPage){
	url = "<%=contextPath%>/parts/baseManager/partsBaseManager/PartBillManager/queryPartBillInfo.json?curPage="+curPage;
}
			
var title = null;

var columns = [
            {header: "序号",width:'5%',  renderer:getIndex},
               {id:'action',header: "操作",sortable: false,dataIndex: 'BILL_ID',renderer:myLink , style: 'text-align: center'},
			{header: "服务商编码", dataIndex: 'DEALER_CODE',  style: 'text-align: center'},
			{header: "服务商名称", dataIndex: 'DEALER_NAME',  style: 'text-align: center'},
			{header: "开票名称", dataIndex: 'TAX_NAME',  style: 'text-align: center'},
			{header: "开票类型", dataIndex: 'INV_TYPE',  style: 'text-align: center',renderer:getItemValue},
			{header: "纳税人识别号", dataIndex: 'TAX_NO',  style: 'text-align: center'},
			{header: "地址", dataIndex: 'ADDR',  style: 'text-align: center'},
			{header: "发票邮寄地址", dataIndex: 'MAIL_ADDR',  style: 'text-align: center'},
			{header: "电话", width:'10%',dataIndex: 'TEL',  style: 'text-align: center'},
			{header: "开户行", dataIndex: 'BANK',  style: 'text-align: center'},
			{header: "账号", dataIndex: 'ACCOUNT',  style: 'text-align: center'},
	/* 		{header: "备注", dataIndex: 'REMARK',  style: 'text-align: center'},
			{header: "创建日期", dataIndex: 'CREATE_DATE',  style: 'text-align: center'},
			{header: "修改日期", dataIndex: 'UPDATE_DATE',  style: 'text-align: center'}, */
			{header: "是否有效", dataIndex: 'STATE',  style: 'text-align: center',renderer:getItemValue}

	      ];


//设置超链接
function myLink(value,meta,record)
{   
	
       var state = record.data.STATE;
       if(state==<%=Constant.STATUS_DISABLE %>){
           return String.format("<a href=\"#\" onclick='sel(\""+value+"\")' id='sel'>[有效]</a>");
       }
       
 		return String.format("<a href=\"#\" onclick='updateBill(\""+value+"\")'>[修改]</a><a href=\"#\" onclick='cel(\""+value+"\")'>[失效]</a>");
}

//新增
function partBillAdd(){
     window.location.href='<%=contextPath%>/parts/baseManager/partsBaseManager/PartBillManager/addPartBillInit.do';
}

   //修改
function updateBill(value){ 
	window.location.href='<%=contextPath%>/parts/baseManager/partsBaseManager/PartBillManager/queryPartBillDetail.do?billId='+value+'&curPage='+myPage.page;
}

//有效
function sel(value){
	 MyConfirm("确定要设置为有效?",validAction,[value]);
}

//失效
function cel(value){
	 MyConfirm("确定要失效?",celAction,[value]);
}

function celAction(value){
	 var url = '<%=contextPath%>/parts/baseManager/partsBaseManager/PartBillManager/celPartBill.json?billId='+value+'&curPage='+myPage.page;
     makeNomalFormCall(url,handleControl,'fm');
}

function validAction(value){
	 var url = '<%=contextPath%>/parts/baseManager/partsBaseManager/PartBillManager/selPartBill.json?billId='+value+'&curPage='+myPage.page;
     makeNomalFormCall(url,handleControl,'fm');
}

function handleControl(jsonObj) {
	 if(jsonObj!=null){
	     var success = jsonObj.success;
	     MyAlert(success);
	      __extQuery__(jsonObj.curPage);
	  }
}

//下载配件服务商开票信息数据
function exportPartBillExcel(){
	fm.action= "<%=contextPath%>/parts/baseManager/partsBaseManager/PartBillManager/exportPartBillExcel.do";
	fm.target="_self";
	fm.submit();
}
</script>
</head>
<body>
	<div class="wbox">
		<div class="navigation">
			<img src="<%=request.getContextPath()%>/img/nav.gif" />&nbsp;当前位置： 配件管理 &gt; 基础信息管理 &gt; 配件基础信息维护 &gt; 经销商商开票信息维护
		</div>
		<form method="post" name="fm" id="fm" enctype="multipart/form-data">
			<input type="hidden" name="curPage" id="curPage" />
			<input type="hidden" name="partId" id="partId" />
			<div class="form-panel">
				<h2>
					<img src="<%=contextPath%>/jmstyle/img/search-ico.png" style="margin-bottom: -13px;" />查询条件
				</h2>
				<div class="form-body">
					<table class="table_query">
						<tr>
							<td class="right">服务商编码：</td>
							<td>
								<input class="middle_txt" type="text" name="DEALER_CODE" />
							</td>
							<td class="right">服务商名称：</td>
							<td>
								<input class="middle_txt" type="text" name="DEALER_NAME" />
							</td>
							<td class="right">开票类型：</td>
							<td>
								<script type="text/javascript">
									genSelBoxExp("dlrInvTpe", <%=Constant.DLR_INVOICE_TYPE%>, "", true, "short_sel u-select", "", "false", "");
								</script>
							</td>
						</tr>
						<tr>
							<td class="center" colspan="6">
								<input name="BtnQuery" id="queryBtn" class="u-button" type="button" value="查 询" onclick="__extQuery__(1)" />
								<input class="u-button" type="button" value="新 增" onclick="partBillAdd();" />
								<input class="u-button" type="button" value="导 出" onclick="exportPartBillExcel();" />
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