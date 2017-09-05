<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>无标题文档</title>
<style type=text/css>
#tb td{
	height:30px; 
	border-bottom:1px solid #000000;
	border-right:1px solid #000000;
	}
.fixedTitle{line-height:30px;}
</style>
<link href="<%=request.getContextPath()%>/jsp/demo/Fixed.css" type="text/css" rel="stylesheet" />
<script src="<%=request.getContextPath()%>/jsp/demo/Fixed.js"></script>
</head>

<body>

<table cellspacing="0" id="tb" style="width:1000px;" >
	<tr>
		<td width="100">项目编号</td>
		<td width="100">项目名称</td>
		<td width="100">合同编号</td>
		<td width="100">业务类型</td>
		<td width="100">工程规模</td>
		<td width="100">委托日期</td>
		<td width="100">送审价</td>
		<td width="100">完成日期</td>
		<td width="100">定案价</td>
		<td width="100">核减价</td>
	</tr>
	<tr>
		<td>1</td>
		<td>1</td>
		<td>1</td>
		<td>1</td>
		<td>1</td>
		<td>1</td>
		<td>1</td>
		<td>1</td>
		<td>1</td>
		<td>1</td>
	</tr>
	<tr>
		<td>2</td>
		<td>2</td>
		<td>2</td>
		<td>2</td>
		<td>2</td>
		<td>2</td>
		<td>2</td>
		<td>2</td>
		<td>2</td>
		<td>2</td>
	</tr>
	<tr>
		<td>3</td>
		<td>3</td>
		<td>3</td>
		<td>3</td>
		<td>3</td>
		<td>3</td>
		<td>3</td>
		<td>3</td>
		<td>3</td>
		<td>3</td>
	</tr>
	<tr>
		<td>4</td>
		<td>4</td>
		<td>4</td>
		<td>4</td>
		<td>4</td>
		<td>4</td>
		<td>4</td>
		<td>4</td>
		<td>4</td>
		<td>4</td>
	</tr>
	<tr>
		<td>5</td>
		<td>5</td>
		<td>5</td>
		<td>5</td>
		<td>5</td>
		<td>5</td>
		<td>5</td>
		<td>5</td>
		<td>5</td>
		<td>5</td>
	</tr>
</table>
</body>
</html>

<script language=javascript>
var Options = {
	table  : "tb",
	width  : 800,
	height : 150,
	rows   : 1,
	cells  : 1
}
new Fixed(Options);
</script>