<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.Constant"%>
<%@ page import="com.infoservice.mvc.context.ActionContext"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@page
	import="com.infodms.dms.bean.ClaimApproveAndStoredReturnInfoBean"%>
<%@page import="com.infodms.dms.po.FsFileuploadPO"%>
<%@page import="java.util.LinkedList"%>
<%@page import="java.util.List"%>

<%@page import="com.infodms.dms.util.CommonUtils"%>
<%@page import="java.util.Map"%><html
	xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>索赔旧件审批入库</title>
<%
	String contextPath = request.getContextPath();
	ClaimApproveAndStoredReturnInfoBean detailBean = (ClaimApproveAndStoredReturnInfoBean) request.getAttribute("returnListBean");
	@SuppressWarnings("unchecked")
	List<Map<String, Object>> detailList1 = (List) request.getAttribute("detailList1");
	List<FsFileuploadPO> attachLs = (LinkedList<FsFileuploadPO>)request.getAttribute("fileList");
%>
<script type="text/javascript">
	var cloMainPart =1;

	
	//选中预检查
	function preChecked(temp) {
          if(checkDate()){
        		MyConfirm("确认操作?",sign,[temp]);
        }
	}
function preChecked2() {
        		MyConfirm("确认退回?",tBack,[]);
	}
	function tBack(){
		$('#qianshou')[0].disabled="disabled";
		$('#qianshou2')[0].disabled="disabled";
		fm.i_back_id.value='<%=detailBean.getId()%>';
		var str="?";
		str+="return_ord_id="+'<%=detailBean.getId()%>';
		var url= "<%=contextPath%>/claim/oldPart/ClaimOldPartApporoveStorageManager/approveAndStoredBack.json"+str;
		makeNomalFormCall(url,afterCall2,'fm','createOrdBtn');
	}
	//签收回调处理
	function afterCall2(json){
		var retCode=json.updateResult;
	    if(retCode!=null&&retCode!=''){
	      if(retCode=="updateSuccess"){
	    	MyAlert("退回成功");
	    	backTo();
	      }else if(retCode=="updateFailure"){
	    	MyAlert("退回失败!");
	     }
	   }
	}
	//格式化日期
	function formatDate(value) {
		 if (value==""||value==null) {
			document.write("");
		 }else {
			document.write(value.substr(0,10));
		 }
	}
	//签收操作
	function sign(temp){
		$('#qianshou')[0].disabled="disabled";
		$('#qianshou2')[0].disabled="disabled";
		fm.i_back_id.value='<%=detailBean.getId()%>';
		var str="?";
		var flag=0;
		str+="return_ord_id="+'<%=detailBean.getId()%>';
		var url= "<%=contextPath%>/claim/oldPart/ClaimOldPartApporoveStorageManager/approveAndStored221.json"+str+"&temp="+temp;
		makeNomalFormCall(url,afterCall,'fm','createOrdBtn');
	}
	//签收回调处理
	function afterCall(json){
		var retCode=json.updateResult;
	    if(retCode!=null&&retCode!=''){
	      if(retCode=="updateSuccess"){
	    	MyAlert("签收成功!");
	    	backTo();
	      }else if(retCode=="updateFailure"){
	    	MyAlert("签收失败!");
	     }
	   }
	}
	function backTo(){
	var yieldly = document.getElementById("yieldly").value;
	if(yieldly==<%=Constant.PART_IS_CHANGHE_01%>){
		fm.action="<%=contextPath%>/claim/oldPart/ClaimOldPartApporoveStorageManager/queryListPage11.do?isReturn=1";
	}else{
		fm.action="<%=contextPath%>/claim/oldPart/ClaimOldPartApporoveStorageManager/queryListPage112.do?isReturn=1";
	}
		
	    fm.method="post";
	    fm.submit();
	}

	function init(){
	
	}
	function checkDate(){
	var reg = /^\d+$/;
	var reg2 = /^(\d+\.\d{1,2}|\d+)$/;
	var num = document.getElementById("REAL_BOX_NO").value;
	//var transportNo = document.getElementById("TRANSPORT_NO").value;
	var PART_PAKGE = document.getElementById("PART_PAKGE").value;
	var PART_DETAIL = document.getElementById("PART_DETAIL").value;
	var PART_MARK = document.getElementById("PART_MARK").value;
	var transType =  document.getElementById("transport").value;
	var time1 = document.getElementById('fay_time').innerHTML;
	var price = $('#price')[0].value;
	var price1 = $('#price1')[0].value;
	var signRemark = $('#signRemark')[0].value;
	if(transType==null ||transType==""||time1=="" ){
		MyAlert("经销商还未补录物流发运信息,不能签收");
		return false;
	} 
	if(num==null||num==""){
		MyAlert("请输入实到箱数!");
		return false;
	}else if(!reg.test(num)){
		MyAlert("实到箱数请输入正整数!");
		return false;
	}else if(<%=CommonUtils.checkNull(detailBean.getParkage_amount())%><num){
		MyAlert("实到箱数不能大于装箱数!");
		return false;
	}else if(<%=CommonUtils.checkNull(detailBean.getParkage_amount())%>>num){
		MyAlert("实到箱数比装箱总数小!");
	}
	if(price==null || price ==""){
		MyAlert("请输入审核运费!");
		return false;
	}else if(!reg2.test(price)){
		MyAlert("审核运费为最多2位小数的数字!");
		return false;
	}else if (parseFloat(price)>parseFloat(price1)){
		MyAlert("审核运费不能大于申请运费!");
		return false;
	}else if ((parseFloat(price)<parseFloat(price1))&&(signRemark==null||signRemark=="")){
		MyAlert("扣减运费时必须填写备注!");
		return false;
	}
	/**
	if(transportNo==null||transportNo==""){
		MyAlert("请输入发运单号,若为自送:请输入0000");
		return false;
	}
	**/
	if(PART_PAKGE==null||PART_PAKGE==""){
		MyAlert("请选择包装情况!");
		return false;
	}
	if(PART_MARK==null||PART_MARK==""){
		MyAlert("请选择故障卡情况!");
		return false;
	}
	if(PART_DETAIL==null||PART_DETAIL==""){
		MyAlert("请选择清单情况!");
		return false;
	}
	return true;
	}
	
	function checkMax(){
	var remark = document.getElementById("signRemark").value;
	if(remark.length>50){
		MyAlert("签收备注不超过50字!");
		document.getElementById("signRemark").focus();
	   return false;
	}
	}
