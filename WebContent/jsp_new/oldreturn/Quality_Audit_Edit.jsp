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
	Integer num = (Integer)request.getAttribute("num");
	String dealerCode = (String )request.getAttribute("dealerCodeSS");
%>
<script type="text/javascript">
	var purchaserArray = new Array(); //创建一个抵扣原因列表
    <c:forEach var= "list" items="${deductList}" varStatus="sta"> //得到有数据的数组集合
    	purchaserArray.push(['${list.CODE_ID}&&${list.CODE_DESC}']);//得到数组的内容（实体bean)加入到新的数组里面
    </c:forEach>
    purchaserArray.push(['95061093&&其它']);
    var isFlag=true;
    var flag=false;
</script>
</head>
<body onload="doInit();">
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif"/>&nbsp;当前位置： 售后服务管理&gt;索赔旧件管理&gt;索赔旧件审核入库</div>
  <form id="fm" name="fm">
  	<input type="hidden" name="i_back_id" id="i_back_id" value="${returnListBean.id }" />
	<input type="hidden" name="types" id="types" value="${types}" />
	<input type="hidden" name="yieldly" id="yieldly" value="${yieldly}" />
	<input type="hidden"  name ="return_id" id="return_id" value="${returnListBean.id }"/>
	<input type="hidden"  name ="pageNum" id="pageNum" value=""/>
	<input id="claimId" name="claimId" type="hidden" value="${claim_id }"/>
	<input id="CLAIM_ID" name="CLAIM_ID" type="hidden" value="${claim_id }"/>
	<table class="table_edit">
			<tr>
				<th colspan="10">
					<img src="<%=contextPath%>/img/subNav.gif" alt="" class="nav" />
					&nbsp;入库旧件查询条件
				</th>
			</tr>
		</table>
    <table class="table_query" >
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
			 <td align="right" nowrap="nowrap">是否已入库：</td>
			 <td align="left"><script type="text/javascript">
		            genSelBoxExp("IS_IN_HOUSE",<%=Constant.IF_TYPE %>,"",true,"short_sel","","false",'');
		           </script></td>
	</tr>
	<tr>
		<td align="right" >VIN：</td>
		<td align="left">
			<input name="vin" type="text" id="vin" maxlength="25" class="middle_txt"/></td>
		</td>
	
		<td align="right" >供应商代码：</td>
		<td align="left">
			<input name="producer_code" type="text" id="producer_code" maxlength="25" class="middle_txt"/></td>
		</td>
	
		<td align="right" >供应商名称：</td>
		<td align="left" >
			<input name="producer_name" type="text" id="producer_name" maxlength="25" class="middle_txt"/></td>
		</td>
	</tr>
       <tr>
         <td align="center" colspan="6" nowrap="nowrap">
          <input type="button" class="normal_btn" name="query" onclick="__extQuery__(1);" value="查询"/>
          &nbsp;&nbsp;&nbsp;&nbsp;
          <input type="reset"  name="bntReset" id="bntReset" value="重置" class="normal_btn" />
    		&nbsp;&nbsp;&nbsp;&nbsp;
           <input type="button"  name="bntAdd"  value="导出" onclick="expotData();" class="normal_btn" />
           &nbsp;&nbsp;&nbsp;&nbsp;
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
<form name="form1" id="form1" style="display:none">
<input id="claimId" name="claimId" type="hidden" value="${claim_id }"/>
    <table>
     <tr>
       <th align="left">审核备注:</th>
     </tr>
     <tr>
       <td>
          <textarea rows="3" cols="100" id="remark" name="remark"></textarea>
       </td>
     </tr>
  </table>
  <table id="bt" class="table_list">
	 <tr>
		<td height="10" align="center" >
			<input type="button" style="display: none" onclick="sureChecked();" disabled="disabled" id="qianshou" class="normal_btn" value="确定审核" />&nbsp;&nbsp;
			<input type="button" onclick=" history.go(-1);" id="backBtn" class="normal_btn" value="返回" />&nbsp;&nbsp;
			<input type="button"   onclick="preChecked(1);"  id="qianshou2" disabled="disabled" class="normal_btn" value="审核通过" />&nbsp;&nbsp;
			<input type="button"   onclick="preChecked(2);"  id="qianshou1" disabled="disabled" class="normal_btn" value="审核退回" />&nbsp;&nbsp;
			<input type="button"   onclick="historyChecked();"  id="qianshou3"  class="normal_btn" value="审核历史" />&nbsp;&nbsp;
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
   var url = "<%=contextPath%>/claim/oldPart/ClaimOldPartApporoveStorageManager/oldPartSignAuditSerch.json";
				
   var title = null;

   var columns = [
  				{header: "序号",align:'center',renderer:getIndex},
				{header: "索赔申请单", dataIndex: 'CLAIM_NO', align:'center',renderer:myLink},
  				{header: "签收数", dataIndex: 'SIGN_AMOUNT', align:'center',renderer:myLink2},
  				{header: "装箱单号",dataIndex: 'BOX_NO',align:'center'},
  				{header: "扣除原因",dataIndex: 'DEDUCT_REMARK',align:'center',renderer:myLink4},
  				{header: "配件代码",dataIndex: 'PART_CODE',align:'center'},
  				{header: "配件名称", dataIndex: 'PART_NAME', align:'center',renderer:myLink5},
				{header: "供应商代码", dataIndex: 'PRODUCER_CODE', align:'center',renderer:myLink8},
  				{header: "供应商名称", dataIndex: 'PRODUCER_NAME', align:'center',renderer:myLink6},
  				{header: "回运数",dataIndex: 'RETURN_AMOUNT',align:'center',renderer:myLink3},
  				{header: "其它原因",dataIndex: 'OTHER_REMARK',align:'center',renderer:myLink10},
  				{header: "责任性质",dataIndex: 'IS_MAIN_CODE',align:'center',renderer:getItemValue},
  				{header: "存放库位",dataIndex: 'LOCAL_WAR_HOUSE',align:'center',renderer:myLink7},
  				{header: "编号",dataIndex: 'BARCODE_NO',align:'center'},
  				{header: "主管审核状态", dataIndex: 'EXECUTIVE_DIRECTOR_STA', align:'center'},
				{header: "主管审核意见", dataIndex: 'EXECUTIVE_DIRECTOR_RAM', align:'center'},
  				{header: "VIN", dataIndex: 'VIN', align:'center'},
  				{header: "维修日期",dataIndex: 'RO_STARTDATE',align:'center'},
  				{header: "索赔单类型",dataIndex: 'CLAIM_TYPE',align:'center',renderer:getItemValue}
  		      ];
  		      __extQuery__(1);
  		      
  		      
  		      function myLink10(value,metaDate,record) {
  		    	  var input='';
  		    	 if(record.data.DEDUCT_REMARK != '95061093'){
   	  		    	input ='<input type="text" readonly id="other_remark'+record.data.ID +'" name="other_remark'+record.data.ID +'" />';
  		    		if (record.data.OTHER_REMARK != "" && record.data.OTHER_REMARK != null) {
  		    			updateOtherRemark(record.data.ID);
  		    		}
  		    	 }else{
  		    		 var rr = record.data.OTHER_REMARK;
  		    		 if (rr == null || rr =="null") {
  		    			 rr = "";
  		    		 }
  	  		    	input ='<input type="text" onblur="updateOtherRemark('+record.data.ID+');" value="'+rr+'" id="other_remark'+record.data.ID +'" name="other_remark'+record.data.ID +'" />';
  	  	  		    }
  				return String.format(input);
  		      }
  		      
  		      function updateOtherRemark(id) {
  		    	var otherRemark = "";
  		    	  if ($('other_remark'+id)) {
  		    		 
  		    		otherRemark = $('other_remark'+id).value;
  		    	  } 
  		    	  
  		    	var url= "<%=contextPath%>/claim/oldPart/ClaimOldPartApporoveStorageManager/updateOtherRemark.json?id="+id+"&otherRemark="+otherRemark;
  		  		makeNomalFormCall(url,null,'fm','');
  		    	  
  		      }
  		    function myCheckBox(value,metaDate,record){
  		    	$('pageNum').value=json.pageNum;
  		    	 var isInHouse = record.data.IS_IN_HOUSE;
  		    	var input2='';
  		    	 if(isInHouse==<%=Constant.IF_TYPE_YES %>){
  		    		 input2 ='<input type="checkbox" disabled  checked id="checkId" name="checkId" value="'+value+'" />';
  	  		    	 }else{
  	  		    	input2 ='<input type="checkbox" id="checkId" name="checkId" value="'+value+'" />';
  	  	  		    }
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
  var house= record.data.LOCAL_WAR_HOUSE;
  var shel = record.data.LOCAL_WAR_SHEL;
  var layer = record.data.LOCAL_WAR_LAYER;
  var letter = record.data.IS_INVOICE;
  var out = record.data.IS_OUT;
  var val = house+shel+layer;
 	var res="";
 	if(house!="" && shel!=""&&layer!=""&&house!=null && shel!=null&&layer!=null){
 		 val = val.replace(/[ ]/g,""); 
 	if(letter==1||out==1){
 		 res = String.format("<input type=\"text\" maxlength=\"5\" disabled id=\"local"+record.data.ID+"\" onkeyup=\"signforwad2("+record.data.ID+");\" name=\"local"+record.data.ID+"\" class=\"short_txt\"  value=\"" + val+ "\"/>"+"<input type=\"hidden\" id=\"local"+record.data.ID+"\" name=\"local"+record.data.ID+"\"   value=\"" + val + "\"/>");
 	}else {
 		 res = String.format("<input type=\"text\" maxlength=\"5\" id=\"local"+record.data.ID+"\" onkeyup=\"signforwad2("+record.data.ID+");\" name=\"local"+record.data.ID+"\" class=\"short_txt\"  value=\"" + val+ "\"/>"+"<input type=\"hidden\" id=\"local"+record.data.ID+"\" name=\"local"+record.data.ID+"\"   value=\"" + val + "\"/>");
 	}
 	}else{
 	 res = String.format("<input type=\"text\" maxlength=\"5\" id=\"local"+record.data.ID+"\" onkeyup=\"signforwad2("+record.data.ID+");\" name=\"local"+record.data.ID+"\" class=\"short_txt\"  value=\"\"/>"+"<input type=\"hidden\" id=\"local"+record.data.ID+"\" name=\"local"+record.data.ID+"\"   value=\"\"/>");
 	}
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
 	OpenHtmlWindow('<%=contextPath%>/claim/dealerClaimMng/ClaimBillTrack/claimBillDetailForward.do?ID='+id,1200,500);
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
 		//$('qianshou').disabled="";
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
 	res = String.format("<input type=\"text\" disabled id=\"signNum"+record.data.ID+"\" onkeyup=\"signforwad("+record.data.ID+","+0+");\" name=\"signNum"+record.data.ID+"\" class=\"short_txt\"  value=\"" + value+ "\"/>"+"<input type=\"hidden\" id=\"signNum1"+record.data.ID+"\" name=\"signNum1"+record.data.ID+"\"   value=\"" + value + "\"/>");
 	return String.format(res);
 }
  function myLink5(value,metaDate,record){
	  return String.format(""+value+"<input type=\"hidden\" id=\"partCode"+record.data.ID+"\" name=\"partCode"+record.data.ID+"\" value=\"" + record.data.PART_CODE + "\"/><input type=\"hidden\"id=\"partName"+record.data.ID+"\" name=\"partName"+record.data.ID+"\" value=\"" + record.data.PART_NAME + "\"/>");
 }
  function myLink6(value,metaDate,record){
  var letter = record.data.IS_INVOICE;
   var out = record.data.IS_OUT;
   var inHouse = record.data.IS_IN_HOUSE;
   var RETURN_TYPE = record.data.RETURN_TYPE;
   if(letter==0&&inHouse==10041001){
  	   $('qianshou2').disabled=false;
  	   $('qianshou1').disabled=false;
  	   $('qianshou3').disabled=false;
	   }
	return String.format("<input type=\"text\" class=\"long_txt\" readonly id=\"supplierName"+record.data.ID+"\" name=\"supplierName"+record.data.ID+"\" value=\"" + value + "\"/>");
 }
  function myLink8(value,metaDate,record){
	  var letter = record.data.IS_INVOICE;
	   var out = record.data.IS_OUT;
		   return String.format("<input type=\"text\" class=\"short_txt\" readonly id=\"supplierCode"+record.data.ID+"\" name=\"supplierCode"+record.data.ID+"\" value=\"" + record.data.PRODUCER_CODE + "\"/>");
  }
  function addSupply(code,id){
		   OpenHtmlWindow('<%=contextPath%>/claim/oldPart/ClaimOldPartOutStorageManager/selectSupplierForward.do?partCode='+code+'&id='+id,800,500);
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
 	MyAlert(flag);
 	if(!flag){
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
		if (value == '95061093') {
			$("other_remark"+id).readonly=false;
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
	function preChecked(type) {
        MyConfirm("确认审核?",sign,[type]);
	}
	//签收操作
	function sign(type){
		$('qianshou').disabled="disabled";
		$('qianshou2').disabled="disabled";
		$('qianshou3').disabled="disabled";
		var url= "<%=contextPath%>/OldReturnAction/oldPartSignAuditIn.json?type="+type;
		makeNomalFormCall(url,afterCall,'form1','');
	}
	function  signAll(){
		 MyConfirm("请谨慎使用该功能,确认一键签收?",allSign,[]);
	}
	function allSign(){
		$('qianshou').disabled="disabled";
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
	function sureChecked() {
	 var i=validateSelectedId();
        if(i==1){		
        MyConfirm("确认审核?",signSure,[]);
     }
	}
	//签收确认操作
	function signSure(){
		$('qianshou').disabled="disabled";
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
			MyAlert("审核确认成功!");
			 __extQuery__(1);
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
	//质量部入库审核
	function afterCall(json){
		if(json.succ==1){
			  MyAlert("操作成功！");
			 window.location= "<%=contextPath%>/OldReturnAction/QualityAudit.do";
			}else{
			  MyAlert("操作失败！");
			}
	}
	function validateSelectedId(){
		var retCode=1;
		var selectArr=document.getElementsByName('orderIds');
		for(var i=0;i<selectArr.length;i++){
			var detailId = selectArr[i].value;
			var requiredNum=document.getElementById("returnAmount"+detailId).value;
			var backNum=document.getElementById("signNum"+detailId).value;
			var deductReason=document.getElementById("deduct"+detailId).value;
			var partName=document.getElementById("partName"+detailId).value;
			var local=document.getElementById("local"+detailId).value;
			var diffNum=requiredNum-backNum;
			local = local.replace(/[ ]/g,""); 
			if((local==null||local=="" || local.length!=5)&&backNum==1){
				parent.window.MyAlert(" 配件名称: '"+partName+" '的存放库位没有填写！");
				retCode=0;
				break;
			}
			if(backNum==null||backNum==''){
				parent.window.MyAlert( " 配件名称: '"+partName+" '的签收数不能为空！");
				retCode=0;
				break;
			}
			if(diffNum<0){
				parent.window.MyAlert(" 配件名称: '"+partName+" '的签收数不能超过回运数！");
				retCode=0;
				break;
			}
			if(diffNum>0&&(deductReason==''||deductReason==0)){
				parent.window.MyAlert( " 配件名称: '"+partName+" '的签收存在数量差异，请选择扣除原因！");
				retCode=0;
				break;
			}
			if(diffNum==0&&deductReason>0){
				parent.window.MyAlert(" 配件名称: '"+partName+" '的签收数量并没有差异，无需选择扣除原因！");
				retCode=0;
				break;
			}
		}
		
		var  IN_WARHOUSE_NAME =  document.getElementById('IN_WARHOUSE_NAME').value; 
		 if(IN_WARHOUSE_NAME.length == 0 )
		 {
		        parent.window.MyAlert("请输入审核人");
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
	  $("input[type=text]").disabled=true;
   }

   function getHistroy() {
	   var num = "<%=num%>";
	   var dealerCode = "<%=dealerCode%>";
		fm.action="<%=contextPath%>/claim/oldPart/ClaimOldPartApporoveStorageManager/oldPartSignAuditQuery.do?curPage="+num+"&dealerCode="+dealerCode;
		   fm.method="post";
		   fm.submit();
 }
   function expotData(){
 	  fm.action="<%=contextPath%>/OutStoreAction/expotDataOldAudit.do";
 	  fm.method="post";
      fm.submit();
 }
   function allRebut(){
	  var checkids = document.getElementsByName('checkId');
	  var temp=0;
	  for(var i=0;i<checkids.length;i++){
		  if(checkids[i].checked){
			  if(!checkids[i].disabled){
				  temp++;
			  }
		  }
	  }
	  if(temp==0){
		  MyAlert("提示：请先选择至少一个再拒绝！");
		  return;
	  }
	  var barcode_nos=""
	  for(var i=0;i<checkids.length;i++){
		  if(checkids[i].checked){
			  if(!checkids[i].disabled){
				  barcode_nos+=checkids[i].value+",";
			  }
		  }
	  }
	  var url= "<%=contextPath%>/OutStoreAction/auditAllRebut.json?barcode_nos="+barcode_nos;
	  makeNomalFormCall(url,auditAllRebutBack,"fm");
   }
   function auditAllRebutBack(json){
	   if(json.succ==1){
		   MyAlert("提示：批量拒绝成功！");
		   __extQuery__(1);
	   }else{
		   MyAlert("提示：批量拒绝失败！");
	   }
   }
   function historyChecked(){
	   var id = document.getElementById("claimId").value;
	   OpenHtmlWindow('<%=contextPath%>/OldReturnAction/historyoldreturnquery.do?id='+id,800,500);
	 }
</script>
</body>
</html>