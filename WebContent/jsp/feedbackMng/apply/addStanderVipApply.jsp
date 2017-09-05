<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c" %>
<%@ page import="com.infodms.dms.common.Constant"%>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<% String contextPath = request.getContextPath(); %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>合格证补办修复或更换申请表</title>
<script type="text/javascript">
	//申请类型和操作类型的联动
	function selectAction(){
		if(document.fm.stType.value == '<%=Constant.ORDER_SV_TYPE_01%>') {
			var sc = genSelBoxStrExp('stAction','<%=Constant.ORDER_SV_ACTION%>','',false,'short_sel',"onchange='selectMoney()'",'false','<%=Constant.ORDER_SV_ACTION_03%>');
			var pu = document.getElementById("productUnit").innerHTML = sc;
			if(document.fm.stType.value == '<%=Constant.ORDER_SV_TYPE_01%>'&&document.fm.stAction.value == '<%=Constant.ORDER_SV_ACTION_01%>'){
				document.getElementById("commentUnit").innerHTML = "<font color='red'>收取500元</font>";
			}
			document.getElementById("fileId").innerHTML = "合格证图片：";
		}else if(document.fm.stType.value == '<%=Constant.ORDER_SV_TYPE_02%>'){
			var sc = genSelBoxStrExp('stAction','<%=Constant.ORDER_SV_ACTION%>','',false,'short_sel',"onchange='selectMoney()'",'false','<%=Constant.ORDER_SV_ACTION_03%>');
			var pu = document.getElementById("productUnit").innerHTML = sc;
			if(document.fm.stType.value == '<%=Constant.ORDER_SV_TYPE_02%>' && document.fm.stAction.value == '<%=Constant.ORDER_SV_ACTION_01%>'){
				document.getElementById("commentUnit").innerHTML = '';//"<font color='red'>收取50元</font>";
			}
			document.getElementById("fileId").innerHTML = "VIN条码拓印件：";
		}
	}
	
	//申请类型和操作类型与收取费用的联动
	function selectMoney(){
		if(document.fm.stType.value == '<%=Constant.ORDER_SV_TYPE_01%>'&& document.fm.stAction.value == '<%=Constant.ORDER_SV_ACTION_01%>'){
			document.getElementById("commentUnit").innerHTML = "<font color='red'>收取500元</font>";
		}else if(document.fm.stType.value == '<%=Constant.ORDER_SV_TYPE_02%>' && document.fm.stAction.value == '<%=Constant.ORDER_SV_ACTION_01%>'){
			document.getElementById("commentUnit").innerHTML = '';//"<font color='red'>收取50元</font>";
		}else{
			document.getElementById("commentUnit").innerHTML = "";
		}
	}
</script>
</head>
<body>
<div id="loader" style='position:absolute;z-index:200;background:#FFCC00;padding:1px;top:4px;display:none;display: none;'></div>
<div class="navigation"><img src="<%=request.getContextPath()%>/img/nav.gif" />&nbsp;当前位置： 信息反馈管理 &gt; 信息反馈提报 &gt;合格证补办更换申请表添加</div>
 <form method="post" name = "fm" >
 <input type="hidden" id="vin2"/>
   <table width=100% border="0" align="center" cellpadding="1" cellspacing="1" bgcolor="91908E" class="table_edit">
          <th colspan="6"><img class="nav" src="../../../img/subNav.gif" /> 基本信息&nbsp;&nbsp;&nbsp;&nbsp; <font color="#FF0000">(合格证补办收费500元/张，合格证车身颜色更改：200元/张)</font></th>
		  <tr>
		    <td align="right">服务中心联系人：</td>
		    <td>
		    	<input type="text" name="linkMan" id="linkMan" class="middle_txt"  datatype="0,is_null,5" maxlength="5" value="<c:out value="${dealerInfo.LINK_MAN}"/>" />
		    </td>
		    <td align="right">服务中心联系电话：</td>
		    <td>
		    	<input type="text" name="tel" id="tel" class="middle_txt" datatype="0,is_phone,20" maxlength="20" value="<c:out value="${dealerInfo.PHONE}"/>" />
		    </td>
		    <td align="right">服务中心邮编：</td>
		    <td>
		    	<input type="text" name="zipCode" id="zipCode" class="middle_txt" datatype="0,is_digit,10" maxlength="10" value="<c:out value="${dealerInfo.ZIP_CODE}"/>" />
		    </td>
	      </tr>
		  <tr >
		    <td  align="right">服务中心地址：</td>
		    <td colspan="5">
		    	<textarea name="customerAdress" id="customerAdress" datatype="0,is_null,100" maxlength="5" style='border: 1px solid #94BBE2;width:70%;overflow: hidden;word-break:break-all;' rows=1><c:out value="${dealerInfo.ADDRESS}"/></textarea>
		    </td>
	      </tr>
          
		  <tr bgcolor="F3F4F8">
            <td align="right">工单类型：</td>
            <td>
              <script type="text/javascript">
 				 genSelBoxExp("stType",'<%=Constant.ORDER_SV_TYPE%>',"",false,"short_sel","onchange='selectAction()'","false",'');
			  </script>
            </td>
            <td align="right" >操作类型：</td>
            <td>
            <div id="productUnit">
              <script>
              	genSelBoxExp("stAction",'<%=Constant.ORDER_SV_ACTION%>',"",false,"short_sel","onchange='selectMoney()'","false",'<%=Constant.ORDER_SV_ACTION_03%>');
              </script>
            </div>
            </td>
            <td>
            <div><span id="commentUnit"><font color="red">收取500元</font></span></div>
            </td>
          </tr>
          <tr >
            <td align="right">申请内容：</td>
            <td colspan="5">
              <textarea name="stContent" id="stContent" datatype="0,is_null,200" rows='5' cols='80' ></textarea>
            </td> 
          </tr>
        </table> 
        
 		<table width=100% border="0" align="center" cellpadding="1" cellspacing="1" bgcolor="91908E"  class="table_edit">
            <th colspan="6"><img class="nav" src="<%=request.getContextPath()%>/img/subNav.gif" /> 车辆信息</th>
		  
		  <tr>
		    <td width="15%" align="right">车辆识别码(VIN)：</td>
		    <td width="20%">
		    	<input class="middle_txt" id="vin"  onblur="getVinMsg(this);" style="cursor: pointer;" name="vin" type="text"/>
		    </td>
		    <td width="10%" align="right">车型：</td>
		    <td id="v2" width="20%"></td>
		    <td width="10%" align="right">发动机号：</td>
		    <td id="v3" width="20%"></td>
	      </tr>
		  <tr>
		    <td width="10%" align="right">颜色：</td>
		    <td id="v4" width="20%"></td>
		    <td width="10%" align="right">生产日期：</td>
		    <td id="v5" width="20%"></td>
		    <td width="10%" align="right">购车日期：</td>
		    <td id="v6" width="20%"></td>
	      </tr>
          <tr>
            <td width="10%" align="right">车主姓名：</td>
            <td id="v7" width="20%"></td>
            <td width="10%" align="right">证件号码：</td>
            <td id="v8" width="20%"></td>
            <td width="10%" align="right">车主联系电话：</td>
            <td id="v9" width="20%"></td>
          </tr>
          <tr>
            <td width="10%" align="right">车主地址：</td>
            <td colspan="5" id="v10"></td>
          </tr>
   </table>        
