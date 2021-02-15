#!/bin/bash

url="http://localhost:8080/soap/hw"

curl -X POST "$url" \
  -H 'Content-Type: text/xml' \
  -d '<?xml version="1.0"?>
  <e:Envelope
    xmlns:e="http://schemas.xmlsoap.org/soap/envelope/"
    xmlns:cxf="http://soap.app.io/">
    <e:Header/>
    <e:Body>
      <cxf:getUsers />
    </e:Body>
  </e:Envelope>'