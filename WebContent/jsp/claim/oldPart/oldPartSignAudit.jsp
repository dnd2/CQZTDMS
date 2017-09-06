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
<BODY >
<div class="wbox">
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif"/>&nbsp;当前位置： 售后服务管理&gt;索赔旧件管理&gt;索赔旧件审核入库</div>
  <form id="fm" name="fm">
    <input id="CLAIM_ID" name="CLAIM_ID" type="hidden" value="${claim_id }"/>
  	<input type="hidden" name="i_back_id" id="i_back_id" value="${returnListBean.id }" />
	<input type="hidden" name="types" id="types" value="${types}" />
	<input type="hidden" name="yieldly" id="yieldly" value="${yieldly}" />
	<input type="hidden"  name ="return_id" id="return_id" value="${returnListBean.id }"/>
	<input type="hidden"  name ="pageNum" id="pageNum" value="${pageNum }"/> 
	<input type="hidden"  name ="myarrval" id="myarrval" value=""/>
	<input type="hidden"  name ="myarr" id="myarr" value=""/>
	
	<input type="hidden"  name ="id" id="id" value=""/>
	<input type="hidden"  name ="claimId" id="claimId" value=""/>
	<input type="hidden"  name ="partId" id="partId" value=""/>
	<input type="hidden"  name ="isMainCode" id="isMainCode" value=""/>
	<input type="hidden"  name ="otherRemark" id="otherRemark" value=""/>
	<input type="hidden"  name ="venderCode" id="venderCode" value=""/>
	<input type="hidden"  name ="venderName" id="venderName" value=""/>
	<input type="hidden"  name ="curPage" id="curPage" value=""/>
	
	<input type="hidden"  name ="inWarhouseType" id="inWarhouseType" value=""/>
	
	<div class="form-panel">
				<h2><img src="<%=contextPath%>/jmstyle/img/search-ico.png" style="margin-bottom: -13px;"/>查询条件</h2>
				<div class="form-body">
    <TABLE class="table_query" >
      <tr>
		<td class="right"  >装箱单号:</td>
		<td  >
			<select name="boxNo" class="u-select" id="boxNo">
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
		</td>
		<td   class="right">签收数：</td>
		<td  >
			<select   id="sing_num" class="u-select" name="sing_num" >
			<option value="" >--请选择--</option>
			<option value="0" >0</option>
			<option value="1" >1</option>
			</select>
		</td>
		<td class="right" >VIN：</td>
		<td >
			<input name="vin" type="text" id="vin" maxlength="25" class="middle_txt"/></td>
		</td>
	</tr>
	<tr>
		<td  class="right" >配件代码：</td>
		<td   >
			<input id="part_code" name="part_code" class="middle_txt"type="text" value="${part_code }"/>
		</td>
		<td  class="right" >配件名称：</td>
		<td  >
			<input id="part_name" name="part_name" class="middle_txt" type="text" value="${part_name }"/>
		</td>
		<td  class="right" >索赔单号：</td>
		<td  >
			<input id="claim_no" name="claim_no" class="middle_txt" type="text" value="${claim_no }"/>
		</td>
	</tr>
	<tr>
		<td class="right" >经销商名称：</td>
		<td >
			<input name="dealerName" type="text" id="dealerName" maxlength="25" class="middle_txt"/></td>
		</td>
		<td  class="right" >经销商编码:</td>
		<td  >
			<input class="middle_txt" id="dealerCode"  name="dealerCode" type="text"  maxlength="25"/>
		</td>
			 <td class="right" nowrap="nowrap">是否审核入库：</td>
			 <td ><script type="text/javascript">
		            genSelBoxExp("inWarhouseStatus",<%=Constant.IF_TYPE %>,"",true,"","","false",'');
		           </script></td>
	</tr>
	<tr>
		
	
		<td class="right" >责任供应商代码：</td>
		<td >
			<input name="producer_code" type="text" id="producer_code" maxlength="25" class="middle_txt"/></td>
		</td>
	
		<td class="right" >责任供应商名称：</td>
		<td >
			<input name="producer_name" type="text" id="producer_name" maxlength="25" class="middle_txt"/></td>
		</td>
	</tr>
       <tr>
         <td class="center" colspan="6" nowrap="nowrap">
          <input type="button" class="u-button u-query" name="query" onclick="__extQuery__(1);" value="查询"/>
          &nbsp;&nbsp;&nbsp;&nbsp;
          <input type="reset"  name="bntReset" id="bntReset" value="重置" class="u-button u-cancel" />
    		&nbsp;&nbsp;&nbsp;&nbsp;
           <input type="button"  name="bntAdd"  value="导出" onclick="expotData();" class="normal_btn" />
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
  </div>
  </div>
  <!--分页 begin -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
  <!--分页 end --> 
