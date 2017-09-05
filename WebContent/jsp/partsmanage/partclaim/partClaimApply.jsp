<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!-- created by lishuai103@yahoo.com.cn 20100603 配件采购订单明细 -->
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="com.infodms.dms.common.Constant"%>
<%@taglib uri="/jstl/cout" prefix="c" %>
<%
	String contextPath = request.getContextPath();
%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta http-equiv="X-UA-Compatible" content="IE=7">
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>配件采购订单明细</title>
<script type="text/javascript">
	function doInit(){
		//查询采购订单项
   		loadShippingItems();
	}
</script>
</head>
<body>
<form name="fm" id="fm">
<div class="navigation">
  <img src="<%=request.getContextPath()%>/img/nav.gif" />&nbsp;当前位置： 配件管理&gt;配件索赔&gt;配件索赔申请</div>
 <table class="table_edit">
 	<tr >
   		<th colspan="19" align="left"><img src="<%=request.getContextPath()%>/img/subNav.gif" alt="" class="nav" /> 基本信息</th>
    </tr>
    <tr >
     <td class="table_query_3Col_label_5Letter">签收单号：</td>
     <td class="table_query_3Col_input">
     	<c:out value="${shippingInfo.SIGN_NO}"/>
     </td>
     <td class="table_query_3Col_label_5Letter">货运单号：</td>
     <td class="table_query_3Col_input">
     	<c:out value="${shippingInfo.DO_NO}"/>
     </td>
     <td class="table_query_3Col_label_6Letter">采购订单编号：</td>
     <td class="table_query_3Col_input">
     	<c:out value="${orderNo}"/>
     </td>
   </tr>
   <tr >
	<td class="table_query_3Col_label_5Letter">签收时间：</td>
     <td class="table_query_2Col_input">
	    <c:out value="${shippingInfo.SIGN_DATE}"/>
	 </td>
	 <td class="table_query_3Col_label_5Letter">运输方式：</td>
     <td class="table_query_3Col_input">
     	<script type="text/javascript">
     		writeItemValue('<c:out value="${orderInfo.TRANS_TYPE}"/>');
     	</script>
     </td>
     <td class="table_query_3Col_input">
     <!-- 
     	<input type="hidden" id="dcId" value='<c:out value="${orderInfo.DC_ID}"/>' />
     	
     -->
     	<input type="hidden" id="orderId" value='<c:out value="${shippingInfo.ORDER_ID}"/>' />
     	<input type="hidden" id="orderNo" value='<c:out value="${shippingInfo.ORDER_NO}"/>' />
     	<input type="hidden" id="doNo" value='<c:out value="${shippingInfo.DO_NO}"/>' />
     	<input type="hidden" id="signNo" value='<c:out value="${shippingInfo.SIGN_NO}"/>' />
     	<input type="hidden" id="claimId" value='<c:out value="${claimId}"/>' />
     </td>
   </tr>
 </table>
	<div id="shippingItems">
	</div>
	<br>
	<table class="table_edit">
		<tr>
		  <td align="center">		    
		  	<input type="button" name="BtnNo" value="保存" class="normal_btn" onclick="saveOrUpdateClaimOrder(0)"/>
			<input type="button" name="BtnNo" value="完成" class="normal_btn" onclick="saveOrUpdateClaimOrder(1)">
          </td>
        </tr>
	</table>  
