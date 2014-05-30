[#ftl]

[#if foo > 123]
    foo text here
[#elseif bar!'nothing' == 'something']
    bar text here
[#else]
    everything else here
[/#if]

Example of simple variable reference: ${thing}
Example of dot variable reference: ${this.thing}            