<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="com.infodms.dms.common.Constant" %>
<%@taglib uri="/jstl/cout" prefix="c" %>
<%
    String contextPath = request.getContextPath();
%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp"/>
<title>配件主信息维护</title>
<script type="text/javascript">
var myPage;

var url = "<%=contextPath%>/parts/baseManager/PartBaseQuery/queryPartBaseInfo.json";

    var title = null;

    var columns = [
        {header: "序号", style: "text-align: center", renderer: getIndex},
        {
            id: 'action',
            header: "操作",
            sortable: false,
            dataIndex: 'PART_ID',
            renderer: myLink,
            style: "text-align: left"
        },
        	
        {header: "配件编码", dataIndex: 'PART_OLDCODE', style: "text-align: center", width: '3%'},
        {header: "配件名称", dataIndex: 'PART_CNAME', style: "text-align: center", width: '5%'},
        {header: "件号", dataIndex: 'PART_CODE', style: "text-align: center", width: '3%'},
        {header: "英文名称", dataIndex: 'PART_ENAME', style: "text-align: center", width: '3%'},
        {header: "单位", dataIndex: 'UNIT', style: "text-align: center"},
        {header: "采购方式", dataIndex: 'PRODUCE_FAC', style: "text-align: center", width: '5%', renderer: getItemValue},
        {header: "单车用量", dataIndex: 'VEHICLE_VOLUME', style: "text-align: center", width: '5%'},
        {header: "是否停用", dataIndex: 'IS_PART_DISABLE', style: "text-align: center", width: '5%', renderer: getItemValue},
        {header: "停用日期", dataIndex: 'PART_DISABLE_DATE', style: "text-align: center", width: '5%'},
        {header: "是否售完停用", dataIndex: 'IS_SALE_DISABLE', style: "text-align: center", width: '5%', renderer: getItemValue},
        {header: "售完停用日期", dataIndex: 'SALE_DISABLE_DATE', style: "text-align: center", width: '5%'},
        {header: "是否停止装车", dataIndex: 'IS_STOP_LOAD', style: "text-align: center", width: '5%', renderer: getItemValue},
        {header: "停止装车日期", dataIndex: 'STOP_LOAD_DATE', style: "text-align: center", width: '5%'},
        {header: "配件种类", dataIndex: 'PRODUCE_STATE', style: "text-align: center", width: '5%', renderer: getItemValue},
        {header: "装配", dataIndex: 'PART_FIT', style: "text-align: center", width: '5%', renderer: getItemValue},
        {header: "配件类别", dataIndex: 'PART_CATEGORY', style: "text-align: center", width: '5%', renderer: getItemValue},
        {header: "车系", dataIndex: 'MODEL_CODE', style: "text-align: center", width: '5%'},
        {header: "适用车型", dataIndex: 'MODEL_NAME', style: "text-align: center", width: '5%'},
        {header: "第一次入库时间", dataIndex: 'FIRST_WARNHOUSE_DATE', style: "text-align: center", width: '5%'},
        {header: "是否协议包装", dataIndex: 'IS_PROTOCOL_PACK', style: "text-align: center", width: '5%', renderer: getItemValue},
        {header: "是否委托发货", dataIndex: 'IS_ENTRUSR_PACK', style: "text-align: center", width: '5%', renderer: getItemValue},
        {header: "最小销售数量", dataIndex: 'MIN_SALE', style: "text-align: center", width: '5%'},
        {header: "最小采购数量", dataIndex: 'MIN_PURCHASE', style: "text-align: center", width: '5%'},
        {header: "包装规格", dataIndex: 'PACK_SPECIFICATION', style: "text-align: center", width: '5%'},
        {header: "长（mm)", dataIndex: 'LENGTH', style: "text-align: center", width: '5%'},
        {header: "宽（mm)", dataIndex: 'WIDTH', style: "text-align: center", width: '5%'},
        {header: "高（mm)", dataIndex: 'HEIGHT', style: "text-align: center", width: '5%'},
        {header: "体积(mm3)	", dataIndex: 'VOLUME', style: "text-align: center", width: '5%'},
        {header: "重量(KG)", dataIndex: 'WEIGHT', style: "text-align: center", width: '5%'},
        // {header: "代用件号", dataIndex: 'OTHER_PART_ID', style: "text-align: center", width: '5%'},
        {header: "3C标识", dataIndex: 'CCC_FLAG', style: "text-align: center", width: '5%', renderer: getItemValue},
//         {header: "${map.prciceName1}(元)", dataIndex: 'SALE_PRICE1', style: "text-align: center", width: '5%'},
//         {header: "${map.prciceName2}(元)", dataIndex: 'SALE_PRICE2', style: "text-align: center", width: '5%'},
//         {header: "${map.prciceName3}(元)", dataIndex: 'SALE_PRICE3', style: "text-align: center", width: '5%'},
//         {header: "${map.prciceName4}(元)", dataIndex: 'SALE_PRICE4', style: "text-align: center", width: '5%'},
//         {header: "${map.prciceName5}(元)", dataIndex: 'SALE_PRICE5', style: "text-align: center", width: '5%'},
//         {header: "${map.prciceName6}(元)", dataIndex: 'SALE_PRICE6', style: "text-align: center", width: '5%'},
//         {header: "${map.prciceName7}(元)", dataIndex: 'SALE_PRICE7', style: "text-align: center", width: '5%'},
//         {header: "价格有效开始日期", dataIndex: 'PRICE_START', style: "text-align: center", width: '5%'},
//         {header: "价格有效截止日期", dataIndex: 'PRICE_END', style: "text-align: center", width: '5%'},
//         {header: "是否批次管理", dataIndex: 'IS_MAG_BATCH', style: "text-align: center", width: '5%', renderer: getItemValue},
        {header: "最大销售数量", dataIndex: 'MAX_SALE_VOLUME', style: "text-align: center", width: '5%'},
//         {header: "暂估价", dataIndex: 'PROVISIONAL_PRICE', style: "text-align: center", width: '5%'},
        /* {header: "配件编码", dataIndex: 'PART_OLDCODE', style: "text-align: ", width: '3%'},
        {header: "配件名称", dataIndex: 'PART_CNAME', style: "text-align: ", width: '5%'},
        {header: "件号", dataIndex: 'PART_CODE', style: "text-align: left", width: '3%'},
        {header: "英文名称", dataIndex: 'PART_ENAME', style: "text-align: left", width: '3%'},
        {header: "分类代码", dataIndex: 'SORT_CODE', style: "text-align: center"},
        {header: "分组代码", dataIndex: 'GROUP_CODE', style: "text-align: center"},
        {header: "发动机标记", dataIndex: 'IS_ENGINE', style: "text-align: center", renderer: getItemValue},
        {header: "是否出口", dataIndex: 'IS_EXPORT', style: "text-align: center", renderer: getItemValue},
        {header: "是否为赠品", dataIndex: 'IS_GIFT', style: "text-align: center", renderer: getItemValue},
        {header: "配件种类", dataIndex: 'PART_TYPE', style: "text-align: center", renderer: getItemValue},
        {header: "是否常备", dataIndex: 'IS_PERMANENT', renderer: getItemValue, renderer: getItemValue},
        {header: "是否可替代", dataIndex: 'IS_REPLACED', renderer: getItemValue, renderer: getItemValue},
        {header: "自制/外购", dataIndex: 'PRODUCE_STATE', style: "text-align: center", renderer: getItemValue},
        {header: "配件品牌", dataIndex: 'PRODUCE_FAC', style: "text-align: center", renderer: getItemValue},
        {header: "包装发运方式", dataIndex: 'PACK_STATE', style: "text-align: center", renderer: getItemValue},
        {header: "材料属性", dataIndex: 'PART_MATERIAL', style: "text-align: center", renderer: getItemValue},
        {header: "流动性", dataIndex: 'MOBILITY', style: "text-align: center", renderer: getItemValue},
        {header: "替代件编码", dataIndex: 'REPART_CODE', style: "text-align: center"},
        {header: "单位", dataIndex: 'UNIT', style: "text-align: center"},
        {header: "是否总成", dataIndex: 'IS_ASSEMBLY', style: "text-align: center", renderer: getItemValue},
        {header: "采购状态", dataIndex: 'BUY_STATE', style: "text-align: center", renderer: getItemValue},
        {header: "适用车系", dataIndex: 'SERIES_NAME', style: "text-align: center;"},
        {header: "适用车型", dataIndex: 'MODEL_NAME', style: "text-align: center;"},
        {header: "所属大类", dataIndex: 'POSITION', style: "text-align: left;", renderer: getItemValue},
        {header: "最小订货量(供)", dataIndex: 'MIN_PACK1', style: "text-align: center"},
        {header: "最小订货量(服)", dataIndex: 'MIN_PACK2', style: "text-align: center"},
        {header: "配件类别代码", dataIndex: 'PART_TPCODE', style: "text-align: center"},
        {header: "宽度", dataIndex: 'WIDTH', style: "text-align: center"},
        {header: "深度", dataIndex: 'DEPTH', style: "text-align: center"},
        {header: "高度", dataIndex: 'HEIGHT', style: "text-align: center"},
        {header: "净重", dataIndex: 'WEIGHT', style: "text-align: center"},
        {header: "体积", dataIndex: 'VOLUME', style: "text-align: center"},
        {header: "结算基地", dataIndex: 'PART_IS_CHANGHE', style: "text-align: center", renderer: getItemValue},
        {header: "是否可随车", dataIndex: 'IS_SC', style: "text-align: center", renderer: getItemValue},
        {header: "是否广宣品", dataIndex: 'IS_GXP', style: "text-align: center", renderer: getItemValue},
        {header: "是否三包索赔急件", dataIndex: 'IS_SPJJ', style: "text-align: center", renderer: getItemValue},
        {header: "备注", dataIndex: 'REMARK', style: "text-align: left"},
//         {header: "计划员", dataIndex: 'PLANER_ID_USER', style: "text-align: center"},
//         {header: "采购员", dataIndex: 'BUYER_ID_USER', style: "text-align: center"},
        {header: "是否有效", dataIndex: 'STATE', style: "text-align: center", renderer: getItemValue},
        {header: "新增日期", dataIndex: 'CREATE_DATE', style: "text-align: center"} */

    ];

    //设置超链接  begin


    //设置超链接
    function myLink(value, meta, record) {
        if (record.data.STATE == <%=Constant.STATUS_ENABLE%>) {
        return String.format("<a href=\"#\" onclick='view(\"" + value + "\")'>[查看]</a>" + "<a href=\"#\" onclick='Mod(\"" + value + "\")'>[修改]</a>" + "<a href=\"#\" onclick='abate(\"" + value + "\")'>[失效]</a>" + "<a href=\"#\" onclick='detailHis(\"" + value + "\")'>[查看修改历史]</a>");
    } else {
        return String.format("<a href=\"#\" onclick='view(\"" + value + "\")'>[查看]</a>" + "<a href=\"#\" onclick='enable2(\"" + value + "\")'>[有效]</a>" + "<a href=\"#\" onclick='detailHis(\"" + value + "\")'>[查看修改历史]</a>");
    }
}

