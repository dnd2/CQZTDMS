<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@taglib uri="/jstl/cout" prefix="c" %>
<%@page import="com.infodms.dms.common.Constant"%>
<%
    String contextPath = request.getContextPath();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"

"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
<meta http-equiv="X-UA-Compatible" content="IE=7"/>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp"/>
<title>服务商发运接收地址添加</title>
<script type="text/javascript">
function confirmAdd() {
    if (!submitForm('fm')) {
        return;
    }
    var tel = document.getElementById("TEL").value;
    var linkman = document.getElementById("LINKMAN").value;
    var addr = document.getElementById("ADDR").value;
    var counties = document.getElementById("COUNTIES").value;
    if(linkman == null || linkman == ""){
        layer.msg("接收人不能为空!", {icon: 15});
        $("#LINKMAN")[0].focus();
        return false;
    }
    if (tel == null || tel == "") {
        layer.msg("接收人电话不能为空!", {icon: 15});
        $("#TEL")[0].focus();
        return false;
    }
    if (addr == null || addr == "") {
        layer.msg("地址不能为空!", {icon: 15});
        $("#ADDR")[0].focus();
        return false;
    }
    if(counties == null || counties == ""){
        layer.msg("请选择省市县!", {icon: 15});
        $("#COUNTIES")[0].focus();
        return false;
    }
    /*else {
        var pattern = /((^[0-9]{3,4}\-[0-9]{7,8})(-(\d{3,}))?$)|(^[0-9]{7,8}$)|(^\([0-9]{3,4}\)[0-9]{3,8}$)|(^1[0-9]{10}$)/;
        if (!pattern.exec(tel)) {
            MyAlert("请输入正确的接收电话!");
            $("TEL").value = "";
            $("TEL").focus();
            return false;
        }
    }*/
    /*if (postCode == null || postCode == "") {
     MyAlert("邮政编码不能为空!");
     $("POST_CODE").value = "";
     $("POST_CODE").focus();
     return false;
     } else {
     var pattern = /^[0-9]{6}$/;
     if (!pattern.exec(postCode)) {
     MyAlert("请输入正确的邮政编码!");
     $("POST_CODE").value = "";
     $("POST_CODE").focus();
     return false;
     }
     }*/
    /*if (station == null || station == "") {
     MyAlert("到站名称不能为空!");
     $("STATION").value = "";
     $("STATION").focus();
     return false;
     }
     if ($("FAX").value != null && $("FAX").value != "") {
     var pattern = /^(([0\+]\d{2,3}-)?(0\d{2,3})-)(\d{7,8})(-(\d{3,}))?$/;
     if (!pattern.exec($("FAX").value)) {
     MyAlert("请输入正确的传真号码!");
     $("FAX").value = "";
     $("FAX").focus();
     return false;
     }
     }*/

    MyConfirm("确定保存?", function() {
        // 验证默认地址
     var dealerId = document.getElementById("DEALER_ID").value;
     var url = "<%=contextPath%>/parts/baseManager/partsBaseManager/PartAddrManager/validPartDefaultAddr.json";
          makeCall(url,validDefaultAddr,{dealerId: dealerId});
         }); 
     }
     
     // 验证默认地址
     function validDefaultAddr(json){
var isDefaultAddr = document.getElementById("isDefaultAddr").value;
     	if(json.returnCode == 0){
	// 默认地址不存在时，是否默认地址选择为“是”
     		document.getElementById("isDefaultAddr").value = <%=Constant.IF_TYPE_YES%>;
	}else if(json.returnCode == 1){
		layer.msg("该经销商已经有3个发运地址，因此不能够再创建新的发运地址", {icon: 15});
		return;
	}else if(json.returnCode == 2 && isDefaultAddr == <%=Constant.IF_TYPE_YES%>){
		// 是否默认地址选择“是”并且默认地址已存在时，修改已存在默认地址标识值
		document.getElementById("existsDefaultAddr").value = <%=Constant.IF_TYPE_YES%>;
	}else if(json.returnCode == -1){
		layer.msg("验证默认发货地址失败！", {icon: 15});
		return;
	}
	// 保存地址信息
	saveAddr();
}

