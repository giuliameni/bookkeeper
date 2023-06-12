#!/bin/sh

BADUACLI="dfc/ba-dua-cli-0.2.0-all.jar"
BADUASER="bookkeeper-server/coverage.ser"
CLASSES="bookkeeper-server/target/classes"
BADUAXML="bookkeeper-server/target/badua.xml"

java -jar ${BADUACLI} report    \
        -input ${BADUASER}      \
        -classes ${CLASSES}     \
        -show-classes           \
        -show-methods           \
        -xml ${BADUAXML}
