<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.Constant"%>
<%@ page import="java.util.List"%>
<%@taglib uri="/jstl/cout" prefix="c"%>

<%
	String contextPath = request.getContextPath();
    int yes = Constant.IF_TYPE_YES;
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>

<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<script type="text/javascript" src="<%=request.getContextPath()%>/js/crm/customer/common.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/regionUtils.js"></script>

<script type="text/javascript">
	function doInit(){
		var msg = document.getElementById("errorMsg").value;
		if(msg!=null&&msg!="") {
			MyAlert(msg);
			msg="";
		}
		loadcalendar();   //初始化时间控件

		var obj = document.getElementById("laoSucessNO"); //定位id
		var index = obj.selectedIndex; // 选中索引
		var laoSucessNO = obj.options[index].value; // 选中值	
		if(laoSucessNO=="60581002"){
			oldhao.style.display = "none";
			oldhao1.style.display = "none";
			}
		var relation="${relation_code}";
		if(relation=="60581001" || relation=="60581002"){
			
			var obj = document.getElementById("JsSucessNO"); //定位id
			var index = obj.selectedIndex; // 选中索引
			var JsSucessNO = obj.options[index].value; // 选中值		
			var oldcustomerList= document.getElementById("oldcustomerList");
			var oldInfo= document.getElementById("oldInfo");
			document.getElementById("JsSucessNO").value="10041001";
			oldcustomerList.style.display = "block";
			oldInfo.style.display = "block";
		}
		
	}
	function txtClr(valueId) {
		document.getElementById(valueId).value = '' ;
	}
	function doSave(){
		var baocun = document.getElementById("baocun");
		var quxiao = document.getElementById("quxiao");
		var ownerName = document.getElementById("owner_name").value;//车主姓名
		var ownerPhone = document.getElementById("owner_phone").value;//车主电话
		var intentType = document.getElementById("intent_type").value;//意向等级
		var salesProgress = document.getElementById("sales_progress").value;//销售流程进度
		var dealType = document.getElementById("deal_type").value;//购置方式
		var testDriving = document.getElementById("test_driving").value;//试乘试驾
		var orderStatus = document.getElementById("orderStatus").value;
		var oldCustomerName = document.getElementById("old_customer_name").value;
		var oldTelephone = document.getElementById("old_telephone").value;
		oldTelephone=oldTelephone.replace(/(^\s+)|(\s+$)/g, ""); 
		var reg = new RegExp("^[0-9]*$");
		var obj = document.getElementById("laoSucessNO"); //定位id
		var index = obj.selectedIndex; // 选中索引
		var laoSucessNO = obj.options[index].value; // 选中值	

		var obj1 = document.getElementById("JsSucessNO"); //定位id
		var index1 = obj1.selectedIndex; // 选中索引
		var JsSucessNO = obj1.options[index1].value; // 选中值	
		if(JsSucessNO=="10041001"){
			if(laoSucessNO=="60581002"){
				if(oldCustomerName ==null||oldCustomerName=="") {
					 MyAlert("朋友/老客户姓名不能为空!");
			         return false;
				}
				if(oldTelephone ==null||oldTelephone=="") {
					 MyAlert("朋友/老客户联系电话不能为空!");
			         return false;
				}
				if(!reg.test(oldTelephone)){
			         MyAlert("朋友/老客户联系电话格式不正确!");
			         return false;
			    }
				
				if(oldTelephone.length != 11 )
			    {
			        MyAlert('朋友/老客户请输入有效的联系电话！');
			        return false;
			    } 
		
			}
			if(laoSucessNO=="60581001"){
				var oldVehicleId= document.getElementById("old_vehicle_id").value;
				if(oldCustomerName ==null||oldCustomerName=="") {
					 MyAlert("朋友/老客户姓名不能为空!");
			         return false;
				}
				if(oldTelephone ==null||oldTelephone=="") {
					 MyAlert("朋友/老客户联系电话不能为空!");
			         return false;
				}
				if(!reg.test(oldTelephone)){
			         MyAlert("朋友/老客户联系电话格式不正确!");
			         return false;
			    }
				if(oldTelephone.length != 11 )
			    {
			        MyAlert('朋友/老客户请输入有效的联系电话！');
			        return false;
			    } 
				if(oldVehicleId ==null||oldVehicleId=="") {
					 MyAlert("老客户车架号不能为空!");
			         return false;
				}
				var oldVin=0;
				var beCount=0;
				var url = "<%=contextPath%>/crm/taskmanage/TaskManage/getOldCustomerVin.json?oldVehicleId="+oldVehicleId+"&telePhone="+ownerPhone;
				makeSameCall(url, showInfo1, "fm") ;
				function showInfo1(json) {
					oldVin=json.oldVin;
					oldCount=json.oldCount;
					}
				if(oldVin==0){
					MyAlert("老客户车架号信息不存在！请重新输入！");
					return false;
					}
				if(oldCount>0){
					MyAlert("该客户已经被推荐过了！");
					return false;
					}
			
				}
		}		
		if(orderStatus!="60231011"){
		var updateRadio = document.getElementById("update_radio");
		}
		if(!isIdentityNumber()){
			return;
		}
		var reg = new RegExp("^[0-9]*$");
		if(!reg.test(ownerPhone)){
	         MyAlert("车主电话格式不正确!");
	         return false;
	    }
		if(ownerPhone.length != 11 )
	    {
	        MyAlert('请输入有效的车主电话！');
	        return false;
	    } 
		if(ownerName ==null||ownerName=="") {
			 MyAlert("车主姓名不能为空!");
	         return false;
		}
		if(ownerPhone ==null||ownerPhone=="") {
			 MyAlert("车主电话不能为空!");
	         return false;
		}
		if(intentType ==null||intentType=="") {
			 MyAlert("请选择意向等级!");
	         return false;
		}
		if(salesProgress ==null||salesProgress=="") {
			 MyAlert("请选择销售流程进度!");
	         return false;
		}
		if(dealType ==null||dealType=="") {
			 MyAlert("请选择购置方式!");
	         return false;
		}
		if(testDriving ==null||testDriving=="") {
			 MyAlert("请选择是否试乘试驾!");
	         return false;
		}
		if(orderStatus!="60231011")
		{
			if(updateRadio.checked == true) {
				if(!doCheckTable1()) {//检查未确定车型表格
					return false;
				}
				if(!doCheckTable2()) {//检查已确定车型表格
					return false;
				}
			}
		}
		var orderId = document.getElementById("orderId").value;
		var customerId = document.getElementById("customerId").value;
		var table1 = document.getElementById("noConfirmVehicleTable");
		var s1 = table1.rows[table1.rows.length-1].id.charAt(table1.rows[table1.rows.length-1].id.length - 1);
		var table2 = document.getElementById("ConfirmVehicleTable");
		var s2 = table2.rows[table2.rows.length-1].id.charAt(table2.rows[table2.rows.length-1].id.length - 1);
		
		if(s1==0&&s2==0) {
			MyAlert("请添加车辆!");
			return false;
		}
		baocun.disabled = true;
		quxiao.disabled = true;
		if(orderStatus=="60231011")
		{
			var AuditFlag=doAlertCount();
			if(!AuditFlag){//输出 订单中车总数量,确认车的总数量是不是大于1台车 大于需要提交经理审核
			   return false;	
			}	
			var url = "<%=contextPath%>/crm/order/OrderManage/orderBohUpdate.json?typeflag=update&AuditFlag="+AuditFlag+"&orderId="+orderId+"&customerId="+customerId+"&table1Length="+s1+"&table2Length="+s2;
			makeFormCall(url, doTask, "fm") ;
			function doTask(json) {
				MyAlert("保存成功!");
				$('fm').action = "<%=contextPath%>/crm/order/OrderManage/doInit.do";
				$('fm').submit();
				baocun.disabled = false;
				quxiao.disabled = false;
			}
		}else{
			if(updateRadio.checked == true) {
				if(judgeOrderDetail()){
					MyAlert("确认车架号的，有一台车数据状态不对，请重新选择！！！");
					baocun.disabled = false;
					quxiao.disabled = false;
					return;
				}
				var url = "<%=contextPath%>/crm/order/OrderManage/orderUpdate.json?typeflag=update&orderId="+orderId+"&customerId="+customerId+"&table1Length="+s1+"&table2Length="+s2;
				makeFormCall(url, doTask, "fm") ;
				function doTask(json) {
					MyAlert("修改订单成功!");
					$('fm').action = "<%=contextPath%>/crm/order/OrderManage/doInit.do";
					$('fm').submit();
					baocun.disabled = false;
					quxiao.disabled = false;
				}
			} else {
				var url2 = "<%=contextPath%>/crm/order/OrderManage/orderUpdate.json?typeflag=toBack&orderId="+orderId+"&customerId="+customerId+"&table1Length="+s1+"&table2Length="+s2;
				makeFormCall(url2, doTask2, "fm") ;
				function doTask2(json) {
					MyAlert("退单成功!");
					$('fm').action = "<%=contextPath%>/crm/order/OrderManage/doInit.do";
					$('fm').submit();
					baocun.disabled = false;
					quxiao.disabled = false;
				}
			}
		}
	}


	//输出订单中车的总数量
	function doAlertCount(){
		var count=0;//总订车数
		
		var table = document.getElementById("noConfirmVehicleTable");
		var s = table.rows[table.rows.length-1].id.charAt(table.rows[table.rows.length-1].id.length - 1);
		var table2 = document.getElementById("ConfirmVehicleTable");
		var s2 = table2.rows[table2.rows.length-1].id.charAt(table2.rows[table2.rows.length-1].id.length - 1);
		
		var numrow=1;
		var ynumyrow=1;
		
		for(var i1=1;i1<=parseInt(s);i1++) {
			var model = document.getElementById("numrow"+i1).value;//数量
			count+=parseInt(model);
		}
		
		for(var i1=1;i1<=parseInt(s2);i1++) {
		var model = document.getElementById("ynumyrow"+i1).value;//数量
		count+=parseInt(model);
		}	
		if(count>1){
	    var confirm=window.confirm("总订车数量是:"+count+",订车数量大于1台将提交经理审核！请确认！");
		}
		if(confirm==true ){
			return true;
		}else if(confirm==false){ 
			return false;
		}else if(count==1){
			return "sucess";
			}
	}
	
	//已经确认车架号的保存时候校验车辆的状态是否对
	function judgeOrderDetail(){
		var flag=false;
		var table = document.getElementById("ConfirmVehicleTable");
		//s为行数
		var s = table.rows[table.rows.length-1].id.charAt(table.rows[table.rows.length-1].id.length - 1);
		var yvinIdyrow=1;
		var ypriceyrow=1;
		var ynumyrow=1;
		var ydeposityrow=1;
		var ypre_pay_dateyrow=1;
		var ypre_delivery_dateyrow=1;
		var reg = new RegExp("^[0-9]*$");
		var array=new Array();
		for(var i1=1;i1<=parseInt(s);i1++) {
			if(document.getElementById("yvinIdyrow"+i1)!=null) {
				var vinId = document.getElementById("yvinIdyrow"+i1).value;//VIN
				flag=judgeVinStatus(vinId);
				if(flag){
					break;
				}
			}
		}
		return flag;
	}
	function judgeVinStatus(vin){
		var flag=false;
		var url = "<%=contextPath%>/crm/taskmanage/TaskManage/judgeVinStatus.json?vin="+vin+"&flag=1";
		makeSameCall(url, doResult, "fm") ;
		function doResult(json) {
			if(json.flag=='1'){
				flag=true;
			}
		}
		return flag;
	}
    //添加验证身份证号码 
	function isIdentityNumber() {
		var card_type=document.getElementById("owner_paper_type").value;
		if(card_type!=10931001){
			return true;
		}
		var identityNumber = document.getElementById("owner_paper_no").value;
		//var sexOnPage = document.getElementById('sex').value;
		if(null==identityNumber ||undefined==identityNumber|| ''==identityNumber ){
			return true;
		}
		identityNumber = identityNumber.toUpperCase();  
	    //身份证号码为15位或者18位，15位时全为数字，18位前17位为数字，最后一位是校验位，可能为数字或字符X。   
	    if(!(/(^\d{15}$)|(^\d{17}([0-9]|X)$)/.test(identityNumber))) { 
	    	MyAlert('输入的身份证号长度不对，或者号码不符合规定！15位号码应全为数字，18位号码末位可以为数字或X。'); 
	        return false; 
	    };
	    var len = identityNumber.length;
	    var year;
	    var month;
	    var day;
	    var sex;
	    if(len == 15) {
	    	year = identityNumber.substr(6,2);
	    	year += '19';
	        month = identityNumber.substr(8,2);     
	        day = identityNumber.substr(10,2); 
	        sex = identityNumber.substr(14);
	    } else if(len == 18) {
	    	year = identityNumber.substr(6,4);     
	        month = identityNumber.substr(10,2);     
	        day = identityNumber.substr(12,2); 
	        sex = identityNumber.substr(16,1);
	    }
	   	 //验证月份
		   if(month.substr(0,1)!=0){
		    	if(parseInt(month)>12){
		    		MyAlert("输入的身份证号月份不匹配!");
		    		return false;
		    	}
		 	}
		 	if(day.substr(0,1)!=0){
		 		if(parseInt(day)>31){
		    		MyAlert("输入的身份证号天数不匹配!");
		    		return false;
		    	}
		 	}
	   
	    return true;
	}
	function addRow() {
		var nowDate=$("nowDate").value;
		var table = document.getElementById("noConfirmVehicleTable");
		var length=document.getElementById("noConfirmVehicleTable").rows.length;
		var s = table.rows[table.rows.length-1].id.charAt(table.rows[table.rows.length-1].id.length - 1);
		var rownum = parseInt(s)+1;
		var tr=document.createElement("tr");
		tr.id="row"+rownum;
		//tr.id=length;
		//tr.id="row"+tr.id;
		tr.style.height="23px";
		tr.style.backgroundColor='white';
		tr.style.border=0;
		tr.style.borderColor="#44BBBB";
		//第一列（车系）
		var td=document.createElement("td");
		colorTdId = tr.id+"col"+2;
		td.innerHTML = "<input type='text' class='middle_txt' id='materialCode"+tr.id+"' name='materialCode"+tr.id+"'  value='' style='display: inline-block; float: left' readonly='readonly'  /><input type='hidden' name='materialName"+tr.id+"' size='20' id='materialName"+tr.id+"' value='' /><input type='button' value='...' class='mini_btn'  onclick=\"toIntentVechileList('materialCode"+tr.id+"','materialName"+tr.id+"');\" style='display: inline-block;'/>";
		td.id=tr.id+"col"+1;
		tr.appendChild(td);
		//第二列（颜色）
		var td=document.createElement("td");
		td.innerHTML = "<select id='intent_color"+tr.id+"' name='intent_color"+tr.id+"'><c:forEach items='${colorList }' var='colorList'><option value='${colorList.CODE_ID }'>${colorList.CODE_DESC }</option></c:forEach></select>";
		td.id=tr.id+"col"+2;
		tr.appendChild(td);
		//第三列（价格）
		var td=document.createElement("td");
		td.innerHTML = "<input type='text' style='width:80px' id='price"+tr.id+"' name='price"+tr.id+"' checkFlag='jiage' onblur='checkNum(this)'/>";
		td.id=tr.id+"col"+3;
		tr.appendChild(td);
		//第四列（数量）
		var td=document.createElement("td");
		td.innerHTML = "<input type='text' style='width:70px' id='num"+tr.id+"' name='num"+tr.id+"' checkFlag='shuliang' onblur='checkNum(this)'/>";
		td.id=tr.id+"col"+4;
		tr.appendChild(td);
		//第五列（总金额）
		var td=document.createElement("td");
		td.innerHTML = "<input type='text' id='amount"+tr.id+"' name='amount"+tr.id+"' style='width:70px' name='amount"+tr.id+"' readonly='readonly' style='background-color: #EEEEEE;'/>";
		td.id=tr.id+"col"+5;
		tr.appendChild(td);
		//第六列（订金）
		var td=document.createElement("td");
		td.innerHTML = "<input type='text' style='width:50px' id='deposit"+tr.id+"' name='deposit"+tr.id+"' checkFlag='dingjin' onblur='checkNum(this)'/>";
		td.id=tr.id+"col"+6;
		tr.appendChild(td);
		//第六列（定金）
		var td=document.createElement("td");
		td.innerHTML = "<input type='text' style='width:50px' id='earnest"+tr.id+"' name='earnest"+tr.id+"' checkFlag='dingjin2' onblur='checkNum(this)'/>";
		td.id=tr.id+"col"+10;
		tr.appendChild(td);
		//第七列（余款付款日期）
		var td=document.createElement("td");
		td.innerHTML = "<input type=\"text\" value=\"\" name=\"pre_pay_date"+tr.id+"\" id=\"pre_pay_date"+tr.id+"\" group=\"pre_pay_date"+tr.id+"\" class=\"short_txt\" datatype=\"1,is_date,10\" size=\"20\" hasbtn='true' readonly=\"readonly\" maxlength=\"60\" callFunction=\"showcalendar(event, 'pre_pay_date"+tr.id+"', false);\" /><span><IMG id=srtImg title=点击按钮 src=\"/dms/images/default/btn.gif\" width=15 height=15 onclick=\"showcalendar(event, 'pre_pay_date"+tr.id+"', false);\"></span>";
		td.id=tr.id+"col"+7;
		tr.appendChild(td);
		//第八列（交车日期）
		var td=document.createElement("td");
		td.innerHTML = "<input type=\"text\" value=\""+nowDate+"\" name=\"pre_delivery_date"+tr.id+"\" id=\"pre_delivery_date"+tr.id+"\" group=\"pre_delivery_date"+tr.id+"\" class=\"short_txt\" datatype=\"1,is_date,10\" size=\"20\" hasbtn='true' readonly=\"readonly\" maxlength=\"60\" callFunction=\"showcalendar(event, 'pre_delivery_date"+tr.id+"', false);\" /><span><IMG id=srtImg title=点击按钮 src=\"/dms/images/default/btn.gif\" width=15 height=15 onclick=\"showcalendar(event, 'pre_delivery_date"+tr.id+"', false);\"></span>";
		td.id=tr.id+"col"+8;
		tr.appendChild(td);
		//第九列（已交车数量）
		var td=document.createElement("td");
		td.innerHTML = "<input type='text' id='delivery_number"+tr.id+"' name='delivery_number"+tr.id+"' value='0' style='width:56px' readonly='readonly' style='background-color: #EEEEEE;'/><input type='button' onclick=deleteRowBy('"+tr.id+"') value='删除' style='margin-left: 5px' />";
		td.id=tr.id+"col"+9;
		tr.appendChild(td);
		
		document.getElementById("newbody").appendChild (tr);
	}
	function deleteRowBy(rowNum) {
		//取行数
		var rownum = rowNum.charAt(rowNum.length - 1);
		var willDeleteRow = document.getElementById("noConfirmVehicleTable");
		var tr = document.getElementById(rowNum);
		if(rownum<1) {
			return false;
		} else {
			willDeleteRow.deleteRow(tr.rowIndex);
		}
	}
	function updateRowBy(rowNum) {
		//取行数
		var rownum = rowNum.charAt(rowNum.length - 1);
		var willDeleteRow = document.getElementById("noConfirmVehicleTable");
		var tr = document.getElementById(rowNum);
		if(rownum<1) {
			return false;
		} else {
			addUpdateRow(rownum); 
			willDeleteRow.deleteRow(tr.rowIndex);
		}
	}
	function addUpdateRow(srownum) {
		var nowDate=$("nowDate").value;
		var table = document.getElementById("noConfirmVehicleTable");
		var length=document.getElementById("noConfirmVehicleTable").rows.length;
		var s = table.rows[table.rows.length-1].id.charAt(table.rows[table.rows.length-1].id.length - 1);
		var rownum = parseInt(s)+1;
	
		var GROUP_CODE=document.getElementById("materialCoderow"+srownum).value;
		var INTENT_MODEL=document.getElementById("materialNamerow"+srownum).value;
		var OrderDetailId=document.getElementById("hiddendetailrow"+srownum).value;
		var colorId=document.getElementById("intent_colorrow"+srownum).value;
		var selectIndex = document.getElementById("intent_colorrow"+srownum).selectedIndex;//获得是第几个被选中了
		var colorName = document.getElementById("intent_colorrow"+srownum).options[selectIndex].text //获得被选中的项目的文本
		var price=document.getElementById("pricerow"+srownum).value;
		var num=document.getElementById("numrow"+srownum).value;
		var amount=document.getElementById("amountrow"+srownum).value;
		var deposit=document.getElementById("depositrow"+srownum).value;
		var earnest=document.getElementById("earnestrow"+srownum).value;
		var pre_pay_date=document.getElementById("pre_pay_daterow"+srownum).value;
		var pre_delivery_date=document.getElementById("pre_delivery_daterow"+srownum).value;
		var delivery_number=document.getElementById("delivery_numberrow"+srownum).value;
	
		var tr=document.createElement("tr");
		tr.id="row"+rownum;
		//tr.id=length;
		//tr.id="row"+tr.id;
		tr.style.height="23px";
		tr.style.backgroundColor='white';
		tr.style.border=0;
		tr.style.borderColor="#44BBBB";
		//第一列（车系）
		var td=document.createElement("td");
		colorTdId = tr.id+"col"+2;
		td.innerHTML = "<input type='text' class='middle_txt' id='materialCode"+tr.id+"' name='materialCode"+tr.id+"'  value='"+GROUP_CODE+"' style='display: inline-block; float: left' readonly='readonly'  /><input type='hidden' name='materialName"+tr.id+"' size='20' id='materialName"+tr.id+"' value='"+INTENT_MODEL+"' /><input type='hidden' name='materialNameh"+tr.id+"' size='20' id='materialNameh"+tr.id+"' value='"+OrderDetailId+"' />";
		td.id=tr.id+"col"+1;
		tr.appendChild(td);
		//第二列（颜色）
		var td=document.createElement("td");
		td.innerHTML = "<select id='intent_color"+tr.id+"' name='intent_color"+tr.id+"'><option value='"+colorId+"'>"+colorName+"</option></select>";
		td.id=tr.id+"col"+2;
		tr.appendChild(td);
		//第三列（价格）
		var td=document.createElement("td");
		td.innerHTML = "<input type='text' style='width:80px '  style='background-color: #EEEEEE;' id='price"+tr.id+"' name='price"+tr.id+"' checkFlag='jiage' value='"+price+"' readonly='readonly' />";
		td.id=tr.id+"col"+3;
		tr.appendChild(td);

		//第四列（数量）
		var td=document.createElement("td");
		td.innerHTML = "<input type='text' style='width:70px' id='num"+tr.id+"' name='num"+tr.id+"' value='"+num+"' checkFlag='shuliang' onblur='checkNum(this)' />";
		td.id=tr.id+"col"+4;
		tr.appendChild(td);
		//第五列（总金额）
		var td=document.createElement("td");
		td.innerHTML = "<input type='text' id='amount"+tr.id+"' name='amount"+tr.id+"' style='width:70px' name='amount"+tr.id+"'value='"+amount+"' readonly='readonly' style='background-color: #EEEEEE;'/>";
		td.id=tr.id+"col"+5;
		tr.appendChild(td);
		//第六列（订金）
		var td=document.createElement("td");
		td.innerHTML = "<input type='text' style='width:50px' id='deposit"+tr.id+"' name='deposit"+tr.id+"' readonly='readonly' style='background-color: #EEEEEE;' value='"+deposit+"' checkFlag='dingjin' onblur='checkNum(this)'/>";
		td.id=tr.id+"col"+6;
		tr.appendChild(td);
		//第六列（定金）
		var td=document.createElement("td");
		td.innerHTML = "<input type='text' style='width:50px' id='earnest"+tr.id+"' name='earnest"+tr.id+"' readonly='readonly' style='background-color: #EEEEEE;' value='"+earnest+"' checkFlag='dingjin2' onblur='checkNum(this)'/>";
		td.id=tr.id+"col"+10;
		tr.appendChild(td);
		//第七列（余款付款日期）
		var td=document.createElement("td");
		td.innerHTML = "<input type=\"text\" value='"+pre_pay_date+"' readonly='readonly' style='background-color: #EEEEEE;' name=\"pre_pay_date"+tr.id+"\" id=\"pre_pay_date"+tr.id+"\" group=\"pre_pay_date"+tr.id+"\" class=\"short_txt\" datatype=\"1,is_date,10\" size=\"20\" hasbtn='true' readonly=\"readonly\" maxlength=\"60\" callFunction=\"showcalendar(event, 'pre_pay_date"+tr.id+"', false);\" /><span><IMG id=srtImg title=点击按钮 src=\"/dms/images/default/btn.gif\" width=15 height=15 onclick=\"showcalendar(event, 'pre_pay_date"+tr.id+"', false);\"></span>";
		td.id=tr.id+"col"+7;
		tr.appendChild(td);
		//第八列（交车日期）
		var td=document.createElement("td");
		td.innerHTML = "<input type=\"text\" value='"+pre_delivery_date+"' readonly='readonly' style='background-color: #EEEEEE;'  name=\"pre_delivery_date"+tr.id+"\" id=\"pre_delivery_date"+tr.id+"\" group=\"pre_delivery_date"+tr.id+"\" class=\"short_txt\" datatype=\"1,is_date,10\" size=\"20\" hasbtn='true' readonly=\"readonly\" maxlength=\"60\" callFunction=\"showcalendar(event, 'pre_delivery_date"+tr.id+"', false);\" /><span><IMG id=srtImg title=点击按钮 src=\"/dms/images/default/btn.gif\" width=15 height=15 onclick=\"showcalendar(event, 'pre_delivery_date"+tr.id+"', false);\"></span>";
		td.id=tr.id+"col"+8;
		tr.appendChild(td);
		//第九列（已交车数量）
		var td=document.createElement("td");
		td.innerHTML = "<input type='text' id='delivery_number"+tr.id+"' name='delivery_number"+tr.id+"' value='"+delivery_number+"' style='width:56px' readonly='readonly' style='background-color: #EEEEEE;'/>";
		td.id=tr.id+"col"+9;
		tr.appendChild(td);
		
		document.getElementById("newbody").appendChild (tr);
		
		
		
	}
	function doFindColor(inputId,nextTdId) {
		var url = "<%=contextPath%>/crm/taskmanage/TaskManage/colorInfoBySeries.json?inputId="+inputId;
		makeFormCall(url, showInfo, "fm") ;
		function showInfo(json) {
			if(json.ps[0]!=null) {
				var colortd=document.getElementById(nextTdId);
				colortd.innerHTML = "";
				var str = "<select ";
				for(var i=0;i<json.psSize;i++) {
					str = str + "id='theColor"+nextTdId+"'>";
					str = str + "<option value="+json.ps[i].COLOR_NAME+">"+json.ps[i].COLOR_NAME+"</option>";
				}
				str = str + "</select>";
				colortd.innerHTML = str;
			}
		}
	}
	function getZ(n){    return /^\d+(\.\d+)?$/.test(n+"");}
	function checkNum(obj) {
		var reg = new RegExp("^[0-9]*$");
		//取行数
		var rownum = obj.id.charAt(obj.id.length - 1);
		var color=document.getElementById("intent_colorrow"+rownum).value;
		if(color==0){
			MyAlert("请先选择车辆颜色！");
			document.getElementById("pricerow"+rownum).value="";
			return false;
			}
		var num=document.getElementById("numrow"+rownum).value;
		var delivery_number=document.getElementById("delivery_numberrow"+rownum).value;
	
		if(num<delivery_number){
			MyAlert("修改数量不能小于已交车数量!");
			document.getElementById("numrow"+rownum).value="";
	        return false;
			}
		//价格
		if(obj.getAttribute("checkFlag")=="jiage") {
			var shuliang = "numrow"+rownum;
			var zongjine = "amountrow"+rownum;
			if(!getZ(obj.value)){
		         MyAlert("价格格式不正确!");
		         return false;
		    } else if(document.getElementById(shuliang).value!=null && document.getElementById(shuliang).value!="") {
		    	if(!reg.test(document.getElementById(shuliang).value)) {
		    		MyAlert("数量格式不正确!");
			         return false;
		    	} else {
		    		document.getElementById(zongjine).value = document.getElementById(shuliang).value * obj.value;
		    	}
		    }
		} else if(obj.getAttribute("checkFlag")=="shuliang"){
			var jiage = "pricerow"+rownum;
			var zongjine = "amountrow"+rownum;
			if(!reg.test(obj.value)){
		         MyAlert("数量格式不正确!");
		         return false;
		    } else if(document.getElementById(jiage).value!=null && document.getElementById(jiage).value!="") {
		    	if(!getZ(document.getElementById(jiage).value)) {
		    		MyAlert("价格格式不正确!");
			         return false;
		    	} else {
		    		document.getElementById(zongjine).value = document.getElementById(jiage).value * obj.value;
		    	}
		    }
		} else if(obj.getAttribute("checkFlag")=="dingjin"){
			if(!getZ(obj.value)) {
	    		MyAlert("订金格式不正确!");
		         return false;
	    	}
		} else if(obj.getAttribute("checkFlag")=="dingjin2"){
			if(!getZ(obj.value)) {
	    		MyAlert("定金格式不正确!");
		         return false;
	    	}
		}
	}


	function doCusJsChange(){
		var obj = document.getElementById("JsSucessNO"); //定位id
		var index = obj.selectedIndex; // 选中索引
		var JsSucessNO = obj.options[index].value; // 选中值		
		var oldcustomerList= document.getElementById("oldcustomerList");
		var oldInfo= document.getElementById("oldInfo");
		if(JsSucessNO=='10041001'){
			oldcustomerList.style.display = "block";
			oldInfo.style.display = "block";
		}else{
			oldcustomerList.style.display = "none";
			oldInfo.style.display = "none";
			}
		}

	function doCusTypeChange(){
		var obj = document.getElementById("laoSucessNO"); //定位id
		var index = obj.selectedIndex; // 选中索引
		var laoSucessNO = obj.options[index].value; // 选中值	
		var oldhao=document.getElementById("oldhao");
		var oldhao1=document.getElementById("oldhao1");
		if(laoSucessNO=='60581001'){
			oldhao.style.display = "block";
			oldhao1.style.display = "block";
		}else{
			oldhao.style.display = "none";
			oldhao1.style.display = "none";
			}	
		
		}
