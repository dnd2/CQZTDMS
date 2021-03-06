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
<script type="text/javascript" src="<%=contextPath%>/js/jslib/zyw/jquery-1.3.2.js"></script> 
<script type="text/javascript" src="<%=contextPath%>/js/jslib/zyw/jquery-calendar.js"></script> 
<link rel="stylesheet" type="text/css" href="<%=contextPath%>/js/jslib/zyw/jquery-calendar.css" /> 
<title>质保手册服务站</title>
<script type="text/javascript">
	$(function(){
		var type=$("#type").val();
		
		/* if("audit"==type){
			$(".tec").hide();
		 //   $("input[type='text']").attr("readonly","readonly");
			var status=$("#status").val();
			$("textarea").attr("readonly","readonly");
			$(".normal_btn").each(function(){
				 if($(this).val()=="删 除" || $(this).val()=="添加附件"){
					 $(this).hide();
				} 
			});
			if(20331002==status){
				$("#service_manager_deal").removeAttr("readonly");
			}
		} */
		
	});
	function audit(identify){
		var status=$("#status").val();
		if(0==identify){
			var audit_money =document.getElementById("audit_money");
			if(audit_money.value==""){
				MyAlert("请填写审核金额！");
				 //$(obj).val("");
				return;
			}
			if(""==$.trim($("#service_manager_deal").val())){
				MyAlert("请填写审核意见！");
				return;
			}		
			if(submitForm('fm')){
				var spje=document.getElementsByName("spje");
				var sumspje=0;
				for(var i=0;i<spje.length;i++){
					sumspje=accAdd(sumspje,spje[i].value);
				}
				var audit_money =document.getElementById("audit_money");
				if(parseFloat(sumspje)!=parseFloat(audit_money.value)){
					MyAlert("索赔金额总和不等于审核金额！");
					return;
				}
				MyConfirm("是否确认通过？",auditCommit,[0]);
			}
		}
		if(1==identify){//驳回
			if(""==$.trim($("#service_manager_deal").val())){
				MyAlert("请填写审核意见！");
				return;
			}
			MyConfirm("是否确认驳回给服务站？",auditCommit,[1]);
		}
		if(2==identify){//拒绝
			if(""==$.trim($("#service_manager_deal").val())){
				MyAlert("请填写审核意见！");
				return;
			}
			MyConfirm("是否确认拒绝？",auditCommit,[2]);
		}
	}
	
	function auditCommit(identify){
		var type=$("#type").val();
		$("#pass").attr("disabled",true);
		$("#rebut").attr("disabled",true);
		var url="<%=contextPath%>/GoodClaimAction/audit.json?type="+type+"&identify="+identify;
		makeNomalFormCall(url,auditCommitBack,"fm");
	}
	function auditCommitBack(json){
		var str="";
		if(json.identify=="0"){
			str+="审核通过";
		}
		if(json.identify=="1"){
			str+="审核驳回";
		}
		if(json.identify=="2"){
			str+="审核拒绝";
		}
		if(json.succ=="1"){
			MyAlert("提示："+str+"成功！");
			var urlTemp=$("#url").val();
			var url='<%=contextPath%>/GoodClaimAction/';
			url+=urlTemp;
			url+='.do';
			window.location.href=url;
		}else{
			MyAlert("提示："+str+"失败,请检查数据！");
			$("#pass").attr("disabled",false);
			$("#rebut").attr("disabled",false);
		}
	}
	function remove(){
	  $(".audit_money").removeAttr("readonly");
	}
	$(document).ready(function(){
		resetNum();
	}); 
	/**
	 * 删除本行
	 * @param _obj
	 */
	function deleteCurRow(_obj){
		var tr = $(_obj).parents("tr");
		$(tr).remove();
		resetNum();
	}

	/**
	 * 重置明细列表的序号
	 */
	function resetNum(){
		var tbody = $('#detailTbody');
		var nums = $(tbody).find(".num");
		$(nums).each(function(i){
			var n = i+1;
			$(this).html(n);
			$(this).parent().find('input[name=n]').val(n);	
			var cls = "table_list_row1";
			if(n%2==0){
				cls = "table_list_row2";
			}
			$(this).parent().attr('class',cls);
			$(this).parent().attr('id',null);
			
			$(this).parent().find('input[name=part_code_dealer]').attr('id',"part_code_dealer"+n);	
			$(this).parent().find('input[name=supply_code_dealer]').attr('id',"supply_code_dealer"+n);
		});
	}
	/**
	 * 增加一行
	 */
	function addRow(){
		var tb = $('#detailTbody');
		var modelTr = $('#modelTr');
		$(modelTr).clone().appendTo($(tb));
		resetNum();
	}
	function yf(){
		var isNum=/^(([1-9][0-9]{0,5})|(([0]\.\d{1,2}|[1-9][0-9]{0,5}\.\d{1,2})))$/;
		var audit_money=document.getElementById("audit_money").value;
		if(audit_money!=""){
			if(isNum.test(audit_money)){
				var audit_money_jl=document.getElementById("audit_money_jl").value;
				if(audit_money_jl=="" || audit_money_jl==null){
					var apply_money=document.getElementById("apply_money").value;
					if(parseFloat(audit_money)>parseFloat(apply_money)){
						MyAlert("审核金额不能大于申请金额!");
						document.getElementById("audit_money").value="";
						return ;
					}else{
						var spjes=document.getElementsByName("spje");
						for(var i=0;i<spjes.length;i++){
							spjes[i].value="";		
						}
						var bfbs=document.getElementsByName("bfb");
						for(var i=0;i<bfbs.length;i++){
							bfbs[i].value="";		
						}
					}
				}else{
					if(parseFloat(audit_money)>parseFloat(audit_money_jl)){
						MyAlert("审核金额不能大于上次审核金额!");
						document.getElementById("audit_money").value=audit_money_jl;
						return ;
					}else{
						var spjes=document.getElementsByName("spje");
						for(var i=0;i<spjes.length;i++){
							spjes[i].value="";		
						}
						var bfbs=document.getElementsByName("bfb");
						for(var i=0;i<bfbs.length;i++){
							bfbs[i].value="";		
						}
					}
				}
				
			}else{
				MyAlert("审核金额输入有误!");
				document.getElementById("audit_money").value="";
				return ;
			}	
		}		
	}
	function jeChange(obj){
		var tr = $(obj).parent().parent();
		var spjeobj = $(tr).find('input[name=spje]');
		$(spjeobj).val("");
		//判断数字
		var r = /^\+?[1-9][0-9]*$/;
		if(!r.test($(obj).val())) {
			MyAlert("输入有误！");
			$(obj).val("");
			return;
		}
		var audit_money =document.getElementById("audit_money");
		if(audit_money.value==""){
			MyAlert("请填写审核金额！");
			 $(obj).val("");
			return;
		}else{
			//判断填写百分率是最后一个时，用审核金额去减
			var spjes=document.getElementsByName("spje");
			var sum=audit_money.value;
			var sumnum=0;
			for(var i=0;i<spjes.length;i++){
				if(spjes[i].value!='0' && spjes[i].value!=""){
					sum=accAdd(sum,-spjes[i].value);
					sumnum+=1;
				}				
			}
			var bfbs=document.getElementsByName("bfb");
			var sumbfb=100;
			for(var i=0;i<bfbs.length;i++){
				if(bfbs[i].value!='0' && bfbs[i].value!=""){
					sumbfb=accAdd(sumbfb,-bfbs[i].value);
				}				
			}
			//只有一个
			if($("#plength").val()=='1'){
				if(obj.value>100){
					MyAlert("百分率不能大于100！");
					$(obj).val("");
					return;
				}else{
					var tr = $(obj).parent().parent();
					var spjeobj = $(tr).find('input[name=spje]');
					$(spjeobj).val(accMul(audit_money.value,obj.value/100).toFixed(2));
				}
			}else{
				if($("#plength").val()-sumnum==1){
					//最后一个总和去减
					if(sumbfb==0){
						var spjes1=document.getElementsByName("spje");
						for(var i=0;i<spjes1.length;i++){
							if(spjes1[i].value==""){
								spjes1[i].value=sum;
							}
						}
					}else{
						MyAlert("百分率输入有误！");
						$(obj).val("");
						var tr = $(obj).parent().parent();
						var spjeobj = $(tr).find('input[name=spje]');
						$(spjeobj).val("");
					}					
				}else{
					if(obj.value>=100){
						MyAlert("百分率只能小于100！");
						$(obj).val("");
						return;
					}else{
						var tr = $(obj).parent().parent();
						var spjeobj = $(tr).find('input[name=spje]');
						$(spjeobj).val(accMul(audit_money.value,obj.value/100).toFixed(2));
					}
				}
			}			
		}
	}
	//自定义浮点相乘
	function accMul(arg1,arg2)
	 {
	 var m=0,s1=arg1.toString(),s2=arg2.toString();
	 try{m+=s1.split(".")[1].length}catch(e){}
	 try{m+=s2.split(".")[1].length}catch(e){}
	 return Number(s1.replace(".",""))*Number(s2.replace(".",""))/Math.pow(10,m)
	 } 
	//自定义浮点数相加
	function accAdd(arg1,arg2){
	    var r1,r2,m,n; 
	    try{r1=arg1.toString().split(".")[1].length}catch(e){r1=0} 
	    try{r2=arg2.toString().split(".")[1].length}catch(e){r2=0} 
	    m=Math.pow(10,Math.max(r1,r2));
	    n=(r1>=r2)?r1:r2;
	    return ((arg1*m+arg2*m)/m).toFixed(n);  
	} 
