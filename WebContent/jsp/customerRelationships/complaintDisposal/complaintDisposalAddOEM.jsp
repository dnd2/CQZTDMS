<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.Constant" %>
<%
	String contextPath = request.getContextPath();
%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>客户投诉维护(总部)</title>
<script type="text/javascript">
	function doInit(){
   		loadcalendar();  //初始化时间控件
   		genLocSel('txt1','txt2','txt3','','',''); // 加载省份城市和县
	}
	function oneVIN() {
		var vin = document.getElementById("vin").value;
		var url = '<%=contextPath%>/claim/dealerClaimMng/ClaimBillMaintainMain/getDetailByVin.json';
		var pattern=/^([A-Z]|[0-9]){17,17}$/;
		if(pattern.exec(vin)) {
			if (vin!=null&&vin!='') {
	    		makeCall(url,oneVINBack,{vinParent:vin});
	    	}
		}else {
			document.getElementById("licenseNo").value = '' ;
			document.getElementById("modelName").value = '' ;
			document.getElementById("modelCode").value = '' ;
			document.getElementById("engineNo").value = '' ;
			document.getElementById("purchasedDate").value='';
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
    		document.getElementById("vin").value =getNull(record.vin) ;
			document.getElementById("licenseNo").value = getNull(record.licenseNo) ;
			
			//document.getElementById("BRAND_NAME0").value = getNull(record.brandName) ;
			//document.getElementById("SERIES_NAME0").value = getNull(record.seriesName) ;
			document.getElementById("modelName").value = getNull(record.modelName) ;
			//document.getElementById("BRAND_CODE0").value = getNull(record.brandCode) ;
			//document.getElementById("SERIES_CODE0").value = getNull(record.seriesCode) ;
			document.getElementById("modelCode").value = getNull(record.modelCode) ;
			document.getElementById("engineNo").value = getNull(record.engineNo) ;
			document.getElementById("purchasedDate").value=formatDate(getNull(record.purchasedDateAct) );
    	}
    }
    //格式化时间为YYYY-MM-DD
	function formatDate(value) {
		if (value==""||value==null) {
			return "";
		}else {
			return value.substr(0,10);
		}
		//MyAlert(value);
		//var date = new Date(value);
		//MyAlert(date);
		//return DateUtil.Format("yyyy-MM-dd",value);
	}
	//null返回“”
	function getNull(data) {
		if (data==null) {
			return '';
		}else {
			return data;
		}
	}
