<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" pageEncoding="UTF-8" %>
<% String contextPath = request.getContextPath(); %>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
<%@taglib uri="/jstl/cout" prefix="c" %>
<jsp:include page="${path}/common/jsp_head_new.jsp"/>
<title>计划参数维护</title>
</head>
<body onload="planSetSearch()">
<div class="wbox">
    <div class="navigation"><img src="<%=request.getContextPath() %>/img/nav.gif"/>&nbsp;当前位置：基础数据管理 &gt; 计划相关参数维护 &gt;
            紧急计划参数维护
    </div>
    <form name="fm" id="fm" method="post" enctype="multipart/form-data">
        <div class="form-panel">
            <h2><img src="<%=contextPath%>/jmstyle/img/search-ico.png" class="panel-query-title" />查询条件</h2>
            <div class="form-body">
                <table class="table_query">
                    <input type="hidden" name="command" value="1"/>
                    <input type="hidden" name="pageUrl" id="pageUrl" value=""/>
                    <tr>
                        <td width="10%" align="right">配件编码：</td>
                        <td width="20%"><input class="middle_txt" type="text" name="PART_OLDCODE"/></td>
                        <td width="10%" align="right">配件名称：</td>
                        <td width="20%"><input class="middle_txt" type="text" name="PART_CNAME"/></td>
                        <td width="10%" align="right">配件仓库：</td>
                        <td width="20%">
                            <select name="WH_ID" id="WH_ID" class="short_sel u-select">
                                <option value="">-请选择-</option>
                                <c:forEach var="map" items="${list}">
                                    <option value="${map.WH_ID}">${map.WH_NAME}</option>
                                </c:forEach>
                            </select>
                            <font color="red">*</font>
                        </td>
                    </tr>
                    <tr>
                        <td width="10%" align="right">配件件号：</td>
                        <td width="20%"><input class="middle_txt" type="text" name="PART_CODE"/></td>
                        <td width="10%" align="right">类别：</td>
                        <td width="20%">
                            <select name="sort" class="short_sel u-select">
                                <option value="-1">-请选择-</option>
                                <c:if test="${mySort!=null}">
                                    <c:forEach items="${mySort}" var="list">
                                        <option value="${list.SORT_ID }">${list.SORT_TYPE }</option>
                                    </c:forEach>
                                </c:if>
                            </select>
                            <%-- <script type="text/javascript">
                                genSelBoxExp("STATE",<%=Constant.STATUS%>,"<%=Constant.STATUS_ENABLE %>",true,"short_sel","","false",'');
                            </script>--%>
                        </td>
                        <td width="10%" align="right">总安全库存金额：</td>
                        <td width="20%">
                            <input class="middle_txt" type="text" id="totalSfAmt" style="background-color: #99D775;" readonly="readonly" value=""/>
                        </td>
                    </tr>
                    <tr>
                        <td class="center" align="center" colspan="6">
                            <input class="u-button u-query" type="button" value="查 询" name="BtnQuery" id="queryBtn"
                                onclick="planSetSearch()"/>
                            <input class="u-button" type="button" value="导 出" onclick="download();"/>
                            <input class="u-button" type="button" value="批量导入" id="upload_button" name="button1"onclick="showUpload();">
                            <input class="long_btn u-button" type="button" value="查看计算公式" onclick="showlogic();"/>
                        </td>
                    </tr>
                </table>
                <table id="showLogic" style="display:none;">
                    <tr style="background-color:#90d7ec">
                        <td>
                            日均销量：前三月日均销量*权重+前六月日均销量*权重+前12月日均销量*权重（具体权重参见平均销量权重维护功能）.</br>
                            安全库存计算公式：安全系数*日均销量*安全周期.</br>
                            计划计算公式：安全库存+日均销量*(订货周期(天)+到货周期(天))+BO数量-可用库存-在途（已下订单单未入库数量）.</br>
                            紧急计划计算公式：安全库存+日均销量*(订货周期(天)+到货周期(天))+BO数量-可用库存-在途（已下订单单未入库数量）.</br>
                            紧急计划订货点：库存<=安全库存+日均销量*30/2.</br>
                            流动性计算公式：</br>
                            0:未满一年不参与计算.
                            1：过去12个月都有销量.
                            2：过去8-11个月有销量.
                            3：过去5-7个月有销量.
                            4：过去1-4个月有销量.
                            5：未销售.</br>
                        </td>
                    </tr>
                </table>
                <div style="display:none ; heigeht: 5px" id="uploadDiv">
                    <table  class="table_query">
                        <th colspan="6"><img class="nav" src="<%=request.getContextPath()%>/img/subNav.gif" /> 上传文件</th>
                        <tr>
                            <td colspan="2"><font color="red">
                                &nbsp;&nbsp;<input type="button" class="u-button" value="下载模版" onclick="exportExcelTemplate()" />
                                仓库&文件选择后,点&quot;确定&quot;按钮,完成上传操作：&nbsp;</font>
                                <input type="file" name="uploadFile" id="uploadFile" style="width: 250px" datatype="0,is_null,2000" value="" /> &nbsp;
                                <input type="button" id="upbtn" class="u-button" value="确 定" onclick="confirmUpload()" />
                            </td>
                        </tr>
                    </table>
                </div>
            </div>    
        </div>    
        
        <!-- 查询条件 end -->
        <!--分页 begin -->
        <jsp:include page="${contextPath}/queryPage/orderHidden.html"/>
        <jsp:include page="${contextPath}/queryPage/pageDiv.html"/>
        <!--分页 end -->
    </form>
