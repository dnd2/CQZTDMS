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
<title>修改市场工单费用</title>
<script type="text/javascript">
</script>
</head>
<body>
<div id="loader" style='position:absolute;z-index:200;background:#FFCC00;padding:1px;top:4px;display:none;display: none;'></div>
<div class="navigation"><img src="<%=request.getContextPath()%>/img/nav.gif" />&nbsp;当前位置： 售后服务管理&gt;特殊费用管理&gt;市场工单费用修改</div>
 <form method="post" name="fm" id="fm">
 <input type="hidden" name="id" value="<c:out value="${map.ID}"/>"/>
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
		    <td align="right" >结算费用类型</td>
		    <td class="table_info_3col_input"> 
		    <script type="text/javascript">
					genSelBoxExp("balance_fee_type",<%=Constant.TAX_RATE_FEE%>,"${map.BALANCE_FEE_TYPE}",false,"short_sel","","true",'');
 			</script>
 		</td>
	      </tr>
		  <tr>
		    <td align="right">制单日期：</td>
		    <td>
		    	<c:out value="${map.CREATE_DATE}"/>点
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
		    <td align="right">申报金额(元)：</td>
		    <td>
		    	<input type="text" value="<c:out value="${map.DECLARE_SUM1}"/>" name="declareSum" id="declareSum" datatype="0,isMoney,30" class="middle_txt"/>
		    </td>
	      </tr>
		  <tr>
		    <td align="right">VIN：</td>
		    <td>
		    	<c:if test="${code.codeId==80081001}">
			    	<input type="text" name="vin" id="vin" class="middle_txt" blurback="true" datatype="0,is_vin,17" maxlength="17" value="${map.VIN}" />
			    </c:if>
			    <c:if test="${code.codeId==80081002}">
			    	<input type="text" name="vin" id="vin" class="middle_txt" blurback="true" datatype="1,is_vin,17" maxlength="17" value="${map.VIN}" />
			    </c:if>
		    </td>
		    <td align="right">车型：</td>
		    <td>
		    	<input type="text" name="model" id="model" class="middle_txt" datatype="1,is_null,40"  value="<c:out value="${map.V_MODEL}"/>" />
		    </td>
		    <td align="right">费用类型：</td>
		    <td>
		        <script type='text/javascript'>
				       var activityType=getItemValue('${map.FEE_TYPE}');
				       document.write(activityType) ;
				     </script>
		    </td>
	      </tr>
	      <tr>
		    <td align="right">服务站联系人：</td>
		    <td>
		    	<input type="text" name="linkman" id="linkman" class="middle_txt" datatype="0,is_null,20"  value="<c:out value="${map.LINKMAN}"/>" />
		    </td>
		    <td align="right">服务站联系电话：</td>
		    <td>
		    	<input type="text" name="tel" id="tel" class="middle_txt" datatype="0,is_null,20"  value="<c:out value="${map.LINKMAN_TEL}"/>" />
		    </td>
		    <td align="right" id="my_td1"></td>
		    <td id="my_td2"></td>
	      </tr>	
	      <tr>
		    <td align="right">主因件名称：</td>
		    <td>
		    	<input name='showBtn' type='button' class='mini_btn' style='cursor: pointer;' onclick='open_part()' value='...' />
		    	<input name="part_name" id="part_name"  value="${map.PART_NAME}" readonly="readonly">
		    	<input type="hidden" name="part_code" id="part_code" value="${map.PART_CODE}" >
		    </td>
		     <td nowrap="true"  width="10%" class="table_query_2Col_label_7Letter" >制造商代码：</td>
		    <td>
		    	<input name='showBtn' type='button' class='mini_btn' style='cursor: pointer;' onclick='supplier()' value='...' />
		    	<input type="text"  name="supplier_code" id="supplier_code"  value="${map.SUPPLIER_CODE}" readonly="readonly">
		    </td>
		    <td nowrap="true"  width="10%" class="table_query_2Col_label_7Letter" >
		    	制造商名称：
		    </td>
		    <td nowrap="true"  width="10%" class="table_query_2Col_label_7Letter" >
		    	<input type="text"  name="supplier_name" class="middle_txt" id="supplier_name"  readonly="readonly" value="${map.SUPPLIER_NAME}">
		    </td>
	      </tr>		
          <tr >
          	<td nowrap="true"  width="10%" class="table_query_2Col_label_7Letter" >索赔单号</td>
		    <td >
		    	<input name='showBtn' type='button' class='mini_btn' style='cursor: pointer;' onclick='showClaimNo();' value='...' />
		    	<input type="text"  name="claimNo" id="claim_no"  value="${map.CLAIM_NO}" readonly="readonly">
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
    		addUploadRowByDbView('<%=CommonUtils.checkNull(fileList.get(i).getFilename()) %>','<%=CommonUtils.checkNull(fileList.get(i).getFjid()) %>','<%=CommonUtils.checkNull(fileList.get(i).getFileurl())%>','<%=CommonUtils.checkNull(fileList.get(i).getFjid()) %>','<%=request.getContextPath()%>/util/FileDownLoad/FileDownLoadQuery.do?fjid=' );
    	</script>
	<%}%>
	</table> 
  <!-- 添加附件 结束 -->
   <!-- 按钮 begin -->
   <table class="table_list">
      <tr > 
      	<th height="12" align=center>
			<input type="button" onClick="addSpeciaExpenses(1)" class="normal_btn" style="width=8%" value="保存"/>&nbsp;&nbsp;
			<input type="button" onClick="addSpeciaExpenses(2)" id="btn2" class="normal_btn" style="width=8%" value="提报"/>&nbsp;&nbsp;
			 <!-- <input type="button" onClick="delSpefee();" class="normal_btn" value="删除"/>&nbsp;&nbsp; -->
			<input type="button" onClick="javascript:history.go(-1);" class="normal_btn" style="width=8%" value="返回"/>
	   	</th>
	  </tr>
   </table>
   <!-- 按钮 end -->
