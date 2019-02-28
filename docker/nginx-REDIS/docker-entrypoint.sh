#!/usr/bin/env bash
set -e

if [ -z "$PROXY_PORT" ]; then
    export PROXY_PORT=8888
fi

if [ -z "$REDIS_HOST" ]; then
    export REDIS_HOST=redis
fi

if [ -z "$REDIS_PORT" ]; then
    export REDIS_PORT=6379
elif [[ "$REDIS_PORT" =~ "tcp://" ]]; then
   export REDIS_PORT=6379
fi

sed -i'' "s/%{PROXY_PORT}/${PROXY_PORT}/" /etc/nginx/nginx.conf
sed -i'' "s/%{REDIS_HOST}/${REDIS_HOST}/" /etc/nginx/nginx.conf
sed -i'' "s/%{REDIS_PORT}/${REDIS_PORT}/" /etc/nginx/nginx.conf

exec "$@"