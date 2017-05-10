# RESTful-web-service-spark

This is a simple REST server created using the Spark Framework to receive data from a device connected to SIGFOX backend, and
sending them over to a MQTT server

This service only contains GET and POST commands for now.

The server will receive data from the SIGFOX backend in the form of a string. The server will then decode this string and save
them in JSON form, and then posting this JSON string to the MQTT server. The data comprises of light intensity, temperature, sound, air quality, timestamp and device number.
