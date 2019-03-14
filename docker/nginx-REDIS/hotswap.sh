#!/bin/bash
STR="    server "
STR+=$1
STR+=":6379;"
STR2="ERROR:Same IP"
while IFS= read -r line; do     case "$line" in *$STR*) echo $STR2 && exit 1;;                                         *) i=0;     esac; done </etc/nginx/nginx.conf
#cat /etc/nginx/nginx.conf
sed -i "s/^    server.*$/    server $1:6379;/" /etc/nginx/nginx.conf
#cat /etc/nginx/nginx.conf
/usr/sbin/nginx -s reload
