# java-dag-scheduler
Java task scheduler to execute threads which dependency is managed by directed acyclic graph. Carl Hewitt Actor Model is implemented to provide message passing.

TODO: to separate Actor Model as a separate project.

# Components
## Direct acyclic graph
The thread or task is described as vertex in direct acyclic graph. The dependency relationship is an edge between vertice. Here is a description of [DAG](https://en.wikipedia.org/wiki/Directed_acyclic_graph)

Topological sorting algorithm is implemented to prevent any cyclic loop.

## Task scheduler
Relationship described in DAG will be transformed to Task (Java Callable with dependency). These tasks will be launched. Message system is implemented to communicated between tasks.

## Actor model
Carl Hewitt's [actor model](https://en.wikipedia.org/wiki/Actor_model) is implemented to provide message passing among tasks. It is similar to Akka implementation in Java to mimic Erlang Actors. Since this is a light weight actor model with no interprocess communication, the implementation is a lot faster than Akka which uses socket.

# Tech
Java 8 compilation and runtime are required.
