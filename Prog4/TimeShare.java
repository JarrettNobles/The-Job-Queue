import java.io.*;
import java.util.*;

public class TimeShare
{
	//declare the CPU global variables
	private static int usage = -1;
	private static int idle = 0;
	private static int clock = 1;

	//main method
	public static void main(String[] args)
	{
		Queue inputQ = new Queue();
		jobInput(args, inputQ);
		print();

		control(inputQ);
	}

	//Checks if the queue is empty and goes through each process if they are not.
	private static void control(Queue input)
	{
		//Queue job and queue for completion
		Queue job = new Queue();
		Queue completed = new Queue();

		
		while((!input.isEmpty()) || (!job.isEmpty()))
		{
			//checks if arrival time is the same as the clock time and moves it from input to job queue
			if(!input.isEmpty())
			{
				Job temp = (Job) input.front();
				if(temp.arrivalTime == clock)
				{
					job.enqueue(input.front());
					input.dequeue();
				}
			}
			
			//if job is completed it moves it from jobqueue to the completed queue and updates the clock
			if(!job.isEmpty())
			{
				Job temp = (Job) job.front();
				int run = temp.runTime;
				int start = temp.startTime;
				if((run == (clock - start)) && (start != 0))
				{
					temp.turnTime = temp.waitTime + run;
         				 completed.enqueue(temp);
         				 job.dequeue();
				}
			}

			//if job hasnt been started yet, arrival time is less than or equal to clock time, it starts the job
			//and updates the start time and wait time for the job
			if(!job.isEmpty())
			{
				Job temp = (Job) job.front();
				int arrival = temp.arrivalTime;
				int start = temp.startTime;
				if((arrival <= clock) && (start == -1))
				{
					temp.startTime = clock;
					temp.waitTime = clock - arrival;

					//change front value from change method
					changeFront(job,clock, (clock - arrival));
				}
			}

			//update CPU counters
			if((job.isEmpty()) && (!input.isEmpty()))
				idle++;
			else 
				usage++;
			clock++;	

		}

		double avgWait = results(completed);
		printCPU(avgWait);

	}	

	 //this method updates current first item in each queue with the current start time and wait time
  	 private static void changeFront(Queue q, int start, int wait)
 	 {
    
   		 Queue temp = new Queue();
   		 int num = 0;
   		 while(!q.isEmpty())
   		 {
   			 if(num == 0)
     			 {
      				 Job a = (Job) q.front();
       				 a.startTime = start;
     				 a.waitTime = wait;
             			 temp.enqueue(a);
     			 //end if  
     			 }
      
   			 else 
   			 {
       				 temp.enqueue(q.front());
   			 }
      
   			 q.dequeue();
      
   		 //end while  
  		 }
 		
		 while(!temp.isEmpty())
 		 {
  			 q.enqueue(temp.front());
  			 temp.dequeue();
  		 //endwhile   
  		 }  
  
 	 //end of method
	 }
   
	//print cpu method that prints info such as usage and idle time
	private static void printCPU(double avgWait)
	{
		int length;
		String waitTime = "Average Wait Time";
		String cpUsage = "CPU Usage";
		String cpuIdle = "CPU Idle";
		String cpuPercent = "CPU Usage (%)";
		System.out.println(); 
		
   		 for(length = 25; length > waitTime.length(); length--)
			System.out.print(" ");
		 System.out.println(waitTime + " => " + decimalFormat(avgWait));
		
   		 for(length = 25; length > cpUsage.length(); length--)
			System.out.print(" ");
		 System.out.println(cpUsage + " => " + decimalFormat((double)usage));
		
   		 for(length = 25; length > cpuIdle.length(); length--)
			System.out.print(" ");
		 System.out.println(cpuIdle + " => " + decimalFormat(idle));
		
   		 double percU = 100 * ( (double)usage / (usage + idle) );
		for(length = 25; length > cpuPercent.length(); length --)
			System.out.print(" ");
		System.out.println(cpuPercent + " => " + decimalFormat(percU) + "%");
		
   		 System.out.println();
		
	//end of method
	}
	
	//decimal format method
	private static String decimalFormat(double numb)
	{
		String format = String.format("%.2f", numb);
		return format;

	//end of method
	}

	//Takes job data from input file and adds it to the input queue
	private static void jobInput(String [] args, Queue q)
	{
		String fileName;
		ArrayList<Job> jobs = new ArrayList<Job>();
		if(args.length == 0)
		{
			fileName = "";
		}
		
		else
		{
			fileName = args[0];
		}
		
		int num = 0;
		try
		{
			Scanner fileScan = new Scanner(new File(fileName));
			while(fileScan.hasNext())
			{
				Scanner in = new Scanner(fileScan.nextLine());
				String jobID = in.next();
				int tArrive = in.nextInt();
				int tRun = in.nextInt();
				jobs.add(new Job(jobID, tArrive, tRun));
				num++;
			}
		}
		
		catch(IOException e )
		{
			System.out.println(e.getMessage());
		}
		
		//sorts and its add it to the queue
		sortJobs(jobs);
		for(int i = 0; i < jobs.size(); i++)
		{
			q.enqueue(jobs.get(i));
			
		}

	//end of method
	}
	
	//sorts all jobs by arrival time.
	private static void sortJobs(ArrayList<Job> jobs)
	{
		int minIndex;
		int index;
		int j;
		Job temp;
		
		for(index = 0; index <jobs.size() - 1; index++)
		{
			minIndex = index;
			for(j = minIndex + 1; j < jobs.size(); j++)
			{
				if(jobs.get(j).arrivalTime < jobs.get(minIndex).arrivalTime)
				{
					minIndex = j;
				}

			}
			
			if(minIndex != index)
			{
				temp = jobs.get(index);
				jobs.set(index, jobs.get(minIndex));
				jobs.set(minIndex, temp);
			}
		}
	//end of method
	}

	//Print method
	private static void print()
	{
	
		System.out.println("\n" + "Job Control Analysis : Summary Report");
		System.out.println("-------------------------------------");
		System.out.println("job id arivval start run  wait   turnaround");
		System.out.println("       time    time  time  time   time");
		System.out.println("------ ------- ----- ----  ------ --------");
			
	}

	//returns results
	private static double results(Queue q)
	{
		int loops = 0;
		int waitTotal = 0;
		double avgWait;
		while(!q.isEmpty())
		{
			Job curr = (Job) q.front();
			String id = curr.jobName;
			int arrival = curr.arrivalTime;
			int start = curr.startTime;
			int run = curr.runTime;
			int wait = curr.waitTime;
			int turn = curr.turnTime;
					
			System.out.print(id + "      ");
			if(arrival < 10)
				System.out.print(" ");
			System.out.print(arrival + "  ");
			if(start < 10)
				System.out.print(" ");
			System.out.print(start + "    ");
			if(run < 10)
				System.out.print(" ");
			System.out.print(run + "     ");
			if(wait < 10)
				System.out.print(" ");
			System.out.print(wait + "       ");
			if(turn < 10)
				System.out.print(" ");
			System.out.print(turn + "\n");
			
			waitTotal += wait;
			loops++;
			q.dequeue();
		
		}
		
		avgWait = (double)waitTotal / loops;
		return avgWait;


	}	


//end of class
}
