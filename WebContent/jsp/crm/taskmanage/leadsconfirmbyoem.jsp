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
	}
	function txtClr(valueId) {
		document.getElementById(valueId).value = '' ;
	}
	function doTurnTask(taskType,customerId,taskId) {
		if(taskType=="跟进任务") {
			$('fm').action = "<%=contextPath%>/crm/taskmanage/TaskManage/doTaskFollowInit.do?customerId="+customerId+"&taskId="+taskId;
			$('fm').submit();
		} else if(taskType=="计划邀约任务") {
			$('fm').action = "<%=contextPath%>/crm/taskmanage/TaskManage/doTaskInviteInit.do?customerId="+customerId+"&taskId="+taskId;
			$('fm').submit();
		} else if(taskType=="邀约到店任务") {
			$('fm').action = "<%=contextPath%>/crm/taskmanage/TaskManage/doTaskInviteInit.do?customerId="+customerId+"&inviteShopId="+taskId;
			$('fm').submit();
		} else if(taskType=="订单任务") {
			$('fm').action = "<%=contextPath%>/crm/taskmanage/TaskManage/doTaskOrderInit.do?customerId="+customerId+"&taskId="+taskId;
			$('fm').submit();
		} else if(taskType=="退单任务") {
			$('fm').action = "<%=contextPath%>/crm/taskmanage/TaskManage/doTaskOrderBackInit.do?customerId="+customerId+"&taskId="+taskId;
			$('fm').submit();
		} else if(taskType=="交车任务") {
			$('fm').action = "<%=contextPath%>/crm/taskmanage/TaskManage/doTaskDeliveryInit.do?customerId="+customerId+"&taskId="+taskId;
			$('fm').submit();
		} else if(taskType=="回访任务") {
			$('fm').action = "<%=contextPath%>/crm/revisit/RevisitManage/detailRevisit.do?typeFrom=taskManage&customerId="+customerId+"&revisitId="+taskId;
			$('fm').submit();
		}
	}
	function nameBlurChange(){
		var customerName = document.getElementById("customer_name");
		var customerNameV = customerName.value;
		var telePhone = document.getElementById("telephone");
		var telePhoneV = telePhone.value;
		var jc_way = document.getElementById("collect_fashion");
		var customer_type = document.getElementById("customer_type");
		var intentVehicleA = document.getElementById("intentVehicleA");
		var intentVehicleB = document.getElementById("intentVehicleB");
		var intent_type = document.getElementById("intent_type");
		var dPro = document.getElementById("dPro");
		var dCity = document.getElementById("dCity");
		var dArea = document.getElementById("dArea");
		var sales_progress = document.getElementById("sales_progress");
		var buy_type = document.getElementById("buy_type");
		var followTable = document.getElementById("follow_table");
		var inviteTable = document.getElementById("invite_table");
		var orderTable = document.getElementById("order_table");
		var defeatTable = document.getElementById("defeat_table");
		var groupRadio = document.getElementById("groupRadio");
		var followRadio = document.getElementById("follow_radio");
		var inviteRadio = document.getElementById("invite_radio");
		var orderRadio = document.getElementById("order_radio");
		var defeatRadio = document.getElementById("defeat_radio");
		var failureRadio = document.getElementById("failure_radio");
		var nextDiv = document.getElementById("nextDiv");
		var saveButton = document.getElementById("saveButton");

		var intentCar= document.getElementById("intentcar").value;
		for(var i=0;i<intentVehicleA.options.length;i++) {
			 if(intentVehicleA.options[i].value==intentCar){  
				 intentVehicleA.options[i].selected=true;  
                   break;  
               }  
		}
		toChangeMenu(intentVehicleA,'intentVehicleB');

		//if(customerName.value==null||customerName.value=="") {
			//MyAlert("请输入客户姓名!");
			//return false;
		// } else 
		if(telePhone.value==null||telePhone.value=="") {
			//MyAlert("请输入联系电话!");
			//return false;
		} else {
			var url = "<%=contextPath%>/crm/taskmanage/TaskManage/customerInfoQuery.json";
			makeCall(url, showInfo,{customerName:customerNameV,telePhone:telePhoneV}) ;
			function showInfo(json) {
				//判断是否已有其他顾问建档信息
				if(json.ps3[0]!=null) {
					//回填数据
					if(json.ps3[0].CUSTOMER_NAME!=null) {
						followTable.style.display = "none";
						inviteTable.style.display = "none";
						orderTable.style.display = "none";
						defeatTable.style.display = "none";
						nextDiv.style.display = "none";
						saveButton.style.display = "none";
						followRadio.checked = false;
						inviteRadio.checked = false;
						defeatRadio.checked = false;
						failureRadio.checked = false;
						orderRadio.checked = false;
						groupRadio.style.display = "none";
						MyAlert("该客户已有其他顾问建档信息,不能进行操作,请联系经理!");
					}
					customerNameV = json.ps3[0].CUSTOMER_NAME;
					telePhoneV = json.ps3[0].TELEPHONE;
					document.getElementById("buy_budget").value = json.ps3[0].BUY_BUDGET;
					if(json.ps3[0].JC_WAY2!=null&&json.ps3[0].JC_WAY2!='') {
						var jc_wayh = json.ps3[0].JC_WAY2;
					} else {
						var jc_wayh = "--请选择--";
					}
					var customer_typeh = json.ps3[0].CTM_PROP2;
					var if_driveh = json.ps3[0].IF_DRIVE2;
					var come_reasonh = json.ps3[0].COME_REASON2;
					var buy_typeh = json.ps3[0].BUY_TYPE2;
					var ctm_rankh = json.ps3[0].CTM_RANK2;
					var sales_progressh = json.ps3[0].SALES_PROGRESS2;
					var buy_budgeth = json.ps3[0].BUY_BUDGET2;
					//document.getElementById("jc_wayh").innerHTML=jc_wayh;
					document.getElementById("customer_typeh").innerHTML=customer_typeh;
					document.getElementById("test_drivingh").innerHTML=if_driveh;
					document.getElementById("come_reasonh").innerHTML=come_reasonh;
					document.getElementById("buy_typeh").innerHTML=buy_typeh;
					document.getElementById("intent_typeh").innerHTML=ctm_rankh;
					document.getElementById("sales_progressh").innerHTML=sales_progressh;
					document.getElementById("buy_budgeth").innerHTML=buy_budgeth;
					
					for(var i=0;i<intentVehicleA.options.length;i++) {
						 if(intentVehicleA.options[i].value==json.ps3[0].UP_SERIES_ID){  
							 intentVehicleA.options[i].selected=true;  
		                        break;  
		                    }  
					}
					toChangeMenu(intentVehicleA,'intentVehicleB');
					for(var i=0;i<intentVehicleB.options.length;i++) {
						 if(intentVehicleB.options[i].value==json.ps3[0].INTENT_VEHICLE){  
							 intentVehicleB.options[i].selected=true;  
		                        break;  
		                    }  
					}
				}
				if(json.ps4[0]!=null) {
		
					if(json.ps4[0].CUSTOMER_NAME!=null) {
						var flag = json.ps4[0].CTM_TYPE2;
						var r=confirm("该客户为 "+flag+" 客户,点击〖确定〗键显示重启重购任务，点击〖取消〗键显示系统已存在该客户档案，可继续操作已修改档案信息!");
						  if (r==true)
						    {
							  document.getElementById("customer_name").readOnly = true;
								document.getElementById("telephone").readOnly = true;
								document.getElementById("customer_name").value = json.ps4[0].CUSTOMER_NAME;
								customerNameV = json.ps4[0].CUSTOMER_NAME;
								telePhoneV = json.ps4[0].TELEPHONE;
								
								document.getElementById("buy_budget").value = json.ps4[0].BUY_BUDGET;
								//if(json.ps4[0].JC_WAY2!=null&&json.ps4[0].JC_WAY2!='') {
								//	var jc_wayh = json.ps4[0].JC_WAY2;
								//} else {
								//	var jc_wayh = "--请选择--";
								//}
								var customer_typeh = json.ps4[0].CTM_PROP2;
								var if_driveh = json.ps4[0].IF_DRIVE2;
								var come_reasonh = json.ps4[0].COME_REASON2;
								var buy_typeh = json.ps4[0].BUY_TYPE2;
								var ctm_rankh = json.ps4[0].CTM_RANK2;
								var sales_progressh = json.ps4[0].SALES_PROGRESS2;
								var buy_budgeth = json.ps4[0].BUY_BUDGET2;
								
								document.getElementById("customer_typeh").innerHTML=customer_typeh;
								document.getElementById("intent_typeh").innerHTML=ctm_rankh;
								document.getElementById("sales_progressh").innerHTML=sales_progressh;
								document.getElementById("buy_budgeth").innerHTML=buy_budgeth;
								document.getElementById("customer_type").value= json.ps4[0].CTM_PROP;
								//if(json.ps4[0].JC_WAY!=null&&json.ps4[0].JC_WAY!='') {
								//	document.getElementById("collect_fashion").value= json.ps4[0].JC_WAY;
								//} else {
								//	document.getElementById("collect_fashion").value= '';
								//}
								document.getElementById("intent_type").value= json.ps4[0].CTM_RANK;
								document.getElementById("sales_progress").value= json.ps4[0].SALES_PROGRESS;
								document.getElementById("customer_name").readOnly = true;
								document.getElementById("telephone").readOnly = true;

								
								
								for(var i=0;i<intentVehicleA.options.length;i++) {
									 if(intentVehicleA.options[i].value==json.ps4[0].UP_SERIES_ID){  
										 intentVehicleA.options[i].selected=true;  
						                    break;  
						                }  
								}
								toChangeMenu(intentVehicleA,'intentVehicleB');
								for(var i=0;i<intentVehicleB.options.length;i++) {
									 if(intentVehicleB.options[i].value==json.ps4[0].INTENT_VEHICLE){  
										 intentVehicleB.options[i].selected=true;  
						                    break;  
						                }  
								}
								document.getElementById("provinceId").value = json.ps4[0].PROVICE_ID;
								document.getElementById("cityId").value = json.ps4[0].CITY_ID;
								document.getElementById("townId").value = json.ps4[0].TOWN_ID;
								addRegionInit();
						    }
						  else
						    {
							  MyAlert("点击〖保存〗则该客户需求信息修改完善，系统自动合并重复客户信息；点击〖无效〗则视为本条信息人工录入错误导致线索无效，系统不会合并重复客户信息。");
							  followTable.style.display = "none";
								inviteTable.style.display = "none";
								defeatTable.style.display = "none";
								orderTable.style.display = "none";
								nextDiv.style.display = "none";
								followRadio.checked = false;
								inviteRadio.checked = false;
								defeatRadio.checked = false;
								failureRadio.checked = false;
								orderRadio.checked = false;
								groupRadio.style.display = "none";
								customerNameV = json.ps4[0].CUSTOMER_NAME;
								telePhoneV = json.ps4[0].TELEPHONE;
								
								document.getElementById("buy_budget").value = json.ps4[0].BUY_BUDGET;
								//if(json.ps4[0].JC_WAY2!=null&&json.ps4[0].JC_WAY2!='') {
								//	var jc_wayh = json.ps4[0].JC_WAY2;
								//} else {
								//	var jc_wayh = '--请选择--';
								//}
								var customer_typeh = json.ps4[0].CTM_PROP2;
								var if_driveh = json.ps4[0].IF_DRIVE2;
								var come_reasonh = json.ps4[0].COME_REASON2;
								var buy_typeh = json.ps4[0].BUY_TYPE2;
								var ctm_rankh = json.ps4[0].CTM_RANK2;
								var sales_progressh = json.ps4[0].SALES_PROGRESS2;
								var buy_budgeth = json.ps4[0].BUY_BUDGET2;
								
								document.getElementById("customer_typeh").innerHTML=customer_typeh;
								document.getElementById("intent_typeh").innerHTML=ctm_rankh;
								document.getElementById("sales_progressh").innerHTML=sales_progressh;
								document.getElementById("buy_budgeth").innerHTML=buy_budgeth;
								document.getElementById("customer_type").value= json.ps4[0].CTM_PROP;
								//if(json.ps4[0].JC_WAY!=null&&json.ps4[0].JC_WAY!='') {
								//	document.getElementById("collect_fashion").value= json.ps4[0].JC_WAY;
								//} else {
								//	document.getElementById("collect_fashion").value= '';
								//}
								document.getElementById("intent_type").value= json.ps4[0].CTM_RANK;
								document.getElementById("sales_progress").value= json.ps4[0].SALES_PROGRESS;
								document.getElementById("customer_name").readOnly = true;
								document.getElementById("telephone").readOnly = true;
								
								for(var i=0;i<intentVehicleA.options.length;i++) {
									 if(intentVehicleA.options[i].value==json.ps4[0].UP_SERIES_ID){  
										 intentVehicleA.options[i].selected=true;  
						                    break;  
						                }  
								}
								toChangeMenu(intentVehicleA,'intentVehicleB');
								for(var i=0;i<intentVehicleB.options.length;i++) {
									 if(intentVehicleB.options[i].value==json.ps4[0].INTENT_VEHICLE){  
										 intentVehicleB.options[i].selected=true;  
						                    break;  
						                }  
								}
								document.getElementById("provinceId").value = json.ps4[0].PROVICE_ID;
								document.getElementById("cityId").value = json.ps4[0].CITY_ID;
								document.getElementById("townId").value = json.ps[0].TOWN_ID;
								addRegionInit();
								
								//未完成任务展示
								if(json.ps2[0]!=null) {
									var unDoTask = document.getElementById("unDoTask");
									var taskType = document.getElementById("taskType");
									var ctmName = document.getElementById("ctmName");
									var ctmPhone = document.getElementById("ctmPhone");
									//var caozuo = document.getElementById("caozuo");
									unDoTask.style.display = "block";
									taskType.innerHTML=""+json.ps2[0].TASK_TYPE+"";
									ctmName.innerHTML=""+json.ps2[0].CUSTOMER_NAME+"";
									ctmPhone.innerHTML=""+json.ps2[0].TELEPHONE+"";
									//caozuo.innerHTML="<a href='#' onclick=\"doTurnTask('"+json.ps2[0].TASK_TYPE+"','"+json.ps2[0].CUSTOMER_ID+"','"+json.ps2[0].TASK_ID+"')\">跳转任务页面</a>";
								} else {
									return false;
								}
						    }
					}
				}
				if(json.ps[0]!=null) {
					//回填数据
					if(json.ps[0].CUSTOMER_NAME!=null) {
						followTable.style.display = "none";
						inviteTable.style.display = "none";
						defeatTable.style.display = "none";
						orderTable.style.display = "none";
						nextDiv.style.display = "none";
						followRadio.checked = false;
						inviteRadio.checked = false;
						defeatRadio.checked = false;
						failureRadio.checked = false;
						orderRadio.checked = false;
						groupRadio.style.display = "none";
						MyAlert("系统已存在该客户档案,可继续操作已修改档案信息!");
					}
					customerNameV = json.ps[0].CUSTOMER_NAME;
					telePhoneV = json.ps[0].TELEPHONE;
					
					document.getElementById("buy_budget").value = json.ps[0].BUY_BUDGET;
					//if(json.ps[0].JC_WAY2!=null&&json.ps[0].JC_WAY2!=''){
					//	var jc_wayh = json.ps[0].JC_WAY2;
					//} else {
					//	var jc_wayh = '--请选择--';
					//}
					var customer_typeh = json.ps[0].CTM_PROP2;
					var if_driveh = json.ps[0].IF_DRIVE2;
					var come_reasonh = json.ps[0].COME_REASON2;
					var buy_typeh = json.ps[0].BUY_TYPE2;
					var ctm_rankh = json.ps[0].CTM_RANK2;
					var sales_progressh = json.ps[0].SALES_PROGRESS2;
					var buy_budgeth = json.ps[0].BUY_BUDGET2;
					
					document.getElementById("customer_typeh").innerHTML=customer_typeh;
					document.getElementById("intent_typeh").innerHTML=ctm_rankh;
					document.getElementById("sales_progressh").innerHTML=sales_progressh;
					document.getElementById("buy_budgeth").innerHTML=buy_budgeth;
					document.getElementById("customer_type").value= json.ps[0].CTM_PROP;
					//if(json.ps[0].JC_WAY!=null&&json.ps[0].JC_WAY!='') {
					//	document.getElementById("collect_fashion").value= json.ps[0].JC_WAY;
					//} else {
					//	document.getElementById("collect_fashion").value= '';
					//}
					
					document.getElementById("intent_type").value= json.ps[0].CTM_RANK;
					document.getElementById("sales_progress").value= json.ps[0].SALES_PROGRESS;
					document.getElementById("customer_name").value=customerNameV;
					document.getElementById("customer_name").readOnly = true;
					document.getElementById("telephone").readOnly = true;
					
					for(var i=0;i<intentVehicleA.options.length;i++) {
						 if(intentVehicleA.options[i].value==json.ps[0].UP_SERIES_ID){  
							 intentVehicleA.options[i].selected=true;  
			                    break;  
			                }  
					}
					toChangeMenu(intentVehicleA,'intentVehicleB');
					for(var i=0;i<intentVehicleB.options.length;i++) {
						 if(intentVehicleB.options[i].value==json.ps[0].INTENT_VEHICLE){  
							 intentVehicleB.options[i].selected=true;  
			                    break;  
			                }  
					}
					document.getElementById("provinceId").value = json.ps[0].PROVICE_ID;
					document.getElementById("cityId").value = json.ps[0].CITY_ID;
					document.getElementById("townId").value = json.ps[0].TOWN_ID;
					addRegionInit();
					
					//未完成任务展示
					if(json.ps2[0]!=null) {
						var unDoTask = document.getElementById("unDoTask");
						var taskType = document.getElementById("taskType");
						var ctmName = document.getElementById("ctmName");
						var ctmPhone = document.getElementById("ctmPhone");
						//var caozuo = document.getElementById("caozuo");
						unDoTask.style.display = "block";
						taskType.innerHTML=""+json.ps2[0].TASK_TYPE+"";
						ctmName.innerHTML=""+json.ps2[0].CUSTOMER_NAME+"";
						ctmPhone.innerHTML=""+json.ps2[0].TELEPHONE+"";
						//caozuo.innerHTML="<a href='#' onclick=\"doTurnTask('"+json.ps2[0].TASK_TYPE+"','"+json.ps2[0].CUSTOMER_ID+"','"+json.ps2[0].TASK_ID+"')\">跳转任务页面</a>";
					} else {
						return false;
					}
				} else {
					return false;
				}
			}
		}
	}
	
	function followClick(){
		var followTable = document.getElementById("follow_table");
		var inviteTable = document.getElementById("invite_table");
		var orderTable = document.getElementById("order_table");
		var defeatTable = document.getElementById("defeat_table");
		var intentType = document.getElementById("intent_type");
		var intentTypeh = document.getElementById("intent_typeh");
		intentType.value = '';
		intentTypeh.innerHTML = "--请选择--";
		followTable.style.display = "block";
		inviteTable.style.display = "none";
		orderTable.style.display = "none";
		defeatTable.style.display = "none";
		intentTypeh.setAttribute("notselected","60101005,60101006,60101007");
		intentTypeh.setAttribute("isload",false);
	}
	function inviteClick(){
		var followTable = document.getElementById("follow_table");
		var inviteTable = document.getElementById("invite_table");
		var orderTable = document.getElementById("order_table");
		var defeatTable = document.getElementById("defeat_table");
		var intentType = document.getElementById("intent_type");
		var intentTypeh = document.getElementById("intent_typeh");
		intentType.value = '';
		intentTypeh.innerHTML = "--请选择--";
		followTable.style.display = "none";
		inviteTable.style.display = "block";
		orderTable.style.display = "none";
		defeatTable.style.display = "none";
		intentTypeh.setAttribute("notselected","60101005,60101006,60101007");
		intentTypeh.setAttribute("isload",false);
	}
	function orderClick(){
		var followTable = document.getElementById("follow_table");
		var inviteTable = document.getElementById("invite_table");
		var defeatTable = document.getElementById("defeat_table");
		var orderTable = document.getElementById("order_table");
		var intentType = document.getElementById("intent_type");
		var intentTypeh = document.getElementById("intent_typeh");
		intentType.value = '60101005';
		intentTypeh.innerHTML = "O";
		followTable.style.display = "none";
		inviteTable.style.display = "none";
		defeatTable.style.display = "none";
		orderTable.style.display = "block";
		intentTypeh.setAttribute("notselected","60101004,60101006,60101007,60101001,60101002,60101003");
		intentTypeh.setAttribute("isload",false);
	}
	function defeatClick(){
		var followTable = document.getElementById("follow_table");
		var inviteTable = document.getElementById("invite_table");
		var orderTable = document.getElementById("order_table");
		var defeatTable = document.getElementById("defeat_table");
		var intentType = document.getElementById("intent_type");
		var intentTypeh = document.getElementById("intent_typeh");
		intentType.value = '60101006';
		intentTypeh.innerHTML = "E";
		followTable.style.display = "none";
		inviteTable.style.display = "none";
		orderTable.style.display = "none";
		defeatTable.style.display = "block";
		intentTypeh.setAttribute("notselected","60101004,60101005,60101007,60101001,60101002,60101003");
		intentTypeh.setAttribute("isload",false);
	}
	function failureClick(){
		var followTable = document.getElementById("follow_table");
		var inviteTable = document.getElementById("invite_table");
		var orderTable = document.getElementById("order_table");
		var defeatTable = document.getElementById("defeat_table");
		var intentType = document.getElementById("intent_type");
		var intentTypeh = document.getElementById("intent_typeh");
		intentType.value = '60101007';
		intentTypeh.innerHTML = "L";
		followTable.style.display = "none";
		inviteTable.style.display = "none";
		orderTable.style.display = "none";
		defeatTable.style.display = "none";
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
	function doSave(){
		var customerName = document.getElementById("customer_name").value;
		var telePhone = document.getElementById("telephone").value;
		//var collectFashion = document.getElementById("collect_fashion").value;
		var buyBudget = document.getElementById("buy_budget");
		var customerType = document.getElementById("customer_type").value;
		var intentVehicle = document.getElementById("intentVehicleB").value;
		document.getElementById("intentVehicle").value = intentVehicle;
		var intentType = document.getElementById("intent_type").value;
		var sales_progress = document.getElementById("sales_progress").value;
		var followRadio = document.getElementById("follow_radio");
		var nextFollowDate = document.getElementById("next_follow_date").value;
		var followType = document.getElementById("follow_type").value;
		var inviteRadio = document.getElementById("invite_radio");
		var xqfx = document.getElementById("xqfx").value;
		var yymb = document.getElementById("yymb").value;
		var ydkhxrsj = document.getElementById("ydkhxrsj").value;
		var gdkhqjsj = document.getElementById("gdkhqjsj").value;
		var planInviteDate = document.getElementById("plan_invite_date").value;
		var planMeetDate = document.getElementById("plan_meet_date").value;
		var inviteType = document.getElementById("invite_type").value;
		var orderRadio = document.getElementById("order_radio");
		var orderDate = document.getElementById("order_date").value;
		var defeatRadio = document.getElementById("defeat_radio");
		var defeatVehicle = document.getElementById("defeatVehicleB").value;
		var defeatReason = document.getElementById("defeatReasonB").value;
		var failureRadio = document.getElementById("failure_radio");
		var intentCar= document.getElementById("intentcar").value;
		var reg = new RegExp("^[0-9]*$");
		var time = new Date().Format("yyyy-MM-dd");
		function getZ(n){    return /^\d+(\.\d+)?$/.test(n+"");}
		if(!reg.test(telePhone)){
	         MyAlert("联系电话格式不正确!");
	         return false;
	    }
		if(telePhone.length != 11 )
	    {
	        MyAlert('请输入有效的联系电话！');
	        return false;
	    } 
		//if(collectFashion ==null||collectFashion=="" &&defeatRadio.checked != true&&failureRadio.checked != true) {
		//	 MyAlert("请选择集客方式!");
	     //    return false;
		//}
		
		if(intentCar==null || intentCar==""){
			}else{
		if(intentVehicle==null || intentVehicle==""){
			MyAlert("意向车型不能为空!请选择!");
			return false;
			}
		}
		
		if((buyBudget.value==null||buyBudget.value=="")&&defeatRadio.checked != true&&failureRadio.checked != true){
	         MyAlert("购车预算不能为空!");
	         return false;
	    }
		if(customerName ==null||customerName=="") {
			 MyAlert("客户姓名不能为空!");
	         return false;
		}
		if(telePhone ==null||telePhone=="") {
			 MyAlert("联系电话不能为空!");
	         return false;
		}
		if((customerType ==null||customerType=="")&&defeatRadio.checked != true&&failureRadio.checked != true) {
			 MyAlert("请选择客户类型!");
	         return false;
		}
		if(intentType ==null||intentType=="") {
			 MyAlert("请选择意向等级!");
	         return false;
		}
		if((sales_progress ==null||sales_progress=="")&&defeatRadio.checked != true&&failureRadio.checked != true) {
			 MyAlert("请选择销售流程进度!");
	         return false;
		}
		if((intentVehicle ==null||intentVehicle=="")&&defeatRadio.checked != true&&failureRadio.checked != true) {
			 MyAlert("请选择意向车型!");
	         return false;
		} if(followRadio.checked==false && inviteRadio.checked==false && orderRadio.checked==false && defeatRadio.checked==false && failureRadio.checked==false) {
			var leadsCode = document.getElementById("leadsCode").value;
			var leadsAllotId = document.getElementById("leadsAllotId").value;
			document.getElementById("saveButton").disabled = true;
			document.getElementById("invalidButton").disabled = true;
			document.getElementById("insertBtn").disabled = true;
			
			$('fm').action = "<%=contextPath%>/crm/taskmanage/TaskManage/leadsConfirmByOem.do?typeFlag=''&leadsCode="+leadsCode+"&leadsAllotId="+leadsAllotId;
			$('fm').submit();
		} else if(followRadio.checked == true) {
			var dd = new Date();
			var nextDate=parseDate(nextFollowDate);
		    var inbound=document.getElementById("intent_typeh").innerHTML.split("<")[0];
		   // var jc_wayh=document.getElementById("jc_wayh").innerHTML.split("<")[0];
		   var jc_wayh="";
			if(jc_wayh.indexOf("首次") >= 0){
					var da = new Date();
			    	da.setDate(da.getDate()+1);
					if(nextDate > da ){
						MyAlert("首次客户下次跟进时间在2天内");
						return false;
					}	
			} else {
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
				var leadsCode = document.getElementById("leadsCode").value;
				var leadsAllotId = document.getElementById("leadsAllotId").value;
				document.getElementById("saveButton").disabled = true;
				document.getElementById("invalidButton").disabled = true;
				document.getElementById("insertBtn").disabled = true;
				
				$('fm').action = "<%=contextPath%>/crm/taskmanage/TaskManage/leadsConfirmByOem.do?typeFlag=followRadio&leadsCode="+leadsCode+"&leadsAllotId="+leadsAllotId;
				$('fm').submit();
			}
		} else if(inviteRadio.checked == true) {
			var dd = new Date();
			var planIDate=parseDate(planInviteDate);
			var planMDate=parseDate(planMeetDate);
			var inbound=document.getElementById("intent_typeh").innerHTML.split("<")[0];
			// var jc_wayh=document.getElementById("jc_wayh").innerHTML.split("<")[0];
			var jc_wayh="";
			if(jc_wayh.indexOf("首次") >= 0){
					var da = new Date();
			    	da.setDate(da.getDate()+1);
					if(planIDate > da ){
						MyAlert("首次客户下次邀约时间在2天内");
						return false;
					}	
			} else {
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
			}
			if(planInviteDate==null||planInviteDate=="") {
				MyAlert("请选择计划邀约时间");
				return false;
			}
			if(planMeetDate==null||planMeetDate=="") {
				MyAlert("请选择计划见面时间！");
				return false;
			}
			if(inviteType==null||inviteType=="") {
				MyAlert("请选择邀约方式！");
				return false;
			}
			if(planIDate < dd.setDate(dd.getDate()-1)){
				MyAlert("邀约时间要大于当前时间！");
				return false;
			}
			if(planMDate < planIDate){
				MyAlert("计划见面时间小于邀约时间！");
				return false;
			}
			var leadsCode = document.getElementById("leadsCode").value;
			var leadsAllotId = document.getElementById("leadsAllotId").value;
			document.getElementById("saveButton").disabled = true;
			document.getElementById("invalidButton").disabled = true;
			document.getElementById("insertBtn").disabled = true;
			
			$('fm').action = "<%=contextPath%>/crm/taskmanage/TaskManage/leadsConfirmByOem.do?typeFlag=inviteRadio&leadsCode="+leadsCode+"&leadsAllotId="+leadsAllotId;
			$('fm').submit();
		} else if(orderRadio.checked == true) {
			var leadsCode = document.getElementById("leadsCode").value;
			var leadsAllotId = document.getElementById("leadsAllotId").value;
			document.getElementById("saveButton").disabled = true;
			document.getElementById("invalidButton").disabled = true;
			document.getElementById("insertBtn").disabled = true;
			
			$('fm').action = "<%=contextPath%>/crm/taskmanage/TaskManage/leadsConfirmByOem.do?typeFlag=orderRadio&leadsCode="+leadsCode+"&leadsAllotId="+leadsAllotId;
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
			var leadsCode = document.getElementById("leadsCode").value;
			var leadsAllotId = document.getElementById("leadsAllotId").value;
			document.getElementById("saveButton").disabled = true;
			document.getElementById("invalidButton").disabled = true;
			document.getElementById("insertBtn").disabled = true;
			
			$('fm').action = "<%=contextPath%>/crm/taskmanage/TaskManage/leadsConfirmByOem.do?typeFlag=defeatRadio&leadsCode="+leadsCode+"&leadsAllotId="+leadsAllotId;
			$('fm').submit();
		} else if(failureRadio.checked == true) {
			var leadsCode = document.getElementById("leadsCode").value;
			var leadsAllotId = document.getElementById("leadsAllotId").value;
			document.getElementById("saveButton").disabled = true;
			document.getElementById("invalidButton").disabled = true;
			document.getElementById("insertBtn").disabled = true;
			
			$('fm').action = "<%=contextPath%>/crm/taskmanage/TaskManage/leadsConfirmByOem.do?typeFlag=failureRadio&leadsCode="+leadsCode+"&leadsAllotId="+leadsAllotId;
			$('fm').submit();
		}
	}
	function parseDate(str)  
	{
	    return new Date(Date.parse(str.replace(/-/g,"/")));
	}
</script>
<title>线索确认</title>
</head>
<body onunload='javascript:destoryPrototype();' onload="nameBlurChange();addRegionInit();loadcalendar();">
	<div class="wbox">
		<div class="navigation">
			<img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置>潜客管理>日程管理>任务管理>车厂线索确认
		</div>
		<form id="fm" name="fm" method="post">
			<input type="hidden" name="curPage" id="curPage" value="1" /> 
			<input type="hidden" id="dlrId" name="dlrId" value="" />
			<input type="hidden" name="leadsCode" id="leadsCode" value="${leadsCode }" />
			<input type="hidden" name="leadsAllotId" id="leadsAllotId" value="${leadsAllotId }" />
			<input type="hidden" name="leadsOrigin" id="leadsOrigin" value="${leadsOrigin }" />
			<input type="hidden" name="intentcar" id="intentcar" value="${intentcar }" />
			<input type="hidden" name="errorMsg" id="errorMsg" value="${errorMsg }" />
			<input type="hidden" name="intentVehicle" id="intentVehicle" value="" />
			<input type="hidden" name="provinceId" id="provinceId" value="" />
			<input type="hidden" id="cityId" name="cityId" value=""/>
			<input type="hidden" id="townId" name="townId" value=""/>
			
			<table class="table_query" width="95%" align="center">
				<tr>
					<td align="right" width="10%">客户姓名：</td>
					<td width="17%"><input id="customer_name" name="customer_name" type="text"
						class="middle_txt" datatype="1,is_textarea,30" size="20" value="${customerName }"  
						maxlength="60" /></td>
					<td align="right" width="7%">联系电话：</td>
					<td width="12%"><input id="telephone" name="telephone"
						type="text" class="middle_txt" datatype="1,is_textarea,30" value="${telephone }" onchange="nameBlurChange()"
						size="20" maxlength="60" /></td>
					<td align="right" width="7%">客户类型：</td>
					<td>
					<input type="hidden" id="customer_type" name="customer_type" value=""/>
		      		<div id="ddtopmenubar28" class="mattblackmenu">
						<ul> 
							<li>
								<a id="customer_typeh" style="width:103px;" rel="ddsubmenu28" href="###" isclick="true" onclick="stree.loadtree(this, '<%= request.getContextPath() %>/crm/data/DataManage/initData.json?codeId=6035', loadCustomerType);" deftitle="--请选择--">
								--请选择--</a>
								<ul id="ddsubmenu28" class="ddsubmenustyle"></ul>
							</li>
						</ul>
					</td>
				</tr>
				
				<tr>
					<td align="right" width="10%">购车预算：</td>
					<td width="17%">
					<input type="hidden" id="buy_budget" name="buy_budget" value=""/>
		      		<div id="ddtopmenubar22" class="mattblackmenu">
						<ul> 
							<li>
								<a id="buy_budgeth" style="width:103px;" rel="ddsubmenu22" href="###" isclick="true" onclick="stree.loadtree(this, '<%= request.getContextPath() %>/crm/data/DataManage/initData.json?codeId=6050', loadBuyBudget2);" deftitle="--请选择--">
								--请选择--</a>
								<ul id="ddsubmenu22" class="ddsubmenustyle"></ul>
							</li>
						</ul>
					</td>
					<td width="8%" align="right">跟进时间：</td>
					<td width="15%">
						<div align="left">
							<input type="text" value="${nowDate }" name="follow_date" id="follow_date" class="middle_txt" datatype="1,is_textarea,30" size="20" maxlength="60" readonly="readonly"/>
						</div></td>
					<td align="right" width="7%">意向车型：</td>
					<td>
					<select id="intentVehicleA" onchange="toChangeMenu(this,'intentVehicleB')" style="width: 70px">
						<option id="all" value="">-请选择-</option>
						<c:forEach items="${menusAList }" var="alist">
							<option id="${alist.MAINID }" value="${alist.MAINID }">${alist.NAME }</option>
						</c:forEach>
					</select>
					<select id="intentVehicleB" style="width: 100px">
						<option id="all" value="">-请选择-</option>
					</select>
					</td>
				</tr>

				<tr>
					<td align="right" width="7%">意向等级：</td>
					<td>
					<input type="hidden" id="intent_type" name="intent_type" value=""/>
		      		<div id="ddtopmenubar27" class="mattblackmenu">
						<ul> 
							<li>
								<a id="intent_typeh" style="width:103px;" rel="ddsubmenu27" href="###" isclick="true" onclick="stree.loadtree(this, '<%= request.getContextPath() %>/crm/data/DataManage/initData.json?codeId=6010', loadIntentType);" notselected="60101005,60101006,60101007" deftitle="--请选择--">
								--请选择--</a>
								<ul id="ddsubmenu27" class="ddsubmenustyle"></ul>
							</li>
						</ul>
					</td>
					<td width="8%" align="right">销售流程进度：</td>
					<td>
					<input type="hidden" id="sales_progress" name="sales_progress" value=""/>
			      		<div id="ddtopmenubar26" class="mattblackmenu">
							<ul> 
								<li>
									<a id="sales_progressh" style="width:103px;" rel="ddsubmenu26" href="###" isclick="true" onclick="stree.loadtree(this, '<%= request.getContextPath() %>/crm/data/DataManage/initData.json?codeId=6037', loadSalesProgress2);" deftitle="--请选择--">
									--请选择--</a>
									<ul id="ddsubmenu26" class="ddsubmenustyle"></ul>
								</li>
							</ul>
						</div>
					</td>
					<!--  
					<td align="right" width="14%" id="collect_fashionx"  >集客方式：</td>
					<td id="collect_fashionx2" >
					<input type="hidden" id="collect_fashion" name="collect_fashion" value=""/>
		      			<div id="ddtopmenubar29" class="mattblackmenu">
						<ul> 
							<li>
								<a id="jc_wayh" style="width:103px;" rel="ddsubmenu29" href="###" isclick="true" onclick="stree.loadtree(this, '<%= request.getContextPath() %>/crm/data/DataManage/initData.json?codeId=6002', loadCollectFashion);" deftitle="--请选择--">
								--请选择--</a>
								<ul id="ddsubmenu29" class="ddsubmenustyle"></ul>
							</li>
						</ul>
					 </div>
					</td>
					-->
					
				</tr>

				<tr>
					<td align="right" width="6%">&nbsp;省份：</td>
					<td align="left" width="32%"><select class="min_sel" id="dPro"
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
					<td align="right" colspan="1">说明：</td>
					<td align="left" colspan="4">
						<textarea rows="5" cols="70" id="follow_info" name="follow_info"></textarea>
					</td>
				</tr>
			</table>
			</br>
			</br>
			<table class="table_query" width="95%" align="center" id="unDoTask" style="display: none">
				<tr><td>任务类型</td><td>客户姓名</td><td>联系电话</td></tr>
				<tr style="height: 23px;background-color: #00FFFF;border: 0; border-color:#44BBBB; ">
					<td id="taskType" style="height: 23px;background-color: #00FFFF;border: 0; border-color:#44BBBB; "></td>
					<td id="ctmName" style="height: 23px;background-color: #00FFFF;border: 0; border-color:#44BBBB; "></td>
					<td id="ctmPhone" style="height: 23px;background-color: #00FFFF;border: 0; border-color:#44BBBB; "></td>
				</tr>
			</table>
			</br>
			<div id = "nextDiv">
				<b style="display: inline-block; float: left">下次计划任务</b><hr style="display: inline-block;"/>
			</div>
			<table class="table_query" width="95%" align="center" id="groupRadio">
				<tr>
					<td align="center">
						<input type="radio" checked="checked" id="follow_radio" name="group_radio" onclick="followClick()" />跟进
						<input type="radio"  id="invite_radio" style="display: none;" name="group_radio" onclick="inviteClick()"/><!-- 邀约 -->
						<input type="radio"  id="order_radio" name="group_radio" onclick="orderClick()" />订车 
						<input type="radio"  id="defeat_radio" name="group_radio" onclick="defeatClick()"/>战败
						<input type="radio"  id="failure_radio" style="display: none;" name="group_radio" onclick="failureClick()"/><!-- 失效 -->
					</td>
				</tr>
			</table>
			</br>
			<table class="table_query" width="95%" align="center" id="follow_table">
				<tr>
					<td width="6%" align="right">下次跟进时间：</td>
					<td width="22%">
						<div align="left">
							<input name="next_follow_date" id="next_follow_date" readonly="readonly" value="" type="text" class="short_txt" datatype="1,is_date,10" group="next_follow_date" />
							<input class="time_ico" type="button" onClick="showcalendar(event, 'next_follow_date', false);" value="&nbsp;" />
						</div>
					</td>
					<td width="6%" align="right">跟进方式：</td>
					<td width="22%">
						<input type="hidden" id="follow_type" name="follow_type" value=""/>
		      			<div id="ddtopmenubar25" class="mattblackmenu">
						<ul> 
							<li>
								<a style="width:103px;" rel="ddsubmenu25" href="###" isclick="true" onclick="stree.loadtree(this, '<%= request.getContextPath() %>/crm/data/DataManage/initData.json?codeId=6046', loadFollowType);" deftitle="--请选择--">
								--请选择--</a>
								<ul id="ddsubmenu25" class="ddsubmenustyle"></ul>
							</li>
						</ul>
					</td>
				</tr>
				<tr>
					<td width="6%" align="right">跟进计划：</td>
					<td align="left" colspan="4">
						<textarea rows="5" cols="70" id="follow_plan" name="follow_plan"></textarea>
					</td>
				</tr>
			</table>
			
			<table class="table_query" width="95%" align="center" id="invite_table" style="display: none">
				<tr>
					<td align="right" colspan="4">是否填写邀约计划</td>
					<td colspan="4"><input type="checkbox" id="checkbox" onclick="checkClick()" /></td>
				</tr>
				<tr id="yaoyuejihua" style="display: none">
					<td align="center" colspan="2">需求分析</td>
					<td align="center" colspan="2">邀约目标</td>
					<td align="center" colspan="2">赢得客户信任设计</td>
					<td align="center" colspan="2">感动客户情景设计</td>
				</tr>
				<tr id="yaoyuejihua2" style="display: none">
					<td align="center" colspan="2"><textarea rows="10" cols="30" id="xqfx" name="xqfx"></textarea></td>
					<td align="center" colspan="2"><textarea rows="10" cols="30" id="yymb" name="yymb"></textarea></td>
					<td align="center" colspan="2"><textarea rows="10" cols="30" id="ydkhxrsj" name="ydkhxrsj"></textarea></td>
					<td align="center" colspan="2"><textarea rows="10" cols="30" id="gdkhqjsj" name="gdkhqjsj"></textarea></td>
				</tr>
				<tr>
					<td> </td>
					<td align="right">计划邀约时间:</td>
					<td>
						<input name="plan_invite_date" id="plan_invite_date" readonly="readonly" value="" type="text" class="short_txt" datatype="1,is_date,10" group="startDate,endDate" />
						<input id="plan_invite_date2" name="plan_invite_date2" style="margin-left: -4px;" class="time_ico" type="button" onclick="changeEvent1()"; />
					</td>
					<td align="right">计划见面时间:</td>
					<td>
					<input name="plan_meet_date" id="plan_meet_date" readonly="readonly" value="" type="text" class="short_txt" datatype="1,is_date,10" group="startDate,endDate" />
						<input id="plan_meet_date2" name="plan_meet_date2" style="margin-left: -4px;" class="time_ico" type="button" onClick="showcalendar(event, 'plan_meet_date', false);" />
 
					</td>
					<td align="right">邀约方式:</td>
					<td>
					<input type="hidden" id="invite_type" name="invite_type" value=""/>
		      			<div id="ddtopmenubar24" class="mattblackmenu">
						<ul> 
							<li>
								<a style="width:103px;" rel="ddsubmenu24" href="###" isclick="true" onclick="stree.loadtree(this, '<%= request.getContextPath() %>/crm/data/DataManage/initData.json?codeId=6047', loadInviteType);" deftitle="--请选择--">
								--请选择--</a>
								<ul id="ddsubmenu24" class="ddsubmenustyle"></ul>
							</li>
						</ul>
					</td>
					<td align="right"></td>
				
				</tr>
			</table>
			
			<table class="table_query" width="95%" align="center" id="order_table" style="display: none">
				<tr>
					<td align="right" width="11%">订车时间:</td>
					<td width="12%"><input type="text" value="" name="order_date" id="order_date" group="order_date"
								class="short_txt" datatype="1,is_date,10" size="20" hasbtn="true" readonly="readonly"
								maxlength="60" callFunction="showcalendar(event, 'order_date', false);" />
					</td>
				</tr>
			</table>
			
			<table class="table_query" width="95%" align="center" id="defeat_table" style="display: none">
				<tr>
					<td align="right" width="10%">战败车型：</td>
					<td width="18%">
					<select id="defeatVehicleA" name="defeatVehicleA" onchange="toChangeMenu2(this,'defeatVehicleB')" style="width: 70px">
						<option id="all" value="">-请选择-</option>
						<c:forEach items="${menusAList2 }" var="alist">
							<option id="${alist.MAINID }" value="${alist.MAINID }">${alist.NAME }</option>
						</c:forEach>
					</select>
					<select id="defeatVehicleB" name="defeatVehicleB" style="width: 100px">
						<option id="all" value="">-请选择-</option>
					</select>
					</td>
					<td align="right" width="10%">战败原因：</td>
					<td>
					<input type="hidden" id="defeatReasonB" name="defeatReasonB" value=""/>
		      		<div id="ddtopmenubar23" class="mattblackmenu">
						<ul> 
							<li>
								<a style="width:103px;" rel="ddsubmenu23" href="###" isclick="true" onclick="stree.loadtree(this, '<%= request.getContextPath() %>/crm/data/DataManage/initData.json?codeId=6033', loadDefeatReason2);" deftitle="--请选择--">
								--请选择--</a>
								<ul id="ddsubmenu23" class="ddsubmenustyle"></ul>
							</li>
						</ul>
					</td>
				</tr>
				<tr>
					<td align="right" colspan="1">原因说明：</td>
					<td align="left" colspan="4">
						<textarea rows="5" cols="70" id="defeat_info" name="defeat_info"></textarea>
					</td>
				</tr>
			</table>
			<table class="table_query" width="95%" align="center">
				<tr>
					<td colspan="8">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
					<td colspan="8">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
					<td colspan="8">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
					<td colspan="8">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
					<c:if test="${adviserLogon=='yes' }">
						<td  align="left">
						<input name="queryBtn"
						type="button" class="normal_btn" onclick="doSave();" id="saveButton"
						value="保存" />
						</td>
						<td>
						<input name="queryBtn" id="invalidButton"
						type="button" class="normal_btn" onclick="invildClick();"
						value="无效" />
						</td>
					</c:if>
					<td>
						<input name="insertBtn" id="insertBtn"
						type="button" class="normal_btn" onclick="javascript:history.go(-1);"
						value="取消" />
					</td>
					<td colspan="8">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
					<td colspan="8">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
					<td colspan="8">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
					<td colspan="8">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
				</tr>
			</table>
			<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
			<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
		</form>

	</div>
<script type="text/javascript">ddlevelsmenu.setup("ddtopmenubar29", "topbar")</script>
<script type="text/javascript">ddlevelsmenu.setup("ddtopmenubar28", "topbar")</script>
<script type="text/javascript">ddlevelsmenu.setup("ddtopmenubar27", "topbar")</script>
<script type="text/javascript">ddlevelsmenu.setup("ddtopmenubar26", "topbar")</script>
<script type="text/javascript">ddlevelsmenu.setup("ddtopmenubar25", "topbar")</script>
<script type="text/javascript">ddlevelsmenu.setup("ddtopmenubar24", "topbar")</script>
<script type="text/javascript">ddlevelsmenu.setup("ddtopmenubar23", "topbar")</script>
<script type="text/javascript">ddlevelsmenu.setup("ddtopmenubar22", "topbar")</script>
<script type="text/javascript">
	var dd1 = new Date(); 
	var data= dd1.Format("yyyy-M-d");
	function changeEvent(){
		var inbound=document.getElementById("intent_typeh").innerHTML.split("<")[0];
		var nextDate=document.getElementById("next_follow_date2");
		//var jc_wayh=document.getElementById("jc_wayh").innerHTML.split("<")[0];
		var jc_wayh="";
		if(jc_wayh.indexOf("首次") >= 0){
				var dd = new Date();
		    	dd.setDate(dd.getDate()+1);
			nextDate.addEventListener("click",showcalendar(event, 'next_follow_date', false,data,dd.Format("yyyy-M-d")));
		}else {
			if(inbound == "A"){
				var dd = new Date();
			    	dd.setDate(dd.getDate()+6);
				nextDate.addEventListener("click",showcalendar(event, 'next_follow_date', false,data,dd.Format("yyyy-M-d")));
			}else if(inbound == "B"){
				var dd = new Date();
		    	dd.setDate(dd.getDate()+14);
			nextDate.addEventListener("click",showcalendar(event, 'next_follow_date', false,data,dd.Format("yyyy-M-d")));
			}else if(inbound == "C"){
				var dd = new Date();
		    	dd.setDate(dd.getDate()+29);
			nextDate.addEventListener("click",showcalendar(event, 'next_follow_date', false,data,dd.Format("yyyy-M-d")));
			}else if(inbound == "H"){
				 var dd = new Date();
			    	dd.setDate(dd.getDate()+2);
				nextDate.addEventListener("click",showcalendar(event, 'next_follow_date', false,data,dd.Format("yyyy-M-d")));
			}else {
				nextDate.addEventListener("click",showcalendar(event, 'next_follow_date', false,data));
			}
		}
	}
	function changeEvent1(){
		var inbound=document.getElementById("intent_typeh").innerHTML.split("<")[0];
		var nextDate=document.getElementById("plan_invite_date2");
		//var jc_wayh=document.getElementById("jc_wayh").innerHTML.split("<")[0];
		var jc_wayh="";
		if(jc_wayh.indexOf("首次") >= 0){
				var dd = new Date();
		    	dd.setDate(dd.getDate()+1);
			nextDate.addEventListener("click",showcalendar(event, 'next_follow_date', false,data,dd.Format("yyyy-M-d")));
		} 
		
		if(inbound == "A"){
			var dd = new Date();
		    	dd.setDate(dd.getDate()+6);
			nextDate.addEventListener("click",showcalendar(event, 'plan_invite_date', false,data,dd.Format("yyyy-M-d")));
		}else if(inbound == "B"){
			var dd = new Date();
	    	dd.setDate(dd.getDate()+14);
		nextDate.addEventListener("click",showcalendar(event, 'plan_invite_date', false,data,dd.Format("yyyy-M-d")));
		}else if(inbound == "C"){
			var dd = new Date();
	    	dd.setDate(dd.getDate()+29);
		nextDate.addEventListener("click",showcalendar(event, 'plan_invite_date', false,data,dd.Format("yyyy-M-d")));
		}else if(inbound == "H"){
			 var dd = new Date();
		    	dd.setDate(dd.getDate()+2);
			nextDate.addEventListener("click",showcalendar(event, 'plan_invite_date', false,data,dd.Format("yyyy-M-d")));
		}else {
			nextDate.addEventListener("click",showcalendar(event, 'plan_invite_date', false,data));
		}
	}
	
function invildClick() {
	var leadsCode = document.getElementById("leadsCode").value;
	var leadsAllotId = document.getElementById("leadsAllotId").value;
	var intentVehicle = document.getElementById("intentVehicleB").value;
	var intentCar= document.getElementById("intentcar").value;
	document.getElementById("intentVehicle").value = intentVehicle;
	if(intentCar==null || intentCar==""){
		}else{
	if(intentVehicle==null || intentVehicle==""){
		MyAlert("意向车型不能为空!请选择!");
		return false;
		}
	}
	document.getElementById("saveButton").disabled = true;
	document.getElementById("invalidButton").disabled = true;
	document.getElementById("insertBtn").disabled = true;
	
	$('fm').action = "<%=contextPath%>/crm/taskmanage/TaskManage/leadsConfirmByOem.do?typeFlag=invidateRadio&leadsCode="+leadsCode+"&leadsAllotId="+leadsAllotId;
	$('fm').submit();
	
} 

//禁止用F5键 
function onkeydown() 
{ 
if ( event.keyCode==116) 
{ 
event.keyCode = 0; 
event.cancelBubble = true; 
return false; 
} 
} 

//禁止右键弹出菜单 
function oncontextmenu(){event.returnValue=false;}//屏蔽鼠标右键 

</script>
</body>
</html>