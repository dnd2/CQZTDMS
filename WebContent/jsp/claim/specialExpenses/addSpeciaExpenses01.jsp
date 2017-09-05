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
</script>
</head>
<body>
<div id="loader" style='position:absolute;z-index:200;background:#FFCC00;padding:1px;top:4px;display:none;display: none;'></div>
<div class="navigation"><img src="<%=request.getContextPath()%>/img/nav.gif" />&nbsp;当前位置： 售后服务管理 &gt; 特殊费用管理 &gt;特殊费用添加</div>
 <form method="post" name="fm" id="fm">
 <input type="hidden" id="vin2"/>
 <input type="hidden" id="activity_id" name="activity_id"/>
 <input type="hidden" name="feeType" value="${feeType}"/>
 <input type="hidden" name="reqGuId" value="<%=request.getSession().getAttribute("GuId") %>">
   <table width=100% border="0" align="center" cellpadding="1" cellspacing="1" bgcolor="91908E" class="table_edit">
   		<tr>
          <th colspan="6"><img class="nav" src="<%=contextPath %>/img/subNav.gif" /> 基本信息</th>
        </tr>
		  <tr>
		    <td nowrap="true"  width="10%" class="table_query_2Col_label_7Letter" >申报单位代码：</td>
		    <td class="table_info_3col_input">
		    	<c:out value="${map.DEALER_CODE}"/>
		    </td>
		    <td nowrap="true"  width="10%" class="table_query_2Col_label_7Letter" >申报单位名称：</td>
		    <td>
		    	<c:out value="${map.DEALER_NAME}"/>
		    </td>
		    <td nowrap="true"  width="10%" class="table_query_2Col_label_7Letter"  >结算费用类型</td>
		    <td class="table_info_3col_input"> 
		    <script type="text/javascript">
					genSelBoxExp("balance_fee_type",<%=Constant.TAX_RATE_FEE%>,"",false,"short_sel","","true",'');
 			</script>
 		</td>
	      </tr>
		  <tr>
		    <td nowrap="true"  width="10%" class="table_query_2Col_label_7Letter" >制单日期：</td>
		    <td class="table_info_3col_input">
		    	<c:out value="${map.SDATE}"/>
		    </td>
		    <td nowrap="true"  width="10%" class="table_query_2Col_label_7Letter" >结算厂家：</td>
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
		    <td nowrap="true"  width="10%" class="table_query_2Col_label_7Letter" >申报金额(元)：</td>
		    <td>
		    	<input type="text" name="declareSum" id="declareSum" datatype="0,isMoney,30" maxlength="10" class="middle_txt"/>
		    </td>
	      </tr>
	      <tr>
		    <td nowrap="true"  width="10%" class="table_query_2Col_label_7Letter" >服务站联系人：</td>
		    <td>
		    	<input type="text" name="linkman" id="linkman" class="middle_txt" datatype="0,is_null,20"  value="" />
		    </td>
		    <td nowrap="true"  width="10%" class="table_query_2Col_label_7Letter" >服务站联系电话：</td>
		    <td>
		    	<input type="text" name="tel" id="tel" class="middle_txt" datatype="0,is_null,20"  value="" />
		    </td>
		    <td  nowrap="true"  width="10%" class="table_query_2Col_label_7Letter" >费用类型：</td>
		    <td  >
		    	  <script type='text/javascript'>
				       var activityType=getItemValue('${feeType}');
				       document.write(activityType) ;
				     </script>
		    </td>
	      </tr>			 
	      <tr>
		    <td nowrap="true"  width="10%" class="table_query_2Col_label_7Letter" >VIN：</td>
		    <td>
			    <c:if test="${code.codeId==80081001}">
			    	<input type="text" name="vin" id="vin" class="middle_txt" blurback="true" datatype="0,is_vin,17" maxlength="17" value="" />
			    </c:if>
			    <c:if test="${code.codeId==80081002}">
			    	<input type="text" name="vin" id="vin" class="middle_txt" blurback="true" datatype="1,is_vin,17" maxlength="17" value="" />
			    </c:if>
		    </td>
		    <td nowrap="true"  width="10%" class="table_query_2Col_label_7Letter" >车型：</td>
		    <td>
		    	<input type="text" name="model" id="model" class="middle_txt" datatype="1,is_null,40"  value="" />
		    </td>
		    <td nowrap="true"  width="10%" class="table_query_2Col_label_7Letter" >索赔单号</td>
		    <td >
		    	<input name='showBtn' type='button' class='mini_btn' style='cursor: pointer;' onclick='showClaimNo();' value='...' />
		    	<input type="text"  name="claimNo" id="claim_no"  value="" readonly="readonly">
		    </td>
	      </tr>	
	      
	       <tr>
		    <td nowrap="true"  width="10%" class="table_query_2Col_label_7Letter" >主因件名称：</td>
		    <td>
		    	<input name='showBtn1' type='button' class='mini_btn' style='cursor: pointer;'  onclick='open_part()' value='...' />
		    	<input name="part_name" id="part_name" datatype="0,is_null,200" value="" readonly="readonly">
		    	<input type="hidden" name="part_code" id="part_code" value="" >
		    </td>
		    <td nowrap="true"  width="10%" class="table_query_2Col_label_7Letter" >制造商代码：</td>
		    <td>
		    	<input name='showBtn' type='button' class='mini_btn' style='cursor: pointer;' onclick='supplier()' value='...' />
		    	<input type="text"  name="supplier_code" id="supplier_code"  value=""  datatype="0,is_null,200" readonly="readonly">
		    </td>
		    <td nowrap="true"  width="10%" class="table_query_2Col_label_7Letter" >
		    	制造商名称：
		    </td>
		    <td nowrap="true"  width="10%" class="table_query_2Col_label_7Letter" >
		    	<input type="text"  name="supplier_name" class="middle_txt" id="supplier_name"  readonly="readonly" value="">
		    </td>
		   
	      </tr>		
	      
          <tr >
            <td nowrap="true"  width="10%" class="table_query_2Col_label_7Letter" >备注：</td>
            <td colspan="5">
              <textarea name="remark" id="remark" datatype="0,is_textarea,100" maxlength="100" rows='5' cols='80' ></textarea>
            </td> 
          </tr>
        </table>        
