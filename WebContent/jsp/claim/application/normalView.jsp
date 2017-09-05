<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ taglib uri="/jstl/cout" prefix="c" %>
<%@taglib uri="/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="/jstl/change" prefix="change" %>
<%@ page import="java.util.LinkedList"%>
<%@ page import="java.util.List" %>
<%@ page import=" com.infodms.dms.util.CommonUtils" %>
<%@ page import="com.infodms.dms.po.FsFileuploadPO"%>
<head> 
<%  String contextPath = request.getContextPath(); 
	List<FsFileuploadPO> fileList = (LinkedList<FsFileuploadPO>)request.getAttribute("fileList");
	request.setAttribute("fileList",fileList);
%>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<script type="text/javascript" src="<%=contextPath%>/js/jslib/jquery-1.7.2.js"></script>
<script type="text/javascript" src="<%=contextPath%>/js/jslib/CalendarZYW.js"></script>

<title>一般索赔单管理</title>
<script type="text/javascript">
	//公共的checked
	function ischecked(className){
		var val=$("#"+className).val();
		if(val!=""){
			var str=val.split(",");
			$("."+className).each(function(){
				for(var i=0;i<str.length;i++){
					if(str[i]==$(this).val()){
						$(this).attr("checked",true).attr("disabled",false);
						$(this).bind("click",function(){
							$(this).attr("checked",true);
						});
					}
				}
			});
		}
	}
	$(function(){
		$("input[type='checkbox']").attr("disabled",true);
		$("textarea").attr("readonly","readonly");
		$("#AUDIT_REMARK").removeAttr("readonly");
		if("view"==$("#type").val()){
			$("#pass_amount_temp").show();
			$("#pass_amount,#sureUnpass,#surePass,#sureUndo,#sureReject,#sureUndo").remove();
			$("textarea").attr("readonly","readonly");
			$("input[type='text']").attr("readonly",true);
		}
	    $(".normal_btn").each(function(){
			 if($(this).val()=="删 除" || $(this).val()=="添加附件"){
				 $(this).hide();
			} 
		});
		var claimType=$("#claim_type").val();
		if("10661001"==claimType || "10661007"==claimType){
			ischecked("out_car");
		}else{
			$("#is_out").remove();
		}
		if("10661006"==claimType){
			$(".campaign_code").show();
		}
		if("10661013"==claimType){
			var FREE_RO=$("#FREE_RO").val();
			if(""!=FREE_RO){
				$("#freeRo").empty();
				$("#freeRo").append("<input type='hidden' name='free_ro' value='"+FREE_RO+"'/><span>"+FREE_RO+"</span>");
			}
			$(".freeRo").show();
			if("add"!=type){
				$(".free_show").hide();
			}
		}
	});
	
	function sureConfirm(identify){
		var temp=0;
		disableBtns();
		if(""==$("#ro_no").val()){
			MyAlert("提示：工单信息丢失！");
			temp++;
			return;
		}
		if(""==$("#vin").val()){
			MyAlert("提示：车辆信息丢失！");
			temp++;
			return;
		}
		var url="<%=contextPath%>/ClaimAction/normalAudit.json?identify="+identify;
		if(temp==0){
			if(0==identify){
				var countSPAN = $("#apply_span").text();
				var autidtAmount  = $("#auditAmount").val();
				if("" == autidtAmount || null == autidtAmount){
					MyAlert("审核金额不能为空");
					enableBtns();
					return false;
				}else{
					if(moneyValidate(autidtAmount) == false){
						MyAlert("只能输入大于0的数字,小数最多两位");
						enableBtns();
						return false;
					}
				}
				if(Number(countSPAN)<Number(autidtAmount)){
					MyAlert("审核金额不能大于费用合计");
					enableBtns();
					return false;
				}
				
				MyUnCloseConfirm("确定要审核通过?", function(identify){
					sendRequest(url);
				}, [identify], enableBtns);
			}
			if(1==identify){
				MyUnCloseConfirm("确定要审核退回?", function(identify){
					sendRequest(url);
				}, [identify], enableBtns);
			}
			if(2==identify){
				MyUnCloseConfirm("确定要撤销审核?", function(identify){
					sendRequest(url);
				}, [identify], enableBtns);
			}
			if(3==identify){
				MyUnCloseConfirm("确定要审核拒绝?", function(identify){
					sendRequest(url);
				}, [identify], enableBtns);
			}
		}
	}

	
	function enableBtns(){
		if(null!=document.getElementById("surePass") &&
				"undefined"!= typeof (document.getElementById("surePass").disabled))
			document.getElementById("surePass").disabled=false;
		if(null!=document.getElementById("sureUnpass") &&
				"undefined"!= typeof (document.getElementById("sureUnpass").disabled))
			document.getElementById("sureUnpass").disabled=false;
		if(null!=document.getElementById("sureReject") &&
				"undefined"!= typeof (document.getElementById("sureReject").disabled))
			document.getElementById("sureReject").disabled=false;
		if(null!=document.getElementById("sureUndo") &&
				"undefined"!= typeof (document.getElementById("sureUndo").disabled))
			document.getElementById("sureUndo").disabled=false;
	}
	function disableBtns(){
		if(null!=document.getElementById("surePass") &&
				"undefined"!= typeof (document.getElementById("surePass").disabled))
			document.getElementById("surePass").disabled=true;
		if(null!=document.getElementById("sureUnpass") &&
				"undefined"!= typeof (document.getElementById("sureUnpass").disabled))
			document.getElementById("sureUnpass").disabled=true;
		if(null!=document.getElementById("sureReject") &&
				"undefined"!= typeof (document.getElementById("sureReject").disabled))
			document.getElementById("sureReject").disabled=true;
		if(null!=document.getElementById("sureUndo") &&
				"undefined"!= typeof (document.getElementById("sureUndo").disabled))
			document.getElementById("sureUndo").disabled=true;
	}
	
	function sendRequest(url) {
		makeNomalFormCall1(url, function(json){
			if (json.Exception) {
			} else {
				MyUnCloseAlert(json.info, function(){
					window.location.href = g_webAppName + '/ClaimBalanceAction/claimBalanceList.do';
				});
			}
		},'fm','');
	}

	function backInfo(json){
		if (json.Exception) {
			MyUnCloseAlert(json.Exception.message, enableBtns);
		} else {
			MyUnCloseAlert(json.info.message, function(){
				window.location.href = g_webAppName + '/ClaimBalanceAction/claimBalanceList.do';
			});
		}
	}
	
	function openPre(){
		var ysq_no=$("#ysq_no").val();
		if(""==ysq_no){
			MyAlert("提示：没有预授权单号！");
			return;
		}
		var url='<%=contextPath%>/YsqAction/changeByYsqRo.json?ysq_no='+ysq_no;
		var id="";
		sendAjax(url,function(json){
			id=json.id;
			if(""!=id){
				var create_by=$("#create_by").val();
				var vin=$("#vin").val();
				OpenHtmlWindow('<%=contextPath%>/YsqAction/ysqView.do?id='+id+'&createBy='+create_by+'&vin='+vin,900,500);
			}else{
				MyAlert("提示：数据异常！");
				return;
			}
		},'fm');
	}
	function goBack(){
		_hide();
	}
