# Start from a base image that has Java 21
FROM selenium/standalone-chrome:4.27.0

# Set working directory inside container
WORKDIR /app

# Copy your project files into container
COPY . .

# Switch to root user to install dependencies
USER root

# Update package lists and install any necessary dependencies
RUN apt-get update

# Install Maven (if not already included in base image)
RUN apt-get install -y maven

# Run Maven tests when container starts
CMD ["mvn", "test"]