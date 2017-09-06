<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="/jstl/cout"%>
<%@page import="com.infodms.dms.common.Constant"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%
    String contextPath = request.getContextPath();
%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta http-equiv="X-UA-Compatible" content="IE=7">
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>退换货解封申请新增</title>
<script type="text/javascript">
var myPage;
var url = '<%=contextPath%>/parts/storageManager/partReturns/ReturnPartStateChange/getReturnParts.json';
var title = null;
var columns = [
    		//renderer:getItemValue
		{header: "序号", renderer: getIndex},
		{id:'action',header: '<input type="checkbox" onclick="ckAllDtl(this);">',sortable: false,dataIndex: 'DTL_ID',renderer:myLink , style: 'text-align: center'},
		{header: "退换单号",dataIndex:'RETURN_CODE', style:"text-align: center"},
		{header: "销售单号",dataIndex:'SO_CODE', style:"text-align: center"},
		{header: "入库单号",dataIndex:'IN_CODE', style:"text-align: center"},
		{header: "配件编码",dataIndex:'PART_OLDCODE', style:"text-align: center"},
		{header: "配件名称",dataIndex:'PART_CNAME', style:"text-align: center"},
		{header: "单位",dataIndex:'UNIT', style:"text-align: center"},
		{header: "批次号",dataIndex:'IN_BATCH_NO', style:"text-align: center"},
		{header: "冻结总量",dataIndex:'IN_QTY', style:"text-align: center"},
		{header: "可解封数量",dataIndex:'KY_QTY', style:"text-align: center"},
		{header: "已解封数",dataIndex:'UNLOC_QTY', style:"text-align: center"},
		{header: "状态",dataIndex:'STATE', style:"text-align: center",renderer:getItemValue}
        ];
    
//操作链接生成
function myLink(value,meta,record){
	var soCode = record.data.SO_CODE == null ? "" : record.data.SO_CODE;
	var inCode = record.data.IN_CODE == null ? "" : record.data.IN_CODE;
	var partCode = record.data.PART_CODE == null ? "" : record.data.PART_CODE;
	
	var str = '<input type="hidden" id="RETURN_CODE_'+value+'" name="RETURN_CODE_'+value+'" value="'+record.data.RETURN_CODE+'" >';//退货单号
		str += '<input type="hidden" id="SO_CODE_'+value+'" name="SO_CODE_'+value+'" value="'+soCode+'" >'; // 销售单号
		str += '<input type="hidden" id="IN_CODE_'+value+'" name="IN_CODE_'+value+'" value="'+inCode+'" >'; // 入库单号
		str += '<input type="hidden" id="PART_ID_'+value+'" name="PART_ID_'+value+'" value="'+record.data.PART_ID+'" >';//配件ID
	    str += '<input type="hidden" id="PART_OLDCODE_'+value+'" name="PART_OLDCODE_'+value+'" value="'+record.data.PART_OLDCODE+'" >';//配件编码
		str += '<input type="hidden" id="PART_CNAME_'+value+'" name="PART_CNAME_'+value+'" value="'+record.data.PART_CNAME+'" >';//配件名称
		str += '<input type="hidden" id="PART_CODE_'+value+'" name="PART_CODE_'+value+'" value="'+record.data.PART_CODE+'" >';//件号
		str += '<input type="hidden" id="UNIT_'+value+'" name="UNIT_'+value+'" value="'+record.data.UNIT+'" >';//单位
		str += '<input type="hidden" id="BATCH_NO_'+value+'" name="BATCH_NO_'+value+'" value="'+record.data.IN_BATCH_NO+'" >';//单位
		str += '<input type="hidden" id="IN_QTY_'+value+'" name="IN_QTY_'+value+'" value="'+record.data.IN_QTY+'" >';//总数量
		str += '<input type="hidden" id="KY_QTY_'+value+'" name="KY_QTY_'+value+'" value="'+record.data.KY_QTY+'" >';//可用数量
		str += '<input type="hidden" id="UNLOC_QTY_'+value+'" name="UNLOC_QTY_'+value+'" value="'+record.data.UNLOC_QTY+'" >';//已解封数
	return '<input type="checkbox" name="dtls" value="'+value+'">'+str;
}
    
