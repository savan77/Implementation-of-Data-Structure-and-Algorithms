## Implementation of Bounded Queue ##

@author Savan Amitbhai Visalpara (sxv180069)
@author Srikumar Ramaswamy (sxr170016)


## Instructions to Run the Code ##

1) Make sure you open terminal outside of sxv180069/ 
2) Now, run following command to compile the code.
   "javac .\sxv180069\BoundedQueue.java"
3) Run following command to run the compiled code.
   "java sxv180069.BoundedQueue"


## Sample Input/Output ##


Enter the size of the queue..
3
---------------------------------
1. Offer an element
2. Poll
3. Peek
4. Find queue size
5. Check if queue is empty
6. Clear queue
7. Copy to array
8. Print queue
0. Exit
---------------------------------
Enter your choice...1
Enter the element to be added..
5
Queue contains:
5 null null

Enter your choice...1
Enter the element to be added..
9
Queue contains:
5 9 null

Enter your choice...1
Enter the element to be added..
3
Queue contains:
5 9 3

Enter your choice...2
5 removed from the queue.
Queue contains:
9 3 null

Enter your choice...4
Queue contains 2 elements

Enter your choice...5
Queue is not empty

Enter your choice...7
Copied array...

Array: 9 3 

Enter your choice...8
Queue contains:
9 3 null

Enter your choice...6
Cleared the queue.
Queue contains:
null null null
