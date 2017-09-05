<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c"%>
<%
    String contextPath = request.getContextPath();
%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta http-equiv="X-UA-Compatible" content="IE=7">
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>配件货位移位-移位</title>
<script type="text/javascript">
var myPage;
var url = "<%=contextPath%>/parts/purchaseOrderManager/PartDisplacement/queryInIdInfo.json";
var title = null;
var columns = [
    {header: "序号", align: 'center', renderer: getIndex},
    {
        header: "<input type='checkbox'  name='ckbAll' id='ckbAll' checked onclick='selAll(this)'  />",
        dataIndex: 'PART_ID',
        align: 'center',
        width: '33px',
        renderer: seled
    },
    {header: "配件编码", dataIndex: 'PART_OLDCODE', align: 'center'},
    {header: "配件名称", dataIndex: 'PART_CNAME', align: 'center'},
    {header: "入库数量", dataIndex: 'IN_QTY', align: 'center'},
    {header: "批次", dataIndex: 'BATCH_NO', align: 'center'},
    {header: "货位", dataIndex: 'LOC_CODE', align: 'center'},
    {header: "单位", dataIndex: 'UNIT', align: 'center'},
    {header: "可移位数量", dataIndex: 'STOCK_QTY', align: 'center'},
    {header: "移位数量", dataIndex: 'STOCK_QTY', align: 'center', renderer: insertInQtyInput},
    {header: "移位货位", dataIndex: 'LOC_CODE', style: 'text-align:left', renderer: choice},
    {header: "备注", dataIndex: 'REMARK', align: 'center', renderer: remarkInput}
];
function selAll(obj) {
    var cks = document.getElementsByName('ck');
    for (var i = 0; i < cks.length; i++) {
        if (obj.checked) {
            cks[i].checked = true;
        } else {
            cks[i].checked = false;
        }
    }
}
//货位弹出框
function choice(value, meta, record) {
    var partId = record.data.PART_ID;
    var locId = record.data.LOC_ID;
    var locCode = record.data.LOC_CODE;
    var whId = record.data.WH_ID;
    var whName = record.data.WH_NAME;
    var partName = record.data.PART_CNAME;
    var code = record.data.PART_OLDCODE;
    var html = "";
    //记录库房、机构、批次
    html += '<input type="hidden" id="WH_ID_'+partId+'" name="WH_ID_'+partId+'" value="'+whId+'" >';
    html += '<input type="hidden" id="ORG_ID_'+partId+'" name="ORG_ID_'+partId+'" value="'+record.data.ORG_ID+'" >';
    html += '<input type="hidden" id="BATCH_NO_'+partId+'" name="BATCH_NO_'+partId+'" value="'+record.data.BATCH_NO+'" >';
    //记录原来的货位和编码
    html += '<input type="hidden" id="OLD_LOC_ID_'+partId+'" name="OLD_LOC_ID_'+partId+'" value="'+locId+'" >';
    html += '<input type="hidden" id="OLD_LOC_CODE_'+partId+'" name="OLD_LOC_CODE_'+partId+'" value="'+locCode+'" >';
    //记录新货位和编码
    html += '<input readonly name="LOC_CODE_'+partId+'" id="LOC_CODE_'+partId+'" class="middle_txt" type="text" />';
    html += '<input name="LOC_ID_'+partId+'" id="LOC_ID_'+partId+'" type="hidden" value=""/>';
    html += '<input class="mini_btn" type="button" value="..."';
    html += 'onclick="codeChoice2(\''+partId+'\', \''+partId+'\',\''+code+'\', \''+partName+'\', \''+whId+'\');"/>';   
	return String.format(html);
}
function codeChoice2(partLocId, id, code, name,whId) {
    var url = g_webAppName + "/parts/baseManager/partsBaseManager/PartLocation/selectLocationInit.do";
    url += "?partLocId="+partLocId+"&partId="+id+"&partOldcode="+code+"&partCname="+name+"&whId="+whId;
    OpenHtmlWindow(encodeURI(url), 800, 400);
}
function seled(value, meta, record) {
    return "<input type='checkbox' value='" + value + "' name='ck' id='ck" + value + "' onclick='chkPart()' checked/>";
}

function chkPart() {
    var cks = document.getElementsByName('ck');
    var flag = true;
    for (var i = 0; i < cks.length; i++) {
        var obj = cks[i];
        if (!obj.checked) {
            flag = false;
        }
    }
    document.getElementById("ckbAll").checked = flag;
}


/* function insertChkQtyInput(value, meta, record) {
    var dtlId = record.data.DTL_ID;
    var partId = record.data.PART_ID;
    var output = '<input type="hidden" id="CHECK_QTY' + dtlId + '" name="CHECK_QTY' + dtlId + '" value="' + value + '"/>\n' + value
            + '<input type="hidden" id="PART_ID_' + dtlId + '"  name="PART_ID_' + dtlId + '" value="' + partId + '"/>\n';
    return output;
}
 */
