<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="com.infodms.dms.common.Constant"%>
<%@ page import="com.infodms.dms.util.CommonUtils"%>
<%@ page import="com.infodms.dms.po.TtDealerActualSalesPO"%>
<%@ page import="com.infodms.dms.po.TtCustomerPO"%>
<%@taglib uri="/jstl/cout" prefix="c"%>
<%@ page import="java.util.Map"%>
<%@ page import="java.util.List"%>
<%
	String contextPath = request.getContextPath();
%>
<head>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<script type="text/javascript">
	function myOnLoad(){
		loadcalendar();   //初始化时间控件
		var ctmType = ${vehicleInfo.CTM_TYPE};
		var s_ctmType =<%=Constant.CUSTOMER_TYPE_02%>;
		if(ctmType == s_ctmType){
			document.getElementById("divcompany_table").style.display = "inline";
			document.getElementById("vehicleInfoid").style.display = "none";
		}else{
			document.getElementById("divcompany_table").style.display = "none";
		}
		document.getElementById("newZuofei").style.display= "none";
		document.getElementById("newCar").style.display="inline";
	}
	function Submit(){   	
	       var src_vin = document.getElementById('src_vin').value;
	       var fanliPrc = document.getElementById('fanliPrc').value;
	       var feifanliPrc = document.getElementById('feifanliPrc').value;
	       var vin = document.getElementById('NEW_VIN').value;
	       var vin2 = document.getElementById('NEW_VIN2').value;
	       var fjid = document.getElementById('fjid');
	        
	        var SCRAP_CERTIFY_NO = document.getElementById('SCRAP_CERTIFY_NO').value;
	        var SCRAP_DATE = document.getElementById('SCRAP_DATE').value;
	        var NEW_MODEL_NAME1 = document.getElementById('NEW_MODEL_NAME1').value;
	        var NEW_SALES_DATE1 = document.getElementById('NEW_SALES_DATE1').value;
	        var CTM_NAME = document.getElementById('CTM_NAME').value;
	        var OLD_BRAND_NAME = document.getElementById('OLD_BRAND_NAME').value;
	        
	        var CTM_NAME2 = document.getElementById("CTM_NAME2").value ;
			var	NEW_SALES_DATE2 = document.getElementById("NEW_SALES_DATE2").value ;
			var NEW_MODEL_NAME2 = document.getElementById("NEW_MODEL_NAME2").value ;
	        
	        var d_type = document.getElementById('displacement_type').value;
	        	        
	        if('80191001' == d_type || '80191003' == d_type || '80191004' == d_type) {
		        if('-1' == fanliPrc ){ 
		          MyAlert('返利价没有维护不能提报！');
		          return false;	         
		        }else if(null == vin || '' == vin){
		           MyAlert('VIN底盘号输入不能为空！');
		           return false;
		        }else if(null == NEW_MODEL_NAME1 || '' == NEW_MODEL_NAME1){
		           MyAlert('旧车型号不能为空！');
		           return false;
		        }else if(null == NEW_SALES_DATE1 || '' == NEW_SALES_DATE1){
		           MyAlert('销售日期不能为空！');
		           return false;
		        }else if(null == CTM_NAME || '' == CTM_NAME){
		           MyAlert('车主姓名不能为空！');
		           return false;
		        }else if(null == OLD_BRAND_NAME || '' == OLD_BRAND_NAME){
		           MyAlert('品牌不能为空！');
		           return false;
		        }else if(src_vin == vin ){
		           MyAlert('旧车VIN底盘号不能与新车一致！');
		           return false;
		        }else if(null == fjid ){
		           MyAlert('请上传附件！');
		           return false;
		        }else{	                	
				  $('fm').action="<%=contextPath%>/sales/displacement/DisplacementCar/DisplacementInsertCar.do";
				  $('fm').submit();
			 } 
		  }
		  if('80191002' == d_type) {
		        if('-1' == feifanliPrc){ 
		          MyAlert('返利价没有维护不能提报！');
		          return false;	         
		        }else if(null == OLD_BRAND_NAME || '' == OLD_BRAND_NAME){
		           MyAlert('品牌不能为空！');
		           return false;
		        }else if(null == vin2 || '' == vin2){
		           MyAlert('VIN底盘号输入不能为空！');
		           return false;
		        }else if(null == SCRAP_CERTIFY_NO || '' == SCRAP_CERTIFY_NO ) {
		           MyAlert('报废证明编号不能为空！');
		           return false; 
		        }else if(null == SCRAP_DATE || '' == SCRAP_DATE){
		           MyAlert('车辆报废时间！');
		           return false; 
		        }else if(null == NEW_MODEL_NAME2 || '' == NEW_MODEL_NAME2){
		           MyAlert('旧车型号不能为空！');
		           return false;
		        }else if(null == NEW_SALES_DATE2 || '' == NEW_SALES_DATE2){
		           MyAlert('销售日期不能为空！');
		           return false;
		        }else if(null == CTM_NAME2 || '' == CTM_NAME2){
		           MyAlert('车主姓名不能为空！');
		           return false;
		        }else if(src_vin == vin2 ){
		           MyAlert('旧车VIN底盘号不能与新车一致！');
		           return false;
		        }else if(null == fjid ){
		           MyAlert('请上传附件！');
		           return false;
		        }else{	                	
				  $('fm').action="<%=contextPath%>/sales/displacement/DisplacementCar/DisplacementInsertCar.do";
				  $('fm').submit();
			  } 
		    }
		}
	function returnSubmit(){
			history.back();
		}
	function doCusChange(){
		var a=document.getElementById("displacement_type").value;
			if(a==<%=Constant.DisplancementCarrequ_replace_1%> || a==<%=Constant.DisplancementCarrequ_replace_3%> || a==<%=Constant.DisplancementCarrequ_replace_4%> || a==<%=Constant.DisplancementCarrequ_replace_5%>){
				document.getElementById("newZuofei").style.display= "none";
				document.getElementById("newCar").style.display="inline";
				
                document.getElementById("CTM_NAME2").value='';
                document.getElementById("NEW_VIN2").value='';              
				document.getElementById("NEW_SALES_DATE2").value='';
				document.getElementById("NEW_MODEL_NAME2").value='';
				makeNomalFormCall('<%=contextPath%>/sales/displacement/DisplacementCar/DisplacementfeiQuery.json',showRes6,'fm');
		 	}
		 	if(a==<%=Constant.DisplancementCarrequ_replace_2%>){
					document.getElementById("newZuofei").style.display="inline";
					document.getElementById("newCar").style.display= "none";
					document.getElementById("SCRAP_DATE").value='';
					document.getElementById("SCRAP_CERTIFY_NO").value='';
					
					document.getElementById("NEW_VIN").value='';   
					document.getElementById("CTM_NAME").value='';
					document.getElementById("NEW_SALES_DATE1").value='';
					document.getElementById("NEW_MODEL_NAME1").value='';	
					makeNomalFormCall('<%=contextPath%>/sales/displacement/DisplacementCar/DisplacementfeiQuery.json',showRes1,'fm');							
			}
		}
   function showRes1(json){
		if(json.returnFalse1 == 2){
			MyAlert('该车经销商未维护返利价格，请维护价格。');
			document.getElementById('fanliPrc').value = '-1';
			document.getElementById('feifanliPrc').value = '-1';
		}else {
		   document.getElementById('feifanliPrc').value = json.returnfeiPrice;
		}
	}
	//获取字符串长度 中文以两个字符计算
	String.prototype.len = function(){   
	    return this.replace(/[^\x00-\xff]/g,"**").trim().length;   
	}   
	
	function showRes6(json){
		if(json.returnFalse1 == 2){
			MyAlert('该车经销商未维护返利价格，请维护价格。');
			document.getElementById('fanliPrc').value = '-1';
			document.getElementById('feifanliPrc').value = '-1';
		}else {
		   document.getElementById('fanliPrc').value = json.returnfeiPrice;
		}
	}
	
