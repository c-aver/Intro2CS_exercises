/*
 * I don't understand why you asked for the first exercise to be included here, but here it is:
GPCD(a, b)
1.	Input(a > 1, b >1)		// receive two integers that are >1 (1 does not have prime divisors)
2.	min = a, max = b		// assume a is the lower of the numbers
3.	If (b < a)			// if assumption is incorrect
	3.1.	min = b, max = a	// set the max and min accordingly
4.	r = max % min		// take the remainder of the division of the two numbers, when it is 0 min is the GCD
5.	while (r != 0)		// iterate until min is the GCD
	5.1.	max = min		// take the min as the next number to divide
	5.2.	min = r			// take the remainder as the next divisor
	5.3.	r = max % min	// recalculate the remainder
6.	gpcd = min			// our first candidate for GPCD is the last number which did not leave a remainder (the GCD)
7.	i = 1				// the first number for GCD checking, we will never actually try to divide by 1
8.	while (gpcd != i++)		// we will try to divide our candidate as long as we did not reach it, the i++ ordering is important here since we are first checking if gpcd was found in our last loop (or is 1 if it is the first loop) and only then incrementing it for the next try
	8.1.	while (gpcd % i == 0 && gpcd != i)	// iterate while GPCD candidate is divisible by i (which means it is not prime), ideally i would only go through prime numbers, but it doesn’t matter since composite numbers will never divide gpcd, this is because it was already divided by all lower numbers (which include their prime divisors), also checking that gpcd did not already reach i, in which case it is the answer
		8.1.1.	gpcd /= i		// divide our candidate by i, eliminating a prime divisor which is not the greatest
9.	return gpcd			// after exiting the loop gcpd will be equal to i which means it is the only divisor (since we divided it by all lower numbers) and is thus a prime and the GPCD
						// note that we never lost gpcd as a GCD of a and b since we only divided it, and every divisor of a CD of a and b is also a CD of a and b

*/

// package exe1;

import java.util.Scanner;

public class Ex1 {
	static public void main(String[] args) {
		long a, b;									// the two input number, will be populated in a moment
		if (args.length < 2) {						// check if numbers have not been provided as command line arguments
			Scanner sc = new Scanner(System.in);	// initialize a scanner to read from stdin to read numbers
			System.out.print("Enter the first number for max prime GCD: ");		// prompt user for first number
			a = sc.nextLong();													// recieve first number from user input
			System.out.print("Enter the second number for max prime GCD: ");	// prompt user for first number
			b = sc.nextLong();													// recieve second number from user input
		} else {							// if numbers have been provided as command line arguments
			a = Long.parseLong(args[0]);	// parse the first one as a long
			b = Long.parseLong(args[1]);	// parse the second one as a long
		}
		double startTime = System.nanoTime(); // save algorithm starting time for timing purposes
		
		long min = a, max = b;	// assume a is the smaller of the numbers
		if (b < a) {			// if assumption is incorrect
			min = b;			// reverse the numbers
			max = a;			// "
		}
		long r = max % min;	// take the remainder of the division of the two numbers, when it is 0 min is the GCD
		while (r != 0) {	// iterate until min is the GCD
			max = min;		// take the min as the next number to divide
			min = r;		// take the remainder as the next divisor
			r = max % min;	// recalculate the remainder
		}
		long gpcd = min;	// our first candidate for GPCD is the GCD
		long i = 1;			// the first number for GCD checking, we will never actually try to divide by 1
		while (gpcd != i++)	// the purpose of this loop is to take out prime divisors until only one is left, which is by the necessity the GPD of the GCD, we start from 2 and go until GPCD is i, which means we divided by all lower primes which means it is the only remaining prime divisor which means it is the GPCD
			while (gpcd % i == 0 && gpcd != i)			// as long as gpcd has i as a divisor, also make sure gpcd didn't reach i
				gpcd /= i;								// divide it by i, by the end of this loop either gpcd does not have i as a divisor or is the answer
		double endTime = System.nanoTime();	// save algorithm end time for timing purposes
		System.out.println("The GPCD(" + a + ',' + b + ") = " + gpcd); 				// print the result for GPCD
		double runtimeInMicroSeconds = (endTime - startTime) / 1000.0;				// calculate the total runtime
		System.out.println("The runtime took: " + runtimeInMicroSeconds + " micro seconds.");	// print the runtime, you asked us to make it work in the same way as your implementation which is why I am making it exactly the same, I know it messes up the output of the tester
	}
}
