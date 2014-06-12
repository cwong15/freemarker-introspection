This is a Freemarker template that tests typed variable discovery. All variables
that start with "boolvar", "numvar", "strvar" and "unknownvar" should be picked
up. Their data type should be inferred from their context and should match 
their respective name prefix (bool, num, str, unknown). No other variables in 
here should be picked up, because they are considered local to the template and 
do not access the model. Namespace, local and loop variable shadowing scenarios 
are also tested.

<#if numvar1 gt 1>
    ${unknownvar1}
<#elseif strvar1 == 'something'>
<#elseif boolvar1>
</#if>

${unknownvar2 + unknownvar3}

<#assign x1 = numvar2 + 123>
<#assign x2 = (numvar3 * 123)>
<#assign x3 = boolvar2 || boolvar3>

<#switch unknownvar3>
<#case "strvar2"> 
	The following will add 'oops' to strvar2's values
	${strvar2!'oops'}
<#case "numvar4">
	The following will add 88 and 99 to numvar4's values	
	${numvar4!88}
	${numvar4!99}
<#case "unknownvar3">
	${x1!'stuff'} ... should be shadowed
<#case "dots">
	${{'a':1, 'b':2, 'c':3}.a} ... not pure identifiers, should not be picked up
	${numvar5.key.subkey!123}
	${strvar3.key!'xyz'}
	${x1.key} ... should be shadowed		
<#default>	
	${numvar6 - 1}
	<@mymacro param1='paramval'/>
</#switch>

<#list [1,2,3] as x4>
	${x4} ... shadowed within list scope
	<#assign x5 = strvar4!''> ... strvar4 not shadowed
</#list>
${x5} ... shadowed from assignment in list above

<#list [4,5,6] as unknownvar4>
	${unknownvar4!123}
</#list>
${unknownvar4} ... not shadowed from assignment in list above

<#macro mymacro param1 param2='paramdefault' param3=1>
	<#local xstrvar5 = 'foo'>
	<#local xvar6>whatever</#local>
	<#local strvar6 = 'bar'>
	${xstrvar5} ... locally shadowed
	${strvar6!''} ... locally shadowed
	${xvar6} ... locally shadowed
	<#local foo1=1 foo2=2 foo3=3>
</#macro>

${strvar6!''} ... no longer shadowed

