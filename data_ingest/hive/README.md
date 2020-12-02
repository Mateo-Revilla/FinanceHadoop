
**Step 1. Setup codes:**
```
beeline
!connect jdbc:hive2://babar.es.its.nyu.edu:10000/
use sc6220;
```

**Step 2. put file into hdfs**
```
hdfs dfs -mkdir /user/sc6220/hiveInput
hdfs dfs -mkdir /user/sc6220/hiveInput/amazon
hdfs dfs -mkdir /user/sc6220/hiveInput/walmart
hdfs dfs -mkdir /user/sc6220/hiveInput/covid
hdfs dfs -mkdir /user/sc6220/hiveInput/sp500

hdfs dfs -put amazon.csv hiveInput/amazon
hdfs dfs -put walmart.csv hiveInput/walmart
hdfs dfs -put cases-daily.csv hiveInput/covid
hdfs dfs -put SP500.csv hiveInput/sp500
```
**Step 3. create hive tables**
```
create external table walmart(wdate date, wopen float, whigh float, wlow float, wclose float, wpc float, wpflux float, wvolumn int) row format delimited fields terminated by ',' location '/user/sc6220/hiveInput/walmart';

create external table amazon(adate date, aopen float, ahigh float, alow float, aclose float, apc float, apflux float, avolumn bigint) row format delimited fields terminated by ',' location '/user/sc6220/hiveInput/amazon';

create external table sp(sdate date, sclose float, shigh float, sopen float, svolumn bigint, slow float,  spc float, spflux float) row format delimited fields terminated by ',' location '/user/sc6220/hiveInput/sp500';

create external table covid(cdate date, infected int, death int) row format delimited fields terminated by ',' location '/user/sc6220/hiveInput/covid';
```

*issues:*
* hive seems to only accept date in the format of yyyy-mm-dd
* some of the volumn of stock (amazon and sp500)'s data type needed to be changed from int to bigint, otherwise showing up as NULL

(see step 4 and 5 in ana_code/hive)


**Step 6. Export hive table to hdfs and then local filesystem as csv**
in beeline:
```
insert overwrite directory '/user/sc6220/output/awc' row format delimited fields terminated by ',' lines terminated by "\n" select * from rawc;
insert overwrite directory '/user/sc6220/output/wsc' row format delimited fields terminated by ',' lines terminated by "\n" select * from rwsc;
insert overwrite directory '/user/sc6220/output/asc' row format delimited fields terminated by ',' lines terminated by "\n" select * from rasc;

insert overwrite directory '/user/sc6220/output/ac' row format delimited fields terminated by ',' lines terminated by "\n" select * from ac;
insert overwrite directory '/user/sc6220/output/wc' row format delimited fields terminated by ',' lines terminated by "\n" select * from wc;
insert overwrite directory '/user/sc6220/output/sc' row format delimited fields terminated by ',' lines terminated by "\n" select * from sc;
```
in hadoop (rename file)
```
hadoop fs -mv /user/sc6220/output/awc/000000_0 /user/sc6220/output/awc/awc.csv
hadoop fs -mv /user/sc6220/output/asc/000000_0 /user/sc6220/output/asc/asc.csv
hadoop fs -mv /user/sc6220/output/wsc/000000_0 /user/sc6220/output/wsc/wsc.csv

hadoop fs -mv /user/sc6220/output/ac/000000_0 /user/sc6220/output/ac/ac.csv
hadoop fs -mv /user/sc6220/output/wc/000000_0 /user/sc6220/output/wc/wc.csv
hadoop fs -mv /user/sc6220/output/sc/000000_0 /user/sc6220/output/sc/sc.csv
```

create a new directory in dumbo to store result from hdfs:
```
hdfs dfs -get /user/sc6220/output/awc/awc.csv
hdfs dfs -get /user/sc6220/output/asc/asc.csv
hdfs dfs -get /user/sc6220/output/wsc/wsc.csv
hdfs dfs -get /user/sc6220/output/ac/ac.csv
hdfs dfs -get /user/sc6220/output/wc/wc.csv
hdfs dfs -get /user/sc6220/output/sc/sc.csv
```


**Step 7. Room to explore:**
1. look further into past for more baseline economic performance without covid
2. predict stocks in relation to covid deaths instead of infected cases
3. use more advanced machine learning methods to train model


**Additional Code**

for combining all data needed for analysis into the same csv file
```
create table all as select amazon.*,walmart.*,covid.*,sp.* from amazon join walmart on (amazon.adate = walmart.wdate) join covid on (walmart.wdate = covid.cdate) join sp on (covid.cdate = sp.sdate) order by adate;

insert overwrite directory '/user/sc6220/output/all' row format delimited fields terminated by ',' lines terminated by "\n" select adate,apflux, apc,wpflux,wpc,spflux,spc,infected,death from all;

hadoop fs -mv /user/sc6220/output/all/000000_0 /user/sc6220/output/all/all.csv

hdfs dfs -get /user/sc6220/output/all/all.csv
```
	
