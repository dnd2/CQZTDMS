<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
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
<script type="text/javascript" src="<%=request.getContextPath()%>/js/regionUtils.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/crm/customer/common.js"></script>
<script type="text/javascript">
	function doInit(){
		var msg = document.getElementById("errorMsg").value;
	   if(msg!=null&&msg!="") {
			MyAlert(msg);
			msg="";
		}
		loadcalendar();   //初始化时间控件
		$("pre_delivery_daterow1").value='${nowDate}';
		$("ypre_delivery_dateyrow1").value='${nowDate}';
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
	//添加验证身份证号码 
	function isIdentityNumber() {
		var card_type=document.getElementById("owner_paper_type").value;
		if(card_type==""||card_type==null) {
			MyAlert("请选择证件类型!");
			return false;
		}
		if(card_type!=10931001){
			return true;
		}
		var identityNumber = document.getElementById("owner_paper_no").value;
		if(identityNumber==""||identityNumber==null) {
			MyAlert("请输入证件号码!");
			return false;
		}
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
	function doSave(){
		var baocun = document.getElementById("baocun");
		var quxiao = document.getElementById("quxiao");
		var ownerName = document.getElementById("owner_name").value;//车主姓名
		var ownerPhone = document.getElementById("owner_phone").value;//车主电话
		var intentType = document.getElementById("intent_type").value;//意向等级
		var salesProgress = document.getElementById("sales_progress").value;//销售流程进度
		var dealType = document.getElementById("deal_type").value;//购置方式
		var testDriving = document.getElementById("test_driving").value;//试乘试驾
		var newproduct=document.getElementById("new_product_sale").value;
		var oldCustomerName = document.getElementById("old_customer_name").value;
		var oldTelephone = document.getElementById("old_telephone").value;
		
		var reg = new RegExp("^[0-9]*$");
		var dpro=document.getElementById("dPro").value;
		var dcity=document.getElementById("dCity").value;

		var obj = document.getElementById("laoSucessNO"); //定位id
		var index = obj.selectedIndex; // 选中索引
		var laoSucessNO = obj.options[index].value; // 选中值	

		var obj1 = document.getElementById("JsSucessNO"); //定位id
		var index1 = obj1.selectedIndex; // 选中索引
		var JsSucessNO = obj1.options[index1].value; // 选中值		

		var flag=judgeIfAbleOderDate(ownerPhone);
		if(!flag){
			MyAlert("DCRC未录入该客户今日到店客流信息，不能保存该订单！！！");
			return;
		}
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
	
		var ifpresell=doCheckPresell();
		if(ifpresell==true && newproduct=="10041002")
		{
			MyAlert("新产品预售请选择<是>！");
			return;
		}
		var color=doCheckColor();
		if(color==false)
		{
			MyAlert("请选择颜色!");
			return;
		}

		if(dpro==null||""==dpro){
			MyAlert("请选择省份！！！");
			return;
		}
		if(dcity==null||""==dcity){
			MyAlert("请选择市！！！");
			return;
		}
		if(!isIdentityNumber()){
			return;
		}
		if(!reg.test(ownerPhone)){
	         MyAlert("车主电话格式不正确!");
	         return false;
	    }
		if(ownerPhone.length != 11 )
	    {
	        MyAlert('请输入有效的联系电话！');
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
		if(!doCheckTable1()) {//检查未确定车型表格
			return false;
		}
		if(!doCheckTable2()) {//检查已确定车型表格
			return false;
		}

		var customerId = document.getElementById("customerId").value;
		var taskId = document.getElementById("taskId").value;
		var table1 = document.getElementById("noConfirmVehicleTable");
		var s1 = table1.rows[table1.rows.length-1].id.charAt(table1.rows[table1.rows.length-1].id.length - 1);
		var table2 = document.getElementById("ConfirmVehicleTable");
		var s2 = table2.rows[table2.rows.length-1].id.charAt(table2.rows[table2.rows.length-1].id.length - 1);
	    var str=table2.rows[table2.rows.length-1].id.substring(table2.rows[table2.rows.length-1].id.length-2,table2.rows[table2.rows.length-1].id.length);
		if(str>=10){
			s2=str;
			}
		if(s1==0&&s2==0) {
			MyAlert("请添加车辆!");
			return false;
		}
		if(judgeOrderDetail()){
			MyAlert("确认车架号的，有一台车数据状态不对，请重新选择！！！");
				baocun.disabled = false;
				quxiao.disabled = false;
			return;
		}
		//var AuditFlag=doAlertCount();
		//if(!AuditFlag){//输出 订单中车总数量,确认车的总数量是不是大于1台车 大于需要提交经理审核
		///   return false;	
		//}
		//MyAlert("AuditFlag=="+AuditFlag);
		var AuditFlag = "sucess";
		baocun.disabled = true;
		quxiao.disabled = true;
		var url = "<%=contextPath%>/crm/taskmanage/TaskManage/doTask.json?taskType=order&typeFlag=''&AuditFlag="+AuditFlag+"&JsSucessNO="+JsSucessNO+"&laoSucessNO="+laoSucessNO+"&customerId="+customerId+"&taskId="+taskId+"&table1Length="+s1+"&table2Length="+s2;
		makeFormCall(url, doTask, "fm") ;
		function doTask(json) {
			MyAlert("保存成功!");
			baocun.disabled = false;
			quxiao.disabled = false;
			$('fm').action = "<%=contextPath%>/crm/taskmanage/TaskManage/doInit.do";
			$('fm').submit();
			
		}
	}
	
	//输出订单中车的总数量
	function doAlertCount(){
		var count=0;//总订车数
		var table = document.getElementById("noConfirmVehicleTable");
		var s = table.rows[table.rows.length-1].id.charAt(table.rows[table.rows.length-1].id.length - 1);
		var table2 = document.getElementById("ConfirmVehicleTable");
		var s2 = table2.rows[table2.rows.length-1].id.charAt(table2.rows[table2.rows.length-1].id.length - 1);
		var str=table2.rows[table2.rows.length-1].id.substring(table2.rows[table2.rows.length-1].id.length-2,table2.rows[table2.rows.length-1].id.length);
		if(str>=10){
			s2=str;
			}
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
	
	function doCheckTable1(){
		//check未确定车架号Table里的数据
		var table = document.getElementById("noConfirmVehicleTable");

		var s = table.rows[table.rows.length-1].id.charAt(table.rows[table.rows.length-1].id.length - 1);
		
		var materialCoderow=1;
		var pricerow=1;
		var numrow=1;
		var depositrow=1;
		var pre_pay_daterow=1;
		var pre_delivery_daterow=1;
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
			if(document.getElementById("numrow"+i1)!=null) {
				var model = document.getElementById("numrow"+i1).value;//数量
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
	
	
	function doCheckColor(){
		var table = document.getElementById("noConfirmVehicleTable");
		var length=document.getElementById("noConfirmVehicleTable").rows.length;
		var s = table.rows[table.rows.length-1].id.charAt(table.rows[table.rows.length-1].id.length - 1);
		for(var i1=1;i1<=parseInt(s);i1++) {
			if(document.getElementById("intent_colorrow"+i1)!=null) {
				var color = document.getElementById("intent_colorrow"+i1).value;
			    if(color==0){
			    	return false;
				    }
			}
		}
		return true;
		
		}
	function doCheckPresell(){
		var ifpresell=false;
		var table = document.getElementById("noConfirmVehicleTable");
		var length=document.getElementById("noConfirmVehicleTable").rows.length;
		var s = table.rows[table.rows.length-1].id.charAt(table.rows[table.rows.length-1].id.length - 1);
		for(var i1=1;i1<=parseInt(s);i1++) {
			if(document.getElementById("materialNamerow"+i1)!=null) {
				var chexId = document.getElementById("materialNamerow"+i1).value;
				var url = "<%=contextPath%>/crm/taskmanage/TaskManage/dopresell.json?chexId="+chexId;
				makeSameCall(url, showInfo, "fm") ;
				function showInfo(json) {
					if(json.ifpresell=="10041001"){
						ifpresell=true;
					}
				}
			    
			}
			
		}
		return ifpresell;
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
		var url = "<%=contextPath%>/crm/taskmanage/TaskManage/judgeVinStatus.json?vin="+vin;
		makeSameCall(url, doResult, "fm") ;
		function doResult(json) {
			if(json.flag=='1'){
				flag=true;
			}
		}
		return flag;
	}
	
	
	function addRow() {
		var nowDate=$("curDate").value;
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
		td.innerHTML = "<input type='text' class='middle_txt' id='materialCode"+tr.id+"' name='materialCode"+tr.id+"'  value='' style='display: inline-block; float: left;width: 110px;' readonly='readonly'  /><input type='hidden' name='materialName"+tr.id+"' size='20' id='materialName"+tr.id+"' value='' /><input type='button' value='...' class='mini_btn'  onclick=\"toIntentVechileList('materialCode"+tr.id+"','materialName"+tr.id+"');\" style='display: inline-block;'/>";
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
		td.innerHTML = "<input type=\"text\" value=\"\" onPropertyChange=\"doChangeDate('"+tr.id+"');\" name=\"pre_pay_date"+tr.id+"\" id=\"pre_pay_date"+tr.id+"\" group=\"pre_pay_date"+tr.id+"\" class=\"short_txt\" datatype=\"1,is_date,10\" size=\"20\" hasbtn='true' readonly=\"readonly\" maxlength=\"60\"/>&nbsp;<input class=\"time_ico\" type=\"button\" onClick=\"showcalendar(event, 'pre_pay_date"+tr.id+"', false);\" value=\"&nbsp;\"/>";
		td.id=tr.id+"col"+7;
		tr.appendChild(td);
		//第八列（交车日期）
		var td=document.createElement("td");
		td.innerHTML = "<input type=\"text\" value=\""+nowDate+"\" name=\"pre_delivery_date"+tr.id+"\" id=\"pre_delivery_date"+tr.id+"\" group=\"pre_delivery_date"+tr.id+"\" class=\"short_txt\" datatype=\"1,is_date,10\" size=\"20\" hasbtn='true' readonly=\"readonly\" maxlength=\"60\" />&nbsp;<input class=\"time_ico\" type=\"button\" onClick=\"showcalendar(event, 'pre_delivery_date"+tr.id+"', false);\" value=\"&nbsp;\"/>";
		td.id=tr.id+"col"+8;
		tr.appendChild(td);
		//第九列（已交车数量）
		var td=document.createElement("td");
		td.innerHTML = "<input type='text' id='delivery_number"+tr.id+"' name='delivery_number"+tr.id+"' style='width:56px' readonly='readonly' style='background-color: #EEEEEE;'/><input type='button' onclick=deleteRowBy('"+tr.id+"') value='删除' style='margin-left: 5px' />";
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

	function followClick(){
		var followTable = document.getElementById("follow_table");
		var inviteTable = document.getElementById("invite_table");
		var defeatTable = document.getElementById("defeat_table");
		var orderTable = document.getElementById("order_table");
		var failureTable = document.getElementById("failure_table");
		var intentType = document.getElementById("intent_type2");
		var intentTypeh = document.getElementById("intent_typeh");
		//intentType.value = '';
		//intentTypeh.innerHTML = "--请选择--";
		//intentType.value = "${ctmRank}";
		//intentTypeh.innerHTML = "${ctmRank2}";
		followTable.style.display = "block";
		inviteTable.style.display = "none";
		defeatTable.style.display = "none";
		orderTable.style.display = "none";
		failureTable.style.display = "none";
		intentTypeh.setAttribute("notselected","60101005,60101006,60101007");
		intentTypeh.setAttribute("isload",false);
	}
	function inviteClick(){
		var followTable = document.getElementById("follow_table");
		var inviteTable = document.getElementById("invite_table");
		var defeatTable = document.getElementById("defeat_table");
		var orderTable = document.getElementById("order_table");
		var failureTable = document.getElementById("failure_table");
		//var intentType = document.getElementById("intent_type3");
		//var intentTypeh = document.getElementById("intent_typeh");
		//intentType.value = '';
		//intentTypeh.innerHTML = "--请选择--";
		//intentType.value = "${ctmRank}";
		//intentTypeh.innerHTML = "${ctmRank2}";
		followTable.style.display = "none";
		inviteTable.style.display = "block";
		defeatTable.style.display = "none";
		orderTable.style.display = "none";
		failureTable.style.display = "none";
		intentTypeh.setAttribute("notselected","60101005,60101006,60101007");
		intentTypeh.setAttribute("isload",false);
	}
	function orderClick(){
		var followTable = document.getElementById("follow_table");
		var inviteTable = document.getElementById("invite_table");
		var defeatTable = document.getElementById("defeat_table");
		var orderTable = document.getElementById("order_table");
		var failureTable = document.getElementById("failure_table");
		var intentType = document.getElementById("intent_type");
		var intentTypeh = document.getElementById("intent_typeh");
		//intentType.value = '60101005';
		//intentTypeh.innerHTML = "O";
		followTable.style.display = "none";
		inviteTable.style.display = "none";
		defeatTable.style.display = "none";
		orderTable.style.display = "block";
		failureTable.style.display = "none";
		intentTypeh.setAttribute("notselected","60101004,60101006,60101007,60101001,60101002,60101003");
		intentTypeh.setAttribute("isload",false);
	}
	function defeatClick(){
		var followTable = document.getElementById("follow_table");
		var inviteTable = document.getElementById("invite_table");
		var defeatTable = document.getElementById("defeat_table");
		var orderTable = document.getElementById("order_table");
		var failureTable = document.getElementById("failure_table");
		var intentType = document.getElementById("intent_type");
		var intentTypeh = document.getElementById("intent_typeh");
		//intentType.value = '60101006';
		//intentTypeh.innerHTML = "E";
		followTable.style.display = "none";
		inviteTable.style.display = "none";
		defeatTable.style.display = "block";
		orderTable.style.display = "none";
		failureTable.style.display = "none";
		intentTypeh.setAttribute("notselected","60101004,60101005,60101007,60101001,60101002,60101003");
		intentTypeh.setAttribute("isload",false);
	}
	function failureClick(){
		var followTable = document.getElementById("follow_table");
		var inviteTable = document.getElementById("invite_table");
		var defeatTable = document.getElementById("defeat_table");
		var orderTable = document.getElementById("order_table");
		var failureTable = document.getElementById("failure_table");
		var intentType = document.getElementById("intent_type");
		var intentTypeh = document.getElementById("intent_typeh");
		//intentType.value = '60101007';
		//intentTypeh.innerHTML = "L";
		followTable.style.display = "none";
		inviteTable.style.display = "none";
		defeatTable.style.display = "none";
		orderTable.style.display = "none";
		failureTable.style.display = "block";
		intentTypeh.setAttribute("notselected","60101004,60101006,60101005,60101001,60101002,60101003");
		intentTypeh.setAttribute("isload",false);
	}
	function checkClick(){
		var check = document.getElementById("checkbox");
		var yaoyuejihua = document.getElementById("yaoyuejihua");
		var yaoyuejihua2 = document.getElementById("yaoyuejihua2");
		if(check.checked){
			yaoyuejihua.style.display = "block";
			yaoyuejihua2.style.display = "block";
			document.getElementById("xqfx").value = "";
			document.getElementById("yymb").value = "";
			document.getElementById("ydkhxrsj").value = "";
			document.getElementById("gdkhqjsj").value = "";
		} else {
			yaoyuejihua.style.display = "none";
			yaoyuejihua2.style.display = "none";
			document.getElementById("xqfx").value = "";
			document.getElementById("yymb").value = "";
			document.getElementById("ydkhxrsj").value = "";
			document.getElementById("gdkhqjsj").value = "";
		}
	}

	function doSaveTask(){
		var salesProgress = document.getElementById("sales_progress").value;
		var followRadio = document.getElementById("follow_radio");
		var nextFollowDate = document.getElementById("next_follow_date").value;
		var telephone = document.getElementById("telephone").value;
		var inviteRadio = document.getElementById("invite_radio");
		var xqfx = document.getElementById("xqfx").value;
		var yymb = document.getElementById("yymb").value;
		var ydkhxrsj = document.getElementById("ydkhxrsj").value;
		var gdkhqjsj = document.getElementById("gdkhqjsj").value;
		var planInviteDate = document.getElementById("plan_invite_date").value;
		var followType = document.getElementById("follow_type").value;
		var planMeetDate = document.getElementById("plan_meet_date").value;
		var inviteTypeNew = document.getElementById("invite_type_new").value;
		var orderRadio = document.getElementById("order_radio");
		var orderDate = document.getElementById("order_date").value;
		var defeatRadio = document.getElementById("defeat_radio");
		var defeatVehicle = document.getElementById("defeatVehicleB").value;
		var defeatReason = document.getElementById("defeatReasonB").value;
		var defeatEndDate = document.getElementById("defeat_end_date").value;
		var failureRadio = document.getElementById("failure_radio");
		var failureDate = document.getElementById("failure_date").value;
		var failureRemark = document.getElementById("failure_remark").value;
		var intentType2 = document.getElementById("intent_type2").value;
		var intentType3 = document.getElementById("intent_type3").value;
		var reg = new RegExp("^[0-9]*$");
		var time = new Date().Format("yyyy-MM-dd");
		function getZ(n){    return /^\d+(\.\d+)?$/.test(n+"");}
		if(followRadio.checked == true) {
			var dd = new Date();
			var nextDate=parseDate(nextFollowDate);
			var inbound=document.getElementById("intent_typeh").innerHTML.split("<")[0];
			if(inbound == "A"){
				var da = new Date();
		    	da.setDate(da.getDate()+6);
				if(nextDate > da ){
					MyAlert("A等级跟进时间要小于7天");
					return false;
				}	
			}
			if(inbound == "B"){
				var db = new Date();
		    	db.setDate(db.getDate()+14);
				if(nextDate >  db ){
					MyAlert("B等级跟进时间要小于15天");
					return false;
				}
			}
			if(inbound == "C"){
				var dc = new Date();
		    	dc.setDate(dc.getDate()+29);
				if(nextDate > dc ){
					MyAlert("C等级跟进时间要小于30天");
					return false;
				}	
			}
			 if(inbound == "H"){
				    var dh = new Date();
			    	dh.setDate(dh.getDate()+2);
					if(nextDate > dh){
						MyAlert("H等级跟进时间要小于3天");
						return false;
					}	
			}
			if(nextFollowDate==null||nextFollowDate=="") {
				MyAlert("请选择下次跟进时间！");
				return false;
			} else if(followType==null||followType=="") {
				MyAlert("请选择跟进方式！");
				return false;
			}else if(nextDate < dd.setDate(dd.getDate()-1)){
				MyAlert("跟进时间要大于当前时间");
				return false;
			} else {
				var customerId = document.getElementById("customerId").value;
				var taskId = document.getElementById("taskId").value;
				document.getElementById("saveButton").disabled = true;
				document.getElementById("insertBtn").disabled = true;
				$('fm').action = "<%=contextPath%>/crm/taskmanage/TaskManage/doTask.do?taskType=orderYN&typeFlag=followRadio&customerId="+customerId+"&taskId="+taskId+"&intentType2="+intentType2;
				$('fm').submit();
			}
		} else if(inviteRadio.checked == true) {
			var dd = new Date();
			var planIDate=parseDate(planInviteDate);
			var planMDate=parseDate(planMeetDate);
			var inbound=document.getElementById("intent_typey").innerHTML.split("<")[0];
			if(inbound == "A"){
				var da = new Date();
		    	da.setDate(da.getDate()+6);
				if(planIDate > da ){
					MyAlert("A等级邀约时间要小于7天");
					return false;
				}	
			}
			if(inbound == "B"){
				var db = new Date();
		    	db.setDate(db.getDate()+14);
				if(planIDate >  db ){
					MyAlert("B等级邀约时间要小于15天");
					return false;
				}
			}
			if(inbound == "C"){
				var dc = new Date();
		    	dc.setDate(dc.getDate()+29);
				if(planIDate > dc ){
					MyAlert("C等级邀约时间要小于30天");
					return false;
				}	
			}
			 if(inbound == "H"){
				    var dh = new Date();
			    	dh.setDate(dh.getDate()+2);
					if(planIDate > dh){
						MyAlert("H等级邀约时间要小于3天");
						return false;
					}	
			}
			
			if(planInviteDate==null||planInviteDate=="") {
				MyAlert("请选择计划邀约时间");
				return false;
			}
			if(planMeetDate==null||planMeetDate=="") {
				MyAlert("请选择计划见面时间！");
				return false;
			}
			if(inviteTypeNew==null||inviteTypeNew=="") {
				MyAlert("请选择邀约方式！");
				return false;
			}
			if(planIDate < dd.setDate(dd.getDate()-1)){
				MyAlert("邀约时间要大于当前时间！");
				return false;
			}
			if(planMDate < planIDate){
				MyAlert("计划见面时间大于邀约时间！");
				return false;
			}
			var customerId = document.getElementById("customerId").value;
			var taskId = document.getElementById("taskId").value;
			document.getElementById("saveButton").disabled = true;
			document.getElementById("insertBtn").disabled = true;
			$('fm').action = "<%=contextPath%>/crm/taskmanage/TaskManage/doTask.do?taskType=orderYN&typeFlag=inviteRadio&customerId="+customerId+"&taskId="+taskId+"&intentType3="+intentType3;
			$('fm').submit();
		} else if(orderRadio.checked == true) {
		
			var telephone=$("telephone").value;
			var flag=judgeIfAbleOder(telephone);
			if(!flag){
				MyAlert("该客户无首次到店客流信息，无法生成订车计划！！！");
				return;
			}
			
			if(orderDate==null||orderDate=="") {
				MyAlert("请选择订车时间！");
				return false;
			}
			if(orderDate < time){
				MyAlert("邀约时间要大于当前时间");
				return false;
			}
		 
			var customerId = document.getElementById("customerId").value;
			var taskId = document.getElementById("taskId").value;
			document.getElementById("saveButton").disabled = true;
			document.getElementById("insertBtn").disabled = true;
			$('fm').action = "<%=contextPath%>/crm/taskmanage/TaskManage/doTask.do?taskType=orderYN&typeFlag=orderRadio&customerId="+customerId+"&taskId="+taskId;
			$('fm').submit();
		} else if(defeatRadio.checked == true) {
			if(defeatVehicle==null||defeatVehicle=="") {
				MyAlert("请选择战败车型！");
				return false;
			}
			if(defeatReason==null||defeatReason=="") {
				MyAlert("请选择战败原因！");
				return false;
			}
			if(defeatEndDate==null||defeatEndDate=="") {
				MyAlert("请选择战败结束日期！");
				return false;
			}
			var customerId = document.getElementById("customerId").value;
			var taskId = document.getElementById("taskId").value;
			document.getElementById("saveButton").disabled = true;
			document.getElementById("insertBtn").disabled = true;
			$('fm').action = "<%=contextPath%>/crm/taskmanage/TaskManage/doTask.do?taskType=orderYN&typeFlag=defeatRadio&customerId="+customerId+"&taskId="+taskId;
			$('fm').submit();
		} else if(failureRadio.checked == true) {
			if(failureDate==null||failureDate=="") {
				MyAlert("请选择失效日期！");
				return false;
			}
			var customerId = document.getElementById("customerId").value;
			var taskId = document.getElementById("taskId").value;
			document.getElementById("saveButton").disabled = true;
			document.getElementById("insertBtn").disabled = true;
			$('fm').action = "<%=contextPath%>/crm/taskmanage/TaskManage/doTask.do?taskType=orderYN&typeFlag=failureRadio&customerId="+customerId+"&taskId="+taskId;
			$('fm').submit();
		}
	}
	function parseDate(str)  
	{
	    return new Date(Date.parse(str.replace(/-/g,"/")));
	}
	function doCusChange()
	{
		var obj = document.getElementById("orderSucessNO"); //定位id
		var index = obj.selectedIndex; // 选中索引
		var orderSucessNO = obj.options[index].value; // 选中值		
		
		var noCVTable=document.getElementById("noConfirmVehicleTable");
		var CVTable=document.getElementById("ConfirmVehicleTable");
		var noSureVin=document.getElementById("noSureVin");
		var sureVin=document.getElementById("sureVin");
		var saveVin=document.getElementById("saveVin");
		var followTable = document.getElementById("follow_table");
		var inviteTable = document.getElementById("invite_table");
		var defeatTable = document.getElementById("defeat_table");
		var orderTable = document.getElementById("order_table");
		var failureTable = document.getElementById("failure_table");
		var intentType = document.getElementById("intent_type");
		var intentTypeh = document.getElementById("intent_typeh");
		var nextTask = document.getElementById("nexttask");
		var followTask = document.getElementById("followtask");
		var saveFollow = document.getElementById("savefollow");
		var customerInfo = document.getElementById("customerInfo");
		var carInfo = document.getElementById("carInfo");
		var customerList = document.getElementById("customerList");
		var carList = document.getElementById("carList");
		var ownerPhone = document.getElementById("owner_phone").value;//车主电话
		var qingLao= document.getElementById("qingLao");
		var qingLao1= document.getElementById("qingLao1");
		var oldcustomerList= document.getElementById("oldcustomerList");
		var oldInfo= document.getElementById("oldInfo");
		var obj1 = document.getElementById("JsSucessNO"); //定位id
		var index1 = obj1.selectedIndex; // 选中索引
		var JsSucessNO = obj.options[index1].value; // 选中值		
		if(orderSucessNO=='10041002')
		{
			noCVTable.style.display="none";
			CVTable.style.display="none";
			noSureVin.style.display="none";
			sureVin.style.display="none";
			saveVin.style.display="none";
			followTable.style.display = "block";
			inviteTable.style.display = "none";
			defeatTable.style.display = "none";
			orderTable.style.display = "none";
			failureTable.style.display = "none";
			nextTask.style.display = "block";
			followTask.style.display = "block";
			saveFollow.style.display = "block";
			customerInfo.style.display = "none";
			carInfo.style.display = "none";
			customerList.style.display = "none";
			carList.style.display = "none";
			qingLao.style.display = "none";
			qingLao1.style.display = "none";
			oldcustomerList.style.display = "none";
			oldInfo.style.display = "none";
		}
		else
		{
			noCVTable.style.display="block";
			CVTable.style.display="block";
			noSureVin.style.display="block";
			sureVin.style.display="block";
			saveVin.style.display="block";
			followTable.style.display = "none";
			inviteTable.style.display = "none";
			defeatTable.style.display = "none";
			orderTable.style.display = "none";
			failureTable.style.display = "none";
			nextTask.style.display = "none";
			followTask.style.display = "none";
			saveFollow.style.display = "none";
			customerInfo.style.display = "block";
			carInfo.style.display = "block";
			customerList.style.display = "block";
			carList.style.display = "block";
			qingLao.style.display = "inline";
			qingLao1.style.display = "inline";
			if(JsSucessNO=='10041002'){
			oldcustomerList.style.display = "block";
			oldInfo.style.display = "block";
			}
		}
		if(orderSucessNO=='10041001'){
			var flag=judgeIfAbleOderDate(ownerPhone);
			if(!flag){
				MyAlert("是否订车成功选择《是》DCRC未录入该客户今日到店客流信息，不能操作该订单信息！！！");
				return;
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
	
	function ydeleteRowBy(rowNum) {
		//取行数
		//var rownum = rowNum.charAt(rowNum.length - 1);
		var rownum=document.getElementById("ConfirmVehicleTable").rows.length;
		var willDeleteRow = document.getElementById("ConfirmVehicleTable");
		var tr = document.getElementById(rowNum);
		if(rownum<1) {
			return false;
		} else {
			willDeleteRow.deleteRow(tr.rowIndex);
		}
	}
</script>
<title>订单任务</title>
</head>
<body onunload='javascript:destoryPrototype();' onload="addRegionInit();doInit();">
	<div class="wbox" id="show">
		<div class="navigation">
			<img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置>潜客管理>日程管理>任务管理>订单任务
		</div>
		<form id="fm" name="fm" method="post">
			<input type="hidden" name="curPage" id="curPage" value="1" /> <input
				type="hidden" id="dlrId" name="dlrId" value="" /> <input
				type="hidden" name="customerId" id="customerId"
				value="${customerId }" /> <input type="hidden" name="taskId"
				id="taskId" value="${taskId }" /> <input type="hidden" id="curDate"
				name="curDate" value="${nowDate}" /> <input type="hidden"
				name="errorMsg" id="errorMsg" value="${errorMsg }" /> <input
				type="hidden" id="provinceId" name="provinceId" value="${province}" />
			<input type="hidden" id="cityId" name="cityId" value="${city}" /> <input
				type="hidden" id="townId" name="townId" value="${area}" />
			<div>
				<b style="display: inline-block; float: left">订单任务</b>
				<hr style="display: inline-block;">
			</div>
			<table class="table_query"  width="95%">
				<tr>
					<td align="right"  width="25%">是否订车成功:</td>
					<td align="left">
						<select id="orderSucessNO"  name="orderSucessNO" onchange="doCusChange();">
							<option id="orderSu" value="10041001">是</option>
							<option id="orderNo" value="10041002">否</option>
						</select>
					</td>
					<td id="qingLao" align="right">是否朋友/老客户介绍:</td>
					<td id="qingLao1"  align="left">
						<select id="JsSucessNO" name="JsSucessNO" onchange="doCusJsChange();">
							<option id="JsSu" value="10041002">否</option>
							<option id="JsNo" value="10041001">是</option>
						</select>
					</td>
				</tr>
			</table>
			
			<div id="oldcustomerList" style="display: none">
				<b style="display: inline-block; float: left">朋友/老客户信息</b>
				<hr style="display: inline-block;">
			</div>
			</br>
			
			<table id="oldInfo" class="table_query" width="95%" align="center" style="display: none">
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
							</c:if>
							<c:if test="${relation_code=='60581001'}">
							<option id="laoSu" value="60581001">老客户介绍</option>
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
						<c:if test="${relation_code!='60581002'}">
						<td align="right" width="12%" id="oldhao">
							老客户车架号：
						</td>
						<td width="17%" id="oldhao1" align="left" >
							<input id="old_vehicle_id" name="old_vehicle_id"
								onchange="nameBlurChange11()" type="text" class="middle_txt"
								datatype="1,is_textarea,30" value="${oldVehicleId }" size="20"
								maxlength="60" />
						</td>
						</c:if>
					</tr>
			</table>
			</br>
			
			
			<div id="customerList">
				<b style="display: inline-block; float: left">客户信息</b>
				<hr style="display: inline-block;">
			</div>
			</br>
			<table class="table_query" width="95%" align="center"
				id="customerInfo">
				<c:forEach items="${customerList }" var="customerList">
					<tr>
						<td align="right" width="12%">客户编码：</td>
						<td><input id="customer_code" name="customer_code"
							type="text" readonly="readonly"
							style="background-color: #EEEEEE;" class="middle_txt"
							datatype="1,is_textarea,30" size="20"
							value="${customerList.CUSTOMER_CODE }" maxlength="60" /></td>
						<td align="right" width="14%">客户姓名：</td>
						<td><input id="customer_name" name="customer_name"
							type="text" readonly="readonly"
							style="background-color: #EEEEEE;" class="middle_txt"
							datatype="1,is_textarea,30" size="20"
							value="${customerList.CUSTOMER_NAME }" maxlength="60" /></td>
						<td align="right" width="14%">联系电话：</td>
						<td width="12%"><input id="telephone" name="telephone"
							readonly="readonly" style="background-color: #EEEEEE;"
							type="text" class="middle_txt" datatype="1,is_textarea,30"
							value="${customerList.TELEPHONE }" size="20" maxlength="60" /></td>
						<td align="right" width="6%">客户地址：</td>
						<td width="12%"><input id="address" name="address"
							readonly="readonly" style="background-color: #EEEEEE;"
							type="text" class="middle_txt" datatype="1,is_textarea,30"
							value="${customerList.ADDRESS }" size="20" maxlength="60" /></td>
					</tr>

					<tr>
						<td align="right" width="6%">客户证件名：</td>
						<td><input id="paper_name" name="paper_name" type="text"
							readonly="readonly" style="background-color: #EEEEEE;"
							class="middle_txt" datatype="1,is_textarea,30" size="20"
							value="${customerList.CODE_DESC }" maxlength="60" /></td>
						<td align="right" width="6%">客户证件号：</td>
						<td><input id="paper_no" name="paper_no" type="text"
							readonly="readonly" style="background-color: #EEEEEE;"
							class="middle_txt" datatype="1,is_textarea,30" size="20"
							value="${customerList.PAPER_NO }" maxlength="60" /></td>
						<td width="3%" align="right">意向等级：</td>
						<td width="22%"><select id="intent_type" name="intent_type"
							style="visibility: visible;width: 50px">
								<option value="60101005">O</option>
						</select></td>
						<td align="right" width="15%">销售流程进度：</td>
						<td width="25%"><select id="sales_progress"
							name="sales_progress" style="visibility: visible;width: 80px">
								<option value="60371006">报价成交</option>
						</select></td>
					</tr>
				</c:forEach>
			</table>
			</br>
			<div id="carList">
				<b style="display: inline-block; float: left">车主信息</b>
				<hr style="display: inline-block;">
			</div>
			</br>
			<table class="table_query" width="95%" align="center" id="carInfo">
				<c:forEach items="${ownerList }" var="ownerList">
					<tr>
						<td align="right" width="5%">车主姓名：</td>
						<td><input id="owner_name" name="owner_name" type="text"
							class="middle_txt" datatype="1,is_textarea,30" size="20"
							value="${ownerList.OWNER_NAME }" maxlength="60" /></td>
						<td align="right" width="5%">车主电话：</td>
						<td width="17%"><input id="owner_phone" name="owner_phone"
							type="text" class="middle_txt" datatype="1,is_textarea,30"
							size="20" value="${ownerList.PHONE }" maxlength="60" /></td>
						<td align="right" width="9%">证件类型：</td>
						<td><input type="hidden" id="owner_paper_type"
							name="owner_paper_type" value="${ownerList.PAPER_TYPE }" />
							<div id="ddtopmenubar28" class="mattblackmenu">
								<ul>
									<li><a style="width:103px;" rel="ddsubmenu28" href="###"
										isclick="true"
										onclick="stree.loadtree(this, '<%= request.getContextPath() %>/crm/data/DataManage/initData.json?codeId=1093', loadOwnerPaperType);"
										deftitle="--请选择--"> <c:if
												test="${ownerList.PAPER_TYPE==null}">--请选择--</c:if>
											<c:if test="${ownerList.PAPER_TYPE!=null}">${ownerList.PAPER_TYPE2}</c:if></a>
										<ul id="ddsubmenu28" class="ddsubmenustyle"></ul></li>
								</ul></td>
						<td align="right" width="7%">证件号码：</td>
						<td><input id="owner_paper_no" name="owner_paper_no"
							type="text" class="middle_txt" datatype="1,is_textarea,30"
							size="20" value="${ownerList.PAPER_NO }" maxlength="60" /></td>
					</tr>
					<tr>
						<td align="right" width="10%">&nbsp;省份：</td>
						<td align="left" width="24%" colspan="3"><select
							class="min_sel" id="dPro" name="dPro"
							onchange="_regionCity(this,'dCity')"></select> 城市： <select
							class="min_sel" id="dCity" name="dCity"
							onchange="_regionCity(this,'dArea')"></select> 区县： <select
							class="min_sel" id="dArea" name="dArea"></select></td>
						<td align="right" width="10%"></td>
						<td></td>
						<td align="right" width="7%"></td>
						<td></td>
					</tr>
					<tr>
						<td align="right" colspan="1">详细地址：</td>
						<td align="left" colspan="10"><textarea rows="2" cols="100"
								id="owner_address" name="owner_address">${address }</textarea></td>
					</tr>
				</c:forEach>
				<tr>
					<td align="right" width="10%">新产品预售：</td>
					<td><select id="new_product_sale" name="new_product_sale">
							<c:if test="${ifpresell=='10041001'}">
								<option id="no" value="10041001" selected="selected">是</option>
								<option id="yes" value="10041002">否</option>
							</c:if>
							<c:if test="${ifpresell!='10041001'}">
								<option id="no" value="10041001">是</option>
								<option id="yes" value="10041002" selected="selected">否</option>
							</c:if>

					</select></td>
					<td align="right" width="8%"></td>
					<td width="17%"></td>
					<td align="right" width="9%"></td>
					<td></td>
					<td align="right" width="7%"></td>
					<td></td>
				</tr>
				<tr>

					<td align="right" width="10%">购置方式：</td>
					<td width="20%"><input type="hidden" id="deal_type"
						name="deal_type" value="${buyWay }" />
						<div id="ddtopmenubar25" class="mattblackmenu">
							<ul>
								<li><a style="width:103px;" rel="ddsubmenu25" href="###"
									isclick="true"
									onclick="stree.loadtree(this, '<%= request.getContextPath() %>/crm/data/DataManage/initData.json?codeId=6005', loadDealType);"
									deftitle="--请选择--"> <c:if test="${buyWay==null}">--请选择--</c:if>
										<c:if test="${buyWay!=null}">${buyWay2}</c:if></a>
									<ul id="ddsubmenu25" class="ddsubmenustyle"></ul></li>
							</ul></td>
					<td align="right" width="8%">试乘试驾：</td>
					<td width="17%"><input type="hidden" id="test_driving"
						name="test_driving" value="" />
						<div id="ddtopmenubar29" class="mattblackmenu">
							<ul>
								<li><a style="width:103px;" rel="ddsubmenu29" href="###"
									isclick="true"
									onclick="stree.loadtree(this, '<%= request.getContextPath() %>/crm/data/DataManage/initData.json?codeId=1004', loadIfDriving2);"
									deftitle="--请选择--"> <c:if
											test="${dataList[0].IF_DRIVING==null}">--请选择--</c:if>
										<c:if test="${dataList[0].IF_DRIVING!=null}">${dataList[0].IF_DRIVING}</c:if></a>
									<ul id="ddsubmenu29" class="ddsubmenustyle"></ul></li>
							</ul></td>
					<td align="right" width="9%"></td>
					<td></td>
					<td align="right" width="7%"></td>
					<td></td>
				</tr>
				
				
				<%-- <tr>
					<td align="center" colspan="8"><a href="#" id="FOLLOW_ID"
						name="FOLLOW_ID" onclick='followOpenInfo("${customerId}")'>客户资料维护</a>
					</td>
				</tr> --%>
			</table>
			</br>

			<div id="nexttask">
				<b style="display: inline-block; float: left">下次计划任务</b>
				<hr style="display: inline-block;">
			</div>
			<table class="table_query" width="95%" align="center" id="groupRadio">
				<tr id="followtask">
					<td align="right" width="20%"><input type="radio"
						id="follow_radio" name="group_radio" onclick="followClick()"
						checked="checked">跟进</input> 
						
						<input type="radio" id="invite_radio"
						name="group_radio" onclick="inviteClick()" style="display:none;"><!-- 邀约 --></input> 
						<input
						type="radio" id="order_radio" name="group_radio"
						onclick="orderClick()">订车</input></td>
					<td width="18%"><input type="radio" id="defeat_radio"
						name="group_radio" onclick="defeatClick()">战败</input> <input
						type="radio" id="failure_radio" name="group_radio"
						onclick="failureClick()" style="display:none;"><!-- 失效 --></input></td>
				</tr>
			</table>
			</br>
			<table class="table_query" width="95%" align="center"
				id="follow_table">
				<tr>
					<td width="10%" align="right">下次跟进时间：</td>
					<td width="20%">
						<div align="left">
							<input name="next_follow_date" id="next_follow_date"
								readonly="readonly" value="" type="text" class="short_txt"
								datatype="1,is_date,10" group="startDate,endDate" /> <input
								id="next_follow_date2" name="next_follow_date2"
								style="margin-left: -4px;" class="time_ico" type="button"
								onclick="changeEvent()" ;  />
						</div>
					</td>
					<td align="right" width="7%">意向等级：</td>
					<td><input type="hidden" id="intent_type2" name="intent_type2"
						value="" />
						<div id="ddtopmenubar24" class="mattblackmenu">
							<ul>
								<li><a id="intent_typeh" style="width: 103px;"
									rel="ddsubmenu24" href="###" isclick="true"
									onclick="stree.loadtree(this, '<%=request.getContextPath()%>/crm/data/DataManage/initData.json?codeId=6010', loadIntentType2);"
									notselected="60101005,60101006,60101007" deftitle="--请选择--">
										--请选择--</a>
									<ul id="ddsubmenu24" class="ddsubmenustyle"></ul></li>
							</ul>
						</div></td>
					<td width="8%" align="right">跟进方式：</td>
					<td width="20%"><input type="hidden" id="follow_type"
						name="follow_type" value="" />
						<div id="ddtopmenubar27" class="mattblackmenu">
							<ul>
								<li><a style="width:103px;" rel="ddsubmenu27" href="###"
									isclick="true"
									onclick="stree.loadtree(this, '<%= request.getContextPath() %>/crm/data/DataManage/initData.json?codeId=6046', loadFollowType);"
									deftitle="--请选择--"> --请选择--</a>
									<ul id="ddsubmenu27" class="ddsubmenustyle"></ul></li>
							</ul></td>
				</tr>
				<tr>
					<td width="6%" align="right">跟进计划：</td>
					<td align="left" colspan="6"><textarea rows="5" cols="70"
							id="follow_plan" name="follow_plan"></textarea></td>
				</tr>
			</table>

			<table class="table_query" width="95%" align="center"
				id="invite_table" style="display: none">
				<tr>
					<td align="right" colspan="4">是否填写邀约计划</td>
					<td colspan="4"><input type="checkbox" id="checkbox"
						onclick="checkClick()" /></td>
				</tr>
				<tr id="yaoyuejihua" style="display: none">
					<td align="center" colspan="2">需求分析</td>
					<td align="center" colspan="2">邀约目标</td>
					<td align="center" colspan="2">赢得客户信任设计</td>
					<td align="center" colspan="2">感动客户情景设计</td>
				</tr>
				<tr id="yaoyuejihua2" style="display: none">
					<td align="center" colspan="2"><textarea rows="10" cols="30"
							id="xqfx" name="xqfx"></textarea></td>
					<td align="center" colspan="2"><textarea rows="10" cols="30"
							id="yymb" name="yymb"></textarea></td>
					<td align="center" colspan="2"><textarea rows="10" cols="30"
							id="ydkhxrsj" name="ydkhxrsj"></textarea></td>
					<td align="center" colspan="2"><textarea rows="10" cols="30"
							id="gdkhqjsj" name="gdkhqjsj"></textarea></td>
				</tr>
				<tr>
					<td>&nbsp;</td>
					<td align="right">计划邀约时间:</td>
					<td><input name="plan_invite_date" id="plan_invite_date"
						readonly="readonly" value="" type="text" class="short_txt"
						datatype="1,is_date,10" group="startDate,endDate" /> <input
						id="plan_invite_date2" name="plan_invite_date2"
						style="margin-left: -4px;" class="time_ico" type="button"
						onClick="changeEvent1()" /></td>

					<td align="right" width="7%">意向等级：</td>
					<td><input type="hidden" id="intent_type3" name="intent_type3"
						value="" />
						<div id="ddtopmenubar22" class="mattblackmenu">
							<ul>
								<li><a id="intent_typey" style="width: 103px;"
									rel="ddsubmenu22" href="###" isclick="true"
									onclick="stree.loadtree(this, '<%=request.getContextPath()%>/crm/data/DataManage/initData.json?codeId=6010', loadIntentType3);"
									notselected="60101005,60101006,60101007" deftitle="--请选择--">
										--请选择--</a>
									<ul id="ddsubmenu22" class="ddsubmenustyle"></ul></li>
							</ul>
						</div></td>
					<td align="right">计划见面时间:</td>
					<td><input type="text" value="" name="plan_meet_date"
						id="plan_meet_date" group="plan_meet_date" class="short_txt"
						datatype="1,is_date,10" size="20" hasbtn="true"
						readonly="readonly" maxlength="60"
						callFunction="showcalendar(event, 'plan_meet_date', false);" /></td>

					<td align="right">邀约方式:</td>
					<td><input type="hidden" id="invite_type_new"
						name="invite_type_new" value="" />
						<div id="ddtopmenubar26" class="mattblackmenu">
							<ul>
								<li><a style="width:103px;" rel="ddsubmenu26" href="###"
									isclick="true"
									onclick="stree.loadtree(this, '<%= request.getContextPath() %>/crm/data/DataManage/initData.json?codeId=6047', loadInviteType2);"
									deftitle="--请选择--"> --请选择--</a>
									<ul id="ddsubmenu26" class="ddsubmenustyle"></ul></li>
							</ul></td>
					<td align="right"></td>

				</tr>
			</table>

			<table class="table_query" width="95%" align="center"
				id="order_table" style="display: none">
				<tr>
					<td align="right" width="11%">订车时间:</td>
					<td width="12%"><input type="text" value="" name="order_date"
						id="order_date" group="order_date" class="short_txt"
						datatype="1,is_date,10" size="20" hasbtn="true"
						readonly="readonly" maxlength="60"
						callFunction="showcalendar(event, 'order_date', false);" /></td>
				</tr>
			</table>

			<table class="table_query" width="95%" align="center"
				id="defeat_table" style="display: none">
				<tr>
					<td align="right">战败车型：</td>
					<td><select id="defeatVehicleA" name="defeatVehicleA"
						onchange="toChangeMenu2(this,'defeatVehicleB')">
							<option id="all" value="">-请选择-</option>
							<c:forEach items="${menusAList2 }" var="alist">
								<option id="${alist.MAINID }" value="${alist.MAINID }">${alist.NAME }</option>
							</c:forEach>
					</select> <select id="defeatVehicleB" name="defeatVehicleB">
							<option id="all" value="">-请选择-</option>
					</select></td>
					<td align="right">战败原因：</td>
					<td><input type="hidden" id="defeatReasonB"
						name="defeatReasonB" value="" />
						<div id="ddtopmenubar23" class="mattblackmenu">
							<ul>
								<li><a style="width:103px;" rel="ddsubmenu23" href="###"
									isclick="true"
									onclick="stree.loadtree(this, '<%= request.getContextPath() %>/crm/data/DataManage/initData.json?codeId=6033', loadDefeatReason2);"
									deftitle="--请选择--"> --请选择--</a>
									<ul id="ddsubmenu23" class="ddsubmenustyle"></ul></li>
							</ul>
						</div></td>
					<td align="right">战败时间：</td>
					<td>
						<div align="left">
							<input type="text" value="${nowDate }" name="defeat_end_date"
								id="defeat_end_date" group="defeat_end_date" class="short_txt"
								datatype="1,is_date,10" size="20" hasbtn="true"
								readonly="readonly" maxlength="60"
								callFunction="showcalendar(event, 'defeat_end_date', false);" />
						</div>
					</td>
				</tr>
				<tr>
					<td align="right" colspan="1">原因分析：</td>
					<td align="left" colspan="8"><textarea rows="5" cols="72"
							id="reason_analysis" name="reason_analysis"></textarea></td>
				</tr>
			</table>
			<table class="table_query" width="95%" align="center"
				id="failure_table" style="display: none">
				<tr>
					<td align="right" width="11%">失效时间:</td>
					<td width="12%"><input type="text" value=""
						name="failure_date" id="failure_date" group="failure_date"
						class="short_txt" datatype="1,is_date,10" size="20" hasbtn="true"
						readonly="readonly" maxlength="60"
						callFunction="showcalendar(event, 'failure_date', false);" /></td>
					<td align="right" width="11%"></td>
					<td width="12%"></td>
					<td align="right" width="11%"></td>
					<td width="12%"></td>
					<td align="right" width="11%"></td>
					<td width="12%"></td>
				</tr>
				<tr>
					<td align="right" colspan="1">原因说明：</td>
					<td align="left" colspan="8"><textarea rows="5" cols="72"
							id="failure_remark" name="failure_remark"></textarea></td>
				</tr>
			</table>
			<table class="table_query" width="95%" align="center">

				<tr id="savefollow">
					<td colspan="3" align="center">
						<!-- 此处只有顾问登陆时才有保存按钮 --> <c:if test="${adviserLogon=='yes' }">
							<input name="queryBtn" type="button" class="normal_btn"
								onclick="doSaveTask();" id="saveButton" value="保存" />
						</c:if> <input name="insertBtn" id="insertBtn" type="button"
						class="normal_btn" onclick="javascript:history.go(-1);" value="取消" />
					</td>
				</tr>
			</table>

			</br>
			<div id="noSureVin">
				<b style="display: inline-block; float: left">未确定车架号</b>
				<hr style="display: inline-block;">
			</div>
			</br> </br>
			<center>
				<table border=0 align=center id="noConfirmVehicleTable" style="width:95%;background-color: #F0F7F2;">
					<tr style="height: 23px; font-weight: bold; color:#39669f;background-color: #DAE0EE;" id="row0">
						<td width="14%">车型</td>
						<td width="10%">颜色</td>
						<td width="7%">价格(元)</td>
						<td width="6%">数量</td>
						<td width="7%">总金额(元)</td>
						<td width="3%">订金(元)</td>
						<td width="3%">定金(元)</td>
						<td width="8%">余款付款日期</td>
						<td width="8%">交车日期</td>
						<td width="8%">已交车数量<input type="button" onclick="addRow()" value="新增" /></td>
					</tr>
					<tr id="row1"
						style="height: 23px;background-color: white;border: 0; border-color:#44BBBB; ">
						<td id="row1col1"><input type="text" class="middle_txt"
							id="materialCoderow1" name="materialCoderow1"
							value="${seriesName }" style="display: inline-block; float: left;width: 110px;"
							readonly="readonly" /> <input type="hidden"
							name="materialNamerow1" size="20" id="materialNamerow1"
							value="${intentVehicle }" /> 
							<input type="button" value="..." class="mini_btn" onclick="toIntentVechileList('materialCoderow1','materialNamerow1')"  style="display: inline-block;" />
						</td>
						<td id="row1col2"><select id="intent_colorrow1" name="intent_colorrow1">
								<c:forEach items="${colorList }" var="color">
									<option value="${color.CODE_ID }">${color.CODE_DESC }</option>
								</c:forEach></select></td>
						<td id="row1col3"><input type="text" id="pricerow1"
							name="pricerow1" checkFlag="jiage" style="width:80px"
							onblur="checkNum(this)" /></td>
						<td id="row1col4"><input type="text" id="numrow1"
							name="numrow1" checkFlag="shuliang" style="width:70px"
							onblur="checkNum(this)" /></td>
						<td id="row1col5"><input type="text" id="amountrow1"
							name="amountrow1" style="width:70px" readonly="readonly"
							style="background-color: #EEEEEE;" /></td>
						<td id="row1col6"><input type="text" id="depositrow1"
							name="depositrow1" checkFlag="dingjin" style="width:50px"
							onblur="checkNum(this)" /></td>
						<td id="row1col10"><input type="text" id="earnestrow1"
							name="earnestrow1" checkFlag="dingjin2" style="width:50px"
							onblur="checkNum(this)" /></td>
						<td id="row1col7"><input type="text" value=""
							name="pre_pay_daterow1" id="pre_pay_daterow1"
							group="pre_pay_daterow1" class="short_txt" datatype="1,is_date,10"
							size="20"  readonly="readonly" maxlength="60" style="width: 70px;"
							onPropertyChange="doChangeDate('row1');" />
							<input class="time_ico" type="button" onClick="showcalendar(event, 'pre_pay_daterow1', false);" value="&nbsp;" />
							</td>
						<td id="row1col8"><input type="text" value="${nowDate}"
							name="pre_delivery_daterow1" id="pre_delivery_daterow1"
							group="pre_delivery_daterow1" size="20" 
							class="short_txt" maxlength="60" style="width: 70px;" />
							<input class="time_ico" type="button" onClick="showcalendar(event, 'pre_delivery_daterow1', false);" value="&nbsp;" />
							</td>
						<td id="row1col9"><input type="text" id="delivery_numberrow1"
							name="delivery_numberrow1" style="width:56px" readonly="readonly"
							style="background-color: #EEEEEE;" /><input type="button"
							onclick="deleteRowBy('row1')" value="删除" style="margin-left: 5px" /></td>
					</tr>
					<tbody id="newbody"></tbody>
				</table>
			</center>
			</br> </br> </br>
			<div id="sureVin">
				<b style="display: inline-block; float: left">已确定车架号</b>
				<hr style="display: inline-block;">
			</div>
			</br> </br>
			<center>
			<table border=0 align=center id="ConfirmVehicleTable" style="width:95%; background-color: #F0F7F2;">
				<tr style="height: 23px; font-weight: bold; color:#39669f;background-color: #DAE0EE;" id="yrow0">
					<td width="14%">VIN</td>
					<td width="10%">车型</td>
					<td width="8%">颜色</td>
					<td width="6%">价格(元)</td>
					<td width="5%">数量</td>
					<td width="6%">总金额(元)</td>
					<td width="6%">订金(元)</td>
					<td width="6%">定金(元)</td>
					<td width="10%">余款付款日期</td>
					<td width="10%">交车日期</td>
					<td width="9%">已交车数量<input type="button" onclick="yaddRow()" value="新增" /></td>
				</tr>
				<tr id="yrow1"
					style="height: 23px;background-color: white;border: 0; border-color:#44BBBB; ">
					<td id="yrow1col0"><input type="text" class="middle_txt"
						id="yvinIdyrow1" name="yvinIdyrow1" value=""
						style="display: inline-block; float: left;width: 110px;" readonly="readonly" />
						<input type="hidden" name="hiddenyvinIdyrow1" size="20"
						id="hiddenyvinIdyrow1" value="" /> <input type="button"
						value="..." class="mini_btn"
						onclick="delv('','','','yvinIdyrow1','ymaterialCodeyrow1','yrow1col2','hiddenymaterialCodeyrow1')"
						style="display: inline-block;" /></td>
					<td id="yrow1col1"><input type="text" class="middle_txt"
						id="ymaterialCodeyrow1" name="ymaterialCodeyrow1" value=""
						style="display: inline-block; float: left; background-color: #EEEEEE;width: 110px;" 
						readonly="readonly" />
						<input type='hidden' id='hiddenymaterialCodeyrow1' name='hiddenymaterialCodeyrow1'
						value='' /></td>
					<td id="yrow1col2"></td>
					<td id="yrow1col3"><input type="text" id="ypriceyrow1"
						name="ypriceyrow1" checkFlag="yjiage" style="width:60px"
						onblur="ycheckNum(this)" /></td>
					<td id="yrow1col4"><input type="text" id="ynumyrow1"
						name="ynumyrow1" checkFlag="yshuliang" style="width:40px"
						readonly="readonly" style="background-color: #EEEEEE;" value="1" /></td>
					<td id="yrow1col5"><input type="text" id="yamountyrow1"
						name="yamountyrow1" style="width:60px" readonly="readonly"
						style="background-color: #EEEEEE;" /></td>
					<td id="yrow1col6"><input type="text" id="ydeposityrow1"
						name="ydeposityrow1" checkFlag="ydingjin" style="width:50px"
						onblur="ycheckNum(this)" /></td>
					<td id="yrow1col10"><input type="text" id="yearnestyrow1"
						name="yearnestyrow1" checkFlag="ydingjin2" style="width:50px"
						onblur="ycheckNum(this)" /></td>
					<td id="yrow1col7"><input type="text" value=""
						name="ypre_pay_dateyrow1" id="ypre_pay_dateyrow1"
						group="ypre_pay_dateyrow1" class="short_txt"
						datatype="1,is_date,10" size="20" h 
						readonly="readonly" maxlength="60" style="width: 70px;"
						onPropertyChange="doChangeDate2('yrow1');" />
						<input class="time_ico" type="button" onClick="showcalendar(event, 'ypre_pay_dateyrow1', false);" value="&nbsp;" />
						</td>
					<td id="yrow1col8"><input type="text"
						name="ypre_delivery_dateyrow1" id="ypre_delivery_dateyrow1"
						group="ypre_delivery_dateyrow1" class="short_txt"
						datatype="1,is_date,10" size="20" 
						readonly="readonly" maxlength="60"  style="width: 70px;"/>
						<input class="time_ico" type="button" onClick="showcalendar(event, 'ypre_delivery_dateyrow1', false);" value="&nbsp;" />
						</td>
					<td id="yrow1col9"><input type="text"
						id="ydelivery_numberyrow1" name="ydelivery_numberyrow1"
						style="width:56px" readonly="readonly"
						style="background-color: #EEEEEE;" /><input type="button"
						onclick="ydeleteRowBy('yrow1')" value="删除"
						style="margin-left: 5px" /></td>
				</tr>
				<tbody id="ynewbody"></tbody>
			</table>
			</center>
			</br> </br>
			<%-- <table class="table_query" width="95%"  style="text-align: center;">
				<tr>
					<td colspan="3" >
						<!-- 此处只有顾问登陆时才有保存按钮 --> 
						<c:if test="${adviserLogon=='yes' }">
							<input name="queryBtn" type="button" class="normal_btn" id="baocun" onclick="doSave();" value="保存" />
						</c:if> 
						<input name="insertBtn" id="quxiao" type="button" class="normal_btn" id="quxiao" onclick="javascript:history.go(-1);" value="取消" />
					</td>
				</tr>
			</table> --%>
			<div  id="saveVin" style="background-color:  #F0F7F2;height: 40px;line-height: 40px;text-align: center;">
				<!-- 此处只有顾问登陆时才有保存按钮 --> 
						<c:if test="${adviserLogon=='yes' }">
							<input name="queryBtn" type="button" class="normal_btn" id="baocun" onclick="doSave();" value="保存" />
						</c:if> 
						<input name="insertBtn" id="quxiao" type="button" class="normal_btn" id="quxiao" onclick="javascript:history.go(-1);" value="取消" />
			</div>
			<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
			<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
		</form>
	</div>
	<script type="text/javascript"> 
	//意向车型
	function toIntentVechileList(textId,textName){
		OpenHtmlWindow("<%=request.getContextPath()%>/crm/customer/CustomerManage/toIntentVechileList.do?textId="+textId+"&textName="+textName,800,600);
	}
	
	//VIN选择页面
	function delv(vechile_id,qkId,qkOrderDetailId,inputId,moderId,colorTdId,hiddenmoderId){
		OpenHtmlWindow("<%=request.getContextPath()%>/crm/delivery/DelvManage/toVinList.do?inputId="+inputId+"&moderId="+moderId+"&colorTdId="+colorTdId+"&hiddenmoderId="+hiddenmoderId,800,600);
	}
	
	var pro = document.getElementById("dPro2").value;
	var city = document.getElementById("dCity2").value;
	var area = document.getElementById("dArea2").value;
	genLocSel('dPro','dCity','dArea',pro,city,area); // 加载省份城市和区县
</SCRIPT>

	<script type="text/javascript">	
	function yaddRow() {
		var nowDate=$("curDate").value;
		var table = document.getElementById("ConfirmVehicleTable");
		var length=document.getElementById("ConfirmVehicleTable").rows.length;
		var s = table.rows[table.rows.length-1].id.charAt(table.rows[table.rows.length-1].id.length - 1);
		var rownum = parseInt(length);
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
		td.innerHTML = "<input type='text' class='middle_txt' id='yvinId"+tr.id+"' name='yvinId"+tr.id+"'  value='' style='display: inline-block; float: left;width: 110px;'   readonly='readonly'/><input type='hidden' name='hiddenyvinId"+tr.id+"' size='20' id='hiddenyvinId"+tr.id+"' value='' /><input type='button' value='...' class='mini_btn'  onclick=delv('','','','yvinId"+tr.id+"','ymaterialCode"+tr.id+"','"+colorTdId+"','hiddenymaterialCode"+tr.id+"') style='display: inline-block;'/>";
		td.id=tr.id+"col"+1;
		tr.appendChild(td);
		//第一列（车系）
		var td=document.createElement("td");
		td.innerHTML = "<input type='text' class='middle_txt' id='ymaterialCode"+tr.id+"' name='ymaterialCode"+tr.id+"'  value='' style='display: inline-block; float: left;width: 110px; background-color: #EEEEEE;'   readonly='readonly'/><input type='hidden' id='hiddenymaterialCode"+tr.id+"' name='hiddenymaterialCode"+tr.id+"' value='' />";
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
		td.innerHTML = "<input type='text' id='ynum"+tr.id+"' name='ynum"+tr.id+"' checkFlag='yshuliang' readonly='readonly' style='background-color: #EEEEEE;width:40px;' value='1'/>";
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
		td.id=tr.id+"col"+10;
		tr.appendChild(td);
		//第七列（余款付款日期）
		var td=document.createElement("td");
		td.innerHTML = "<input type=\"text\" value=\"\" onPropertyChange=\"doChangeDate2('"+tr.id+"');\" name=\"ypre_pay_date"+tr.id+"\" id=\"ypre_pay_date"+tr.id+"\" group=\"ypre_pay_date"+tr.id+"\" class=\"short_txt\" datatype=\"1,is_date,10\" size=\"20\"  readonly=\"readonly\" maxlength=\"60\" />&nbsp;<input class=\"time_ico\" type=\"button\" onClick=\"showcalendar(event, 'ypre_pay_date"+tr.id+"', false);\" value=\"&nbsp;\"/>";
		td.id=tr.id+"col"+7;
		tr.appendChild(td);
		//第八列（交车日期）
		var td=document.createElement("td");
		td.innerHTML = "<input type=\"text\" value=\""+nowDate+"\" name=\"ypre_delivery_date"+tr.id+"\" id=\"ypre_delivery_date"+tr.id+"\" group=\"ypre_delivery_date"+tr.id+"\" class=\"short_txt\" datatype=\"1,is_date,10\" size=\"20\"  readonly=\"readonly\" maxlength=\"60\"/>&nbsp;<input class=\"time_ico\" type=\"button\" onClick=\"showcalendar(event, 'ypre_delivery_date"+tr.id+"', false);\" value=\"&nbsp;\"/>";
		td.id=tr.id+"col"+8;
		tr.appendChild(td);
		//第九列（已交车数量）
		var td=document.createElement("td");
		td.innerHTML = "<input type='text' id='ydelivery_number"+tr.id+"' name='ydelivery_number"+tr.id+"' style='width:56px' readonly='readonly' style='background-color: #EEEEEE;'/><input type='button' onclick=ydeleteRowBy('"+tr.id+"') value='删除' style='margin-left: 5px' />";
		td.id=tr.id+"col"+9;
		tr.appendChild(td);
		document.getElementById("ynewbody").appendChild (tr);
	}

	function ydoFindColor(inputId,nextTdId) {
		var url = "<%=contextPath%>/crm/taskmanage/TaskManage/colorInfoBySeries.json?inputId="+inputId;
		makeFormCall(url, showInfo, "fm") ;
		function showInfo(json) {
			if(json.ps[0]!=null) {
				var colortd=document.getElementById(nextTdId);
				colortd.innerHTML = "";
				var str = "<select ";
				for(var i=0;i<json.psSize;i++) {
					str = str + "id='ytheColor"+nextTdId+"'>";
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
		var str=obj.id.substring(obj.id.length-2,obj.id.length);
		if(str>=10){
			rownum=str;
			}
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
	
	function doChangeDate(row){
		var payDateId = "pre_pay_date"+row;
		var deliveryDateId = "pre_delivery_date"+row;
		var date = document.getElementById(payDateId).value;
		document.getElementById(deliveryDateId).value = date;
	}
	function doChangeDate2(row){
		var payDateId = "ypre_pay_date"+row;
		var deliveryDateId = "ypre_delivery_date"+row;
		var date = document.getElementById(payDateId).value;
		document.getElementById(deliveryDateId).value = date;
	}

		/**  弹出框设置   **/
	function followOpenInfo(customerId){
		var context=null;
		var openUrl = "<%=request.getContextPath()%>/crm/customer/CustomerManage/ctmUpdateInit.do?ctmId="+customerId;
		var parameters="fullscreen=yes, toolbar=yes, menubar=no, scrollbars=no, resizable=yes, location=no, status=yes";
		window.open (openUrl, "newwindow");
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
			document.getElementById("new_product_sale").options[1].selected=true;
			}else if(json.ifpresell=="10041001"){
			document.getElementById("new_product_sale").options[0].selected=true;
				}
		}
		
	}

	var dd1 = new Date(); 
	var data= dd1.Format("yyyy-M-d");
	function changeEvent(){
		var inbound=document.getElementById("intent_typeh").innerHTML.split("<")[0];
		var nextDate=document.getElementById("next_follow_date2");
		if(inbound == "A"){
			var dd = new Date();
		    	dd.setDate(dd.getDate()+6);
			nextDate.addEventListener("click",showcalendar(event, 'next_follow_date', false,data,dd.Format("yyyy-M-d")));
		}
		if(inbound == "B"){
			var dd = new Date();
	    	dd.setDate(dd.getDate()+14);
		nextDate.addEventListener("click",showcalendar(event, 'next_follow_date', false,data,dd.Format("yyyy-M-d")));
		}
		if(inbound == "C"){
			var dd = new Date();
	    	dd.setDate(dd.getDate()+29);
		nextDate.addEventListener("click",showcalendar(event, 'next_follow_date', false,data,dd.Format("yyyy-M-d")));
		}
		 if(inbound == "H"){
			 var dd = new Date();
		    	dd.setDate(dd.getDate()+2);
			nextDate.addEventListener("click",showcalendar(event, 'next_follow_date', false,data,dd.Format("yyyy-M-d")));
		}
		nextDate.addEventListener("click",showcalendar(event, 'next_follow_date', false,data));
	}
	function changeEvent1(){
		var inbound=document.getElementById("intent_typey").innerHTML.split("<")[0];
		var nextDate=document.getElementById("plan_invite_date2");
		if(inbound == "A"){
			var dd = new Date();
		    	dd.setDate(dd.getDate()+6);
			nextDate.addEventListener("click",showcalendar(event, 'plan_invite_date', false,data,dd.Format("yyyy-M-d")));
		}
		if(inbound == "B"){
			var dd = new Date();
	    	dd.setDate(dd.getDate()+14);
		nextDate.addEventListener("click",showcalendar(event, 'plan_invite_date', false,data,dd.Format("yyyy-M-d")));
		}
		if(inbound == "C"){
			var dd = new Date();
	    	dd.setDate(dd.getDate()+29);
		nextDate.addEventListener("click",showcalendar(event, 'plan_invite_date', false,data,dd.Format("yyyy-M-d")));
		}
		 if(inbound == "H"){
			 var dd = new Date();
		    	dd.setDate(dd.getDate()+2);
			nextDate.addEventListener("click",showcalendar(event, 'plan_invite_date', false,data,dd.Format("yyyy-M-d")));
		}
		nextDate.addEventListener("click",showcalendar(event, 'plan_invite_date', false,data));
	}

	
</script>
	<script type="text/javascript">ddlevelsmenu.setup("ddtopmenubar29", "topbar")</script>
	<script type="text/javascript">ddlevelsmenu.setup("ddtopmenubar28", "topbar")</script>
	<script type="text/javascript">ddlevelsmenu.setup("ddtopmenubar27", "topbar")</script>
	<script type="text/javascript">ddlevelsmenu.setup("ddtopmenubar26", "topbar")</script>
	<script type="text/javascript">ddlevelsmenu.setup("ddtopmenubar25", "topbar")</script>
	<script type="text/javascript">ddlevelsmenu.setup("ddtopmenubar24", "topbar")</script>
	<script type="text/javascript">ddlevelsmenu.setup("ddtopmenubar23", "topbar")</script>
	<script type="text/javascript">ddlevelsmenu.setup("ddtopmenubar22", "topbar")</script>
</body>
</html>