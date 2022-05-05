THE JOB QUEUE FINAL PROJECT
CSC205
DUE May 6th @ 10:00PM
1. I began the assignment by adding my code into dequeue in the Queue.java file
2. Start the time share file. Time share a input from the dat files and stores them to be processed. It prints the process in the table format shown below
Job Control Analysis : Summary Report
job id arrival start run wait turnaround
        time      time    time       time
------ ------- ----- ---- ---- ----------
 


it will also print average wait time, cpu usage, cpu idle, and prints cpu usage as a percentage.

TIMESHARE STEPS
1. Began by declaring 3 global integers usage, idle, and clock.
2. MAIN METHOD: Declared the Queue InputQ, called jobInput, called print, called control methods
3. Control: Control method  checks if the queue is empty and goes through each process if arrival time is the same as clock time,
if job is completed it moves it from job queue to completed queue a updates the clock, it will also judge whether or not the job hasnt been started yet and starts it.
4. ChangeFront: updates current first item in the queue with the current start time and wait time
5. PrintCPU: Prints all of the CPU information that is necessary such as usage, idle time, and usage percent time
6.DecimalFormat: Formats to 2 decimal places
7.jobInput:takes job input file and adds it to the queue
8.sortJobs: sorts jobs by arrival time
9.print: Print will print the menu shown above
10. Results: Prints the results of items in queue and calculates the necessary time info.
