<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.Constant" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
<%
	String contextPath = request.getContextPath();
    List<Map<String, Object>>  listCheckBox = (List<Map<String, Object>>)request.getAttribute("ListCheckBox"); 
	List actionList = (List)request.getAttribute("actionList");
%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>客户投诉维护(总部)</title>
<script type="text/javascript">
function doInit(){
		//初始化时间控件
   		loadcalendar();  
   		// 加载省份城市和县
   		genLocSel('txt1','txt2','txt3','<c:out value="${complaintMap.PROVINCE}"/>','<c:out value="${complaintMap.CITY}"/>','<c:out value="${complaintMap.DISTRICT}"/>'); 
   		// 显示备件编码和上级保供单位
   		selectAction();
   		// 投诉类型的显示
   		selType();
	}
</script>
</head>
<body>
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 客户关系管理 &gt; 客户投诉管理 &gt;客户投诉维护(总部)</div>
 <form method="post" name = "fm" id="fm">
   <input type="hidden" name="compId" id="compId" value="<c:out value="${complaintMap.COMP_ID}"/>"/>
   <input type="hidden" name="auditId" id="auditId" value="<c:out value="${detailMap.ID}"/>"/>
   <table width=100% border="0" align="center" cellpadding="1" cellspacing="1" class="table_query">
     <th colspan="7" align="left"><img class="nav" src="<%=contextPath%>/img/subNav.gif" /> 客户基本信息</th>
     <tr>
	    <td class="table_query_2Col_label_5Letter">联系人名称：</td>
	    <td><input type='text'  class="middle_txt" datatype="0,is_null,10" name="linkman"  id='linkmanId' value="<c:out value="${complaintMap.LINK_MAN}"/>"/></td>
	    <td class="table_query_2Col_label_5Letter">性别：</td>
	    <td><script type="text/javascript">
                genSelBoxExp("sex",<%=Constant.GENDER_TYPE%>,"<c:out value="${complaintMap.SEX}"/>",false,"short_sel","","false",'');
            </script>
	    </td>
	 </tr>
     <tr>
       <td class="table_query_2Col_label_5Letter">生日：</td>
	   <td><input  name="birthday"  id="t1" value="<c:out value="${complaintMap.BIRTHDAY}"/>" type="text" class="short_txt" datatype="1,is_date,10"  hasbtn="true" callFunction="showcalendar(event, 't1', false);"/></td>
       <td class="table_query_2Col_label_5Letter">年龄：</td>
       <td>
          <script type="text/javascript">
                genSelBoxExp("age",<%=Constant.AGE_TYPE%>,"<c:out value="${complaintMap.AGE}"/>",false,"short_sel","","false",'');
            </script>
       </td>
    </tr>
    <tr>
       <td class="table_query_2Col_label_5Letter">所属大区：</td>
       <td><input type="text"  class="middle_txt" name="ownOrgCode"  id="ownOrgCode" value="<c:out value="${complaintMap.ORG_CODE}"/>"/>
           <input name="ownOrgId" type="hidden" id="ownOrgId" value="<c:out value="${complaintMap.OWN_ORG_ID}"/>"/>
           <input name="ownOrg" type="button" class="mini_btn" onclick="showOrg('ownOrgCode' ,'ownOrgId' ,false,'')" value="&hellip;" /></td>
       <td class="table_query_2Col_label_5Letter">联系电话：</td>
       <td><input type="text"  class="middle_txt" datatype="0,is_phone,13" name="tel"  id='tel' value="<c:out value="${complaintMap.TEL}"/>"/></td>
    </tr>
    <tr>
      <td class="table_query_2Col_label_5Letter">省份：</td>
      <td><select class="min_sel" id="txt1" name="province" onchange="_genCity(this,'txt2')"></select> </td>
      <td class="table_query_2Col_label_5Letter">Email：</td>
      <td><input type="text"  class="middle_txt" name="email"  id="email" value="<c:out value="${complaintMap.E_MAIL}"/>"/></td>
    </tr>
    <tr>
      <td class="table_query_2Col_label_5Letter">地级市：</td>
      <td><select class="min_sel" id="txt2" name="city" onchange="_genCity(this,'txt3')"></select></td>  
      <td class="table_query_2Col_label_5Letter">邮编：</td>
      <td><input type="text"  class="middle_txt" name="zipCode"  id="zipCode" value="<c:out value="${complaintMap.ZIP_CODE}"/>"/></td>
     </tr>
     <tr>
     <td class="table_query_2Col_label_5Letter">区、县：</td>
     <td><select class="min_sel" id="txt3" name="district"></select> </td> 
     <td class="table_query_2Col_label_5Letter">投诉经销商：</td>
     <td><input class="middle_txt" id="compDealerCode" name="compDealerCode" value="<c:out value="${complaintMap.DEALER_CODE}"/>" type="text"/>
         <input class="mark_btn" type="button" value="&hellip;" onclick="showOrgDealer('compDealerCode','compDealerId','false','',true)"/> 
         <input type="hidden" name="compDealerId" id="compDealerId" value=""/>  
         <input type="button" name="clearBtn" class="cssbutton" value="清空" onclick="clrTxt('compDealerCode','compDealerId')"/>  
     </td>
     </tr>
     <tr>
      <td class="table_query_2Col_label_5Letter">家庭住址：</td>
      <td><textarea name="address" id="address"  style='border: 1px solid #94BBE2;width:70%;overflow: hidden;word-break:break-all;' rows="1"><c:out value="${complaintMap.ADDRESS}"/></textarea></td> 
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
      <TR class="table_list_row2">
      <TD><input type="text"  class="middle_txt" name="vin" onblur="oneVIN()" id="vin" maxlength="17" value="<c:out value="${complaintMap.VIN}" />"/></TD>
      <TD>
	  <input type="hidden"  class="middle_txt" name="modelCode"  id="modelCode" value="<c:out value="${complaintMap.MODEL_CODE}" />"/>
	  <input type="text"  class="middle_txt" name="modelName"  id="modelName" value="<c:out value="${complaintMap.GROUP_NAME}" />"/>
      </TD>
      <TD><input type="text"  class="middle_txt" name="engineNo"  id="engineNo" value="<c:out value="${complaintMap.ENGINE_NO}"/>"/></TD>
      <TD><input type="text"  class="middle_txt" name="licenseNo"  id="licenseNo" value="<c:out value="${complaintMap.LICENSE_NO}"/>"/></TD>
      <TD><input  name="purchasedDate"  id="t2" value="<c:out value="${complaintMap.PURCHASED_DATE}"/>" type="text" class="short_txt" datatype="1,is_date,10"  hasbtn="true" callFunction="showcalendar(event, 't2', false);"/></TD>
     </TR>
  </table>
  <br>
  <TABLE class="table_query">
		<th colspan="6" align="left"><img class="nav" src="<%=contextPath%>/img/subNav.gif" /> 投诉内容</th>
		<TR>
		  <TD><div align="right">投诉编号：</div></TD>
		  <TD><input type="hidden" name="compCode" id="compCode" value="<c:out value="${complaintMap.COMP_CODE}"/>"/></TD>
		  <TD><div align="left"><c:out value="${complaintMap.COMP_CODE}"/></div></TD>
	  	</TR>
		<TR>
		  <TD><div align="right">投诉时间：</div></TD>
		  <TD>&nbsp;</TD>
		  <TD><div align="left"><c:out value="${complaintMap.CREATE_DATE}"/></div></TD>
	  	</TR>
		<TR>
		  <TD><div align="right">投诉等级：</div></TD>
		  <TD>&nbsp;</TD>
		  <TD><div align="left">
		    <script type="text/javascript">
            	genSelBoxExp("compLevel",<%=Constant.COMP_LEVEL_TYPE%>,"<c:out value="${complaintMap.COMP_LEVEL}"/>",false,"short_sel","","false",'');
                </script>
		  </div></TD>
	    </TR>
		<TR>
		  <TD><div align="right">投诉类型：</div></TD>
		  <TD>&nbsp;</TD>
		  <TD><div align="left">
		    <script type="text/javascript">
            	genSelBoxExp("compType",<%=Constant.COMP_TYPE_TYPE%>,"<c:out value="${compType}"/>",false,"min_sel","onchange='selType()'","false",'');
            </script>
            <span id="compTypechng">
            </span>
		  </div></TD>
	  </TR>
	  <TR>
		  <TD><div align="right">投诉来源：</div></TD>
		  <TD>&nbsp;</TD>
		  <TD><div align="left">
		    <script type="text/javascript">
            	genSelBoxExp("compSource",<%=Constant.COMP_SOURCE_TYPE%>,"<c:out value="${complaintMap.COMP_SOURCE}"/>",false,"short_sel","","false",'<%=Constant.COMP_SOURCE_TYPE_01%>');
            </script>
		  </div></TD>
	  </TR>
	   <TR>
		  <TD><div align="right">客户投诉内容：</div></TD>
		  <TD>&nbsp;</TD>
		  <TD><textarea name="compContent"  style='border: 1px solid #94BBE2; width: 95%; overflow: hidden; word-break: break-all;'rows=5><c:out value="${complaintMap.COMP_CONTENT}"/></textarea></TD>
	  </TR>
	</TABLE>
	<br>
  <table class="table_query">
		<th colspan="6" align="left"><img class="nav" src="<%=contextPath%>/img/subNav.gif"/> 处理明细</th>
		<TR>
			<TD>
			<div align="right">处理时间：</div></TD>
			<TD>&nbsp;</TD>
			<TD><!--<c:out value="${detailMap.AUDIT_DATE}"/>--></TD>
		</TR>
		<TR>
			<TD>
			<div align="right">发生动作：</div></TD>
			<TD>&nbsp;</TD>
			<TD>
			<div align="left">
			<%-- 
			<%for(int i=0;i<listCheckBox.size();i++){ %>
			<%if(actionList!=null&&actionList.contains(listCheckBox.get(i).get("CODE_ID"))) {%>
				<input type="checkbox" name="auditAction" value="<%=listCheckBox.get(i).get("CODE_ID") %>" checked> <%=listCheckBox.get(i).get("CODE_DESC") %>&nbsp;&nbsp;
			<%}else{ %>
			  <input type="checkbox" name="auditAction" value="<%=listCheckBox.get(i).get("CODE_ID") %>"><%=listCheckBox.get(i).get("CODE_DESC") %>&nbsp;&nbsp;
			<%} %>
			<%} %>
			--%>
			<%for(int i=0;i<listCheckBox.size();i++){ %>
			<input type="checkbox" name="auditAction" value="<%=listCheckBox.get(i).get("CODE_ID") %>"><%=listCheckBox.get(i).get("CODE_DESC") %>&nbsp;&nbsp;
			<%} %>
			</div>			
			</TD>
		</TR>
		<TR>
			<TD>
			<div align="right">处理结果：</div></TD>
			<TD>&nbsp;</TD>
			<TD>
			<div align="left">
			<!-- 
			  <script type="text/javascript">
            	genSelBoxExp("auditResult",<%=Constant.AUDIT_RESULT_TYPE%>,"<c:out value="${detailMap.AUDIT_RESULT}"/>",true,"short_sel","onchange='selectAction()'","false",'');
              </script>
              -->
              <script type="text/javascript">
            	genSelBoxExp("auditResult",<%=Constant.AUDIT_RESULT_TYPE%>,"",true,"short_sel","onchange='selectAction()'","false",'');
              </script>
             <span id="partcode" style="display:none">备件编码：
              <input name="partCode" type="text" id="partCode"  class="middle_txt" datatype="0,is_null,15" value="<c:out value="${detailMap.PART_CODE}"/>"></span>
               <span id="supplier" style="display:none">上级保供单位：
              <input name="supplier" type="text" id="supplier"  class="middle_txt" datatype="0,is_null,20" value="<c:out value="${detailMap.SUPPLIER}"/>"></span>
			</div></TD>
		</TR>
		<TR>
			<TD>
			<div align="right">跟进结果描述：</div></TD>
			<TD>&nbsp;</TD>
			<TD>
			<!-- 
            	<textarea name="auditContent"
				style='border: 1px solid #94BBE2; width: 95%; overflow: hidden; word-break: break-all;'rows=5><c:out value="${detailMap.AUDIT_CONTENT}"/></textarea>
			-->
			<textarea name="auditContent"
				style='border: 1px solid #94BBE2; width: 95%; overflow: hidden; word-break: break-all;'rows=5></textarea>
			</TD>
			
		</TR>
        <TR>
		  <td colspan="3" align="center">
		  	 <input name="saveBtn" type="button" class="normal_btn" onclick="saveModify();" value="保存" />&nbsp;&nbsp;&nbsp;&nbsp;
		  	 <input type="checkbox" name="callBack" value="<%=Constant.IF_TYPE_NO%>"/>不需回访&nbsp;&nbsp;&nbsp;&nbsp;
			 <input name="cnfrmClsBtn" type="button" class="long_btn" onclick="confirmClose()" value="确认关闭投诉" /></td>
	    </TR>
  </table>
  <br>
    <table class="table_query">
	  <tr>
	    <th colspan="6" align="left"><img src="<%=contextPath%>/img/subNav.gif" alt="" class="nav" />
	      分配明细</th>
      </tr>
	  <tr>
	    <td><div align="center">区域
	      <input name="orgId" type="hidden" id="orgId"/>
          <input name="orgCode" type="text" id="orgCode" size="40" value=""/>
		  <input name="orgSel" type="button" class="mini_btn" onclick="showOrg('orgCode' ,'orgId' ,false,'')" value="&hellip;" />	
		</div></td>	
	    <td><input name="assign" type="button" class="normal_btn" onclick="assignDisposaltoArea();" value="分配" />
		&nbsp;&nbsp;&nbsp;&nbsp;</td>		
      </tr>
	</table>
	<br>
	 <TABLE class="table_list">
    <th colspan="8" align="left"><img class="nav" src="<%=contextPath%>/img/subNav.gif" /> 客户投诉处理历史</th>
	 <TR >
        <th>序号</th>
        <th>处理大区</th>
        <th>处理服务中心</th>
        <th>处理人</th>
        <th>处理时间</th>
        <th>发生动作</th>
        <th>联系结果</th>
        <th>联系结果描述</th>
    </TR>
   	   <%
   	   		List list = (List)request.getAttribute("detailList");
   	   		if(list.size()>0){
   	   			for(int i=0;i<list.size();i++){
   	   				Map map = (Map)list.get(i);
   	   	%>
   	   	 <tr class="table_list_row2">
      		<td><%=map.get("ROWNUM")==null?"":map.get("ROWNUM")%></td>
           	<td><%=map.get("ORG_NAME")==null?"":map.get("ORG_NAME")%></td>
           	<td><%=map.get("DEALER_NAME")==null?"":map.get("DEALER_NAME")%></td>
           	<td><%=map.get("NAME")==null?"":map.get("NAME")%></td>
            <td><%=map.get("AUDIT_DATE")==null?"":map.get("AUDIT_DATE")%></td>	
          	<td>
          		 <script type='text/javascript'>
          			writeItemValues('<%=map.get("AUDIT_ACTION")%>')
				</script>
			</td>
          	<td>
          		<%
          			if (map.get("AUDIT_RESULT") != null) {
          				if (String.valueOf(map.get("AUDIT_RESULT")).equals(String.valueOf(Constant.AUDIT_RESULT_TYPE_02)) || 
          					String.valueOf(map.get("AUDIT_RESULT")).equals(String.valueOf(Constant.AUDIT_RESULT_TYPE_03))) {
          		%>
          					<a href="#" onclick="showPart('<%=map.get("PART_CODE")%>', '<%=map.get("SUPPLIER")%>');">
          						<script type='text/javascript'>
          							writeItemValue(<%=map.get("AUDIT_RESULT")%>)
          						</script>
          					</a>
          		<% 
          				} else {
          		%>
          					<script type='text/javascript'>
          							writeItemValue(<%=map.get("AUDIT_RESULT")%>)
          					</script>
          		<%
          				}
          			}
          		 %>
			</td>
          	<td title="<%=map.get("AUDIT_CONTENT")%>">&nbsp;
          		<a href="#" onclick="showAllMsg('<%=map.get("ID")%>');">
          		<%
          			if(map.get("AUDIT_CONTENT")!=null){
          				if(String.valueOf(map.get("AUDIT_CONTENT")).length()<=10){
          		%>
          			<%=map.get("AUDIT_CONTENT")%>
          		<%
          				}
          		%>
          		<%
          				if(String.valueOf(map.get("AUDIT_CONTENT")).length()>10){
          					String s = String.valueOf(map.get("AUDIT_CONTENT"));
          					s = s.substring(0,9);
          		%>
          			<%=s%>...
          		<%
          				}
          			}
          		%>
          		</a>
          	</td>
       	</tr>
   	   	<%		
   	   			}
   	   		}
   	    %>
    </TABLE>
    <br>
    <TABLE class="table_query">
	<TR><TD align="center">
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
function showAllMsg(value){
	var url = '<%=contextPath%>/customerRelationships/search/ComplaintSearch/showAllMsg.do?value='+value ;
	OpenHtmlWindow(url,440,300);
}