</script>
</head>
<body>
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 客户关系管理 &gt; 客户投诉管理 &gt;客户投诉维护(总部)</div>
 <form method="post" name = "fm" >
   <!-- 车系列表 -->
   <input type="hidden" name="seriesList" id="seriesList" value="<%=request.getAttribute("seriesList")%>"/>
   <table width=100% border="0" align="center" cellpadding="1" cellspacing="1" class="table_query">
     <th colspan="7" align="left"><img class="nav" src="<%=contextPath%>/img/subNav.gif" /> 客户基本信息</th>
     <tr>
	    <td class="table_query_2Col_label_5Letter">联系人名称：</td>
	    <td><input type='text'  class="middle_txt" datatype="0,is_null,10" name="linkman"  id='linkmanId' value=""/></td>
	    <td class="table_query_2Col_label_5Letter">性别：</td>
	    <td><script type="text/javascript">
                genSelBoxExp("sex",<%=Constant.GENDER_TYPE%>,"",false,"short_sel","","false",'');
            </script>
	    </td>
	 </tr>
     <tr>
       <td class="table_query_2Col_label_5Letter">生日：</td>
	   <td><input  name="birthday"  id="t1" value="" type="text" class="short_txt" datatype="1,is_date,10"  hasbtn="true" callFunction="showcalendar(event, 't1', false);"/></td>
       <td class="table_query_2Col_label_5Letter">年龄：</td>
       <td>
          <script type="text/javascript">
                genSelBoxExp("age",<%=Constant.AGE_TYPE%>,"",false,"short_sel","","false",'');
            </script>
       </td>
    </tr>
    <tr>
       <td class="table_query_2Col_label_5Letter">所属大区：</td>
       <td><input type="text"  class="middle_txt" name="ownOrgName" datatype="0,is_null,50" id="ownOrgName" value=""/>
           <input name="ownOrg" type="button" class="mini_btn" onclick="showNewOrg('ownOrgName' ,'ownOrgId' ,false,'')" value="&hellip;" />
           <input name="ownOrgId" type="hidden" id="ownOrgId"/>	
       </td> 
       <td class="table_query_2Col_label_5Letter">联系电话：</td>
       <td><input type="text"  class="middle_txt" datatype="0,is_phone,13" name="tel"  id='tel' value=""/></td>
    </tr>
    <tr>
      <td class="table_query_2Col_label_5Letter">省份：</td>
      <td><select class="short_sel" id="txt1" name="province" onchange="_genCity(this,'txt2')"></select><font color="red">*</font> </td>
      <td class="table_query_2Col_label_5Letter">Email：</td>
      <td><input type="text"  class="middle_txt" name="email"  id="email" datatype="1,is_email,30" value=""/></td>
    </tr>
    <tr>
      <td class="table_query_2Col_label_5Letter">地级市：</td>
      <td><select class="short_sel" id="txt2" name="city" onchange="_genCity(this,'txt3')"></select></td>  
      <td class="table_query_2Col_label_5Letter">邮编：</td>
      <td><input type="text"  class="middle_txt" name="zipCode"  id="zipCode" value=""/></td>
     </tr>
     <tr>
     <td class="table_query_2Col_label_5Letter">区、县：</td>
     <td><select class="short_sel" id="txt3" name="district"></select> </td> 
     <td class="table_query_2Col_label_5Letter">投诉经销商：</td>
     <td>
         <input class="middle_txt" id="compDealerCode" name="compDealerCode" value="" type="text"/>
         <input class="mini_btn" type="button" value="&hellip;" onclick="showNewOrgDealer('compDealerCode','compDealerId','false','')"/> 
         <input type="hidden" name="compDealerId" id="compDealerId" value=""/>
     </td>
     </tr>
     <tr>
      <td class="table_query_2Col_label_5Letter">家庭住址：</td>
      <td><textarea name="address" id="address"  style='border: 1px solid #94BBE2;width:70%;overflow: hidden;word-break:break-all;' rows="1"></textarea></td> 
     </tr>
  </table>
  <br>
  <table class="table_list">
   <th colspan="6" align="left"><img class="nav" src="<%=contextPath%>/img/subNav.gif" /> 客户车辆信息</th>
      <TR>
        <th>VIN</th>
        <th>车型</th>
        <th>发动机号</th>
        <th>车牌号</th>
        <th>购车日期</th>
      </TR>
      <tr class="table_list_row2">
      <TD><input type="text"  class="middle_txt" name="vin" onblur="oneVIN()" id="vin" maxlength="17" value=""/></TD>
      <TD>
      <!--  
      <script type="text/javascript">
              var seriesList=document.getElementById("seriesList").value;
    	      var str="";
    	      str += "<select id='modelCode' name='modelCode' class='middle_sel'>";
    	      str+=seriesList;
    		  str += "</select>";
    		  document.write(str);
	  </script>
	  -->
	  <input type="hidden"  class="middle_txt" name="modelCode"  id="modelCode" value=""/>
	  <input type="text"  class="middle_txt" name="modelName"  id="modelName" value=""/>
      </TD>
      <TD><input type="text"  class="middle_txt" name="engineNo"  id="engineNo" value=""/></TD>
      <TD><input type="text"  class="middle_txt" name="licenseNo"  id="licenseNo" value=""/></TD>
      <TD><input  name="purchasedDate"  id="t2" value="" type="text" class="short_txt" datatype="1,is_date,10"  hasbtn="true" callFunction="showcalendar(event, 't2', false);"/></TD>
     </tr>
  </table>
  <br>
  <TABLE class="table_query">
		<th colspan="6" align="left"><img class="nav" src="<%=contextPath%>/img/subNav.gif" /> 投诉内容</th>
		<TR>
		  <TD><div align="right">投诉等级：</div></TD>
		  <TD>&nbsp;</TD>
		  <TD><div align="left">
		    <script type="text/javascript">
            	genSelBoxExp("compLevel",<%=Constant.COMP_LEVEL_TYPE%>,"",false,"short_sel","","false",'');
                </script>
		  </div></TD>
	    </TR>
		<TR>
		  <TD><div align="right">投诉类型：</div></TD>
		  <TD>&nbsp;</TD>
		  <TD><div align="left">
		    <script type="text/javascript">
            	genSelBoxExp("compType",<%=Constant.COMP_TYPE_TYPE%>,"",false,"min_sel","onchange='selType()'","false",'');
            </script>
		    <span id="compTypechng">
            	<script type="text/javascript">
            	genSelBoxExp("compType2",<%=Constant.SEDAN_QUALITY%>,"",false,"min_sel","onchange='selType()'","false",'');
            </script>
            </span>
           
		  </div></TD>
	  </TR>
	  <TR>
		  <TD><div align="right">投诉来源：</div></TD>
		  <TD>&nbsp;</TD>
		  <TD><div align="left">
		    <script type="text/javascript">
            	genSelBoxExp("compSource",<%=Constant.COMP_SOURCE_TYPE%>,"",false,"short_sel","","false",'');
            </script>
		  </div></TD>
	  </TR>
	   <TR>
		  <TD><div align="right">客户投诉内容：</div></TD>
		  <TD>&nbsp;</TD>
		  <TD><textarea name="compContent"  style='border: 1px solid #94BBE2; width: 95%; overflow: hidden; word-break: break-all;'rows=5></textarea></TD>
	  </TR>
	</TABLE>
	<br>
  <table class="table_query">
		<th colspan="6" align="left"><img class="nav" src="<%=contextPath%>/img/subNav.gif"/> 处理明细</th>
		<TR>
			<TD>
			<div align="right">发生动作：</div></TD>
			<TD>&nbsp;</TD>
			<TD>
			<div align="left">
			<c:forEach items="${ListCheckBox}" var="lcb">
       			<input type="checkbox" name="auditAction" value="<c:out value="${lcb.CODE_ID}"/>"><c:out value="${lcb.CODE_DESC}"/>&nbsp;&nbsp;
       		</c:forEach>
			</div>			
			</TD>
		</TR>
		<TR>
			<TD>
			<div align="right">处理结果：</div></TD>
			<TD>&nbsp;</TD>
			<TD>
			<div align="left">
		
			  <script type="text/javascript">
            	genSelBoxExp("auditResult",<%=Constant.AUDIT_RESULT_TYPE%>,"",true,"short_sel","onchange='selectAction()'","false",'');
              </script>
    		  <span id="partcode" style="display:none">备件编码
                <input name="partCode" type="text" id="partCode"  class="middle_txt" datatype="0,is_null,15"></span>

              <span id="supplier" style="display:none">上级保供单位
                <input name="supplier" type="text" id="supplier"  class="middle_txt" datatype="0,is_null,15"></span>

			</div></TD>
		</TR>
		<TR>
			<TD>
			<div align="right">跟进结果描述：</div></TD>
			<TD>&nbsp;</TD>
			<TD>
            	<textarea name="auditContent"
				style='border: 1px solid #94BBE2; width: 95%; overflow: hidden; word-break: break-all;'rows=5></textarea></TD>
		</TR>
        <TR>
		  <td colspan="3" align="center">
		  	  <input name="saveBtn" type="button" class="normal_btn" onclick="saveComplaint()" value="保存" />&nbsp;&nbsp;&nbsp;&nbsp;
		  </td>
	    </TR>
  </table>
  <br>
    <table class="table_query">
	  <tr>
	    <th colspan="6" align="left"><img src="<%=contextPath%>/img/subNav.gif" class="nav"/> 分配明细</th>
      </tr>
	  <tr>
	    <td><div align="center">区域
	      <input name="orgCode" type="text" id="orgCode" size="40" />
	      <input name="orgId" type="hidden" id="orgId" />
		  <input name="orgSel" type="button" class="mini_btn" onclick="showNewOrg('orgCode','orgId',false,'')" value="&hellip;" />	
		</div></td>	
	    <td>
		&nbsp;&nbsp;&nbsp;&nbsp;</td>		
      </tr>
	</table>
	<br>
    <TABLE class="table_query">
	<TR><TD align="center">
	<input name="assign" type="button" class="normal_btn" onclick="assignComplaint()" value="分配" />
	<input name="back" type="button" class="normal_btn" onClick="history.back();" value="返回"></TD>
	</TR>	
	</TABLE>
