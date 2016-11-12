./mvn_script.sh compile remote-interfaces/
./mvn_script.sh install remote-interfaces/

./mvn_script.sh compile ca/
./mvn_script.sh install ca/

./mvn_script.sh compile vehicle/
./mvn_script.sh install vehicle/

./mvn_script.sh compile vehicle-network/
./mvn_script.sh install vehicle-network/