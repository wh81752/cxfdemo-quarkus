QUARKUS CXF DEMO

Diff to master:
a) @RolesAllowed moved from WebService | REST to HelloWorldService, i.e. security
would be handled by second rank.

Result:
o REST: works as expected
o SOAP: 
```java
Caused by: javax.enterprise.context.ContextNotActiveException
at io.quarkus.arc.impl.ClientProxies.getDelegate(ClientProxies.java:40)
at io.quarkus.security.runtime.SecurityIdentityProxy_ClientProxy.arc$delegate(SecurityIdentityProxy_ClientProxy.zig:42)
at io.quarkus.security.runtime.SecurityIdentityProxy_ClientProxy.hasRole(SecurityIdentityProxy_ClientProxy.zig:398)
at io.quarkus.security.runtime.interceptor.check.RolesAllowedCheck.apply(RolesAllowedCheck.java:54)
at io.quarkus.security.runtime.interceptor.SecurityConstrainer.check(SecurityConstrainer.java:27)
at io.quarkus.security.runtime.interceptor.SecurityHandler.handle(SecurityHandler.java:23)
at io.quarkus.security.runtime.interceptor.RolesAllowedInterceptor.intercept(RolesAllowedInterceptor.java:29)
at io.quarkus.security.runtime.interceptor.RolesAllowedInterceptor_Bean.intercept(RolesAllowedInterceptor_Bean.zig:386)
at io.quarkus.arc.impl.InterceptorInvocation.invoke(InterceptorInvocation.java:41)
at io.quarkus.arc.impl.AroundInvokeInvocationContext.perform(AroundInvokeInvocationContext.java:41)
at io.quarkus.arc.impl.InvocationContexts.performAroundInvoke(InvocationContexts.java:32)
at io.app.backend.HelloWorldService_Subclass.hello(HelloWorldService_Subclass.zig:568)
at io.app.ws.HelloWorldImpl.hello(HelloWorldImpl.java:26)
at java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
at java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62)
at java.base/jdk.internal.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)
at java.base/java.lang.reflect.Method.invoke(Method.java:566)
at org.apache.cxf.service.invoker.AbstractInvoker.performInvocation(AbstractInvoker.java:179)
at org.apache.cxf.jaxws.JAXWSMethodInvoker.performInvocation(JAXWSMethodInvoker.java:66)
at org.apache.cxf.service.invoker.AbstractInvoker.invoke(AbstractInvoker.java:96)
```

Demo example implementing a webservice using CXF and -- for reasons of
comparing -- the same service as REST resource.

Compile and run:
```shell
> mvn clean quarkus:dev
```

Then in another shell tab try:
```shell
> curl http://localhost:8080/soap/hw?wsdl
<?xml version='1.0' encoding='UTF-8'?>
<wsdl:definitions>
  ..
</wsdl:definitions>

> ./bin/hello.sh
<soap:Envelope xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/">
  <soap:Body>
    <ns1:helloResponse xmlns:ns1="http://soap.app.io/">
      <return>Hello World</return>
    </ns1:helloResponse>
  </soap:Body>
</soap:Envelope>
[..]
```

Try the REST service as well:
```shell
> curl http://localhost:8080/rest/hw/hello
Hello World
> curl -X POST http://localhost:8080/rest/hw/hello/user
Hello user
## trying "secured" hello with incorrect passwd (px instead of p)
> curl -v -u u:px -X POST http://localhost:8080/rest/hw/sechello/user
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
> curl -v -u u:p -X POST http://localhost:8080/rest/hw/sechello/user
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
