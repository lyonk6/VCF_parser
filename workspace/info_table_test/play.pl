$file = "Test";
open(FILE, $file) or die "The file $file could not be found!\n";
$line = <FILE>;
@array = split("\t", $line);
foreach(@array)
{
    print "$_\n";
}