</script>
</head>
<body>
<div class="navigation">
<img src="../jsp_new/img/nav.gif" />&nbsp;当前位置：售后服务管理&gt;特殊费用管理&gt;特殊费用(善意索赔)
</div>
<form name="fm" id="fm" method="post">
<input class="middle_txt" id="specialNo" value="${specialNo }" type="hidden"  />
<input type="hidden" id="dealerCode" value="${dealerCode }">
<input type="hidden" id="status" name="status" value="${t.STATUS }">
<input type="hidden" id="is_claim" value="${t.IS_CLAIM }"/>
<input class="middle_txt" id="type" value="${type }" type="hidden"  />
<input class="middle_txt" id="url" value="${url }" type="hidden"  />
<input class="middle_txt" id="spe_id" value="${t.SPE_ID }" name="spe_id" type="hidden"  />
<input class="middle_txt" id="special_type" value="1" name="special_type" type="hidden"  />
<input class="middle_txt" id="amountOffline" value="${lamount.AMOUNT_OFFLINE }" name="amountOffline" type="hidden"  />
<input class="middle_txt" id="amountCeil" value="${lamount.AMOUNT_CEIL}" name="amountCeil" type="hidden"  />
<table border="0" cellpadding="0" cellspacing="0"  width="100%" >
	<tr>
		<td width="35%" nowrap="true" align="center" >