</script>
</head>
<body>
<div class="navigation">
<img src="../../../img/nav.gif" />&nbsp;当前位置：售后服务管理&gt;经销商索赔单管理&gt;一般索赔单管理
</div>
<form name="fm" id="fm" method="post">
<input class="middle_txt" id="type" value="${type }" name="type" type="hidden"  />
<input class="middle_txt" id="id" value="${t.ID }" name="id" type="hidden"  />
<input class="middle_txt" id="claim_type" value="${claim_type }" name="claim_type" type="hidden"  />
<input class="middle_txt" id="model_id" value="${t.MODEL_ID }" name="model_id" type="hidden"  />
<input class="middle_txt" id="series_id" value="${t.SERIES_ID }" name="series_id" type="hidden"  />
<input class="middle_txt" id="package_id" value="${t.PACKAGE_ID }" name="package_id" type="hidden"  />
<input class="middle_txt" id="wrgroup_id" value="${t.WRGROUP_ID }" name="wrgroup_id" type="hidden"  />
<input class="middle_txt" id="submit_times" value="${t.SUBMIT_TIMES }" name="submit_times" type="hidden"  />
<input class="middle_txt" id="status"  name="status" value="${t.STATUS }"  type="hidden"  />
<input class="middle_txt" id="goBackType" value="${goBackType }" name="goBackType" type="hidden"  />
<input type="hidden" id="out_car"  value="${r.OUT_CAR }"/>
<input type="hidden" id="FREE_RO"  value="${t.FREE_RO }"/>


