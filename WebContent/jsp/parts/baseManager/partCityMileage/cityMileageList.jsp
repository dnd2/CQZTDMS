<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c" %>
<head>
    <%
        String contextPath = request.getContextPath();
    %>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <jsp:include page="${contextPath}/common/jsp_head_new.jsp"/>
    <title> 城市里程数维护 </title>
</head>

<body onload="__extQuery__(1);">
<div class="navigation"><img src="<%=contextPath %>/img/nav.gif"/>&nbsp;当前位置>配件管理>基础基础信息>储运相关信息维护>发运时效和运费维护
</div>
<form name="fm" method="post" id="fm" enctype="multipart/form-data">
    <!-- 查询条件 begin -->
    <table class="table_query" id="subtab">
        <tr align="center">
            <td align="right">省份：</td>
            <td align="left">
                <select class="min_sel" id="txt1" name="PROVINCE" onchange="_genCity(this,'txt2')"></select>
            </td>
            <td align="right">地级市：</td>
            <td align="left">
                <select class="min_sel" id="txt2" name="CITY_ID"></select>
            </td>
            <%-- <td align="right">区县：</td>
             <td align="left">
                 <input type="text" id="COUNTY_ID" name="COUNTY_ID" class="middle_txt" size="15"/>
             </td>--%>
        </tr>
        <tr>
            <td align="right">承运物流：</td>
            <td>
                <select name="transportOrg" id="transportOrg" onclick="" style="">
                    <option value="">--请选择</option>
                    <c:forEach items="${listc}" var="obj">
                        <option value="${obj.fixValue}">${obj.fixName}</option>
                    </c:forEach>
                </select>
            </td>
            <td align="right">发运方式：</td>
            <td>
                <select name="transType" id="transType" onclick="" style="">
                    <option value="">--请选择</option>
                    <c:forEach items="${listf}" var="obj">
                        <option value="${obj.fixValue}">${obj.fixName}</option>
                    </c:forEach>
                </select>
            </td>
        </tr>
        <tr align="center">
            <td colspan="4" align="center">
                <input type="button" id="queryBtn" class="cssbutton" value="查询" onclick="__extQuery__(1);"/>
                <input type="button" class="cssbutton" value="新增" onclick="addLogistics();"/>
                <input type="hidden" id="export_sel" name="export_sel"/>
                <input class="normal_btn" style="width: 80px;" type="button" value="物流费用导入" id="upload_button"
                       name="button1"
                       onclick="showUpload(1);">
                <input class="normal_btn" style="width: 80px;" type="button" value="物流时效导入" id="upload_button"
                       name="button1"
                       onclick="showUpload(2);">
                <input class="normal_btn" type="button" value="导 出" onclick="exportPartStockExcel()"/>
                <input type="reset" class="cssbutton" id="resetButton" value="重置"/>
                <!--
              <input type="button" id="saveButton" class="cssbutton"  value="保存" onclick="addReservoir();" />
                 -->
            </td>
        </tr>
    </table>
    <div style="display:none" id="uploadDiv_1">
        <table class="table_query">
            <th colspan="6"><img class="nav" src="<%=contextPath %>/img/subNav.gif"/> 上传文件</th>
            <tr>
                <td colspan="2">
                    <input type="button" class="normal_btn" value="下载模版" onclick="exportExcelTemplate(1);"/>
                    <font color="red">文件选择后，点&quot;确定&quot;按钮,完成上传操作：&nbsp;</font>
                    <input type="file" name="uploadFile1" id="uploadFile1" style="width: 250px"
                           datatype="0,is_null,2000" value=""/> &nbsp;
                    <input type="button" id="upbtn1" class="normal_btn" value="确 定" onclick="confirmUpload(1)"/>
                </td>
            </tr>
        </table>
    </div>
    <div style="display:none ; heigeht: 5px" id="uploadDiv_2">
        <table class="table_query">
            <th colspan="6"><img class="nav" src="<%=contextPath %>/img/subNav.gif"/> 上传文件</th>
            <tr>
                <td colspan="2">
                    <input type="button" class="normal_btn" value="下载模版" onclick="exportExcelTemplate(2);"/>
                    <font color="red">文件选择后，点&quot;确定&quot;按钮,完成上传操作：&nbsp;</font>
                    <input type="file" name="uploadFile2" id="uploadFile2" style="width: 250px"
                           datatype="0,is_null,2000" value=""/> &nbsp;
                    <input type="button" id="upbtn2" class="normal_btn" value="确 定" onclick="confirmUpload(2);"/>
                </td>
            </tr>
        </table>
    </div>
    <!-- 查询条件 end -->
    <!--分页 begin -->
    <jsp:include page="${contextPath}/queryPage/orderHidden.html"/>
    <jsp:include page="${contextPath}/queryPage/pageDiv.html"/>
    <!--分页 end -->
