HMAC 512 authentication implementation

Compilation:
1.Run maven clean install in root project dir.
    
Launch Server:
1. Run db/db-script.sql for initialize database
2. Enter valid database creditals in src/main/resources/application.properties
3. Create launch configuration:
    a. Select spring boot configuration
    b. Select Main class - by.haidash.server.SpringRestServerApplication
4. Launch created configuration.


Launch Client:
1. Run by.haidash.client.RestApiRunner as java application.
2. Change variables TEST_PORT, TEST_HOST, TEST_CONSUMER_KEY, TEST_CONSUMER_SECRET in ru.instinctools.client.RestApiRunner
