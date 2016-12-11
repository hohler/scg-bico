# Big Commit Analysis #

## Environment of developer ##
- Eclipse Java EE 4.6
- Tomcat Server 9.0
- MySQL Server 5.7
- Java Version 1.8

## Setup ##

You need a MySQL and a Tomcat Server.

Additional MySQL configuration variables: see chapter 7.



### Outline ###

1. Clone repo
2. Import in Eclipse
3. Update configuration
4. Set up a local Tomcat Server Version >= 6 (Version <= 7 with Eclipse) and add it in the Run Configurations in Eclipse
5. Deploy the project to the Tomcat server
6. Example repositories
7. MySQL configuration

## 1. & 2. ##
Trivial
## 3. Update configuration ##
Adapt database parameters in file
`/src/main/webapp/WEB-INF/applicationContext.xml`

	<bean id="dataSource" class="org.springframework.jdbc.datasource.DriverManagerDataSource">
		<property name="driverClassName" value="com.mysql.jdbc.Driver" />
		<property name="url" value="jdbc:mysql://localhost:3306/bico?autoReconnect=true" />
		<property name="username" value="bico" />
		<property name="password" value="bico" />
	</bean>

Adapt path for cloning repositories in file
`/src/main/java/org/springframework/batch/admin/sample/repository/GitLoader.java`

`private static String REPOSITORY_PATH = "target/repositories/";`

With Eclipse on Windows, this is equivalent to *C:\eclipse\target\repositories\*

## 4. & 5. ##
After deploying, navigate to [http://localhost:8080/scg-bico/](http://localhost:8080/scg-bico/)

## 6. Example repositories and tips ##

** Apache Flume **

Amount of commits: ~1'700

URL: https://github.com/apache/flume.git

Type: JIRA

Issue Tracker: https://issues.apache.org/jira/si/jira.issueviews:issue-xml/%s/%s.xml

Time measurements:
- batch process without cloning: 3 minutes

** Apache Lucene-Solr **

Amount of commits: ~26'000

URL: https://github.com/apache/lucene-solr.git

Type: JIRA

Issue Tracker: https://issues.apache.org/jira/si/jira.issueviews:issue-xml/%s/%s.xml

Time measurements:
- no data

** Apache Nutch **

Amount of commits: ~2'222

URL: https://github.com/apache/nutch.git

Type: JIRA

Issue Tracker: https://issues.apache.org/jira/si/jira.issueviews:issue-xml/%s/%s.xml

Time measurements:
- no data

** Hibernate Search **

Amount of commits: ~4'800

URL: https://github.com/hibernate/hibernate-search.git

Type: JIRA

Issue Tracker: https://hibernate.atlassian.net/si/jira.issueviews:issue-xml/%s/%s.xml

Time measurements:
- no data

** elasticsearch **

Amount of commits: ~25'560

URL: https://github.com/elastic/elasticsearch.git

Type: GitHub

Issue Tracker: https://github.com/elastic/elasticsearch 

Time measurements:
- about 1 hour without cloning
- no data

** Apache httpclient **

Amount of commits: ~2'650 

URL: https://github.com/apache/httpclient.git

Type: JIRA

Issue Tracker: https://issues.apache.org/jira/si/jira.issueviews:issue-xml/%s/%s.xml

Time measurements:
-no data

## 7. MySQL Configuration ##
MySQL specific configuration: some patches are too big. You have to set the max_allowed_packets higher in your MySQL Server configuration. e.g. `max_allowed_packet=20M`.

Also, `innodb_buffer_pool_size` should be raised to 32M.

Location of configuration file on Windows: `C:\ProgramData\MySQL\MySQL Server 5.7\my.ini`
