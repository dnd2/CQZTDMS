<%@page import="java.text.SimpleDateFormat"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.Constant"%>
<%@ page import="com.infodms.dms.util.CommonUtils"%>
<%@ page import="com.infodms.dms.po.TtDealerActualSalesPO"%>
<%@ page import="com.infodms.dms.po.TtCustomerPO"%>
<%@ page import="com.infodms.dms.po.FsFileuploadPO"%>
<%@page import="java.util.LinkedList"%>
<%@taglib uri="/jstl/cout" prefix="c"%>
<%@ page import="java.util.Map"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.Calendar" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%
	String contextPath = request.getContextPath();
    Map<String,Object>  wareHouseIdList = (Map<String,Object>)request.getAttribute("wareHouseIdList");
    String PROV_CODE = wareHouseIdList.get("PROV_CODE").toString();
    String CITY_CODE = wareHouseIdList.get("CITY_CODE").toString();
    String COUNTY_CODE = wareHouseIdList.get("COUNTY_CODE").toString();
    String STATUS = wareHouseIdList.get("STATUS").toString();
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<script type="text/javascript">
	var prov =<%=PROV_CODE%>;
	var city =<%=CITY_CODE%>;
	var county =<%=COUNTY_CODE%>;
	var STATUS =<%=STATUS%>;
	function doInit(){

		genLocSel('txt4','txt5','txt6','','',''); // 加载省份城市和县
		//setJson("1");
		var text4 = document.getElementById("txt4");
		var text5 = document.getElementById("txt5");
		var text6 = document.getElementById("txt6");
		var status = document.getElementById("status");
		mySel(text4,prov);
		_regionCity(text4,'txt5')
		mySel(text5,city);
		_regionCity(txt5,'txt6')
		mySel(text6,county);
		mySel(status,STATUS);
	}
	
	function mySel(obj,value){
        for(var i=0;i<obj.options.length;i++){
            if(obj.options[i].value==value){
                //ops.options[i].selected="selected";
                obj.options[i].setAttribute("selected","true");               
                break;
            }
        }
    }
	
	function doSave(){
		var company_man = document.getElementById("company_man").value;
		if(company_man==null||company_man==''){
			MyAlert ("联系人未填");
			return;
		}
		
		var company_tel = document.getElementById("company_tel").value;
		if(company_tel==null||company_tel==''){
			MyAlert ("联系电话未填");
			return;
		}
		if(company_tel.length==11){ //手机号码检测
			var isPhone = /^(((13[0-9]{1})|(15[0-9]{1})|(18[0-9]{1})|(17[0,1,7]{1}))+\d{8})$/;
	        if(!isPhone.test(company_tel)){
	            MyAlert ('请输入有效的手机号码！');
	            return ;
	        }
		}
		
		var house_name = document.getElementById("house_name").value;
		if(house_name==null||house_name==''){
			MyAlert ("仓库名未填");
			return;
		}
		
		var house_code = document.getElementById("house_code").value;
		if(house_code==null||house_code==''){
			MyAlert ("仓库代码未填");
			return;
		}
		if(checkStringType(house_code)){
			MyAlert ("仓库代码只能是英文与数字");
			return;
		}	
		
		var area = document.getElementById("area").value;
		if(area==null||area==''){
			MyAlert ("请选择区域");
			return;
		}
		
		var houseType = document.getElementById("houseType").value;
		if(houseType==null||houseType==''){
			MyAlert ("请选择仓库类型");
			return;
		}
		
		var txt4 = document.getElementById("txt4").value;
		if(txt4==null||txt4==''){
			MyAlert ("请选择省份");
			return;
		}
		
		var txt5 = document.getElementById("txt5").value;
		if(txt5==null||txt5==''){
			MyAlert ("请选择地级市");
			return;
		}
		
		var txt6 = document.getElementById("txt6").value;
		if(txt6==null||txt6==''){
			MyAlert ("请选择区、县");
			return;
		}
		
		var address = document.getElementById("address").value;
		if(address==null||address==''){
			MyAlert ("详细地址未填");
			return;
		}
		
		MyConfirm("是否保存?",doSavetAction);
	}
	
	//是否是只有数字与字符串
	function checkStringType(str){
		for(var i=0;i<str.length;i++){
			var ch = str.charAt(i);
			if(ch>='a'&&ch<='z'){
				continue;
			}
			else if(ch>='A'&&ch<='Z'){
				continue;
			}
			else if(ch>=0&&ch<=9){
				continue;
			}
			else{
				return true;
			}
		}
		return false;
	}
	
	function doSavetAction(){				
		var url = "<%=contextPath%>/sysmng/orgmng/WareHouseMng/wareHouseDoEditSave.json";
		sendAjax(url,doSaveBack,'fm');
	}
	
	function doSaveBack(json){
		history.back();
	}
	