<table border="0" id="tab_base" cellpadding="1" cellspacing="1" class="table_edit" width="100%" style="text-align: center;" >
	<th colspan="8">
		<img class="nav" src="../../../img/subNav.gif"/>基本信息
	</th>
	<tr>
		<td width="12.5%"></td>
		<td nowrap="true" width="10%" >索赔单号：</td>
    	<td nowrap="true" width="15%" >
    		<span>${t.CLAIM_NO }</span>
    	</td>
		<td nowrap="true" width="10%" >VIN：</td>
    	<td nowrap="true" width="15%" >
    		<span>${t.VIN }</span>
    	</td>
    	<td nowrap="true" width="10%" >发动机号：</td>
    	<td nowrap="true" width="15%" >
    		<span>${ t.ENGINE_NO }</span>
    	</td>
    	<td width="12.5%"></td>
	</tr>
	<tr>
		<td width="12.5%"></td>
		<td nowrap="true" width="10%" >工单号：</td>
    	<td  width="15%" >
    		<span>${ t.RO_NO }</span>
    	</td>
		<td nowrap="true" width="10%" >行驶里程：</td>
    	<td nowrap="true" width="15%" >
    		<span>${ t.IN_MILEAGE }</span>
    	</td>
    	<td nowrap="true" width="10%" >颜色：</td>
    	<td nowrap="true" width="15%" >
    		<span>${ t.APP_COLOR }</span>
    	</td>
    	<td width="12.5%"></td>
	</tr>
	<tr>
		<td width="12.5%"></td>
		<td nowrap="true" width="10%" >预授权号：</td>
    	<td  width="15%" >
    		<span><a href = "javascript:openPre();">${ t.YSQ_NO }</a></span>
    	</td>
		<td nowrap="true" width="10%" >购车日期：</td>
    	<td nowrap="true" width="15%" >
    		<span><fmt:formatDate value="${t.GUARANTEE_DATE}" pattern="yyyy-MM-dd"/></span>
    	</td>
		<td nowrap="true" width="10%" >三包预警：</td>
    	<td nowrap="true" width="15%" >
    		<span>${ t.WARNING_LEVEL }</span>
    	</td>
    	<td width="12.5%"></td>
	</tr>
	<tr>
		<td width="12.5%"></td>
		<td nowrap="true" width="10%" >索赔类型：</td>
    	<td  width="15%" >
    		${claim_type }
    	</td>
		<td nowrap="true" width="10%" style="display: none;" class="campaign_code">活动代码</td>
    	<td nowrap="true" width="15%" style="display: none;" class="campaign_code" >
    		<span>${ t.CAMPAIGN_CODE }</span>
    	</td>
		<td nowrap="true" width="10%" style="display: none;" class="freeRo">自费工单:</td>
    	<td nowrap="true" width="15%" style="display: none;" class="freeRo" id="freeRo">
    		<select class='short_sel' name='free_ro'><option value=''>-请选择-</option></select>
    	</td>
    	<td width="12.5%"></td>
	</tr>
</table>
<div style="text-align: center; font-weight: bolder;">
	<table border="0" id="tab_2" cellpadding="1" cellspacing="1" class="table_edit" width="100%" style="text-align: center;">
		<tr>
				<td nowrap="true" width="5%" ></td>
				<td nowrap="true" width="30%" style="font-weight: bold;"></td>
				<td nowrap="true" width="30%" style="font-weight: bold;"></td>
				<td nowrap="true" width="30%" style="font-weight: bold;display: none;">处理结果</td>
				<td nowrap="true" width="5%" ></td>
		</tr>
		<tr>
				<td nowrap="true" width="5%" >故障现象</td>
				<td nowrap="true" width="30%" >
					<textarea style="font-weight: bold;" name="trouble_reason" id="trouble_reason" rows="4" cols="30">${t.TROUBLE_REASON }</textarea>
				</td>
				<td nowrap="true" width="5%" >分析原因及处理结果</td>
				<td nowrap="true" width="30%" >
					<textarea style="font-weight: bold;" name="trouble_desc" id="trouble_desc" rows="4" cols="30">${t.TROUBLE_DESC }</textarea>
				</td>
				<td nowrap="true" width="30%" style="display: none;">
					<textarea style="font-weight: bold;" name="repair_method" id="repair_method" rows="4" cols="30">${t.REPAIR_METHOD }</textarea>
				</td>
		</tr>
	</table>
