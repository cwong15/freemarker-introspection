[#ftl]

[#if foo > 123]
    foo text here
[#elseif bar!'nothing' == 'something']
    bar text here
[#else]
    everything else here
[/#if]

[#include "includedContent.html"]

Example of simple variable reference: ${thing?upper_case?trim}
Example of dot variable reference: ${this.thing?lower_case!'default_txt'}            