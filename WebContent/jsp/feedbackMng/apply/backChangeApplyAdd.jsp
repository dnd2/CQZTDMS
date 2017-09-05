<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.infoservice.mvc.context.ActionContext" %>
<%@ page import="com.infodms.dms.bean.AclUserBean" %>
<%@ page import="com.infodms.dms.common.Constant" %>
<%
	String contextPath = request.getContextPath();
	ActionContext act = ActionContext.getContext();
	AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
	String name = logonUser.getName();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>

<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>退换车申请书维护</title>
  </head>
  <body>
<BODY>

<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置：  信息反馈管理&gt;信息反馈提报 &gt;退换车申请书</div>
<form id="fm" name="fm">
<input type="hidden" id="vin2"/>
<table width=100% border="0" align="center" cellpadding="1" cellspacing="1" bgcolor="91908E"  class="table_edit">
	      <th colspan="6"><img class="nav" src="<%=contextPath%>/img/subNav.gif" /> 基本信息</th>
          <tr bgcolor="F3F4F8">
            <td align="right" width="13%">服务中心经理：</td>
            <td width="20%"><input  type='text'  name='LINK_MANAGER'  id='LINK_MANAGER'    size='18'  datatype="0,is_null,20"  value=''  class="middle_txt" /></td>
            <td align="right" width="10%">服务中心经办人：</td>
            <td width="20%"><input  type='text'  name='LINK_MAN'  id='LINK_MAN'    size='18'  datatype="0,is_null,20"  value='<%=name%>'  class="middle_txt" /></td>
            <td height="27" align="right" bgcolor="FFFFFF" width="10%"><!-- 退换类型： --></td>
            <td align="left" bgcolor="FFFFFF" width="20%">
                 <script type="text/javascript">
	              //genSelBoxExp("EX_TYPE",<%=Constant.EX_TYPE%>,"",false,"short_sel","","false",'');
	            </script>
	            <input type="hidden" name="EX_TYPE" value="<%=Constant.EX_TYPE_02%>"/>
            </td>
          </tr>
          <tr bgcolor="FFFFFF">
            <td align="right" >车辆VIN码：</td>
            <td>
            	<input class="middle_txt" id="VIN"  onblur="getVinMsg(this);" style="cursor: pointer;" name="VIN" type="text"/>
            </td>
            <td height="27" align="right">车型：</td>
            <td align="left" id="v2" ></td>
            <td align="right" bgcolor="FFFFFF">发动机号：</td>
            <td bgcolor="FFFFFF" id="v3"></td>
          </tr>
          <tr bgcolor="F3F4F8">
            <td height="27"  align="right" bgcolor="F3F4F8">出厂日期：</td>
            <td bgcolor="F3F4F8" id="v5" ></td> 
            <td align="right" bgcolor="F3F4F8">购车日期：</td>
            <td bgcolor="F3F4F8" id="v6" ></td>
            <td width="12%" align="right" bgcolor="F3F4F8" >行驶里程（KM）：</td>
            <td id="v4"></td>
          </tr>
          <tr bgcolor="FFFFFF">
            <td height="27" align="right">客户姓名：</td>
            <td align="left" id="v7" ></td>
            <td align="right">客户联系电话：</td>
            <td id="v9"></td>
            <td align="right">&nbsp;</td>
            <td>&nbsp;</td>
          </tr>
          <tr bgcolor="FFFFFF">
            <td height="27" align="right">客户联系地址：</td>
            <td height="27" colspan="5" align="left" id="v10" ></td>
          </tr>
</table>
<table width=100% border="0" align="center" cellpadding="1" cellspacing="1" bgcolor="91908E"  class="table_edit">
         <tr bgcolor="FFFFFF">
            <td height="27" align="right">问题描述：</td>
            <td height="27" colspan="5" align="left" ><span class="tbwhite">
             <textarea  name="question_content" id="question_content" rows="3" cols="80" datatype="1,is_textarea,200"></textarea><font color="red">*</font>
            </span></td>
          </tr>
         <tr bgcolor="FFFFFF">
            <td height="27" align="right">用户要求如何：</td>
            <td height="27" colspan="5" align="left" ><span class="tbwhite">
             <textarea  name="user_content" id="user_content" rows="3" cols="80" datatype="1,is_textarea,200"></textarea><font color="red">*</font>
            </span></td>
          </tr>
         <tr bgcolor="FFFFFF">
            <td height="27" align="right">建议处理方式：</td>
            <td height="27" colspan="5" align="left" ><span class="tbwhite">
             <textarea  name="deal_content" id="deal_content" rows="3" cols="80" datatype="1,is_textarea,200"></textarea><font color="red">*</font>
            </span></td>
          </tr>
         <tr bgcolor="FFFFFF">
            <td height="27" align="right">费用明细：</td>
            <td height="27" colspan="5" align="left" ><span class="tbwhite">
             <textarea  name="cost_detail" id="cost_detail" rows="3" cols="80" datatype="1,is_textarea,200"></textarea><font color="red">*</font>
            </span></td>
          </tr>
   </table>
    <tr bgcolor="F3F4F8"> 
      <td></td>
    </tr>
    <tr> 
      <td  height=10> 
 <!-- 添加附件 开始  -->
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
  <!-- 添加附件 结束 -->       
      <table class="table_list">
          <tr > 
            <th height="12" align=center>
			<input type="button" onClick="checkForm('<%=contextPath%>/feedbackmng/apply/BackChangeApplyMantain/addBackChangeApply.do');" class="normal_btn" style="width=8%" value="确定"/>
			&nbsp;&nbsp;
			<input type="button" onClick="javascript:history.go(-1);" class="normal_btn" style="width=8%" value="返回"/></th>
		  </tr>
        </table>
      </td>
    </tr>
    <!-- 资料显示区结束 -->

