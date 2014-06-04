[#ftl]

[#if foo > 123]
    foo text here
[#elseif bar!'nothing' == 'something']
    bar text here
[#else]
    everything else here
[/#if]

Example of simple variable reference: ${thing?upper_case?trim}
Example of dot variable reference: ${this.thing?upper_case!'default_txt'}            