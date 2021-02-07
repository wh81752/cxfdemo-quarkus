#!/bin/bash

url="http://localhost:8080/soap/hw"

username=${1-}
case -$1- in
--)
soap='<?xml version="1.0"?>
  <e:Envelope
    xmlns:e="http://schemas.xmlsoap.org/soap/envelope/"
    xmlns:cxf="http://cxf.app.io/">
    <e:Header/>
    <e:Body>
      <cxf:sayHiToUser>
      </cxf:sayHiToUser>
    </e:Body>
  </e:Envelope>'
  ;;
*)
soap=$(echo '<?xml version="1.0"?>
  <e:Envelope
    xmlns:e="http://schemas.xmlsoap.org/soap/envelope/"
    xmlns:cxf="http://cxf.app.io/">
    <e:Header/>
    <e:Body>
      <cxf:sayHiToUser>
        <user>
          <name>
            @@NAME@@
          </name>
        </user>
      </cxf:sayHiToUser>
    </e:Body>
  </e:Envelope>' | sed -e "s/@@NAME@@/$username/g")
  ;;
esac

curl -v -X POST "$url" \
  -H 'Content-Type: text/xml' \
  -d "${soap}"