<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="com.infodms.dms.common.Constant" %>
<%@taglib uri="/jstl/cout" prefix="c" %>
<%
    String contextPath = request.getContextPath();

%>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp"/>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <title>计划维护</title>


    <SCRIPT type=text/javascript>
        function getYearSelect(id, name, scope, value) {
            var date = new Date();
            var year = date.getFullYear();    //获取完整的年份
            var str = "";
            str += "<select  id='" + id + "' name='" + name + "'  style='width:55px;'>";
            str += "<option selected value=''>-请选择-</option>";
            for (var i = (year - scope); i <= (year + scope); i++) {
                if (value == "") {
                    if (i == year) {
                        str += "<option  selected value =" + i + ">" + i + "</option >";
                    } else {
                        str += "<option   value =" + i + ">" + i + "</option >";
                    }
                } else {
                    str += "<option  " + (i == value ? "selected" : "") + "value =" + i + ">" + i + "</option >";
                }
            }
            str += "</select> 年";
            document.write(str);
        }
        function getMonThSelect(id, name, value) {
            var date = new Date();
            var month = date.getMonth() + 1;
            var str = "";
            str += "<select  id='" + id + "' name='" + name + "'  style='width:45px;'>";
            str += "<option selected value=''>-请选择-</option>";
            for (var i = 1; i <= 12; i++) {
                if (value == "") {
                    if (i == month) {
                        str += "<option selected value =" + (i < 10 ? "0" + i : i) + ">" + (i < 10 ? "0" + i : i) + "</option >";
                    } else {
                        str += "<option  value =" + (i < 10 ? "0" + i : i) + ">" + (i < 10 ? "0" + i : i) + "</option >";
                    }
                } else {
                    str += "<option " + (i == value ? "selected" : "") + "value =" + (i < 10 ? "0" + i : i) + ">" + (i < 10 ? "0" + i : i) + "</option >";
                }
            }
            str += "</select> 月";
            document.write(str);
        }
        function doQuery() {
            var msg = "";
            /*	if(document.getElementById("MYYEAR").value!=""){
             if(document.getElementById("MYMONTH").value==""){
             msg  += "请选择计划月!</br>";
             }
             }
             if(document.getElementById("MYMONTH").value!=""){
             if(document.getElementById("MYYEAR").value==""){
             msg += "请选择计划年!</br>";
             }
             }*/
            if (msg != "") {
                MyAlert(msg);
                return;
            }
            __extQuery__(1);
        }
        //

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
        
      //显示隐藏
        function showUpload(id) {
            jQuery('#'+id).toggle();
        }
      
      //下载计划模板
        function downRollingPlanTemp(){
        	if(confirm('确认下载模板？')){
        		fm.action = "<%=contextPath%>/parts/planManager/PartPlanManager/downRollingPlanTemp.do";
         		fm.submit();
        	}
        }
    </script>