<%--      		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<img alt="北汽银翔" width="100px;"  height="60px;" src="<%=request.getContextPath()%>/img/bqyx.png"> --%>
     	</td>
     	<td width="35%" nowrap="true" align="center">
     		<span style="font-weight: bold;font-size: 25px;">善意索赔申请单</span>
     	</td>
     	<td width="30%" nowrap="true" align="right">
     		申请单号:<span id="apply_no">${t.APPLY_NO }</span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input class="middle_txt" id="specialNoAdd" name="apply_no" value="${t.APPLY_NO }" type="hidden"  />
     	</td>
	</tr>
</table>
<table border="1" cellpadding="1" cellspacing="1"  class="tab_edit" width="100%" style="text-align: center;">
	<tr height="30px;">
		<td width="35%" nowrap="true" align="center">
     		服务商简称（盖公章）
     	</td>
     	<td width="20%" nowrap="true" align="center">
     		服务商代码
     	</td>
     	<td width="25%" nowrap="true" align="center">
     		服务商联系人
     	</td>
     	<td width="15%" nowrap="true" align="center">
     		<input class="middle_txt" maxlength="30" id="dealer_contact" name="dealer_contact" value="${t.DEALER_CONTACT }" type="hidden"  /><span>${t.DEALER_CONTACT }</span>
     	</td>
	</tr>
	<tr height="30px;">
		<td width="35%" nowrap="true" align="center">
     		${t.DEALER_SHORTNAME }
     	</td>
     	<td width="20%" nowrap="true" align="center">
     		<input type="hidden" name="dealer_code" value="${t.DEALER_CODE }"><span id="dealer_code">${t.DEALER_CODE }</span>
     	</td>
     	<td width="25%" nowrap="true" align="center">
     		服务商联系电话
     	</td>
     	<td width="15%" nowrap="true" align="center">
     		<input class="middle_txt" maxlength="30" id="dealer_phone" name="dealer_phone" value="${t.DEALER_PHONE }" type="hidden"  /><span>${t.DEALER_PHONE }</span>
     	</td>
	</tr>