//返回申请页面
function goback(){
	location = '<%=contextPath%>/parts/storageManager/partReturns/ReturnPartStateChange/toReturnPartAppl.do';
}

//保存申请
function saveReturnApply(){
	//1验证
	var leng = $('input[name="ck"]:checked').length;
	if(leng==0){
		MyAlert('请选择要申请解封的数据！');
		return;
	}else{
		//验证申请数量
		var len = $('input[name="ck"]').length;
		for(var i=0;i<len;i++){
			var flag = $('input[name="ck"]').eq(i).prop('checked');
			if(flag==true){
				var id = $('input[name="ck"]').eq(i).val();
				var applyQty = $('#applyQty'+id).val();
				if(applyQty==''){
					MyAlert('第'+(i+1)+'行，申请数量不能为空！');
					return;
				}
			}
		}
	}
	
	var dealerId = $('#dealerId').val();
	if(dealerId==''){
		MyAlert('申请解封单位异常，请退出重试，或者联系管理员！');
		return;
	}
	var remark = $('#remark').val();
	if(remark==''){
		MyAlert('请输入解封原因 ！');
		return;
	}
	//2保存
	MyConfirm('确认保存？', function(){
		btnDisable();
		var urlkey = '<%=contextPath%>/parts/storageManager/partReturns/ReturnPartStateChange/saveReturnApply.json';
		sendAjax(urlkey, getResult, 'fm');
		
	});
}
//操作结果
function getResult(json){
	var success = json.success;
	var error = json.error;
	var ex = json.Exception;
	if(success!=null && success!='' && success!='null' && success!='undefined'){
		MyAlert(success,goback);
	}else if(error!=null && error!='' && error!='null' && error!='undefined'){
		MyAlert(error);
	}else if(ex!=null && ex!='' && ex!='null' && ex!='undefined'){
		MyAlert(json.Exception.message);
	}else{
		MyAlert("解封单保存失败，请联系管理员！");
	}
	btnEnable();
}
//显示或隐藏退换货配件查询块
function showPartDiv(){
	var addPartViv = $('#addPartViv').val();
	if(addPartViv=='增加'){
		$('#partDiv').show();
		$('#addPartViv').val('隐藏');
		__extQuery__(1);
	}else{
		$('#partDiv').hide();
		$('#addPartViv').val('增加');
	}
}
//选择全部已选择
function ckAlls(self){
	var cks = $(self).prop('checked');
	$('input[name="ck"]').prop('checked',cks);
}
//选择全部明细
function ckAllDtl(self){
	var ck = $(self).prop('checked');
	$('input[name="dtls"]').prop('checked',ck);
}
//添加选择的配件到保存去
function addCells(){
	var len = $('input[name="dtls"]:checked').length;
	if(len==0){
		MyAlert('请选择要添加的数据！');
		return;
	}
	var error = '';
	for(var i=0;i<len;i++){
		var dtlId = $('input[name="dtls"]:checked').eq(i).val();
		
		var RETURN_CODE = $('#RETURN_CODE_'+dtlId).val();//退换单号
		var SO_CODE = $('#SO_CODE_'+dtlId).val();//销售单号
		var IN_CODE = $('#IN_CODE_'+dtlId).val();//入库单号
		var PART_ID = $('#PART_ID_'+dtlId).val();//配件ID
		var PART_OLDCODE = $('#PART_OLDCODE_'+dtlId).val();//配件编码
		var PART_CNAME = $('#PART_CNAME_'+dtlId).val();//配件名称
		var PART_CODE = $('#PART_CODE_'+dtlId).val();//配件件号
		var UNIT = $('#UNIT_'+dtlId).val();//单位
		var BATCH_NO = $('#BATCH_NO_'+dtlId).val();//单位
		var IN_QTY = $('#IN_QTY_'+dtlId).val();//总数量
		var KY_QTY = $('#KY_QTY_'+dtlId).val();//可用数量
		var UNLOC_QTY = $('#UNLOC_QTY_'+dtlId).val();//已解封数
		
		//验证是否重复
		var leng = $('input[name="ck"]').length;
		var flag = true;
		for(var k=0;k<leng;k++){
			//判断重复
			var dtlk = $('input[name="ck"]').eq(k).val();
			if(dtlk==dtlId){
				flag = false;
			}
		}
		if(flag==true){
			//不重复，写入保存信息块
			var rsStr = '<input type="hidden" id="returnCode'+dtlId+'" name="returnCode'+dtlId+'" value="'+RETURN_CODE+'">';
				rsStr += '<input type="hidden" id="inCode'+dtlId+'" name="soCode'+dtlId+'" value="'+SO_CODE+'">';
				rsStr += '<input type="hidden" id="inCode'+dtlId+'" name="inCode'+dtlId+'" value="'+IN_CODE+'">';
				rsStr += '<input type="hidden" id="partId'+dtlId+'" name="partId'+dtlId+'" value="'+PART_ID+'">';
				rsStr += '<input type="hidden" id="partOldcode'+dtlId+'" name="partOldcode'+dtlId+'" value="'+PART_OLDCODE+'">';
				rsStr += '<input type="hidden" id="partCname'+dtlId+'" name="partCname'+dtlId+'" value="'+PART_CNAME+'">';
				rsStr += '<input type="hidden" id="partCode'+dtlId+'" name="partCode'+dtlId+'" value="'+PART_CODE+'">';
				rsStr += '<input type="hidden" id="unit'+dtlId+'" name="unit'+dtlId+'" value="'+UNIT+'">';
				rsStr += '<input type="hidden" id="batchNo'+dtlId+'" name="batchNo'+dtlId+'" value="'+BATCH_NO+'">';
				rsStr += '<input type="hidden" id="inQty'+dtlId+'" name="inQty'+dtlId+'" value="'+IN_QTY+'">';
				rsStr += '<input type="hidden" id="kyQty'+dtlId+'" name="kyQty'+dtlId+'" value="'+KY_QTY+'">';
				rsStr += '<input type="hidden" id="unlocQty'+dtlId+'" name="unlocQty'+dtlId+'" value="'+UNLOC_QTY+'">';
				
			var str = '<tr id="delete_'+dtlId+'" class="delete_claszzall">';
			    str += '<td><input type="checkbox" checked="checked" name="ck" value="'+dtlId+'" />'+rsStr+'</td>';
			    str += '<td class="my_xh"></td>';
			    str += '<td>'+RETURN_CODE+'</td>';
			    str += '<td>'+SO_CODE+'</td>';
			    str += '<td>'+IN_CODE+'</td>';
			    str += '<td>'+PART_OLDCODE+'</td>';
			    str += '<td>'+PART_CNAME+'</td>';
			    str += '<td>'+UNIT+'</td>';
			    str += '<td>'+BATCH_NO+'</td>';
			    str += '<td>'+KY_QTY+'</td>';
			    str += '<td><input type="text" class="short_txt" style="background-color:#FFFFCC;text-align:center;color:#0000;" id="applyQty'+dtlId+'" name="applyQty'+dtlId+'" vlaue="" onblur="valiaApplyQty(\''+dtlId+'\')" /></td>';
			    str += '<td>'+IN_QTY+'</td>';
			    str += '<td>'+UNLOC_QTY+'</td>';
			    str += '<td><input type="button" class="u-button" value="删除" onclick="deleteRow(\'delete_'+dtlId+'\')"></td>';
			    str += '</tr>';
			$('#savefile').append(str);
		}else{
			//重复，写入提示信息
			error += '退换单号:['+RETURN_CODE+']，配件编码:['+PART_OLDCODE+']已经存在！<br>';
		}
	}
	if(error!=''){
		MyAlert(error);
	}
	
	setXhDom();//写入序号元素
	setStyleDom();//写入行样式元素
}
//删除行
function deleteRow(id){
	$('#'+id).remove();
	setXhDom();//重写序号
	setStyleDom();
}
      //写入行样式元素
