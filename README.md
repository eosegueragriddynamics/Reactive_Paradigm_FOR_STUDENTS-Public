# Launch of external services for the capstone project

### Prerequisites

Install [Docker](https://docs.docker.com/desktop/mac/install/) and [Docker Compose](https://docs.docker.com/compose/install/).

### Instruction

1. Go to directory with `docker-compose.yml`.
    - `cd {PROJECT_DIR}`
    
2. Build and run apps with Docker Compose.
    - `docker-compose up --build`

4. Call new api and see response and logs
    - `GET: http://localhost:8083/api/orders/user2`

4. Stop **Product Info service** _(for testing)_
    - `docker-compose stop product-info-service`
    
5. Start **Product Info service** _(for testing)_
    - `docker-compose start product-info-service`