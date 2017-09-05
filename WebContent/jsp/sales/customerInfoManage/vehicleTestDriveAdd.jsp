<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.Constant"%>
<%@ taglib uri="/jstl/cout" prefix="c"%>
<%
	String contextPath = request.getContextPath();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<script type="text/javascript">
	function doInit(){
		loadcalendar();   //初始化时间控件
		genLocSel('txt1','txt2','txt3','','',''); // 加载省份城市和县
	}
</script>

</head>
<body onunload="javascript:destoryPrototype();">
	<div id="loader" style="position:absolute;z-index:200;background:#FFCC00;padding:1px;top:4px;display:none;display: none;"></div>
		<input type="hidden" id="sys_date__" name="sys_date__" value="2012,09,10,11,01,29" />
			

<title>试乘试驾新增</title>
<div class="wbox" id="wbox">
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif"/>&nbsp;当前位置： 整车销售 &gt; 客户信息管理 &gt; 试乘试驾新增</div>
<form id="fm" name="fm" method="post"><input type="hidden" name="curPage" id="curPage" value="1"/> 

<div id="customerInfoId">
<table class="table_edit" align="center" id="ctm_table_id">
	<tbody>
		<tr>
			<th colspan="4"><img class="nav" src="<%=contextPath%>/img/subNav.gif"/>试驾客户信息</th>
		</tr>
		<tr>
			<td width="15%" align="right" id="tcmtd">客户姓名：</td>
			<td width="35%" align="left">
				<input id="ctm_name" name="ctm_name" value="" type="text" size="10" datatype="0,is_textarea,50" maxlength="50"/> 
			</td>
			<td width="15%" align="right" id="sextd">性别：</td>
			<td align="left">
				<script type="text/javascript">
					genSelBoxExp("sex",<%=Constant.GENDER_TYPE%>,"",false,"short_sel",'',"false",'');
				</script>
				 
			</td>
		</tr>
</tbody></table>
<table class="table_edit" align="center" id="ctm_table_id_2">
	<tbody>
		<tr>
			<td width="15%" align="right">证件类别：</td>
			<td width="35%" align="left">
				<script type="text/javascript">
					genSelBoxExp("card_type",<%=Constant.CARD_TYPE%>,"",false,"short_sel",'',"false",'');
				</script>
			</td>
			<td width="15%" align="right">证件号码：</td>
			<td width="35%" align="left">
				<input id="card_num" name="card_num" value="" type="text" class="middle_txt" size="20" datatype="0,isIdentityNumber,30" maxlength="30"/>
			</td>
		</tr>
	<tr>
		<td align="right">主要联系电话：</td>
		<td align="left">
			<input id="main_phone" name="main_phone" value="" type="text" class="middle_txt" size="20"  maxlength="15" datatype="0,is_phone,50"/>
		</td>
		<td align="right">其他联系电话：</td>
		<td align="left">
			<input id="other_phone" value="" name="other_phone" type="text" class="middle_txt" size="20" maxlength="15" datatype="1,is_phone,50"/>
		</td>
	</tr>
	<tr>
		<td align="right">电子邮件：</td>
		<td align="left">
			<input id="email" name="email" value="" type="text" class="middle_txt" size="20" datatype="1,is_email,70" maxlength="70"/>
		</td>
		<td align="right">邮编：</td>
		<td align="left">
			<input id="post_code" name="post_code" value="" type="text" class="middle_txt" size="20" datatype="1,is_digit,10" maxlength="10"/>
		</td>
	</tr>
	<tr>
		<td align="right">出生年月：</td>
		<td align="left">
			<input name="birthday" id="birthday" value="" type="text" class="short_txt" datatype="0,is_date,10" hasbtn="true" callFunction="showcalendar(event, 'birthday', false);" /></td>
		<td align="right">机动车驾车号：</td>
		<td align="left">
			<input type="text" name="vichleNo" id="vichleNo"/>
		</td>
		
	</tr>
	<tr>
		<td align="right">家庭月收入：</td>
		<td align="left">
			<script type="text/javascript">
				genSelBoxExp("income",<%=Constant.EARNING_MONTH%>,"",false,"short_sel",'',"false",'');
			</script>
			<font color="red">*</font>
		</td>
		<td align="right">教育程度：</td>
		<td align="left">
			<script type="text/javascript">
				genSelBoxExp("education",<%=Constant.EDUCATION_TYPE%>,"",false,"short_sel",'',"false",'');
			</script>
			<font color="red">*</font>
		</td>
	</tr>
	<tr>
		<td align="right">所在行业：</td>
		<td align="left">
			<script type="text/javascript">
				genSelBoxExp("industry",<%=Constant.TRADE_TYPE%>,"",false,"short_sel",'',"false",'');
			</script>
			<font color="red">*</font>
		</td>
		<td align="right">婚姻状况：</td>
		<td align="left">
			<script type="text/javascript">
				genSelBoxExp("is_married",<%=Constant.MARRIAGE_TYPE%>,"",false,"short_sel",'',"false",'');
			</script>
			<font color="red">*</font>
		</td>
	</tr>
	<tr>
		<td align="right">职业：</td>
		<td align="left">
			<script type="text/javascript">
				genSelBoxExp("profession",<%=Constant.PROFESSION_TYPE%>,"",false,"short_sel",'',"false",'');
			</script>
			<font color="red">*</font>
		</td>
		<td align="right">职务：</td>
		<td align="left">
			<input id="job" name="job" value="" type="text" class="middle_txt" size="20" datatype="1,is_textarea,50" maxlength="50"/>
		</td>
	</tr>
	<tr>
		<td align="right">购买用途：</td>
		<td align="left">
			<script type="text/javascript">
				genSelBoxExp("salesaddress",<%=Constant.SALES_ADDRESS%>,"",false,"short_sel",'',"false",'');
			</script>
			<font color="red">*</font>
		</td>
		<td align="right">购买原因：</td>
		<td align="left">
			<script type="text/javascript">
				genSelBoxExp("salesreson",<%=Constant.SALES_RESON%>,"",false,"short_sel",'',"false",'');
			</script>
			<font color="red">*</font>
		</td>
	</tr>
	<tr>
		<td align="right">所在地：</td>
		<td colspan="3" align="left">
			省份：<select class="min_sel" id="txt1" name="province1"  onchange="_genCity(this,'txt2')"></select>
			地级市：<select class="min_sel" id="txt2" name="city1"  onchange="_genCity(this,'txt3')"></select>
			区、县<select class="min_sel" id="txt3" name="district1"></select><font color="red">*</font> 
		</td>
		
	</tr>
	<tr>
		<td align="right">了解途径：</td>
		<td align="left">
			<script type="text/javascript">
				genSelBoxExp("ctm_form",<%=Constant.KNOW_ADDRESS%>,"",false,"short_sel",'',"false",'<%=Constant.CUSTOMER_FROM_01%>,<%=Constant.CUSTOMER_FROM_03%>,<%=Constant.CUSTOMER_FROM_12%>');
			</script>
			<font color="red">*</font>
		</td>
		<td align="right">欲购车型：</td>
		<td width="35%" align="left">
			<input type="text" class="middle_txt" name="materialCode" size="15"  value="" id="materialCode" readonly="readonly" datatype="0,is_textarea,30"/>  
			<input type="hidden" class="middle_txt" name="materialId" size="15"  value="" id="materialId"/>
			<input name="button1" type="button" class="mini_btn" onclick="materialShow();" value="..."/>
			<input class="normal_btn" type="button" value="清空" onclick="clrTxt('materialCode');"/>
		</td>
	</tr>
	<tr>
		<td align="right">试车日期：</td>
		<td align="left">
			<input name="testDriveDate" id="testDriveDate" value="" type="text" class="short_txt" datatype="0,is_date,10" hasbtn="true" callFunction="showcalendar(event, 'testDriveDate', false);" />
		</td>
	</tr>
	<tr>
		<td align="right">详细地址：</td>
		<td colspan="3" align="left">
			<input id="address" name="address" type="text" size="80" value="" datatype="0,is_address,200" maxlength="200"/>
		</td>
	</tr>
	<tr>
		<td align="right">试驾体验感受：</td>
		<td colspan="3" align="left">
			<textarea rows="6" cols="40" name="feeling" id="feeling" datatype="0,is_textarea,400"></textarea>
		</td>
	</tr>
