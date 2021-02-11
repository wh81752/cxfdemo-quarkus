#!/bin/bash

url="http://localhost:8080/soap/hw"

curl -X POST "$url" \
  -H 'Content-Type: text/xml' \
  -d '
  <soapenv:Envelope
    xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/"
    xmlns:blz="http://soap.app.io/">
    <soapenv:Header/>
    <soapenv:Body>
      <blz:hello />
    </soapenv:Body>
  </soapenv:Envelope>'