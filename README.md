# DISCO - application discovery service

Simple, HTTP-based, application discovery service that helps you to manage
applications running in your environment.

## Features
- Simple RESTful API for application registration/removal
- Flexible data model with optional information on per-application basis
- Automatic polling of registered applications to check their status
- Asynchronous polling so main server is not blocked for app registration/removal

## TODO
- Plug this repo to Travis or other CI service
- Publish jar of Disco server (Bintray?)
- Document current RESTful API & data model
- Persisting registered application data (in-memory storage for now)  
- Basic UI 
- Java agent for automating Java apps registration/health check/removal
- Support TLS (SSL) for secure communication  

## License
The Disco server is licensed under MIT license. See [license details](LICENSE.md) for more information.

Copyright [Slawomir Puklo](https://github.com/spuklo)