<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ page import="com.infodms.dms.common.Constant" %>
<%@taglib uri="/jstl/cout" prefix="c" %>
<%
    String contextPath = request.getContextPath();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
<meta http-equiv="X-UA-Compatible" content="IE=7"/>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/web/jquery-1.7.2.min.js"></script>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp"/>
<title>配件供应商维护新增</title>
<script type="text/javascript">
//获取供应商
function getDealerInfoke(code,name){
	var urlkey ='<%=contextPath%>/jsp/parts/baseManager/partsBaseManager/partVender/getVenders.jsp?flag=partAndVender';
	OpenHtmlWindow(urlkey,900,540);
}
//设置供应商
function setDealerInfoke(id,code,name){
	$("#VENDER_ID").val(id);
	$('#VENDER_CODE').val(code);
	$("#VENDER_NAME").val(name);
}
//设置配件
function setPartInfoke(id,oldcode,name,code){
	$('#PART_ID').val(id);
	$('#PART_OLDCODE').val(oldcode);
	$('#PART_CNAME').val(name);
	$('#PART_CODE').val(code);
}
//设置配件
function getparts(){
	var urlkey ='<%=contextPath%>/jsp/parts/baseManager/partsBaseManager/partVender/getParts.jsp?flag=partAndVender';
	OpenHtmlWindow(urlkey,900,540);
}
//保存
function confirmAdd(){
	if($('#VENDER_ID')[0].value==''){
		MyAlert('请选择供应商！');
		return;
	}
	if($('#PART_ID')[0].value==''){
		MyAlert('请选择配件！');
		return;
	}
	MyConfirm('确认保存？', function(){
		btnDisable();
		var url = '<%=contextPath%>/parts/baseManager/partsBaseManager/PartAndVenderManager/saveRelation.json';
		makeNomalFormCall(url, getResult, 'fm');
	});
}
function getResult(jsonObj) {
	// $('input[type="button"]').prop('disabled',false);
    if (jsonObj != null) {
        var error = jsonObj.error;
        var success = jsonObj.success;
        var exceptions = jsonObj.Exception;
        if (error) {
            MyAlert(error);
        }else if (success) {
            MyAlert(success, goback);
        }else if(exceptions){
	    	MyAlert(exceptions.message);
	    }
    }
}

//返回
function goback(){
	location = "<%=contextPath%>/parts/baseManager/partsBaseManager/PartAndVenderManager/toPartAndVenderMain.do";
}
</script>
</head>
<body>
<div class="wbox">
    <div class="navigation">
    	<img src="<%=contextPath%>/img/nav.gif"/>&nbsp;当前位置：配件管理 &gt; 基础信息管理&gt;&nbsp;计划相关信息维护&gt;&nbsp;供应商管理&gt;&nbsp;配件供应商维护&gt;&nbsp;新增
    </div>
    <form id="fm" name="fm" method="post">
    	<div class="form-panel">
        <h2><img src="<%=contextPath%>/img/subNav.gif"/>新增信息</h2>
	        <div class="form-body">
		        <table class="table_query">
		            <tr>
		                <td class="right">供应商编码：</td>
		                <td>
		                	<input type="hidden" name="VENDER_ID" id="VENDER_ID" value="" />
		                    <input type="text" readonly="readonly" class="middle_txt" name="VENDER_CODE" id="VENDER_CODE" datatype="0,is_null" maxlength="50"/>
		                	<input type="button" class="mini_btn" onclick="getDealerInfoke('dealerCode_in','dealerName_in')" value="...">
		                </td>
		                
		                <td class="right">供应商名称：</td>
		                <td>
		                	<input type="text" readonly="readonly" class="middle_txt" name="VENDER_NAME" id="VENDER_NAME"/>
		                </td>
		                
		                <td class="right">是否有效：</td>
		                <td>
		                	<script type="text/javascript">
		                        genSelBox("state", <%=Constant.IF_TYPE%>, '<%=Constant.IF_TYPE_YES%>', false, "");
		                    </script>
		                </td>
		            </tr>
		            
		            <tr>
		                <td class="right">配件编码：</td>
		                <td>
		                	<input type="hidden" name="PART_ID" id="PART_ID" value="" />
		                    <input type="text" readonly="readonly" class="middle_txt" name="PART_OLDCODE" id="PART_OLDCODE" datatype="0,is_null" maxlength="50"/>
		                	<input type="button" class="mini_btn" onclick="getparts()" value="...">
		                </td>
		                <td class="right">配件名称：</td>
		                <td>
		                	<input type="text" readonly="readonly" class="middle_txt" name="PART_CNAME" id="PART_CNAME"/>
		                </td>
		                
		                <td class="right">配件件号：</td>
		                <td>
		                	<input type="text" readonly="readonly" class="middle_txt" name="PART_CODE" id="PART_CODE"/>
		                </td>
		            </tr>
		            <tr>
		                <td colspan="6" class="center">
		                    <input type="button" name="saveBtn" id="saveBtn" value="保 存"  onclick="confirmAdd();" class="u-button"/>
		                    <input type="button" name="saveBtn2" id="saveBtn2" value="返 回"  onclick="javascript:goback();" class="u-button"/>
		                </td>
		            </tr>
		        </table>
	        </div>
        </div>
    </form>
</div>
</body>
</html>
