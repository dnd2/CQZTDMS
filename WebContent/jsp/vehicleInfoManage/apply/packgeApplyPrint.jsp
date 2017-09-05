<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.Constant" %>
<%@ page import="com.infodms.dms.po.FsFileuploadPO"%>
<%@ page import="com.infodms.dms.po.TtAsBarcodeApplyPO"%>
<%@page import="java.util.LinkedList"%>
<%@ page import="java.util.List" %>
<%@ page import=" com.infodms.dms.util.CommonUtils" %>
<%@taglib uri="/jstl/cout" prefix="c"%>
<%@taglib uri="/jstl/fmt" prefix="fmt"%>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<%@page import="java.text.DecimalFormat"%>
<style media=print>
/* 应用这个样式的在打印时隐藏 */
.Noprint {
	display: none;
}

/* 应用这个样式的，从那个标签结束开始另算一页，之后在遇到再起一页，以此类推 */
hr {
	page-break-after: always;
}
</style>

<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>索赔申请单打印</title>
<% String contextPath = request.getContextPath(); 
	List<FsFileuploadPO> fileList = (LinkedList<FsFileuploadPO>)request.getAttribute("fileList");
	request.setAttribute("fileList",fileList);
	%>
<script type="text/javascript">
	function doInit(){
   		loadcalendar();  //初始化时间控件
   		doCusChange();
	}

</script>
</head>
<body onload="doInit()">
	<object classid="CLSID:8856F961-340A-11D0-A96B-00C04FD705A2" height="0" id="wb" name="wb" width="3"></object>
	<form name="fm" id="fm">
