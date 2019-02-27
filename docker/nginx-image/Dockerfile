FROM nginx

# Copy the provided configuration over the default one
COPY nginx.conf /etc/nginx/
RUN echo "daemon off;" >> /etc/nginx/nginx.conf

# Expose port 8888
EXPOSE 8888

# Set the default command to execute when creating a new container
CMD service nginx start