</form>
<!--页面列表 begin -->
<script type="text/javascript" >
// 动态显示备件编码和上级保供单位
function selectAction(){
		if(document.fm.auditResult.value == '<%=Constant.AUDIT_RESULT_TYPE_02%>'||document.fm.auditResult.value == '<%=Constant.AUDIT_RESULT_TYPE_03%>') {
			
			document.getElementById("partcode").style.display = "inline";
			document.getElementById("supplier").style.display = "inline";
			
		}else{
		    document.getElementById("partcode").style.display = "none";
			document.getElementById("supplier").style.display = "none";
		}
	}	

// 投诉类型的联动显示
function selType(){
	if(document.fm.compType.value == '<%=Constant.COMP_TYPE_TYPE_01%>') {
			var sc = genSelBoxStrExp('compType2',<%=Constant.SEDAN_QUALITY%>,"",false,"min_sel","","false",'');
			var pu = document.getElementById("compTypechng").innerHTML = sc;
			
		}else if(document.fm.compType.value == '<%=Constant.COMP_TYPE_TYPE_02%>'){
			var sc = genSelBoxStrExp('compType2',<%=Constant.SEDAN_MEDIUM_COMPLAINS%>,"",false,"min_sel","","false",'');
			var pu = document.getElementById("compTypechng").innerHTML = sc;
		
		}else if(document.fm.compType.value == '<%=Constant.COMP_TYPE_TYPE_03%>'){
			var sc = genSelBoxStrExp('compType2',<%=Constant.SEDAN_SERVES%>,"",false,"min_sel","","false",'');
			var pu = document.getElementById("compTypechng").innerHTML = sc;
		
		}
		else if(document.fm.compType.value == '<%=Constant.COMP_TYPE_TYPE_04%>'){
			var sc = genSelBoxStrExp('compType2',<%=Constant.SEDAN_ACCESSORIES%>,"",false,"min_sel","","false",'');
			var pu = document.getElementById("compTypechng").innerHTML = sc;
		
		}
		else if(document.fm.compType.value == '<%=Constant.COMP_TYPE_TYPE_05%>'){
			var sc = genSelBoxStrExp('compType2',<%=Constant.SALE_CONSULTS%>,"",false,"min_sel","","false",'');
			var pu = document.getElementById("compTypechng").innerHTML = sc;
		
		}
		else if(document.fm.compType.value == '<%=Constant.COMP_TYPE_TYPE_06%>'){
			var sc = genSelBoxStrExp('compType2',<%=Constant.SALE_COMPLAINS%>,"",false,"min_sel","","false",'');
			var pu = document.getElementById("compTypechng").innerHTML = sc;
		
		}
		else if(document.fm.compType.value == '<%=Constant.COMP_TYPE_TYPE_07%>'){
			var sc = genSelBoxStrExp('compType2',<%=Constant.SEDAN_HELPS%>,"",false,"min_sel","","false",'');
			var pu = document.getElementById("compTypechng").innerHTML = sc;
		
		}
		else if(document.fm.compType.value == '<%=Constant.COMP_TYPE_TYPE_08%>'){
			var sc = genSelBoxStrExp('compType2',<%=Constant.SEDAN_SERVES_DEALER%>,"",false,"min_sel","","false",'');
			var pu = document.getElementById("compTypechng").innerHTML = sc;
		
		}
		else if(document.fm.compType.value == '<%=Constant.COMP_TYPE_TYPE_09%>'){
			var sc = genSelBoxStrExp('compType2',<%=Constant.SEDAN_ACTIVITY%>,"",false,"min_sel","","false",'');
			var pu = document.getElementById("compTypechng").innerHTML = sc;
		
		}else if(document.fm.compType.value == '<%=Constant.COMP_TYPE_TYPE_10%>'){
			var sc = genSelBoxStrExp('compType2',<%=Constant.SEDAN_OTHER_PROBLEMS%>,"",false,"min_sel","","false",'');
			var pu = document.getElementById("compTypechng").innerHTML = sc;
		
		}else{
			var sc = genSelBoxStrExp('compType2','','',true,'min_sel','','true','');
			var pu = document.getElementById("compTypechng").innerHTML = sc;
		}
}