</script>
<title>非长安二手车置换申请</title>
</head>
<body onload="myOnLoad();">
<div class="wbox">
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 整车销售 &gt; 客户信息管理 &gt; 非长安二手车置换申请</div>
<form id="fm" name="fm" method="post"><input type="hidden" name="curPage" id="curPage" value="1" />
<input type ="hidden" name="IS_CHANA" id="IS_CHANA" value="10041002"/>
<input type ="hidden" name="src_vin" id="src_vin" value="${vehicleInfo.VIN }"/>
<table class="table_edit" align=center>
	<tr class="tabletitle">
		<th colspan="4"><img class="nav" src="<%=contextPath%>/img/subNav.gif" />车辆资料</th>
	</tr>
	<tr>
		<td width="15%" align=right>VIN：</td>
		<td align=left>${vehicleInfo.VIN }</td>
		<td align=right>发动机号：</td>
		<td align=left>${vehicleInfo.ENGINE_NO }</td>
	</tr>
	<tr>
		<td align=right>车系：</td>
		<td align=left>${vehicleInfo.SERIES_NAME }</td>
		<td align=right>车型：</td>
		<td align=left>${vehicleInfo.MODEL_NAME }</td>
	</tr>
	<tr>
		<td align=right>物料代码：</td>
		<td align=left>${vehicleInfo.MATERIAL_CODE }</td>
		<td align=right>物料名称：</td>
		<td align=left>${vehicleInfo.MATERIAL_NAME }</td>
	</tr>
	<tr>
		<td align=right>颜色：</td>
		<td align=left>${vehicleInfo.COLOR }</td>
		<td align=right>品牌:</td>
		<td align=left>${vehicleInfo.BRAND}</td>		
	</tr>
	<tr>
	  <td align=right>生产基地:</td>
	  <td align=left>
		 <script>document.write(getItemValue(${vehicleInfo.PRODUCE_BASE}));</script>
	  </td>
	</tr>
	<tr>
	  <td>
	   <input type="hidden" id="VEHICLE_ID" name="VEHICLE_ID" value ="${vehicleInfo.VEHICLE_ID }"/>
	    <input type="hidden" id="ORDER_ID" name="ORDER_ID" value="${vehicleInfo.ORDER_ID }"/>
	    <input type="hidden" id="OEM_COMPANY_ID" name="OEM_COMPANY_ID" value="${vehicleInfo.OEM_COMPANY_ID} "/>
	    <input type="hidden" id="DEALER_ID" name="DEALER_ID" value="${vehicleInfo.DEALER_ID} "/>
	  </td>
	</tr>