</head>
<body onload="__extQuery__(1);enableAllClEl()">
<form name="fm" id="fm" method="post">

    <div class="navigation"><img src="<%=contextPath%>/img/nav.gif"/>&nbsp;当前位置 &gt;配件管理 &gt;采购计划管理 &gt;紧急计划维护</div>
    <input type="hidden" name="planIds" id="planIds"/></td>
    <table class="table_query">
        <input type="hidden" name="planType" id="planType" value="<%=Constant.PART_PURCHASE_PLAN_TYPE_02%>">
        <tr>
            <th colspan="4" ><img src="<%=contextPath%>/img/subNav.gif" alt="" class="nav"/> 查询条件</th>
        </tr>
        <tr>
            <td width="10%"   align="right">计划年月:</td>
            <td width="20%">
                <script type="text/javascript">
                    getYearSelect("MYYEAR", "MYYEAR", 1, '');
                </script>
                <script type="text/javascript">
                    getMonThSelect("MYMONTH", "MYMONTH", '');
                </script>
            </td>
            <td width="10%"   align="right">计划单号:</td>
            <td width="20%">
                <input class="middle_txt" type="text" name="PLAN_CODE" id="PLAN_CODE"/></td>
           
        </tr>
       
     
        <tr>
            <td align="center" colspan="6"><input class="normal_btn" type="button" name="BtnQuery" id="queryBtn"
                                                  value="查 询" onclick="doQuery()"/>
                &nbsp;
                <input class="cssbutton" type="button" value="新 增" name="button1" onclick="confirmAdd()">
                &nbsp;
              <input class="normal_btn" style="width:80px;" type="button" value="导入滚动计划" name="button5" onclick="showUpload('uploadDiv');">
            </td>
        </tr>
    </table>
    
    
    <!-- 批量导入start -->
        <div style="display: none;" id="uploadDiv">
            <table class="table_query">
                <th colspan="6"><img class="nav" src="<%=request.getContextPath()%>/img/subNav.gif"/> 上传文件</th>
                <tr>
                    <td colspan="6" style="padding-left: 12px; color:red;">
                        <label>格式：备件编码 +计划数量+备件备注</label>
                    </td>
                </tr>
                <tr>
                    <td colspan="6" style="padding-left: 12px;">
                    	<label>1.点击：</label>
                    	<input type="button" class="normal_btn" value="下载模版" onclick="downRollingPlanTemp()"/>
                    	<label>下载计划导入模板！</label>
                    </td>
                </tr>
                
                <tr>
                    <td colspan="6" style="padding-left: 12px;">
                    	<label>2.选择：</label>
                        <script type="text/javascript">
	                        getYearSelect("planYearImp", "planYearImp", 1, '');
	                    </script>
	                    <script type="text/javascript">
	                        getMonThSelect("planMonthImp", "planMonthImp", '');
	                    </script>
	                    
                        <label>，计划年月</label>
                        <font color="red">*(必选项)</font>
                    </td>
                </tr>
                
                <tr>
                    <td colspan="6" style="padding-left: 12px;">
                    	<label>3.选择：</label>
                        <input type="file" name="uploadFile" id="uploadFile" style="width: 250px" datatype="0,is_null,2000"/>
                        <label>要导入的计划文件！</label>
                        <font color="red">(必选项)</font>
                    </td>
                </tr>
               
                <tr>
                    <td colspan="6" style="padding-left: 12px;">
                    	<label>4.输入：</label>
                        <input type="text" name="uploadRemark" id="uploadRemark" style="width: 250px"/>
                        <label>计划备注(可选填)</label>
                    </td>
                </tr>
                <tr>
                    <td colspan="6" style="padding-left: 12px;">
                    	<label>5.点击：</label>
                        <input type="button" id="upbtn" class="normal_btn" value="确 定" onclick="confirmUploadUpdate()"/>
                        <label>确认按钮，完成计划导入！</label>
                    </td>
                </tr>
            </table>
        </div>
        <!-- 批量导入end -->

    <jsp:include page="${contextPath}/queryPage/orderHidden.html"/>
    <jsp:include page="${contextPath}/queryPage/pageDiv.html"/>


