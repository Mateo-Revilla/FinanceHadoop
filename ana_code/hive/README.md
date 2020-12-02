**Objective:**
<p>
This file describe the step-by-step commandline issues to use hive to combine different datasets and calculating indexs that we designed using various fields in various datasets. 
	
We are using datasets from the clean_data_source folder which contains 4 csv files, with daily stock prices from three stocks (Amazon, Walmart, and S&P 500)and daily cases all marked by dates. Previously, we have cleaned up the stock prices using MapReduce (code attatched in the folder DataCleaning/clean) to calculate the percentage change and percentage fluctuation from day to day. The purpose of this project is to combine the tables by dates (discarding the dates that the datasets do not have in common), and look at the relation between covid cases (as independent variables) and change in stock price of each companies (dependent variables) as well as comparing stock price change between companies as a result of covid.

For the comparison between stocks, we are looking at Amazon and Walmart separately using S&P 500 as a baseline for the general economic performance during covid, and we are also comparing Amazon and Walmart to see the differences between how they perform under the influence of covid.
	
(see steps 1 to 3 for inporting data to hive in data_ingest/hive)

**Step 4. join tables:**

(1) awc: amazon + walmart + covid //Amazon and Walmart (separately) contrast to S&P 500 (as baseline) as an effect of covid
```
create table awc as select amazon.*,walmart.*,covid.* from amazon join walmart on (amazon.adate = walmart.wdate) join covid on (walmart.wdate = covid.cdate);
```
(2) wsc: walmart + sp500 + covid //covid's influence on walmart with sp500 as economic backdrop baseline
```
create table wsc as select walmart.*,sp.*,covid.* from walmart join sp on (walmart.wdate = sp.sdate) join covid on (sp.sdate = covid.cdate);
```
(3) asc: amazon + sp500 + covid //covid's influence on amazon with sp500 as economic backdrop baseline
```
create table asc as select amazon.*,sp.*,covid.* from amazon join sp on (amazon.adate = sp.sdate) join covid on (sp.sdate = covid.cdate);
```
*issues:*
* duplicate date field, even though I am doing inner join by date, the date of each table is still a separate field -> solution: add a table prefix to all variables (e.g.: wdate, adate...) as shown in code above
* it took me a while to figure out the aliasing and variable use for joining multiple tables in one go, I decided against aliasing eventually because I wanted to make variables straightforward to understand
*notes:*
* creating table from cvs file is fast on hive but joining table takes a long time
* a lot of covid cases show up as consecutive all 0 towards the beginning of 2020

**Step 5. Create new result tables with calculated independent and dependent variable pairs we are looking at**
Compare difference of percentage stock price change:
	
(1) rawc: {%change amazon - %change walmart, covid infection}
```
create table rawc as select infected, (apc - wpc) as diff_pc from awc;
```
(2) rwsc: {%change walmart - % change sp500, covid infection}
```
create table rwsc as select infected, (wpc - spc) as diff_pc from wsc;
```
(3) rasc: {%change amazon - %change sp500, covid infection}
```
create table rasc as select infected, (apc - spc) as diff_pc from asc;
```

Compare individual stock price change:
(4) ac: {%change amazon, covid infections}
```
create table ac as select infected, apc as pchange from awc;
```
(5) wc: {%change walmart, covid infections}
```
create table wc as select infected, wpc as pchange from awc;
```
(6) sc: {%change s&p500, covid infections}
```
create table sc as select infected, spc as pchange from asc;
```



(see export from hive code from data_ingest/hive)

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