// 保存新增信息
function saveComplaint(){
    var cnt=0;
	var chk = document.getElementsByName("auditAction");
	
	var l = chk.length;
	
	for(var i=0;i<l;i++){
		if(chk[i].checked){
			 cnt++;
		}
	}
	
	var auditResult = document.fm.auditResult.value;
	
	var auditContent = document.fm.auditContent.value;
	if(cnt==0){
		if(auditContent.length>0 || auditResult){
			MyAlert("未做处理，跟进结果描述将无效!");
			return;
		}
		
	}
	if(cnt != 0){
		if (!auditResult) {
			MyAlert("请选择处理结果");
			return;
		}
		if (!auditContent) {
			MyAlert("跟进结果描述不能为空");
			return;
		}	
	}
	var val = document.getElementById("txt1").value;
	if(val==null||val==""||val=="null")
	{
		MyAlert("省份为必填项！");
		return;	
	}
	if(submitForm('fm')){
		fm.action = "<%=contextPath%>/customerRelationships/complaint/ComplaintDisposalOEM/saveComplaint.do?";
    	fm.submit();
	}
}

// 确认关闭
function confirmClose(){
	var cnt=0;
	var chk = document.getElementsByName("auditAction");
	
	var l = chk.length;
	
	for(var i=0;i<l;i++){
		if(chk[i].checked){
			 cnt++;
		}
	}
	
	var auditResult = document.fm.auditResult.value;
	
	if(cnt==0&&auditResult.indexOf("")==0){
	    MyAlert("未做处理，不能关闭！");
		return ;
	}else{
		if(submitForm('fm')){
    		fm.action = "<%=contextPath%>/customerRelationships/complaint/ComplaintDisposalOEM/closeComplaint.do?flag=0";
    		fm.submit();
	
		}
	}
}

