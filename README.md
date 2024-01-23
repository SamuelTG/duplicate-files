# Duplicate Files -Algo Dev Assessment 2024

Explain how you went about choosing the number of threads for each run:
I chose the number of threads to use based on the root directories on the filesystem. This is so that the work could be share for each thread.

Explain why you choose to solve the problem in that way and your choices of all data:
While iterating through the filesystem:
- I use a stack to hold the current file/directory being worked on. that way I can remove/pop it from the stack. This is conveniet because if the stack is empty then I know there's no file or directories to check.
- I keep track of all the files found so far by
using a Hashmap(filesSeenAlready). This map uses the file's content as key and path as value. so if the same key/content is found then I know its a duplicate.

- To store and group the duplicates I used a Map again with file content as key and value as a Set<> of duplicate paths. I used a Set avoid having duplicate paths in the same grouping.


