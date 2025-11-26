FROM python:3.11-slim

# Set working directory
WORKDIR /app

# Copy application files
COPY task_manager.py .
COPY requirements.txt .

# Install dependencies
RUN pip install --no-cache-dir -r requirements.txt || true

# Set environment variables
ENV PYTHONUNBUFFERED=1

# Run the application
CMD ["python3", "task_manager.py"]