</script>
</head>
<body onload="init();">
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置：售后服务管理&gt;索赔旧件管理&gt;索赔旧件审批入库</div>
<form method="post" name="fm" id="fm"><input type="hidden" name="i_back_id" id="i_back_id" value="${claim_id }" />
	<input type="hidden" name="types" id="types" value="${types}" />
	<input type="hidden" name="price1" id="price1" value="<%=CommonUtils.checkNull(detailBean.getPrice()) %>" />
	<input  type="hidden" name="yieldly" id="yieldly" value="<%=CommonUtils.checkNull(detailBean.getYieldly())%>"/>
    <div class="form-panel">
    <h2>基本信息</h2>
    <div class="form-body">
	<table border="0" align="center" cellpadding="1" cellspacing="1" class="table_query">
	<tr >
		<td class="right">经销商代码：</td>
		<td><%=CommonUtils.checkNull(detailBean.getDealer_code())%></td>
		<td class="right">经销商名称：</td>
		<td><%=CommonUtils.checkNull(detailBean.getDealer_name())%></td>
		<td class="right">所属区域：</td>
		<td><%=CommonUtils.checkNull(detailBean.getAttach_area())%></td>
	</tr>
	<tr >
		<td class="right">回运清单号：</td>
		<td><%=CommonUtils.checkNull(detailBean.getReturn_no())%></td>
		<%-- <td class="right">回运类型：</td>
		<td ><%=CommonUtils.checkNull(detailBean.getReturn_desc())%></td> --%>
		<td class="right">旧件回运起止时间：</td>
		<td colspan="1">
			<%=CommonUtils.checkNull(detailBean.getWr_start_date()) %>
		</td>
		<td class="right">发运单号：</td>
		<td ><%=CommonUtils.checkNull(detailBean.getTransport_no()) %></td>
	</tr>
	<tr >
		<td class="right" >发运时间：</td>
		<td id="fay_time"><%=CommonUtils.checkNull(detailBean.getCreate_date())%></td>
		<td class="right">货运方式：</td>
		<td><%=CommonUtils.checkNull(detailBean.getTransport_desc())%>
		<input name="transport" id="transport" value="<%=CommonUtils.checkNull(detailBean.getTransport_desc())%>" type="hidden"/>
		</td>
		<td class="right" >物流公司：</td>
		<td >
		  <%=CommonUtils.checkNull(detailBean.getTransportCompany())%>
		</td>
	</tr>
	<tr  id="bb">
		<td class="right">装箱总数：</td>
		<td><%=CommonUtils.checkNull(detailBean.getParkage_amount())%></td>
		<td class="right" >实到箱数：</td>
		<td ><input class="short_txt"  name="REAL_BOX_NO" id="REAL_BOX_NO"  value="<%=detailBean.getRealBoxNo()==0?detailBean.getParkage_amount():detailBean.getRealBoxNo() %>" />&nbsp;&nbsp;<span style="color:red">*</span> </td>
		</tr>
	<tr  id="bb">
		<td class="right">申请运费：</td>
		<td ><%=CommonUtils.checkNull(detailBean.getPrice()) %>(元)</td>
