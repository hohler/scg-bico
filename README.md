# Big Commit Analysis #

## Setup ##

### Outline ###

1. Clone repo
2. Import in Eclipse
3. Make configuration changes
3. Set up a local Tomcat Server Version >= 6 (with Eclipse)
4. Build the project with Maven
5. Deploy the project to the Tomcat server

## 1. Clone repo ##
Trivial
## 2. Import in Eclipse ##
Trivial
## 3. Make configuration changes ##
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