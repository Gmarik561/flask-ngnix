# Use the official Nginx base image
FROM nginx:latest

# Copy custom Nginx configuration file to the container
COPY nginx.conf /etc/nginx/nginx.conf

# Expose ports
EXPOSE 80

# Define the command to start Nginx when the container starts
CMD ["nginx", "-g", "daemon off;"]
