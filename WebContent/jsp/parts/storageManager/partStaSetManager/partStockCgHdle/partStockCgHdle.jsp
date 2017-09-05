<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c" %>
<%@ page import="com.infodms.dms.common.Constant"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<% String contextPath = request.getContextPath(); %>
<title>库存状态变更处理 </title>
<script type="text/javascript" >
$(function(){searchOrdInfos();});
var myPage;
var url = "<%=contextPath%>/parts/storageManager/partStaSetManager/partStockCgHdleAction/partStockCgHdleSearch.json";
var title = null;
var columns = null;
//查询
function searchOrdInfos()
{
	var isFished = document.getElementById("isFinish").value;
	var whObj = document.getElementById("whId");
	document.getElementById("whIdSel").value = whObj.value;
	document.getElementById("whNameSel").value = whObj.options[whObj.selectedIndex].text;
	if("1" == isFished){
		columns = [
					{header: "序号", dataIndex: 'DTL_ID', renderer:getIndex,align:'center'},
					{header: "配件编码", dataIndex: 'PART_OLDCODE', style:'text-align: center;'},
					{header: "配件名称", dataIndex: 'PART_CNAME', style:'text-align: center;'},
					{header: "件号", dataIndex: 'PART_CODE', style:'text-align: center;'},
					{header: "货位", dataIndex: 'LOC_CODE', style:'text-align: center;'},
					{header: "批次号", dataIndex: 'BATCH_NO', style:'text-align: center;'},
					{header: "可用库存", dataIndex: 'NORMAL_QTY', align:'center'},
					{header: "业务类型", dataIndex: 'CHANGE_REASON', align:'center',renderer:getItemValue},
					{header: "调整类型", dataIndex: 'CHANGE_TYPE', align:'center',renderer:getItemValue},
					{header: "调整数量", dataIndex: 'RETURN_QTY', align:'center'},
					{header: "备注", dataIndex: 'REMARK', style:'text-align: center;'},
					{header: "制单人", dataIndex: 'NAME', style:'text-align: center;'},
					{header: "可处理数量", dataIndex: 'UNCLS_QTY'},
					{header: "处理数量", dataIndex: 'UNCLS_QTY', renderer:getClQtyTxt},
					{header: "处理备注", dataIndex: 'DTL_ID', renderer:getClRmkTxt},
					{id:'action',header: "操作",sortable: false,dataIndex: 'ID',renderer:myLink ,align:'center'}
			      ];
	}else{
		columns = [
					{header: "序号", dataIndex: 'DTL_ID', renderer:getIndex,align:'center'},
					{header: "配件编码", dataIndex: 'PART_OLDCODE', align:'center',style:'text-align: center;'},
					{header: "配件名称", dataIndex: 'PART_CNAME', align:'center',style:'text-align: center;'},
					{header: "件号", dataIndex: 'PART_CODE', align:'center',style:'text-align: center;'},
					{header: "货位", dataIndex: 'LOC_CODE', style:'text-align: center;'},
					{header: "批次号", dataIndex: 'BATCH_NO', style:'text-align: center;'},
					{header: "可用库存", dataIndex: 'NORMAL_QTY', align:'center'},
					{header: "业务类型", dataIndex: 'CHANGE_REASON', align:'center',renderer:getItemValue},
					{header: "处理方式", dataIndex: 'CHANGE_TYPE', align:'center',renderer:getItemValue},
					{header: "调整数量", dataIndex: 'RETURN_QTY', align:'center'},
					{header: "创建日期", dataIndex: 'CREATE_DATE', align:'center'},
					{header: "备注(原因)", dataIndex: 'REMARK', style:'text-align: center;'},
					{header: "制单人", dataIndex: 'NAME', style:'text-align: center;'},
					{header: "已处理数量", dataIndex: 'COLSE_QTY', align:'center'},
					{header: "可处理数量", dataIndex: 'UNCLS_QTY', align:'center'},
					{header: "处理日期", dataIndex: 'UPDATE_DATE', align:'center'},
					{header: "处理原因", dataIndex: 'REMARK2', style:'text-align: center;'},
					{header: "完成状态", dataIndex: 'STATUS',renderer:getState},
					{header: "仓库", dataIndex: 'WH_CNAME', align:'center',style:'text-align: center;'}
			      ];
	}
	__extQuery__(1);
}