</form>
<!--页面列表 begin -->
<script type="text/javascript">
    var myPage;
    //查询路径
    var url = "<%=contextPath%>/parts/baseManager/partCityMileage/CityMileageManage/cityMileageQuery.json";
    var title = null;
    var columns = [
        {header: "序号", align: 'center', renderer: getIndex},
        {header: "物流商 ", dataIndex: 'TRANS_ORG', align: 'center'},
        {header: "运输方式 ", dataIndex: 'TRANS_TYPE', align: 'center'},
//				{header: "运输出发地", dataIndex: 'START_NAME', align:'center'},
        {header: "运输目的地", dataIndex: 'END_NAME', align: 'center'},
        {header: "约定时效（天）", dataIndex: 'ARRIVE_DAYS', align: 'center'},
        {header: "计费首重(元/1kg)", dataIndex: 'FIRST_WEIGHT', align: 'center'},
        {header: "计费续重(元/kg)", dataIndex: 'ADDITIONAL_WEIGHT', align: 'center'},
        {header: "最小发运重量(kg)", dataIndex: 'MINI_WEIGHT', align: 'center'},
        {header: "备注", dataIndex: 'REMARK', align: 'left'}
        //{header: "操作", id: 'action',sortable: false, dataIndex: 'DIS_ID', renderer: myLink, align: 'center'}
    ];
    function myLink(value, meta, record) {
        var text = "<a href='#' onclick='modify(\"" + value + "\");'>[修改]</a>&nbsp;&nbsp;";
        text = text + "<a href='#' onclick='cancel(\"" + value + "\");'>[删除]</a>";
        return String.format(text);
    }
    //初始化
    function doInit() {
        //__extQuery__(1);
        genLocSel('txt1', '', '');//支持火狐
    }
    function myDistance(value, metaDate, record) {
        var str = "<input type='hidden' name='areaId' value=" + record.data.AREA_ID + " />";
        str += "<input type='hidden' name='yieldly' value=" + record.data.YIELDLY + " />";
        str += "<input type='hidden' name='provinceId' value=" + record.data.PROVINCE_ID + " />";
        str += "<input type='hidden' name='cityId' value=" + record.data.CITY_ID + " />";
        str += "<input type='hidden' name='countyId' value=" + record.data.COUNTY_ID + " />";
        str += "<input type='text' name='DISTANCE' class='middle_txt' onblur='checkValue(this)' style='width:50px' value='" + value + "' />";
        return String.format(str);
    }
    function myArriveDays(value, metaDate, record) {
        return String.format("<input type='text' id='ARRIVE_DAYS' name='ARRIVE_DAYS' onblur='checkValue(this)' style='width:50px' class='middle_txt' value='" + value + "' />");
    }
    function checkValue(v) {
        if (v.value != "") {
            if (!isNumber(v.value)) {
                v.value = "";
                v.focus();
            }
        }
    }
    //添加
    function addReservoir() {

        MyConfirm("确认保存信息！", saveCityMileage);
    }

    function saveCityMileage() {
        disabledButton(["saveButton"], true);
        makeNomalFormCall("<%=contextPath%>/parts/baseManager/partCityMileage/CityMileageManage/saveCityMileage.json", saveCityMileageBack, 'fm', 'queryBtn');
    }

    function saveCityMileageBack(json) {
        if (json.returnValue == 1) {
            parent.MyAlert("操作成功！");
            fm.action = "<%=contextPath%>/parts/baseManager/partCityMileage/CityMileageManage/cityMileageInit.do";
            fm.submit();
        }
        else {
            disabledButton(["saveButton"], false);
            MyAlert("操作失败！请联系系统管理员！");
        }
    }
    function addLogistics() {
        fm.action = g_webAppName + "/parts/baseManager/partCityMileage/CityMileageManage/addLogisticsInit.do";
        fm.submit();
    }
    function showUpload(sel) {
        if (sel == 1) {
            var uploadDiv = document.getElementById("uploadDiv_1");
            if (uploadDiv.style.display == "block") {
                uploadDiv.style.display = "none";
            } else {
                uploadDiv.style.display = "block";
            }
        } else {
            var uploadDiv = document.getElementById("uploadDiv_2");
            if (uploadDiv.style.display == "block") {
                uploadDiv.style.display = "none";
            } else {
                uploadDiv.style.display = "block";
            }
        }
    }
    function fileVilidate(sel) {
        var importFileName;
        if (sel == 1) {
            importFileName = document.getElementById("uploadFile1").value;
        } else {
            importFileName = document.getElementById("uploadFile2").value;
        }
        if (importFileName == "") {
            MyAlert("请选择导入文件!");
            return false;
        }
        var index = importFileName.lastIndexOf(".");
        var suffix = importFileName.substr(index + 1, importFileName.length).toLowerCase();
        if (suffix != "xls" && suffix != "xlsx") {
            MyAlert("请选择Excel格式文件");
            return false;
        }
        return true;
    }

    //上传检查和确认信息
    function confirmUpload(sel) {
        if (fileVilidate(sel)) {
            MyConfirm("确定导入选择的文件?", uploadExcel(sel), []);
        }
    }
    //上传
    function uploadExcel(sel) {
        document.getElementById("export_sel").value = sel;
        fm.action = g_webAppName + "/parts/baseManager/partCityMileage/CityMileageManage/locImpUpload.do";
        fm.submit();
    }
    //模板下载
    function exportExcelTemplate(sel) {
        document.getElementById("export_sel").value = sel;
        fm.action = "<%=contextPath%>/parts/baseManager/partCityMileage/CityMileageManage/exportExcelTemplate.do";
        fm.submit();
    }
    //导出
    function exportPartStockExcel() {
        document.fm.action = "<%=contextPath%>/parts/baseManager/partCityMileage/CityMileageManage/exportExcel.do";
        document.fm.target = "_self";
        document.fm.submit();
    }
</script>
</body>
</html>
