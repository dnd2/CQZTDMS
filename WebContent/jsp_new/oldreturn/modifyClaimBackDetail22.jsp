<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.Constant" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@page import="java.util.List"%>
<%@ page import="com.infodms.dms.po.FsFileuploadPO"%>
<%@page import="java.util.LinkedList"%>
<%String contextPath = request.getContextPath();%>
<%@page import="com.infodms.dms.util.CommonUtils"%><html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c" %>
<%@taglib uri="/jstl/change" prefix="select" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>索赔回运清单装箱</title>
<script type="text/javascript">
	function doInit(){
	   loadcalendar();
	}
	function init(){
	   if("10731001"==$('backType').value){
		 //根据id查找对象，
         var obj=document.getElementById('backType');
		  obj.options.add(new Option("常规回运","10731002")); //这个兼容IE与firefox
	   }
	   if($('notice').value=='noTrans'){
	   	MyAlert("你还未申请旧件运输方式或者未通过审核,请先申请运输方式！");
	   }
	}
</script>
</head>
<body onload="doInit();init();">
 <form method="post" name ="fm" id="fm">
  <input type="hidden" name="claimId" id="claimId" value="${claimId }" /><!-- 回运单ID -->
  <input type="hidden" name="notice" id="notice" value="${notice }" />
  <table width="100%">
  <tr>
  	<td>
  	<div class="navigation"><img src="<%=contextPath%>/img/nav.gif"/>&nbsp;当前位置：售后服务管理 &gt;索赔旧件管理 &gt;索赔件回运物流单管理</div>
  	</td>
  </tr>
  <tr>
  	<td>
  <table class="table_edit">
          <tr>
	         <th colspan="6"><img class="nav" src="<%=contextPath%>/img/subNav.gif" />基本信息</th>
          </tr>
          <tr>
          	<td align="right" nowrap="nowrap">货运单号：</td>
          	<td align="left">
          		<input type="text" id="transNo" name="transNo" value="${tranNo }" class="middle_txt"  readonly="readonly"/>
          	</td>
          	<td align="right" nowrap="nowrap">发运日期：</td>
       		<td align="left">
       		<input name="sendDate" type="text" class="short_time_txt"value="${time }" id="sendDate" readonly="readonly"/> 
			<input name="button" value=" " type="button" class="time_ico" onclick="showcalendar(event, 'sendDate', false);" /><span style="color: red">*</span>  	
       			</td>
       			 <td align="right" nowrap="nowrap">三包员电话：</td>
            <td align="left" colspan="5"><input type="text" name="tel" id="tel" class="long_txt" value="${poValue.tel }" datatype="0,is_null,50" />&nbsp;</td>
       		
          </tr>
          <tr>
	            <td align="right" nowrap="nowrap">运费：</td>
	            <td align="left">
	               <input type="text" id="price" datatype="1,isMoney,7" name="price" class="short_txt" value="${poValue.price }"/>
	            </td>
	            <td align="right" nowrap="nowrap">回运类型：</td>
	            <td align="left">
	            	<select:select  name="backType" type="1073" style="short_sel" exist="${return_type }" noTop="true" value="${return_type }" />
	                <%-- <script type="text/javascript">
	                	genSelBoxExp("backType",<%=Constant.BACK_TRANSPORT_TYPE%>,"",false,"short_sel","","false",'');
				    </script>&nbsp; --%>
	            </td>
	           <td align="right" nowrap="nowrap">货运方式：</td>
            <td align="left">
            <script type="text/javascript">
			    genSelBoxExp("freight_type",<%=Constant.OLD_RETURN_STATUS%>,'${poValue.transportType }',false,"short_sel","","true",'<%=Constant.OLD_RETURN_STATUS_02%>,<%=Constant.OLD_RETURN_STATUS_04%>');
			 </script>
            </td>
             </tr>
            <tr>
	            <td align="right" nowrap="nowrap">发运单号：</td>
            <td align="left" ><input type="text" name="TRANSPORT_NO" id="TRANSPORT_NO" class="middle_txt" value="${poValue.transportNo }" maxlength="20"/>
            <span style="color: red;" id="que">*</span><span style="color: red;display: none"  id="que2">注：自送,请勿修改!</span>
            &nbsp;</td>
            <td align="right" id="tranPerson1" style="display: " nowrap="nowrap">快递公司：</td>
             <td id="tranPerson2" style="display: ">
            	<select id='tranPerson' class="short_sel" name='tranPerson'>
            	<option value="">--请选择--</option>
				
					<c:forEach items="${select}" var="select">
					<c:if test="${poValue.tranPerson==select.DETAIL_ID }">
						<option value="${select.DETAIL_ID }" selected >${select.TRANSPORT_NAME }</option>
					</c:if>
					<c:if test="${poValue.tranPerson!=select.DETAIL_ID }">
						<option value="${select.DETAIL_ID }"  >${select.TRANSPORT_NAME }</option>
					</c:if>
					</c:forEach>
				
			</select>
            </td>
            <td align="right" nowrap="nowrap">预计到货时间：</td>
	            <td align="left">
	              <input name="arriveDate" type="text" class="short_time_txt" value="${arriveDate }" id="arriveDate" readonly="readonly"/> 
					<input name="button" value=" " type="button" class="time_ico" onclick="showcalendar(event, 'arriveDate', false);" /> 
	            </td>
          </tr>
          <tr>
			<td colspan="6">
          		<table class="table_info" border="0" id="file">
					<input type="hidden" name="fjids"/>
					<tr colspan="8">
					<th><img class="nav" src="<%=contextPath%>/img/subNav.gif" />&nbsp;附件列表：&nbsp;&nbsp;&nbsp;&nbsp;
						<span align="left"><input type="button" class="normal_btn"  onclick="showUpload2('<%=contextPath%>')" value ='添加附件'/></span>
					</th>
					</tr>
					<tr>
						<td width="100%" colspan="2">
							<jsp:include page="${contextPath}/uploadDiv.jsp" />
						</td>
					</tr>
					<%
						List<FsFileuploadPO> fileList = (LinkedList<FsFileuploadPO>)request.getAttribute("fileList");
					request.setAttribute("fileList",fileList); 
						for(int i=0;i<fileList.size();i++) { %>
    						<script type="text/javascript">
    							addUploadRowByDb('<%=CommonUtils.checkNull(fileList.get(i).getFilename()) %>','<%=CommonUtils.checkNull(fileList.get(i).getFjid()) %>','<%=CommonUtils.checkNull(fileList.get(i).getFileurl())%>');
    						</script>
    				<%} %>
				</table>
         	</td>
		  </tr>
  		  
  		  <tr>
	      	<td colspan="6">
	      	<table class="table_info" border="0">
			<tr>
    			<td>备注：<textarea rows="5" cols="100" name="info">${poValue.remark }</textarea></td>
  			</tr>
			</table> 
	      	</td>
	      </tr>
  </table>
  </td></tr>
	<tr><td>	  
     <table class="table_list">
       <tr > 
         <td height="12" align="center">
          <input id="a11111" type="button" onclick="Save();" class="normal_btn" style="width=8%" value="确定"/>
           &nbsp;&nbsp;
          <input id="b11111" type="button" onclick="history.back();" class="normal_btn" style="width=8%" value="返回"/>
         </td>
       </tr>
     </table>
      </td></tr>
  </table>
     <div id="loader" style='position:absolute;z-index:200;background:#FFCC00;padding:1px;top:4px;display:none;display: none;'></div>
