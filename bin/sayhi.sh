#!/bin/bash

url="http://localhost:8080/soap/hw"

text=${1-CXF}
soap=$(echo '<?xml version="1.0"?>
  <e:Envelope
    xmlns:e="http://schemas.xmlsoap.org/soap/envelope/"
    xmlns:cxf="http://cxf.app.io/">
    <e:Header/>
    <e:Body>
      <cxf:sayHiToUser>
        <user>
          <name>
            @@TEXT@@
          </name>
        </user>
      </cxf:sayHiToUser>
    </e:Body>
  </e:Envelope>' | sed -e "s/@@TEXT@@/$text/g")

curl -v -X POST "$url" \
  -H 'Content-Type: text/xml' \
  -d "${soap}"