#!/bin/bash

# Cambia al directorio donde está el script
cd "$(dirname "$0")"

# Ejecuta el programa Java con el classpath adecuado
java -cp 'bin:lib/*' org.swb.Executor processor.properties main
