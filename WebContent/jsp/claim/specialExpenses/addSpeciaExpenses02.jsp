<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c" %>
<%@ page import="com.infodms.dms.common.Constant"%>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<% String contextPath = request.getContextPath(); %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>新增市场工单费用</title>
<script type="text/javascript">
	function doInit()
	{
		loadcalendar();  //初始化时间控件
	}
</script>
</head>
<body>
<div id="loader" style='position:absolute;z-index:200;background:#FFCC00;padding:1px;top:4px;display:none;display: none;'></div>
<div class="navigation"><img src="<%=request.getContextPath()%>/img/nav.gif" />&nbsp;当前位置： 售后服务管理 &gt; 特殊费用管理 &gt;特殊外出费用添加</div>
 <form method="post" name="fm" id="fm">
<input type="hidden" name="totalCount" id="totalCount" value=""/>
 <input type="hidden" id="vin2"/>
 <input type="hidden" id="mile_4_check" />
 <input type="hidden" id="outDay_4_check" />
   <table width=100% border="0" align="center" cellpadding="1" cellspacing="1" bgcolor="91908E" class="table_edit">
   		<tr>
          <th colspan="6"><img class="nav" src="<%=contextPath %>/img/subNav.gif" /> 基本信息</th>
        </tr>
		  <tr>
		    <td align="right">申报单位代码：</td>
		    <td class="table_info_3col_input">
		    	<c:out value="${map.DEALER_CODE}"/>
		    </td>
		    <td align="right">申报单位名称：</td>
		    <td>
		    	<c:out value="${map.DEALER_NAME}"/>
		    </td>
		    <td>&nbsp;</td>
		    <td>&nbsp;</td>
	      </tr>
		  <tr>
		    <td align="right">制单日期：</td>
		    <td class="table_info_3col_input">
		    	<c:out value="${map.SDATE}"/>
		    </td>
		    <td align="right">结算厂家：</td>
		    <td>
				 <select style="width: 152px;" name="yieldly" id="yieldly">
				 <option value="" >
    				-请选择-
    			  </option>
	              <c:forEach var="Area" items="${Area}" >
 				  <option value="${Area.areaId}" >
    				<c:out value="${Area.areaName}"/>
    			  </option>
    			 </c:forEach>
             </select>
		    </td>
		    <td>&nbsp;</td>
		    <td>&nbsp;</td>
	      </tr>
	      <tr>
	       <td align="right">目的地：</td>
		    <td>
		    	<input type="text" name="purposeAddress" id="tel" class="middle_txt" datatype="0,is_null,200"  value="" />
		    	<input type="hidden" name="address" id="tel2" />
		    	<input type="hidden" name="mileage2" id="mileage2" />
		    	 <input type="hidden" name="feeType" value="${feeType}"/>
		    	<span id="add" style="display:none">
		    		<input type="button" value="..." class="mini_btn" onclick="getCruise();"/>
		    	</span>
		    </td>
		    <td align="right" nowrap="nowrap" >外出时间：</td>
            <td nowrap="nowrap" colspan="3">
            	<div align="left">
            		<input class="middle_txt" id="t1" name="beginTime" datatype="0,is_datetime,16"
           			 readonly="readonly" group="t1,t2"/>
	        	 	<input class="time_ico" value=" " onclick="showcalendar(event, 't1', true);" type="button"/>
	          		 至
		        	 <input class="middle_txt" id="t2" name="endTime" datatype="0,is_datetime,16"
		           	  readonly="readonly" group="t1,t2"/>
	        		 <input class="time_ico" value=" " onclick="showcalendar(event, 't2', true);" type="button"/>
            	</div>
            </td>
	      </tr>			 
	      <tr>
		    <td align="right" nowrap="nowrap" >外出人员数量：</td>
            <td nowrap="nowrap">
				<input type="text" name="personNum" id="personNum" class="middle_txt" datatype="0,isDigit,20"  value="" />
            </td>
		    <td align="right" nowrap="nowrap">外出人员姓名：</td>
		    <td>
		    	<input type="text" name="personName" id="personName" class="middle_txt" datatype="0,is_null,200"  value="" />
		    </td>
		    <td align="right" nowrap="nowrap">外出天数：</td>
		    <td>
				<input type="text" name="out_days" id="out_days" class="middle_txt" datatype="0,isDigit,5"/>
			</td>
	      </tr>		
	      <tr>
		    <td align="right" nowrap="nowrap" >总里程(公里)：</td>
            <td nowrap="nowrap">
				<input type="text" name="SINGLE_MILEAGE" id="SINGLE_MILEAGE" class="middle_txt" datatype="0,isDigit,20"  value="" />
            </td>
		    <td align="right">过路过桥费(元)：</td>
		    <td>
		    	<input type="text" name="PASS_FEE" id="PASS_FEE" datatype="1,isMoney,30" blurback="true" maxlength="10" class="middle_txt"/>
		    </td>
		    <td align="right">交通补助(元)：</td>
		    <td>
				<input type="text" name="TRAFFIC_FEE" id="TRAFFIC_FEE" datatype="1,isMoney,30" blurback="true" maxlength="10" class="middle_txt"/>
			</td>
	      </tr>	     
	      <tr>
		    <td align="right" nowrap="nowrap" >住宿费(元)：</td>
            <td nowrap="nowrap">
				<input type="text" name="QUARTER_FEE" id="QUARTER_FEE" datatype="1,isMoney,30" blurback="true" maxlength="10" class="middle_txt"/>
            </td>
		    <td align="right">餐补费(元)：</td>
		    <td>
		    	<input type="text" name="EAT_FEE" id="EAT_FEE" datatype="1,isMoney,30" blurback="true" maxlength="10" class="middle_txt"/>
		    </td>
		    <td align="right">人员补助(元)：</td>
		    <td>
				<input type="text" name="PERSON_SUBSIDE" id="PERSON_SUBSIDE" datatype="1,isMoney,30" maxlength="10" blurback="true" class="middle_txt"/>
			</td>
	      </tr>	  
	      <tr>
		    <td align="right" nowrap="nowrap" >总申报费用(元)：</td>
            <td nowrap="nowrap" id="to">0.00元</td>
		    <td style="display: none" align="right">费用渠道：</td>
		    <td style="display: none">
				<script type="text/javascript">
					 genSelBoxExp("FEE_CHANNEL",<%=Constant.FEE_CHANNEL%>,"",false,"short_sel","","true",'');
		    	</script>
		    </td>
		    <td align="right">费用类型：</td>
		    <td>
		    	  <script type='text/javascript'>
				       var activityType=getItemValue('${feeType}');
				       document.write(activityType) ;
				     </script>
		    </td>
		    <td id="vin_num1" style="display:none" align="right">三包内用户数：</td>
		    <td id="vin_num2" style="display:none">
				<input type="text" name="vin_num" id="vin_num" class="middle_txt" datatype="0,is_digit,5"/>
			</td>
	      </tr>	 
	       <tr>
		   <td align="right" >结算费用类型:</td>
		    <td class="table_info_3col_input"> 
		    <script type="text/javascript">
					genSelBoxExp("balance_fee_type",<%=Constant.TAX_RATE_FEE%>,"",false,"short_sel","","true",'');
 			</script>
 			</td>
		   <td align="right">主因件名称：</td>
		    <td>
		    	<input name='showBtn' type='button' class='mini_btn' style='cursor: pointer;' onclick='open_part()' value='...' />
		    	<input name="part_name" datatype="0,is_null,200" id="part_name" value="" readonly="readonly">
		    	<input type="hidden" name="part_code" id="part_code" value="" >
		    </td>
		    <td align="right">制造商名称：</td>
		    <td>
		    	<input name='showBtn' type='button' class='mini_btn' style='cursor: pointer;' onclick='supplier()' value='...' />
		    	<input type="hidden" name="supplier_code" id="supplier_code" datatype="0,is_null,200" value="" readonly="readonly">
		    	<input  name="supplier_name" id="supplier_name" datatype="0,is_null,200" value="">
		    </td>
	      </tr>	  
          <tr >
            <td align="right">申请内容：</td>
            <td colspan="5">
              <textarea name="remark" id="remark" datatype="0,is_textarea,100" maxlength="100" rows="5" cols="80" ></textarea>
            </td> 
          </tr>
        </table> 
	<table class="table_info" border="0" id="file">
		<input type="hidden" id="fjids" name="fjids"/>
	    <tr>
	        <th>
				<img class="nav" src="<%=contextPath%>/img/subNav.gif" />附件信息
			     <input type="button" class="normal_btn"  onclick="showUpload('<%=contextPath%>')" value ='添加附件'/>
			     &nbsp;
			</th>
		</tr>
		<tr>
    		<td width="100%" colspan="2"><jsp:include page="${contextPath}/uploadDiv.jsp" /></td>
  		</tr>
	</table> 
  
   <!-- 按钮 begin -->
   <table class="table_list">
      <tr > 
      	<th height="12" align=center>
			<input type="button" onClick="addSpeciaExpenses(1)" class="normal_btn" style="width=8%" value="保存"/>&nbsp;&nbsp;
			<input type="button" onClick="javascript:history.go(-1);" class="normal_btn" style="width=8%" value="返回"/>
	   	</th>
	  </tr>
   </table>
   <!-- 按钮 end -->
