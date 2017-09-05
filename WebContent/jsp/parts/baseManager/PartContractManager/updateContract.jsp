<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" pageEncoding="UTF-8" %>
<%@ taglib uri="/jstl/cout" prefix="c" %>
<%@ page import="com.infodms.dms.common.Constant" %>
<%
    String contextPath = request.getContextPath();
%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp"/>
<title>合同管理</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
<script language="javascript" type="text/javascript">

$(function(){
	var val=document.getElementById("CONTRACT_TYPE").value;
	if (val==<%=Constant.CONTRACT_TYPE_02%>){
		__extQuery__(1);
	}
});

var myPage;
var url = "<%=contextPath%>/parts/baseManager/PartContractManager/PartContractQuery/updateByContract.json";
var title = null;
var columns = [
    {header: "序号", style: "text-align: left", renderer: getIndex},
    {header: "备件编码", dataIndex: 'PART_OLDCODE', style: "text-align: left", width: '3%'},
    {header: "备件名称", dataIndex: 'PART_CNAME', style: "text-align: left", width: '5%'},
    {header: "备件件号", dataIndex: 'PART_CODE', style: "text-align: left", width: '3%'},
    {header: "合同价", dataIndex: 'CONTRACT_PRICE', style: "text-align: center", width: '3%',renderer:insertInput}
];

function insertInput(value, meta, record) {
    var output = '<input type="hidden" name="DEF_ID" value="'+record.data.DEF_ID+'"/><input type="hidden"  id="PART_ID' + record.data.DEF_ID + '"  value="'+record.data.PART_ID+'" name="PART_ID' + record.data.DEF_ID + '"/><input type="text" class="phone_txt" style=" background-color:#FF9;text-align:center"  onblur="checkPrice(this)"  id="CONTRACT_PRICE' + record.data.DEF_ID + '" name="CONTRACT_PRICE' + record.data.DEF_ID + '"  value="' + value + '" size ="10" style="background-color:#FF9"/>';
    return output;
}

function checkPrice(obj) {
    if (obj.value == "") {
        MyAlert("价格不能为空!");
        return;
    }
    var patrn = /^((0\.[0-9]*[1-9][0-9]*)|([1-9][0-9]*\.[0-9]+)|([1-9][0-9]*))$/;
    if (!patrn.exec(obj.value)) {
        MyAlert("价格无效,请重新输入!");
        obj.value = "";
        return;
    } else {
        if (obj.value.indexOf(".") >= 0) {
            var patrn = /^[0-9]{0,10}.[0-9]{0,7}$/;
            if (!patrn.exec(obj.value)) {
                MyAlert("价格整数部分不能超过10位,且保留精度最大为7位!");
                obj.value = "";
                return;
            }
        } else {
            var patrn = /^[0-9]{0,10}$/;
            if (!patrn.exec(obj.value)) {
                MyAlert("价格整数部分不能超过10位!");
                obj.value = "";
                return;
            }
        }
    }
}

function showResult(json) {
    btnEnable();
    if (json.errorExist != null && json.errorExist.length > 0) {
        MyAlert(json.errorExist);
    } else if (json.success != null && json.success == "true") {
		MyAlert("保存成功!", function(){
			window.location.href='<%=contextPath%>/parts/baseManager/PartContractManager/PartContractQuery/partContractQueryInit.do';
		});
    } else {
        MyAlert("保存失败，请联系管理员!");
    }
}

function doInit() {
    var _this = document.getElementById("actPartType");
    hideImportBtn(_this);
    loadcalendar();  //初始化时间控件
}
//返回
function goBack() {
    btnDisable();
    location = '<%=contextPath%>/parts/baseManager/PartContractManager/PartContractQuery/partContractQueryInit.do';
}

