#!/bin/sh
envsubst '${USER_SERVICE_URL} ${TASK_SERVICE_URL}' \
  < /etc/nginx/templates/default.conf.template \
  > /etc/nginx/conf.d/default.conf

exec nginx -g 'daemon off;'
