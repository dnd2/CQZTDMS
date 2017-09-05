<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c" %>
<%@ page import="com.infodms.dms.common.Constant"%>
<%@ page import="com.infodms.dms.po.FsFileuploadPO"%>
<%@page import="java.util.LinkedList"%>
<%@ page import="java.util.List" %>
<%@ page import=" com.infodms.dms.util.CommonUtils" %>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<% String contextPath = request.getContextPath(); 
	List<FsFileuploadPO> fileList = (LinkedList<FsFileuploadPO>)request.getAttribute("fileList");
	request.setAttribute("fileList",fileList);
%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>特殊外出费用</title>
<script type="text/javascript">
	function doInit()
	{
		loadcalendar();  //初始化时间控件
	}
</script>
</head>
<body>
<div id="loader" style='position:absolute;z-index:200;background:#FFCC00;padding:1px;top:4px;display:none;display: none;'></div>
<div class="navigation"><img src="<%=request.getContextPath()%>/img/nav.gif" />&nbsp;当前位置： 售后服务管理&gt;特殊费用管理&gt;特殊外出费用修改</div>
 <form method="post" name="fm" id="fm">
 <input type="hidden" name="id" value="<c:out value="${map.ID}"/>"/>
 <input type="hidden" name="totalCount" value="<c:out value="${map.DECLARE_SUM}"/>"/>
 <input type="hidden" id="mile_4_check" value="${map.SINGLE_MILEAGE}"/>
 <input type="hidden" id="outDay_4_check" value="${map.OUT_DAYS}"/>
   <table width=100% border="0" align="center" cellpadding="1" cellspacing="1" bgcolor="91908E" class="table_edit">
   		<tr>
          <th colspan="6"><img class="nav" src="<%=contextPath %>/img/subNav.gif" /> 基本信息</th>
        </tr>
		  <tr>
		    <td align="right">申报单位代码：</td>
		    <td>
		    	<c:out value="${map.DEALER_CODE}"/>
		    </td>
		    <td align="right">申报单位名称：</td>
		    <td>
		    	<c:out value="${map.DEALER_SHORTNAME}"/>
		    </td>
		    <td>&nbsp;</td>
		    <td>&nbsp;</td>
	      </tr>
		  <tr>
		    <td align="right">制单日期：</td>
		    <td>
		    	<c:out value="${map.CREATE_DATE1}"/>点
		    </td>
		    <td align="right">结算厂家：</td>
		    <td>
				 <select style="width: 152px;" name="yieldly" id="yieldly">
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
		    	<input type="text" name="purposeAddress" id="tel" class="middle_txt" datatype="0,is_null,200"  value="${map.PURPOSE_ADDRESS}"/>
		    	<input type="hidden" name="address" id="tel2" value="${map.PURPOSE_ADDRESS}"/>
		    	<input type="hidden" name="mileage2" id="mileage2" value="${map.SINGLE_MILEAGE}"/>
		    	<span id="add" style="display:none">
		    		<input type="button" value="..." class="mini_btn" onclick="getCruise();"/>
		    	</span>
		    </td>
		    <td align="right" nowrap="nowrap" >外出时间：</td>
            <td nowrap="nowrap" colspan="3">
            	<div align="left">
            		<input name="beginTime" id="t1" value="${map.STARTDATE}" type="text" class="middle_txt" datatype="0,is_datetime,16" group="t1,t2" hasbtn="true" callFunction="showcalendar(event, 't1', true);">
            		&nbsp;至&nbsp;
            		<input name="endTime" id="t2" value="${map.ENDDATE}" type="text" class="middle_txt" datatype="0,is_datetime,16" group="t1,t2" hasbtn="true" callFunction="showcalendar(event, 't2', true);">
            	</div>
            </td>
	      </tr>
	      <tr>
		    <td align="right" nowrap="nowrap" >外出人员数量：</td>
            <td nowrap="nowrap">
				<input type="text" name="personNum" id="personNum" class="middle_txt" datatype="0,isDigit,20"  value="<c:out value="${map.PERSON_NUM}"/>" />
            </td>
		    <td align="right" nowrap="nowrap">外出人员姓名：</td>
		    <td>
		    	<input type="text" name="personName" id="personName" class="middle_txt" datatype="0,is_null,200"  value="<c:out value="${map.PERSON_NAME}"/>" />
		    </td>
		    <td align="right" nowrap="nowrap">外出天数：</td>
		    <td>
				<input type="text" name="out_days" id="out_days" value="${map.OUT_DAYS}" class="middle_txt" datatype="0,isDigit,5"/>
			</td>
	      </tr>		
	      <tr>
		    <td align="right" nowrap="nowrap" >总里程(公里)：</td>
            <td nowrap="nowrap">
				<input type="text" name="SINGLE_MILEAGE" id="SINGLE_MILEAGE" class="middle_txt" datatype="0,isDigit,20"  value="${map.SINGLE_MILEAGE}"/>
            </td>
		    <td align="right">过路过桥费(元)：</td>
		    <td>
		    	<input type="text" name="PASS_FEE" value="<c:out value="${map.PASS_FEE}"/>" id="PASS_FEE" datatype="1,isMoney,30" blurback="true" class="middle_txt"/>
		    </td>
		    <td align="right">交通补助(元)：</td>
		    <td>
				<input type="text" name="TRAFFIC_FEE" value="<c:out value="${map.TRAFFIC_FEE}"/>"  id="TRAFFIC_FEE" datatype="1,isMoney,30" blurback="true" class="middle_txt"/>
			</td>
	      </tr>	     
	      <tr>
		    <td align="right" nowrap="nowrap" >住宿费(元)：</td>
            <td nowrap="nowrap">
				<input type="text" name="QUARTER_FEE" value="<c:out value="${map.QUARTER_FEE}"/>" id="QUARTER_FEE" datatype="1,isMoney,30" blurback="true" class="middle_txt"/>
            </td>
		    <td align="right">餐补费(元)：</td>
		    <td>
		    	<input type="text" name="EAT_FEE" value="<c:out value="${map.EAT_FEE}"/>" id="EAT_FEE" datatype="1,isMoney,30" blurback="true" class="middle_txt"/>
		    </td>
		    <td align="right">人员补助(元)：</td>
		    <td>
				<input type="text" name="PERSON_SUBSIDE" value="<c:out value="${map.PERSON_SUBSIDE}"/>" id="PERSON_SUBSIDE" datatype="1,isMoney,30" blurback="true" class="middle_txt"/>
			</td>
	      </tr>	  
	      <tr>
		    <td align="right" nowrap="nowrap" >总申报费用(元)：</td>
            <td nowrap="nowrap" id="to">
            	<script type="text/javascript">
            		document.write(amountFormat(<c:out value="${map.DECLARE_SUM1}"/>))
            	</script>元
            </td>
		    <td align="right">费用渠道：</td>
		    <td>
				<script type="text/javascript">
					 genSelBoxExp("FEE_CHANNEL",<%=Constant.FEE_CHANNEL%>,"<c:out value="${map.FEE_CHANNEL}"/>",false,"short_sel","","true",'');
		    	</script>
		    </td>
		    <td id="vin_num1" style="display:none" align="right">三包内用户数：</td>
		    <td id="vin_num2" style="display:none">
				<input type="text" name="vin_num" id="vin_num" class="middle_txt" datatype="0,is_digit,5" value="${map.FREE_CHARGE}"/>
			</td>
	      </tr>	 
	       <tr>
		   <td align="right" >结算费用类型:</td>
		    <td class="table_info_3col_input"> 
		    <script type="text/javascript">
					genSelBoxExp("balance_fee_type",<%=Constant.TAX_RATE_FEE%>,"${map.BALANCE_FEE_TYPE}",false,"short_sel","","true",'');
 			</script>
 			</td>
		   <td align="right">主因件名称：</td>
		    <td>
		    	<input name='showBtn' type='button' class='mini_btn' style='cursor: pointer;' onclick='open_part()' value='...' />
		    	<input name="part_name" datatype="1,is_null,200" id="part_name" value="${map.PART_NAME}" readonly="readonly">
		    	<input type="hidden" name="part_code" id="part_code" value="${map.PART_CODE}" >
		    </td>
		   <td align="right">制造商名称：</td>
		    <td>
		    	<input name='showBtn' type='button' class='mini_btn' style='cursor: pointer;' onclick='supplier()' value='...' />
		    	<input type="hidden" name="supplier_code" id="supplier_code" datatype="0,is_null,200" value="${map.SUPPLIER_CODE}" readonly="readonly">
		    	<input  name="supplier_name" id="supplier_name" datatype="0,is_null,200" value="${map.SUPPLIER_NAME}">
		    </td>
	      </tr>	  
          <tr >
            <td align="right">备注：</td>
            <td colspan="5">
              <textarea name="remark" id="remark" rows='5' cols='80' ><c:out value="${map.APPLY_CONTENT}"/></textarea>
            </td> 
          </tr>
        </table>        
   <!-- 添加附件 开始  -->
	<table class="table_info" border="0" id="file">
		<input type="hidden" id="fjids" name="fjids"/>
	    <tr>
	        <th>
				<img class="nav" src="<%=contextPath%>/img/subNav.gif" />附件信息
			     <input type="button" class="normal_btn"  onclick="showUpload('<%=contextPath%>')" value ='添加附件'/>
			</th>
		</tr>
		<tr>
    		<td width="100%" colspan="2"><jsp:include page="${contextPath}/uploadDiv.jsp" /></td>
  		</tr>
  		<%for(int i=0;i<fileList.size();i++) { %>
	 	 <script type="text/javascript">
	 	 addUploadRowByDb('<%=CommonUtils.checkNull(fileList.get(i).getFilename()) %>','<%=CommonUtils.checkNull(fileList.get(i).getFileid()) %>','<%=CommonUtils.checkNull(fileList.get(i).getFileurl())%>','<%=CommonUtils.checkNull(fileList.get(i).getFjid()) %>');
	 	 </script>
	<%}%>
	</table> 
  <!-- 添加附件 结束 -->
  <table class="table_list" style="border-bottom:1px solid #DAE0EE;display: NONE">
   		<tr>
			<th colspan="11" align="left">
				<img class="nav" src="<%=request.getContextPath()%>/img/subNav.gif" />明细信息
				&nbsp;
				<input type="button" class="long_btn" value="新增工单" onclick="addRO()"/>
			</th>
		</tr>
        <tr bgcolor="F3F4F8">
            <th>序号</th>
            <th>工单单号</th> 
            <th>车系</th>
            <th>车型</th>
            <th>VIN</th>
            <th>发动机号</th>
            <th>里程</th>
            <th>厂家</th>
            <th>操作</th>
    	</tr>
        <c:forEach items="${claim}" var="claim" varStatus="st">
       		<tr class="table_list_row2">
            	<td>${st.index+1}</td>
            	<td>
            		<a href="#" onclick="showDetail(<c:out value="${claim.CLAIM_ID}"/>)"><c:out value="${claim.CLAIM_NO}"/></a>
            	</td>
           	 	<td><c:out value="${claim.SERIES}"/></td>
           	 	<td><c:out value="${claim.MODEL}"/></td>
           	 	<td><c:out value="${claim.VIN}"/></td>
           	 	<td><c:out value="${claim.ENGINE_NO}"/></td>
           	 	<td><c:out value="${claim.MILEAGE}"/></td>
           	 	<td>
           	 		<script type="text/javascript">
           	 			writeItemValue(${claim.YIELDLY});
           	 		</script>
           	 	</td>
           	 	<td>
           	 		<a href="#" onclick="delClaim(<c:out value="${claim.ID}"/>)">[删除]</a>
           	 	</td>
        	</tr>
       	</c:forEach>
   </table>  
   <!-- 按钮 begin -->
   <table class="table_list">
      <tr > 
      	<th height="12" align=center>
			<input type="button" onClick="addSpeciaExpenses(1)" class="normal_btn" style="width=8%" value="保存"/>&nbsp;&nbsp;
			<input type="button" onClick="addSpeciaExpenses(2)" id="btn2" class="normal_btn" style="width=8%" value="提报"/>&nbsp;&nbsp;
			<!--<input type="button" onClick="delSpefee();" class="normal_btn" value="删除"/> -->&nbsp;&nbsp;
			<input type="button" onClick="goBack();" class="normal_btn" style="width=8%" value="返回"/>
	   	</th>
	  </tr>
   </table>
   <!-- 按钮 end -->