</form>
  
  </body>
</html>
<script>
//表单提交前的验证：
function checkForm(url){
	if($('vin2').value)
		submitForm('fm') == true ? backChangeApplyAdd(url) : "";
	else {
		MyAlert('无效的VIN码!');
		return ;
	}
}
//表单提交方法：
function backChangeApplyAdd(url){
	var content = document.getElementById('question_content').value;
	var le = document.getElementById('question_content').value.length;
	var user_content = document.getElementById('user_content').value;
	var user_le = document.getElementById('user_content').value.length;
	var deal_content = document.getElementById('deal_content').value;
	var deal_le = document.getElementById('deal_content').value.length;
	var cost_detail = document.getElementById('cost_detail').value;
	var cost_le = document.getElementById('cost_detail').value.length;
	
	if(content==null||content=='' ){
		MyAlert("问题描述是必填项！");
	}else if(user_content==null||user_content==''){
		MyAlert("用户要求如何是必填项！");
	}else if(deal_content==null||deal_content==''){
		MyAlert("建议处理方式是必填项！");
	}else if(cost_detail==null||cost_detail==''){
		MyAlert("费用明细是必填项！");
	}else if(le>200){
		MyAlert("问题描述不能超过200个字符！");
	}else if(user_le>200){
		MyAlert("用户要求如何不能超过200个字符！");
	}else if(deal_le>200){
		MyAlert("建议处理方式不能超过200个字符！");
	}else if(cost_le>200){
		MyAlert("费用明细不能超过200个字符！");
	}else{
		fm.action = url;
		MyConfirm("确认新增？",fm.submit);
		//fm.submit();
	}
}

   //调用VIN的公用方法
function showVIN(){
	OpenHtmlWindow('<%=contextPath%>/feedbackmng/apply/StandardVipApplyManager/showVinList.do',800,500);
}

//获取子页面传过来的数据
function getVIN(VIN,GROUP_NAME,ENGINE_NO,COLOR,PRODUCT_DATE,PURCHASED_DATE,CUSTOMER_NAME,CERT_NO,MOBILE,ADDRESS_DESC,HISTORY_MILE){
	//document.getElementById("VIN").value = VIN;  //vin
	document.getElementById("v2").innerHTML = GROUP_NAME; //车系
	document.getElementById("v3").innerHTML = ENGINE_NO;  //发动机号
	document.getElementById("v4").innerHTML = HISTORY_MILE; //公里数
	document.getElementById("v5").innerHTML = PRODUCT_DATE; //生产日期
	document.getElementById("v6").innerHTML = PURCHASED_DATE; //购车日期
	document.getElementById("v7").innerHTML = CUSTOMER_NAME;  //客户姓名
	//document.getElementById("v8").innerHTML = CERT_NO;     身份证号
	document.getElementById("v9").innerHTML = MOBILE;   //客户手机
	document.getElementById("v10").innerHTML = ADDRESS_DESC;  //客户联系地址
}
// 用户手动输入VIN后,执行查询操作
function getVinMsg(obj){
	if(obj.value.length==17){
		var url = '<%=contextPath%>/feedbackmng/apply/StandardVipApplyManager/queryVchByVin.json?vin='+obj.value ;
		makeFormCall(url,vinMsgCallback,'fm');
	} else {
		getVIN('','','','','','','','','','','');
		$('vin2').value='';
	}
}
function vinMsgCallback(json){
	if(json.flag){
		var vin = json.vin;
		var group_name = json.group_name;
		var engine_no = json.engine_no;
		var mile = json.mile;
		var product_date = json.product_date;
		var purchase_date = json.purchased_date;
		var ctm_name = json.ctm_name;
		var cert_no = json.cert_no;
		var mobile = json.mobile;
		var address = json.address;
		getVIN(vin,group_name,engine_no,'',product_date,purchase_date,ctm_name,cert_no,mobile,address,mile);
		$('vin2').value = vin ;
	}
}
	
</script>