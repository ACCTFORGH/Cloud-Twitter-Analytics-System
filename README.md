<h1>Cloud-based Twitter Data Analytics System</h1>
<p>
<b>The project is divided into two tasks/queries and each query is summarized below. The full details can be accessed through the project requirements file 15-319_619 Cloud Computing.html:</b><br><br> 
Query 1: simple frontend establishment and authentication by deciphering the encripted/wrapped message with private key. Target Throughput: 15000 rps<br>
Query 2: Text Cleaning and Analysis. This task filters the malformed entries that, for example, has fields missing. In addition, it calculates the sentence sentiment score usign AFINN datasets and does texts filtering. Target Throughput: 5500 rps<br>
Budget constraint including system and data processing costs: 40USD <br><br>

<b>Files Explained:</b> <br><br>
<b>Frontend Files: </b><br>
Cloud.java: Undertow server definition and configuration.<br>
Decode: For Q1. Decodes the encripted message.<br>
HBase2: HBase connection and search methods for HBase-based solutions<br>
JDBC4: Java MySQL connection and search methods for MySQL-based solutions<br>
JDBCSelector.java: round robin style load balancer<br><br>

<b>Backend:</b><br>
The backend of the system is is based on MySQL and HBase respectively in Amazon Web Services (AWS). Both of them are loaded with the same data to compare and analyze the performance of SQL and NoSQL databases. HBase is configured from AWS EMR(Elastic MapReduce) clusters while MySQL is installed directly into AWS EC2 instances. Both of the two solutions should be configured and optimized to have stable support for heavy workloads. This is done through system cache and memory optimization, duplicating MySQL nodes,multi-threading/connection pool, etc. The scripts used for data loading is included in the repo. <br>
1. load.sql and loadall.sql: SQL commands for loading filtered output into MySQL database<br>
2. loadbase.py: python file for loading filtered output into HBase. HDFS bulk load commands are also used but not shown in the repo. <br><br> 

<b>Query 2 ETL Codes</b><br>
1. JsonFilter.java: This script cleans the original 1TB of Twitter API data and filters out malformed entries<br>
2. batch process.py: automated script for running the filtering over the large datasets<br>
3. SimpleTweet.java, Tweet.java: simplifying data objects for filtering assistance. <br>
4. StringToDataStructure.java: converts JSON strings to Java objects for filtering purpose<br>
5. SentiAndCensor.java: Defines methods for calculating Twitter sentiment score and censoring banned words (mostly vulgarities)<br>

</p>