// 分配到大区
function assignComplaint(){
	
	var val = document.getElementById("txt1").value;
	if(val==null||val==""||val=="null")
	{
		MyAlert("省份为必填项！");
		return;	
	}

	if(submitForm('fm')){
		fm.action = "<%=contextPath%>/customerRelationships/complaint/ComplaintDisposalOEM/assignComplaint.do?flag=0";
    	fm.submit();
    }
}
	



// 通过vin带出其他车辆信息
function showVIN(){
	OpenHtmlWindow('<%=contextPath%>/customerRelationships/complaint/ComplaintDisposalOEM/showVinList.do',800,500);
}	
	
	
// 取得子页面传回的值	
function getVIN(VIN,GROUP_NAME,ENGINE_NO,VEHICLE_NO,PURCHASED_DATE){
		document.getElementById("vin").value = VIN;
        var obj = document.getElementById("modelCode").options; 
		var le = obj.length;
		for(var i=0;i<le;i++){
			if(obj.options[i].text==GROUP_NAME){
				obj[i].selected =true;
			}
		
		}
		document.getElementById("engineNo").value = ENGINE_NO;
		document.getElementById("licenseNo").value = VEHICLE_NO;
		document.fm.purchasedDate.value = PURCHASED_DATE;
	}
</script>
<!--页面列表 end -->
</body>
</html>