//详细页面
function view(value) {
    disableAllClEl();
    OpenHtmlWindow('<%=contextPath%>/parts/baseManager/PartBaseQuery/queryPartBaseDetail.do?flag=view&&partId=' + value, 1100, 500, '配件主数据维护');
    enableAllClEl();
}

//修改页面
function Mod(value) {
    disableAllClEl();
    OpenHtmlWindow('<%=contextPath%>/parts/baseManager/PartBaseQuery/queryPartBaseDetail.do?flag=mod&&partId=' + value, 1100, 500, '配件主数据维护');
    enableAllClEl();
}
function detailHis(value) {
    disableAllClEl();
    OpenHtmlWindow('<%=contextPath%>/parts/baseManager/PartBaseQuery/detailHis.do?partId=' + value, 1100, 350, '配件主数据维护');
    enableAllClEl();
}
function abate(value) {
    MyConfirm("确定要失效?", abateAction, [value], null, null);
}
//失效
function abateAction(value) {
    disableAllClEl();
    var abateUrl = '<%=contextPath%>/parts/baseManager/PartBaseQuery/queryPartBaseDetail.json?flag=abate&&partId=' + value + '&curPage=' + myPage.page;
    makeNomalFormCall(abateUrl, veiwParts, 'fm');
}
function enable2(value) {
    MyConfirm("确定有效?", enableAction, [value]);
}
//有效
function enableAction(value) {
    disableAllClEl();
    var abateUrl = '<%=contextPath%>/parts/baseManager/PartBaseQuery/queryPartBaseDetail.json?flag=enable&&partId=' + value + '&curPage=' + myPage.page;
    makeNomalFormCall(abateUrl, veiwParts, 'fm');
}
function veiwParts(jsonObj) {
    enableAllClEl();
    if (jsonObj != null) {
        var success = jsonObj.success;
        MyAlert(success);
        __extQuery__(jsonObj.curPage);
    }
}

