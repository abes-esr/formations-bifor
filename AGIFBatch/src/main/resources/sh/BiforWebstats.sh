cd /home/batch/Bifor/

LANG=fr_FR.UTF-8
CLASSPATH=current/lib/*:current/AGIFBatch.jar:$CLASSPATH

export CONFIG_PATH=/home/batch/Bifor/config.properties

java -cp $CLASSPATH -Djava.security.egd=file:///dev/urandom org.formation.batch.WebStatsExport "$@" > logs/batch_webstats.log 2>&1
