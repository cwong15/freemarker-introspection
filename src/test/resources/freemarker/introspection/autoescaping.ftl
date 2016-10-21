[#ftl output_format='HTML']

This is markup that tests the autoescape directives separately from
everything.ftl because they cannot coexist with the legacy escape directive
being tested there.

[#autoesc]This ampersand will be escaped: ${"&"}[/#autoesc]
[#noautoesc]This text will not be escaped[/#noautoesc]