</script>
<title>订单任务</title>
</head>
<body onunload='javascript:destoryPrototype();' onload="addRegionInit();">
	<div class="wbox">
		<div class="navigation">
			<img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置>潜客管理>日程管理>任务管理>订单任务
		</div>
		<form id="fm" name="fm" method="post">
			<input type="hidden" name="curPage" id="curPage" value="1" /> 
			<input type="hidden" id="dlrId" name="dlrId" value="" />
			<input type="hidden" id="orderId" name="orderId" value="${orderId}" />
			<input type="hidden" id="provinceId" name="provinceId" value="${oldPro}" />
			<input type="hidden" id="cityId" name="cityId" value="${oldCity}" />
			<input type="hidden" id="townId" name="townId" value="${oldArea}" />
			<input type="hidden" id="orderStatus" name="orderStatus" value="${orderStatus}" />
			<input type="hidden" name="errorMsg" id="errorMsg" value="${errorMsg }" />
			<input type="hidden" name="nowDate" id="nowDate" value="${nowDate }" />
			
			<table class="table_query" width="95%" align="center">
				<tr>	
					<td id="qingLao" align="right" width="15%">是否朋友/老客户介绍:</td>
					<td id="qingLao1" ><select id="JsSucessNO" name="JsSucessNO"
						onchange="doCusJsChange();">
							<option id="JsSu" value="10041002">否</option>
							<option id="JsNo" value="10041001">是</option>
					</select></td>
				</tr>

			</table>
		
			<div id="oldcustomerList" style="display: none" >
				<b style="display: inline-block; float: left">朋友/老客户信息</b>
				<hr style="display: inline-block;">
			</div>
			</br>
			
			<table id="oldInfo" class="table_query" width="95%" align="center"  style="display: none">
				<tr>
					<td align="right" width="10%">介绍类型:</td>
					<td ><select id="laoSucessNO" name="laoSucessNO"
						onchange="doCusTypeChange();">
							<c:if test="${relation_code=='' || relation_code==null }">
								<option id="laoSu" value="60581001">老客户介绍</option>
								<option id="laoNo" value="60581002">朋友介绍</option>
							</c:if>
							<c:if test="${relation_code=='60581002'}">
							<option id="laoNo" value="60581002">朋友介绍</option>
							<option id="laoSu" value="60581001">老客户介绍</option>
							</c:if>
							<c:if test="${relation_code=='60581001'}">
							<option id="laoSu" value="60581001">老客户介绍</option>
							<option id="laoNo" value="60581002">朋友介绍</option>
							</c:if>
					</select></td>
				</tr>
				
				<tr id="Laohidden"   align="right"    >
						<td align="right" width="12%">
							朋友/老客户姓名：
						</td>
						<td width="12%" align="left" >
							<input id="old_customer_name" name="old_customer_name" type="text"
								class="middle_txt" datatype="1,is_textarea,30" size="20"
								value="${oldCustomerName }" maxlength="60" />
						</td>
						<td align="right" width="12%">
							朋友/老客户电话：
						</td>
						<td width="12%" align="left" >
							<input id="old_telephone" name="old_telephone"
								onchange="nameBlurChange111()" type="text" class="middle_txt"
								datatype="1,is_textarea,30" value="${oldTelephone }" size="20"
								maxlength="60" />
						</td>
						<td align="right" width="12%" id="oldhao">
							老客户车架号：
						</td>
						<td width="17%" id="oldhao1" align="left" >
							<input id="old_vehicle_id" name="old_vehicle_id" 
								onchange="nameBlurChange11()" type="text" class="middle_txt"
								datatype="1,is_textarea,30" value="${oldVehicleId }" size="20"
								maxlength="60" />
						</td>
					</tr>
			</table>
			</br>
			
			<div id="customerList">
				<b style="display: inline-block; float: left">客户信息</b>
				<hr style="display: inline-block;">
			</div>
			</br>
			
			<table class="table_query" width="95%" align="center">
			<c:forEach items="${customerList }" var="customerList">
			<input type="hidden" id="customerId" name="customerId" value="${customerList.CUSTOMER_ID}" />
				<tr>
					<td align="right" width="12%">客户编码：</td>
					<td><input id="customer_code" name="customer_code" type="text" readonly="readonly" style="background-color: #EEEEEE;"
						class="middle_txt" datatype="1,is_textarea,30" size="20" value="${customerList.CUSTOMER_CODE }"
						maxlength="60" /></td>
					<td align="right" width="14%">客户姓名：</td>
					<td><input id="customer_name" name="customer_name" type="text" readonly="readonly" style="background-color: #EEEEEE;"
						class="middle_txt" datatype="1,is_textarea,30" size="20" value="${customerList.CUSTOMER_NAME }"
						maxlength="60" /></td>
					<td align="right" width="14%">联系电话：</td>
					<td width="12%"><input id="telephone" name="telephone" readonly="readonly" style="background-color: #EEEEEE;"
						type="text" class="middle_txt" datatype="1,is_textarea,30" value="${customerList.TELEPHONE }"
						size="20" maxlength="60" /></td>
					<td align="right" width="6%">客户地址：</td>
					<td width="12%"><input id="address" name="address" readonly="readonly" style="background-color: #EEEEEE;"
						type="text" class="middle_txt" datatype="1,is_textarea,30" value="${customerList.ADDRESS }"
						size="20" maxlength="60" /></td>
				</tr>
				
				<tr>
					<td align="right" width="6%">客户证件名：</td>
					<td><input id="paper_name" name="paper_name" type="text" readonly="readonly" style="background-color: #EEEEEE;"
						class="middle_txt" datatype="1,is_textarea,30" size="20" value="${customerList.PAPER_TYPE }"
						maxlength="60" /></td>
					<td align="right" width="6%">客户证件号：</td>
					<td><input id="paper_no" name="paper_no" type="text" readonly="readonly" style="background-color: #EEEEEE;"
						class="middle_txt" datatype="1,is_textarea,30" size="20" value="${customerList.PAPER_NO }"
						maxlength="60" /></td>
					<td width="3%" align="right"></td>
					<td width="22%"></td>
					<td align="right" width="15%"></td>
					<td width="25%"></td>
				</tr>
				</c:forEach>
			</table>
			</br>
			<div>
				<b style="display: inline-block; float: left">车主信息</b><hr style="display: inline-block;">
			</div>
			</br>
			<table class="table_query" width="95%" align="center">
			<c:forEach items="${ownerList }" var="ownerList">
				<tr>
					<td align="right" width="13%">车主姓名：</td>
					<td><input id="owner_name" name="owner_name" type="text"
						class="middle_txt" datatype="1,is_textarea,30" size="20" value="${ownerList.OWNER_NAME }"
						maxlength="60" /></td>
					<td align="right" width="8%">车主电话：</td>
					<td><input id="owner_phone" name="owner_phone" type="text"
						class="middle_txt" datatype="1,is_textarea,30" size="20" value="${ownerList.OWNER_PHONE }"
						maxlength="60" /></td>
					<td align="right" width="12%">证件类型：</td> 
					<td width="25%">
					<input type="hidden" id="owner_paper_type" name="owner_paper_type" value="${ownerList.OWNER_PAPER_TYPE }"/>
		      		<div id="ddtopmenubar29" class="mattblackmenu">
						<ul> 
							<li>
								<a style="width:103px;" rel="ddsubmenu29" href="###" isclick="true" onclick="stree.loadtree(this, '<%= request.getContextPath() %>/crm/data/DataManage/initData.json?codeId=1093', loadOwnerPaperType);" deftitle="--请选择--">
								<c:if test="${ownerList.OWNER_PAPER_TYPE==null}">--请选择--</c:if><c:if test="${ownerList.OWNER_PAPER_TYPE!=null}">${ownerList.PAPER_TYPE}</c:if></a>
								<ul id="ddsubmenu29" class="ddsubmenustyle"></ul>
							</li>
						</ul>
					</td>
					<td align="right" width="16%">证件号码：</td>
					<td><input id="owner_paper_no" name="owner_paper_no" type="text"
						class="middle_txt" datatype="1,is_textarea,30" size="20" value="${ownerList.OWNER_PAPER_NO }"
						maxlength="60" /></td>
				</tr>
				<tr>
					<td align="right" width="12%">&nbsp;省份：</td>
					<td align="left" width="24%" colspan="3"><select class="min_sel" id="dPro" 
						name="dPro" onchange="_regionCity(this,'dCity')"></select> 城市： <select
						class="min_sel" id="dCity" name="dCity"
						onchange="_regionCity(this,'dArea')"></select> 区县： <select
						class="min_sel" id="dArea" name="dArea"></select></td>
					<td align="right" width="10%"></td>
					<td>
					</td>
					<td align="right" width="10%"></td>
					<td>
					</td>
				</tr>
				<tr>
					<td align="right" colspan="1">详细地址：</td>
					<td align="left" colspan="10">
						<textarea rows="2" cols="100" id="owner_address" name="owner_address">${ownerList.OWNER_ADDRESS }</textarea>
					</td>
				</tr>
				<tr>
					<td align="right" width="13%">新产品预售：</td>  
					<td>
					<input type="hidden" id="new_product_sale" name="new_product_sale" value="${ownerList.PRODUCT_SALE2 }"/>
		      		<div id="ddtopmenubar28" class="mattblackmenu">
						<ul> 
							<li>
								<a  id="new_product_sale1" style="width:103px;" rel="ddsubmenu28" href="###" isclick="true" onclick="stree.loadtree(this, '<%= request.getContextPath() %>/crm/data/DataManage/initData.json?codeId=1004', loadProductSale);" deftitle="--请选择--">
								<c:if test="${ownerList.PRODUCT_SALE2==null}">--请选择--</c:if><c:if test="${ownerList.PRODUCT_SALE2!=null}">${ownerList.PRODUCT_SALE}</c:if></a>
								<ul id="ddsubmenu28" class="ddsubmenustyle"></ul>
							</li>
						</ul>
					</td>
					<td align="right" width="15%">意向等级：</td>
					<td width="25%">
					<input type="hidden" id="intent_type" name="intent_type" value="60101005"/>
		      		<div id="ddtopmenubar27" class="mattblackmenu">
						<ul> 
							<li>
								<a id="intentVehicleX" style="width:103px;" rel="ddsubmenu27" href="###" isclick="true" onclick="stree.loadtree(this, '<%= request.getContextPath() %>/crm/data/DataManage/initData.json?codeId=6010', loadIntentType);" deftitle="--请选择--">
								O</a>
								<ul id="ddsubmenu27" class="ddsubmenustyle"></ul>
							</li>
						</ul>
					</td>
					<td align="right" width="8%">销售流程进度：</td>
					<td>
					<input type="hidden" id="sales_progress" name="sales_progress" value="${ownerList.SALES_PROGRESS2}"/>
		      		<div id="ddtopmenubar26" class="mattblackmenu">
						<ul> 
							<li>
								<a style="width:103px;" rel="ddsubmenu26" href="###" isclick="true" onclick="stree.loadtree(this, '<%= request.getContextPath() %>/crm/data/DataManage/initData.json?codeId=6009', loadSalesProgress2);" deftitle="--请选择--">
								<c:if test="${ownerList.SALES_PROGRESS2==null}">--请选择--</c:if><c:if test="${ownerList.SALES_PROGRESS2!=null}">${ownerList.SALES_PROGRESS}</c:if></a>
								<ul id="ddsubmenu26" class="ddsubmenustyle"></ul>
							</li>
						</ul>
					</td>
					<td align="right" width="13%"></td>
					<td></td>
				</tr>
				<tr>
					
					<td align="right" width="15%">购置方式：</td>
					<td width="20%">
					<input type="hidden" id="deal_type" name="deal_type" value="${ownerList.DEAL_TYPE2}"/>
		      		<div id="ddtopmenubar25" class="mattblackmenu">
						<ul> 
							<li>
								<a style="width:103px;" rel="ddsubmenu25" href="###" isclick="true" onclick="stree.loadtree(this, '<%= request.getContextPath() %>/crm/data/DataManage/initData.json?codeId=6005', loadDealType);" deftitle="--请选择--">
								<c:if test="${ownerList.DEAL_TYPE2==null}">--请选择--</c:if><c:if test="${ownerList.DEAL_TYPE2!=null}">${ownerList.DEAL_TYPE}</c:if></a>
								<ul id="ddsubmenu25" class="ddsubmenustyle"></ul>
							</li>
						</ul>
					</td>
					<td align="right" width="15%">试乘试驾：</td>
					<td  width="25%">
						<input type="hidden" id="test_driving" name="test_driving" value="${ownerList.IF_DRIVE2}"/>
		      			<div id="ddtopmenubar24" class="mattblackmenu">
						<ul> 
							<li>
								<a style="width:103px;" rel="ddsubmenu24" href="###" isclick="true" onclick="stree.loadtree(this, '<%= request.getContextPath() %>/crm/data/DataManage/initData.json?codeId=1004', loadIfDriving2);" deftitle="--请选择--">
								<c:if test="${ownerList.IF_DRIVE2==null}">--请选择--</c:if><c:if test="${ownerList.IF_DRIVE2!=null}">${ownerList.IF_DRIVE}</c:if></a>
								<ul id="ddsubmenu24" class="ddsubmenustyle"></ul>
							</li>
						</ul>
					</td>
					<td align="right" width="14%"></td>
					<td>
					</td>
					<td></td>
					<td></td>
				</tr>
				</c:forEach>
			</table>
			</br>
			</br>
			<div id="table1Div">
				<b style="display: inline-block; float: left">未确定车架号</b><hr style="display: inline-block;">
			</div>
			</br>
			</br>
			<table border=0 align=center id="noConfirmVehicleTable" bgcolor="rgb(218, 224, 238)" style="width:95%; margin-left: -15px; margin-top: -15px;" >
				<tr style="height: 23px; font-weight: bold; color:#39669f" id="row0">
					<td width="10%">车型</td><td width="10%">颜色</td><td width="7%">价格(元)</td><td width="6%">数量</td><td width="7%">总金额(元)</td><td width="4%">订金(元)</td><td width="4%">定金(元)</td><td width="8%">余款付款日期</td><td width="8%">交车日期</td><td width="8%">已交车数量<input type="button" onclick="addRow()" value="新增" /></td>
				</tr>
				<c:forEach items="${table1List }" var="table1List" varStatus="x">
					<tr id="row${x.index+1 }" style="height: 23px;background-color: white;border: 0; border-color:#44BBBB; ">
						<td id="row${x.index+1 }col1">
						<input type="text" class="middle_txt" id="materialCoderow${x.index+1 }" name="materialCoderow${x.index+1 }"  value="${table1List.GROUP_NAME }" style="display: inline-block; float: left"   readonly="readonly"/>
						<input type="hidden" name="materialNamerow${x.index+1 }" size="20" id="materialNamerow${x.index+1 }" value="${table1List.INTENT_MODEL }" />
						<input  disabled="disabled" type="button" <c:if test="${x.index+1 <= size1 && table1List.DELIVERY_NUMBER>0&&table1List.IF_FORGEIGN==10041002}"> disabled </c:if>  value="..." class="mini_btn"  onclick="toIntentVechileList('materialCoderow${x.index+1 }','materialNamerow${x.index+1 }');" style="display: inline-block;"/>
						<input type="hidden" name="hiddendetailrow${x.index+1 }" size="20" id="hiddendetailrow${x.index+1 }" value="${table1List.DETAIL_ID }" />
						</td>
						<td id="row${x.index+1 }col2" >
						<select <c:if test="${x.index+1 <= size1 && table1List.DELIVERY_NUMBER>0}">  </c:if> id="intent_colorrow${x.index+1 }" name="intent_colorrow${x.index+1 }"><option value="${table1List.COLORID }" selected="selected">${table1List.COLOR }</option></select>
						</td>
						<!--  
						<script type="text/javascript">
						var INTENT_MODEL=${table1List.INTENT_MODEL };
						var colorId=${table1List.COLORID };
						var index=${x.index+1 };
						var url = "<%=contextPath%>/crm/taskmanage/TaskManage/docolor.json?pose_id="+INTENT_MODEL;
						makeSameCall(url, showInfo, "fm") ;
						function showInfo(json) {
							if(json.colorList[0]!=null) {
								 for(var i=0;i<json.colorList.length;i++)
								 {
									 document.getElementById("intent_colorrow"+index).options.add(new Option(json.colorList[i].CODE_DESC,json.colorList[i].CODE_ID));
									 if(json.colorList[i].CODE_ID==colorId){
										 document.getElementById("intent_colorrow"+index).options[i].selected=true;
										 }
								 }
								
							}
							}
						</script>
						-->
						<td id="row${x.index+1 }col3">
						<input readonly='readonly' style='background-color: #EEEEEE;' type="text" id="pricerow${x.index+1 }" name="pricerow${x.index+1 }" value="${table1List.PRICE }" checkFlag="jiage" style="width:80px" onblur="checkNum(this)"/>
						</td>
						<td id="row${x.index }col4">
						<input readonly='readonly' style='background-color: #EEEEEE;' type="text" id="numrow${x.index+1 }" name="numrow${x.index+1 }" value="${table1List.NUM }" checkFlag="shuliang" style="width:70px" onblur="checkNum(this)"/>
						</td>
						<td id="row${x.index+1 }col5">
						<input readonly='readonly' style='background-color: #EEEEEE;' type="text" id="amountrow${x.index+1 }" name="amountrow${x.index+1 }" value="${table1List.AMOUNT }" style="width:70px" readonly="readonly" style="background-color: #EEEEEE;"/>
						</td>
						<td id="row${x.index+1 }col6">
						<input  readonly='readonly' style='background-color: #EEEEEE;' type="text" id="depositrow${x.index+1 }" name="depositrow${x.index+1 }" value="${table1List.DEPOSIT }" checkFlag="dingjin" style="width:50px" onblur="checkNum(this)"/>
						</td>
						<td id="row${x.index+1 }col10">
						<input  readonly='readonly' style='background-color: #EEEEEE;' type="text" id="earnestrow${x.index+1 }" name="earnestrow${x.index+1 }" value="${table1List.EARNEST }" checkFlag="dingjin2" style="width:50px" onblur="checkNum(this)"/>
						</td>
						<td id="row${x.index+1 }col7">
						<input readonly='readonly' style='background-color: #EEEEEE;' type="text" value="${table1List.BALANCE_DATE }" name="pre_pay_daterow${x.index+1 }" id="pre_pay_daterow${x.index+1 }" group="pre_pay_daterow${x.index+1 }"
								class="short_txt" datatype="1,is_date,10" size="20" hasbtn="true" readonly="readonly" 
								maxlength="60" />
						</td>
						<td id="row${x.index+1 }col8">
						<input readonly='readonly' style='background-color: #EEEEEE;' type="text" value="${table1List.DELIVERY_DATE }" name="pre_delivery_daterow${x.index+1 }" id="pre_delivery_daterow${x.index+1 }" group="pre_delivery_daterow${x.index+1 }"
								class="short_txt" datatype="1,is_date,10" size="20" hasbtn="true" readonly="readonly"
								maxlength="60"  />
						</td>
						<td id="row${x.index+1 }col9" style="text-align: left">
						<c:if test="${x.index+1 <= size1 && table1List.DELIVERY_NUMBER>0&&table1List.IS_FOREIGN==10041002 }">
						<input  readonly='readonly' style='background-color: #EEEEEE;' type="text" id="delivery_numberrow${x.index+1 }" name="delivery_numberrow${x.index+1 }" value="${table1List.DELIVERY_NUMBER }" style="width:56px;background-color: #EEEEEE;" readonly="readonly"/><c:if test="${ table1List.NUM>table1List.DELIVERY_NUMBER}"><input type="button" onclick="updateRowBy('row${x.index+1 }')" value="修改" style="margin-left: 5px" /></c:if>
						</c:if>
						<c:if test="${x.index+1 > size1 || table1List.DELIVERY_NUMBER==0||table1List.IS_FOREIGN==10041001}">
						<input readonly='readonly' style='background-color: #EEEEEE;' type="text" id="delivery_numberrow${x.index+1 }" name="delivery_numberrow${x.index+1 }" value="${table1List.DELIVERY_NUMBER }" style="width:56px;background-color: #EEEEEE;" readonly="readonly"/><input type="button" onclick="deleteRowBy('row${x.index+1 }')" value="删除" style="margin-left: 5px" />
						</c:if>
						</td>
					</tr>
					</c:forEach>
				<tbody id="newbody"></tbody> 
			</table>
			
			</br>
			</br>
			</br>
			<div id="table2Div">
				<b style="display: inline-block; float: left">已确定车架号</b><hr style="display: inline-block;">
			</div>
			</br>
			</br>
			<table border=0 align=center id="ConfirmVehicleTable" bgcolor="rgb(218, 224, 238)" style="width:95%; margin-left: -15px; margin-top: -15px;" >
				<tr style="height: 23px; font-weight: bold; color:#39669f" id="yrow0">
					<td width="10%">VIN</td><td width="10%">车型</td><td width="10%">颜色</td><td width="7%">价格(元)</td><td width="6%">数量</td><td width="7%">总金额(元)</td><td width="4%">订金(元)</td><td width="4%">定金(元)</td><td width="8%">余款付款日期</td><td width="8%">交车日期</td><td width="9%">已交车数量<input type="button" onclick="yaddRow()" value="新增" /></td>
				</tr>
				<c:forEach items="${table2List }" var="table2List" varStatus="x">
					<tr id="yrow${x.index+1 }" style="height: 23px;background-color: white;border: 0; border-color:#44BBBB; ">
						<td id="yrow${x.index+1 }col0">
						<input type="text" class="middle_txt" id="yvinIdyrow${x.index+1 }" name="yvinIdyrow${x.index+1 }"  value="${table2List.VIN }" style="display: inline-block; float: left"   readonly="readonly"/>
						<input type="hidden" name="hiddenyvinIdyrow${x.index+1 }" size="20" id="hiddenyvinIdyrow${x.index+1 }" value="${table2List.VEHICLE_ID }" />
						<input disabled="disabled"  type="button"  <c:if test="${ table2List.DELIVERY_NUMBER>0}"> disabled </c:if>  value="..." class="mini_btn"  onclick="delv('','','','yvinIdyrow${x.index+1 }','ymaterialCodeyrow${x.index+1 }','yrow${x.index+1 }col2','hiddenymaterialCodeyrow${x.index+1 }')" style="display: inline-block;"/>
						<input type="hidden" name="yhiddendetailyrow${x.index+1 }" size="20" id="yhiddendetailyrow${x.index+1 }" value="${table2List.DETAIL_ID }" />
						</td>
						<td id="yrow${x.index+1 }col1">
						<input readonly='readonly' style='background-color: #EEEEEE;' type="text" class="middle_txt" id="ymaterialCodeyrow${x.index+1 }" name="ymaterialCodeyrow${x.index+1 }"  value="${table2List.MATERIAL_NAME }" style="display: inline-block; float: left; background-color: #EEEEEE;"   readonly="readonly"/><input type='hidden' id='hiddenymaterialCodeyrow${x.index+1 }' name='hiddenymaterialCodeyrow${x.index+1 }' value='${table2List.MATERIAL }' />
						</td>
						<td id="yrow${x.index+1 }col2">
						<select  id='ytheColoryvinIdyrow${x.index+1 }' name='ytheColoryvinIdyrow${x.index+1 }'><option id="${table2List.COLOR_CODE }" value="${table2List.COLOR_CODE }">${table2List.COLOR }</option></select>
						</td>
						<td id="yrow${x.index+1 }col3">
						<input readonly='readonly' style='background-color: #EEEEEE;' type="text" id="ypriceyrow${x.index+1 }" name="ypriceyrow${x.index+1 }" value="${table2List.PRICE }" checkFlag="yjiage" style="width:70px" onblur="ycheckNum(this)"/>
						</td>
						<td id="yrow${x.index+1 }col4">
						<input readonly='readonly' style='background-color: #EEEEEE;' type="text" id="ynumyrow${x.index+1 }" name="ynumyrow${x.index+1 }" value="${table2List.NUM }" checkFlag="yshuliang" style="width:55px" readonly="readonly" style="background-color: #EEEEEE;" value="1"/>
						</td>
						<td id="yrow${x.index+1 }col5">
						<input readonly='readonly' style='background-color: #EEEEEE;' type="text" id="yamountyrow${x.index+1 }" name="yamountyrow${x.index+1 }" value="${table2List.AMOUNT }" style="width:60px" readonly="readonly" style="background-color: #EEEEEE;"/>
						</td>
						<td id="yrow${x.index+1 }col6">
						<input readonly='readonly' style='background-color: #EEEEEE;' type="text" id="ydeposityrow${x.index+1 }" name="ydeposityrow${x.index+1 }" value="${table2List.DEPOSIT }" checkFlag="ydingjin" style="width:50px" onblur="ycheckNum(this)"/>
						</td>
						<td id="yrow${x.index+1 }col10">
						<input readonly='readonly' style='background-color: #EEEEEE;' type="text" id="yearnestyrow${x.index+1 }" name="yearnestyrow${x.index+1 }" value="${table2List.EARNEST }" checkFlag="ydingjin2" style="width:50px" onblur="ycheckNum(this)"/>
						</td>
						<td id="yrow${x.index+1 }col7">
						<input readonly='readonly' style='background-color: #EEEEEE;' type="text" value="${table2List.BALANCE_DATE }" name="ypre_pay_dateyrow${x.index+1 }" id="ypre_pay_dateyrow${x.index+1 }" group="ypre_pay_dateyrow${x.index+1 }"
								class="short_txt" datatype="1,is_date,10" size="20" hasbtn="true" readonly="readonly"
								maxlength="60"  />
						</td>
						<td id="yrow${x.index+1 }col8">
						<input readonly='readonly' style='background-color: #EEEEEE;' type="text" value="${table2List.DELIVERY_DATE }" name="ypre_delivery_dateyrow${x.index+1 }" id="ypre_delivery_dateyrow${x.index+1 }" group="ypre_delivery_dateyrow${x.index+1 }"
								class="short_txt" datatype="1,is_date,10" size="20" hasbtn="true" readonly="readonly"
								maxlength="60"  />
						</td>
						<td id="yrow${x.index+1 }col9" style="text-align: left">
						<c:if test="${x.index+1 <= size2 && table2List.DELIVERY_NUMBER>0}">
						<input readonly='readonly' style='background-color: #EEEEEE;' type="text" id="ydelivery_numberyrow${x.index+1 }" name="ydelivery_numberyrow${x.index+1 }" value="${table2List.DELIVERY_NUMBER }" style="width:56px" readonly="readonly" style="background-color: #EEEEEE;"/><input type="hidden" onclick="ydeleteRowBy('yrow${x.index+1 }')" value="删除" style="margin-left: 5px" />
						</c:if>
						<c:if test="${x.index+1 > size2 || table2List.DELIVERY_NUMBER==0}">
						<input readonly='readonly' style='background-color: #EEEEEE;' type="text" id="ydelivery_numberyrow${x.index+1 }" name="ydelivery_numberyrow${x.index+1 }" value="${table2List.DELIVERY_NUMBER }" style="width:56px" readonly="readonly" style="background-color: #EEEEEE;"/><input type="button" onclick="ydeleteRowBy('yrow${x.index+1 }')" value="删除" style="margin-left: 5px" />
						</c:if>
						</td>
					</tr>
				</c:forEach>
				<tbody id="ynewbody"></tbody> 
			</table>
			</br>
			</br>
		<c:if test="${orderStatus != '60231011'}">
			<div>
				<b style="display: inline-block; float: left">修改/退单申请</b><hr style="display: inline-block;">
			</div>
			</br>
			</br>
			<table class="table_query" width="95%" align="center">
				<tr>
					<td align="left" width="11%">
						<input type="radio" checked="checked" id="update_radio" name="group_radio" onclick="updateClick()">修改</input>
						<input type="radio"  id="return_radio" name="group_radio" onclick="returnClick()">退单</input>
					</td>
				</tr>
				<tr>
					<td align="left" colspan="1">修改/退单说明：</td>
					<td align="left" colspan="3">
						<textarea rows="5" cols="72" id="reason_remark" name="reason_remark"></textarea>
					</td>
				</tr>
			</table>
		</c:if>
			<table class="table_query" width="95%" align="center">
				<tr>
					<td colspan="3" align="center"><input name="queryBtn"
						type="button" class="normal_btn" id="baocun" onclick="doSave();"
						value="保存" />
						<input name="insertBtn"
						type="button" class="normal_btn" id="quxiao" onclick="javascript:history.go(-1);"
						value="取消" />
					</td>
				</tr>
			</table>
			<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
			<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
		</form>

	</div>
	