<!-- 添加附件 开始  -->
	<table class="table_info" border="0" id="file">
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
  <!-- 添加附件 结束 -->
   <!-- 按钮 begin -->
   <table class="table_list">
      <tr > 
      	<th height="12" align=center>
			<input type="button" onClick="addSpeciaExpenses(1)" class="normal_btn" id="btn1" style="width=8%" value="保存"/>&nbsp;&nbsp;
			<input type="button" onClick="addSpeciaExpenses(2)" class="normal_btn" id="btn2" style="width=8%" value="提报"/>&nbsp;&nbsp;
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
	function addSpeciaExpenses(val)
	{   blurBack();
		if(submitForm(fm)==false) return ;
		var yieldly = $('yieldly').value ;
		if(!yieldly){
			MyAlert('结算厂家为必填项！');
			return ; 
		}
		//附件添加必填限制。
		//if(val==2){
		//	var arr = document.getElementsByName('uploadFileId') ;
		//	if(arr.length==0){
		//		MyAlert('附件为必填项！');
		//		return ;
		//	}
		//}
		fm.action = "<%=contextPath%>/claim/specialExpenses/SpecialExpensesManage/addSpeciaExpensesDO.do";
		MyConfirm("确认新增！",addSpecia, [val]);
	}
   
   function open_part()
   {
   		OpenHtmlWindow('<%=contextPath%>/claim/specialExpenses/SpecialExpensesManage/open_part.do',800,500);
   }
   
   
   
   	function showActivity(){
		var dealer_code = '${map.DEALER_CODE}';
		var url = '<%=contextPath%>/claim/specialExpenses/SpecialExpensesManage/queryActivityInit.do?code='+dealer_code ;
		OpenHtmlWindow(url,800,500);
   	}
	function addSpecia(val)
	{
		if(val == 2){
			$('btn1').disabled = true ;
			$('btn2').disabled = true ;
		}
		makeNomalFormCall("<%=contextPath%>/claim/specialExpenses/SpecialExpensesManage/addSpeciaExpensesDO.json?feeFlag="+val+"&uuFlag=2",addSpeciaBack,'fm','queryBtn'); 
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
	
	function blurBack()
	{
		var vin = document.getElementById("vin").value;
		var vinlen = len(vin);
		if(vinlen==17)
		{
			makeNomalFormCall("<%=contextPath%>/claim/specialExpenses/SpecialExpensesManage/getModel.json?vin="+vin,getModelBack,'fm','queryBtn'); 
		}
	}
	
	function getModelBack(json)
	{
		if(json.map==null)
		{
			MyAlert("无效VIN！请输入正确VIN！");
		}
		else
		{
			document.getElementById("model").value = json.map.GROUP_NAME;
		}
	}
	
	function len(s) 
	{ 
		var l = 0; 
		var a = s.split(""); 
		for (var i=0;i<a.length;i++) 
		{ 
			if (a[i].charCodeAt(0)<299) 
			{ 
				l++; 
			} 
			else 
			{ 
				l+=2; 
			} 
		} 
		return l; 
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