</div>    

<script type="text/javascript">
var myPage;

var url = "<%=contextPath%>/parts/baseManager/partsPlanManager/PartPlanSetting/partPlanSettingQuery.json";

var title = null;

var columns = [
    {header: "序号",  style: 'text-align:left', renderer: getIndex, width: '1%'},
    {header: "配件编码", dataIndex: 'PART_OLDCODE',  style: 'text-align:left'},
    {header: "配件名称", dataIndex: 'PART_CNAME',  style: 'text-align:left'},
    {header: "配件件号", dataIndex: 'PART_CODE',  style: 'text-align:left'},
    {header: "前3月日均销量", dataIndex: 'QUARTER_QTY',  style: 'text-align:center'},
    {header: "前6月日均销量", dataIndex: 'HFYEAR_QTY',  style: 'text-align:center'},
    {header: "前12月日均销量", dataIndex: 'YEAR_QTY',  style: 'text-align:center'},
    {header: "日均销量", dataIndex: 'AVG_QTY',  style: 'text-align:center'},
    {header: "分类", dataIndex: 'PLAN_RATIO_TYPE',  style: 'text-align:center', renderer: returnSortSelect},
    {header: "安全系数", dataIndex: 'SAFTY_RATE',  style: 'text-align:center'},
    {header: "安全周期", dataIndex: 'SAFETY_CYCLE',  style: 'text-align:center'},
    {header: "安全库存", dataIndex: 'SAFETY_STOCK',  style: 'text-align:center',renderer: insertInput1},

    /*   {header: "最大库存", dataIndex: 'MAX_STOCK',  style: 'text-align:left',renderer: insertInput2},*/
    {header: "订货周期(天)", dataIndex: 'PLAN_CYCLE',  style: 'text-align:center', renderer: insertInput3},
    {header: "到货周期(天)", dataIndex: 'ARRIVE_CYCLE',  style: 'text-align:center', renderer: insertInput4},
    {header: "BO数量", dataIndex: 'BO_QTY',  style: 'text-align:center'},
    {header: "现场BO数量", dataIndex: 'LOC_BO_QTY',  style: 'text-align:center'},
    {header: "在途数量", dataIndex: 'ORDER_QTY',  style: 'text-align:center'},
    {header: "月计划数量", dataIndex: 'PLAN_QTY',  style: 'text-align:center'},
    {header: "当前库存", dataIndex: 'ITEM_QTY',  style: 'text-align:center'},
    {header: "仓库", dataIndex: 'WH_NAME',  style: 'text-align:center'},
    {header: "计划价", dataIndex: 'SALE_PRICE3',  style: 'text-align: right;'},
    {header: "安全库存金额", dataIndex: 'SF_AMOUNT',  style: 'text-align: right;'},
    /*   {header: "是否有效", dataIndex: 'STATE',  style: 'text-align:left',renderer:getItemValue},*/
    {id: 'action', header: "操作", sortable: false, dataIndex: 'PLAN_ID', renderer: myLink,  style: 'text-align:left'}
];