</form>
<script type="text/javascript">

var freight_type=$('freight_type').value;
getTypeChangeStyle(freight_type);
function backUp(){
	location.href="<%=contextPath%>/claim/oldPart/ClaimBackPieceBackListOrdManager/returnOrder.do";
}
function Save(){
	if(submitForm(fm)==false){
		return;
	}
	var no = document.getElementById("TRANSPORT_NO").value;
	var price = document.getElementById("price").value;
	
	
	
	
	
	var tranPerson = $('tranPerson').value;
	var type = document.getElementById("freight_type").value;
	//if(type!='<%=Constant.OLD_RETURN_STATUS_03%>'&&(no==null|| no=="")){
	if(no==null|| no==""){
		MyAlert("请填写发运单号!");
		return false;
	}
	if(Number($('price').value)>0){
		
	
		if($('fileUploadTab').rows.length<=1){
			MyAlert('当运费大于0的时候必须上传附件!');
			return;
		}
	}
	 if($('notice').value=='noTrans'){
	   	MyAlert("你还未申请旧件运输方式或者未通过审核,请先申请运输方式！");
	   	return;
	   }
	if(type != <%=Constant.OLD_RETURN_STATUS_03%> &&(tranPerson==""||tranPerson==null) ){
			MyAlert('不是自送时,必须选择快递公司!');
			return;
	}
	if(price>0){
		var url="<%=contextPath%>/claim/oldPart/ClaimBackPieceBackListOrdManager/checkTransport.json";
		sendAjax(url,function(json){
			if(json.result==false){
				MyAlert("该发运单号已经申报过运费，请勿重复申报！");
				return false;
			}
			MyConfirm("确定补录吗?",save,[]);
		},'fm');	
	}else{
		MyConfirm("确定补录吗?",save,[]);
	}
	
	
}
function save(){
	fm.action = "<%=contextPath%>/OldReturnAction/updateReturnListInfo22.do";
	fm.submit();
}

function trim(str){ //删除左右两端的空格
    return str.replace(/(^\s*)|(\s*$)/g, "");
}
 var obj = document.getElementById("freight_type");
  if(obj){
   		obj.attachEvent('onchange',getTypeChangeStyleParam);//
   	}
   function getTypeChangeStyleParam() {
   		getTypeChangeStyle(obj.value);
   	}
   	function getTypeChangeStyle(obj) {
    	if(obj=='<%=Constant.OLD_RETURN_STATUS_03%>') {//自送
    		document.getElementById("que").style.display = 'none';
    		$('tranPerson').value=" ";
    		document.getElementById("tranPerson1").style.display = 'none';
    		document.getElementById("tranPerson2").style.display = 'none';
    		//document.getElementById("que2").style.display = '';
    		//document.getElementById("TRANSPORT_NO").setAttribute("readOnly","true");
    		document.getElementById("TRANSPORT_NO").readOnly = true;
    		document.getElementById("TRANSPORT_NO").value="0000";
    	}else {
    		document.getElementById("que").style.display = '';
    		document.getElementById("tranPerson1").style.display = '';
    		document.getElementById("tranPerson2").style.display = '';
    		//document.getElementById("que2").style.display = 'none';
    		document.getElementById("TRANSPORT_NO").value='${poValue.transportNo }';
    		document.getElementById("TRANSPORT_NO").readOnly = false;
    	}
    	}
</script>
</body>
</html>