<center>
		<table  style="border-collapse: collapse;"  width="800px">
		<tr >
			<td align="center" width="90%" style="font-size: 25px" >
			家用汽车三包凭证(正面)
			</td>
			<td align="right" width="10%" style="font-size: 25px" >
			<span style="border: 2px solid;font-size: 20px" >补办</span>
			</td>
		</tr>
		</table>
	<br />      
	<table class="tab_printsep2" width="800px" >
		<tr  height="50px;" >
			<td colspan="2"  align="left"  style="font-size: 20px" >三包凭证编号：${bean.vin}</td>
		</tr>
		<tr height="30px;">
			<td colspan="2" align="left" style="font-size: 20px" >产品信息</td>
		</tr>
		<tr height="50px;">
			<td align="left" width="50%" style="font-size: 20px" >产品品牌： 
				<input type="checkbox" onclick="return false;"  /><span style="font-size: 15px">昌河汽车</span>
				&nbsp;&nbsp;
				<input type="checkbox" onclick="return false;"  /><span style="font-size: 15px">昌河铃木</span>
			</td>
			<td align="left" width="50%" style="font-size: 20px" >型号： ${bean.modelCode }</td>
		</tr>
		<tr height="50px;">
			<td align="left" width="50%" style="font-size: 20px" >车辆类型: &nbsp;&nbsp;
				<input type="checkbox" onclick="return false;"  /><span style="font-size: 15px">普通乘用车</span>
				<input type="checkbox" onclick="return false;"  /><span style="font-size: 15px">多用途乘用车</span>
			</td>
			<td align="left" width="50%" style="font-size: 20px" >车辆规格: 
				<input type="checkbox" onclick="return false;"  /><span style="font-size: 15px">小型载客汽车</span>
				<input type="checkbox" onclick="return false;"  /><span style="font-size: 15px">微型载客汽车</span>
			</td>
		</tr>
		<tr height="50px;">
			<td align="left" width="50%" style="font-size: 20px" >车辆识别码： 
			${bean.vin }
			</td>
			<td align="left" width="50%" style="font-size: 20px" >生产日期： 
			${bean.productDates }</td>
		</tr>
		<tr height="30px;">
			<td colspan="2" align="left" style="font-size: 20px" >生产者信息</td>
		</tr>
		<tr height="50px;">
			<td align="left" width="50%" style="font-size: 20px" >名称： 
			${name}
			</td>
			<td align="left" width="50%" style="font-size: 20px" >邮政编码： 
			${fax }</td>
		</tr>
		<tr height="50px;">
			<td align="left" width="50%" style="font-size: 20px" >地址：
			${address}
			</td>
			<td align="left" width="50%" style="font-size: 20px" >客服电话： 
			4008879986&nbsp;&nbsp;&nbsp;4008879988</td>
		</tr>
		<tr height="30px;">
			<td colspan="2" align="left" style="font-size: 20px" >销售者信息</td>
		</tr>
		<tr height="50px;">
			<td align="left" width="50%" style="font-size: 20px" >名称： 
			${bean.dealerName}
			</td>
			<td align="left" width="50%" style="font-size: 20px" >邮政编码： 
			${bean.zipCode }</td>
		</tr>
		<tr height="50px;">
			<td align="left" width="50%" style="font-size: 20px" >地址： 
			${bean.address}
			</td>
			<td align="left" width="50%" style="font-size: 20px" >电话： 
			${bean.phone }</td>
		</tr>
		<tr height="50px;" >
			<td align="left" width="50%" style="font-size: 20px" >销售日期： 
			${bean.saleDate}
			</td>
			<td align="left" width="50%" style="font-size: 20px" > &nbsp;
			</td>
		</tr>
		<tr height="30px;">
			<td colspan="2" align="left" style="font-size: 20px" >三包条款</td>
		</tr>
		<tr height="50px;">
			<td colspan="2" align="left" style="font-size: 20px" >
			 汽车产品包修期：3年或者60000公里(时间和里程以先到达者为准)
			</td>
		</tr>
		<tr height="50px;">
			<td colspan="2" align="left" style="font-size: 20px" >
			 汽车产品三包有效期：2年或者50000公里(时间和里程以先到达者为准)
			</td>
		</tr>
		<tr>
			<td colspan="2"  align="left" style="font-size: 20px" >
			其他三包责任承若：<br />
			1, 以下零件包修期为3年或者80000公里(时间和里程以先到达者为准)<br />
			&nbsp;&nbsp;&nbsp;&nbsp;气门室罩,缸盖,凸轮轴,进气门,排气门,气门弹簧,气门锁片,缸体,活塞,活塞销,活塞销卡簧,<br />
			&nbsp;&nbsp;&nbsp;&nbsp;连杆,曲轴,定位套,定位销,油底壳,集油器,支承盖,发动机支架,发电机支架,排气歧管隔排气<br />
			&nbsp;&nbsp;&nbsp;&nbsp;歧管隔热罩总成,变速器壳体<br />
			2, 以下零件包修期与《用户手册》规定的保养更换周期一致(时间和里程以先到达者为准)<br />
			&nbsp;&nbsp;&nbsp;&nbsp;制动液,机油,变速器齿轮轴,制冷剂,冷却液<br />
			3, 在质量保修期内,由于零件制造产生的质量问题而检修发电机,变速器,制动部件,散热器和
			&nbsp;&nbsp;&nbsp;&nbsp;空调系统等作业,引起更换各类辅助料如润滑油,润滑脂,冷却液,制冷剂,油封等材料,按保
			&nbsp;&nbsp;&nbsp;&nbsp;修处理
			</td>
		</tr>
		<tr height="80px;">
			<td colspan="2"  align="left" style="font-size: 25px" >
			签章：
			</td>
		</tr>
	</table>
	<hr />
	
	<div  style="font-size: 25px; font-weight: bold;">家用汽车三包凭证(背面)</div>   
	<br />      <br />
	<div  style="font-size: 25px; font-weight: bold;">主要总成和系统及其主要零件种类范围</div>   
	<br />
	<table class="tab_printsep2" width="800px" >
		<tr  height="50px;" >
		<td align="center" width="30%" style="font-size: 20px" >总成和系统
			</td>
			<td align=""center"" width="70%" style="font-size: 20px" >
			主要零件的种类范围
			</td>
		</tr>
		<tr  height="50px;" align="left">
			<td align="center" width="20%" style="font-size: 20px" >发电机 </td>
			<td align=""left"" width="80%" style="font-size: 20px" >
			曲轴,主轴承,连杆,连杆轴承,活塞,活塞环,活塞销,汽缸盖,凸轮轴,气门,汽缸体
			</td>
		</tr>
		<tr  height="50px;"align="left" >
			<td align="center" width="20%" style="font-size: 20px" >变速器 </td>
			<td align=""left"" width="80%" style="font-size: 20px" >
			箱体,齿轮,轴承,箱内动力传动元件(含离合器,制动器),差速器(前驱)
			</td>
		</tr>
		<tr  height="50px;"align="left" >
			<td align="center" width="20%" style="font-size: 20px" >转向系统 </td>
			<td align=""left"" width="80%" style="font-size: 20px" >
			转向机总成,转向柱,转向万向节,转向拉杆(不含球头),转向节
			</td>
		</tr>
		<tr  height="50px;"  align="left">
			<td align="center" width="20%" style="font-size: 20px" >制动系统 </td>
			<td align=""left"" width="80%" style="font-size: 20px" >
			制动主缸,轮缸,助力器,制动踏板及其支架
			</td>
		</tr>
		<tr  height="50px;"  align="left">
			<td align="center" width="20%" style="font-size: 20px" >悬架系统 </td>
			<td align=""left"" width="80%" style="font-size: 20px" >
			减震器,稳定杆,控制臂,连杆,钢板弹簧
			</td>
		</tr>
		<tr  height="50px;"  align="left">
			<td align="center" width="20%" style="font-size: 20px" >前/后桥 </td>
			<td align=""left"" width="80%" style="font-size: 20px" >
			主减速器及差速器(后驱),后轴/后桥,(驱动)传动轴
			</td>
		</tr>
		<tr  height="50px;"  align="left">
			<td align="center" width="20%" style="font-size: 20px" >车身(车架) </td>
			<td align=""left"" width="80%" style="font-size: 20px" >
			车身骨架,副车架,纵梁,横梁,前后车门本体
			</td>
		</tr>
	</table>
	<br />
	<div  style="font-size: 20px; font-weight: bold;">易损耗零部件种类范围及质量保证期(时间和里程以先到达者为准)</div>   <br />
	<table class="tab_printsep2" width="800px" >
		<tr  height="50px;" >
		<td align="center" width="75%" style="font-size: 20px" >易损耗零部件
			</td>
			<td align=""center"" width="25%" style="font-size: 20px" >
			质量保证期
			</td>
		</tr>
		<tr  height="50px;" align="left">
			<td align=""left"" width="75%" style="font-size: 20px" >
			空气滤清器,空调滤清器,机油滤清器,燃油滤清器,火花塞,制动衬片,离合器片,轮胎,遥控器电池,灯泡,雨刮器刮片,保险丝
			</td>
			<td align="center" width="25%" style="font-size: 20px" >3个月/5000公里 </td>
		</tr>
		<tr  height="50px;" align="left">
			<td align=""left"" width="75%" style="font-size: 20px" >
			蓄电池,普通继电器(不含集成控制单元)
			</td>
			<td align="center" width="25%" style="font-size: 20px" >12个月/30000公里 </td>
		</tr>
		</table>
	<br />
	<table class="tab_printsep2" width="800px" >
		<tr  height="50px;" >
			<td align="left"   style="font-size: 20px" >
			退换车的使用补偿系数及其计算公式：<br />
			<strong>补偿费用 = [购车价格(元)×行驶里程(km)/1000]×0.8%</strong>
			</td>
		</tr>
		<tr  height="50px;" >
			<td align="left"   style="font-size: 20px" >
			需要根据车辆识别代码定制的特殊零部件种类范围：<br />
			<strong> 售后条形码,车辆铭牌,VIN条码,车壳,钥匙,防盗控制模块,发电机控制模块,无钥匙启动和进入控制模块,发电机线束,仪表板线束
			</strong>
			</td>
		</tr>
		</table><br />
		
		
		<table  style="border-collapse: collapse;"  width="800px">
		<tr   >
			<td align="left"  style="font-size: 15px" >
			注:&nbsp;&nbsp;&nbsp;1,需要根据车辆识别代号(VIN)定制的特殊零部件的运输时间,外出救援路途所占用的时间不计入维修时间。<br />
		    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;2,四轮定位,灯光调整,动平衡等属于正常维修保养范畴的不在整车质量保修范围。<br />
		    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;3,请妥善保管三包凭证,如不慎丢失,请及时向经销商申请补办。<br />
		    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;4,其他事宜见随车《保修手册》<br />
			</td>
		</tr>
		</table>
	<table width="100%" cellpadding="1"  class="Noprint">
		<tr>
			<td width="100%" height="25" colspan="3"><div id="kpr"
					align="center">
					<input class="ipt" type="button" value="打印"
						onclick="javascript:printit();" /> <input class="ipt"
						type="button" value="打印页面设置" onclick="javascript:printsetup();" />
					<input class="ipt" type="button" value="打印预览"
						onclick="javascript:printpreview();" />
			</td>
		</tr>
	</table>
	</center>
	</form>
	<script language="javascript">
		function printsetup() {
			wb.execwb(8, 1); // 打印页面设置 
		}
		function printpreview() {
			wb.execwb(7, 1); // 打印页面预览       
		}
		function printit() {
			if (confirm('确定打印吗？')) {
				wb.execwb(6, 6)
			}
		}
	</script>
</body>
</html>