//设置超链接
function myLink(value, meta, record) {
    return String.format("<a href=\"#\" onclick='confirmSave(\"" + value + "\")'>[保存]</a>");
}
//文本框
function insertInput1(value, meta, record) {
    var output = '<input type="text" class="short_txt" id="SAFETY_STOCK' + (record.data.PLAN_ID) + '" name="SAFETY_STOCK' + (record.data.PLAN_ID) + '" value="' + value + '"  style="background-color:#FF9;ime-mode:Disabled;text-align:right"  onkeyup="check(SAFETY_STOCK' + (record.data.PLAN_ID) + ');"><input type="hidden" name="OLD_SAFETY_STOCK' + (record.data.PLAN_ID)+'" value="' + value+'"/>';
    return output;
}
function insertInput2(value, meta, record) {
    var output = '<input type="text" class="short_txt" id="MAX_STOCK' + (record.data.PLAN_ID) + '" name="MAX_STOCK' + (record.data.PLAN_ID) + '" value="' + value + '" style="background-color:#FF9;ime-mode:Disabled;text-align:right"  onkeyup="check(MAX_STOCK' + (record.data.PLAN_ID) + ');">';
    return output;
}
function insertInput3(value, meta, record) {
    var output = '<input type="text" class="short_txt" id="PLAN_CYCLE' + (record.data.PLAN_ID) + '" name="PLAN_CYCLE' + (record.data.PLAN_ID) + '" value="' + value + '"  style="background-color:#FF9;ime-mode:Disabled;text-align:right"  onkeyup="check(PLAN_CYCLE' + (record.data.PLAN_ID) + ');">';
    return output;
}
function insertInput4(value, meta, record) {
    var output = '<input type="text" class="short_txt" id="ARRIVE_CYCLE' + (record.data.PLAN_ID) + '" name="ARRIVE_CYCLE' + (record.data.PLAN_ID) + '" value="' + value + '"  style="background-color:#FF9;ime-mode:Disabled;text-align:right"  onkeyup="check(ARRIVE_CYCLE' + (record.data.PLAN_ID) + ');">';
    return output;
}
function insertInput5(value, meta, record) {
    var output = '<input type="text" class="short_txt" id="SAFETY_CYCLE' + (record.data.PLAN_ID) + '" name="SAFETY_CYCLE' + (record.data.PLAN_ID) + '" value="' + value + '"  style="background-color:#FF9;ime-mode:Disabled"  onkeyup="check(SAFETY_CYCLE' + (record.data.PLAN_ID) + ');">';
    return output;
}

//查询
function planSetSearch()
{
    getTotalSfAmount();
    window.setTimeout(function () { __extQuery__(1); }, 300);
}

//获取总安全库存金额
function getTotalSfAmount()
{
    var url = '<%=contextPath%>/parts/baseManager/partsPlanManager/PartPlanSetting/getTotalSfAmount.json';
    sendAjax(url, getTotalSfAmountRes, 'fm');
}

function getTotalSfAmountRes(json) {
    var obj = document.getElementById("totalSfAmt");
    if (null != json.totalSfAmount && json.totalSfAmount.length > 0) {
        obj.value = json.totalSfAmount.toString();
    } else {
        obj.value = "0.00";
    }
}

function confirmSave(value)
{
    MyConfirm("确定保存设置?",save,[value]);
}
//保存
function save(value) {
    btnDisable();
    var url = '<%=contextPath%>/parts/baseManager/partsPlanManager/PartPlanSetting/partPlanSave.json?PLAN_ID=' + value + '&curPage=' + myPage.page;
    sendAjax(url, showResult, 'fm');

}
function showResult(json) {
    btnEnable();
    if (json.success == 'success') {
        MyAlert("保存成功");
        __extQuery__(json.curPage);
    } else {
        MyAlert(json.error);
    }
}

