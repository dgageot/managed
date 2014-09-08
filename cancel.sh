#!/bin/bash

gcloud config set project dga-managed
gcloud preview app cancel-deployment --version gbdocker default