<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.Constant" %>
<%
	String contextPath = request.getContextPath();
%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>经销商地址维护</title>
<script type="text/javascript">
function doInit()
{  
	genLocSel('txt1','txt2','txt3','','',''); // 加载省份城市和县
}
</script>
</head>
<body>
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 配件管理 &gt; 基础信息管理 &gt;配件基础信息维护&gt;服务商信息&gt;添加服务商地址</div>
 <form method="post" name = "fm" id="fm" >
 <input id="DEALER_ID" name="DEALER_ID" type="hidden" value="${dealerId}"/>
 <input id="DEALER_CODE" name="DEALER_CODE" type="hidden" value="${dealerCode}"/>
 <input id="DEALER_NAME" name="DEALER_NAME" type="hidden" value="${dealerName}"/>
    <table width=100% border="0" align="center" cellpadding="1" cellspacing="1" class="table_query">
		  <tr>
		    <td   align="right">地址：</td>
		    <td>
		    <input type='text'  class="middle_txt" name="address"  id="address" datatype="0,is_null,150"  value="" size="80" style="width:320px" maxlength="150"/>
		    </td>
		    <td   align="right"></td>
		    <td></td>
	      </tr>
	      <tr>
		    <td   align="right">联系人：</td>
		    <td>
		    <input type='text'  class="middle_txt" name="linkMan"  id="linkMan" datatype="1,is_null,8"  value="" maxlength="8"/>
		    </td>
		    <td   align="right">电话：</td>
		    <td>
		    <input type='text'  class="middle_txt" name="tel"  id="tel" datatype="1,is_null,30"  value="" maxlength="30"/>
		    </td>
	     </tr>
	    <%--  <tr>
		    <td   align="right">地址状态：
		    </td>
		    <td>
		   <label>
				<script type="text/javascript">
					genSelBoxExp("ADDRESSSTATUS",<%=Constant.STATUS%>,"-1",'',"short_sel",'',"false",'');
				</script>
		  </label>
		    </td>
<!-- 	     <td   align="right">收货单位：</td> -->
<!-- 		    <td> -->
<!-- 		    <input type='text'  class="middle_txt" name="myaddress"  id="myaddress" datatype="1,is_null,90"  value="" maxlength="90"/> -->
<!-- 		    </td> -->
	     </tr> --%>
	     <tr> <td   align="right">到站名称：</td>
		    <td>
		    <input type='text'  class="middle_txt" name="station"  id="station" datatype="1,is_null,90"  value="" maxlength="90"/>
		    </td>
	      <td   align="right">传真：</td>
		    <td>
		    <input type='text'  class="middle_txt" name="fax"  id="fax" datatype="1,is_null,90"  value="" maxlength="90"/>
		    </td>
	     </tr>
	     <tr> <td   align="right">邮编：</td>
		    <td>
		    <input type='text'  class="middle_txt" name="postcode"  id="postcode" datatype="1,is_null,90"  value="" maxlength="90"/>
		    </td> 
	     </tr>
	     
<!-- 	     <tr> -->
<!-- 	     	<td   align="right">省份：</td> -->
<!-- 	      <td><select class="min_sel" id="txt1" name="province" onchange="_genCity(this,'txt2')"></select> </td> -->
<!--          	<td   align="right">地级市：</td> -->
<!-- 	      <td><select class="min_sel" id="txt2" name="city"  onchange="_genCity(this,'txt3')" ></select></td>   -->
<!-- 	     </tr> -->
<!-- 	     <tr> -->
<!-- 	     <td   align="right">县：</td> -->
<!-- 	      <td><select class="min_sel" id="txt3" name="area" ></select></td> -->
<!-- 	     </tr> -->
<!-- 	     <tr style=display:none> -->
<!-- 	     	<td   align="right">业务范围:</td> -->
<!-- 	     	<td colspan="3"> -->
<%-- 	     		<c:forEach items="${areaList }" var="areaList"> --%>
<%-- 	     			<input type="checkBox" name="addressAreas" id="${areaList.AREA_ID }" value="${areaList.AREA_ID }|${dealerId }"> --%>
<%-- 	     			<label for="${areaList.AREA_ID }">${areaList.AREA_NAME }</label> --%>
<%-- 	     		</c:forEach> --%>
<!-- 	     	</td> -->
<!-- 	     </tr> -->
	      <tr>
	        <td   align="right">备注：</td>
	      	<td><textarea name="remark" id="remark" cols="40" rows="2" datatype="1,is_textarea,1000"></textarea></td>
	      </tr>
     </table> 
     <table class=table_query>
	 <tr>
	 <td>
	<input type="button" value="添加" name="saveBtn" class="normal_btn" onclick="saveAddress()"/>	
	<input type="button" value="关闭" name="cancelBtn"  class="normal_btn" onclick="_hide();" /></td>
	</tr>
   </table>
</form>

<script type="text/javascript" >
function saveAddress()
{
	var addValue=document.getElementById('address').value;
 	var linkmanValue=document.getElementById('linkMan').value;
 	var telValue=document.getElementById('tel').value;
 	var stationValue=document.getElementById('station').value;
	
	if(addValue==''){
		MyAlert('必须填写街道地址！');
		return;
	}
	if(linkmanValue==''){
		MyAlert('必须填写联系人！');
		return;
	}
	if(telValue==''){
		MyAlert('必须填写电话！');
		return;
	}
	if(stationValue==''){
		MyAlert('必须填写到站名称！');
		return;
	}
	if(submitForm('fm'))
	{
	 if(confirm("确认添加吗？"))
		{
	     toSubmit();
		} 
	}
}

function isSubmit() {
	var url = "<%=request.getContextPath()%>/sales/storageManage/AddressAddApply/chkAddressSame.json" ;
	
	makeFormCall(url, showResult, "fm");
}

function toSubmit(){
	
	makeNomalFormCall('<%=contextPath%>/parts/baseManager/PartDealerManager/PartDlrMng/addPartDlrAddr.json',showResult,'fm');
}
//回调方法
function showResult(json){
	if(json.returnValue == 1){
		 MyAlert("新增成功!");
 		 parentContainer.parentMonth();
 	 	_hide();
	}else{
		windows.parent.MyAlert("操作失败！请联系系统管理员！");
	}
}
</script>

</body>
</html>
