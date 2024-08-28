#!/bin/sh
set -e
echo "Starting SSH..."
/usr/sbin/sshd -D &
echo "..."
java -cp app:app/lib/* com.dockercurso.demo.DemoApplication