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
function backTo(id){
	
	fm.action="<%=contextPath%>/claim/oldPart/ClaimOldPartApporoveStorageManager/goApproveAndStoredPage411.do?CLAIM_ID="+id;
    fm.method="post";
    fm.submit();
}

function PartStorage(box_id,id){
	MyAlert(id);
	MyAlert(box_id);
	fm.action="<%=contextPath%>/claim/oldPart/ClaimOldPartApporoveStorageManager/PartStorage.do?box_id="+box_id+"&id="+id;
    fm.method="post";
    fm.submit();
	}

	function is_fleet_Sel(value){
		if(10041001 == value){
			document.getElementById("xl").checked=true;
			document.getElementById("aa").style.display = "inline";
			document.getElementById("bb").style.display = "none";
		}else{
			document.getElementById("sd").checked=true;
			document.getElementById("aa").style.display = "none";
			document.getElementById("bb").style.display = "inline";
		}
	}


 	//索赔明细ID
	function hiddenDetailId(id){
	   var res = String.format("<input type='hidden' name='orderIds' value='" + id + "' />");
	   document.write(res);
	}
	
	//选中预检查
	function preChecked() {
        var i=validateSelectedId();
        if(i==1){
        	if(Number($('price').value)>Number($('price1').value)){
    			MyAlert('审核运费不能大于申请运费!');
    			return;
    		}else{
        		MyConfirm("确认审核?",sign,[]);
    		}
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
		
		var res = String.format("<input type=\"text\" id=\"signNum"+id+"\" onChange=\"signforwad("+id+");\" name=\"signNum"+id+"\" class=\"short_txt\"  value=\"" + value + "\"/>"+"<input type=\"hidden\" id=\"signNum1"+id+"\" name=\"signNum1"+id+"\" class=\"short_txt\"  value=\"" + value + "\"/>");
		document.write(res);
	}
	function signforwad(id){
		var returnAmount =  document.getElementById('returnAmount'+id).value;//回运数
		var signNum =  document.getElementById('signNum'+id).value;//签收数
		var signNum1 =  document.getElementById('signNum1'+id).value;//签收数(隐藏域)
		if(isNaN(signNum)){
			MyAlert('请输入数字!');
			return;
		}
		if(signNum<returnAmount){
			$("deduct"+id).disabled=false;
		}
		if(signNum>=returnAmount){
			$("deduct"+id).value='';
			$("deduct"+id).disabled=true;
		}
		var value='';
		var url="<%=contextPath%>/claim/oldPart/ClaimOldPartApporoveStorageManager/approveAndStored33.json?id="+id+"&signNum="+signNum+"&deduct="+value;
		makeNomalFormCall(url,afterCall111,'fm','');
		
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
		var res = String.format("<input type=\"text\" id=\"wrHouse"+id+"\" name=\"wrHouse"+id+"\" class=\"short_txt\"  value=\"" + value + "\" />");
		document.write(res);
	}
	//抵扣原因编辑
	function editDeductCon(value,id){
		var str=genSelBoxStrExp("deduct"+id,<%=Constant.OLDPART_DEDUCT_TYPE%>,value,true,"","onChange=isCheck22('"+id+"',this.value);","true",'');
		var res = String.format(str);
		document.write(res);
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
		if(signNum!=signNum1){
			var url="<%=contextPath%>/claim/oldPart/ClaimOldPartApporoveStorageManager/approveAndStored33.json?id="+id+"&signNum="+signNum+"&deduct="+value;
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
	
	
	
	
	

	function isCheck(id){
		var boxNo=document.getElementById("boxNo").value;
		var CLAIM_ID=document.getElementById("CLAIM_ID").value;
		//location.href = "<%=contextPath%>/claim/oldPart/ClaimOldPartApporoveStorageManager/goApproveAndStoredPage5.do?CLAIM_ID="+CLAIM_ID+"&boxNo="+boxNo;
		fm.action="<%=contextPath%>/claim/oldPart/ClaimOldPartApporoveStorageManager/goApproveAndStoredPage5.do?CLAIM_ID="+CLAIM_ID+"&boxNo="+boxNo+"&isReturn=1";
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
	
	<input type="hidden" name="id" value="${i_claim_id }" />
	<input type="hidden" name="box_id" value="${boxNo }" />
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
		<td colspan="5">
			<%
				for (int i = 0; i < detailList1.size(); i++) {
				Map<String, Object> detailMap1 = detailList1.get(i);
			%>
			<%=CommonUtils.getDataFromMap(detailMap1, "WR_START_DATE")%>
			<%} %>
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

 <br />
	<c:set var="Num" value="${1}"/>
	<table class="table_edit" >
		<tr>
			<td align="center">序号</td>
			<td align="center">箱号</td>
			<td align="center">索赔申请单号</td>
			<td align="center">配件代码</td>
			<td align="center">配件名称</td>
			<td align="center">维修日期</td>
			<td align="center">供应商名称</td>
			<td align="center">旧件条码</td>
			<td align="center">抵扣原因</td>
			<td align="center">备注</td>
		</tr>
			<c:forEach items="${boxNoDetail}" var="boxNoDetail">
		<tr>
		<td align="center">${Num}</td>
		<td align="center">${boxNoDetail.BOX_NO}</td>
		<td align="center">${boxNoDetail.CLAIM_NO}</td>
		<td align="center">${boxNoDetail.PART_CODE}</td>
		<td align="center">${boxNoDetail.PART_NAME}</td>
		<td align="center">${boxNoDetail.RO_STARTDATE}</td>
		<td align="center">${boxNoDetail.PRODUCER_NAME}</td>
	
		<td align="center">${boxNoDetail.BARCODE_NO}</td>
	
		<c:if test="${boxNoDetail.IS_UPLOAD==null}">
		<td > </td>
		</c:if>
		<c:if test="${boxNoDetail.DEDUCTIBLE_REASON_CODE==null}">
		<c:if test="${boxNoDetail.IS_UPLOAD==null}">
		<td align="center">无件</td>
			</c:if>
		</c:if>
		<c:if test="${boxNoDetail.DEDUCTIBLE_REASON_CODE!=null}">
		<td align="center">${boxNoDetail.DEDUCTIBLE_REASON_CODE}</td>
		</c:if>
		<c:if test="${boxNoDetail.DEDUCTIBLE_REASON_CODE!=null}">
		<td > </td>
		</c:if>
		<c:if test="${boxNoDetail.REMARKS=='CQCVS'}">
		<td align="center">重庆微车</td>
		</c:if>
		<c:if test="${boxNoDetail.REMARKS=='CQJC'}">
		<td align="center">重庆轿车</td>
		</c:if>
		<c:if test="${boxNoDetail.REMARKS=='HBCA'}">
		<td align="center">河北长安</td>
		</c:if>
		<c:if test="${boxNoDetail.REMARKS=='NJCA'}">
		<td align="center">南京长安</td>
		</c:if>
		<c:if test="${boxNoDetail.REMARKS=='CHCA'}">
		<td align="center">昌河长安</td>
		</c:if>
		</tr>
		 <c:set var="Num" value="${Num+1}"/>
		</c:forEach>
	</table>
	<table class="table_list" id="id2">
	<tr>
		<td height="10" align="center" colspan="2"></td>
	</tr>
	<tr>
		<td height="10" align="center" colspan="2">
		
			<input type="button" onclick="backTo(${i_claim_id });"class="normal_btn" value="返回" />
		</td>
	</tr>
</table>


</form>
</body>
</html>