<div id="loader" style='position:absolute;z-index:200;background:#FFCC00;padding:1px;top:4px;display:none;display: none;'></div>
</form> 
</div>
<form name="form1" id="form1" >
  <table id="bt" class="table_list">
	 <tr>
		<td align="center" >
			<input type="button"  onclick="inWarhouseStatus(<%=Constant.BACK_LIST_STATUS_04%>);" id="inWarhouseBtn" class="normal_btn" value="部分审核入库" />&nbsp;&nbsp;
			<input type="button"  onclick="inWarhouseStatus(<%=Constant.BACK_LIST_STATUS_05%>);"  id="inWarhouseAllBtn" class="normal_btn" value="全部审核入库" />&nbsp;&nbsp;
			<input type="button" onclick="getHistroy();" id="backBtn" class="normal_btn" value="返回" />&nbsp;&nbsp;
		</td>
	</tr>
  </table>
</form>

<br>
<script type="text/javascript">
   
   var myPage;
   //查询路径
   var url = "<%=contextPath%>/claim/oldPart/ClaimOldPartApporoveStorageManager/oldPartSignAuditSerch.json";
				
   var title = null;

   var columns = [
  				{header: "序号",align:'center',renderer:getIndex},
  				//{id:'action',header: "选择", width:'3%',align:'center',sortable: false,dataIndex: 'ID',renderer:myCheckBox},
				{header: "索赔申请单", dataIndex: 'CLAIM_NO', align:'center',renderer:myLink},
  				{header: "签收数", dataIndex: 'SIGN_AMOUNT', align:'center',renderer:myLink2},
  				{header: "装箱单号",dataIndex: 'BOX_NO',align:'center'},
  				{header: "扣除原因",dataIndex: 'DEDUCT_REMARK',align:'center',renderer:myLink4},
  				{header: "其它原因",dataIndex: 'OTHER_REMARK',align:'center',renderer:myLink10},
  				{header: "配件代码",dataIndex: 'PART_CODE',align:'center'},
  				{header: "配件名称", dataIndex: 'PART_CNAME', align:'center',renderer:myLink5},
  				{header: "索赔供应商代码", dataIndex: 'CLAIM_SUPPLIER_CODE', align:'center'},
  				{header: "索赔供应商名称", dataIndex: 'CLAIM_SUPPLIER_NAME', align:'center'},
				{header: "责任供应商代码", dataIndex: 'PRODUCER_CODE', align:'center',renderer:myLink8},
  				{header: "责任供应商名称", dataIndex: 'PRODUCER_NAME', align:'center',renderer:myLink6},
  				//{header: "回运数",dataIndex: 'RETURN_AMOUNT',align:'center',renderer:myLink3},
  				{header: "责任性质",dataIndex: 'IS_MAIN_CODE',align:'center',renderer:setIsMainName}, //getItemValue
  				{header: "VIN", dataIndex: 'VIN', align:'center'},
  				{header: "维修日期",dataIndex: 'RO_STARTDATE',align:'center'},
  				{header: "索赔单类型",dataIndex: 'REPAIR_TYPE',align:'center',renderer:getItemValue},
  				{header: "是否审核入库",dataIndex: 'IN_WARHOUSE_STATUS',align:'center',renderer:getItemValue},
  		      ];
  		      //设置主因件名称
              function setIsMainName(value,metaDate,record){
            	  if(value=="<%=Constant.PART_BASE_FLAG_YES%>"){
            		  return String.format("主因件");
            	  }else{
            		  return String.format("次因件");
            	  }
            	  return String.format(str);
              }
  		      
  		      function myLink10(value,metaDate,record) {
  		         var id = record.data.ID;
  		         var deductRemark = record.data.DEDUCT_REMARK;
  		         var otherRemark = record.data.OTHER_REMARK==null?"":record.data.OTHER_REMARK;
  		    	 var input='';
  		    	 if(deductRemark==<%=Constant.OLDPART_DEDUCT_TYPE_23%>){//其他
   	  		    	input ='<input type="text" id="other_remark_'+id+'" name="other_remark_'+id+'" class="middle_txt" onblur="otherRemarkChange('+id+',this.value)" value="'+otherRemark+'"/>';
  		    	 }else{
  		    		input ='<input type="text" readonly id="other_remark_'+id+'" name="other_remark_'+id+'" class="middle_txt" value="'+otherRemark+'"/>';
  	  	  		 }
  				return String.format(input);
  		      }
  		      
  		      function updateOtherRemark(id) {
  		    	var otherRemark = "";
  		    	  if ($('#other_remark'+id)[0]) {
  		    		 
  		    		otherRemark = $('#other_remark'+id)[0].value;
  		    	  } 
  		    	  
  		    	var url= "<%=contextPath%>/claim/oldPart/ClaimOldPartApporoveStorageManager/updateOtherRemark.json?id="+id+"&otherRemark="+otherRemark;
  		  		makeNomalFormCall(url,null,'fm','');
  		    	  
  		      }
  		    function myCheckBox(value,metaDate,record){
  		    	 var isInHouse = record.data.IS_IN_HOUSE;
  		    	var input2='';
  		    	 if(isInHouse==<%=Constant.IF_TYPE_YES %>){
  		    		 input2 ='<input type="checkbox" disabled  checked id="checkId" name="checkId" value="'+value+'" />';
  	  		    	 }else{
  	  		    	input2 ='<input type="checkbox" disabled  id="checkId" name="checkId" value="'+value+'" />';
  	  	  		    }
  				return String.format(input2);
  			}
 function myLink(value,metaDate,record){
	 var imports = record.data.IS_IMPORT;
	 if(imports==<%=Constant.IF_TYPE_YES%>){
		 return String.format( "<input type='hidden' id=\"barNo"+record.data.ID+"\" value='" + record.data.BARCODE_NO + "' /><input type='hidden' name='orderIds' value='" + record.data.ID + "' />["+record.data.CLAIM_NO+"]");
	 }else{
	     return String.format( "<input type='hidden' id=\"barNo"+record.data.ID+"\" value='" + record.data.BARCODE_NO + "' /><input type='hidden' name='orderIds' value='" + record.data.ID + "' /><a href='#' onClick='claimDetail(\""+record.data.CLAIM_ID+"\",\""+record.data.CREATE_DATE+"\",\""+record.data.RO_NO+"\",\""+record.data.CLAIM_TYPE+"\");'>["+record.data.CLAIM_NO+"]</a>");
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
 function claimDetail(id,create_date,ro_no,claimtype){
	//新分单时间
		var str ='2015-05-25 19:30:00';
		var st = str.replace(/-/g,"/");
		var date = (new Date(st)).getTime();  
		var st1 =create_date.replace(/-/g,"/");
		var date1 = (new Date(st1)).getTime();
		if(date<date1){//分单后
			OpenHtmlWindow("<%=contextPath%>/ClaimBalanceAction/claimBalanceAuditingPage.do?goBackType=2&view=view&roNo="+ro_no+"&id="+id +"&claim_type="+claimtype,900,500);
		}else{
			OpenHtmlWindow('<%=contextPath%>/claim/dealerClaimMng/ClaimBillTrack/claimBillDetailForward.do?ID='+id,900,500);
		}
 }
  function myLink3(value,metaDate,record){
 		return String.format( "" + value + "<input type=\"hidden\" id=\"returnAmount"+record.data.ID+"\" name=\"returnAmount"+record.data.ID+"\" value=\"" + value + "\"/>");
 }
 function myLink4(value,metaDate,record){
        var id = record.data.ID;
        var signAmount = record.data.SIGN_AMOUNT;//签收数
        var isBill = record.data.IS_BILL;//是否开票
        var isOut = record.data.IS_OUT;//是否出库
        var deductRemark = record.data.DEDUCT_REMARK;//扣件原因
 		var str = "";
 		if(isBill==<%=Constant.PART_BASE_FLAG_YES%>||isOut==<%=Constant.PART_BASE_FLAG_YES%>||signAmount=="1"||deductRemark==<%=Constant.OLDPART_DEDUCT_TYPE_22%>){
 		 //已开票、已出库、签收数为1、扣件原因为连带扣件就不能再选择了
 		 str = "<select class=\"u-select\" id = 'deduct_remark_"+record.data.ID+"' ><option value='"+value+"'>"+record.data.DEDUCT_DESC+"</option></select>";
 		}else{
 		 str = genSelBoxStrExp("deduct_remark_"+record.data.ID,<%=Constant.OLDPART_DEDUCT_TYPE%>,deductRemark,"true","u-select","onChange=\"deductRemarkChange('"+id+"',this.value)\"","false",'<%=Constant.OLDPART_DEDUCT_TYPE_22%>');
 		}
		//str = str + "</select>";
		
		return String.format(str);
 }
  //修改扣件原因
  function deductRemarkChange(id,deductRemark){
    //alert(deductRemark);
    var curPage = myPage.page;//
    var url = "<%=contextPath%>/claim/oldPart/ClaimOldPartApporoveStorageManager/deductRemarkChange.json?id="+id+"&deductRemark="+deductRemark
	        + "&curPage="+curPage;
    makeNomalFormCall(url,deductRemarkChangeResult,'fm','');
  }
  //修改扣件原因结果
  function deductRemarkChangeResult(json){
    //保证只有当修改成功了且扣件原因为其他时，才能输入其他原因
    if(json.code=="succ"){
      if(json.deductRemark==<%=Constant.OLDPART_DEDUCT_TYPE_23%>){
        document.getElementById("other_remark_"+json.id).readOnly = false;
      }else{
        document.getElementById("other_remark_"+json.id).readOnly = true;
      }
	  __extQuery__(json.curPage);
	}
  }
  //填写其他原因
  function otherRemarkChange(id,otherRemark){
    //alert(otherRemark);
    //return;
    document.getElementById("id").value = id;
    document.getElementById("otherRemark").value = otherRemark;
    document.getElementById("curPage").value = myPage.page;
    //var curPage = myPage.page;//
    var url = "<%=contextPath%>/claim/oldPart/ClaimOldPartApporoveStorageManager/otherRemarkChange.json";
    makeNomalFormCall(url,null,'fm','');
  }
  
 function addPurchaserList(parms)
	{	
	
		var obj = document.getElementById(parms);
		if(obj.options.length < 2)
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
				if(uID != obj.options[0].value)
				{
					obj.options.add(new Option(uName,uID));
				}
			}
		}
	}
 function myLink2(value,metaDate,record){
 	var res="";
 	var isBill = record.data.IS_BILL;//是否开票
    var isOut = record.data.IS_OUT;//是否出库
    var deductRemark = record.data.DEDUCT_REMARK;//扣件原因
    var signAmount = record.data.SIGN_AMOUNT;//签收数
    
    var id = record.data.ID;//配件明细ID
    var returnId = record.data.RETURN_ID;//配件清单ID
    var isMainCode = record.data.IS_MAIN_CODE;//是否主因件
    var claimId = record.data.CLAIM_ID;//索赔单ID
    var claimPartId = record.data.CLAIM_PART_ID;//索赔配件表ID
    var partId = record.data.PART_ID;//配件ID,用于主因件匹配
    var options = "";//可选择下拉框值
    var option = "";//不可再选择下拉框值
    if(signAmount==1){
      options = "  <option value=\"0\">0</option>"
              + "  <option value=\"1\" selected>1</option>";
      option  = "  <option value=\"1\">1</option>";
    }else{
      options = "  <option value=\"0\" selected>0</option>"
              + "  <option value=\"1\">1</option>";
      option  = "  <option value=\"0\">0</option>";
    }
 	if(isBill==<%=Constant.PART_BASE_FLAG_YES%>||isOut==<%=Constant.PART_BASE_FLAG_YES%>||deductRemark==<%=Constant.OLDPART_DEDUCT_TYPE_22%>){
 		//已开票、已出库、扣件原因为连带扣件就不能再选择了
 		 //res = String.format("<input type=\"text\" readOlny id=\"signNum"+record.data.ID+"\" onkeyup=\"signforwad("+record.data.ID+","+0+");\" name=\"signNum"+record.data.ID+"\" class=\"short_txt\"  value=\"" + value+ "\"/>"+"<input type=\"hidden\" id=\"signNum1"+record.data.ID+"\" name=\"signNum1"+record.data.ID+"\"   value=\"" + value + "\"/>");
 		res = "<select id=\"signAmount_"+record.data.ID+"\" name=\"signAmount_"+record.data.ID+"\" class=\"u-select\" style=\"width:40px\" >" 
            + option
            + "</select>";
 	}else {
 		//$('qianshou').disabled="";
 		//res = String.format("<input type=\"text\"  id=\"signNum"+record.data.ID+"\" onkeyup=\"signforwad("+record.data.ID+","+0+");\" name=\"signNum"+record.data.ID+"\" class=\"short_txt\"  value=\"" + value+ "\"/>"+"<input type=\"hidden\" id=\"signNum1"+record.data.ID+"\" name=\"signNum1"+record.data.ID+"\"   value=\"" + value + "\"/>");
 		res = "<select id=\"signAmount_"+record.data.ID+"\" name=\"signAmount_"+record.data.ID+"\" class=\"u-select\" style=\"width:40px\" onchange=\"signAmountChange("+id+","+returnId+","+isMainCode+","+claimId+","+claimPartId+","+partId+")\">" 
            + options
            + "</select>";
 	}
 	return String.format(res);
 }
  function myLink5(value,metaDate,record){
	  return String.format(""+value+"<input type=\"hidden\" id=\"partCode"+record.data.ID+"\" name=\"partCode"+record.data.ID+"\" value=\"" + record.data.PART_CODE + "\"/><input type=\"hidden\"id=\"partName"+record.data.ID+"\" name=\"partName"+record.data.ID+"\" value=\"" + record.data.PART_NAME + "\"/>");
 }
  function myLink6(value,metaDate,record){
    var id = record.data.ID;
    var producerName = record.data.PRODUCER_NAME;//责任供应商名称
	return String.format("<input type=\"text\" class=\"middle_txt\" readonly id=\"producerName_"+id+"\" name=\"producerName"+id+"\" value=\"" + producerName + "\"/>");
 }
  function myLink8(value,metaDate,record){
    var id = record.data.ID;
    var claimId = record.data.CLAIM_ID;//索赔单ID
    var partId = record.data.PART_ID;//配件ID
    var isMainCode = record.data.IS_MAIN_CODE;//是否主因件
    var producerCode = record.data.PRODUCER_CODE;//责任供应商代码
    var isBill = record.data.IS_BILL;//是否开票
    var isOut = record.data.IS_OUT;//是否出库
    if(isBill==<%=Constant.PART_BASE_FLAG_YES%>||isOut==<%=Constant.PART_BASE_FLAG_YES%>||isMainCode==<%=Constant.PART_BASE_FLAG_NO%>){
 	  //已开票、已出库、次因件就不能再选择了
	  return String.format("<input type=\"text\" class=\"short_txt\" readonly id=\"producerCode_"+id+"\" name=\"producerCode_"+id+"\" value=\""+producerCode+"\"/>");
    }else{
      return String.format("<input type=\"text\" class=\"short_txt\" readonly id=\"producerCode_"+id+"\" name=\"producerCode_"+id+"\" value=\""+producerCode+"\"/><input type=\"button\" class=\"mini_btn\" onclick=\"chooseProducerWin("+id+","+claimId+","+partId+","+isMainCode+");\" value=\"...\"/>");
    }
  }
  //选择索赔供应商
  function chooseProducerWin(id,claimId,partId,isMainCode){
    var url = "<%=contextPath%>/claim/oldPart/ClaimOldPartApporoveStorageManager/chooseProducerWin.do?partId="+partId+"&id="+id
            + "&claimId="+claimId+"&partId="+partId+"&isMainCode="+isMainCode;
    OpenHtmlWindow(url,900,500);
  }
  //设置索赔供应商信息
  function setProducerInfo(id,venderCode,venderName,claimId,partId,isMainCode){
    //alert(id);
    var producerCode = document.getElementById('producerCode_'+id);//索赔供应商代码
    var producerName = document.getElementById('producerName_'+id);//索赔供应商名称
    producerCode.value = venderCode;
    producerName.value = venderName;
    producerInfoSave(venderCode,venderName,claimId,partId,isMainCode);
  }
  //保存索赔供应商信息
  function producerInfoSave(venderCode,venderName,claimId,partId,isMainCode){
    //document.getElementById("id").value = id;
    document.getElementById("venderCode").value = venderCode;
    document.getElementById("venderName").value = venderName;
    document.getElementById("claimId").value = claimId;
    document.getElementById("partId").value = partId;
    document.getElementById("isMainCode").value = isMainCode;
    document.getElementById("curPage").value = myPage.page;
      
    var url="<%=contextPath%>/claim/oldPart/ClaimOldPartApporoveStorageManager/producerInfoSave.json";
    makeNomalFormCall(url,producerInfoSaveResult,'fm','');
  }
  //修改签收数结果
  function producerInfoSaveResult(json){
    if(json.code=="succ"){
       __extQuery__(json.curPage);
    }
  }
  
  function myLinkRemark(value,metaDate,record){
	  var letter = record.data.IS_INVOICE;
	   var out = record.data.IS_OUT;
	   if(record.data.IS_SCAN==1||letter==1||out==1){
		   return String.format("<input type=\"text\" class=\"long_txt\" readonly id=\"supplierCode"+record.data.ID+"\" name=\"supplierCode"+record.data.ID+"\" value=\"" + record.data.PRODUCER_CODE + "\"/>");
		  }else{
			  var str = "<input type=\"hidden\" class=\"short_txt\"  id=\"PartRemark1"+record.data.ID+"\" name=\"PartRemark1\" value=\"" + record.data.PRODUCER_CODE + "\"/>";
			      str+="<input type=\"text\" class=\"long_txt\"  onblur='checkVal(this);' id=\"PartRemark"+record.data.ID+"\" name=\"PartRemark\" value=\""+value+"\"/>";
			  return String.format(str);
	 }
  }
  function checkVal(obj){
     var val = obj.value;
     obj.value=val.replace(",", '，');//替换成中文状态的，
  }
  function addSupply(code,id){
	  OpenHtmlWindow('<%=contextPath%>/claim/oldPart/ClaimOldPartOutStorageManager/selectSupplierForward1.do?partCode='+code+'&code='+code+'&id='+id,800,430);
		}