</form>
<script type="text/javascript">
	//添加
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
	  function open_part()
   {
   		OpenHtmlWindow('<%=contextPath%>/claim/specialExpenses/SpecialExpensesManage/open_part.do',800,500);
   }
   
	function addSpeciaExpenses(val)
	{
		var t1 = $('t1').value ;
		var t2 = $('t2').value ;
		if(t1>t2 && t2!=''){
			MyAlert('开始时间不能大于结束时间!');
			return ;
		}
		if(val==2){
			
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
		var b = false ;
		var type=$('FEE_CHANNEL').value ;
		
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
			if(json.freeCharge<$('vin_num').value && json.val == 2){
				MyAlert('维修工单数低于三包用户数！');
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
			MyConfirm("确认操作?",addSpecia, [json.val]);
		}
	}
	function addSpecia(val)
	{
		if(val == 2){
			$('btn2').disabled = true ;
		}
		makeNomalFormCall("<%=contextPath%>/claim/specialExpenses/SpecialExpensesManage/updateSpeciaExpensesDO02.json?feeFlag="+val,addSpeciaBack,'fm','queryBtn'); 
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
		$('tel2').value = json.address ;
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

	function addCLaim()
	{
		var channel = '${map.FEE_CHANNEL}' ;
		var yieldly = '${map.YIELD}' ;
		var url = '<%=contextPath%>/claim/specialExpenses/SpecialExpensesManage/addClaim.do?id=${map.ID}&channel='+channel+'&yieldly='+yieldly ;
		OpenHtmlWindow(url,800,500);	
	}

	function addRO()
	{
		var channel = '${map.FEE_CHANNEL}' ;
		var yieldly = '${map.YIELD}' ;
		var url = '<%=contextPath%>/claim/specialExpenses/SpecialExpensesManage/addRO.do?id=${map.ID}&channel='+channel+'&yieldly='+yieldly ;
		OpenHtmlWindow(url,800,500);	
	}
	
	function akka(val)
	{
		parent.MyAlert("操作成功！");
		fm.action = "<%=contextPath%>/claim/specialExpenses/SpecialExpensesManage/updateSpecialExpenses.do?id=" + ${map.ID} + "&feeType=" + ${map.FEE_TYPE};
		fm.submit();
	}
	
	function delClaim(val)
	{
		MyConfirm("确认删除？",deleteClaim,[val]);
	}
	
	function deleteClaim(val)
	{
		makeNomalFormCall("<%=contextPath%>/claim/specialExpenses/SpecialExpensesManage/delCliam.json?id="+val,showForwordValue,'fm','queryBtn'); 
	}
	
	//删除
	function showForwordValue(json)
	{
		if(json.returnValue == '1')
		{
			parent.MyAlert("删除成功！");
			akka(document.getElementById("id").value);
		}
		else
		{
			MyAlert("操作失败！请联系系统管理员！");
		}
	}

	function goBack(){
		location = '<%=contextPath%>/claim/specialExpenses/SpecialExpensesManage/dealerSpProposeFor.do' ;
	}	
	function showDetail(val){
		var url='<%=contextPath%>/repairOrder/RoMaintainMain/roBalanceDetail.do?type=000&ID='+val ;
		OpenHtmlWindow(url,1000,700);
	}
	//特殊费用的删除
	function delSpefee(){
		MyConfirm("确认删除？",delSpefeeSure);
	}
	function delSpefeeSure(){
		fm.action = "<%=contextPath%>/claim/specialExpenses/SpecialExpensesManage/delSpefee.do";
		fm.submit();
	}	
</script>
</body>
</html>
