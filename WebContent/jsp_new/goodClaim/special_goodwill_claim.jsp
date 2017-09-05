<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/fmt" prefix="fmt"%>
<%@ page import="com.infodms.dms.common.Constant" %>
<%@ page import="com.infodms.dms.common.FileConstant"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
	String contextPath = request.getContextPath();
%>
<head> 
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<script type="text/javascript" src="<%=request.getContextPath()%>/js/web/common.js"></script>
<script type="text/javascript" src="<%=contextPath%>/js/jslib/jquery-1.7.2.js"></script>
<script type="text/javascript" src="<%=contextPath%>/js/jslib/zyw/jquery-calendar.js"></script> 
<link rel="stylesheet" type="text/css" href="<%=contextPath%>/js/jslib/zyw/jquery-calendar.css" /> 
<title>质保手册服务站</title>
<script type="text/javascript">
	$(function(){
		var type=$("#type").val();
		if("add"==type){
			$("#apply_no").text($("#specialNo").val());
			$("#specialNoAdd").val($("#specialNo").val());
			
			$("#dealer_code").text($("#dealerCode").val());
			$("input[name='is_claim']").each(function(){
				var is_claim=$(this).val();
				bindisClaim(is_claim,$(this));
			});
		}
		if("update"==type){
			var isR=$("#is_claim").val();
			if(1==isR){
				$("#vin").attr("readonly","readonly");
			}else{
				$("#vin").removeAttr("readonly");
			}
			$("input[name='is_claim']").each(function(){
				var is_claim=$(this).val();
				bindisClaim(is_claim,$(this));
			});
			$("#part_code_dealer").bind("click",function(){
				OpenHtmlWindow('<%=contextPath%>/jsp_new/goodClaim/spe_show_part_code.jsp',800,500);
			});
			$("#supply_code_dealer").bind("click",function(){
				OpenHtmlWindow('<%=contextPath%>/jsp_new/goodClaim/spe_show_supplier_code.jsp',800,500);
			});
		}
		isradio($("#is_claim").val(),"is_claim");
		if("view"==type){
			$("input[type='text']").attr("readonly","readonly");
			$("select").attr("disabled",true);
			$("textarea").attr("readonly","readonly");
			$(".normal_btn").each(function(){
				 if($(this).val()=="删 除" || $(this).val()=="添加附件"){
					 $(this).hide();
				} 
			});
			$("input[name='del']").each(function(){
				$(this).attr("disabled",true);
			});
			$("#add").attr("disabled",true);
			$("#sure,#report").hide();
		}
		
	});
 	//设置供应商
	function setSupplierCode(maker_code,maker_shotname,id,id_name){
		document.getElementById(id).value=maker_code;
		document.getElementById(id_name).value=maker_shotname;
	}
	//设置主因件
	function setPartCode(part_code,part_name,id,id_name){
		document.getElementById(id).value=part_code;
		document.getElementById(id_name).value=part_name;
	}   
	function bindisClaim(is_claim,obj){
		if("1"==is_claim){
			$(obj).bind("click",function(){
				$("#vin").unbind("");
				$("#vin").attr("readonly","readonly");
				OpenHtmlWindow('<%=contextPath%>/jsp_new/goodClaim/special_gooodwill_qurey_claim.jsp',800,500);
			});
		}
		if("0"==is_claim){
			$(obj).bind("click",function(){
				$("#carType").text("");//车型
				$("#carNo").text("");//车牌号
				$("#productDate").text("");//生产日期
				$("#buyCarDate").text("");//购车日期
				//$("#userPhone").text("");//联系电话
				$("#userName").text("");//用户姓名
				$("#userAddr").text("");//用户地址
				$("#vin").val("");
				$("#claim_no").val("");
				$("#vin").removeAttr("readonly");
				$("#vin").focus();
				$("#vin").bind("blur",function(){
					findDataByVin();
				});
			});
		}
		
	}
	function choose_claim(claim_no,vin){
		$("#claim_no").val(claim_no);
		$("#vin").val(vin);
		findDataByVin();
	}
	function findDataByVin(){
		var vin=$("#vin").val();
		if(""!=vin){
			var url="<%=contextPath%>/GoodClaimAction/findDataByVin.json?vin="+vin;
			sendAjax(url,function(json){
				var t=json.dataByVin;
				if(null!=t){
					$("#carType").text(getNull(t.MODEL_NAME));//车型
					$("#carNo").text(getNull(t.LICENSE_NO));//车牌号
					$("#productDate").text(getNull(t.PRODUCT_DATE));//生产日期
					$("#buyCarDate").text(getNull(t.BUY_DATE));//购车日期
					//$("#userPhone").text(getNull(t.PHONE));//联系电话
					$("#userName").text(getNull(t.CTM_NAME));//用户姓名
					$("#userAddr").text(getNull(t.ADDRESS));//用户地址
					$("#vin").val(t.VIN);
				}else{
					$("#carType").text("");//车型
					$("#carNo").text("");//车牌号
					$("#productDate").text("");//生产日期
					$("#buyCarDate").text("");//购车日期
					//$("#userPhone").text("");//联系电话
					$("#userName").text("");//用户姓名
					$("#userAddr").text("");//用户地址
					MyAlert("此VIN号无法带出数据！");
					$("#vin").val("");
				}
			},"fm");
		}
	}
	//null返回“”
	function getNull(data) {
		if (data==null) {
			return '';
		}else {
			return data;
		}
	}
	function sureInsert(identify){
		var temp=0;
		
		var strRadio="is_claim";
		var textStrRadio="是否索赔";
		var strRadios=strRadio.split(",");
		var textStrRadios= textStrRadio.split(",");
		var alertStr="";
		for(var j=0;j<strRadios.length;j++){
			if($('input:radio[name="'+strRadios[j]+'"]:checked').val()==null){
				alertStr+= " ["+textStrRadios[j]+"]";
			}
		}
		if(alertStr!=""){
			MyAlert("提示：请选择{"+alertStr+"}");
			temp++;
			return;
		}
		var dealer_contact=$.trim($("#dealer_contact").val());
		if( ""==dealer_contact){
			MyAlert("提示：请填写服务商联系人!");
			temp++;
			return;
		}
		var dealer_phone=$.trim($("#dealer_phone").val());
		if( ""==dealer_phone){
			MyAlert("提示：请填写服务商联系电话!");
			temp++;
			return;
		}
		var problem_date=$.trim($("#problem_date").val());
		if( ""==problem_date){
			MyAlert("提示：请填写故障日期!");
			temp++;
			return;
		}
		var mileage=$.trim($("#mileage").val());
		if( ""==mileage){
			MyAlert("提示：请填写行驶里程!");
			temp++;
			return;
		}
		var vin=$.trim($("#vin").val());
		if( ""==vin && ""==$("#buyCarDate").text()){
			MyAlert("提示：请填写VIN并带出正确的数据!");
			temp++;
			return;
		}
		var apply_money=$.trim($("#apply_money").val());
		if( ""==apply_money){
			MyAlert("提示：请填写申请金额!");
			temp++;
			return;
		}
		
		/* var job=$.trim($("#job").val());
		if( ""==job){
			MyAlert("提示：请填写职业!");
			temp++;
			return;
		} */
		var event_theme=$.trim($("#event_theme").val());
		if( ""==event_theme){
			MyAlert("提示：请填写事件主题!");
			temp++;
			return;
		}
		var complain_advice=$.trim($("#complain_advice").val());
		if( ""==complain_advice){
			MyAlert("提示：请填写投诉内容及车主意见!");
			temp++;
			return;
		}
		if(document.getElementsByName("part_code_dealer").length<2){
			MyAlert("提示：请选择主因件代码!");
			temp++;
			return;
		}
		if(document.getElementsByName("supply_code_dealer").length<2){
			MyAlert("提示：请选择供应商代码!");
			temp++;
			return;
		}
		if(temp==0){
			if(0==identify){
				if(submitForm('fm')){
					//选择重复验证
					var pcd=document.getElementsByName("part_code_dealer");
					for(var i=0;i<pcd.length;i++){
						for(var j=pcd.length-1;i<j;j--){
							if(pcd[i].value!="" && pcd[j].value!="" && pcd[i].value==pcd[j].value){
								MyAlert("主因件代码("+pcd[i].value+")选择重复");
								return;
							}
						}
					}
					/* var scd=document.getElementsByName("supply_code_dealer");
					for(var i=0;i<scd.length;i++){
						for(var j=scd.length-1;i<j;j--){
							if(scd[i].value!=null && scd[j].value!=null && scd[i].value==scd[j].value){
								MyAlert("供应商代码("+scd[i].value+")选择重复");
								return;
							}
						}
					} */
					MyConfirm("是否确认保存？",sureInsertCommit,[0]);
				}				
			}
			if(1==identify){
			    if(submitForm('fm')){
			    	var pcd=document.getElementsByName("part_code_dealer");
					for(var i=0;i<pcd.length;i++){
						for(var j=pcd.length-1;i<j;j--){
							if(pcd[i].value!="" && pcd[j].value!="" && pcd[i].value==pcd[j].value){
								MyAlert("主因件代码("+pcd[i].value+")选择重复");
								return;
							}
						}
					}
					/* var scd=document.getElementsByName("supply_code_dealer");
					for(var i=0;i<scd.length;i++){
						for(var j=scd.length-1;i<j;j--){
							if(scd[i].value!=null && scd[j].value!=null && scd[i].value==scd[j].value){
								MyAlert("供应商代码("+scd[i].value+")选择重复");
								return;
							}
						}
					} */
					MyConfirm("是否确认上报？",sureInsertCommit,[1]);
			    }
			}
		}
	}
	function sureInsertCommit(identify){
		$("#sure").attr("disabled",true);
		$("#report").attr("disabled",true);
		var url="<%=contextPath%>/GoodClaimAction/saveOrUpdate.json?identify="+identify;
		makeNomalFormCall(url,sureInsertCommitBack,"fm");
	}
	function sureInsertCommitBack(json){
		var str="";
		if(json.identify=="0"){
			str+="保存";
		}
		if(json.identify=="1"){
			str+="上报";
		}
		if(json.succ=="1"){
			MyAlert("提示："+str+"成功！");
			var url='<%=contextPath%>/GoodClaimAction/specialDealerList.do';
			window.location.href=url;
		}else{
			MyAlert("提示："+str+"失败！");
			$("#sure").attr("disabled",false);
			$("#report").attr("disabled",false);
		}
	}
	 /* function checkVin(obj) {
		var vin=obj.value;
		if (vin == "" || vin.trim().length == 0) {
			MyAlert("车架号不能为空！");
			return ;
		}
		makeNomalFormCall(
				globalContextPath
						+ "/GoodClaimAction/checkVin.json?vin="
						+ vin, showResult, "fm");
	}
	function showResult(obj) {
		if (obj.msg == "01") {
			MyAlert("车架号有误！");
			document.getElementById("vin").value="";
		}
	}  */
	function yf(){
		var isNum=/^(([1-9][0-9]{0,5})|(([0]\.\d{1,2}|[1-9][0-9]{0,5}\.\d{1,2})))$/;
		var apply_money=document.getElementById("apply_money").value;
		if(apply_money!=""){
			if(isNum.test(apply_money)){				
			}else{
				MyAlert("申请金额输入有误!");
				document.getElementById("apply_money").value="";
				return ;
			}	
		}
	}
	//公共的radio
	function isradio(val,className){
		$("."+className).each(function(){
			if(val==$(this).val()){
				$(this).attr("checked",true);
			}
		});
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
			$(this).parent().find('input[name=part_name_dealer]').attr('id',"part_name_dealer"+n);
			$(this).parent().find('input[name=supply_code_dealer]').attr('id',"supply_code_dealer"+n);
			$(this).parent().find('input[name=supply_name_dealer]').attr('id',"supply_name_dealer"+n);
			
		});
		if(document.getElementsByName("supply_code_dealer").length<2){
			document.getElementById("div_detailThead").style.display="none";
		}else{
			document.getElementById("div_detailThead").style.display="block";
		}
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
	function onPart(obj){
			var tr = $(obj).parent().parent();
			var ipt = $(tr).find('input[name=n]');
			var id='part_code_dealer'+$(ipt).val();
			var id_name='part_name_dealer'+$(ipt).val();
			OpenHtmlWindow(globalContextPath +"/jsp_new/goodClaim/spe_show_part_code.jsp?id_name="+id_name+"&id="+id,800,500);
	}
	function onSupply(obj){	
			var tr = $(obj).parent().parent();
			var ipt = $(tr).find('input[name=n]');	
			var id="supply_code_dealer"+$(ipt).val();
			var id_name="supply_name_dealer"+$(ipt).val();
			var old_code=$('#part_code_dealer'+$(ipt).val()).val();
			if(old_code!=null && old_code!=""){
				OpenHtmlWindow(globalContextPath +"/jsp_new/goodClaim/spe_show_supplier_code.jsp?old_code="+old_code+"&id_name="+id_name+"&id="+id,800,500);
			}else{
				MyAlert("主因件不能为空!");
			}
			
	}
