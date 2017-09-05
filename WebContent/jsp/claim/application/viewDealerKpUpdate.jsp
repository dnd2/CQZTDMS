<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@page import="java.util.List"%>
<%@page import="java.util.LinkedList"%>
<%@page import="com.infodms.dms.po.FsFileuploadPO"%>
<%@page import="com.infodms.dms.util.CommonUtils"%>
<%
	String contextPath = request.getContextPath();
	List<FsFileuploadPO> attachLs = (LinkedList<FsFileuploadPO>)request.getAttribute("attachLs");
%>
<%@page import="com.infodms.dms.common.Constant"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	<title>结算单管理</title>
	<script type="text/javascript">
	    function doInit()
		{
		   loadcalendar();
		}
	</script>
</head>
<title>开票单位变更申请</title>

<body>
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />
         当前位置：售后服务管理&gt;索赔结算管理&gt;新增开票单位变更申请
</div>
		<form method="post" name="fm" id="fm">
			<table   class="table_query">
				<th colspan="6">
					<img class="nav" src="../../../img/subNav.gif" />
					单据信息
				</th>
				<tr>
					<td class="table_edit_2Col_label_7Letter" nowrap>
						结算单号：
					</td>
					<td align="left" nowrap="nowrap">
						<input type="text" class="middle_txt" name="BALANCE_NO"  value ="${list.BALANCE_NO}" id="BALANCE_NO" disabled/>
						<input type="hidden" name="id" id="id" value="${list.ID}"/>
						<input type="hidden" name="did" id="did" VALUE="${list.DELAERID }"/>
						<input type="hidden" name="yilidname" id="yilidname" value="${list.YIELDLY}"/>
					</td>
					<td class="table_edit_2Col_label_7Letter" nowrap="nowrap" nowrap>
						经销商代码：
					</td>
					<td align="left" >
						<input type="text" name="dealerCode" id="dealerCode" value="${list.DEALERCODE }" disabled 
							class="middle_txt" />
					</td>
						<td class="table_edit_2Col_label_7Letter" nowrap="nowrap" nowrap>
						经销商名称：
					</td>
					<td align="left" > 
					<input type="text" name="dealerName" id="dealerName"  value="${list.DEALERNAME }" readonly
							class="middle_txt" disabled/>
					</td>
				</tr>
				<tr>
					<td class="table_edit_2Col_label_7Letter" nowrap>
					 基地：
					</td>
					<td align="left" id="yilid">
						<script type="text/javascript">
						 document.write(getItemValue(${list.YIELDLY}));
		   				 </script>
		   				 
					</td>
					<td class="table_edit_2Col_label_7Letter" nowrap>
					 结算月份：
					</td>
					<td align="left" >
					<input type="text" name="mouths" id="mouths"  value = "${list.INVOICE_DATE } "  readonly
							class="middle_txt" disabled/>	
					</td>
				<td class="table_edit_2Col_label_7Letter" nowrap>
					 原开票单位：
					</td>
					<td align="left" >
					<input type="text" name="oldDealerName" id="oldDealerName" value="${list.INVOICE_MARK }" readonly
							class="middle_txt" disabled/>	
					</td>
				
				</tr>
				<tr>
					<td class="table_edit_2Col_label_7Letter" nowrap>
						当前开票单位代码：
					</td>
					<td><input class="middle_txt" type="text" value ="${list.NEW_INVOICE_CODE }" disabled/></td>
				    <td class="table_edit_2Col_label_7Letter" nowrap>
						当前开票单位：
					</td>
				
			 		<td align="left" nowrap="nowrap">
					<input class="middle_txt" id="dealerCodeNew"  name="dealerCodeNew" type="text" value ="${list.NEW_INVOICE_MARK }" disabled/>
					<input class="middle_txt" id="dealerId"  name="dealerIdNew" type="hidden" value="${list.NEW_INVOICE_ID }"/>
					</td>
					<td></td>
				</tr>
				
				<th colspan="6" nowrap>
					<img class="nav" src="../../../img/subNav.gif" />
					申请内容
				</th>
				</table>
				<table class="table_query">
				<tr>
					<td class="table_edit_2Col_label_7Letter">申请内容：</td>
					<td>
						<textarea rows="6" cols="70" id="remark" name="remark" disabled>${list.REMARK }</textarea>
					</td>
				</tr>
				
				
			</table>
			<table class="table_query">
				<tr>
				<th colspan="6" nowrap>
					<img class="nav" src="../../../img/subNav.gif" />
					审核内容
				</th>
				</tr>
			</table>
			<table class="table_query">
				<tr>
					<td class="table_edit_2Col_label_7Letter">审核内容：</td>
					<td>
						<textarea rows="6" cols="70" id="auditingRemark" name="auditingRemark" disabled>${list.AUDITING_REMARK}</textarea>
					</td>
				</tr>
				
			</table>
		<table class="table_edit" border="0" id="file">
		    <tr>
		        <th>
				<img class="nav" src="<%=contextPath%>/img/subNav.gif" />
				附件信息
				</th>
			</tr>
		</table>
		<table class="table_edit" id="attachTabId" style="">
			<% if(attachLs!=null && attachLs.size()>0) {%>
			<tr>
	    		<td width="100%" colspan="2"><jsp:include page="${contextPath}/uploadDiv.jsp" /></td>
	  		</tr>
	  			<%for(int i=0;i<attachLs.size();i++) { %>
	  			<script type="text/javascript">
	  			addUploadRowByDL('<%=CommonUtils.checkNull(attachLs.get(i).getFilename()) %>','<%=CommonUtils.checkNull(attachLs.get(i).getFjid()) %>','<%=CommonUtils.checkNull(attachLs.get(i).getFileurl())%>');
	  			</script>
	  			<%} %>
	  		<%} else { %>
	  			<tr>
	  				<td>&nbsp;未上传附件</td>
	  			</tr>
	  		<%} %>
		</table>
			
				<table border="0" cellspacing="0" cellpadding="0" class="table_edit">
				<tr>
					
					<td align="center">
						<input type="button" onClick="_hide();" class="normal_btn"
							style="" value="关闭" />
					</td>
					
				</tr>
			</table>
		</form>	
<script type="text/javascript">

function showNotice(){
	var url = '<%=contextPath%>/claim/laborlist/LaborListForTax/noticeUrlInit.do' ;
	OpenHtmlWindow(url,800,500);
}
function setNotic(id,ro_no,did,dcode,dname,maker,yieldly,sd,ed,status){
	$('BALANCE_NO').value = ro_no ;
	$('did').value=did
	$('dealerCode').value = dcode;
	$('dealerName').value = dname;
	$('yilid').innerHTML=getItemValue(yieldly);
	$('yilidname').value=yieldly;
	$('mouths').value = sd+"至"+ed;
	$('oldDealerName').value = maker;
}	
function wrapOut(){
	setNotic('','','','','','','','','','');
}


function confirmAdd(){
	if($('BALANCE_NO').value=="") {

		MyAlert("必须选择开票单号!")
		return;
		};
		if($('remark').value==""){
			MyAlert("申请备注必填!")
			return;

		};
		if($('dealerCodeNew').value==""){
			MyAlert("当前开票单位必选!")
			return;

		};
	makeNomalFormCall("<%=contextPath%>/claim/application/DealerNewKpUpdate/modifyDealerKpSave.json",showForwordValue,'fm','queryBtn');
	
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
function showForwordValue(json){
	if(json.success=='true'){
		MyAlert("保存成功");
	}else{
		MyAlert("保存失败");	
	}

	
}
</script>
</body>
</html>