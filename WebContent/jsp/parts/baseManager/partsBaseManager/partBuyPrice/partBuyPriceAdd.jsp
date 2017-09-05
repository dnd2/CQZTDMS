<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.Constant"%>
<%@taglib uri="/jstl/cout" prefix="c"%>
<%
    String contextPath = request.getContextPath();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>配件采购价格添加</title>
<script type="text/javascript">
function confirmAdd() {
     if(""==$("#PART_CNAME").val()){
    	 MyAlert("请选择配件!");
         return;
     }
    if(""==$("#VENDER_NAME").val()){
    	MyAlert("请选择供应商!") ;
        return;
    }
    if(""==$("#BUY_PRICE").val()){
    	MyAlert("采购价不能为空!")
        return;
    }
    if (!submitForm('fm')) {
        return;
    }
    MyConfirm("确定保存?", saveRecord, [], null, null, 2);
}

function saveRecord(){
	btnDisable();
    var url = "<%=contextPath%>/parts/baseManager/partsBaseManager/PartBuyPriceManager/addPartBuyPriceInfo.json";
	sendAjax(url, getResult, 'fm');
}

function getResult(jsonObj) {
	btnEnable();
    if (jsonObj != null) {
        var success = jsonObj.success;
        var error = jsonObj.error;
        var exception = jsonObj.Exception;
        if (success) {
            MyAlertForFun(success, function(){
	            window.location.href = '<%=contextPath%>/parts/baseManager/partsBaseManager/PartBuyPriceManager/partBuyPriceQueryInit.do';
            });
          }else if(error){
        	  MyAlert(error);
          }
          else if(exception){
        	  MyAlert(exception.message);
  		}
      }
}


//验证最大长度指定的正小数,inputObj为input对象，beforeLength为小数点前面的位数个数，afterLength为小数点后面的位数个数
  /*function checkNumberLength(inputObj, beforeLength, afterLength) {
      if (inputObj.value.indexOf(".") >= 0) {
          //var regex = new RegExp("/^[0-9]{0," + beforeLength + "}.[0-9]{0," + afterLength + "}$/");  +\.[0-9]{2})[0-9]*
          var regex = new RegExp("/^[0-9]{0,10}.\\d{0,7}$/");
          if(regex.test(inputObj.value)==true){
              MyAlert("请录入正确的采购金额，且小数保留精度最大为"+afterLength+"位!");
              inputObj.value="";
              inputObj.focus();
          }else {
              var re = /([0-9]+\.[0-9]{7})[0-9]*$/;
              inputObj.value=  inputObj.value.replace(re,"$1");
              var regex = new RegExp("^[0-9]{0," + beforeLength + "}.[0-9]{0," + afterLength + "}$");
              if(regex.test(inputObj.value)==false){
                  MyAlert("整数部分不能超过"+beforeLength+"位!");
                  inputObj.value="";
                  inputObj.focus();
              }
          }
      }
      else {
          var regex = new RegExp("^[0-9]{0," + beforeLength + "}$");
          if(regex.test(inputObj.value)==false){
              MyAlert("只能是数字，且纯数字不能超过"+beforeLength+"位!");
              inputObj.value="";
          }
      }
  }*/

  function checkBuyPrice(obj){
  	var patrn = /^((0\.[0-9]*[1-9][0-9]*)|([1-9][0-9]*\.[0-9]+)|([1-9][0-9]*))$/;
      if (!patrn.exec(obj.value)){
    	  MyAlert("采购价无效,请重新输入!");
          obj.value="";
          return;
      }else{
          if(obj.value.indexOf(".") >= 0){
          	var patrn = /^[0-9]{0,10}.[0-9]{0,7}$/;
          	if(!patrn.exec(obj.value)){
          		MyAlert("采购价整数部分不能超过10位,且保留精度最大为7位!");
          		obj.value="";
                  return;
              }
          }else{
          	var patrn = /^[0-9]{0,10}$/;
          	if(!patrn.exec(obj.value)){
          		MyAlert("采购价整数部分不能超过10位!");
          		obj.value="";
                  return;
              }
          }
      }
  }
  function checkPlanPrice(obj){
  	var patrn = /^((0\.[0-9]*[1-9][0-9]*)|([1-9][0-9]*\.[0-9]+)|([1-9][0-9]*))$/;
      if (!patrn.exec(obj.value)){
    	  MyAlert("计划价无效,请重新输入!");
          obj.value="";
          return;
      }else{
          if(obj.value.indexOf(".") >= 0){
          	var patrn = /^[0-9]{0,10}.[0-9]{0,7}$/;
          	if(!patrn.exec(obj.value)){
          		MyAlert("计划价整数部分不能超过10位,且保留精度最大为7位!");
          		obj.value="";
                  return;
              }
          }else{
          	var patrn = /^[0-9]{0,10}$/;
          	if(!patrn.exec(obj.value)){
          		MyAlert("计划价整数部分不能超过10位!");
          		obj.value="";
                  return;
              }
          }
      }
  }
  //返回查询页面