function setSupplier(code,name,id){
	var oldname = $('#supplierName'+id)[0].value;//旧供应商名
	$('#supplierName'+id)[0].value=name;//新供应商名
	var oldcode = $('#supplierCode'+id)[0].value;//旧供应商代码
	if(code!=oldcode){
	  $('#PartRemark'+id)[0].value="供应商代码由:"+oldcode+"，改为:"+code;
	}
	$('#supplierCode'+id)[0].value=code;//新供应商代码
	var url= "<%=contextPath%>/claim/oldPart/ClaimOldPartApporoveStorageManager/modSupp.json?id="+id+"&code="+code+"&name="+encodeURI(name)+"&oldname="+encodeURI(oldname)+"&oldcode="+oldcode;
	makeNomalFormCall(url,afterModSupp,'fm','createOrdBtn');
	}
function afterModSupp(json){
	if(json.msg=="succ"){
		
	}else{
		MyAlert("修改失败!");
		 __extQuery__(1);
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
			$("#other_remark"+id)[0].readonly=false;
		}
		var pageNum = $('#pageNum')[0].value;
			var url="<%=contextPath%>/claim/oldPart/ClaimOldPartApporoveStorageManager/approveAndStored33.json?id="+id+"&signNum="+signNum+"&deduct="+value+"&pageNum"+pageNum;
			makeNomalFormCall(url,afterCall111,'fm','');
	}
    //选择签收数
    function signAmountChange(id,returnId,isMainCode,claimId,claimPartId,partId){
      var signAmount = document.getElementById("signAmount_"+id).value;
      var curPage = myPage.page;//
      
	  var url="<%=contextPath%>/claim/oldPart/ClaimOldPartApporoveStorageManager/signAmountChange.json?id="+id+"&signAmount="+signAmount
	         +"&returnId="+returnId+"&isMainCode="+isMainCode+"&claimId="+claimId+"&claimPartId="+claimPartId
	         +"&partId="+partId+"&curPage="+curPage;
	  makeNomalFormCall(url,signAmountChangeResult,'fm','');
    }
    //修改签收数结果
    function signAmountChangeResult(json){
	    if(json.code=="succ"){
	      __extQuery__(json.curPage);
	    }
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
		
		$('#qianshou3')[0].disabled=false;
	}
		if(signNum<returnAmount){
			$("#deduct"+id)[0].disabled=false;
		}
		if(signNum>=returnAmount){
			$("#deduct"+id)[0].value='';
			$("#deduct"+id)[0].disabled=true;
		}
			var pageNum = $('#pageNum')[0].value;
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
	//审核入库
	function inWarhouseStatus(inWarhouseType){
	  MyConfirm("确认审核入库?",inWarhouseStatusSave,[inWarhouseType]);
	}
	//审核入库保存
	function inWarhouseStatusSave(inWarhouseType){
	  //失效按钮
	  //$('#inWarhouseBtn')[0].disabled="disabled";
	  //$('#inWarhouseAllBtn')[0].disabled="disabled";
	  //$('#backBtn')[0].disabled="disabled";
	  document.getElementById("inWarhouseType").value = inWarhouseType;//审核入库类型
	  document.getElementById("curPage").value = myPage.page;//当前页
	  var url= "<%=contextPath%>/claim/oldPart/ClaimOldPartApporoveStorageManager/inWarhouseStatusSave.json";
	  makeNomalFormCall(url,inWarhouseStatusSaveResult,'fm','');
	}
	//审核入库结果
	function inWarhouseStatusSaveResult(json){
	  if(json.code=="succ"){
	    if(json.inWarhouseType==<%=Constant.BACK_LIST_STATUS_04%>){
	      MyAlert("部分审核入库成功!");
	    }
	    if(json.inWarhouseType==<%=Constant.BACK_LIST_STATUS_05%>){
	      MyAlert("全部审核入库成功!");
	    }
	    __extQuery__(1);
	  }else{
	     MyAlert(json.msg);
	  }
	}
	
	function preChecked(str) {
        var i=validateSelectedId();
        if(i==1){		
        MyConfirm("确认审核?",sign,[str]);
     }
	}
	//审核
	function sign(str){
		$('#inWarhouseBtn')[0].disabled="disabled";
		$('#inWarhouseAllBtn')[0].disabled="disabled";
		$('#backBtn')[0].disabled="disabled";
		var partRemarks = document.getElementsByName("partRemark");
		 var myarr=new Array();
		 var myarrval=new Array();
		for(var i=0;i<partRemarks.length;i++){
          var id = partRemarks[i].id;
          var value = document.getElementById(id).value;
          myarrval[i]=value;
          id=id.substring(10,partRemarks[i].id.length);
          myarr[i]=id;
		}
		document.getElementById("myarrval").value=myarrval;
		document.getElementById("myarr").value=myarr;
		var url= "<%=contextPath%>/claim/oldPart/ClaimOldPartApporoveStorageManager/oldPartSignAuditIn.json?type="+str;
		makeNomalFormCall(url,afterCall,'fm','');
	}
	function  signAll(){
		 MyConfirm("请谨慎使用该功能,确认一键签收?",allSign,[]);
	}
	function allSign(){
		$('#qianshou')[0].disabled="disabled";
		$('#qianshou3')[0].disabled="disabled";
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
			$('#qianshou')[0].disabled=false;
	}
	//选中预检查
	function sureChecked() {
	 var i=validateSelectedId();
      MyAlert(i);
        if(i==1){		
        MyConfirm("确认审核?",signSure,[]);
     }
	}
	//签收确认操作
	function signSure(){
		$('#qianshou')[0].disabled="disabled";
		$('#qianshou3')[0].disabled="disabled";
		var return_id=$('#return_id')[0].value;
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
	    	$('#qianshou')[0].disabled=false;
	     }
	   }
	}
	
	//签收回调处理
	function afterCall(json){
		var retCode=json.updateResult;
	    if(retCode!=null&&retCode!=''){
	      if(retCode=="updateSuccess"){
	    	MyAlert("审核成功!");
	    	//backTo();
	    	 __extQuery__(1);
	      }else if(retCode=="updateFailure"){
	    	MyAlert("审核失败!");
	     }
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
			var diffNum=requiredNum-backNum;
			/* var local=document.getElementById("local"+detailId).value;
			local = local.replace(/[ ]/g,""); 
			if((local==null||local=="" || local.length!=5)&&backNum==1){
				parent.window.MyAlert(" 配件名称: '"+partName+" '的存放库位没有填写！");
				retCode=0;
				break;
			} */
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
				$("#deduct"+detailId)[0].disabled=true;
				$("#deduct"+detailId)[0].value=0;
				break;
			}
		  }
		  sendToAction(value)
		  obj.value="";
		}
	}
	function sendToAction(barCode){
			var value='';
			var pageNum = $('#pageNum')[0].value;
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
  function doInit()
  {	
    //alert("${returnListBean.status}");
    if("${returnListBean.status}"=="10811005"){
      document.getElementById("inWarhouseBtn").disabled = true;
      document.getElementById("inWarhouseAllBtn").disabled = true;
    }
    __extQuery__(1);
  }
</script>
</BODY>
</html>