[#ftl]

[#-- contains all FM element and expression types, apart from autoescape stuff --]

[#attempt]
   [#assign x=1 y=true]
   [#assign hash = {one : 1}]
   [#assign somelist = [1, 2, 3]]
   [#global gvar = 33]
[#recover]
   [#assign x][#compress]something[/#compress][/#assign]
[/#attempt]

[#macro mymacro mparam1 mparam2]
   [#local localvar = 1]
   [#nested]
   [#fallback]
   [#return]
[/#macro]

[#switch svar.subvar]
   [#case 'a']#{heh}[#break]
   [#case ick[99]][#break]
   [#default][#stop]
[/#switch]

[#if n!2 < 123 && m-n < -123]
   [#escape e as e?html]
      [#noescape]no escape here[/#noescape]
   [/#escape]
[#elseif n > 300 || o?? || !(oops?lower_case='qwer')]
   [#list 1..5 as i]
      ${i}
      [@dostuff 'param1'/]
      [#sep],
      [#else]
      no items
   [/#list]
   [#visit 'node' + '2'/]
   [#recurse 'node'/]
   [#flush]
[#else]
   [#setting locale='en_US' /]
   [#transform html_escape]
      ${.lang}
      ${mymacro()}
   [/#transform]
[/#if]

[#list 6..9]
   start
   [#items as i]
      item ${i}
   [/#items]
   end
[/#list]

[#function plus l r]
   [#return l + r]
[/#function]

[#outputformat 'XML']<text>This is XML</text>[/#outputformat]

[#import 'foo.ftl' as namespace/]
[#include 'oops.ftl']
