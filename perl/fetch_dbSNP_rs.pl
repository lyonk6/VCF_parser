#
#
# fetch dbsnp by Ken Lyon
# $path is the path to the executable: ./ascp
$bandwidth = "100M"; #bandwith is an interger followed by 'M'.
#this is the first part of the command line utility. 
$a = "./ascp -QTr -l $bandwidth -k 1 -i ~/.aspera/connect/etc/asperaweb_id_dsa.putty ";
$c = " ftp@ftp.ncbi.nih.gov:/snp/organisms/human_9606/rs_fasta/ ";
$d = "/home/UNLV2/biology/lyon/data/rs_fasta/.";
print "$a $c\n";
exec  "$a $c $d";
#$d =  "/home/SCRATCH/lyon/."
#dbtest\@gap-upload
