# freemarker-introspection: Freemarker parse tree introspection

This project provides Freemarker template introspection capability.

This project is basically in a very rough proof-of-concept state. All code here
is for illustrative purposes only and cannot be relied on to be stable.

The starting point is the TemplateIntrospector class, which creates the 
introspection tree. With the root node of the tree in hand, you can then navigate
the tree to discover its structure. You can either directly navigate each node's
children and param properties or use the visitor pattern.

This project provides 2 consumers of this introspection tree: 

* The VariableFinder class, implementing the visitor interfaces, will walk the
tree to find information on model variables. This provides information on what
variables the template will request from the template model.
* The TemplateEditor class, given the original template text, allows you to 
specify template nodes and replacement text. The output from this class is
the modified template text with substitutions made.


## Building quick start:

You must have Gradle installed to build this project.

To build the library: "gradle jar"

To build the Javadoc of the public interfaces: "gradle javadoc"

To generate the Eclipse project: "gradle eclipse"