function goback() {
	window.location.href = '<%=contextPath%>/parts/baseManager/partsBaseManager/PartBuyPriceManager/partBuyPriceQueryInit.do';
}
  
/**
 * 选择配件
 * isMulti   : true值多选，否则单选
 */
function showPartInfo2(inputCode,inputOldCode,inputName ,inputId , inputDefBuyPrice, isMulti){
	if(!inputName){ inputName = null;}
	if(!inputId){ inputId = null;}
	OpenHtmlWindow(g_webAppName+"/dialog/partSelectSingle.jsp?INPUTCODE="+inputCode+"&INPUTOLDCODE="+inputOldCode+"&INPUTNAME="+inputName+"&INPUTID="+inputId+"&INPUTDEFBUYPRICE="+inputDefBuyPrice+"&ISMULTI="+isMulti,730,450);
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
	<div class="wbox">
		<div class="navigation">
			<img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 基础信息管理 &gt; 配件基础信息维护 &gt; 配件采购价格维护 &gt; 采购价格新增
		</div>
		<form id="fm" name="fm" method="post">
			<input id="PART_CODE" name="PART_CODE" type="hidden" value="">
			<input id="PART_CNAME" name="PART_CNAME" type="hidden" value="">
			<div class="form-panel">
				<h2>
					<img class="nav" src="<%=contextPath%>/img/subNav.gif" />采购价格新增
				</h2>
				<div class="form-body">
					<table class="table_query">
						<tr>
							<td class="right">配件编码：</td>
							<td>
								<input class="middle_txt" type="text" readonly="readonly" id="PART_OLDCODE" name="PART_OLDCODE" style="width: 200px;" />
								<input class="mark_btn" type="button" value="&hellip;" onclick="showPartInfo2('PART_CODE','PART_OLDCODE','PART_CNAME','PART_ID','BUY_PRICE','false')" />
								<input id="PART_ID" name="PART_ID" type="hidden" value="">
								<font color="red">*</font>
							</td>
							<td class="right">供应商名称：</td>
							<td>
								<input class="middle_txt" type="text" readonly="readonly" id="VENDER_NAME" name="VENDER_NAME" style="width: 200px;" />
								<input id="VENDER_ID" name="VENDER_ID" type="hidden" value="">
								<input class="mark_btn" type="button" value="&hellip;" onclick="showPartVender('VENDER_NAME','VENDER_ID','false')" />
								<font color="red">*</font>
							</td>
							<td class="right">采购价：</td>
							<td>
								<input type="text" id="BUY_PRICE" name="BUY_PRICE" class="middle_txt" onblur="checkBuyPrice(this)" />
								<font color="red">*</font>
							</td>
						</tr>
						<tr>
							<td class="right">是否暂估：</td>
							<td colspan="5">
								<script type="text/javascript">
									genSelBox("IS_GUARD", <%=Constant.IS_GUARD%>, "", false, "", "");
								</script>
							</td>
						</tr>
						<tr>
							<td align="right">&nbsp;</td>
							<td>&nbsp;</td>
							<td align="right">&nbsp;</td>
							<td>&nbsp;</td>
						</tr>
					</table>
					<table class="table_edit tb-button-set">
						<tr>
							<td align="center">
								<input type="button" name="saveBtn" id="saveBtn" value="保 存" class="normal_btn" onclick="confirmAdd();" />
								<input type="button" name="backBtn" id="backBtn" value="返 回" class="normal_btn" onclick="javascript:goback();" />
							</td>
						</tr>
					</table>
				</div>
			</div>
		</form>
	</div>
</body>
</html>
