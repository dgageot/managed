#!/bin/bash

gcloud config set project dga-managed
gcloud preview app deploy --server preview.appengine.google.com target/guestbook-1.0-SNAPSHOT
