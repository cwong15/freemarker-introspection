[#ftl]

[#if foo > 123]
    foo text here
[#elseif bar!'nothing' == 'something']
    bar text here
[#else]
    everything else here
[/#if]

[#include "includedContent.html"]

Leave the embedded tab characters in the next 2 lines to test tab handling. First
line's tab is after the "An" and the second line's is at the beginning:

An 	example of simple variable reference: ${thing?upper_case?trim}
	Example of dot variable reference: ${this.thing?lower_case!'default_txt'}

[#if 0 < somevar && somevar < 10]
    Something.
[/#if]

Now for something complicated:
${((APR7MAILINGLIST_OJC_964.LastShoppingDate!'2013-12-04 12:30:45')?date("yyyy-MM-dd HH:mm:ss"))?string.short}            