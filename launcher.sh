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
	echo "Detected ctrl-c. Killing launched processes..."
	kill $(jobs -p)
	exit
}

trap ctrl_c INT

echo "[+] Launching ca"
$vterm -e ./mvn_script.sh ca &

echo "[+] Launching vanet"
$vterm -e ./mvn_script.sh vehicle-network &

echo "[*] Press enter when both are running"
read

echo "[+] Launching rsu"
$vterm -e ./mvn_script.sh rsu &

echo "[*] Press enter when rsu is running"
read

while true; do
	echo "[*] Write arguments to launch vehicle with:"
	read args

	echo "[+] Launching vehicle with: $args"
	$vterm -e ./mvn_script.sh vehicle $args &
done

