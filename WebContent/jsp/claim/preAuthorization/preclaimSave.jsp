<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.Constant" %>
<%
	String contextPath = request.getContextPath();
	Map  dealer = (Map)request.getAttribute("DEALERHM");//经销商信息
	Map  user = (Map)request.getAttribute("USERHM");//登录人信息
	
%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>预授权状态查询_预授权申请编辑</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<script>

//预授权所需的基本信息
<%--var	str = {TT_REPAIR_ORDER:[{roNo:'维修工单号',roStrartDate:'保修开始日期',inFactoryDate:'进厂日期',--%>
<%--	       inMileage:'进厂里程公里数',serveAdvisor:'接待员',--%>
<%--		   vin:'vin码',lincesNo:'车牌号',engineNo:'发动机号',brand:'品牌（name）',series:'车系（name）',model:'车型（name）',brandCode:'品牌（code）',seriesCode:'车系（code）',modelCode:'车型（code）',--%>
<%--		   apply:'申请人姓名'applyPhone:'申请人的联系电话',yieldly:'产地'--%>
<%--					}]--%>
<%--}--%>
/*
var		str = {TT_REPAIR_ORDER:[{roNo:'R201007140001',lineNo:'1',roStrartDate:'2010-04-09',roEndDate:'2010-04-10',
						inMileage:'100.03',guaranteeDate:'2010-09-03',serveAdvisor:'张三',campaingCode:'20100714',isPrice:12781001,
						vin:'LACUS123456789021',lincesNo:'吉A0730',engineNo:'E0730',brand:'长安',series:'奔奔系列',model:'A101',
						gearboxNo:'G0730',rearaxleNo:'R0730',transferNo:'098',claimType:10661003,troubleCode:'',
						damageCode:'',damageType:'',damageDegree:'',troubleDesc:'ddd',troubleReason:'ccc',repairMethod:'eeee',appRemark:'aaaa',
						yieldly:'重庆'
					}]

					,
				TT_RO_REPAIR_ITEM:[{code:'AM2010081',type:'10871001'},
								   {code:'AB123452C',type:'10871001'},
								   {code:'73701-4A202',type:'10871002'},
								   {code:'76003-4A003',type:'10871002'},
								   {code:'AB005',type:'10871003'},
								   {code:'AB001',type:'10871003'},
								   {code:'QT001',type:'10871003'}
								  ]
								  */
<%--								   ,--%>
<%--				TT_RO_LABOUR:[{labourCode:'',labourName:'aaaa',labourHours:'10.0',labourPrice:10,labourAmount:100},--%>
<%--								{labourCode:'002',labourName:'bbbb',labourHours:'13.0',labourPrice:10,labourAmount:100},--%>
<%--								{labourCode:'003',labourName:'cccc',labourHours:'12.0',labourPrice:10,labourAmount:100}],--%>
<%--				TT_RO_ADD_ITEM:[{itemCode:'001',itemName:'aaaa',remark:'10.0',itemAmount:100},--%>
<%--								{itemCode:'002',itemName:'bbbb',remark:'13.0',itemAmount:100},--%>
<%--								{itemCode:'003',itemName:'cccc',remark:'12.0',itemAmount:100}]--%>

				};
	function doInit(){
   		loadcalendar();  //初始化时间控件
   		//setJson(str); //接口测试		
	}
	//null返回“”
	function getNull(data) {
		if (data==null) {
			return '';
		}else {
			return data;
		}
	}
	//下拉框修改时赋值
		function assignSelect(name,value) {
		var sel = document.getElementById(name);
		var option = sel.options;
		var optionLength = option.length;
		for (var i = 0;i<optionLength;i++) {
		  	if (value ==option[i].value) {
		  		sel.selectedIndex = i;
		  		break;
		  	}
		}
		}
	function keyListnerResp(){
   if((typeof window.event)!= 'undefined'){
	   var type = event.srcElement.type;   
       var code = event.keyCode;
       if(type=='text'||type=='textarea'){
    	   event.returnValue=true;
       }else{//如 不是文本域则屏蔽 Alt+ 方向键 ← Alt+ 方向键 →   //屏蔽后退键    
         if(code==8||((window.event.altKey)&&((code==37)||(code==39)))){
            event.returnValue=false;       
         }
       }
   }
}
function setDate(){
	var myDate = new Date();
	var aDate = myDate.Format("yyyy-MM-dd");
	document.getElementById("t3").value = aDate;
	//document.getElementById("t2").value = aDate;
	
	
}

