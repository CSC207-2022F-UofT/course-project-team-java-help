# JavaHelp

## Introduction

A Java based platform where people can connect with the mental health services and providers that are right for them.
On JavaHelp, users are able to search for and filter mental health providers in their area, and are able to
contact or reach out to any they desire. Search recommendations are based on the user's information,
as well as on surveys that the user may fill out.

This application was put together as a final project for CSC207 at the University of Toronto.

## Design

This application uses the CLEAN architecture, and SOLID design principles. Views facing the end user make use
of MVVM patterns implemented through the Android SDK.

### Stack & Structure

The project is structured as three separate Java modules: **Model**, **App**, & **Backend**. Model contains the entities for
the application: `User`s, `Survey`s, `Token`s, etc... As well as any logic relating solely to the entities. For instance,
it contains the logic, interfaces, and implementations for serialization of these entities into
JSON using Jakarta JSON. The frontend and backend modules depend on the model module, and aside from that, there
are no other dependencies between modules. From there on, the frontend and backend are built totally
independently. While greater coupling between these components is possible, we felt CLEAN architecture was
best embodies by retaining the capacity to switch to a different, non-proprietary backend at any time. For instance,
a BAAS such as Firebase, or a low code solution.

The backend module specifies our backend (shocker). Our backend is built as a REST API using AWS Lambda functions on top of
an AWS HTTP API Gateway endpoint. The routes on the gateway map to Lambda functions, which are called with event parameters
derived from the HTTP request made to the endpoint. The backend is connected to DynamoDB, a non-relational, document database
from Amazon. Requests made to the backend can be authenticated using our custom token authentication scheme. 
Connections to our backend are HTTPS but no sensitive information (passwords) is sent over the air either way. 
The backend makes use of the Serverless framework to manage deployment and configuration. Our AWS CloudFormation stack, 
DynamoDB tables, and Lambda functions and handlers are entirely specified through Serverless. This simplifies the deployment 
process, centralizing all configuration, and providing a deploy function in a single command. The RESTAssured library is used
for testing response formatting in the backend, alongside JUnit, which is also used by the rest of the project.

The app module specifies our frontend, which is built using the Android SDK. The frontend makes use of Apache HTTPComponents for
connecting to the backend. The decision to use HTTPComponents rather than the built in Java 11 `HTTPClient`, older Java `HTTPURLConnection`
or Google's `HTTPClient` built into Android, was driven by a desire for simple asynchronous capabilities in our connections
to the backend. Despite their excellent asynchronous capabilities, switching to Java 11 to use the newer `HTTPClient` was not
an option for us. The rest of our project used Java 8, and switching to Java 11 required raising the minimum Android API level.
To avoid this, and to stay consistent with the rest of our code, we decided to remain in Java 8. Of the remaining options,
the built in `HTTPURLConnection` was another obvious loser due to its high complexity for even simple requests. While Google's
`HTTPClient` is well built and often used on Android, in the end we decided HTTPComponents had better asynchronous capabilities.

Gradle is used for all builds involved in the project.

### A Brief Note On AWS Lambda

In the interest of ensuring the terminology in this document is understandable, here is a brief overview of AWS Lambda.

_Lambda_ is a service offered by AWS that will execute _functions_ (snippets of code) after arbitrary _events_. 
Events could be a set interval of time passing, an email coming into a specified inbox, really anything. In the case of our 
application, our AWS Lambda functions were executed in response to HTTP request events to our AWS HTTP API Gateway.

Different functions are triggered by HTTP requests with different HTTP methods, to different routes. Each Lambda function
has an entrypoint called a handler. The handler is the piece of code that the Lambda function will execute. In our project,
these are found in the backend module under `com.javahelp.backend.endpoint`. In Java, there are several options for managing
the input of events into the program, and output of responses. In our configuration, events are input through a `APIGatewayV2ProxyRequestEvent`
we created. This is based on the `APIGatewayProxyRequestEvent` class created by Amazon; except it is compatible with V2 of the
integration between Lambda and AWS API Gateway.

Incoming events take the form of JSONs, and are deserialized into `APIGatewayV2ProxyRequestEvent` objects by the Jackson library, 
which is included in Lambda. This why the `APIGatewayV2ProxyRequestEvent` takes the form of a bean, and has a weird structure.
The same approach is used to serialize response objects, which is why `APIGatewayResponse` has a method called `isIsBase64Encoded`.