</script>
</head>
<body>
<div class="navigation">
<img src="../jsp_new/img/nav.gif" />&nbsp;当前位置：售后服务管理&gt;特殊费用管理&gt;善意索赔申请
</div>
<form name="fm" id="fm" method="post">
<input class="middle_txt" id="specialNo" value="${specialNo }" type="hidden"  />
<input type="hidden" id="dealerCode" value="${dealerCode }">
<input type="hidden" id="is_claim" value="${t.IS_CLAIM }"/>
<input class="middle_txt" id="type" value="${type }" type="hidden"  />
<input class="middle_txt" id="spe_id" value="${t.SPE_ID }" name="spe_id" type="hidden"  />
<input class="middle_txt" id="special_type" value="1" name="special_type" type="hidden"  />
<table border="0" cellpadding="0" cellspacing="0"  width="100%" >
	<tr>
		<td width="35%" nowrap="true" style="text-align:center;" >
     		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<img alt="北汽银翔" width="100px;"  height="60px;" src="<%=request.getContextPath()%>/img/bqyx.png">
     	</td>
     	<td width="35%" nowrap="true" style="text-align:center;">
     		<span style="font-weight: bold;font-size: 25px;">善意索赔申请单</span>
     	</td>
     	<td width="30%" nowrap="true" align="right">
     		申请单号:<span id="apply_no">${t.APPLY_NO }</span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input class="middle_txt" id="specialNoAdd" name="apply_no" value="${t.APPLY_NO }" type="hidden"  />
     	</td>
	</tr>