// 新增
function partAdd() {
    disableAllClEl();
    OpenHtmlWindow('<%=contextPath%>/parts/baseManager/PartBaseQuery/partBaseAdd.do?', 1100, 500, '新增配件');
    enableAllClEl();
}

//清除供应商代码
function reset() {
    document.getElementById("SUPPLIER_CODE").value = "";
}

function exportExcelTemplate(flag) {
    document.getElementById("flag").value = flag;
    fm.action = "<%=contextPath%>/parts/baseManager/PartBaseQuery/exportExcelTemplate.do";
    fm.submit();
}
function uploadEx(flag) {
    var fileValue;
    if (flag == '1') {
        fileValue = document.getElementById("uploadFile").value;
    } else {
        fileValue = document.getElementById("uploadFile2").value;
    }

    if (fileValue == '') {
        layer.msg('导入文件不能空!', {icon: 2});
        return;
    }
    var fi = fileValue.substring(fileValue.length - 3, fileValue.length);
    if (fi != 'xls') {
        layer.msg('导入文件格式不对,请导入xls文件格式', {icon: 2});
        return;
    }
    document.getElementById("flag").value = flag;
    layer.confirm("确定导入?", {btn: ['确定', '取消'], icon: 15}, function() {
        btnDisable();
        fm.action = "<%=contextPath%>/parts/baseManager/PartBaseQuery/uploadPartBaseExcel.do";
        fm.submit();
    });
}

