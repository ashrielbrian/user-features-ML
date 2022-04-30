# Intro

Spring Boot REST API for enabling/disabling users' features access, as part of ML's take home assignment.

# Usage

To setup the REST API on your local machine,

1. Spin up the Postgres database
```bash
    docker run --rm --name feature-switch-postgres -e POSTGRES_PASSWORD=supersecret -d -p 5432:5432 postgres:alpine
    docker exec feature-switch-postgres psql -U postgres -c "CREATE DATABASE featuresdb"
```

2. Configure the secret in `src/main/resources/application.yml` by setting the `password` to be the same as above, which in this case, is `supersecret`. 

3. Run the application:

```bash
    ./mvnw spring-boot:run
```

4. Abbreviated API Schema:

```yaml
      paths:
        /feature:
          get:
            summary: Returns whether a user has access to a specific feature.
              parameters:
                - in: query
                  name: email
                  required: true
                - in: query
                  name: featureName
                  required: true
            responses:
              '200':
                description: User flag successfully fetched.
                content:
                  application/json:
                    schema:
                      properties:
                        canAccess:
                          type: boolean
              '404':
                description: No such user/feature.
            
          post:
            summary: Enables/disables a certain feature based on user's email and feature names.
            requestBody:
              content:
                application/json:
                  schema:
                    properties:
                      email:
                        type: string
                        required: true
                      featureName:
                        type: string
                        required: true
                      enable:
                        type: boolean
                        required: true
            responses:
              '200':
                description: User flag successfully updated.
              '304':
                description: User flag not modified.
              '400':
                description: Invalid JSON body, missing body parameters.
              '404':
                description: No such user/feature.
```

# Testing

With maven installed,

```bash
    ./mvnw clean test
```