#!/bin/bash

mvn clean package
gcloud --verbosity debug preview app run target/guestbook-1.0-SNAPSHOT
