freemarker-introspection
========================

Project provides Freemarker template introspection capability.

This project is basically in a very rough proof-of-concept state. All code here
is for illustrative purposes only and cannot be relied on to be stable.

A good starting point is the TemplateIntrospector class, which creates the 
introspection tree. The VariableFinder class demonstrates how this tree can be
traversed. The VariableDiscoveryTests unit test shows how these classes can be 
invoked.

Building quick start:
---------------------

You must have Gradle installed to build this project.

To build the library: "gradle jar"

To build the Javadoc of the public interfaces: "gradle javadoc"

To generate the Eclipse project: "gradle eclipse"
