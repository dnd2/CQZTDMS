<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.Constant"%>
<%@ page import="com.infodms.dms.po.TcCodePO"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@page import="java.util.List"%>
<%@page import="java.util.Map"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>索赔旧件审核入库</title>
<%
	String contextPath = request.getContextPath();
	List<Map<String, Object>> list = (List) request.getAttribute("list");
%>
<script type="text/javascript">
	var purchaserArray = new Array(); //创建一个抵扣原因列表
    <c:forEach var= "list" items="${deductList}" varStatus="sta"> //得到有数据的数组集合
    	purchaserArray.push(['${list.CODE_ID}&&${list.CODE_DESC}']);//得到数组的内容（实体bean)加入到新的数组里面
    </c:forEach>
    var isFlag=false;
    var flag=false;
</script>
</head>
<BODY onload="doInit();">
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif"/>&nbsp;当前位置： 售后服务管理&gt;索赔旧件管理&gt;索赔主管扣件审核</div>
  <form id="fm" name="fm">
    
  	<input type="hidden" name="i_back_id" id="i_back_id" value="${returnListBean.id }" />
	<input type="hidden" name="types" id="types" value="${types}" />
	<input type="hidden" name="yieldly" id="yieldly" value="${yieldly}" />
	<input type="hidden"   name ="return_id" id="return_id" value="${returnListBean.id }"/>
	<input type="hidden"  name ="pageNum" id="pageNum" value=""/>
	<table class="table_edit">
			<tr>
				<th colspan="10">
					<img src="<%=contextPath%>/img/subNav.gif" alt="" class="nav" />
					&nbsp;入库旧件查询条件
				</th>
			</tr>
		</table>
    <TABLE class="table_query" >
      <tr>
		<td align="right"  >装箱单号:</td>
		<td  align="left" >
			<select name="boxNo" class="short_sel" id="boxNo">
				<option value="">--请选择--</option>
				<c:forEach var="listBoxNo" items="${listBoxNo }">
					<c:if test="${boxNo==listBoxNo.BOX_NO }">
						<option value="${listBoxNo.BOX_NO }" selected="selected">${listBoxNo.BOX_NO }</option>
					</c:if>
					<c:if test="${boxNo!=listBoxNo.BOX_NO }">
						<option value="${listBoxNo.BOX_NO }">${listBoxNo.BOX_NO }</option>
					</c:if>
				</c:forEach>
			</select>
			<input id="claimId" name="claimId" type="hidden" value="${claim_id }"/>
			<input id="CLAIM_ID" name="CLAIM_ID" type="hidden" value="${claim_id }"/>
			<!--  条码：<input id="bar_code" name="bar_code" class="middle_txt"type="text" value="${bar_code }"/>-->
		</td>
		<td   align="right">签收数：</td>
		<td  align="left" >
			<select   id="sing_num" class="short_sel" name="sing_num" >
			<option value="" >--请选择--</option>
			<option value="0" >0</option>
			<option value="1" >1</option>
			</select>
		</td>
		<td align="right" >旧件类型：</td>
		<td  align="left" >
			<select   id="is_import" class="short_sel" name="is_import" >
			<option value="" >--请选择--</option>
			<option value="10041002" >北汽幻速</option>
			<option value="10041001" >北汽销售</option>
			</select>
		</td>
	</tr>
	<tr>
		<td  align="right" >配件代码：</td>
		<td   align="left">
			<input id="part_code" name="part_code" class="middle_txt"type="text" value="${part_code }"/>
		</td>
		<td  align="right" >配件名称：</td>
		<td  align="left" >
			<input id="part_name" name="part_name" class="middle_txt" type="text" value="${part_name }"/>
		</td>
		<td  align="right" >索赔单号：</td>
		<td  align="left" >
			<input id="claim_no" name="claim_no" class="middle_txt" type="text" value="${claim_no }"/>
		</td>
	</tr>
	<tr>
		<td align="right" >经销商名称：</td>
		<td align="left" >
			<input name="dealerName" type="text" id="dealerName" maxlength="25" class="middle_txt"/></td>
		</td>
		<td  align="right" >经销商编码:</td>
		<td align="left"  >
			<input class="middle_txt" id="dealerCode"  name="dealerCode" type="text"  maxlength="25"/>
		</td>	
	   <td align="right" >VIN：</td>
		<td align="left" colspan="5" >
			<input name="vin" type="text" id="vin" maxlength="25" class="middle_txt"/></td>
		</td>
	</tr>
	<tr>
		
	</tr>
       <tr>
         <td align="center" colspan="6" nowrap="nowrap">
          <input type="button" class="normal_btn" name="query" onclick="__extQuery__(1);" value="查询"/>
         </td>
       </tr>
  </table>
  <table class="table_edit" style="display: none">
			<tr>
				<th colspan="10">
					<img src="<%=contextPath%>/img/subNav.gif" alt="" class="nav" />
					&nbsp;旧件条码扫描框
				</th>
			</tr>
		</table>
	<TABLE class="table_query"style="display: none">
      <tr>
		<td>
 			条码扫描处：<input id="barCode" name="barCode"  onkeyup="changeValue(this);" class="middle_txt"type="text" value=""/>
		</td>
		</tr>
  </table>
  <!--分页 begin -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
  <!--分页 end --> 