</table>
<div id="divcompany_table" style="">
<table class="table_edit" align="center" id="company_table">
	<tr class="tabletitle">
		<th colspan="4" align="right"><img class="nav" src="<%=contextPath%>/img/subNav.gif" />公司客户信息</th>
	</tr>

	<tr>
		<td width="15%" align="right">公司名称：</td>
		<td width="35%" align="left">${vehicleInfo.COMPANY_NAME }</td>
		<td width="15%" align="right">公司简称：</td>
		<td width="35%" align="left">${vehicleInfo.COMPANY_S_NAME }</td>
	</tr>
	<tr>
		<td align="right">公司电话：</td>
		<td align="left">${vehicleInfo.COMPANY_PHONE}</td>
		<td align="right">公司规模 ：</td>
		<td align="left">
		<script>document.write(getItemValue(${vehicleInfo.LEVEL_ID}));</script>				
		</td>
	</tr>
	<tr>
		<td align="right">公司性质：</td>
		<td align="left">
		<script>document.write(getItemValue(${vehicleInfo.KIND}));</script>		
		</td>
		<td align="right">目前车辆数：</td>
		<td align="left">${vehicleInfo.VEHICLE_NUM}</td>
	</tr>
		<tr>
		<td align="right">客户来源：</td>
		<td align="left">
		<script>document.write(getItemValue(${vehicleInfo.CTM_FORM}));</script>		
		</td>
	</tr>
	<tr>
		<td align="right">所在地：</td>
		<td colspan="3" align="left">
		省份：<script type='text/javascript'>
     			writeRegionName(${vehicleInfo.PROVINCE });
 			</script>
	&nbsp;&nbsp;&nbsp;
		地级市：<script type='text/javascript'>
     			writeRegionName(${vehicleInfo.CITY });
 			</script>&nbsp;&nbsp;&nbsp;
		区、县 :<script type='text/javascript'>
     			writeRegionName(${vehicleInfo.TOWN });
 			</script>&nbsp;&nbsp;&nbsp;
		</td>
	</tr>
