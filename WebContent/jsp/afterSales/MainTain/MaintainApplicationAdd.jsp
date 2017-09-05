<%@page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
	String contextPath = request.getContextPath();
%>
<%@ page import="com.infodms.dms.common.Constant" %>
<%@ page import="com.infodms.dms.common.FileConstant"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<%-- <script type="text/javascript" src="<%=FileConstant.regionJsUrl%>"></script> --%>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<script type="text/javascript" src="<%=contextPath%>/js/jslib/jquery-1.7.2.js"></script>
<script type="text/javascript" >
	//var $ = jQuery.noConflict();
	$(document).ready(function(){
		genLocSel('txt1','txt2','txt3','','',''); // 加载省份城市和县 
	});
	function setMoreValue(inputId,vin) {
		document.getElementById(inputId).value=vin;
		showCustomerInfos(vin);
	}

	var myPage;
	var btnIds = new Array("userQuery","add");
	var menuUrl = "<%=contextPath%>/OutMaintainAction/MaintainSelect.do";
	var saveUrl = "<%=contextPath%>/OutMaintainAction/saveMaintainApplication.json";
	var checkVinUrl = "<%=contextPath%>/OutMaintainAction/showCustomerInfosByVin.json";
	// 保存信息反馈类型
	function saveMainTain() {
		if($("#vin").val() == null || $("#vin").val() == ""){
			MyAlert("vin不能为空");
			return;
		}
		if($("#linkMan").val() == null || $("#linkMan").val() == ''){
			MyAlert("联系人不能为空");
			return;
		}
		if($("#tel").val() == null || $("#tel").val() == ''){
			MyAlert("联系电话不能为空");
			return;
		}
		if($("#mileage").val() == null || $("#mileage").val() == ''){
			MyAlert("行驶里程不能为空");
			return;
		}
		if($("#relief_mileage").val() == null || $("#relief_mileage").val() == ''){
			MyAlert("急救里程不能为空");
			return;
		}
		if($("#txt1").val() == null || $("#txt1").val() == ''){
			MyAlert("救急所在省不能为空");
			return;
		}
		if($("#txt2").val() == null || $("#txt2").val() == ''){
			MyAlert("救急所在市不能为空");
			return;
		}
		if($("#txt3").val() == null || $("#txt3").val() == ''){
			MyAlert("救急所在县(区)不能为空");
			return;
		}
		if($("#egress_reamrk").val() == null || $("#egress_reamrk").val() == ''){
			MyAlert("申请内容不能为空");
			return;
		}
		MyConfirm("确定保存吗？",EditAsDo);
	}
	function EditAsDo(){
		var tUrl = saveUrl;
		makeNomalFormCall(tUrl, message, 'fm');
	}
	function message(json){
		if(json.message == "success"){
			MyAlertForFun("保存成功！",backInit);
		}else{
			MyAlert("保存失败！");
		}
	}

	function backInit(){
		window.location.href = menuUrl;
	}


	function showCustomerInfos (vin) {
		//清空数据
		//车系
		document.getElementById("seName").innerHTML = "";
		//车型
		document.getElementById("vehicleName").innerHTML = "";
		//配置
		document.getElementById("pzName").innerHTML =  "";
		//出厂日期
		document.getElementById("outDate2").innerHTML =  "";
		//购车日期
		document.getElementById("buyDate2").innerHTML =  "";
		//客户id
		document.getElementById("ctmId").value =  "";
		//车主
		document.getElementById("userName").innerHTML =  "";
		//用户地址
		document.getElementById("userAddress").innerHTML =  "";
		//车牌号
		document.getElementById("plateNum").innerHTML = "";
		
		/*var vin = obj.value;//获取当前对象的VIN码
	*/	if (vin == "" || vin.trim().length == 0 || vin == null) {
			MyAlert("请输入VIN号！");
			return;
		}
		makeNomalFormCall(				
						checkVinUrl+"?vin="
						+ vin, showResult, "fm");
	}
	function showResult(obj) {
			//判断是否存在该VIN
			if (obj.msg == ""  || obj.msg == null) {
				MyAlert("该VIN码不存在！");
				document.getElementById("vin").value = "";
				return;
			}
			//车系
			document.getElementById("seName").innerHTML = obj.maps.SE_NAME != "" && obj.maps.SE_NAME != null ? obj.maps.SE_NAME: "";
			//车型
			document.getElementById("vehicleName").innerHTML = obj.maps.GROUP_NAME != "" && obj.maps.GROUP_NAME != null ? obj.maps.GROUP_NAME: "";
			//配置
			document.getElementById("pzName").innerHTML = obj.maps.PZ_NAME != "" && obj.maps.PZ_NAME != null ? obj.maps.PZ_NAME: "";
			//出厂日期
			document.getElementById("outDate2").innerHTML = obj.maps.FACTORY_DATE != "" && obj.maps.FACTORY_DATE != null ? obj.maps.FACTORY_DATE : "";
			//购车日期
			document.getElementById("buyDate2").innerHTML = obj.maps.PURCHASED_DATE != "" && obj.maps.PURCHASED_DATE != null ? obj.maps.PURCHASED_DATE : "";
			//客户id
			document.getElementById("ctmId").value = obj.maps.CTM_ID != "" && obj.maps.CTM_ID != null ? obj.maps.CTM_ID : "";
			//车主
			document.getElementById("userName").innerHTML = obj.maps.CTM_NAME != "" && obj.maps.CTM_NAME != null ? obj.maps.CTM_NAME : "";
			//用户地址
			document.getElementById("userAddress").innerHTML = obj.maps.ADDRESS != "" && obj.maps.ADDRESS != null ? obj.maps.ADDRESS : "";
			//车牌号
			document.getElementById("plateNum").innerHTML = obj.maps.LICENSE_NO != "" && obj.maps.LICENSE_NO != null ? obj.maps.LICENSE_NO : "";
	}

	/* 验证数字且只能输，两位小数 */
	function vaildateNum(obj){
		obj.value = obj.value.replace(/[^\d.]/g,""); //清除"数字"和"."以外的字符
		obj.value = obj.value.replace(/^\./g,""); //验证第一个字符是数字
		obj.value = obj.value.replace(/\.{2,}/g,"."); //只保留第一个, 清除多余的
		obj.value = obj.value.replace(".","$#$").replace(/\./g,"").replace("$#$",".");
		obj.value = obj.value.replace(/^(\-)*(\d+)\.(\d\d).*$/,'$1$2.$3'); //只能输入两个小数
	}