</form>
<script type="text/javascript">
function showClaimNo(){
 	OpenHtmlWindow('<%=contextPath%>/claim/specialExpenses/SpecialExpensesManage/showClaimNo.do',800,500);
}
function setClaimNo(claimNo){
	$("claim_no").value=claimNo;
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
	//添加
	function addSpeciaExpenses(val)
	{ 
		if(submitForm(fm)==false) return ;
		MyConfirm("确认提交！",addSpecia, [val]);
	}
	
	function addSpecia(val)
	{
		if(val==2){
			$('btn2').disabled = true ;
		}
		makeNomalFormCall("<%=contextPath%>/claim/specialExpenses/SpecialExpensesManage/updateSpeciaExpensesDO.json?feeFlag="+val,addSpeciaBack,'fm','queryBtn'); 
	}
	var obj = $('feeType1');
    if(obj){
   		obj.attachEvent('onchange',getTypeChangeStyleParam);//
   	}
   	function getTypeChangeStyleParam() {
   		
   		//系统判断
	   	if(${code.codeId}=='<%=Constant.chana_wc%>'){
	   		var sel = "genSelBoxExpforThisPage('activityProject',"+<%=Constant.ACTIVITY_PROJECT%>+",'"+$('activity_project').value+"',false,'short_sel','','true','')";
	   		var sel2 = "<input type='text' class='middle_txt' id='activity_name' name='activity_name' value='"+$('activity_name').value+"' readonly/><input type='button' value='...' class='mini_btn' onclick='showActivity();'/>" ;
	   		var val = $('feeType1').value ;
	   		if(val=='<%=Constant.FEE_TYPE1_01%>'){
	   		//if(true){
				$('my_td1').innerHTML = '活动项目：' ;
				$('my_td2').innerHTML = eval(sel);
				//$('my_td2').innerHTML = '<input type="text" class="middle_txt" value="商誉索赔" readonly/>' ;
	   		}else if(val=='<%=Constant.FEE_TYPE1_02%>'){
				$('my_td1').innerHTML = '活动项目：' ;
				$('my_td2').innerHTML = sel2;
				//$('activity_name').value=${map.ACTIVITY_NAME};
	   		}else{
	   			$('my_td1').innerHTML = '' ;
				$('my_td2').innerHTML = '' ;
	   		}
	   	}
   	}
   	getTypeChangeStyleParam();
   	function showActivity(){
		var dealer_code = '${map.DEALER_CODE}';
		var url = '<%=contextPath%>/claim/specialExpenses/SpecialExpensesManage/queryActivityInit.do?code='+dealer_code ;
		OpenHtmlWindow(url,800,500);
   	}
   	function setActivity(value){
   	   	$('activity_id').value = value ;
		var url = '<%=contextPath%>/claim/specialExpenses/SpecialExpensesManage/queryActivityName.json?id='+value ;
		sendAjax(url,a__back,'fm');
   	}
   	function a__back(json){
		$('activity_name').value = json.name ;
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
	//特殊费用的删除
	function delSpefee(){
		MyConfirm("确认删除？",delSpefeeSure);
	}
	function delSpefeeSure(){
		fm.action = "<%=contextPath%>/claim/specialExpenses/SpecialExpensesManage/delSpefee.do";
		fm.submit();
	}
	
		//重写下拉框JS
	//根据FIXCODE形成下拉框，除了expStr字符串中包含的状态，expStr可为多个中间用逗号隔开
function genSelBoxExpforThisPage(id,type,selectedKey,setAll,_class_,_script_,nullFlag,expStr){
	var str = "";
	var arr;
	if(expStr.indexOf(",")>0)
		arr = expStr.split(",");
	else {
		expStr = expStr+",";
		arr = expStr.split(",");
	}
	str += "<select id='" + id + "' name='" + id +"' class='"+ _class_ +"' " + _script_ ;
	// modified by lishuai@infoservice.com.cn 2010-05-18 解决select下拉框中增加属性datatype判断bug begin
	if(nullFlag && nullFlag == "true"){
		str += " datatype='0,0,0' ";
	}
	// end
	str += " onChange=doCusChange(this.value);> ";
	if(setAll){
		str += genDefaultOpt();
	}
	for(var i=0;i<codeData.length;i++){
		var flag = true;
		for(var j=0;j<arr.length;j++){
			if(codeData[i].codeId == arr[j]){
				flag = false;
			}
		}
		if(codeData[i].type == type && flag){
			str += "<option " + (codeData[i].codeId == selectedKey ? "selected" : "") + " value='" + codeData[i].codeId + "' title = '"+codeData[i].codeDesc+"' >" + codeData[i].codeDesc + "</option>";
		}
	}
	str += "</select>";	
	return str;
}
</script>
</body>
</html>
