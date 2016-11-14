#!/bin/bash

# echo function $1 = type	(type 1 = err | 2 = suc | 3 = inf)
#				$2 = operation
#				$3 = target
#				$4 = offset
# if type given is 1 pretty_echo exits stops the script
pretty_echo () {
	[[ $1 = 1 ]] && dash_size=`expr $(echo $3 | wc -c) + 10` || dash_size=`echo $3 | wc -c`
	echo -e "\n\t\033[0;3${1}m$(printf "%${4}s%${dash_size}s"|tr " " "=")\n\t$([[ $1 = 1 ]] && echo "FAILED TO ${2^^}" || echo ${2^^}): $3\n\t$(printf "%${4}s%${dash_size}s"|tr " " "=")\033[0m\n"
	[[ $1 = 1 ]] && exit 1
}

# Custom dirs
dirs="resources remote-interfaces ca vehicle vehicle-network rsu"
# dirs=$(find . -maxdepth 1 -type d -not -name '\.*' -not -name 'target')

[[ $# -eq 0 || "$1" = "help" || "$1" = "-help" || "$1" = "-h" ]] && echo -e "Usage ./mvn_script  [ compile | install | <maven arguments> ]  <directory> \n
	- For \033[4;29minstall\033[0m, \033[4;29mcompile\033[0m and \033[4;29mclean\033[0m in case no more arguments are given
		all directories ($dirs) will run given command
	- If only the directory is given it runs \033[4;29mmvn clean compile exec:java\033[0m on it.\n" && exit

echo -e "\n\t-- (╯°□°）╯︵ ┻━┻ --\n"

if [ "$1" = "compile" -o "$1" = "install" -o "$1" = "clean" ] && [ $# -eq 1 ]; then  # Compile or install all or specified source dir
	[[ ! -z "$2" ]] && dirs=$2
	for i in $dirs; do
		pretty_echo 3 $1 $i 8 ;
		cd $i && mvn $1 ;
		[[ $? == 0 ]] && cd .. || pretty_echo 1 $1 $i 8
		pretty_echo 2 $1 $i 8
	done
else		 # specific arguments for specified source dir
	dir="${@: -1}"; length=$(($#-1));
	[[ $# -eq 1 ]] && commands="clean compile exec:java" || commands=${@:1:$length};
	pretty_echo 3 "maven" $dir 6
	cd "$dir" && echo -e "Running: \033[1;35;40mmvn $commands\033[0m on \033[1;34;40m$dir\033[0m\n" && mvn $commands ;
	[[ $? == 0 ]] && cd .. || pretty_echo 1 "run" $dir 4 ;
	pretty_echo 2 "maven" $dir 6
fi

echo -e "\t┬─┬ ノ(゜.゜ノ)   a11 iz d0n3 \n"
