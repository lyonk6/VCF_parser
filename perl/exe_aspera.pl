# 
# 
# $path is the path to the executable: ./ascp
$path = "/home/UNLV2/biology/lyon/.aspera/connect/bin/";
$usersInput = "@ARGV";
$file = "$path$usersInput";
$bandwidth = "100M"; #bandwith is an interger followed by 'M'.

#this is the first part of the command line utility. 
$a = "./ascp -QTr -l $bandwidth -k 1 -i ~/.aspera/connect/etc/asperaweb_id_dsa.putty -W ";
print "$file\n";
# A key required by the ncbi as a password. This key follows the '-W' argument in 
#the command line example provided by the NCBI When the download is initiated. 

open(FILE,$file) or die "Error, the specified file could not be found.\n";
$ticket = <FILE>;
chomp $ticket;
$c = " dbtest\@gap-upload.ncbi.nlm.nih.gov:data/instant/mschiller/27523 /home/UNLV2/biology/lyon/sra/. ";

print "$a $ticket $c\n";
exec "$a $ticket $c\n";