// 保存地址信息
function saveAddr(){
    btnDisable();
    //提交前把禁用的字段设置为可用
    document.getElementById("DEALER_CODE").disabled = "";
    document.getElementById("DEALER_NAME").disabled = "";
    var url = "<%=contextPath%>/parts/baseManager/partsBaseManager/PartAddrManager/addPartAddrInfo.json";
    sendAjax(url, getResult, 'fm');
}

function getResult(jsonObj) {
    btnEnable();
    if (jsonObj != null) {
        var success = jsonObj.success;
        var error = jsonObj.error;
        var exceptions = jsonObj.Exception;
        if (success) {
        	MyAlert(success, function(){
	            window.location.href = '<%=contextPath%>/parts/baseManager/partsBaseManager/PartAddrManager/partAddrQueryInit.do';
        	});
        } else if (exceptions) {
            layer.msg(exceptions.message, {icon: 2});
        }
        else if (error) {
            layer.msg(error, {icon: 2});
            document.getElementById("DEALER_CODE").disabled = "";
            document.getElementById("DEALER_NAME").disabled = "";
        }
    }
}

//返回查询页面
function goback() {
    window.location.href = '<%=contextPath%>/parts/baseManager/partsBaseManager/PartAddrManager/partAddrQueryInit.do';
}
function setItemValue(selectName, objItemValue) {
    var objSelect = document.getElementById(selectName);
    if(!objSelect) {return;}
    if(!objItemValue || objItemValue == '-1' || objItemValue == '') {return;}

    for (var i = 0; i < objSelect.options.length; i++) {
        if (objSelect.options[i].value == objItemValue) {
            objSelect.options[i].selected = true;
            break;
        }
    }
}
$(function(){
	genLocSel('PROVINCE_ID','CITY_ID','COUNTIES');
	var p = document.getElementById("PROVINCE_ID");
	setItemValue('PROVINCE_ID', '${dMap.PROVINCE_ID}');
	_genCity(p,'CITY_ID');
	var c = document.getElementById("CITY_ID");
	setItemValue('CITY_ID', '${dMap.CITY_ID}');
	_genCity(c,'COUNTIES');
	var t = document.getElementById("COUNTIES");
	setItemValue('COUNTIES', '${dMap.COUNTIES}');
});