<script type="text/javascript">	
	function yaddRow() {
		var nowDate=$("nowDate").value;
		var table = document.getElementById("ConfirmVehicleTable");
		var length=document.getElementById("ConfirmVehicleTable").rows.length;
		var s = table.rows[table.rows.length-1].id.charAt(table.rows[table.rows.length-1].id.length - 1);
		var rownum = parseInt(s)+1;
		var tr=document.createElement("tr");
		tr.id="yrow"+rownum;
		//tr.id=length+1;
		//tr.id="yrow"+tr.id;
		tr.style.height="23px";
		tr.style.backgroundColor='white';
		tr.style.border=0;
		tr.style.borderColor="#44BBBB";
		//第0列（VIN）
		var td=document.createElement("td");
		colorTdId = tr.id+"col"+2;
		td.innerHTML = "<input type='text' class='middle_txt' id='yvinId"+tr.id+"' name='yvinId"+tr.id+"'  value='' style='display: inline-block; float: left'   readonly='readonly'/><input type='hidden' name='hiddenyvinId"+tr.id+"' size='20' id='hiddenyvinId"+tr.id+"' value='' /><input type='button' value='...' class='mini_btn'  onclick=delv('','','','yvinId"+tr.id+"','ymaterialCode"+tr.id+"','"+colorTdId+"','hiddenymaterialCode"+tr.id+"') style='display: inline-block;'/>";
		td.id=tr.id+"col"+1;
		tr.appendChild(td);
		//第一列（车系）
		var td=document.createElement("td");
		td.innerHTML = "<input type='text' class='middle_txt' id='ymaterialCode"+tr.id+"' name='ymaterialCode"+tr.id+"'  value='' style='display: inline-block; float: left; background-color: #EEEEEE;'   readonly='readonly'/><input type='hidden' id='hiddenymaterialCode"+tr.id+"' name='hiddenymaterialCode"+tr.id+"' value='' />";
		td.id=tr.id+"col"+1;
		tr.appendChild(td);
		//第二列（颜色）
		var td=document.createElement("td");
		td.innerHTML = "";
		td.id=tr.id+"col"+2;
		tr.appendChild(td);
		//第三列（价格）
		var td=document.createElement("td");
		td.innerHTML = "<input type='text' style='width:70px' id='yprice"+tr.id+"' name='yprice"+tr.id+"' checkFlag='yjiage' onblur='ycheckNum(this)'/>";
		td.id=tr.id+"col"+3;
		tr.appendChild(td);
		//第四列（数量）
		var td=document.createElement("td");
		td.innerHTML = "<input type='text' style='width:55px' id='ynum"+tr.id+"' name='ynum"+tr.id+"' checkFlag='yshuliang' readonly='readonly' style='background-color: #EEEEEE;' value='1'/>";
		td.id=tr.id+"col"+4;
		tr.appendChild(td);
		//第五列（总金额）
		var td=document.createElement("td");
		td.innerHTML = "<input type='text' id='yamount"+tr.id+"' style='width:60px' name='yamount"+tr.id+"' readonly='readonly' style='background-color: #EEEEEE;'/>";
		td.id=tr.id+"col"+5;
		tr.appendChild(td);
		//第六列（订金）
		var td=document.createElement("td");
		td.innerHTML = "<input type='text' style='width:50px' id='ydeposit"+tr.id+"' name='ydeposit"+tr.id+"' checkFlag='ydingjin' onblur='ycheckNum(this)'/>";
		td.id=tr.id+"col"+6;
		tr.appendChild(td);
		//第六列（定金）
		var td=document.createElement("td");
		td.innerHTML = "<input type='text' style='width:50px' id='yearnest"+tr.id+"' name='yearnest"+tr.id+"' checkFlag='ydingjin2' onblur='ycheckNum(this)'/>";
		td.id=tr.id+"col"+6;
		tr.appendChild(td);
		//第七列（余款付款日期）
		var td=document.createElement("td");
		td.innerHTML = "<input type=\"text\" value=\"\" name=\"ypre_pay_date"+tr.id+"\" id=\"ypre_pay_date"+tr.id+"\" group=\"ypre_pay_date"+tr.id+"\" class=\"short_txt\" datatype=\"1,is_date,10\" size=\"20\" hasbtn='true' readonly=\"readonly\" maxlength=\"60\" callFunction=\"showcalendar(event, 'ypre_pay_date"+tr.id+"', false);\" /><span><IMG id=srtImg title=点击按钮 src=\"/dms/images/default/btn.gif\" width=15 height=15 onclick=\"showcalendar(event, 'ypre_pay_date"+tr.id+"', false);\"></span>";
		td.id=tr.id+"col"+7;
		tr.appendChild(td);
		//第八列（交车日期）
		var td=document.createElement("td");
		td.innerHTML = "<input type=\"text\" value=\""+nowDate+"\" name=\"ypre_delivery_date"+tr.id+"\" id=\"ypre_delivery_date"+tr.id+"\" group=\"ypre_delivery_date"+tr.id+"\" class=\"short_txt\" datatype=\"1,is_date,10\" size=\"20\" hasbtn='true' readonly=\"readonly\" maxlength=\"60\" callFunction=\"showcalendar(event, 'ypre_delivery_date"+tr.id+"', false);\" /><span><IMG id=srtImg title=点击按钮 src=\"/dms/images/default/btn.gif\" width=15 height=15 onclick=\"showcalendar(event, 'ypre_delivery_date"+tr.id+"', false);\"></span>";
		td.id=tr.id+"col"+8;
		tr.appendChild(td);
		//第九列（已交车数量）
		var td=document.createElement("td");
		td.innerHTML = "<input type='text' id='ydelivery_number"+tr.id+"' name='ydelivery_number"+tr.id+"' value='0' style='width:56px' readonly='readonly' style='background-color: #EEEEEE;'/><input type='button' onclick=ydeleteRowBy('"+tr.id+"') value='删除' style='margin-left: 5px' />";
		td.id=tr.id+"col"+9;
		tr.appendChild(td);
		
		document.getElementById("ynewbody").appendChild (tr);
	}
	function ydeleteRowBy(rowNum) {
		//取行数
		var rownum = rowNum.charAt(rowNum.length - 1);
		var willDeleteRow = document.getElementById("ConfirmVehicleTable");
		var tr = document.getElementById(rowNum);
		if(rownum<1) {
			return false;
		} else {
			willDeleteRow.deleteRow(tr.rowIndex);
		}
	}
	function ydoFindColor(inputId,nextTdId) {
		MyAlert("ytheColor"+nextTdId);
		var url = "<%=contextPath%>/crm/taskmanage/TaskManage/colorInfoBySeries.json?inputId="+inputId;
		makeFormCall(url, showInfo, "fm") ;
		function showInfo(json) {
			if(json.ps[0]!=null) {
				var colortd=document.getElementById(nextTdId);
				colortd.innerHTML = "";
				var str = "<select ";
				for(var i=0;i<json.psSize;i++) {
					str = str + "id='ytheColor"+nextTdId+"' name='ytheColor"+nextTdId+"'>";
					str = str + "<option value="+json.ps[i].COLOR_CODE+">"+json.ps[i].COLOR_NAME+"</option>";
				}
				str = str + "</select>";
				colortd.innerHTML = str;
			}
		}
	}
	function getZ(n){    return /^\d+(\.\d+)?$/.test(n+"");}
	function ycheckNum(obj) {
		var reg = new RegExp("^[0-9]*$");
		//取行数
		var rownum = obj.id.charAt(obj.id.length - 1);
		//价格
		if(obj.getAttribute("checkFlag")=="yjiage") {
			var shuliang = "ynumyrow"+rownum;
			var zongjine = "yamountyrow"+rownum;
			if(!getZ(obj.value)){
		         MyAlert("价格格式不正确!");
		         return false;
		    } else if(document.getElementById(shuliang).value!=null && document.getElementById(shuliang).value!="") {
		    	if(!reg.test(document.getElementById(shuliang).value)) {
		    		MyAlert("数量格式不正确!");
			         return false;
		    	} else {
		    		document.getElementById(zongjine).value = document.getElementById(shuliang).value * obj.value;
		    	}
		    }
		} else if(obj.getAttribute("checkFlag")=="yshuliang"){
			var jiage = "ypriceyrow"+rownum;
			var zongjine = "yamountyrow"+rownum;
			if(!reg.test(obj.value)){
		         MyAlert("数量格式不正确!");
		         return false;
		    } else if(document.getElementById(jiage).value!=null && document.getElementById(jiage).value!="") {
		    	if(!getZ(document.getElementById(jiage).value)) {
		    		MyAlert("价格格式不正确!");
			         return false;
		    	} else {
		    		document.getElementById(zongjine).value = document.getElementById(jiage).value * obj.value;
		    	}
		    }
		} else if(obj.getAttribute("checkFlag")=="ydingjin"){
			if(!getZ(obj.value)) {
	    		MyAlert("订金格式不正确!");
		         return false;
	    	}
		} else if(obj.getAttribute("checkFlag")=="ydingjin2"){
			if(!getZ(obj.value)) {
	    		MyAlert("定金格式不正确!");
		         return false;
	    	}
		}
	}
	
	//VIN选择页面
	function delv(vechile_id,qkId,qkOrderDetailId,inputId,moderId,colorTdId,hiddenmoderId){
		OpenHtmlWindow("/dms/crm/delivery/DelvManage/toVinList.do?inputId="+inputId+"&moderId="+moderId+"&colorTdId="+colorTdId+"&hiddenmoderId="+hiddenmoderId,800,600);
	}
	
	function doFindModelColor(inputId,moderId,colorTdId,hiddenmoderId) {
		var url = "<%=contextPath%>/crm/taskmanage/TaskManage/seriesColorInfoByVIN.json?inputId="+inputId;
		makeFormCall(url, showInfo, "fm") ;
		function showInfo(json) {
			if(json.ps[0]!=null) {
				document.getElementById(moderId).value = json.ps[0].MATERIAL_NAME;
				document.getElementById(hiddenmoderId).value = json.ps[0].MATERIAL_ID;
				var colortd=document.getElementById(colorTdId);
				colortd.innerHTML = "";
				var str = "<select id='ytheColor"+inputId+"' name='ytheColor"+inputId+"'><option id="+json.ps[0].COLOR_CODE+" value="+json.ps[0].COLOR_CODE+">"+json.ps[0].COLOR+"</option></select>";
				colortd.innerHTML = str;
			}
		}
	}
	function doCheckTable1() {
		//check未确定车架号Table里的数据
		var table = document.getElementById("noConfirmVehicleTable");
		var s = table.rows[table.rows.length-1].id.charAt(table.rows[table.rows.length-1].id.length - 1);
		var materialCoderow=1;
		var pricerow=1;
		var numrow=1;
		var depositrow=1;
		var pre_pay_daterow=1;
		var pre_delivery_daterow=1;
		var delivery_numberrow=1;
		var reg = new RegExp("^[0-9]*$");
		for(var i1=1;i1<=parseInt(s);i1++) {
			if(document.getElementById("materialCoderow"+i1)!=null) {
				var model = document.getElementById("materialCoderow"+i1).value;//车型
				if(model ==null||model=="") {
					 MyAlert("未确定车架号中第"+materialCoderow+"行,车型为空！");
			         return false;
				}
				materialCoderow++;
			}
			if(document.getElementById("pricerow"+i1)!=null) {
				var model = document.getElementById("pricerow"+i1).value;//价格
				if(model ==null||model=="") {
					 MyAlert("未确定车架号中第"+pricerow+"行,价格为空！");
			         return false;
				} else if(!getZ(model)){
					MyAlert("未确定车架号中第"+pricerow+"行,价格格式不正确！");
			         return false;
				}
				pricerow++;
			}
			if(document.getElementById("delivery_numberrow"+i1)!=null) {
				var model = document.getElementById("delivery_numberrow"+i1).value;//已交车数量
				if(!reg.test(model)) {
					 MyAlert("未确定车架号中第"+delivery_numberrow+"行,已交车数量格式不正确！");
			         return false;
				} else if(model ==null||model==""){
					MyAlert("未确定车架号中第"+delivery_numberrow+"行,已交车数量格式不正确！");
			         return false;
				}
				delivery_numberrow++;
			}
			if(document.getElementById("numrow"+i1)!=null) {
				var model = document.getElementById("numrow"+i1).value;//数量
				var delivery_num = document.getElementById("delivery_numberrow"+i1).value;//已交车数量
				if(parseInt(model)<parseInt(delivery_num)) {
					MyAlert("未确定车架号中第"+numrow+"行,数量不能小于已交车数量！");
					return false;
				}
				if(model ==null||model=="") {
					 MyAlert("未确定车架号中第"+numrow+"行,数量为空！");
			         return false;
				} else if(!reg.test(model)){
					MyAlert("未确定车架号中第"+numrow+"行,数量格式不正确！");
			         return false;
				}
				numrow++;
			}
			if(document.getElementById("depositrow"+i1)!=null) {
				var model = document.getElementById("depositrow"+i1).value;//订金
				var model2 = document.getElementById("earnestrow"+i1).value;//定金
				if((model ==null||model=="") &&(model2 ==null||model2=="")) {
					MyAlert("未确定车架号中第"+depositrow+"行,订金或定金至少输入一项！");
			         return false;
				} else if((model !=null&&model!="") &&(model2 !=null&&model2!="")){
					 MyAlert("未确定车架号中第"+depositrow+"行,订金或定金只能输入一项！");
			         return false;
				} else if((model !=null&&model!="")&&!getZ(model)){
					MyAlert("未确定车架号中第"+depositrow+"行,订金格式不正确！");
			         return false;
				} else if((model2 !=null&&model2!="")&&!getZ(model2)){
					MyAlert("未确定车架号中第"+depositrow+"行,定金格式不正确！");
			         return false;
				}
				depositrow++;
			}
			if(document.getElementById("pre_pay_daterow"+i1)!=null) {
				var model = document.getElementById("pre_pay_daterow"+i1).value;//余款付款日期
				if(model ==null||model=="") {
					 MyAlert("未确定车架号中第"+pre_pay_daterow+"行,余款付款日期为空！");
			         return false;
				}
				pre_pay_daterow++;
			}
			if(document.getElementById("pre_delivery_daterow"+i1)!=null) {
				var model = document.getElementById("pre_delivery_daterow"+i1).value;//交车日期
				if(model ==null||model=="") {
					 MyAlert("未确定车架号中第"+pre_delivery_daterow+"行,交车日期为空！");
			         return false;
				}
				pre_delivery_daterow++;
			}
		}
		return true;
	}
	
	function doCheckTable2() {
		//check确定车架号Table里的数据
		var table = document.getElementById("ConfirmVehicleTable");
		var s = table.rows[table.rows.length-1].id.charAt(table.rows[table.rows.length-1].id.length - 1);
		var yvinIdyrow=1;
		var ypriceyrow=1;
		var ynumyrow=1;
		var ydeposityrow=1;
		var ypre_pay_dateyrow=1;
		var ypre_delivery_dateyrow=1;
		var reg = new RegExp("^[0-9]*$");
		var array=new Array();
		for(var i1=1;i1<=parseInt(s);i1++) {
			if(document.getElementById("yvinIdyrow"+i1)!=null) {
				var vinId = document.getElementById("yvinIdyrow"+i1).value;//VIN
				//vin重复判断
				for(var u=0;u<array.length;u++) {
					if(array[u] == vinId) {
						MyAlert("已确定车架号中不能有重复VIN号！");
						return false;
					}
				}
				array.push(vinId);
				}
			if(document.getElementById("yvinIdyrow"+i1)!=null) {
				var model = document.getElementById("yvinIdyrow"+i1).value;//VIN
				if(model ==null||model=="") {
					 MyAlert("已确定车架号中第"+yvinIdyrow+"行,VIN为空！");
			         return false;
				}
				yvinIdyrow++;
			}
			if(document.getElementById("ypriceyrow"+i1)!=null) {
				var model = document.getElementById("ypriceyrow"+i1).value;//价格
				if(model ==null||model=="") {
					 MyAlert("已确定车架号中第"+ypriceyrow+"行,价格为空！");
			         return false;
				} else if(!getZ(model)){
					MyAlert("已确定车架号中第"+ypriceyrow+"行,价格格式不正确！");
			         return false;
				}
				ypriceyrow++;
			}
			if(document.getElementById("ynumyrow"+i1)!=null) {
				var model = document.getElementById("ynumyrow"+i1).value;//数量
				if(model ==null||model=="") {
					 MyAlert("已确定车架号中第"+ynumyrow+"行,数量为空！");
			         return false;
				} else if(!reg.test(model)){
					MyAlert("已确定车架号中第"+ynumyrow+"行,数量格式不正确！");
			         return false;
				}
				ynumyrow++;
			}
			if(document.getElementById("ydeposityrow"+i1)!=null) {
				var model = document.getElementById("ydeposityrow"+i1).value;//订金
				var model2 = document.getElementById("yearnestyrow"+i1).value;//定金
				if((model ==null||model=="") &&(model2 ==null||model2=="")) {
					 MyAlert("已确定车架号中第"+ydeposityrow+"行,订金或定金至少输入一项！");
			         return false;
				} else if((model !=null&&model!="") &&(model2 !=null&&model2!="")){
					 MyAlert("未确定车架号中第"+ydeposityrow+"行,订金或定金只能输入一项！");
			         return false;
				} else if((model !=null&&model!="")&&!getZ(model)){
					MyAlert("未确定车架号中第"+ydeposityrow+"行,订金格式不正确！");
			         return false;
				} else if((model2 !=null&&model2!="")&&!getZ(model2)){
					MyAlert("未确定车架号中第"+ydeposityrow+"行,定金格式不正确！");
			         return false;
				}
				ydeposityrow++;
			}
			if(document.getElementById("ypre_pay_dateyrow"+i1)!=null) {
				var model = document.getElementById("ypre_pay_dateyrow"+i1).value;//余款付款日期
				if(model ==null||model=="") {
					 MyAlert("已确定车架号中第"+ypre_pay_dateyrow+"行,余款付款日期为空！");
			         return false;
				}
				ypre_pay_dateyrow++;
			}
			if(document.getElementById("ypre_delivery_dateyrow"+i1)!=null) {
				var model = document.getElementById("ypre_delivery_dateyrow"+i1).value;//交车日期
				if(model ==null||model=="") {
					 MyAlert("已确定车架号中第"+ypre_delivery_dateyrow+"行,交车日期为空！");
			         return false;
				}
				ypre_delivery_dateyrow++;
			}
		}
		return true;
	}
	function updateClick(){
		var intentVehicle = document.getElementById("intentVehicleX");
		var intent_type = document.getElementById("intent_type");
		var atable1 = document.getElementById("table1Div");
		var btable1 = document.getElementById("noConfirmVehicleTable");
		var atable2 = document.getElementById("table2Div");
		var btable2 = document.getElementById("ConfirmVehicleTable");
		intentVehicle.innerHTML = "O";
		intent_type.value = "60101005";
		atable1.style.display = "block";
		btable1.style.display = "block";
		atable2.style.display = "block";
		btable2.style.display = "block";
	}
	function returnClick(){
		var orderStatus = document.getElementById("orderStatus").value;
		if(orderStatus!=<%=Constant.TPC_ORDER_STATUS_01%>) {//订单状态不为有效的 不能做退单
			 document.getElementById("update_radio").checked=true;
			 document.getElementById("return_radio").checked=false;
			 MyAlert("该订单为已交过车,不能做退单处理!");
			 return false;
		} else {
			var intentVehicle = document.getElementById("intentVehicleX");
			var atable1 = document.getElementById("table1Div");
			var btable1 = document.getElementById("noConfirmVehicleTable");
			var atable2 = document.getElementById("table2Div");
			var btable2 = document.getElementById("ConfirmVehicleTable");
			var intent_type = document.getElementById("intent_type");
			intentVehicle.innerHTML = "E";
			intent_type.value = "60101006";
			atable1.style.display = "none";
			btable1.style.display = "none";
			atable2.style.display = "none";
			btable2.style.display = "none";
		}
	}
	
	//意向车型
	function toIntentVechileList(textId,textName){
		OpenHtmlWindow("<%=request.getContextPath()%>/crm/customer/CustomerManage/toIntentVechileList.do?textId="+textId+"&textName="+textName,800,600);
	}
	//显示意向车型
	function showInfo(pose_id,pose_name,textId,textName){		
		document.getElementById(textId).value = pose_name;
		document.getElementById(textName).value = pose_id;

		var colorId=textId.replace("materialCode","intent_color");
		document.getElementById(colorId).options.length = 0;
		var url = "<%=contextPath%>/crm/taskmanage/TaskManage/docolor.json?pose_id="+pose_id;
		makeFormCall(url, showInfo, "fm") ;
		function showInfo(json) {
			if(json.colorList[0]!=null) {
				 for(var i=0;i<json.colorList.length;i++)
				 {
					 document.getElementById(colorId).options.add(new Option(json.colorList[i].CODE_DESC,json.colorList[i].CODE_ID));
				 }
				
			}
			if(json.ifpresell=="10041002"){
				document.getElementById("new_product_sale1").innerHTML="否";
				document.getElementById("new_product_sale").value="10041002";
			}else if(json.ifpresell=="10041001"){
				document.getElementById("new_product_sale1").innerHTML="是";
				document.getElementById("new_product_sale").value="10041001";
				}
		}
		
	}
</script>
<script type="text/javascript">ddlevelsmenu.setup("ddtopmenubar29", "topbar")</script>
<script type="text/javascript">ddlevelsmenu.setup("ddtopmenubar28", "topbar")</script>
<script type="text/javascript">ddlevelsmenu.setup("ddtopmenubar27", "topbar")</script>
<script type="text/javascript">ddlevelsmenu.setup("ddtopmenubar26", "topbar")</script>
<script type="text/javascript">ddlevelsmenu.setup("ddtopmenubar25", "topbar")</script>
<script type="text/javascript">ddlevelsmenu.setup("ddtopmenubar24", "topbar")</script>
</body>
</html>