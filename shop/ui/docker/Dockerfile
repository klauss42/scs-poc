FROM nginx:1.11.5-alpine
MAINTAINER Klaus Schroeder <klaus.schroeder@sinnerschrader.com>

RUN rm /etc/nginx/conf.d/*

COPY shop-ui.conf /etc/nginx/conf.d/shop-ui.conf
COPY ./maven/build/. /usr/share/nginx/html/shop-ui/

# service port
EXPOSE 80


