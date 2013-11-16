# Bitcoin for glass

Display latest Bitcoin exchange rate in a "pinnable" timeline card.

### Setup

Install [Maven](http://maven.apache.org/) using [Homebrew](http://brew.sh/)

```sh
$ brew install maven
```

Fetch dependencies and build project

```sh
$ mvn verify
```

You should see a message similar to this one

```
BUILD SUCCESS
 Total time: 10.724s
 Finished at: Thur Jul 04 14:50:06 PST 2013
 Final Memory: 24M/213M
```

### Running dev server

Run the command using the App Engine Maven plugin

```sh
$ mvn appengine:devserver
```

You should see something like this:

```
[INFO] INFO: Module instance default is running at http://localhost:8888/
[INFO] Nov 16, 2013 4:05:35 PM com.google.appengine.tools.development.AbstractModule startup
[INFO] INFO: The admin console is running at http://localhost:8888/_ah/admin
[INFO] Nov 16, 2013 4:05:35 PM com.google.appengine.tools.development.DevAppServerImpl doStart
[INFO] INFO: Dev App Server is now running
```

Visit `http://localhost:8888/` in your browser

**Note:** You can compile the application without restarting the dev server by running:

```sh
$ mvn package
```

The dev server automatically checks for these packages every 5 seconds and reloads the web application.