</script>

</head>
<body onkeydown="keyListnerResp();" onload="setDate();">
<div class="navigation">
<img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 售后服务管理&gt;索赔预授权&gt;索赔预授权工单申请</div> 
<form name='fm' id='fm'>
	<input type="hidden" name="MODEL_ID" id="MODEL_ID"/><!-- 车型id -->
	<input type="hidden" name="IDS" id="IDS"/><!-- 项目添加完的id -->
	<input type="hidden" name="dealerCode" id="dealerCode" value="<%=dealer.get("DEALER_CODE")==null ? "":dealer.get("DEALER_CODE").toString() %>"/><!-- 经销商code -->
    <input type="hidden" name="BRAND_CODE" id="BRAND_CODE"/><!-- 品牌 -->
    <input type="hidden" name="SERIES_CODE" id="SERIES_CODE"/><!-- 车系 -->
    <input type="hidden" name="MODEL_CODE" id="MODEL_CODE"/><!-- 车型 -->
    <!-- <input type="hidden" name="ENGINE_NO_H" id="ENGINE_NO_H"/>发动机号 -->
     <input type="hidden" name="KEEP_BEG_DATE" id="KEEP_BEG_DATE"/><!-- 保险开始日期 -->
    <table class="table_edit">
       <tr>
         <th colspan="6"><img class="nav" src="<%=contextPath%>/img/subNav.gif" /> 基本信息</th>
       </tr>
       <tr>
         <td class="table_edit_3Col_label_6Letter">经销商代码：</td>
         <td><%=dealer.get("DEALER_CODE")==null ? "":dealer.get("DEALER_CODE").toString() %></td>
         <td class="table_edit_3Col_label_5Letter">经销商名称：</td>
         <td ><%=dealer.get("DEALER_SHORTNAME")==null ? "":dealer.get("DEALER_SHORTNAME").toString() %></td>
         <td class="table_edit_3Col_label_8Letter">维修工单号：</td>
         <td ><input type="text"  name="RO_NO"  id="RO_NO"   class="middle_txt" datatype="1,is_digit_letter,30"/></td>
       </tr>
       <tr>
         <td class="table_edit_3Col_label_6Letter">VIN：</td>
         <td>
         	<input type='text'  name='VIN'  id='VIN'   datatype="0,is_vin,17" blurback="true" class="short2_txt"/>
         </td>
         <td class="table_edit_3Col_label_5Letter">牌照号：</td>
         <td>
         	<input type='text'  name='LICENSE_NO'  id='LICENSE_NO' datatype="1,is_carno,30"   class="short_txt"/>
         </td>
         <td class="table_edit_3Col_label_8Letter">发动机号：</td>
         <td align="left" >
         	<input type="text" id="ENGINE_NO_H" name="ENGINE_NO_H" class="short_txt" value=""/>
         </td>
       </tr>
       <tr>
         <td class="table_edit_3Col_label_6Letter">品牌：</td>
         <td align="left" id="BAND_NAME"></td>
         <td class="table_edit_3Col_label_5Letter">车系：</td>
         <td align="left" id="SERIES_NAME"></td>
         <td class="table_edit_3Col_label_8Letter">车型：</td>
         <td align="left" id="MODEL_NAME"></td>
       </tr>       
       <tr>
         <td class="table_edit_3Col_label_6Letter">保修开始日期：</td>
         <td align="left" id="PURCHASED_DATE"></td>
               
         <td class="table_edit_3Col_label_5Letter">产地：</td>
         <td>