<!-- 添加附件 开始  -->
	<table class="table_info" border="0" id="file">
		<input type="hidden" id="fjids" name="fjids"/>
	    <tr>
	        <th>
				<img class="nav" src="<%=contextPath%>/img/subNav.gif" />附件信息
			     <input type="button" class="normal_btn"  onclick="showUpload('<%=contextPath%>')" value ='添加附件'/>
			     &nbsp;
			     <font style="color:red">(需上传1、派出所证明 2、车架号和发动机号拓印件 3、合格证更换需快件寄回)</font>
			</th>
		</tr>
		<tr>
    		<td width="100%" colspan="2"><jsp:include page="${contextPath}/uploadDiv.jsp" /></td>
  		</tr>
	</table> 
  <!-- 添加附件 结束 -->
   <!-- 按钮 begin -->
   <table class="table_list">
      <tr > 
      	<th height="12" align=center>
			<input type="button" onClick="addStandarVip()" class="normal_btn" style="width=8%" value="确定"/>&nbsp;&nbsp;
			<input type="button" onClick="javascript:history.go(-1);" class="normal_btn" style="width=8%" value="返回"/>
	   	</th>
	  </tr>
   </table>
   <!-- 按钮 end -->
</form>
<script type="text/javascript">
	//添加
	function addStandarVip(){
		var customerAdress = document.getElementById('customerAdress').value;
		var content = document.getElementById('stContent').value;
		var le = document.getElementById('stContent').value.length;
		if(customerAdress==null||customerAdress==''){
			MyAlert("服务中心地址是必填项！");
		}else if(!$('vin2').value){
			MyAlert('无效的VIN码!');
		}else{
				if(content != null && content != ''){
				if(le > 0 && le <= 200){
					submitForm(fm);
					fm.action = "<%=contextPath%>/feedbackmng/apply/StandardVipApplyManager/addStandderVip.do";
					MyConfirm("确认新增",fm.submit);
					//fm.submit();
				}else{
					MyAlert("申请内容不能超过200个字符！");
				}
				
			}else{
				MyAlert("申请内容是必填项！");
			}
		}
	}
	
	//获取VIN的方法
	function showVIN(){
		OpenHtmlWindow('<%=contextPath%>/feedbackmng/apply/StandardVipApplyManager/showVinList.do',800,500);
	}
	
	//获取子页面传过来的数据
	function getVIN(VIN,GROUP_NAME,ENGINE_NO,COLOR,PRODUCT_DATE,PURCHASED_DATE,CUSTOMER_NAME,CERT_NO,MOBILE,ADDRESS_DESC,HISTORY_MILE){
		//document.getElementById("vin").value = VIN;
		document.getElementById("v2").innerHTML = GROUP_NAME;
		document.getElementById("v3").innerHTML = ENGINE_NO;
		document.getElementById("v4").innerHTML = COLOR;
		document.getElementById("v5").innerHTML = PRODUCT_DATE;
		document.getElementById("v6").innerHTML = PURCHASED_DATE;
		document.getElementById("v7").innerHTML = CUSTOMER_NAME;
		document.getElementById("v8").innerHTML = CERT_NO;
		document.getElementById("v9").innerHTML = MOBILE;
		document.getElementById("v10").innerHTML = ADDRESS_DESC;
	}
	//用户手动输入VIN后,执行查询操作
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
</body>
</html>
