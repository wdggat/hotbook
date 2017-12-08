#!/bin/bash 

asin=$1;
type=$2;

if [[ -n $asin ]] && [[ -n $type ]];
then
mysql -uroot -pwdggat -Diread -e "insert into published values ('$asin', '$type');";
else
echo "args not finished, asin: $asin, type: $type";
fi;

