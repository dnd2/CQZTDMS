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
			<table   class="table_edit">
				<th colspan="6">
					<img class="nav" src="../../../img/subNav.gif" />
					单据信息
				</th>
				<tr>
					<td class="table_edit_2Col_label_7Letter">
						结算单号：
					</td>
					<td align="left" nowrap="nowrap">
						<input type="text" class="middle_txt"  name="BALANCE_NO" id="BALANCE_NO"  readOnly/>
						<span style="color:red">*</span>
						<input type="button" value="..." class="mini_btn" onclick="showNoticeChange();"/>
						<input type="hidden" name="id" id="id"/>
						<input type="hidden" name="did" id="did"/>
					</td>
					<td class="table_edit_2Col_label_7Letter" nowrap="nowrap">
						经销商代码：
					</td>
					<td align="left" >
						<input type="text" name="dealerCode" id="dealerCode" readonly
							class="middle_txt" />
					</td>
						<td class="table_edit_2Col_label_7Letter" nowrap="nowrap">
						经销商名称：
					</td>
					<td align="left" > 
					<input type="text" name="dealerName" id="dealerName" readonly
							class="middle_txt" />
					</td>
				</tr>
				<tr>
					<td class="table_edit_2Col_label_7Letter">
					 基地：
					</td>
					<td align="left" >
					<input type="text" name="yilid" id="yilid" readonly 
							class="middle_txt" />	
						<input type="hidden" name="yilidname" id="yilidname" 
							class="middle_txt" />			
					
				
					</td>
					<td class="table_edit_2Col_label_7Letter">
					 结算月份：
					</td>
					<td align="left" >
					<input type="text" name="mouths" id="mouths" readonly
							class="middle_txt" />	
					</td>
					<td class="table_edit_2Col_label_7Letter">
					 原开票单位：
					</td>
					<td align="left" >
					<input type="text" name="oldDealerName" id="oldDealerName" readonly
							class="middle_txt" />	
					</td>
					
				</tr>
				<tr>
					 <td class="table_edit_2Col_label_7Letter" nowrap>
						当前开票单位代码：
					</td>
					<td><input class="middle_txt" id="dealerC"  name="dealerC" type="text"  readonly/></td>
				    <td class="table_edit_2Col_label_7Letter">
						当前开票单位：
					</td>
				
			 		<td align="left" nowrap="nowrap">
					<input class="middle_txt" id="dealerCodeNew"  name="dealerCodeNew" type="text" readonly/>
					<input class="middle_txt" id="dealerId"  name="dealerIdNew" type="hidden"/>
		            <input name="showBtn" type="button" class="mini_btn" style="cursor: pointer;" onclick="showOrgDealerNew('dealerC','dealerId','true','',true,'','','dealerCodeNew');" value="..." />        
					</td>
					<td></td>
				</tr>
				<th colspan="6">
					<img class="nav" src="../../../img/subNav.gif" />
					申请内容
				</th>
				</table>
				<table class="table_query">
				<tr>
					<td class="table_edit_2Col_label_7Letter">申请内容：</td>
					<td class="tbwhite">
						<textarea rows="6" cols="70" id="remark" name="remark" datatype="0,is_textarea,1000"></textarea>
					</td>
					<td></td>
				</tr>
				
				
			</table>
			
				<!-- 添加附件 -->
			<table class="table_info" border="0" id="file">
				<input type="hidden" id="fjids" name="fjids" />
				<tr colspan="8">
					<th>
						<img class="nav" src="../../../img/subNav.gif" />
						&nbsp;附件列表：
					</th>
					<th>
						<span align="left"><input type="button" class="normal_btn"
								onclick="showUpload('<%=contextPath%>')" value='添加附件' /> </span>
					</th>
				</tr>
				<tr>
					<td width="100%" colspan="2"><jsp:include
							page="${contextPath}/uploadDiv.jsp" /></td>
				</tr>
				<tr>
					<td>
					<span style="color:red">该单用于更改[开票通知单]上的开票单位名称，请如实选择“当前开票单位”。如果选不出结算单，可能是已经做了应税劳务清单参数设置，请先发email要求系统室删除。</span>
					</td>
				</tr>
			</table>
			
			
				<table border="0" cellspacing="0" cellpadding="0" class="table_edit">
				<tr>
					
					<td align="center">
						<input type="button" onClick="confirmAdd();" class="normal_btn" id="butADD"
							 value="确定" />
						<input type="button" onClick="goBack();" class="normal_btn"
							 value="返回" />
					</td>
					
				</tr>
			</table>
		</form>	
<script type="text/javascript">

function showNoticeChange(){
	var url = '<%=contextPath%>/claim/laborlist/LaborListForTax/noticeChangeUrlInit.do' ;
	OpenHtmlWindow(url,800,500);
}
function setNotic(id,ro_no,did,dcode,dname,maker,yieldly,sd,ed,status){
	$('BALANCE_NO').value = ro_no ;
	$('did').value=did
	$('dealerCode').value = dcode;
	$('dealerName').value = dname;
	$('yilid').value=getItemValue(yieldly);
	$('yilidname').value=yieldly;
	$('mouths').value = sd+"至"+ed;
	$('oldDealerName').value = maker;
}	
function wrapOut(){
	setNotic('','','','','','','','','','');
}
function goBack(){
	fm.action="<%=contextPath%>/claim/application/DealerNewKpUpdate/mainUrl.do";
	fm.submit();
	
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
	makeNomalFormCall("<%=contextPath%>/claim/application/DealerNewKpUpdate/addDealerKpSave.json",showForwordValue,'fm','queryBtn');
	
}
function showForwordValue(json){
	if(json.success=='true'){
			fm.action="<%=contextPath%>/claim/application/DealerNewKpUpdate/mainUrl.do";
			fm.submit();	
	}else{
		MyAlert("保存失败");	
	}

	
}
</script>
</body>
</html>