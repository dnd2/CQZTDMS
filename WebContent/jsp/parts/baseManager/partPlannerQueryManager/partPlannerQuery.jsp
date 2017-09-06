<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="com.infodms.dms.common.Constant"%>
<%@taglib uri="/jstl/cout" prefix="c"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>配件采购属性维护</title>
<%
    String contextPath = request.getContextPath();
%>
<script type="text/javascript">
	$(function(){
		__extQuery__(1);
	});
	var myPage;

	var url = "<%=contextPath%>/parts/baseManager/partPlannerQueryManager/partPlannerQuery/partPlannerQuerySearch.json";
				
	var title = null;
	
	var columns = [
				{header: "序号", dataIndex: 'SEQUENCE_ID', renderer: getIndex,style:'text-align:center'},
                {id:'action',header: "操作",sortable: false,dataIndex: 'ID',renderer:myLink ,align:'center'},
				{header: "配件编码", dataIndex: 'PART_OLDCODE', style:'text-align:center'},
				{header: "配件名称", dataIndex: 'PART_CNAME',style:'text-align:center'},
                {header: "件号", dataIndex: 'PART_CODE', style:'text-align:center'},
                {header: "供应商", dataIndex: 'VENDER_ID', style:'text-align:center', renderer: defaultVender},
				{header: "计划员", dataIndex: 'PLANER_ID',style:'text-align:center;',renderer: returnUserSelect},
				{header: "采购员", dataIndex: 'BUYER_ID', style:'text-align:center',renderer: returnUserSelect},
				{header: "默认收货库房", dataIndex: 'WH_ID', style:'text-align:center', renderer: getWarnHouseSel},
// 				{header: "是否计划", dataIndex: 'IS_PLAN', style:'text-align:center',renderer: planCheckBox},
				{header: "配件种类", dataIndex: 'PRODUCE_STATE', style:'text-align:center', index: getIndex, renderer: getItemValueSel},
				{header: "上级采购单位", dataIndex: 'SP', style:'text-align:center', renderer: getItemValue},
				{header: "采购方式", dataIndex: 'PRODUCE_FAC', style:'text-align:center', renderer: getPartProduceWay},
				{header: "交付周期", dataIndex: 'DELIVER_PERIOD', style:'text-align:center' ,renderer: getInputTextBox},
				{header: "最小包装量", dataIndex: 'BUY_MIN_PKG', style:'text-align:center',renderer: getInputTextBox},
				{header: "最小采购量", dataIndex: 'MIN_PURCHASE', style:'text-align:center',renderer: getInputTextBox}
				
				//{header: "通知单号", dataIndex: 'NOTICE_NUM', style:'text-align:center' ,renderer: getInputTextBox},
				// {header: "车厂是否计划", dataIndex: 'OEM_PLAN', style:'text-align:center',renderer:oemPlanCheckBox},
				//{header: "是否自制", dataIndex: 'IS_PLAN', style:'text-align:center',renderer: getItemValue},
				//{header: "材料属性", dataIndex: 'PART_MATERIAL', style:'text-align:center',renderer: getItemValueSel},
				//{header: "计划周期", dataIndex: 'PLAN_CYCLE', style:'text-align:center',renderer: getItemValueSel},
				
// 				{header: "是否直发", dataIndex: 'IS_DIRECT', style:'text-align:center',renderer:directCheckBox},
// 				{header: "是否紧缺件", dataIndex: 'IS_LACK', style:'text-align:center',renderer:lackCheckBox},
// 				{header: "是否领用", dataIndex: 'IS_RECEIVE', style:'text-align:center',renderer:recCheckBox},
// 				{header: "是否特殊配件", dataIndex: 'IS_SPECIAL', style:'text-align:center',renderer:isSpecialCheckBox},//add by zhumingwei 2013-09-16
// 				{header: "是否有效", dataIndex: 'STATE', style:'text-align:center',renderer: getItemValue}

		      ];
	
	//移除非数字字符
	function removeNotNum(obj){
		var str = $(obj).value.replace(/\D/g,'');
		$(obj).value = str;
	}
	
    //生成采购方式下拉框
    function getPartProduceWay(value, meta, record) {
        var partID = record.data.PART_ID;
        var name = record.data.PRODUCE_FAC_NAME;
        
        if(!name || name == null) name = "请选择";
        
        var output = "<a href=\"#\" style=\"color: #08327E;\" onclick='changeType(\"" + partID + "\")'>" + name + "</a>";
        return output;
    }
    function changeType(obj) {
        OpenHtmlWindow('<%=contextPath%>/parts/baseManager/partPlannerQueryManager/partPlannerQuery/UpdateProduceWay.do?partId=' + obj, 800, 450);
    }
	
	//设置超链接
	function myLink(value,meta,record)
	{
		var partId = record.data.PART_ID;
		var param = partId + '_RNUM'+record.data.ROWNUM;
	 	var state = record.data.STATE;
		return String.format("<a href=\"#\" onclick='save(\""+record.data.PART_ID+"\",\""+param+"\")'>[保存]</a>");
	}
    // 设置供应商选择
    function defaultVender(value, meta, record){
    	var html = "";
    	var venderName = record.data.VENDER_NAME;
    	if(!venderName){
    		venderName = "";
    	}
    	var priceId = record.data.PRICE_ID;
    	priceId = priceId == null ? "" : priceId;
		var partId = record.data.PART_ID +'_RNUM'+record.data.ROWNUM;
   		html += '<input class="long_txt" type="text" readonly="readonly" id="VENDER_NAME_'+partId+'" name="VENDER_NAME_'+partId+'" value="'+venderName+'">';
   		html += '<input type="hidden" id="PRICE_ID_'+partId+'" name="PRICE_ID_'+partId+'" value="'+priceId+'">';
   		html += '<input class="mark_btn" type="button" value="…" onclick="showPartVender(\'VENDER_NAME_'+partId+'\',\'VENDER_ID_'+partId+'\', \''+record.data.PART_ID+'\', \'false\')">';
   		html += '<input id="VENDER_ID_'+partId+'" name="VENDER_ID_'+partId+'" type="hidden" value="'+value+'">';
		return String.format(html);
    }
    
    // 设置供应商选择
	function showPartVender(inputCode ,inputId , notExistsPartId, isMulti ){
		if(!inputCode){ inputCode = null;}
		if(!inputId){ inputId = null;}
		OpenHtmlWindow(g_webAppName+"/dialog/venderSelectSingle.jsp?notExistsPartId="+notExistsPartId+"&INPUTCODE="+inputCode+"&INPUTID="+inputId+"&ISMULTI="+isMulti,730,390);
	}
	
	// 获取输入框
	function getInputTextBox(value, meta, record){
		var partId = record.data.PART_ID +'_RNUM'+record.data.ROWNUM;
		var dataIndex = this.dataIndex;
		var width = '50px;';
		var evetStr = "";
		var propertyStr = "maxlength='10'";
		if(dataIndex == 'NOTICE_NUM'){
			width = '150px;';
			propertyStr = "maxlength='100'";
		}else {
			evetStr = 'onkeyup="removeNotNum(this)"';
			propertyStr = "maxlength='10'";
			if(dataIndex == 'DELIVER_PERIOD'){
				propertyStr = "maxlength='4'";
			}
		}
		
		var html = '<input type="text" id="'+dataIndex + '_' + partId+'" name="'+dataIndex + '_' + partId+'" value="'+value+'" style="width: '+width+';" '+evetStr+' '+propertyStr+'>';
		
		return String.format(html);
		
	}
	
	// 获取下拉选择框
	function getItemValueSel(value, meta, record){
		var partId = record.data.PART_ID +'_RNUM'+record.data.ROWNUM;
		var dataIndex = this.dataIndex;
		var typeValue = 0;
		if(dataIndex == "PART_MATERIAL"){
			// 材料属性
			typeValue = <%=Constant.PART_BASE_MATERIAL%>;
		}else if(dataIndex == "OWNED_BASE"){
			// 所属基地
			typeValue = <%=Constant.PURCHASE_WAY%>;
		}else if(dataIndex == "PRODUCE_STATE"){
			// 计划周期
			typeValue = <%=Constant.PART_PRODUCE_STATE%>;
		}else{
			typeValue = <%=Constant.IF_TYPE%>;
		}
		var html = '<select id="'+dataIndex+'_'+partId+'" name="'+dataIndex+'_'+partId+'" style="width: 100px;">';
		html += '<option value="">-请选择-</option>';
		for(var i=0;i<codeData.length;i++){
			if(codeData[i].type == typeValue){
				html += '<option '+(codeData[i].codeId == value ? "selected" : "") ;
				html += ' value="'+codeData[i].codeId+'"';
				html += ' title="'+codeData[i].codeDesc+'" >';
				html += codeData[i].codeDesc;
				html += '</option>';
			}
		}
		html += '</select>';
		return String.format(html);
	}

	// 获取仓库下拉框
	// cellCnt,{},__data__,__data__.data[this.columns[j].renderParValue]
	function getWarnHouseSel(value, meta, record) {
		var partId = record.data.PART_ID +'_RNUM'+record.data.ROWNUM;
		var dataIndex = this.dataIndex;
		var whList = ${warnHouseList};
		var html = '';
		var html = '<select id="'+dataIndex+'_'+partId+'" name="'+dataIndex+'_'+partId+'" style="width: 160px;">';
		html += '<option value="">-请选择-</option>';
		for(var i=0;i<whList.length;i++){
			html += "<option "+(whList[i].WH_ID == value ? "selected" : "");
			html += " value='"+whList[i].WH_ID+"'";
			html += " title='"+whList[i].WH_NAME+"' >";
			html += whList[i].WH_NAME;
			html += "</option>";
		}
		return String.format(html);
	}
	
	//是否计划
	function planCheckBox(value,meta,record)
	{
		var partId = this.dataIndex + '_' + record.data.PART_ID +'_RNUM'+record.data.ROWNUM;
		var checkedValue = <%=Constant.IF_TYPE_YES%>;
		if(checkedValue == value)
		{
			return String.format("<input type='radio' id='"+partId+"' name='"+partId+"' value=\""+value+"\" onclick = 'setValue()' checked />");
		}
		else
			return String.format("<input type='radio' id='"+partId+"' name='"+partId+"' value=\""+value+"\" onclick = 'setValue()' />");
	}
	

	//设置人员下拉框
	function returnUserSelect(value,meta,record)
	{
		var userList = [];
		var dataIndex = this.dataIndex;
		if(dataIndex == "PLANER_ID"){
			userList = ${plannersList};
		}else if(dataIndex == "BUYER_ID"){
			userList = ${purchasersList};
		}
		dataIndex = dataIndex + "_" + record.data.PART_ID +'_RNUM'+record.data.ROWNUM;
		var html = "<select style='width: 80px;' class='short_sel' size='1' id='"+dataIndex+"'>";
		html += '<option value="">-请选择-</option>';
		for(var i = 0; i < userList.length; i++){
			var selected = "";
			var user = userList[i];
			if(value == user.USER_ID){
				selected = "selected";
			}
			html += '<option value="'+user.USER_ID+'" '+selected+'>'+user.NAME+'</option>';
		}
		html += '</select>';
// 		var str = "<select class=\"short_sel\" size = '1' id = 'PlanerSelect_"+partId+"' onmouseover='addPlannerList(\"PlanerSelect_"+partID+"\")' onclick='addPlannerList(\"PlanerSelect_"+partID+"\")'><option value='"+pId+"'>"+value+"</option>";
// 		str = str + "</select>";
		return String.format(html);
	}
	
	//保存
	function save(partId, parms)
	{
		var planerId = document.getElementById("PLANER_ID_"+parms).value; // 计划员
		var buyerId = document.getElementById("BUYER_ID_"+parms).value;// 采购员
		if(null == planerId || "null" == planerId || "" == planerId)
		{
			layer.msg("计划员不能为空!", {icon: 2});
			return false;
		}
		MyConfirm("确定要保存?", saveRecord, [partId, parms, planerId, buyerId], null, null);
	}

	//保存
	function saveRecord(partId, parms, planerId, buyerId){
		var venderId = document.getElementById("VENDER_ID_"+parms).value; // 供应商id
		var priceId = document.getElementById("PRICE_ID_"+parms).value; // 采购价ID
		var whId = document.getElementById("WH_ID_"+parms).value; // 仓库id
// 		var isPlan; // 是否计划
// 		if(document.getElementById("IS_PLAN_"+parms).checked){
<%-- 			isPlan = <%=Constant.IF_TYPE_YES%>; --%>
// 		}else{
<%-- 			isPlan = <%=Constant.IF_TYPE_NO%>; --%>
// 		}

		var produceState = document.getElementById("PRODUCE_STATE_"+parms).value; // 配件种类 -自制-配套
// 		var ownedBase = document.getElementById("OWNED_BASE_"+parms).value;// 所属基地-采购方式
		var deliveryCycle = document.getElementById("DELIVER_PERIOD_"+parms).value;// 交付周期
		var buyMinPkg = document.getElementById("BUY_MIN_PKG_"+parms).value;// 最小包装量
		var minPurchase = document.getElementById("MIN_PURCHASE_"+parms).value; // 最小采购量
     	var url = '<%=contextPath%>/parts/baseManager/partPlannerQueryManager/partPlannerQuery/savePartPlanner.json';
     	url += '?partId='+partId;
     	url += '&planerId='+planerId;
     	url += '&buyerId='+buyerId;
     	url += '&venderId='+venderId;
     	url += '&priceId='+priceId;
     	url += '&whId='+whId;
//      	url += '&isPlan='+isPlan;
     	url += '&produceState='+produceState;
//      	url += '&ownedBase='+ownedBase;
     	url += '&deliveryCycle='+deliveryCycle;
     	url += '&buyMinPkg='+buyMinPkg;
     	url += '&minPurchase='+minPurchase;
     	url += '&curPage='+myPage.page;
		btnDisable();
     	makeNomalFormCall(url,saveResult,'fm');
	}

	// 保存返回
    function saveResult(json) {
    	btnEnable();
        if (json.errorExist != null && json.errorExist.length > 0) {
            MyAlert(json.errorExist);
        } else if (json.success != null && json.success == "true") {
        	layer.msg("保存成功!", {icon: 1});
        	__extQuery__(1);
        } else {
            MyAlert("保存失败，请联系管理员!");
        }
	
	}
	
	
	//下载
	function exportPartBuyProExcel(){
		document.fm.action="<%=contextPath%>/parts/baseManager/partPlannerQueryManager/partPlannerQuery/expPartPurProExcel.do";
		document.fm.target="_self";
		document.fm.submit();
	}

	//上传
	function uploadExcel(){
		btnDisable();
		fm.action = "<%=contextPath%>/parts/baseManager/partPlannerQueryManager/partPlannerQuery/purProUpload.do";
		fm.submit();
	}

	//上传检查和确认信息
	function confirmUpload()
	{
		if(fileVilidate())
		{
			MyConfirm("确定导入选择的文件?",uploadExcel,[]);
		}
		
	}

	function fileVilidate(){
		var importFileName = $("#uploadFile").val();
		if(importFileName==""){
		    MyAlert("请选择导入文件!");
			return false;
		}
		var index = importFileName.lastIndexOf(".");
		var suffix = importFileName.substr(index+1,importFileName.length).toLowerCase();
		if(suffix != "xls" && suffix != "xlsx"){
		MyAlert("请选择Excel格式文件");
			return false;
		}
		return true;
	}	
	
	function showUpload(){
		var uploadDiv = document.getElementById("uploadDiv");
		if(uploadDiv.style.display=="block" ){
			uploadDiv.style.display = "none";
		}else {
		uploadDiv.style.display = "block";
		}
	}

	//下载上传模板
	function exportExcelTemplate(){
		fm.action = "<%=contextPath%>/parts/baseManager/partPlannerQueryManager/partPlannerQuery/exportExcelTemplate.do";
		fm.submit();
	}
	
