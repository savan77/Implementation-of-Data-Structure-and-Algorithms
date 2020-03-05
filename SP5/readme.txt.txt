## Implementation of Topological Ordering using DFS ##

@author Savan Amitbhai Visalpara (sxv180069)
@author Srikumar Ramaswamy (sxr170016)

This class finds topological ordering of a graph using DFS. If a graph contains cycle it will return null.


## Instructions to Run the Code ##

1) Make sure you open terminal outside of sxr170016
2) Now, run following command to compile the code.
   "javac .\sxr170016\DFS.java"
3) Run following command to run the compiled code.
   "java sxr170016.DFS"


## Sample Run ##

Graph: n: 7, m: 7, directed: true, Edge weights: false
1 :  (1,2) (1,3)
2 :  (2,4)
3 :  (3,4)
4 :  (4,5)
5 :  (5,6)
6 :  (6,7)
7 :
______________________________________________
Topological Order:
1 3 2 4 5 6 7



______________________________________________
Graph: n: 7, m: 8, directed: true, Edge weights: false
1 :  (1,2) (1,3)
2 :  (2,4)
3 :  (3,4)
4 :  (4,5)
5 :  (5,1)
6 :  (6,7)
7 :  (7,6)
______________________________________________
Graph contains a cycle. No topological ordering is possible. (null returned)

*/