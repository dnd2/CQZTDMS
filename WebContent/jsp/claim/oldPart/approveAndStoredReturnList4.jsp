<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.Constant"%>
<%@ page import="com.infoservice.mvc.context.ActionContext"%>
<%@ page import="com.infodms.dms.bean.AclUserBean"%>
<%@ page import="com.infodms.dms.po.FsFileuploadPO"%>
<%@page import="java.util.LinkedList"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@page
	import="com.infodms.dms.bean.ClaimApproveAndStoredReturnInfoBean"%>
<%@page import="com.infodms.dms.bean.TtAsWrOldPartSignDetailListBean"%>
<%@page import="java.util.List"%>

<%@page import="com.infodms.dms.util.CommonUtils"%>
<%@page import="java.util.Map"%><html
	xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>索赔旧件审批入库(批量入库)</title>
<%
	String contextPath = request.getContextPath();
	ActionContext act = ActionContext.getContext();
	AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
	String logonName = logonUser.getName();
	ClaimApproveAndStoredReturnInfoBean detailBean = (ClaimApproveAndStoredReturnInfoBean) request.getAttribute("returnListBean");
	@SuppressWarnings("unchecked")
	List<Map<String, Object>> detailList = (List) request.getAttribute("detailList");
	List<Map<String, Object>> detailList1 = (List) request.getAttribute("detailList1");
%>
<script type="text/javascript">
 	//索赔明细ID
	function hiddenDetailId(id){
	   var res = String.format("<input type='hidden' name='orderIds' value='" + id + "' />");
	   document.write(res);
	}
	
	//选中预检查
	function preChecked() {
        var i=validateSelectedId();
        if(i==1){
        	MyConfirm("确认签收？",sign,[]);
        }
	}
	//格式化日期
	function formatDate(value) {
		 if (value==""||value==null) {
			document.write("");
		 }else {
			document.write(value.substr(0,10));
		 }
	}
	//签收数量编辑
	function editSignNum(value,id){
		var res = String.format("<input  disabled=\"disabled\" type=\"text\" id=\"signNum"+id+"\" name=\"signNum"+id+"\" class=\"short_txt\" datatype='0,is_digit,10' value=\"" + value + "\"/>");
		//var res = String.format(value);
		document.write(res);
	}
	//配件名称,用于校验弹出名称提示
	function hiddenPartName(value,id){
		var res = String.format(value+"<input type=\"hidden\" id=\"partName"+id+"\" name=\"partName"+id+"\" value=\"" + value + "\"/>");
		document.write(res);
	}
	//隐藏需回运数量
	function hiddenReturnAmount(id,prototype_value){
		var res = String.format(prototype_value+"<input type=\"hidden\" id=\"returnAmount"+id+"\" name=\"returnAmount"+id+"\" value=\"" + prototype_value + "\"/>");
		document.write(res);
	}
	//库区编辑
	function editWrHouse(value,id){
		var res = String.format("<input type=\"text\" onblur=\"isCheck123('"+id+"',this.value);\" id=\"wrHouse"+id+"\" name=\"wrHouse"+id+"\" class=\"short_txt\"  value=\"" + value + "\" />"+"<input type=\"hidden\" id=\"wrHouse1"+id+"\" name=\"wrHouse1"+id+"\" value=\"" + value + "\" />");
		document.write(res);
	}
	function isCheck123(id,value){
		var wrHouse1 =  document.getElementById('wrHouse1'+id).value;//库区(隐藏域)
		if(value!=wrHouse1){
			var url="<%=contextPath%>/claim/oldPart/ClaimOldPartApporoveStorageManager/approveAndStored44.json?id="+id+"&wrHouse="+value;
			makeNomalFormCall(url,afterCall111,'fm','');
		}
	}
	function afterCall111(json){
		var retCode=json.updateResult;
	    if(retCode!=null&&retCode!=''){
	      if(retCode=="updateSuccess"){
	    	//MyAlert("签收成功!");
	   	  }
	    }
	}
	
	//抵扣原因编辑
	function editDeductCon(value,id){
		var str=genSelBoxStrExp("deduct"+id,<%=Constant.OLDPART_DEDUCT_TYPE%>,value,true,"","disabled=disabled","true",'');
		var res = String.format(str);
		document.write(res);
	}
	
	//签收操作
	function sign(){
		$('qianshou').disabled="disabled";
		fm.i_back_id.value='<%=detailBean.getId()%>';
		var str="?";
		var flag=0;
		str+="return_ord_id="+'<%=detailBean.getId()%>';
		var url= "<%=contextPath%>/claim/oldPart/ClaimOldPartApporoveStorageManager/approveAndStored222.json"+str;
		makeNomalFormCall(url,afterCall,'fm','createOrdBtn');
	}

	//zhumingwei 2011-8-11
	function detail(){
		window.open("<%=contextPath%>/claim/oldPart/ClaimOldPartApporoveStorageManager/detail.do?return_id=<%=detailBean.getId()%>");
		//var tarUrl = "<%=contextPath%>/claim/oldPart/ClaimOldPartApporoveStorageManager/detail.do?return_id=<%=detailBean.getId()%>";
		//var width=900;
		//var height=500;
		//var screenW = window.screen.width-30;	
		//var screenH = document.viewport.getHeight();

		//if(screenW!=null && screenW!='undefined')
			//width = screenW;
		//if(screenH!=null && screenH!='undefined')
			//height = screenH;
		//OpenHtmlWindow(tarUrl,width,height);
	}
	
	//签收回调处理
	function afterCall(json){
		var retCode=json.updateResult;
	    if(retCode!=null&&retCode!=''){
	      if(retCode=="updateSuccess"){
	    	MyAlert("签收成功!");
	    	backTo();
	      }else if(retCode=="updateFailure"){
	    	MyAlert("签收失败!");
	     }
	   }
	}
	
	//点击签收动作，只对勾选的入库配件进行数量和抵扣原因校验
	function validateSelectedId(){
		var retCode=1;
		var selectArr=document.getElementsByName('orderIds');
		for(var i=0;i<selectArr.length;i++){
			var detailId = selectArr[i].value;
			var requiredNum=document.getElementById("returnAmount"+detailId).value;
			var backNum=document.getElementById("signNum"+detailId).value;
			var deductReason=document.getElementById("deduct"+detailId).value;
			var partName=document.getElementById("partName"+detailId).value;
			var indexNum = $('INDEX'+detailId).value;
			var diffNum=requiredNum-backNum;
			if(backNum==null||backNum==''){
				parent.window.MyAlert("序号：" + indexNum + " 配件名称'"+partName+"'的签收数不能为空！");
				retCode=0;
				break;
			}
			if(diffNum<0){
				parent.window.MyAlert("序号：" + indexNum + " 配件名称'"+partName+"'的签收数不能超过回运数！");
				retCode=0;
				break;
			}
			if(diffNum>0&&deductReason==''){
				parent.window.MyAlert("序号：" + indexNum + " 配件名称'"+partName+"'的签收存在数量差异，请选择抵扣原因！");
				retCode=0;
				break;
			}
			if(diffNum==0&&deductReason>0){
				parent.window.MyAlert("序号：" + indexNum + " 配件名称'"+partName+"'的签收数量并没有差异，无需选择抵扣原因！");
				retCode=0;
				break;
			}
		}
		return retCode;
	}

	function backTo(){
		//location.href = "<%=contextPath%>/claim/oldPart/ClaimOldPartApporoveStorageManager/queryListPage11.do";
		fm.action="<%=contextPath%>/claim/oldPart/ClaimOldPartApporoveStorageManager/queryListPage11.do?isReturn=1";
	    fm.method="post";
	    fm.submit();
	}

	function isCheck(id){
		var boxNo=document.getElementById("boxNo").value;
		var CLAIM_ID=document.getElementById("CLAIM_ID").value;
		//location.href = "<%=contextPath%>/claim/oldPart/ClaimOldPartApporoveStorageManager/goApproveAndStoredPage8.do?CLAIM_ID="+CLAIM_ID+"&boxNo="+boxNo;
		fm.action="<%=contextPath%>/claim/oldPart/ClaimOldPartApporoveStorageManager/goApproveAndStoredPage8.do?CLAIM_ID="+CLAIM_ID+"&boxNo="+boxNo+"&isReturn=1";
	    fm.method="post";
	    fm.submit();
	}
