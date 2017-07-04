# Big Commit Analysis #

## Environment of developer ##
- Eclipse Java EE 4.6
- Tomcat Server 9.0
- PostgreSQL
- Java Version 1.8

## We have a ready-to-use VM ##

If you want to test the project without setting it up, just go to the latest section and download the VM.


## Setup ##

You need a PostgreSQL and a Tomcat Server.

### Outline ###

1. Clone repo
2. Import in Eclipse
3. Update configuration
4. Set up a local Tomcat Server Version >= 6 (Eclipse only provides Version <=7) and add it in the Run Configurations in Eclipse
5. Deploy the project to the Tomcat server
6. How to use the tool
6.1 Spring Batch Admin
7. Example repositories
8. Deployment for production environment
9. Ready-to-use VirtualBox VM

## 1. & 2. ##
Trivial
## 3. Update configuration ##
Adapt database parameters in file
`/src/main/resources/application.properties`


	bico.db.url = jdbc:postgresql://localhost:5432/bico?autoReconnect=true
	bico.db.username = bico
	bico.db.password = bico
	bico.db.driverClassName = org.postgresql.Driver


Adapt path for cloning repositories in file
`/src/main/java/org/springframework/batch/admin/sample/repository/GitLoader.java`

	private static String REPOSITORY_PATH = "target/repositories/";

With Eclipse on Windows, this is equivalent to *C:\eclipse\target\repositories\*

## 4. Install Tomcat and Eclipse Configuration ##
Install Tomcat from [https://tomcat.apache.org](https://tomcat.apache.org). Use Version 8 or 9.

In Eclipse, go to Window -> Show View -> Servers . Then in the servers view, right click and add new. It will show a pop up containing many server vendors. Under Apache select Tomcat v8.0 (Depending upon your downloaded server version). And in the run time configuration point it to the Tomcat folder you have downloaded. 

## 5. Deploy test server ##

After you completed Step 4, you may "Run as" the project, choose "Run on Server" and use your newly created Tomcat server. This way, you don't have to generate a .war file and deploy manually.

After deploying, navigate to [http://localhost:8080/bico/](http://localhost:8080/bico/)

## 6. How to use the tool ##

1. Create a new project (Go to `Projects` and click `Create Project`) and use the example repositories from section 7.

2. After creating a project, click in the `Projects` view on `Batch Job` and start the job with `Launch`. This will clone the repository, parse the commits and link the issues.

3. You can check the status of the batch job in the `Batch Admin` where you started it.

4. After successful execution, go back to the BiCo interface and open the project with `Details`. You can now do the analysis with `Analyze` and look for `Possible Big Commits`.
Also you can go to `Metrics` and mine source and change metrics. The SZZ algorithm is also available.

**Important: For metrics don't use "Every 1th commit" in big projects, because this will need hours to calculate.**

### Job Control ###
In each analysis page, there is a dedicated job control button where you can start the analysis batch job and observe when it finishes. For more details about the batch jobs such as pausing, resuming, and stopping, you can use the dedicated Batch Admin UI explained in the following subsection.

### Batch Admin interface ###

If you click on `Batch Admin` in the BiCo interface navigation, you'll get to the Spring Batch Admin interface.

Click on `Jobs` and you see the list of all batch jobs from the BiCo projects.

Click on one job. This is the execution form - click on `Launch` and the job is executed.

The navigation link `Executions` shows current running jobs and their status.

Do not click on `Home` since this version of Spring Batch Admin will redirect to a wrong url.
 
## 7. Example repositories and tips ##

### Apache Flume ###

Amount of commits: ~1'700

URL: https://github.com/apache/flume.git

Type: JIRA

Issue Tracker: https://issues.apache.org/jira/si/jira.issueviews:issue-xml/%s/%s.xml

Branch: trunk

Time measurements:
- batch process without cloning: 3 minutes

### Apache Lucene-Solr ###

Amount of commits: ~26'000

URL: https://github.com/apache/lucene-solr.git

Type: JIRA

Issue Tracker: https://issues.apache.org/jira/si/jira.issueviews:issue-xml/%s/%s.xml

Branch: master

Time measurements:
- no data

### Apache Nutch ###

Amount of commits: ~2'222

URL: https://github.com/apache/nutch.git

Type: JIRA

Issue Tracker: https://issues.apache.org/jira/si/jira.issueviews:issue-xml/%s/%s.xml

Branch: master

Time measurements:
- no data

### Hibernate Search ###

Amount of commits: ~4'800

URL: https://github.com/hibernate/hibernate-search.git

Type: JIRA

Issue Tracker: https://hibernate.atlassian.net/si/jira.issueviews:issue-xml/%s/%s.xml

Branch: master

Time measurements:
- no data

### elasticsearch ###

Amount of commits: ~25'560

URL: https://github.com/elastic/elasticsearch.git

Type: GitHub

Issue Tracker: https://github.com/elastic/elasticsearch 

Branch: master

Time measurements:
- about 1 hour without cloning
- no data

### Apache httpclient ###

Amount of commits: ~2'650 

URL: https://github.com/apache/httpclient.git

Type: JIRA

Issue Tracker: https://issues.apache.org/jira/si/jira.issueviews:issue-xml/%s/%s.xml

Branch: trunk

Time measurements:
-no data

## 8. Deployment for production environment ##
### Generation of .war file with Maven (in project directory): ###

Change the configuration files (see above) according to your environment.

	mvn clean install -X

### Generation of .war file with Eclipse: ###

Project -> Run as -> "Maven build..."
-> Goals: clean install -X
-> JRE: set Java 1.8 JDK
-> Execute

### Deploy the .war file to a Tomcat Server. ###

.war File is located in /target/

Copy .war file to /webapps/ folder of Tomcat.

Tables for Spring batch should get auto-generated. If not, `db_structure.sql` is included in the base git directory.

Tables for the App itself are not auto-generated by default. If you want that they are auto-generated, go to file `WEB-INF/applicationContext.xml` and set `generateDdl` in entityManagerFactory bean to true. Pay attention: On every app start, the tables are re-created.

I recommend to just import the db_structure.sql and don't touch `generateDdl`.

## 9. Ready-to-use VirtualBox VM ##

Download-Link: [download here](https://mega.nz/#!e5hS3D5Z!l_ESF5f-NyA9aXFx_IW-n3gNNAIvzjaM6_m-Bg4eV5o)

The VM does already contain the Apache Kafka repository ready-to-use analyzed for testing.

Import the appliance with Oracle VM VirtualBox Manager [https://www.virtualbox.org/wiki/Downloads](https://www.virtualbox.org/wiki/Downloads)

Start the VM and double-click on "Start BiCo" on the desktop to open the web interface. Direct link is [http://localhost:8080/bico](http://localhost:8080/bico)

**Accounts**

Ubuntu User Account *bico* : bico

postgresql *postgres* : bico

**Details**

Restart services if necessary:

`systemctl restart tomcat`
`systemctl restart postgres`

**Information**
We had to add following line to the config file to make it work. `src/main/resources/application.properties`:
`batch.job.configuration.file.dir=/opt/tmp`