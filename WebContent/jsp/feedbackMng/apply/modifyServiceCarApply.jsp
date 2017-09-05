<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/jstl/cout" prefix="c" %>
<head>
<%@ page import="com.infodms.dms.po.TtIfServicecarPO"%>
<TITLE>服务车申请表修改</TITLE>
<%
	TtIfServicecarPO tisp = (TtIfServicecarPO)request.getAttribute("servicecarBean");
 %>
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
	function confirModify(){
		if(submitForm('fm')){
		//if(document.getElementById("MODEL_ID").options[document.getElementById("MODEL_ID").selectedIndex].value==""){;
		//	MyAlert("请选择一个拟购车型!");
		//}else{
				var content = document.getElementById('CONTENT').value;
				var le = document.getElementById('CONTENT').value.length;
					if(content != null && content != ''){
						if(le > 0 && le <= 200){
				document.getElementById("fm");
				fm.action = '<%=request.getContextPath()%>/feedbackmng/apply/ServiceCarApply/servicecarapplyUpdate.do?ORDER_ID=<%=tisp.getOrderId()%>';
				MyConfirm("确认修改？",fm.submit);
				//fm.submit();
				//sendAjax('<%=request.getContextPath()%>/feedbackmng/apply/ServiceCarApply/servicecarapplyUpdate.do?ORDER_ID='+<%=tisp.getOrderId()%>,showResult,'fm');
						}else{
					MyAlert("申请内容不能超过200个字符！");
					}
				}else{
					MyAlert("申请内容是必填项！");
				}
			
			}
		//}
	}

	//选择厂商
	function selectProducerCodeVendor(){
		window.open("../../MainServlet?action=OEM_APPLICATION_CREATE_PRE_SELECT_PRODUCER_CODE_PRE", "", "width=835px,height=520px,left=100,top=80,toolbar=no,location=no,directories=no,status=no,menubar=no,scrollbars=yes,resizable=yes,copyhistory=no maximum=yes");
	}//提交

	//车型选择弹出框
	//function selModel(){
	//	var dept = $('MODEL_ID1').value ;
	//	var url = '<%=request.getContextPath()%>/common/SeriesShow/carModelUrlInit.do?id='+dept ;
	//	OpenHtmlWindow(url,800,500);
	//}
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
	//车系下拉框
	function showSeries(){
		var sid = $('sId').value ;
		var sname = $('sName').value ;
		var arr1 = document.getElementsByName('series_id');
		var arr2 = document.getElementsByName('series_name');
		var str = '<select name="aaaId" id="aaaId" class="short_sel" onchange="selModel();"><option>-请选择-</option>' ;
		for(var i=0;i<arr1.length;i++){
			if(sid==arr1[i].value){
				str += '<option value='+arr1[i].value+' selected>'+arr2[i].value+'</option>' ;
			}else
				str += '<option value='+arr1[i].value+'>'+arr2[i].value+'</option>' ;
		}
		$('series_sel').innerHTML = str+'</select>'; 
	}
	
</SCRIPT>
</HEAD>
<BODY>

<div class="navigation"><img src="../../../img/nav.gif" />&nbsp;当前位置： 信息反馈管理&gt;信息反馈提报&gt;服务车申请表维护</div>
<form id="fm" name="fm" method="post">
<input type="hidden" name="seriesList" id="seriesList" value="<%=request.getAttribute("seriesList")%>"/>
<input type="hidden" id="MODEL_ID_1" name="MODEL_ID" />
<input type="hidden" id="sId" value="${s.seriesId}"/>
<input type="hidden" id="sName" value="${s.seriesName}"/>
<c:forEach var="s" items="${list}">
<tr>
	<td><input type="hidden" name="series_id" value="${s.groupId}"/></td>
	<td><input type="hidden" name="series_name" value="${s.groupName}"/></td>
