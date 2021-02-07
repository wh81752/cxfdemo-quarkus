QUARKUS CXF DEMO

Demo example implementing a webservice using CXF and -- for reasons of
comparing -- the same service as REST resource.

Compile and run:
```
$> mvn clean quarkus:dev
```

Then in another shell tab try:
```
$> curl http://localhost:8080/soap/hw?wsdl
<?xml version='1.0' encoding='UTF-8'?><wsdl:definitions> .. </wsdl:definitions>

$ ./bin/hello.sh
<soap:Envelope xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/"><soap:Body><ns1:helloResponse xmlns:ns1="http://soap.app.io/"><return>Hello World</return></ns1:helloResponse></soap:Body></soap:Envelope>

[..]
```

Try the REST service as well:
```
$ curl http://localhost:8080/rest/hw/hello
Hello World
$> curl -X POST http://localhost:8080/rest/hw/hello/user
Hello user
## trying "secured" hello with incorrect passwd (px instead of p)
$ curl -v -u u:px -X POST http://localhost:8080/rest/hw/sechello/user
[..]
* Server auth using Basic with user 'u'
> POST /rest/hw/sechello/user HTTP/1.1
> Host: localhost:8080
> Authorization: Basic dTpweA==
> User-Agent: curl/7.73.0
> Accept: */*
>
* Mark bundle as not supporting multiuse
< HTTP/1.1 401 Unauthorized
[..]
## Same with correct password ..
$ curl -v -u u:p -X POST http://localhost:8080/rest/hw/sechello/user
[..]
]> Authorization: Basic dTpw
> User-Agent: curl/7.73.0
> Accept: */*
>
* Mark bundle as not supporting multiuse
< HTTP/1.1 200 OK
[..]
]***Hello user***
```

Problems:

a) @RolesAllowed does not work on CXF WebService
b) 