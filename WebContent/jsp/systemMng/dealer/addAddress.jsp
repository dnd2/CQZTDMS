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
<title>经销商维护</title>
<script type="text/javascript">
function doInit()
{  
	genLocSel('txt1','txt2','txt3','','',''); // 加载省份城市和县
}
</script>
</head>
<body>
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 系统管理 &gt; 经销商管理 &gt;新增经销商地址</div>
 <form method="post" name = "fm" id="fm" >
 <input id="DEALER_ID" name="DEALER_ID" type="hidden" value="${dealerId}"/>
    <table width=100% border="0" align="center" cellpadding="1" cellspacing="1" class="table_query">
		  <tr>
		    <td class="table_query_2Col_label_6Letter">街道地址：</td>
		    <td>
		    <input type='text'  class="middle_txt" name="address"  id="address" datatype="0,is_null,150"  value="" size="80" style="width:320px" maxlength="150"/>
		    </td>
		     <td class="table_query_2Col_label_6Letter">联系人：</td>
		    <td>
		    <input type='text'  class="middle_txt" name="linkMan"  id="linkMan" datatype="1,is_null,8"  value="" maxlength="8"/>
		    </td>
	      </tr>
	      <tr>
		    <td class="table_query_2Col_label_6Letter">手机：</td>
		    <td>
		    <input type='text'  class="middle_txt" name="mobilePhone"  id="mobilePhone" datatype="1,is_null,20"  value="" />
		    </td>
		    <td class="table_query_2Col_label_6Letter">电话：</td>
		    <td>
		    <input type='text'  class="middle_txt" name="tel"  id="tel" datatype="1,is_null,30"  value="" maxlength="30"/>
		    </td>
	     </tr>
	     <tr>
		    <td class="table_query_2Col_label_6Letter">地址状态：
		    </td>
		    <td>
		   <label>
				<script type="text/javascript">
					genSelBoxExp("ADDRESSSTATUS",<%=Constant.STATUS%>,"-1",'',"",'',"false",'');
				</script>
		  </label>
		    </td>
	     <td class="table_query_2Col_label_6Letter">收货单位：</td>
		    <td>
		    <input type='text'  class="middle_txt" name="myaddress"  id="myaddress" datatype="1,is_null,90"  value="" maxlength="90"/>
		    </td>
	     </tr>
	     <tr>
	     	<td class="table_query_2Col_label_6Letter">省份：</td>
	      <td><select class="u-select" id="txt1" name="province" onchange="_genCity(this,'txt2')"></select> </td>
         	<td class="table_query_2Col_label_6Letter">地级市：</td>
	      <td><select class="u-select" id="txt2" name="city"  onchange="_genCity(this,'txt3')" ></select></td>  
	     </tr>
	     <tr>
	     <td class="table_query_2Col_label_6Letter">县：</td>
	      <td><select class="u-select" id="txt3" name="area" ></select></td>
	     </tr>
	     <tr style="display:none">
	     	<td class="table_query_2Col_label_6Letter">业务范围:</td>
	     	<td colspan="3">
	     		<c:forEach items="${areaList }" var="areaList">
	     			<input type="checkBox" name="addressAreas" id="${areaList.AREA_ID }" value="${areaList.AREA_ID }|${dealerId }">
	     			<label for="${areaList.AREA_ID }">${areaList.AREA_NAME }</label>
	     		</c:forEach>
	     	</td>
	     </tr>
	      <tr>
	        <td class="table_query_2Col_label_6Letter">备注：</td>
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
	var aAreas = document.getElementsByName("addressAreas") ;
	/*
	if(!aAreas || aAreas.length == 0) {
		MyAlert("业务范围未维护!") ;
		
		return false ;
	} else {
		var iAreaLen = aAreas.length ;
		var bAreaFlag = false ;
		
		for(var i=0; i<iAreaLen; i++) {
			if(aAreas[i].checked) {
				bAreaFlag = true ;
			}
		}
		
		if(!bAreaFlag) {
			MyAlert("请选择业务范围!") ;
			
			return false ;
		}
	}
	*/
	var province = document.getElementById('txt1');
	var provinceValue=province.options[province.selectedIndex].value
	var city = document.getElementById('txt2');
	var cityValue=city.options[city.selectedIndex].value
	var area = document.getElementById('txt3');
	var areaValue=area.options[area.selectedIndex].value
	if(provinceValue==''){
		MyAlert('必须填写省份！');
		return;
	}
	if(cityValue==''){
		MyAlert('必须填写地级市！');
		return;
	}
	if(areaValue==''){
		MyAlert('必须填写区县！');
		return;
	}
	if(submitForm('fm'))
	{
	 	MyConfirm("确认添加吗？",toSubmit);
	}
}

function isSubmit() {
	var url = "<%=request.getContextPath()%>/sales/storageManage/AddressAddApply/chkAddressSame.json" ;
	makeFormCall(url, toSubmit, "fm") ;
}

function toSubmit(){
	makeNomalFormCall('<%=contextPath%>/sysmng/dealer/DealerInfo/saveAddress.json',showResult,'fm');
}
//回调方法
function showResult(json){
	if(json.returnValue == '1'){
		parentContainer.parentMonth();
	 	_hide();
	}else{
		windows.parent.MyAlert("操作失败！请联系系统管理员！");
	}
}
</script>

</body>
</html>