Unlike something like an EC2 instance, Lambda is not always running. Each handler instance, will close down after some period of time
if it does not respond to any events. If another event is then received, Lambda must 'cold start' a new handler instance. This
is more time consuming than responding to a regular request, especially when using Java with Lambda (as Lambda must
start up the JVM before doing anything).

### Design Decisions

There are several places where smaller scale design decisions had to be made, this section will address the rationale behind 
some of these.

First, the decision to use AWS Lambda. This was driven by the desire to use on of the simplest, cleanest, and most popular 
modern API patterns: REST.

Despite using a REST API, we used the more basic AWS HTTP API Gateway rather than the REST API Gateway because it was simpler,
and integrated better with the Serverless framework we use to manage deployment.

The MVVM approach was used rather than MVP, MVC, etc... because of Android's built in `ViewModel` classes.

Our handlers make use of an inverted input boundary pattern where rather than the use case implementing its own input boundary
and handler's making use of this interface, instead the handlers implement the use case's input boundary, and pass themselves to
the use case. This way, dependency inversion is maintained, and it has the additional benefit that it is easier to add a parameter,
and requires fewer refactors to do so. The reason for this, is that because the handler simply accepts an input and returns
an output, the conventional output boundary pattern is a bit unwieldy. Having an output boundary would mean modifying
the state of the handler through interface methods as it runs, to return an object based on these modifications. Instead,
the handler can extract all information from the request, package it into the required objects, and pass it through the input
boundary it implements. The use case can then receive this information, without knowing anything about the handler, or the nature
of the handler's implementation of its input boundary, and return a result which the handler will package into a
`APIGatewayResponse`.

Another place where the CLEAN architecture looks a bit funny is the activities. In Android, `Activity` is not just the view class.
It is the view class, AND the outermost class facing the OS. Specifically, it is the only class where a lot of operations relying
on the OS can be performed. Even something so simple as making use of the network interface to make an HTTP request. For this reason,
some functionalities that would traditionally be initiated through the ViewModel must instead be initiated through activities. For instance,
before making an HTTP request, permission from the operating system (& end user) must first be obtained through an activity. Only
if permission is provided, does the activity call a method in the ViewModel initiating the request. Similarly, when storing information
in Android's shared preferences (kind of like application settings), some type of `Context` must be provided. This frequently
comes from an activity.

### Package Structure

The project combines packaging by component and packaging by layer. Use cases are packaged by component, but end user facing, and data access
is packaged separately, in packages for their own layers. For instance, take the login use case. The interactor, and all the interfaces
it requires are located in `com.javahelp.frontend.domain.user.login`. However, the implementations of the data access interfaces for this
use case can be found in `com.javahelp.frontend.gateway`. The views, on the other hand, can be found in `com.javahelp.frontend.activity`.

## Deployment & QA

### Testing

As already mentioned, JUnit 4 and RESTAssured (only for the backend) are used for testing. The JUnit tests for the backend are
the most extensive, and probably the most complex. For components involving the database, the backend verifies that the
current process is authenticated with DynamoDB, before attempting to run the test. It does this through JUnit 4's `assertTrue`.
This is necessary, as without process authentication, attempts to read from, or modify the database would fail. In cases such
as these, since the expected behaviour is that the attempts to access the database would fail, these tests are ignored by JUnit.

If you wish to run these tests and require credentials, reach out to me at [jacob.klimczak@gmail.com](jacob.klimczak@gmail.com),
and I can provide credentials, and instructions for using them.

RESTAssured was used as it provided a simple, functional interface for quickly parsing JSON responses, asserting expected values,
and verifying other response properties.

### Deployment

The Serverless framework is responsible for the bulk of the deployment and configuration of our project. It simplifies the creation
of all sorts of AWS resources, and allows for their management from a single YAML file. Deploying with serverless is also
much easier compared to using the AWS-CLI. It simply requires running the `serverless deploy` command, and it will
deploy automatically using the YAML file for the project. In contrast, AWS requires you build a function package, and then
specifically point AWS SAM at the package before uploading it.

