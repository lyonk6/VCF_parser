#ftp://ftp.1000genomes.ebi.ac.uk/vol1/ftp/phase1/analysis_results/integrated_call_sets/ 
# 
# $path is the path to the executable: ./ascp
$bandwidth = "100M"; #bandwith is an interger followed by 'M'.
#this is the first part of the command line utility. 
$a = "./ascp -QTr -l $bandwidth -k 1 -i ~/.aspera/connect/etc/asperaweb_id_dsa.putty ";
#$c = " dbtest\@gap-upload.ncbi.nlm.nih.gov:data/instant/mschiller/27523 /home/winnt/SCHILLERLAB/ken.lyon/ ";
$c = " fasp-g1k\@fasp.1000genomes.ebi.ac.uk:/vol1/ftp/phase1/analysis_results/integrated_call_sets/ALL.chrX.integrated_phase1_v3.20101123.snps_indels_svs.genotypes.vcf.gz  ";
$d = "/home/UNLV2/biology/lyon/data/integrated_call_sets/.";
print "$a $c\n";
exec "$a $c $d";