function showPart(partCode, supplier) {
	var url = '<%=contextPath%>/customerRelationships/search/ComplaintSearch/showPart.do?partCode='+partCode+'&supplier='+supplier ;
	OpenHtmlWindow(url,440,300);
}

// 保存新增信息
function saveModify(){
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
	
  if(submitForm('fm')){
	fm.action = "<%=contextPath%>/customerRelationships/complaint/ComplaintDisposalOEM/saveModify.do?";
    fm.submit();
  }
}

// 确认关闭

function confirmClose()
{
	MyConfirm("确认关闭吗？", complaintCloseToDo);
}

function complaintCloseToDo()
{
	if(submitForm('fm'))
	{
		fm.action = "<%=contextPath%>/customerRelationships/complaint/ComplaintDisposalOEM/closeComplaint.do?flag=1";
    	fm.submit();
    }
}

// 分配给区域	
function assignDisposaltoArea(){
	var orgId = document.getElementById("orgId").value;
	if (!orgId) {
		MyAlert('请选择区域');
		return;
	}
	fm.action = "<%=contextPath%>/customerRelationships/complaint/ComplaintDisposalOEM/assignComplaint.do?flag=1";
    fm.submit();
}

//清空
function clrTxt(value1,value2){
	document.getElementById(value1).value = "";
	document.getElementById(value2).value = "";

}

//动态的回显投诉类型的大类和小类
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
// 通过vin带出其他车辆信息
function showVIN()
{
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
<!--页面列表 end -->
</body>
</html>
