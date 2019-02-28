FROM ubuntu

# Check for updates and install nginx
RUN apt-get update
RUN apt-get install -y nano wget dialog net-tools curl vim nginx

# Copy our configuration in place of the default one
RUN rm -v /etc/nginx/nginx.conf
COPY nginx.conf /etc/nginx/
RUN echo "daemon off;" >> /etc/nginx/nginx.conf

# Copy the script that we can use to initialize nginx properly (and give ourselves exec privileges on it)
COPY docker-entrypoint.sh /root/
RUN chmod +x /root/docker-entrypoint.sh

# Invoke this script the moment the container comes up
ENTRYPOINT [ "/root/docker-entrypoint.sh" ]

# Expose default port and start nginx
EXPOSE 8888
CMD ["/usr/sbin/nginx"]