<div id="loader" style='position:absolute;z-index:200;background:#FFCC00;padding:1px;top:4px;display:none;display: none;'></div>
</form> 
<form id="form1" name="form1" style="display:none">
  <table id="bt" class="table_list">
	 <tr>
		<td height="10" align="center" >
			<input type="button" onclick="sureChecked(1);"  id="qianshou1" class="normal_btn" value="确认扣件" />&nbsp;&nbsp;
			<input type="button" onclick="preChecked(2);"  id="qianshou2" class="normal_btn" value="驳回扣件" />&nbsp;&nbsp;
			<input type="button" onclick="backTo();" id="backBtn" class="normal_btn" value="返回" />&nbsp;&nbsp;
		</td>
	</tr>
  </table>
</form>
<br>
<script type="text/javascript">
 document.form1.style.display = "none";

   var HIDDEN_ARRAY_IDS=['form1'];
   
   var myPage;
   //查询路径
   var url = "<%=contextPath%>/claim/oldPart/ClaimOldPartApporoveStorageManager/oldPartSignAuditSerchZg.json";
				
   var title = null;

   var columns = [
  				{header: "序号",align:'center',renderer:getIndex},
  				{id:'action',header: "选择<input type='checkbox' name='checkAll' onclick='selectAll(this,\"checkId\")' />", width:'3%',align:'center',sortable: false,dataIndex: 'ID',renderer:myCheckBox},
  				{header: "主管意见", dataIndex: 'CLAIM_NO', align:'center',renderer:myLink7},
				{header: "索赔申请单", dataIndex: 'CLAIM_NO', align:'center',renderer:myLink},
  				{header: "回运数",dataIndex: 'RETURN_AMOUNT',align:'center',renderer:myLink3},
  				{header: "签收数", dataIndex: 'SIGN_AMOUNT', align:'center'},
  				{header: "装箱单号",dataIndex: 'BOX_NO',align:'center'},
  				{header: "扣除原因",dataIndex: 'DEDUCT_REMARK',align:'center',renderer:getItemValue},
  				{header: "责任性质",dataIndex: 'IS_MAIN_CODE',align:'center',renderer:getItemValue},
  				{header: "配件代码",dataIndex: 'PART_CODE',align:'center'},
  				{header: "配件名称", dataIndex: 'PART_NAME', align:'center',renderer:myLink5},
  				{header: "编号",dataIndex: 'BARCODE_NO',align:'center'},
  				{header: "存放库位",dataIndex: 'LOCAL_WAR_HOUSE',align:'center'},
  				{header: "供应商代码", dataIndex: 'PRODUCER_CODE', align:'center'},
  				{header: "供应商名称", dataIndex: 'PRODUCER_NAME', align:'center'},
  				{header: "VIN", dataIndex: 'VIN', align:'center'},
  				{header: "维修日期",dataIndex: 'RO_STARTDATE',align:'center'},
  				{header: "索赔单类型",dataIndex: 'CLAIM_TYPE',align:'center',renderer:getItemValue}
  		      ];
  		      __extQuery__(1);
  		    function myCheckBox(value,metaDate,record){
  	  		    var input2 ='<input type="checkbox" id="checkId" name="checkId" value="'+value+'" />';
  				return String.format(input2);
  			}
  			
 function myLink(value,metaDate,record){
	 var imports = record.data.IS_IMPORT;
	 if(imports==<%=Constant.IF_TYPE_YES%>){
		 return String.format( "<input type='hidden' id=\"barNo"+record.data.ID+"\" value='" + record.data.BARCODE_NO + "' /><input type='hidden' name='orderIds' value='" + record.data.ID + "' />["+record.data.CLAIM_NO+"]");
		 }else{
			 return String.format( "<input type='hidden' id=\"barNo"+record.data.ID+"\" value='" + record.data.BARCODE_NO + "' /><input type='hidden' name='orderIds' value='" + record.data.ID + "' /><a href='#' onClick='claimDetail(\""+record.data.CLAIM_ID+"\");'>["+record.data.CLAIM_NO+"]</a>");
			 }
 		}
  function myLink7(value,metaDate,record){
    var res = String.format("<input type='text' style='width: 200px;'  name= 'Executive_director_ram' id='Executive_director_ram'"+value+"' >" );
 	return String.format(res);
 }
 //库位修改时，进行异步更新数据
 function signforwad2(id){
		var local =  document.getElementById('local'+id).value;
		local = local.replace(/[ ]/g,""); 
		if(local.length==5){
			var url="<%=contextPath%>/claim/oldPart/ClaimOldPartApporoveStorageManager/approveAndStored44.json?id="+id+"&localWar="+local;
			makeNomalFormCall(url,afterWar,'fm','');
		}
	}
	function afterWar(json){
		var retCode=json.updateResult;
	    if(retCode!=null&&retCode!=''){
	      if(retCode=="updateSuccess"){
	      document.getElementById('local'+json.ID).value=json.wrHouse;
	   	  }else{
	   	  document.getElementById('local'+json.ID).value="";
	   	  	MyAlert(retCode);
	   	  }
	    }
	}
 function claimDetail(id){
 	OpenHtmlWindow('<%=contextPath%>/claim/dealerClaimMng/ClaimBillTrack/claimBillDetailForward.do?ID='+id,900,500);
 }
  function myLink3(value,metaDate,record){
 		return String.format( "" + value + "<input type=\"hidden\" id=\"returnAmount"+record.data.ID+"\" name=\"returnAmount"+record.data.ID+"\" value=\"" + value + "\"/>");
 }
 function myLink4(value,metaDate,record){
 		var num = record.data.SIGN_AMOUNT;
 		var letter = record.data.IS_INVOICE;
 		 var out = record.data.IS_OUT;
 		var str = "";
 		if(num==1||letter==1||out==1){
 		 str = "<select class=\"short_sel\" disabled id = 'deduct"+record.data.ID+"' onmouseover='addPurchaserList(\"deduct"+record.data.ID+"\")'onChange=isCheck22('"+record.data.ID+"',this.value) ><option value='"+value+"'>"+record.data.DEDUCT_DESC+"</option>";
 		}else{
 		$('qianshou').disabled="";
 		 str = "<select class=\"short_sel\"  id = 'deduct"+record.data.ID+"' onmouseover='addPurchaserList(\"deduct"+record.data.ID+"\")'onChange=isCheck22('"+record.data.ID+"',this.value) ><option value='"+value+"'>"+record.data.DEDUCT_DESC+"</option>";
 		}
		str = str + "</select>";
		
		return String.format(str);
 }
 
 function addPurchaserList(parms)
	{	
		var obj = document.getElementById(parms);
		if(obj.options.children.length < 2)
		{
			var strTemp;
			var strsTemp= new Array();
			for(var i = 0; i < purchaserArray.length; i ++)
			{	
				strTemp = purchaserArray[i].toString();
				 //定义一数组
				strsTemp = strTemp.split("&&"); //字符分割     
				var uID = strsTemp[0];
				var uName = strsTemp[1];
				if(uID != obj.options.children[0].value)
				{
					obj.options.add(new Option(uName,uID));
				}
			}
		}
	}
 function myLink2(value,metaDate,record){
 	var res="";
 	var letter = record.data.IS_INVOICE;
 	 var out = record.data.IS_OUT;
 //	if(value==0){
 //		$('qianshou3').disabled=false;
 //	}
 	if(record.data.IS_SCAN==1||letter==1||out==1){
 		 res = String.format("<input type=\"text\" disabled id=\"signNum"+record.data.ID+"\" onkeyup=\"signforwad("+record.data.ID+","+0+");\" name=\"signNum"+record.data.ID+"\" class=\"short_txt\"  value=\"" + value+ "\"/>"+"<input type=\"hidden\" id=\"signNum1"+record.data.ID+"\" name=\"signNum1"+record.data.ID+"\"   value=\"" + value + "\"/>");
 	}else {
 		$('qianshou').disabled="";
 		 res = String.format("<input type=\"text\"  id=\"signNum"+record.data.ID+"\" onkeyup=\"signforwad("+record.data.ID+","+0+");\" name=\"signNum"+record.data.ID+"\" class=\"short_txt\"  value=\"" + value+ "\"/>"+"<input type=\"hidden\" id=\"signNum1"+record.data.ID+"\" name=\"signNum1"+record.data.ID+"\"   value=\"" + value + "\"/>");
 	}
 	return String.format(res);
 }
  function myLink5(value,metaDate,record){
	  return String.format(""+value+"<input type=\"hidden\" id=\"partCode"+record.data.ID+"\" name=\"partCode"+record.data.ID+"\" value=\"" + record.data.PART_CODE + "\"/><input type=\"hidden\"id=\"partName"+record.data.ID+"\" name=\"partName"+record.data.ID+"\" value=\"" + record.data.PART_NAME + "\"/>");
 }
  function myLink6(value,metaDate,record){
  var letter = record.data.IS_INVOICE;
   var out = record.data.IS_OUT;
  if(letter!=1&&out!=1){
  		flag=true;
  }
	return String.format("<input type=\"text\" class=\"long_txt\" readonly id=\"supplierName"+record.data.ID+"\" name=\"supplierName"+record.data.ID+"\" value=\"" + value + "\"/>");
 }
  function myLink8(value,metaDate,record){
	  var letter = record.data.IS_INVOICE;
	   var out = record.data.IS_OUT;
	   if(record.data.IS_SCAN==1||letter==1||out==1){
		   return String.format("<input type=\"text\" class=\"short_txt\" readonly id=\"supplierCode"+record.data.ID+"\" name=\"supplierCode"+record.data.ID+"\" value=\"" + record.data.PRODUCER_CODE + "\"/>");
		  }else{
			  return String.format("<input type=\"text\" class=\"short_txt\" readonly id=\"supplierCode"+record.data.ID+"\" name=\"supplierCode"+record.data.ID+"\" value=\"" + record.data.PRODUCER_CODE + "\"/><input type=\"button\" class=\"mini_btn\" onclick=\"addSupply('" + record.data.PART_CODE + "'," + record.data.ID + ");\"   id=\"addsupply"+record.data.ID+"\" name=addsupply"+record.data.ID+"\" value=\"...\"/>");
	 }
  }
  function addSupply(code,id){
		OpenHtmlWindow('<%=contextPath%>/claim/oldPart/ClaimOldPartOutStorageManager/selectSupplierForward.do?code='+code+'&id='+id,800,430);	
		}
