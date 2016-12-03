#!/bin/sh

# get terminal emulator application
if hash konsole 2>/dev/null ; then
	vterm=konsole
elif hash gnome-terminal 2>/dev/null ; then
	vterm=gnome-terminal
elif hash iterm2 2>/dev/null ; then
	vterm=iterm2
else
	vterm=xterm
fi

function ctrl_c() {
	echo -e "\nDetected ctrl-c. Killing launched processes..."
	kill $(jobs -p)
	exit
}

trap ctrl_c INT

echo -e "[+] Launching CA"
$vterm -e "./mvn_script.sh ca" >/dev/null 2>&1 &

echo "[+] Launching VANET"
$vterm -e "./mvn_script.sh vehicle-network" >/dev/null 2>&1 &

if [ "$1" == "auto" ] ; then
	echo "[*] Waiting 8 seconds to launch RSU"
	sleep 8
else
	echo "[*] Press enter when both are running to launch RSU"
	read
fi

echo "[+] Launching RSU"
$vterm -e "./mvn_script.sh rsu" >/dev/null 2>&1 &

if [ "$1" == "auto" ] ; then
	echo "[*] Waiting 8 seconds to launch vehicles"
	sleep 8
else
	echo "[*] Press enter when rsu is running"
	read
fi

while true; do
	echo -n "[*] Write arguments to launch vehicle with: "
	read args

	echo "[+] Launching vehicle with: $args"
	$vterm -e "./mvn_script.sh vehicle $args" >/dev/null 2>&1 &
done