</form>
<script type="text/javascript">
	 function supplier()
     {
         var part_code = document.getElementById('part_code').value;
         if(part_code.length > 0)
         {
         	 OpenHtmlWindow('<%=contextPath%>/claim/specialExpenses/SpecialExpensesManage/open_supplier.do?part_code='+part_code,800,500);
         }else
         {
         	OpenHtmlWindow('<%=contextPath%>/claim/specialExpenses/SpecialExpensesManage/open_supplier.do',800,500);
         }
     	
     }
	//添加
	
	function showResult(json)
	{
		var supplier_code = document.getElementById('supplier_code');
		supplier_code.options.length=0;
		if(json.supplier.length > 0)
		{
			for (var j=0 ; j < json.supplier.length ; j++) 
			{
				supplier_code.options.add(new Option(json.supplier[j].VENDER_NAME,json.supplier[j].VENDER_CODE));
			}
		}
	}
   function open_part()
   {
   		OpenHtmlWindow('<%=contextPath%>/claim/specialExpenses/SpecialExpensesManage/open_part.do',800,500);
   }
	//添加
	function addSpeciaExpenses(val)
	{
		var yieldly = $('yieldly').value ;
		if(!yieldly){
			MyAlert('结算厂家为必填项！');
			return ; 
		}
		var t1 = $('t1').value ;
		var t2 = $('t2').value ;
		if(t1>t2 && t2!=''){
			MyAlert('开始时间不能大于结束时间!');
			return ;
		}
		var channel = $('FEE_CHANNEL').value ;
		if('<%=Constant.FEE_CHANNEL_03%>'==channel){
			var c1 = $('mile_4_check').value ;
			var c2 = $('outDay_4_check').value ;
			var c3 = $('SINGLE_MILEAGE').value ;
			var c4 = $('out_days').value ;
			if(c3*1>c1*1){
				MyAlert('总里程填写过大！');
				return ;
			}
			if(c4*1>c2*1){
				MyAlert('外出天数填写过大！');
				return ;
			}
		}
		if(submitForm('fm')==false)return;
		//添加费用判断逻辑
		var url = '<%=contextPath%>/claim/specialExpenses/SpecialExpensesManage/speOutFeeCheck.json?type='+val ;
		makeNomalFormCall(url,checkBak,'fm');
	}
	function checkBak(json){
		var type = $('FEE_CHANNEL').value ;
		var b = false ;
		if(json.bridge<$('PASS_FEE').value){
			MyAlert('过路过桥费超过政策标准，请重新填写！');
			b = true ;
		}
		if(json.eat<$('EAT_FEE').value){
			MyAlert('餐补费超过政策标准，请重新填写！');
			b = true ;
		}
		if(json.outDay<$('out_days').value){
			MyAlert('外出天数超过政策标准，请重新填写！');
			b = true ;
		}
		if(json.quarter<$('QUARTER_FEE').value){
			MyAlert('住宿费超过政策标准，请重新填写！');
			b = true ;
		}
		if(json.train<$('TRAFFIC_FEE').value){
			MyAlert('交通补助超过政策标准，请重新填写！');
			b = true ;
		}
		if(type=='<%=Constant.FEE_CHANNEL_02%>'){
			if(json.person*$('vin_num').value<$('PERSON_SUBSIDE').value){
				MyAlert('人员补助超过政策标准，请重新填写！');
				b = true ;
			}
		} else if($('personNum').value*json.person*$('out_days').value<$('PERSON_SUBSIDE').value){
			MyAlert('人员补助超过政策标准，请重新填写！');
			b = true ;
		}
		if(b){
			b = false ;
			return ;
		}else{
			MyConfirm("确认新增！",addSpecia, [json.val]);
		}
	}
	function addSpecia(val)
	{
		makeNomalFormCall("<%=contextPath%>/claim/specialExpenses/SpecialExpensesManage/addSpeciaExpensesDO02.json?feeFlag="+val,addSpeciaBack,'fm','queryBtn'); 
	}
	
	function addSpeciaBack(json)
	{
		if(json.returnValue == 1)
		{
			parent.MyAlert("操作成功！");
			fm.action = "<%=contextPath%>/claim/specialExpenses/SpecialExpensesManage/dealerSpProposeFor.do";
			fm.submit();
		}
		else
		{
			MyAlert("操作失败！请联系系统管理员！");
		}
	}
	var obj = $('FEE_CHANNEL') ;
	if(obj){
		obj.attachEvent('onchange',getTypeChangeStyleParam);//
	}
	function getTypeChangeStyleParam(){
		var val = $('FEE_CHANNEL').value ;
		if(<%=Constant.FEE_CHANNEL_03%>==val){
			$('add').style.display = '' ;
			$('tel').value = '' ;
			$('SINGLE_MILEAGE').value = '' ;
			$('tel').disabled = true ;
			$('SINGLE_MILEAGE').disabled = false ;
			$('vin_num1').style.display = 'none' ;
			$('vin_num2').style.display = 'none' ;
		}else if(<%=Constant.FEE_CHANNEL_02%>==val){
			$('vin_num1').style.display = 'block' ;
			$('vin_num2').style.display = 'block' ;
			$('add').style.display = 'none' ;
			$('tel').disabled = false ;
			$('SINGLE_MILEAGE').disabled = false ;
			$('mile_4_check').value = '';
			$('outDay_4_check').value = '';
		}else{
			$('add').style.display = 'none' ;
			$('tel').disabled = false ;
			$('SINGLE_MILEAGE').disabled = false ;
			$('vin_num1').style.display = 'none' ;
			$('vin_num2').style.display = 'none' ;
			$('mile_4_check').value = '';
			$('outDay_4_check').value = '';
		}
	}
	function getCruise(){
		var url = '<%=contextPath%>/claim/specialExpenses/SpecialExpensesManage/queryCruiseInit.do' ;
		OpenHtmlWindow(url,800,500);
	}
	function setCruise(id){
		var url = '<%=contextPath%>/claim/specialExpenses/SpecialExpensesManage/getCurise.json?id='+id ;
		sendAjax(url,setCruiseBak,'fm');
	};
	function setCruiseBak(json){
		$('tel').value = json.address;
		//$('out_days').value = json.days;
		//$('SINGLE_MILEAGE').value = json.mileage;
		$('tel2').value = json.address ;
		//$('mileage2').value = json.mileage ;
		$('mile_4_check').value = json.mileage;
		$('outDay_4_check').value = json.days;
	}
	function blurBack()
	{
		var val1 = Number(formatNumber(document.getElementById("PASS_FEE").value));
		var val2 = Number(formatNumber(document.getElementById("TRAFFIC_FEE").value));
		var val3 = Number(formatNumber(document.getElementById("QUARTER_FEE").value));
		var val4 = Number(formatNumber(document.getElementById("EAT_FEE").value));
		var val5 = Number(formatNumber(document.getElementById("PERSON_SUBSIDE").value));
		document.getElementById("totalCount").value = val1 + val2 + val3 + val4 + val5;
		document.getElementById("to").innerHTML = amountFormat(val1 + val2 + val3 + val4 + val5) + "元";
	}
	
	function formatNumber(value)
	{
		if(value)
		{ 
			return value;
		}
		else return 0;
	}
	
</script>
</body>
</html>