</script>
</head>
<body>
	<div class="wbox">
		<form method="post" name="fm" id="fm" method="post" enctype="multipart/form-data">
			<input type="hidden" id="selPartId" name="selPartId" value="" />
			<div class="navigation">
				<img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置：配件管理 &gt; 基础信息管理 &gt; 配件基础信息维护 &gt; 配件采购属性维护
			</div>

			<div class="form-panel">
				<h2>
					<img src="<%=contextPath%>/jmstyle/img/search-ico.png" style="margin-bottom: -13px;" />查询条件
				</h2>
				<div class="form-body">
					<table class="table_query">
						<tr>
							<td class="right">配件编码：</td>
							<td>
								<input class="middle_txt" type="text" name="PART_OLDCODE" />
							</td>
							<td class="right">配件名称：</td>
							<td>
								<input class="middle_txt" type="text" name="PART_CNAME" />
							</td>
							<td class="right">计划员：</td>
							<%--<td ><input class="middle_txt" type="text" name="NAME" /></td>--%>
							<td>
								<select id="PLANER_ID" name="PLANER_ID" class="u-select">
									<option value="">-请选择-</option>
									<c:forEach items="${plannersList}" var="plannersList">
										<option value="${plannersList.USER_ID }">${plannersList.NAME }</option>
									</c:forEach>
								</select>
							</td>
						</tr>
						<tr>
							<td class="right">配件种类：</td>
							<td>
								<script type="text/javascript">
									genSelBox("PRODUCE_STATE",<%=Constant.PART_PRODUCE_STATE%>, "", true, "");
								</script>
							</td>
							<td class="right">默认收货库房：</td>
							<td>
								<select id="WH_ID" name="WH_ID" class="u-select">
									<option value="">-请选择-</option>
									<c:forEach items="${warnHouseList}" var="wh">
										<option value="${wh.WH_ID }">${wh.WH_NAME }</option>
									</c:forEach>
								</select>
							</td>
							<td class="right">采购员：</td>
							<td>
								<select id="BUYER_ID" name="BUYER_ID" class="u-select">
									<option value="">-请选择-</option>
									<c:forEach items="${purchasersList}" var="var">
										<option value="${var.USER_ID }">${var.NAME }</option>
									</c:forEach>
								</select>
							</td>
						</tr>
						<tr>
							<td class="right">采购方式：</td>
							<td>
								<script type="text/javascript">
									genSelBox("PRODUCE_FAC", <%=Constant.PURCHASE_WAY%> , "", true, "");
								</script>
							</td>
							<td class="right">供应商：</td>
							<td colspan="3">
								<input id="VENDER_ID" name="VENDER_ID" type="hidden" value="">
								<input style="width: 200px;" class="middle_txt" type="text" readonly="readonly" id="VENDER_NAME" name="VENDER_NAME" />
								<input class="mark_btn" type="button" value="&hellip;" onclick="showPartVender('VENDER_NAME','VENDER_ID', '', 'false')" />
							</td>
						</tr>
						<tr>
							<td class="center" colspan="6">
								<input class="u-button" type="button" value="查 询" name="BtnQuery" id="queryBtn" onclick="__extQuery__(1)" />
								<input class="u-button" type="button" value="重 置" onclick="reset()" />
								<input class="u-button" type="button" value="导 出" onclick="exportPartBuyProExcel()" />
								<input class="u-button" type="button" value="批量导入" onclick="showUpload()" />
							</td>
						</tr>
					</table>
				</div>
			</div>
			<div style="display: none; heigeht: 5px" id="uploadDiv">
				<table class="table_query">
					<tr>
						<td colspan="2">
							<font color="red"> &nbsp;&nbsp;<input type="button" class="normal_btn" value="下载模版" onclick="exportExcelTemplate()" /> 文件选择后,点&quot;确定&quot;按钮,完成上传操作：&nbsp;
							</font>
							<input type="file" name="uploadFile" id="uploadFile" style="width: 250px" datatype="0,is_null,2000" value="" />
							&nbsp;
							<input type="button" id="upbtn" class="normal_btn" value="确 定" onclick="confirmUpload()" />
						</td>
					</tr>
				</table>
			</div>
			<!--分页 begin -->
			<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
			<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
			<!--分页 end -->
		</form>
		<!--页面列表 end -->
	</div>
</body>
</html>