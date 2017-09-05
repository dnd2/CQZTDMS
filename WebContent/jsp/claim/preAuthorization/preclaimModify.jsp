<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="com.infodms.dms.util.CommonUtils"%>
<%@page import="com.infodms.dms.po.FsFileuploadPO"%>
<%@ page import="com.infodms.dms.common.Constant" %>
<%
	String contextPath = request.getContextPath();
	Map  dealer = (Map)request.getAttribute("DEALERHM");//经销商信息
	HashMap hm = (HashMap)request.getAttribute("FOREAPPROVAL_HASHMAP");//预授权信息
	//预授权明细
	List items = (List)request.getAttribute("FOREAPPROVALITEM_LIST");
	List<FsFileuploadPO> attachLs = (LinkedList<FsFileuploadPO>)request.getAttribute("attachLs");
%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>预授权状态查询_预授权申请编辑</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<script>
	function doInit(){
   		loadcalendar();  //初始化时间控件 		
	}
</script>
</head>
<body>
<div class="navigation">
<img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 售后服务管理&gt;索赔预授权&gt;索赔预授权工单申请</div> 
<form name='fm' id='fm'>
	<input type="hidden" name="MODEL_ID" id="MODEL_ID" value="<%=hm.get("MODEL_ID")==null ? "" : hm.get("MODEL_ID") %>"/><!-- 车型id -->
	<input type="hidden" name="IDS" id="IDS"/><!-- 项目添加完的id -->
	 <input type="hidden" name="ID" id="ID" value="<%=request.getAttribute("ID")==null?"":request.getAttribute("ID")%>"/><!-- 待修改的主键ID -->
	<input type="hidden" name="dealerCode" id="dealerCode" value="<%=dealer.get("DEALER_CODE")==null ? "":dealer.get("DEALER_CODE").toString() %>"/><!-- 经销商code -->
	<input type="hidden" name="BRAND_CODE" id="BRAND_CODE" value="<%=dealer.get("BRAND_CODE")==null ? "":dealer.get("BRAND_CODE").toString() %>"/><!-- 品牌 -->
    <input type="hidden" name="SERIES_CODE" id="SERIES_CODE" value="<%=dealer.get("SERIES_CODE")==null ? "":dealer.get("SERIES_CODE").toString() %>"/><!-- 车系 -->
    <input type="hidden" name="MODEL_CODE" id="MODEL_CODE" value="<%=dealer.get("MODEL_CODE")==null ? "":dealer.get("MODEL_CODE").toString() %>"/><!-- 车型 -->
    <input type="hidden" name="ENGINE_NO_H" id="ENGINE_NO_H" value="<%=dealer.get("ENGINE_NO")==null ? "":dealer.get("ENGINE_NO").toString() %>"/><!-- 发动机号 -->
     <input type="hidden" name="KEEP_BEG_DATE" id="KEEP_BEG_DATE" value="<%=dealer.get("KEEP_BEG_DATE")==null ? "":dealer.get("KEEP_BEG_DATE").toString() %>"/><!-- 保险开始日期 -->
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
         <td ><input type="text"  name="RO_NO"  id="RO_NO" value="<%=hm.get("RO_NO")==null ? "" : hm.get("RO_NO") %>"   class="middle_txt" datatype="1,is_digit_letter,30"/></td>
       </tr>
       <tr>
         <td class="table_edit_3Col_label_6Letter">VIN：</td>
         <td>
         	<input type='text'  name='VIN'  id='VIN' readonly="readonly"  value="<%=hm.get("VIN")==null ? "" : hm.get("VIN") %>" onclick="showVIN()" datatype="0,is_vin,17" style="cursor: pointer;" class="short2_txt"/>
         </td>
         <td class="table_edit_3Col_label_5Letter">牌照号：</td>
         <td>
         	<input type='text'  name='LICENSE_NO'  id='LICENSE_NO' value="<%=hm.get("LICENSE")==null ? "" : hm.get("LICENSE") %>" datatype="1,is_carno,30"   class="short_txt"/>
         </td>
         <td class="table_edit_3Col_label_8Letter">发动机号：</td>
         <td align="left" id="ENGINE_NO"><%=hm.get("ENGINE_NO")==null ? "" : hm.get("ENGINE_NO") %></td>
       </tr>
       <tr>
         <td class="table_edit_3Col_label_6Letter">品牌：</td>
         <td align="left" id="BAND_NAME"><%=hm.get("BRAND_NAME")==null ? "" : hm.get("BRAND_NAME") %></td>
         <td class="table_edit_3Col_label_5Letter">车系：</td>
         <td align="left" id="SERIES_NAME"><%=hm.get("SERIES_NAME")==null ? "" : hm.get("SERIES_NAME") %></td>
         <td class="table_edit_3Col_label_8Letter">车型：</td>
         <td align="left" id="MODEL_NAME"><%=hm.get("MODEL_NAME")==null ? "" : hm.get("MODEL_NAME") %></td>
       </tr>       
       <tr>
         <td class="table_edit_3Col_label_6Letter">保修开始日期：</td>
         <td align="left" id="PURCHASED_DATE"><%=hm.get("KEEP_BEG_DATE")==null ? "" : hm.get("KEEP_BEG_DATE").toString().substring(0,10) %></td>
               
         <td class="table_edit_3Col_label_5Letter">产地：</td>
         <td>
