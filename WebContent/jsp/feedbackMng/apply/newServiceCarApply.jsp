<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/jstl/cout" prefix="c" %>
<head>

<TITLE>服务车申请表新增</TITLE>

<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<SCRIPT LANGUAGE="JavaScript">
	function showResult(json){
		if(json.ACTION_RESULT == '1'){
			goBack();
		//	MyConfirm("新增成功！点击确认返回查询界面或者点击左边菜单进入其他功能！","window.location.href = '<%=request.getContextPath()%>/sysmng/orgmng/DlrInfoMng/queryAllDlrInfo.do'");
		}else if(json.ACTION_RESULT == '2'){
			MyAlert("新增失败！请重新载入或者联系系统管理员！");
		}
	}
	function goBack(){
		window.location.href = "<%=request.getContextPath()%>/feedbackmng/apply/ServiceCarApply/servicecarapplyQuery.json";
	}
	function confirmAdd(){
	if(!$('MODEL_ID_1').value){;
		MyAlert("请选择一个拟购车型!");
	}else {
		if(submitForm('fm')){
			var content = document.getElementById('CONTENT').value;
			var le = document.getElementById('CONTENT').value.length;
				if(content != null && content != ''){
					if(le > 0 && le <= 200){
							var fm = document.getElementById('fm');
							fm.action='<%=request.getContextPath()%>/feedbackmng/apply/ServiceCarApply/servicecarapplyAdd.do';
							MyConfirm("确认新增？",fm.submit);
							//fm.submit();
							//sendAjax('<%=request.getContextPath()%>/feedbackmng/apply/ServiceCarApply/servicecarapplyAdd.json',showResult,'fm');
					}else{
				MyAlert("申请内容不能超过200个字符！");
				}
			}else{
				MyAlert("申请内容是必填项！");
			}
		}
		}
	}
    //检查VIN
	function checkVin(){
		var vin = document.FRM.VIN.value;
		if(vin.length<1||vin.length>17){
			MyAlert("VIN不能为空且不能多余17位！");
			return ;
		}	
		//window.open("../../MainServlet?action=OEM_APPLICATION_CREATE_PRE_CHECK_VIN&VIN="+vin, "", "width=20px,height=10px,left=600,top=400,toolbar=no,location=no,directories=no,status=no,menubar=no,scrollbars=yes,resizable=yes,copyhistory=no maximum=yes");
	}

	//选择厂商
	function selectProducerCodeVendor(){
		window.open("../../MainServlet?action=OEM_APPLICATION_CREATE_PRE_SELECT_PRODUCER_CODE_PRE", "", "width=835px,height=520px,left=100,top=80,toolbar=no,location=no,directories=no,status=no,menubar=no,scrollbars=yes,resizable=yes,copyhistory=no maximum=yes");
	}//提交

	//车型选择弹出框
	function selModel(){
		$('MODEL_ID_1').value = '' ;
		$('model_1').innerHTML = '' ;
		var dept = $('aaaId').value ;
		var url = '<%=request.getContextPath()%>/common/SeriesShow/carModelUrlInit.do?id='+dept ;
		OpenHtmlWindow(url,800,500);
	}

	function setCarModel(id,code,name,status){
		$('model_1').innerHTML = code ;
		//$('status_1').innerHTML = getItemValue(status);
		$('MODEL_ID_1').value = id ;
		//$('STATUS_2').value = status ;
	}
	function goBack(){
		location = '<%=request.getContextPath()%>/feedbackmng/apply/ServiceCarApply/servicecarapplyforward.do' ;
	}
	//车系下拉框
	function showSeries(){
		var arr1 = document.getElementsByName('series_id');
		var arr2 = document.getElementsByName('series_name');
		var str = '<select name="aaaId" id="aaaId" class="short_sel" onchange="selModel();"><option>-请选择-</option>' ;
		for(var i=0;i<arr1.length;i++){
			str += '<option value='+arr1[i].value+'>'+arr2[i].value+'</option>' ;
		}
		$('series_sel').innerHTML = str+'</select>'; 
	}
	
</SCRIPT>
</HEAD>
<BODY onLoad="javascript:changBand();">

<div class="navigation"><img src="../../../img/nav.gif" />&nbsp;当前位置：  信息反馈管理&gt;信息反馈提报 &gt;服务车申请表维护</div>
<form id="fm" name="fm" method="post">
<input type="hidden" name="seriesList" id="seriesList" value="<%=request.getAttribute("seriesList")%>"/>
<input type="hidden" id="MODEL_ID_1" name="MODEL_ID" />

              	<c:forEach var="s" items="${list}">
              	<tr>
              		<td><input type="hidden" name="series_id" value="${s.groupId}"/></td>
              		<td><input type="hidden" name="series_name" value="${s.groupName}"/></td>
              	</tr>
              	</c:forEach>
<table width=100% border="0" align="center" cellpadding="1" cellspacing="1"   class="table_edit">
	       <th colspan="6"><img class="nav" src="../../../img/subNav.gif" /> 基本信息&nbsp;&nbsp;&nbsp;&nbsp; <font color="#FF0000">(申请的车辆只能用于长安轿车的售后服务工作，并按长安轿车要求制作形象。一年内不得转让)</font></th>
          <tr >
            <td width="15%" align="right">经销商联系人：</td>
            <td width="20%"><input type='text'  name='LINK_MAN'  id='LINK_MAN' value="<c:out value="${map.LINK_MAN}"/>"   class="middle_txt" datatype='0,is_digit_letter_cn' /></td>
            <td width="10%" align="right">经销商电话：</td>
            <td width="20%"><input type='text'  name='TEL'  id='TEL'  class="middle_txt"    value="<c:out value="${map.PHONE}"/>"  datatype='0,is_phone,11'/></td>
            <td width="10%" align="right">经销商传真：</td>
            <td width="20%"><input type='text'  name='FAX'  id='FAX' class="middle_txt"   datatype='0,is_phone,11' value="<c:out value="${map.FAX_NO}"/>" /></td>
          </tr>
          <tr >
            <td width="15%" align="right">申请购买车系：</td>
              <td>
              	<div id="series_sel">
              		<script>showSeries();</script>
              	</div>
              </td>
            <td width="15%" align="right">申请购买车型及状态：</td>
            <td><div id="model_1"></div></td>
          </tr>
          <tr>
          	 <td width="15%" align="right">申请车型市场价：</td>
             <td   ><input type='text' name='SALE_AMOUNT' id='SALE_AMOUNT'   class="middle_txt"  datatype='0,is_yuan,11'  value='' /></td>
          </tr>
          <tr>
            <td width="15%" align="right">申请内容：</td>
            <td height="27" colspan="5" align="left" ><span class="tbwhite">
              <textarea  name='CONTENT'  id='CONTENT' datatype="0,is_textarea,200"  rows='5' cols='80' ></textarea>
            </span></td>
          </tr>
        </table>
   
       <table class="table_list">
          <tr > 
            <th height="12" align=center>
				<input type="button" onclick="confirmAdd();" class="normal_btn" value="确定" />
				&nbsp;&nbsp;
			    <input type="button" onclick="goBack();" class="normal_btn"  value="返回"/>
			</th>
		  </tr>
        </table>
    <!-- 资料显示区结束 -->

</form>
</BODY>
</html>