</tbody></table>
</div>
<table class="table_query" id="submitTable">
	<tbody><tr align="center">
		<td>		
			<input type="button" id="re_Id" value="保存" class="normal_btn" onclick="save();"/> 
			<input type="button" value="返 回" class="normal_btn" onclick="toGoBack();"/> 
		</td>
	</tr>
</tbody></table>
</form>
</div>

<script type="text/javascript">
	//欲购车型内容清空
	function clrTxt(valueId) {
		document.getElementById(valueId).value = '' ;
	}
	
	//添加物料信息，获取物料id MATERIAL_ID
	function materialShow(){
		showMaterial2('materialCode','materialId',' ','false',''); 
	}
	
	//试乘试驾新增信息保存
	function save(){
		//省份
	    var pro = document.getElementById("province1");
	    var pro1 = pro.selectedIndex;
	    var province =  pro.options[pro1].value;
	    //地级市
	    var cit = document.getElementById("city1");
	    var cit1 = cit.selectedIndex;
	    var city =  cit.options[cit1].value;
	    //区、县
	    var dis = document.getElementById("district1");
	    var dis1 = dis.selectedIndex;
	    var district =  dis.options[dis1].value;
		if((province!=""&&province!=null)&&(city!=""&&city!=null)&&(district!=""&&district!=null)){
			if(submitForm("fm")){
				var feelingValue=document.getElementById("feeling").value
				if(feelingValue==""||feelingValue==null||(feelingValue!=null&&feelingValue.trim()=="")){
					MyAlert("试驾体验感受不能为空！！")
					return;
				}
				MyConfirm("是否确定新增？",insert,'');
			}
		}else{
			MyAlert("所在地信息填写不能为空！");
		}
	}
	
	function insert(){
	   makeNomalFormCall('<%=contextPath%>/sales/customerInfoManage/VehicleTestDrive/addVehicleTestDrive.json',insertCall,'fm','');  
	}
	function insertCall(json){
	  if(json.flag!= null && json.flag== true) {
			///MyAlert("新增成功！");
			toGoBack();
		} else {
			MyAlert("新增失败！请联系管理员！");
		}
	}
	//返回
	function toGoBack() {
		window.location = "<%=contextPath%>/sales/customerInfoManage/VehicleTestDrive/vehicleTestDriveQueryInit.do";
	}
	//验证所在地信息
	function  verifyAddress(){
		
	}
</script>
</body>
</html>