<%--         	<input type='text'  name='YIELDLY'  id='YIELDLY' datatype="1,is_null,15"   class="short_txt"/>--%>
			<script type="text/javascript">
             genSelBoxExp("YIELDLY",<%=Constant.SERVICEACTIVITY_CAR_YIELDLY%>,"",false,"min_sel","","false",'');
           	</script>
         </td>
       </tr>
       <tr>
        <td class="table_edit_3Col_label_6Letter">接待员：</td>
         <td ><input type='text'  name='DEST_CLERK'  id='DEST_CLERK' datatype="0,is_digit_letter_cn,6" value=""  class="short_txt"/></td>       
         <td class="table_edit_3Col_label_5Letter">进厂日期：</td>
         <td>
         <input name="IN_FACTORY_DATE" id="t2" value="" type="text" class="short_txt" datatype="0,is_date_now,10"  hasbtn="true" callFunction="showcalendar(event, 't2', false);"/>
         </td>        
        <td class="table_edit_3Col_label_8Letter">进厂里程数(公里)：</td>
         <td><input type='text'  name='IN_MILEAGE'  id='IN_MILEAGE' datatype="0,is_double,11"  value=""   class="middle_txt"/></td>       
       </tr>
       <tr>
         <td class="table_edit_3Col_label_6Letter">申请日期：</td>
         <td>
         <input name="APPROVAL_DATE" id="t3" value="" type="text" class="short_txt" datatype="0,is_date,10"  hasbtn="true" callFunction="showcalendar(event, 't3', false);"/>
         </td>
         <td class="table_edit_3Col_label_5Letter">申请人：</td>
         <td><input type='text'  name='APPROVAL_PERSON'  id='APPROVAL_PERSON' datatype="0,is_digit_letter_cn,6"   class="short_txt" value="<%=user.get("NAME")==null?"":user.get("NAME").toString() %>"/></td>
         <td class="table_edit_3Col_label_8Letter">联系电话：</td>
         <td><input type='text'  name='APPROVAL_PHONE'  id='APPROVAL_PHONE' value="<%=user.get("HAND_PHONE")==null?"":user.get("HAND_PHONE").toString() %>" datatype="0,is_phone,15" class="middle_txt"/></td>
       </tr>
       <tr>
         <td class="table_edit_3Col_label_6Letter">外出时间：</td>
         <td>
         <input name="OUT_DATE" id="t4" value="" type="text" class="short_txt" datatype="0,is_date,10"  hasbtn="true" callFunction="showcalendar(event, 't4', false);"/>
         </td>
         <td class="table_edit_3Col_label_5Letter">外出人：</td>
         <td><input type='text'  name='OUT_PERSON'  id='OUT_PERSON' datatype="0,is_digit_letter_cn,6"   class="short_txt" value=""/></td>
         <td class="table_edit_3Col_label_8Letter">外出费用：</td>
         <td><input type='text'  name='OUT_FEE'  id='OUT_FEE' value="" datatype="0,is_double,11" class="middle_txt"/></td>
       </tr>       
       <tr>
       	<td class="table_edit_3Col_label_6Letter">申请类型：</td>
       	<td>
			<script type="text/javascript">
             genSelBoxExp("APPROVAL_TYPE",<%=Constant.APPLY_TYPE%>,"",false,"min_sel","","false",'');
           	</script>       		
       	</td>
       </tr>              
    </table>


     <table  class="table_list" style="border-bottom:1px solid #DAE0EE" >
       <tr>
         <th colspan="6"  align="left" ><img class="nav" src="<%=contextPath%>/img/subNav.gif" /> 项目信息 
         	<input type="button" name="add" value="新增" class="normal_btn" onclick="showItem();"/>
         </th>         
       </tr>
	  <tbody id="tb1">
         <th >序号</th>
         <th >项目类型</th>
         <th >项目代码</th>
         <th >项目名称</th>
         <th >故障描述及维修方案</th>
         <th >操作</th>
       
       </tbody>
     </table>
    <br/>
	<!-- 添加附件 -->
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
		</table>     
     <table class="table_add">
		<tr> 
            <td align=center>
				<input type="button" onclick="checkFormUpdate('s');" class="normal_btn"  value="保存"/>&nbsp;&nbsp;
				<input type="button" onclick="checkFormUpdate('u');" class="normal_btn"  value="提报"/>
				&nbsp;&nbsp;
				<input type="button" onclick="javascript:history.go(-1);" class="normal_btn"  value="返回"/>
		    </td>
		</tr>
	</table>