<%--         	<input type='text'  name='YIELDLY'  id='YIELDLY' value="<%=hm.get("T_YIELDLY")==null ? "" : hm.get("T_YIELDLY").toString() %>" datatype="1,is_null,15"   class="short_txt"/>--%>
			<script type="text/javascript">
             genSelBoxExp("YIELDLY",<%=Constant.SERVICEACTIVITY_CAR_YIELDLY%>,"<%=hm.get("T_YIELDLY")==null ? "" : hm.get("T_YIELDLY") %>",false,"min_sel","","false",'');
           	</script> 
         </td>
       </tr>
       <tr>
        <td class="table_edit_3Col_label_6Letter">接待员：</td>
         <td ><input type='text'  name='DEST_CLERK'  id='DEST_CLERK'  value="<%=hm.get("DEST_CLERK")==null ? "" : hm.get("DEST_CLERK") %>" datatype="0,is_digit_letter_cn,6" value=""  class="short_txt"/></td>       
         <td class="table_edit_3Col_label_5Letter">进厂日期：</td>
         <td>
         <input name="IN_FACTORY_DATE" id="t1" value="<%=hm.get("IN_FACTORY_DATE")==null ? "" : hm.get("IN_FACTORY_DATE").toString().substring(0,10) %>" type="text" class="short_txt" datatype="0,is_date_now,10"  hasbtn="true" callFunction="showcalendar(event, 't1', false);"/>
         </td>        
        <td class="table_edit_3Col_label_8Letter">进厂里程数(公里)：</td>
         <td><input type='text'  name='IN_MILEAGE' value="<%=hm.get("IN_MILEAGE")==null ? "" : hm.get("IN_MILEAGE") %>"  id='IN_MILEAGE' datatype="0,is_double,11"  value=""   class="middle_txt"/></td>       
       </tr>
       <tr>
         <td class="table_edit_3Col_label_6Letter">申请日期：</td>
         <td>
         <input name="APPROVAL_DATE" id="t3" value="<%=hm.get("APPROVAL_DATE")==null ? "" : hm.get("APPROVAL_DATE").toString().substring(0,10) %>" type="text" class="short_txt" datatype="0,is_date,10"  hasbtn="true" callFunction="showcalendar(event, 't3', false);"/>
         </td>
         <td class="table_edit_3Col_label_5Letter">申请人：</td>
         <td><input type='text'  name='APPROVAL_PERSON'  id='APPROVAL_PERSON' value="<%=hm.get("APPROVAL_PERSON")==null ? "" : hm.get("APPROVAL_PERSON") %>" datatype="0,is_digit_letter_cn,6"   class="short_txt" /></td>
         <td class="table_edit_3Col_label_8Letter">联系电话：</td>
         <td><input type='text'  name='APPROVAL_PHONE'  id='APPROVAL_PHONE' value="<%=hm.get("APPROVAL_PHONE")==null ? "" : hm.get("APPROVAL_PHONE") %>"  datatype="0,is_phone,15" class="middle_txt"/></td>
       </tr>
       <tr>
         <td class="table_edit_3Col_label_6Letter">外出时间：</td>
         <td>
         <input name="OUT_DATE" id="t4" value="<%=hm.get("OUT_DATE")==null ? "" : hm.get("OUT_DATE").toString().substring(0,10) %>" type="text" class="short_txt" datatype="0,is_date,10"  hasbtn="true" callFunction="showcalendar(event, 't4', false);"/>
         </td>
         <td class="table_edit_3Col_label_5Letter">外出人：</td>
         <td><input type='text'  name='OUT_PERSON'  id='OUT_PERSON' datatype="0,is_digit_letter_cn,6"   class="short_txt" value="<%=hm.get("OUT_PERSON")==null ? "" : hm.get("OUT_PERSON") %>"/></td>
         <td class="table_edit_3Col_label_8Letter">外出费用：</td>
         <td><input type='text'  name='OUT_FEE'  id='OUT_FEE' value="<%=hm.get("OUT_FEE")==null ? "" : hm.get("OUT_FEE") %>" datatype="0,is_double,11" class="middle_txt"/></td>
       </tr>      
       <tr>
       	<td class="table_edit_3Col_label_6Letter">申请类型：</td>
       	<td>
			<script type="text/javascript">
             genSelBoxExp("APPROVAL_TYPE",<%=Constant.APPLY_TYPE%>,"<%=hm.get("APPROVAL_TYPE")==null ? "" : hm.get("APPROVAL_TYPE") %>",false,"min_sel","","false",'');
           	</script>       		
       	</td>
       </tr>                     
    </table>


     <table  class="table_list" style="border-bottom:1px solid #DAE0EE" >
         <th colspan="6"  align="left" ><img class="nav" src="<%=contextPath%>/img/subNav.gif" /> 项目信息 
         	<input type="button" name="add" value="新增" class="normal_btn" onclick="showItem();"/>
         </th>         
     <tbody id="tb1">       
         <th >序号</th>
         <th >项目类型</th>
         <th >项目代码</th>
         <th >项目名称</th>
         <th >故障描述及维修方案</th>
         <th >操作</th>
	<%	
		for (int i=0; i<items.size(); i++){
			HashMap tempItem = (HashMap)items.get(i); 
			String className = ((i+1)%2)==0?"table_list_row2":"table_list_row1";
	%>       
	<tr class="<%=className%>">
		<td align="center">
		<%=i+1%>
			<input type="hidden" name="ITEMID_ID" value="<%= tempItem.get("ITEM_ID")==null ? "":tempItem.get("ITEM_ID")%>"/>
		</td>
		<td align="center">
			<input type="hidden" name="ITEMID_TYPE" value="<%= tempItem.get("ITEM_TYPE")==null ? "":tempItem.get("ITEM_TYPE")%>"/>
			<script type="text/javascript">
			<!--
				writeItemValue(<%= tempItem.get("ITEM_TYPE")==null ? "":tempItem.get("ITEM_TYPE")%>);
			//-->
			</script>			
		</td>
		<td align="center">
			<input type="hidden" name="ITEMID_CODE" value="<%= tempItem.get("ITEM_CODE")==null ? "":tempItem.get("ITEM_CODE")%>"/>
			<%= tempItem.get("ITEM_CODE")==null ? "":tempItem.get("ITEM_CODE")%>
		</td>
		<td align="center">
			<input type="hidden" name="ITEMID_NAME" value="<%= tempItem.get("ITEM_DESC")==null ? "":tempItem.get("ITEM_DESC")%>"/>
			<%= tempItem.get("ITEM_DESC")==null ? "":tempItem.get("ITEM_DESC")%>
		</td>
		<td align="center">
			<textarea id="DEALER_REMARK"  name="DEALER_REMARK"  datatype="1,is_textarea,200"  rows="1" cols="30"><%= tempItem.get("DEALER_REMARK") == null ? "":tempItem.get("DEALER_REMARK")%></textarea>
		</td>
		<td align="center">
			<input type="button" value="删除" class="normal_btn" name="remain" onclick="javascript:delItem(this)"/>
		</td>
    </tr>
    <%}%> 			      
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
  			<%for(int i=0;i<attachLs.size();i++) { %>
  			<script type="text/javascript">
  			addUploadRowByDb('<%=CommonUtils.checkNull(attachLs.get(i).getFilename()) %>','<%=CommonUtils.checkNull(attachLs.get(i).getFjid()) %>','<%=CommonUtils.checkNull(attachLs.get(i).getFileurl())%>');
  			</script>
  			<%} %>
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
		//车型id
		var modelid = document.getElementById("MODEL_ID").value;
		var le = tb1.rows.length;
		var val = "";
		for(var i=1;i<le;i++ ){
			if(val)
				val +=","+tb1.childNodes[i].childNodes[0].childNodes[1].value;
			else
				val = tb1.childNodes[i].childNodes[0].childNodes[1].value;	//取某行的第一列的隐藏域的值，也是项目id
		}
		OpenHtmlWindow('<%=contextPath%>/claim/preAuthorization/PreclaimPreMain/preclaimPreAddInit.do?MODEL_ID='+modelid+'&ids='+val,800,500);
	}	
	//获取VIN的方法
	function showVIN(){
		OpenHtmlWindow('<%=contextPath%>/claim/preAuthorization/PreclaimPreMain/getDetailByVinForward.do',800,500);
	}
	//获取根据VIN子页面传过来的数据
	function setVIN(VIN,LICENSE_NO,MODEL_NAME,SERIES_NAME,ENGINE_NO,PURCHASED_DATE,YIELDLY,MODEL_ID,BRAND_NAME,BRAND_CODE,SERIES_CODE,MODEL_CODE){
		document.getElementById("VIN").value = VIN;
		document.getElementById("LICENSE_NO").value = LICENSE_NO;
		document.getElementById("SERIES_NAME").innerHTML = SERIES_NAME;
		document.getElementById("ENGINE_NO").innerHTML = ENGINE_NO;
		document.getElementById("ENGINE_NO_H").value = ENGINE_NO;
		document.getElementById("MODEL_NAME").innerHTML = MODEL_NAME;
		document.getElementById("BAND_NAME").innerHTML = BRAND_NAME;
		document.getElementById("MODEL_ID").value = MODEL_ID;//车型id
		
		document.getElementById("YIELDLY").value = YIELDLY;//产地
		if(PURCHASED_DATE && PURCHASED_DATE !=''){
			document.getElementById("PURCHASED_DATE").innerHTML = PURCHASED_DATE.substring(0,10);//保险开始日期
			document.getElementById("KEEP_BEG_DATE").value = PURCHASED_DATE.substring(0,10);
		}else{
			document.getElementById("PURCHASED_DATE").innerHTML = PURCHASED_DATE;//保险开始日期
			document.getElementById("KEEP_BEG_DATE").value = PURCHASED_DATE;			
		}		
		//document.getElementById("PURCHASED_DATE").innerHTML = PURCHASED_DATE;//保险开始日期
		//document.getElementById("KEEP_BEG_DATE").value = PURCHASED_DATE;		
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
	function setItem(str,itemtypedesc,itemtypeid,itemcode,itemname){
		//判断id
		var i = tb1.rows.length;
		var cl = tb1.childNodes.length;
		var el = tb1.childNodes[cl - 1].childNodes[0].innerText;

		if(parseInt(el)){
			rowlen = parseInt(el) + 1;
		}else{
			rowlen = i;
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
				aTD1.innerHTML = rowlen++ + td1;
				
			}else{
				aTD1.innerHTML= rowlen + td1;
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
		if (tb1.rows.length == 0){
			MyAlert("项目信息不能为空");
			return false;
		}
		var le = tb1.rows.length;
		var val = "";
		for(var i=1;i<le;i++ ){
			if(val)
				val +=","+tb1.childNodes[i].childNodes[0].childNodes[1].value;
			else
				val = tb1.childNodes[i].childNodes[0].childNodes[1].value;	//取某行的第一列的隐藏域的值，也是项目id
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
			makeFormCall('<%=contextPath%>/claim/preAuthorization/PreclaimPreMain/preclaimUpdate.json?submitType='+st,showResult,'fm');			
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
//-->
</script>
</body>
</html>
