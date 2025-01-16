# Notes - Blake Sanders
## Java Fundamentals

- In Java, all methods are declared in a class
- Eight primitive types - four signed, two floating-point, char, boolean
- Four forms of switch statements
- String objects use Unicode
- Math class provides mathematical functions
- "System.out" and a Scanner with "System.in" is how to get IO

- Java is an object-oriented language, with objects and classes
  - all code is defined inside classes
- Methods are functions declared inside a class
  - main is one of them
- Static indicates that the method does not operate on any objects
- We declare many features as public or private
- A package is a set of related classes
- // initalizes a comment, there is also /* and */

- Java objects use constructors much like C, using the word "new Object"
- JShell lets you write and execute code bit by bit so that you can learn things without an inside

### Primitive Types 
- These are not objects
- Byte, short, int, and long are signed
  - have standard size on all machines
  - Long integers need the suffix L
  - Floating-point literals need an F suffix to be a float, otherwise they are a long
- Names of variables and methods start with lowercase, while names of classes start with uppercase
- Constants are used by using the keyword "final"
  - "static final" is used outside of a method

### Arithmetic Operators
1. If either of the operands is of type double, the other one is converted to double.
1. If either of the operands is of type float, the other one is converted to float.
1. If either of the operands is of type long, the other one is converted to long.
1. Otherwise, both operands are converted to int.
- The majority of operators work exactly like C

### Strings
- Connectation is possible, also with a delimiter if using "String.join"
- .substring(x,y) returns the substring of those indicies
- .split returns an array of strings seperated by a delimiter
- .equals checks for string equality, don't use ==
  - strings can be null
- .equalsIgnoreCase ignores the case when checking for string equality
- Integer.tostring - int -> string
- String.parseInt - string -> ints 
- The string class is immutable, so they return a new string when the method is called
- ASCII causes international problems, so Java uses Unicode
- """ are for text blocks - multi-line strings
  - \ can avoid line breaks

### Input and Output
- system.in needs a Scanner so that it reads more than bytes
- output works much like C with % characters
- there are different flags to format output, see chart 

### Control Flow
- If/else is exactly the same as C 
- switch statements are similar, but they have fall-through if there is not a yield or break statement
- cases can be followed by a -> for a one-line yield, or by a : or {} for multi-line
  - for multi-line, there must be a yield or break, or there will be fall-through
- For, while, do-while are the same as C 
- Scope is the same as Connectation

### Arrays and Array Lists
- ArrayList class is for arrays that grow and shrink on demand
  - Yet it does not use normal array syntax, it uses object syntax
  - Cannot use primitive types, so wrapper classes are needed
- Arrays need to be initalized with a constructor
- - one cannot access an out of bounds element
- Arrays can be initalized with a loop or curly braces
- Enhanced for loops allow us to iterate through an array
- Copying arrays is shallow, so the arrays.copyof method is needed
- declaring methods is much like declaring functions in C, just use "static"
  - methods can have a variable number of paramaters

### Processing Input and Output
#### Input/Output Streams, Readers, and Writers
- Similar file streams to C, it reads bytes with different methods
- There are different charsets for Unicode
- See the book for specific implementations

#### Paths, Files, and Directories
- See book for specific implementations
