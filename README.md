# OrdersProcessor

This project essentially takes in detailed list of items that a client has purchased from a file. This code then takes the summary of the all the items, and stores it into a hashmap with the item and its price. At the end, there is a summary that shows the number of each item and the total cost for everything. There can be many orders in a single file, so the code also goe through an parses each client's information to form a summary. This summary is stored into a results file, which prints everything out for each client. Afer all the processing, the time that it has taken for everything is shown in milliseconds.

The main purpose of this project is to show the difference between single thread and multithread processing for very large orders. Orders can be hundreds of clients long, and can take very long if using single threading. However, the use of multithreading has cut the time by many seconds. This is shown in the report as well. 
