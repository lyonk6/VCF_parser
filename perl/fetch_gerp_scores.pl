# 
# $path is the path to the executable: ./ascp
$bandwidth = "100M"; #bandwith is an interger followed by 'M'.
#this is the first part of the command line utility. 
$a = "./ascp -QTr -l $bandwidth -k 1 -i ~/.aspera/connect/etc/asperaweb_id_dsa.putty ";
$c = " fasp-g1k\@fasp.1000genomes.ebi.ac.uk:/vol1/ftp/phase1/analysis_results/supporting/variant_gerp_scores/  ";
$d = "/home/UNLV2/biology/lyon/data/.";
print "$a $c\n";
exec  "$a $c $d";