//设置超链接
function myLink(value,meta,record)
{
	var dtlId = record.data.DTL_ID;
	var chgType = record.data.CHANGE_TYPE;
	var chgType1 = <%=Constant.PART_STOCK_STATUS_CHANGE_TYPE_01 %>;
	if(chgType1.toString() != chgType.toString()){
		return String.format("<input type='button' class='u-button' onclick='saveInfos()' value='封 存'/>");
	}else{
		return String.format("<input type='button' class='u-button' onclick='saveInfos()' value='解 封'/>");
	}
}

function getClQtyTxt(value,meta,record){
	var dtlId = record.data.DTL_ID;
	var unClsQty = record.data.UNCLS_QTY;
	return String.format("<input type='hidden' id='unClsQty_"+dtlId+"' name='unClsQty_"+dtlId+"' value='"+unClsQty+"'/>" 
			+ "<input type='text' id='"+dtlId+"' name='clsQtys' style='width: 50px;' class='middle_txt' value='' onchange='dataTypeCheck(this)'/>");
}

function getClRmkTxt(value,meta,record){
	var dtlId = record.data.DTL_ID;
	var chgType = record.data.CHANGE_TYPE;
	var chgReason = record.data.CHANGE_REASON;
	return String.format("<input type='hidden' id='chgType_"+dtlId+"' name='chgType_"+dtlId+"' value='"+chgType+"'/>"
			+ "<input type='hidden' id='chgReason_"+dtlId+"' name='chgReason_"+dtlId+"' value='"+chgReason+"'/>"
			+ "<input type='text' id='clsRmk_"+dtlId+"' style='width: 50px;'  class='middle_txt' name='clsRmk_"+dtlId+"' value=''/>");
}

function getState(value,meta,record){
	return String.format("<font color='red'>已完成</font>");
}

//数据验证
function dataTypeCheck(obj){
	var value = obj.value;
    if (isNaN(value) || "" == value) {
        MyAlert("请输入数字!");
        obj.value = "";
        return;
    }
    var re = /^([1-9]+[0-9]*]*)$/;
    if (!re.test(obj.value)) {
        MyAlert("请输入正整数!");
        obj.value = "";
        return;
    }
    var dtlId = obj.id.toString();
    var unClsQty = document.getElementById("unClsQty_" + dtlId).value;
    if(parseInt(value) > parseInt(unClsQty)){
    	MyAlert("处理数量不能大于可处理数量!");
    	obj.value = "";
        return;
    }
}

//保存处理信息
function saveInfos(){
	var clsQtyArr = document.getElementsByName("clsQtys");
	var dtlIds = "";
	var count = 0;
	for(var i = 0; i < clsQtyArr.length; i ++){
		var temVal = clsQtyArr[i].value;
		if(null != temVal && "" != temVal){
			dtlIds += clsQtyArr[i].id +":"+ temVal + ",";
			count ++;
		}
	}
	document.getElementById("dtlIds").value = dtlIds;

	if(count < 1){
		MyAlert("请先设置需处理配件数量!")
		return;
	}
		
	MyConfirm("确定保存设置?",commitOrder,[]);
}

function commitOrder()
{
	btnDisable();
	var url = "<%=contextPath%>/parts/storageManager/partStaSetManager/partStockCgHdleAction/commitHandleResult.json";	
	makeNomalFormCall(url,getResult,'fm');
}

function getResult(json){
	btnEnable();
	if(null != json){
        if (json.errorExist != null && json.errorExist.length > 0) {
        	MyAlert(json.errorExist);
        	__extQuery__(json.curPage);
        } else if (json.success != null && json.success.length > 0) {
        	MyAlert("操作成功!");
        	__extQuery__(json.curPage);
        	
        } else {
            MyAlert("操作失败，请联系管理员!");
        }
	}
}

//查看
function viewDetail(parms){
	btnDisable();
	document.fm.action="<%=contextPath%>/parts/storageManager/partStaSetManager/partStockCgHdleAction/viewStockDeatilInint.do?changeId=" + parms;
	document.fm.target="_self";
	document.fm.submit();
}

//下载
function exportPartStockExcel(){
	document.fm.action="<%=contextPath%>/parts/storageManager/partStaSetManager/partStockCgHdleAction/exportPartStockStatusExcel.do";
	document.fm.target="_self";
	document.fm.submit();
}

