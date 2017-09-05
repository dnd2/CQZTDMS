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

var url = "<%=contextPath%>/parts/storageManager/partStoInveManager/partBaseManager/queryPartBaseInfo.json";

    var title = null;

    var columns = [
        {header: "序号", style: "text-align: center", renderer: getIndex},
        {
            id: 'action',
            header: "操作",
            sortable: false,
            dataIndex: 'PART_ID',
            renderer: myLink,
            style: "text-align: center"
        },
        	
        {header: "配件编码", dataIndex: 'PART_OLDCODE', style: "text-align: center", width: '3%'},
        {header: "配件名称", dataIndex: 'PART_CNAME', style: "text-align: center", width: '5%'},
        {header: "件号", dataIndex: 'PART_CODE', style: "text-align: center", width: '3%'},
        {header: "英文名称", dataIndex: 'PART_ENAME', style: "text-align: center", width: '3%'},
        {header: "单位", dataIndex: 'UNIT', style: "text-align: center"},
        {header: "包装规格", dataIndex: 'PACK_SPECIFICATION', style: "text-align: center", width: '5%'},
        {header: "长（mm)", dataIndex: 'LENGTH', style: "text-align: center", width: '5%'},
        {header: "宽（mm)", dataIndex: 'WIDTH', style: "text-align: center", width: '5%'},
        {header: "高（mm)", dataIndex: 'HEIGHT', style: "text-align: center", width: '5%'},
        {header: "体积(mm3)	", dataIndex: 'VOLUME', style: "text-align: center", width: '5%'}
//         {header: "3C标识", dataIndex: 'CCC_FLAG', style: "text-align: center", width: '5%', renderer: getItemValue},

    ];

    //设置超链接  begin


//设置超链接
function myLink(value, meta, record) {
	var html = "<a href=\"#\" onclick='view(\"" + value + "\")'>[查看]</a>";
	if (record.data.STATE == <%=Constant.STATUS_ENABLE%>) {
		html += "<a href=\"#\" onclick='Mod(\"" + value + "\")'>[修改]</a>";
		html += "<a href=\"#\" onclick='abate(\"" + value + "\")'>[失效]</a>";
	} else {
		html += "<a href=\"#\" onclick='enable2(\"" + value + "\")'>[有效]</a>";
	}
	return html;
}

//详细页面
function view(value) {
    disableAllClEl();
    OpenHtmlWindow('<%=contextPath%>/parts/storageManager/partStoInveManager/partBaseManager/queryPartBaseDetail.do?flag=view&&partId=' + value, 1100, 440, '配件主数据维护');
    enableAllClEl();
}

//修改页面
function Mod(value) {
    disableAllClEl();
    OpenHtmlWindow('<%=contextPath%>/parts/storageManager/partStoInveManager/partBaseManager/queryPartBaseDetail.do?flag=mod&&partId=' + value, 1100, 400, '配件主数据维护');
    enableAllClEl();
}
function detailHis(value) {
    disableAllClEl();
    OpenHtmlWindow('<%=contextPath%>/parts/storageManager/partStoInveManager/partBaseManager/detailHis.do?partId=' + value, 1100, 350, '配件主数据维护');
    enableAllClEl();
}
function abate(value) {
    MyConfirm("确定要失效?", abateAction, [value], null, null);
}
//失效
function abateAction(value) {
    disableAllClEl();
    var abateUrl = '<%=contextPath%>/parts/storageManager/partStoInveManager/partBaseManager/queryPartBaseDetail.json?flag=abate&&partId=' + value + '&curPage=' + myPage.page;
    makeNomalFormCall(abateUrl, veiwParts, 'fm');
}
function enable2(value) {
    MyConfirm("确定有效?", enableAction, [value]);
}
//有效
function enableAction(value) {
    disableAllClEl();
    var abateUrl = '<%=contextPath%>/parts/storageManager/partStoInveManager/partBaseManager/queryPartBaseDetail.json?flag=enable&&partId=' + value + '&curPage=' + myPage.page;
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
    OpenHtmlWindow('<%=contextPath%>/parts/storageManager/partStoInveManager/partBaseManager/partBaseAdd.do?', 1100, 500, '新增配件');
    enableAllClEl();
}

//清除供应商代码
function reset() {
    document.getElementById("SUPPLIER_CODE").value = "";
}

function exportExcelTemplate(flag) {
    document.getElementById("flag").value = flag;
    fm.action = "<%=contextPath%>/parts/storageManager/partStoInveManager/partBaseManager/exportExcelTemplate.do";
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
        fm.action = "<%=contextPath%>/parts/storageManager/partStoInveManager/partBaseManager/uploadPartBaseExcel.do";
        fm.submit();
    });
}

function exportPartBaseExcel() {
    document.fm.action = "<%=contextPath%>/parts/storageManager/partStoInveManager/partBaseManager/exportPartBaseExcel.do";
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
                                genSelBox("STATE", <%=Constant.STATUS%>, "<%=Constant.STATUS_ENABLE%>", true, "", "", "false");
                            </script>
                        </td>
                    </tr>
                    <tr>
                        <td class="formbtn-aln" align="center" colspan="6">
                            <input class="u-button" type="button" name="queryBtn" id="queryBtn" value="查 询"onclick="__extQuery__(1)"/> &nbsp; 
							<input type="reset" class="u-button" value="重 置"/> &nbsp; 
                            <input class="u-button" type="button" value="新 增" onclick="partAdd();"/>
                        </td>
                    </tr>
                </table>
            </div>
        </div>

        <jsp:include page="${contextPath}/queryPage/orderHidden.html"/>
        <jsp:include page="${contextPath}/queryPage/pageDiv.html"/>
    </form> 
</div>    
</body>
</html>
