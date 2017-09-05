//================================================================================
function choiceFormat(pattern , number1){
	var	_choicePattern = "^[-+]?\\d+(\\.\\d*)?[#><][^|]*([|][-+]?\\d+(\\.\\d*)?[#><][^|]*)*$";
	var	_choiceItemPattern = "[-+]?\\d+(\\.\\d*)?[#><][^|]*";
	if ( !pattern || !(new RegExp(_choicePattern,'g').test(pattern)) ){
		alert('choiceFormat() invalide pattern parameter : "'+pattern+'"');
		return ;
	}
	if ( !isNumber(number1) ){
		alert('choiceFormat() invalide number parameter : "'+number1+'"');
		return ;
	}
	
	var strAry = pattern.match(new RegExp(_choiceItemPattern,'g'));
	if ( strAry && strAry.length>0 ){
		for(var i=0;i<strAry.length&&strAry[i];i++){
			tmp  =strAry[i].split(/[#><]/g);
			sign = strAry[i].charAt(tmp[0].length);
			if ( sign =='#' && tmp[0]== number1){
				return tmp[1];
			}
			if ( sign =='>' && tmp[0]>=number1 ){
				return tmp[1]
			}
			if ( sign =='<' && tmp[0]<number1 ){
				return tmp[1];
			}
		}
	}
	return '';
}
function amountFormatNew(val)
{
	if(val==0)
	{
	   return "0.00";
	}
	else
	{
	   return amountFormat(val);
	}
}

//format for ajax format invoke
//add by wangwenhu 2010-03-16
function amountFormat(val){
	//modify 2010-05-31 start
	if(!val) return "0"; //è§£å³undefinedæåµ
	//modify 2010-05-31 end
	return zeroFormat(numberFormat('#,###.00',val));
}
//when val is 0.00, change it as 0 format
//add by wangwenhu 2010-03-16
function zeroFormat(val){
	if('0.00'==val){
		return '0';
	}else{
		return val;
	}
}
//alert(choiceFormat('0#ok|1#true',0));
//alert(choiceFormat('0#ok|1#true','0'));

//=======================================================================================
function numberFormat(pattern , val){
	var oldVal = val;
	if(val < 0){
		val = -val;
	} 
	var _nfPattern = "[0#]+[,]?[0#]*(\\.[0#]+)?";
	if ( !pattern || !(new RegExp(_nfPattern,'g').test(pattern)) ){
		alert('numberFormat() invalide pattern parameter : "'+pattern+'"');
		return ;
	}
	if ( !isNumber((new String(val)).replace(".",""))){//modify by zhaolunda 2010-07-27
		alert('numberFormat() invalide number parameter : "'+val+'"');
		return ;
	}

	var strAry = pattern.match(new RegExp(_nfPattern,'g'));
	p1 = strAry[0].indexOf('.');
	if ( p1>0 ){
		len = strAry[0].length-p1-1;
		val = Math.round (val*Math.pow(10,len))/Math.pow(10,len);
	}else{
		val = Math.round(val);
	}	
	val = String(val);
	p2=val.indexOf('.');
	var ret='';
	if (p1>0){
		for(var i=p1+1;i<strAry[0].length;i++){
			ch = (p2>0)&&(p2+i-p1<val.length)?val.charAt(p2+i-p1):'';
			if ( strAry[0].charAt(i)=='0' ){
				ret = ret+(ch==''?'0':ch);
			}else{
				ret = ret+(ch==''?'':ch);				
			}
		}
		ret = ret==''?'':('.'+ret);
	}
	
	p3=strAry[0].indexOf(',');
	if( p3>0 ){
		len = p1>0?p1-p3-1:strAry[0].length-p3-1;
		len2 = p2>0?p2-1:val.length-1;
		for(var i=len2;i>=0;i--){
			ret = ((len2-i+1)%len==0?',':'')+val.charAt(i)+ret;
		}
		ret = ret.charAt(0)==','?ret.substring(1,ret.length):ret;//modify by zhaolunda 2010-07-27 原代码（ret = ret.charAt(0)==','?ret.substring(1,ret.length-1):ret;）
	}else{
		ret = val.substring(0,p2>0?p2:val.length)+ret;
	}
	if(oldVal < 0){
		ret = "-"+ret;
	}
	return pattern.replace(strAry[0],ret);
}
/*
alert('='+numberFormat('RMB #,###.##',1234.567));
alert('='+numberFormat('RMB #,###.##',1234));
alert('='+numberFormat('RMB #,###.00',1234.567));
alert('='+numberFormat('RMB #,###.00',1234));
alert('='+numberFormat('RMB #,###.00',0));
alert('='+numberFormat('RMB #,###.00','0'));
*/

//============================================================================

function dateFormat(pattern, dateString){
	var _datePattern = "(yyyy|MM|dd|HH|mm|ss)";
	var _dateString =  "^\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}$";
	if ( !pattern || !(new RegExp(_datePattern,'g')).test(pattern) ){
		alert('dateFormat() invalide pattern parameter : "'+pattern+'"');
		return ;
	}
	if ( !(new RegExp(_dateString,'g')).test(dateString) ){
		alert('dateFormat() invalide date parameter : "'+dateString+'"');
		return ;
	}
	var strAry = dateString.split(/[-:\s]/g);
	var ret = pattern ;
	ret = ret.replace(/yyyy/g,strAry[0]);
	ret = ret.replace(/MM/g,strAry[1]);
	ret = ret.replace(/dd/g,strAry[2]);
	ret = ret.replace(/HH/g,strAry[3]);
	ret = ret.replace(/mm/g,strAry[4]);
	ret = ret.replace(/ss/g,strAry[5]);	
	return ret;
}
//alert('='+dateFormat('yyyyMMdd-HH:mm:ssOK','2005-12-21 22:33:09'));



function messageFormat(pattern){
	var _msgPatter="{\\d+(,(number|choice|date),(\\S)+)?}"
	if ( !pattern || !new RegExp(_msgPatter,'g').test(pattern) ){
		return pattern;
	}
	
	var strAry = pattern.match(new RegExp(_msgPatter,'g'));
	var params = new Array(arguments.length-1);
	for(var i=1;i<arguments.length;i++){
		params[i-1]=arguments[i];
	}
	
	var ret = pattern;
	for(i=0;i<strAry.length;i++){
		strs = strAry[i].substring(1,strAry[i].length-1).split(',');
		idx = parseInt(strs[0]);
		if( strs.length>1 ){
			if (idx<params.length){
				tmp = eval(strs[1]+'Format(\''+strs[2]+'\',\''+params[idx]+'\')');
				ret=ret.replace(strAry[i],tmp);
			}
		}else{
			if ( idx<params.length ) ret=ret.replace(strAry[i],params[idx]);
		}
	}
	return ret;
}

//alert(messageFormat('{0,number,#.#0}  {1,date,yyyy}','123.236','2007-12-22 12:33:44','c'));