</table>
<table border="1" cellpadding="1" cellspacing="1"   class="tab_edit" width="100%" style="text-align: center;font-weight: bold;" >
	<tr height="30px;">  	
     	<td width="5%" nowrap="true" ></td>
     	<td width="12.5%" nowrap="true" >
     		是否索赔：
     	</td>
     	<td width="17.5%" nowrap="true" >
     		<c:if test="${t.IS_CLAIM==1 }">
     			是
     		</c:if>
     		<c:if test="${t.IS_CLAIM==0 }">
     			否
     		</c:if>
     	</td>
     	<td width="12.5%" nowrap="true" >
     	</td>
     	<td width="17.5%" nowrap="true" >
     	</td>
     	<td width="12.5%" nowrap="true" >
     	</td>
     	<td width="17.5%" nowrap="true" >
     	</td>
     	<td width="5%" nowrap="true" ></td>
	</tr>
</table>
<table border="1" cellpadding="1" cellspacing="1"  class="tab_edit" width="100%" style="text-align: center;">
	<tr height="30px;">
		<td width="5%" nowrap="true" ></td>
     	<td width="12.5%" nowrap="true" >
     		索赔单号：
     	</td>
     	<td width="17.5%" nowrap="true" >
     		<input class="middle_txt" readonly="readonly" id="claim_no" name="claim_no" value="${t.CLAIM_NO }" type="hidden"  /><span>${t.CLAIM_NO }</span>
     	</td>
     	<td width="12.5%" nowrap="true" >
     	</td>
     	<td width="17.5%" nowrap="true" >
     	</td>
     	<td width="12.5%" nowrap="true" >
     	</td>
     	<td width="17.5%" nowrap="true" >
     	</td>
     	<td width="5%" nowrap="true" ></td>
	</tr>
	<tr height="30px;">
		<td width="5%" nowrap="true" ></td>
     	<td width="12.5%" nowrap="true" >
     		VIN码：
     	</td>
     	<td width="17.5%" nowrap="true" >
     		<input class="middle_txt" id="vin" name="vin" maxlength="30" value="${t.VIN }" type="hidden"  /><span>${t.VIN }</span>
     	</td>
     	<td width="12.5%" nowrap="true" >
     		车型：
     	</td>
     	<td width="17.5%" nowrap="true" >
     		<span id="carType">${t.MODEL_NAME }</span>
     	</td>
     	<td width="12.5%" nowrap="true" >
     		行驶里程：
     	</td>
     	<td width="17.5%" nowrap="true" >
     		<input class="middle_txt" id="mileage" name="mileage" maxlength="30" value="${t.MILEAGE }" type="hidden"  /><span>${t.MILEAGE }</span>
     	</td>
     	<td width="5%" nowrap="true" ></td>
	</tr>
	<tr height="30px;">
		<td width="5%" nowrap="true" ></td>
     	<td width="12.5%" nowrap="true" >
     		生产日期：
     	</td>
     	<td width="17.5%" nowrap="true" >
     		<span id="productDate">${t.PRODUCT_DATE }</span>
     	</td>
     	<td width="12.5%" nowrap="true" >
     		购车日期：
     	</td>
     	<td width="17.5%" nowrap="true" >
     		<span id="buyCarDate">${t.BUY_DATE }</span>
     	</td>
     	<td width="12.5%" nowrap="true" >
     		故障发生日期：
     	</td>
     	<td width="17.5%" nowrap="true" >
     		<input class="middle_txt" id="problem_date" name="problem_date"  value="<fmt:formatDate value="${t.PROBLEM_DATE}" pattern='yyyy-MM-dd HH:mm'/>" readonly="readonly"  onfocus="$(this).calendar()" type="hidden"  /><span><fmt:formatDate value="${t.PROBLEM_DATE}" pattern='yyyy-MM-dd HH:mm'/></span>
     	</td>
     	<td width="5%" nowrap="true" ></td>
	</tr>
	<tr height="30px;">
		<td width="5%" nowrap="true" ></td>
     	<td width="12.5%" nowrap="true" >
     		姓名：
     	</td>
     	<td width="17.5%" nowrap="true" >
     		<span id="userName">${t.CTM_NAME }</span>
     	</td>
        <td width="12.5%" nowrap="true" >电话：</td>
     	<td width="17.5%" nowrap="true" ><span id="userPhone">${t.PHONE }</span></td>  	
     	<td width="12.5%" nowrap="true" >
     		职业：
     	</td>
     	<td width="17.5%" nowrap="true" >
     		<input class="middle_txt" id="job" name="job" maxlength="30" value="${t.JOB }" type="hidden"  /><span>${t.JOB }</span>
     	</td>
     	<td width="5%" nowrap="true" ></td>
	</tr>
	<tr height="30px;">
		<td width="5%" nowrap="true" ></td>
     	<td width="12.5%" nowrap="true" >
     		地址：
     	</td>
     	<td colspan="5">
     		<span id="userAddr">${t.ADDRESS }</span>
     	</td>
     	<td width="5%" nowrap="true" ></td>
     </tr>
     <tr height="30px;">
     	<td width="5%" nowrap="true" ></td>
     	<td width="12.5%" nowrap="true" >
     		申请金额(元)：
     	</td>
     	<td width="17.5%" nowrap="true" >
     		<input class="middle_txt" id="apply_money" name="apply_money" maxlength="30" value="${t.APPLY_MONEY }" type="hidden"  /><span>${t.APPLY_MONEY }</span>
     	</td>
     	<td width="12.5%" nowrap="true" >
     		<font color="red">审核</font>金额(元)：
     	</td>
     	<td width="17.5%" nowrap="true" align="left">
     		<input  id="audit_money" name="audit_money" onblur="yf();" class="middle_txt" type="text" value="${t.AUDIT_AMOUNT }"/>
     		<input  id="audit_money_jl" name="audit_money_jl" class="middle_txt" type="hidden" value="${t.AUDIT_AMOUNT }"/>
     	</td>
     	<td width="12.5%" nowrap="true" >
     		车牌号：
     	</td>
     	<td width="17.5%" nowrap="true" >
     		<span id="carNo">${t.LICENSE_NO }</span>
     	</td>
     	<td width="5%" nowrap="true"></td>
	</tr>