</table>
<table border="1" cellpadding="1" cellspacing="1"  class="tab_edit" width="100%" style="text-align: center;">
	<tr height="30px;">
		<td width="35%" nowrap="true" style="text-align:center;">
     		服务商简称（盖公章）
     	</td>
     	<td width="20%" nowrap="true" style="text-align:center;">
     		服务商代码
     	</td>
     	<td width="25%" nowrap="true" style="text-align:center;">
     		服务商联系人
     	</td>
     	<td width="15%" nowrap="true" style="text-align:center;">
     		<input class="middle_txt" maxlength="30" id="dealer_contact" name="dealer_contact" value="${t.DEALER_CONTACT }" type="text"  />
     	</td>
	</tr>
	<tr height="30px;">
		<td width="35%" nowrap="true" style="text-align:center;">
     		
     	</td>
     	<td width="20%" nowrap="true" style="text-align:center;">
     		<input type="hidden" name="dealer_code" value="${t.DEALER_CODE }"><span id="dealer_code">${t.DEALER_CODE }</span>
     	</td>
     	<td width="25%" nowrap="true" style="text-align:center;">
     		服务商联系电话
     	</td>
     	<td width="15%" nowrap="true" style="text-align:center;">
     		<input class="middle_txt" maxlength="30" id="dealer_phone" name="dealer_phone" value="${t.DEALER_PHONE }" type="text"  />
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
     		<input id="is_claim_yes" type="radio" class="is_claim" name="is_claim" value="1"/>是&nbsp;&nbsp;&nbsp;<input id="is_claim_no" class="is_claim" name="is_claim" type="radio" value="0"/>否
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
     		<input class="middle_txt" readonly="readonly" id="claim_no" name="claim_no" value="${t.CLAIM_NO }" type="text"  />
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
     		<input class="middle_txt" id="vin" name="vin" maxlength="30" value="${t.VIN }" type="text"  />
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
     		<input class="middle_txt" id="mileage" name="mileage" maxlength="30" value="${t.MILEAGE }" type="text"  />
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
     		<input class="middle_txt" id="problem_date" name="problem_date"  value="<fmt:formatDate value="${t.PROBLEM_DATE}" pattern='yyyy-MM-dd HH:mm'/>" readonly="readonly"  onfocus="$(this).calendar()" type="text"  />
     	</td>
     	<td width="5%" nowrap="true" ></td>
	</tr>
	<!-- <tr height="22px;">
		<td width="5%" nowrap="true" ></td>
     	<td width="12.5%" nowrap="true" style="font-weight: bold;">
     		车主信息
     	</td>
     	<td colspan="5"></td>
     	<td width="5%" nowrap="true" ></td>
	</tr> -->
	<tr>
		<td width="5%" nowrap="true" ></td>
     	<td width="12.5%" nowrap="true" >
     		姓名：
     	</td>
     	<td width="17.5%" nowrap="true" >
     		<span id="userName">${t.CTM_NAME }</span>
     	</td>
     	<td width="12.5%" nowrap="true" >
     		车牌号：
     	</td>
     	<td width="17.5%" nowrap="true" >
     		<span id="carNo">${t.LICENSE_NO }</span>
     	</td>
     	<td width="12.5%" nowrap="true" >
     		职业：
     	</td>
     	<td width="17.5%" nowrap="true" >
     		<input class="middle_txt" id="job" name="job" maxlength="30" value="${t.JOB }" type="text"  />
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
     <tr>
		<td width="5%" nowrap="true" ></td>
     	<td width="12.5%" nowrap="true" >
     		申请金额：
     	</td>
     	<td width="17.5%" nowrap="true" >
     		<input class="middle_txt" id="apply_money" name="apply_money" onchange="yf();" maxlength="30" value="${t.APPLY_MONEY }" type="text"  />
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
     	<td  width="12.5%" nowrap="true" style="font-weight: bold;" >
     		事件主题:
    	</td>
    	<td  width="77.5%">
    		<textarea style="width: 100%;height: 40px;" id="event_theme" name="event_theme" >${t.EVENT_THEME }</textarea>
    	</td>
    	<td width="5%" nowrap="true" ></td>
    </tr>
    <tr height="30px;">
		<td width="5%" nowrap="true" ></td>
     	<td  width="12.5%" nowrap="true" >
     		投诉内容及车主意见:
    	</td>
    	<td  width="77.5%">
    		<textarea style="width: 100%;height: 40px;" id="complain_advice" name="complain_advice" >${t.COMPLAIN_ADVICE }</textarea>
    	</td>
    	<td width="5%" nowrap="true" ></td>
    </tr>
