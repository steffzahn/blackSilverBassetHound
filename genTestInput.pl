#!/usr/bin/perl --

use strict;
use warnings;


my $testFile = "DUMMY.csv";
my $numberOfEntries = 1000;

if( defined( $ARGV[0] ) && ($ARGV[0] ne "") )
{
    $testFile = $ARGV[0];
}
if( defined( $ARGV[1] ) && ($ARGV[1] =~ /^\d+$/) )
{
    $numberOfEntries = ($ARGV[1]+0);
}

my $firstName;
my $lastName;

my %usedName;

my $vocals = "aeiouy";
my $consonants = "bcdfghklmnpqrstvwxz";

sub makeRandomName()
{
    my $numberOfVocals = 1+int( rand( 4 ) );
    my $s="";
    if( rand(1)<0.3 )
    {
	$s .= substr( $vocals, int( rand( length( $vocals) ) ), 1);
	$numberOfVocals--;
    }
    for(;;)
    {
	my $c = substr( $consonants, int( rand( length( $consonants) ) ), 1);
	if( $c eq "q" )
	{
	    $s .= "qu";
	} else {
	    $s .= $c;
	    if( rand(1)<0.5 )
	    {
		my $c2 = substr( $consonants, int( rand( length( $consonants) ) ), 1);
		if( $c2 eq "q" )
		{
		    $s .= "qu"; 
		} elsif ( ( $c2 ne $s ) 
			  && ( ($c ne "h") || ($c2 ne "h") )
			  && ( ($c ne "v") || ($c2 ne "v") )
			  && ( ($c ne "w") || ($c2 ne "w") )
			  && ( ($c ne "z") || ($c2 ne "z") )
			  && ( ($c ne "j") || ($c2 ne "j") )
			  && ( ($c ne "p") || ($c2 ne "b") )
			  && ( ($c ne "b") || ($c2 ne "p") )
			  && ( ($c ne "v") || ($c2 ne "w") )
			  && ( ($c ne "w") || ($c2 ne "v") )
			  && ( ($c ne "f") || ($c2 ne "w") )
			  && ( ($c ne "w") || ($c2 ne "f") )
			  && ( ($c ne "v") || ($c2 ne "f") )
			  && ( ($c ne "f") || ($c2 ne "v") )
			  && ( ($c ne "g") || ($c2 ne "k") )
			  && ( ($c ne "k") || ($c2 ne "g") )
			  && ( ($c ne "t") || ($c2 ne "d") )
			  && ( ($c ne "x") || ($c2 ne "x") ) ) {
		    $s .= $c2;
		}
	    }
	    last if($numberOfVocals<=0);
	}
	$s .= substr( $vocals, int( rand( length( $vocals) ) ), 1);
	$numberOfVocals--;
	last if( ($numberOfVocals<=0) && (rand(1)<0.3) );
    }
    return uc( substr($s,0,1) ).substr($s,1);
}
sub randomName()
{
    for(my $attempt=0;$attempt<50;$attempt++)
    {
	$firstName = makeRandomName();
	$lastName = makeRandomName();
	my $fullName = $firstName . "." . $lastName;
	if( !exists $usedName{ $fullName } )
	{
	    $usedName{ $fullName }=0;
	    return;
	}
    }
    die "Failed to generate new random name";
}

open( OUT, ">$testFile" ) || die "Failed to create file $testFile.";
my $percent = 0;
for( my $i=0; $i<$numberOfEntries; $i++ )
{
    randomName();
    print OUT "\"$firstName.$lastName\@kfzteile24.de\";\"$firstName\";\"$lastName\"\n";
    if( $i % 10000 == 0 )
    {
	my $currentPercent = int( ($i * 100.0 / $numberOfEntries ) );
	if( $currentPercent ne $percent )
	{
	    print STDERR $currentPercent."% ";
	    $percent = $currentPercent;
	}
    }
}

close(OUT);

exit 0;