//表单提交方法：
function checkForm() {
    btnDisable();
    <%--var url = '<%=contextPath%>/parts/baseManager/PartContractManager/PartContractQuery/saveConTract.json';--%>
	fm.action='<%=contextPath%>/parts/baseManager/PartContractManager/PartContractQuery/saveConTract.do';
	fm.submit();
//            makeNomalFormCall(url, showResult, 'fm');
}
function showResult(json) {
    btnEnable();
   if (json.success != null && json.success == "true") {
        MyAlert("新增成功!", function(){
	        window.location.href = "<%=contextPath%>/parts/baseManager/PartContractManager/PartContractQuery/partContractQueryInit.do";
        });
    }else {
        MyAlert("新增失败，请联系管理员!");
    }
}
//表单提交前的验证：
function checkFormUpdate() {
    var conType = document.getElementById("CONTRACT_TYPE1").value;
    var startDate = document.getElementById("checkSDate").value;
    var endDate = document.getElementById("checkEDate").value;
    var connum = document.getElementById("CONTRACT_NUMBER").value;
    var price=document.getElementsByName("DEF_ID");
    if ("" == conType || null == conType) {
        layer.msg('请选择合同类型!', {icon: 2});
        return false;
    }

    if ("" == connum || null == connum) {
        layer.msg('请填写合同编号!', {icon: 2});
        return false;
    }


    if ("" == startDate || null == startDate) {
        layer.msg('请先设置合同开始日期!', {icon: 2});
        return false;
    }

    if ("" == endDate || null == endDate) {
        layer.msg('请先设置合同结束日期!', {icon: 2});
        return false;
    }

    var startDateFormat = new Date(startDate.replace("-", "/"));
    var endDateFormat = new Date(endDate.replace("-", "/"));

    if ((endDateFormat - startDateFormat) < 0) {
        layer.msg('活动结束日期要晚于开始日期!', {icon: 2});
        return false;
    }
    if(conType==<%=Constant.CONTRACT_TYPE_02%>) {
        for (var i = 0; i < price.length; i++) {
            var val = document.getElementById("CONTRACT_PRICE" + price[i].value + "").value;
            if (val == "") {
                MyAlert("合同价不可为空");
                return;
            }
        }
    }
    checkForm();

}

function showPart() {
    var val=document.getElementById("VENDER_ID").value;
    if(val==""){
        MyAlert("请选择供应商!");
        return;
    }
    var url = '<%=contextPath%>/parts/baseManager/PartContractManager/PartContractQuery/queryPartsForAddInit.do?VENDER_ID='+val ;
    OpenHtmlWindow(url, 700, 500);
}




function myValue(obj) {
    if (obj.value == '') {
        obj.value = '10041002';
    } else {
        obj.value = '10041001';
    }
}
//数据验证
function dataTypeCheck(obj) {
    var value = obj.value;
    if (isNaN(value)) {
        ("请输入数字!");
        obj.value = "";
        return;
    }
    var re = /^[1-9]+[0-9]*]*$/;
    if (!re.test(obj.value)) {
        ("请输入正整数!");
        obj.value = "";
        return;
    }
}
function  checkAppNum(obj){
    var pattern = /^([a-zA-Z0-9]|[-]){0,100}$/;
    if (!pattern.exec(obj.value)){
        MyAlert("不能输入数字,字母,'/'以外的字符.");
        obj.value="";
        return ;
    }

}

function is_double(obj){
    if(obj.value!=""){
        var pattern = /^\d+(\.\d+)?$/;
        if (!pattern.exec(obj.value)){
            MyAlert("输入有误，请重新输入");
            obj.value="";
            return;
        }
    }

}

function deleteTblRow(obj, rowNum) {
    var tbl = document.getElementById(obj);
    tbl.deleteRow(rowNum);
    var count = tbl.rows.length;
    for (var i = rowNum; i <= count; i++) {
        tbl.rows[i].cells[0].innerText = i;
        if (obj == 'add_tab') {
            tbl.rows[i].cells[5].innerHTML = '<input type="button" class="normal_btn" value="删 除" onclick="deleteTblRow(\'' + obj + '\' ,' + i + ');" / > '
        } else {
            tbl.rows[i].cells[3].innerHTML = '<input type="button" class="normal_btn" value="删 除" onclick="deleteTblRow(\'' + obj + '\' ,' + i + ');" / > '
        }
        if ((i + 1) % 2 == 0) {
            tbl.rows[i].className = "table_list_row1";
        } else {
            tbl.rows[i].className = "table_list_row2";
        }
    }
}

function hideImportBtn(_this) {
    var uploadDiv = document.myIframe.uploadDiv;
    if (_this.value == 95621001) {
        $("#saveBtn").show();
    } else {
        $("#saveBtn").show();
    }

}

function HIDE_BAND_ACT_CODE(_this) {
    if (_this.value == 95621001) {
        $("#band1").show();
        $("#band2").show();
    } else {
        $("#band1").hide();
        $("#band2").hide();
    }
}

function showUpload() {
    var uploadDiv = document.myIframe.uploadDiv;
    if (uploadDiv.style.display == "block") {
        uploadDiv.style.display = "none";
    } else {
        uploadDiv.style.display = "block";
    }
}
function exportExcelTemplate() {
    fm.action = "<%=contextPath%>/parts/baseManager/activityPartSet/activityPartSetAction/exportExcelTemplate.do";
    fm.submit();
}
function deleteTabCell(_this) {
	if (<%=Constant.PART_ACTIVITY_TYPE_REPLACED_01 %> == parseInt(_this.value)){
        $("#repartCode").show();
        $("#repartCname").show();
        $("#isneedFlag").show();
        $("#isNormal").show();
    } else {
        $("#repartCode").hide();
        $("#repartCname").hide();
        $("#isneedFlag").hide();
        $("#isNormal").hide();
    }
    var tb = document.getElementById('add_tab');
    var rowNum = tb.rows.length;
    for (i = 0; i < rowNum; i++) {
        if (i > 0) {
            tb.deleteRow(i);
            rowNum = rowNum - 1;
            i = i - 1;
        }
    }
}

