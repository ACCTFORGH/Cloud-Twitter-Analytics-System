<h1>Cloud-based Twitter Data Analytics System</h1>
<p>
<b>The project is divided into multiple tasks/queries and each query is summarized below. The full details can be accessed through the project requirements in the same folder:<b><br><br> 
Query 1: simple frontend establishment and authentication by decipher the encripted/wrapped message with private key. Target Throughput: 15000 rps<br>
Query 2: Text Cleaning and Analysis. This task filters the malformed entries that, for example, has fields missing. In addition, it calculates the sentence sentiment score usign AFINN datasets and does texts filtering. Target Throughput: 5500 rps

<b>Files Explained:<b> <br><br>
<b>Frontend Files: <b><br>
Cloud.java: Undertow server definition and configuration.<br>
Decode: For Q1. Decodes the encripted message.<br>
HBase2: HBase connection and search methods for HBase-based solutions<br>
JDBC4: Java MySQL connection and search methods for MySQL-based solutions<br>
JDBCSelector.java: round robin style load balancer<br><br>

<b>ETL Codes<b><br>
JsonFilter.java: For Q2. This script cleans the origin 1TB of Twitter API data and filters out malformed entries<br>
batch process.py: automated script for running the filtering over the large datasets<br>
SimpleTweet.java, Tweet.java: filtering assistance objects. <br>
StringToDataStructure.java: converts JSON strings to Java objects for filtering purpose<br>
SentiAndCensor.java: For Q2. defines methods for calculating Twitter sentiment score and censoring banned words (mostly vulgarities)<br>
adduser.sql: SQL commands for adding users<br>
load.sql and loadall.sql: SQL commands for loading filtered output into MySQL database<br>
loadbase.py: python file for loading filtered output into HBase<br><br>



</p>