</script>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>外出救援申请添加</title>
</head>
<body>
<div class="navigation"><font style="font-size: 13px;font-family:serif;"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 售后服务管理 &gt; 车辆信息管理管理&gt; 外出救援申请添加</font></div>
<form method="post" name="fm" id="fm">
	<!--隐藏表单域 -->
	<input title="单据编码" type="hidden" value="${listCode }" name="egress_no" id="egress_no"/>
	<input title="制单人姓名" type="hidden" value="${listUser.NAME  }" name="create_by" id="create_by"/>
	<input title="制单人日期" type="hidden" value="${date }" name="create_date" id="create_date"/>
	<input type="hidden" title="服务站编码" value="${dealerInfos.DEALER_CODE }" name="dealer_id" id="dealer_id"/>
	<input type="hidden" id="ctmId" name="ctmId"/><!-- 客户ID -->
	<table border="0" align="center" cellpadding="1" cellspacing="1" class="table_query">
      <tr  >
        <td class="table_add_2Col_label_5Letter" style="text-align:right;width:125px;"> 单据编码：</td>
        <td align="left">
        	${listCode }
        </td>
        <td class="table_add_2Col_label_5Letter" style="text-align:right;width:125px;"> 制单人姓名：</td>
        <td align="left">
        	${listUser.NAME }
        </td>
        <td class="table_add_2Col_label_5Letter" style="text-align:right;width:125px;"> 制单日期：</td>
        <td align="left">
			${date }
         </td>
      </tr>
      <tr  >
        <td class="table_add_2Col_label_5Letter" style="text-align:right;width:125px;"> 服务站编码：</td>
        <td align="left">
        	${dealerInfos.DEALER_CODE }
        </td>
        <td class="table_add_2Col_label_5Letter" style="text-align:right;width:125px;"> 服务站：</td>
        <td align="left">
      	  	${dealerInfos.DEALER_NAME }
        </td>
        <td class="table_add_2Col_label_5Letter" style="text-align:right;width:125px;"> 服务站电话：</td>
        <td align="left">
        	${dealerInfos.PHONE }
        </td>
      </tr>
     <tr  >
        <td class="table_add_2Col_label_5Letter" style="text-align:right;width:125px;"> VIN码：</td>
         <td class="table_info_3col_input"  nowrap="nowrap">
        	<input class="middle_txt" title="VIN码" onchange="showCustomerInfos(this.value);" maxlength="50" name="vin" id="vin"/>
        	<span style="color:red;">*</span>
        </td>
        <td class="table_add_2Col_label_5Letter" style="text-align:right;width:125px;"> 车系名称：</td>
        <td align="left" id="seName"></td>
        <td class="table_add_2Col_label_5Letter" style="text-align:right;width:125px;"> 车型名称：</td>
        <td align="left" id="vehicleName"></td>
      </tr>
      
      <tr  >     	
        <td class="table_add_2Col_label_5Letter" style="text-align:right;width:125px;"> 配置：</td>
        <td align="left" id="pzName">
        </td>
        <td class="table_add_2Col_label_5Letter" style="text-align:right;width:125px;"> 车牌号：</td>
        <td align="left" id="plateNum">
        </td>  
        <td class="table_add_2Col_label_5Letter" style="text-align:right;width:125px;"> 车主名字：</td>
         <td class="table_info_3col_input"  nowrap="nowrap" id="userName">
         </td> 
      </tr>
      
      <tr  >     	
        <td class="table_add_2Col_label_5Letter" style="text-align:right;width:125px;"> 出厂日期：</td>
		<td align="left" id="outDate2">
		</td>
        
        <td class="table_add_2Col_label_5Letter" style="text-align:right;width:125px;"> 购车日期：</td>
        <td align="left" id="buyDate2">
        </td>
        <td class="table_add_2Col_label_5Letter" style="text-align:right;width:125px;"> 用户地址：</td>
        <td class="table_info_3col_input"  nowrap="nowrap" id="userAddress">
        </td> 
      </tr>
      
      <tr  >
         <td class="table_add_2Col_label_5Letter" style="text-align:right;width:125px;"> 客户名字：</td>
         <td class="table_info_3col_input"  nowrap="nowrap">
        	<input class="middle_txt" title="联系人" maxlength="50" name="linkMan" id="linkMan"/>
        	<span style="color: red">*</span>
        </td>
        <td class="table_add_2Col_label_5Letter" style="text-align:right;width:125px;"> 客户电话：</td>
        <td class="table_info_3col_input"  nowrap="nowrap">
        	<input class="middle_txt" title="电话" maxlength="15" onkeyup="this.value=this.value.replace(/\D/g,'')" onblur="this.value=this.value.replace(/\D/g,'')" name="tel" id="tel"/>
        	<span style="color: red">*</span>
        </td>   
        <td class="table_add_2Col_label_5Letter" style="text-align:right;width:125px;"> 行驶里程：</td>
        <td class="table_info_3col_input"  nowrap="nowrap">
        	<input class="middle_txt" title="行驶里程" maxlength="8" onkeyup="vaildateNum(this)" onblur="vaildateNum(this)" name="mileage" id="mileage" />
        	<span style="color: red">*</span>
        </td>
      </tr>
          
      <tr  >     	    
        <td class="table_add_2Col_label_5Letter" style="text-align:right;width:125px;"> 单程救急里程(公里)：</td>
        <td class="table_info_3col_input"  nowrap="nowrap">
        	<input class="middle_txt" title="单程救急里程(公里)" maxlength="8" onblur="vaildateNum(this)" name="relief_mileage" id="relief_mileage"/>
        	<span style="color: red">*</span>
        </td>
        <td class="table_add_2Col_label_5Letter" style="text-align:right;width:125px;"> 救急所在省：</td>
         <td class="table_info_3col_input"  nowrap="nowrap">
        	<select class="middle_txt" id="txt1" name="province" onchange="_genCity(this,'txt2');"> </select>
        	<span style="color: red">*</span>
        </td>
        <td class="table_add_2Col_label_5Letter" style="text-align:right;width:125px;"> 救急所在市：</td>
         <td class="table_info_3col_input"  nowrap="nowrap">
        	<select class="middle_txt" id="txt2" name="city" onchange="_genCity(this,'txt3')"> </select><span style="color: red">*</span>
        </td>
      </tr>
      
      <tr  >      
        <td class="table_add_2Col_label_5Letter" style="text-align:right;width:125px;"> 救急所在县(区)：</td>
         <td class="table_info_3col_input"  nowrap="nowrap">
        	<select class="middle_txt" id="txt3" name="county"></select>
        	<span style="color: red">*</span>
        </td>
        <td class="table_add_2Col_label_5Letter" style="text-align:right;width:125px;">
				详细地址：</td>
			<td align="left">
				<input class="middle_txt" title="详细地址" maxlength="30" name="town" id="town" />
				<span style="color: red">*</span>
			</td>
			<td class="table_add_2Col_label_5Letter" style="text-align:right;width:125px;"></td>
			<td align="left">
			</td>
       </tr>
      
      <tr  >
        <td class="table_add_2Col_label_5Letter" style="text-align:right;width:125px;"> 申请内容：</td>
        <td class="table_info_3col_input"  nowrap="nowrap" colspan="5">
        	<textarea style="width: 95%;height: 80px" name="egress_reamrk" id="egress_reamrk"></textarea>
       		<span style="color:red;">*</span>
        </td>
      </tr>
	<tr  >
		<td colspan="6" style="text-align: center;">
			<input class="normal_btn" type="button" name="button1"  id="queryBtn" value="保存" onclick="saveMainTain();" />
			<input class="normal_btn" type="button" name="button1"  id="backBtn" value="返回" onclick="backInit();"/>
		</td>
	</tr>
</table>
	
</form>
</body>
</html>