//下载
function download() {
    fm.action = '<%=contextPath%>/parts/baseManager/partsPlanManager/PartPlanSetting/partPlanDownload.do';
    fm.submit();
}
var sortArray = new Array(); //创建一个新的计划员数组
<c:forEach var= "list" items="${mySort}"> //得到有数据的数组集合
sortArray.push(['${list.SORT_ID}&&${list.SORT_TYPE}']);//得到数组的内容（实体bean)加入到新的数组里面
</c:forEach>
function addSortList(parms) {
    var obj = document.getElementById(parms);
    if (obj.options.children.length < 2) {
        var strTemp;
        var strsTemp = new Array();
        for (var i = 0; i < sortArray.length; i++) {
            strTemp = sortArray[i].toString();
            //定义一数组
            strsTemp = strTemp.split("&&"); //字符分割
            var uID = strsTemp[0];
            var uName = strsTemp[1];
            if (uID != obj.options.children[0].value) {
                obj.options.add(new Option(uName, uID));
            }
        }
    }
}
//设置下拉框
function returnSortSelect(value, meta, record) {
    var pId = record.data.PLAN_ID;
    var str = "<select class=\"mini_sel\" size = '1' id = 'PLAN_RATIO" + pId + "'  name = 'PLAN_RATIO" + pId + "'onmouseover='addSortList(\"PLAN_RATIO" + pId + "\")' onclick='addSortList(\"PLAN_RATIO" + pId + "\")'><option value='" + record.data.PLAN_RATIO + "'>" + value + "</option>";
    str = str + "</select>";
    return String.format(str);
}
function check(value) {
    if (isNaN($(value).value)) {
        MyAlert("请录入正整数!");
        $(value).value = $(value).value.replace(/\D/g, '');
        $(value).focus();
    }
}
function showlogic(){
    /* $("uploadDiv").style.display = "none"
    if ($("showLogic").style.display == "none") {
        $("showLogic").style.display = "block";
    } else {
        $("showLogic").style.display = "none";
    } */

    layer.open({
        type: 1,
        skin: 'layui-layer-rim', 
        area: ['420px', '240px'],
        title: '流动性计算公式',
        shade: false,
        content: '<ul class="note-li"><li>日均销量：前三月日均销量*权重+前六月日均销量*权重+前12月日均销量*权重（具体权重参见平均销量权重维护功能）.</li><li>安全库存计算公式：安全系数*日均销量*安全周期.</li><li>计划计算公式：安全库存+日均销量*(订货周期(天)+到货周期(天))+BO数量-可用库存-在途（已下订单单未入库数量）.</li><li>紧急计划计算公式：安全库存+日均销量*(订货周期(天)+到货周期(天))+BO数量-可用库存-在途（已下订单单未入库数量）.</li><li>紧急计划订货点：库存<=安全库存+日均销量*30/2.</li><li>流动性计算公式：<ul class="ch"><li>0:未满一年不参与计算.</li><li>1：过去12个月都有销量.</li><li>2：过去8-11个月有销量.</li><li>3：过去5-7个月有销量.</li><li> 4：过去1-4个月有销量.</li><li> 5：未销售.</li></ul>'
    });
}

//上传
function uploadExcel(){
    btnDisable();
    $("pageUrl").value = "<%=contextPath%>/parts/baseManager/partsPlanManager/PartPlanSetting/partPlanSettingInit.do";
    fm.action = "<%=contextPath%>/parts/baseManager/partsPlanManager/PartPlanSetting/planSettingUpload.do";
    fm.submit();
}

//上传检查和确认信息
function confirmUpload()
{
    if("" == $("WH_ID").value)
    {
        MyAlert("请选择导入的仓库!")
        return false;
    }

    if(fileVilidate())
    {
        MyConfirm("确定导入选择的文件?",uploadExcel,[]);
    }

}

function fileVilidate(){
    var importFileName = $("uploadFile").value;
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
    $("showLogic").style.display = "none";
    if(uploadDiv.style.display=="block" ){
        uploadDiv.style.display = "none";
    }else {
        uploadDiv.style.display = "block";
    }
}

//下载上传模板
function exportExcelTemplate(){
    fm.action = "<%=contextPath%>/parts/baseManager/partsPlanManager/PartPlanSetting/exportExcelTemplate.do";
    fm.submit();
}

//失效按钮
function btnDisable(){

    $$('input[type="button"]').each(function(button) {
        button.disabled = true;
    });

}

//有效按钮
function btnEnable(){

    $$('input[type="button"]').each(function(button) {
        button.disabled = "";
    });

}
</script>
</body>
</html>
