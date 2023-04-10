FROM postgres:latest

# Install packages
RUN apt-get update
RUN apt-get install -y make
RUN apt-get install -y git
RUN apt-get install -y perl

# Install and activate pgtap
RUN git clone https://github.com/theory/pgtap.git
WORKDIR pgtap
RUN make
RUN make install
WORKDIR /docker-entrypoint-initdb.d/
RUN touch init.sql
RUN echo "CREATE EXTENSION IF NOT EXISTS pgtap;" > init.sql

CMD ["postgres"]