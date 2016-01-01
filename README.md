# VCF_parser
This project was created to read VCF files from phase 1 of the 1000 genomes project. It was 
originally developed toward the end of 2012, shortly after the phase 1 call sets were released.
Few revisions have been made to it since. 

This readme details the design and purpose of the VariantRecord class and it's associated classes.
These classes are intented to operate on the integrated_call_sets that were obtained at the url
below. Should any other VCF file callset be used, it is a neer certainty that the AllelicInfo class
will need substantial revision.


Please see the official Variant Call Format (VCF) documentation at the url provided below:

http://www.1000genomes.org/wiki/Analysis/Variant%20Call%20Format/vcf-variant-call-format-version-40

The final variant call sets for the 1000genomes phase1 release can be found at:
ftp.1000genomes.ebi.ac.uk/vol1/ftp/phase1/analysis_results/integrated_call_sets/

A variant record can have an unlimmited number of fields. The first 8 are mandatory but all
those that follow specify genotypes of multiple samples(people). The format field describes
the format of the genotype data. Below is a sample record and header.


#CHROM   POS   ID   REF   ALT   QUAL   FILTER   INFO    FORMAT  <SAMPLE_NAME_1> <SAMPLE_NAME_2>
22      16050408        rs149201999     T       C       100     PASS    <info>  GT:DS:GL        0|0:0.050:-0.03,-1.17,-5.00
<info> = LDAF=0.0649;RSQ=0.8652;AN=2184;ERATE=0.0046;VT=SNP;AA=.;AVGPOST=0.9799;THETA=0.0149;SNPSOURCE=LOWCOV;AC=134;AF=0.06;ASN_AF=0.04;AMR_AF=0.05;AFR_AF=0.10;EUR_AF=0.06

The info field has been extracted to make this easier to read. All of the tags in the INFO and FORMAT
fields (ie LDAF, RSQ, GT, DS etc.) are described in the VCF header which is marked by '#' chars. The
final header line defines the fields of the VCF record and provides a list of sample names.

About the program:
                       AllelicInfo
                           ^
                           |
NieveTableBuilder -> MultisampleVariantRecord -> VariantRecord


  AllelicInfo
      ^
      |
MultisampleVariantRecord -> VariantRecord
        ^
        |
  StatsBuilder
        ^
        |
InfoTableBuilder -> DetailedVariantRecord -> VariantRecord


The VariantRecord class is a simple struct-like object that stores information about a single
variant record from a VCF file. Sucessfully instatiating a VariantRecord object guarentees
the validity of the Chromosome(CHROM), position(POS), reference(REF), variant(ALT), and the
quality score(QUAL) values as per the VCF guidelines with the exception that a NULL value is
not permitted as either a position or quality score by VariantRecord.

The class MultisampleVariantRecord extends VariantRecord and accomodates the existence of multiple
samples as part of a single varaint record. It does this by creating an AllelicInfo object for each
genotype field within a single variant record. So a MultisampleVariantRecord object has an array of
AllelicInfo objects, one for each sample, and a single VariantRecord object which it extends.

