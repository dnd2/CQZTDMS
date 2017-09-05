<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" pageEncoding="UTF-8"%>
<%
    String contextPath = request.getContextPath();
%>
<%@ page import="com.infodms.dms.common.Constant"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>设计变更维护</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<script language="javascript" type="text/javascript">
    //返回
    function reBack() {
    	btnDisable();
        window.location = "<%=contextPath%>/parts/baseManager/partExceptionQueryManger/partExceptionQuery/partExceptionQueryInit.do";
    }

    // 配件选择
    function sel1() {
    	var partId = $('#PART_ID2')[0].value;
    	var url = '<%=contextPath%>/parts/baseManager/partExceptionQueryManger/partExceptionQuery/partExceptionQuerySelect.do';
    	url += '?partOldId='+partId;
        OpenHtmlWindow(url, 700, 560, '配件编码选择');
    }
    
 	// 替换配件选择
    function sel2() {
    	var partId = $('#PART_ID')[0].value;
    	var url = '<%=contextPath%>/parts/baseManager/partExceptionQueryManger/partExceptionQuery/partExceptionQuerySelect.do';
    	url += '?partOldId='+partId;
    	url += '&reType=2';
        OpenHtmlWindow(url, 700, 560, '变更后编码');
    }
    
    //表单提交方法：
    function checkForm() {
    	btnDisable();
    	makeNomalFormCall('<%=contextPath%>/parts/baseManager/partExceptionQueryManger/partExceptionQuery/partExceptionQueryAdd.json', showResult, 'fm');
    }
    
	function showResult(json) {
    	btnEnable();
    	if (json.errorExist != null && json.errorExist.length > 0) {
			layer.msg("配件编码：【" + json.errorExist + "】替代件系统中已创建，不能重复创建！", {icon: 15});
        } else if (json.success != null && json.success == "true") {
			MyAlert('新增成功！', function(){
	            window.location.href = "<%=contextPath%>/parts/baseManager/partExceptionQueryManger/partExceptionQuery/partExceptionQueryInit.do";
			});
		}else {
			layer.msg("新增失败，请联系管理员！", {icon: 15});
		}	
	}
	
	//表单提交前的验证：
	function checkFormUpdate() {

		if (!submitForm('fm')) {
			return false;
		}
		var part_code1 = document.getElementById('PART_OLDCODE').value;
		var part_code2 = document.getElementById('PART_OLDCODE2').value;
		var type = document.getElementById('TYPE').value;
		if ("" == part_code1) {
			layer.msg("原配件编码不能为空！", {icon: 15});
			return false;
		}
		if ("" == part_code2) {
			layer.msg("变更后的配件编码空！", {icon: 15});
			return false;
		}
		if ("" == type) {
			layer.msg("请选择替换类型！", {icon: 15});
			return false;
		}
		/*if (part_code1 == part_code2) {
			MyAlert("设变后的代码不能和原代码相同！");
			document.getElementById('PART_OLDCODE2').value = "";
			return false;
		}*/
		MyConfirm("是否确认新增?", checkForm);
	}
</script>
</head>
<body>
	<div class="wbox">
		<form name='fm' id='fm' method="post">
			<div class="navigation">
				<img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置：配件管理 &gt; 基础信息管理 &gt; 配件基础信息维护 &gt; 设计变更维护 &gt; 新增
			</div>
			<div class="form-panel">
				<h2>
					<img class="panel-icon nav" src="<%=contextPath%>/img/subNav.gif" /> 变更信息
				</h2>
				<div class="form-body">
					<table class="table_query">
						<tr>
							<td width="15%" class="right">配件编码：</td>
							<td width="35%">
								<input class="middle_txt" id="PART_OLDCODE" name="PART_OLDCODE" type="text" readonly="readonly" />
								<input name="BUTTON" type="button" class="mini_btn" onclick="sel1()" value="..." />
								<input type="hidden" name="PART_ID" id="PART_ID" />
								<input type="hidden" name="PART_DATA" id="PART_DATA" />
								<font color="red">*</font>
							</td>
							<td width="15%" class="right">变更后编码：</td>
							<td width="35%">
								<input class="middle_txt" type="text" id="PART_OLDCODE2" name="PART_OLDCODE2" readonly="readonly" />
								<input name="BUTTON" type="button" class="mini_btn" onclick="sel2()" value="..." />
								<input type="hidden" name="PART_ID2" id="PART_ID2" />
								<input type="hidden" name="PART_DATA2" id="PART_DATA2" />
								<font color="red">*</font>
							</td>
							<%--<td class="right">备注：</td>
							<td width="25%"><input type="text" class="middle_txt" id="REMARK" name="REMARK" /></td>--%>
						</tr>
						<tr>
							<td class="right">替换类型：</td>
							<td width="25%" colspan="3">
								<script type="text/javascript">
									genSelBoxExp("TYPE", <%=Constant.ZT_PB_PART_REPLACE_TYPE%> , "", true, "short_sel u-select", "", "false", '');
								</script>
								<font color="red">*</font>
							</td>
						</tr>
						<tr>
							<td class="right">备注：</td>
							<td colspan="3">
								<textarea class="form-control align" style="width: 80%;" cols="2" id="REMARK" name="REMARK"></textarea>
							</td>
						</tr>
						<tr>
							<td colspan="4" class="center">
								<input type="button" name="saveBtn" id="saveBtn" value="保 存" onclick="checkFormUpdate();" class="u-button" />
								<input type="button" name="backBtn" id="backBtn" value="返 回" onclick="reBack()" class="u-button" />
							</td>
						</tr>
					</table>
				</div>
			</div>
		</form>
	</div>
</body>
</html>
