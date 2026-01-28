#!/bin/bash

echo "Waiting for Couchbase server..."

until curl -s http://couchbase:8091 > /dev/null; do
  sleep 3
done

echo "Initializing Couchbase cluster..."

couchbase-cli cluster-init \
 --cluster couchbase \
 --cluster-username Administrator \
 --cluster-password password \
 --services data,index,query || true

echo "Creating bucket courier_bucket..."

couchbase-cli bucket-create \
 --cluster couchbase \
 --username Administrator \
 --password password \
 --bucket courier_bucket \
 --bucket-type couchbase \
 --bucket-ramsize 256 \
 --bucket-replica 0 \
 --storage-backend couchstore || true

echo "Waiting for bucket to be available..."

until couchbase-cli bucket-list \
 --cluster couchbase \
 --username Administrator \
 --password password | grep courier_bucket > /dev/null; do
  sleep 3
done

echo "Couchbase initialization finished."
