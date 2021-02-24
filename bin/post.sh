#!/bin/bash

function hwurl() {
  echo "http://localhost:8080/soap/hw"
}

#curl "$url" -u u:p -H 'Content-Type: text/xml'

hello=$(
  cat <<EOF
<soapenv:Envelope
    xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/"
    xmlns:blz="http://cxf.app.io/">
    <soapenv:Header/>
    <soapenv:Body>
      <blz:hello />
    </soapenv:Body>
</soapenv:Envelope>
EOF
)

sayhi=$(
  cat <<EOF
<?xml version="1.0"?>
  <e:Envelope
    xmlns:e="http://schemas.xmlsoap.org/soap/envelope/"
    xmlns:cxf="http://cxf.app.io/">
    <e:Header/>
    <e:Body>
      <cxf:sayHi>
        <arg0>
            Bob
        </arg0>
      </cxf:sayHi>
    </e:Body>
  </e:Envelope>
EOF
)

newuser=$(
  cat <<EOF
<?xml version="1.0"?>
  <e:Envelope
    xmlns:e="http://schemas.xmlsoap.org/soap/envelope/"
    xmlns:cxf="http://cxf.app.io/">
    <e:Header/>
    <e:Body>
      <cxf:sayHiToUser>
        <user>
          <name>
            Alice
          </name>
        </user>
      </cxf:sayHiToUser>
    </e:Body>
  </e:Envelope>
EOF
)

users=$(
  cat <<EOF
<?xml version="1.0"?>
  <e:Envelope
    xmlns:e="http://schemas.xmlsoap.org/soap/envelope/"
    xmlns:cxf="http://cxf.app.io/">
    <e:Header/>
    <e:Body>
      <cxf:getUsers />
    </e:Body>
  </e:Envelope>
EOF
)

case $1 in
  hello|newuser|sayhi|users)
    op=$1 ; shift 1
    ;;
  *) echo "unsupported operation" >&2; exit 1
  ;;
esac

XARGS=()
XARGS+=("-H" "Content-Type: text/xml")

while getopts "vs" opt; do
  case $opt in
    v) XARGS+=("-v") ;;
    s) XARGS+=("-u" "u:p") ;;
    *) ;;
  esac
done
curl --fail "${XARGS[@]}" -d "${!op}" $(hwurl)