<!--		<td class="right" >审核运费：</td>-->
		<td class="right" nowrap="nowrap" >是否有外箱封面：</td>
	         <td align="left" nowrap="nowrap">
	          <script type="text/javascript">
	            genSelBoxExp("OUT_PART_PAKGE",<%=Constant.IF_TYPE%>,"<%=CommonUtils.checkNull(detailBean.getOutPartPackge()) %>",false,"","","false",'');
	           </script>
	          </td>
		<td  ><input type="hidden" class="middle_txt" maxlength="25" name="price" id="price" value="<%=detailBean.getNewPrice()==0?detailBean.getPrice():detailBean.getNewPrice() %>"/> </td>
		</tr>
	    <tr  id="aa">
		<td class="right" nowrap="nowrap" >包装情况：</td>
	         <td align="left" nowrap="nowrap">
	          <script type="text/javascript">
	            genSelBoxExp("PART_PAKGE",<%=Constant.OLD_PART_PAKGE%>,"<%=CommonUtils.checkNull(detailBean.getPartPakge()) %>",true,"","","true",'');
	           </script>
	          </td>
	          <td class="right" nowrap="nowrap" style="display:none;">故障卡情况：</td>
	         <td align="left" nowrap="nowrap" style="display:none;">
	          <script type="text/javascript">
	            genSelBoxExp("PART_MARK",<%=Constant.OLD_PART_MARK%>,"<%=CommonUtils.checkNull(detailBean.getPartMark()) %>",true,"","","true",'');
	            document.getElementById("PART_MARK").value = <%=Constant.OLD_PART_MARK_01%>;
	           </script>
	          </td>
	          <td class="right" nowrap="nowrap" >清单情况：</td>
	         <td align="left" nowrap="nowrap">
	          <script type="text/javascript">
	            genSelBoxExp("PART_DETAIL",<%=Constant.OLD_PART_DETAIL%>,"<%=CommonUtils.checkNull(detailBean.getPartDetail()) %>",true,"","","true",'');
	           </script>
	          </td>
	</tr>	
	<tr>
		<td class="right">申请备注：</td>
		<td colspan="5">
		  <%=CommonUtils.checkNull(detailBean.getRemark()) %>
		</td>
	</tr>
	<tr>
		<td class="right">发运备注：</td>
		<td colspan="5">
		  <%=CommonUtils.checkNull(detailBean.getTransport_remark()) %>
		</td>
	</tr>
	<tr>
		<td class="right" valign="top">签收备注：</td>
		<td colspan="5">
			<textarea name="signRemark" id="signRemark" rows="5" onblur="checkMax();" cols="80"><%=CommonUtils.checkNull(detailBean.getSignRemark()) %></textarea>
			<span style="color: red">注：签收备注不超过50字</span>
		</td>
	</tr>
    </table>
    </div>
    </div>
    <!-- 附件信息 -->
    <div class="form-panel">
	<h2>附件信息</h2>
	<div class="form-body">
	  <table class="table_list"   id="file">
	  <tr >
	    <th class="center"> 附件名称 </th>
	    <th class="center"> 操作</th>
	  </tr>
	  <c:forEach items="${fileList}" var="list" varStatus="st">
	  <tr>
	    <td class="center">${list.filename }</td>
		<td class="center">
		  <a href="<%=contextPath%>/util/FileDownLoad/fileDownloadQuery.do?fjid=${list.fjid}" >&nbsp;下载</a>
		</td>
	  </tr>
      </c:forEach>
	</table>
	</div>
	</div>
	<!-- 回运清单明细 -->
	<div class="form-panel">
	<h2>回运清单明细</h2>
	<div class="form-body">
    <table border="0" align="center" cellpadding="1" cellspacing="1" class="table_list" style="border-bottom:1px solid #DAE0EE;" ondrag="return false;">
        <tr class="table_list_th">
            <th align="center">序号</th>
            <th align="center">索赔申请单</th>
            <th align="center">VIN</th>
            <th align="center">配件代码</th>
            <th align="center">配件名称</th>
            <th align="center">产地</th>
            <th align="center">需回运数</th>
            <th align="center">回运数</th>
            <th align="center">装箱单号</th>
            <th align="center">签收数</th>
            <th align="center">扣件说明</th>
              <th align="center">编号</th>
              <th align="center">审核日期</th>
       </tr>
       <c:forEach var="detailList" items="${detailList}" varStatus="num">
        <tr class="table_list_row1">
           <td align="center">
             <c:out value="${num.index+1}"></c:out>
           </td>
           <td align="center">
               <c:out value="${detailList.claim_no}"></c:out>
           </td>
           <td align="center">
               <c:out value="${detailList.vin}"></c:out>
           </td>
           <td align="center">
               <c:out value="${detailList.part_code}"></c:out>
           </td>
           <td align="center">
               <c:out value="${detailList.part_name}"></c:out>
           </td>
           <td align="center">
               <c:out value="${detailList.proc_factory}"></c:out>
           </td>
           <td align="center">
               <c:out value="${detailList.n_return_amount}"></c:out>
           </td>
           <td align="center">
               <c:out value="${detailList.return_amount}"></c:out>
           </td>
           <td align="center">
              <c:out value='${detailList.box_no}'/>
           </td>
            <td align="center">
               <c:out value="${detailList.sign_amount}"></c:out>
           </td>
           <td align="center">
	       	   <c:if test="${detailList.deduct_remark == '其它'}">
           	  		<c:out value='${detailList.deduct_remark}:${detailList.other_remark}'/>
           	  </c:if>
           	  
           	  <c:if test="${detailList.deduct_remark != '其它'}">
           	  		<c:out value='${detailList.deduct_remark}'/>
           	  </c:if>
           </td>
           
            <td align="center">
              <c:out value='${detailList.barcode_no}'/>
           </td>
           <td align="center">
               <c:out value="${detailList.report_date}"></c:out>
           </td>
         </tr>
      </c:forEach>  
     </table>
     </div>
     </div>
     <table class="table_edit" border="0" id="file">
	    	<tr >
    			<td width="100%" colspan="2">&nbsp;</td>
  			</tr>
     </table> 
		
    <table class="table_query" id="id2">
	<tr>
		<td class="center" colspan="2">
		<c:if test="${temp ==1 }">
		<input type="button" onclick="preChecked(1);" id="qianshou"  class="normal_btn" value="撤销签收" />&nbsp;&nbsp;
		<input type="button" onclick="preChecked2();" style="display: none;" id="qianshou2"  class="normal_btn" value="退回" />&nbsp;&nbsp;
		</c:if>
		<c:if test="${temp !=1 }">
		<input type="button" onclick="preChecked(0);" id="qianshou"  class="normal_btn" value="签收" />&nbsp;&nbsp;
		<input type="button" onclick="preChecked2();" id="qianshou2"  class="normal_btn" value="退回" />&nbsp;&nbsp;
		</c:if>
		
		<input type="button" onclick="backTo();"class="normal_btn" value="返回" />
		</td>
	</tr>
</table>
<br /><br />
</form>
</body>
</html>