</div>
<br>
<div style="text-align: center;">
			&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span style="color: green;">申请费用合计: </span>
    		<span style="color: red; font-weight: bold;" id="apply_span">${t.REPAIR_TOTAL }</span>
    		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span style="color: green;">结算费用合计: </span>
    		<span style="color: red; font-weight: bold;" id="count_span">${t.BALANCE_AMOUNT }</span>
    		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span style="color: green;">审核金额: </span>
    		<input type="text" id="auditAmount" name="auditAmount" class ="bottom_line" value="${t.BALANCE_AMOUNT}"></input>
    		</div> 
    		<br>
<!-- 添加附件 开始  -->
        <table id="add_file"  width="75%" class="table_info" border="0" id="file">
	    		<tr>
	        		<th>
						<input type="hidden" id="fjids" name="fjids"/>
						<img class="nav" src="<%=contextPath%>/img/subNav.gif" />附件信息
						<font color="red">
							<span id="span1"></span>
						</font>
			     		<input type="button" class="normal_btn" align="right" id="addfile" onclick="showUpload('<%=contextPath%>')" value ='添加附件'/>
					</th>
				</tr>
				<tr>
    				<td width="75%" colspan="2"><jsp:include page="${contextPath}/uploadDiv.jsp" /></td>
  				</tr>
  				<%if(fileList!=null){
	  				for(int i=0;i<fileList.size();i++) { %>
					  <script type="text/javascript">
					  addUploadRowByDbView('<%=CommonUtils.checkNull(fileList.get(i).getFilename()) %>','<%=CommonUtils.checkNull(fileList.get(i).getFjid()) %>','<%=CommonUtils.checkNull(fileList.get(i).getFileurl())%>','<%=CommonUtils.checkNull(fileList.get(i).getFjid()) %>','<%=request.getContextPath()%>/util/FileDownLoad/FileDownLoadQuery.do?fjid=' );
				    	</script>
				<%}}%>
			</table> 
  		<!-- 添加附件 结束 -->
  		<br>
    		<div id="new_add">
	    		<table border="1" id="tab_part" cellpadding="1" cellspacing="1" class="table_edit" width="110%" style="text-align: center;">
					<th colspan="12">
					<img class="nav" src="<%=contextPath%>/img/subNav.gif" />作业项目 &nbsp;&nbsp;
					</th>
					<tr>
						<td nowrap="true" width="10%" >维修配件</td>
						<td nowrap="true" width="10%" >新件代码</td>
						<td nowrap="true" width="10%" >新件名称</td>
						<td nowrap="true" width="10%" >旧件代码</td>
						<td nowrap="true" width="10%" >单价</td>
						<td nowrap="true" width="10%" >配件数量</td>
						<td nowrap="true" width="10%" >金额（元）</td>
						<td nowrap="true" width="10%" >责任性质</td>
						<td nowrap="true" width="10%" >维修方式</td>
						<td nowrap="true" width="10%" >供应商代码</td>
						<td nowrap="true" width="10%" >是否回运</td>
					</tr>
					<c:forEach items="${parts }" var="p" varStatus="status">
						<tr>
						<td nowrap="true" width="10%" >
						${status.index+1}<input name="part_id" type="hidden" class="middle_txt" value="${p.PART_ID }"/>
						</td>
						<td nowrap="true" width="10%" >
						<span>${p.PART_CODE }</span>
						</td>
						<td nowrap="true" width="10%" >
						<span>${p.PART_NAME }</span>
						</td>
						<td nowrap="true" width="10%" >
						<span>${p.DOWN_PART_CODE }</span>
						</td>
						<td nowrap="true" width="10%" >
						<span>${p.APPLY_PRICE }</span>
						</td>
						<td nowrap="true" width="10%" >
						<span>${p.APPLY_QUANTITY }</span>
						</td>
						<td nowrap="true" width="10%" >
						<span>${p.APPLY_AMOUNT }</span>
						</td>
						<td nowrap="true" width="10%" >
						<c:if test="${p.RESPONSIBILITY_TYPE==94001001}">
							<span>主因件</span>
						</c:if>
						<c:if test="${p.RESPONSIBILITY_TYPE==94001002}">
							<span>次因件</span>
						</c:if>
						</td>
						<td nowrap="true" width="10%" >
						<c:if test="${p.PART_USE_TYPE==1}">
							<span>更换</span>
						</c:if>
						<c:if test="${p.PART_USE_TYPE==0}">
							<span>维修</span>
						</c:if>
						</td>
						<td nowrap="true" width="10%" >
						<span>${p.PRODUCER_CODE }</span>
						</td>
						<td nowrap="true" width="10%" >
						<c:if test="${p.IS_RETURN==95361001}">
							<span>回运</span>
						</c:if>
						<c:if test="${p.IS_RETURN==95361002}">
							<span>不回运</span>
						</c:if>
						</td>
						</tr>
					</c:forEach>
	    		</table>
    			<table border="1" id="tab_labour" cellpadding="1" cellspacing="1" class="table_edit" width="110%" style="text-align: center;">
					<tr>
					<td nowrap="true" width="10%" >维修工时</td>
					<td nowrap="true" width="20%" >工时代码</td>
					<td nowrap="true" width="20%" >工时名称</td>
					<td nowrap="true" width="20%" >工时定额</td>
					<td nowrap="true" width="20%" >工时单价</td>
					<td nowrap="true" width="20%" >工时金额（元）</td>
					</tr>
	    			 <c:forEach items="${labours }" var="l" varStatus="status">
			          <tr>
						<td nowrap="true" width="10%" >${status.index+1}</td>
						<td nowrap="true" width="20%" >
						<span>${l.LABOUR_CODE }</span>
						</td>
						<td nowrap="true" width="20%" >
						<span>${l.LABOUR_NAME }</span>
						</td>
						<td nowrap="true" width="20%" >
						<span>${l.APPLY_QUANTITY }</span>
						</td>
						<td nowrap="true" width="20%" >
						<span>${l.APPLY_PRICE }</span>
						</td>
						<td nowrap="true" width="20%" >
						<span>${l.APPLY_AMOUNT }</span>
						</td>
						</tr>
	    			 </c:forEach>
	    		</table>
