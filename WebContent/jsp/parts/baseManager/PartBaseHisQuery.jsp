<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c" %>
<%
    String contextPath = request.getContextPath();
%>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp"/>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <title>配件修改历史</title>
</head>
<script type="text/javascript">
var myPage;

var url = '<%=contextPath%>/parts/baseManager/PartBaseQuery/detailHisJson.json?partId=' + '${partId}';

var title = null;

var columns = [
    {header: "序号", style: "text-align: left", renderer: getIndex},
    {header: "配件编码", dataIndex: 'PART_OLDCODE', style: "text-align: center", width: '5%'},
    {header: "配件名称", dataIndex: 'PART_CNAME', style: "text-align: center", width: '5%'},
    {header: "件号", dataIndex: 'PART_CODE', style: "text-align: center", width: '5%'},
    {header: "英文名称", dataIndex: 'PART_ENAME', style: "text-align: center", width: '5%'},
    {header: "配件种类", dataIndex: 'PRODUCE_STATE', style: "text-align: center", width: '5%', renderer: getItemValue},
    {header: "是否常备", dataIndex: 'IS_PERMANENT', style: "text-align: center", width: '5%', renderer: getItemValue},
    {header: "是否可替代", dataIndex: 'IS_REPLACED', style: "text-align: center", width: '5%', renderer: getItemValue},
//     {header: "配件品牌", dataIndex: 'PRODUCE_FAC', style: "text-align: center", width: '5%', renderer: getItemValue},
//     {header: "材料属性", dataIndex: 'PART_MATERIAL', style: "text-align: center", width: '5%', renderer: getItemValue},
    {header: "单位", dataIndex: 'UNIT', style: "text-align: center", width: '5%'},
//     {header: "所属大类", dataIndex: 'POSITION', style: "text-align: center", width: '5%', renderer: getItemValue},
    {header: "备件类别", dataIndex: 'PART_CATEGORY', style: "text-align: center", width: '5%', renderer: getItemValue},
    {header: "适用车系", dataIndex: 'SERIES_NAME', style: "text-align: center", width: '5%'},
    {header: "适用车型", dataIndex: 'MODEL_NAME', style: "text-align: center", width: '5%'},
    {header: "车系", dataIndex: 'MODEL_CODE', style: "text-align: center", width: '5%'},
    {header: "最小采购数量", dataIndex: 'MIN_SALE', style: "text-align: center", width: '5%'},
    {header: "最小销售数量", dataIndex: 'MIN_PURCHASE', style: "text-align: center", width: '5%'},
    {header: "最大销售量", dataIndex: 'MAX_SALE_VOLUME', style: "text-align: center", width: '5%'},
    {header: "长", dataIndex: 'LENGTH', style: "text-align: center", width: '5%'},
    {header: "宽", dataIndex: 'WIDTH', style: "text-align: center", width: '5%'},
    {header: "高", dataIndex: 'HEIGHT', style: "text-align: center", width: '5%'},
    {header: "净重", dataIndex: 'WEIGHT', style: "text-align: center", width: '5%'},
    {header: "体积", dataIndex: 'VOLUME', style: "text-align: center", width: '5%'},
//     {header: "所属基地", dataIndex: 'OWNED_BASE', style: "text-align: center", width: '5%', renderer: getItemValue},
    {header: "采购方式", dataIndex: 'PRODUCE_FAC', style: "text-align: center", width: '5%', renderer: getItemValue},
    {header: "单车用量", dataIndex: 'VEHICLE_VOLUME', style: "text-align: center", width: '5%'},
    {header: "装配", dataIndex: 'PART_FIT', style: "text-align: center", width: '5%', renderer: getItemValue},
    // {header: "代用件号", dataIndex: 'OTHER_PART_ID', style: "text-align: center", width: '5%'},
    {header: "是否停用", dataIndex: 'IS_PART_DISABLE', style: "text-align: center", width: '5%', renderer: getItemValue},
    {header: "停用日期", dataIndex: 'PART_DISABLE_DATE', style: "text-align: center", width: '5%'},
    {header: "是否售完停用", dataIndex: 'IS_SALE_DISABLE', style: "text-align: center", width: '5%', renderer: getItemValue},
    {header: "售完停用日期", dataIndex: 'SALE_DISABLE_DATE', style: "text-align: center", width: '5%'},
    {header: "是否停止装车", dataIndex: 'IS_STOP_LOAD', style: "text-align: center", width: '5%', renderer: getItemValue},
    {header: "停止装车日期", dataIndex: 'STOP_LOAD_DATE', style: "text-align: center", width: '5%'},
    {header: "是否校验防伪", dataIndex: 'IS_SECURITY', style: "text-align: center", width: '5%', renderer: getItemValue},
    {header: "是否协议包装", dataIndex: 'IS_PROTOCOL_PACK', style: "text-align: center", width: '5%', renderer: getItemValue},
    {header: "是否批次管理", dataIndex: 'IS_MAG_BATCH', style: "text-align: center", width: '5%', renderer: getItemValue},
    {header: "是否委托发货", dataIndex: 'IS_ENTRUSR_PACK', style: "text-align: center", width: '5%', renderer: getItemValue},
    {header: "3C标识", dataIndex: 'CCC_FLAG', style: "text-align: center", width: '5%', renderer: getItemValue},
    {header: "包装规格", dataIndex: 'PACK_SPECIFICATION', style: "text-align: center", width: '5%'},
//     {header: "说明", dataIndex: 'NOTE', style: "text-align: center", width: '5%'},
    {header: "备注", dataIndex: 'REMARK', style: "text-align: center", width: '5%'},
    {header: "修改日期", dataIndex: 'UPDATE_DATE', style: "text-align: center", width: '5%'},
    {header: "修改人", dataIndex: 'UPDATE_BY_NAME', style: "text-align: center", width: '5%'},
];

//获取选择框的值
function getCode(value) {
  var str = getItemValue(value);
  document.write(str);
}
//获取序号
function getIdx() {
  document.write(document.getElementById("file1").rows.length - 2);
}
//获取序号
function getIdx1() {
  document.write(document.getElementById("file2").rows.length - 2);
}
//返回
function goBack() {
  window.location.href = '<%=contextPath%>/parts/salesManager/carFactorySalesManager/PartDlrOrderCheck/partDlrOrderCheckInit.do';
}


$(document).ready(function(){__extQuery__(1);succAlert();});
</script>
<body>

<div class="wbox">
    <div class="navigation"><img src="<%=contextPath%>/img/nav.gif"/>&nbsp;当前位置：配件主数据&gt; 配件修改历史</div>
    <form name="fm" id="fm" method="post" enctype="multipart/form-data">
		<table id="file2" class="table_list" style="border-bottom:1px solid #DAE0EE">
			<tr>
				<th align="left"><img class="panel-icon nav" src="<%=contextPath%>/img/subNav.gif"/>操作信息</th>
			</tr>
		</table>
		<jsp:include page="${contextPath}/queryPage/orderHidden.html"/>
		<jsp:include page="${contextPath}/queryPage/pageDiv.html"/>
    </form>
</div>
</body>
<script type="text/javascript">

</script>
</html>
