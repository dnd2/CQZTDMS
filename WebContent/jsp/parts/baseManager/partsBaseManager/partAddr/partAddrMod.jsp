<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@taglib uri="/jstl/cout" prefix="c" %>
<%@page import="com.infodms.dms.common.Constant"%>
<%
    String contextPath = request.getContextPath();
    String curPage = (String) request.getAttribute("curPage");
%>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <jsp:include page="${contextPath}/common/jsp_head_new.jsp"/>
    <title>经销商发运接收地址修改</title>
    <script type="text/javascript">
        function confirmUpdate() {
            MyConfirm("确定修改?", function() {
                if (!submitForm('fm')) {
                    return;
                }
	            // 验证默认地址
	            var dealerId = document.getElementById("DEALER_ID").value;
	            var params = {dealerId: dealerId};
	            params.actionType = 'edit';
				var isDefaultAddr = document.getElementById("isDefaultAddr").value;
	            if(isDefaultAddr == <%=Constant.IF_TYPE_YES%>){
	            	params.notExistsAddrId = document.getElementById("ADDR_ID").value;
	            }
	            var url = "<%=contextPath%>/parts/baseManager/partsBaseManager/PartAddrManager/validPartDefaultAddr.json";
	            makeCall(url,validDefaultAddr,params);
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
        	}else if(json.returnCode == 3){
        		// 默认地址改为否时
        		document.getElementById("isDefaultAddr").value = <%=Constant.IF_TYPE_YES%>;
        	}else if(json.returnCode == -1){
        		layer.msg("验证默认发货地址失败！", {icon: 15});
        		return;
        	}
        	// 保存地址信息
        	saveAddr();
        }

        // 保存地址信息
        function saveAddr(){
            $("#saveBtn")[0].disabled = true;
            $("#saveBtn")[0].value = '请等待 ...';
            //提交前把禁用的字段设置为可用
            document.getElementById("DEALER_CODE").disabled = "";
            document.getElementById("DEALER_NAME").disabled = "";

            var url = "<%=contextPath%>/parts/baseManager/partsBaseManager/PartAddrManager/updatePartAddrInfo.json";
            makeNomalFormCall(url, veiwParts, 'fm');
        }

        function veiwParts(jsonObj) {
            btnEnable();
            if (jsonObj != null) {
                var success = jsonObj.success;
                var error = jsonObj.error;
                var exceptions = jsonObj.Exception;
                if (success) {
                	MyAlert(success, function(){
                        window.location.href = '<%=contextPath%>/parts/baseManager/partsBaseManager/PartAddrManager/partAddrQueryInit.do?' + 'curPage=' +<%=curPage%>;
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
            if (!objSelect) {
                return;
            }
            if (!objItemValue || objItemValue == '-1' || objItemValue == '') {
                return;
            }

            for (var i = 0; i < objSelect.options.length; i++) {
                if (objSelect.options[i].value == objItemValue) {
                    objSelect.options[i].selected = true;
                    break;
                }
            }
        }
        
        $(function(){
	        genLocSel('PROVINCE_ID', 'CITY_ID', 'COUNTIES');
	
	        var p = document.getElementById("PROVINCE_ID");
	        setItemValue('PROVINCE_ID', '${addrInfo.PROVINCE_ID}');
	        _genCity(p, 'CITY_ID');
	        var c = document.getElementById("CITY_ID");
	        setItemValue('CITY_ID', '${addrInfo.CITY_ID}');
	        _genCity(c, 'COUNTIES');
	        var t = document.getElementById("COUNTIES");
	        setItemValue('COUNTIES', '${addrInfo.COUNTIES}');
        })
    </script>
<body>
<div class="wbox">
    <div class="navigation"><img src="<%=contextPath%>/img/nav.gif"/>&nbsp;当前位置：配件管理 &gt; 
        基础信息管理 &gt; 配件基础信息维护 &gt; 经销商发运接收地址维护 &gt; 修改
    </div>
    <form id="fm" name="fm" method="post">
        <input id="ADDR_ID" name="ADDR_ID" type="hidden" value="${addrInfo.ADDR_ID}">
        <input id="DEALER_ID" name="DEALER_ID" type="hidden" value="${addrInfo.DEALER_ID}">
        <div class="form-panel">
            <h2><img class="panel-icon nav" src="<%=contextPath%>/img/subNav.gif"/>经销商发运接收地址修改</h2>
            <div class="form-body">
                <table class="table_query">
                    <tr>
                        <td class="right">经销商编码：</td>
                        <td>
                            <input class="middle_txt" type="text" disabled="disabled" id="DEALER_CODE" name="DEALER_CODE"
                                value="${addrInfo.DEALER_CODE}"/>
                        </td>
                        <td class="right">经销商名称：</td>
                        <td>
                            <input class="middle_txt input-medium" type="text" disabled="disabled" id="DEALER_NAME" name="DEALER_NAME"
                                value="${addrInfo.DEALER_NAME}"/>
                        </td>
                        <td class="right">接收人：</td>
                        <td>
                            <input type="text" id="LINKMAN" name="LINKMAN" class="middle_txt" value="${addrInfo.LINKMAN}"/>
                            <font color="red">*</font>
                        </td>
                    </tr>
                    <tr>
                        <td class="right">接收人电话：</td>
                        <td>
                            <input type="text" id="TEL" name="TEL" class="middle_txt"
                                value="${addrInfo.TEL}"/>
                            <font color="red">*</font>
                        </td>
                        <td class="right">到站名称：</td>
                        <td>
                            <input type="text" id="STATION" name="STATION" class="middle_txt" value="${addrInfo.STATION}"/>
                            <font color="red">*</font>
                        </td>

                        <td class="right">传真：</td>
                        <td colspan="5"><input type="text" id="FAX" name="FAX" class="middle_txt" datatype="1,is_phone"
                                            value="${addrInfo.FAX }"/>
                        <td class="right"></td>
                        </td>
                    </tr>
                    <tr>
                        <td class="right">省份：</td>
                        <td align="left"><select class="min_sel u-select" id="PROVINCE_ID" name="PROVINCE_ID"
                                                onchange="_genCity(this,'CITY_ID')"></select></td>
                        <td class="right">城市：</td>
                        <td align="left"><select class="min_sel u-select" id="CITY_ID" name="CITY_ID"
                                                onchange="_genCity(this,'COUNTIES')"></select></td>
                        <td class="right">区县：</td>
                        <td align="left"><select class="min_sel u-select" id="COUNTIES" name="COUNTIES"></select></td>
                    </tr>
                    <tr>
                        <td class="right">邮编：</td>
                        <td>
                            <input type="text" id="POST_CODE" name="POST_CODE" class="middle_txt"
                                maxlength="6" value="${addrInfo.POST_CODE}"/>
                        </td>
                        <td class="right">是否默认地址：</td>
                        <td colspan="4">
                            <script type="text/javascript">
                                genSelBoxExp("isDefaultAddr",<%=Constant.IF_TYPE%>,"${addrInfo.IS_DEFAULT_ADDR}",false,"short_sel u-select",null,"false",'');
                            </script>
                            <input type="hidden" id="existsDefaultAddr" name="existsDefaultAddr" value="<%=Constant.IF_TYPE_NO%>">
                        </td>
                    </tr>
                    <tr>
                        <td class="right">地址：</td>
                        <td colspan="5">
                            <input class="middle_txt input-495" type="text" size="100" id="ADDR" name="ADDR" value="${addrInfo.ADDR}"/>
                            <font color="red">*</font>
                        </td> 
					</tr>
                    <tr>
                        <td class="right">备注：</td>
                        <td colspan="5">
                            <textarea rows="5" cols="100" id="REMARK" name="REMARK" class="form-control textarea-737 align">${addrInfo.REMARK}</textarea>
                        </td>
                    </tr>
                </table>
                <table class="table_edit" width="100%">
                    <tr>
                        <td align="center">
                            <input type="button" name="saveBtn" id="saveBtn" value="保 存"
                                onclick="confirmUpdate();" class="u-button"/>
                            <input type="button" name="backBtn" id="backBtn" value="返 回"
                                onclick="goback();" class="u-button"/>
                        </td>
                    </tr>
                </table>
            </div>
        </div>
    </form>
</div>
</body>
</html>