</div>
<br>
<div id="new_com_add"  style="text-align: center; width: 100%;">
<c:if test="${com!=null}">
	<table id="com" border="1" cellpadding="1" cellspacing="1" class="table_edit" width="80%" style="text-align: center;">
		<th colspan="4">
			<img class="nav" src="<%=contextPath%>/img/subNav.gif" />补偿费&nbsp;&nbsp;
		</th>
		<tr>
			<td nowrap="true" width="20%" >补偿费申请金额</td>
			<td nowrap="true" width="20%" >审批金额</td>
			<td nowrap="true" width="40%" >备注</td>
		</tr>
		<tr>
			<td nowrap="true" width="20%" >
			<span>${com.APPLY_PRICE }</span>
		</td>
			<td nowrap="true" width="20%" >
			<span id="pass_amount_temp" style="display: none;">${com.PASS_PRICE }</span>
			<input id="pass_amount" name="pass_amount"  size="25" value="${com.PASS_PRICE }"/>
		</td>
			<td nowrap="true" width="40%" >
			<span>${com.REASON }</span>
		</tr>
	</table>
	<br>
	</c:if>
	<c:if test="${acc!=null}">
			<table id="acc" border="1" cellpadding="1" cellspacing="1" class="table_edit" width="80%" style="text-align: center;">
				<th colspan="8">
					<img class="nav" src="<%=contextPath%>/img/subNav.gif" />辅料项目&nbsp;&nbsp;
				</th>
				<tr>
					<td nowrap="true" width="20%" >辅料代码</td>
					<td nowrap="true" width="20%" >辅料名称</td>
					<td nowrap="true" width="20%" >辅料费用</td>
					<td nowrap="true" width="20%" >关联配件</td>
				</tr>
   			 <c:forEach items="${acc }" var="a">
   				<tr><td nowrap="true" width="20%" >
					<input type="hidden"name="workHourCode" class="middle_txt" value="${a.WORKHOUR_CODE }"/><span>${a.WORKHOUR_CODE }</span>
					</td>
					<td nowrap="true" width="20%" >
					<input type="hidden"name="workhour_name" class="middle_txt" value="${a.WORKHOUR_NAME }"/><span>${a.WORKHOUR_NAME }</span>
					</td>
					<td nowrap="true" width="20%" >
					<input type="hidden"name="accessoriesPrice" class="middle_txt" value="${a.PRICE }"/><span>${a.PRICE }</span>
					</td>
					<td nowrap="true" width="20%" >
					<input type="hidden"name="accessoriesOutMainPart" class="middle_txt"  value="${a.MAIN_PART_CODE }"/><span>${a.MAIN_PART_CODE }</span>
					</td>
					</tr>
   			 </c:forEach>
   			 </table>
    		</c:if>
