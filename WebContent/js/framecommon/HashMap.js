/**
 * added by andy.ten@tom.com ，误删
 * ver:20090921_v0.1
 */

function HashMap()
{
    this.length = 0;
    this.prefix = "hashmap_prefix_20090921_";
}
/**
 *  HashMap中put方法实现
 */
HashMap.prototype.put = function (key, value)
{
    this[this.prefix + key] = value;
    this.length ++;
}
/**
 * HashMap中get方法实现
 */
HashMap.prototype.get = function(key)
{
    return typeof this[this.prefix + key] == "undefined" 
            ? null : this[this.prefix + key];
}
/**
 * HashMap中keySet方法
 */
HashMap.prototype.keySet = function()
{
    var arrKeySet = new Array();
    var index = 0;
    for(var strKey in this)
    {
        if(strKey.substring(0,this.prefix.length) == this.prefix)
            arrKeySet[index ++] = strKey.substring(this.prefix.length);
    }
    return arrKeySet.length == 0 ? null : arrKeySet;
}
/**
 * HashMap中values方法
 */
HashMap.prototype.values = function()
{
    var arrValues = new Array();
    var index = 0;
    for(var strKey in this)
    {
        if(strKey.substring(0,this.prefix.length) == this.prefix)
            arrValues[index ++] = this[strKey];
    }
    return arrValues.length == 0 ? null : arrValues;
}
/**
 * HashMap中size方法
 */
HashMap.prototype.size = function()
{
    return this.length;
}
/**
 * HashMap中remove方法
 */
HashMap.prototype.remove = function(key)
{
    delete this[this.prefix + key];
    this.length --;
}
/**
 * HashMap中clear方法
 */
HashMap.prototype.clear = function()
{
    for(var strKey in this)
    {
        if(strKey.substring(0,this.prefix.length) == this.prefix)
            delete this[strKey];   
    }
    this.length = 0;
}
/**
 * HashMap中isEmpty方法
 */
HashMap.prototype.isEmpty = function()
{
    return this.length == 0;
}
/**
 * HashMap中containsKey方法
 */
HashMap.prototype.containsKey = function(key)
{
    for(var strKey in this)
    {
       if(strKey == this.prefix + key)
          return true;  
    }
    return false;
}
/**
 * HashMap中containsValue方法
 */
HashMap.prototype.containsValue = function(value)
{
    for(var strKey in this)
    {
       if(this[strKey] == value)
          return true;  
    }
    return false;
}
/**
 * HashMap中putAll方法
 */
HashMap.prototype.putAll = function(map)
{
    if(map == null)
        return;
    if(map.constructor != JHashMap)
        return;
    var arrKey = map.keySet();
    var arrValue = map.values();
    for(var i in arrKey)
       this.put(arrKey[i],arrValue[i]);
}
//toString
HashMap.prototype.toString = function()
{
    var str = "";
    for(var strKey in this)
    {
        if(strKey.substring(0,this.prefix.length) == this.prefix)
              str += (str.length>0?",":"") + this[strKey];
    }
    return str;
}

//toString
HashMap.prototype.getString = function()
{
    var str = new Array(2);
    str[0]="";
    str[1]="";
    for(var strKey in this)
    {
        if(strKey.substring(0,this.prefix.length) == this.prefix)
        {
              str[0] += (str[0].length>0?",":"") + strKey.substring(this.prefix.length);
              str[1] += (str[1].length>0?",":"") + this[strKey];
        }
    }
    return str;
}