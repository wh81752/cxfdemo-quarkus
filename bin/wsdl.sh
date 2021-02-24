#!/bin/bash

url="http://localhost:8080/soap/hw"

curl -v -s -X GET "$url?wsdl"