</div>

 <div style="text-align: center;"  id="is_out">
			<table  border="0" cellpadding="1" cellspacing="1" class="table_edit" width="100%" style="text-align: center;">
			<th colspan="8">
						<img class="nav" src="<%=contextPath%>/img/subNav.gif" />外出维修&nbsp;&nbsp;&nbsp;&nbsp;
						<input type="checkbox" name="out_car" class="out_car" id="our_car_1" value="乘车外出"/>乘车外出&nbsp;&nbsp;
						<input type="checkbox" name="out_car" class="out_car" id="our_car_2" value="自备车外出"/>自备车外出&nbsp;&nbsp;
						<input type="checkbox" name="out_car" class="out_car" id="our_car_3" value="外出背车"/>外出背车&nbsp;&nbsp;
					</th>
					<tr>
						<td nowrap="true" width="10%" >&nbsp;&nbsp;开始时间:&nbsp;</td>
						<td nowrap="true" width="10%" >
							<span><fmt:formatDate value="${r.START_TIME }" pattern='yyyy-MM-dd HH:mm'/></span>
						</td>
						<td nowrap="true" width="10%" >&nbsp;&nbsp;外出人员:&nbsp;&nbsp;</td>
						<td nowrap="true" width="10%" >
							<span>${r.OUT_PERSON }</span>
						</td>
						<td nowrap="true" width="10%" >&nbsp;&nbsp;单程里程:&nbsp;&nbsp;</td>
						<td nowrap="true" width="10%" >
							<span>${r.OUT_MILEAGE }</span>
						</td>
						<td nowrap="true" width="20%" ></td>
						<td nowrap="true" width="20%" ></td>
					</tr>
					<tr>
						<td nowrap="true" width="10%" >&nbsp;&nbsp;结束时间:&nbsp;</td>
						<td nowrap="true" width="10%" >
							<span><fmt:formatDate value="${r.END_TIME }" pattern='yyyy-MM-dd HH:mm'/></span>
						</td>
						<td nowrap="true" width="10%" >&nbsp;&nbsp;外出目的地:&nbsp;&nbsp;</td>
						<td nowrap="true" width="10%" >
							<span>${r.OUT_SITE }</span>
						</td>
						<td nowrap="true" width="10%" ></td>
						<td nowrap="true" width="10%" >
						</td>
						<td nowrap="true" width="20%" ></td>
						<td nowrap="true" width="20%" ></td>
					</tr>
			</table>
			<br>
			<table id="accessories"  border="1" cellpadding="1" cellspacing="1" class="table_edit" width="80%" style="text-align: center;">
					<tr >
						<th nowrap="true" width="10%" style="text-align: center;">项目名称</th>
						<th nowrap="true" width="10%" style="text-align: center;">过路过桥费</th>
						<th nowrap="true" width="10%" style="text-align: center;">交通补助费</th>
						<th nowrap="true" width="10%" style="text-align: center;">住宿费</th>
						<th nowrap="true" width="10%" style="text-align: center;">餐补费</th>
						<th nowrap="true" width="10%" style="text-align: center;">背车费</th>
					</tr>
					<tr>
						<td nowrap="true" width="10%" >
							申请金额（元）
						</td>
						<td nowrap="true" width="10%" >
							<span>${o.QT006_APPLY }</span>
						</td>
						<td nowrap="true" width="10%" >
							<span>${o.QT007_APPLY }</span>
						</td>
						<td nowrap="true" width="10%" >
							<span>${o.QT008_APPLY }</span>
						</td>
						<td nowrap="true" width="10%" >
							<span>${o.QT009_APPLY }</span>
						</td>
						<td nowrap="true" width="10%" >
							<span>${o.QT001_APPLY }</span>
						</td>
					</tr>
			</table>
	</div>    
	<br>
