#!/bin/sh

BADUACLI="../dfc/ba-dua-cli-0.8.1-SNAPSHOT-all.jar"
BADUASER="./target/badua.ser"
CLASSES="./target/classes"
BADUAXML="./target/badua.xml"

java -jar ${BADUACLI} report    \
        -input ${BADUASER}      \
        -classes ${CLASSES}     \
        -show-classes           \
        -show-methods           \
        -xml ${BADUAXML}