</table>
<table border="1" cellpadding="1" cellspacing="1"  class="tab_edit" width="100%" style="text-align: center;">
	<tr height="30px;">
		<td width="5%" nowrap="true" ></td>
     	<td  width="12.5%" nowrap="true" style="font-weight: bold;" >
     		事件主题:
    	</td>
    	<td  width="77.5%">
    		<textarea style="width: 100%;height: 40px;" readonly="readonly" id="event_theme" name="event_theme" >${t.EVENT_THEME }</textarea>
    	</td>
    	<td width="5%" nowrap="true" ></td>
    </tr>
    <tr height="30px;">
		<td width="5%" nowrap="true" ></td>
     	<td  width="12.5%" nowrap="true" >
     		投诉内容及车主意见:
    	</td>
    	<td  width="77.5%">
    		<textarea style="width: 100%;height: 40px;" readonly="readonly" id="complain_advice" name="complain_advice" >${t.COMPLAIN_ADVICE }</textarea>
    	</td>
    	<td width="5%" nowrap="true" ></td>
    </tr>
    <tr height="30px;">
		<td width="5%" nowrap="true" ></td>
     	<td width="12.5%" nowrap="true" >
     		审核意见:
    	</td>
    	<td width="77.5%">
    		<textarea style="width: 100%;height: 40px;" id="service_manager_deal" name="service_manager_deal"></textarea>
    	</td>
    	<td width="5%" nowrap="true" ></td>
    </tr>
    </table>
    <br>
    <!-- 主因件信息 -->
  <div class="form-panel" id="parts_message">
  <h2>&nbsp;<img src="<%=contextPath%>/img/nav.gif" />主因件信息</h2>
    <div class="form-body">
    <table border="0" style="text-align:center;" cellpadding="1" cellspacing="1" class="table_list" style="border-bottom:1px solid #DAE0EE;" ondrag="return false;">
    	<thead>		
			<th style="text-align:center" width="20%" >序号</th>
			<th style="text-align:center" width="20%">主因件代码</th>
			<th style="text-align:center" width="20%">主因件名称</th>
			<th style="text-align:center" width="20%">供应商代码</th>
			<th style="text-align:center" width="20%">供应商名称</th>
			<th style="text-align:center" width="20%">供应商索赔百分比</th>
			<th style="text-align:center" width="20%">索赔金额</th>
		</thead>	
		<tbody id="detailTbody">
			 <input type="hidden" id="plength" value="${psLength }"/>
			<c:forEach items="${psList}" var="ps">
				<tr class="table_list_row1">
					<td width="5%" class="num" align="center"></td> 
					<td width="12.5%" align="center" >${ps.PART_CODE_DEALER }</td>
					<td width="12.5%" align="center" >${ps.PART_NAME_DEALER }</td>
     				<td width="12.5%" align="center" >${ps.SUPPLY_CODE_DEALER }</td>	
     				<td width="12.5%" align="center" >${ps.SUPPLY_NAME_DEALER }</td>
     				<td width="12.5%" align="center" ><input type="text" id="j" onchange="jeChange(this);" value="${ps.PERCENT }" name="bfb">&nbsp;&nbsp;<font color="red">%</font></td> 
     				<td width="12.5%" align="center" ><input type="text" id="s" name="spje" value="${ps.CLAIM_AMOUNT }" readonly="readonly"></td> 							
				</tr>
			</c:forEach>
		</tbody>
    </table>
    </div>
  </div><br>