function setStyleDom(){
	var leng = $('input[name="ck"]').length;
	
	for(var i=0;i<leng;i++){
		/* var trclass="";
		if((i+1)%2==0){
			trclass = 'table_list_row2';
		}else{
			trclass = 'table_list_row1';
		} */
		var ckId = $('input[name="ck"]').eq(i).val();
		$('#delete_'+ckId).removeClass('table_list_row2');
		$('#delete_'+ckId).removeClass('table_list_row1');
		// $('#delete_'+ckId).addClass(trclass);
		$('#applyQty'+ckId).attr('tabindex',(i+1));//按tab快捷到下一个输入框
		var kyQty = $('#kyQty'+ckId).val();
		if($('#applyQty'+ckId).val()==''){
			$('#applyQty'+ckId).val(kyQty);//可用=申请
		}
		
	}
}
//写入序号元素
      function setXhDom(){
	var leng = $('.my_xh').length;
	for(var i=0;i<leng;i++){
		$('.my_xh').eq(i).html(i+1);
	}
}
//验证数量是否正确
function valiaApplyQty(id){
	var applyQty = $('#applyQty'+id).val();
	if(applyQty==''){
		MyAlert('申请数量不能为空！');
		$('#label'+id).html('0');
		return;
	}else{
		var kyQty = $('#kyQty'+id).val();
		if(isNaN(applyQty)){
			MyAlert('请输入大于 0 小于等于 '+kyQty+' 数字！');
			$('#applyQty'+id).val('');
			$('#label'+id).html('0');
			return;
		}else if(Number(applyQty)<=0 || Number(applyQty)>Number(kyQty)){
			MyAlert('请输入大于 0 小于等于 '+kyQty+' 数字！');
			$('#applyQty'+id).val('');
			$('#label'+id).html('0');
			return;
		}
	}
}