</form>
<script type="text/javascript">
<!--
	//项目选择弹出页面：Add('/claim/preAuthorization/PreclaimPreMain/preclaimPreAddInit.do');
	function showItem(){
		var vin = document.getElementById("VIN").value;
		if(vin==null || vin==""){
			MyAlert("VIN码不能为空!");
			return false;
		}
		//车型id
		var modelid = document.getElementById("MODEL_ID").value;
		var temids =  document.getElementById("IDS").value;
		var le = tb1.rows.length;
		var val = "";
		for(var i=1;i<le;i++ ){
			if(val)
				val +=","+tb1.childNodes[i].childNodes[0].childNodes[0].value;
			else
				val = tb1.childNodes[i].childNodes[0].childNodes[0].value;	//取某行的第一列的隐藏域的值，也是项目id
		}
		OpenHtmlWindow('<%=contextPath%>/claim/preAuthorization/PreclaimPreMain/preclaimPreAddInit.do?MODEL_ID='+modelid+'&ids='+val,800,500);
	}	
	//获取VIN的方法
	function showVIN(){
		OpenHtmlWindow('<%=contextPath%>/claim/preAuthorization/PreclaimPreMain/getDetailByVinForward.do',800,500);
	}
	//校验工单和行号在索赔单表中是否有重复
	function oneVIN() {
		var vin = document.getElementById("VIN").value;
		var url = '<%=contextPath%>/claim/preAuthorization/PreclaimPreMain/getDetailByVin.json?COMMAND=1';
		var pattern=/^([A-Z]|[0-9]){17,17}$/;
		if(pattern.exec(vin)) {
			if (vin!=null&&vin!='') {
	    		makeCall(url,oneVINBack,{vinParent:vin});
	    	}
		}else {
			document.getElementById("LICENSE_NO").value = '';
			document.getElementById("SERIES_NAME").innerHTML = '';
			//document.getElementById("ENGINE_NO").innerHTML = '';
			document.getElementById("ENGINE_NO_H").value = '';
			document.getElementById("MODEL_NAME").innerHTML = '';
			document.getElementById("BAND_NAME").innerHTML = '';
			document.getElementById("MODEL_ID").value = '';//车型id
			document.getElementById("YIELDLY").value = '';//产地
			document.getElementById("PURCHASED_DATE").innerHTML = '';//保险开始日期
			document.getElementById("KEEP_BEG_DATE").value = '';			
		
			document.getElementById("BRAND_CODE").value = '';
			document.getElementById("SERIES_CODE").value = '';
			document.getElementById("MODEL_CODE").value = '';
			MyAlert("输入的不是有效VIN格式！");
		}
	}
	//回调函数
	function oneVINBack(json) {
    	var last=json.ps.records;
    	var size=last.length;
    	var record;
    	if (size>0) {
    		record = last[0];
    		document.getElementById("VIN").value = getNull(record.vin);
			document.getElementById("LICENSE_NO").value = getNull(record.licenseNo);
			document.getElementById("SERIES_NAME").innerHTML = getNull(record.seriesName);
			//document.getElementById("ENGINE_NO").innerHTML = getNull(record.engineNo);
			document.getElementById("ENGINE_NO_H").value = getNull(record.engineNo);
			document.getElementById("MODEL_NAME").innerHTML = getNull(record.modelName);
			document.getElementById("BAND_NAME").innerHTML = getNull(record.brandName);
			document.getElementById("MODEL_ID").value = getNull(record.modelId);//车型id
			//document.getElementById("YIELDLY").value = getNull(record.yieldly);//产地
			assignSelect("YIELDLY",getNull(record.yieldly));
			if(getNull(record.purchasedDate) && getNull(record.purchasedDate) !=''){
				document.getElementById("PURCHASED_DATE").innerHTML = getNull(record.purchasedDate).substring(0,10);//保险开始日期
				document.getElementById("KEEP_BEG_DATE").value = getNull(record.purchasedDate).substring(0,10);
			}else{
				document.getElementById("PURCHASED_DATE").innerHTML = getNull(record.purchasedDate);//保险开始日期
				document.getElementById("KEEP_BEG_DATE").value = getNull(record.purchasedDate);			
			}
		
			document.getElementById("BRAND_CODE").value = getNull(record.brandCode);
			document.getElementById("SERIES_CODE").value = getNull(record.seriesCode);
			document.getElementById("MODEL_CODE").value = getNull(record.modelCode);
			deleteItems();
    	}
    }
	//获取根据VIN子页面传过来的数据
	function setVIN(VIN,LICENSE_NO,MODEL_NAME,SERIES_NAME,ENGINE_NO,PURCHASED_DATE,YIELDLY,MODEL_ID,BRAND_NAME,BRAND_CODE,SERIES_CODE,MODEL_CODE){
		document.getElementById("VIN").value = VIN;
		//document.getElementById("LICENSE_NO").innerHTML = LICENSE_NO;
		document.getElementById("LICENSE_NO").value = LICENSE_NO;
		document.getElementById("SERIES_NAME").innerHTML = SERIES_NAME;
		//document.getElementById("ENGINE_NO").innerHTML = ENGINE_NO;
		document.getElementById("ENGINE_NO_H").value = ENGINE_NO;
		document.getElementById("MODEL_NAME").innerHTML = MODEL_NAME;
		document.getElementById("BAND_NAME").innerHTML = BRAND_NAME;
		document.getElementById("MODEL_ID").value = MODEL_ID;//车型id
		//document.getElementById("YIELDLY").innerHTML = YIELDLY;//产地
		document.getElementById("YIELDLY").value = YIELDLY;//产地
		if(PURCHASED_DATE && PURCHASED_DATE !=''){
			document.getElementById("PURCHASED_DATE").innerHTML = PURCHASED_DATE.substring(0,10);//保险开始日期
			document.getElementById("KEEP_BEG_DATE").value = PURCHASED_DATE.substring(0,10);
		}else{
			document.getElementById("PURCHASED_DATE").innerHTML = PURCHASED_DATE;//保险开始日期
			document.getElementById("KEEP_BEG_DATE").value = PURCHASED_DATE;			
		}
		
		document.getElementById("BRAND_CODE").value = BRAND_CODE;
		document.getElementById("SERIES_CODE").value = SERIES_CODE;
		document.getElementById("MODEL_CODE").value = MODEL_CODE;
		deleteItems();
		
	}
	//删除项目：
	function deleteItems(){
		var le = tb1.rows.length;
		if(le > 1){
			for(var i=1;i<le;i++){
				tb1.deleteRow(1);
			}
		}
	}
	//行号计数：
	var rowlen;
	/**动态生成项目信息的表格
	*  str          :  项目的ID
	*  itemtypedesc :  项目类型的描述（维修项目、维修材料、其他费用）
	*  itemtypeid   :  项目类型对应的tc_code
	*  itemcode     :  项目代码
	*  itemname     :  项目名称
	*/
	function setItem(str,itemtypedesc,itemtypeid,itemcode,itemname){
		//判断id
		var i = tb1.rows.length;
		if(i==1){
			rowlen = 1;
		}else{
			//rowlen++;
			rowlen = parseInt(tb1.childNodes[i-1].childNodes[0].innerText) + 1;//取出行号
		}
		//要筛选的id
		var tdsvalue = document.getElementById("IDS").value;
		var tdsv = "";
		//循环添加行数：
		for(var j=0;j<str.length;j++){
			if(tdsvalue){
					tdsv += ","+str[j];
			}else{
				if(j != str.length - 1){
					tdsv += str[j]+",";
				}else{
					tdsv +=str[j];
				}
			}
			//itemid
			var td1 = '<input type=\"hidden\" name=\"ITEMID_ID\" value=\"'+str[j]+'\"/>';
			//项目类型：
			var td2 = '<input type=\"hidden\" name=\"ITEMID_TYPE\" value=\"'+itemtypeid[j]+'\"/>';
			//项目代码：
			var td3 = '<input type=\"hidden\" name=\"ITEMID_CODE\" value=\"'+itemcode[j]+'\"/>';	
			//项目名称：
			var td4 = '<input type=\"hidden\" name=\"ITEMID_NAME\" value=\"'+itemname[j]+'\"/>';									
			//描述意见
			var td5 = '<textarea  name=\"DEALER_REMARK\"  datatype=\"0,is_textarea,200\"  rows=\"1\" cols=\"30\"></textarea>';
			//操作
			var td6 = '<input type=\"button\" value=\"删除\" class=\"normal_btn\" name=\"remain\" onclick=\"javascript:delItem(this)\">';
			var aTr = document.createElement("tr");	
			if(rowlen%2==0){
				aTr.className = "table_list_row2";//偶数行样式
			}else{
				aTr.className = "table_list_row1";//奇数行样式
			}
			tb1.appendChild(aTr);
			//6列
			var aTD1 = document.createElement("td");
			var aTD2 = document.createElement("td");
			var aTD3 = document.createElement("td");
			var aTD4 = document.createElement("td");
			var aTD5 = document.createElement("td");
			var aTD6 = document.createElement("td");
				
			aTr.appendChild(aTD1);
			aTr.appendChild(aTD2);
			aTr.appendChild(aTD3);
			aTr.appendChild(aTD4);
			aTr.appendChild(aTD5);
			aTr.appendChild(aTD6);
			if(j!=str.length-1){
				aTD1.innerHTML = td1 + rowlen++;
				
			}else{
				aTD1.innerHTML= td1 + rowlen;
			}
			aTD1.align = "center";
			aTD2.innerHTML = td2 + itemtypedesc[j];
			aTD2.align = "center";
			aTD3.innerHTML = td3 + itemcode[j];
			aTD3.align = "center";
			aTD4.innerHTML = td4 + itemname[j];
			aTD4.align = "center";
			aTD5.innerHTML =td5;	
			aTD5.align = "center";
			aTD6.innerHTML =  td6;	
			aTD6.align = "center";								
		}
		//给存在id赋值
		//document.getElementById("IDS").value = tdsvalue + tdsv;
	}
	//移除行
	function delItem(obj)
	{	
		var i = obj.parentElement.parentElement.rowIndex;
		//行号是从0开始的
		tb1.deleteRow(i-1);
	}
	
	//表单提交前的验证：
	function checkFormUpdate(st){
		if(!submitForm('fm')) {
			return false;
		}
		if (tb1.rows.length < 2){
			MyAlert("项目信息不能为空");
			return false;
		}
		var le = tb1.rows.length;
		var val = "";
		for(var i=1;i<le;i++ ){
			if(val)
				val +=","+tb1.childNodes[i].childNodes[0].childNodes[0].value;
			else
				val = tb1.childNodes[i].childNodes[0].childNodes[0].value;	//取某行的第一列的隐藏域的值，也是项目id
		}
		//给存在id赋值
		fm.IDS.value = val;			
		if(st == 's'){
			MyConfirm("是否确认保存?",checkForm,[st]);
		}else if(st == 'u'){
			MyConfirm("是否确认提报?",checkForm,[st]);
		}	
	}
	//表单提交方法：
	function checkForm(st){
			makeFormCall('<%=contextPath%>/claim/preAuthorization/PreclaimPreMain/preclaimAdd.json?submitType='+st,showResult,'fm');			
	}
	function goBack(){
		history.go(-1);
	}
	function showResult(json){
		if(json.success != null && json.success == "true"){
			window.location.href = "<%=contextPath%>/claim/preAuthorization/PreclaimPreMain/preclaimPreInit.do";
		}else{
			MyAlert("保存或提报失败，请联系管理员！");
		}
	}	
	
	//下端系统测试程序段：开始
	function getServiceName(){
		return "ClaimOrder";
	}
	
	function getSystemName(){
		return "ChangAn";
	}
	
	function setJson(str){
		var json = str.evalJSON();
		document.getElementById('RO_NO').value=json.TT_REPAIR_ORDER[0].roNo;
		document.getElementById('VIN').value=json.TT_REPAIR_ORDER[0].vin;
		document.getElementById('LICENSE_NO').value=json.TT_REPAIR_ORDER[0].lincesNo;
		document.getElementById('ENGINE_NO_H').value=json.TT_REPAIR_ORDER[0].engineNo;
		document.getElementById('BAND_NAME').innerHTML=json.TT_REPAIR_ORDER[0].brand;
		document.getElementById('SERIES_NAME').innerHTML=json.TT_REPAIR_ORDER[0].series;
		document.getElementById('MODEL_NAME').innerHTML=json.TT_REPAIR_ORDER[0].model;
		//保修开始日期
		document.getElementById('PURCHASED_DATE').innerHTML=json.TT_REPAIR_ORDER[0].guaranteeDate;
		document.getElementById('YIELDLY').value=json.TT_REPAIR_ORDER[0].yieldly;
		document.getElementById('DEST_CLERK').value=json.TT_REPAIR_ORDER[0].serveAdvisor;
		//进厂日期
		document.getElementById('t2').value=json.TT_REPAIR_ORDER[0].inFactoryDate;
		//申请日期
		document.getElementById('t3').value=json.TT_REPAIR_ORDER[0].inFactoryDate;
		document.getElementById('IN_MILEAGE').value=json.TT_REPAIR_ORDER[0].inMileage;
		//联系电话 APPROVAL_PHONE
		document.getElementById('APPROVAL_PHONE').value=json.TT_REPAIR_ORDER[0].applyPhone;
		var codes = new Array();
		var types = new Array();
		for (var i = 0; i < json.TT_RO_REPAIR_ITEM.length; i++) {
			codes.push(json.TT_RO_REPAIR_ITEM[i].CODE);
			types.push(json.TT_RO_REPAIR_ITEM[i].TYPE);	
		}
		makeFormCall('<%=contextPath%>/claim/preAuthorization/PreclaimPreMain/getItemsIdByCodes.json?codes='+codes+'&types='+types,handle,'fm');
					
	}
	function handle(json) {
		var str = new Array();//id数组
		var itemtypedesc = new Array();
		var itemtypeid = new Array();
		var itemcode = new Array();
		var itemname = new Array();
		for(var i=0;i<json.str.length;i++){        
			str.push(json.str[i]);
			itemtypedesc.push(json.itemtypedesc[i]);
			itemtypeid.push(json.itemtypeid[i]);
			itemcode.push(json.itemcode[i]);
			itemname.push(json.itemname[i]);
		}
		setItem(str,itemtypedesc,itemtypeid,itemcode,itemname)
	}
	
	function blurBack(obj) {
		oneVIN();
	}

//下端系统测试程序段：结束				
			
//-->
</script>
</body>
</html>