</form>
</body>
<script type=text/javascript>
    loadcalendar();  //初始化时间控件

    var myPage;

    var url = "<%=contextPath%>/parts/purchaseManager/purchasePlanSetting/PurchasePlanSetting/queryPurchasePlan.json";

    var title = null;

    var columns = [
        {header: "序号", align: 'center', renderer: getIndex},
        {header: "<input type='checkbox' id='selectAll' name='selectAll' onclick='checkselectAllBox(this)'  />", dataIndex: 'PLAN_ID', align: 'center', renderer: checkBoxLink},
        {header: "计划单号", dataIndex: 'PLAN_CODE', align: 'center'},
        {header: "计划员", dataIndex: 'NAME', align: 'center'},
        {header: "制单日期", dataIndex: 'CREATE_DATE', align: 'center'},
        {header: "库房", dataIndex: 'WH_NAME', align: 'center'},
        {header: "计划年月", dataIndex: 'YEAR_MONTH', align: 'center'},
        {header: "计划类型", dataIndex: 'PLAN_TYPE', align: 'center', renderer: getItemValue},
        {header: "项数", dataIndex: 'DTL_COUNT', align: 'center'},
        {header: "总数量", dataIndex: 'SUM_QTY', align: 'center'},
        {header: "总金额", dataIndex: 'AMOUNT', style: 'text-align:right'},
        {header: "生成方式", dataIndex: 'CREATE_TYPE', align: 'center', renderer: getItemValue},
        {id: 'action', header: "操作", sortable: false, dataIndex: 'PLAN_ID', renderer: myLink, align: 'center'}
    ];
    function myLink(value, meta, record) {
        return String.format("<a href=\"#\" onclick='mod(\"" + value + "\")'>[修改]</a>" + "<a href=\"#\" onclick='del(\"" + value + "\")'>[删除]</a>" 
                + "<a href=\"#\" onclick='view(\"" + value + "\")'>[查看]</a>"
                + "<a href=\"#\" onclick='expDlt(\"" + value + "\")'>[导出]</a>");
    }
    function mod(value) {
        window.location.href = "<%=contextPath%>/parts/purchaseManager/purchasePlanSetting/PurchasePlanSetting/partPlanActions.do?flag=mod&planId=" + value;
    }
    function del(value) {
        MyConfirm("确定删除计划?", delAction, [value]);
    }
    
    function delAction(value) {
    	var url = "<%=contextPath%>/parts/purchaseManager/purchasePlanSetting/PurchasePlanSetting/deletePlan.json?planId=" + value;
    	sendAjax(url, delResult, 'fm');
    }

    function delResult(jsonObj) {
    	enableAllClEl();
        if (jsonObj != null) {
            var success = jsonObj.success;
            var error = jsonObj.error;
            var exceptions = jsonObj.Exception;
            if (success) {
                MyAlert(success);
                __extQuery__(1);
                enableAllClEl();
            } else if (error) {
                MyAlert(error);
            } else if (exceptions) {
                MyAlert(exceptions.message);
            }
        }
    }
    function view(value) {
        window.location.href = "<%=contextPath%>/parts/purchaseManager/purchasePlanSetting/PurchasePlanSetting/partPlanActions.do?flag=view&planId=" + value;
    }

  //导出明细
    function expDlt(value) {
    	fm.action = "<%=contextPath%>/parts/purchaseManager/purchasePlanSetting/PurchasePlanSetting/expDtl.do?planId="+value+"&flag=1";
        fm.target = "_self";
        fm.submit();
    }
    
    function checkBoxLink(value, meta, record) {
        return "<input type='checkbox' id='checkboxs' name='checkboxs' value='" + value + "'  />";
    }
    function checkselectAllBox(obj) {
        var boxs = document.getElementsByName('checkboxs');
        for (var i = 0; i < boxs.length; i++) {
            boxs[i].checked = obj.checked;
        }

    }
    function confirmAdd(){
        if($('partType').value!=""||$('venderId').value!=""){
            if(confirm("确定批量生成计划?")){
                batchAdd();
            }else{
                add();
            }
        }else{
            add();
        }
    }

    function batchAdd(){
        var url = "<%=contextPath%>/parts/purchaseManager/purchasePlanSetting/PurchasePlanSetting/batchAddPlan.json?partType="+$('partType').value+"&venderId="+$('venderId').value;
        sendAjax(url, getResult, 'fm');
    }

    function getResult(jsonObj) {
        enableAllClEl();
        if (jsonObj != null) {
            var success = jsonObj.success;
            var error = jsonObj.error;
            var exceptions = jsonObj.Exception;
            if (success) {
                MyAlert(success);
                __extQuery__(1);
                enableAllClEl();
            } else if (error) {
                MyAlert(error);
            } else if (exceptions) {
                MyAlert(exceptions.message);
            }
        }
    }

    function add() {
        disableAllClEl();
        fm.action = "<%=contextPath%>/parts/purchaseManager/purchasePlanSetting/PurchasePlanSetting/addPurchasePlan.do";
        fm.submit();
    }
    function confirmPlan() {
        var planId = "";
        var cb = document.getElementsByName("checkboxs");
        var planId = "";
        for (var i = 0; i < cb.length; i++) {
            if (cb[i].checked) {
                planId += cb[i].value + ",";
            }
        }
        if (planId == "") {
            MyAlert("请选择提交记录!");
            return;
        }
        document.getElementById("planIds").value = planId;
        MyConfirm("确定提交计划?", confirmPlanCommit, []);
    }
    function confirmPlanCommit() {
        disableAllClEl();
        fm.action = "<%=contextPath%>/parts/purchaseManager/purchasePlanSetting/PurchasePlanSetting/partPlanCheckCommit.do";
        fm.submit();
    }
    function exportPartPlanExcel() {
        fm.action = "<%=contextPath%>/parts/purchaseManager/purchasePlanSetting/PurchasePlanSetting/uploadPartPlanExcel.do";
        fm.submit();
    }

</script>
</html>