<!-- 添加附件 开始 -->
  <div class="form-panel" id="parts_message">
  <h2>&nbsp;<img src="<%=contextPath%>/img/nav.gif" />附件信息</h2>
    <div class="form-body">
    <table border="0" style="text-align:left;" cellpadding="1" cellspacing="1" class="table_list" style="border-bottom:1px solid #DAE0EE;" ondrag="return false;">
			<tr>
				<jsp:include page="${contextPath}/uploadDiv.jsp" />
			</tr>
			<%if(fileList!=null){
			for(int i=0;i<fileList.size();i++) { %>
	  		<script type="text/javascript">
    			addUploadRowByDb('<%=CommonUtils.checkNull(fileList.get(i).getFilename()) %>','<%=CommonUtils.checkNull(fileList.get(i).getFjid()) %>','<%=CommonUtils.checkNull(fileList.get(i).getFileurl())%>','<%=CommonUtils.checkNull(fileList.get(i).getFjid()) %>','<%=request.getContextPath()%>/util/FileDownLoad/FileDownLoadQuery.do?fjid=' );
    		</script>
		<%}}%>
	</table> 
	</div>
	</div>
<!-- 添加附件 结束 -->
<br>		
<table width="100%" border="0" cellspacing="0" cellpadding="0">
		<tr>
			<td height="12" align=left width="33%">&nbsp;</td>
            	<td height="12" align=center width="33%" nowrap="true">
               	&nbsp;&nbsp;
               	<input type="button" class="normal_btn" id="pass" onclick="audit(0);"  style="width=8%" value="通过" />
               	&nbsp;&nbsp;
               	<input type="button" class="normal_btn" id="rebut" onclick="audit(1);"  style="width=8%" value="驳回" />
               	&nbsp;&nbsp;
               	<input type="button" class="normal_btn" id="rebut" onclick="audit(2);"  style="width=8%" value="拒绝" />
               	&nbsp;&nbsp;
				<input type="button" id="back" onClick="history.back();" class="normal_btn"  style="width=8%" value="返回"/>
   			</td>
           	<td height="12" align=center width="33%">
     			</td>
		</tr>
</table>
<br>
<!-- 审核记录 -->
  <div class="form-panel">
  <h2>&nbsp;<img src="<%=contextPath%>/img/nav.gif" />审核记录</h2>
    <div class="form-body">
    <table border="0" id="detailTable" align="center" cellpadding="1" cellspacing="1" class="table_list" style="border-bottom:1px solid #DAE0EE;" ondrag="return false;">
    <tr class="table_list_th">
    		<th style="text-align:center" width="20%" class="noSort">审核时间</th>
			<th style="text-align:center" width="40%" class="noSort">审核意见</th>
			<th style="text-align:center" width="20%" class="noSort">审核人</th>
			<th style="text-align:center" width="20%" class="noSort">审核状态</th>
    	</tr>
		<c:forEach items="${rList}" var="applyR">
			<tr class="table_list_row2">
				<td style="background-color:#FFFFFF;">${applyR.AUDIT_DATE}</td>
				<td style="background-color:#FFFFFF;">${applyR.AUDIT_RECORD}</td>
				<td style="background-color:#FFFFFF;">${applyR.NAME}</td>
				<td style="background-color:#FFFFFF;">${applyR.OPERA_STSTUS}</td> 
			</tr> 
		</c:forEach>
    </table>
    </div>
  </div>
</form>
</body>
</html>