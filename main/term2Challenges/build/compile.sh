#!/bin/bash

cd Challenge1A/
ant jar
cd out/
scp -oKexAlgorithms=+diffie-hellman-group1-sha1 Challenge1A.jar root@10.0.1.1:/home/lejos/programs
cd ../../Challenge1B
ant jar
cd out/
scp -oKexAlgorithms=+diffie-hellman-group1-sha1 Challenge1B.jar root@10.0.1.1:/home/lejos/programs
cd ../../Challenge2A
ant jar
cd out/
scp -oKexAlgorithms=+diffie-hellman-group1-sha1 Challenge2A.jar root@10.0.1.1:/home/lejos/programs
cd ../../Challenge2B
ant jar
cd out/
scp -oKexAlgorithms=+diffie-hellman-group1-sha1 Challenge2B.jar root@10.0.1.1:/home/lejos/programs
