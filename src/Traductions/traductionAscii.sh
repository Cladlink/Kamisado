cd base_traductions
listeFichiers=$(ls *.properties)
for fich in $listeFichiers
do
	native2ascii -encoding UTF-8 $fich ../$fich
done