</script>

<title>车厂仓库维护</title>
</head>
<body onunload='javascript:destoryPrototype();' onload="doInit();">
<div class="wbox"  id="wbox" >
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置：储运管理 &gt; 基础管理 &gt; 仓库维护</div>
<form id="fm" name="fm" method="post">
<input id="warehouse_id" name="warehouse_id" value="${wareHouseIdList.WAREHOUSE_ID }" type="hidden"   /> 

<div id="customerInfoId">

<table class="table_query table_list" class="center" id="ctm_table_id_2">
	<tr>
		<td class="right">联系人：</td>
		<td class="left"><input id="company_man" name="company_man" value="${wareHouseIdList.LINK_MAN }" type="text" class="middle_txt" size="20" 
			 /> <font color="red">*</font> </td>
		<td class="right">联系电话：</td>
		<td class="left"><input id="company_tel" value="${wareHouseIdList.TEL }" name="company_tel" type="text" class="middle_txt" size="20" datatype="1,is_digit,15"
			maxlength="11" /><font color="red">*</font> </td>
	</tr>
	
	<tr>
		<td class="right">仓库名：</td>
		<td class="left"><input id="house_name" name="house_name" value="${wareHouseIdList.WAREHOUSE_NAME }" type="text" class="middle_txt" size="20" 
			 /><font color="red">*</font></td>
		<td class="right">仓库代码：</td>			
		<td class="left">
		<div>${wareHouseIdList.WAREHOUSE_CODE }</div>
		<input id="house_code" value="${wareHouseIdList.WAREHOUSE_CODE }" name="house_code" type="hidden" class="middle_txt" size="20" 
			maxlength="15" /></font></td>
	</tr>
	
	<tr>
		<td class="right">区域：</td>
		<td class="left">
			<select name="area" id="area" class='u-select'>
				<c:forEach items="${areaList}" var="areaList" >
					<c:if test="${wareHouseIdList.AREA_ID==areaList.AREA_ID}">
						 <option selected="selected" value="${areaList.AREA_ID }">${areaList.AREA_NAME}</option>
					</c:if>
					<c:if test="${wareHouseIdList.AREA_ID!=areaList.AREA_ID}">
						 <option  value="${areaList.AREA_ID }">${areaList.AREA_NAME}</option>
					</c:if>
				</c:forEach> 
			</select>		
			<font color="red">*</font>	
		</td>
		<td class="right">仓库类型：</td>
		 <td class="left">
			<select name="houseType" id="houseType" class='u-select'>
				<c:forEach items="${houseTypeList}" var="houseTypeList" >
					<c:if test="${wareHouseIdList.WAREHOUSE_TYPE==houseTypeList.CODE_ID}">
						 <option selected="selected" value="${houseTypeList.CODE_ID }">${houseTypeList.CODE_DESC}</option>
					</c:if>
					<c:if test="${wareHouseIdList.WAREHOUSE_TYPE!=houseTypeList.CODE_ID}">
						 <option value="${houseTypeList.CODE_ID }">${houseTypeList.CODE_DESC}</option>
					</c:if>
						 
				</c:forEach> 
			</select>
			<font color="red">*</font>
		</td>
	</tr>
	
	<tr>
		<td class="right">状态：</td>
		<td class="left">
			<select name="status" id="status" class='u-select'>			
				<option value="10011001">有效</option>	
				<option value="10011002">无效</option>			
			</select>		
			<font color="red">*</font>	
		</td>
	</tr>
	
	<tr>
		<td class="right">所在地：</td>
		<td colspan="3" class="left">
			省份： <select  id="txt4"  name="province1"  onchange="_regionCity(this,'txt5')"  class="u-select"   style="width: 100px;"></select> 
			地级市： <select id="txt5"  name="city1"  onchange="_regionCity(this,'txt6')"  class="u-select"   style="width: 100px;"></select> 
			区、县 <select  id="txt6"  name="district1" class="u-select"  style="width: 100px;"></select><font color="red"></font>
		<font color="red">*</font>
		</td>
		
	</tr>
	<tr>
		<td class="right">详细地址：</td>
		<td colspan="3" class="left">
			<input id="address" name="address" type="text" size="80" value="${wareHouseIdList.ADDRESS}" datatype="0,is_textarea,200" maxlength="200" />
			<font color="red">*</font>
		</td>
	</tr>
</table>
</div>


<table class="table_query table_list" id="submitTable">
	<tr >
		<td class="center">
			<input type="button" id="re_Id" value="保存" class="u-button u-submit" onclick="doSave();" /> 
			<input type="button" value="返 回"  class="u-button u-reset"  onclick="history.back();" /> 
		</td>
	</tr>
</table>


</form>
</div>
</body>
</html>