</script>
</head>
<body>
<div class="wbox">
    <div class="navigation"><img src="<%=contextPath%>/img/nav.gif"/>&nbsp;当前位置：
       配件管理 &gt; 基础信息管理 &gt; 配件基础信息维护 &gt; 经销商发运接收地址维护 &gt; 新增
    </div>
    <form id="fm" name="fm" method="post">
        <input id="DEALER_ID" name="DEALER_ID" type="hidden" value="">
        <div class="form-panel">
            <h2><img class="panel-icon nav" src="<%=contextPath%>/img/subNav.gif"/>经销商发运接收地址新增</h2>
            <div class="form-body">
                <table class="table_query">
                    <tr>
                        <td class="right">经销商编码：</td>
                        <td width="25%">
                            <input class="middle_txt" type="text" disabled="disabled" id="DEALER_CODE" name="DEALER_CODE"/>
                            <input class="mark_btn" type="button" value="&hellip;"
                                onclick="showOrgDealer('DEALER_CODE', 'DEALER_ID', 'false', '', '<%= Constant.DEALER_LEVEL_01 %>', 'true', '<%=Constant.DEALER_TYPE_DWR %>', 'DEALER_NAME');"/>
                            <font color="red">*</font>
                        </td>
                        <td class="right">经销商名称：</td>
                        <td width="21%">
                            <input class="middle_txt" type="text" disabled="disabled" id="DEALER_NAME" name="DEALER_NAME"/>
                            <font color="red">*</font>
                        </td>
                        <td class="right">接收人：</td>
                        <td width="22%">
                            <input type="text" id="LINKMAN" name="LINKMAN" class="middle_txt" maxlength="15"/>
                            <font color="red">*</font>
                        </td>
                    </tr>
                    <tr>
                        <td class="right">接收人电话：</td>
                        <td>
                            <input type="text" id="TEL" name="TEL" class="middle_txt"/><font color="red">*</font>
                        </td>
                        <td class="right">到站名称：</td>
                        <td>
                            <input type="text" id="STATION" name="STATION" class="middle_txt"/>
                        </td>
                        <td class="right">传真：</td>
                        <td colspan="5"><input type="text" id="FAX" name="FAX" class="middle_txt"/>
                        <td class="right"></td>
                        <td>
                        </td>
                    </tr>
                    <tr>
                        <td class="right">省份：</td>
                        <td>
                            <select class="min_sel u-select" id="PROVINCE_ID" name="PROVINCE_ID" onchange="_genCity(this,'CITY_ID')"></select>
                            <font color="red">*</font>
                        </td>
                        <td class="right">城市：</td>
                        <td>
                            <select class="min_sel u-select" id="CITY_ID" name="CITY_ID" onchange="_genCity(this,'COUNTIES')"></select>
                            <font color="red">*</font>	
                        </td>
                        <td class="right">区县：</td>
                        <td>
                            <select class="min_sel u-select" id="COUNTIES" name="COUNTIES" datatype="0,is_null,200"></select>
                        </td>
                    </tr>
                    <tr>
                        <td class="right">邮编：</td>
                        <td>
                            <input type="text" id="POST_CODE" name="POST_CODE" class="middle_txt" maxlength="6"/>
                        </td>
                        <td class="right">是否默认地址：</td>
                        <td colspan="4">
                            <script type="text/javascript">
                                genSelBoxExp("isDefaultAddr",<%=Constant.IF_TYPE%>,<%=Constant.IF_TYPE_NO%>,false,"short_sel u-select",null,"false",'');
                             </script>
                            <input type="hidden" id="existsDefaultAddr" name="existsDefaultAddr" value="<%=Constant.IF_TYPE_NO%>">
                        </td>
                    </tr>
                    <tr>
                        <td class="right">地址：</td>
                        <td colspan="5">
                            <input class="middle_txt input-495" type="text" size="100" id="ADDR" name="ADDR" />
                            <font color="red">*</font>
                        </td>
                    </tr>
                    <tr>
                        <td class="right">备注：</td>
                        <td colspan="5">
                            <textarea rows="5" cols="100" id="REMARK" name="REMARK" class="form-control textarea-737 align"></textarea>
                        </td>
<!--                     </tr> -->
<!--                         <td class="right">联系人：</td> -->
<!--                         <td> -->
<!--                             <input type="text" id="LINKMAN2" name="LINKMAN2" class="middle_txt" maxlength="100"/> -->
<!--                         </td> -->
<!--                         <td class="right">联系人电话：</td> -->
<!--                         <td> -->
<!--                             <input type="text" id="TEL2" name="TEL2" class="middle_txt" maxlength="100"/> -->
<!--                         </td> -->
<!--                         <td class="right">是否默认地址：</td> -->
<!--                         <td> -->
<!--                             <script type="text/javascript"> -->
<%--                                 genSelBoxExp("isDefaultAddr",<%=Constant.IF_TYPE%>,<%=Constant.IF_TYPE_NO%>,false,"short_sel u-select",null,"false",''); --%>
<!--                             </script> -->
<%--                             <input type="hidden" id="existsDefaultAddr" name="existsDefaultAddr" value="<%=Constant.IF_TYPE_NO%>"> --%>
<!--                         </td> -->
<!--                     </tr> -->
                    <tr>
                        <td class="center" colspan="6">
                            <input type="button" name="saveBtn" id="saveBtn" value="保 存"
                                onclick="confirmAdd();" class="u-button"/>
                            <input type="button" name="backBtn" id="backBtn" value="返 回"
                                onclick="javascript:goback();" class="u-button"/>
                        </td>
                    </tr>
                </table>
            </div>
        </div>
    </form>
</div>

</body>
</html>
