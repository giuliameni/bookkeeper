

BADUACLI="/Users/giuliamenichini/Documents/bookkeeper-1/dfc/ba-dua-cli-0.2.0-all.jar"
BADUASER="/Users/giuliamenichini/Documents/bookkeeper-1//bookkeeper-server/target/badua.ser"
CLASSES="/Users/giuliamenichini/Documents/bookkeeper-1//bookkeeper-server/target/classes"
BADUAXML="/Users/giuliamenichini/Documents/bookkeeper-1//bookkeeper-server/target/badua.xml"

java -jar ${BADUACLI} report    \
        -input ${BADUASER}      \
        -classes ${CLASSES}     \
        -show-classes           \
        -show-methods           \
        -xml ${BADUAXML}