//汇总所有选择数据的金额，验证不了的全部设置为最大可解封数量
function getSumAmount(){
	var leng = $('input[name="ck"]:checked').length;
	var money = 0;
	for(var i=0;i<leng;i++){
		var ckId = $('input[name="ck"]:checked').eq(i).val();
		var applyQty = $('#applyQty'+ckId).val();
		var kyQty = $('#kyQty'+ckId).val();
		var salePrice = $('#salePrice'+ckId).val();
		if(applyQty=='' || isNaN(applyQty) || Number(applyQty)<=0 || Number(applyQty)>Number(kyQty)){
			$('#applyQty'+ckId).val(kyQty);
			applyQty = kyQty;
		}
		var rs = Number(applyQty) * Number(salePrice);
		rs = rs.toFixed(2);
		$('#label'+ckId).html(rs);
		$('#saleAmount'+ckId).val(rs);
		money = Number(money)+Number(rs);
	}
	$('#amount').val(money);
}
      
  </script>

</head>
<style type="text/css">
.table_list_row0 td {
	background-color: #FFFFCC;
	border: 1px solid #DAE0EE;
	white-space: nowrap;
}
</style>
<body>
	<div class="wbox">
		<div class="navigation">
			<img src="<%=request.getContextPath()%>/img/nav.gif" />&nbsp;当前位置： 配件仓储管理&gt; 配件退换货状态变更&gt; 退换货解封申请 &gt; 新增
		</div>
		<form method="post" name="fm" id="fm" enctype="multipart/form-data">
			<input type="hidden" id="dealerId" name="dealerId" value="${dealerId}">
			<input type="hidden" id="dealerCode" name="dealerCode" value="${dealerCode}">
			<input type="hidden" id="dealerName" name="dealerName" value="${dealerName}">
			<div class="form-panel">
				<h2>
					<img src="<%=request.getContextPath()%>/img/subNav.gif" /> 保存条件
				</h2>
				<div class="form-body">
					<table class="table_query">
						<tr>
							<td class="right">申请单位：</td>
							<td>
								<label>${dealerName}</label>
							</td>
							<td class="right">申请人：</td>
							<td>
								<label>${userName}</label>
							</td>
						</tr>
						<tr>
							<td class="right">解封原因：</td>
							<td colspan="3">
								<textarea class="form-control" style="width: 80%;" id="remark" name="remark" rows="3"></textarea>
							</td>
						</tr>
					</table>
				</div>
			</div>

			<table id="savefile" class="table_list" style="border-bottom: 1px;">
				<caption><img src="<%=contextPath%>/img/nav.gif" />&nbsp;配件解封明细</caption>
				<tr>
					<th>
						<input type="checkbox" checked="checked" onclick="ckAlls(this)" />
					</th>
					<th>序号</th>
					<th>退换单编码</th>
					<th>销售单号</th>
					<th>入库单号</th>
					<th>配件编码</th>
					<th>配件名称</th>
					<th>单位</th>
					<th>批次号</th>
					<th>可解封数量</th>
					<th>申请数量</th>
					<th>冻结总量</th>
					<th>已解封数</th>
					<th>操作</th>
				</tr>
			</table>
			<table style="width: 100%;">
				<tr>
					<td align="center">
						<input type="button" class="u-button" onclick="saveReturnApply()" value="保存" />
						<input type="button" class="u-button" onclick="goback()" value="返回" />
					</td>
				</tr>
			</table>
			<fieldset>
				<legend style="MozUserSelect: none; KhtmlUserSelect: none" unselectable="on">
					<th colspan="6">
						<img src="<%=contextPath%>/img/subNav.gif" alt="" class="nav" />退换货单查询
						<input type="button" class="u-button" id="addPartViv" name="addPartViv" value="增加" onclick="showPartDiv()" />
					</th>
				</legend>
				<div id="partDiv" style="display: none; heigeht: 5px;">
					<table class="table_query" width=100% border="0" align="center" cellpadding="1" cellspacing="1">
						<tr>
							<td class="right">退换单号：</td>
							<td>
								<input type="text" class="middle_txt" id="returnCode" name="returnCode" value="" />
							</td>
							<td class="right">入库单号：</td>
							<td>
								<input type="text" class="middle_txt" id="inCode" name="inCode" value="" />
							</td>
							<td class="right">仓库：</td>
							<td>
								<select class="u-select" id="whId" name="whId">
									<c:forEach items="${whList}" var="list" varStatus="v">
										<c:if test="${list.whId eq 32082}">
											<option selected="selected" value="${list.whId}">${list.whName}</option>
										</c:if>
										<c:if test="${list.whId != 32082}">
											<option value="${list.whId}">${list.whName}</option>
										</c:if>
									</c:forEach>
								</select>
							</td>
						</tr>
						<tr>
							<td class="right">配件编码：</td>
							<td>
								<input type="text" class="middle_txt" id="partOldcode" name="partOldcode" value="" />
							</td>
							<td class="right">配件名称：</td>
							<td>
								<input type="text" class="middle_txt" id="partCname" name="partCname" value="" />
							</td>
							<td class="right">单位：</td>
							<td>
								<select class="u-select" id="unit" name="unit">
									<option value="">-请选择-</option>
									<c:forEach items="${unitList}" var="unit" varStatus="v">
										<option value="${unit.fixName }" >${unit.fixName }</option>
									</c:forEach>
								</select>
							</td>
						</tr>
						<tr>
							<td class="center" colspan="6">
								<input class="u-button" type="button" name="BtnQuery" id="queryBtn" value="查 询" onclick="__extQuery__(1);" />
								<input class="u-button" type="reset">
								<input class="u-button" type="button" name="BtnQuery" id="queryBtn" value="添 加" onclick="addCells();" />
							</td>
						</tr>
					</table>
					<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
					<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
				</div>
			</fieldset>
		</form>

	</div>
</body>
</html>