//移位数量
function insertInQtyInput(value, meta, record) {
    var partId = record.data.PART_ID;
    var output = '<input type="hidden" id="ABLE_DIS_QTY_' + partId + '" name="ABLE_DIS_QTY_' + partId + '" value="' + record.data.STOCK_QTY + '"/>' +
            '<input type="text" class="short_txt" id="DIS_QTY_' + partId + '" name="DIS_QTY_' + partId + '" value="' + record.data.STOCK_QTY + '" size ="10" style="background-color:#FF9"/>\n';
    return output;
}
//备注
function remarkInput(value, meta, record) {
    var partId = record.data.PART_ID;
    var remark = '<input type="text" id="REMARK_' + partId + '" name="REMARK_' + partId + '" value="" class="middle_txt"/>' ;
    return remark;
}
 
/* function check(obj, partId) {
    var pattern1 = /^[1-9][0-9]*$/;
    if (!pattern1.exec(obj.value)) {
        obj.value = obj.value.replace(/\D/g, '');
        obj.focus();
    }
    if (isNumber(obj.value)) {
        if (obj.value == 0) {
            MyAlert("正整数且必须大于0！");
            obj.value = "";
            obj.focus();
            return;
        }
    }
    $("#ck" + partId)[0].checked = true;
    chkPart();
} */

//移位
function partDisplacement() {
    var ck = document.getElementsByName('ck');
    var l = ck.length;
    if ( l == 0) {
        MyAlert("请选择要移位的配件！");
        return;
    }
    for (var i = 0; i < l; i++) {
        if (ck[i].checked) {
            var partId = ck[i].value;
//             var locId = document.getElementById("LOC_ID_" + partId).value;//移位后的货位ID
            var oldLocCode = document.getElementById("OLD_LOC_CODE_" + partId).value;//移位后的货位编码
            var locCode = document.getElementById("LOC_CODE_" + partId).value;//移位后的货位编码
            var ableDisQty = document.getElementById("ABLE_DIS_QTY_" + partId).value;//可移位数量
            var disQty = document.getElementById("DIS_QTY_" + partId).value;//移位数量

            var pattern1 = /^[1-9][0-9]*$/;
            if (!pattern1.exec(disQty)) {
                MyAlert("第" + (i + 1) + "行的移位数量只能输入非零的正整数!");
                return;
            }
            if (parseInt(disQty) > parseInt(ableDisQty)) {
                MyAlert("第" + (i + 1) + "行的移位数量不能大于可移位数量：" + ableDisQty + "！");
                return;
            } 
            if (locCode == '') {
                MyAlert("第" + (i + 1) + "行的移位货位不能为空!");
                return;
            }
            if (oldLocCode == locCode) {
                MyAlert("第" + (i + 1) + "行的货位和原货位相同，如需移位请先维护货位信息!");
                return;
            }
        }
    }
    MyAlert("确定移位?", function(){
        btnDisable();
        var url = '<%=contextPath%>/parts/purchaseOrderManager/PartDisplacement/saveDisplacement.json';
        makeNomalFormCall(url, getResult, 'fm'); 
    });
}

function getResult(jsonObj) {
	btnEnable();
    if (jsonObj != null) {
        var success = jsonObj.success;
        var errors = jsonObj.errors;
        var exceptions = jsonObj.Exception;
        if (success) {
            MyAlert(success);
            window.location.href = '<%=contextPath%>/parts/purchaseOrderManager/PartDisplacement/partDisplacementInit.do';
        }else if (errors && errors != "") {
            MyAlert(errors);
            window.location.href = '<%=contextPath%>/parts/purchaseOrderManager/PartDisplacement/partDisplacementInit.do';
        }else if (exceptions) {
            MyAlert(exceptions.message);
        }
    }
}
//返回查询页面
function goback1() {
    window.location.href = '<%=contextPath%>/parts/purchaseOrderManager/PartDisplacement/partDisplacementInit.do';
}

$(function(){
 	__extQuery__(1); 
});

</script>
</head>
<body>
	<div class="wbox">
		<div class="navigation">
			<img src="<%=contextPath%>/img/nav.gif"/>&nbsp;当前位置:配件管理 > 采购订单管理 &gt;配件货位移位 &gt;移位
		</div>
		<form name="fm" id="fm" method="post">
			<input type="hidden" name="inId"   id="inId" value="${inId}" />
			<input type="hidden" name="WH_ID"   id="WH_ID" value="${whId}" />
			<div class="form-panel" style="margin-top: 2px; margin-bottom: 0px;">
				<h2 style="border-bottom: 0px;">
					<img class="nav" src="<%=contextPath%>/img/subNav.gif" /> 配件信息
				</h2>
			</div>
			<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
			<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
			<table class="table_query">
				<tr>
					<td class="center">
						<input class="u-button u-submit" type="button" name="saveBtn" id="saveBtn" value="移位" onclick="partDisplacement();" />
						&nbsp;
						<input class="u-button u-cancel" type="button" value="返 回" onclick="goback1();" />
						&nbsp;
					</td>
				</tr>
			</table>
		</form>
	</div>
</body>
</html>