</table>
<br>
<!-- 主因件信息 -->
  <div class="form-panel" id="parts_message">
  <h2>&nbsp;<img src="<%=contextPath%>/img/nav.gif" />主因件信息&nbsp;<input name="button" type="button" class="short_btn" onclick="addRow();" value="增加一行"/></h2>
    <div class="form-body">
    <div id="div_detailThead" style="display: none;">
    <table border="0" style="text-align:center;" cellpadding="1" cellspacing="1" class="table_list" style="border-bottom:1px solid #DAE0EE;" ondrag="return false;">
    	<thead class="table_list_th">	
			<th class="noSort" style="width: 10%;">序号</th>
			<th class="noSort" style="width: 10%;">操作</th>
			<th class="noSort" style="width: 20%;">主因件代码</th>
			<th class="noSort" style="width: 20%;">主因件名称</th>
			<th class="noSort" style="width: 20%;">供应商代码</th>
			<th class="noSort" style="width: 20%;">供应商名称</th>	
		</thead>
    	</table>
    </div>
    <table border="0" style="text-align:center;" cellpadding="1" cellspacing="1" class="table_list" style="border-bottom:1px solid #DAE0EE;" ondrag="return false;">		
		<tbody id="detailTbody">
			<c:forEach items="${psList}" var="ps"> 
				<tr class="table_list_row1">
					<td style="width: 10%;" class="num"></td>
					<td style="width: 10%;"><input type="hidden" name="n" ><input type="button" value="删除" onclick="deleteCurRow(this);" style="width: 40px;" class="mini_btn" /></td>  
					<td style="width: 20%;"><input class="middle_txt" name="part_code_dealer" id="p" datatype="0,is_null,20" onclick="onPart(this);" value="${ps.PART_CODE_DEALER }" readonly="readonly" type="text"/></td>
					<td style="width: 20%;"><input class="middle_txt" name="part_name_dealer" id="p1" value="${ps.PART_NAME_DEALER }" readonly="readonly" type="text"/></td>
     				<td style="width: 20%;"><input class="middle_txt" name="supply_code_dealer" id="s" datatype="0,is_null,20" onclick="onSupply(this);" value="${ps.SUPPLY_CODE_DEALER }" readonly="readonly" type="text"/></td>	
     				<td style="width: 20%;"><input class="middle_txt" name="supply_name_dealer" id="s1" value="${ps.SUPPLY_NAME_DEALER }" readonly="readonly" type="text"/></td>							
				</tr>
			</c:forEach>
		</tbody>
    </table>
    </div>
  </div>
	<div style="display: none;">
	<table class="table_query">
		<tbody >
		<tr id="modelTr">
		<td style="width: 10%;"class="num" ></td>
		<td style="width: 10%;"><input type="hidden" name="n" ><input type="button" value="删除" onclick="deleteCurRow(this);" style="width: 40px;" class="mini_btn" /></td>  
     	<td style="width: 20%;"><input class="middle_txt" name="part_code_dealer" id="part_code_dealer" datatype="0,is_null,20" onclick="onPart(this);" readonly="readonly" type="text"/></td>
     	<td style="width: 20%;"><input class="middle_txt" name="part_name_dealer" id="part_name_dealer" readonly="readonly" type="text"/></td>
     	<td style="width: 20%;"><input class="middle_txt" name="supply_code_dealer" id="supply_code_dealer" datatype="0,is_null,20" onclick="onSupply(this);" readonly="readonly" type="text"/></td>
     	<td style="width: 20%;"><input class="middle_txt" name="supply_name_dealer" id="supply_name_dealer" readonly="readonly" type="text"/></td>
		</tr>
		</tbody>
	</table>
