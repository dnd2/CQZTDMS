<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="com.infodms.dms.util.CommonUtils"%>
<%@page import="com.infodms.dms.po.FsFileuploadPO"%>
<%
	String contextPath = request.getContextPath();
	Map  dealer = (Map)request.getAttribute("DEALERHM");//经销商信息
	HashMap hm = (HashMap)request.getAttribute("FOREAPPROVAL_HASHMAP");//预授权信息
	//预授权明细
	List items = (List)request.getAttribute("FOREAPPROVALITEM_LIST");
	//附近列表
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
//展示附件
   function addUploadRowByDb1(filename,fileId,fileUrl){
 	var tab =document.getElementById("fileUploadTab");
 	var row =  tab.insertRow();
    row.className='table_list_row1';
    row.insertCell();
    row.insertCell();
    row.cells(0).innerHTML = "<a  target='_blank' href='"+fileUrl+"'/>"+filename+"</a><input type='hidden' value='"+fileId+"' name='uploadFileId' /> "+"<input type='hidden' value='"+filename+"' name='uploadFileName' /> ";
    row.cells(1).innerHTML = "<input disabled type=button onclick='delUploadFile(this)' class='normal_btn' value='删 除' />";    
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
    <table class="table_edit">
       <tr>
         <th colspan="6"><img class="nav" src="<%=contextPath%>/img/subNav.gif" /> 基本信息</th>
       </tr>
       <tr>
         <td class="table_edit_3Col_label_6Letter">经销商代码：</td>
         <td><%=dealer.get("DEALER_CODE")==null ? "":dealer.get("DEALER_CODE").toString() %></td>
         <td class="table_edit_3Col_label_5Letter">经销商名称：</td>
         <td ><%=dealer.get("DEALER_SHORTNAME")==null ? "":dealer.get("DEALER_SHORTNAME").toString() %></td>
         <td class="table_edit_3Col_label_5Letter">维修工单号：</td>
         <td >
				<%=hm.get("RO_NO")==null ? "" : hm.get("RO_NO") %>
         </td>
       </tr>
       <tr>
         <td class="table_edit_3Col_label_6Letter">保修开始日期：</td>
         <td>
         	<%=hm.get("KEEP_BEG_DATE")==null ? "" : hm.get("KEEP_BEG_DATE").toString().substring(0,10) %>
         </td>
         <td class="table_edit_3Col_label_5Letter">进厂日期：</td>
         <td>
         	 <%=hm.get("IN_FACTORY_DATE")==null ? "" : hm.get("IN_FACTORY_DATE").toString().substring(0,10) %>
         </td>
         <td class="table_edit_3Col_label_5Letter">进厂里程数：</td>
         <td>
         	<%=hm.get("IN_MILEAGE")==null ? "" : hm.get("IN_MILEAGE") %>
         </td>
       </tr>
       <tr>
         <td class="table_edit_3Col_label_6Letter">接待员：</td>
         <td >
         <%=hm.get("DEST_CLERK")==null ? "" : hm.get("DEST_CLERK") %>
         </td>
         <td class="table_edit_3Col_label_5Letter">&nbsp;</td>
         <td >&nbsp;</td>
         <td class="table_edit_3Col_label_5Letter">&nbsp;</td>
         <td   >&nbsp;</td>
       </tr>
       <tr>
         <td class="table_edit_3Col_label_6Letter">申请日期：</td>
         <td>
         <%=hm.get("APPROVAL_DATE")==null ? "" : hm.get("APPROVAL_DATE").toString().substring(0,10) %>
         </td>
         <td class="table_edit_3Col_label_5Letter">申请人：</td>
         <td>
         	<%=hm.get("APPROVAL_PERSON")==null ? "" : hm.get("APPROVAL_PERSON") %>
         </td>
         <td class="table_edit_3Col_label_5Letter">联系电话：</td>
         <td>
         <%=hm.get("APPROVAL_PHONE")==null ? "" : hm.get("APPROVAL_PHONE") %>
         </td>
       </tr>  
       <tr>
         <td class="table_edit_3Col_label_6Letter">外出时间：</td>
         <td>
         <%=hm.get("OUT_DATE")==null ? "" : hm.get("OUT_DATE").toString().substring(0,10) %>
         </td>
         <td class="table_edit_3Col_label_5Letter">外出人：</td>
         <td>
         	<%=hm.get("OUT_PERSON")==null ? "" : hm.get("OUT_PERSON") %>
         </td>
         <td class="table_edit_3Col_label_5Letter">外出费用：</td>
         <td>
         <%=hm.get("OUT_DATE")==null ? "" : hm.get("OUT_DATE") %>
         </td>
       </tr>            
       <tr>
         <td class="table_edit_3Col_label_6Letter">VIN：</td>
         <td>
         <%=hm.get("VIN")==null ? "" : hm.get("VIN") %>
         </td>
         <td class="table_edit_3Col_label_5Letter">牌照号：</td>
         <td align="left" id="LICENSE_NO"><%=hm.get("LICENSE_NO")==null ? "" : hm.get("LICENSE_NO") %></td>
         <td class="table_edit_3Col_label_5Letter">发动机号：</td>
         <td align="left" id="ENGINE_NO"><%=hm.get("ENGINE_NO")==null ? "" : hm.get("ENGINE_NO") %></td>
       </tr>
       <tr>
         <td class="table_edit_3Col_label_6Letter">品牌：</td>
         <td align="left" id="BAND_NAME">长安</td>
         <td class="table_edit_3Col_label_5Letter">车系：</td>
         <td align="left" id="SERIES_NAME"><%=hm.get("SERIES_NAME")==null ? "" : hm.get("SERIES_NAME") %></td>
         <td class="table_edit_3Col_label_5Letter">车型：</td>
         <td align="left" id="MODEL_NAME"><%=hm.get("MODEL_NAME")==null ? "" : hm.get("MODEL_NAME") %></td>
       </tr>
    </table>


     <table  class="table_list" style="border-bottom:1px solid #DAE0EE" >
         <th colspan="6"  align="left" ><img class="nav" src="<%=contextPath%>/img/subNav.gif" /> 项目信息 
         </th>         
     <tbody id="tb1">       
         <th >序号</th>
         <th >项目类型</th>
         <th >项目代码</th>
         <th >项目名称</th>
         <th >故障描述及维修方案</th>
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
		<%= tempItem.get("DEALER_REMARK") == null ? "":tempItem.get("DEALER_REMARK")%>
<%--			<textarea  name="DEALER_REMARK"  datatype="1,is_textarea,200"  rows="1" cols="30" readonly="readonly"></textarea>--%>
		</td>
    </tr>
    <%}%> 			      
       </tbody>
     </table>
<!-- 添加附件 -->
	<table class="table_info" border="0" id="file">
		<input type="hidden" id="fjids" name="fjids"/>
	    <tr colspan="8">
	        <th>
			<img class="nav" src="../../../img/subNav.gif" />
			&nbsp;附件列表：
			</th>
		</tr>
		<tr>
    				<td width="100%" colspan="2"><jsp:include page="${contextPath}/uploadDiv.jsp" /></td>
  			</tr>
  			<%for(int i=0;i<attachLs.size();i++) { %>
  			<script type="text/javascript">
  			addUploadRowByDb1('<%=CommonUtils.checkNull(attachLs.get(i).getFilename()) %>','<%=CommonUtils.checkNull(attachLs.get(i).getFjid()) %>','<%=CommonUtils.checkNull(attachLs.get(i).getFileurl())%>');
  			</script>
  			<%} %>
		</table>     
     <table class="table_add">
		<tr> 
            <td align=center>
				<input type="button" onclick="_hide();" class="normal_btn"  value="关闭"/>
		    </td>
		</tr>
	</table>
</form>
</body>
</html>