</tr>
</c:forEach>
<table width=100% border="0" align="center" cellpadding="1" cellspacing="1"   class="table_edit">
	       <th colspan="6" ><img class="nav" src="../../../img/subNav.gif" /> 基本信息&nbsp;&nbsp;&nbsp;&nbsp; <font color="#FF0000">(申请的车辆只能用于长安轿车的售后服务工作，并按长安轿车要求制作形象。一年内不得转让)</font></th>
           <tr >
             <td width="15%" align="right">工单号：</td>
             <td><%=tisp.getOrderId()==null?"":tisp.getOrderId() %></td>
             <td>&nbsp;</td>
             <td>&nbsp;</td>
             <td>&nbsp;</td>
             <td>&nbsp;</td>
           </tr>
          <tr >
            <td width="15%" align="right">经销商联系人：</td>
            <td width="20%"><input type='text'  name='LINK_MAN'  id='LINK_MAN' value="<%=tisp.getLinkMan()==null?"":tisp.getLinkMan()  %>" datatype='0,is_digit_letter_cn'   class="middle_txt"  /></td>
            <td width="10%" align="right">经销商电话：</td>
            <td><input type='text'  name='TEL'  id='TEL'  class="middle_txt"    value='<%=tisp.getTel()==null?"":tisp.getTel() %>' datatype='0,is_phone,11'/></td>
            <td width="10%" align="right">经销商传真：</td>
            <td   ><input type='text'  name='FAX'  id='FAX' class="middle_txt"   value='<%=tisp.getFax()==null?"":tisp.getFax() %>' datatype='0,is_phone,11'/></td>
          </tr>
          <tr>
            <td width="10%" align="right">申请购买车系：</td>
             <td> 
              	<div id="series_sel">
              		<script>showSeries();</script>
              	</div>
              </td>
            <td width="15%" align="right">申请购买车型及状态：</td>
            <td><div id="model_1">${vpo.materialCode}</div></td>
          </tr>
          <tr>
            <td width="10%" align="right">申请车型市场价：</td>
            <td   ><input type='text'  name='SALE_AMOUNT'  id='SALE_AMOUNT'   class="middle_txt"    value='' datatype='0,is_yuan,11'  /></td>
          </tr>
          <tr>
            <td width="10%" align="right">申请内容：</td>
            <td height="27" colspan="5" align="left" datatype="0,is_textarea,200"><span class="tbwhite">
              <textarea  name='CONTENT'  id='CONTENT'   rows='5' cols='80' ><%=tisp.getContent()==null?"":tisp.getContent() %></textarea>
            </span></td>
          </tr>
        </table>
    <tr> 
      <td></td>
    </tr>
    <tr> 
      <td  height=10> 
        
       <table class="table_list">
          <tr > 
            <th height="12" align=center>
				<input type="button" onclick="confirModify();" class="normal_btn" value="确定" />
				&nbsp;&nbsp;
				<input type="button" onclick="javascript:history.go(-1);" class="normal_btn" style="width=8%" value="返回"/></th>
		  </tr>
        </table>
      </td>
    </tr>
    <!-- 资料显示区结束 -->

</form>
<script LANGUAGE="JavaScript">
	function mathPow(){
	var a = <%=tisp.getSaleAmount()%>;
	document.getElementById("SALE_AMOUNT").value = a.toString();
	}
	window.onload=mathPow();
	function assignSelect(name,value) {
		var sel = document.getElementById(name);
		var option = sel.options;
		var optionLength = option.length;
		for (var i = 0;i<optionLength;i++) {
		  	if (value ==option[i].value) {
		  		sel.selectedIndex = i;
		  	}
		}
	}
	assignSelect("MODEL_ID","<%=tisp.getGroupId()%>");
	//var sel = document.getElementById("MODEL_ID");
	//var op = sel.options(sel.selectedIndex);
	//var obj=document.getElementById("MODEL_ID");

	//document.getElementById("MODEL_ID").selectedIndex=1;
	//document.getElementById("MODEL_ID").text='<%=tisp.getGroupId()%>';
  </script>
</BODY>
</html>