</table>
</div>
<div id="vehicleInfoid"  style="">
<table class="table_edit" align="center" id="ctm_table_id">
	<tr>
		<th colspan="4"><img class="nav" src="<%=contextPath%>/img/subNav.gif" />个人客户信息</th>
	</tr>
	<tr>
		<td width="15%" align="right" id="tcmtd">客户姓名：</td>
		<td width="35%" align="left">
		${vehicleInfo.CTM_NAME}
		</td>
		<td width="15%" align="right" id="sextd">性别：</td>
		<td align="left">
		<script>document.write(getItemValue(${vehicleInfo.SEX}));</script>			
		</td>
	</tr>
</table>
<table class="table_edit" align="center" id="ctm_table_id_2">
	<tr>
		<td width="15%" align="right">证件类别：</td>
		<td width="35%" align="left">
		<script>document.write(getItemValue(${vehicleInfo.CARD_TYPE}));</script>				
		</td>
		<td width="15%" align="right">证件号码：</td>
		<td width="35%" align="left">${vehicleInfo.CARD_NUM }</td>
	</tr>
	<tr>
		<td align="right">主要联系电话：</td>
		<td align="left">${vehicleInfo.MAIN_PHONE}</td>
		<td align="right">其他联系电话：</td>
		<td align="left">${vehicleInfo.OTHER_PHONE}</td>
	</tr>
	<tr>
		<td align="right">职业：</td>
		<td align="left">
		<script>document.write(getItemValue(${vehicleInfo.PROFESSION}));</script>		
		</td>
		<td align="right">职务：</td>
		<td align="left">${vehicleInfo.JOB}</td>
	</tr>
	<tr>
		<td align="right">所在地：</td>
		<td colspan="3" align="left">
		省份：<script type='text/javascript'>
     			writeRegionName(${vehicleInfo.PROVINCE});
 			</script>
	&nbsp;&nbsp;&nbsp;
		地级市：<script type='text/javascript'>
     			writeRegionName(${vehicleInfo.CITY});
 			</script>&nbsp;&nbsp;&nbsp;
		区、县 :<script type='text/javascript'>
     			writeRegionName(${vehicleInfo.TOWN});
 			</script>&nbsp;&nbsp;&nbsp;
		</td>
	</tr>
	
	<tr>
		<td align="right">详细地址：</td>
		<td colspan="3" align="left">${vehicleInfo.ADDRESS}</td>
	</tr>