<table border="0" cellpadding="1" cellspacing="1" class="table_edit" width="100%" style="text-align: center;">
      <tr>
       <th colspan="8">
			<img class="nav" src="<%=contextPath%>/img/subNav.gif"/>审核备注
		</th>
	 </tr>	
		<tr>
			<td height="12" align=left width="33%" colspan='2'>
				<textarea name='audit_remark' id='AUDIT_REMARK' readonly="readonly" maxlength="100" rows='2' cols='145'>${t.AUDIT_REMARK }</textarea>
			</td>
		</tr>
</table>
<br>

<table border="1" cellpadding="1" cellspacing="1" class="table_edit" width="70%" style="text-align: center;">
<th colspan="8">
			<img class="nav" src="<%=contextPath%>/img/subNav.gif"/>状态跟踪
		</th>
		<tr>
			<td>环节</td>
			<td>操作时间</td>
			<td>操作人</td>
		</tr>
		<tr>
			<td>索赔单申报</td>
			<td>${pg.SB_DATE}</td>
			<td>${pg.SB_NAME}</td>
		</tr>
		<tr>
			<td>索赔审核</td>
			<td>${pg.SH_DATE}</td>
			<td>${pg.SH_NAME}</td>
		</tr>
			<tr>
			<td>旧件运输处理</td>
			<td>${pg.HY_DATE}</td>
			<td>${pg.HY_NAME}</td>
		</tr>
			<tr>
			<td>旧件签收</td>
			<td>${pg.QS_DATE}</td>
			<td>${pg.QS_NAME}</td>
		</tr>
			<tr>
			<td>旧件审核</td>
			<td>${pg.RK_DATE}</td>
			<td>${pg.RK_NAME}</td>
		</tr>
			<tr>
			<td>开票通知</td>
			<td>${pg.KP_DATE}</td>
			<td>${pg.KP_NAME}</td>
		</tr>
			<tr>
			<td>收票确认</td>
			<td>${pg.SP_DATE}</td>
			<td>${pg.SP_NAME}</td>
		</tr>
			<tr>
			<td>验票确认</td>
			<td>${pg.YP_DATE}</td>
			<td>${pg.YP_NAME}</td>
		</tr>
			<tr>
			<td>财务转账</td>
			<td>${pg.ZZ_DATE}</td>
			<td>${pg.ZZ_NAME}</td>
		</tr>
</table>
<br>
<table width=100% border="0" cellspacing="0" cellpadding="0" style="text-align: center;">
		<tr>
			<td height="12" align=left width="33%">&nbsp;</td>
            	<td height="12" align=center width="33%" nowrap="true">
            	<c:if test="${t.STATUS==10791003 || t.STATUS==10791002}">
               	&nbsp;&nbsp;
               	<input type="button" class="normal_btn" id="surePass" onclick="sureConfirm(0);"  style="width=8%" value="审核通过" />
               	</c:if>
               	<c:if test="${t.STATUS==10791003 || t.STATUS==10791002}">
               	&nbsp;&nbsp;
               	<input type="button" class="normal_btn" id="sureUnpass" onclick="sureConfirm(1);"  style="width=8%" value="审核退回" />
               	</c:if>
               	<c:if test="${t.STATUS==10791003 || t.STATUS==10791002}">
               	&nbsp;&nbsp;
               	<input type="button" class="normal_btn" id="sureReject" onclick="sureConfirm(3);"  style="width=8%" value="审核拒绝" />
               	</c:if>
               	<c:if test="${t.STATUS==10791008}">
               	&nbsp;&nbsp;
				<input type="button" id="sureUndo" onClick="sureConfirm(2);" class="normal_btn"  style="width=8%" value="撤销审核"/>
				</c:if>
               	&nbsp;&nbsp;
				<input type="button" id="back" onClick="goBack();" class="normal_btn"  style="width=8%" value="关闭"/>
   			</td>
           	<td height="12" align=center width="33%">
     			</td>
		</tr>
</table>
</form>
</body>
</html>