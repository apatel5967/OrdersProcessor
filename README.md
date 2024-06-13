# OrdersProcessor

There are a few different components to this project:

1. The console requests a file for the information, yes or no for threads, the number of orders, the dataset, and the results file.
2. This project essentially takes in detailed list of items that a client has purchased from a file. This code then parses the file for each client. There can be many orders in a single file, so the code also goes through an parses each client's information in the file.
3. This information is then stored it into a hashmap with the item and its price.
4. This summary is stored into a results file, which prints everything out for each client, and then a complete sumary everyday.
5. After all the processing, the time that it has taken for everything is shown in milliseconds.

The main purpose of this project is to show the difference between single thread and multithread processing for very large orders. Orders can be hundreds of clients long, and can take very long if using single threading. However, the use of multithreading has cut the time by many seconds. This is shown in the report as well. 
