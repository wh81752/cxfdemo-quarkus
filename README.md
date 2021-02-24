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

Problem:
* Schema validation throws unecpected exception

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
```

However, `./bin/hello.sh` throws on server side:
```
13:27:35 WARN  [org.apache.cxf.phase.PhaseInterceptorChain] Interceptor for {http://soap.app.io/}HelloWorldService#{http://soap.app.io/}hello has thrown exception, unwinding now: org.apache.cxf.interceptor.Fault: Marshalling Error: cvc-elt.1.a: Cannot find the declaration of element 'return'.
        at org.apache.cxf.jaxb.JAXBEncoderDecoder.marshall(JAXBEncoderDecoder.java:267)
        at org.apache.cxf.jaxb.io.DataWriterImpl.write(DataWriterImpl.java:240)
        at org.apache.cxf.interceptor.AbstractOutDatabindingInterceptor.writeParts(AbstractOutDatabindingInterceptor.java:137)
        at org.apache.cxf.wsdl.interceptors.BareOutInterceptor.handleMessage(BareOutInterceptor.java:68)
        at org.apache.cxf.phase.PhaseInterceptorChain.doIntercept(PhaseInterceptorChain.java:308)
        at org.apache.cxf.interceptor.OutgoingChainInterceptor.handleMessage(OutgoingChainInterceptor.java:90)
        at org.apache.cxf.phase.PhaseInterceptorChain.doIntercept(PhaseInterceptorChain.java:308)
        at org.apache.cxf.transport.ChainInitiationObserver.onMessage(ChainInitiationObserver.java:121)
        at org.apache.cxf.transport.http.AbstractHTTPDestination.invoke(AbstractHTTPDestination.java:265)
        at org.apache.cxf.transport.servlet.ServletController.invokeDestination(ServletController.java:234)
        at org.apache.cxf.transport.servlet.ServletController.invoke(ServletController.java:208)
        at org.apache.cxf.transport.servlet.ServletController.invoke(ServletController.java:160)
        at io.quarkiverse.cxf.transport.CxfHandler.process(CxfHandler.java:243)
        at io.quarkiverse.cxf.transport.CxfHandler.handle(CxfHandler.java:187)
        at io.quarkiverse.cxf.transport.CxfHandler.handle(CxfHandler.java:39)
        at io.vertx.ext.web.impl.BlockingHandlerDecorator.lambda$handle$0(BlockingHandlerDecorator.java:48)
        at io.vertx.core.impl.ContextImpl.lambda$executeBlocking$2(ContextImpl.java:313)
        at java.base/java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1128)
        at java.base/java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:628)
        at io.netty.util.concurrent.FastThreadLocalRunnable.run(FastThreadLocalRunnable.java:30)
        at java.base/java.lang.Thread.run(Thread.java:834)
Caused by: javax.xml.bind.MarshalException
 - with linked exception:
[org.xml.sax.SAXParseException; lineNumber: 0; columnNumber: 0; cvc-elt.1.a: Cannot find the declaration of element 'return'.]
        at com.sun.xml.bind.v2.runtime.MarshallerImpl.write(MarshallerImpl.java:301)
        at com.sun.xml.bind.v2.runtime.MarshallerImpl.marshal(MarshallerImpl.java:153)
        at org.apache.cxf.jaxb.JAXBEncoderDecoder.writeObject(JAXBEncoderDecoder.java:640)
        at org.apache.cxf.jaxb.JAXBEncoderDecoder.marshall(JAXBEncoderDecoder.java:244)
        ... 20 more
Caused by: org.xml.sax.SAXParseException; lineNumber: 0; columnNumber: 0; cvc-elt.1.a: Cannot find the declaration of element 'return'.
        at java.xml/com.sun.org.apache.xerces.internal.util.ErrorHandlerWrapper.createSAXParseException(ErrorHandlerWrapper.java:204)
        at java.xml/com.sun.org.apache.xerces.internal.util.ErrorHandlerWrapper.error(ErrorHandlerWrapper.java:135)
        at java.xml/com.sun.org.apache.xerces.internal.impl.XMLErrorReporter.reportError(XMLErrorReporter.java:396)
        at java.xml/com.sun.org.apache.xerces.internal.impl.XMLErrorReporter.reportError(XMLErrorReporter.java:327)
        at java.xml/com.sun.org.apache.xerces.internal.impl.XMLErrorReporter.reportError(XMLErrorReporter.java:284)
        at java.xml/com.sun.org.apache.xerces.internal.impl.xs.XMLSchemaValidator.handleStartElement(XMLSchemaValidator.java:2132)
        at java.xml/com.sun.org.apache.xerces.internal.impl.xs.XMLSchemaValidator.startElement(XMLSchemaValidator.java:829)
        at java.xml/com.sun.org.apache.xerces.internal.jaxp.validation.ValidatorHandlerImpl.startElement(ValidatorHandlerImpl.java:570)
        at java.xml/org.xml.sax.helpers.XMLFilterImpl.startElement(XMLFilterImpl.java:551)
        at com.sun.xml.bind.v2.runtime.output.SAXOutput.endStartTag(SAXOutput.java:98)
        at com.sun.xml.bind.v2.runtime.output.ForkXmlOutput.endStartTag(ForkXmlOutput.java:76)
        at com.sun.xml.bind.v2.runtime.XMLSerializer.endAttributes(XMLSerializer.java:277)
        at com.sun.xml.bind.v2.runtime.XMLSerializer.childAsXsiType(XMLSerializer.java:665)
        at com.sun.xml.bind.v2.runtime.ElementBeanInfoImpl$1.serializeBody(ElementBeanInfoImpl.java:125)
        at com.sun.xml.bind.v2.runtime.ElementBeanInfoImpl$1.serializeBody(ElementBeanInfoImpl.java:100)
        at com.sun.xml.bind.v2.runtime.ElementBeanInfoImpl.serializeBody(ElementBeanInfoImpl.java:302)
        at com.sun.xml.bind.v2.runtime.ElementBeanInfoImpl.serializeRoot(ElementBeanInfoImpl.java:309)
        at com.sun.xml.bind.v2.runtime.ElementBeanInfoImpl.serializeRoot(ElementBeanInfoImpl.java:45)
        at com.sun.xml.bind.v2.runtime.XMLSerializer.childAsRoot(XMLSerializer.java:464)
        at com.sun.xml.bind.v2.runtime.MarshallerImpl.write(MarshallerImpl.java:298)
        ... 23 more
```
