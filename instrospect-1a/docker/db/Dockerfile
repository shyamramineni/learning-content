# TODO

# We use Postgres for the database, version 11.6 - it can use a minimal OS.

# There's an init script which populates the reference data that we need to include:
#  - src/db/init-products-db.sh

# Your base image should make it easy to run that script as part of the container startup.


#
# NOTE: THIS DOCKERFILE IS GENERATED VIA "apply-templates.sh"
#
# PLEASE DO NOT EDIT IT DIRECTLY.
#
# Dockerfile
FROM postgres:11.6

RUN mkdir -p /tmp/psql_data/

#COPY db/structure.sql /tmp/psql_data/
COPY init-products-db.sh /docker-entrypoint-initdb.d/

#docker build -t shyam/db .
#docker exec -it 7e0f6125c397 bash
#psql -U postgres
#select * from products


                                                                  