function exportPartBaseExcel() {
    document.fm.action = "<%=contextPath%>/parts/baseManager/PartBaseQuery/exportPartBaseExcel.do";
    document.fm.target = "_self";
    document.fm.submit();

}

function disableAllA() {
    var inputArr = document.getElementsByTagName("a");
    for (var i = 0; i < inputArr.length; i++) {
        inputArr[i].disabled = true;
    }
}

function enableAllA() {

    var inputArr = document.getElementsByTagName("a");
    for (var i = 0; i < inputArr.length; i++) {
        inputArr[i].disabled = false;
    }
}
function disableAllBtn() {
    var inputArr = document.getElementsByTagName("input");
    for (var i = 0; i < inputArr.length; i++) {
        if (inputArr[i].type == "button") {
            inputArr[i].disabled = true;
        }
    }
}
function enableAllBtn() {
    var inputArr = document.getElementsByTagName("input");
    for (var i = 0; i < inputArr.length; i++) {
        if (inputArr[i].type == "button") {
            inputArr[i].disabled = false;
        }
    }
}
function disableAllClEl() {
    disableAllA();
    disableAllBtn();
}
function enableAllClEl() {
    enableAllBtn();
    enableAllA();
}
function showUpload() {
    $("#BtnuploadTable")[0].style.cssText = "background-color:red";
    if ($("#uploadTable")[0].style.display == "none") {
        $("#uploadTable")[0].style.display = "block";
    } else {
        $("#uploadTable")[0].style.display = "none";
        $("#BtnuploadTable")[0].style.cssText = "background-color:none";
    }
    $("#uploadTable2")[0].style.display = "none";
    $("#BtnuploadTable2")[0].style.cssText = "background-color:none";
}
function showlogic() {
    layer.open({
        type: 1,
        skin: 'layui-layer-rim', 
        area: ['420px', '240px'],
        title: '流动性计算公式',
        shade: false,
        content: '<ul class="note-li"><li>0: 未满一年不参与计算.</li><li>1：过去12个月都有销量.</li><li>2：过去8-11个月有销量.</li><li>3：过去5-7个月有销量.</li><li>4：过去1-4个月有销量.</li><li>5：未销售.</li></ul>'
    });
    /* $("#uploadTable")[0].style.display = "none"
    if ($("#showLogic")[0].style.display == "none") {
        $("#showLogic")[0].style.display = "block";
    } else {
        $("#showLogic")[0].style.display = "none";
    } */
}
function showUpload2() {
    $("#BtnuploadTable2")[0].style.cssText = "background-color:red";
    if ($("#uploadTable2")[0].style.display == "none") {
        $("#uploadTable2")[0].style.display = "block";
    } else {
        $("#uploadTable2")[0].style.display = "none";
        $("#BtnuploadTable2")[0].style.cssText = "background-color:none";
    }
    $("#uploadTable")[0].style.display = "none";
    $("#BtnuploadTable")[0].style.cssText = "background-color:none";
}
function succAlert() {
    if ('${flag}' != '') {
        MyAlert('${flag}');
    }
}
$(document).ready(function(){__extQuery__(1);enableAllClEl();succAlert();});
</script>
</head>
<body>
<div class="wbox">
   <div class="navigation"><img src="<%=contextPath%>/img/nav.gif"/>&nbsp;当前位置：配件管理 > 基础信息管理 > 配件基础信息维护 > 配件主数据维护</div>
    <form name="fm" id="fm" method="post" enctype="multipart/form-data">
        <input type="hidden" name="flag" id="flag"/>
        <div class="form-panel">
			<h2><img src="<%=contextPath%>/jmstyle/img/search-ico.png" style="margin-bottom: -13px;"/>查询条件</h2>
            <div class="form-body">
                <table class="table_query" width=100% border="0" align="center" cellpadding="1" cellspacing="1">
                    <tr>
                        <td class="right">配件编码：</td>
                        <td><input class="middle_txt" type="text" id="PART_OLDCODE" name="PART_OLDCODE"/></td>
                        <td class="right">配件名称：</td>
                        <td><input class="middle_txt" type="text" id="PART_CNAME" name="PART_CNAME"/></td>
                        <td class="right">件号：</td>
                        <td><input class="middle_txt" type="text" id="PART_CODE" name="PART_CODE"/></td>
                    </tr>
                    <tr>
                        <td  class="right">备注：</td>
                        <td><input class="middle_txt" type="text" id="remark" name="remark"/></td>
                        <td  class="right">是否有效：</td>
                        <td colspan="3">
                            <script type="text/javascript">
                                genSelBoxExp("STATE", <%=Constant.STATUS%>, "<%=Constant.STATUS_ENABLE%>", true, "", "", "false", '');
                            </script>
                        </td>
                    </tr>
                    <tr>
                        <td class="formbtn-aln" align="center" colspan="6">
                            <input class="u-button" type="button" name="queryBtn" id="queryBtn" value="查 询"onclick="__extQuery__(1)"/> &nbsp; 
							<input type="reset" class="u-button" value="重 置"/> &nbsp; 
                            <input class="u-button" type="button" value="新 增" onclick="partAdd();"/> &nbsp; 
                            <input class="u-button" type="button" name="BtnExportPartBaseExcel" value="导 出" onclick="exportPartBaseExcel()"/> &nbsp; 
                            <input class="u-button" type="button" id="BtnuploadTable" value="批量导入" onclick="showUpload();"/> &nbsp; 
                            <input class="u-button" type="button" value="查看计算公式" onclick="showlogic();"/> &nbsp; 
                        </td>
                    </tr>
                </table>
            </div>
        </div>
        <table id="uploadTable" style="display: none">
            <tr>
                <td>
                	<font color="red">
                    	<input type="button" class="normal_btn" value="下载模版" onclick="exportExcelTemplate('1')"/>
                    	文件选择后,点&quot;确定&quot;按钮,完成上传操作：&nbsp;
                    </font>
                    <input type="file" name="uploadFile" style="width: 250px" id="uploadFile" value=""/>
                    &nbsp;
                    <input type="button" id="upbtn" class="normal_btn" value="确定" onclick="uploadEx('1')"/>
                 </td>
            </tr>
        </table>
        <table id="uploadTable2" style="display: none">
            <tr>
                <td>
                	<font color="red">
                    	<input type="button" class="normal_btn" value="下载模版" onclick="exportExcelTemplate('2')"/>
                    	文件选择后,点&quot;确定&quot;按钮,完成上传操作：&nbsp;
                    </font>
                    <input type="file" name="uploadFile2" style="width: 250px" id="uploadFile2" value=""/>
                    &nbsp;
                    <input type="button" id="upbtn" class="normal_btn" value="确定" onclick="uploadEx('2')"/>
                 </td>
            </tr>
        </table>
        <table id="showLogic" style="display:none;background-color:#90d7ec">
            <tr>
                <td style="font-style: inherit">
                    	流动性计算公式：
                    </br>
                    0:未满一年不参与计算.
                    1：过去12个月都有销量.
                    2：过去8-11个月有销量.
                    3：过去5-7个月有销量.
                    4：过去1-4个月有销量.
                    5：未销售.
                    </br>
                </td>
            </tr>
        </table>

        <jsp:include page="${contextPath}/queryPage/orderHidden.html"/>
        <jsp:include page="${contextPath}/queryPage/pageDiv.html"/>
    </form> 
</div>    
</body>
</html>