</script>
</head>
<body >
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置：售后服务管理&gt;索赔旧件管理&gt;索赔旧件审批入库</div>
<form method="post" name="fm" id="fm"><input type="hidden"
	name="i_back_id" id="i_back_id" value="" />
	<input type="hidden" name="dealerCode" value="${dealerCode }" />
	<input type="hidden" name="dealerName" value="${dealerName }" />
	<input type="hidden" name="back_order_no" value="${back_order_no }" />
	<input type="hidden" name="yieldly" value="${yieldly }" />
	<input type="hidden" name="freight_type" value="${freight_type }" />
	<input type="hidden" name="create_start_date" value="${create_start_date }" />
	<input type="hidden" name="create_end_date" value="${create_end_date }" />
	<input type="hidden" name="report_start_date" value="${report_start_date }" />
	<input type="hidden" name="report_end_date" value="${report_end_date }" />
	<input type="hidden" name="back_type" value="${back_type }" />
	<input type="hidden" name="trans_no" value="${trans_no }" />
<table class="table_edit">
	<tr>
		<th colspan="6"><img class="nav"
			src="<%=contextPath%>/img/subNav.gif" />基本信息</th>
	</tr>
	<tr bgcolor="F3F4F8">
		<td align="right">经销商代码：</td>
		<td><%=CommonUtils.checkNull(detailBean.getDealer_code())%></td>
		<td align="right">经销商名称：</td>
		<td><%=CommonUtils.checkNull(detailBean.getDealer_name())%></td>
		<td align="right">所属区域：</td>
		<td><%=CommonUtils.checkNull(detailBean.getAttach_area())%></td>
	</tr>
	<tr bgcolor="F3F4F8">
		<td align="right">回运清单号：</td>
		<td><%=CommonUtils.checkNull(detailBean.getReturn_no())%></td>
		<td align="right">提报日期：</td>
		<td><%=CommonUtils.checkNull(detailBean.getReturn_date())%></td>
		<td align="right">建单日期：</td>
		<td><%=CommonUtils.checkNull(detailBean.getCreate_date())%></td>
	</tr>
	<tr bgcolor="F3F4F8">
		<td align="right">索赔申请单数：</td>
		<td><%=CommonUtils.checkNull(detailBean.getWr_amount())%></td>
		<td align="right">配件项数：</td>
		<td><%=CommonUtils.checkNull(detailBean.getPart_item_amount())%>
		</td>
		<td align="right">配件数：</td>
		<td><%=CommonUtils.checkNull(detailBean.getPart_amount())%></td>
	</tr>
	<tr bgcolor="F3F4F8">
		<td align="right">货运方式：</td>
		<td><%=CommonUtils.checkNull(detailBean.getTransport_desc())%></td>
		<td align="right">回运类型：</td>
		<td><%=CommonUtils.checkNull(detailBean.getReturn_desc())%></td>
		<td align="right">装箱总数：</td>
		<td><%=CommonUtils.checkNull(detailBean.getParkage_amount())%></td>
	</tr>

	<tr bgcolor="F3F4F8">
		<td align="right">三包员电话：</td>
		<td colspan="5"><%=CommonUtils.checkNull(detailBean.getTel())%></td>
	</tr>
	
	<tr bgcolor="F3F4F8">
		<td align="right">旧件回运起止时间：</td>
		<td colspan="5">${time }</td>
	</tr>
	<tr bgcolor="F3F4F8">
		<td align="right">运费：</td>
		<td colspan="5">
			${price_con }
			<input name="price" type="hidden" value="${price_con }" />
			<input name="price1" type="hidden" value="${price_con1 }" />
			<%//if(detailBean.getStatus().intValue()==Constant.BACK_LIST_STATUS_02.intValue()){%>
				<!--<input name="price" type="text" value="${price_con }" />-->
				<!--<input name="price1" type="hidden" value="${price_con }" />-->
			<%//}else{%>
				<!-- ${price_con } -->
			<%//}%>
		</td>
	</tr>
	<tr bgcolor="F3F4F8">
		<td align="right">备注：</td>
		<td colspan="5">
			<%=CommonUtils.checkNull(detailBean.getPrice_remark())%>
		</td>
	</tr>
	<tr>
		<td colspan="6">
		<%
			List<FsFileuploadPO> fileList = (LinkedList<FsFileuploadPO>)request.getAttribute("fileList");
	    	request.setAttribute("fileList",fileList); 
	    %>
	    <table class="table_info" border="0" id="file">
	    	<tr>
    			<td width="100%" colspan="2"><jsp:include page="${contextPath}/uploadDiv.jsp" /></td>
  			</tr>
  			<%for(int i=0;i<fileList.size();i++) { %>
  				<script type="text/javascript">
	 	 			addUploadRowByDL('<%=CommonUtils.checkNull(fileList.get(i).getFilename()) %>','<%=CommonUtils.checkNull(fileList.get(i).getFileid()) %>','<%=CommonUtils.checkNull(fileList.get(i).getFileurl())%>','<%=CommonUtils.checkNull(fileList.get(i).getFjid()) %>');
	 	 		</script>
			<%}%>
		</table> 
	    </td>
	</tr>
</table>
<!-- <table>
	<tr>
		<td>请选择装箱单号查询:
			<select name="boxNo" id="boxNo">
				<option value="">-请选择-</option>
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
		</td>
		<td><input type="button" name="query" onclick="isCheck(${claim_id });" value="查询"/></td>
	</tr>
</table> -->
<div id="loader"
	style='position: absolute; z-index: 200; background: #FFCC00; padding: 1px; top: 4px; display: none; display: none;'></div>
<%
	if (detailList != null && detailList.size() > 0) {
%>
<!--   -->
<%}%>
<table class="table_list" id="id2">
	<tr>
		<td height="10" align="center" colspan="2"></td>
	</tr>
	<tr>
		<td height="10" align="center" colspan="2">
			<input type="button" onclick="preChecked();" id="qianshou" class="normal_btn" value="签收" />&nbsp;&nbsp;
			<input type="button" onclick="detail();" id="mingxi" class="normal_btn" value="明细" />&nbsp;&nbsp;
			<input type="button" onclick="backTo();"class="normal_btn" value="返回" />
		</td>
	</tr>
</table>
</form>
</body>
</html>