function setSupplier(code,name,id){
	$('supplierName'+id).value=name;
	$('supplierCode'+id).value=code;
	var url= "<%=contextPath%>/claim/oldPart/ClaimOldPartApporoveStorageManager/modSupp.json?id="+id+"&code="+code+"&name="+encodeURI(name);
	makeNomalFormCall(url,afterModSupp,'fm','createOrdBtn');
	}
function afterModSupp(json){
	if(json.msg=="succ"){
		
	}else{
		MyAlert("修改失败!");
		 __extQuery__(1);
		}
		
}
 function setFlag(){
 	if(!flag){
 		//$('qianshou').disabled=true;
	//	$('qianshou2').disabled=true;
 	}
 }
 function isCheck22(id,value){
		var returnAmount =  document.getElementById('returnAmount'+id).value;//回运数
		var signNum =  document.getElementById('signNum'+id).value;//签收数
		var signNum1 =  document.getElementById('signNum1'+id).value;//签收数(隐藏域)
		if(isNaN(signNum)){
			MyAlert('请输入数字!');
			return;
		}
		if(signNum>returnAmount){
			MyAlert('输入值不能大于需回运数!');
			return;
		}
		var pageNum = $('pageNum').value;
			var url="<%=contextPath%>/claim/oldPart/ClaimOldPartApporoveStorageManager/approveAndStored33.json?id="+id+"&signNum="+signNum+"&deduct="+value+"&pageNum"+pageNum;
			makeNomalFormCall(url,afterCall111,'fm','');
	}
	function signforwad(id,type){
		var returnAmount =  document.getElementById('returnAmount'+id).value;//回运数
		var signNum =  document.getElementById('signNum'+id).value;//签收数
		var reg = /^\d+$/;
		if(signNum==""){
			MyAlert('请输入签收数!');
			 document.getElementById('signNum'+id).value=0;
			 signforwad(id,0);
			return false;
		}else if(!reg.test(signNum)){
		MyAlert("签收数请输入正整数!");
		 document.getElementById('signNum'+id).value=0;
		  signforwad(id,0);
		return false;
		}else if(returnAmount <signNum ){
		MyAlert("签收数不能大于回运数!");
		 document.getElementById('signNum'+id).value=0;
		  signforwad(id,0);
		return false;
	}
	signNum = parseInt(signNum);
	if(signNum==0){
		$('qianshou3').disabled=false;
	}
		if(signNum<returnAmount){
			$("deduct"+id).disabled=false;
		}
		if(signNum>=returnAmount){
			$("deduct"+id).value='';
			$("deduct"+id).disabled=true;
		}
			var pageNum = $('pageNum').value;
			var value='';
			var url="<%=contextPath%>/claim/oldPart/ClaimOldPartApporoveStorageManager/approveAndStored33.json?id="+id+"&signNum="+signNum+"&deduct="+value+"&type="+type+"&pageNum"+pageNum;
			makeNomalFormCall(url,afterCall111,'fm','');
	}
	function afterCall111(json){
		var retCode=json.results;
	    if(retCode!=null&&retCode!=""){
	      MyAlert(retCode);
	    }
	    __extQuery__(json.pageNum);
	}
	function preChecked(val) {
        var i=validateSelectedId(val);
        if(i==1){		
       	 MyConfirm("你确认驳回扣件审核?",sign,[val]);
        }
	}
	function sign(val){
		$('qianshou1').disabled="disabled";
		$('qianshou2').disabled="disabled";
		var url= "<%=contextPath%>/claim/oldPart/ClaimOldPartApporoveStorageManager/oldPartSignAuditInZg.json?type="+val;
		makeNomalFormCall(url,afterCall,'fm','');
	}
	
	function afterCall(json){
		var retCode=json.updateResult;
	    if(retCode!=null&&retCode!=''){
	      if(retCode=="updateSuccess"){
	    	MyAlert("主管扣件审核成功!");
	    	 __extQuery__(1);
	      }else if(retCode=="updateFailure"){
	    	MyAlert("主管扣件审核!");
	     }
	   }
	}
	
	
	
	function  signAll(){
		 MyConfirm("请谨慎使用该功能,确认一键签收?",allSign,[]);
	}
	function allSign(){
		$('qianshou').disabled="disabled";
		$('qianshou2').disabled="disabled";
		$('qianshou3').disabled="disabled";
		var url= "<%=contextPath%>/claim/oldPart/ClaimOldPartApporoveStorageManager/oldPartSignAll.json?return_ord_id="+$('return_id').value;
		makeNomalFormCall(url,afterAllSign,'fm','createOrdBtn');
	}
	
	function afterAllSign(json){
		if(json.msg!=null&&json.msg!=""){
			MyAlert("一键签收成功,请点击【确定】后-等待页面自动刷新!");
			__extQuery__(1);
		}else{
			MyAlert("一键签收失败,请联系管理员!");
		}
			$('qianshou').disabled=false;
	}
	//选中预检查
	function sureChecked(val) {
	 var i=validateSelectedId(val);
      
        if(i==1){		
        MyConfirm("你确认通过扣件审核?",sign,[val]);
       }
	}
	//签收确认操作
	function signSure(){
		$('qianshou').disabled="disabled";
		$('qianshou2').disabled="disabled";
		$('qianshou3').disabled="disabled";
		var return_id=$('return_id').value;
		fm.i_back_id.value=return_id;
		var str="?";
		var flag=0;
		str+="return_ord_id="+return_id;
		var url= "<%=contextPath%>/claim/oldPart/ClaimOldPartApporoveStorageManager/approveAndStoredSure221.json"+str;
		makeNomalFormCall(url,afterCallSure,'fm','createOrdBtn');
	}
	//签收确认回调处理
	function afterCallSure(json){
		var retCode=json.updateResult;
	    if(retCode!=null&&retCode!=''){
	      if(retCode=="updateSuccess"){
// 	    	MyAlert("审核确认成功,此次操作流水号："+json.auditNo);
			MyAlert("审核确认成功!");
			 __extQuery__(1);
	    	//backTo();
	      }else if(retCode=="updateFailed"){
	      	if(json.notice!=""){
	      	MyAlert(json.notice);
	     	 }else{
	    	MyAlert("审核确认失败!");
	    	}
	    	$('qianshou').disabled=false;
	     }
	   }
	}
	
	
	function validateSelectedId(val){
	   
		var retCode=1;
		var selectArr=document.getElementsByName('checkId');
		var Executive_director_ram=document.getElementsByName('Executive_director_ram');
		
		var fag =false;
		for(var i=0;i<selectArr.length;i++)
		{
		    if(selectArr[i].checked)
		    {
		      
		      if(val == 2)
		      {
			      if(Executive_director_ram[i].value.length == 0)
			      {
			         parent.window.MyAlert("所有驳回的单据都必须填写意见！");
			         retCode=0;
			         fag =true;
	      			 break;
			      }
		      }
		     
		      fag =true;
		      break;
		    }
		}
		if(!fag)
		{
		   parent.window.MyAlert("请选择需要审核的扣件明细！");
		   retCode=0;
		}
		return retCode;
	}
	function backTo(){
	var yieldly  = document.getElementById("yieldly").value;
	if(yieldly==<%=Constant.PART_IS_CHANGHE_01%>){
		fm.action="<%=contextPath%>/claim/oldPart/ClaimOldPartApporoveStorageManager/oldPartSignAuditPer.do?isReturn=1";
	}else {
		fm.action="<%=contextPath%>/claim/oldPart/ClaimOldPartApporoveStorageManager/oldPartSignAuditPer2.do?isReturn=1";
	}
	    fm.method="post";
	    fm.submit();
	}
	
	function changeValue(obj){
	var value = obj.value;
		if(value.length==18){
			var ids = document.getElementsByName("orderIds");
		for(var i=0;i<ids.length;i++){
			var detailId = ids[i].value;
			var barNo = document.getElementById('barNo'+detailId).value;
			if(value==barNo && document.getElementById('signNum'+detailId).value!=1){
				document.getElementById('signNum'+detailId).value=1;
				document.getElementById('signNum'+detailId).setAttribute('disabled','true');
				$("deduct"+detailId).disabled=true;
				$("deduct"+detailId).value=0;
				break;
			}
		  }
		  sendToAction(value)
		  obj.value="";
		}
	}
	function sendToAction(barCode){
			var value='';
			var pageNum = $('pageNum').value;
			var url="<%=contextPath%>/claim/oldPart/ClaimOldPartApporoveStorageManager/approveAndStored33.json?id=&signNum=1&barCode="+barCode+"&deduct=0&type=5&pageNum="+pageNum;
			makeNomalFormCall(url,afterCall111,'fm','');
	
	}
   //格式化时间为YYYY-MM-DD
   function formatDate(value,meta,record) {
	 if (value==""||value==null) {
		return "";
	 }else {
		return value.substr(0,10);
	 }
   }
   function doInit(){
	  loadcalendar();
   }
</script>
</BODY>
</html>