function checkdlrSelect() {
    var dlrSelect = document.getElementById("dlrSelect");
    var dlr_tab = document.getElementById("dlr_tab");

    if (dlrSelect.checked) {
        dlrSelect.checked = false;
        dlr_tab.style.display = "block";
    } else {
    }
}

function dlrSelectChecked() {
    var dlrSelect = document.getElementById("dlrSelect");
    var dlr_tab = document.getElementById("dlr_tab");
    if (dlrSelect.checked) {
        dlr_tab.style.display = "none";
    } else {
        dlr_tab.style.display = "block";
    }
}
</script>
</head>
<body>
<div id="wbox" class="wbox">
<form name='fm' id='fm'  method="post" enctype="multipart/form-data">
	<div class="navigation">
	    <img src="<%=contextPath%>/img/nav.gif"/>&nbsp;当前位置：  配件管理 &gt; 基础信息管理 &gt;供应商管理 &gt;合同管理&gt;修改
	</div>
	<input type="hidden" id="dlrSelectVal" name="dlrSelectVal" value="1"/>
   	<div class="form-panel">
        <h2><img class="panel-icon" src="<%=contextPath%>/img/subNav.gif"/>修改信息</h2>
        <div class="form-body">
	        <table class="table_query">
	            <tr>
	            <tr>
	                <td class="right"">合同编号：</td>
	                <td>
	                    <input name="CONTRACT_NUMBER" type="hidden" value="${stringObjectMap.CONTRACT_NUMBER}"/>
	                    <input class="middle_txt" type="text" name="NCONTRACT_NUMBER"  value="${stringObjectMap.CONTRACT_NUMBER}" onblur="checkAppNum(this);"  id="CONTRACT_NUMBER"/> <font color="red">*</font>
	                </td>
	                <td class="right"">合同类型：</td>
	                	<input type="hidden" name="CONTRACT_TYPE1" id="CONTRACT_TYPE1" value="${stringObjectMap.CONTRACT_TYPE}" />
	                <td align="left">
	                    <script type="text/javascript">
	                        genSelBox("CONTRACT_TYPE", <%=Constant.CONTRACT_TYPE%>, "${stringObjectMap.CONTRACT_TYPE}", true, "u-select u-disabled", "disabled", "false", '');
	                    </script>
	                </td>
	            </tr>
	            <tr>
		            <td class="right"">供应商名称：</td>
		            <td>
		                <input class="middle_txt" type="hidden" name="VENDER_ID" value="${stringObjectMap.VENDER_ID}"  />
		                <input class="middle_txt" style="border:0;background:transparent; width: 240px;" type="text"  value="${stringObjectMap.VENDER_NAME}" readonly="readonly"/>
		            </td>
	                <td class="right"">合同有效期：</td>
	                <td><input name="checkSDate" type="text" class="middle_txt" id="checkSDate" style="width:80px" value="${stringObjectMap.CONTRACT_SDATE}"/>
	                    <input name="button2" value=" " type="button" class="time_ico" />
	                    	至
	                    <input name="checkEDate" type="text" class="middle_txt" id="checkEDate" style="width:80px" value="${stringObjectMap.CONTRACT_EDATE}"/>
	                    <input name="button2" value=" " type="button" class="time_ico"/>
	                </td>
	            </tr>
	            <tr>
					<td class="right"">是否临时：</td>
					<td >
						<script type="text/javascript">
							genSelBox("ISTEMP", <%=Constant.IF_TYPE%>, "${stringObjectMap.ISTEMP}", true, "");
						</script>
					</td>
				</tr>
	            <tr>
	                <td class="right"">备注：</td>
	                <td  colspan="3">
	                    <textarea class="form-control align" style="width: 80%;" name="remark" id="remark" rows="3" cols="60"  datatype="1,is_textarea,200" >${stringObjectMap.REMARK}</textarea>
	                </td>
	            </tr>
	            <tr>
	                <td class="center" colspan="4">
						<input type="button" name="saveBtn" id="saveBtn" value="保 存" onclick="checkFormUpdate()" class="u-button"/>
						<input type="button" name="backBtn" id="backBtn" value="返 回" onclick="goBack()" class="u-button"/>
	                </td>
	            </tr>
	        </table>
        </div>
    </div>
    <jsp:include page="${contextPath}/queryPage/orderHidden.html"/>
    <jsp:include page="${contextPath}/queryPage/pageDiv.html"/>
</form>
</div>
</script>
</body>
</html>
