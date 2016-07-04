#!/usr/bin/env sh
trap 'killall' INT

killall() {
    trap '' INT TERM     # ignore INT and TERM while shutting down
    echo "**** Shutting down... ****"     # added double quotes
    kill -TERM 0         # fixed order, send TERM not INT
    wait
    echo DONE
}

hub="java -jar /home/oss/Documentos/CreditMiner/selenium-server-standalone-2.53.1.jar -role hub"
node="java -jar /home/oss/Documentos/CreditMiner/selenium-server-standalone-2.53.1.jar -role node -hub http://localhost:4444/grid/register"
phantom="phantomjs --webdriver=8080 --webdriver-selenium-grid-hub=http://127.0.0.1:4444"
#chrome="java -Dwebdriver.chrome.driver=./chromedriver -jar selenium-server-standalone-2.37.0.jar -role webdriver -hub http://localhost:4444/grid/register -port 5556 -browser browserName=chrome,maxInstances=10"

#opera="java -Dwebdriver.opera.driver=./operadriver-v1.1/operadriver-v1.1.jar -jar selenium-server-standalone-2.37.0.jar -role webdriver -hub http://localhost:4444/grid/register -port 5557 -browser browserName=opera"

${hub} &
echo '3'
sleep 1
echo '2'
sleep 1
echo '1'
sleep 1
echo 'registering drivers'
${node} &
${phantom} &
#${chrome} &
#${opera} &
cat #wait forever…