</form>
</body>
</html>
<script type="text/javascript">
	function loadShippingItems() {
		var signNo = document.getElementById("signNo").value;
		var claimId = document.getElementById("claimId").value;
		var url = "<%=contextPath%>/partsmanage/partclaim/PartShippingAction/partShippingItems.json?signNo="+ signNo+"&claimId="+claimId;
		makeNomalFormCall(url,callBackShippingItems,'fm');
	}
	function callBackShippingItems(json) {
		var partInfo = document.getElementById("shippingItems");
		var str = '';
		str += '<table id="myTable" class="table_list">';
		str += '<tr><th colspan="38" align="left"><img src="<%=request.getContextPath()%>/img/subNav.gif" alt="" class="nav" /> 配件信息<input type="button" name="BtnYes2" value="新增" class="normal_btn" onclick="addPart()" /></th></tr>' + 
		'<tr class="descMark">' +
		' <th>配件号</th> ' + 
		' <th>配件名称</th>' +
		' <th>单位</th>' +
		' <th>订货数量</th>' +
		' <th>货运数量</th>' +
		' <th>签收数量</th>' +
		' <th>索赔数量</th>' +
		' <th>索赔类型</th>' +
		' <th>备注</th>' +
		' <th>删除</th>' +
		'</tr>';
		//var inputsArr = [];
		for (var i = 0; i < json.ps.length; i++) {
			//document.getElementById("claimId").value = json.ps[i].claimId;
			var selStr = assembleSelect(json, json.ps[i].claimType, json.ps[i].uuid);
			if (i % 2 == 0) {
				str += '<tr class="table_list_row1">';
			} else {
				str += '<tr class="table_list_row2">';
			}
			str += '<td>' + json.ps[i].partCode + '</td>'
			str += '<td>' + json.ps[i].partName + '</td>'
			str += '<td>' + json.ps[i].unit + '</td>'
			str += '<td>' + json.ps[i].orderCount + '</td>'
			str += '<td>' + json.ps[i].transCount + '</td>'
			str += '<td>' + json.ps[i].signCount + '</td>'
			str += '<td><input type="text" id="ccount' + i +'" name="ccount" onblur="modifyItem(this.value, 2, \''+json.ps[i].uuid+'\')" class="short_txt" value="' + json.ps[i].claimCount + '" /></td>'
			str += '<td>' + selStr + '</td>' 
			str += '<td><input type="text" name="cremark" onblur="modifyItem(this.value, 3, \''+json.ps[i].uuid+'\')" value="' + json.ps[i].remark + '"/></td>'
			str += '<td><input type="button" class="normal_btn" onclick="delItem(this.value, 0, \''+json.ps[i].uuid+'\')" value="删除" /></td>'
			str += '</tr>';
		}
		str += '</table>';
		partInfo.innerHTML = str;

		/*for(var n=0;n<json.ps.length;n++) {
			inputsArr[n] = document.getElementById("ccount"+n);
		}
		setMustStyle(inputsArr);//input 增加*号
		addListener();*/
	}
	
	function assembleSelect(json, claimId, uuid) {
		//根据索赔类型组装selected索赔类型
		var selStr = '<select name="claimType" id="claimType" onchange="modifyItem(this.value, 1,  \'' + uuid+'\')">';
			//如果数据库没有索赔项,select框默认为请选择
		if ('' == claimId) {
			selStr += '<option value="" selected="true">==请选择==</option>';
			for (var j = 0; j < json.pcts.length; j++) {
				selStr += '<option value="' + json.pcts[j].CLAIM_TYPE_ID + '">' + json.pcts[j].CLAIM_TYPE_NAME + '</option>';
			}
		} else {
			//默认项为数据库的索赔类型
			selStr += '<option value="" >==请选择==</option>';
			for (var j = 0; j < json.pcts.length; j++) {
				if (claimId == json.pcts[j].CLAIM_TYPE_ID) {
					selStr += '<option selected="true" value="' + json.pcts[j].CLAIM_TYPE_ID + '">' + json.pcts[j].CLAIM_TYPE_NAME + '</option>'
				} else {
					selStr += '<option value="' + json.pcts[j].CLAIM_TYPE_ID + '">' + json.pcts[j].CLAIM_TYPE_NAME + '</option>';
				}
			}
		}
		selStr += '</select>';
		return selStr;
	}

	function del(uuid) {
		makeNomalFormCall("<%=contextPath%>/partsmanage/common/PartClaimItemMemory/delPartClaimItem.json?uuid=" + uuid,callBackShippingItems,'fm','queryBtn');
	}

	function addPart() {
		//var dcId = document.getElementById("dcId").value;
		//var orderId = document.getElementById("orderId").value;
		var signNo = document.getElementById("signNo").value;
		var claimId = document.getElementById("claimId").value;
		//OpenHtmlWindow('<%=contextPath%>/partsmanage/purchase/PurchaseOrderSearch/setPartInfo.do?dcId='+dcId+'&orderId='+orderId+'&flag=1',800,500);
		OpenHtmlWindow("<%=contextPath%>/partsmanage/purchase/PurchaseOrderSearch/setPartInfo.do?flag=1&signNo="+signNo+"&claimId="+claimId,800,500);
	}
	
	function addPartInfo(json) {
		callBackShippingItems(json);
	}
	/**
		flag 0 保存操作 1 完成操作
	**/
	function saveOrUpdateClaimOrder(flag) {
	    /*
	    if(!submitForm('fm')) {
	    	//表单校验
			return false;
		}
		*/
		if (!isExist()) {
			if (validate()) {
				var orderId = document.getElementById("orderId").value;
				var claimId = document.getElementById("claimId").value;
				var doNo = document.getElementById("doNo").value;
				var url = "<%=contextPath%>/partsmanage/partclaim/PartClaimApply/saveOrUpdateClaimOrder.json?orderId="+orderId+"&claimId="+claimId+"&doNo="+doNo+"&flag="+flag;
				if (flag == 0) {
					makeNomalFormCall(url ,callBackSave,'fm','queryBtn');
				} else {
					makeNomalFormCall(url ,callBackUpdate,'fm','queryBtn');
				}
				
				//fm.method = "post";
				//fm.action = "<%=contextPath%>/partsmanage/partclaim/PartClaimApply/saveClaimOrder.do?orderId="+orderId+"&claimId="+claimId;
				//fm.submit();
			}
		}
	}
	/*
	function updatePartClaimOrder() {
		if (!isExist()) {
			if (validate()) {
				var orderId = document.getElementById("orderId").value;
				var claimId = document.getElementById("claimId").value;
				var doNo = document.getElementById("doNo").value;
				var url = "<%=contextPath%>/partsmanage/partclaim/PartClaimApply/updateClaimOrder.json?orderId="+orderId+"&claimId="+claimId+"&doNo="+doNo;
				makeNomalFormCall(url ,callBackSave,'fm','queryBtn');
			}
		}
	}
	*/
	//保存操作的回调方法
	function callBackSave(json) {
	//MyAlert(json.param[0] + '  ' + json.param[1]);
		parent.window.MyAlert('保存成功');
		var claimId = json.param[0];
		var orderNo = json.param[1];
		var doNo = json.param[2];
		fm.method = 'post';
		fm.action = '<%=contextPath%>/partsmanage/partclaim/PartClaimApply/partOrderDetail.do?orderNo=' + orderNo + '&doNo=' + doNo + '&claimId=' + claimId;
		fm.submit();
	
	}
	
	function callBackUpdate() {
		parent.window.MyAlert('申请成功');
		var url = '<%=contextPath%>/jsp/partsmanage/partclaim/partClaimApplyQuery.jsp';
		window.location = url;
	}
	
	//判断索赔项是否重复的标志,同一个配件号的索赔类型不能重复
	function isExist() {
		//索赔配件项表格
		var si = document.getElementById("myTable");
		//配件号
		var parts = new Array();
		//索赔类型
		var claimTypes = new Array();
		//除去表头
		if (si.rows.length <= 2) {
			MyAlert('至少选择一项配件进行索赔');
			return true;
		}
		for(var i=2; i < si.rows.length; i++) {
			//标识元素是否存在于已知数组中
			var isExist = false;
			for (var j = 0; j < parts.length; j++) {
				if (parts[j] == si.rows[i].cells[0].innerText) {
					isExist = true;
					break;
				} 
			}
			//如果没有相同的配件号
			if (!isExist) {
				//将配件号存进数组
				parts.push(si.rows[i].cells[0].innerText);
				//将索赔类型存进数组
				claimTypes.push(si.rows[i].cells[7].firstChild.selectedIndex);
			} else {
				//如果配件号和索赔类型相同
				if (claimTypes[j] == si.rows[i].cells[7].firstChild.selectedIndex) {
					//MyAlert('exist in  ' + (i - 1) + ' row  ' + parts[j] + '  ' + claimTypes[j]);
					MyAlert('有重复项');
					return true;
				} else {
					//将配件号存进数组
					parts.push(si.rows[i].cells[0].innerText);
					//将索赔类型存进数组
					claimTypes.push(si.rows[i].cells[7].firstChild.selectedIndex);
				}
			}
		}
		return false;
	}
	
	function validate() {
		var si = document.getElementById("myTable");
		for (var i=2; i < si.rows.length; i++) {
			var c = si.rows[i].cells[6].firstChild.value;
			var t = si.rows[i].cells[7].firstChild.value;
			if (c == '') {
				MyAlert('第' + (i-1) + '行索赔数量不能为空');
				return false;
			}
			if (isNaN(c)) {
				MyAlert('第' + (i-1) + '行索赔数量只能填入数字');
				return false;
			}
			if (c.trim().length > 4) {
				MyAlert('第' + (i-1) + '行索赔数量长度不能大于4位');
				return false;
			}
			if (t == '') {
				MyAlert('第' + (i-1) + '行索赔类型必须选择');
				return false;
			}
		}
		return true;
	}
	
	/*
		modifyType : 0 删除按钮, 1 索赔类型onchange 2 索赔数量 onblur 3 备注 onblur
	*/
	function modifyItem(value, modifyType, uuid) {
		var url = "<%=contextPath%>/partsmanage/common/PartClaimItemMemory/modifyItem.json?uuid=" + uuid + '&value=' + value + '&modifyType=' + modifyType;
		makeNomalFormCall(url ,callBack,'fm','queryBtn');
	}
	function delItem(value, modifyType, uuid) {
		var url = "<%=contextPath%>/partsmanage/common/PartClaimItemMemory/modifyItem.json?uuid=" + uuid + '&value=' + value + '&modifyType=' + modifyType;
		makeNomalFormCall(url ,callBackShippingItems,'fm','queryBtn');
	}
	function callBack() {
	}
	
</script>