</table>
</div>
<table class="table_edit" align="center" id="displancement_table">
	<tr>
		<th colspan="4"><img class="nav" src="<%=contextPath%>/img/subNav.gif" />旧车信息</th>
	</tr>
	<tr>
		<td align="right" width="20%">二手车置换类型：</td>
		<td align="left" width="30%">
		<script type="text/javascript">
					genSelBoxExp("displacement_type",<%=Constant.DisplancementCarrequ_replace%>,"",false,"short_sel",'onchange=\"doCusChange()\"',"false",'');
			</script>
		</td>
		<td align="right" width="20%">品牌：</td>
		<td align="left" width="30%"><input type="text" class="middle_txt" id="OLD_BRAND_NAME" name="OLD_BRAND_NAME"   />&nbsp;<font color="red">*</font>	
		</td>
	</tr>
	</table>
	<div id="newCar" style="">
	<table  class="table_edit" align="center" id="newCar">
	<tr>
		<td align="right" width="20%">旧车VIN号：</td>
		<td align="left" width="30%"><input type="text" class="middle_txt" id="NEW_VIN" name="NEW_VIN"    />&nbsp;<font color="red">*</font></td>
		<td align="right" width="20%">旧车型号：</td>
		<td align="left" width="30"><input type="text" class="middle_txt" id="NEW_MODEL_NAME1" name="NEW_MODEL_NAME1"  />&nbsp;<font color="red">*</font></td>
	</tr>
	<tr>
		<td align="right" width="20%">销售日期：</td>
		<td align="left" width="30%"><input  id="NEW_SALES_DATE1" name="NEW_SALES_DATE1"  type="text" class="short_txt"     hasbtn="true" callFunction="showcalendar(event, 'NEW_SALES_DATE1', false);" readonly="readonly"/>&nbsp;<font color="red">*</font></td>
		<td align="right" width="20%">返利价格：</td>
		<td align="left" width="30"><input type="text" id="fanliPrc" class="middle_txt" name="fanliPrc"  value="${returnRrice}" readonly="readonly"/></td>
	</tr>
	<tr>
		<td align="right" width="20%">车主姓名：</td>
		<td align="left" width="30%"><input type="text" class="middle_txt" id="CTM_NAME" name="CTM_NAME" />&nbsp;<font color="red">*</font></td>
		<td></td>
		<td></td>
	</tr>
	</table>
	</div>
	<div id="newZuofei" style="">
	<table  class="table_edit" align="center" id="newZuofei">
	<tr>
	  <td align="right" width="20%">旧车VIN号：</td>
		<td align="left" width="30%"><input type="text" class="middle_txt" id="NEW_VIN2" name="NEW_VIN2"  value=""  />&nbsp;<font color="red">*</font></td>
		<td align="right" width="20%">旧车型号：</td>
		<td align="left" width="30"><input type="text" class="middle_txt" id="NEW_MODEL_NAME2" name="NEW_MODEL_NAME2" /></td>
	</tr>
	<tr>
		<td align="right" width="20%">报废证明编号：</td>
		<td align="left" width="30%"><input type="text" class="middle_txt" id="SCRAP_CERTIFY_NO" name="SCRAP_CERTIFY_NO"  value=""  />&nbsp;<font color="red">*</font></td>
		<td align="right" width="20%">车辆报废时间：</td>
		<td align="left" width="30%"><input name="SCRAP_DATE" id="SCRAP_DATE" type="text" class="short_txt" value="" readonly="readonly"  hasbtn="true" callFunction="showcalendar(event, 'SCRAP_DATE', false);" />&nbsp;<font color="red">*</font></td>
	</tr>
	<tr>
	  <td align="right" width="20%">销售日期：</td>
	  <td align="left" width="30%"><input id="NEW_SALES_DATE2" name="NEW_SALES_DATE2" type="text" class="short_txt" value="" hasbtn="true" callFunction="showcalendar(event, 'NEW_SALES_DATE2', false);" readonly="readonly"/></td>
	  <td align="right" width="20%">车主姓名：</td>
		<td align="left" width="30"><input type="text" class="middle_txt" id="CTM_NAME2" name="CTM_NAME2"  /></td>
		<td></td>
		<td></td>
	</tr>
	<tr>
		<td align="right" width="20%">返利价格：</td>
		<td align="left" width="30%"><input type="text" class="middle_txt" id="feifanliPrc" name="feifanliPrc"  readonly="readonly" /></td>
		<td align="right" width="20%"></td>
		<td align="left" width="30%"></td>
	</tr>
	</table>
	</div>
	<table  class="table_edit" align="center">
	<tr>
		<td align="right" width="20%">备注：</td>
		<td align="left" width="80%" colspon="3"><textarea id="remark" name="remark" cols="80" rows="3" ></textarea></td>
	</tr>
</table>
	<table class="table_info" border="0" id="file">
	    <tr>
	        <th>附件列表：
				<span>
					<input type="button" class="cssbutton"  onclick="showUpload('<%=contextPath%>')" value ='添加附件'/>
				</span>
			</th>
		</tr>
		<tr>
			<td width="100%" colspan="2"><jsp:include page="${contextPath}/uploadDiv.jsp" /></td>
  		</tr>
	</table>
<table  class="table_edit" align="center" >
	<tr class="cssTable">
		<td align="center" colspan="4">
			<input type="button" name="mySubmit"  class="normal_btn" value="提交" onclick="Submit();" />
			<input type="button" name="mySubmit"  class="normal_btn" value="返回" onclick="returnSubmit();" />

		</td>
	</tr>
</table>

</form>
</div>
</body>
</html>