</div>
<br>
<!-- 添加附件 开始 -->
  <div class="form-panel" id="parts_message">
  <h2>&nbsp;<img src="<%=contextPath%>/img/nav.gif" />附件信息&nbsp;<input type="button" class="short_btn"onclick="showUpload('<%=contextPath%>','PNG;PDF;JPG;JPEG;BMP;RAR;ZIP;TXT;XLS;XLSX;DOC;DOCX',10)" value="添加附件" /> </h2>
    <div class="form-body">
    <table border="0" style="text-align:left;" cellpadding="1" cellspacing="1" class="table_list" style="border-bottom:1px solid #DAE0EE;" ondrag="return false;">
			<tr>
				<jsp:include page="${contextPath}/uploadDiv.jsp" />
			</tr>
			<c:choose>
				<c:when test="${empty select}">
					<c:forEach items="${fileList}" var="flist">
			    		<script type="text/javascript">
			    			addUploadRowByDb('${flist.filename}','${flist.fjid}','${flist.fileurl}','${flist.fjid}','<%=request.getContextPath()%>/util/FileDownLoad/FileDownLoadQuery.do?fjid=' );
						</script>
	    			</c:forEach>
				</c:when>
				<c:otherwise>
					<c:forEach items="${fileList}" var="flist">
			    		<script type="text/javascript">
			    			addUploadRowByDb('${flist.filename}','${flist.fjid}','${flist.fileurl}','${flist.fjid}','<%=request.getContextPath()%>/util/FileDownLoad/FileDownLoadQuery.do?fjid=' );
						</script>
			    	</c:forEach>
				</c:otherwise>
			</c:choose>
	</table> 
	</div>
	</div>
<!-- 添加附件 结束 -->
	<br>
<table width=100% border="0" cellspacing="0" cellpadding="0">
		<tr>
			<td height="12" align=left width="33%">&nbsp;</td>
            	<td height="12" align=center width="33%" nowrap="true">
               	&nbsp;&nbsp;
               	<input type="button" class="normal_btn" id="sure" onclick="sureInsert(0);"  style="width=8%" value="保存" />
               	&nbsp;&nbsp;
               	<input type="button" class="normal_btn" id="report" onclick="sureInsert(1);"  style="width=8%" value="上报" />
               	&nbsp;&nbsp;
				<input type="button" id="back" onClick="history.back();" class="normal_btn"  style="width=8%" value="返回"/>
   			</td>
           	<td height="12" align=center width="33%">
     			</td>
		</tr>
</table>
</form>
</body>
</html>