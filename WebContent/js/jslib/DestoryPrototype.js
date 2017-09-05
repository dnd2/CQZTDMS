

function destoryPrototype(){
		var isPrototype = null;
	if( !Prototype ) return ;
	
	//remove Object
	removeExt(Object,['inspect','toJSON','toQueryString','toHTML','keys','values','clone',
		'isElement','isArray','isHash','isFunction','isString','isNumber','isUndefined']);
	
	//remove Function.prototype
	removeExt(Function.prototype,['argumentNames','bind','bindAsEventListener','curry'
		,'delay','wrap','methodize','defer']);
		
	//remove Date.protoype
	removeExt(Date.prototype,['toJSON']);
	
	//remove RegExp.prototype
	removeExt(RegExp,['escape']);
	removeExt(RegExp.prototype,['match']);
	
	//remove String
	removeExt(String.prototype.gsub,['prepareReplacement']);
	removeExt(String.prototype.escapeHTML,['div','text']);
	removeExt(String, ['interpret','specialChar']);
	removeExt(String.prototype, ['gsub','sub','scan','truncate','strip','stripTags','stripScripts'
		,'extractScripts','evalScripts','escapeHTML','unescapeHTML','toQueryParams','toArray','succ'
		,'times','camelize','capitalize','underscore','dasherize','inspect','toJSON','unfilterJSON'
		,'isJSON','evalJSON','include','startsWith','endsWith','empty','blank','interpolate','parseQuery']);


	//remove event
	removeExt(document,['fire','observe','stopObserving']);
	removeExt(Event,['isLeftClick','isMiddleClick','isRightClick','element','findElement','pointer'
		,'pointerX','pointerY','stop','observe','stopObserving','fire','cache','relatedTarget']);
	
	
	//remove array
	removeExt2(Array.prototype, Enumerable);
	
	
	//remove element
	removeExt2(Element.ClassNames.prototype, Enumerable);
	removeExt(Element.ClassNames.prototype, ['initialize','_each','set','add','remove','toString']);	
	removeExt2(Element, Element.Methods);
	removeExt2(Element.Methods, Element.Methods);
	
	removePrototype($break);
	$break = null;
	
	removePrototype($continue);
	$continue =null;
	
	removePrototype(Abstract);
	Abstract = null;
	
	removePrototype(Ajax);
	Ajax =null;
	
	removePrototype(Class);
	Class=null;
	
	removePrototype(Enumerable);
	Enumerable =null;
	
	removePrototype(Field);
	Field =null;
	
	removePrototype(Form);
	Form =null;
	
	removePrototype(Insertion);
	Insertion=null;
	
	removePrototype(Position);
	Position =null;
	
	removePrototype(Prototype);
	Prototype =null;
	
	removePrototype(Toggle);
	Toggle =null;
	
	removePrototype(Try);
	Try=null;
	
	removePrototype(Hash);
	Hash =null;
	
	removePrototype(ObjectRange);
	ObjectRange=null;
	
	removePrototype(PeriodicalExecuter);
	PeriodicalExecuter=null;
	
	removePrototype(Selector);
	Selector=null;
	
	removePrototype(Template);
	Template =null;
	
	removePrototype($);
	$ = null;
	removePrototype($$);
	$$=null;
	removePrototype($A);
	$A =null;
	removePrototype($F);
	$F=null;
	removePrototype($H);
	$H=null;
	removePrototype($R);
	$R=null;
	removePrototype($w);
	$w=null;
	
	removePrototype(Element);
	Element =null;

	
	//alert('destory ok!');
}

function removeExt(dest, source){
	try{
		for( var i=0;i<source.length; i++){
			if( dest && dest[source[i]] ) dest[source[i]]=null;
		}
	}catch(e){
		alert('ERROR:'+e.name+'='+e.message);
	}
}
function removeExt2(dest, source){
	try{
		for( var property in source ){
			if( dest && dest[property] ) dest[property]=null;
		}
	}catch(e){
		alert('ERROR:'+e.name+'='+e.message);
	}
}
function removePrototype(obj){
	if( typeof(obj) == 'function' ){
		obj = null;
	}else if( typeof(obj) == 'object' ){
		for(var pro in obj ){
			if( typeof(obj[pro]) == 'object' ) {
				removePrototype(obj[pro]); 
			}
			obj[pro] = null;			
		}		
	}else if( typeof(obj) == 'string' ){
		obj = null;
	}
}