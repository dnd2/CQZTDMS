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
<title>活动配件明细设置</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
<script language="javascript" type="text/javascript">
    
    $(function() {
//         var _this = document.getElementById("actPartType");
//         hideImportBtn(_this);
	});
    //返回
    function goBack() {
        btnDisable();
        location = '<%=contextPath%>/parts/baseManager/PartContractManager/PartContractQuery/partContractQueryInit.do';
    }

    //表单提交方法：
    function checkForm() {
        var conType = document.getElementById("CONTRACT_TYPE").value;
        var parts = [];
        var prices=[];
        var k = 0;
        if(conType==<%=Constant.CONTRACT_TYPE_02%>){
            var obj = document.getElementsByName("partIds");
            var price=document.getElementsByName("CONTRACT_PRICE");
            for (var i = 0; i < obj.length; i++) {
                parts[k] = obj[i].value;
                prices[k]=price[i].value;
                k++;
            }
        }
        btnDisable();
        var url = '<%=contextPath%>/parts/baseManager/PartContractManager/PartContractQuery/InsertContract.json?parts='+parts+'&prices='+prices;
        makeNomalFormCall(url, showResult, 'fm');
    }
    function showResult(json) {
        btnEnable();
       if (json.success != null && json.success == "true") {
            MyAlert("新增成功!", function(){
	            window.location.href = "<%=contextPath%>/parts/baseManager/PartContractManager/PartContractQuery/partContractQueryInit.do";
            });
        }else{
            MyAlert("新增失败，请联系管理员!");
        }
    }
    //表单提交前的验证：
    function checkFormUpdate() {

        var conType = document.getElementById("CONTRACT_TYPE").value;
        var startDate = document.getElementById("checkSDate").value;
        var endDate = document.getElementById("checkEDate").value;
        var connum = document.getElementById("CONTRACT_NUMBER").value;
        var isTemp = document.getElementById("ISTEMP").value;
        var venderId = document.getElementById("VENDER_ID").value;


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
        
        if ("" == venderId || null == venderId) {
        	layer.msg('请选择供应商!', {icon: 2});
            return false;
        }
        
        if ("" == isTemp || null == isTemp) {
        	layer.msg('请选择是否临时!', {icon: 2});
            return false;
        }

        var startDateFormat = new Date(startDate.replace("-", "/"));
        var endDateFormat = new Date(endDate.replace("-", "/"));

        if ((endDateFormat - startDateFormat) < 0) {
            MyAlert('活动结束日期要晚于开始日期!');
            return false;
        }
        if(conType==<%=Constant.CONTRACT_TYPE_02%>){
            var price=document.getElementsByName("CONTRACT_PRICE");
            var partIds = document.getElementsByName("partIds");
            if (null == partIds || partIds.length <= 0) {
                MyAlert('请至少选择一个新增的配件!');
                return false;
            }
            for (var i = 0; i < price.length; i++) {
                var  val=price[i].value;
                if(val==""){
                    MyAlert("合同价不可为空");
                    return;
                }
            }
        }
        MyConfirm("确认是否新增?", checkForm);
    }

    function showPart() {
        var url = '<%=contextPath%>/parts/baseManager/PartContractManager/PartContractQuery/queryPartsForAddInit.do?';
        OpenHtmlWindow(url, 700, 500);
    }



    function setPartCode(partIdsNames) {
        var tab = $('#add_tab')[0];
        var prePartIds = document.getElementsByName("partIds");
        var rpPartNames = "";
        var partIdArr = new Array();
        if (null != prePartIds && prePartIds.length > 0) {
            var idsSize = prePartIds.length;
            for (var i = 0; i < idsSize; i++) {
                partIdArr.push([prePartIds[i].value]);
            }
        }
        var strTemp;
        for (var i = 0; i < partIdsNames.length; i++) {
            strTemp = partIdsNames[i].toString();
            //定义一数组
            strsTemp = strTemp.split("@@"); //字符分割
            var partId = strsTemp[0];
            var partCode = strsTemp[1];
            var partOldcode = strsTemp[2];
            var partName = strsTemp[3];

            if (partIdArr.length > 0
                    && partIdArr.toString().indexOf(partId) > -1) {
                rpPartNames = rpPartNames + partName + " ";
            } else {
                var idx = tab.rows.length;
                var insert_row = tab.insertRow(idx);
                if (idx % 2 == 0)
                    insert_row.className = 'table_list_row2';
                else
                    insert_row.className = 'table_list_row1';
                insert_row.insertCell(0);
                insert_row.insertCell(1);
                insert_row.insertCell(2);
                insert_row.insertCell(3);
                insert_row.insertCell(4);
                insert_row.insertCell(5);
                var cur_row = tab.rows[idx];
                cur_row.cells[0].innerHTML = idx;
                cur_row.cells[1].innerHTML = partOldcode
                        + '<input type="hidden" name="partOldcode" value=' + partOldcode + '>';
                cur_row.cells[2].align = "left";
                cur_row.cells[2].innerHTML = partName
                        + '<input type="hidden"  name="partName" value=' + partName + '>';
                cur_row.cells[3].innerHTML = partCode
                        + '<input type="hidden" name="partIds" value=' + partId + '><input type="hidden" name="partCode" value=' + partCode + '>';

                cur_row.cells[4].innerHTML =  '<input type="text" class="short_txt" name="CONTRACT_PRICE" onblur="is_double(this)"  value="" />';
                cur_row.cells[5].innerHTML = '<input type="button" class="normal_btn" value="删 除" onclick="deleteTblRow(\'add_tab\',' + idx + ')"/>';

            }
        }
        if ("" != rpPartNames) {
            MyAlert("配件：【" + rpPartNames + "】不能被重复添加!");
        }
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
            MyAlert("请输入数字!");
            obj.value = "";
            return;
        }
        var re = /^[1-9]+[0-9]*]*$/;
        if (!re.test(obj.value)) {
            MyAlert("请输入正整数!");
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
        if (<%=Constant.PART_ACTIVITY_TYPE_REPLACED_01 %> == parseInt(_this.value)
    )
        {
            $("#repartCode").show();
            $("#repartCname").show();
            $("#isneedFlag").show();
            $("#isNormal").show();
        }
    else
        {
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

    function doCusChange(obj){
        var add_tab=document.getElementById("add_tab");
        if(obj==<%=Constant.CONTRACT_TYPE_01%>){
            add_tab.style.display = "none";
            var rowNum = add_tab.rows.length;
            for (i = 0; i < rowNum; i++) {
                if (i > 0) {
                    add_tab.deleteRow(i);
                    rowNum = rowNum - 1;
                    i = i - 1;
                }
            }
        }else{
            add_tab.style.display = "inline-table";
        }


    }
    function doMySel(){
    	
    }

    function clearThing(){
       var venderId=document.getElementById("VENDER_ID").value;
        if(venderId!=""){
            document.getElementById("VENDER_ID").value="";
            document.getElementById("VENDER_CODE").value="";
            document.getElementById("VENDER_NAME").value="";
        }
        showPartVender('VENDER_NAME','VENDER_ID','false');
    }
    
    /**
     * 选择供应商
     * inputId   : 回填页供应商code域id
     * inputName ：回填页供应商id域id
     * isMulti   : true值多选，否则单选
     */
    function showPartVender(inputCode ,inputId ,isMulti ){
    	if(!inputCode){ inputCode = null;}
    	if(!inputId){ inputId = null;}
    	OpenHtmlWindow(g_webAppName+"/dialog/venderSelectSingle.jsp?INPUTCODE="+inputCode+"&INPUTID="+inputId+"&ISMULTI="+isMulti,730,450);
    }
</script>
</head>
<body>
<div id="wbox" class="wbox">
<form name='fm' id='fm'>
   <div class="navigation">
       <img src="<%=contextPath%>/img/nav.gif"/>&nbsp;当前位置：配件管理 &gt; 基础信息管理 &gt; 供应商管理 &gt;合同管理&gt;新增
   </div>
   <input type="hidden" id="dlrSelectVal" name="dlrSelectVal" value="1"/>
   <div class="form-panel">
   <h2><img src="<%=contextPath%>/img/subNav.gif"/>新增合同</h2>
   <div class="form-body">
   <table class="table_query">
          <tr>
              <td class="right">合同编号：</td>
              <td>
                  <input class="middle_txt" type="text" name="CONTRACT_NUMBER" onblur="checkAppNum(this);" id="CONTRACT_NUMBER"/> <font color="red">*</font>
              </td>
              <td class="right">合同有效期：</td>
              <td>
					<input name="checkSDate" type="text" class="middle_txt" id="checkSDate" style="width:80px" value=""/>
					<input name="button2" value=" " type="button" class="time_ico"/>
                  	至
					<input name="checkEDate" type="text" class="middle_txt" id="checkEDate" style="width:80px" value=""/>
					<input name="button2" value=" " type="button" class="time_ico">
					<font color="red">*</font>
              </td>
          </tr>
          <tr>
              <td class="right">供应商名称：</td>
              <td>
                  <input class="middle_txt" type="text"   name="VENDER_NAME" id="VENDER_NAME" readonly="readonly"/>
                  <input class="middle_txt" type="hidden"   name="VENDER_CODE" id="VENDER_CODE" readonly="readonly"/>
                  <input class="middle_txt" type="hidden"   name="VENDER_ID" id="VENDER_ID"/>
                  <input class="mark_btn" type="button" value="&hellip;" onclick="clearThing();"/>
                  <font color="red">*</font>
              </td>
              <td class="right">合同类型：</td>
              <td>
                  <script type="text/javascript">
<%--                       genSelBox("CONTRACT_TYPE", <%=Constant.CONTRACT_TYPE%>, "<%=Constant.CONTRACT_TYPE_01%>", true, ""); --%>
                      genSelBoxExp("CONTRACT_TYPE", <%=Constant.CONTRACT_TYPE%>, "<%=Constant.CONTRACT_TYPE_01%>", true, "u-select", "", "false", '');
                  </script>
                  <font color="red">*</font>
              </td>
          </tr>
           <tr>
		<td class="right">是否临时：</td>
		<td>
			<script type="text/javascript">
				genSelBox("ISTEMP", <%=Constant.IF_TYPE%>, "", true, "");
			</script>
			<font color="red">*</font>
		</td>
		</tr>
          <tr>
              <td class="right">备注(来源)：</td>
              <td colspan="3">
                  <textarea class="form-control align" name="remark" id="remark" rows="3" cols="60" datatype="1,is_textarea,200" style="width: 80%;"></textarea>
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

		<table class="table_list" id="add_tab" style="border-bottom:1px solid #DAE0EE; display: none;" >
			<tr class="table_list_th" style="background-color: #DAE0EE;">
				<th class="noSort" style="word-wrap: break-word;word-break: break-all; width: 70px;">序号</th>
				<th class="noSort" style="word-wrap: break-word;word-break: break-all;">配件编码</th>
				<th class="noSort" style="word-wrap: break-word;word-break: break-all;">配件名称</th>
				<th class="noSort" style="word-wrap: break-word;word-break: break-all;">件号</th>
				<th class="noSort" style="word-wrap: break-word;word-break: break-all;">合同价</th>
				<th class="noSort" style="word-wrap: break-word;word-break: break-all;">
					<input type="button" value="选择配件" class="normal_btn" onclick="showPart();"/>
				</th>
			</tr>
      </table>
</form>
</div>
</body>
</html>
