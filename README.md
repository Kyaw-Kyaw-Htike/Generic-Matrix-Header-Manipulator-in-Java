# Generic-Matrix-Header-Manipulator-in-Java
Generic Matrix Header Manipulator in Java

This project contains a class named "MatrixHeader" that stores the information about a particular matrix such as the number of rows, the number of columns, the number of channels, and whether the matrix (being referred to) is a (continuous and discontinuous) slice (view) or a full matrix. If if is a slice, then information about the ranges of the slice, etc. are recorded. The header is generic in the sense that it does not contain any actual matrix data and therefore it can be attached to any standard Java array or matrix to help with operations. It could also be attached to any data structure that can store the underlying data of a matrix. This allows manipulating only the header without affecting the underlying data.

https://kyaw.xyz/2017/12/18/generic-matrix-header-manipulator-java

Copyright (C) 2017 Kyaw Kyaw Htike @ Ali Abdul Ghafur. All rights reserved.



Dr. Kyaw Kyaw Htike @ Ali Abdul Ghafur



https://kyaw.xyz
