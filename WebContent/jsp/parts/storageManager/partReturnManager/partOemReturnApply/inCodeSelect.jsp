<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<html xmlns="http://www.w3.org/1999/xhtml">
<%
    String contextPath = request.getContextPath();
%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>通用选择框</title>

<script type="text/javascript">
var myPage;

var url = "<%=contextPath%>/parts/storageManager/partReturnManager/PartOemReturnApplyManager/queryInCode.json";

var title = null;

var columns = [
    {header: "选择", dataIndex: 'VENDER_ID', align: 'center', renderer: seled},
    {header: "入库单号", dataIndex: 'IN_CODE', align: 'center'},
    {header: "供应商编码", dataIndex: 'VENDER_CODE', align: 'center'},
    {header: "供应商", dataIndex: 'VENDER_NAME', align: 'center'}
];

function seled(value, meta, record) {
    var inCode = record.data.IN_CODE;
    var venderId = record.data.VENDER_ID;
    var venderCode = record.data.VENDER_CODE;
    var venderName = record.data.VENDER_NAME;
    return "<input type='radio' name='singleSel' onclick='singleSelect(\"" + inCode + "\", \"" + venderId + "\", \"" + venderCode + "\", \"" + venderName + "\");' />";
}

function singleSelect(inCode, venderId, venderCode, venderName) {
	var parentInCode = parentDocument.getElementById('IN_CODE').value;
	parentDocument.getElementById('IN_CODE').value = inCode;
// 	parentDocument.getElementById('VENDER_ID').value = venderId;
// 	parentDocument.getElementById('VENDER_CODE').value = venderCode;
// 	parentDocument.getElementById('VENDER_NAME').value = venderName;
	if(parentInCode != inCode){
		parentContainer.onChangeInCode();
	}
    _hide();
}

Date.prototype.format = function (format) {
    var o = {
        "M+": this.getMonth() + 1, //month
        "d+": this.getDate(),    //day
        "h+": this.getHours(),   //hour
        "m+": this.getMinutes(), //minute
        "s+": this.getSeconds(), //second
        "q+": Math.floor((this.getMonth() + 3) / 3), // quarter	
        "S": this.getMilliseconds() // millisecond
    }
    if (/(y+)/.test(format)) format = format.replace(RegExp.$1,
            (this.getFullYear() + "").substr(4 - RegExp.$1.length));
    for (var k in o)if (new RegExp("(" + k + ")").test(format))
        format = format.replace(RegExp.$1,
                        RegExp.$1.length == 1 ? o[k] :
                        ("00" + o[k]).substr(("" + o[k]).length));
    return format;
}

function showMonthFirstDay() {
    var Nowdate = new Date();
    var MonthFirstDay = new Date(Nowdate.getFullYear(), Nowdate.getMonth(), 1);
    //return DateUtil.Format("yyyy-MM-dd", MonthFirstDay);
    return MonthFirstDay.format("yyyy-MM-dd");
}
function showMonthLastDay() {
    var Nowdate = new Date();
    var MonthNextFirstDay = new Date(Nowdate.getFullYear(), Nowdate.getMonth() + 1, 1);
    var MonthLastDay = new Date(MonthNextFirstDay - 86400000);
    //return DateUtil.Format("yyyy-MM-dd", MonthLastDay);
    return MonthLastDay.format("yyyy-MM-dd");
}

$(function(){
	$('#t1').val(showMonthFirstDay());
	$('#t2').val(showMonthLastDay());
	__extQuery__(1);
});
</script>
</head>
<body>
	<div class="wbox">
		<div class="navigation">
			<img src="<%=request.getContextPath()%>/img/nav.gif" />&nbsp;当前位置： 选择入库单号
		</div>
		<form method="post" name="fm" id="fm">
			<input id="IN_CODE" name="IN_CODE" type="hidden" value="" />
			<div class="form-panel">
				<h2>
					<img src="<%=request.getContextPath()%>/img/subNav.gif" /> 信息
				</h2>
				<div class="form-body">
					<table class="table_query">
						<tr>
							<td class="right">入库单号：</td>
							<td>
								<input class="middle_txt" id="inCode" name="inCode" type="text" />
							</td>
							<td class="right">入库时间：</td>
							<td>
								<input name="beginTime" id="t1" value="" type="text" class="short_txt" datatype="1,is_date,10" group="t1,t2" style="width:80px;">
								<input name='button3' value=" " type='button' class='time_ico' title="点击选择时间" />
								&nbsp;至&nbsp;
								<input name="endTime" id="t2" value="" type="text" class="short_txt" datatype="1,is_date,10" group="t1,t2" style="width:80px;">
								<input name='button3' value=" " type='button' class='time_ico' title="点击选择时间" />
							</td>
						</tr>
						<tr>
							<td colspan="4" class="center">
								<input type="button" name="queryBtn" id="queryBtn" value="查询" class="u-button" onClick="__extQuery__(1);">
								<input type="button" name="closeBtn" id="closeBtn" value="关闭" class="u-button" onClick="_hide();">
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