<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<%@page import="com.infodms.dms.common.Constant"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.Calendar"%>

<%@page import="com.infodms.dms.util.CommonUtils"%><html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>旧件抵扣单结算</title>
<%@ page import="com.infodms.dms.common.Constant"%>
<%
	String contextPath = request.getContextPath();
%>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<%
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	Calendar calendar = Calendar.getInstance();
	calendar.add(Calendar.DAY_OF_MONTH,-7);
	String limitDate = sdf.format(calendar.getTime());
%>
</head>
<body onload="doInit();">
<div class="navigation">
	<img src="<%=contextPath%>/img/nav.gif" />
	当前位置：售后服务管理&gt;索赔旧件管理&gt;索赔旧件抵扣单结算
</div>

<form method="post" name="fm" id="fm">
<table align="center" class="table_query">
	<tr>
		<td class="table_query_2Col_label_6Letter">经销商代码：</td>
		<td align="left">
			<textarea id="DEALER_CODE"  name="DEALER_CODE" rows="3" cols="20" datatype="1,is_null,2000"></textarea>
			<input class="mark_btn" type="button" value="&hellip;" 
				onclick="showOrgDealer('DEALER_CODE','','true','',true)" />
			<input name="clrBtn" type="button" class="normal_btn" onclick="clearInput();" value="清除"/>
		</td>
		<td class="table_query_2Col_label_6Letter">经销商名称：</td>
		<td>
			<input name="DEALER_NAME" id="DEALER_NAME" value="" type="text" 
				class="middle_txt" datatype="1,is_digit_letter_cn" />
		</td>
	</tr>
	<tr>
		<td class="table_query_2Col_label_6Letter">产地：</td>
		<td>
			<script type="text/javascript">
			genSelBoxContainStr("YIELDLY",<%=Constant.SERVICEACTIVITY_CAR_YIELDLY%>,"",true,"short_sel","","true",'<%=CommonUtils.checkNull(request.getAttribute("yieldly"))%>');
		    </script>
		</td>
		<td class="table_query_2Col_label_6Letter">抵扣单状态:</td>
		<td>
			<select name="DEDUCT_STATUS">
				<option value="0">未结算</option>
			</select>
        </td>
	</tr>
	<tr>
		<td class="table_query_2Col_label_6Letter">截止通知日期：</td>
		<td colspan="3">
			<input name="LAST_DAY" type="text" id="LAST_DAY" class="short_txt" group="LAST_DAY,LIMIT_DAY"  
				datatype="0,is_date,10" hasbtn="true" callFunction="showcalendar(event, 'LAST_DAY', false);" 
				 value="<%=limitDate%>"/>
			&nbsp;小于&nbsp;<input id="LIMIT_DAY" name="LIMIT_DAY" class="short_txt" value="<%=limitDate%>" readonly="readonly"/>
		    &nbsp;&nbsp;注：抵扣单通知日期不超过7日的抵扣单，不能结算！
		</td>
	</tr>
	<tr>
		<td colspan="4" align="center">
			<input class="normal_btn" type="button" value="查询" name="recommit" id="queryBtn" onclick="showTable(false);__extQuery__(1);" />
        </td>
	</tr>
</table>
   <!-- ��ѯ��� end --> 
   <!--��ҳ begin --> 
   <jsp:include page="${contextPath}/queryPage/orderHidden.html" /> 
   <jsp:include page="${contextPath}/queryPage/pageDiv.html" /> 
   <!--分页 end -->
</form>
<script type="text/javascript">
    <!--
    var isShow = true;
    var myPage;
	//查询路径
	var url = "<%=contextPath%>/claim/oldPart/ClaimOldPartDeductBalance/newQueryDeductBalance.json";		
	var title = null;
	var columns = [
					{header: "起始号", width:'20%', dataIndex: 'NOBOUND'},
					{header: "产地", width:'10%', dataIndex: 'YIELDLY',renderer:getItemValue},
					{header: "经销商代码", width:'10%', dataIndex: 'DEALER_CODE',renderer:showBlanceBtn},
					{header: "经销商简称", width:'15%', dataIndex: 'DEALER_NAME'},
					{header: "抵扣单数", width:'5%', dataIndex: 'NO_COUNT'},
					{header: "工时抵扣金额(元)", width:'5%', dataIndex: 'MANHOUR_MONEY',renderer:formatCurrency},
					{header: "配件配件金额(元)", width:'5%', dataIndex: 'MATERIAL_MONEY',renderer:formatCurrency},
					{header: "其他其他费用(元)", width:'5%', dataIndex: 'OTHER_MONEY',renderer:formatCurrency},
					{header: "抵扣总金额(元)", width:'5%', dataIndex: 'TOTALMONEY',renderer:formatCurrency}
		      ];
		      
	//初始化     
	function doInit(){
   		loadcalendar();
	}

	//清空经销商框
	function clearInput(){
		var target = document.getElementById('DEALER_CODE');
		target.value = '';
	}

	//根据选择条件进行结算
	function balance(){
		var frm = 'fm';
		if(submitForm('fm')){
			var message = '确认结算  满足选择条件的  抵扣单？';
			MyConfirm(message,subFrm,[frm]);
		}
	}

	//结算抵扣单
	function subFrm(frmName){
		 var turl = '<%=contextPath%>'+'/claim/oldPart/ClaimOldPartDeductBalance/newBlance.json';
		 makeNomalFormCall(turl,showResult,frmName);
		 waitBlance();
		 showTable(false);
		 isShow = true;
	}

    //审核成后跳转到首页
	 function showResult(json){
		if(json.SUCCESS == 'DEALED'){
			MyAlert("其他人正在结算旧件抵扣单！");
		}
		if(json.SUCCESS != 'ERROR')
			__extQuery__(1);
		noticeBlance();
	 }

     //当查询到数据时显示结算按钮
	 function showBlanceBtn(value){
		 if(isShow){
			 isShow = false;
			 showTable(true);
		 }
		 return String.format(value);
	 }

	 function showTable(show){
		 var tabEle = document.getElementById('bt');
		 if(show)
			 tabEle.style.display = '';
		 else{
			 isShow = true;
			 tabEle.style.display = 'none'; 
		 }
	 }

	 function waitBlance(){
			if($('blanceWait')){
				var screenW = document.viewport.getWidth()/2;	
				var screenH = document.viewport.getHeight()/2;
				$('blanceWait').style.left = (screenW-20) + 'px';
				$('blanceWait').style.top = (screenH-20) + 'px';
				$('blanceWait').innerHTML = ' 正在结算中... ';
				$('blanceWait').show();
			}
		}
		
	 function noticeBlance(){
		if($('blanceWait'))
			$('blanceWait').hide();
	 }
	 -->
</script>
<table id='bt' class="table_list" style="display:none">
	<tr>
		<th align="center">
			<p>
	    	  <input class="normal_btn" type="button" value='结算' onclick='balance()' name="balanceBtn" />
	  		</p>
  		</th>
    </tr>
</table>

<div id="blanceWait" 
style='position:absolute;z-index:200;background:#FFCC00;padding:1px;top:4px;display:none;display: none;'>
</div>

</body>
</html>