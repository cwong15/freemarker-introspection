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
	All of these variables should be shadowed and not picked up
	${unknownvar4!123}
	${unknownvar4.foo}
	${unknownvar4.foo.bar}
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

<#list numVar7..numVar8 as x7>
	<#assign numvar9 = numvar9 + 1>
	<#assign x6 = '${unknownvar7}'>
</#list>

<#if unknownvar8?? || unknownvar9?has_content>
</#if>

Numeric builtins:
${numvar10?floor} should be ?abs, but not supported in older FM version 
${numvar11?floor} should be ?is_infinite, but not supported in older FM version 
${numvar12?floor} should be ?is_nan, but not supported in older FM version 
${numvar13?round} ${numvar14?floor} ${numvar15?ceiling}

Date builtins:
${datevar1?iso_utc} ${datevar2?iso_utc_nz} ${datevar3?iso_utc_ms}
${datevar4?iso_utc_ms_nz} ${datevar5?iso_utc_m} ${datevar6?iso_utc_m_nz}
${datevar7?iso_utc_h} ${datevar8?iso_utc_h_nz} ${datevar9?iso_local}
${datevar10?iso_local_nz} ${datevar11?iso_local_ms} ${datevar12?iso_local_ms_nz}
${datevar13?iso_local_m} ${datevar14?iso_local_m_nz} ${datevar15?iso_local_h}
${datevar16?iso_local_h_nz} ${datevar17?iso} ${datevar18?iso_nz} 
${datevar19?iso_ms} ${datevar20?iso_ms_nz} ${datevar21?iso_m}
${datevar22?iso_m_nz} ${datevar23?iso_h} ${datevar24?iso_h_nz}

String builtins:
${strvar7?substring(0)} ${strvar8?cap_first} ${strvar9?uncap_first}
${strvar10?capitalize} ${strvar11?chop_linebreak} ${strvar12?ends_with('x')}
${strvar13?html} ${strvar14?groups[0]} ${strvar15?index_of('x')}
${strvar16?j_string} ${strvar17?js_string} ${strvar18?json_string}
${strvar19?last_index_of('x')} ${strvar20?length} ${strvar21?lower_case}
${strvar22?left_pad(9)} ${strvar23?right_pad} ${strvar24?contains('x')}
${strvar25?matches('foo*')} ${strvar26?number} ${strvar27?replace('x', 'y')}
${strvar28?rtf} ${strvar29?url} ${strvar30?split('x')} ${strvar31?starts_with('x')}
${strvar32?trim} ${strvar33?upper_case} ${strvar34?word_list} ${strvar35?xhtml}
${strvar36?xml}

Unknown builtins:
${unknownvar10?has_content} ${unknownvar11?is_boolean} 