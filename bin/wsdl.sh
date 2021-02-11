#!/bin/bash

url="http://localhost:8080/soap/hw"

curl -s -X GET "$url?wsdl"