//汇总导出
function exportStaSetDetExcel(){
	document.fm.action="<%=contextPath%>/parts/storageManager/partStaSetManager/partStockCgHdleAction/exportStaSetDetExcel.do";
	document.fm.target="_self";
	document.fm.submit();
}

</script>
</head>
<body>
<div class="wbox">
	<form method="post" name ="fm" id="fm">
		<input type="hidden" name="parentOrgId" id="parentOrgId" value="${parentOrgId }"/>
		<input type="hidden" name="parentOrgCode" id="parentOrgCode" value="${parentOrgCode }"/>
		<input type="hidden" name="companyName" id="companyName" value="${companyName }"/>
		<input type="hidden" name="dtlIds" id="dtlIds" value=""/>
		<input type="hidden" name="whIdSel" id="whIdSel" value=""/>
		<input type="hidden" name="whNameSel" id="whNameSel" value=""/>
		<div class="navigation">
			<img src="<%=request.getContextPath()%>/img/nav.gif" />&nbsp;当前位置： 配件仓库管理 &gt; 库存状态变更 &gt; 库存状态变更处理
		</div>
		<div class="form-panel">
		<h2><img src="<%=contextPath%>/jmstyle/img/search-ico.png" style="margin-bottom: -13px;"/>查询条件</h2>
			<div class="form-body">
					<table class="table_query">
						<tr>
							<td class="right">配件编码：</td>
							<td>
								<input class="middle_txt" type="text" name="partOldcode" id="partOldcode" />
							</td>
							<td class="right">配件名称：</td>
							<td>
								<input class="middle_txt" type="text"name="partCname" id="partCname" />
							</td>
							<td class="right">备注：</td>
							<td>
								<input class="middle_txt" type="text"name="remark" id="remark" />
							</td>
						</tr>
						<tr>
							<td class="right">处理日期：</td>
							<td>
								<input id="handleSDate" class="short_txt" name="handleSDate" datatype="1,is_date,10" maxlength="10" group="handleSDate,handleEDate" style="width:80px;" /> 
								<input class="time_ico" onclick="showcalendar(event, 'handleSDate', false);" value=" " type="button" /> 
								&nbsp;至&nbsp; 
								<input id="handleEDate" class="short_txt" name="handleEDate" datatype="1,is_date,10" maxlength="10" group="handleSDate,handleEDate" style="width:80px;"/> 
								<input class="time_ico" onclick="showcalendar(event, 'handleEDate', false);" value=" " type="button" />
							</td>
							<td class="right">业务类型：</td>
							<td>
								<script type="text/javascript">
					        		genSelBoxExp("businessType",<%=Constant.PART_STOCK_STATUS_BUSINESS_TYPE%>,"",true,"","onchange='__extQuery__(1)'","false","");
						  		</script>
						  	</td>
							<td class="right">处理类型：</td>
							<td>
								<script type="text/javascript">
		        					genSelBoxExp("changeType",<%=Constant.PART_STOCK_STATUS_CHANGE_TYPE%>,"",true,"","onchange='__extQuery__(1)'","false","");
			  	  				</script>
			  	  			</td>
						</tr>
						<tr>
							<td class="right">完成状态：</td>
							<td>
								<select name="isFinish" id="isFinish" class="u-select" onchange="searchOrdInfos()">
									<option value="1">-未完成-</option>
									<option value="0">-已完成-</option>
								</select>
							</td>
							<td class="right">仓库：</td>
							<td>
								<select name="whId" id="whId" class="u-select" onchange="searchOrdInfos()">
									<c:if test="${WHList!=null}">
										<c:forEach items="${WHList}" var="list">
											<option value="${list.WH_ID }">${list.WH_CNAME }</option>
										</c:forEach>
									</c:if>
								</select>
							</td>
							<td class="right">制单人：</td>
							<td>
								<input class="middle_txt" type="text" name="maker" id="maker" value="" />
							</td>
						</tr>
						<tr>
							<td class="center" colspan="6">
								<input class="u-button" type="button" value="查 询" name="BtnQuery" id="queryBtn" onclick="searchOrdInfos()" /> 
								<input class="u-button" type="reset" value="重 置" onclick="reset()" />
								<input class="u-button" type="button" value="批量处理" onclick="saveInfos()" />
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