One of the major concerns with our deployments, particularly of the backend, was keeping the size of our function package low.
This is necessary because AWS imposes a maximum size (250MB uncompressed, 50MB compressed) for function packages that can be 
uploaded as archives. The alternative is to use a Docker image for your Lambda handler. While the ability to make use of large
libraries would have been nice, the time it takes for Lambda to cold start a handler instance inside a Docker container is much
greater than from an archive. This would have compounded with the already increased Lambda cold start time, due to needing
to launch the JVM on every cold start. For this reason, we felt the archive function package was most desirable.

### CI/CD & Github Actions

We used Github actions for our CI/CD pipelines. Every push triggered a Gradle build followed by running all the tests.
Whenever a pull request is merged into main, Github actions uses the Serverless framework to redeploy the backend (provided all tests pass).

## The Project

The connected Github project is used to manage tasks for this repository. Issues should be assigned a module and milestone,
and then named _\[Module | Milestone\] Issue Name_. Pull requests must be reviewed by three contributors besides the last person
to push the branch, and must also pass all tests before merging. Branches should also be rebased before merges,
and commits should be squashed when merging into main.

## Building, Running, & Testing

### Building

The project can be built with Gradle using (all gradle commands should be executed from the root directory of the project)

```shell
./gradlew build
```

or alternatively on Windows

```powershell
./gradlew.bat build
```

To build an unsigned APK (Android application file) run 

```shell
./gradlew assembleRelease
```

or

```shell
./gradlew assembleDebug
```

(or their Windows equivalents with `./gradlew.bat`)

This APK can then be installed as a test application (this is very important, it will not install normally) 
using [ADB](https://developer.android.com/studio/command-line/adb). To avoid assembling your own unsigned APK, 
a signed one can be downloaded [here](https://drive.google.com/file/d/1y_KieN6km13-GR2R0SJ6H_HIla6poQyF/view?usp=share_link).
To install this APK, enable install from unknown sources in your devices setting for the app you wish to open
the APK with. Then, open the APK and install it.

### Running

The build does not produce any executable jar files. The handlers in the backend are meant to be invoked by Lambda, and will not
work properly, nor execute independently, outside of that context. The frontend specifies an Android app, which similarly,
will not work outside of Android. The model is meant as a non-executable library that both the frontend and backend take
advantage of so that does not execute either. To execute the frontend, install and run the APK as described above.
To execute and test the backend, see the routes section below.

### API Routes

Currently, the API exposes the following routes, with the following functionalities.

GET ["https://gwkvm1k2j5.execute-api.us-east-1.amazonaws.com/users/salt"]("https://gwkvm1k2j5.execute-api.us-east-1.amazonaws.com/users/salt")
    - Gets the salt for a specific user (to be used when hashing a password attempt)
    - Either a 'username' or 'email' query string parameter should be added with a value corresponding to the identity of the user to fetch salt for
    - Returns a JSON response with a single 'salt' key
    - The value of the salt key in the response is the base 64 encoded salt for the user

GET [https://gwkvm1k2j5.execute-api.us-east-1.amazonaws.com/users/{userid}/salt](https://gwkvm1k2j5.execute-api.us-east-1.amazonaws.com/users/{userid}/salt)
    - Gets the salt for a specific user (to be used when hashing a password attempt)
    - The ID of the user should be used in place of the userid path parameter in the URL
    - Returns an identical response to the route above

POST [https://gwkvm1k2j5.execute-api.us-east-1.amazonaws.com/login](https://gwkvm1k2j5.execute-api.us-east-1.amazonaws.com/login)
    - This logs in as a specified user and returns a token for that user
    - Expects request bodies to be JSONs containing: 
        - A 'stayLoggedIn' boolean key indicating whether the token issued should be valid long term, or only in the short term 
        - A 'saltHash' key containing a base 64 encoded byte array that has the first 4 bytes taken up by the
        32-bit integer length of the salt in bytes. The next bytes (the number the first 4 bytes specifies), are the salt. After the salt, the
        remaining bytes are the hash
        - Either a 'username', 'id', or 'email' key with a corresponding identifying value

### Testing

Tests can be run with 

```shell
./gradlew test
```

(or the Windows equivalent with `gradlew.bat`)

All tests in this project involving the database should use `assumeTrue` to verify the database is accessible
before beginning to execute. If it is not, the test will terminate immediately. Furthermore, any tests that create resources
in the database, should clean up these resources in a finally block. This way, even if the test fails, the resources will
still be cleaned up, and will not be left in the database (potentially to mess with future runs).
